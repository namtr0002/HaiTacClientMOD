package activities;

import itemz.Rebuild_Item;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.Player;
import historys.zLog;
import network.Service;
import core.ZUtil;
import network.Message;
import template.ItemBag47;
import template.ItemFashion;
import template.ItemFashionP2;
import template.ItemTemplate4;
import template.ItemTemplate7;
import template.Upgrade_Skin_Info;

public class Upgrade_Skin {
  public static byte[] PERCENT = new byte[] {3, 6, 11, 20, 32, 50};

  public static void show_table(Player p) throws IOException {
    if (p == null || p.trade_target != null) {
      if (p != null) p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
      return;
    }
    p.upgrade_skin = new Upgrade_Skin_Info();
    p.upgrade_skin.upgrade_skin_data = new short[] {-1, -1, -1, -1, -1, -1};
    //
    // Đếm số fashion item hợp lệ trước để count chính xác
    List<template.ItemFashionP2> validFashion = new ArrayList<>();
    for (int i = 0; i < p.fashion.size(); i++) {
      ItemFashion temp = ItemFashion.get_item(p.fashion.get(i).id);
      if (temp != null) {
        validFashion.add(p.fashion.get(i));
      }
    }
    Message m = new Message(81);
    m.writer().writeByte(0);
    m.writer().writeByte(105);
    m.writer().writeByte(validFashion.size()); // count chính xác
    for (int i = 0; i < validFashion.size(); i++) {
      ItemFashion temp = ItemFashion.get_item(validFashion.get(i).id);
      m.writer().writeShort(temp.ID);
      m.writer().writeUTF("");
      m.writer().writeUTF("");
      m.writer().writeShort(temp.idIcon);
      m.writer().writeByte(validFashion.get(i).level);
    }
    //
    List<ItemBag47> list_da_kham = new ArrayList<>();
    for (int i = 0; i < p.item.bag47.size(); i++) {
      ItemBag47 it47 = p.item.bag47.get(i);
      if (it47.category == 4) {
        ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(it47.id);
        if (itemTemplate4 != null && itemTemplate4.type == 12 && itemTemplate4.id < 80 && check_id_ngoc(itemTemplate4.id)) {
          list_da_kham.add(it47);
        }
      }
      if (it47.category == 7 && (
      it47.id == 16 || // bùa sao 8 cánh (đã được các handler process xử lý)
      it47.id == 17)) { // nlieu
        list_da_kham.add(it47);
      }
    }
    m.writer().writeByte(list_da_kham.size()); // size da kham
    for (int i = 0; i < list_da_kham.size(); i++) {
      if (list_da_kham.get(i).category == 4) {
        ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(list_da_kham.get(i).id);
        if (itemTemplate4 == null) continue;
        m.writer().writeByte(4);
        m.writer().writeShort(itemTemplate4.id);
        m.writer().writeShort(list_da_kham.get(i).quant);
        m.writer().writeUTF(itemTemplate4.name);
        m.writer().writeShort(itemTemplate4.icon);
      } else if (list_da_kham.get(i).category == 7
          && (list_da_kham.get(i).id == 16 || list_da_kham.get(i).id == 17)) {
        ItemTemplate7 itemTemplate7 = ItemTemplate7.get_it_by_id(list_da_kham.get(i).id);
        if (itemTemplate7 == null) continue;
        m.writer().writeByte(7);
        m.writer().writeShort(itemTemplate7.id);
        m.writer().writeShort(list_da_kham.get(i).quant);
        m.writer().writeUTF(itemTemplate7.name);
        m.writer().writeShort(itemTemplate7.icon);
      }
    }
    p.addmsg(m);
    m.cleanup();
  }

  private static boolean check_id_ngoc(short id) {
    return Rebuild_Item.get_percent_hop_ngoc(id) >= 0;
  }

  public static void process(Player p, Message m2) throws IOException {
    if (p == null || p.trade_target != null) return;
    // System.out.println(m2.reader().available());
    if (m2.reader().available() == 6) {
      byte type = m2.reader().readByte();
      byte cat = m2.reader().readByte();
      short id = m2.reader().readShort();
      byte pos = m2.reader().readByte();
      byte bovao = m2.reader().readByte();
      // System.out.println(type + " " + cat + " " + id + " " + pos + " " + bovao);
      if (type == 1 && cat == 105 && pos == 0 && bovao == 1) {
        ItemFashionP2 myFashion = p.check_fashion(id);
        if (myFashion != null && p.upgrade_skin != null) { // update lai sau khi upgrade
          // Đếm fashion valid trước
          List<template.ItemFashionP2> validFashion2 = new ArrayList<>();
          for (int i = 0; i < p.fashion.size(); i++) {
            if (ItemFashion.get_item(p.fashion.get(i).id) != null) {
              validFashion2.add(p.fashion.get(i));
            }
          }
          Message m3 = new Message(81);
          m3.writer().writeByte(5);
          m3.writer().writeByte(105);
          m3.writer().writeByte(validFashion2.size()); // count chính xác
          for (int i = 0; i < validFashion2.size(); i++) {
            ItemFashion temp = ItemFashion.get_item(validFashion2.get(i).id);
            m3.writer().writeShort(temp.ID);
            m3.writer().writeUTF("");
            m3.writer().writeUTF("");
            m3.writer().writeShort(temp.idIcon);
            m3.writer().writeByte(validFashion2.get(i).level);
          }
          //
          List<ItemBag47> list_da_kham = new ArrayList<>();
          for (int i = 0; i < p.item.bag47.size(); i++) {
            ItemBag47 it47 = p.item.bag47.get(i);
            if (it47.category == 4) {
              ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(it47.id);
              if (itemTemplate4 != null && itemTemplate4.type == 12 && itemTemplate4.id < 80
                  && check_id_ngoc(itemTemplate4.id)) {
                list_da_kham.add(it47);
              }
            }
            if (it47.category == 7 && (it47.id == 16 || it47.id == 17)) { // nlieu
              list_da_kham.add(it47);
            }
          }
          m3.writer().writeByte(list_da_kham.size()); // size da kham
          for (int i = 0; i < list_da_kham.size(); i++) {
            if (list_da_kham.get(i).category == 4) {
              ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(list_da_kham.get(i).id);
              if (itemTemplate4 == null) continue;
              m3.writer().writeByte(4);
              m3.writer().writeShort(itemTemplate4.id);
              m3.writer().writeShort(list_da_kham.get(i).quant);
              m3.writer().writeUTF(itemTemplate4.name);
              m3.writer().writeShort(itemTemplate4.icon);
            } else if (list_da_kham.get(i).category == 7
                && (list_da_kham.get(i).id == 16 || list_da_kham.get(i).id == 17)) {
              ItemTemplate7 itemTemplate7 = ItemTemplate7.get_it_by_id(list_da_kham.get(i).id);
              if (itemTemplate7 == null) continue;
              m3.writer().writeByte(7);
              m3.writer().writeShort(itemTemplate7.id);
              m3.writer().writeShort(list_da_kham.get(i).quant);
              m3.writer().writeUTF(itemTemplate7.name);
              m3.writer().writeShort(itemTemplate7.icon);
            }
          }
          p.addmsg(m3);
          m3.cleanup();
        }
        if (myFashion != null && p.upgrade_skin != null) { // bo vao hoac update sau khi
          // upgrade
          Message m = new Message(81);
          m.writer().writeByte(1);
          m.writer().writeByte(105);
          m.writer().writeShort(id);
          m.writer().writeByte(0);
          m.writer().writeByte(0);
          m.writer().writeByte(1);
          //
          m.writer().writeInt(get_beri_up(myFashion.level));
          m.writer().writeShort(get_ruby_up(myFashion.level));
          m.writer().writeInt(get_extol_up(myFashion.level));
          p.addmsg(m);
          m.cleanup();
          p.upgrade_skin.skin = myFashion;
        }
      } else if (type == 1 && cat == 7 && (id == 16 || id == 17) && pos == 1 && bovao == 1) {
        // them sao 8 canh
        if (p.item.total_item_bag_by_id(7, id) > 0 && p.upgrade_skin != null
            && p.upgrade_skin.skin != null) {
          Message m = new Message(81);
          m.writer().writeByte(1);
          m.writer().writeByte(7);
          m.writer().writeShort(id);
          m.writer().writeByte(0); // percent
          m.writer().writeByte(pos); // pos
          m.writer().writeByte(1);
          //
          m.writer().writeInt(get_beri_up(p.upgrade_skin.skin.level));
          m.writer().writeShort(get_ruby_up(p.upgrade_skin.skin.level));
          m.writer().writeInt(get_extol_up(p.upgrade_skin.skin.level));
          p.addmsg(m);
          m.cleanup();
          p.upgrade_skin.upgrade_skin_data[1] = id;
          //
          // if (id == 17) {
          // for (int i = 2; i < p.upgrade_skin.upgrade_skin_data.length; i++) {
          // if (p.upgrade_skin.upgrade_skin_data[i] != -1) {
          // if (p.item.total_item_bag_by_id(4, p.upgrade_skin.upgrade_skin_data[i]) > 0
          // && p.upgrade_skin.upgrade_skin_data[i] < 80
          // && check_id_ngoc(p.upgrade_skin.upgrade_skin_data[i])) {
          // m = new Message(81);
          // m.writer().writeByte(1);
          // m.writer().writeByte(4);
          // m.writer().writeShort(p.upgrade_skin.upgrade_skin_data[i]);
          // m.writer().writeByte(0);
          // m.writer().writeByte(i);
          // m.writer().writeByte(1);
          // //
          // m.writer().writeInt(get_beri_up(p.upgrade_skin.skin.level));
          // m.writer().writeShort(get_ruby_up(p.upgrade_skin.skin.level));
          // m.writer().writeInt(get_extol_up(p.upgrade_skin.skin.level));
          // p.addmsg(m);
          // m.cleanup();
          // }
          // }
          // }
          // }
        }
      } else if (type == 1 && cat == 7 && (id == 16 || id == 17) && pos == 1 && bovao == 0) {
        // huy sao 8 canh
        if (p.upgrade_skin != null && p.upgrade_skin.skin != null) {
          Message m = new Message(81);
          m.writer().writeByte(1);
          m.writer().writeByte(7);
          m.writer().writeShort(id);
          m.writer().writeByte(0); // percent
          m.writer().writeByte(pos); // pos
          m.writer().writeByte(0);
          //
          m.writer().writeInt(get_beri_up(p.upgrade_skin.skin.level));
          m.writer().writeShort(get_ruby_up(p.upgrade_skin.skin.level));
          m.writer().writeInt(get_extol_up(p.upgrade_skin.skin.level));
          p.addmsg(m);
          m.cleanup();
          p.upgrade_skin.upgrade_skin_data[1] = -1;
          //
          // if (id == 17) {
          // for (int i = 2; i < p.upgrade_skin.upgrade_skin_data.length; i++) {
          // if (p.upgrade_skin.upgrade_skin_data[i] != -1) {
          // if (p.item.total_item_bag_by_id(4, p.upgrade_skin.upgrade_skin_data[i]) > 0
          // && p.upgrade_skin.upgrade_skin_data[i] < 80
          // && check_id_ngoc(p.upgrade_skin.upgrade_skin_data[i])) {
          // m = new Message(81);
          // m.writer().writeByte(1);
          // m.writer().writeByte(4);
          // m.writer().writeShort(p.upgrade_skin.upgrade_skin_data[i]);
          // m.writer().writeByte(0);
          // m.writer().writeByte(i);
          // m.writer().writeByte(1);
          // //
          // m.writer().writeInt(get_beri_up(p.upgrade_skin.skin.level));
          // m.writer().writeShort(get_ruby_up(p.upgrade_skin.skin.level));
          // m.writer().writeInt(get_extol_up(p.upgrade_skin.skin.level));
          // p.addmsg(m);
          // m.cleanup();
          // }
          // }
          // }
          // }
        }
      } else if (type == 1 && cat == 4 && bovao == 1) {
        if (p.item.total_item_bag_by_id(4, id) > 0 && p.upgrade_skin != null
            && p.upgrade_skin.skin != null && pos > 0
            && pos < p.upgrade_skin.upgrade_skin_data.length && id < 80 && check_id_ngoc(id)) {
          Message m = new Message(81);
          m.writer().writeByte(1);
          m.writer().writeByte(4);
          m.writer().writeShort(id);
          m.writer().writeByte(PERCENT[Rebuild_Item.get_percent_hop_ngoc(id)]);
          m.writer().writeByte(pos);
          m.writer().writeByte(1);
          //
          m.writer().writeInt(get_beri_up(p.upgrade_skin.skin.level));
          m.writer().writeShort(get_ruby_up(p.upgrade_skin.skin.level));
          m.writer().writeInt(get_extol_up(p.upgrade_skin.skin.level));
          p.addmsg(m);
          m.cleanup();
          p.upgrade_skin.upgrade_skin_data[pos] = id;
          //
          // if (p.upgrade_skin != null && p.upgrade_skin.skin != null
          // && (p.upgrade_skin.upgrade_skin_data[1] == 16
          // || p.upgrade_skin.upgrade_skin_data[1] == 17)
          // && p.item.total_item_bag_by_id(7, p.upgrade_skin.upgrade_skin_data[1]) > 0) {
          // int percent2 = -1;
          // if (p.upgrade_skin != null && p.upgrade_skin.skin != null) {
          // for (int i = 0; i < p.upgrade_skin.upgrade_skin_data.length; i++) {
          // if (p.upgrade_skin.upgrade_skin_data[i] != -1
          // && p.upgrade_skin.upgrade_skin_data[i] < 80
          // && check_id_ngoc(p.upgrade_skin.upgrade_skin_data[i])) {
          // //
          // int percent0 = get_percent(p.upgrade_skin.skin.level);
          // percent0 += p.upgrade_skin.skin.level * 4;
          // percent0 /= 4;
          // percent0 = (percent0 * PERCENT[Rebuild_Item
          // .get_percent_hop_ngoc(p.upgrade_skin.upgrade_skin_data[i])]) / PERCENT[5];
          // //
          // percent2 += percent0;
          // }
          // }
          // }
          // percent2 -= p.upgrade_skin.skin.level * 4;
          // if (percent2 < 0) {
          // percent2 = 0;
          // }
          // //
          // m = new Message(81);
          // m.writer().writeByte(1);
          // m.writer().writeByte(7);
          // m.writer().writeShort(p.upgrade_skin.upgrade_skin_data[1]);
          // m.writer().writeByte(percent2); // percent
          // m.writer().writeByte(1); // pos
          // m.writer().writeByte(1);
          // //
          // m.writer().writeInt(get_beri_up(p.upgrade_skin.skin.level));
          // m.writer().writeShort(get_ruby_up(p.upgrade_skin.skin.level));
          // m.writer().writeInt(get_extol_up(p.upgrade_skin.skin.level));
          // p.addmsg(m);
          // m.cleanup();
          // }
        }
      } else if (type == 1 && cat == 4 && bovao == 0) {
        if (p.upgrade_skin != null && p.upgrade_skin.skin != null && pos > 0
            && pos < p.upgrade_skin.upgrade_skin_data.length) {
          Message m = new Message(81);
          m.writer().writeByte(1);
          m.writer().writeByte(4);
          m.writer().writeShort(id);
          m.writer().writeByte(0);
          m.writer().writeByte(pos);
          m.writer().writeByte(0);
          //
          m.writer().writeInt(get_beri_up(p.upgrade_skin.skin.level));
          m.writer().writeShort(get_ruby_up(p.upgrade_skin.skin.level));
          m.writer().writeInt(get_extol_up(p.upgrade_skin.skin.level));
          p.addmsg(m);
          m.cleanup();
          p.upgrade_skin.upgrade_skin_data[pos] = -1;
          //
          // if (p.upgrade_skin != null && p.upgrade_skin.skin != null
          // && (p.upgrade_skin.upgrade_skin_data[1] == 16
          // || p.upgrade_skin.upgrade_skin_data[1] == 17)
          // && p.item.total_item_bag_by_id(7, p.upgrade_skin.upgrade_skin_data[1]) > 0) {
          // int percent2 = -1;
          // if (p.upgrade_skin != null && p.upgrade_skin.skin != null) {
          // for (int i = 0; i < p.upgrade_skin.upgrade_skin_data.length; i++) {
          // if (p.upgrade_skin.upgrade_skin_data[i] != -1
          // && p.upgrade_skin.upgrade_skin_data[i] < 80
          // && check_id_ngoc(p.upgrade_skin.upgrade_skin_data[i])) {
          // //
          // int percent0 = get_percent(p.upgrade_skin.skin.level);
          // percent0 += p.upgrade_skin.skin.level * 4;
          // percent0 /= 4;
          // percent0 = (percent0 * PERCENT[Rebuild_Item
          // .get_percent_hop_ngoc(p.upgrade_skin.upgrade_skin_data[i])]) / PERCENT[5];
          // //
          // percent2 += percent0;
          // }
          // }
          // }
          // percent2 -= p.upgrade_skin.skin.level * 4;
          // if (percent2 < 0) {
          // percent2 = 0;
          // }
          // //
          // m = new Message(81);
          // m.writer().writeByte(1);
          // m.writer().writeByte(7);
          // m.writer().writeShort(p.upgrade_skin.upgrade_skin_data[1]);
          // m.writer().writeByte(percent2); // percent
          // m.writer().writeByte(1); // pos
          // m.writer().writeByte(1);
          // //
          // m.writer().writeInt(get_beri_up(p.upgrade_skin.skin.level));
          // m.writer().writeShort(get_ruby_up(p.upgrade_skin.skin.level));
          // m.writer().writeInt(get_extol_up(p.upgrade_skin.skin.level));
          // p.addmsg(m);
          // m.cleanup();
          // }
        }
      }
    } else if (m2.reader().available() == 3) {
      byte type = m2.reader().readByte();
      short id = m2.reader().readShort();
      // System.out.println(type + " " + id);
      if (type == 4) {
        ItemFashionP2 myFashion = p.check_fashion(id);
        if (myFashion != null && p.upgrade_skin != null && p.upgrade_skin.skin != null
            && p.upgrade_skin.skin.id == myFashion.id) {
          boolean check_da_kham = false;
          for (int i = 0; i < p.upgrade_skin.upgrade_skin_data.length; i++) {
            if (p.upgrade_skin.upgrade_skin_data[i] != -1) {
              check_da_kham = true;
              break;
            }
          }
          if (check_da_kham) {
            ItemFashion itF = ItemFashion.get_item(myFashion.id);
            boolean isTest = core.Manager.gI().isTestMode() || (p.admin == 1);
            String[] options = !isTest
                ? new String[]{"1 lần", "5 lần", "10 lần", "Đóng"}
                : new String[]{"1 lần", "10 lần", "20 lần", "50 lần", "100 lần", "200 lần", "500 lần", "Nhập số lần", "Đóng"};
            byte[] icons = new byte[options.length];
            for (int i = 0; i < icons.length - 1; i++) icons[i] = -1;
            icons[icons.length - 1] = 1;

            model.YesNoDialog ynd = new model.YesNoDialog(p, 81, "Auto Cường Hóa Thời Trang",
                "Chọn số lần Auto Cường Hóa " + itF.name + " (Cấp hiện tại: +" + myFashion.level + "/42):",
                options, icons, value -> {
                    if (!isTest) {
                        int count = switch (value) {
                            case 0 -> 1;
                            case 1 -> 5;
                            case 2 -> 10;
                            default -> 0;
                        };
                        if (count > 0) {
                            executeAutoSkinUpgrade(p, count);
                        }
                        return;
                    }
                    if (value == 7) {
                        new model.InputDialog(p, 81, "Nhập số lần", new String[]{"Số lần muốn cường hóa:"}, inputs -> {
                            if (inputs == null || inputs.length == 0 || inputs[0].trim().isEmpty()) return;
                            try {
                                int count = Integer.parseInt(inputs[0].trim());
                                if (count <= 0) {
                                    p.getService().send_box_ThongBao_OK("Số lần không hợp lệ!");
                                    return;
                                }
                                executeAutoSkinUpgrade(p, count);
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
                        executeAutoSkinUpgrade(p, count);
                    }
                });
            ynd.startYesNo();
          } else {
            p.getService().send_box_ThongBao_OK("Hãy bỏ đá khảm trước");
          }
        }
      }
    } else {
      byte type = m2.reader().readByte();
      if (type == 2) {
        if (p.upgrade_skin != null && p.upgrade_skin.skin != null) {
          executeAutoSkinUpgrade(p, 1);
        }
      } else if (type == 6) {
        m2.reader().readShort(); // id
        byte size = m2.reader().readByte();
        for (int i = 0; i < size; i++) {
          m2.reader().readShort(); // list da kham
        }
        Message m = new Message(81);
        m.writer().writeByte(6);
        m.writer().writeByte(0); // ti le may man + them
        p.addmsg(m);
        m.cleanup();
      }
    }
  }

  public static void executeAutoSkinUpgrade(Player p, int loopCount) {
    if (p == null || p.item == null || p.upgrade_skin == null || p.upgrade_skin.skin == null) {
      try { p.getService().send_box_ThongBao_OK("Vui lòng chọn thời trang cần nâng cấp!"); } catch (Exception ignored) {}
      return;
    }
    if (p.trade_target != null) {
      try { p.getService().send_box_ThongBao_OK("Không thể nâng cấp khi đang giao dịch!"); } catch (Exception ignored) {}
      return;
    }
    ItemFashionP2 skin = p.upgrade_skin.skin;
    ItemFashion itF = ItemFashion.get_item(skin.id);
    String skinName = (itF != null) ? itF.name : "Thời trang";

    if (skin.level >= 42) {
      try { p.getService().send_box_ThongBao_OK("Thời trang đã đạt cấp tối đa (+42)!"); } catch (Exception ignored) {}
      return;
    }

    short starId = p.upgrade_skin.upgrade_skin_data[1];
    List<Short> gemIds = new ArrayList<>();
    for (int i = 2; i < p.upgrade_skin.upgrade_skin_data.length; i++) {
      short gId = p.upgrade_skin.upgrade_skin_data[i];
      if (gId != -1 && gId < 80 && check_id_ngoc(gId)) {
        gemIds.add(gId);
      }
    }

    if (gemIds.isEmpty()) {
      try { p.getService().send_box_ThongBao_OK("Hãy bỏ đá khảm vào bàn nâng trước!"); } catch (Exception ignored) {}
      return;
    }

    int startLevel = skin.level;
    int successCount = 0;
    int failCount = 0;
    long totalBeriSpent = 0;
    int totalRubySpent = 0;
    long totalExtolSpent = 0;
    String stopReason = "Đã hoàn thành " + loopCount + " lần nâng cấp";

    synchronized (p.item) {
      for (int step = 0; step < loopCount; step++) {
        if (skin.level >= 42) {
          stopReason = "Đạt cấp tối đa (+42)!";
          break;
        }

        int beri_req = get_beri_up(skin.level);
        int ruby_req = get_ruby_up(skin.level);
        int extol_req = get_extol_up(skin.level);

        if (p.get_vang() < beri_req) {
          stopReason = "Không đủ " + ZUtil.number_format(beri_req) + " Beri!";
          break;
        }
        if (p.get_ngoc() < ruby_req) {
          stopReason = "Không đủ " + ZUtil.number_format(ruby_req) + " Ruby!";
          break;
        }
        if (p.get_vnd() < extol_req) {
          stopReason = "Không đủ " + ZUtil.number_format(extol_req) + " Extol!";
          break;
        }

        if (starId == 16 || starId == 17) {
          if (p.item.total_item_bag_by_id(7, starId) < 1) {
            stopReason = "Hết bùa sao 8 cánh!";
            break;
          }
        }

        boolean hasAllGems = true;
        for (short gId : gemIds) {
          if (p.item.total_item_bag_by_id(4, gId) < 1) {
            hasAllGems = false;
            break;
          }
        }
        if (!hasAllGems) {
          stopReason = "Hết đá khảm nguyên liệu trong hành trang!";
          break;
        }

        // Deduct currencies
        p.update_vang(-beri_req);
        p.update_ngoc(-ruby_req);
        p.updateVnd(-extol_req);
        totalBeriSpent += beri_req;
        totalRubySpent += ruby_req;
        totalExtolSpent += extol_req;

        // Deduct items
        if (starId == 16 || starId == 17) {
          p.item.remove_item47(7, starId, 1);
        }
        for (short gId : gemIds) {
          p.item.remove_item47(4, gId, 1);
        }

        int percent = 0;
        boolean mm = (starId == 16 || starId == 17);
        for (short gId : gemIds) {
          percent += PERCENT[Rebuild_Item.get_percent_hop_ngoc(gId)];
        }
        // BUG #2 fix: giảm hệ số penalty từ *4 xuống *2 để đá tốt nhất (50%)
        // vẫn có cơ hội ở level cao hơn.
        // Ví dụ đá tốt nhất (idx5=50) tại level 20: 50 - 20*2 = 10%  hợp lý
        // Tại level 25: 50 - 25*2 = 0%  cần bùa sao để có cơ hội
        percent -= skin.level * 2;
        if (percent < 0) {
          percent = 0;
        }
        if (mm) {
          percent *= 2; // bùa sao 8 cánh nhân đôi tỷ lệ
        }

        boolean suc;
        if (percent >= 100) {
          suc = true;
        } else {
          // random(100) để range rõ ràng: percent% thành công
          suc = percent > ZUtil.random(100);
        }

        if (suc) {
          skin.level++;
          successCount++;
        } else {
          failCount++;
          if (skin.level % 3 != 0) {
            skin.level -= (skin.level % 3);
          }
        }
      }
    }

    try {
      p.updateMoney();
      p.item.updateInventory(false);
      p.setAbility();
      p.update_info_to_all();
      // BUG #4 fix: không reset skin và upgrade_skin_data để người dùng
      // không cần bỏ đá lại từ đầu nếu muốn tiếp tục cường hóa

      p.getService().send_box_ThongBao_OK(
          "KẾT QUẢ AUTO CƯỜNG HÓA THỜI TRANG:\n" +
          "- Thời trang: " + skinName + "\n" +
          "- Cấp độ: +" + startLevel + " -> +" + skin.level + "\n" +
          "- Thành công: " + successCount + " lần | Thất bại: " + failCount + " lần\n" +
          "- Tổng tiêu hao: " + ZUtil.number_format(totalBeriSpent) + " Beri | " + ZUtil.number_format(totalRubySpent) + " Ruby | " + ZUtil.number_format(totalExtolSpent) + " Extol\n" +
          "- Trạng thái: " + stopReason
      );
    } catch (Exception ignored) {}
  }

  private static int get_percent(byte level) {
    // BUG #1 fix: phải check level > 39 TRƯỚC level > 27
    // Thứ tự cũ sai: level > 39 không bao giờ chạy vì branch > 27 bắt trước
    if (level > 39) {
      return 4; // Cố định 4% từ level 40 trở lên
    } else if (level > 27) {
      int val = 16 - (level - 27); // lv2815, lv2914, ... lv395  khớp với branch trên
      return Math.max(val, 4);    // clamp tối thiểu 4% để tránh âm
    } else {
      int result = 80;
      for (int i = 0; i < level; i++) {
        if (i > 0 && i % 5 == 0) {
          result -= 4;
        } else {
          result -= 2;
        }
      }
      return Math.max(result, 4); // clamp tối thiểu 4%
    }
  }

  private static int get_extol_up(int level) {
    return (get_ruby_up(level) * 1_000);
  }

  private static int get_ruby_up(int level) { // max 32_000
    // BUG #11 fix: xóa dead code `level == 2` trong else (đã return ở if trên)
    // Logic: chỉ các mốc level % 3 == 2 (lv 2,5,8,11,...) mới tốn ruby — thiết kế cố ý
    if (level == 2) {
      return 5;
    }
    int result = 0;
    if (level % 3 == 2) {
      for (int i = 0; i < level; i++) {
        if (i == 2 || i % 3 == 2) {
          result += 5;
        }
      }
    }
    return result;
  }

  private static int get_beri_up(int level) {
    int result = 7_500;
    int moc_buff = 22_500;
    for (int i = 0; i <= level; i++) {
      if (i == 2 || i % 3 == 2) {
        result += moc_buff;
        if (moc_buff < 30_000) {
          moc_buff += 7_500;
        }
      } else {
        result += 3_750;
      }
    }
    return result;
  }
}
