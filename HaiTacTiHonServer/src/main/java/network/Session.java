package network;



import java.io.ByteArrayOutputStream;

import java.io.DataOutputStream;

import java.io.File;

import java.io.IOException;

import java.nio.ByteBuffer;

import java.nio.channels.SocketChannel;

import java.sql.Connection;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.sql.SQLException;

import java.sql.Statement;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import java.util.Arrays;

import java.util.Comparator;

import java.util.List;

import java.util.concurrent.BlockingQueue;

import java.util.concurrent.LinkedBlockingQueue;

import java.util.concurrent.TimeUnit;

import java.util.concurrent.locks.ReentrantLock;

import java.util.regex.Pattern;

import itemz.UpgradeItem;

import clan.Clan;

import itemz.Item;

import network.MessageHandler;

import model.Player;

import model.ListNapHangNgay;

import model.ListTichNap;

import model.TichTieuRuby;

import model.Quest;

import model.Wanted_Chest;

import core.Manager;

import core.ServerManager;

import network.Service;

import database.DbManager;

import map.Zone;

import org.json.simple.JSONArray;

import org.json.simple.JSONObject;

import org.json.simple.JSONValue;

import template.*;



public class Session implements Runnable {



    private static final byte[] KEYS = "huathanh@".getBytes();

    public static List<String> IP_LOCK = new ArrayList<>();



    // -- NIO --------------------------------------------------------------------

    private final SocketChannel socketChannel;



    /**

     * readBuffer: chỉ dùng trong receiv-thread (run()), KHÔNG cần lock.

     * 64 KB đủ cho mọi packet hợp lệ (max packet = 5 KB theo code check).

     */

    private final ByteBuffer readBuffer = ByteBuffer.allocateDirect(65536);
    private final ByteBuffer sendBuffer = ByteBuffer.allocateDirect(131072);

    /**
     * writeLock bảo vệ socketChannel.write() vì nhiều thread (sendd, receiv)
     * đều có thể gọi send_msg / sendkeys.
     */
    private final ReentrantLock writeLock = new ReentrantLock();



    // -- Threads -----------------------------------------------------------------

    private Thread sendd;

    public Thread receiv;

    public boolean connected;

    private final BlockingQueue<Message> list_msg;



    // -- Crypto state -------------------------------------------------------------

    private volatile boolean sendKeyComplete;

    private byte curR;

    private byte curW;



    // -- Account / Session info ---------------------------------------------------

    public String user;

    public String pass;

    private final MessageHandler controller;

    public Player p;

    public List<String> list_char;

    public byte zoomlv;

    public String version;

    public int versionInt;

    public byte lock;

    public byte status;

    public int coin;

    public int vip;

    private boolean getImgAPK = false;

    public int tongnap;

    public int tongnap2;

    public int naphangngay;

    public boolean kh;

    public int idUser;

    public int role = -1;

    public int is_admin = 0;

    public String ip;

    public String accCreatedAt = "";

    public long timeOnline = 0L;

    public long loginTime = 0L;



    // -- NIO read-state machine ---------------------------------------------------

    /**

     * -1  : đang chờ đọc CMD byte

     *  0  : đã có CMD, size = 0 (không có data)

     * >0  : đã có CMD, đang đọc data

     */

    private int  expectedSize = -1;

    private byte[] messageData;

    private int  dataPosition;

    private byte lastCmd;



    // ------------------------------------------------------------------------------

    public Session(SocketChannel socketChannel) {

        this.socketChannel = socketChannel;

        try {

            this.ip = socketChannel.socket().getInetAddress().getHostAddress();

        } catch (Exception e) {

            this.ip = "unknown";

        }

        this.list_msg  = new LinkedBlockingQueue<>();

        this.sendKeyComplete = false;

        this.connected = false;

        this.controller = new MessageHandler(this);

    }



    // -- Init ----------------------------------------------------------------------
    public void init() {
        // Send-thread: lấy batch message từ queue và gửi gom cụm (coalesced write)
        this.sendd = new Thread(() -> {
            final List<Message> batch = new ArrayList<>(32);
            try {
                while (connected) {
                    Message m = list_msg.poll(500, TimeUnit.MILLISECONDS);
                    if (m != null) {
                        batch.add(m);
                        list_msg.drainTo(batch, 31);
                        send_batch(batch);
                        batch.clear();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                disconnectSC();
            }
        });
        this.sendd.setDaemon(true);
        this.sendd.setPriority(Thread.MAX_PRIORITY);

        // Receive-thread: run()
        this.receiv = new Thread(this);
        this.receiv.setDaemon(true);

        this.connected = true;
        this.receiv.start();
        this.sendd.start();
    }



    // -- Disconnect ----------------------------------------------------------------

    public void disconnect() {
        this.connected = false;
        disconnectSC();
        list_msg.clear();
        SessionManager.client_disconnect(this);
    }



    public void close() {

        disconnect();

    }



    public void disconnectSC() {

        if (socketChannel != null && socketChannel.isOpen()) {

            try {

                socketChannel.close();

            } catch (Exception ex) {

                System.err.println("closeSocket Err: " + ex);

            }

        }

    }



    // -- Add message to send-queue -------------------------------------------------

    public void addmsg(Message m) throws IOException {
        if (!this.connected || m == null) return;
        if (m.writer() != null) {
            m.writer().flush();
        }
        if (list_msg.size() >= 5000) {
            System.out.println("network.Session.addmsg() too many messages: "
                    + (user == null ? "null" : user) + " " + list_msg.size()
                    + " -> disconnecting");
            list_msg.clear(); // xả queue để tránh memory leak
            disconnect();     // cleanup đầy đủ (gọi client_disconnect)
            return;
        }
        this.list_msg.add(m);
    }
    // -- Receive thread (run) ------------------------------------------------------
    /**
     * Đọc tuần tự từ SocketChannel (blocking mode).
     * Chỉ thread này đọc readBuffer -> không cần lock.
     * Phân tích từng byte bằng state-machine để không bao giờ mất byte.
     */
    @Override
    public void run() {
        try {
            while (this.connected) {
                // Đọc thêm data vào phần còn lại của buffer
                int read = socketChannel.read(readBuffer);
                if (read < 0) {
                    break; // EOF – client ngắt kết nối
                }
                if (read == 0) {
                    // SocketChannel blocking đảm bảo read() chỉ trả 0 nếu buffer full
                    // -> không busy-wait: sleep ngắn rồi thử lại
                    Thread.sleep(1);
                    continue;
                }
                // Xử lý tất cả byte đã đọc được
                readBuffer.flip();
                processBuffer();
                readBuffer.compact(); // giữ lại byte chưa xử lý xong
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            // connection reset / closed – normal
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Đảm bảo socket LUÔN được đóng dù disconnect() có throw
            try { disconnect(); } catch (Exception ignore) {}
            disconnectSC();
        }
    }



    /**

     * State-machine xử lý buffer:

     *   PHASE 1  – đọc CMD (1 byte) + SIZE (2 hoặc 4 byte)

     *   PHASE 2  – đọc DATA (expectedSize byte)

     *   PHASE 3  – xử lý message hoàn chỉnh, reset state

     *

     * Nếu buffer không đủ -> thoát, lần đọc tiếp tục từ điểm đang dở.

     */

    private static final int MAX_PACKET_SIZE = 65535; // Max 2-byte unsigned (0xFFFF)



    private void processBuffer() throws IOException {

        while (readBuffer.hasRemaining()) {



            // -- PHASE 1: đọc CMD + SIZE ------------------------------------------

            if (expectedSize == -1) {



                // Lưu lại vị trí buffer VÀ crypto state TRƯỚC khi đọc bất kỳ byte nào.

                // Nếu buffer chưa đủ cho header, ta restore cả hai về trạng thái ban đầu.

                final int  savedPos  = readBuffer.position();

                final byte savedCurR = this.curR;



                // --- CMD (1 byte) ---

                if (!readBuffer.hasRemaining()) return;

                lastCmd = readBuffer.get();

                if (sendKeyComplete) {

                    lastCmd = readKey(lastCmd); // curR tiến 1

                }



                // --- SIZE field width ---

                final boolean bigSize = (lastCmd == -39 || lastCmd == -101 || lastCmd == -93 || lastCmd == 76);

                final int sizeBytes = bigSize ? 4 : 2;



                // Kiểm tra đủ byte cho SIZE TRƯỚC khi gọi readKey trên SIZE.

                // Nếu không đủ -> rollback buffer pos VÀ curR về trạng thái lưu.

                if (readBuffer.remaining() < sizeBytes) {

                    readBuffer.position(savedPos); // trả lại CMD byte vào buffer

                    this.curR = savedCurR;         // <- FIX: khôi phục curR tránh decrypt lệch

                    return;

                }



                // --- Đọc SIZE ---

                if (sendKeyComplete) {

                    if (bigSize) {

                        expectedSize = (readKey(readBuffer.get()) & 0xFF) << 24

                                     | (readKey(readBuffer.get()) & 0xFF) << 16

                                     | (readKey(readBuffer.get()) & 0xFF) << 8

                                     | (readKey(readBuffer.get()) & 0xFF);

                    } else {

                        expectedSize = (readKey(readBuffer.get()) & 0xFF) << 8

                                     | (readKey(readBuffer.get()) & 0xFF);

                    }

                } else if (bigSize) {

                    expectedSize = readBuffer.getInt();

                } else {

                    expectedSize = readBuffer.getShort() & 0xFFFF;

                }



                // Validate size

                if (expectedSize < 0 || expectedSize > MAX_PACKET_SIZE) {

                    System.out.println("Size Data Msg Too Big cmd: " + lastCmd

                            + " size: " + expectedSize + " user: " + user);

                    disconnect();

                    return;

                }



                messageData  = new byte[expectedSize];

                dataPosition = 0;



                // size == 0: không có data body -> dispatch ngay

                if (expectedSize == 0) {

                    dispatchMessage(lastCmd, messageData);

                    expectedSize = -1;

                    continue;

                }

            }



            // -- PHASE 2: đọc DATA -------------------------------------------------

            int toRead = Math.min(expectedSize - dataPosition, readBuffer.remaining());

            if (toRead > 0) {

                readBuffer.get(messageData, dataPosition, toRead);

                dataPosition += toRead;

            }



            // -- PHASE 3: hoàn chỉnh? ---------------------------------------------

            if (dataPosition == expectedSize) {

                if (sendKeyComplete) {

                    for (int i = 0; i < messageData.length; i++) {

                        messageData[i] = readKey(messageData[i]);

                    }

                }

                dispatchMessage(lastCmd, messageData);

                expectedSize = -1;

            }

        }

    }



    /**

     * Xử lý một message hoàn chỉnh.

     * Vẫn gọi trên receiv-thread -> sendkeys() gọi send_msg() phải dùng writeLock.

     */

    private void dispatchMessage(byte cmd, byte[] data) throws IOException {

        Message msg = new Message(cmd, data);

        try {

            if (cmd == -27) {

                sendkeys(); // gửi key ngay, không qua queue

            } else if (sendKeyComplete) {

                try {

                    controller.process_msg(msg);

                } catch (java.io.EOFException e) {

                    System.err.println("warn processing msg cmd " + cmd + ": truncated packet (length=" + (data != null ? data.length : 0) + ")");

                } catch (Exception e) {

                    e.printStackTrace();

                    System.err.println("err processing msg cmd " + cmd + ": " + e.getMessage());

                }

            }

        } finally {

            try { msg.cleanup(); } catch (Exception ignore) {}

        }

    }



    // -- Send message (thread-safe với writeLock) ----------------------------------

    /**

     * Được gọi từ:

     *  • sendd-thread  (thông qua queue)

     *  • receiv-thread (sendkeys -> send_msg)

     * -> phải synchronized bằng writeLock.

     */

    private void send_msg(Message msg) throws IOException {
        if (msg == null) return;
        List<Message> single = new ArrayList<>(1);
        single.add(msg);
        send_batch(single);
    }

    private void send_batch(List<Message> batch) throws IOException {
        if (batch == null || batch.isEmpty()) return;
        writeLock.lock();
        try {
            sendBuffer.clear();
            for (int k = 0; k < batch.size(); k++) {
                Message msg = batch.get(k);
                if (msg == null) continue;
                byte[] data = msg.getData();
                boolean bigSize = (msg.cmd == -39 || msg.cmd == -101 || msg.cmd == -93 || msg.cmd == 76);
                int dataLen = (data != null) ? data.length : 0;
                int sizeBytes = bigSize ? 4 : 2;
                int totalLen = 1 + sizeBytes + dataLen;

                // Nếu packet lớn hơn dung lượng còn lại của buffer, flush buffer hiện tại trước
                if (sendBuffer.remaining() < totalLen) {
                    if (sendBuffer.position() > 0) {
                        sendBuffer.flip();
                        while (sendBuffer.hasRemaining()) {
                            socketChannel.write(sendBuffer);
                        }
                        sendBuffer.clear();
                    }
                }

                if (totalLen > sendBuffer.capacity()) {
                    // Packet cực lớn (ảnh / big data), cấp phát buffer riêng
                    ByteBuffer largeBuf = ByteBuffer.allocate(totalLen);
                    if (sendKeyComplete) {
                        largeBuf.put(writeKey(msg.cmd));
                        if (bigSize) {
                            largeBuf.put(writeKey((byte) (dataLen >> 24)));
                            largeBuf.put(writeKey((byte) (dataLen >> 16)));
                            largeBuf.put(writeKey((byte) (dataLen >> 8)));
                            largeBuf.put(writeKey((byte) dataLen));
                        } else {
                            largeBuf.put(writeKey((byte) (dataLen >> 8)));
                            largeBuf.put(writeKey((byte) dataLen));
                        }
                        if (data != null) {
                            for (int i = 0; i < dataLen; i++) {
                                largeBuf.put(writeKey(data[i]));
                            }
                        }
                    } else {
                        largeBuf.put(msg.cmd);
                        if (bigSize) {
                            largeBuf.put((byte) (dataLen >> 24));
                            largeBuf.put((byte) (dataLen >> 16));
                            largeBuf.put((byte) (dataLen >> 8));
                            largeBuf.put((byte) dataLen);
                        } else {
                            largeBuf.put((byte) (dataLen >> 8));
                            largeBuf.put((byte) dataLen);
                        }
                        if (data != null) {
                            largeBuf.put(data);
                        }
                    }
                    largeBuf.flip();
                    while (largeBuf.hasRemaining()) {
                        socketChannel.write(largeBuf);
                    }
                    continue;
                }

                // Ghi trực tiếp vào reusable sendBuffer
                if (sendKeyComplete) {
                    sendBuffer.put(writeKey(msg.cmd));
                    if (bigSize) {
                        sendBuffer.put(writeKey((byte) (dataLen >> 24)));
                        sendBuffer.put(writeKey((byte) (dataLen >> 16)));
                        sendBuffer.put(writeKey((byte) (dataLen >> 8)));
                        sendBuffer.put(writeKey((byte) dataLen));
                    } else {
                        sendBuffer.put(writeKey((byte) (dataLen >> 8)));
                        sendBuffer.put(writeKey((byte) dataLen));
                    }
                    if (data != null) {
                        for (int i = 0; i < dataLen; i++) {
                            sendBuffer.put(writeKey(data[i]));
                        }
                    }
                } else {
                    sendBuffer.put(msg.cmd);
                    if (bigSize) {
                        sendBuffer.put((byte) (dataLen >> 24));
                        sendBuffer.put((byte) (dataLen >> 16));
                        sendBuffer.put((byte) (dataLen >> 8));
                        sendBuffer.put((byte) dataLen);
                    } else {
                        sendBuffer.put((byte) (dataLen >> 8));
                        sendBuffer.put((byte) dataLen);
                    }
                    if (data != null) {
                        sendBuffer.put(data);
                    }
                }
            }

            if (sendBuffer.position() > 0) {
                sendBuffer.flip();
                while (sendBuffer.hasRemaining()) {
                    socketChannel.write(sendBuffer);
                }
                sendBuffer.clear();
            }
        } finally {
            writeLock.unlock();
            for (int k = 0; k < batch.size(); k++) {
                Message msg = batch.get(k);
                if (msg != null) {
                    try { msg.cleanup(); } catch (Exception ignore) {}
                }
            }
        }
    }

    // -- Crypto --------------------------------------------------------------------
    private byte readKey(final byte b) {
        final byte cur = this.curR;
        this.curR = (byte) (cur + 1);
        final byte i = (byte) ((KEYS[cur & 0xFF] & 0xFF) ^ (b & 0xFF));
        if (this.curR >= KEYS.length) {
            this.curR = 0;
        }
        return i;
    }

    private byte writeKey(final byte b) {
        final byte cur = this.curW;
        this.curW = (byte) (cur + 1);
        final byte i = (byte) ((KEYS[cur & 0xFF] & 0xFF) ^ (b & 0xFF));
        if (this.curW >= KEYS.length) {
            this.curW = 0;
        }
        return i;
    }

    /**
     * Gửi key cho client (gọi trực tiếp từ receiv-thread, không qua queue).
     * Dùng writeLock để an toàn với sendd-thread.
     */
    public void sendkeys() throws IOException {
        Message msg = new Message(-27);
        msg.writer().writeByte(KEYS.length);
        msg.writer().writeByte(KEYS[0]);
        for (int i = 1; i < KEYS.length; i++) {
            msg.writer().writeByte(KEYS[i] ^ KEYS[i - 1]);
        }
        send_msg(msg); // writeLock bên trong
        try { msg.cleanup(); } catch (Exception ignore) {}
        sendKeyComplete = true;
    }

    // -- request_data_update -------------------------------------------------------
    private static final java.util.concurrent.ConcurrentHashMap<Byte, byte[]> STATIC_DATA_UPDATE_CACHE = new java.util.concurrent.ConcurrentHashMap<>();

    public static void clearStaticDataCache() {
        STATIC_DATA_UPDATE_CACHE.clear();
    }

    public void request_data_update(Message m) throws IOException {
        byte type = m.reader().readByte();
        if (type == 3 && p != null && p.conn != null) {
            p.send_skill();
        } else if (type == 17) {
            Message m2 = new Message(-7);
            m2.writer().writeByte(17);
            m2.writer().writeLong(System.currentTimeMillis());
            this.addmsg(m2);
            m2.cleanup();
        } else {
            byte[] cached = STATIC_DATA_UPDATE_CACHE.get(type);
            if (cached != null && cached.length > 0) {
                Message m2 = new Message(-7);
                m2.writer().write(cached);
                this.addmsg(m2);
                m2.cleanup();
                return;
            }

            Message m2 = new Message(-7);
            switch (type) {
                case 2: {
                    m2.writer().writeByte(2);
                    m2.writer().writeShort(ItemOptionTemplate.ENTRYS.size());
                    for (int i = 0; i < ItemOptionTemplate.ENTRYS.size(); i++) {
                        String optName = ItemOptionTemplate.ENTRYS.get(i).name;
                        m2.writer().writeUTF(optName != null ? optName : "");
                        m2.writer().writeByte(ItemOptionTemplate.ENTRYS.get(i).color);
                        m2.writer().writeByte(ItemOptionTemplate.ENTRYS.get(i).percent);
                    }
                    m2.writer().writeShort(DataTemplate.VerdataAttri);
                    break;
                }
                case 4: {
                    m2.writer().writeByte(4);
                    m2.writer().writeByte(DataTemplate.mLockMap.length);
                    m2.writer().write(DataTemplate.mLockMap);
                    break;
                }
                case 6: {
                    m2.writer().writeByte(6);
                    byte[] ab = getMsg7_6();
                    if (ab == null) {
                        System.out.println("err data type 6");
                        return;
                    }
                    m2.writer().write(ab);
                    m2.writer().writeShort(DataTemplate.VerdataNameMap);
                    break;
                }
                case 7: {
                    m2.writer().writeByte(7);
                    m2.writer().writeShort(DataTemplate.NamePotionquest.length);
                    for (int i = 0; i < DataTemplate.NamePotionquest.length; i++) {
                        m2.writer().writeUTF(DataTemplate.NamePotionquest[i] != null ? DataTemplate.NamePotionquest[i] : "");
                    }
                    m2.writer().writeShort(DataTemplate.VerdataNamePotionquest);
                    break;
                }
                case 8: {
                    m2.writer().writeByte(8);
                    m2.writer().writeShort(DataTemplate.TabInventory_ItemSell[0]);
                    m2.writer().writeShort(DataTemplate.TabInventory_ItemSell[1]);
                    m2.writer().writeShort(DataTemplate.TabInventory_ItemSell[2]);
                    break;
                }
                case 10: {
                    m2.writer().writeByte(10);
                    m2.writer().writeByte(DataTemplate.mMapLang.length);
                    for (int i = 0; i < DataTemplate.mMapLang.length; i++) {
                        m2.writer().writeShort(DataTemplate.mMapLang[i]);
                    }
                    break;
                }
                case 11: {
                    m2.writer().writeByte(11);
                    m2.writer().writeByte(ItemTemplate7.ENTRYS.size());
                    for (int i = 0; i < ItemTemplate7.ENTRYS.size(); i++) {
                        ItemTemplate7 temp = ItemTemplate7.ENTRYS.get(i);
                        m2.writer().writeByte(temp.id);
                        m2.writer().writeUTF(temp.name != null ? temp.name : "");
                        m2.writer().writeByte(temp.type);
                        m2.writer().writeByte(temp.icon);
                        m2.writer().writeInt(temp.price);
                        m2.writer().writeShort(temp.priceruby);
                        m2.writer().writeByte(temp.istrade);
                    }
                    break;
                }
                case 12: {
                    m2.writer().writeByte(12);
                    m2.writer().writeByte(UpgradeItem.DATA.size());
                    for (int i = 0; i < UpgradeItem.DATA.size(); i++) {
                        DataUpgrade temp = UpgradeItem.DATA.get(i);
                        m2.writer().writeByte(temp.level);
                        m2.writer().writeShort(temp.per);
                        m2.writer().writeByte(temp.prelevel);
                        m2.writer().writeInt(temp.beri);
                        m2.writer().writeInt(temp.beri_white);
                        m2.writer().writeShort(temp.ruby);
                        m2.writer().writeShort(temp.att);
                        m2.writer().writeByte(temp.material.length);
                        for (int j = 0; j < temp.material.length; j++) {
                            m2.writer().writeByte(temp.material[j].type);
                            m2.writer().writeByte(temp.material[j].id);
                            m2.writer().writeShort(temp.material[j].quant);
                        }
                    }
                    m2.writer().writeShort(DataTemplate.VerdataUpgradeSave);
                    break;
                }
                case 13: {
                    m2.writer().writeByte(13);
                    m2.writer().writeByte(DataTemplate.mSea.length);
                    for (int i = 0; i < DataTemplate.mSea.length; i++) {
                        for (int j = 0; j < DataTemplate.mSea[i].length; j++) {
                            m2.writer().writeShort(DataTemplate.mSea[i][j]);
                        }
                    }
                    break;
                }
                case 15: {
                    m2.writer().writeByte(15);
                    m2.writer().writeShort(MobTemplate.ENTRYS.size());
                    for (int i = 0; i < MobTemplate.ENTRYS.size(); i++) {
                        MobTemplate temp = MobTemplate.ENTRYS.get(i);
                        m2.writer().writeShort(temp.mob_id);
                        m2.writer().writeUTF(temp.name != null ? temp.name : "");
                        m2.writer().writeShort(temp.level);
                        m2.writer().writeShort(temp.hOne);
                        m2.writer().writeInt(temp.hp_max);
                        m2.writer().writeByte(temp.typemove);
                        m2.writer().writeByte(temp.ishuman);
                        m2.writer().writeByte(temp.typemonster);
                        if (temp.ishuman == 1) {
                            m2.writer().writeShort(temp.head);
                            m2.writer().writeShort(temp.hair);
                            m2.writer().writeByte(temp.wearing.length);
                            for (int j = 0; j < temp.wearing.length; j++) {
                                if (temp.wearing[j] != -1) {
                                    m2.writer().writeByte(1);
                                    m2.writer().writeShort(temp.wearing[j]);
                                } else {
                                    m2.writer().writeByte(-1);
                                }
                            }
                        } else {
                            m2.writer().writeShort(temp.icon);
                        }
                    }
                    m2.writer().writeShort(DataTemplate.VerdataMon);
                    break;
                }
                case 19: {
                    m2.writer().writeByte(19);
                    m2.writer().writeByte(DataTemplate.mTileUpdate.length);
                    for (int i = 0; i < DataTemplate.mTileUpdate.length; i++) {
                        m2.writer().writeShort(DataTemplate.mTileUpdate[i]);
                    }
                    m2.writer().writeByte(DataTemplate.mTileGhepĐa.length);
                    for (int i = 0; i < DataTemplate.mTileGhepĐa.length; i++) {
                        m2.writer().writeShort(DataTemplate.mTileGhepĐa[i]);
                    }
                    break;
                }
                case 21: {
                    m2.writer().writeByte(21);
                    m2.writer().writeByte(0); // h12plus = 0
                    break;
                }
                case 26: {
                    m2.writer().writeByte(26);
                    m2.writer().writeByte(DataTemplate.AttriKichAn.length);
                    for (int i = 0; i < DataTemplate.AttriKichAn.length; i++) {
                        m2.writer().writeUTF(DataTemplate.AttriKichAn[i] != null ? DataTemplate.AttriKichAn[i] : "");
                    }
                    m2.writer().writeShort(-31525);
                    break;
                }
                case 28: {
                    m2.writer().writeByte(28);
                    m2.writer().writeShort(ItemTemplate4.ENTRYS.size());
                    for (int i = 0; i < ItemTemplate4.ENTRYS.size(); i++) {
                        ItemTemplate4 temp = ItemTemplate4.ENTRYS.get(i);
                        m2.writer().writeShort(temp.id);
                        m2.writer().writeShort(temp.icon);
                        m2.writer().writeUTF(temp.name != null ? temp.name : "");
                        m2.writer().writeShort(temp.indexInfoPotion);
                        m2.writer().writeInt(temp.beri);
                        m2.writer().writeShort(temp.ruby);
                        m2.writer().writeByte(temp.istrade);
                        m2.writer().writeByte(temp.type);
                        m2.writer().writeShort(temp.timedelay);
                        m2.writer().writeShort(temp.value);
                        m2.writer().writeShort(temp.timeactive);
                        m2.writer().writeUTF(temp.nameuse != null ? temp.nameuse : "");
                    }
                    m2.writer().writeShort(DataTemplate.VerdataPotion);
                    break;
                }
                case 27: {
                    m2.writer().writeByte(27);
                    m2.writer().writeByte(0); // isopenDao
                    break;
                }
                case 18:
                case 29: {
                    m2.writer().writeByte(18);
                    m2.writer().writeShort(ItemTemplate8.ENTRYS.size());
                    for (int i = 0; i < ItemTemplate8.ENTRYS.size(); i++) {
                        ItemTemplate8 temp = ItemTemplate8.ENTRYS.get(i);
                        m2.writer().writeShort(temp.id);
                        m2.writer().writeShort(temp.icon);
                        m2.writer().writeUTF(temp.name != null ? temp.name : "");
                        m2.writer().writeUTF(temp.info != null ? temp.info : "");
                        m2.writer().writeInt(temp.beri);
                        m2.writer().writeShort(temp.ruby);
                        m2.writer().writeByte(temp.istrade);
                        m2.writer().writeByte(temp.type);
                        m2.writer().writeShort(temp.timedelay);
                        m2.writer().writeShort(temp.value);
                        m2.writer().writeShort(temp.timeactive);
                        m2.writer().writeUTF(temp.nameuse != null ? temp.nameuse : "");
                    }
                    m2.writer().writeShort(DataTemplate.VerdataPotionClan);
                    break;
                }
                case 30: {
                    m2.writer().writeByte(30);
                    m2.writer().writeByte(DataTemplate.mEffSpec.length);
                    for (int i = 0; i < DataTemplate.mEffSpec.length; i++) {
                        m2.writer().writeUTF(DataTemplate.mEffSpec[i] != null ? DataTemplate.mEffSpec[i] : "");
                    }
                    m2.writer().writeShort(-7547);
                    break;
                }
                case 31: {
                    m2.writer().writeByte(31);
                    byte[] ab = getMsg7_31();
                    if (ab == null) {
                        System.out.println("err data type 31");
                        return;
                    }
                    m2.writer().write(ab);
                    break;
                }
            }
            if (m2.writer().size() > 0) {
                byte[] data = m2.getData();
                if (data != null && data.length > 0) {
                    STATIC_DATA_UPDATE_CACHE.put(type, data);
                }
                this.addmsg(m2);
            }
            m2.cleanup();
        }
    }

    // -- DB helpers ----------------------------------------------------------------
    public void update_quatop() {
        try (Connection connection = DbManager.gI().getConnect();
             PreparedStatement ps = connection.prepareStatement("UPDATE `account` SET `quatop` = 1 WHERE BINARY `username` = ? AND BINARY `password` = ? LIMIT 1;")) {
            ps.setString(1, this.user);
            ps.setString(2, this.pass);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update_naphangngay() {
        try (Connection connection = DbManager.gI().getConnect();
             PreparedStatement ps = connection.prepareStatement("UPDATE `account` SET `naphangngay` = 0 WHERE BINARY `username` = ? AND BINARY `password` = ? LIMIT 1;")) {
            ps.setString(1, this.user);
            ps.setString(2, this.pass);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update_tongnap2_zero() {
        this.tongnap2 = 0;
        try (Connection connection = DbManager.gI().getConnect();
             PreparedStatement ps = connection.prepareStatement("UPDATE `account` SET `tongnap2` = 0 WHERE `id` = ? LIMIT 1;")) {
            ps.setInt(1, this.idUser);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getQuaTop() {
        try (Connection connection = DbManager.gI().getConnect();
             PreparedStatement ps = connection.prepareStatement("SELECT `quatop` FROM `account` WHERE BINARY `username` = ? AND BINARY `password` = ? LIMIT 1;")) {
            ps.setString(1, this.user);
            ps.setString(2, this.pass);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt("quatop") : 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // -- send_data_from_server -----------------------------------------------------
    public static class DfsCategory {
        public final String name;
        public final int count;
        public final String[] imgNames;
        public final byte[][] imgDatas;

        public DfsCategory(String name, int count, String[] imgNames, byte[][] imgDatas) {
            this.name = name;
            this.count = count;
            this.imgNames = imgNames;
            this.imgDatas = imgDatas;
        }
    }

    public static class DfsFlatItem {
        public final boolean isMsg;
        public final int cmd;
        public final String path;
        public final String imgName;
        public final byte[] imgData;

        public DfsFlatItem(boolean isMsg, int cmd, String path, String imgName, byte[] imgData) {
            this.isMsg = isMsg;
            this.cmd = cmd;
            this.path = path;
            this.imgName = imgName;
            this.imgData = imgData;
        }
    }

    public static class DfsZoomData {
        public final boolean hasSubFolders;
        public final List<DfsCategory> categories;
        public final List<DfsFlatItem> flatItems;

        public DfsZoomData(boolean hasSubFolders, List<DfsCategory> categories, List<DfsFlatItem> flatItems) {
            this.hasSubFolders = hasSubFolders;
            this.categories = categories;
            this.flatItems = flatItems;
        }
    }

    private static final java.util.concurrent.ConcurrentHashMap<Byte, DfsZoomData> DFS_CACHE = new java.util.concurrent.ConcurrentHashMap<>();

    public static void clearDfsCache() {
        DFS_CACHE.clear();
    }

    private static DfsZoomData loadDfsZoomData(byte safeZoom) {
        File folder = new File("data/datafromserver/x" + safeZoom);
        if (!folder.exists() || !folder.isDirectory()) {
            folder = new File("release/data/datafromserver/x" + safeZoom);
        }
        if (!folder.isDirectory()) {
            return null;
        }

        String[] categories = new String[] {
            "point", "big", "interface", "bg", "eff", "mfont", "efflow", "map", "hinhkhonglo"
        };

        boolean hasSubFolders = false;
        for (String cat : categories) {
            File catDir = new File(folder, cat);
            if (catDir.exists() && catDir.isDirectory()) {
                hasSubFolders = true;
                break;
            }
        }

        if (hasSubFolders) {
            List<DfsCategory> catList = new ArrayList<>();
            for (String cat : categories) {
                File catDir = new File(folder, cat);
                File[] catFiles = (catDir.exists() && catDir.isDirectory())
                        ? catDir.listFiles(f -> f.isFile() && f.getName().endsWith(".png"))
                        : null;
                int count = (catFiles != null) ? catFiles.length : 0;
                List<String> imgNames = new ArrayList<>();
                List<byte[]> imgDatas = new ArrayList<>();
                if (catFiles != null && count > 0) {
                    Arrays.sort(catFiles, Comparator.comparing(File::getName));
                    for (File imgFile : catFiles) {
                        String imgName = cat + "_" + imgFile.getName();
                        try {
                            byte[] imgData = core.ZUtil.loadfile(imgFile.getAbsolutePath());
                            if (imgData != null && imgData.length > 0) {
                                imgNames.add(imgName);
                                imgDatas.add(imgData);
                            }
                        } catch (Exception ignored) {}
                    }
                }
                catList.add(new DfsCategory(cat, count, imgNames.toArray(new String[0]), imgDatas.toArray(new byte[0][])));
            }
            return new DfsZoomData(true, catList, null);
        } else {
            File[] files = folder.listFiles();
            if (files == null) return null;
            Arrays.sort(files, Comparator.comparingInt(f -> {
                String num = "";
                for (int i = 0; i < f.getName().length(); i++) {
                    if (f.getName().charAt(i) == '_') break;
                    num += f.getName().charAt(i);
                }
                try { return Integer.parseInt(num); } catch (NumberFormatException e) { return 0; }
            }));

            List<DfsFlatItem> flatList = new ArrayList<>();
            for (File file : files) {
                if (file.isDirectory()) continue;
                String name = file.getName();
                if (name.contains("_msg_")) {
                    int cmdIdx = name.lastIndexOf('_');
                    if (cmdIdx != -1) {
                        String cmdStr = name.substring(cmdIdx + 1);
                        try {
                            int cmd = Integer.parseInt(cmdStr);
                            flatList.add(new DfsFlatItem(true, cmd, file.getAbsolutePath(), null, null));
                        } catch (NumberFormatException ignored) {}
                    }
                } else if (name.endsWith(".png")) {
                    int firstUnderscore = name.indexOf('_');
                    String imgName = (firstUnderscore != -1) ? name.substring(firstUnderscore + 1) : name;
                    try {
                        byte[] imgData = core.ZUtil.loadfile(file.getAbsolutePath());
                        if (imgData != null && imgData.length > 0) {
                            flatList.add(new DfsFlatItem(false, 0, null, imgName, imgData));
                        }
                    } catch (Exception ignored) {}
                }
            }
            return new DfsZoomData(false, null, flatList);
        }
    }

    public void sendDataClient(Message m) throws IOException {
        this.getImgAPK = false; // Always allow requesting datafromserver
        this.zoomlv = m.reader().readByte();
        byte safeZoom = this.zoomlv;
        if (safeZoom < 1 || safeZoom > 4) safeZoom = 1;
        final byte finalSafeZoom = safeZoom;

        Thread send = new Thread(() -> {
            try {
                DfsZoomData zoomData = DFS_CACHE.computeIfAbsent(finalSafeZoom, Session::loadDfsZoomData);
                if (zoomData == null) return;

                if (zoomData.hasSubFolders) {
                    for (DfsCategory cat : zoomData.categories) {
                        Message msg41 = new Message((byte) -41);
                        msg41.writer().writeUTF(cat.name);
                        msg41.writer().writeShort(cat.count);
                        Session.this.addmsg(msg41);
                        msg41.cleanup();

                        for (int i = 0; i < cat.imgNames.length; i++) {
                            byte[] imgData = cat.imgDatas[i];
                            if (imgData != null && imgData.length > 0) {
                                Message msg39 = new Message((byte) -39);
                                msg39.writer().writeUTF(cat.imgNames[i]);
                                msg39.writer().write(imgData);
                                Session.this.addmsg(msg39);
                                msg39.cleanup();
                            }
                        }
                    }
                    Message msg40 = new Message((byte) -40);
                    Session.this.addmsg(msg40);
                    msg40.cleanup();
                } else if (zoomData.flatItems != null) {
                    for (DfsFlatItem item : zoomData.flatItems) {
                        if (item.isMsg) {
                            Service.send_msg_data(Session.this, item.cmd, item.path, false);
                        } else if (item.imgData != null && item.imgData.length > 0) {
                            Message msg = new Message((byte) -39);
                            msg.writer().writeUTF(item.imgName);
                            msg.writer().write(item.imgData);
                            Session.this.addmsg(msg);
                            msg.cleanup();
                        }
                    }
                }
            } catch (Exception e) {
                // e.printStackTrace();
            }
        });
        send.setDaemon(true);
        send.start();
    }

    // -- login ---------------------------------------------------------------------

    public void login(Message m) throws IOException {

        if (core.Manager.gI().isClosedMode()) {
            login_notice("Máy chủ đang tạm đóng cửa để bảo trì, vui lòng quay lại sau!");
            return;
        }

        if (ServerManager.gI().isBaoTri) {
            login_notice("Máy chủ đang bảo trì nâng cấp, vui lòng quay lại sau ít phút!");
            return;
        }

        byte type = m.reader().readByte();

        String user_ = m.reader().readUTF().replace(" ", "");

        String pass_ = m.reader().readUTF().replace(" ", "");



        long time_can_login_again = 0;

        if (SessionManager.CLIENT_LOGIN_TIME.containsKey(user_)) {

            long time_can_login = SessionManager.CLIENT_LOGIN_TIME.get(user_);

            if (time_can_login > System.currentTimeMillis()) {

                SessionManager.CLIENT_LOGIN_TIME.replace(user_, time_can_login, time_can_login);

                time_can_login_again = (time_can_login - System.currentTimeMillis()) / 1000;

            } else {

                SessionManager.CLIENT_LOGIN_TIME.remove(user_);

            }

        }



        this.lock      = 0;

        this.coin      = 0;

        this.tongnap   = 0;

        this.status    = 0;

        list_char      = new ArrayList<>();



        if (type == 0) {

            Pattern p = Pattern.compile("^[a-zA-Z0-9@.]{1,30}$");

            if (!p.matcher(user_).matches() || !p.matcher(pass_).matches()) {

                login_notice("Tài khoản bị khóa, liên hệ admin để biết thêm chi tiết");

                return;

            }

            SessionManager.time_login.remove(user_);

            SessionManager.CLIENT_LOGIN_TIME.remove(user_);



            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                conn = DbManager.gI().getConnect();
                if (conn == null) {
                    login_notice("Máy chủ đang bận, vui lòng thử lại sau giây lát!");
                    return;
                }
                ps   = conn.prepareStatement("SELECT * FROM `account` WHERE BINARY `username` = ? AND BINARY `password` = ? LIMIT 1;");
                ps.setString(1, user_);
                ps.setString(2, pass_);
                rs   = ps.executeQuery();
                if (!rs.next()) {
                    login_notice("Tài khoản mật khẩu không chính xác");
                    return;
                }
                this.idUser      = rs.getInt("id");
                this.vip         = rs.getInt("vip");
                this.coin        = rs.getInt("coin");
                this.tongnap     = rs.getInt("tongnap");
                try {
                    this.role = rs.getInt("role");
                } catch (Exception ignored) {
                    this.role = -1;
                }
                try {
                    this.is_admin = rs.getInt("is_admin");
                } catch (Exception ignored) {
                    this.is_admin = 0;
                }
                try {
                    this.tongnap2 = rs.getInt("tongnap2");
                } catch (Exception ignored) {
                    this.tongnap2 = 0;
                }
                int calcVip = core.VipManager.getVipByRecharge(this.tongnap);
                if (calcVip > this.vip) {
                    this.vip = calcVip;
                    core.VipManager.updateAccountVipInDb(this.idUser, this.vip, this.tongnap);
                }
                this.status      = rs.getByte("status");
                this.lock        = rs.getByte("lock");
                this.naphangngay = rs.getInt("naphangngay");
                try {
                    this.timeOnline = rs.getLong("time_online");
                } catch (Exception ignored) {
                    this.timeOnline = 0L;
                }
                this.loginTime = System.currentTimeMillis();
                try {
                    String cAt = null;
                    try { cAt = rs.getString("create_at"); } catch (Exception ignored) {}
                    if (cAt == null || cAt.isBlank()) {
                        try { cAt = rs.getString("date"); } catch (Exception ignored) {}
                    }
                    if (cAt == null || cAt.isBlank()) {
                        cAt = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
                        try (PreparedStatement psUpAcc = conn.prepareStatement("UPDATE `account` SET `create_at` = NOW(), `date` = ? WHERE `id` = ?")) {
                            psUpAcc.setString(1, cAt);
                            psUpAcc.setInt(2, this.idUser);
                            psUpAcc.executeUpdate();
                        } catch (Exception ignored) {}
                    }
                    this.accCreatedAt = cAt;
                } catch (Exception e) {
                    this.accCreatedAt = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
                }
                if (this.lock != 0) {
                    login_notice("Tài khoản bị khóa để kiểm tra, liên hệ admin để biết chi tiết, "
                            + "đừng spam tin nhắn kẻo bị t tắt thông báo!");
                    return;
                }
                if (this.ip != null && IP_LOCK.contains(this.ip)) {
                    login_notice("Tài khoản bị chặn!");
                    return;
                }
                list_char.clear();
                try (PreparedStatement psChar = conn.prepareStatement(
                        "SELECT `name` FROM `players` WHERE `account_id` = ? ORDER BY `id` ASC LIMIT 3")) {
                    psChar.setInt(1, this.idUser);
                    try (ResultSet rsChar = psChar.executeQuery()) {
                        while (rsChar.next()) {
                            list_char.add(rsChar.getString("name"));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                login_notice("Có lỗi xảy ra khi đăng nhập, vui lòng thử lại!");
                return;
            } finally {
                closeQuietly(rs, ps, conn);
            }
        } else {
            if (user_.equals("") && pass_.equals("")) {
                login_notice("Tài khoản đang đăng nhập máy khác!");
                return;
            } else {
                Connection conn = null;
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    conn = DbManager.gI().getConnect();
                    if (conn == null) {
                        login_notice("Máy chủ đang bận, vui lòng thử lại sau giây lát!");
                        return;
                    }
                    ps   = conn.prepareStatement("SELECT * FROM `account` WHERE BINARY `username` = ? AND BINARY `password` = ? LIMIT 1;");
                    ps.setString(1, user_);
                    ps.setString(2, pass_);
                    rs   = ps.executeQuery();
                    if (!rs.next()) {
                        login_notice("Tài khoản mật khẩu không chính xác");
                        return;
                    }
                    this.lock   = rs.getByte("lock");
                    this.idUser = rs.getInt("id");
                    try {
                        this.role = rs.getInt("role");
                    } catch (Exception ignored) {
                        this.role = -1;
                    }
                    try {
                        this.is_admin = rs.getInt("is_admin");
                    } catch (Exception ignored) {
                        this.is_admin = 0;
                    }
                    try {
                        this.timeOnline = rs.getLong("time_online");
                    } catch (Exception ignored) {
                        this.timeOnline = 0L;
                    }
                    this.loginTime = System.currentTimeMillis();
                    try {
                        String cAt = null;
                        try { cAt = rs.getString("create_at"); } catch (Exception ignored) {}
                        if (cAt == null || cAt.isBlank()) {
                            try { cAt = rs.getString("date"); } catch (Exception ignored) {}
                        }
                        if (cAt == null || cAt.isBlank()) {
                            cAt = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
                            try (PreparedStatement psUpAcc = conn.prepareStatement("UPDATE `account` SET `create_at` = NOW(), `date` = ? WHERE `id` = ?")) {
                                psUpAcc.setString(1, cAt);
                                psUpAcc.setInt(2, this.idUser);
                                psUpAcc.executeUpdate();
                            } catch (Exception ignored) {}
                        }
                        this.accCreatedAt = cAt;
                    } catch (Exception e) {
                        this.accCreatedAt = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
                    }
                    if (this.lock != 0) {
                        login_notice("Tài khoản đang đăng nhập máy khác!");
                        return;
                    }
                    list_char.clear();
                    try (PreparedStatement psChar = conn.prepareStatement(
                            "SELECT `name` FROM `players` WHERE `account_id` = ? ORDER BY `id` ASC LIMIT 3")) {
                        psChar.setInt(1, this.idUser);
                        try (ResultSet rsChar = psChar.executeQuery()) {
                            while (rsChar.next()) {
                                list_char.add(rsChar.getString("name"));
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    login_notice("Có lỗi xảy ra khi đăng nhập, vui lòng thử lại!");
                    return;
                } finally {
                    closeQuietly(rs, ps, conn);
                }
            }
        }

        zoomlv  = m.reader().readByte();
        version = m.reader().readUTF();
        try {
            String digits = version != null ? version.replaceAll("[^0-9]", "") : "";
            versionInt = digits.isEmpty() ? 129 : Integer.parseInt(digits);
        } catch (Exception e) {
            versionInt = 129;
        }
        m.reader().readByte();
        byte IndexCharSelected = m.reader().readByte();
        this.user = user_;
        this.pass = pass_;

        // --- XÁC THỰC BẢN QUYỀN KEY & CHẶN PHIÊN BẢN / MOD LẬU (GITHUB) ---
        if (KeyServerValidator.isKeyCheckEnabled()) {
            String clientKey = "";
            String clientToken = "";
            try {
                if (m.reader().available() > 0) m.reader().readUTF(); // loginPlus
                if (m.reader().available() > 0) m.reader().readUTF(); // checkmod
                if (m.reader().available() > 0) clientKey = m.reader().readUTF();
                if (m.reader().available() > 0) clientToken = m.reader().readUTF();
            } catch (Exception ignored) {}

            if (!KeyServerValidator.validateClient(this.ip, clientKey, clientToken)) {
                try {
                    login_notice(KeyServerValidator.getLastRejectNotice());
                    Thread.sleep(500);
                } catch (Exception ignored) {}
                disconnect();
                return;
            }
        }

        // O(1) Session map replacement & kick existing session
        Session oldSession = SessionManager.CLIENTS_MAP.put(this.user, this);
        if (oldSession != null && oldSession != this) {
            try { oldSession.disconnect(); } catch (Exception ignore) {}
            try { oldSession.disconnectSC(); } catch (Exception ignore) {}
        }
        SessionManager.time_login.remove(this.user);
        SessionManager.CLIENT_LOGIN_TIME.remove(this.user);

        // Async/direct onl status updates without blocking CLIENT_ENTRYS
        if (this.check_onl()) {
            this.update_onl(0);
        }
        this.update_onl(1);

        for (int i = 0; i < list_char.size(); i++) {
            String cName = list_char.get(i);
            Player p0 = Zone.get_player_by_name_allmap(cName);
            if (p0 != null) {
                p0.isClosed = true;
                if (p0.conn != null && p0.conn != this) {
                    try { p0.conn.disconnect(); } catch (Exception ignore) {}
                }
                if (p0.map != null) {
                    try { p0.map.leave_map(p0, 0); } catch (Exception ignore) {}
                }
                SessionManager.PLAYERS_MAP.remove(p0.IDPlayer);
                SessionManager.PLAYERS_BY_NAME.remove(p0.name);
                SessionManager.PLAYERS_BY_INDEX.remove((int) p0.index_map);
            }
        }



        Service.send_msg_data(this, 72, X2MSG_72_1, false);

        Service.send_msg_data(this, 72, X2MSG_72_2, false);

        Service.send_msg_data(this, 72, X2MSG_72_3, false);



        if (this.zoomlv < 2) {

            Message m22 = new Message(-7);

            m22.writer().writeByte(15);

            m22.writer().writeShort(MobTemplate.ENTRYS.size());

            for (int i = 0; i < MobTemplate.ENTRYS.size(); i++) {

                MobTemplate temp = MobTemplate.ENTRYS.get(i);

                m22.writer().writeShort(temp.mob_id);

                m22.writer().writeUTF(temp.name);

                m22.writer().writeShort(temp.level);

                m22.writer().writeShort(temp.hOne);

                m22.writer().writeInt(temp.hp_max);

                m22.writer().writeByte(temp.typemove);

                m22.writer().writeByte(temp.ishuman);

                m22.writer().writeByte(temp.typemonster);

                if (temp.ishuman == 1) {

                    m22.writer().writeShort(temp.head);

                    m22.writer().writeShort(temp.hair);

                    m22.writer().writeByte(temp.wearing.length);

                    for (int j = 0; j < temp.wearing.length; j++) {

                        if (temp.wearing[j] != -1) {

                            m22.writer().writeByte(1);

                            m22.writer().writeShort(temp.wearing[j]);

                        } else {

                            m22.writer().writeByte(-1);

                        }

                    }

                } else {

                    m22.writer().writeShort(temp.icon);

                }

            }

            m22.writer().writeShort(DataTemplate.VerdataMon);

            this.addmsg(m22);

            m22.cleanup();

        }



        if (this.user.startsWith("htth_vietvan_")) {

            Message m2 = new Message(-57);

            m2.writer().writeUTF(user_);

            addmsg(m2);

            m2.cleanup();

        }



        this.sendd.setName(user);

        this.receiv.setName(user);



        send_list_char();

        Message m2 = new Message(-2);

        addmsg(m2);

        m2.cleanup();



        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement("update `account` set `ip` = ? where `username` = ? limit 1")) {
            ps.setString(1, this.ip);
            ps.setString(2, this.user);
            ps.executeUpdate();
        } catch (SQLException e) {
            // không quan trọng
        }

        lechHead(this);

    }



    // -- login_after_time ----------------------------------------------------------

    private void login_after_time(long l) throws IOException {

        Message m = new Message(-69);

        m.writer().writeUTF("Mời bạn đăng nhập lại sau thời gian");

        m.writer().writeShort((int) l);

        addmsg(m);

        m.cleanup();

    }



    // -- send_list_char ------------------------------------------------------------

    private void send_list_char() throws IOException {

        Message m2 = new Message(-4);

        m2.writer().writeByte(list_char.size());

        for (int i = 0; i < list_char.size(); i++) {

            String name = list_char.get(i);

            Connection connection  = null;

            PreparedStatement ps   = null;

            ResultSet rs           = null;

            try {

                connection = DbManager.gI().getConnect();

                ps = connection.prepareStatement(

                        "SELECT `clazz`, `level`, `body_parts`, `it_body`, `fashion`, `inventory` FROM `players` WHERE `name` = '"

                        + name + "' LIMIT 1;");

                rs = ps.executeQuery();

                while (rs.next()) {
                    List<ItemFashionP2> fashion    = new ArrayList<>();
                    List<ItemFashionP>  itfashionP = new ArrayList<>();
                    
                    String inventoryRaw = rs.getString("inventory");
                    JSONObject invObj = (JSONObject) JSONValue.parse(inventoryRaw == null ? "{}" : inventoryRaw);
                    Object parsedSite = invObj != null ? invObj.get("site") : null;
                    if (parsedSite instanceof String) parsedSite = JSONValue.parse((String) parsedSite);

                    String rawFashion = rs.getString("fashion");
                    Object parsedFashion = JSONValue.parse(rawFashion);

                    if (parsedFashion instanceof JSONObject) {

                        JSONObject fo = (JSONObject) parsedFashion;

                        JSONArray fpArr = (JSONArray) fo.get("fp");

                        if (fpArr != null) {

                            for (Object o : fpArr) {

                                JSONObject eo = (JSONObject) o;

                                itfashionP.add(new ItemFashionP(

                                    jShort(eo, "id", (short)0), jShort(eo, "icon", (short)0),

                                    jByte(eo, "cat", (byte)0), jBool(eo, "use", false)));

                            }

                        }

                        JSONArray f2Arr = (JSONArray) fo.get("f2");

                        if (f2Arr != null) {

                            for (Object o : f2Arr) {

                                JSONObject eo = (JSONObject) o;

                                ItemFashionP2 tempf = new ItemFashionP2();

                                tempf.id     = jShort(eo, "id", (short)0);

                                tempf.is_use = jBool(eo, "use", false);

                                fashion.add(tempf);

                            }

                        }

                    } else if (parsedFashion instanceof JSONArray) {

                        JSONArray js0 = (JSONArray) parsedFashion;

                        if (js0.size() > 0) {

                            JSONArray js_temp_2 = (JSONArray) JSONValue.parse(js0.get(0).toString());

                            for (int i0 = 0; i0 < js_temp_2.size(); i0++) {

                                JSONArray js_temp = (JSONArray) JSONValue.parse(js_temp_2.get(i0).toString());

                                ItemFashionP tempf = new ItemFashionP(

                                        Short.parseShort(js_temp.get(1).toString()),

                                        Short.parseShort(js_temp.get(2).toString()),

                                        Byte.parseByte(js_temp.get(0).toString()),

                                        Byte.parseByte(js_temp.get(3).toString()) == 1);

                                itfashionP.add(tempf);

                            }

                        }

                        if (js0.size() > 1) {

                            JSONArray js_temp_2 = (JSONArray) JSONValue.parse(js0.get(1).toString());

                            for (int i0 = 0; i0 < js_temp_2.size(); i0++) {

                                JSONArray js_temp = (JSONArray) JSONValue.parse(js_temp_2.get(i0).toString());

                                ItemFashionP2 tempf = new ItemFashionP2();

                                tempf.id     = Short.parseShort(js_temp.get(0).toString());

                                tempf.is_use = Byte.parseByte(js_temp.get(1).toString()) == 1;

                                fashion.add(tempf);

                            }

                        }

                    }



                    short hair_ = -999;
                    short head_ = -999;

                    boolean hfHair = false;
                    boolean hfHead = false;
                    boolean is_show_hat = true;
                    if (parsedSite instanceof JSONObject) {
                        hfHair = jBool((JSONObject) parsedSite, "hfhair", false);
                        hfHead = jBool((JSONObject) parsedSite, "hfhead", false);
                        is_show_hat = jBool((JSONObject) parsedSite, "hat", true);
                    } else if (parsedSite instanceof JSONArray) {
                        JSONArray jsSite = (JSONArray) parsedSite;
                        if (jsSite.size() > 6) is_show_hat = Byte.parseByte(jsSite.get(6).toString()) == 1;
                    }

                    short[] fashion_ = null;
                    for (int i0 = 0; i0 < fashion.size(); i0++) {
                        if (fashion.get(i0).is_use) {
                            ItemFashion temp = ItemFashion.get_item(fashion.get(i0).id);
                            if (temp != null) {
                                fashion_ = temp.mWearing;
                                break;
                            }
                        }
                    }

                    if (fashion_ != null) {
                        if (!hfHead && fashion_.length > 6 && fashion_[6] > 0) {
                            head_ = fashion_[6];
                        }
                        if (!hfHair) {
                            if (fashion_.length > 7 && fashion_[7] > 0) {
                                hair_ = fashion_[7];
                            } else if (!hfHead && fashion_.length > 6 && fashion_[6] > 0) {
                                hair_ = -2;
                            } else if (is_show_hat && fashion_.length > 1 && fashion_[1] > 0 && fashion_.length > 7 && fashion_[7] == -2) {
                                hair_ = -2;
                            }
                        }
                    }

                    if (hair_ == -999) {
                        for (int i0 = 0; i0 < itfashionP.size(); i0++) {
                            if (itfashionP.get(i0).category == 103 && itfashionP.get(i0).is_use) {
                                hair_ = itfashionP.get(i0).icon;
                                break;
                            }
                        }
                    }
                    if (head_ == -999) {
                        for (int i0 = 0; i0 < itfashionP.size(); i0++) {
                            if (itfashionP.get(i0).category == 108 && itfashionP.get(i0).is_use) {
                                head_ = itfashionP.get(i0).icon;
                                break;
                            }
                        }
                    }

                    m2.writer().writeShort(i);
                    m2.writer().writeUTF(name);
                    m2.writer().writeByte(rs.getByte("clazz"));

                    String rawLevel = rs.getString("level");
                    Object parsedLevel = JSONValue.parse(rawLevel);
                    short lv = 1;
                    if (parsedLevel instanceof JSONObject) {
                        lv = jShort((JSONObject) parsedLevel, "lv", (short) 1);
                    } else if (parsedLevel instanceof JSONArray) {
                        JSONArray js_level = (JSONArray) parsedLevel;
                        lv = js_level.size() > 0 ? Short.parseShort(js_level.get(0).toString()) : 1;
                    }
                    m2.writer().writeShort(lv);

                    String rawBody = rs.getString("body_parts");
                    Object parsedBody = JSONValue.parse(rawBody);
                    short bHead = 0, bHair = 0;
                    if (parsedBody instanceof JSONObject) {
                        JSONObject jsBody = (JSONObject) parsedBody;
                        bHead = jShort(jsBody, "head", (short) 0);
                        bHair = jShort(jsBody, "hair", (short) 0);
                    } else if (parsedBody instanceof JSONArray) {
                        JSONArray jsBody = (JSONArray) parsedBody;
                        bHead = jsBody.size() > 0 ? Short.parseShort(jsBody.get(0).toString()) : 0;
                        bHair = jsBody.size() > 1 ? Short.parseShort(jsBody.get(1).toString()) : 0;
                    }

                    m2.writer().writeShort((head_ != -999) ? head_ : bHead);
                    m2.writer().writeShort((hair_ != -999) ? hair_ : bHair);

                    m2.writer().writeShort(Clan.get_icon_clan(name));

                    m2.writer().writeByte(6);

                    Item_wear[] it = new Item_wear[itemz.Item.MAX_BODY];

                    JSONArray jsIt = (JSONArray) JSONValue.parse(rs.getString("it_body"));

                    if (jsIt != null) {
                        for (int i1 = 0; i1 < jsIt.size(); i1++) {

                            Object obj = jsIt.get(i1);

                            if (obj != null) {

                                Item_wear temp = new Item_wear();

                                Item.readUpdateItem(obj.toString(), temp);

                                if (temp.index >= 0 && temp.index < it.length) {
                                    it[temp.index] = temp;
                                }

                            }

                        }
                    }



                    for (int j = 0; j < 6; j++) {
                        short part = -1;
                        if (j == 1) {
                            if (is_show_hat) {
                                if (fashion_ != null && fashion_.length > 1) {
                                    if (fashion_[1] > 0) {
                                        part = fashion_[1];
                                    } else if (fashion_[1] == -2) {
                                        part = -1;
                                    } else if (!hfHead && fashion_.length > 6 && fashion_[6] != -1) {
                                        part = -1;
                                    } else if (it[1] != null && it[1].template != null) {
                                        ItemTemplate3 t3 = ItemTemplate3.get_it_by_id(it[1].template.id);
                                        part = t3 != null ? (short) t3.part : (short) it[1].template.part;
                                    }
                                } else if (it[1] != null && it[1].template != null) {
                                    ItemTemplate3 t3 = ItemTemplate3.get_it_by_id(it[1].template.id);
                                    part = t3 != null ? (short) t3.part : (short) it[1].template.part;
                                }
                            }
                        } else {
                            if (fashion_ != null && (j == 0 || j == 3 || j == 5) && j < fashion_.length && fashion_[j] != -1) {
                                part = fashion_[j];
                            } else if (it[j] != null && it[j].template != null) {
                                ItemTemplate3 t3 = ItemTemplate3.get_it_by_id(it[j].template.id);
                                part = t3 != null ? (short) t3.part : (short) it[j].template.part;
                            }
                        }

                        if (part != -1) {
                            m2.writer().writeByte(1);
                            m2.writer().writeShort(part);
                        } else {
                            m2.writer().writeByte(0);
                        }
                    }

                    m2.writer().writeByte(0);

                }

            } catch (SQLException e) {

                e.printStackTrace();

            } finally {

                try {

                    if (rs != null) rs.close();

                    if (ps != null) ps.close();

                    if (connection != null) connection.close();

                } catch (SQLException e) {

                    e.printStackTrace();

                }

            }

        }

        addmsg(m2);

        m2.cleanup();

    }



    private static short jShort(JSONObject o, String key, short def) {

        try { Object v = o.get(key); return v == null ? def : Short.parseShort(v.toString()); } catch (Exception e) { return def; }

    }

    private static byte jByte(JSONObject o, String key, byte def) {

        try { Object v = o.get(key); return v == null ? def : Byte.parseByte(v.toString()); } catch (Exception e) { return def; }

    }

    private static boolean jBool(JSONObject o, String key, boolean def) {
        try {
            Object v = o.get(key);
            if (v == null) return def;
            if (v instanceof Boolean) return (Boolean) v;
            String s = v.toString().trim();
            if (s.equalsIgnoreCase("true") || s.equals("1")) return true;
            if (s.equalsIgnoreCase("false") || s.equals("0")) return false;
            return Integer.parseInt(s) == 1;
        } catch (Exception e) {
            return def;
        }
    }



    // -- login_notice --------------------------------------------------------------

    private void login_notice(String s) throws IOException {

        Message m = new Message(-11);

        m.writer().writeShort(0);

        m.writer().writeByte(0);

        m.writer().writeUTF("Thông báo");

        m.writer().writeUTF(s);

        m.writer().writeByte(0);

        addmsg(m);

        m.cleanup();

    }



    // -- ReadPartNew ---------------------------------------------------------------

    public void ReadPartNew(Message m2) throws IOException {

        short index = m2.reader().readShort();

        Part part = Part.get_part(index);

        if (part != null) {

            Message m = new Message(-82);

            m.writer().writeShort(index);

            m.writer().writeByte(part.type);

            for (int i = 0; i < part.pi.length; i++) {

                m.writer().writeShort(part.pi[i].id);

                m.writer().writeByte(part.pi[i].dx);

                m.writer().writeByte(part.pi[i].dy);

            }

            addmsg(m);

            m.cleanup();

        }

    }



    // -- create_char ---------------------------------------------------------------

    public void create_char(Message m2) throws IOException {

        if (!this.user.equals("admin")) {
            int currentChars = 0;
            try (Connection checkConn = DbManager.gI().getConnect();
                 PreparedStatement psCheck = checkConn.prepareStatement("SELECT COUNT(*) FROM `players` WHERE `account_id` = ?")) {
                psCheck.setInt(1, this.idUser);
                try (ResultSet rsCheck = psCheck.executeQuery()) {
                    if (rsCheck.next()) {
                        currentChars = rsCheck.getInt(1);
                    }
                }
            } catch (Exception e) {
                currentChars = list_char.size();
            }
            if (currentChars >= 3 || list_char.size() >= 3) {
                login_notice("Chỉ có thể tạo tối đa 3 nhân vật!");
                return;
            }
        }

        String name = m2.reader().readUTF().replace(" ", "");

        name = name.toLowerCase();

        if (!name.matches("^[a-z0-9]{4,15}$")) {

            login_notice("Tên nhân vật không hợp lệ!");

            return;

        }

        if (name.contains("admin") || name.contains("gm") || name.equals("ad") || name.contains("quantri") || name.contains("hethong")) {
            login_notice("Tên nhân vật chứa từ khóa cấm của hệ thống!");
            return;
        }

        byte clazz = m2.reader().readByte();

        int head   = m2.reader().readShort();

        int hair   = m2.reader().readShort();



        Connection connection = null;

        PreparedStatement ps = null;

        try {

            connection = DbManager.gI().getConnect();

            

            // 1. Insert minimal row

            String query = "INSERT INTO `players` (`account_id`, `name`, `clazz`) VALUES (?, ?, ?)";

            ps = connection.prepareStatement(query, java.sql.Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, this.idUser);

            ps.setString(2, name);

            ps.setInt(3, clazz);

            ps.executeUpdate();



            // Get generated ID

            int generatedId = 0;

            try (ResultSet rsKey = ps.getGeneratedKeys()) {

                if (rsKey.next()) {

                    generatedId = rsKey.getInt(1);

                }

            }



            // 2. Instantiate a temporary Player and initialize starting attributes

            Player p = new Player(this, name);
            if (this.role == 1 || this.is_admin == 1 || "admin".equalsIgnoreCase(this.user) || "ad".equalsIgnoreCase(this.user)) {
                p.admin = 1;
            }

            p.IDPlayer = generatedId;

            p.clazz = clazz;

            p.head = (short) head;

            p.hair = (short) hair;

            p.part_body = -1;

            p.part_leg = -1;

            p.part_ring = -1;

            p.part_weapon = -1;



            p.level = 1;

            p.exp = 0;

            p.thongthao = 0;

            p.date = org.joda.time.DateTime.now();
            p.timeOnline = 0L;
            p.loginTime = System.currentTimeMillis();



            p.pointAttribute = 5;

            p.point1 = 1;

            p.point2 = 1;

            p.point3 = 1;

            p.point4 = 1;

            p.point5 = 1;

            p.pointAttributeThongThao = 0;

            p.list_op_thongthao = new ArrayList<>();



            if (core.Manager.gI().isTestPhase()) {
                p.vang = 2_000_000_000L;
                p.kimcuong = 10_000_000;
            } else {
                p.vang = 2_000_000_000L;
                p.kimcuong = 1_000_000;
            }

            p.vnd = 0;

            p.bua = 0;

            p.tichLuy = 0;

            p.pvp_win = 0;

            p.pvp_lose = 0;

            p.time_ship = 0;

            p.time_can_hs = 7;

            p.time_nvl = 0;

            p.time_ttvt = 0;

            p.wanted_point = 0;

            p.tieuRuby = 0;

            p.coin = 0;

            p.ruby = 0;

            p.huongnghiep = 0;

            p.diem_danh_event = 0;

            p.hd_max = 3;

            p.pvppoint = 0;

            p.diemdanh = 0;

            p.diemdanh_ngay = 0;



            p.ticket = 20;

            p.cd_ticket_next = System.currentTimeMillis() + 600_000L;

            p.pvp_ticket = 3;

            p.cd_pvp_next = System.currentTimeMillis() + 7_200_000L;

            p.key_boss = 3;

            p.cd_keyboss_next = System.currentTimeMillis() + 3_600_000L;

            p.aiDonLevel = 0;

            p.nvlMax = 50;

            p.ttvtMax = 20;

            p.ltMax = 25;

            p.namiMax = 5;

            p.aidonMax = 20;

            p.mr3Max = 5;



            p.item = new Item(p);

            p.item.it_body = new Item_wear[itemz.Item.MAX_BODY];

            p.item.bag3 = new Item_wear[p.item.max_bag];

            p.item.box3 = new Item_wear[p.item.max_box];

            p.item.bag47 = new ArrayList<>();
            template.ItemBag47 card872 = new template.ItemBag47();
            card872.category = 4;
            card872.id = 872;
            card872.quant = 1;
            p.item.bag47.add(card872);



            p.item.box47 = new ArrayList<>();

            p.item.save_item_wear = new ArrayList<>();

            p.item.save_item_47 = new ArrayList<>();

            p.daHanhTrinh = new ArrayList<>();



            // Initialize equipment, skills, and fashion using class-based helpers

            List<Item_wear> equips = model.DeTu.getDefaultEquip(clazz);

            for (Item_wear w : equips) {

                if (w != null && w.index >= 0 && w.index < p.item.it_body.length) {

                    p.item.it_body[w.index] = w;

                }

            }



            p.skill_point = new ArrayList<>(Player.getDefaultPlayerSkills(clazz));

            p.setDefaultFashion(clazz);

            p.updateParts();



            p.list_eff = new CopyOnWriteArrayList<>();

            p.eff_save = new int[5];



            p.rms = new byte[11][];

            for (int i = 0; i < 11; i++) {

                p.rms[i] = new byte[0];

            }

            p.rms[0] = p.generateDefaultRms0();

            p.rms[4] = new byte[]{0, 18};



            p.tichTieuCheck = new byte[ListTichNap.ENTRY.size()];

            p.tieuRubyCheck = new byte[TichTieuRuby.ENTRY.size()];

            p.tichHangNgayCheck = new byte[ListNapHangNgay.ENTRY.size()];

            p.timeresetHangNgay = java.time.LocalDateTime.of(java.time.LocalDate.now(), java.time.LocalTime.MIDNIGHT);



            p.wanted_chest = new Wanted_Chest[2];

            for (int i = 0; i < 2; i++) {

                p.wanted_chest[i] = new Wanted_Chest();

            }



            p.my_pet = new ArrayList<>();

            p.friend_list = new ArrayList<>();

            p.enemy_list = new ArrayList<>();

            p.eventData = new ArrayList<>();

            achievement.ArchiDaily.ramdomArchiDaily(p);



            p.list_quest = new ArrayList<>();

            QuestP startQuest = new QuestP();

            startQuest.template = Quest.get_quest((short) 0);

            if (startQuest.template != null) {

                if (startQuest.template.data_quest != null && startQuest.template.data_quest.length > 0) {

                    startQuest.data = new short[startQuest.template.data_quest.length][];

                    for (int i = 0; i < startQuest.data.length; i++) {

                        startQuest.data[i] = new short[startQuest.template.data_quest[i].length];

                        for (int j = 0; j < startQuest.data[i].length; j++) {

                            startQuest.data[i][j] = startQuest.template.data_quest[i][j];

                        }

                    }

                } else {

                    startQuest.data = new short[0][];

                }

                p.list_quest.add(startQuest);

            }



            Zone[] startMap = Zone.getMapByID(0);

            if (startMap != null && startMap.length > 0 && startMap[0] != null) {

                p.map = startMap[0];

            }

            p.x = 300;

            p.y = 300;

            p.id_map_save = 1;

            p.time_can_hs = 7;

            p.is_show_hat = true;

            p.is_show_weapon = true;

            p.pointPk = 0;

            p.ability = new ability.Ability(p);

            p.hp = p.ability.get_hp_max(true);

            p.mp = p.ability.get_mp_max(true);

            p.mbv = "";

            p.timeHuyMbv = -1L;



            // Flush/Save to DB

            p.flush(p, true);

        } catch (SQLException e) {

            login_notice("Tên đã được sử dụng!");

            return;

        } catch (Exception e) {

            e.printStackTrace();

            login_notice("Lỗi tạo nhân vật!");

            return;

        } finally {

            closeQuietly(null, ps, connection);

        }

        list_char.add(name);

        flush();

        send_list_char();

    }



    // -- flush ---------------------------------------------------------------------

    public void flush() {
        // Không lưu list_char vào account.char vì players đã liên kết trực tiếp bằng account_id
        saveOnlineTime();
    }

    public long getTotalOnlineSeconds() {
        long sessionSec = 0L;
        if (this.loginTime > 0) {
            sessionSec = (System.currentTimeMillis() - this.loginTime) / 1000L;
        }
        return this.timeOnline + sessionSec;
    }

    public void saveOnlineTime() {
        if (this.user == null || this.loginTime <= 0) return;
        long totalSec = getTotalOnlineSeconds();
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = DbManager.gI().getConnect();
            if (connection == null) return;
            ps = connection.prepareStatement("UPDATE `account` SET `time_online` = ? WHERE `id` = ?;");
            ps.setLong(1, totalSec);
            ps.setInt(2, this.idUser);
            ps.executeUpdate();
            this.timeOnline = totalSec;
            this.loginTime = System.currentTimeMillis();
        } catch (Exception ignored) {
        } finally {
            closeQuietly(null, ps, connection);
        }
    }

    // -- update_onl ----------------------------------------------------------------

    public void update_onl(int type) {
        if (Manager.gI().server_admin) type = 0;
        if (this.user == null) return;
        saveOnlineTime();
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = DbManager.gI().getConnect();
            if (connection == null) return;
            ps = connection.prepareStatement("UPDATE `account` SET `onl` = ? WHERE BINARY `username` = ? LIMIT 1;");
            ps.setInt(1, type);
            ps.setString(2, this.user);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeQuietly(null, ps, connection);
        }
    }

    // -- check_onl -----------------------------------------------------------------
    public boolean check_onl() {
        if (this.user == null) return false;
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = DbManager.gI().getConnect();
            if (connection == null) return false;
            ps = connection.prepareStatement("SELECT `onl` FROM `account` WHERE BINARY `username` = ? LIMIT 1;");
            ps.setString(1, this.user);
            rs = ps.executeQuery();
            return rs.next() && rs.getBoolean("onl");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeQuietly(rs, ps, connection);
        }
    }

    // -- LockAccount ---------------------------------------------------------------
    public boolean LockAccount() {
        if (this.user == null) return false;
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = DbManager.gI().getConnect();
            if (connection == null) return false;
            ps = connection.prepareStatement("UPDATE `account` SET `lock` = 1 WHERE BINARY `username` = ? LIMIT 1;");
            ps.setString(1, this.user);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeQuietly(null, ps, connection);
        }
    }



    // -- clear_network -------------------------------------------------------------

    public void clear_network(Session ss) {

        if (ss.sendd != null) {

            ss.sendd.interrupt();

            ss.sendd = null;

        }

        if (ss.receiv != null) {

            ss.receiv.interrupt();

            ss.receiv = null;

        }

        try {

            if (ss.socketChannel != null && ss.socketChannel.isOpen()) {

                ss.socketChannel.close();

            }

        } catch (IOException e) {

            e.printStackTrace();

        }

    }



    // -- Check_Data_Ver ------------------------------------------------------------

    public void Check_Data_Ver() throws IOException {

        Message m = new Message(-6);

        m.writer().writeShort(DataTemplate.VerdataMon);

        m.writer().writeShort(DataTemplate.VerdataPotion);

        m.writer().writeShort(DataTemplate.VerdataAttri);

        m.writer().writeShort(-1);

        m.writer().writeShort(DataTemplate.VerdataNameMap);

        m.writer().writeShort(DataTemplate.VerdataNamePotionquest);

        m.writer().writeShort(-1);

        m.writer().writeShort(DataTemplate.VerdataImageSave);

        m.writer().writeShort(DataTemplate.VerdataUpgradeSave);

        m.writer().writeShort(DataTemplate.VerdataPotionClan);

        m.writer().writeShort(-1);

        addmsg(m);

        m.cleanup();



        m = new Message(-7);

        m.writer().writeByte(11);

        m.writer().writeByte(ItemTemplate7.ENTRYS.size());

        for (int i = 0; i < ItemTemplate7.ENTRYS.size(); i++) {

            ItemTemplate7 temp = ItemTemplate7.ENTRYS.get(i);

            m.writer().writeByte(temp.id);

            m.writer().writeUTF(temp.name);

            m.writer().writeByte(temp.type);

            m.writer().writeByte(temp.icon);

            m.writer().writeInt(temp.price);

            m.writer().writeShort(temp.priceruby);

            m.writer().writeByte(temp.istrade);

        }

        addmsg(m);

        m.cleanup();

    }



    public static final byte[] X2MSG_72_1 = new byte[] {0, 0, 16, 2, (byte)207, 2, (byte)236, 2, (byte)239, 2, (byte)244, 3, 30, 3, 31, 3, 33, 3, 34, 3, 81, 3, 83, 3, 122, 3, 126, 3, (byte)128, 3, (byte)182, 3, (byte)195, 3, (byte)204};

    public static final byte[] X2MSG_72_2 = new byte[] {1, 0, 17, 0, 0, 1, 54, 1, 55, 1, 56, 1, 57, 1, 58, 1, 59, 1, 83, 1, (byte)160, 1, (byte)158, 2, 9, 2, 30, 2, 31, 2, 32, 2, 33, 2, 34, 2, 35, 0, 2, 0, 4, 0, 10};

    public static final byte[] X2MSG_72_3 = new byte[] {4, 2, 0, 93, 0, 96};



    private static byte[] cached_msg7_6 = null;

    private static synchronized byte[] getMsg7_6() {

        if (cached_msg7_6 == null) {

            try {

                int maxId = 0;

                for (map.MapTemplate temp : map.MapTemplate.ENTRYS) {

                    if (temp.id > maxId) {

                        maxId = temp.id;

                    }

                }

                String[] names = new String[maxId + 1];

                for (int i = 0; i < names.length; i++) {

                    names[i] = "";

                }

                for (map.MapTemplate temp : map.MapTemplate.ENTRYS) {

                    if (temp.id >= 0 && temp.id < names.length) {

                        names[temp.id] = temp.name;

                    }

                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                DataOutputStream dos = new DataOutputStream(baos);

                dos.writeShort(names.length);

                for (String name : names) {

                    dos.writeUTF(name);

                }

                dos.close();

                cached_msg7_6 = baos.toByteArray();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

        return cached_msg7_6;

    }



    private static byte[] cached_msg7_31 = null;

    private static synchronized byte[] getMsg7_31() {

        if (cached_msg7_31 == null) {

            try {

                // Map ID vật phẩm/tóc -> ID Icon/Hiệu ứng

                java.util.Map<Integer, Integer> hairEffects = new java.util.LinkedHashMap<>();

                hairEffects.put(714, 200);

                hairEffects.put(715, 201);

                hairEffects.put(716, 202);

                hairEffects.put(717, 203);

                hairEffects.put(718, 204);

                hairEffects.put(771, 205);

                hairEffects.put(772, 206);

                hairEffects.put(773, 207);

                hairEffects.put(774, 208);

                hairEffects.put(775, 209);

                hairEffects.put(776, 210);

                hairEffects.put(777, 211);

                hairEffects.put(1008, 212);

                hairEffects.put(1012, 213);

                hairEffects.put(1016, 214);

                hairEffects.put(1019, 215);

                hairEffects.put(1023, 216);

                hairEffects.put(1026, 217);

                hairEffects.put(1061, 218);



                // Map ID vật phẩm -> Chỉ số mô tả

                java.util.Map<Integer, String> fashionStats = new java.util.LinkedHashMap<>();

                fashionStats.put(45, "+5% Tăng HP\n");

                fashionStats.put(46, "+3% Né tránh\n");

                fashionStats.put(47, "+20 Tự hồi HP\n");

                fashionStats.put(48, "+15 Tự hồi HP\n");

                fashionStats.put(55, "+3% Tăng HP\n+10% Tăng tấn công\n");

                fashionStats.put(56, "+5% Chí mạng\n+3% Tăng HP\n");

                fashionStats.put(57, "+3% Tăng HP\n+5% Xuyên giáp\n");

                fashionStats.put(58, "+3% Tăng HP\n+10% Tăng tấn công\n");

                fashionStats.put(59, "+5% Chí mạng\n+3% Tăng HP\n");

                fashionStats.put(60, "+3% Tăng HP\n+5% Xuyên giáp\n");

                fashionStats.put(61, "+2 T/n thể lực\n+2 T/n sức mạnh\n+50% Tăng tấn công\n");

                fashionStats.put(62, "+1% Tăng HP\n+3% Tăng MP\n");

                fashionStats.put(63, "+2% Tăng HP\n+5% Phản đòn\n");

                fashionStats.put(64, "+2% Tăng HP\n+5% Chí mạng\n");

                fashionStats.put(65, "+2% Tăng HP\n+3% Tăng MP\n");

                fashionStats.put(66, "+2% Tăng HP\n+5% Phản đòn\n");

                fashionStats.put(67, "+2% Tăng HP\n+5% Chí mạng\n");



                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                DataOutputStream dos = new DataOutputStream(baos);

                

                dos.writeShort(hairEffects.size());

                for (java.util.Map.Entry<Integer, Integer> entry : hairEffects.entrySet()) {

                    dos.writeInt(entry.getKey());

                    dos.writeInt(entry.getValue());

                }

                

                dos.writeShort(fashionStats.size());

                for (java.util.Map.Entry<Integer, String> entry : fashionStats.entrySet()) {

                    dos.writeInt(entry.getKey());

                    dos.writeUTF(entry.getValue());

                }

                

                dos.close();

                cached_msg7_31 = baos.toByteArray();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

        return cached_msg7_31;

    }



    // -- lechHead ------------------------------------------------------------------

    public static short[] ListLechHEAD = new short[] {

        719, 748, 751, 756, 798, 799, 801, 802, 849, 851, 894, 896, 950, 963, 972, 836, 736, 1053

    };



    public static void lechHead(Session session) {

        try {

            Message message = new Message(72);

            message.writer().writeByte(0);

            message.writer().writeShort(ListLechHEAD.length);

            for (short id : ListLechHEAD) {

                message.writer().writeShort(id);

            }

            session.addmsg(message);

            message.cleanup();

        } catch (IOException e) {

            // ignore

        }

    }



    // -- Helper: đóng JDBC resources -----------------------------------------------

    private static void closeQuietly(ResultSet rs, Statement st, Connection conn) {

        try { if (rs   != null) rs.close();   } catch (SQLException ignore) {}

        try { if (st   != null) st.close();   } catch (SQLException ignore) {}

        try { if (conn != null) conn.close(); } catch (SQLException ignore) {}

    }

}

