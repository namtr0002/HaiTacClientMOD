package zinterfaces.menus.npcs;

import model.Player;
import core.MenuController;
import zinterfaces.iMenuDymanic;
import zinterfaces.iNpc;
import java.io.IOException;
import map.Npc;
import rank.TopCaoThu;
import rank.TopClan;
import rank.TopPVP;
import rank.TopWanted;
import template.GiftTemplate;


public class NpcTruongLang implements iNpc {

    @Override
    public short[] getId() {
        return new short[]{-145,-122,-118,-103,-87,-74,-67,-45,-31,-21,-13,-1};
    }

    @Override
    public void sendMenu(Player p, Npc npc) throws IOException {
        short type = npc != null ? npc.idmenu : -1;
        java.util.List<model.Menu> menus = new java.util.ArrayList<>();
        menus.add(new model.Menu("Thách đấu", (short) 101, () -> {
            try { TopPVP.gI().show(p, 0); } catch (Exception e) { e.printStackTrace(); }
        }));
        menus.add(new model.Menu("Cao thủ", (short) 101, () -> {
            try { TopCaoThu.gI().show(p, 0); } catch (Exception e) { e.printStackTrace(); }
        }));
        menus.add(new model.Menu("Băng hải tặc", (short) 101, () -> {
            try { TopClan.gI().show(p, 0); } catch (Exception e) { e.printStackTrace(); }
        }));
        menus.add(new model.Menu("Truy nã", (short) 101, () -> {
            try { TopWanted.gI().show(p, 0); } catch (Exception e) { e.printStackTrace(); }
        }));
        menus.add(new model.Menu("Đá hành trình", (short) 127, () -> {
            try { p.getService().openDynamicMenu(978, "Đá hành trình", new String[]{"Kho hành trình", "Bản đồ hành trình", "Hướng dẫn"}, null); } catch (Exception e) { e.printStackTrace(); }
        }));
        menus.add(new model.Menu("Top Siêu trùm", (short) 101, () -> {
            try { boss.SuperBossManager.sendSuperBossMenu(p); } catch (Exception e) { e.printStackTrace(); }
        }));
        menus.add(new model.Menu("Mã quà tặng", (short) 135, () -> {
            p.sendInput("Quà tặng máy chủ", new String[]{"Nhập giftcode"}, name -> {
                try {
                    GiftTemplate.execute(p, name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }));
        if (p.admin == 1) {
            menus.add(new model.Menu("Bỏ qua nhiệm vụ", (short) 138, () -> {
                p.skipAllQuestsToEnd();
            }));
            menus.add(new model.Menu("Làm nhiệm vụ từ đầu", (short) 124, () -> {
                p.resetQuestToBegin();
            }));
        }
        p.getService().openDynamicMenu(type, MenuController.get_name_npc(type), menus);
    }

    @Override
    public void handleMenu(Player p, int index) throws IOException {
        short currentId = (short) p.currentNpcId;
        if (currentId == 978) {
            switch (index) {
                case 0: // Kho hành trình
                    activities.HanhTrinh.show_table(p, 1);
                    break;
                case 1: // Bản đồ hành trình
                    activities.HanhTrinh.show_table(p, 0);
                    break;
                case 2: { // Hướng dẫn
                    String txt = "HƯỚNG DẪN ĐÁ HÀNH TRÌNH\n\n"
                            + "- Đá Hành Trình là những viên đá cổ xưa ẩn chứa sức mạnh to lớn trên khắp các vùng biển.\b"
                            + "CÁCH THU THẬP:\n"
                            + "- Mỗi khi tiêu diệt Boss cuối tại mỗi bản đồ làng, bạn sẽ có 10% cơ hội nhận được Đá Hành Trình tương ứng.\n"
                            + "- Ngoài ra có thể tìm thấy tại các phó bản và rương kho báu đặc biệt.\b"
                            + "TÁC DỤNG & SỬ DỤNG:\n"
                            + "- Đem Đá Hành Trình kích hoạt tại Bản Đồ Hành Trình để tăng vĩnh viễn các chỉ số tiềm năng cho nhân vật.\n"
                            + "- Bạn có thể kiểm tra các viên đá đã thu thập được tại Kho Hành Trình.";
                    p.getService().Help_From_Server((short) p.currentNpcId, txt);
                    break;
                }
            }
            return;
        }

        if (p.menus != null && index >= 0 && index < p.menus.size()) {
            p.menus.get(index).execute(p, index);
        }
    }
}
