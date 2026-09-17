package store;

import core.ZUtil;
import historys.zLog;
import model.Player;
import network.Message;
import template.ItemTemplate4;
import template.ItemTemplate4_Info;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Shop Đá Thần Thoại — type = 116 (NPC Johny).
 * Tự động tải từ ItemTemplate4 (SQL item4 & item4_info) cho các ID từ 647 đến 682.
 * Không cần file binary tĩnh.
 */
public class DaThanThoaiShop extends zabstracts.AbsShop {

    public static final short START_ID = 647;
    public static final short END_ID = 682;
    public static final int UPGRADE_RUBY_COST = 40;
    public static final int UPGRADE_MATERIAL_COUNT = 3;

    private volatile byte[] cachedShopPacket;

    public DaThanThoaiShop() {
        super(116, "Đá Thần Thoại");
    }

    /**
     * Xác định ID đá siêu cấp nguyên liệu tương ứng:
     * - ID 647..676: id - 406 (ID 241..270)
     * - ID 677..682: id - 309 (ID 368..373)
     */
    public static int getSourceStoneId(int mythicStoneId) {
        if (mythicStoneId >= 677 && mythicStoneId <= 682) {
            return mythicStoneId - 309;
        }
        return mythicStoneId - 406;
    }

    /**
     * Xây dựng nội dung mô tả vật phẩm và công thức nâng cấp từ dữ liệu SQL.
     */
    public static String buildDescription(ItemTemplate4 gem) {
        StringBuilder sb = new StringBuilder();
        ItemTemplate4_Info infoTemp = ItemTemplate4_Info.get_by_id(gem.indexInfoPotion);
        if (infoTemp != null && infoTemp.info != null && !infoTemp.info.isEmpty()) {
            String[] lines = infoTemp.info.split("\r?\n");
            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (!line.startsWith("-")) {
                    sb.append("- ").append(line).append("\n");
                } else {
                    sb.append(line).append("\n");
                }
            }
        }
        int sourceId = getSourceStoneId(gem.id);
        ItemTemplate4 sourceGem = ItemTemplate4.get_it_by_id(sourceId);
        String sourceName = (sourceGem != null) ? sourceGem.name : "Đá siêu cấp";
        sb.append(" + Bạn có muốn NÂNG CẤP ").append(gem.name)
          .append(" từ ").append(UPGRADE_MATERIAL_COUNT).append(" ").append(sourceName)
          .append(" + ").append(UPGRADE_RUBY_COST).append(" ruby không (tỉ lệ không xác định)?");
        return sb.toString();
    }

    /**
     * Xóa cache packet khi cần reload database.
     */
    public void invalidateCache() {
        this.cachedShopPacket = null;
    }

    private byte[] getShopData() throws IOException {
        if (cachedShopPacket == null) {
            synchronized (this) {
                if (cachedShopPacket == null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    DataOutputStream dos = new DataOutputStream(baos);
                    dos.writeByte(type);
                    dos.writeUTF(name);
                    dos.writeByte(11); // format 11 (MainItem format with custom info & upgrade confirmation)

                    List<ItemTemplate4> gems = new ArrayList<>();
                    for (short id = START_ID; id <= END_ID; id++) {
                        ItemTemplate4 gem = ItemTemplate4.get_it_by_id(id);
                        if (gem != null) {
                            gems.add(gem);
                        }
                    }

                    dos.writeShort(gems.size());
                    for (ItemTemplate4 gem : gems) {
                        dos.writeShort(gem.id);
                        dos.writeByte(4); // typeObject = 4 (Item4)
                        dos.writeUTF(gem.name);
                        dos.writeShort(gem.icon);
                        dos.writeUTF(buildDescription(gem));
                    }
                    dos.close();
                    cachedShopPacket = baos.toByteArray();
                }
            }
        }
        return cachedShopPacket;
    }

    @Override
    public void openUI(Player p) throws IOException {
        p.isShopSk = false;
        p.typeShop = type;
        byte[] data = getShopData();
        Message m = new Message(-19);
        m.writer().write(data);
        p.addmsg(m);
        m.cleanup();
    }

    @Override
    public void buy(Player p, byte cat, short id, int num) throws IOException {
        if (id < START_ID || id > END_ID) {
            p.getService().send_box_ThongBao_OK("Vật phẩm không tồn tại trong cửa hàng!");
            return;
        }

        ItemTemplate4 it4 = ItemTemplate4.get_it_by_id(id);
        if (it4 == null) {
            p.getService().send_box_ThongBao_OK("Có lỗi xảy ra, hãy báo cho admin!");
            return;
        }

        int sourceId = getSourceStoneId(id);
        ItemTemplate4 sourceIt = ItemTemplate4.get_it_by_id(sourceId);
        String sourceName = (sourceIt != null) ? sourceIt.name : "Đá siêu cấp";

        model.YesNoDialog ynd = new model.YesNoDialog(p, 116, "Auto Đổi Đá Thần Thoại",
            "Chọn số lần Auto Nâng/Đổi " + it4.name + "\n(Mỗi lần: " + UPGRADE_MATERIAL_COUNT + " " + sourceName + " + " + UPGRADE_RUBY_COST + " Ruby):",
            new String[]{"1 lần", "10 lần", "20 lần", "50 lần", "100 lần", "200 lần", "500 lần", "Nhập số lần", "Đóng"},
            new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, 1}, value -> {
                if (value == 7) {
                    new model.InputDialog(p, 116, "Nhập số lần", new String[]{"Số lần muốn nâng:"}, inputs -> {
                        if (inputs == null || inputs.length == 0 || inputs[0].trim().isEmpty()) return;
                        try {
                            int count = Integer.parseInt(inputs[0].trim());
                            if (count <= 0 || count > 500) {
                                p.getService().send_box_ThongBao_OK("Số lần không hợp lệ (tối đa 500 lần mỗi đợt)!");
                                return;
                            }
                            executeAutoCraftMythicStone(p, id, count);
                        } catch (NumberFormatException e) {
                            p.getService().send_box_ThongBao_OK("Vui lòng nhập số hợp lệ!");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).startInput();
                    return;
                }
                int count = switch (value) {
                    case 0 -> 1;
                    case 1 -> 10;
                    case 2 -> 20;
                    case 3 -> 50;
                    case 4 -> 100;
                    case 5 -> 200;
                    case 6 -> 500;
                    default -> 0;
                };
                if (count > 0) {
                    executeAutoCraftMythicStone(p, id, count);
                }
            });
        ynd.startYesNo();
    }

    public static void executeAutoCraftMythicStone(Player p, short id, int loopCount) {
        if (p == null || p.item == null || id < START_ID || id > END_ID) return;
        if (p.trade_target != null) {
            p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
            return;
        }
        if (loopCount <= 0) return;
        if (loopCount > 500) loopCount = 500;

        synchronized (p.item) {
            ItemTemplate4 it4 = ItemTemplate4.get_it_by_id(id);
            if (it4 == null) return;

            int sourceId = getSourceStoneId(id);
            ItemTemplate4 sourceIt = ItemTemplate4.get_it_by_id(sourceId);
            String sourceName = (sourceIt != null) ? sourceIt.name : "Đá siêu cấp";

            int successCount = 0;
            int failCount = 0;
            int totalRubySpent = 0;
            int totalSourceSpent = 0;
            String stopReason = "Đã hoàn thành " + loopCount + " lần nâng cấp";

            for (int step = 0; step < loopCount; step++) {
                if (p.item.total_item_bag_by_id(4, sourceId) < UPGRADE_MATERIAL_COUNT) {
                    stopReason = "Không đủ " + UPGRADE_MATERIAL_COUNT + " " + sourceName + "!";
                    break;
                }
                if (p.get_ngoc() < UPGRADE_RUBY_COST) {
                    stopReason = "Không đủ " + UPGRADE_RUBY_COST + " Ruby!";
                    break;
                }
                if (!p.item.can_add_item_bag47(4, id, 1)) {
                    stopReason = "Hành trang đầy!";
                    break;
                }

                p.update_ngoc(-UPGRADE_RUBY_COST);
                p.update_TieuRuby(UPGRADE_RUBY_COST);
                p.item.remove_item47(4, sourceId, UPGRADE_MATERIAL_COUNT);
                totalRubySpent += UPGRADE_RUBY_COST;
                totalSourceSpent += UPGRADE_MATERIAL_COUNT;

                boolean suc = (100 == ZUtil.random(220));
                if (suc) {
                    p.item.add_item_bag47(4, id, 1);
                    successCount++;
                    zLog.gI().add_log(p, "DA_THAN_THOAI", "Nâng cấp THÀNH CÔNG: " + it4.name + " (" + id + ") từ " + UPGRADE_MATERIAL_COUNT + " " + sourceName + " + " + UPGRADE_RUBY_COST + " ruby");
                } else {
                    failCount++;
                    zLog.gI().add_log(p, "DA_THAN_THOAI", "Nâng cấp THẤT BẠI: " + it4.name + " (" + id + ") từ " + UPGRADE_MATERIAL_COUNT + " " + sourceName + " + " + UPGRADE_RUBY_COST + " ruby");
                }
            }

            try {
                p.updateMoney();
                p.item.updateInventory(false);
                p.getService().send_box_ThongBao_OK(
                    "KẾT QUẢ AUTO NÂNG ĐÁ THẦN THOẠI:\n" +
                    "- Vật phẩm: " + it4.name + "\n" +
                    "- Thành công: " + successCount + " viên | Thất bại: " + failCount + " lần\n" +
                    "- Tiêu hao: " + totalSourceSpent + " " + sourceName + " | " + totalRubySpent + " Ruby\n" +
                    "- Trạng thái: " + stopReason
                );
            } catch (Exception ignored) {}
        }
    }
}
