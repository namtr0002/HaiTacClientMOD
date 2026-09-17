package zinterfaces.menus.dymanics;

import model.Player;
import event.Event;
import event.EventManager;
import java.io.IOException;
import zinterfaces.iMenuDymanic;

/**
 * DynamicEventTopGift — Tự động thêm nút "Nhận Quà Đua Top" dưới NPC khi kết thúc đua top.
 */
public class DynamicEventTopGift implements iMenuDymanic {

    @Override
    public short[] getNpcId() {
        // Trưởng Làng IDs và NPC Hoạt Động (-78)
        return new short[]{-145, -122, -118, -103, -87, -74, -67, -45, -31, -21, -13, -1, -78};
    }

    @Override
    public String[] getMenuNames() {
        return new String[]{"Hướng Dẫn Đua Top Sự Kiện"};
    }

    @Override
    public Position getPosition() {
        return Position.BOTTOM; // Thêm vào dưới cùng danh sách
    }

    @Override
    public boolean shouldShow(Player p) {
        return !EventManager.gI().getActiveEventsList().isEmpty();
    }

    @Override
    public void handleSelect(Player p, short npcId, short menuId) throws IOException {
        p.menus.clear();
        for (Event ev : EventManager.gI().getActiveEventsList()) {
            if (ev.bxhSubTypes != null && ev.bxhSubTypes.length > 0) {
                for (int i = 0; i < ev.bxhSubTypes.length; i++) {
                    final int st = ev.bxhSubTypes[i];
                    final String bName = (ev.bxhNames != null && i < ev.bxhNames.length) ? ev.bxhNames[i] : ("Bảng Xếp Hạng " + (i + 1));
                    final Event currentEvent = ev;
                    p.menus.add(new model.Menu(bName, (short) 124, () -> {
                        try {
                            showEventTopGuide(p, currentEvent, st, bName);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }));
                }
            }
        }
        if (p.menus.isEmpty()) {
            p.getService().send_box_ThongBao_OK("Hiện tại không có sự kiện đua top nào đang diễn ra!");
            return;
        }
        p.getService().openDynamicMenu(npcId, "Hướng Dẫn Đua Top Sự Kiện", p.menus);
    }

    private static void showEventTopGuide(Player p, Event ev, int subType, String topName) throws IOException {
        if (p == null) return;
        zabstracts.AbsRanked bxh = zabstracts.AbsRanked.getBySubType(subType);
        StringBuilder sb = new StringBuilder();
        sb.append("--- THÔNG TIN ").append(topName.toUpperCase()).append(" ---\n\n");
        if (ev != null) {
            sb.append("• Tên Sự Kiện: ").append(ev.name).append(" (Mùa ").append(ev.getSeasonKey()).append(")\n");
            sb.append("• Thời Gian: ").append(ev.time != null && !ev.time.isEmpty() ? ev.time : "Đang cập nhật").append("\n");
            sb.append("• Trạng Thái: ").append(ev.isEventActive() ? "ĐANG DIỄN RA" : (ev.isEventEnded() ? "ĐÃ KẾT THÚC" : "CHƯA BẮT ĐẦU")).append("\n\n");
        }
        sb.append("• HÌNH THỨC NHẬN QUÀ: Khi sự kiện kết thúc, hệ thống sẽ TỰ ĐỘNG GỬI PHẦN THƯỞNG VÀO HỘP THƯ của người chơi đạt giải trong BXH, không cần nhận quà thủ công tại NPC.\n\n");
        if (bxh != null) {
            sb.append("• Giới hạn BXH: Top ").append(bxh.getMaxItems()).append("\n");
        }
        p.getService().send_box_ThongBao_OK(sb.toString());
    }
}
