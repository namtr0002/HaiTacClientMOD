package zinterfaces.menus.npcs;

import activities.HelpDialog;
import model.Player;
import model.Menu;
import core.MenuController;
import zinterfaces.iNpc;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import map.Npc;

public class NpcHuongDan implements iNpc {

    @Override
    public short[] getId() {
        return new short[]{-997};
    }

    private void buildMenuOptions(Player p) {
        p.menus.clear();
        int mapId = p.map != null && p.map.template != null ? p.map.template.id : -1;

        switch (mapId) {
            case 0:
            case 1: {
                String[] options = new String[]{"Đăng ký tài khoản", "Nhiệm vụ tân thủ", "Vật phẩm", "Vận buôn", "Trang bị", "Kỹ năng"};
                for (int i = 0; i < options.length; i++) {
                    final byte bIndex = (byte) i;
                    p.menus.add(new Menu(options[i], () -> {
                        try {
                            HelpDialog.show_LangCoiXayGio(p, bIndex);
                        } catch (IOException ex) {
                            Logger.getLogger(NpcHuongDan.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }));
                }
                break;
            }
            case 9: {
                String[] options = new String[]{"Bảng xếp hạng", "Nhiệm vụ hàng ngày", "Cường hóa trang bị", "Khảm đá", "Chuyển hóa", "Săn trùm", "Phó bản liên tầng", "Phó bản PvP", "Khóa bảo vệ", "Nạp tiền"};
                for (int i = 0; i < options.length; i++) {
                    final byte bIndex = (byte) i;
                    p.menus.add(new Menu(options[i], () -> {
                        try {
                            HelpDialog.show_ThiTranVoSo(p, bIndex);
                        } catch (IOException ex) {
                            Logger.getLogger(NpcHuongDan.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }));
                }
                break;
            }
            case 17: {
                String[] options = new String[]{"Chợ mua bán", "Vòng quay kho báu", "Hoàn mỹ", "Kích ẩn", "Thuộc tính kích ẩn 1-4", "Thuộc tính kích ẩn 5-8", "Thuộc tính kích ẩn 9-13"};
                for (int i = 0; i < options.length; i++) {
                    final byte bIndex = (byte) i;
                    p.menus.add(new Menu(options[i], () -> {
                        try {
                            HelpDialog.show_ThiTranOrange(p, bIndex);
                        } catch (IOException ex) {
                            Logger.getLogger(NpcHuongDan.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }));
                }
                break;
            }
            case 25: {
                p.menus.add(new Menu("Vượt Redline", () -> {
                    try {
                        HelpDialog.show_LangSiRup(p, (byte) 0);
                    } catch (IOException ex) {
                        Logger.getLogger(NpcHuongDan.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }));
                break;
            }
            case 33: {
                String[] options = new String[]{"Băng hải tặc", "Phó bản băng", "Phó bản khổng lồ", "Bảo vệ pháo đài"};
                for (int i = 0; i < options.length; i++) {
                    final byte bIndex = (byte) i;
                    p.menus.add(new Menu(options[i], () -> {
                        try {
                            HelpDialog.show_ThuyenBarati(p, bIndex);
                        } catch (IOException ex) {
                            Logger.getLogger(NpcHuongDan.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }));
                }
                break;
            }
            case 41: {
                String[] options = new String[]{"Bảo vệ kho báu Namie", "Siêu boss"};
                for (int i = 0; i < options.length; i++) {
                    final byte bIndex = (byte) i;
                    p.menus.add(new Menu(options[i], () -> {
                        try {
                            HelpDialog.show_LangHatDe(p, bIndex);
                        } catch (IOException ex) {
                            Logger.getLogger(NpcHuongDan.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }));
                }
                break;
            }
            case 49: {
                String[] options = new String[]{"Lệnh truy nã", "Siêu boss"};
                for (int i = 0; i < options.length; i++) {
                    final byte bIndex = (byte) i;
                    p.menus.add(new Menu(options[i], () -> {
                        try {
                            HelpDialog.show_ThiTranKhoiDau(p, bIndex);
                        } catch (IOException ex) {
                            Logger.getLogger(NpcHuongDan.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }));
                }
                break;
            }
            case 66: {
                p.menus.add(new Menu("Vượt Redline", () -> {
                    try {
                        HelpDialog.show_MomSinhDoi(p, (byte) 0);
                    } catch (IOException ex) {
                        Logger.getLogger(NpcHuongDan.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }));
                break;
            }
            case 69: {
                String[] options = new String[]{"Trái ác quỷ", "Đấu trường tự do", "Siêu boss"};
                for (int i = 0; i < options.length; i++) {
                    final byte bIndex = (byte) i;
                    p.menus.add(new Menu(options[i], () -> {
                        try {
                            HelpDialog.show_ThiTranWhiskey(p, bIndex);
                        } catch (IOException ex) {
                            Logger.getLogger(NpcHuongDan.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }));
                }
                break;
            }
            case 79: {
                String[] options = new String[]{"Đá đít Mr.3", "Phó bản khổng lồ"};
                for (int i = 0; i < options.length; i++) {
                    final byte bIndex = (byte) i;
                    p.menus.add(new Menu(options[i], () -> {
                        try {
                            HelpDialog.show_DaoLittleGrand(p, bIndex);
                        } catch (IOException ex) {
                            Logger.getLogger(NpcHuongDan.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }));
                }
                break;
            }
            default: {
                p.menus.add(new Menu("Chưa có", () -> {}));
                break;
            }
        }
    }

    private String[] getMapOptions(int mapId) {
        switch (mapId) {
            case 0:
            case 1:
                return new String[]{"Đăng ký tài khoản", "Nhiệm vụ tân thủ", "Vật phẩm", "Vận buôn", "Trang bị", "Kỹ năng", "Lính Đánh Thuê", "Tổng quan tính năng"};
            case 9:
                return new String[]{"Bảng xếp hạng", "Nhiệm vụ hàng ngày", "Cường hóa trang bị", "Khảm đá", "Chuyển hóa", "Săn trùm", "Phó bản liên tầng", "Phó bản PvP", "Khóa bảo vệ", "Nạp tiền", "Lính Đánh Thuê"};
            case 17:
                return new String[]{"Chợ mua bán", "Vòng quay kho báu", "Hoàn mỹ", "Kích ẩn", "Thuộc tính kích ẩn 1-4", "Thuộc tính kích ẩn 5-8", "Thuộc tính kích ẩn 9-13", "Lính Đánh Thuê"};
            case 25:
                return new String[]{"Cường hóa ác quỷ", "Lính Đánh Thuê", "Tổng quan tính năng"};
            case 33:
                return new String[]{"Băng hải tặc", "Phó bản băng", "Phó bản khổng lồ", "Bảo vệ pháo đài", "Lính Đánh Thuê"};
            case 41:
                return new String[]{"Bảo vệ kho báu Namie", "Siêu boss", "Lính Đánh Thuê"};
            case 49:
                return new String[]{"Lệnh truy nã", "Siêu boss", "Lính Đánh Thuê", "Tổng quan tính năng"};
            case 66:
                return new String[]{"Vượt Redline", "Lính Đánh Thuê", "Tổng quan tính năng"};
            case 69:
                return new String[]{"Trái ác quỷ", "Đấu trường tự do", "Siêu boss", "Lính Đánh Thuê"};
            case 79:
                return new String[]{"Phó bản Mr.3", "Phó bản khổng lồ", "Lính Đánh Thuê"};
            case 88:
                return new String[]{"Vương quốc Drum", "Boss Wapol", "Lính Đánh Thuê"};
            case 98:
            case 105:
            case 114:
                return new String[]{"Vương quốc Alabasta", "Đại trùm Crocodile", "Lính Đánh Thuê"};
            case 135:
            case 145:
                return new String[]{"Đảo Trên Trời Skypiea", "Chúa Tể Enel", "Lính Đánh Thuê"};
            case 155:
                return new String[]{"Thành phố Water 7", "Tổ chức CP9", "Lính Đánh Thuê"};
            case 185:
                return new String[]{"Quần đảo Sabaody", "Đấu trường Tân Tinh", "Lính Đánh Thuê"};
            default:
                return new String[]{"Hướng dẫn Lính Đánh Thuê", "Hệ thống Cường hóa & Khảm đá", "Hoàn Mỹ & Kích Ẩn", "Hệ thống Băng Hải Tặc", "Tổng quan tính năng"};
        }
    }

    @Override
    public void sendMenu(Player p, Npc npc) throws IOException {
        short type = npc != null ? npc.idmenu : -997;
        int mapId = p.map != null && p.map.template != null ? p.map.template.id : -1;
        String[] options = getMapOptions(mapId);
        p.menus.clear();
        for (int i = 0; i < options.length; i++) {
            final int idx = i;
            p.menus.add(new model.Menu(options[i], (short) -1, () -> { try { handleMenu(p, idx); } catch (IOException e) {} }));
        }
        p.getService().openDynamicMenu(type, "Hướng dẫn", p.menus);
    }

    @Override
    public void handleMenu(Player p, int index) throws IOException {
        int mapId = p.map != null && p.map.template != null ? p.map.template.id : -1;
        byte bIndex = (byte) index;
        switch (mapId) {
            case 0:
            case 1:
                if (bIndex <= 5) HelpDialog.show_LangCoiXayGio(p, bIndex);
                else if (bIndex == 6) HelpDialog.show_LinhDanhThue(p, (byte) 0);
                else HelpDialog.show_TongQuanTinhNang(p, (byte) 0);
                break;
            case 9:
                if (bIndex <= 9) HelpDialog.show_ThiTranVoSo(p, bIndex);
                else HelpDialog.show_LinhDanhThue(p, (byte) 0);
                break;
            case 17:
                if (bIndex <= 6) HelpDialog.show_ThiTranOrange(p, bIndex);
                else HelpDialog.show_LinhDanhThue(p, (byte) 0);
                break;
            case 25:
                if (bIndex == 0) HelpDialog.show_LangSiRup(p, (byte) 0);
                else if (bIndex == 1) HelpDialog.show_LinhDanhThue(p, (byte) 0);
                else HelpDialog.show_TongQuanTinhNang(p, (byte) 0);
                break;
            case 33:
                if (bIndex <= 3) HelpDialog.show_ThuyenBarati(p, bIndex);
                else HelpDialog.show_LinhDanhThue(p, (byte) 0);
                break;
            case 41:
                if (bIndex <= 1) HelpDialog.show_LangHatDe(p, bIndex);
                else HelpDialog.show_LinhDanhThue(p, (byte) 0);
                break;
            case 49:
                if (bIndex <= 1) HelpDialog.show_ThiTranKhoiDau(p, bIndex);
                else if (bIndex == 2) HelpDialog.show_LinhDanhThue(p, (byte) 0);
                else HelpDialog.show_TongQuanTinhNang(p, (byte) 0);
                break;
            case 66:
                if (bIndex == 0) HelpDialog.show_MomSinhDoi(p, (byte) 0);
                else if (bIndex == 1) HelpDialog.show_LinhDanhThue(p, (byte) 0);
                else HelpDialog.show_TongQuanTinhNang(p, (byte) 0);
                break;
            case 69:
                if (bIndex <= 2) HelpDialog.show_ThiTranWhiskey(p, bIndex);
                else HelpDialog.show_LinhDanhThue(p, (byte) 0);
                break;
            case 79:
                if (bIndex <= 1) HelpDialog.show_DaoLittleGrand(p, bIndex);
                else HelpDialog.show_LinhDanhThue(p, (byte) 0);
                break;
            case 88:
                if (bIndex <= 1) HelpDialog.show_DaoDrum(p, bIndex);
                else HelpDialog.show_LinhDanhThue(p, (byte) 0);
                break;
            case 98:
            case 105:
            case 114:
                if (bIndex <= 1) HelpDialog.show_Alabasta(p, bIndex);
                else HelpDialog.show_LinhDanhThue(p, (byte) 0);
                break;
            case 135:
            case 145:
                if (bIndex <= 1) HelpDialog.show_DaoTrenTroi(p, bIndex);
                else HelpDialog.show_LinhDanhThue(p, (byte) 0);
                break;
            case 155:
                if (bIndex <= 1) HelpDialog.show_Water7(p, bIndex);
                else HelpDialog.show_LinhDanhThue(p, (byte) 0);
                break;
            case 185:
                if (bIndex <= 1) HelpDialog.show_Sabaody(p, bIndex);
                else HelpDialog.show_LinhDanhThue(p, (byte) 0);
                break;
            default:
                if (bIndex == 0) HelpDialog.show_LinhDanhThue(p, (byte) 0);
                else if (bIndex == 1) HelpDialog.show_ThiTranVoSo(p, (byte) 2); // Cường hóa & Khảm đá
                else if (bIndex == 2) HelpDialog.show_ThiTranOrange(p, (byte) 2); // Hoàn mỹ & Kích ẩn
                else if (bIndex == 3) HelpDialog.show_ThuyenBarati(p, (byte) 0); // Băng hải tặc
                else HelpDialog.show_TongQuanTinhNang(p, (byte) 0);
                break;
        }
    }
}
