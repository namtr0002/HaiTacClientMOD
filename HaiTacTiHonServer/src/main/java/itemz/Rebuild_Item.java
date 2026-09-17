package itemz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Player;
import network.Service;
import core.ZUtil;
import network.Message;
import template.*;

import zabstracts.AbsUpgrade;
import zabstracts.AbsCombie;
import itemz.rebuilds.*;

public class Rebuild_Item {

    public static short[] ID_SELL
            = new short[]{74, 68, 62, 56, 50, 44, 272, 273, 274, 275, 276, 277};
    public static byte[] PERCENT_HOP_NGOC = new byte[]{120, 85, 70, 55, 40, 0};
    public static int[] PRICE_THAO_NGOC = new int[]{2, 6, 18, 54, 162, 300};
    public static short[][] ITEM_NGOC_SIEU_CAP;

    public static final java.util.Map<Byte, AbsUpgrade> upgrades = new java.util.HashMap<>();
    public static final java.util.Map<Byte, AbsCombie> combines = new java.util.HashMap<>();

    static {
        short[] id_ = new short[]{49, 55, 61, 67, 73, 79};
        short a = 0, b = 1;
        Rebuild_Item.ITEM_NGOC_SIEU_CAP = new short[30][];
        for (int i = 241; i < 271; i++) {
            Rebuild_Item.ITEM_NGOC_SIEU_CAP[i - 241] = new short[]{(short) i, id_[a], id_[b]};
            if (b < (id_.length - 1)) {
                b++;
                if (b == a && b < (id_.length - 1)) {
                    b++;
                }
            } else if (a < (id_.length - 1)) {
                a++;
                b = 0;
            }
        }
        scanAndRegister();
    }

    public static void show_table(Player p, int type) throws IOException {
        switch (type) {
            case 1: { // Ghep Da (Client RE_NANGCAPDA = 4)
                Message m = new Message(-67);
                m.writer().writeByte(0);
                m.writer().writeByte(4);
                p.addmsg(m);
                m.cleanup();
                p.item_to_kham_ngoc = null;
                p.item_to_kham_ngoc_id_ngoc = -1;
                p.data_yesno = null;
                return;
            }
            case 2: // Duc Lo (Client RE_DUT = 2)
                NangCapDucLo.getInstance().showTable(p);
                return;
            case 3: // Kham Vat Pham (Client RE_KHAM = 1)
                NangCapKhamNgoc.getInstance().showTable(p);
                return;
            case 4: // Tach Da / Thao Ngoc (Client RE_LAY = 3)
                NangCapThaoNgoc.getInstance().showTable(p);
                return;
            case 5: { // Da Sieu Cap (Client RE_DA_SIEU_CAP = 13)
                Message m = new Message(-67);
                m.writer().writeByte(0);
                m.writer().writeByte(13);
                p.addmsg(m);
                m.cleanup();
                p.item_to_kham_ngoc = null;
                p.item_to_kham_ngoc_id_ngoc = -1;
                p.data_yesno = null;
                return;
            }
            case 6: // Hoan My (Client RE_HOAN_MY = 10)
                NangCapHoanMy.getInstance().showTable(p);
                return;
            case 7: // Kich An (Client RE_KICH_AN = 11)
                NangCapKichAn.getInstance().showTable(p);
                return;
            case 8: // Cong Che Tac (Client RE_CONG_CHE_TAC = 12)
                NangCapCongCheTac.getInstance().showTable(p);
                return;
            case 9: { // Ghep Manh Trang Bi (Client RE_DO_9X = 14)
                Message m = new Message(-67);
                m.writer().writeByte(0);
                m.writer().writeByte(14);
                p.addmsg(m);
                m.cleanup();
                p.item_to_kham_ngoc = null;
                p.item_to_kham_ngoc_id_ngoc = -1;
                p.data_yesno = null;
                return;
            }
            case 10: // Duc Lo Dial (Client RE_DUC_LO_DIAL = 19)
                NangCapDucLoDial.getInstance().showTable(p);
                return;
            case 11: { // Ghep Su Kien (Client RE_SIEU_NANGCAPITEM = 15)
                Message m = new Message(-67);
                m.writer().writeByte(0);
                m.writer().writeByte(15);
                p.addmsg(m);
                m.cleanup();
                p.item_to_kham_ngoc = null;
                p.item_to_kham_ngoc_id_ngoc = -1;
                p.data_yesno = null;
                return;
            }
            default: {
                Message m = new Message(-67);
                m.writer().writeByte(0);
                m.writer().writeByte(type);
                p.addmsg(m);
                m.cleanup();
                p.item_to_kham_ngoc = null;
                p.item_to_kham_ngoc_id_ngoc = -1;
                p.data_yesno = null;
                return;
            }
        }
    }

    private static void scanAndRegister() {
        try {
            String packageName = "itemz.rebuilds";
            String path = packageName.replace('.', '/');
            java.net.URL resource = Rebuild_Item.class.getClassLoader().getResource(path);
            if (resource != null) {
                if (resource.getProtocol().equals("file")) {
                    java.io.File directory = new java.io.File(resource.toURI());
                    for (java.io.File file : directory.listFiles()) {
                        if (file.getName().endsWith(".class")) {
                            String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                            registerClass(className);
                        }
                    }
                } else if (resource.getProtocol().equals("jar")) {
                    java.net.JarURLConnection conn = (java.net.JarURLConnection) resource.openConnection();
                    java.util.jar.JarFile jar = conn.getJarFile();
                    java.util.Enumeration<java.util.jar.JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        java.util.jar.JarEntry entry = entries.nextElement();
                        String name = entry.getName();
                        if (name.startsWith(path + "/") && name.endsWith(".class")) {
                            String className = name.replace('/', '.').substring(0, name.length() - 6);
                            registerClass(className);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void registerClass(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            if (AbsUpgrade.class.isAssignableFrom(clazz) && !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) {
                AbsUpgrade inst = (AbsUpgrade) clazz.getDeclaredConstructor().newInstance();
                upgrades.put(inst.getType(), inst);
                //System.out.println("Auto registered upgrade type " + inst.getType() + " -> " + clazz.getSimpleName());
            } else if (AbsCombie.class.isAssignableFrom(clazz) && !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) {
                AbsCombie inst = (AbsCombie) clazz.getDeclaredConstructor().newInstance();
                combines.put(inst.getType(), inst);
                //core.Log.info("CombineHandler", "Registered combine type " + inst.getType() + " (" + clazz.getSimpleName() + ")");
            }
        } catch (Exception e) {
            // ignore
        }
    }

    public static void process(Player p, Message m2) throws IOException {
        if (p == null) return;
        if (p.trade_target != null) {
            p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
            return;
        }
        byte type = m2.reader().readByte();
        byte action = m2.reader().readByte();
        short idItem = m2.reader().readShort();
        byte cat = m2.reader().readByte();
        short num = m2.reader().readShort();
        m2.reader().readShort();

        AbsUpgrade upgrade = p.getUpgrade();
        if (upgrade == null || (upgrade.getType() != type && !(type == 12 && (upgrade instanceof itemz.rebuilds.NangCapThanTrang || upgrade instanceof itemz.rebuilds.NangCapTachThanTrang)))) {
            upgrade = upgrades.get(type);
            if (upgrade == null) {
                upgrade = AbsUpgrade.get(type);
            }
        }
        if (upgrade != null) {
            p.setUpgrade(upgrade);
            upgrade.process(p, type, action, idItem, cat, num);
            return;
        }

        AbsCombie combine = (p.getCombie() != null && p.getCombie().getType() == type) ? p.getCombie() : combines.get(type);
        if (combine == null) {
            combine = AbsCombie.get(type);
        }
        if (combine != null) {
            p.setCombie(combine);
            combine.process(p, type, action, idItem, cat, num);
            return;
        }
    }

    public static boolean isDaCap6(short id) {
        return id == 49 || id == 55 || id == 61 || id == 67 || id == 73 || id == 79 || id == 367;
    }

    public static boolean check_it_can_upgrade_da_sieu_cap(short idItem) {
        return isDaCap6(idItem);
    }

    public static boolean isGemCaoCap(short id) {
        if ((id >= 241 && id <= 270) || (id >= 368 && id <= 373) || (id >= 647 && id <= 682) || (id >= 324 && id <= 326) || id == 910) {
            return true;
        }
        // Đá Cấp 6
        if (id == 49 || id == 55 || id == 61 || id == 67 || id == 73 || id == 79 || id == 367 || id == 226) {
            return true;
        }
        return false;
    }

    public static int get_percent_hop_ngoc(short idItem) {
        if (idItem >= 221 && idItem <= 226) {
            return idItem - 221;
        } else if (idItem >= 362 && idItem <= 367) {
            return idItem - 362;
        } else if (idItem >= 44 && idItem <= 79) {
            return (idItem - 44) % 6;
        } else {
            return -1;
        }
    }

    public static void add_op_ngoc_kham_new(Item_wear it_select, short id) {// vuong code chỉ số đá khảm
        if (it_select == null) return;
        if (it_select.mdakham != null && it_select.mdakham.length > 0) {
            short[] temp = new short[it_select.mdakham.length + 1];
            for (int i = 0; i < it_select.mdakham.length; i++) {
                temp[i] = it_select.mdakham[i];
            }
            temp[temp.length - 1] = id;
            it_select.mdakham = temp;
        } else {
            it_select.mdakham = new short[]{id};
        }
        byte[] id_add = null;
        int[] par_add = null;
        switch (id) {
            case 44: {
                id_add = new byte[]{4};
                par_add = new int[]{10};
                break;
            }
            case 45: {
                id_add = new byte[]{4};
                par_add = new int[]{15};
                break;
            }
            case 46: {
                id_add = new byte[]{4};
                par_add = new int[]{20};
                break;
            }
            case 47: {
                id_add = new byte[]{4};
                par_add = new int[]{30};
                break;
            }
            case 48: {
                id_add = new byte[]{4};
                par_add = new int[]{45};
                break;
            }
            case 49: {
                id_add = new byte[]{4};
                par_add = new int[]{60};
                break;
            }
            case 50: {
                id_add = new byte[]{1};
                par_add = new int[]{25};
                break;
            }
            case 51: {
                id_add = new byte[]{1};
                par_add = new int[]{35};
                break;
            }
            case 52: {
                id_add = new byte[]{1};
                par_add = new int[]{50};
                break;
            }
            case 53: {
                id_add = new byte[]{1};
                par_add = new int[]{70};
                break;
            }
            case 54: {
                id_add = new byte[]{1};
                par_add = new int[]{90};
                break;
            }
            case 55: {
                id_add = new byte[]{1};
                par_add = new int[]{120};
                break;
            }
            case 56: {
                id_add = new byte[]{10};
                par_add = new int[]{5};
                break;
            }
            case 57: {
                id_add = new byte[]{10};
                par_add = new int[]{10};
                break;
            }
            case 58: {
                id_add = new byte[]{10};
                par_add = new int[]{15};
                break;
            }
            case 59: {
                id_add = new byte[]{10};
                par_add = new int[]{20};
                break;
            }
            case 60: {
                id_add = new byte[]{10};
                par_add = new int[]{25};
                break;
            }
            case 61: {
                id_add = new byte[]{10};
                par_add = new int[]{30};
                break;
            }
            case 62: {
                id_add = new byte[]{13};
                par_add = new int[]{5};
                break;
            }
            case 63: {
                id_add = new byte[]{13};
                par_add = new int[]{10};
                break;
            }
            case 64: {
                id_add = new byte[]{13};
                par_add = new int[]{15};
                break;
            }
            case 65: {
                id_add = new byte[]{13};
                par_add = new int[]{20};
                break;
            }
            case 66: {
                id_add = new byte[]{13};
                par_add = new int[]{25};
                break;
            }
            case 67: {
                id_add = new byte[]{13};
                par_add = new int[]{30};
                break;
            }
            case 68: {
                id_add = new byte[]{26, 27};
                par_add = new int[]{5, 5};
                break;
            }
            case 69: {
                id_add = new byte[]{26, 27};
                par_add = new int[]{10, 10};
                break;
            }
            case 70: {
                id_add = new byte[]{26, 27};
                par_add = new int[]{15, 15};
                break;
            }
            case 71: {
                id_add = new byte[]{26, 27};
                par_add = new int[]{20, 20};
                break;
            }
            case 72: {
                id_add = new byte[]{26, 27};
                par_add = new int[]{25, 25};
                break;
            }
            case 73: {
                id_add = new byte[]{26, 27};
                par_add = new int[]{30, 30};
                break;
            }
            case 74: {
                id_add = new byte[]{14};
                par_add = new int[]{3};
                break;
            }
            case 75: {
                id_add = new byte[]{14};
                par_add = new int[]{5};
                break;
            }
            case 76: {
                id_add = new byte[]{14};
                par_add = new int[]{8};
                break;
            }
            case 77: {
                id_add = new byte[]{14};
                par_add = new int[]{10};
                break;
            }
            case 78: {
                id_add = new byte[]{14};
                par_add = new int[]{12};
                break;
            }
            case 79: {
                id_add = new byte[]{14};
                par_add = new int[]{15};
                break;
            }
            case 241: {
                id_add = new byte[]{4, 48};
                par_add = new int[]{60, 10};
                break;
            }
            case 242: {
                id_add = new byte[]{4, 49};
                par_add = new int[]{60, 20};
                break;
            }
            case 243: {
                id_add = new byte[]{4, 50};
                par_add = new int[]{60, 20};
                break;
            }
            case 244: {
                id_add = new byte[]{4, 51};
                par_add = new int[]{60, 20};
                break;
            }
            case 245: {
                id_add = new byte[]{4, 52};
                par_add = new int[]{60, 20};
                break;
            }
            case 246: {
                id_add = new byte[]{1, 47};
                par_add = new int[]{120, 20};
                break;
            }
            case 247: {
                id_add = new byte[]{1, 49};
                par_add = new int[]{120, 20};
                break;
            }
            case 248: {
                id_add = new byte[]{1, 50};
                par_add = new int[]{120, 20};
                break;
            }
            case 249: {
                id_add = new byte[]{1, 51};
                par_add = new int[]{120, 20};
                break;
            }
            case 250: {
                id_add = new byte[]{1, 52};
                par_add = new int[]{120, 20};
                break;
            }
            case 251: {
                id_add = new byte[]{10, 47};
                par_add = new int[]{30, 20};
                break;
            }
            case 252: {
                id_add = new byte[]{10, 48};
                par_add = new int[]{30, 10};
                break;
            }
            case 253: {
                id_add = new byte[]{10, 50};
                par_add = new int[]{30, 20};
                break;
            }
            case 254: {
                id_add = new byte[]{10, 51};
                par_add = new int[]{30, 20};
                break;
            }
            case 255: {
                id_add = new byte[]{10, 52};
                par_add = new int[]{30, 20};
                break;
            }
            case 256: {
                id_add = new byte[]{13, 47};
                par_add = new int[]{30, 20};
                break;
            }
            case 257: {
                id_add = new byte[]{13, 48};
                par_add = new int[]{30, 10};
                break;
            }
            case 258: {
                id_add = new byte[]{13, 49};
                par_add = new int[]{30, 20};
                break;
            }
            case 259: {
                id_add = new byte[]{13, 51};
                par_add = new int[]{30, 20};
                break;
            }
            case 260: {
                id_add = new byte[]{13, 52};
                par_add = new int[]{30, 20};
                break;
            }
            case 261: {
                id_add = new byte[]{26, 27, 47};
                par_add = new int[]{30, 30, 20};
                break;
            }
            case 262: {
                id_add = new byte[]{26, 27, 48};
                par_add = new int[]{30, 30, 10};
                break;
            }
            case 263: {
                id_add = new byte[]{26, 27, 49};
                par_add = new int[]{30, 30, 20};
                break;
            }
            case 264: {
                id_add = new byte[]{26, 27, 50};
                par_add = new int[]{30, 30, 20};
                break;
            }
            case 265: {
                id_add = new byte[]{26, 27, 52};
                par_add = new int[]{30, 30, 20};
                break;
            }
            case 266: {
                id_add = new byte[]{14, 47};
                par_add = new int[]{10, 20};
                break;
            }
            case 267: {
                id_add = new byte[]{14, 48};
                par_add = new int[]{10, 10};
                break;
            }
            case 268: {
                id_add = new byte[]{14, 49};
                par_add = new int[]{10, 20};
                break;
            }
            case 269: {
                id_add = new byte[]{14, 50};
                par_add = new int[]{10, 20};
                break;
            }
            case 270: {
                id_add = new byte[]{14, 51};
                par_add = new int[]{10, 20};
                break;
            }
            case 362: {
                id_add = new byte[]{12};
                par_add = new int[]{5};
                break;
            }
            case 363: {
                id_add = new byte[]{12};
                par_add = new int[]{10};
                break;
            }
            case 364: {
                id_add = new byte[]{12};
                par_add = new int[]{15};
                break;
            }
            case 365: {
                id_add = new byte[]{12};
                par_add = new int[]{20};
                break;
            }
            case 366: {
                id_add = new byte[]{12};
                par_add = new int[]{25};
                break;
            }
            case 367: {
                id_add = new byte[]{12};
                par_add = new int[]{30};
                break;
            }
            case 368: {
                id_add = new byte[]{12, 47};
                par_add = new int[]{30, 20};
                break;
            }
            case 369: {
                id_add = new byte[]{12, 48};
                par_add = new int[]{30, 10};
                break;
            }
            case 370: {
                id_add = new byte[]{12, 49};
                par_add = new int[]{30, 20};
                break;
            }
            case 371: {
                id_add = new byte[]{12, 50};
                par_add = new int[]{30, 20};
                break;
            }
            case 372: {
                id_add = new byte[]{12, 51};
                par_add = new int[]{30, 20};
                break;
            }
            case 373: {
                id_add = new byte[]{12, 52};
                par_add = new int[]{30, 20};
                break;
            }
            case 324: {
                id_add = new byte[]{1, 4, 10, 13, 14, 26, 27};
                par_add = new int[]{40, 50, 6, 20, 5, 20, 20};
                break;
            }
            case 325: {
                id_add = new byte[]{49, 50, 52, 51, 47, 48};
                par_add = new int[]{20, 20, 20, 20, 20, 10};
                break;
            }
            case 910: {
                id_add = new byte[]{49, 50, 52, 51, 47, 48};
                par_add = new int[]{15, 15, 15, 15, 15, 10};
                break;
            }
            case 326: {
                id_add = new byte[]{1, 4, 10, 13, 14, 26, 27};
                par_add = new int[]{60, 80, 8, 35, 8, 35, 35};
                break;
            }
            case 647: {
                id_add = new byte[]{4, 48};
                par_add = new int[]{100, 15};
                break;
            }
            case 648: {
                id_add = new byte[]{4, 49};
                par_add = new int[]{100, 25};
                break;
            }
            case 649: {
                id_add = new byte[]{4, 50};
                par_add = new int[]{100, 25};
                break;
            }
            case 650: {
                id_add = new byte[]{4, 51};
                par_add = new int[]{100, 25};
                break;
            }
            case 651: {
                id_add = new byte[]{4, 52};
                par_add = new int[]{100, 25};
                break;
            }
            case 652: {
                id_add = new byte[]{1, 47};
                par_add = new int[]{80, 25};
                break;
            }
            case 653: {
                id_add = new byte[]{1, 49};
                par_add = new int[]{80, 25};
                break;
            }
            case 654: {
                id_add = new byte[]{1, 50};
                par_add = new int[]{80, 25};
                break;
            }
            case 655: {
                id_add = new byte[]{1, 51};
                par_add = new int[]{80, 25};
                break;
            }
            case 656: {
                id_add = new byte[]{1, 52};
                par_add = new int[]{80, 25};
                break;
            }
            case 657: {
                id_add = new byte[]{10, 47};
                par_add = new int[]{8, 25};
                break;
            }
            case 658: {
                id_add = new byte[]{10, 48};
                par_add = new int[]{8, 15};
                break;
            }
            case 659: {
                id_add = new byte[]{10, 50};
                par_add = new int[]{8, 25};
                break;
            }
            case 660: {
                id_add = new byte[]{10, 51};
                par_add = new int[]{8, 25};
                break;
            }
            case 661: {
                id_add = new byte[]{10, 52};
                par_add = new int[]{8, 25};
                break;
            }
            case 662: {
                id_add = new byte[]{13, 47};
                par_add = new int[]{40, 25};
                break;
            }
            case 663: {
                id_add = new byte[]{13, 48};
                par_add = new int[]{40, 15};
                break;
            }
            case 664: {
                id_add = new byte[]{13, 49};
                par_add = new int[]{40, 25};
                break;
            }
            case 665: {
                id_add = new byte[]{13, 51};
                par_add = new int[]{40, 25};
                break;
            }
            case 666: {
                id_add = new byte[]{13, 52};
                par_add = new int[]{40, 25};
                break;
            }
            case 667: {
                id_add = new byte[]{26, 27, 47};
                par_add = new int[]{40, 40, 25};
                break;
            }
            case 668: {
                id_add = new byte[]{26, 27, 48};
                par_add = new int[]{40, 40, 15};
                break;
            }
            case 669: {
                id_add = new byte[]{26, 27, 49};
                par_add = new int[]{40, 40, 25};
                break;
            }
            case 670: {
                id_add = new byte[]{26, 27, 50};
                par_add = new int[]{40, 40, 25};
                break;
            }
            case 671: {
                id_add = new byte[]{26, 27, 52};
                par_add = new int[]{40, 40, 25};
                break;
            }
            case 672: {
                id_add = new byte[]{14, 47};
                par_add = new int[]{8, 25};
                break;
            }
            case 673: {
                id_add = new byte[]{14, 48};
                par_add = new int[]{8, 15};
                break;
            }
            case 674: {
                id_add = new byte[]{14, 49};
                par_add = new int[]{8, 25};
                break;
            }
            case 675: {
                id_add = new byte[]{14, 50};
                par_add = new int[]{8, 25};
                break;
            }
            case 676: {
                id_add = new byte[]{14, 51};
                par_add = new int[]{8, 25};
                break;
            }
            case 677: {
                id_add = new byte[]{12, 47};
                par_add = new int[]{8, 25};
                break;
            }
            case 678: {
                id_add = new byte[]{12, 48};
                par_add = new int[]{8, 15};
                break;
            }
            case 679: {
                id_add = new byte[]{12, 49};
                par_add = new int[]{8, 25};
                break;
            }
            case 680: {
                id_add = new byte[]{12, 50};
                par_add = new int[]{8, 25};
                break;
            }
            case 681: {
                id_add = new byte[]{12, 51};
                par_add = new int[]{8, 25};
                break;
            }
            case 682: {
                id_add = new byte[]{12, 52};
                par_add = new int[]{8, 25};
                break;
            }
        }
        if (id_add != null && par_add != null) {
            if (it_select.option_item_2 == null) {
                it_select.option_item_2 = new ArrayList<>();
            }
            for (int i = 0; i < id_add.length; i++) {
                Option op_new = null;
                for (int j = 0; j < it_select.option_item_2.size(); j++) {
                    if (it_select.option_item_2.get(j).id == id_add[i]) {
                        op_new = it_select.option_item_2.get(j);
                        break;
                    }
                }
                if (op_new != null) {
                    int par_old = op_new.getParam();
                    op_new.setParam(par_old + par_add[i]);
                } else {
                    op_new = new Option(id_add[i], par_add[i]);
                    it_select.option_item_2.add(op_new);
                }
            }
        }
    }

    public static boolean check_can_kham_len_item(Item_wear it_select, int id) {
        if (it_select == null || it_select.template == null) {
            return false;
        }
        // Thần Trang không thể khảm ngọc
        if (it_select.isThanTrang()) {
            return false;
        }
        // Quả Tim (Type 6) không có lỗ và không thể khảm ngọc
        if (it_select.template.typeEquip == 6 || it_select.template.id == 11000) {
            return false;
        }
        // Đá Hải Thạch không cho khảm lên trang bị
        if (id >= 221 && id <= 226) {
            return false;
        }
        if (it_select.template.typeEquip == 7 || (id >= 324 && id <= 326) || id == 910) {
            return true;
        }
        boolean result = false;
        switch (it_select.template.typeEquip) {
            case 0: {
                if (id >= 50 && id <= 55 || id >= 246 && id <= 250 || id >= 652 && id <= 656) {
                    result = true;
                }
                break;
            }
            case 1:
            case 3:
            case 5: {
                if (id >= 68 && id <= 73 || id >= 44 && id <= 49 || id >= 241 && id <= 245
                        || id >= 261 && id <= 265 || id >= 362 && id <= 373
                        || id >= 647 && id <= 651 || id >= 667 && id <= 671
                        || id >= 677 && id <= 682) {
                    result = true;
                }
                break;
            }
            case 2:
            case 4: {
                if (id >= 74 && id <= 79 || id >= 56 && id <= 67 || id >= 266 && id <= 270
                        || id >= 251 && id <= 260 || id >= 672 && id <= 676
                        || id >= 657 && id <= 666) {
                    result = true;
                }
                break;
            }
        }
        return result;
    }

    public static short get_id_ngoc_sieu_cap(int id1, int id2) {
        if (id1 == 367) {
            return switch (id2) {
                case 49 -> 368;
                case 55 -> 369;
                case 61 -> 370;
                case 67 -> 371;
                case 73 -> 372;
                case 79 -> 373;
                default -> 368;
            };
        }
        for (int i = 0; i < Rebuild_Item.ITEM_NGOC_SIEU_CAP.length; i++) {
            if (Rebuild_Item.ITEM_NGOC_SIEU_CAP[i][1] == id1
                    && Rebuild_Item.ITEM_NGOC_SIEU_CAP[i][2] == id2) {
                return Rebuild_Item.ITEM_NGOC_SIEU_CAP[i][0];
            }
        }
        return -1;
    }
}
