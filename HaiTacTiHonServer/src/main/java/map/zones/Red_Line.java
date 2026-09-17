package map.zones;

import model.Player;
import core.ZUtil;
import network.Message;
import map.Zone;
import map.Vgo;
import template.EffTemplate;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class Red_Line {
    public static byte[][] KEY0;
    public static byte[][] KEY2;
    public static byte[][] KEY3;
    public static byte[][] KEY4;
    static {
        File folder = new File("data/red_line/x2");
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            Arrays.sort(files, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    int a1 = Integer.parseInt(o1.getName().split("\\.")[0]);
                    int a2 = Integer.parseInt(o2.getName().split("\\.")[0]);
                    return a1 > a2 ? 1 : -1;
                }
            });
            Red_Line.KEY2 = new byte[files.length][];
            try {
                for (int i = 0; i < Red_Line.KEY2.length; i++) {
                    File f = files[i];
                    Red_Line.KEY2[i] = ZUtil.loadfile(f.getAbsolutePath());
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        //
        folder = new File("data/red_line/x0");
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            Arrays.sort(files, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    int a1 = Integer.parseInt(o1.getName().split("\\.")[0]);
                    int a2 = Integer.parseInt(o2.getName().split("\\.")[0]);
                    return a1 > a2 ? 1 : -1;
                }
            });
            Red_Line.KEY0 = new byte[files.length][];
            try {
                for (int i = 0; i < Red_Line.KEY0.length; i++) {
                    File f = files[i];
                    Red_Line.KEY0[i] = ZUtil.loadfile(f.getAbsolutePath());
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        //
        folder = new File("data/red_line/x3");
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            Arrays.sort(files, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    int a1 = Integer.parseInt(o1.getName().split("\\.")[0]);
                    int a2 = Integer.parseInt(o2.getName().split("\\.")[0]);
                    return a1 > a2 ? 1 : -1;
                }
            });
            Red_Line.KEY3 = new byte[files.length][];
            try {
                for (int i = 0; i < Red_Line.KEY3.length; i++) {
                    File f = files[i];
                    Red_Line.KEY3[i] = ZUtil.loadfile(f.getAbsolutePath());
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        //
        folder = new File("data/red_line/x4");
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            Arrays.sort(files, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    int a1 = Integer.parseInt(o1.getName().split("\\.")[0]);
                    int a2 = Integer.parseInt(o2.getName().split("\\.")[0]);
                    return a1 > a2 ? 1 : -1;
                }
            });
            Red_Line.KEY4 = new byte[files.length][];
            try {
                for (int i = 0; i < Red_Line.KEY4.length; i++) {
                    File f = files[i];
                    Red_Line.KEY4[i] = ZUtil.loadfile(f.getAbsolutePath());
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }

    public static void process(Player p, Message m2) throws IOException {
        byte type = m2.reader().readByte();
        byte key = m2.reader().readByte();
        // System.out.println(type);
        // System.out.println(key);
        if (p.map.template.id == 64 && type == 0) {
            switch (key) {
                case 1: // up
                case 2: // right
                case 3: // down
                case 0: { // left
                    try {
                        if (p.key_red_line.size() > 0) {
                            if (p.key_red_line.take() != key) {
                                Red_Line.end(p, 1);
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (p.time_key_red_line != -1 && p.key_red_line.size() == 0) {
                        p.time_key_red_line--;
                        if (p.time_key_red_line == -1) {
                            Red_Line.end(p, 2);
                        } else {
                            Red_Line.init_key(p);
                        }
                    }
                    break;
                }
                case 10: { // init
                    if (p.time_key_red_line == -1) {
                        p.time_key_red_line = ZUtil.random(4, 7);
                        Red_Line.init_key(p);
                    } else if (p.key_red_line.size() == 0) {
                        Red_Line.init_key(p);
                    } else {
                        Red_Line.end(p, 1);
                    }
                    break;
                }
            }
        }
    }

    private static void end(Player p, int status) throws IOException {
        // finish 2: fail 1
        Message m = new Message(-72);
        m.writer().writeByte(status);
        p.addmsg(m);
        m.cleanup();
        //
        Vgo vgo = new Vgo();
        if (status == 2) {
            Zone[] map_go = Zone.getMapByID(65);
            vgo.map_go = map_go;
            vgo.xnew = 136;
            vgo.ynew = 230;
        } else {
            Zone[] map_go = Zone.getMapByID(63);
            vgo.map_go = map_go;
            vgo.xnew = 1515;
            vgo.ynew = 285;
        }
        final Vgo vgoFinal = vgo;
        final Player pFinal = p;
        new Thread(() -> {
            try {
                Thread.sleep(2500L);
                pFinal.goto_map(vgoFinal);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "RedLine-Exit-" + p.IDPlayer).start();
    }

    private static void init_key(Player p) throws IOException {
        if (!p.key_red_line.isEmpty()) {
            p.key_red_line.clear();
        }
        int key_size = ZUtil.random(3, 7);
        Message m = new Message(-72);
        m.writer().writeByte(0);
        m.writer().writeByte(key_size);
        for (int i = 0; i < key_size; i++) {
            int index = ZUtil.random(Red_Line.KEY2.length);
            p.key_red_line.add(index % 4);
            switch (p.getZoomlv()) {
                case 2: {
                    m.writer().writeInt(Red_Line.KEY2[index].length);
                    m.writer().write(Red_Line.KEY2[index]);
                    break;
                }
                case 3: {
                    m.writer().writeInt(Red_Line.KEY3[index].length);
                    m.writer().write(Red_Line.KEY3[index]);
                    break;
                }
                case 4: {
                    m.writer().writeInt(Red_Line.KEY4[index].length);
                    m.writer().write(Red_Line.KEY4[index]);
                    break;
                }
                default: {
                    m.writer().writeInt(Red_Line.KEY0[index].length);
                    m.writer().write(Red_Line.KEY0[index]);
                    break;
                }
            }
        }
        m.writer().writeInt(2000 * key_size);
        p.addmsg(m);
        m.cleanup();
    }

    public static boolean isBotPlayer(Player p) {
        if (p == null) return true;
        return p.isBot || p.isDe || p.conn == null || (p.getService() != null && p.getService() instanceof network.NoService);
    }

    public static void init_key_TTVT(Player p) throws IOException {
        if (isBotPlayer(p)) {
            return;
        }
        if (p.key_red_line.size() > 0) {
            p.key_red_line.clear();
        }
        int key_size = ZUtil.random(3, 8);
        Message m = new Message(-72);
        m.writer().writeByte(3);
        m.writer().writeByte(key_size);
        for (int i = 0; i < key_size; i++) {
            int index = ZUtil.random(Red_Line.KEY2.length);
            p.key_red_line.add(index % 4);
            switch (p.getZoomlv()) {
                case 2: {
                    m.writer().writeInt(Red_Line.KEY2[index].length);
                    m.writer().write(Red_Line.KEY2[index]);
                    break;
                }
                case 3: {
                    m.writer().writeInt(Red_Line.KEY3[index].length);
                    m.writer().write(Red_Line.KEY3[index]);
                    break;
                }
                case 4: {
                    m.writer().writeInt(Red_Line.KEY4[index].length);
                    m.writer().write(Red_Line.KEY4[index]);
                    break;
                }
                default: {
                    m.writer().writeInt(Red_Line.KEY0[index].length);
                    m.writer().write(Red_Line.KEY0[index]);
                    break;
                }
            }
        }
        m.writer().writeInt(1800 * key_size);
        p.addmsg(m);
        m.cleanup();
        EffTemplate eff = p.get_eff(20);
        if (eff != null) {
            eff.time = System.currentTimeMillis() + (1800 * key_size) + 1000L;
        } else {
            p.add_new_eff(20, 1, ((1800 * key_size) + 1000L));
        }
    }

    public static void process_TTVT(Player p, Message m2) throws IOException {
        if (isBotPlayer(p)) {
            return;
        }
        byte type = m2.reader().readByte();
        byte key = m2.reader().readByte();
        // System.out.println(type);
        // System.out.println(key);
        if (p.map != null && p.map.map_ThuThachVeThan != null && !p.map.map_ThuThachVeThan.isFinish && type == 3) {
            switch (key) {
                case 1: // up
                case 2: // right
                case 3: // down
                case 0: { // left
                    boolean isPressOK = false;
                    try {
                        if (p.key_red_line.size() > 0) {
                            if (p.key_red_line.take() != key) {
                                Red_Line.end_TTVT(p, 4);
                            } else {
                                isPressOK = true;
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (isPressOK) {
                        if (p.time_key_red_line != -1 && p.key_red_line.size() == 0) {
                            p.time_key_red_line--;
                            if (p.time_key_red_line == -1) {
                                Red_Line.end_TTVT(p, 5);
                            } else {
                                if (p.map.map_ThuThachVeThan.okP(p)) {
                                    for (int i = 0; i < p.map.players.size(); i++) {
                                        Player p0 = p.map.players.get(i);
                                        if (!isBotPlayer(p0)) {
                                            Red_Line.init_key_TTVT(p0);
                                        }
                                    }
                                    p.map.map_ThuThachVeThan.update_okP();
                                }
                            }
                        }
                    }
                    break;
                }
                case 10: { // init key request from client
                    Red_Line.init_key_TTVT(p);
                    break;
                }
            }
        }
    }

    private static void end_TTVT(Player p, int status) throws IOException {
        // finish 5: fail 4
        Message m = new Message(-72);
        m.writer().writeByte(status);
        for (int i = 0; i < p.map.players.size(); i++) {
            p.map.players.get(i).addmsg(m);
        }
        m.cleanup();
        //
        p.map.map_ThuThachVeThan.time_state = System.currentTimeMillis() + 3_000L;
        p.map.map_ThuThachVeThan.isFinish = true;
        if (status == 5) {
            p.map.map_ThuThachVeThan.isReceiv = true;
        }
    }

    public static void init_key_sky(Player p) throws IOException {
        if (p.key_red_line.size() > 0) {
            p.key_red_line.clear();
        }
        int key_size = ZUtil.random(3, 7);
        Message m = new Message(-72);
        m.writer().writeByte(3);
        m.writer().writeByte(key_size);
        for (int i = 0; i < key_size; i++) {
            int index = ZUtil.random(Red_Line.KEY2.length);
            p.key_red_line.add(index % 4);
            switch (p.getZoomlv()) {
                case 2: {
                    m.writer().writeInt(Red_Line.KEY2[index].length);
                    m.writer().write(Red_Line.KEY2[index]);
                    break;
                }
                case 3: {
                    m.writer().writeInt(Red_Line.KEY3[index].length);
                    m.writer().write(Red_Line.KEY3[index]);
                    break;
                }
                case 4: {
                    m.writer().writeInt(Red_Line.KEY4[index].length);
                    m.writer().write(Red_Line.KEY4[index]);
                    break;
                }
                default: {
                    m.writer().writeInt(Red_Line.KEY0[index].length);
                    m.writer().write(Red_Line.KEY0[index]);
                    break;
                }
            }
        }
        m.writer().writeInt(2000 * key_size);
        p.addmsg(m);
        m.cleanup();
    }

    public static void process_sky(Player p, Message m2) throws IOException {
        byte type = m2.reader().readByte();
        byte key = m2.reader().readByte();
        if (p.map.template.id == 109 && type == 3) {
            switch (key) {
                case 1: // up
                case 2: // right
                case 3: // down
                case 0: { // left
                    boolean isPressOK = false;
                    try {
                        if (p.key_red_line.size() > 0) {
                            if (p.key_red_line.take() != key) {
                                if (p.party != null) {
                                    for (int i = 0; i < p.party.list.size(); i++) {
                                        Player mem = p.party.list.get(i);
                                        mem.getService().send_box_ThongBao_OK(p.name + " đã trượt chân! Cả nhóm thất bại!");
                                        Red_Line.end_sky(mem, 4);
                                    }
                                } else {
                                    Red_Line.end_sky(p, 4);
                                }
                            } else {
                                isPressOK = true;
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (isPressOK) {
                        if (p.time_key_red_line != -1 && p.key_red_line.size() == 0) {
                            p.time_key_red_line--;
                            if (p.time_key_red_line == -1) {
                                if (p.party != null) {
                                    p.party.activeKeyIndex++;
                                    if (p.party.activeKeyIndex < p.party.list.size()) {
                                        Player nextP = p.party.list.get(p.party.activeKeyIndex);
                                        for (int i = 0; i < p.party.list.size(); i++) {
                                            p.party.list.get(i).getService().send_box_ThongBao_OK("Đến lượt " + nextP.name + " vượt thác!");
                                        }
                                        nextP.time_key_red_line = 3;
                                        Red_Line.init_key_sky(nextP);
                                    } else {
                                        for (int i = 0; i < p.party.list.size(); i++) {
                                            Red_Line.end_sky(p.party.list.get(i), 5);
                                        }
                                    }
                                } else {
                                    Red_Line.end_sky(p, 5);
                                }
                            } else {
                                Red_Line.init_key_sky(p);
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    private static void end_sky(Player p, int status) throws IOException {
        Message m = new Message(-72);
        m.writer().writeByte(status);
        p.addmsg(m);
        m.cleanup();
        
        Vgo vgo = new Vgo();
        if (status == 5) {
            Zone[] map_go = Zone.getMapByID(111);
            vgo.map_go = map_go;
            vgo.xnew = 24;
            vgo.ynew = 312;
        } else {
            Zone[] map_go = Zone.getMapByID(110);
            vgo.map_go = map_go;
            vgo.xnew = 1100;
            vgo.ynew = 250;
        }
        p.key_red_line.clear();
        p.time_key_red_line = -1;
        
        new Thread(() -> {
            try {
                Thread.sleep(4000L);
                p.goto_map(vgo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
