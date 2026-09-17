package zabstracts;

import model.Player;
import core.ZUtil;
import model.InputDialog;
import model.YesNoDialog;
import template.ItemTemplate4;
import template.ItemTemplate7;
import network.Message;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbsCheTao {

    public static final java.util.Map<Integer, AbsCheTao> CHE_TAO_MAP = new java.util.concurrent.ConcurrentHashMap<>();

    public static void register(AbsCheTao cheTao) {
        if (cheTao != null) {
            CHE_TAO_MAP.put(cheTao.getId(), cheTao);
        }
    }

    public static AbsCheTao get(int resultId) {
        if (CHE_TAO_MAP.isEmpty()) {
            initRegistry();
        }
        return CHE_TAO_MAP.get(resultId);
    }

    public static void showTable(Player p, int resultId) throws IOException {
        AbsCheTao cheTao = get(resultId);
        if (cheTao != null) {
            p.setCheTao(cheTao);
            cheTao.show_table(p);
        } else {
            p.getService().send_box_ThongBao_OK("Không tìm thấy công thức chế tạo cho vật phẩm này!");
        }
    }

    public static void showTable(Player p, Class<? extends AbsCheTao> clazz) throws IOException {
        if (CHE_TAO_MAP.isEmpty()) {
            initRegistry();
        }
        for (AbsCheTao instance : CHE_TAO_MAP.values()) {
            if (clazz.isInstance(instance)) {
                p.setCheTao(instance);
                instance.show_table(p);
                return;
            }
        }
        try {
            AbsCheTao instance = clazz.getDeclaredConstructor().newInstance();
            register(instance);
            p.setCheTao(instance);
            instance.show_table(p);
        } catch (Exception e) {
            e.printStackTrace();
            p.getService().send_box_ThongBao_OK("Không tìm thấy công thức chế tạo!");
        }
    }

    public static synchronized void initRegistry() {
        if (!CHE_TAO_MAP.isEmpty()) return;
        java.util.List<Class<? extends AbsCheTao>> classes = core.ZUtil.findSubclasses(AbsCheTao.class);
        for (Class<? extends AbsCheTao> clazz : classes) {
            try {
                AbsCheTao instance = clazz.getDeclaredConstructor().newInstance();
                register(instance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class Ingredient {
        public final int id;
        public final byte cat;
        public final int quantity;

        public Ingredient(int id, byte cat, int quantity) {
            this.id = id;
            this.cat = cat;
            this.quantity = quantity;
        }
    }

    protected final int resultId;
    protected final byte resultCat;
    protected final int costBeri;
    protected final int costRuby;
    protected int costExtol = 0;
    protected int successRate = 100;
    protected boolean payChoice = false; // false = đóng phí gộp toàn bộ theo logic gốc
    protected final List<Ingredient> ingredients = new ArrayList<>();
    protected String key;

    public String getSeasonKey() {
        return key != null ? key : "CheTao_" + resultId;
    }

    public void setSeasonKey(String key) {
        this.key = key;
    }

    protected AbsCheTao(int resultId, byte resultCat, int costBeri, int costRuby) {
        this.resultId = resultId;
        this.resultCat = resultCat;
        this.costBeri = costBeri;
        this.costRuby = costRuby;
    }

    protected void addIngredient(int id, byte cat, int quantity) {
        ingredients.add(new Ingredient(id, cat, quantity));
    }

    public int getId() {
        return resultId;
    }

    public byte getClientType() {
        return 20; // Bảng ghép/chế tạo
    }

    public void show_table(Player p) throws IOException {
        if (p.trade_target != null) {
            p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
            return;
        }
        p.setCheTao(this);
        int n = ingredients.size();
        short[] ingIds = new short[n];
        short[] ingQtys = new short[n];
        byte[] ingCats = new byte[n];
        short[] ingIcons = new short[n];
        for (int i = 0; i < n; i++) {
            Ingredient ing = ingredients.get(i);
            ingIds[i] = (short) ing.id;
            ingQtys[i] = (short) ing.quantity;
            ingCats[i] = ing.cat;
            if (ing.cat == 7) {
                ItemTemplate7 it7 = ItemTemplate7.get_it_by_id(ing.id);
                ingIcons[i] = it7 != null ? it7.icon : 0;
            } else {
                ItemTemplate4 it4 = ItemTemplate4.get_it_by_id(ing.id);
                ingIcons[i] = it4 != null ? it4.icon : 0;
            }
        }
        short resultIcon = 0;
        String title = "Chế tạo";
        if (resultCat == 7) {
            ItemTemplate7 res7 = ItemTemplate7.get_it_by_id(resultId);
            if (res7 != null) {
                resultIcon = res7.icon;
                title = "Ghép " + res7.name;
            }
        } else {
            ItemTemplate4 res4 = ItemTemplate4.get_it_by_id(resultId);
            if (res4 != null) {
                resultIcon = res4.icon;
                title = "Ghép " + res4.name;
            }
        }
        p.getService().sendUpgradeDevilCraftPanel(
            title,
            (byte) n,
            ingIds,
            ingQtys,
            ingCats,
            ingIcons,
            costBeri,
            (short) costRuby,
            costExtol,
            (short) resultId,
            (short) 1,
            resultCat,
            resultIcon,
            (byte) successRate
        );
    }

    public boolean checkProcess(Player p, byte action, short id, byte cat, short num) throws IOException {
        return action == 20 && id == resultId && cat == resultCat && num == 0;
    }

    public void process(Player p, byte action, short id, byte cat, short num) throws IOException {
        if (p.trade_target != null) {
            p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
            return;
        }
        if (!isEventActive()) {
            p.getService().send_box_ThongBao_OK("Thời gian ghép vật phẩm này đã kết thúc hoặc chưa mở!");
            return;
        }
        if (p.level < 10) {
            p.getService().send_box_ThongBao_OK("Bạn cần đạt cấp độ 10 trở lên để thực hiện!");
            return;
        }

        String itemName = "";
        if (resultCat == 7) {
            ItemTemplate7 res7 = ItemTemplate7.get_it_by_id(resultId);
            if (res7 != null) itemName = res7.name;
        } else {
            ItemTemplate4 res4 = ItemTemplate4.get_it_by_id(resultId);
            if (res4 != null) itemName = res4.name;
        }
        if (itemName.isEmpty()) itemName = "Vật phẩm " + resultId;

        if (payChoice) {
            final String fItemName = itemName;
            p.setyesNoDialog(new YesNoDialog(p, 998, "Xác nhận", "Chọn loại phí để chế tạo " + fItemName + ":",
                    new String[]{"Beri", "Ruby", "Hủy"}, new byte[]{6, 7, 1},
                    value -> {
                        if (value == 0 || value == 1) {
                            boolean isBeri = (value == 0);
                            boolean isRuby = (value == 1);
                            YesNoDialog ynd = new YesNoDialog(p, 998, "Chế Tạo " + fItemName,
                                    "Chọn số lần chế tạo " + fItemName + " bằng " + (isBeri ? "Beri" : "Ruby") + " (Tỉ lệ: " + successRate + "%):",
                                    new String[]{"1 lần", "10 lần", "20 lần", "50 lần", "100 lần", "200 lần", "500 lần", "Nhập số lần", "Đóng"},
                                    new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, 1}, valCount -> {
                                        if (valCount == 7) {
                                            new model.InputDialog(p, 998, "Nhập số lần", new String[]{"Số lần muốn chế tạo:"}, inputs -> {
                                                if (inputs == null || inputs.length == 0 || inputs[0].trim().isEmpty()) return;
                                                try {
                                                    int count = Integer.parseInt(inputs[0].trim());
                                                    if (count <= 0) {
                                                        p.getService().send_box_ThongBao_OK("Số lần không hợp lệ!");
                                                        return;
                                                    }
                                                    executeCraft(p, count, isBeri, isRuby);
                                                } catch (NumberFormatException e) {
                                                    p.getService().send_box_ThongBao_OK("Vui lòng nhập số hợp lệ!");
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }).startInput();
                                            return;
                                        }
                                        int count = switch (valCount) {
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
                                            try {
                                                executeCraft(p, count, isBeri, isRuby);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                            ynd.startYesNo();
                        }
                    }));
            p.getService().startYesNo();
        } else {
            final String fItemName = itemName;
            YesNoDialog ynd = new YesNoDialog(p, 998, "Chế Tạo " + fItemName,
                    "Chọn số lần chế tạo " + fItemName + " (Tỉ lệ thành công: " + successRate + "%):",
                    new String[]{"1 lần", "10 lần", "20 lần", "50 lần", "100 lần", "200 lần", "500 lần", "Nhập số lần", "Đóng"},
                    new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, 1}, value -> {
                        if (value == 7) {
                            new model.InputDialog(p, 998, "Nhập số lần", new String[]{"Số lần muốn chế tạo:"}, inputs -> {
                                if (inputs == null || inputs.length == 0 || inputs[0].trim().isEmpty()) return;
                                try {
                                    int count = Integer.parseInt(inputs[0].trim());
                                    if (count <= 0) {
                                        p.getService().send_box_ThongBao_OK("Số lần không hợp lệ!");
                                        return;
                                    }
                                    executeCraft(p, count, false, false);
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
                            try {
                                executeCraft(p, count, false, false);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            ynd.startYesNo();
        }
    }

    protected abstract boolean isEventActive();

    protected void afterCraft(Player p, int quantity, boolean success) throws IOException {
        // Hook cho các lớp con kế thừa
    }

    private void confirmCraft(Player p, final int quantity) throws IOException {
        String itemName = "";
        if (resultCat == 7) {
            ItemTemplate7 it7 = ItemTemplate7.get_it_by_id(resultId);
            if (it7 != null) itemName = it7.name;
        } else {
            itemName = ItemTemplate4.get_item_name(resultId);
        }
        if (itemName == null || itemName.isEmpty()) {
            itemName = "Vật phẩm " + resultId;
        }

        StringBuilder costDesc = new StringBuilder("\nChi phí chế tạo:");
        if (costBeri > 0) costDesc.append(String.format("\n- %s Beri", ZUtil.number_format((long) costBeri * quantity)));
        if (costRuby > 0) costDesc.append(String.format("\n- %d Ruby", costRuby * quantity));
        if (costExtol > 0) costDesc.append(String.format("\n- %s Extol", ZUtil.number_format((long) costExtol * quantity)));

        String confirmText = String.format("Bạn muốn chế tạo %d %s? (Tỷ lệ thành công: %d%%)%s",
                quantity, itemName, successRate, costDesc.toString());

        if (payChoice) {
            p.setyesNoDialog(new YesNoDialog(p, 998, "Xác nhận", confirmText,
                    new String[]{"Beri", "Ruby", "Hủy"}, new byte[]{6, 7, 1},
                    value -> {
                        if (value == 0) {
                            executeCraft(p, quantity, true, false);
                        } else if (value == 1) {
                            executeCraft(p, quantity, false, true);
                        }
                    }));
        } else {
            p.setyesNoDialog(new YesNoDialog(p, 998, "Xác nhận", confirmText,
                    new String[]{"Đồng ý", "Hủy"}, new byte[]{0, 1},
                    value -> {
                        if (value == 0) {
                            executeCraft(p, quantity, false, false);
                        }
                    }));
        }
        p.getService().startYesNo();
    }

    private void executeCraft(Player p, int quantity, boolean chooseBeriOnly, boolean chooseRubyOnly) throws IOException {
        if (p.trade_target != null) {
            p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
            return;
        }
        if (quantity <= 0 || quantity > 500) {
            p.getService().send_box_ThongBao_OK("Số lần không hợp lệ (1-500)!");
            return;
        }

        int successCount = 0;
        String itemName = resultCat == 7 ? ItemTemplate7.get_it_by_id(resultId).name : ItemTemplate4.get_item_name(resultId);

        synchronized (p.item) {
            if (p.item.able_bag() < 1) {
                p.getService().send_box_ThongBao_OK("Hành trang của bạn không đủ chỗ trống!");
                return;
            }

            // Kiểm tra nguyên liệu
            for (Ingredient ing : ingredients) {
                int needed = ing.quantity * quantity;
                int current = p.item.total_item_bag_by_id(ing.cat, ing.id);
                if (current < needed) {
                    String ingName = ing.cat == 7 ? ItemTemplate7.get_it_by_id(ing.id).name : ItemTemplate4.get_item_name(ing.id);
                    p.getService().send_box_ThongBao_OK(String.format("Không đủ nguyên liệu: cần %d %s (có %d)", needed, ingName, current));
                    return;
                }
            }

            // Tính chi phí
            long neededBeri = 0;
            int neededRuby = 0;
            long neededExtol = 0;

            if (payChoice) {
                if (chooseBeriOnly) {
                    neededBeri = (long) costBeri * quantity;
                    neededExtol = (long) costExtol * quantity;
                } else if (chooseRubyOnly) {
                    neededRuby = costRuby * quantity;
                }
            } else {
                neededBeri = (long) costBeri * quantity;
                neededRuby = costRuby * quantity;
                neededExtol = (long) costExtol * quantity;
            }

            // Kiểm tra số dư tài khoản
            if (neededBeri > 0 && p.get_vang() < neededBeri) {
                p.getService().send_box_ThongBao_OK("Bạn không có đủ Beri!");
                return;
            }
            if (neededRuby > 0 && p.get_ngoc() < neededRuby) {
                p.getService().send_box_ThongBao_OK("Bạn không có đủ Ruby!");
                return;
            }
            if (neededExtol > 0 && p.get_vnd() < neededExtol) {
                p.getService().send_box_ThongBao_OK("Bạn không có đủ Extol!");
                return;
            }

            // Trừ tiền
            if (neededBeri > 0) p.update_vang(-neededBeri);
            if (neededRuby > 0) p.update_ngoc(-neededRuby);
            if (neededExtol > 0) p.updateVnd(-neededExtol);

            // Trừ nguyên liệu
            for (Ingredient ing : ingredients) {
                p.item.remove_item47(ing.cat, ing.id, ing.quantity * quantity);
            }

            // Kiểm tra tỷ lệ thành công cho từng lượt chế tạo
            for (int i = 0; i < quantity; i++) {
                if (successRate >= 100 || successRate > ZUtil.random(100)) {
                    successCount++;
                }
            }

            if (successCount > 0) {
                p.item.add_item_bag47(resultCat, resultId, successCount);
            }

            p.updateMoney();
            p.item.updateInventory(false);
        }

        String msgResult = "";
        if (quantity == 1) {
            msgResult = successCount > 0 ? String.format("Chúc mừng bạn chế tạo thành công %s!", itemName)
                    : "Rất tiếc, bạn đã chế tạo thất bại!";
        } else {
            msgResult = String.format("KẾT QUẢ CHẾ TẠO %s:\n- Số lần chế tạo: %d lần\n- Thành công: %d cái\n- Thất bại: %d lần", itemName, quantity, successCount, quantity - successCount);
        }
        p.getService().send_box_ThongBao_OK(msgResult);

        afterCraft(p, quantity, successCount > 0);
    }
}
