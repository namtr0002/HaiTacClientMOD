package zabstracts;

import model.Player;
import network.Message;
import template.ItemTemplate4;
import template.GiftBox;
import network.Service;
import historys.DiemDanhHistory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbsDiemDanh {
    protected int id;
    protected String title;
    protected String buttonText;
    protected short[] itemIds;
    protected short[] itemNums;
    protected short idItemBack;
    protected byte catBack;
    protected String key;

    public String getSeasonKey() {
        return key != null ? key : "DiemDanh_" + id;
    }

    public void setSeasonKey(String key) {
        this.key = key;
    }

    private static final Map<Integer, AbsDiemDanh> instances = new HashMap<>();

    static {
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void init() {
        instances.clear();

        // Manual registration fallback to prevent classpath scanning issues
        try {
            register(model.DiemDanhEvent.gI());
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Class<?>> classes = core.ZUtil.getClasses("model");
        for (Class<?> clazz : classes) {
            if (AbsDiemDanh.class.isAssignableFrom(clazz) && !clazz.isInterface() && !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) {
                try {
                    java.lang.reflect.Constructor<?> constructor = clazz.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    AbsDiemDanh instance = (AbsDiemDanh) constructor.newInstance();
                    instances.put((int) instance.getIdItemBack(), instance);
                } catch (NoSuchMethodException e) {
                    // Ignore subclasses without default constructor
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void register(AbsDiemDanh instance) {
        instances.put((int) instance.getIdItemBack(), instance);
    }

    public static AbsDiemDanh get(int idItemBack) {
        return instances.get(idItemBack);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getButtonText() {
        return buttonText;
    }

    public short[] getItemIds() {
        return itemIds;
    }

    public short[] getItemNums() {
        return itemNums;
    }

    public short getIdItemBack() {
        return idItemBack;
    }

    public byte getCatBack() {
        return catBack;
    }

    public void sendDiemDanhMenu(Player p) throws IOException {
        Message m = new Message(69);
        m.writer().writeUTF(title);
        m.writer().writeUTF(buttonText);
        m.writer().writeByte(itemIds.length);
        for (int i = 0; i < itemIds.length; i++) {
            ItemTemplate4 it = ItemTemplate4.get_it_by_id(itemIds[i]);
            m.writer().writeByte(4);
            m.writer().writeUTF("Ngày " + (i + 1));
            m.writer().writeShort(it != null ? it.icon : 0);
            m.writer().writeByte(0);
            m.writer().writeShort(itemNums[i]);
            m.writer().writeByte(0);
        }
        m.writer().writeShort(idItemBack);
        m.writer().writeByte(catBack);
        p.addmsg(m);
        m.cleanup();
    }

    public void process(Player p, int select) throws IOException {
        if (select < 0 || select >= itemIds.length) {
            return;
        }

        if (p.diemdanh_ngay > 0) {
            p.getService().send_box_ThongBao_OK("Bạn đã báo danh hôm nay rồi. Hãy quay lại vào ngày mai!");
            return;
        }

        if (DiemDanhHistory.hasReceivedReward(p, itemIds.length - 1)) {
            p.getService().send_box_ThongBao_OK("Bạn đã hoàn thành điểm danh rồi. Hãy chờ sự kiện tiếp theo!");
            return;
        }

        int lastDiemDanhDay = -1;
        for (int i = 0; i < itemIds.length; i++) {
            if (DiemDanhHistory.hasReceivedReward(p, i)) {
                lastDiemDanhDay = i;
            }
        }

        if (select != lastDiemDanhDay + 1) {
            if (lastDiemDanhDay == -1) {
                p.getService().send_box_ThongBao_OK("Bạn chưa báo danh ngày nào. Hãy bắt đầu từ ngày 1.");
            } else {
                p.getService().send_box_ThongBao_OK("Bạn cần điểm danh theo thứ tự từng ngày. Hôm nay là ngày " + (lastDiemDanhDay + 2) + ". Hãy quay lại báo danh ngày trước đó.");
            }
            return;
        }

        if (DiemDanhHistory.hasReceivedReward(p, select)) {
            p.getService().send_box_ThongBao_OK("Bạn đã nhận quà ngày " + (select + 1) + " rồi.");
            return;
        }

        short itemId = itemIds[select];
        short itemNum = itemNums[select];
        DiemDanhHistory.logDetail(p, select + 1, itemId, itemNum);
        
        GiftBox gb = new GiftBox();
        gb.id = itemId;
        gb.type = 4;
        gb.name = ItemTemplate4.get_item_name(itemId);
        gb.icon = ItemTemplate4.get_it_by_id(itemId).icon;
        gb.num = itemNum;
        gb.color = 0;

        List<GiftBox> listGift = new ArrayList<>();
        listGift.add(gb);
        Service.send_gift(p, 1, title, "Bạn đã nhận quà Ngày " + (select + 1), listGift, true);
        p.diemdanh_ngay++;
    }
}
