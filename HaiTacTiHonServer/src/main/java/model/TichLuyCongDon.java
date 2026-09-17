package model;

import historys.HistoryManager;
import event.EventManager;
import network.Service;
import core.ZUtil;
import network.Message;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import template.GiftBox;
import template.ItemFashion;
import template.ItemTemplate4;
import template.ItemTemplate7;
import template.TichLuyEntry;
import zabstracts.AbsTichLuyCongDon;

public class TichLuyCongDon extends AbsTichLuyCongDon {

    public TichLuyCongDon() {
        this.type = 4; // Type 4 for Cumulative Topup / Recharge
        initDefaultEntries();
        this.setSeasonKey("TICH_LUY_CONG_DON_MAIN_1");
        timeStart("0:00 19/8/2026");
        timeEnd("23:59 30/8/2026");
    }

    /**
     * Khởi tạo danh sách các mốc tích lũy cộng dồn mặc định vào biến `entries` của super class (AbsTichLuyCongDon).
     */
    private void initDefaultEntries() {
        entries.clear();

        TichLuyEntry t = new TichLuyEntry();
        t.num = 50; // Mốc 50K VNĐ
        t.cat = new byte[]{4, 4, 7, 4, 4, 105};
        t.id = new short[]{0, 1, 2, 802, 29, 14};
        t.quant = new short[]{5, 100, 10, 5, 2, 1}; // 5M Beri, 100 Ruby, 10 Đá CH 3, 5 Vé VIP, 2 Rương Ác Quỷ, Thời Trang Đội Trưởng
        entries.add(t);

        t = new TichLuyEntry();
        t.num = 100; // Mốc 100K VNĐ
        t.cat = new byte[]{4, 4, 7, 7, 4, 4, 110};
        t.id = new short[]{0, 1, 3, 10, 802, 158, 5};
        t.quant = new short[]{10, 250, 10, 10, 10, 2, 1}; // 10M Beri, 250 Ruby, 10 Đá CH 4, 10 Thạch anh, 10 Vé VIP, 2 Rương Đại Ác Quỷ, Pet Capybara
        entries.add(t);

        t = new TichLuyEntry();
        t.num = 200; // Mốc 200K VNĐ
        t.cat = new byte[]{4, 4, 7, 7, 4, 4, 105};
        t.id = new short[]{0, 1, 4, 10, 802, 732, 123};
        t.quant = new short[]{25, 500, 10, 20, 20, 1, 1}; // 25M Beri, 500 Ruby, 10 Đá CH 5, 20 Thạch anh, 20 Vé VIP, 1 Rương Đá Thần Tự Chọn, Thời Trang UTA
        entries.add(t);

        t = new TichLuyEntry();
        t.num = 500; // Mốc 500K VNĐ
        t.cat = new byte[]{4, 4, 7, 7, 4, 4, 105};
        t.id = new short[]{0, 1, 5, 10, 802, 691, 132};
        t.quant = new short[]{60, 1500, 15, 35, 35, 1, 1}; // 60M Beri, 1500 Ruby, 15 Đá CH 6, 35 Thạch anh, 35 Vé VIP, 1 Rương Đá Siêu Cấp Tự Chọn, Thời Trang Katakuri
        entries.add(t);

        t = new TichLuyEntry();
        t.num = 1000; // Mốc 1M VNĐ
        t.cat = new byte[]{4, 4, 7, 4, 4, 4, 110};
        t.id = new short[]{0, 1, 10, 802, 894, 829, 38};
        t.quant = new short[]{120, 3500, 60, 50, 1, 1, 1}; // 120M Beri, 3500 Ruby, 60 Thạch anh, 50 Vé VIP, 1 Rương Trái Ác Quỷ Cao Cấp, 1 Rương Cam +15 Cùng Hệ, Pet Rồng Tết
        entries.add(t);

        t = new TichLuyEntry();
        t.num = 2000; // Mốc 2M VNĐ
        t.cat = new byte[]{4, 4, 7, 4, 4, 110, 105};
        t.id = new short[]{0, 1, 10, 802, 829, 48, 56};
        t.quant = new short[]{250, 8000, 100, 80, 1, 1, 1}; // 250M Beri, 8000 Ruby, 100 Thạch anh, 80 Vé VIP, 1 Rương Cam +15 Cùng Hệ, Pet Solgaleo, Thời Trang Natra Ma Đồng
        entries.add(t);

        t = new TichLuyEntry();
        t.num = 3000; // Mốc 3M VNĐ
        t.cat = new byte[]{4, 4, 7, 4, 4, 110, 105};
        t.id = new short[]{0, 1, 10, 802, 829, 51, 74};
        t.quant = new short[]{350, 12000, 150, 120, 1, 1, 1}; // 350M Beri, 12000 Ruby, 150 Thạch anh, 120 Vé VIP, 1 Rương Cam +15 Cùng Hệ, Pet Luffy Mèo, Thời Trang Râu Đen
        entries.add(t);

        t = new TichLuyEntry();
        t.num = 5000; // Mốc 5M VNĐ
        t.cat = new byte[]{4, 4, 7, 4, 4, 4, 110, 105};
        t.id = new short[]{0, 1, 10, 802, 829, 911, 50, 85};
        t.quant = new short[]{600, 20000, 250, 200, 1, 3, 1, 1}; // 600M Beri, 20000 Ruby, 250 Thạch anh, 200 Vé VIP, 1 Rương Cam +15 Cùng Hệ, 3 Rương Siêu Đại Ác Quỷ, Pet Thần Tình Yêu, Thời Trang Tôn Ngộ Không
        entries.add(t);
    }

    /**
     * Lấy Key mùa hiện tại cho Tích Lũy Cộng Dồn.
     */
    @Override
    public String getSeasonKey() {
        return (this.key != null && !this.key.trim().isEmpty()) ? this.key : "TICH_LUY_CONG_DON";
    }

    /**
     * Kiểm tra thời gian mùa tích lũy cộng dồn còn hiệu lực hay đã hết hạn.
     */
    public static boolean isSeasonActive() {
        AbsTichLuyCongDon inst = AbsTichLuyCongDon.get(4);
        if (inst != null) {
            return inst.checkTimeActive();
        }
        return true;
    }

    public List<TichLuyEntry> getActiveEntries(Player p) {
        return this.entries;
    }

    /**
     * Tải hoặc tự động khởi tạo dữ liệu lịch sử tích lũy cộng dồn mùa riêng từ bảng historys.
     */
    private JSONObject loadOrInitPlayerHistory(Player p, String seasonKey, int numEntries) {
        JSONObject json = null;
        try {
            String jsonStr = HistoryManager.loadData(p.IDPlayer, seasonKey);
            if (jsonStr != null && !jsonStr.trim().isEmpty()) {
                Object obj = JSONValue.parse(jsonStr);
                if (obj instanceof JSONObject) {
                    json = (JSONObject) obj;
                }
            }
        } catch (Exception e) {
            json = null;
        }

        if (json == null) {
            // Tự động khởi tạo dữ liệu mặc định với thời gian hiện tại
            json = new JSONObject();
            JSONArray claimedArr = new JSONArray();
            for (int i = 0; i < numEntries; i++) {
                claimedArr.add(0);
            }
            json.put("claimed", claimedArr);
            json.put("created_at", System.currentTimeMillis());

            // Lưu dữ liệu khởi tạo chuẩn vào bảng historys
            try {
                HistoryManager.saveData(p, seasonKey, json.toJSONString());
            } catch (Exception e) {
                // Ignore DB error
            }
        }

        // Đảm bảo mảng claimed có đủ kích thước
        JSONArray claimedArr = (JSONArray) json.get("claimed");
        if (claimedArr == null) {
            claimedArr = new JSONArray();
            json.put("claimed", claimedArr);
        }
        while (claimedArr.size() < numEntries) {
            claimedArr.add(0);
        }

        return json;
    }

    private void savePlayerHistory(Player p, String seasonKey, JSONObject json) {
        try {
            HistoryManager.saveData(p, seasonKey, json.toJSONString());
        } catch (Exception e) {
            // Ignore DB error
        }
    }

    @Override
    public void showTable(Player p) throws IOException {
        if (p == null || p.isBot) return;
        p.refreshRecharge();

        List<TichLuyEntry> entryList = getActiveEntries(p);
        if (entryList == null || entryList.isEmpty()) {
            p.getService().send_box_ThongBao_OK("Hiện tại không có mốc tích lũy cộng dồn nào.");
            return;
        }

        byte[] checkArray = new byte[entryList.size()];
        try {
            String seasonKey = getSeasonKey();
            JSONObject json = loadOrInitPlayerHistory(p, seasonKey, entryList.size());
            if (json != null) {
                JSONArray claimedArr = (JSONArray) json.get("claimed");
                if (claimedArr != null) {
                    for (int i = 0; i < entryList.size(); i++) {
                        if (i < claimedArr.size()) {
                            checkArray[i] = ((Number) claimedArr.get(i)).byteValue();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int pointK = Math.max(0, (int) (p.getTongnap() / 1000));
        p.getService().sendTichLuyCongDonTable(pointK, entryList, checkArray);
    }

    @Override
    public void process(Player p, Message m2) throws IOException {
        byte type = m2.reader().readByte();
        if (type == 0) {
            showTable(p);
            return;
        }

        if (type == 1) {
            p.refreshRecharge();
            // Client gửi writeShort(cost) (mốc cost tính bằng K, ví dụ: 50, 100, 200, 500, 1000...)
            short cost = m2.reader().readShort();

            List<TichLuyEntry> entryList = getActiveEntries(p);
            if (entryList == null || entryList.isEmpty()) {
                p.getService().send_box_ThongBao_OK("Hiện tại không có mốc tích lũy cộng dồn nào.");
                return;
            }

            int foundIndex = -1;
            TichLuyEntry listGet = null;
            for (int i = 0; i < entryList.size(); i++) {
                TichLuyEntry entry = entryList.get(i);
                int entryCostK = (entry.num >= 10000 ? entry.num / 1000 : entry.num);
                if (entryCostK == cost || entry.num == cost) {
                    foundIndex = i;
                    listGet = entry;
                    break;
                }
            }

            if (foundIndex == -1) {
                // Fallback nếu client gửi index thay vì cost
                if (cost >= 0 && cost < entryList.size()) {
                    foundIndex = cost;
                    listGet = entryList.get(foundIndex);
                }
            }

            if (listGet == null || foundIndex == -1) {
                p.getService().send_box_ThongBao_OK("Mốc nhận quà không hợp lệ.");
                return;
            }

            int reqCostK = (listGet.num >= 10000 ? listGet.num / 1000 : listGet.num);

            String seasonKey = getSeasonKey();
            JSONObject json = loadOrInitPlayerHistory(p, seasonKey, entryList.size());

            int pointK = Math.max(0, (int) (p.getTongnap() / 1000));

            JSONArray claimedArr = (JSONArray) json.get("claimed");
            boolean isClaimed = (foundIndex < claimedArr.size() && ((Number) claimedArr.get(foundIndex)).intValue() == 1);

            if (isClaimed) {
                p.getService().send_box_ThongBao_OK("Bạn đã nhận quà ở mốc này rồi!");
                return;
            }

            // Kiểm tra điều kiện nạp đủ mốc (so sánh theo đơn vị K)
            if (pointK < reqCostK) {
                p.getService().send_box_ThongBao_OK("Chưa đủ mốc tích lũy nạp để nhận quà!");
                return;
            }

            // Xử lý phát quà
            List<GiftBox> list = new ArrayList<>();
            for (int i = 0; i < listGet.cat.length; i++) {
                if (listGet.cat[i] == 4) {
                    ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(listGet.id[i]);
                    if (listGet.id[i] == -10) {
                        int level = p.level / 10;
                        if (level == 0) level = 1;
                        itemTemplate4 = ItemTemplate4.get_it_by_id(level + 694);
                    }
                    if (listGet.id[i] == -11) {
                        short[] ids = new short[]{49, 55, 61, 67, 73, 79};
                        itemTemplate4 = ItemTemplate4.get_it_by_id(ids[ZUtil.random(ids.length)]);
                    }
                    if (itemTemplate4 != null) {
                        GiftBox gb4 = new GiftBox();
                        gb4.id = itemTemplate4.id;
                        gb4.type = 4;
                        gb4.name = itemTemplate4.name;
                        gb4.icon = itemTemplate4.icon;
                        gb4.num = listGet.quant[i];
                        if (gb4.id == 0) {
                            gb4.num *= 1_000_000;
                        }
                        gb4.color = 0;
                        list.add(gb4);
                    }
                } else if (listGet.cat[i] == 7) {
                    ItemTemplate7 itemTemplate7 = ItemTemplate7.get_it_by_id(listGet.id[i]);
                    if (itemTemplate7 != null) {
                        GiftBox gb4 = new GiftBox();
                        gb4.id = itemTemplate7.id;
                        gb4.type = 7;
                        gb4.name = itemTemplate7.name;
                        gb4.icon = itemTemplate7.icon;
                        gb4.num = listGet.quant[i];
                        gb4.color = 0;
                        list.add(gb4);
                    }
                } else if (listGet.cat[i] == 3) {
                    template.ItemTemplate3 itemTemplate3 = template.ItemTemplate3.get_it_by_id(listGet.id[i]);
                    if (itemTemplate3 != null) {
                        GiftBox gb4 = new GiftBox();
                        gb4.id = itemTemplate3.id;
                        gb4.type = 3;
                        gb4.name = itemTemplate3.name;
                        gb4.icon = itemTemplate3.icon;
                        gb4.num = listGet.quant[i];
                        gb4.color = (byte) itemTemplate3.color;
                        list.add(gb4);
                    }
                } else if (listGet.cat[i] == 105) {
                    ItemFashion itemFashion = ItemFashion.get_item(listGet.id[i]);
                    if (itemFashion != null) {
                        GiftBox gb4 = new GiftBox();
                        gb4.id = itemFashion.ID;
                        gb4.type = 105;
                        gb4.name = itemFashion.name;
                        gb4.icon = itemFashion.idIcon;
                        gb4.num = listGet.quant[i];
                        gb4.color = 0;
                        list.add(gb4);
                    }
                } else if (listGet.cat[i] == 110) {
                    Pet petTemplate = Pet.getTemplate(listGet.id[i]);
                    short petId = (petTemplate != null) ? petTemplate.id : listGet.id[i];
                    if (p.my_pet == null) {
                        p.my_pet = new ArrayList<>();
                    }
                    MyPet pet = null;
                    for (int i1 = 0; i1 < p.my_pet.size(); i1++) {
                        if (p.my_pet.get(i1).id == petId) {
                            pet = p.my_pet.get(i1);
                            break;
                        }
                    }
                    if (pet != null) {
                        pet.time = -1;
                    } else {
                        pet = new MyPet();
                        pet.id = petId;
                        pet.isUse = false;
                        pet.template = (petTemplate != null) ? petTemplate : Pet.getTemplate(petId);
                        pet.time = -1;
                        p.my_pet.add(pet);
                    }
                    GiftBox gb4 = new GiftBox();
                    gb4.id = petId;
                    gb4.type = 110;
                    gb4.name = "Pet " + (petTemplate != null ? petTemplate.name : ("#" + petId));
                    gb4.icon = (petTemplate != null ? petTemplate.icon : 0);
                    gb4.num = listGet.quant[i];
                    gb4.color = 0;
                    list.add(gb4);
                }
 else {
                    p.getService().send_box_ThongBao_OK("Có lỗi xảy ra, hãy thử lại sau!");
                    return;
                }
            }

            // Đánh dấu đã nhận trong mảng JSON
            claimedArr.set(foundIndex, 1);
            savePlayerHistory(p, seasonKey, json);

            if (!list.isEmpty()) {
                Service.send_gift(p, 1, "Tích Lũy Cộng Dồn", "Phần thưởng mốc " + reqCostK + "K", list, true);
            }

            // Gửi packet thông báo nhận thành công (SubCmd 2) theo mốc reqCostK
            p.getService().sendTichLuyCongDonClaimSuccess((short) reqCostK, pointK);
        }
    }
}
