package core;

import historys.zLog;
import achievement.ArchiDaily;
import database.IDManager;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import network.Session;
import network.SessionManager;

public class ServerManager implements Runnable {
    private static ServerManager instance;
    private final Thread mythread;
    private ServerEventManager serverEventManager;
    public boolean running;
    private ServerSocketChannel serverChannel;
    private final long time;
    public boolean isBaoTri;

    public ServerManager() {
        this.time = System.currentTimeMillis();
        this.mythread = new Thread(this);
    }

    public static ServerManager gI() {
        if (instance == null) {
            instance = new ServerManager();
        }
        return instance;
    }

    public void init() {
        IDManager.init();
        Manager.gI().init();
        

        ArchiDaily.init();
        achievement.ArchiPrivatePass.init();
        zLog.gI().start_log();
        core.MailService.cleanExpiredMails();
        
        map.MapManager.getInstance().start();
        event.EventManager.gI().init(Manager.gI().ZEVENT_ID);
        serverEventManager = new ServerEventManager();
        serverEventManager.init();
        bot.botplayer.BotPlayerManager.init();
        this.running = true;
        this.mythread.start();
    }

    @Override
    public void run() {
        boolean bound = false;
        for (int retry = 0; retry < 10; retry++) {
            try {
                this.serverChannel = ServerSocketChannel.open();
                ServerSocket serverSocket = this.serverChannel.socket();
                serverSocket.setReuseAddress(true);
                serverSocket.bind(new java.net.InetSocketAddress(Manager.gI().server_port));
                bound = true;
                break;
            } catch (IOException e) {
                if (retry < 9) {
                    Log.warn("ServerManager", "Port " + Manager.gI().server_port + " is busy or still closing, retrying in 2s... (attempt " + (retry + 1) + "/10)");
                    try {
                        if (this.serverChannel != null) {
                            this.serverChannel.close();
                        }
                    } catch (Exception ignored) {}
                    try {
                        Thread.sleep(2000L);
                    } catch (InterruptedException ignored) {}
                } else {
                    Log.error("ServerManager", "Failed to bind to port " + Manager.gI().server_port + " after 10 attempts", e);
                    System.exit(0);
                }
            }
        }
        if (!bound) {
            System.exit(0);
        }
        long bootTimeMs = System.currentTimeMillis() - this.time;
        Log.summary(Manager.gI().server_port, bootTimeMs);

        
        while (this.running) {
            if(isBaoTri) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            try {
                SocketChannel clientChannel = this.serverChannel.accept();
                if (clientChannel != null) {
                    clientChannel.configureBlocking(true);
                    clientChannel.socket().setTcpNoDelay(true);
                    clientChannel.socket().setKeepAlive(true);
                    clientChannel.socket().setSoTimeout(300000); // 5 minutes read timeout
                    Session ss = new Session(clientChannel);
                    SessionManager.client_connect(ss);
                }
            } catch (java.nio.channels.ClosedChannelException e) {
                // Expected when serverChannel is closed during maintenance/shutdown
                break;
            } catch (Exception e) {
                if (this.running && !isBaoTri) {
                    e.printStackTrace();
                    System.err.println("err accept socket");
                }
            }
        }
    }

    public void close() throws IOException {
        zLog.gI().close_log();
        serverEventManager.close();
        running = false;
        if (serverChannel != null) serverChannel.close();
        instance = null;
    }

    public ServerSocketChannel get_server() {
        return this.serverChannel;
    }
}