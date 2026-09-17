package zinterfaces.menus.npcs;

import model.Player;
import java.io.IOException;
import map.Npc;
import zinterfaces.iNpc;
import zinterfaces.iMenuDymanic;
import itemz.UpgradeItem;

public class NpcLaw implements iNpc {

    @Override
    public short[] getId() {
        return new short[]{-138};
    }

    @Override
    public String getChatText() {
        return "Ta là Bác sĩ tử thần Law!";
    }

    @Override
    public String[] getChatTexts() {
        return new String[]{
            "Ta là Bác sĩ tử thần Law!",
            "Chỉ có kẻ mạnh mới có quyền sống sót!",
            "Phẫu thuật tim máu sẽ tăng 10% HP tối đa của ngươi!",
            "Chuẩn bị đủ bột vàng và beri chưa?"
        };
    }

    @Override
    public void sendMenu(Player p, Npc npc) throws IOException {
        short type = npc != null ? npc.idmenu : -138;
        p.menus.clear();
        p.menus.add(new model.Menu("Phẫu thuật", (short) -1, () -> { try { handleMenu(p, 0); } catch (IOException e) {} }));
        p.menus.add(new model.Menu("Hướng dẫn", (short) -1, () -> { try { handleMenu(p, 1); } catch (IOException e) {} }));
        p.getService().openDynamicMenu(type, "Law", p.menus);
    }

    @Override
    public void handleMenu(Player p, int index) throws IOException {
        switch (index) {
            case 0: {
                if (p.item.it_heart == null) {
                    String text = "Để có thể tách tim được bạn sẽ mất %s bột vàng, %s đá ác quỷ, %s đá hải thạch cấp 1, %s.000.000 beri. Bạn thật sự muốn tách?";
                    p.setyesNoDialog(new model.YesNoDialog(p, 27, "Thông báo",
                            String.format(text,
                                    ((p.level < 40) ? 120 : 100),
                                    ((p.level < 40) ? 120 : 100),
                                    ((p.level < 40) ? 120 : 100),
                                    ((p.level < 40) ? 12 : 10)),
                            new String[]{"Đồng ý", "Hủy"},
                            new byte[]{-1, -1}));
                    p.getService().startYesNo();
                } else {
                    if (p.item.it_heart.levelUp > 109) {
                        p.getService().send_box_ThongBao_OK("Đã nâng cấp tối đa");
                    } else {
                        UpgradeItem.show_table_upgrade_heart(p);
                    }
                }
                break;
            }
            case 1: {
                String txt = "CHỨC NĂNG TÁCH TRÁI TIM\b"
                        + "Được mệnh danh là bác sĩ tử thần\nLaw chứa năng lực cực kỳ quái dị\n"
                        + "Được mệnh danh là bác sĩ tử thần\nLaw chứa năng lực cực kỳ quái dị\n"
                        + "Hãy chuẩn bị trước bột vàng, đá hải thạch, đá ác quỷ và beri để đến gặp Law và tiến hành tách trái tim\\b"
                        + "Sau khi tách trái tim sẽ tăng 10% HP cuối\\b"
                        + "Bạn có thể đến gặp Law để phẫu thuật, giúp tăng cấp cho quả tim máu được mạnh mẽ hơn";
                p.getService().Help_From_Server(-138, txt);
                break;
            }
        }
    }
}
