package core;



import model.MyPet;

import model.Player;

import event.EventData;

import historys.zLog;

import skill.Skill_info;

import skill.Skill_Template;

import network.Service;



import model.TichTieuRuby;

import model.ListTichNap;

import itemz.Rebuild_Item;

import clan.ClanMember;

import clan.ClanChat;

import clan.ClanHanhTrinhIcon;

import clan.Clan;

import model.DauGia;

import boss.SuperBossManager;

import zabstracts.AbsRanked;

import rank.Ranked;

import itemz.Split_Item;

import itemz.UpgradeSuperItem;

import itemz.UpgradeItem;

import itemz.UpgradeDial;

import itemz.Join_Item;

import map.zones.WorldWar;

import map.zones.ThuLinhBienKhoi;

import network.MessageHandler;

import event.SuKienHalloween;

import event.eboss.BiNgoMa;



import java.io.IOException;

import java.util.ArrayList;

import java.util.Comparator;

import java.util.HashMap;

import java.util.List;



import event.SuKienNoel;

import event.eboss.SantaNoel;

import event.eboss.QuaiVatTuyetNoel;

import org.joda.time.LocalTime;

import activities.*;

import map.zones.BaoVePhaoDai;

import map.zones.DauTruongTuDo;

import achievement.ArchiDaily;

import database.IDManager;

import database.DbManager;

import event.SuKienGioTo;

import event.EventManager;

import event.SuKienTrongCay;

import network.Message;

import network.Session;

import network.SessionManager;

import java.sql.Connection;

import java.sql.PreparedStatement;

import java.sql.SQLException;

import java.time.LocalDateTime;

import java.util.HashSet;

import java.util.Set;

import java.util.logging.Logger;

import map.*;

import org.json.simple.JSONObject;

import template.*;

import model.VongQuay;

import model.VongQuayOcSen;

import model.YesNoDialog;



public class MenuController {



    public static final boolean KM50 = false;

    public static int[] ID_MAP_LANG

            = new int[]{1, 9, 17, 25, 33, 41, 49, 66, 69, 79, 83, 93, 107, 113, 191};



    public static void send_menu(Player p, Message m) throws IOException {
        if (p != null && !p.isdie && p.map != null && p.map.template != null) {
            short type = m.reader().readShort();

            // System.out.println("npc id " + type);



            // Reset sạch sẽ trạng thái menu cũ khi người chơi mở tương tác với NPC mới

            if (p.menus != null) {

                p.menus.clear();

            }

            p.currentMenuOptions = null;

            p.currentNpcId = type;

            p.currentMenuNpcId = type;



            Npc npcTarget = null;
            boolean in_map = false;
            for (int i = 0; i < p.map.template.npcs.size(); i++) {
                if (type == p.map.template.npcs.get(i).idmenu) {
                    npcTarget = p.map.template.npcs.get(i);
                    in_map = true;
                    break;
                }
            }

            // Chặn exploit tương tác NPC từ xa đối với NPC map thực tế (type >= 0)
            if (type >= 0) {
                if (!in_map || npcTarget == null) {
                    return;
                }
                int dx = Math.abs(p.x - npcTarget.x);
                int dy = Math.abs(p.y - npcTarget.y);
                if (dx > 400 || dy > 400) {
                    return;
                }
            }

            if (EventManager.dispatchSendMenu(p, type)) {
                return;
            }



            zinterfaces.iNpc handler = (in_map && npcTarget != null && npcTarget.getHandler() != null) ? npcTarget.getHandler() : zinterfaces.iNpc.get(type);

            if (handler != null) {

                p.currentNpcId = type; // track để handleMenu dispatch đúng

                p.currentMenuNpcId = type;

                handler.sendMenu(p, npcTarget);

                return;

            }





            switch (type) {

                case -9999: {

                    ItemFashionP.show_table(p, 105);

                    break;

                }

                // DELEGATED to NpcKookoroo

                // case -99: {

                //     sendDymanicMenu(p, type, get_name_npc(type), new String[]{"Quán ăn", "Rời khỏi đây"},

                //             new short[]{104, 157});

                //     break;

                // }

                case -98: {

                    List<model.Menu> menus = new ArrayList<>();

                    menus.add(new model.Menu("Thông tin", () -> {

                        try {

                            if (p.map.template.id == 994) {

                                p.getService().send_box_ThongBao_OK("Bảng thông tin Trận Chiến Bang Hội");

                            } else {

                                p.getService().send_box_ThongBao_OK("Bảng thông tin phòng chờ PvP Bang");

                            }

                        } catch (Exception e) {

                            e.printStackTrace();

                        }

                    }));

                    if (p.isSpectator) {

                        menus.add(new model.Menu("Thoát chế độ xem", () -> {

                            try {

                                p.exitSpectatorMode();

                                p.getService().send_box_ThongBao_OK("Đã thoát Chế Độ Xem và trở về vị trí cũ!");

                            } catch (Exception e) {

                                e.printStackTrace();

                            }

                        }));

                    } else if (p.map.template.id == 260) {

                        menus.add(new model.Menu("Xem thi đấu PvP Bang", () -> {

                            try {

                                map.Zone[] zones994 = map.Zone.getMapByID(994);

                                if (zones994 != null && zones994.length > 0 && zones994[0] != null) {

                                    p.enterSpectatorMode(zones994[0], (short) (zones994[0].template.maxW / 2), (short) (zones994[0].template.maxH / 2));

                                    p.getService().send_box_ThongBao_OK("BAN DA VAO XEM THI DAU PVP BANG HOI!\n- Ban dang trong trang thai quan sat khong bi nhan sat thuong.\n- Chon menu de Thoat che do xem tro ve vi tri cu.");

                                } else {

                                    p.getService().send_box_ThongBao_OK("Chưa có trận PvP Bang nào đang diễn ra!");

                                }

                            } catch (Exception e) {

                                e.printStackTrace();

                            }

                        }));

                    }

                    p.getService().openMenu(type, "", menus);

                    break;

                }

                // DELEGATED to NpcWipper

                // case -140: {

                //     sendDymanicMenu(p, type, "WIPPER", new String[]{"Chế tạo DIAL", "Thử thách vệ thần"},

                //             null);

                //     break;

                // }

                // DELEGATED to NpcDungeon

                // case -86: {

                //     sendDymanicMenu(p, type, "Phó bản",

                //             new String[]{"Đá đít Mr3", "Phó bản khổng lồ", "Hướng dẫn Phó bản khổng lồ"},

                //             new short[]{150, 142, 148});

                //     break;

                // }



                // DELEGATED to NpcCroket

                // case -73: {

                //     sendDymanicMenu(p, type, "Croket", new String[]{"Hướng dẫn"}, null);

                //     break;

                // }

                // DELEGATED to NpcClan
                // case -84: {
                // }
                case -991: {
                    zinterfaces.menus.SubMenuWorldWar.openWorldWarMenu(p);
                    break;
                }
                case -106:
                case -91:
                case -71:
                case -48: {
                    switch (p.map.template.id) {
                        case 9: {
                            sendDymanicMenu(p, type, "Zosaku", new String[]{"Săn trùm", "Thách đấu",
                                "Vượt liên ải", "Trận chiến lớn", "Vượt ải đơn"},
                                    new short[]{136, 137, 138, 146, 111});
                            break;
                        }
                        case 191:

                        case 189:

                        case 113:

                        case 93:

                        case 69:

                        case 33:

                        case 17: {

                            sendDymanicMenu(p, type, "Zosaku",

                                    new String[]{"Săn trùm", "Thách đấu", "Vượt siêu liên ải", "Trận chiến lớn"},

                                    new short[]{136, 137, 138, 146});

                            break;

                        }

                        case 25: {

                            sendDymanicMenu(p, type, "Zosaku", new String[]{"Săn trùm", "Thách đấu",

                                "Vượt liên ải", "Trận chiến lớn", "Vượt ải đơn"},

                                    new short[]{136, 137, 138, 146, 111});

                            break;

                        }

                        case 41: {

                            sendDymanicMenu(

                                    p, type, "Zosaku", new String[]{"Săn trùm", "Thách đấu", "Vượt liên ải",

                                        "Trận chiến lớn", "Bảo vệ kho báu Namie"},

                                    new short[]{136, 137, 138, 146, 139});

                            break;

                        }

                        case 49: {

                            sendDymanicMenu(p, type, "Zosaku", new String[]{"Săn trùm", "Thách đấu",

                                "Vượt liên ải", "Trận chiến lớn", "Lệnh truy nã"},

                                    new short[]{136, 137, 138, 146, 160});

                            break;

                        }

                        case 83: {

                            sendDymanicMenu(p, type, "Zosaku",

                                    new String[]{"Săn trùm", "Thách đấu", "Vượt siêu liên ải", "Trận chiến lớn"},

                                    new short[]{136, 137, 159, 146});

                            break;

                        }

                    }

                    break;

                }





                case -997: {

                    switch (p.map.template.id) {

                        case 1: { // lang coi xay gio

                            sendDymanicMenu(p, type, "Hướng dẫn", new String[]{"Đăng ký tài khoản",

                                "Nhiệm vụ tân thủ", "Vật phẩm", "Vận buôn", "Trang bị", "Kỹ năng"}, null);

                            break;

                        }

                        case 9: { // thi tran vo so

                            sendDymanicMenu(p, type, "Hướng dẫn",

                                    new String[]{"Bảng xếp hạng", "Nhiệm vụ hàng ngày", "Cường hóa trang bị",

                                        "Khảm đá", "Chuyển hóa", "Săn trùm", "Phó bản liên tầng", "Phó bản PvP",

                                        "Khóa bảo vệ", "Nạp tiền"},

                                    null);

                            break;

                        }

                        case 17: { // thi tran orange

                            sendDymanicMenu(p, type, "Hướng dẫn",

                                    new String[]{"Chợ mua bán", "Ṿng xoay kho báu", "Hoàn mỹ", "Kích ẩn",

                                        "Thuộc tính kích ẩn (1-4)", "Thuộc tính kích ẩn (5-8)",

                                        "Thuộc tính kích ẩn (9-13)"},

                                    null);

                            break;

                        }

                        case 25: { // sirup

                            sendDymanicMenu(p, type, "Hướng dẫn", new String[]{"Vượt Redline"}, null);

                            break;

                        }

                        case 33: { // barati

                            sendDymanicMenu(p, type, "Hướng dẫn", new String[]{"Băng hải tặc", "Phó bản băng",

                                "Phó bản khổng lồ", "Bảo vệ pháo đài"}, null);

                            break;

                        }

                        case 41: { // hat de

                            sendDymanicMenu(p, type, "Hướng dẫn",

                                    new String[]{"Bảo vệ kho báu Namie", "Siêu boss"}, null);

                            break;

                        }

                        case 49: { // khoi dau

                            sendDymanicMenu(p, type, "Hướng dẫn", new String[]{"Lệnh truy nã", "Siêu boss"},

                                    null);

                            break;

                        }

                        case 66: { // mom sinh doi

                            sendDymanicMenu(p, type, "Hướng dẫn", new String[]{"Vượt Redline"}, null);

                            break;

                        }

                        case 69: { // whiskey

                            sendDymanicMenu(p, type, "Hướng dẫn",

                                    new String[]{"Trái ác quỷ", "Đấu trường tự do", "Siêu boss"}, null);

                            break;

                        }

                        case 79: { // little grand

                            sendDymanicMenu(p, type, "Hướng dẫn",

                                    new String[]{"Đá đít Mr.3", "Phó bản khổng lồ"}, null);

                            break;

                        }

                    }

                    break;

                }



                // DELEGATED to NpcVongQuay

                // case -967:

                //     sendDymanicMenu(

                //             p, -967, get_name_npc(type), new String[]{"Vòng Quay Thuường", "Vòng Quay Vip"},

                //             null);

                //     break;



              

                // DELEGATED to NpcGarp

                // case -37: {

                //     sendDymanicMenu(p, type, "Gap", new String[]{"Học Skill", "Tẩy tiềm năng",

                //         "Xóa nội tại", "Người giới thiệu", "Đá hành trình"},

                //             new short[]{123, 124, 125, 138, 127});

                //     break;

                // }

                // case -4: {

                //     if (p.huongnghiep <= 0) {

                //         sendDymanicMenu(p, type, "Gap", new String[]{"Học Skill", "Tẩy tiềm năng",

                //             "Xóa nội tại", "Người giới thiệu", "Thông thạo", "Chọn Phe","Sư Đồ"},

                //                 new short[]{123, 124, 125, 138, 138, 158, 145, 145});

                //     } else {

                //         sendDymanicMenu(p, type, "Gap",

                //                 new String[]{"Học Skill", "Tẩy tiềm năng", "Xóa nội tại", "Người giới thiệu", "Thông thạo","Sư Đồ"},

                //                 new short[]{123, 124, 125, 138, 158, 145});

                //     }

                //     break;

                // }

                // case -144: // kinh do nuoc

                case -153:

                case -152:

                case -151:

                case -150:

                case -149:

                case -148: {

                    Show_List_Map_Tele(p, 0, -144);

                    break;

                }

                // case -124: // thi tran thien su

                case -131:

                case -130:

                case -129:

                case -128:

                case -127:

                case -126:

                case -125:

                case -123: {

                    Show_List_Map_Tele(p, 0, -124);

                    break;

                }

                case -115:

                case -114:

                case -113:

                case -112:

                case -111:

                case -110:

                case -109:

                case -108: {

                    Show_List_Map_Tele(p, 0, -107);

                    break;

                }

                case -96:

                case -94:

                case -93:

                case -92: {

                    Show_List_Map_Tele(p, 0, -85);

                    break;

                }

                case -83:

                case -81:

                case -80:

                case -79: {

                    Show_List_Map_Tele(p, 0, 0);

                    break;

                }

                case -59:

                case -63:

                case -62:

                case -61: {

                    Show_List_Map_Tele(p, 0, -60);

                    break;

                }

                case -58:

                case -51:

                case -50:

                case -49: {

                    Show_List_Map_Tele(p, 0, -44);

                    break;

                }

                case -57:

                case -42:

                case -41:

                case -40: {

                    Show_List_Map_Tele(p, 0, -36);

                    break;

                }

                case -56:

                case -34:

                case -33:

                case -32: {

                    Show_List_Map_Tele(p, 0, -28);

                    break;

                }

                case -55:

                case -26:

                case -25:

                case -24: {

                    Show_List_Map_Tele(p, 0, -20);

                    break;

                }

                case -54:

                case -18:

                case -17:

                case -16: {

                    Show_List_Map_Tele(p, 0, -12);

                    break;

                }

                case -53:

                case -10:

                case -9:

                case -8: {

                    // send_dynamic_menu(p, type, "Nhiệm vụ", new String[] {"Nhiệm vụ chính", "Nhiệm\n                    // vụ lặp"}, null);

                    Show_List_Map_Tele(p, 0, -5);

                    break;

                }

                case -6: {

                    p.getService().Send_UI_Shop(99);

                    break;

                }

//                case -145:

//                case -122:

//                case -118:

//                case -103:

//                case -87:

//                case -74:

//                case -67:

//                case -45:

//                case -31:

//                case -21:

//                case -13:

//                case -1: {

//                    send_dynamic_menu(p, type, get_name_npc(type), new String[]{"Thách đấu", "Cao thủ",

//                        "Băng hải tặc", "Truy nã", "Đá hành trình", "Top Siêu trùm", "Mã quà tặng"}, 

//                        new short[]{101, 101, 101, 101, 127, 101, 135});

//                    break;

//                }

                // DELEGATED to NpcKhoBau

                // case -133: {

                //     sendDymanicMenu(p, type, "Kho BÌÁu",

                //             new String[]{"Ṿng quay kho báu", "Hoàn mỹ - Kích ẩn", "Vòng quay ốc sên"}, null);

                //     break;

                // }

                // DELEGATED to NpcJohny

                // case -105:

                // case -90:

                // case -70:

                // case -47: {

                //     if (p.map.template.id == 25) { // cuong hoa ac quy

                //         sendDymanicMenu(p, type, "Johny",

                //                 new String[]{"Cường Hóa", "Khảm đá", "Chuyển hóa", "Ghép mảnh trang bị",

                //                     "Ghép mảnh trang bị", "Cường hóa thời trang"},

                //                 new short[]{126, 127, 128, 126, 126, 154});

                //     } else {

                //         sendDymanicMenu(

                //                 p, type, "Johny", new String[]{"Cường Hóa", "Khảm đá", "Chuyển hóa",

                //                     "Ghép mảnh trang bị", "Cường hóa thời trang"},

                //                 new short[]{126, 127, 128, 126, 126});

                //     }

                //     break;

                // }

                // DELEGATED to NpcKookoroo

                // case -147:

                // case -120:

                // case -116:

                // case -102:

                // case -89:

                // case -76:

                // case -68:

                // case -46:

                // case -39:

                // case -29:

                // case -22:

                // case -14:

                // case -2: {

                //     sendDymanicMenu(

                //             p, type, get_name_npc(type), new String[]{"Quán ăn", "Vận Chuyển Hàng", "Tiệm tóc",

                //         "Đóng thuyền", "Thời trang", "Thẩm mỹ viện"},

                //             new short[]{104, 107, 106, 105, 108, 158});

                //     break;

                // }

                // DELEGATED to NpcTeleport

                // case -82: // mom sinh doi

                // case -60: // thi tran khoi dau

                // case -44: // lang hat de

                // case -36: // nha hang barati

                // case -28: // lang sirup

                // case -20: // thi tran orang

                // case -12: // thi tran vo so

                // case -5: { // lang coi xay gio

                //     sendDymanicMenu(p, type, "", new String[]{"Trong lÌÊng", "Thế giới"});

                //     break;

                // }

                case -7: {

                    Menu_Change_Zone(p);

                    break;

                }

                // DELEGATED to NpcPaule

                // case -146:

                // case -121:

                // case -117:

                // case -101:

                // case -88:

                // case -75:

                // case -69:

                // case -38:

                // case -30:

                // case -23:

                // case -15:

                // case -3: {

                //     if (p.detu != null) {

                //         sendDymanicMenu(p, type, get_name_npc(type),

                //                 new String[]{Clazz.NAME[p.clazz - 1], "Hệ khác",

                //                     (!p.is_show_hat ? "Bật hiển thị nón" : "Tắt hiển thị nón"), "Khóa bảo vệ",

                //                     "Thùng rác", "Đệ tử"},

                //                 new short[]{Clazz.ICON[p.clazz - 1], 116, 117, 118, 113, 125});

                //     } else {

                //         sendDymanicMenu(p, type, get_name_npc(type),

                //                 new String[]{Clazz.NAME[p.clazz - 1], "Hệ khác",

                //                     (!p.is_show_hat ? "Bật hiển thị nón" : "Tắt hiển thị nón"), "Khóa bảo vệ",

                //                     "Thùng rác"},

                //                 new short[]{Clazz.ICON[p.clazz - 1], 116, 117, 118, 113});

                //         break;

                //     }

                // }

                case -119: {

                    break;

                }



                default: {

                    List<model.Menu> menus = new ArrayList<>();

                    menus.add(new model.Menu("Chưa có", (short) 117, () -> {

                        try {

                            p.getService().send_box_ThongBao_OK("Chức năng chưa có!");

                        } catch (Exception e) {

                            e.printStackTrace();

                        }

                    }));

                    p.getService().openMenu(type, (get_name_npc(type) + " id " + type), menus);

                    break;

                }

            }

        }

    }



    public static String get_name_npc(int type) {

        switch (type) {

            case -145:

                return "Icebug";

            case -122:

                return "Gan";

            case -118:

                return "Cricket";

            case -103:

                return "Cobran";

            case -87:

                return "Daltont";

            case -74:

                return "Mr Opera";

            case -67:

                return "Mastersun";

            case -45:

                return "Genzo";

            case -31:

                return "Băng hải tặc nhí";

            case -1:

                return "Trưởng làng";

            case -146:

                return "Paule";

            case -147:

                return "kookoroo";

            case -120:

                return "Conic";

            case -121:

                return "Pagada";

            case -117:

                return "Spect";

            case -116:

                return "Terri";

            case -101:

                return "Kohzak";

            case -102:

                return "Yoshi moto";

            case -89:

                return "Dr Kure";

            case -88:

                return "Stook";

            case -75:

                return "Ms Vivi";

            case -76:

                return "Mr Acrobatic";

            case -68:

                return "Sapie";

            case -46:

                return "Noziko";

            case -39:

                return "Cami";

            case -29:

                return "Kaiya";

            case -21:

                return "Thị Trưởng";

            case -13:

                return "Cobi";

            case -69:

                return "Masu";

            case -3:

                return "Guru";

            case -15:

                return "Mẹ Rita";

            case -23:

                return "Poroy";

            case -30:

                return "Merri";

            case -38:

                return "Partty";

            case -2:

                return "Machiko";

            case -14:

                return "Rita";

            case -22:

                return "Cho Cho";

            case -877:

                return "Chị Hằng";

            case -78:

                return "Hoạt động";

            case -967:

                return "NPC";

            case -1001:

                return "Cối xay gió";

            case -1002:

                return "Boa Hancock";

            case -1003:

                return "Cây lì xì";

        }

        return "NPC";

    }



    public static void process_menu(Player p, Message m2) throws IOException {
        if (p != null && !p.isdie) {
            short idNPC = m2.reader().readShort();

            // byte idMenu =

            m2.reader().readByte();

            byte index = m2.reader().readByte();

            // System.out.println("idNPC " + idNPC);

            // System.out.println("idMenu " + idMenu);

            // System.out.println("index " + index);



            p.currentNpcId = idNPC;
            p.currentMenuNpcId = idNPC;



            if (p.menus != null && !p.menus.isEmpty()) {

                java.util.List<model.Menu> menus = new java.util.ArrayList<>(p.menus);

                p.menus.clear();

                p.currentMenuOptions = null;

                if (index >= 0 && index < menus.size()) {

                    model.Menu menu = menus.get(index);

                    menu.execute(p, index);

                    return;

                }

            }



            if (p.currentMenuOptions != null) {

                @SuppressWarnings("unchecked")

                java.util.List<zinterfaces.iMenuDymanic.MenuSnapshot> snaps =

                        (java.util.List<zinterfaces.iMenuDymanic.MenuSnapshot>) p.currentMenuOptions;

                p.currentMenuOptions = null;

                if (index >= 0 && index < snaps.size()) {

                    zinterfaces.iMenuDymanic.MenuSnapshot snap = snaps.get(index);

                    if (snap.isDynamic) {

                        snap.entry.handler.handleSelect(p, idNPC, snap.entry.menuId);

                        return;

                    } else {

                        index = (byte) snap.baseIndex;

                    }

                }

            }



            map.Npc npcTarget = null;

            if (p.map != null && p.map.template != null) {

                for (int i = 0; i < p.map.template.npcs.size(); i++) {

                    if (idNPC == p.map.template.npcs.get(i).idmenu) {

                        npcTarget = p.map.template.npcs.get(i);

                        break;

                    }

                }

            }

            zinterfaces.iNpc npcHandler = (npcTarget != null && npcTarget.getHandler() != null) ? npcTarget.getHandler() : zinterfaces.iNpc.get(idNPC);

            if (npcHandler != null) {

                p.currentNpcId = idNPC; // track NPC đang tương tác để handler dispatch đúng

                p.currentMenuNpcId = idNPC;

                npcHandler.handleMenu(p, idNPC, index);

                return;

            }



            zinterfaces.iMenu menuHandler = zinterfaces.iMenu.get(idNPC);

            if (menuHandler != null) {

                p.currentNpcId = idNPC;

                p.currentMenuNpcId = idNPC;

                menuHandler.handleMenu(p, idNPC, index);

                return;

            }





            if (event.EventManager.dispatchHandleMenu(p, idNPC, index)) {

                return;

            }

            switch (idNPC) {

                case 701: {
                    if (p.party != null && p.party.list.size() > 1) {
                        if (p.party.list.get(0).equals(p)) {
                            List<String> missingKeys = new ArrayList<>();
                            for (Player member : p.party.list) {
                                if (member != null && !member.isBot && !member.isDe && !(member instanceof model.DeTu) && !(member instanceof bot.mercenary.MercenaryBot)) {
                                    if (member.get_key_boss() < 1) {
                                        missingKeys.add(member.name);
                                    }
                                }
                            }
                            if (!missingKeys.isEmpty()) {
                                p.getService().send_box_ThongBao_OK(String.join(", ", missingKeys) + " không đủ 1 chìa khóa phó bản");
                                return;
                            }
                            p.sanTrumLv = (index + 1) * 10;
                            p.sanTrumTick = new boolean[p.party.list.size()];
                            p.sanTrumTick[0] = true;
                            
                            String bossName = map.zones.ZSanTrum.NAME[p.sanTrumId];
                            for (int i = 1; i < p.party.list.size(); i++) {
                                final int memberIdx = i;
                                Player member = p.party.list.get(i);
                                Player p0 = map.Zone.get_player_by_name_allmap(member.name);
                                if (p0 != null && p0.map != null && p0.map.equals(p.map)) {
                                    p0.setyesNoDialog(new YesNoDialog(p0, 83, "Thông báo",
                                            String.format("%s mời bạn tham gia săn trùm %s cấp %d, bạn có đồng ý?",
                                                    p.name, bossName, p.sanTrumLv),
                                            new String[]{"Đồng ý", "Hủy"}, new byte[]{-1, -1}, choice -> {
                                                if (choice == 0) { // Đồng ý
                                                    if (p.party == null || p.party.list.isEmpty() || !p.party.list.get(0).equals(p)) {
                                                        return;
                                                    }
                                                    if (p.sanTrumTick != null && memberIdx < p.sanTrumTick.length) {
                                                        p.sanTrumTick[memberIdx] = true;
                                                    }
                                                    boolean isOk = true;
                                                    if (p.sanTrumTick != null) {
                                                        for (int j = 1; j < p.sanTrumTick.length; j++) {
                                                            if (!p.sanTrumTick[j]) {
                                                                isOk = false;
                                                                break;
                                                            }
                                                        }
                                                    } else {
                                                        isOk = false;
                                                    }
                                                    if (isOk) {
                                                        try {
                                                            zinterfaces.menus.SubMenuSanTrum.createAndEnterPartySanTrum(p);
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                    } else {
                                                        p0.getService().send_box_ThongBao_OK("Đợi thành viên khác đồng ý vào phó bản");
                                                    }
                                                } else {
                                                    p.getService().send_box_ThongBao_OK(p0.name + " đã từ chối tham gia săn trùm");
                                                }
                                            }));
                                    p0.getService().startYesNo();
                                }
                            }
                        }
                    } else {
                        if (p.get_key_boss() < 1) {
                            p.getService().send_box_ThongBao_OK("Bạn không đủ 1 chìa khóa phó bản");
                            return;
                        }
                        p.sanTrumLv = (index + 1) * 10;
                        zinterfaces.menus.SubMenuSanTrum.createAndEnterSoloSanTrum(p);
                    }
                    break;
                }

                case 932: {

                    if (index == 0) {



                    } else if (index == 1) {

                        if (p.detu == null) {

                            p.getService().send_box_ThongBao_OK("Bạn chưa có đệ tử");

                            return;

                        }

                    }

                    break;

                }

                case 933: {

                    if (index == 0) {

                        EffTemplate effTemplate = p.get_eff(8);

                        if (effTemplate != null) {

                            effTemplate.time = 0;

                            p.getService().send_box_ThongBao_OK("Hủy thành công");

                        } else {

                            p.getService().send_box_ThongBao_OK("Bạn đang không sử dụng khóa EXP");

                        }

                    } else if (index == 1) {

                        EffTemplate effTemplate = p.detu.get_eff(8);

                        if (effTemplate != null) {

                            p.detu.removeEff(effTemplate);

//                            effTemplate.time = 0;

                            p.getService().send_box_ThongBao_OK("Hủy thành công");

                        } else {

                            p.getService().send_box_ThongBao_OK("Đệ tử đang không sử dụng khóa EXP");

                        }

                    }

                }

                break;

                case 934: {
                    // DELEGATED to SubMenuEvent (iMenu 934) / SuKienTrungThu
                    if (event.EventManager.dispatchHandleMenu(p, 934, index)) {
                        return;
                    }
                    break;
                }

                

                

                 case -934: {
                    // DELEGATED to SubMenuEvent (iMenu -934) / SuKien8Thang3
                    if (event.EventManager.dispatchHandleMenu(p, -934, index)) {
                        return;
                    }
                    break;
                }



//                        if (index == 0) {

//                            p.getService().send_box_ThongBao_OK(WorldWar.findTop());

//                        } else if (index == 1) {

//                            String txt = "Lễ Hội Hải Tặc\\n"

//                                    + "Chào mừng bạn đến với Lễ Hội Hải Tặc.\\b"

//                                    + "Thời gian hoạt động hàng ngày:\\n"

//                                    + "- 19h00 - 19h15: Thời gian đăng ký và chọn phe.\\n"

//                                    + "- 19h15 - 21h00: Lễ hội chính thức bắt đầu, PK tự do giữa 2 phe Hải Quân và Hải Tặc.\\n"

//                                    + "- Sau 21h00: Kết thúc lễ hội và nhận thưởng cho TOP 3 người chơi có điểm cao nhất.\\b"

//                                    + "Mỗi khi hoạt động mở, bạn bắt buộc phải chọn lại phe từ đầu.\\b"

//                                    + "Chúc các bạn có những trải nghiệm vui vẻ.";

//                            p.getService().Help_From_Server(-78, txt);

//                        }

//                    }

//                }

//                break;





                

                

                case -989: {

                    if (index >= 0 && index < 20) { 

                        int requiredExp = 1000;

                        if (p.exp_so_tay() < requiredExp) {

                            p.getService().send_box_ThongBao_OK("Bạn Không đủ 1000 EXP Sổ Tay. Bạn có " + p.exp_so_tay() + " EXP!");

                            return;

                        }

                        if (p.so_tay() < index) {

                            p.getService().send_box_ThongBao_OK("Bạn chưa nhận quà ở các cấp thấp hơn. Hãy hoàn thành các cấp trước đó.");

                            return;

                        }

                        if (p.so_tay() >= index + 1) {

                            p.getService().send_box_ThongBao_OK("Bạn đã nhận quà ở cấp " + (index + 1) + " hoặc cấp cao hơn.");

                            return;

                        }

                        List<GiftBox> list = new ArrayList<>();

                        int giftItemId = 0;

                        int giftType = 0;

                        int giftQuantity = 1;



                        // Ã¯Â¿Â½???iÃ¯Â¿Â½???u kiÃ¡Â»â€¡n chia quÃƒÂ  cho ngÃ†Â°Ã¯Â¿Â½???i chÃ†Â¡i cÃƒÂ y (cÃ¡ÂºÂ¥p 1-9) vÃƒÂ  ngÃ†Â°Ã¯Â¿Â½???i chÃ†Â¡i nÃ¡ÂºÂ¡p tiÃ¯Â¿Â½???n (cÃ¡ÂºÂ¥p 10-20)

                        if (index >= 0 && index <= 9) { 

                            switch (index) {

                                case 0:

                                    giftItemId = 10; 

                                    giftType = 7;    

                                    giftQuantity = 15;

                                    break;

                                case 1:

                                    giftItemId = 339; 

                                    giftType = 4;     

                                    giftQuantity = 5; 

                                    break;

                                case 2:

                                    giftItemId = 1;    // ID quÃƒÂ  cÃ¡ÂºÂ¥p 3

                                    giftType = 4;      // Type 4

                                    giftQuantity = 5000;  // SÃ¡Â»â€ lÃ†Â°Ã¡Â»Â£ng quÃƒÂ  cÃ¡ÂºÂ¥p 3

                                    break;

                                case 3:

                                    giftItemId = 549;   // ID quÃƒÂ  cÃ¡ÂºÂ¥p 4

                                    giftType = 4;      // Type 7

                                    giftQuantity = 2; // SÃ¡Â»â€ lÃ†Â°Ã¡Â»Â£ng quÃƒÂ  cÃ¡ÂºÂ¥p 4

                                    break;

                                case 4:

                                    giftItemId = 691;

                                    giftType = 4;    

                                    giftQuantity = 1; 

                                    break;

                                case 5:

                                    giftItemId = 691;  // ID quÃƒÂ  cÃ¡ÂºÂ¥p 6

                                    giftType = 4;      // Type 7

                                    giftQuantity = 1;  // SÃ¡Â»â€ lÃ†Â°Ã¡Â»Â£ng quÃƒÂ  cÃ¡ÂºÂ¥p 6

                                    break;

                                case 6:

                                    giftItemId = 823;  // ID quÃƒÂ  cÃ¡ÂºÂ¥p 7

                                    giftType = 4;      // Type 4

                                    giftQuantity = 1;  // SÃ¡Â»â€ lÃ†Â°Ã¡Â»Â£ng quÃƒÂ  cÃ¡ÂºÂ¥p 7

                                    break;

                                case 7:

                                    giftItemId = 551;  // ID quÃƒÂ  cÃ¡ÂºÂ¥p 8

                                    giftType = 4;      // Type 7

                                    giftQuantity = 1;  // SÃ¡Â»â€ lÃ†Â°Ã¡Â»Â£ng quÃƒÂ  cÃ¡ÂºÂ¥p 8

                                    break;

                                case 8:

                                    giftItemId = 323;   // ID quÃƒÂ  cÃ¡ÂºÂ¥p 9

                                    giftType = 4;      // Type 4

                                    giftQuantity = 2; // SÃ¡Â»â€ lÃ†Â°Ã¡Â»Â£ng quÃƒÂ  cÃ¡ÂºÂ¥p 9

                                    break;

                                case 9:

                                    giftItemId = 62;  // ID quÃƒÂ  cÃ¡ÂºÂ¥p 10

                                    giftType = 105;      // Type 7

                                    giftQuantity = 1;  // SÃ¡Â»â€ lÃ†Â°Ã¡Â»Â£ng quÃƒÂ  cÃ¡ÂºÂ¥p 10

                                    break;

                            }

                        } else if (index >= 10 && index < 20) { 

                            if (p.so_tay_vip() <= 0) { 

                                p.getService().send_box_ThongBao_OK("???ể nhận quà từ cấp 10 trở lên, bạn cần nạp ti???n!");

                                return;

                            }



                            switch (index) {

                                case 10:

                                    giftItemId = 10;   // ID quÃƒÂ  cÃ¡ÂºÂ¥p 11

                                    giftType = 7;      // Type 4

                                    giftQuantity = 30; // SÃ¡Â»â€ lÃ†Â°Ã¡Â»Â£ng quÃƒÂ  cÃ¡ÂºÂ¥p 11

                                    break;

                                case 11:

                                    giftItemId = 1;    // ID quÃƒÂ  cÃ¡ÂºÂ¥p 12

                                    giftType = 4;      // Type 7

                                    giftQuantity = 5000; // SÃ¡Â»â€ lÃ†Â°Ã¡Â»Â£ng quÃƒÂ  cÃ¡ÂºÂ¥p 12

                                    break;

                                case 12:

                                    giftItemId = 732;  // ID quÃƒÂ  cÃ¡ÂºÂ¥p 13

                                    giftType = 4;      // Type 4

                                    giftQuantity = 1;  // SÃ¡Â»â€ lÃ†Â°Ã¡Â»Â£ng quÃƒÂ  cÃ¡ÂºÂ¥p 13

                                    break;

                                case 13:

                                    giftItemId = 732;  // ID quÃƒÂ  cÃ¡ÂºÂ¥p 14

                                    giftType = 4;      // Type 7

                                    giftQuantity = 1;  // SÃ¡Â»â€ lÃ†Â°Ã¡Â»Â£ng quÃƒÂ  cÃ¡ÂºÂ¥p 14

                                    break;

                                case 14:

                                    giftItemId = 325;  // ID quÃƒÂ  cÃ¡ÂºÂ¥p 15

                                    giftType = 4;      // Type 4

                                    giftQuantity = 1;  // SÃ¡Â»â€ lÃ†Â°Ã¡Â»Â£ng quÃƒÂ  cÃ¡ÂºÂ¥p 15

                                    break;

                                case 15:

                                    giftItemId = 325;  // ID quÃƒÂ  cÃ¡ÂºÂ¥p 16

                                    giftType = 4;      // Type 7

                                    giftQuantity = 1;  // SÃ¡Â»â€ lÃ†Â°Ã¡Â»Â£ng quÃƒÂ  cÃ¡ÂºÂ¥p 16

                                    break;

                                case 16:

                                    giftItemId = 326;   // ID quÃƒÂ  cÃ¡ÂºÂ¥p 17

                                    giftType = 4;      // Type 4

                                    giftQuantity = 1; // SÃ¡Â»â€ lÃ†Â°Ã¡Â»Â£ng quÃƒÂ  cÃ¡ÂºÂ¥p 17

                                    break;

                                case 17:

                                    giftItemId = 323;  // ID quÃƒÂ  cÃ¡ÂºÂ¥p 18

                                    giftType = 4;      // Type 7

                                    giftQuantity = 5;  // SÃ¡Â»â€ lÃ†Â°Ã¡Â»Â£ng quÃƒÂ  cÃ¡ÂºÂ¥p 18

                                    break;

                                case 18:

                                    giftItemId = 832;  // ID quÃƒÂ  cÃ¡ÂºÂ¥p 19

                                    giftType = 4;      // Type 4

                                    giftQuantity = 1;  // SÃ¡Â»â€ lÃ†Â°Ã¡Â»Â£ng quÃƒÂ  cÃ¡ÂºÂ¥p 19

                                    break;

                                case 19:

                                    giftItemId = 36;  // ID quÃƒÂ  cÃ¡ÂºÂ¥p 20

                                    giftType = 105;      // Type 7

                                    giftQuantity = 1;  // SÃ¡Â»â€ lÃ†Â°Ã¡Â»Â£ng quÃƒÂ  cÃ¡ÂºÂ¥p 20

                                    break;

                            }

                        }



                        // TÃ¡ÂºÂ¡o vÃƒÂ  thÃƒÂªm quÃƒÂ  vÃƒÂ o danh sÃƒÂ¡ch

                        if (giftType == 4) {

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(giftItemId);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = itemTemplate4.id;

                                gb4.type = 4;  // Ã¯Â¿Â½???Ã¡ÂºÂ£m bÃ¡ÂºÂ£o giftType lÃƒÂ  int

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = giftQuantity;  // SÃ¡Â»Â­ dÃ¡Â»Â¥ng sÃ¡Â»â€ lÃ†Â°Ã¡Â»Â£ng Ã„â€ÃƒÂ£ chÃ¯Â¿Â½???n cho tÃ¡Â»Â«ng cÃ¡ÂºÂ¥p

                                gb4.color = 0;

                                list.add(gb4);

                            }

                        } else if (giftType == 7) {

                            ItemTemplate7 itemTemplate7 = ItemTemplate7.get_it_by_id(giftItemId);

                            if (itemTemplate7 != null) {

                                GiftBox gb7 = new GiftBox();

                                gb7.id = itemTemplate7.id;

                                gb7.type = 7;  // Ã¯Â¿Â½???Ã¡ÂºÂ£m bÃ¡ÂºÂ£o giftType lÃƒÂ  int

                                gb7.name = itemTemplate7.name;

                                gb7.icon = itemTemplate7.icon;

                                gb7.num = giftQuantity;  // SÃ¡Â»Â­ dÃ¡Â»Â¥ng sÃ¡Â»â€ lÃ†Â°Ã¡Â»Â£ng Ã„â€ÃƒÂ£ chÃ¯Â¿Â½???n cho tÃ¡Â»Â«ng cÃ¡ÂºÂ¥p

                                gb7.color = 0;

                                list.add(gb7);

                            }

                        } else if (giftType == 105) {

                            // XÃ¡Â»Â­ lÃƒÂ½ phÃ¡ÂºÂ§n thÃ†Â°Ã¡Â»Å¸ng loÃ¡ÂºÂ¡i 105

                            ItemFashion itemFashion = ItemFashion.get_item(giftItemId);

                            if (itemFashion != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = itemFashion.ID;

                                gb4.type = 105;

                                gb4.name = itemFashion.name;

                                gb4.icon = itemFashion.idIcon;

                                gb4.num = giftQuantity;

                                gb4.color = 0;

                                list.add(gb4);

                            }

                        }



                        if (list.size() > 0) {

                            // GÃ¡Â»Â­i quÃƒÂ  cho ngÃ†Â°Ã¯Â¿Â½???i chÃ†Â¡i

                            Service.send_gift(p, 1, "Nhận Quà Sổ Tay", "Cấp " + (index + 1),

                                    list, true);



                            // CÃ¡ÂºÂ­p nhÃ¡ÂºÂ­t EXP vÃƒÂ  cÃ¡ÂºÂ¥p SÃ¡Â»â€¢ Tay sau khi nhÃ¡ÂºÂ­n quÃƒÂ 

                            p.update_exp_so_tay(-requiredExp);  // TrÃ¡Â»Â« Ã„â€i sÃ¡Â»â€ EXP Ã„â€ÃƒÂ£ sÃ¡Â»Â­ dÃ¡Â»Â¥ng

                            p.update_level_so_tay(1);  // ChÃ¡Â»â€° tÃ„Æ’ng 1 cÃ¡ÂºÂ¥p mÃ¯Â¿Â½???i lÃ¡ÂºÂ§n nhÃ¡ÂºÂ­n quÃƒÂ , khÃƒÂ´ng tÃ„Æ’ng thÃƒÂªm

                            break;

                        }

                    }

                }

                

                case -988: {

                    if (index == 0) {

                        if (p.active_so_tay() >= 1) {

                            p.getService().send_box_ThongBao_OK("Người chơi " + p.name + " đã nâng Sổ Tay Thường rồi không thể nâng nữa !.");

                            return;

                        }



                        if (p.get_ngoc() < 6_900) {

                            p.getService().send_box_ThongBao_OK("Tài khoản không đủ 6.900 Ruby. Vui lòng nạp thêm ruby!.");

                            return;

                        }

                        p.update_ngoc(-6_900);

                        p.updateMoney();

                        p.update_active_so_tay(1);

                        p.getService().send_box_ThongBao_OK("Chúc mừng người chơi " + p.name + " đã nâng cấp thành công Sổ Tay Hải Tặc Thường");



                        break;

                    }

                     if (index == 1) {

                        if (p.so_tay_vip() >= 1) {

                            p.getService().send_box_ThongBao_OK("Người chơi " + p.name + " đã nâng Sổ Tay Cao Cấp rồi không thể nâng nữa !.");

                            return;

                        }



                        if (p.get_gcoin() < 699_000) {

                            p.getService().send_box_ThongBao_OK("Tài khoản không đủ 690.000 GCoin. Vui lòng nạp thêm ti???n!.");

                            return;

                        }

                        p.update_gcoin(-699_000);

                        p.update_so_tay_vip(1);

                        p.getService().send_box_ThongBao_OK("Chúc mừng người chơi " + p.name + " đã nâng cấp thành công Sổ Tay Hải Tặc Cao Cấp rồi");



                        break;

                    }



                }

                

                case -984: {

                    // Tìm event đang active

                    event.Event activeEvent = null;

                    for (event.Event ev : event.EventManager.gI().getActiveEventsList()) {

                        activeEvent = ev;

                        break;

                    }



                    if (activeEvent == null) {

                        p.getService().send_box_ThongBao_OK("Hiện tại không có sự kiện đua top nào đang diễn ra.");

                        return;

                    }



                    // Kiểm tra xem thời gian đua top đã kết thúc chưa (phải sau endDate của time)

                    if (!activeEvent.isEventEnded()) {

                        p.getService().send_box_ThongBao_OK("Thời gian đua top chưa kết thúc! Hãy tiếp tục đua top.");

                        return;

                    }



                    switch (index) {

                        case 0: { // TOP Đốt Pháo Hoa

                            List<template.InfoMemList> cache = rank.TopDotPhao.gI().getCache();

                            if (cache.isEmpty()) {

                                rank.TopDotPhao.gI().update();

                                cache = rank.TopDotPhao.gI().getCache();

                            }

                            int playerRank = -1;

                            for (int i = 0; i < Math.min(cache.size(), 10); i++) {

                                if (cache.get(i).name.equals(p.name)) {

                                    playerRank = i;

                                    break;

                                }

                            }

                            if (playerRank != -1) {

                                if (!TraoQuaTop.hasReceivedRewardManual(p.name, LocalDateTime.now(), "TOP ĐỐT PHÁO")) {

                                    if (p.item.able_bag() < 5) {

                                        p.getService().send_box_ThongBao_OK("Yêu cầu 5 ô trống trong hành trang");

                                        return;

                                    }

                                    TraoQuaTop.giveRewardToManualTop(p, playerRank, "TOP ĐỐT PHÁO");

                                } else {

                                    p.getService().send_box_ThongBao_OK("Bạn đã nhận quà TOP ĐỐT PHÁO rồi");

                                }

                            } else {

                                p.getService().send_box_ThongBao_OK("Bạn không nằm trong danh sách nhận quà TOP ĐỐT PHÁO HOA");

                            }

                            break;

                        }

                        case 1: { // TOP Ghép Huy Hiệu

                            List<template.InfoMemList> cache = rank.TopGhepHuyHieu.gI().getCache();

                            if (cache.isEmpty()) {

                                rank.TopGhepHuyHieu.gI().update();

                                cache = rank.TopGhepHuyHieu.gI().getCache();

                            }

                            int playerRank = -1;

                            for (int i = 0; i < Math.min(cache.size(), 10); i++) {

                                if (cache.get(i).name.equals(p.name)) {

                                    playerRank = i;

                                    break;

                                }

                            }

                            if (playerRank != -1) {

                                if (!TraoQuaTop.hasReceivedRewardManual(p.name, LocalDateTime.now(), "TOP GHÉP HUY HIỆU")) {

                                    if (p.item.able_bag() < 5) {

                                        p.getService().send_box_ThongBao_OK("Yêu cầu 5 ô trống trong hành trang");

                                        return;

                                    }

                                    TraoQuaTop.giveRewardToManualTop(p, playerRank, "TOP GHÉP HUY HIỆU");

                                } else {

                                    p.getService().send_box_ThongBao_OK("Bạn đã nhận quà TOP GHÉP HUY HIỆU rồi");

                                }

                            } else {

                                p.getService().send_box_ThongBao_OK("Bạn không nằm trong danh sách nhận quà TOP GHÉP HUY HIỆU");

                            }

                            break;

                        }

                        case 2: { // TOP Nạp Tiền

                            List<template.InfoMemList> cache = rank.TopHalloweenNap.gI().getCache();

                            if (cache.isEmpty()) {

                                rank.TopHalloweenNap.gI().update();

                                cache = rank.TopHalloweenNap.gI().getCache();

                            }

                            int playerRank = -1;

                            for (int i = 0; i < Math.min(cache.size(), 10); i++) {

                                if (cache.get(i).name.equals(p.name)) {

                                    playerRank = i;

                                    break;

                                }

                            }

                            if (playerRank != -1) {

                                if (!TraoQuaTop.hasReceivedRewardManual(p.name, LocalDateTime.now(), "TOP_NAP")) {

                                    if (p.item.able_bag() < 5) {

                                        p.getService().send_box_ThongBao_OK("Yêu cầu 5 ô trống trong hành trang");

                                        return;

                                    }

                                    TraoQuaTop.giveRewardToManualTop(p, playerRank, "TOP_NAP");

                                } else {

                                    p.getService().send_box_ThongBao_OK("Bạn đã nhận quà TOP NẠP rồi");

                                }

                            } else {

                                p.getService().send_box_ThongBao_OK("Bạn không nằm trong danh sách nhận quà TOP NẠP");

                            }

                            break;

                        }

                    }

                    break;

                }

                

                case -985: {

                    if (index == 0) {

                        if (p.diemdanh > 0) {

                            p.getService().send_box_ThongBao_OK("Hôm nay bạn đã điểm danh rồi.");

                            return;

                        }

                        if (p.active_so_tay() == 1) {

                            p.update_exp_so_tay(ZUtil.random(50, 200));

                        }

                        p.update_ngoc(500);

                        p.update_vang(1_000_000);

                        p.updateMoney();

                        p.diemdanh++;

                        p.getService().send_box_ThongBao_OK("Điểm danh thành công nhận được 500 Ruby - 1M Beri");

                        break;

                    }

                    if (index == 1) {

                        String so_tay = (p.so_tay_vip() == 1) ? "Cao Cấp" : "Thường";

                        p.getService().send_box_ThongBao_OK("EXP: " + p.exp_so_tay()

                                + " - Level " + p.so_tay() + " - " + " Sổ : " + so_tay);

                        break;

                    }



                }



                case -834: {

                        if (index == 0) {

                            p.getService().send_box_ThongBao_OK("Bạn có " + p.pointEvent1 + " Điểm Giỏ Hoa và "  + p.pointEvent2 + " Điểm Bó Hoa");

                            break;

                        } else if (index == 1) {

                            if (p.pointEvent2 < 2000) {

                                p.getService().send_box_ThongBao_OK("Bạn Không đủ 2000 Điểm Bó Hoa .Bạn có " + p.pointEvent2 + " Điểm Bó Hoa!");

                                return;

                            }

                            List<GiftBox> list = new ArrayList<>();

                            {

                                ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(821);

                                if (itemTemplate4 != null) {

                                    GiftBox gb4 = new GiftBox();

                                    gb4.id = itemTemplate4.id;

                                    gb4.type = 4;

                                    gb4.name = itemTemplate4.name;

                                    gb4.icon = itemTemplate4.icon;

                                    gb4.num = 1;

                                    gb4.color = 0;

                                    list.add(gb4);

                                }

                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Đổi Điểm", "Nhận được",

                                        list, true);

                                p.update_pointEvent2(-2000);

                                break;

                            }

                        } else if (index == 2) {

                            if (p.pointEvent2 < 5000) {

                                p.getService().send_box_ThongBao_OK("Bạn Không đủ 5000 Điểm Bó Hoa .Bạn có " + p.pointEvent2 + " Điểm Bó Hoa!");

                                return;

                            }

                            List<GiftBox> list = new ArrayList<>();

                            {

                                ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(822);

                                if (itemTemplate4 != null) {

                                    GiftBox gb4 = new GiftBox();

                                    gb4.id = itemTemplate4.id;

                                    gb4.type = 4;

                                    gb4.name = itemTemplate4.name;

                                    gb4.icon = itemTemplate4.icon;

                                    gb4.num = 1;

                                    gb4.color = 0;

                                    list.add(gb4);

                                }

                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Đổi Điểm", "Nhận được",

                                        list, true);

                                p.update_pointEvent2(-5000);

                                break;

                            }

                        } else if (index == 3) {

                            if (p.pointEvent1 < 7000) {

                                p.getService().send_box_ThongBao_OK("Bạn Không đủ 7.000 Điểm Giỏ Hoa .Bạn có " + p.pointEvent1 + " Điểm Giỏ Hoa!");

                                return;

                            }

                            List<GiftBox> list = new ArrayList<>();

                            {

                                ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(749);

                                if (itemTemplate4 != null) {

                                    GiftBox gb4 = new GiftBox();

                                    gb4.id = itemTemplate4.id;

                                    gb4.type = 4;

                                    gb4.name = itemTemplate4.name;

                                    gb4.icon = itemTemplate4.icon;

                                    gb4.num = 1;

                                    gb4.color = 0;

                                    list.add(gb4);

                                }

                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Đổi điểm", "Nhận được",

                                        list, true);

                                p.update_pointEvent1(-7000);

                                break;

                            }

                        } else if (index == 4) {

                            if (p.pointEvent1 < 10_000) {

                                p.getService().send_box_ThongBao_OK("Bạn Không đủ 10000 Điểm Giỏ Hoa .Bạn có " + p.pointEvent1 + " Điểm Giỏ Hoa!");

                                return;

                            }

                            List<GiftBox> list = new ArrayList<>();

                            {

                                ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(325);

                                if (itemTemplate4 != null) {

                                    GiftBox gb4 = new GiftBox();

                                    gb4.id = itemTemplate4.id;

                                    gb4.type = 4;

                                    gb4.name = itemTemplate4.name;

                                    gb4.icon = itemTemplate4.icon;

                                    gb4.num = 2;

                                    gb4.color = 0;

                                    list.add(gb4);

                                }

                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Đổi điểm", "Nhận được",

                                        list, true);

                                p.update_pointEvent1(-10000);

                                break;

                            }

                        }

                    }

                

                break;



                case -936: {

                    if (index == 0) {

                        if (p.get_ngoc() < 3_000) {

                            p.getService().send_box_ThongBao_OK("Bạn Không đủ 3.000 Ruby .Bạn có " + p.get_ngoc() + " Ruby!");

                            return;

                        }

                        p.update_ngoc(-3_000);

                        p.updateVnd(3_000_000);

                        p.updateMoney();

                        break;

                    } else if (index == 1) {

                        if (p.get_ngoc() < 30_000) {

                            p.getService().send_box_ThongBao_OK("Bạn Không đủ 30.000 Ruby .Bạn có " + p.get_ngoc() + " Ruby!");

                            return;

                        }

                        p.update_ngoc(-30_000);

                        p.updateVnd(30_000_000);

                        p.updateMoney();

                        break;

                    } else if (index == 2) {

                        if (p.get_ngoc() < 300_000) {

                            p.getService().send_box_ThongBao_OK("Bạn Không đủ 300.000 Ruby .Bạn có " + p.get_ngoc() + " Ruby!");

                            return;

                        }

                        p.update_ngoc(-300_000);

                        p.updateVnd(300_000_000);

                        p.updateMoney();

                        break;

                    }



                }

                

                break;



                case 935: {
                    // DELEGATED to SuKienTrungThu / EventManager
                    if (event.EventManager.dispatchHandleMenu(p, 935, index)) {
                        return;
                    }
                    break;
                }

                

                 

                case 936: {

                    if (p.detu != null) {

                        List< MyPet> pet_select = new ArrayList<>();

                        for (int i = 0; i < p.my_pet.size(); i++) {

                            if (!p.my_pet.get(i).isUse) {

                                pet_select.add(p.my_pet.get(i));

                            }

                        }

                        for (int i = 0; i < pet_select.size(); i++) {

                            if (index == i) {

                                if (!pet_select.get(i).isUse) {

                                    MyPet pet1 = pet_select.remove(i);

                                    MyPet pet2 = null;

                                    if (!p.detu.my_pet.isEmpty()) {

                                        pet2 = p.detu.my_pet.remove(0);

                                    }

                                    if (pet2 != null) {

//                                        int temp = pet2.id;

                                        pet2.id = pet1.id;

//                                        pet1.id = (short) temp;

                                        pet2.isUse = false;

                                        p.my_pet.add(pet2);

                                    }

                                    pet1.isUse = true;

                                    p.my_pet.remove(pet1);

                                    p.detu.my_pet.add(pet1);

                                    p.detu.update_info_to_all();

                                } else {

                                    p.getService().send_box_ThongBao_OK("Pet đang được sử dụng");

                                }

                                break;

                            }

                        }



                    }

                }

                break;

                case 937: {

                    if (p.detu != null) {

                        List< Skill_info> list = new ArrayList<>();

                        for (int i = 0; i < p.detu.skill_point.size(); i++) {

                            Skill_info sk = p.detu.skill_point.get(i);

                            if (sk.temp.typeSkill == 1 && sk.temp.Lv_RQ > 0) {

                                list.add(sk);

                            }

                        }

                        if (index >= 0 && index < list.size()) {

                            Skill_info sk = list.get(index);



                            int percent = (sk.lvdevil == 0) ? 10 //

                                    : ((sk.lvdevil == 1) ? 8 //

                                            : ((sk.lvdevil == 2) ? 6 //

                                                    : ((sk.lvdevil == 3) ? 5 : 4)));

                            String notice = ("Xác nhận nâng cấp ác quỷ kỹ năng " + sk.temp.name + "? Hiện tại cấp"

                                    + + +sk.lvdevil + " - " + sk.devilpercent + "%. Thành công sẽ tăng thêm " + percent

                                    + "% vào cấp ác quỷ. Nguyên liệu 10 đá ác quỷ");

                            p.data_yesno = new int[]{93, p.detu.skill_point.indexOf(sk)};

                            p.setyesNoDialog(new YesNoDialog(p, 93, "Thông báo", notice, new String[]{"100.000", "100", "Đóng"}, new byte[]{6, 7, 1}));

                            p.getService().startYesNo();

                            break;



                        }

                    }

                }

                break;

                case 938: {

                    if (p.detu != null) {

                        if (index == 0) {

                            if (p.detu.item.it_heart == null) {

                                String text = "Để có thể tách tim đệ tử, bạn sẽ mất %s bột vàng, "

                                        + "%s đá ác quỷ, %s đá hải thạch cấp 1, %s.000.000 beri. Bạn thật sự muốn tách?";

                                p.data_yesno = new int[]{94};

                                p.setyesNoDialog(new YesNoDialog(p, 94, "Thông báo",

                                        String.format(text, ((p.detu.level < 40) ? 120 : 100), ((p.detu.level < 40) ? 120 : 100),

                                                ((p.detu.level < 40) ? 120 : 100), ((p.detu.level < 40) ? 12 : 10)),

                                        new String[]{"Đồng ý", "Hủy"}, new byte[]{-1, -1}));

                                p.getService().startYesNo();

                            }

                        } else if (index == 1) {

                            if (p.detu.item.it_heart != null && p.detu.item.it_heart.levelUp < 99) {



                                int ruby_req = 0;

                                if ((p.detu.item.it_heart.levelUp % 5) == 4) {

                                    ruby_req = ((p.detu.item.it_heart.levelUp / 5) + 1) * 10;

                                }

                                p.data_yesno = new int[]{95};

                                p.setyesNoDialog(new YesNoDialog(p, 95, "Thông báo",

                                        ("Để tiến hành phẫu thuật tim cho đệ tử, bạn cần có "

                                        + ZUtil.number_format(

                                                1_200_000L + (p.detu.item.it_heart.levelUp * 200_000L))

                                        + " beri" + ((ruby_req > 0) ? (" và " + ruby_req + " ruby ") : "")

                                        + ". " + "Bạn thật sự muốn phẫu thuật? Cấp hiện tại: " + p.detu.item.it_heart.levelUp),

                                        new String[]{"Đồng ý", "Hủy"}, new byte[]{2, 1}));

                                p.getService().startYesNo();



                            }

                        } else if (index == 2) {



                        }

                    }

                }

                break;

                case 939: {

                    if (p.detu != null) {

                        if (index == 0) {

                            p.isDeOnl = !p.isDeOnl;

                            if (!p.isDeOnl) {

                                p.map.remove_obj(p.detu.index_map, 0);

                                p.getService().send_box_ThongBao_OK("Đã cất đệ");

                            } else {

                                if (p.map.map_vp != null || MessageHandler.isMapDetu(p.map.template.id)) {

                                        Message m_local = new Message(1);

                                        m_local.writer().writeByte(0);

                                        m_local.writer().writeShort(p.detu.index_map);

                                        m_local.writer().writeShort(p.x);

                                        m_local.writer().writeShort(p.y);

                                        p.map.send_msg_all_p(m_local, null, true);



                                        m_local.cleanup();

                                        p.detu.map = p.map;

                                        p.detu.x = p.x;

                                        p.detu.y = p.y;

                                    }

                                p.getService().send_box_ThongBao_OK("Gọi đệ thành công");

                            }



                        } else if (index == 1) {

                            String txt;

                            if (p.isDeOnl) {

                                txt = "Trạng thái: xuất hiện\\n";

                            } else {

                                txt = "Trạng thái: ẩn\\n";

                            }

                            byte[] a = new byte[]{-1, -2, -3, 10, 11, 12, 13, 14, 47, 48, 49, 50, 51, 52};

                            int[] b = new int[a.length];

                            for (int i = 0; i < a.length; i++) {

                                if (a[i] == -1) {

                                    txt += ("Tấn công: " + p.detu.ability.get_dame(true) + "\n");

                                } else if (a[i] == -2) {

                                    txt += ("Hp: " + p.detu.ability.get_hp_max(true) + "\n");

                                } else if (a[i] == -3) {

                                    txt += ("Mp: " + p.detu.ability.get_mp_max(true) + "\n");

                                } else {

                                    ItemOptionTemplate opTemp = ItemOptionTemplate.ENTRYS.get(a[i]);

                                    b[i] = p.detu.ability.view_in4(a[i]);

                                    txt += (opTemp.name + ": "

                                            + (opTemp.percent == 1

                                                    ? String.format("%.1f%%\n", (((float) b[i]) / 10f))

                                                    : (b[i] + "\n")));

                                }



                            }



                            p.getService().send_box_ThongBao_OK(txt);

                        } else if (index == 2) {

                            String info = "Danh sách kỹ năng:";

                            for (int i = 0; i < p.detu.skill_point.size(); i++) {

                                if (p.detu.skill_point.get(i).temp.typeSkill == 1 && p.detu.skill_point.get(i).temp.Lv_RQ > 0) {

                                    info += ("\n " + p.detu.skill_point.get(i).temp.name + " Lv" + p.detu.skill_point.get(i).temp.Lv_RQ

                                            + (p.detu.skill_point.get(i).temp.ID < 4 ? (" " + (p.detu.skill_point.get(i).get_percent() / 10) + "%") : ""));

                                }

                            }

                            p.getService().send_box_ThongBao_OK(info);

                        }

                    }



                }

                break;

                case 940: {

                    if (p.detu != null) {

                        if (index == 0) {

                            String info = "Danh sách kỹ năng:";

                            for (int i = 0; i < p.detu.skill_point.size(); i++) {

                                Skill_info sk = p.detu.skill_point.get(i);

                                if (sk.temp.typeSkill == 1 && sk.temp.Lv_RQ > 0) {

                                    info += ("\n + " + sk.temp.name + " Lv" + sk.temp.Lv_RQ

                                            + (sk.temp.ID < 4 ? (" " + (sk.get_percent() / 10) + "%") : "")

                                            + "\\nCấp ác quỷ "

                                            + +sk.lvdevil + " " + sk.devilpercent + "%");

                                }

                            }

                            p.getService().send_box_ThongBao_OK(info);

                        } else if (index == 1) {

                            List< String> list = new ArrayList<>();

                            for (int i = 0; i < p.detu.skill_point.size(); i++) {

                                Skill_info sk = p.detu.skill_point.get(i);

                                if (sk.temp.typeSkill == 1 && sk.temp.Lv_RQ > 0) {

                                    list.add(sk.temp.name);

                                }

                            }

                            String[] menu = new String[list.size()];

                            for (int i = 0; i < menu.length; i++) {

                                menu[i] = list.get(i);



                            }

                            sendDymanicMenu(p, 937, "Nâng cấp kỹ năng",

                                    menu,

                                    null);

                        }

                    }

                }

                break;

                case 941: {

                    if (index == 0) {

                        p.typeShopTichTieu = 1;

                        zabstracts.AbsTichTieuRuby tichTieu = zabstracts.AbsTichTieuRuby.get(p.typeShopTichTieu);

                        if (tichTieu != null) {

                            tichTieu.showTable(p);

                        }

                    } else if (index == 1) {

                        p.getService().send_box_ThongBao_OK(String.format("Điểm tích: %s điểm. Số ruby đã dùng: %s",

                                        (p.getTieuRuby() / 10),

                                        p.getTieuRuby()

                                ));

                    }

                }

                break;

                case 942: {

                    if (p.detu != null) {

                        if (index == 0) {

                            p.getService().send_box_ThongBao_OK(String.format("???iểm tiểm năng: %s\\nSức mạnh: %s\\nPhòng thủ: %s\\nThể lực: %s\\nTinh Thần: %s\\nNhanh nhẹn: %s",

                                            p.detu.pointAttribute, p.detu.point1, p.detu.point2, p.detu.point3, p.detu.point4, p.detu.point5));

                        } else if (index >= 1 && index <= 5) {

                            p.data_yesno = new int[]{86, index};

                            p.setyesNoDialog(new YesNoDialog(p, 86, "Thông báo", "Bạn muốn cộng bao nhiêu điểm tiềm năng", new String[]{"1", "2", "10", "Hủy"}, new byte[]{-1, -1, -1, -1}));

                            p.getService().startYesNo();

                        }

                    }

                }

                break;

                case 943: {

                    if (p.detu != null) {

                        if (index == 0) {

                            String txt = "";

                            if (p.isDeOnl) {

                                txt = "Trạng thái: xuất hiện\\n";

                            } else {

                                txt = "Trạng thái: ẩn\\n";

                            }

                            byte[] a = new byte[]{-1, -2, -3, 10, 11, 12, 13, 14};

                            int[] b = new int[a.length];

                            for (int i = 0; i < a.length; i++) {

                                if (a[i] == -1) {

                                    txt += ("Tấn công: " + p.detu.ability.get_dame(true) + "\n");

                                } else if (a[i] == -2) {

                                    txt += ("Hp: " + p.detu.ability.get_hp_max(true) + "\n");

                                } else if (a[i] == -3) {

                                    txt += ("Mp: " + p.detu.ability.get_mp_max(true) + "\n");

                                } else {

                                    ItemOptionTemplate opTemp = ItemOptionTemplate.ENTRYS.get(a[i]);

                                    b[i] = p.detu.ability.view_in4(a[i]);

                                    txt += (opTemp.name + ": "

                                            + (opTemp.percent == 1

                                                    ? String.format("%.1f%%\n", (((float) b[i]) / 10f))

                                                    : (b[i] + "\n")));

                                }



                            }



                            p.getService().send_box_ThongBao_OK(txt);

                        } else if (index == 1) {

                            sendDymanicMenu(p, 942, "Đệ tử",

                                    new String[]{"Thông tin", "Sức mạnh", "Phòng thủ", "Thể lực", "Tinh Thần", "Nhanh nhẹn"},

                                    null);

                        } else if (index == 2) {

                            sendDymanicMenu(p, 940, "Kỹ năng đệ tử",

                                    new String[]{"Thông tin", "Nâng cấp ác quỷ"},

                                    null);

                        } else if (index == 3) {

                            sendDymanicMenu(p, 938, "Tim",

                                    new String[]{"TÌÁch", "Nâng cấp"},

                                    null);

                        } else if (index == 4) {

                            List< MyPet> pet_select = new ArrayList<>();

                            for (int i = 0; i < p.my_pet.size(); i++) {

                                if (!p.my_pet.get(i).isUse) {

                                    pet_select.add(p.my_pet.get(i));

                                }

                            }

                            String[] menu = new String[pet_select.size()];

                            for (int i = 0; i < menu.length; i++) {

                                menu[i] = pet_select.get(i).template.name;

                            }

                            if (!pet_select.isEmpty()) {

                                sendDymanicMenu(p, 936, "Sử dụng Pet",

                                        menu,

                                        null);

                            }

                        } else if (index == 5) {

                            if (p.detu.level >= 100) {

                                String[] name = new String[]{"Kháng phép", "MP+", "Kháng vật lý", "Tăng phòng thủ", "HP+",

                                    "Tăng tấn công"};

                                String notice = "Điểm tiềm năng: " + p.detu.pointAttributeThongThao;



                                short[] id_op = new short[]{27, 16, 26, 4, 15, 1};

                                for (int i = 0; i < name.length; i++) {



                                    int value = 0;

                                    for (int j = 0; j < p.detu.list_op_thongthao.size(); j++) {

                                        if (p.detu.list_op_thongthao.get(j).id == id_op[i]) {

                                            value += p.detu.list_op_thongthao.get(j).getParam();

                                        }

                                    }

                                    notice += ("\n" + name[i] + ": " + value + "điểm");



                                }

                                p.data_yesno = new int[]{96};

                                p.setyesNoDialog(new YesNoDialog(p, 96, "Thông báo", notice, new String[]{

                                    "+Kháng phép", "+MP", "+Kháng vật lý", "+Tăng phòng thủ", "+HP",

                                    "+Tăng tấn công", "Hủy"}, new byte[]{-1, -1, -1, -1, -1, -1, -1}));

                                p.getService().startYesNo();

                            } else {

                                p.getService().send_box_ThongBao_OK("Chưa đạt level 100");

                            }



                        } else if (index == 6) {

                            p.switchCharacter(p.detu);

                        } else if (index == 7) {

                            // Đổi trạng thái đệ tử

                            model.DeTu dt = p.detu;

                            if (dt == null && !p.isFusion) {

                                p.getService().send_box_ThongBao_OK("Đệ tử không khả dụng!");

                            } else {

                                String curStatus = (dt != null)

                                        ? model.DeTu.STATUS_NAMES[Math.min(dt.detuStatus, model.DeTu.STATUS_NAMES.length - 1)]

                                        : "Hợp thể";

                                YesNoDialog dlgStatus = new YesNoDialog(p, 87101,

                                        "Trạng Thái Đệ Tử [Đang: " + curStatus + "]",

                                        "Chọn trạng thái:",

                                        new String[]{"Đi theo", "Bảo vệ", "Tấn công", "Hợp thể", "Về nhà", "Hủy"},

                                        new byte[]{-1, -1, -1, -1, -1, -1});

                                dlgStatus.setHandler(val -> {

                                    try {

                                        model.DeTu dt2 = p.detu;

                                        if (val == 5) return;

                                        if (val == 0 && dt2 != null) dt2.setStatus(model.DeTu.STATUS_FOLLOW);

                                        else if (val == 1 && dt2 != null) dt2.setStatus(model.DeTu.STATUS_PROTECT);

                                        else if (val == 2 && dt2 != null) dt2.setStatus(model.DeTu.STATUS_ATTACK);

                                        else if (val == 3 && dt2 != null) dt2.setStatus(model.DeTu.STATUS_FUSION);

                                        else if (val == 4) {

                                            if (p.isFusion && dt2 != null) dt2.removeFusion();

                                            if (dt2 != null) dt2.setStatus(model.DeTu.STATUS_HOME);

                                        }

                                    } catch (Exception e) {

                                        e.printStackTrace();

                                    }

                                });

                                p.setyesNoDialog(dlgStatus);

                                p.getService().startYesNo();

                            }

                        }

                    }

                }

                break;

                

                case -969: {

                    if (index == 0) {

                        p.getService().Send_UI_Shop(8);

                    }

                    break;

                }

                

                case 960: {
                    if (event.EventManager.dispatchHandleMenu(p, idNPC, index)) {
                        return;
                    }
                    break;
                }

                case 944: {

                    if (event.EventManager.isActive(9)) {

                        if (index == 0) {

                            p.data_yesno = new int[]{84, 384};

                        } else if (index == 1) {

                            p.data_yesno = new int[]{84, 385};

                        }

                        p.setyesNoDialog(new YesNoDialog(p, 84, "Thông báo", "Dùng 1 vé săn heo thường để vào?", new String[]{"Đồng ý", "Hủy"}, new byte[]{2, 1}));

                        p.getService().startYesNo();

                    }

                }

                break;

                case 945:
                case 946:
                case 948:
                case 949: {
                    // DELEGATED to SuKienTrongCay / EventManager
                    if (event.EventManager.dispatchHandleMenu(p, idNPC, index)) {
                        return;
                    }
                    break;
                }

                case 954: {

                    if (ThuLinhBienKhoi.isOpen() && p.clan != null && (p.clan.getTypeMem(p) == 0 || p.clan.getTypeMem(p) == 1)) {

                        String[] name = new String[]{"Đông", "Tây", "Nam", "Bắc"};

                        int flag = index == 0 ? 4 : (index == 1 ? 5 : (index == 2 ? 6 : 7));

                        p.data_yesno = new int[]{82, flag};

                        p.setyesNoDialog(new YesNoDialog(p, 82, "Thông báo", "Xác nhận chọn phe Biển " + name[index] + " để tham gia phó bản thủ lĩnh biển khơi?", new String[]{"Đồng ý", "Hủy"}, new byte[]{2, 1}));

                        p.getService().startYesNo();

                    }

                }

                break;

                case 950: {

                    if (index == 0) {

                        model.DeTu.showDeTuInfo(p);

                    } else if (index == 1) {

                        model.DeTu.showDeTuSkillMenu(p);

                    } else if (index == 2) {

                        model.DeTu.showDeTuStatusMenu(p);

                    } else if (index == 3) {

                        Sudo mySudo = Sudo.getSuDoByName(p.name);

                        if (mySudo == null) {

                            if (p.item.total_item_bag_by_id(4, 627) > 0) {

                                p.item.remove_item47(4, 627, 1);

                                p.item.updateInventory(false);

                                Sudo sudoAdd = new Sudo();

                                sudoAdd.id = p.IDPlayer;

                                sudoAdd.accountId = p.conn != null ? p.conn.idUser : 0;

                                sudoAdd.name = p.name;

                                sudoAdd.head = p.head;

                                sudoAdd.hair = p.hair;

                                sudoAdd.hat = p.get_hat();

                                sudoAdd.chucInSudo = 1;

                                sudoAdd.lvExp = 1;

                                sudoAdd.exp = 0;

                                sudoAdd.point = 1;

                                sudoAdd.nameRelative = new ArrayList<>();

                                sudoAdd.nameRelative.add(p.name);

                                Sudo.addSuDoByName(p.name, sudoAdd);

                                Sudo.updateDb();

                                p.getService().sendSudoInfo(false);

                                p.getService().send_box_ThongBao_OK("Chúc mừng bạn đã mở thành công chức năng Sư Phụ! Bạn có thể nhận người chơi khác làm đệ tử cũng như thu nhận đệ tử hoang.");

                            } else {

                                p.getService().send_box_ThongBao_OK("Bạn cần có 1 [Chứng nhận sư phụ] để mở chức năng Sư Phụ! (Vật phẩm rơi từ Phó bản với tỉ lệ may mắn 1%).");

                            }

                        } else {

                            p.getService().send_box_ThongBao_OK("Bạn hiện tại đã có sư đồ.");

                        }

                    } else if (index == 4) {

                        p.getService().sendUpgradeDevilCraftPanel(

                            "Ghép vật phẩm",

                            (byte) 4,

                            new short[]{623, 624, 625, 626},

                            new short[]{1, 1, 1, 1},

                            new byte[]{4, 4, 4, 4},

                            new short[]{587, 588, 589, 590},

                            0,

                            (short) 50,

                            50_000,

                            (short) 627,

                            (short) 1,

                            (byte) 4,

                            (short) 591,

                            (byte) 100

                        );

                    }

                }

                break;

                case 955: {

                    model.DeTu.handleDeTuSkillSelect(p, index);

                    break;

                }

                case 956: {

                    model.DeTu.handleDeTuStatusSelect(p, index);

                    break;

                }

                case 9899: {

                    TopGift.handleReceiveMenu(p, index);

                    break;

                }

                case 951: {

                    boss.SuperBossManager.handleMenu(p, index);

                    break;

                }

                case 952: {

                    if (p.clan == null || index > 2 && !p.clan.members.get(0).name.equals(p.name)) {

                        return;

                    }

                    if (index == 0) {

                        Message m = new Message(-95);

                        m.writer().writeByte(0);

                        m.writer().writeUTF("Danh sách huy hiệu hành trình");
                        m.writer().writeShort(ClanHanhTrinhIcon.ENTRY.size());
                        for (int i = 0; i < ClanHanhTrinhIcon.ENTRY.size(); i++) {
                            ClanHanhTrinhIcon temp = ClanHanhTrinhIcon.ENTRY.get(i);
                            m.writer().writeShort(temp.id);
                            m.writer().writeUTF(temp.name != null ? temp.name : "");
                            m.writer().writeUTF(temp.info != null ? temp.info : "");
                            m.writer().writeShort(temp.icon);
                            m.writer().writeUTF("");
                            if (temp.op != null && !temp.op.isEmpty()) {
                                m.writer().writeByte(temp.op.size());
                                for (template.Option op : temp.op) {
                                    m.writer().writeByte(op.id);
                                    m.writer().writeShort(op.getParam());
                                }
                            } else {
                                m.writer().writeByte(0);
                            }
                        }
                        p.addmsg(m);
                        m.cleanup();
                    } else if (index == 1) {
                        Message m = new Message(-95);
                        m.writer().writeByte(0);
                        m.writer().writeUTF("Danh sách sở hữu");
                        java.util.List<ClanHanhTrinhIcon> owned = new java.util.ArrayList<>();
                        if (p.clan.hanhtrinh != null && p.clan.hanhtrinh.size() > 1) {
                            for (int i = 1; i < p.clan.hanhtrinh.size(); i++) {
                                ClanHanhTrinhIcon temp = ClanHanhTrinhIcon.getById(p.clan.hanhtrinh.get(i));
                                if (temp != null) {
                                    owned.add(temp);
                                }
                            }
                        }
                        m.writer().writeShort(owned.size());
                        for (ClanHanhTrinhIcon temp : owned) {
                            m.writer().writeShort(temp.id);
                            m.writer().writeUTF(temp.name != null ? temp.name : "");
                            m.writer().writeUTF(temp.info != null ? temp.info : "");
                            m.writer().writeShort(temp.icon);
                            m.writer().writeUTF("");
                            if (temp.op != null && !temp.op.isEmpty()) {
                                m.writer().writeByte(temp.op.size());
                                for (template.Option op : temp.op) {
                                    m.writer().writeByte(op.id);
                                    m.writer().writeShort(op.getParam());
                                }
                            } else {
                                m.writer().writeByte(0);
                            }
                        }

                        p.addmsg(m);

                        m.cleanup();

                    } else if (index == 2) {

                        Message m = new Message(-95);

                        m.writer().writeByte(3);

                        m.writer().writeByte(0);

                        m.writer().writeShort(580);

                        m.writer().writeShort(p.clan.hanhtrinh.get(0));

                        m.writer().writeByte(4);

                        m.writer().writeShort(ItemTemplate4.get_it_by_id(580).icon);

                        p.addmsg(m);

                        m.cleanup();

                    }

                }

                break;

                case 953: {

                    if (p.clan == null || index > 2 && !p.clan.members.get(0).name.equals(p.name)) {

                        return;

                    }

                    if (index == 0) {

                        p.getService().sendUpgradeDevilCraftPanel(

                            "Ghép vật phẩm",

                            (byte) 1,

                            new short[]{579},

                            new short[]{100},

                            new byte[]{4},

                            new short[]{551},

                            50_000,

                            (short) 10,

                            10_000,

                            (short) 580,

                            (short) 1,

                            (byte) 4,

                            (short) 552,

                            (byte) 10

                        );

                    } else if (index == 1) {

                        new model.InputDialog(p, 27, "Cống hiến xu hành trình", new String[]{"Nhập xu hành trình"}).startInput();

                    } else if (index == 2) {

                        sendDymanicMenu(p, 952, "Huy hiệu hành trình", new String[]{"Danh sách huy hiệu hành trình", "Danh sách sở hữu", "Mở xu hành trình"}, null);

                    }

                }

                break;

                case 973: {
                    EventManager.dispatchHandleMenu(p, 973, index);
                }
                break;

                case 974: {

                    EventManager.dispatchHandleMenu(p, 974, index);

                }

                break;

                 case 975: {

                    EventManager.dispatchHandleMenu(p, 975, index);

                }

                break; // [FIX MC-H3] avoid fall-through to admin case 976

                case 976: {

                    if (p.getUsername().equals("admin")) {

                        index--;

                        if (index >= 0 && index < DauGia.ENTRY.size()) {

                            p.getService().send_box_ThongBao_OK("Xoá item đấu giá "

                                    + ItemTemplate4.get_item_name(DauGia.ENTRY.get(index).itemID));

                            DauGia.ENTRY.remove(index);

                        }

                    }

                }

                break;

                case 977: {

                    if (p.getUsername().equals("admin")) {

                        if (index == 0) {

                            new model.InputDialog(p, 16, "Nhập thông tin",

                                    new String[]{"Nhập ItemID", "Nhập giá khởi điểm", "Nhập mức tăng thấp nhất",

                                        "Nhập ngày kết thúc", "Nhập giờ kết thúc"}).startInput();

                        } else {

                            String[] list = new String[DauGia.ENTRY.size() + 1];

                            list[0] = "Chọn item để xoá";

                            for (int i = 0; i < list.length - 1; i++) {

                                list[i + 1] = ItemTemplate4.get_item_name(DauGia.ENTRY.get(i).itemID);

                            }

                            sendDymanicMenu(p, 976, "DauGiaPanel", list, null);

                        }

                    }

                }

                break;



                case 978: {

                    if (index == 0) {

                        HanhTrinh.show_table(p, 1);

                    } else if (index == 1) {

                        HanhTrinh.show_table(p, 0);

                    } else if (index == 2) {

                        p.getService().send_box_ThongBao_OK("M???i khi bạn tiêu diệt boss cuối m???i làng sẽ có 10% cơ hội nhận được đá hành trình. "

                                + "Gặp trưởng làng xem đá kiếm được");

                    }

                    break;

                }

                case 979: {

                    if (index == 0) {

//                        Message m = new Message(-19); // show table select icon

//                        m.writer().writeByte(97);

//                        m.writer().writeUTF("Cửa hàng biểu tượng thường");

//                        m.writer().writeByte(107);

//                        m.writer().writeShort(60);

//                        for (int i = 0; i < 60; i++) {

//                            m.writer().writeShort(i);

//                            m.writer().writeShort(i);

//                            m.writer().writeUTF("Huy hiệu " + (i + 1));

//                            m.writer().writeUTF("Cửa hàng biểu tượng thường");

//                            if (i < 10) {

//                                m.writer().writeShort(0);

//                            } else {

//                                m.writer().writeShort(5000);

//                            }

//                        }

//                        p.addmsg(m);

//                        m.cleanup();



                        Message m = new Message(-19); // show table select icon

                        m.writer().writeByte(97);

                        m.writer().writeUTF("Cửa hàng biểu tượng thường");

                        m.writer().writeByte(107);

                        Set<Integer> hs = new HashSet<>();

                        for (int i = 0; i < 316; i++) {

                            if ((i >= 278 && i <= 292) || i == 303) {

                                continue;

                            }

                            hs.add(i);

                        }

                        for (int i = 0; i < Clan.ENTRY.size(); i++) {

                            int iconn = Clan.ENTRY.get(i).icon;

                            if (hs.contains(iconn)) {

                                hs.remove(iconn);

                            }

                        }

                        m.writer().writeShort(hs.size());

                        hs.forEach(s -> {

                            try {

                                m.writer().writeShort(s);

                                m.writer().writeShort(s);

                                m.writer().writeUTF("Huy hiệu " + (s + 1));

                                m.writer().writeUTF("Cửa hàng biểu tượng thường");

                                if (s < 10) {

                                    m.writer().writeShort(0);

                                } else {

                                    m.writer().writeShort(200);

                                }

                            } catch (IOException ex) {

                            }

                        });

                        p.addmsg(m);

                        m.cleanup();

                    } else if (index == 1) {

                        Message m = new Message(-19); // show table select icon

                        m.writer().writeByte(97);

                        m.writer().writeUTF("Cửa hàng biểu tượng cao cấp");

                        m.writer().writeByte(107);

                        Set<Integer> hs = new HashSet<>();

                        for (int i = 317; i < 349; i++) {

                            if (i == 317) {

                                continue;

                            }

                            hs.add(i);

                        }

                        for (int i = 370; i < 401; i++) {



                            hs.add(i);

                        }

                        for (int i = 500; i <= 522; i++) {



                            hs.add(i);

                        }

                        for (int i = 0; i < Clan.ENTRY.size(); i++) {

                            int iconn = Clan.ENTRY.get(i).icon;

                            if (hs.contains(iconn)) {

                                hs.remove(iconn);

                            }

                        }

                        m.writer().writeShort(hs.size());

                        hs.forEach(s -> {

                            try {

                                m.writer().writeShort(s);

                                m.writer().writeShort(s);

                                m.writer().writeUTF("Huy hiệu " + (s + 1));

                                m.writer().writeUTF("Cửa hàng biểu tượng cao cấp");

                                if (s < 10) {

                                    m.writer().writeShort(0);

                                } else {

                                    m.writer().writeShort(Clan.get_ngoc_icon((short) (int) s));

                                }

                            } catch (IOException ex) {

                            }

                        });

                        p.addmsg(m);

                        m.cleanup();

                    }

                    break;

                }

                case 980: {

                    switch (index) {

                        case 0: {

                            new UpgradeDevil().show_table(p, 3);

                            break;

                        }

                        case 1: {

                            new UpgradeDevil().show_table(p, 4);

                            break;

                        }

                        case 2: {

                            new UpgradeDevil().show_table(p, 5);

                            break;

                        }

                        case 3: {

                            UpgradeDial.show_table(p);

                            break;

                        }

                        case 4: {

                            Rebuild_Item.show_table(p, 10);

                            break;

                        }

                    }

                    break;

                }

                case 981: {

                    if (index == 0) {

                        DauGia.show_table(p);

                    } else if (index == 1) {

                        new model.InputDialog(p, 15, "Đổi búa đấu giá",

                                new String[]{"Nhập số búa (1búa = 1k extol)"}).startInput();

                    } else if (index == 2) {

                        new model.InputDialog(p, 14, "Đổi extol từ búa",

                                new String[]{"Nhập số búa (1búa = 1k extol)"}).startInput();

                    } else if (index == 3) {

                        DauGia.getBuaBack(p);

                    }

                    break;

                }

                case 982: {

                    if (p.ship_pet != null && p.name_ThoSanHaiTac != null

                            && index < p.name_ThoSanHaiTac.length) {

                        Player p0 = Zone.get_player_by_name_allmap(p.name_ThoSanHaiTac[index]);

                        if (p0 != null && p0.name_ThoSanHaiTac == null) {

                            p0.name_ThoSanHaiTac = new String[]{p.name};

                            p0.setyesNoDialog(new YesNoDialog(p0, 53, "Thông báo",

                                    p.name + " muốn mời bạn bảo vệ hàng, bạn hãy trả lời?",

                                    new String[]{"Đồng ý", "Hủy"}, new byte[]{2, 1}));

                            p0.getService().startYesNo();

                            p.getService().send_box_ThongBao_OK("Gửi yêu cầu thành công, đợi đối phương xác nhận");

                        } else {

                            p.getService().send_box_ThongBao_OK("Đối phương đã rời đi, hãy thử lại");

                        }

                    } else {

                        p.getService().send_box_ThongBao_OK("Hãy nhận hàng trước");

                    }

                    break;

                }

                case 984: {

                    // pho ban bang select

                    if (p.clan != null) {

                        switch (index) {

                            case 0: { // pvp bang
                                boolean isOpenPvp = activities.TimedDungeonManager.gI().isDungeonOpen("PVP_BANG")
                                        || (p.clan.map_create != null && p.clan.map_create.pvpBang != null);
                                if (!isOpenPvp && p.admin == 0) {
                                    p.getService().send_box_ThongBao_OK("Phó bản PvP Băng hiện chưa mở cửa!\n"
                                            + activities.TimedDungeonManager.gI().getDungeonStatusMessage("PVP_BANG"));
                                    break;
                                }
                                if (p.clan.map_create != null && p.clan.map_create.pvpBang != null) { // enter map

                                    Vgo vgo = new Vgo();

                                    vgo.map_go = new Zone[1];

                                    vgo.map_go[0] = p.clan.map_create;

                                    vgo.xnew = 530;

                                    vgo.ynew = 260;

                                    p.goto_map(vgo);

                                } else {

                                    boolean check_tt_tp = false;

                                    for (int i = 0; i < p.clan.members.size(); i++) {

                                        if (p.clan.members.get(i).name.equals(p.name)

                                                && (p.clan.members.get(i).levelInclan == 0

                                                || p.clan.members.get(i).levelInclan == 1)) {

                                            check_tt_tp = true;

                                            break;

                                        }

                                    }

                                    if (check_tt_tp) {

                                        if (p.tableTickOption == null) {

                                            p.tableTickOption = new functions.PvpBangTick(p);

                                            p.tableTickOption.idDialog = 11;

                                            p.tableTickOption.listP = new ArrayList<>();

                                            p.tableTickOption.listP.add(p);

                                            for (int i = 0; i < p.clan.members.size(); i++) {

                                                Player p0 = Zone.get_player_by_name_allmap(p.clan.members.get(i).name);

                                                if (p0 != null && p0.index_map != p.index_map && p0.map != null && p0.map.equals(p.map)) {

                                                    p.tableTickOption.listP.add(p0);

                                                }

                                            }

                                            p.tableTickOption.list_check = new byte[p.tableTickOption.listP.size()];

                                            p.tableTickOption.list_check[0] = 1;

                                            for (int i = 1; i < p.tableTickOption.list_check.length; i++) {

                                                p.tableTickOption.list_check[i] = 0;

                                            }

                                            p.tableTickOption.show("Phó bản PVP băng");

                                        } else {

                                            p.getService().send_box_ThongBao_OK("Băng đã đăng ký, đang chờ ghép đội!");

                                        }

                                    } else {

                                        p.getService().send_box_ThongBao_OK("Bạn không phải thuyền trưởng hoặc thuyền phó");

                                    }

                                }

                                break;

                            }



                            case 1: { // dang ky pho ban khong lo
                                boolean isOpenKhongLo = map.zones.TranChienKhongLo.isOpen()
                                        || (p.clan.map_create != null && p.clan.map_create.map_little_garden != null);
                                if (!isOpenKhongLo && p.admin == 0) {
                                    p.getService().send_box_ThongBao_OK("Phó bản Trận Chiến Khổng Lồ hiện chưa mở cửa!\n"
                                            + activities.TimedDungeonManager.gI().getDungeonStatusMessage("TRAN_CHIEN_KHONG_LO"));
                                    break;
                                }

                                if (p.clan.map_create != null && p.clan.map_create.map_little_garden != null) { // enter map

                                    if (p.clan.map_create.map_little_garden.clan1 != null && p.clan.map_create.map_little_garden.clan1.equals(p.clan)) {

                                        p.type_pk = 4;

                                    } else {

                                        p.type_pk = 5;

                                    }

                                    Vgo vgo = new Vgo();

                                    vgo.map_go = new Zone[1];

                                    vgo.map_go[0] = p.clan.map_create;

                                    vgo.xnew = 350;

                                    vgo.ynew = 260;

                                    p.goto_map(vgo);

                                } 

                                else {

                                    boolean check_tt_tp = false;

                                    for (int i = 0; i < p.clan.members.size(); i++) {

                                        if (p.clan.members.get(i).name.equals(p.name)

                                                && (p.clan.members.get(i).levelInclan == 0

                                                || p.clan.members.get(i).levelInclan == 1)) {

                                            check_tt_tp = true;

                                            break;

                                        }

                                    }

                                    if (check_tt_tp) {

                                        if (p.tableTickOption == null) {
                                            p.tableTickOption = new functions.PhoBanKhongLoTick(p);
                                            p.tableTickOption.listP = new ArrayList<>();
                                            p.tableTickOption.listP.add(p);
                                            for (int i = 0; i < p.clan.members.size(); i++) {
                                                Player p0 = Zone.get_player_by_name_allmap(p.clan.members.get(i).name);
                                                if (p0 != null && p0.index_map != p.index_map && p0.map != null && p0.map.equals(p.map)) {
                                                    p.tableTickOption.listP.add(p0);
                                                }
                                            }
                                            p.tableTickOption.list_check = new byte[p.tableTickOption.listP.size()];
                                            p.tableTickOption.list_check[0] = 1;
                                            for (int i = 1; i < p.tableTickOption.list_check.length; i++) {
                                                p.tableTickOption.list_check[i] = 0;
                                            }
                                            p.tableTickOption.show("Phó bản khổng lồ");
                                        } else {

                                            p.getService().send_box_ThongBao_OK("Băng đã đăng ký, đang chờ ghép đội!");

                                        }

                                    } else {

                                        p.getService().send_box_ThongBao_OK("Bạn không phải thuyền trưởng hoặc thuyền phó");

                                    }

                                }

                                break;

                            }



                            case 2: { 
                                // dang ky pho ban thu linh bien khoi
                                boolean isOpenThuLinh = map.zones.ThuLinhBienKhoi.isOpen()
                                        || (p.clan.map_create != null && p.clan.map_create.map_thuLinhBienKhoi != null);
                                if (!isOpenThuLinh && p.admin == 0) {
                                    p.getService().send_box_ThongBao_OK("Phó bản Thủ Lĩnh Biển Khơi hiện chưa mở cửa!\n"
                                            + activities.TimedDungeonManager.gI().getDungeonStatusMessage("THU_LINH_BIEN_KHOI"));
                                    break;
                                }

                                if (p.clan.map_create != null && p.clan.map_create.map_thuLinhBienKhoi != null) { // enter map
                                    Vgo vgo = new Vgo();
                                    vgo.map_go = new Zone[1];
                                    vgo.map_go[0] = p.clan.map_create;
                                    vgo.xnew = 100;
                                    vgo.ynew = 230;
                                    p.goto_map(vgo);
                                } else {
                                    if (p.clan.getTypeMem(p) == 0 || p.clan.getTypeMem(p) == 1) {
                                        if (p.tableTickOption == null) {
                                            sendDymanicMenu(p, 954, "Thủ lĩnh biển khơi", new String[]{"Biển Đông", "Biển Tây", "Biển Nam", "Biển Bắc",}, null);
                                        } else {
                                            p.getService().send_box_ThongBao_OK("Băng đã đăng ký, đang chờ ghép đội!");
                                        }
                                    } else {
                                        p.getService().send_box_ThongBao_OK("Bạn không phải thuyền trưởng hoặc thuyền phó");
                                    }
                                }

                                break;

                            }

                        }

                    }

                    break;

                }

                case 985: {

                    if (p.ship_pet == null) {

                        if (index == 0 || index == 1 || index == 2) {

                            p.typePirate = index;

                        } else {

                            p.typePirate = -1;

                        }

                        p.update_info_to_all();

                    }

                    break;

                }

                case 986: {

                    Menu_VanChuyenHang(p, index);

                    break;

                }

                case 987: {

                    if (index == 0) {

                        p.getService().Send_UI_Shop(1);

                    }

                    break;

                }

                case 1004: {

                    if (p.tableTickOption == null) {



                        p.tableTickOption = new functions.LienTangTick(p);

                        p.tableTickOption.listP = new ArrayList<>();

//                         p.tableTickOption.idDialog = 1;

                        p.tableTickOption.listP.add(p);

                        for (int i = 0; i < p.party.list.size(); i++) {

                            Player p0 = Zone.get_player_by_name_allmap(p.party.list.get(i).name);

                            if (p0 != null && p0.index_map != p.index_map && p0.map != null && p0.map.equals(p.map)) {

                                p.tableTickOption.listP.add(p0);

                            }

                        }

                        p.tableTickOption.list_check = new byte[p.tableTickOption.listP.size()];

                        p.tableTickOption.list_check[0] = 1;

                        for (int i = 1; i < p.tableTickOption.list_check.length; i++) {

                            p.tableTickOption.list_check[i] = 0;

                        }

                        p.tableTickOption.show("Phó bản Liên tầng");



                    }



                    break;

                }

                case 888: {

                    if (p.aiDonLevel < index) {

                        p.getService().send_box_ThongBao_OK("Hãy hoàn thành cấp độ " + (p.aiDonLevel + 3) + " trước");

                        return;

                    }

                    if (index >= 0 && index <= 12 && p.dungeon == null) {

                        int save = index;

                        p.data_yesno = new int[]{save};

                        if (save < 7) {

                            p.setyesNoDialog(new YesNoDialog(p, 52, "Thông báo",

                                    ("Vào phó bản đơn cấp độ " + (index + 3) + " cần 1 chìa khóa phó bản"),

                                    new String[]{"Đồng ý", "Hủy"}, new byte[]{2, 1}));

                            p.getService().startYesNo();

                        } else {

                            p.setyesNoDialog(new YesNoDialog(p, 52, "Thông báo",

                                    ("Vào phó bản đơn cấp độ " + (index + 3) + " cần 2 chìa khóa phó bản"),

                                    new String[]{"Đồng ý", "Hủy"}, new byte[]{2, 1}));

                            p.getService().startYesNo();

                        }

                    } else {

                        p.dungeon.mobs.clear();

                        for (int i = 0; i < p.dungeon.maps.size(); i++) {

                            p.dungeon.maps.get(i).stop_map();

                        }

                        p.dungeon = null;

                        p.getService().send_box_ThongBao_OK("Có lỗi xảy ra");

                    }

                    break;

                }

                case 988: {

                    if (p.aiDonLevel < index) {

                        p.getService().send_box_ThongBao_OK("Hãy hoàn thành cấp độ " + (p.aiDonLevel + 3) + " trước");

                        return;

                    }

                    if (index >= 0 && index <= 12 && p.dungeon == null) {

                        int save = index;

                        p.data_yesno = new int[]{save};

                        if (save < 7) {

                            p.setyesNoDialog(new YesNoDialog(p, 52, "Thông báo",

                                    ("Vào phó bản đơn cấp độ " + (index + 3) + " cần 1 chìa khóa phó bản"),

                                    new String[]{"Đồng ý", "Hủy"}, new byte[]{2, 1}));

                            p.getService().startYesNo();

                        } else {

                            p.setyesNoDialog(new YesNoDialog(p, 52, "Thông báo",

                                    ("Vào phó bản đơn cấp độ " + (index + 3) + " cần 2 chìa khóa phó bản"),

                                    new String[]{"Đồng ý", "Hủy"}, new byte[]{2, 1}));

                            p.getService().startYesNo();

                        }

                    } else {

                        p.dungeon.mobs.clear();

                        for (int i = 0; i < p.dungeon.maps.size(); i++) {

                            p.dungeon.maps.get(i).stop_map();

                        }

                        p.dungeon = null;

                        p.getService().send_box_ThongBao_OK("Có lỗi xảy ra");

                    }

                    break;

                }

                case 989: { // taixiu

                    if (!p.checkPassRuong()) {

                        return;

                    }

                    if (index == 0) {

                        p.getService().showTableTaiXiu(0);

                    } else if (index == 1) {

                        TaiXiuInfo t = Manager.gI().TaiXiu().get_my_result(p);

                        if (t != null) {

                            if (t.isReceive == 0) {

                                t.isReceive = 1;

                                p.update_vang(t.money);

                                p.updateMoney();

                                p.getService().send_box_ThongBao_OK("Nhận " + t.money + " beri");

                                Manager.gI().TaiXiu().remove_result(p);

                            } else {

                                p.getService().send_box_ThongBao_OK("Đã nhận rồi!");

                            }

                        } else {

                            p.getService().send_box_ThongBao_OK("Không thấy thông tin");

                        }

                    }

                    break;

                }

                case 990: {

                    if (index == 0) {

                        if (event.EventManager.isActive(13)) {

                            new itemz.rebuilds.GhepRuongWorldcup().show_table(p);

                        } else {

                            new itemz.rebuilds.GhepVeTuoiTho().show_table(p);

                        }

                    } else if (index == 1) {

                        p.getService().Send_UI_Shop(12);

                    }

                    break;

                }

                case -140: { // wipper

                    if (index == 0) {

                        sendDymanicMenu(p, 980, "WIPPER", new String[]{"Ghép sách công thức", "Ghép vỏ ốc",

                            "Chế tạo dial", "Cường hóa dial", "Đục lỗ dial"}, null);

                    } else if (index == 1) {

                        if (p.party == null || p.party.list.size() < 2

                                || !p.party.list.get(0).name.equals(p.name)) {

                            p.getService().send_box_ThongBao_OK("Hãy tạo nhóm từ 2 ngư???i trở lên để vào phó bản");

                            return;

                        }

                        if (p.tableTickOption == null) {



                            p.tableTickOption = new functions.TTVTTick(p);

                            p.tableTickOption.listP = new ArrayList<>();

//                             p.tableTickOption.idDialog = 3;

                            p.tableTickOption.listP.add(p);

                            for (int i = 0; i < p.party.list.size(); i++) {

                                Player p0 = Zone.get_player_by_name_allmap(p.party.list.get(i).name);

                                if (p0 != null && p0.index_map != p.index_map && p0.map != null && p0.map.equals(p.map)) {

                                    p.tableTickOption.listP.add(p0);

                                }

                            }

                            p.tableTickOption.list_check = new byte[p.tableTickOption.listP.size()];

                            p.tableTickOption.list_check[0] = 1;

                            for (int i = 1; i < p.tableTickOption.list_check.length; i++) {

                                p.tableTickOption.list_check[i] = 0;

                            }

                            p.tableTickOption.show("Thử Thách vệ thần");

                        }



                    }

                    break;

                }

                case -86: { // miss pho ban

                    if (index == 0) {

                        if (p.party != null && p.party.list.size() > 1) {

                            if (!p.party.list.get(0).equals(p)) {

                                p.getService().send_box_ThongBao_OK("Chỉ có trưởng nhóm mới có thể bắt đầu phó bản");

                                break;

                            }

                        }

                        if (p.tableTickOption == null) {

                            p.data_yesno = new int[]{67};

                            p.tableTickOption = new functions.MrCandleTick(p);

                            p.tableTickOption.listP = new ArrayList<>();

                            p.tableTickOption.listP.add(p);

                            if (p.party != null && p.party.list.size() > 1) {

                                for (int i = 0; i < p.party.list.size(); i++) {

                                    Player p0 = Zone.get_player_by_name_allmap(p.party.list.get(i).name);

                                    if (p0 != null && p0.index_map != p.index_map && p0.map != null && p0.map.equals(p.map)) {

                                        p.tableTickOption.listP.add(p0);

                                    }

                                }

                            }

                            p.tableTickOption.list_check = new byte[p.tableTickOption.listP.size()];

                            p.tableTickOption.list_check[0] = 1;

                            for (int i = 1; i < p.tableTickOption.list_check.length; i++) {

                                p.tableTickOption.list_check[i] = 0;

                            }

                            p.tableTickOption.show("Bộ đôi Mr Candle");

                        }

                        break;

                    } else if (index == 1) {

                        // dang ky pho ban khong lo

                        if (p.clan.map_create != null) { // enter map

                            //

                            if (p.clan.map_create.map_little_garden.clan1.equals(p.clan)) {

                                p.type_pk = 4;

                            } else {

                                p.type_pk = 5;

                            }

                            //

                            Vgo vgo = new Vgo();

                            vgo.map_go = new Zone[1];

                            vgo.map_go[0] = p.clan.map_create;

                            vgo.xnew = 350;

                            vgo.ynew = 260;

                            p.goto_map(vgo);

                        } else {

                            boolean check_tt_tp = false;

                            for (int i = 0; i < p.clan.members.size(); i++) {

                                if (p.clan.members.get(i).name.equals(p.name)

                                        && (p.clan.members.get(i).levelInclan == 0

                                        || p.clan.members.get(i).levelInclan == 1)) {

                                    check_tt_tp = true;

                                    break;

                                }

                            }

                            if (check_tt_tp) {

                                if (p.tableTickOption == null) {

                                    int time_h = LocalTime.now().getHourOfDay();

                                    if ((ZUtil.is_DayofWeek(2) || ZUtil.is_DayofWeek(4) || ZUtil.is_DayofWeek(6))

                                            && time_h == 21) {

                                        p.tableTickOption = new functions.PhoBanKhongLoTick(p);

                                        p.tableTickOption.listP = new ArrayList<>();

//                                         p.tableTickOption.idDialog = 0;

                                        p.tableTickOption.listP.add(p);

                                        for (int i = 0; i < p.clan.members.size(); i++) {

                                            Player p0 = Zone.get_player_by_name_allmap(p.clan.members.get(i).name);

                                            if (p0 != null && p0.index_map != p.index_map && p0.map != null && p0.map.equals(p.map)) {

                                                p.tableTickOption.listP.add(p0);

                                            }

                                        }

                                        p.tableTickOption.list_check = new byte[p.tableTickOption.listP.size()];

                                        p.tableTickOption.list_check[0] = 1;

                                        for (int i = 1; i < p.tableTickOption.list_check.length; i++) {

                                            p.tableTickOption.list_check[i] = 0;

                                        }

                                        p.tableTickOption.show("Phó bản khổng lồ");

                                    } else {

                                        p.getService().send_box_ThongBao_OK("Phó bản hoạt động vào 21h tối thứ 3, 5, 7");

                                    }

                                } else {

                                    p.getService().send_box_ThongBao_OK("Băng đã đăng ký, đang chờ ghép đội!");

                                }

                            } else {

                                p.getService().send_box_ThongBao_OK("Bạn không phải thuyền trưởng");

                            }

                        }

                        break;

                    } else if (index == 2) {



                    }



                    break;

                }



                case -77: { // ms gym

                    break;

                }

                case -154: { // pudding

                    switch (index) {

                        case 0: {

                            event.Event ev = event.EventManager.gI().getEvent(3);

                            if (ev != null) {

                                ev.openShop(p);

                            }

                            break;

                        }

                        case 1: {

                            String txt = "Boss Thế Giới\\n"

                                    + "Chào mừng bạn đến với Phó Bản BIGMOM. "

                                    + "BigMom xuất hiện từ 21h00 tối.\\b"

                                    + "Chào mừng bạn đến với Phó Bản BIGMOM. "

                                    + "Chào mừng bạn đến với Phó Bản BIGMOM. "

                                    + "Chào mừng bạn đến với Phó Bản BIGMOM. "

                                    + "Chào mừng bạn đến với Phó Bản BIGMOM. "

                                    + "Chào mừng bạn đến với Phó Bản BIGMOM. "

                                    + "Chúc các bạn có những trải nghiệm vui vẻ.";

                            p.getService().Help_From_Server(-154, txt);



                            break;

                        }

                    }

                    break;



                }

//                case -78: { // ms gukong

//                    switch (index) {

//                        case 0: {

//                            if (WorldWar.runnning) {

//                                p.getService().send_box_ThongBao_OK(WorldWar.findTop());

//                            } else {

//                                p.getService().send_box_ThongBao_OK("Không thể thực hiện thao tác này khi chưa diễn ra lễ hội");

//                            }

//                            break;

//                        }

//                        case 1: {

//                            if (WorldWar.runnning) {

//                                p.getService().send_box_ThongBao_OK("Chưa tới thời gian nhận quà");

//                                return;

//                            }

//                            if (!WorldWar.top3.isEmpty()) {

//                                Integer value = WorldWar.top3.get(p.name);

//                                if (value != null && value == 1) {

//                                    // quÃƒÂ  top 1

//                                    List<GiftBox> listGift = new ArrayList<>();

//                                    {

//                                        GiftBox gb = new GiftBox();

//                                        ItemTemplate4 it_temp4 = ItemTemplate4.get_it_by_id(0);

//                                        if (it_temp4 != null) {

//                                            gb.id = it_temp4.id;

//                                            gb.type = 4;

//                                            gb.name = it_temp4.name;

//                                            gb.icon = it_temp4.icon;

//                                            gb.num = 20_00_000;

//                                            gb.color = 0;

//                                            listGift.add(gb);

//                                        }

//                                    }

//                                    {

//                                        GiftBox gb = new GiftBox();

//                                        ItemTemplate4 it_temp4 = ItemTemplate4.get_it_by_id(1);

//                                        if (it_temp4 != null) {

//                                            gb.id = it_temp4.id;

//                                            gb.type = 4;

//                                            gb.name = it_temp4.name;

//                                            gb.icon = it_temp4.icon;

//                                            gb.num = 10_000;

//                                            gb.color = 0;

//                                            listGift.add(gb);

//                                        }

//                                    }

//                                    {

//                                        GiftBox gb = new GiftBox();

//                                        ItemTemplate4 it_temp4 = ItemTemplate4.get_it_by_id(((p.level < 10 ? 10 : p.level) / 10) + 694);

//                                        if (it_temp4 != null) {

//                                            gb.id = it_temp4.id;

//                                            gb.type = 4;

//                                            gb.name = it_temp4.name;

//                                            gb.icon = it_temp4.icon;

//                                            gb.num = 10;

//                                            gb.color = 0;

//                                            listGift.add(gb);

//                                        }

//                                    }

//                                    Service.send_gift(p, 1, "Top 1 Lễ Hội", "Nhận được", listGift, true);

//                                    WorldWar.top3.remove(p.name);

//                                }

//                                if (value != null && value == 2) {

//                                    // quÃƒÂ  top 2

//

//                                    List<GiftBox> listGift = new ArrayList<>();

//                                    {

//                                        GiftBox gb = new GiftBox();

//                                        ItemTemplate4 it_temp4 = ItemTemplate4.get_it_by_id(0);

//                                        if (it_temp4 != null) {

//                                            gb.id = it_temp4.id;

//                                            gb.type = 4;

//                                            gb.name = it_temp4.name;

//                                            gb.icon = it_temp4.icon;

//                                            gb.num = 15_00_000;

//                                            gb.color = 0;

//                                            listGift.add(gb);

//                                        }

//                                    }

//                                    {

//                                        GiftBox gb = new GiftBox();

//                                        ItemTemplate4 it_temp4 = ItemTemplate4.get_it_by_id(1);

//                                        if (it_temp4 != null) {

//                                            gb.id = it_temp4.id;

//                                            gb.type = 4;

//                                            gb.name = it_temp4.name;

//                                            gb.icon = it_temp4.icon;

//                                            gb.num = 8_000;

//                                            gb.color = 0;

//                                            listGift.add(gb);

//                                        }

//                                    }

//                                    {

//                                        GiftBox gb = new GiftBox();

//                                        ItemTemplate4 it_temp4 = ItemTemplate4.get_it_by_id(((p.level < 10 ? 10 : p.level) / 10) + 694);

//                                        if (it_temp4 != null) {

//                                            gb.id = it_temp4.id;

//                                            gb.type = 4;

//                                            gb.name = it_temp4.name;

//                                            gb.icon = it_temp4.icon;

//                                            gb.num = 8;

//                                            gb.color = 0;

//                                            listGift.add(gb);

//                                        }

//                                    }

//                                    Service.send_gift(p, 1, "Top 2 Lễ Hội", "Nhận được", listGift, true);

//                                    WorldWar.top3.remove(p.name);

//                                }

//                                if (value != null && value == 3) {

//                                    // quÃƒÂ  top 3

//

//                                    List<GiftBox> listGift = new ArrayList<>();

//                                    {

//                                        GiftBox gb = new GiftBox();

//                                        ItemTemplate4 it_temp4 = ItemTemplate4.get_it_by_id(0);

//                                        if (it_temp4 != null) {

//                                            gb.id = it_temp4.id;

//                                            gb.type = 4;

//                                            gb.name = it_temp4.name;

//                                            gb.icon = it_temp4.icon;

//                                            gb.num = 10_00_000;

//                                            gb.color = 0;

//                                            listGift.add(gb);

//                                        }

//                                    }

//                                    {

//                                        GiftBox gb = new GiftBox();

//                                        ItemTemplate4 it_temp4 = ItemTemplate4.get_it_by_id(1);

//                                        if (it_temp4 != null) {

//                                            gb.id = it_temp4.id;

//                                            gb.type = 4;

//                                            gb.name = it_temp4.name;

//                                            gb.icon = it_temp4.icon;

//                                            gb.num = 5_000;

//                                            gb.color = 0;

//                                            listGift.add(gb);

//                                        }

//                                    }

//                                    {

//                                        GiftBox gb = new GiftBox();

//                                        ItemTemplate4 it_temp4 = ItemTemplate4.get_it_by_id(((p.level < 10 ? 10 : p.level) / 10) + 694);

//                                        if (it_temp4 != null) {

//                                            gb.id = it_temp4.id;

//                                            gb.type = 4;

//                                            gb.name = it_temp4.name;

//                                            gb.icon = it_temp4.icon;

//                                            gb.num = 5;

//                                            gb.color = 0;

//                                            listGift.add(gb);

//                                        }

//                                    }

//                                    Service.send_gift(p, 1, "Top 3 Lễ Hội", "Nhận được", listGift, true);

//                                    WorldWar.top3.remove(p.name);

//                                }

//                            }

//                            /* if (!WorldWar.wins.isEmpty() && WorldWar.wins.containsKey(p.name)) {

//                                // quÃƒÂ  phe win, top 3 nhÃ¡ÂºÂ­n thÃƒÂªm Ã„â€Ã†Â°Ã¡Â»Â£c quÃƒÂ  nÃƒÂ y

//

//                                List<GiftBox> listGift = new ArrayList<>();

//                                {

//                                    GiftBox gb = new GiftBox();

//                                    ItemTemplate4 it_temp4 = ItemTemplate4.get_it_by_id(0);

//                                    if (it_temp4 != null) {

//                                        gb.id = it_temp4.id;

//                                        gb.type = 4;

//                                        gb.name = it_temp4.name;

//                                        gb.icon = it_temp4.icon;

//                                        gb.num = 1_00_000;

//                                        gb.color = 0;

//                                        listGift.add(gb);

//                                    }

//                                }

//                                {

//                                    GiftBox gb = new GiftBox();

//                                    ItemTemplate4 it_temp4 = ItemTemplate4.get_it_by_id(1);

//                                    if (it_temp4 != null) {

//                                        gb.id = it_temp4.id;

//                                        gb.type = 4;

//                                        gb.name = it_temp4.name;

//                                        gb.icon = it_temp4.icon;

//                                        gb.num = 2_000;

//                                        gb.color = 0;

//                                        listGift.add(gb);

//                                    }

//                                }

//                                {

//                                    GiftBox gb = new GiftBox();

//                                    ItemTemplate4 it_temp4 = ItemTemplate4.get_it_by_id(((p.level < 10 ? 10 : p.level) / 10) + 695);

//                                    if (it_temp4 != null) {

//                                        gb.id = it_temp4.id;

//                                        gb.type = 4;

//                                        gb.name = it_temp4.name;

//                                        gb.icon = it_temp4.icon;

//                                        gb.num = 1;

//                                        gb.color = 0;

//                                        listGift.add(gb);

//                                    }

//                                }

//                                Service.send_gift(p, 1, "Phe chiến thắng", "Nhận được", listGift, true);

//                                WorldWar.wins.remove(p.name);

//                            }*/

//                            break;

//                        }

//                        case 2: {

//                            String txt = "Lễ Hội Hải Tặc\\n"

//                                    + "Chào mừng bạn đến với Lễ hội hải tặc (code by Kho Báu Hải Tặc). "

//                                    + "Bắt đầu từ 17h00 sáng đến 19h00 tối.\\b"

//                                    + "Chào mừng bạn đến với Lễ hội hải tặc (code by Kho Báu Hải Tặc). "

//                                    + "Chào mừng bạn đến với Lễ hội hải tặc (code by Kho Báu Hải Tặc). "

//                                    + "Chào mừng bạn đến với Lễ hội hải tặc (code by Kho Báu Hải Tặc). "

//                                    + "Bạn cũng có thể trùy lùng những kẻ đứng đầu bằng rada được bán tại Shop thức ăn.\\b"

//                                    + "Chào mừng bạn đến với Lễ hội hải tặc (code by Kho Báu Hải Tặc). "

//                                    + "Chào mừng bạn đến với Lễ hội hải tặc (code by Kho Báu Hải Tặc). "

//                                    + "Chúc các bạn có những trải nghiệm vui vẻ.";

//                            p.getService().Help_From_Server(-78, txt);

//

//                            break;

//                        }

//                    }

//                    break;

//

//                }



                case -73: { // croket

                    break;

                }



                case 991: { // hoan my kich an

                    switch (index) {

                        case 0: {

                            Rebuild_Item.show_table(p, 6);

                            break;

                        }

                        case 1: {

                            Rebuild_Item.show_table(p, 7);

                            break;

                        }

                        case 2: {

                            Rebuild_Item.show_table(p, 8);

                            break;

                        }

                    }

                    break;

                }

                case -106:

                case -91:

                case -71:

                case -48: {

                    Menu_Zosaku(p, index);

                    break;

                }

                case 992: {

                    Join_Item.show_table(p, index);

                    break;

                }

                case 9955: {

                    if (index >= 0 && index < achievement.ArchiPrivatePass.SEASONS.length) {

                        String selectedSeason = achievement.ArchiPrivatePass.SEASONS[index];

                        achievement.ArchiPrivatePass.show_table(p, selectedSeason);

                    }

                    break;

                }

                case 9933: {
                    switch (index) {
                        case 0: { // Tích nạp ngày
                            p.typeShopTichNap = 2;
                            p.typeShopTichNapSuKien = 0;
                            p.refreshRecharge();
                            p.ensureTichHangNgayCheck();
                            zabstracts.AbsListTichNap napNgay = zabstracts.AbsListTichNap.get(2);
                            if (napNgay != null) {
                                napNgay.showTable(p);
                            } else {
                                p.getService().send_box_ThongBao_OK("Chức năng Tích nạp ngày đang bảo trì!");
                            }
                            break;
                        }
                        case 1: { // Tích nạp tổng
                            p.typeShopTichNap = 0;
                            p.typeShopTichNapSuKien = 0;
                            p.refreshRecharge();
                            p.ensureTichNapCheck();
                            zabstracts.AbsListTichNap tichNap = zabstracts.AbsListTichNap.get(0);
                            if (tichNap != null) {
                                tichNap.showTable(p);
                            } else {
                                p.getService().send_box_ThongBao_OK("Chức năng Tích nạp tổng đang bảo trì!");
                            }
                            break;
                        }
                    }
                    break;
                }

                case 993: {
                    if (Manager.gI().isTopRacingRunning()) {
                        p.getService().send_box_ThongBao_OK("Đang trong thời gian đua Top Cao Thủ, tạm thời đóng nạp/đổi Extol để đảm bảo công bằng cho người chơi cày cuốc!");
                        return;
                    }
               switch (index) {

                   case 0: {

                       if (p.getConnStatus() != 1) {

                           p.getService().send_box_ThongBao_OK("Chưa Kích hoạt không thể đổi ");

                           return;

                       }

                       p.setyesNoDialog(new YesNoDialog(10, "Thông báo",

                               "Bạn muốn đổi 3000 ruby sang 2.000.000 extol?", new String[]{"Đồng ý", "Hủy"},

                               new byte[]{2, 1}, value -> {

                                   if (value == 0) { // Đồng ý

                                       if (p.get_ngoc() < 3_000) {

                                           p.getService().send_box_ThongBao_OK("Bạn không đủ 3.000 Ruby");

                                           return;

                                       }

                                       p.update_ngoc_ex(-3_000);

                                       p.updateVnd(2_000_000);

                                       p.updateMoney();

                                       p.getService().send_box_ThongBao_OK("Bạn đã đổi thành công 2.000.000 Extol.");

                                       zLog.gI().add_log(p, "Đổi extol");

                                   }

                               }));

                       p.getService().startYesNo();

                       break;

                   }

                   case 1: {

                       p.setinputDialog(new model.InputDialog(4, "Đổi Extol Sang Ruby", new String[]{"Extol muốn đổi : Tl 1.000 extol = 1rb"}, inputs -> {

                           if (inputs.length == 1) {

                               if (!ZUtil.isnumber(inputs[0])) {

                                   p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ");

                                   return;

                               }

                               long extolVal = Long.parseLong(inputs[0]) * 1000L;

                               if (extolVal <= 0) {

                                   p.getService().send_box_ThongBao_OK("Số nhập không hợp lệ");

                                   return;

                               }

                               if (p.get_vnd() < extolVal) {

                                   p.getService().send_box_ThongBao_OK("Bạn không đủ " + ZUtil.number_format(extolVal) + " extol");

                                   return;

                               }

                               int rubyVal = (int)(extolVal / 1000L);

                               p.setyesNoDialog(new YesNoDialog(9, "Thông báo",

                                       "Bạn có thật sự muốn đổi " + ZUtil.number_format(extolVal) + " Extol để đổi lấy " + ZUtil.number_format(rubyVal) + " Ruby không?",

                                       new String[]{"Đồng ý", "Hủy"}, new byte[]{2, 1}, val -> {

                                           if (val == 0) {

                                               long realExtol = (long) rubyVal * 1000L;

                                               if (p.get_vnd() < realExtol) {

                                                    p.getService().send_box_ThongBao_OK("Bạn không đủ " + ZUtil.number_format(realExtol) + " extol");

                                                    return;

                                               }

                                               if (realExtol < 0) {

                                                   zLog.gI().add_log("BUG", "Bug ne 4: " + realExtol);

                                                   System.err.println("Bug ne 4: " + realExtol);

                                                   return;

                                               }

                                               p.updateVnd(-realExtol);

                                               p.update_ngoc(rubyVal);

                                               p.updateMoney();

                                               p.getService().send_box_ThongBao_OK("Bạn đã đổi thành công " + ZUtil.number_format(realExtol) + " extol ra "

                                                       + ZUtil.number_format(rubyVal) + " Ruby.");

                                           }

                                       }));

                               p.getService().startYesNo();

                           }

                       }));

                       p.getService().startInput();

                       break;

                   }

                   case 2: {

                       p.sendInput("Quà tặng máy chủ", new String[]{"Nhập giftcode"}, name -> {

                            GiftTemplate.execute(p, name);

                        });

                       break;

                   }

               }

                    break;

                }

                case 885: {

                    switch (index) {

                        case 0: {

                            sendDymanicMenu(p, 776, "Cẩm Thạch",

                                    new String[]{"Cẩm thạch - Topaz siêu cấp", "Cẩm thạch - Ruby siêu cấp", "Cẩm thạch - Ngọc siêu cấp", "Cẩm thạch - Saphia siêu cấp", "Cẩm thạch - Thạch anh siêu cấp"},

                                    new short[]{282, 282, 283, 284, 285});

                            break;

                        }

                        case 1: {

                            sendDymanicMenu(p, 777, "Topaz",

                                    new String[]{"Cẩm thạch - Topaz siêu cấp", "Cẩm thạch - Ruby siêu cấp", "Cẩm thạch - Ngọc siêu cấp", "Cẩm thạch - Saphia siêu cấp", "Cẩm thạch - Thạch anh siêu cấp"},

                                    new short[]{286, 287, 288, 289, 290});

                            break;

                        }

                        case 2: {

                            sendDymanicMenu(p, 778, "Ruby",

                                    new String[]{"Cẩm thạch - Topaz siêu cấp", "Cẩm thạch - Ruby siêu cấp", "Cẩm thạch - Ngọc siêu cấp", "Cẩm thạch - Saphia siêu cấp", "Cẩm thạch - Thạch anh siêu cấp"},

                                    new short[]{291, 292, 293, 294, 295});

                            break;

                        }

                        case 3: {

                            sendDymanicMenu(p, 779, "Ngọc lục bảo",

                                    new String[]{"Cẩm thạch - Topaz siêu cấp", "Cẩm thạch - Ruby siêu cấp", "Cẩm thạch - Ngọc siêu cấp", "Cẩm thạch - Saphia siêu cấp", "Cẩm thạch - Thạch anh siêu cấp"},

                                    new short[]{296, 297, 298, 299, 300});

                            break;

                        }

                        case 4: {

                            sendDymanicMenu(p, 780, "Saphia",

                                    new String[]{"Cẩm thạch - Topaz siêu cấp", "Cẩm thạch - Ruby siêu cấp", "Cẩm thạch - Ngọc siêu cấp", "Cẩm thạch - Saphia siêu cấp", "Cẩm thạch - Thạch anh siêu cấp"},

                                    new short[]{301, 302, 303, 304, 305});

                            break;

                        }

                        case 5: {

                            sendDymanicMenu(p, 781, "Thạch anh tím",

                                    new String[]{"Cẩm thạch - Topaz siêu cấp", "Cẩm thạch - Ruby siêu cấp", "Cẩm thạch - Ngọc siêu cấp", "Cẩm thạch - Saphia siêu cấp", "Cẩm thạch - Thạch anh siêu cấp"},

                                    new short[]{306, 307, 308, 309, 310});

                            break;

                        }

                        case 6: {

                            sendDymanicMenu(p, 782, "Hổ phách",

                                    new String[]{"Hổ phách - Cẩm thạch siêu cấp", "Hổ phách - Topaz siêu cấp", "Hổ phách - Ruby siêu cấp", "Hổ phách - Ngọc siêu cấp", "Hổ phách - Saphia siêu cấp", "Hổ phách - Thạch anh siêu cấp"},

                                    new short[]{311, 312, 313, 314, 315, 316});

                            break;

                        }

                    }

                    break;

                }

                case 886: {

                    switch (index) {

                        case 0: {

                            sendDymanicMenu(p, 783, "Cẩm Thạch",

                                    new String[]{"Topaz - Cẩm thạch siêu cấp", "Topaz - Ruby siêu cấp", "Topaz - Ngọc siêu cấp", "Topaz - Saphia siêu cấp", "Topaz - Thạch Anh siêu cấp"},

                                    new short[]{282, 282, 283, 284, 285});

                            break;

                        }

                        case 1: {

                            sendDymanicMenu(p, 784, "Topaz",

                                    new String[]{"Ruby - Cẩm thạch siêu cấp", "Ruby - Topaz siêu cấp", "Ruby - Ngọc siêu cấp", "Ruby - Saphia siêu cấp", "Ruby - Thạch anh siêu cấp"},

                                    new short[]{286, 287, 288, 289, 290});

                            break;

                        }

                        case 2: {

                            sendDymanicMenu(p, 785, "Ruby",

                                    new String[]{"Ngọc - Cẩm thạch siêu cấp", "Ngọc - Topaz siêu cấp", "Ngọc - Ruby siêu cấp", "Ngọc - Saphia siêu cấp", "Ngọc - Thạch anh siêu cấp"},

                                    new short[]{291, 292, 293, 294, 295});

                            break;

                        }

                        case 3: {

                            sendDymanicMenu(p, 786, "Ngọc lục bảo",

                                    new String[]{"Saphia - Cẩm thạch siêu cấp", "Saphia - Topaz siêu cấp", "Saphia - Ruby siêu cấp", "Saphia - Ngọc siêu cấp", "Saphia - Thạch anh siêu cấp"},

                                    new short[]{296, 297, 298, 299, 300});

                            break;

                        }

                        case 4: {

                            sendDymanicMenu(p, 787, "Saphia",

                                    new String[]{"Thạch anh - Cẩm thạch siêu cấp", "Thạch anh - Topaz siêu cấp", "Thạch anh - Ruby siêu cấp", "Thạch anh - Ngọc siêu cấp", "Thạch anh - Saphia siêu cấp"},

                                    new short[]{301, 302, 303, 304, 305});

                            break;

                        }

                        case 5: {

                            sendDymanicMenu(p, 788, "Thạch anh tím",

                                    new String[]{"Thạch anh - Cẩm thạch siêu cấp", "Thạch anh - Topaz siêu cấp", "Thạch anh - Ruby siêu cấp", "Thạch anh - Ngọc siêu cấp", "Thạch anh - Saphia siêu cấp"},

                                    new short[]{306, 307, 308, 309, 310});

                            break;

                        }

                        case 6: {

                            sendDymanicMenu(p, 789, "Hổ phách",

                                    new String[]{"Hổ phách - Cẩm thạch siêu cấp", "Hổ phách - Topaz siêu cấp", "Hổ phách - Ruby siêu cấp", "Hổ phách - Ngọc siêu cấp", "Hổ phách - Saphia siêu cấp", "Hổ phách - Thạch anh siêu cấp"},

                                    new short[]{311, 312, 313, 314, 315, 316});

                            break;

                        }

                    }

                    break;

                }

                case 776: {

                    switch (index) {

                        case 0: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(241);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 241;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 691, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 1: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(242);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 242;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 691, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 2: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(243);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 243;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 691, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 3: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(244);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 244;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 691, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 4: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(245);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 245;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 691, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }



                    }

                    break;

                }

                case 777: {

                    switch (index) {

                        case 0: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(246);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 246;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 691, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 1: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(247);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 247;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 691, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 2: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(248);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 248;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 691, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 3: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(249);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 249;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 691, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 4: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(250);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 250;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 691, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }



                    }

                    break;

                }

                case 778: {

                    switch (index) {

                        case 0: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(251);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 251;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 691, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 1: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(252);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 252;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 691, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 2: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(253);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 253;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 691, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 3: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(254);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 254;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 691, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 4: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(255);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 255;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 691, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }



                    }

                    break;

                }

                case 779: {

                    switch (index) {

                        case 0: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(256);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 256;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 691, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 1: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(257);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 257;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 691, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 2: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(258);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 258;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 691, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 3: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(259);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 259;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 691, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 4: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(260);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 260;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 691, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }



                    }

                    break;

                }

                case 780: {

                    switch (index) {

                        case 0: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(261);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 261;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 691, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 1: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(262);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 262;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 691, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 2: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(263);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 263;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 691, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 3: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(264);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 264;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 691, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 4: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(265);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 245;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 691, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }



                    }

                    break;

                }

                case 781: {

                    switch (index) {

                        case 0: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(266);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 266;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 691, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 1: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(267);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 267;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 691, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 2: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(268);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 268;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 691, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 3: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(269);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 269;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 691, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 4: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(270);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 270;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 691, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                    }

                    break;

                }

                case 782: {

                    switch (index) {

                        case 0: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(368);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 368;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 691, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 1: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(369);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 369;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 691, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 2: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(370);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 370;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 691, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 3: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(371);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 371;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 691, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 4: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(372);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 372;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 691, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 5: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(373);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 373;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 691, 1);

                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }



                    }

                    break;

                }

                case 783: {

                    switch (index) {

                        case 0: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(647);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 647;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 732, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 1: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(648);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 648;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 732, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 2: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(649);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 649;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 732, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 3: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(650);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 650;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 732, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 4: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(651);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 651;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 732, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }



                    }

                    break;

                }

                case 784: {

                    switch (index) {

                        case 0: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(652);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 652;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 732, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 1: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(653);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 653;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 732, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 2: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(654);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 654;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 732, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 3: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(655);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 655;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 732, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 4: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(656);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 656;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 732, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }



                    }

                    break;

                }

                case 785: {

                    switch (index) {

                        case 0: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(657);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 657;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 732, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 1: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(658);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 658;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 732, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 2: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(659);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 659;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 732, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 3: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(660);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 660;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 732, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 4: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(661);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 661;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 732, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }



                    }

                    break;

                }

                case 786: {

                    switch (index) {

                        case 0: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(662);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 662;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 732, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 1: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(663);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 663;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 732, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 2: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(664);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 664;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 732, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 3: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(665);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 665;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 732, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 4: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(666);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 666;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 732, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }



                    }

                    break;

                }

                case 787: {

                    switch (index) {

                        case 0: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(667);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 667;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 732, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 1: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(668);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 668;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 732, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 2: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(669);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 669;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 732, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 3: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(670);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 670;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 732, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 4: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(671);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 671;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 732, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                    }

                    break;

                }

                case 788: {

                    switch (index) {

                        case 0: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(672);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 672;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 732, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 1: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(673);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 673;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 732, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 2: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(674);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 674;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 732, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 3: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(675);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 675;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 732, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 4: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(676);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 676;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 732, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }



                    }

                    break;

                }

                case 789: {

                    switch (index) {

                        case 0: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(677);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 677;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 732, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 1: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(678);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 678;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 732, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 2: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(679);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 679;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 732, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 3: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(680);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 680;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 732, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 4: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(681);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 681;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 732, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }

                        case 5: {

                            List<GiftBox> list = new ArrayList<>();

                            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(682);

                            if (itemTemplate4 != null) {

                                GiftBox gb4 = new GiftBox();

                                gb4.id = (short) 682;

                                gb4.type = 4;

                                gb4.name = itemTemplate4.name;

                                gb4.icon = itemTemplate4.icon;

                                gb4.num = 1;

                                gb4.color = 0;

                                list.add(gb4);

                                p.item.remove_item47(4, 732, 1);



                            }

                            if (list.size() > 0) {

                                Service.send_gift(p, 1, "Mở rương", "Nhận được", list, true);

                            }

                            break;

                        }



                    }

                    break;

                }

                case 499: {

                    switch (index) {

                        case 0: {

                            p.setyesNoDialog(new YesNoDialog(p, 64, "Thông báo",

                                    "Bạn có muốn gia nhập Hải Quân không ?", new String[]{"Có", "Không"},

                                    new byte[]{2, 1}));

                            p.getService().startYesNo();

                            break;

                        }

                        case 1: {

                            p.setyesNoDialog(new YesNoDialog(p, 66, "Thông báo",

                                    "Bạn có muốn gia nhập Hải Quân không ?", new String[]{"Có", "Không"},

                                    new byte[]{2, 1}));

                            p.getService().startYesNo();

                            break;

                        }



                    }

                    break;

                }



                case 994: {

                    if (index == 0) {

                        if (p.mbv.isBlank()) {

                            new model.InputDialog(p, 22, "Nhập mã bảo vệ:", new String[]{""}).startInput();

                        } else {

                            p.getService().send_box_ThongBao_OK("Hiện tại đang dùng mã khoá bảo vệ khác!");

                        }

                    } else if (index == 1) {

                        String txt

                                = "Khóa bảo vệ\nAi cũng có những món đồ mình rất quý trọng và không muốn mất nó.\nChức năng khóa bảo vệ sẽ giúp "

                                + "bạn làm điều đó.\b"

                                + "Sau khi đăng ký đặt khóa thì các thao tác có ảnh hưởng đến tài khoản của bạn sẽ phải nhập đúng mã để xác nhận đó "

                                + "chính là bạn chứ không phải ai khác.\\b"

                                + "bạn làm điều đó.\b"

                                + " đẹp trai.";

                        switch (p.map.template.id) {

                            case 1: {

                                p.getService().Help_From_Server(-3, txt);

                                break;

                            }

                            case 9: {

                                p.getService().Help_From_Server(-15, txt);

                                break;

                            }

                            case 17: {

                                p.getService().Help_From_Server(-23, txt);

                                break;

                            }

                            case 25: {

                                p.getService().Help_From_Server(-30, txt);

                                break;

                            }

                            case 33: {

                                p.getService().Help_From_Server(-38, txt);

                                break;

                            }

                            case 49: {

                                p.getService().Help_From_Server(-69, txt);

                                break;

                            }

                            case 69: {

                                p.getService().Help_From_Server(-75, txt);

                                break;

                            }

                            case 83: {

                                p.getService().Help_From_Server(-88, txt);

                                break;

                            }

                        }

                    } else if (index == 2) {

                        if (p.mbv.isBlank()) {

                            p.getService().send_box_ThongBao_OK("Hiện tại chưa đăng ký mã khoá bảo vệ!");

                        } else {

                            if (p.timeHuyMbv != -1) {

                                long t = p.timeHuyMbv - System.currentTimeMillis();

                                t /= 1000;

                                if (t < 0) {

                                    t = 0;

                                }

                                p.getService().send_box_ThongBao_OK("Hiện tại đã đăng ký hủy mã. Th???i gian còn lại: " + t + "s");

                            } else {

                                p.data_yesno = new int[]{90};

                                p.setyesNoDialog(new YesNoDialog(p, 90, "Thông báo", "Bạn muốn đăng ký hủy mã khóa? Thời gian hủy 1 ngày.",

                                        new String[]{"Đồng ý", "Hủy"}, new byte[]{-1, -1}));

                                p.getService().startYesNo();

                            }

                        }

                    } else if (index == 3) {

                        if (!p.mbv.isBlank() && p.timeHuyMbv != -1) {

                            p.timeHuyMbv = -1;

                            p.mbv = "";

                            p.passBagOK = true;

                            p.getService().send_box_ThongBao_OK("Hủy đăng ký thành công");

                        }

                    }

                    break;

                }

                // DELEGATED to SubMenuTeleport

                // case 995: {

                //     Select_Map_Tele_world(p, index);

                //     break;

                // }

                // case 996: {

                //     if (p.ship_pet != null) {

                //         p.getService().send_box_ThongBao_OK("Không thể chuyển map khi đang chuyển hàng");

                //     } else {

                //         Select_Map_Tele(p, index);

                //     }

                //     break;

                // }

                // DELEGATED to NpcHuongDan

                // case -997: {

                //     Menu_HuongDan(p, index);

                //     break;

                // }



                case 999: {
                    if (index >= 0 && index < activities.DanhHieu.ENY.size()) {
                        activities.DanhHieu dhSelect = activities.DanhHieu.ENY.get(index);
                        int[] getDh = p.getDanhHieuById(dhSelect.id);
                        if (getDh != null) {
                            if (getDh.length >= 2 && getDh[1] == 1) {
                                getDh[1] = 0;
                                p.id_danh_hieu_su_dung = -1;
                                p.setAbility();
                                p.getService().send_box_ThongBao_OK("Đã gỡ bỏ danh hiệu: [" + dhSelect.Name + "]!");
                            } else {
                                p.offAllDh();
                                if (getDh.length >= 2) {
                                    getDh[1] = 1;
                                }
                                p.id_danh_hieu_su_dung = dhSelect.id;
                                p.setAbility();
                                p.getService().send_box_ThongBao_OK("Đã kích hoạt danh hiệu: [" + dhSelect.Name + "]!\n\nChỉ số cộng thêm:\n" + dhSelect.getOptionString());
                            }
                        } else {
                            p.getService().send_box_ThongBao_OK("Bạn chưa sở hữu danh hiệu [" + dhSelect.Name + "]!\n\nChỉ số danh hiệu:\n" + dhSelect.getOptionString());
                        }
                    }
                    break;
                }

                // DELEGATED to MenuXoaSkill (iMenu 997)

                // case 997: ...

                // DELEGATED to MenuHocSkill (iMenu 998)

                // case 998: ...

                // DELEGATED to NpcGarp

                // case -37: {

                //     if (index < 4) {

                //         Menu_Gap(p, index);

                //     } else if (index == 4) {

                //         sendDymanicMenu(p, 978, "Đá hành trình",

                //                 new String[]{"Kho h¼Ónh tr¼ýnh", "Bản đồ hành trình", "Hướng dẫn"}, null);

                //     }

                //     break;

                // }

                // case -4: {

                //     Menu_Gap(p, index);

                //     break;

                // }

                // DELEGATED to NpcVongQuay

                // case -967:

                //     Menu_Vongquayevent(p, index);

                //     break;

                case -145:

                case -122:

                case -118:

                case -103:

                case -87:

                case -74:

                case -67:

                case -45:

                case -31:

                case -21:

                case -13:

                case -1: {

//                    Menu_TruongLang(p, index);

                    break;

                }

                case 120: { // bhx

                    break;

                }

                // DELEGATED to NpcKhoBau

                // case -133: {

                //     Menu_Buggi(p, index);

                //     break;

                // }



                // DELEGATED to NpcHoatDong

                // case -78: {

                //     Menu_Hoat_Dong(p, index);

                //     break;

                // }

                case 9999: {

                    Menu_Admin(p, index);

                    break;

                }

                case 1000: {

                    new model.InputDialog(p, 24 + index, "Cộng điểm", new String[]{"Nhập điểm"}).startInput();

                    break;

                }

                // DELEGATED to MenuHeKhac (iMenu 32003)

                // case 32003: ...

                // DELEGATED to MenuCuongHoaAcQuy (iMenu 32002)

                // case 32002: ...

                // DELEGATED to MenuKhamNgoc (iMenu 32001)

                // case 32001: {

                //     Menu_KhamNgoc(p, index);

                //     break;

                // }

                // DELEGATED to MenuRebuiltItem (iMenu 32000)

                // case 32000: {

                //     Menu_Rebuilt_Item(p, index);

                //     break;

                // }

                // DELEGATED to NpcJohny

                // case -105:

                // case -90:

                // case -70:

                // case -47: {

                //     Menu_Johny(p, index);

                //     break;

                // }

                // DELEGATED to NpcKookoroo

                // case -147:

                // case -120:

                // case -116:

                // case -102:

                // case -89:

                // case -76:

                // case -68:

                // case -46:

                // case -39:

                // case -29:

                // case -22: // menu cho cho

                // case -14: // menu rita

                // case -2: {

                //     Menu_Machiko(p, index);

                //     break;

                // }

                // DELEGATED to NpcPaule

                // case -146:

                // case -121:

                // case -117:

                // case -101:

                // case -88:

                // case -75:

                // case -69: // masu

                // case -38: // menu partty

                // case -30: // menu merri

                // case -23: // menu poroy

                // case -15: // Menu_MomRiTa

                // case -3: {

                //     Menu_Guru(p, index);

                //     break;

                // }

                // DELEGATED to NpcTeleport

                // case -144: // kinh do nuoc

                // case -124: // thi tran thien su

                // case -132: // dao jaza

                // case -107: // thi tran nanohano

                // case -85: // thi tran horn

                // case -97: // dao little grand

                // case 0: // thi tran whiskey

                // case -82: // mom sinh doi

                // case -60: // thi tran khoi dau

                // case -44: // lang hat de

                // case -36: // nha hang barati

                // case -28: // lang sirup

                // case -20: // thi tran orang

                // case -12: // thi tran vo so

                // case -5: { // lang coi xay gio

                //     Show_List_Map_Tele(p, index, idNPC);

                //     break;

                // }

                // DELEGATED to NpcKookoroo

                // case -99: {

                //     if (index == 0) {

                //         Menu_Machiko(p, index);

                //     } else {

                //         Vgo vgo = new Vgo();

                //         try {

                //             vgo.map_go = Zone.get_map_by_id(p.id_map_save);

                //             for (int i = 0; i < vgo.map_go[0].template.npcs.size(); i++) {

                //                 Npc npc_temp = vgo.map_go[0].template.npcs.get(i);

                //                 if (npc_temp.namegt.contains("Zosaku")) {

                //                     vgo.xnew = npc_temp.x;

                //                     if (npc_temp.y < 250) {

                //                         vgo.ynew = (short) (npc_temp.y + 20);

                //                     } else {

                //                         vgo.ynew = (short) (npc_temp.y - 40);

                //                     }

                //                     break;

                //                 }

                //             }

                //             if (vgo.xnew == 0 || vgo.ynew == 0) {

                //                 vgo.xnew = (short) (vgo.map_go[0].template.maxW / 2);

                //                 vgo.ynew = (short) (vgo.map_go[0].template.maxH / 2);

                //             }

                //         } catch (Exception e) {

                //             vgo.map_go = Zone.get_map_by_id(1);

                //             vgo.xnew = (short) (vgo.map_go[0].template.maxW / 2);

                //             vgo.ynew = (short) (vgo.map_go[0].template.maxH / 2);

                //         }

                //         p.goto_map(vgo);

                //     }

                //     break;

                // }



                case -98: {

                    if (p.map.template.id == 994) { // bang thong tin map tran chien lon

//            if (p.worldWarInfo != null) {

//              // p.getService().send_box_ThongBao_OK("Điểm hiện tại là " + p.pointTranChienLon + "\n//              // điểm");

//              p.getService().send_box_ThongBao_OK("Điểm hiện tại là: " + p.worldWarInfo.kill

//                  + " giết - " + p.worldWarInfo.dead + " chết");

//            }

                    } else if (p.map.template.id == 260) { // bang thong tin phong cho pvp bang

                        // send_dynamic_menu(p, type, "", new String[] {"Thông tin", "Xem thi đấu"});

                        if (index == 0) {

                            if (p.map.pvpBang != null && p.map.pvpBang.state == 1

                                    && p.map.pvpBang.mapFight != null

                                    && p.map.pvpBang.mapFight.pvpBangMapFight != null) {

                                String notice = "Băng đối thủ: ";

                                if (p.map.pvpBang.mapFight.pvpBangMapFight.clan1.equals(p.clan)) {

                                    notice += p.map.pvpBang.mapFight.pvpBangMapFight.clan2.name;

                                } else {

                                    notice += p.map.pvpBang.mapFight.pvpBangMapFight.clan1.name;

                                }

                                notice

                                        += ("\nHình thức thi đấu giáp lá cà. Tất cả các thành viên có mặt đánh 1 trận duy nhất phân thắng thua.");

                                p.getService().send_box_ThongBao_OK(notice);

                            } else {

                                p.getService().send_box_ThongBao_OK("Đang trong thời gian tìm ghép đối thủ");

                            }

                        } else if (index == 1) {

                            if (p.map.pvpBang != null && p.map.pvpBang.state == 2) {

                                // p.getService().send_box_ThongBao_OK("Bang đối thủ: ");

                            } else {

                                p.getService().send_box_ThongBao_OK("Hiện tại chưa diễn ra");

                            }

                        }

                    }

                    break;

                }

            }

        }

    }



    private static void Menu_VanChuyenHang(Player p, byte index) throws IOException {

        zinterfaces.menus.MenuVanChuyenHang.gI().handleMenu(p, index);

    }





    private static void Menu_Zosaku(Player p, byte index) throws IOException {

        switch (index) {

            case 3:

            case 0: {

                break;

            }

            case 1: { // map pvp

                Vgo vgo = new Vgo();

                vgo.map_go = Zone.getMapByID(1000);

                if (vgo.map_go != null) {

                    boolean full = false;

                    if (!full) {

                        vgo.xnew = (short) ZUtil.random(150, 300);

                        vgo.ynew = (short) ZUtil.random(200, 300);

                        p.goto_map(vgo);

                    } else {

                        p.getService().send_box_ThongBao_OK("Bản đồ quá tải!");

                    }

                }

                break;

            }

            case 2: { // pho ban lien tang



                switch (p.map.template.id) {

                    case 9:

                    case 17:

                    case 25:

                    case 33:

                    case 41:

                    case 49:

                    case 93:

                    case 69:

                    case 113:

                    case 191: {

                        if (p.party != null && p.party.list.size() > 1) {

                            if (!p.party.list.get(0).equals(p)) {

                                p.getService().send_box_ThongBao_OK("Chỉ có trưởng nhóm mới có thể bắt đầu phó bản");

                                break;

                            }

                        }

                        p.data_yesno = new int[]{62};

                        if (p.tableTickOption == null) {

                            p.tableTickOption = new functions.LienTangTick(p);

                            p.tableTickOption.listP = new ArrayList<>();

                            p.tableTickOption.listP.add(p);

                            if (p.party != null && p.party.list.size() > 1) {

                                for (int i = 0; i < p.party.list.size(); i++) {

                                    Player p0 = Zone.get_player_by_name_allmap(p.party.list.get(i).name);

                                    if (p0 != null && p0.index_map != p.index_map && p0.map != null && p0.map.equals(p.map)) {

                                        p.tableTickOption.listP.add(p0);

                                    }

                                }

                            }

                            p.tableTickOption.list_check = new byte[p.tableTickOption.listP.size()];

                            p.tableTickOption.list_check[0] = 1;

                            for (int i = 1; i < p.tableTickOption.list_check.length; i++) {

                                p.tableTickOption.list_check[i] = 0;

                            }

                            p.tableTickOption.show("Phó bản Liên tầng ");

                        }

                        break;

                    }

                    case 83: {

                        if (p.party != null && p.party.list.size() > 1) {

                            if (!p.party.list.get(0).equals(p)) {

                                p.getService().send_box_ThongBao_OK("Chỉ có trưởng nhóm mới có thể bắt đầu phó bản");

                                break;

                            }

                        }

                        p.data_yesno = new int[]{68};

                        if (p.tableTickOption == null) {

                            p.tableTickOption = new functions.SieuLienTangTick(p);

                            p.tableTickOption.listP = new ArrayList<>();

                            p.tableTickOption.listP.add(p);

                            if (p.party != null && p.party.list.size() > 1) {

                                for (int i = 0; i < p.party.list.size(); i++) {

                                    Player p0 = Zone.get_player_by_name_allmap(p.party.list.get(i).name);

                                    if (p0 != null && p0.index_map != p.index_map && p0.map != null && p0.map.equals(p.map)) {

                                        p.tableTickOption.listP.add(p0);

                                    }

                                }

                            }

                            p.tableTickOption.list_check = new byte[p.tableTickOption.listP.size()];

                            p.tableTickOption.list_check[0] = 1;

                            for (int i = 1; i < p.tableTickOption.list_check.length; i++) {

                                p.tableTickOption.list_check[i] = 0;

                            }

                            p.tableTickOption.show("Phó bản Siêu Liên tầng ");

                        }

                        break;

                    }

                }

            }

            break;

            case 4: {

                switch (p.map.template.id) {

                    case 9:
                    case 25:

                        String[] name = new String[12];

                        for (int i = 0; i < name.length; i++) {

                            name[i] = ("Cấp độ " + (i + 3));

                        }

                        short[] icon = new short[12];

                        for (int i = 0; i < icon.length; i++) {

                            if (p.aiDonLevel < i) {

                                icon[i] = 168;

                            } else {

                                if (i < 2) {

                                    icon[i] = 164;

                                } else if (i < 5) {

                                    icon[i] = 165;

                                } else if (i < 7) {

                                    icon[i] = 166;

                                } else {

                                    icon[i] = 167;

                                }

                            }

                        }

                        sendDymanicMenu(p, 988, "Vượt ải đơn", name, icon);

                        break;

                    case 49:

                        // lenh truy na

                        Vgo vgo = new Vgo();

                        vgo.map_go = Zone.getMapByID(119);

                        if (vgo.map_go != null) {

                            boolean full = false;

                            if (!full) {

                                vgo.xnew = (short) ZUtil.random(120, 380);

                                vgo.ynew = (short) ZUtil.random(230, 330);

                                p.goto_map(vgo);

                            } else {

                                p.getService().send_box_ThongBao_OK("Zone đầy hãy quay lại sau");

                            }

                        }

                        break;

                    case 41: {

                        if (p.party != null && p.party.list.size() > 1) {

                            if (!p.party.list.get(0).equals(p)) {

                                p.getService().send_box_ThongBao_OK("Chỉ có trưởng nhóm mới có thể bắt đầu phó bản");

                                break;

                            }

                        }

                        p.data_yesno = new int[]{63};

                        if (p.tableTickOption == null) {

                            p.tableTickOption = new functions.VuonCamTick(p);

                            p.tableTickOption.listP = new ArrayList<>();

                            p.tableTickOption.listP.add(p);

                            if (p.party != null && p.party.list.size() > 1) {

                                for (int i = 0; i < p.party.list.size(); i++) {

                                    Player p0 = Zone.get_player_by_name_allmap(p.party.list.get(i).name);

                                    if (p0 != null && p0.index_map != p.index_map && p0.map != null && p0.map.equals(p.map)) {

                                        p.tableTickOption.listP.add(p0);

                                    }

                                }

                            }

                            p.tableTickOption.list_check = new byte[p.tableTickOption.listP.size()];

                            p.tableTickOption.list_check[0] = 1;

                            for (int i = 1; i < p.tableTickOption.list_check.length; i++) {

                                p.tableTickOption.list_check[i] = 0;

                            }

                            p.tableTickOption.show("Bảo vệ kho báu Namie");

                        }

                        break;

                    }

                    default:

                        break;

                }

            }

        }

    }



    public static int getVillageId(int mapId) {
        if (mapId >= 0 && mapId <= 7) return 0; // Foosha
        if (mapId >= 8 && mapId <= 15) return 1; // TT. Vỏ Sò
        if (mapId >= 16 && mapId <= 23) return 2; // TT. Orange
        if (mapId >= 24 && mapId <= 31) return 3; // Làng Sirup
        if (mapId >= 32 && mapId <= 39) return 4; // Baratie
        if ((mapId >= 40 && mapId <= 47) || mapId == 62) return 5; // Làng Hạt Dẻ
        if (mapId >= 48 && mapId <= 54) return 6; // TT. Khởi Đầu
        if (mapId >= 63 && mapId <= 68) return 7; // Mỏm Sinh Đôi
        if (mapId >= 69 && mapId <= 77) return 8; // TT. Whiskay
        if (mapId >= 78 && mapId <= 82) return 9; // Đảo Little Garden
        if (mapId >= 83 && mapId <= 90) return 10; // TT. Horn
        if (mapId >= 91 && mapId <= 105) return 11; // TT. Nanohana
        if (mapId >= 106 && mapId <= 111) return 12; // Đảo Jaza
        if (mapId >= 112 && mapId <= 127) return 13; // TT. Thiên Sứ
        if (mapId >= 189 && mapId <= 211) return 14; // Water 7
        return -1;
    }

    public static boolean isSameVillage(int mapId1, int mapId2) {
        int v1 = getVillageId(mapId1);
        int v2 = getVillageId(mapId2);
        return v1 != -1 && v1 == v2;
    }

    public static void Select_Map_Tele(Player p, byte index) throws IOException {
        if (p.map_tele == null && p.map != null && p.map.template != null) {
            p.map_tele = getVillageMaps(p, p.map.template.id);
        }
        if (p.map_tele != null && index >= 0 && index < p.map_tele.length) {
            if (p.ship_pet != null) {
                p.getService().send_box_ThongBao_OK("Không thể chuyển map khi đang chuyển hàng");
                p.map_tele = null;
                return;
            }
            int targetMapId = p.map_tele[index];
            Zone[] map_go = Zone.getMapByID(targetMapId);
            if (map_go == null || map_go.length == 0 || map_go[0] == null || map_go[0].template == null) {
                p.getService().send_box_ThongBao_OK("Không tìm thấy bản đồ!");
                p.map_tele = null;
                return;
            }
            if (p.map != null && p.map.template != null && p.map.template.id == targetMapId) {
                p.getService().send_box_ThongBao_OK("Đang ở map này rồi!");
                p.map_tele = null;
                return;
            }
            int curMapId = (p.map != null && p.map.template != null) ? p.map.template.id : -1;
            boolean sameVillage = (curMapId >= 0 && isSameVillage(curMapId, targetMapId));
            int mapIDCanGo = p.checkQuest();
            if (!sameVillage && !Zone.isMapNoQuestLimit(targetMapId) && targetMapId > mapIDCanGo) {
                p.getService().send_box_ThongBao_OK("Bản đồ này chưa mở! Hãy hoàn thành nhiệm vụ để mở khóa.");
                p.map_tele = null;
                return;
            }
            Vgo vgo = new Vgo();
            vgo.map_go = map_go;
            if (vgo.map_go[0].template.npcs != null) {
                for (int i = 0; i < vgo.map_go[0].template.npcs.size(); i++) {
                    Npc npc_temp = vgo.map_go[0].template.npcs.get(i);
                    if (npc_temp != null) {
                        boolean isTeleNpc = false;
                        zinterfaces.iNpc teleHandler = zinterfaces.iNpc.get((short) -5);
                        if (teleHandler != null && teleHandler.getId() != null) {
                            for (short teleId : teleHandler.getId()) {
                                if (npc_temp.idmenu == teleId) {
                                    isTeleNpc = true;
                                    break;
                                }
                            }
                        }
                        if (isTeleNpc || (npc_temp.namegt != null && npc_temp.namegt.equals("Bản đồ")) 
                                || (npc_temp.name != null && npc_temp.name.contains("Bản đồ"))) {
                            vgo.xnew = npc_temp.x;
                            if (npc_temp.y < 250) {
                                vgo.ynew = (short) (npc_temp.y + 20);
                            } else {
                                vgo.ynew = (short) (npc_temp.y - 20);
                            }
                            break;
                        }
                    }
                }
            }
            if (vgo.xnew == 0 || vgo.ynew == 0) {
                if (vgo.map_go[0].template.npcs != null && !vgo.map_go[0].template.npcs.isEmpty()) {
                    Npc anyNpc = vgo.map_go[0].template.npcs.get(0);
                    if (anyNpc != null && anyNpc.x > 0 && anyNpc.y > 0) {
                        vgo.xnew = anyNpc.x;
                        vgo.ynew = anyNpc.y < 250 ? (short) (anyNpc.y + 20) : (short) (anyNpc.y - 20);
                    }
                }
            }
            if (vgo.xnew == 0 || vgo.ynew == 0) {
                vgo.xnew = (short) (vgo.map_go[0].template.maxW / 2);
                vgo.ynew = (short) (vgo.map_go[0].template.maxH / 2);
            }
            p.goto_map(vgo);
            p.updateArchiDaily(2);
        }
        p.map_tele = null;
    }



    public static void Select_Map_Tele_world(Player p, byte index) throws IOException {
        if (p.map_tele != null && index >= 0 && index < p.map_tele.length) {
            if (p.ship_pet != null) {
                p.getService().send_box_ThongBao_OK("Không thể chuyển map khi đang chuyển hàng");
                p.map_tele = null;
                return;
            }
            int targetMapId = p.map_tele[index];
            int mapIDCanGo = p.checkQuest();
            if (!Zone.isMapNoQuestLimit(targetMapId) && targetMapId > mapIDCanGo) {
                p.getService().send_box_ThongBao_OK("Bản đồ này chưa mở! Hãy hoàn thành nhiệm vụ để mở khóa.");
                p.map_tele = null;
                return;
            }
            Zone[] targetMap = Zone.getMapByID(targetMapId);
            if (targetMap == null || targetMap.length == 0 || targetMap[0] == null || targetMap[0].template == null) {
                p.getService().send_box_ThongBao_OK("Không tìm thấy bản đồ!");
                p.map_tele = null;
                return;
            }
            if (p.map != null && p.map.template != null && p.map.template.id == targetMapId) {
                p.getService().send_box_ThongBao_OK("Đang ở map này rồi!");
                p.map_tele = null;
                return;
            }
            p.data_yesno = new int[]{index};
            p.setyesNoDialog(new YesNoDialog(p, 5, "", "Bạn có muốn dịch chuyển qua "
                    + targetMap[0].template.name + " ?",
                    new String[]{"20", "Hủy"}, new byte[]{6, -1}));
            p.getService().startYesNo();
        }
    }



    public static void Menu_HuongDan(Player p, byte index) throws IOException {

        zinterfaces.iNpc handler = zinterfaces.iNpc.get((short) -997);

        if (handler != null) {

            handler.handleMenu(p, index);

        }

    }



    public static void Menu_Remove_Skill(Player p, byte index) throws IOException {

        for (int i = 0; i < p.skill_point.size(); i++) {

            Skill_info temp = p.skill_point.get(i);

            if (temp.temp.ID >= 1000 && temp.temp.ID < 2000 && temp.temp.Lv_RQ > 0) {

                index--;

                if (index == -1) {

                    p.data_yesno = new int[]{i};

                    p.setyesNoDialog(new YesNoDialog(p, 4, "Thông báo",

                            ("Bạn có chắc muốn xóa kỹ năng " + temp.temp.name

                            + " này không? Phí xóa kỹ năng này là 2 ruby"),

                            new String[]{"2", "Không"}, new byte[]{7, -1}));

                    p.getService().startYesNo();

                    break;

                }

            }

        }

    }



    public static void Menu_Learn_Skill(Player p, byte index) throws IOException {

        for (int i = 0; i < p.skill_point.size(); i++) {

            Skill_info temp = p.skill_point.get(i);

            if ((temp.temp.ID < 4 && temp.temp.Lv_RQ == -1)

                    || (temp.temp.ID > 3 && temp.temp.ID < 2000 && temp.temp.Lv_RQ < 5)) {

                index--;

                if (index == -1) {

                    p.data_yesno = new int[]{i};

                    if (temp.temp.ID < 4) {

                        p.setyesNoDialog(new YesNoDialog(p, 3, "Thông báo",

                                ("Bạn có muốn học kỹ năng " + temp.temp.name + "?"),

                                new String[]{"10.000", "Không"}, new byte[]{6, -1}));

                        p.getService().startYesNo();

                    } else {

                        Skill_Template sk_temp = null;

                        if (temp.temp.ID > 3 && temp.temp.ID < 2000 && temp.temp.Lv_RQ > -1) {

                            sk_temp = Skill_Template.get_temp((temp.temp.indexSkillInServer + 1), 0);

                        }

                        if (temp.temp.ID == 1015) {

                            if (sk_temp == null) {

                                p.setyesNoDialog(new YesNoDialog(p, 3, "Thông báo",

                                        ("Bạn có muốn học chiêu nội tại Haki quan sát? từ + 1 Sách Haki Quan Sát"),

                                        new String[]{"Có", "Không"}, new byte[]{-1, -1}));

                                p.getService().startYesNo();

                            } else {

                                String req = " từ 100.000 extol + 50.000.000 beri?";

                                if (sk_temp.Lv_RQ == 3) {

                                    req = " từ 150.000 extol + 100.000.000 beri?";

                                } else if (sk_temp.Lv_RQ == 4) {

                                    req = " từ 200.000 extol + 150.000.000 beri?";

                                } else if (sk_temp.Lv_RQ == 5) {

                                    req = " từ 300.000 extol + 200.000.000 beri?";

                                }

                                p.setyesNoDialog(new YesNoDialog(p, 3, "Thông báo",

                                        ("Bạn có muốn học chiêu nội tại " + sk_temp.name + "?" + req),

                                        new String[]{"Có", "Không"}, new byte[]{-1, -1}));

                                p.getService().startYesNo();

                            }

                        } else if (temp.temp.ID == 1016) {

                            if (sk_temp == null) {

                                p.setyesNoDialog(new YesNoDialog(p, 3, "Thông báo",

                                        ("Bạn có muốn học chiêu nội tại Haki Vũ Trang? từ + 1 Sách Haki Vũ Trang"),

                                        new String[]{"Có", "Không"}, new byte[]{-1, -1}));

                                p.getService().startYesNo();

                            } else {

                                String req = " từ 5.000 ruby + 100.000.000 beri?";

                                if (sk_temp.Lv_RQ == 3) {

                                    req = " từ 10.000 ruby + 200.000.000 beri?";

                                } else if (sk_temp.Lv_RQ == 4) {

                                    req = " từ 20.000 ruby + 400.000.000 beri?";

                                } else if (sk_temp.Lv_RQ == 5) {

                                    req = " từ 40.000 ruby + 800.000.000 beri?";

                                }

                                p.setyesNoDialog(new YesNoDialog(p, 3, "Thông báo",

                                        ("Bạn có muốn học chiêu nội tại " + sk_temp.name + "?" + req),

                                        new String[]{"Có", "Không"}, new byte[]{-1, -1}));

                                p.getService().startYesNo();

                            }

                        } else if (temp.temp.ID == 1017) {

                            if (sk_temp == null) {

                                p.setyesNoDialog(new YesNoDialog(p, 3, "Thông báo",

                                        ("Bạn có muốn học chiêu nội tại Haki Bá Vương? từ + 1 Sách Haki Bá Vương"),

                                        new String[]{"Có", "Không"}, new byte[]{-1, -1}));

                                p.getService().startYesNo();

                            } else {

                                String req = " từ 5.000 ruby + 100.000.000 beri?";

                                if (sk_temp.Lv_RQ == 3) {

                                    req = " từ 10.000 ruby + 200.000.000 beri?";

                                } else if (sk_temp.Lv_RQ == 4) {

                                    req = " từ 20.000 ruby + 400.000.000 beri?";

                                } else if (sk_temp.Lv_RQ == 5) {

                                    req = " từ 40.000 ruby + 800.000.000 beri?";

                                }

                                p.setyesNoDialog(new YesNoDialog(p, 3, "Thông báo",

                                        ("Bạn có muốn học chiêu nội tại " + sk_temp.name + "?" + req),

                                        new String[]{"Có", "Không"}, new byte[]{-1, -1}));

                                p.getService().startYesNo();

                            }

                        } else {

                            p.setyesNoDialog(new YesNoDialog(p, 3, "Thông báo",

                                    ("Bạn có muốn học chiêu nội tại "

                                    + (sk_temp != null ? sk_temp.name : temp.temp.name) + "?"),

                                    new String[]{"10.000", "Không"}, new byte[]{6, -1}));

                            p.getService().startYesNo();

                        }

                    }

                    break;

                }

            }

        }

    }







    public static void Menu_Vongquayevent(Player p, byte index) throws IOException {

        switch (index) {

            case 0: {

                p.typeVongQuay = 3;

                VongQuay.show_table(p);

                break;

            }

            case 1: {

                p.typeVongQuay = 4;

                VongQuay.show_table(p);

                break;

            }

            case 2:

                String txt = "Hải Tặc Cổ Đại - Sự Kiện 20/11\n"

                        + "Thời gian: từ ngày 24/11 đến 23h59 4/12\n"

                        + "1. Bó hoa hồng\\n"

                        + "Thời gian: từ ngày 24/11 đến 23h59 4/12\n"

                        + "Thời gian: từ ngày 24/11 đến 23h59 4/12\n"

                        + "+ Tham gia Truy nã (Thắng 6/ Thua 3) (Tối đa 60 hoa hồng/ngày)\\n"

                        + "+ Tham gia PVP (Thắng 6/ Thua 3) (Tối đa 60 hoa hồng/ngày)\\n"

                        + "+ Tham gia Vận buôn (Tối đa 50 hoa hồng/ngày)\\n"

                        + "Thời gian: từ ngày 24/11 đến 23h59 4/12\n"

                        + "Thời gian: từ ngày 24/11 đến 23h59 4/12\n"

                        + "Thời gian: từ ngày 24/11 đến 23h59 4/12\n"

                        + "Khi đạt mốc 300, 500, 1000, 1500, 2000, 3000 điểm có thể đổi quà tại Cửa hàng sự kiện\\b"

                        + "Thời gian: từ ngày 24/11 đến 23h59 4/12\n"

                        + "Thời gian: từ ngày 24/11 đến 23h59 4/12\n"

                        + "Thời gian: từ ngày 24/11 đến 23h59 4/12\n"

                        + "Thời gian: từ ngày 24/11 đến 23h59 4/12\n"

                        + "Thời gian: từ ngày 24/11 đến 23h59 4/12\n"

                        + "Thời gian: từ ngày 24/11 đến 23h59 4/12\n"

                        + "Thời gian: từ ngày 24/11 đến 23h59 4/12\n"

                        + "Thời gian: từ ngày 24/11 đến 23h59 4/12\n"

                        + "Thời gian: từ ngày 24/11 đến 23h59 4/12\n"

                        + "Thời gian: từ ngày 24/11 đến 23h59 4/12\n"

                        + "Thời gian: từ ngày 24/11 đến 23h59 4/12\n"

                        + "Thời gian: từ ngày 24/11 đến 23h59 4/12\n"

                        + "Thời gian: từ ngày 24/11 đến 23h59 4/12\n"

                        + "Thời gian: từ ngày 24/11 đến 23h59 4/12\n"

                        + "Thời gian: từ ngày 24/11 đến 23h59 4/12\n"

                        + "Thời gian: từ ngày 24/11 đến 23h59 4/12\n"

                        + "Thời gian: từ ngày 24/11 đến 23h59 4/12\n"

                        + "TOP 6-10: 200K Coin, 1 hộp thời trang trung";

                p.getService().Help_From_Server(-967, txt);

                break;

        }

    }







    private static void Menu_Admin(Player p, byte index) throws IOException {

        if (p.getUsername().equals("admin")) {

            switch (index) {

                case 0: {

                    new Thread(() -> {

                        p.getService().send_box_ThongBao_OK("Chuẩn bị bảo trì sau 10s");

                        SaveData.BaoTri();

                    }).start();

                    break;

                }

                case 1: {

                    p.update_vang(1_000_000_000);

                    p.update_ngoc(1_000_000_000);

                    p.updateMoney();

                    break;

                }

                case 10: {

                    map.Zone zone = p.map;

                    mob.Mob boss = new mob.Mob();

                    boss.is_boss = true;

                    boss.mtemplate = template.MobTemplate.ENTRYS.get(174);

                    if (boss.mtemplate != null) {

                        boss.x = p.x; boss.y = p.y;

                        boss.hp_max = 20000000; boss.hp = 20000000;

                        boss.level = 120;

                        boss.map = zone;

                        boss.index = core.Manager.gI().index_mob.getAndIncrement();

                        boss.isdie = false;

                        mob.Mob.ENTRYS.put(boss.index, boss);

                        if (boss.map != null) {

                            boss.map.mobs.put(boss.index, boss);

                        }

                        int[] mob_list = new int[zone.list_mob.length + 1];

                        System.arraycopy(zone.list_mob, 0, mob_list, 0, zone.list_mob.length);

                        mob_list[mob_list.length - 1] = boss.index;

                        zone.list_mob = mob_list;

                        Manager.gI().chatKTG(0, "Boss đã xuất hiện tại " + zone.template.name + "!", 0);

                    }

                    break;

                }

                case 2: {

                    p.updateVnd(1_000_000_000);

                    p.updateMoney();

                    break;

                }

                case 3: {

                    new model.InputDialog(p, 32000, "Uplevel", new String[]{"Nhập level"}).startInput();

                    break;

                }

                case 4: {

                    new model.InputDialog(p, 32001, "SetXP", new String[]{"Nhập mức"}).startInput();

                    break;

                }

                case 5: {

                    new model.InputDialog(p, 32002, "Get Item",

                            new String[]{"Type item", "Id item", "Số lượng"}).startInput();

                    break;

                }

                case 6: {

                    sendDymanicMenu(p, 977, "DauGiaPanel", new String[]{"Thêm Item", "Xoá Item"}, null);

                    break;

                }

                case 7: {

                    new model.InputDialog(p, 23, "Buff coin",

                            new String[]{"Tên tài khoản", "coin buff"}).startInput();

                    break;

                }

                case 8: {

                    new model.InputDialog(p, 25, "Buff Mốc",

                            new String[]{"Tên tài khoản", "coin buff"}).startInput();

                    break;

                }

            }

        }

    }







    public static void Menu_Johny(Player p, byte index) throws IOException {

        if (p.map.template.id == 25 && index == 5) { // cuong hoa ac quy
            sendDymanicMenu(p, 32002, "Cường hóa ác quỷ",
                    new String[]{"Cửa hàng đá khảm", "Cường hóa Rương ác quỷ", "Cường hóa Kỹ năng"},
                    new short[]{129, 155, 156});
        } else {

            switch (index) {

                case 0: {

                    sendDymanicMenu(

                            p, 32000, "Cường Hóa", new String[]{"Cửa hàng Nguyên liệu", "Ghép nguyên liệu",

                                "Tách nguyên liệu", "Cường hóa đồ", "Cường hóa cao cấp"},

                            new short[]{129, 127, 130, 131, 163});

                    break;

                }

                case 1: {

                    sendDymanicMenu(p, 32001, "Khảm Đá",

                            new String[]{"Cửa Hàng Đá Khảm", "Ghép Đá", "Đục lỗ khảm", "Khảm Vật Phẩm",

                                "Tách Đá", "Đá siêu cấp", "Đá thần thoại", "Hướng dẫn"},

                            new short[]{129, 127, 133, 126, 130, 141, 132, 148});

                    break;

                }

                case 2: {

                    ChuyenHoa.show_table(p);

                    break;

                }

                case 3: {

                    Rebuild_Item.show_table(p, 9);

                    break;

                }

                case 4: {

                    int ver_ = p.getConnVersionInt(); // dùng int version trực tiếp, tránh NumberFormatException

                    if (ver_ >= 115) {

                        Upgrade_Skin.show_table(p);

                    } else {

                        p.getService().send_box_ThongBao_OK("Hãy sử dụng phiên bản từ 1.1.5 trở lên");

                    }

                    break;

                }

            }

        }

    }







    private static void Menu_Change_Zone(Player p) throws IOException {
        if (p == null || p.map == null || p.map.template == null) {
            return;
        }

        Zone[] map_ = Zone.getMapByID(p.map.template.id);
        if (map_ == null || map_.length == 0) {
            return;
        }

        Message m = new Message(23);
        m.writer().writeByte((byte) map_.length);

        for (int i = 0; i < map_.length; i++) {
            if (map_[i] != null && map_[i].template != null) {
                int s = map_[i].getNumPlayerSlot();
                int max = map_[i].template.max_player;
                // 0 green, 1 orange, 2 red, 3 violet, other green
                m.writer().writeByte((s >= max) ? 2 : ((s > (max / 2)) ? 1 : 0));
            } else {
                m.writer().writeByte(0);
            }
        }

        p.addmsg(m);
        m.cleanup();
    }



    public static int[] getVillageMaps(Player p, int idNPC) {
        switch (idNPC) {
            case -5: case -8: case -9: case -10: case -53:
                return new int[]{1, 2, 3, 4, 6};
            case -12: case -16: case -17: case -18: case -54:
                return new int[]{9, 10, 11, 12, 14};
            case -20: case -24: case -25: case -26: case -55:
                return new int[]{17, 18, 19, 20, 22};
            case -28: case -32: case -33: case -34: case -56:
                return new int[]{25, 26, 27, 28, 30};
            case -36: case -40: case -41: case -42: case -57:
                return new int[]{33, 34, 35, 36, 38};
            case -44: case -49: case -50: case -51: case -58:
                return new int[]{41, 42, 43, 44, 46};
            case -60: case -61: case -62: case -63: case -59:
                return new int[]{49, 50, 51, 52, 54};
            case -82: // Mỏm Sinh Đôi (Map 66)
                return new int[]{66, 67, 68};
            case 0: case -79: case -80: case -81: case -83: // Thị Trấn Whiskay (Map 69)
                return new int[]{69, 70, 71, 72, 74};
            case -97: // Đảo Little Garden (Map 79)
                return new int[]{79, 80, 81, 82};
            case -85: case -92: case -93: case -94: case -96: // Thị Trấn Horn (Map 83)
                return new int[]{83, 84, 85, 86, 88};
            case -107: case -108: case -109: case -110: case -111: case -112: case -113: case -114: case -115: // Thị Trấn Nanohano (Map 93)
                return new int[]{93, 94, 95, 96, 97, 98, 99, 100, 101, 103};
            case -132: // Đảo Jaza (Map 107)
                return new int[]{107, 108, 109, 110, 111};
            case -124: case -123: case -125: case -126: case -127: case -128: case -129: case -130: case -131: // Thị Trấn Thiên Sứ (Map 113)
                return new int[]{113, 112, 115, 116, 117, 118, 124, 125, 126};
            case -144: case -148: case -149: case -150: case -151: case -152: case -153: // Kinh Đô Nước Water 7 (Map 191)
                return new int[]{191, 192, 193, 194, 195, 196, 197};
        }

        // Fallback theo map hiện tại của người chơi nếu idNPC không khớp
        if (p != null && p.map != null && p.map.template != null) {
            int mapId = p.map.template.id;
            if (mapId >= 0 && mapId <= 7) return new int[]{1, 2, 3, 4, 6};
            if (mapId >= 8 && mapId <= 15) return new int[]{9, 10, 11, 12, 14};
            if (mapId >= 16 && mapId <= 23) return new int[]{17, 18, 19, 20, 22};
            if (mapId >= 24 && mapId <= 31) return new int[]{25, 26, 27, 28, 30};
            if (mapId >= 32 && mapId <= 39) return new int[]{33, 34, 35, 36, 38};
            if ((mapId >= 40 && mapId <= 47) || mapId == 62) return new int[]{41, 42, 43, 44, 46};
            if (mapId >= 48 && mapId <= 54) return new int[]{49, 50, 51, 52, 54};
            if (mapId >= 63 && mapId <= 68) return new int[]{66, 67, 68};
            if (mapId >= 69 && mapId <= 77) return new int[]{69, 70, 71, 72, 74};
            if (mapId >= 78 && mapId <= 82) return new int[]{79, 80, 81, 82};
            if (mapId >= 83 && mapId <= 90) return new int[]{83, 84, 85, 86, 88};
            if (mapId >= 91 && mapId <= 105) return new int[]{93, 94, 95, 96, 97, 98, 99, 100, 101, 103};
            if (mapId >= 106 && mapId <= 111) return new int[]{107, 108, 109, 110, 111};
            if (mapId >= 112 && mapId <= 127) return new int[]{113, 112, 115, 116, 117, 118, 124, 125, 126};
            if (mapId >= 189 && mapId <= 211) return new int[]{191, 192, 193, 194, 195, 196, 197};
            return new int[]{mapId};
        }

        return new int[]{1, 2, 3, 4, 6};
    }

    public static void Show_List_Map_Tele(Player p, int index, int idNPC) throws IOException {
        if (index == 1) { // Thế giới
            p.map_tele = MenuController.ID_MAP_LANG;
            sendDymanicMenu(p, 995, "Dịch chuyển", p.map_tele);
        } else if (index == 0) { // Trong làng
            p.map_tele = getVillageMaps(p, idNPC);
            sendDymanicMenu(p, 996, "Dịch chuyển", p.map_tele);
        }
    }

    public static void sendDymanicMenu(Player p, int id_npc, String name_npc, String[] list_menu, short[] list_icon) throws IOException {

        if (p != null && p.getService() != null) {

            p.getService().openDynamicMenu(id_npc, name_npc, list_menu, list_icon);

        }

    }



    public static void sendDymanicMenu(Player p, int id_npc, String name_npc, String[] list_menu, byte[] list_icon, int b) throws IOException {

        if (p != null && p.getService() != null) {

            short[] shortIcons = null;

            if (list_icon != null) {

                shortIcons = new short[list_icon.length];

                for (int i = 0; i < list_icon.length; i++) {

                    shortIcons[i] = list_icon[i];

                }

            }

            p.getService().openDynamicMenu(id_npc, name_npc, list_menu, shortIcons);

        }

    }



    public static void sendDymanicMenu(Player p, int id_npc, String name_npc, List<String> list_menu, List<Integer> list_icon) throws IOException {

        if (p != null && p.getService() != null) {

            String[] arrMenu = list_menu != null ? list_menu.toArray(new String[0]) : new String[0];

            short[] arrIcon = null;

            if (list_icon != null) {

                arrIcon = new short[list_icon.size()];

                for (int i = 0; i < list_icon.size(); i++) {

                    arrIcon[i] = list_icon.get(i).shortValue();

                }

            }

            p.getService().openDynamicMenu(id_npc, name_npc, arrMenu, arrIcon);

        }

    }



    public static void sendDymanicMenu(Player p, int idNPC, String title, String[] name) throws IOException {

        if (p != null && p.getService() != null) {

            p.getService().openDynamicMenu(idNPC, title, name, null);

        }

    }



    public static void sendDymanicMenu(Player p, int idNPC, String title, int[] name) throws IOException {

        if (p != null && p.getService() != null) {

            p.getService().send_dynamic_menu_maps(idNPC, 0, title, name);

        }

    }



    /**

     * Gửi event BXH theo p.typeBXH hiện tại.

     * Thay thế pattern: p.typeBXH = X; Ranked.send(p, 7, 0);

     * Dùng trong toàn bộ MenuController thay vì gọi Ranked.send() kiểu cũ.

     */

    public static void sendEventRank(model.Player p) {

        try {

            AbsRanked rankObj = AbsRanked.getBySubType(p.typeBXH);

            if (rankObj != null) rankObj.show(p, 0);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}





