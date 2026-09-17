package activities;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Player;
import network.Message;
import zabstracts.AbsUpgradeDevil;

public class UpgradeDevil extends AbsUpgradeDevil {
    private static final Map<Integer, AbsUpgradeDevil> handlers = new HashMap<>();

    static {
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void init() {
        handlers.clear();
        registerPackage("itemz.rebuilds");
        registerPackage("model");
    }

    private static void registerPackage(String packageName) {
        List<Class<?>> classes = core.ZUtil.getClasses(packageName);
        for (Class<?> clazz : classes) {
            if (AbsUpgradeDevil.class.isAssignableFrom(clazz) && !clazz.isInterface() && !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) {
                try {
                    AbsUpgradeDevil handler = (AbsUpgradeDevil) clazz.getDeclaredConstructor().newInstance();
                    handlers.put(handler.getId(), handler);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public byte getClientType() {
        return 0;
    }

    @Override
    public void show_table(Player p) throws IOException {
        // Default empty implementation
    }

    public static void show_table(Player p, int index) throws IOException {
        if (p == null || p.trade_target != null) {
            if (p != null) p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
            return;
        }
        AbsUpgradeDevil handler = handlers.get(index);
        if (handler == null) {
            handler = AbsUpgradeDevil.get(index);
        }
        if (handler != null) {
            p.setUpgradeDevil(handler);
            handler.show_table(p);
        }
    }

    @Override
    public boolean checkProcess(Player p, byte action, short id, byte cat, short num) throws IOException {
        return false;
    }

    @Override
    public void process(Player p, byte action, short id, byte cat, short num) throws IOException {
        // Default empty implementation
    }

    @Override
    public void process(Player p, Message m2) throws IOException {
        if (p == null || p.trade_target != null) return;
        byte act = m2.reader().readByte();
        short id = m2.reader().readShort();
        byte cat = m2.reader().readByte();
        short num = m2.reader().readShort();

        AbsUpgradeDevil active = p.getUpgradeDevil();
        if (active != null && active.checkProcess(p, act, id, cat, num)) {
            active.process(p, act, id, cat, num);
            return;
        }

        zabstracts.AbsCheTao cheTao = p.getCheTao();
        if (cheTao != null && cheTao.checkProcess(p, act, id, cat, num)) {
            cheTao.process(p, act, id, cat, num);
            return;
        }

        for (AbsUpgradeDevil handler : handlers.values()) {
            if (handler.checkProcess(p, act, id, cat, num)) {
                p.setUpgradeDevil(handler);
                handler.process(p, act, id, cat, num);
                return;
            }
        }

        for (zabstracts.AbsCheTao ct : zabstracts.AbsCheTao.CHE_TAO_MAP.values()) {
            if (ct.checkProcess(p, act, id, cat, num)) {
                p.setCheTao(ct);
                ct.process(p, act, id, cat, num);
                return;
            }
        }
    }
}
