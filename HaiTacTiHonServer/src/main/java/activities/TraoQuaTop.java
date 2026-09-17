package activities;

import model.Player;
import database.DbManager;
import network.Service;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import template.GiftBox;
import template.ItemTemplate4;

public class TraoQuaTop {

    public static void giveRewardToPlayer(Player p, int rank, String topType) throws IOException {
        if (p == null) {
            return;
        }
        int beriReceiv;
        int rubiReceiv;
        switch (rank) {
            case 0:
                beriReceiv = 5_000_000;
                rubiReceiv = 500;
                break;
            case 1:
                beriReceiv = 3_000_000;
                rubiReceiv = 300;
                break;
            case 2:
                beriReceiv = 1_500_000;
                rubiReceiv = 150;
                break;
            case 3: case 4: case 5: case 6: case 7: case 8: case 9:
                beriReceiv = 500_000;
                rubiReceiv = 50;
                break;
            default:
                p.getService().send_box_ThongBao_OK("Bạn không nằm trong top 10.");
                return;
        }
        List<GiftBox> listGift = new ArrayList<>();
        {
            GiftBox gb = new GiftBox();
            ItemTemplate4 it_temp4 = ItemTemplate4.get_it_by_id(0);
            gb.id = it_temp4.id;
            gb.type = 4;
            gb.name = it_temp4.name;
            gb.icon = it_temp4.icon;
            gb.num = beriReceiv;
            gb.color = 0;
            listGift.add(gb);
        }
        {
            GiftBox gb = new GiftBox();
            ItemTemplate4 it_temp4 = ItemTemplate4.get_it_by_id(1);
            gb.id = it_temp4.id;
            gb.type = 4;
            gb.name = it_temp4.name;
            gb.icon = it_temp4.icon;
            gb.num = rubiReceiv;
            gb.color = 0;
            listGift.add(gb);
        }
        core.RewardService.sendGiftOrMail(p, 1, "Quà " + topType, "Quà Top " + (rank + 1), listGift, true);
        logRewardClaim(p, topType);
    }

    public static void giveRewardToManualTop(Player p, int rank, String topType) throws IOException {
        if (p == null) {
            return;
        }

        List<GiftBox> listGift = new ArrayList<>();

        switch (topType) {
            case "TOP GHÉP HUY HIỆU":
                switch (rank) {
                    case 0:
                        listGift.add(createGiftBox(29, 4, 3));   // 3 Rương ác quỷ
                        listGift.add(createGiftBox(866, 4, 15)); // 15 Vé quay VIP
                        listGift.add(createGiftBox(106, 4, 2));  // 2 Rương vàng
                        listGift.add(createGiftBox(0, 4, 5_000_000));
                        listGift.add(createGiftBox(1, 4, 500));
                        p.update_coin(200_000);
                        break;
                    case 1:
                        listGift.add(createGiftBox(29, 4, 2));   // 2 Rương ác quỷ
                        listGift.add(createGiftBox(866, 4, 10)); // 10 Vé quay VIP
                        listGift.add(createGiftBox(801, 4, 15)); // 15 Vé quay thường
                        listGift.add(createGiftBox(0, 4, 3_000_000));
                        listGift.add(createGiftBox(1, 4, 300));
                        p.update_coin(100_000);
                        break;
                    case 2:
                        listGift.add(createGiftBox(29, 4, 1));   // 1 Rương ác quỷ
                        listGift.add(createGiftBox(866, 4, 5));  // 5 Vé quay VIP
                        listGift.add(createGiftBox(801, 4, 10)); // 10 Vé quay thường
                        listGift.add(createGiftBox(0, 4, 1_500_000));
                        listGift.add(createGiftBox(1, 4, 150));
                        p.update_coin(50_000);
                        break;
                    default:
                        listGift.add(createGiftBox(866, 4, 2));  // 2 Vé quay VIP
                        listGift.add(createGiftBox(801, 4, 5));  // 5 Vé quay thường
                        listGift.add(createGiftBox(0, 4, 500_000));
                        listGift.add(createGiftBox(1, 4, 50));
                        p.update_coin(20_000);
                        break;
                }
                break;
                
            case "TOP ĐỐT PHÁO":
                switch (rank) {
                    case 0:
                        listGift.add(createGiftBox(29, 4, 3));   // 3 Rương ác quỷ
                        listGift.add(createGiftBox(866, 4, 15)); // 15 Vé quay VIP
                        listGift.add(createGiftBox(801, 4, 30)); // 30 Vé quay thường
                        listGift.add(createGiftBox(0, 4, 5_000_000));
                        listGift.add(createGiftBox(1, 4, 500));
                        p.update_coin(200_000);
                        break;
                    case 1:
                        listGift.add(createGiftBox(29, 4, 2));   // 2 Rương ác quỷ
                        listGift.add(createGiftBox(866, 4, 10)); // 10 Vé quay VIP
                        listGift.add(createGiftBox(801, 4, 20)); // 20 Vé quay thường
                        listGift.add(createGiftBox(0, 4, 3_000_000));
                        listGift.add(createGiftBox(1, 4, 300));
                        p.update_coin(100_000);
                        break;
                    case 2:
                        listGift.add(createGiftBox(29, 4, 1));   // 1 Rương ác quỷ
                        listGift.add(createGiftBox(866, 4, 5));  // 5 Vé quay VIP
                        listGift.add(createGiftBox(801, 4, 10)); // 10 Vé quay thường
                        listGift.add(createGiftBox(0, 4, 1_500_000));
                        listGift.add(createGiftBox(1, 4, 150));
                        p.update_coin(50_000);
                        break;
                    default:
                        listGift.add(createGiftBox(866, 4, 2));  // 2 Vé quay VIP
                        listGift.add(createGiftBox(801, 4, 5));  // 5 Vé quay thường
                        listGift.add(createGiftBox(0, 4, 500_000));
                        listGift.add(createGiftBox(1, 4, 50));
                        p.update_coin(20_000);
                        break;
                }
                break;

            case "TOP NẠP":
            case "TOP_NAP":
                switch (rank) {
                    case 0:
                        listGift.add(createGiftBox(427, 4, 1));  // Trái bóng tối
                        listGift.add(createGiftBox(866, 4, 30)); // 30 Vé quay VIP
                        listGift.add(createGiftBox(0, 4, 10_000_000));
                        listGift.add(createGiftBox(1, 4, 1000));
                        p.update_coin(300_000);
                        break;
                    case 1:
                        listGift.add(createGiftBox(29, 4, 5));   // 5 Rương ác quỷ
                        listGift.add(createGiftBox(866, 4, 20)); // 20 Vé quay VIP
                        listGift.add(createGiftBox(801, 4, 30)); // 30 Vé quay thường
                        listGift.add(createGiftBox(0, 4, 6_000_000));
                        listGift.add(createGiftBox(1, 4, 600));
                        p.update_coin(200_000);
                        break;
                    case 2:
                        listGift.add(createGiftBox(29, 4, 3));   // 3 Rương ác quỷ
                        listGift.add(createGiftBox(866, 4, 10)); // 10 Vé quay VIP
                        listGift.add(createGiftBox(801, 4, 20)); // 20 Vé quay thường
                        listGift.add(createGiftBox(0, 4, 3_000_000));
                        listGift.add(createGiftBox(1, 4, 300));
                        p.update_coin(100_000);
                        break;
                    default:
                        listGift.add(createGiftBox(866, 4, 5));  // 5 Vé quay VIP
                        listGift.add(createGiftBox(801, 4, 10)); // 10 Vé quay thường
                        listGift.add(createGiftBox(0, 4, 1_000_000));
                        listGift.add(createGiftBox(1, 4, 100));
                        p.update_coin(50_000);
                        break;
                }
                break;
            default:
                p.getService().send_box_ThongBao_OK("Loại top không hợp lệ!");
                return;
        }
        core.RewardService.sendGiftOrMail(p, 1, "Quà " + topType, "Quà Top " + (rank + 1), listGift, true);
        p.updateMoney();
        logManualReward(p, topType);
    }

    private static GiftBox createGiftBox(int itemId, int type, int num) {
        GiftBox gb = new GiftBox();
        gb.id = (short) itemId;
        gb.type = (byte) type;
        gb.num = num;
        gb.color = 0;
        gb.name = "Vật phẩm " + itemId;
        gb.icon = 0;

        if (type == 7) {
            template.ItemTemplate7 t7 = template.ItemTemplate7.get_it_by_id(itemId);
            if (t7 != null) {
                gb.name = t7.name;
                gb.icon = t7.icon;
            }
        } else if (type == 3) {
            template.ItemTemplate3 t3 = template.ItemTemplate3.get_it_by_id(itemId);
            if (t3 != null) {
                gb.name = t3.name;
                gb.icon = t3.icon;
                gb.color = t3.color;
            }
        } else if (type == 8) {
            template.ItemTemplate8 t8 = template.ItemTemplate8.get_it_by_id(itemId);
            if (t8 != null) {
                gb.name = t8.name;
                gb.icon = t8.icon;
            }
        } else {
            ItemTemplate4 it_temp4 = ItemTemplate4.get_it_by_id(itemId);
            if (it_temp4 != null) {
                gb.name = it_temp4.name;
                gb.icon = it_temp4.icon;
            }
        }
        return gb;
    }

    public static boolean hasReceivedReward(Player p, String topType) {
        return historys.TopRewardHistory.hasReceivedReward(p, topType);
    }

    public static boolean hasReceivedReward(String playerName, LocalDateTime date, String topType) {
        return historys.TopRewardHistory.hasReceivedReward(playerName, date, topType);
    }

    public static boolean hasReceivedRewardManual(String playerName, LocalDateTime date, String topType) {
        return historys.TopRewardHistory.hasReceivedRewardManual(playerName, date, topType);
    }

    public static void logRewardClaim(Player p, String topType) {
        historys.TopRewardHistory.logRewardClaim(p, topType);
    }

    public static void logManualReward(Player p, String topType) {
        historys.TopRewardHistory.logManualReward(p, topType);
    }
}
