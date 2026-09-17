import java.io.*;
import java.lang.reflect.*;
import java.util.Hashtable;
import java.util.Vector;

public final class Agetmap {
    private static final String BASE = "C:\\ThMadara\\HTTH\\SQL\\";
    private static final String SQL_FILE = BASE + "map_" + (int)(System.currentTimeMillis()/1000L) + ".sql";
    private static boolean running = false;
    private static int min = 0;
    private static int max = 32767;
    
    private static final Hashtable<Integer, String> boatDataMap = new Hashtable<>();
    private static final Hashtable<Integer, String> mobsDataMap = new Hashtable<>();

    private static final int DEFAULT_MAX_ZONE = 20;
    private static final int DEFAULT_MAX_PLAYER = 15;

    public static void run(final int from, final int to, final int delay) {
        tryAutoHook();
        if (running) return;
        running = true;
        new Thread() {
            public void run() {
                ensure();
                appendLog("RUN start from=" + from + " to=" + to + " delay=" + delay);
                for (int i = from; i <= to && running; i++) {
                    try {
                        appendLog("Requesting map id=" + i);
                        request((short)i);
                        try { Thread.sleep(delay); } catch (InterruptedException ie) { 
                            appendLog("Sleep interrupted: " + ie.toString()); 
                        }
                    } catch (Throwable t) { 
                        appendLog("Error in run loop for id=" + i + " -> " + t.toString()); 
                    }
                }
                running = false;
                appendLog("RUN finished");
            }
        }.start();
    }

    public static void stop() {
        running = false;
        appendLog("Stopped by user");
    }

    public static void onAnyPayloadReceived(int msgType, byte[] payload) {
        appendLog("onAnyPayloadReceived type=" + msgType + " len=" + ((payload==null)?0:payload.length));
        if (payload == null || payload.length == 0) {
            appendLog("payload empty");
            return;
        }
        appendLog("Received payload from message type: " + msgType);
        String preview = bytesPreview(payload, 64);
        appendLog("Payload preview: " + preview);

        try {
            String sql = toUpdateSql(payload);
            if (sql != null && sql.length() > 0) {
                appendLog("toUpdateSql succeeded, appending SQL");
                append(sql);
                return;
            } else {
                appendLog("toUpdateSql returned null/empty for this payload");
            }
        } catch (Throwable t) {
            appendLog("toUpdateSql threw: " + t.toString());
        }
        try {
            DataInputStream dtest = new DataInputStream(new ByteArrayInputStream(payload));
            if (payload.length >= 2) {
                int possibleId = dtest.readShort();
                appendLog("First short of payload (possible map id): " + possibleId);
            }
        } catch (Throwable t) {
            appendLog("Error testing payload structure: " + t.toString());
        }
        
        appendLog("No UPDATE produced for this payload (kept raw).");
    }

    private static void request(short id) {
        try {
            // SỬA MESSAGE TYPE TỪ 0 THÀNH 0x0B (ĐÚNG VỚI SERVER)
            Message m = new Message((byte)0x0B);
            DataOutputStream w = m.writer();
            w.writeShort(id);
            w.writeByte(1);  // YÊU CẦU DỮ LIỆU ĐẦY ĐỦ
            appendLog("Sending request for map id=" + id);
            Session_ME.getInstance().sendMessage(m);
        } catch (Throwable t) {
            appendLog("Error when sending request for id=" + id + " -> " + t.toString());
        }
    }

    private static String toUpdateSql(byte[] p) {
        try {
            DataInputStream d = new DataInputStream(new ByteArrayInputStream(p));
            short id = d.readShort();
            byte zone_id = d.readByte();
            byte typeViewPlayer = d.readByte();
            short px = d.readShort();
            short py = d.readShort();
            int maxHp = d.readInt();
            int curHp = d.readInt();
            int maxMp = d.readInt();
            int curMp = d.readInt();
            byte b = d.readByte();
            byte specMap = d.readByte();

            // CHỈ XỬ LÝ KHI b == 1 (MAP ĐẦY ĐỦ)
            if (b != 1) {
                appendLog("Skipping non-full map data (b != 1) for id=" + id);
                return null;
            }

            // ĐỌC DỮ LIỆU MAP
            byte[] dataMap = null;
            byte[] dataItem = null;
            Vector<String> vgosArray = new Vector<>();

            int l0 = d.readInt();
            if (l0 > 0) {
                dataMap = new byte[l0];
                d.readFully(dataMap);
            }
            
            int l1 = d.readInt();
            if (l1 > 0) {
                dataItem = new byte[l1];
                d.readFully(dataItem);
            }
            
            byte vc = d.readByte();
            for (int i = 0; i < vc; i++) {
                // SỬA LỖI: SERVER GỬI ID MAP, KHÔNG PHẢI TÊN MAP
                short mapId = d.readShort();
                short x = d.readShort();
                short y = d.readShort();
                short xnew = d.readShort();
                short ynew = d.readShort();
                vgosArray.addElement(mapId + "," + x + "," + y + "," + xnew + "," + ynew);
            }

            // ĐỌC CÁC TRƯỜNG KHÁC
            byte IDBack = d.readByte();
            short HBack = d.readShort();
            byte id_eff_map = d.readByte();
            byte level = d.readByte();
            byte typeChangeMap = d.readByte();

            Vector<String> mPosArray = new Vector<>();
            String strTime = "";

            if (specMap == 3) {
                byte cnt = d.readByte();
                for (int i = 0; i < cnt; i++) {
                    byte a = d.readByte();
                    byte b2 = d.readByte();
                    mPosArray.addElement(a + "," + b2);
                }
                strTime = d.readUTF();
            }

            String name = d.readUTF();
            
            // XỬ LÝ DATA MAP
            String dataField = "NULL";
            if (dataMap != null && dataMap.length >= 3) {
                try {
                    DataInputStream d2 = new DataInputStream(new ByteArrayInputStream(dataMap));
                    int w = d2.readUnsignedByte();
                    int h = d2.readUnsignedByte();
                    d2.readUnsignedByte(); // Bỏ qua byte thừa
                    
                    // Xử lý layer 0 (map data)
                    StringBuffer sbdata = new StringBuffer();
                    sbdata.append("[[");
                    
                    for (int r = 0; r < h; r++) {
                        for (int c = 0; c < w; c++) {
                            if (d2.available() > 0) {
                                sbdata.append(d2.readUnsignedByte());
                            } else {
                                sbdata.append(0);
                            }
                            if (c < w - 1) sbdata.append(",");
                        }
                        if (r < h - 1) sbdata.append("],[");
                    }
                    
                    sbdata.append("],[");
                    
                    // Xử lý layer 1 (item data)
                    for (int r = 0; r < h; r++) {
                        for (int c = 0; c < w; c++) {
                            if (d2.available() > 0) {
                                sbdata.append(d2.readUnsignedByte());
                            } else {
                                sbdata.append(0);
                            }
                            if (c < w - 1) sbdata.append(",");
                        }
                        if (r < h - 1) sbdata.append("],[");
                    }
                    
                    sbdata.append("]]");
                    dataField = "'" + escSql(sbdata.toString()) + "'";
                } catch (Throwable t) {
                    appendLog("Error parsing dataMap for id=" + id + " -> " + t.toString());
                    dataField = "NULL";
                }
            }

            // SỬA LỖI FORMAT MAPBACK
            String mapBack = "'[" + (IDBack & 0xFF) + "," + (HBack & 0xFFFF) + ",0,0]'";

            // LẤY DỮ LIỆU BOAT
            String boat = "[]";
            if (boatDataMap.containsKey(id)) boat = boatDataMap.get(id);

            // LẤY DỮ LIỆU MOBS
            String mobs = "[]";
            if (mobsDataMap.containsKey(id)) mobs = mobsDataMap.get(id);

            // SỬA LỖI FORMAT VGOS - ĐỊNH DẠNG ĐÚNG [[id,x,y,xnew,ynew]]
            String vgos = "[]";
            if (vgosArray.size() > 0) {
                StringBuffer vsb = new StringBuffer();
                vsb.append("[");
                for (int i = 0; i < vgosArray.size(); i++) {
                    String e = vgosArray.elementAt(i);
                    vsb.append("[").append(e).append("]");
                    if (i < vgosArray.size() - 1) vsb.append(",");
                }
                vsb.append("]");
                vgos = vsb.toString();
            }

            // SỬA LỖI FORMAT MPOS
            String mPosStr = "[]";
            if (mPosArray.size() > 0) {
                StringBuffer msb = new StringBuffer();
                msb.append("[");
                for (int i = 0; i < mPosArray.size(); i++) {
                    String[] parts = mPosArray.elementAt(i).split(",");
                    if (parts.length >= 2) {
                        msb.append("[").append(parts[0]).append(",").append(parts[1]).append("]");
                        if (i < mPosArray.size() - 1) msb.append(",");
                    }
                }
                msb.append("]");
                mPosStr = msb.toString();
            }

            // TẠO CÂU LỆNH UPDATE ĐÚNG ĐỊNH DẠNG
            StringBuilder upd = new StringBuilder();
            upd.append("UPDATE `pirvateserverz`.`maps` SET ");
            upd.append("`name` = '").append(escSql(name)).append("', ");
            upd.append("`mobs` = '").append(escSql(mobs)).append("', ");
            upd.append("`maxzone` = ").append(DEFAULT_MAX_ZONE).append(", ");
            upd.append("`maxplayer` = ").append(DEFAULT_MAX_PLAYER).append(", ");
            upd.append("`boat` = '").append(escSql(boat)).append("', ");
            upd.append("`typeViewPlayer` = ").append(typeViewPlayer & 0xFF).append(", ");
            upd.append("`b` = ").append(b & 0xFF).append(", ");
            upd.append("`specMap` = ").append(specMap & 0xFF).append(", ");
            upd.append("`vgos` = '").append(escSql(vgos)).append("', ");
            upd.append("`data` = ").append(dataField).append(", ");
            upd.append("`MapBack` = '").append(escSql(mapBack)).append("', ");
            upd.append("`id_eff_map` = ").append(id_eff_map & 0xFF).append(", ");
            upd.append("`level` = ").append(level & 0xFF).append(", ");
            upd.append("`typeChangeMap` = ").append(typeChangeMap & 0xFF).append(", ");
            upd.append("`mPosMapTrain` = '").append(escSql(mPosStr)).append("', ");
            upd.append("`strTimeChange` = '").append(escSql(strTime)).append("' ");
            upd.append("WHERE `id` = ").append(id).append(";");
            
            return upd.toString();
        } catch (Throwable t) {
            appendLog("toUpdateSql exception: " + t.toString());
            return null;
        }
    }

    private static void ensure() {
        try {
            File f = new File(BASE);
            if (!f.exists()) f.mkdirs();
            File f2 = new File(SQL_FILE);
            if (!f2.exists()) {
                f2.createNewFile();
                FileOutputStream fos = new FileOutputStream(f2);
                fos.write("-- SQL MAP\n".getBytes());
                fos.close();
            }
        } catch (Throwable t) {
            System.err.println("ensure error: " + t.toString());
        }
    }

    private static synchronized void append(String s) {
        try {
            ensure();
            FileOutputStream fos = new FileOutputStream(SQL_FILE, true);
            fos.write((s + "\r\n").getBytes());
            fos.close();
            appendLog("Appended SQL: " + (s.length()>120 ? s.substring(0,120) + "..." : s));
        } catch (Throwable t) {
            appendLog("append SQL error: " + t.toString());
        }
    }

    private static synchronized void appendLog(String s) {
        try {
            ensure();
            String line = System.currentTimeMillis() + " - " + s + "\r\n";
            System.out.println(line);
        } catch (Throwable t) {
            System.err.println("appendLog fail: " + t.toString());
        }
    }

    private static String bytesPreview(byte[] b, int max) {
        if (b == null) return "null";
        StringBuffer sb = new StringBuffer();
        int len = (b.length < max) ? b.length : max;
        for (int i = 0; i < len; i++) {
            int v = b[i] & 0xFF;
            if (v < 16) sb.append("0");
            sb.append(Integer.toHexString(v)).append(" ");
        }
        if (b.length > max) sb.append("...");
        return sb.toString();
    }

    private static String escSql(String s) {
        if (s == null) return "";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\\') sb.append("\\\\");
            else if (c == '\'') sb.append("\\'");
            else if (c == '\"') sb.append("\\\"");
            else if (c == '\n') sb.append("\\n");
            else if (c == '\r') sb.append("\\r");
            else sb.append(c);
        }
        return sb.toString();
    }
    
    // SỬA LỖI HOOK MESSAGE - NHẬN ĐÚNG RESPONSE TỪ SERVER
    private static void tryAutoHook() {
        try {
            Class<?> sm = Session_ME.class;
            Method[] m = sm.getMethods();
            Method setHandler = null;
            for (int i = 0; i < m.length; i++) {
                String methodName = m[i].getName().toLowerCase();
                if (methodName.contains("sethandler") || 
                    methodName.contains("addhandler") || 
                    methodName.contains("setlistener")) {
                    setHandler = m[i];
                    break;
                }
            }
            if (setHandler == null) {
                appendLog("ERROR: Cannot find handler method in Session_ME");
                return;
            }
            
            Class<?>[] params = setHandler.getParameterTypes();
            if (params == null || params.length == 0) {
                appendLog("ERROR: Handler method has no parameters");
                return;
            }
            
            final Class<?> handlerInterface = params[0];
            Object proxy = Proxy.newProxyInstance(
                handlerInterface.getClassLoader(), 
                new Class<?>[]{handlerInterface}, 
                new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if (args != null) {
                            for (int i = 0; i < args.length; i++) {
                                Object a = args[i];
                                if (a instanceof byte[]) {
                                    // NHẬN TẤT CẢ MESSAGE TYPE ĐỂ TÌM MAP DATA
                                    onAnyPayloadReceived(-999, (byte[]) a);
                                } else if (a != null && a.getClass().getName().indexOf("Message") >= 0) {
                                    try {
                                        Method readerM = a.getClass().getMethod("reader");
                                        Object dis = readerM.invoke(a);
                                        if (dis != null) {
                                            Method availM = dis.getClass().getMethod("available");
                                            int avail = ((Integer) availM.invoke(dis)).intValue();
                                            byte[] raw = new byte[avail];
                                            Method readFully = dis.getClass().getMethod("readFully", byte[].class);
                                            try { 
                                                readFully.invoke(dis, new Object[]{raw}); 
                                            } catch (Throwable ig) {}
                                            // NHẬN TẤT CẢ MESSAGE TYPE ĐỂ TÌM MAP DATA
                                            onAnyPayloadReceived(-998, raw);
                                        }
                                    } catch (Throwable t) {
                                        appendLog("Error in proxy invocation: " + t.toString());
                                    }
                                }
                            }
                        }
                        return null;
                    }
                });
            
            Object sess = Session_ME.getInstance();
            setHandler.invoke(sess, new Object[]{proxy});
            appendLog("SUCCESS: Auto-hooked Session_ME handler with proxy");
        } catch (Throwable t) {
            appendLog("ERROR: tryAutoHook failed: " + t.toString());
            t.printStackTrace();
        }
    }
}