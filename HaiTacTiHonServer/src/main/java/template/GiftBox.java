package template;

import model.Player;
import core.ZUtil;

import java.util.ArrayList;
import java.util.List;

public class GiftBox {

    public static void addGift(List<GiftBox> listGift, int type, int id, int num) {
        if (listGift == null || num <= 0) return;
        for (GiftBox existing : listGift) {
            if (existing != null && existing.type == (byte) type && existing.id == (short) id && (existing.options == null || existing.options.isEmpty())) {
                long total = (long) existing.num + num;
                existing.num = (int) Math.min(Integer.MAX_VALUE, total);
                return;
            }
        }
        GiftBox giftBox = new GiftBox();
        giftBox.id = (short) id;
        giftBox.type = (byte) type;
        giftBox.num = num;
        giftBox.color = 0;
        giftBox.name = "Vật phẩm " + id;
        giftBox.icon = 0;

        switch (type) {
            case 99: { // EXP nhân vật
                giftBox.name = "Kinh nghiệm";
                giftBox.icon = 333;
                giftBox.color = 0;
                break;
            }
            case 3: {
                ItemTemplate3 itemTemplate3 = ItemTemplate3.get_it_by_id(id);
                if (itemTemplate3 != null) {
                    giftBox.name = itemTemplate3.name;
                    giftBox.icon = itemTemplate3.icon;
                    giftBox.color = itemTemplate3.color;
                } else {
                    giftBox.name = "Trang bị " + id;
                }
                break;
            }
            case 4: {
                if (id == 0) {
                    giftBox.name = "Beri";
                    giftBox.icon = 0;
                } else if (id == 1) {
                    giftBox.name = "Ruby";
                    giftBox.icon = 1;
                } else if (id == 2 || id == 908) {
                    giftBox.name = "Extol";
                    giftBox.icon = (short) (id == 908 ? 908 : 2);
                } else if (id == 6) {
                    giftBox.name = "Vé";
                    giftBox.icon = 6;
                } else if (id == 333) {
                    giftBox.name = "Kinh nghiệm kỹ năng";
                    giftBox.icon = 333;
                } else if (id == -10) {
                    giftBox.name = "Kinh nghiệm Bang";
                    giftBox.icon = 0;
                } else if (id == -11) {
                    giftBox.name = "Beri Bang";
                    giftBox.icon = 0;
                } else if (id == -12) {
                    giftBox.name = "Ruby Bang";
                    giftBox.icon = 1;
                } else {
                    ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(id);
                    if (itemTemplate4 != null) {
                        giftBox.name = itemTemplate4.name;
                        giftBox.icon = itemTemplate4.icon;
                    } else {
                        giftBox.name = "Vật phẩm " + id;
                    }
                }
                break;
            }
            case 7: {
                ItemTemplate7 itemTemplate7 = ItemTemplate7.get_it_by_id(id);
                if (itemTemplate7 != null) {
                    giftBox.name = itemTemplate7.name;
                    giftBox.icon = itemTemplate7.icon;
                } else {
                    giftBox.name = "Nguyên liệu " + id;
                }
                break;
            }
            case 105: {
                ItemFashion itemFashion = ItemFashion.get_item(id);
                if (itemFashion != null) {
                    giftBox.name = itemFashion.name;
                    giftBox.icon = itemFashion.idIcon;
                } else {
                    ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(id);
                    if (itemTemplate4 != null) {
                        giftBox.name = itemTemplate4.name;
                        giftBox.icon = itemTemplate4.icon;
                    } else {
                        giftBox.name = "Thời trang " + id;
                    }
                }
                break;
            }
            case 110: {
                model.Pet pet = model.Pet.getTemplate(id);
                if (pet != null) {
                    giftBox.name = pet.name;
                    giftBox.icon = pet.icon;
                } else {
                    ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(id);
                    if (itemTemplate4 != null) {
                        giftBox.name = itemTemplate4.name;
                        giftBox.icon = itemTemplate4.icon;
                    } else {
                        giftBox.name = "Thú cưng " + id;
                    }
                }
                break;
            }
            default: {
                ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(id);
                if (itemTemplate4 != null) {
                    giftBox.name = itemTemplate4.name;
                    giftBox.icon = itemTemplate4.icon;
                }
                break;
            }
        }
        listGift.add(giftBox);
    }
    public byte type;
    public String name;
    public short icon;
    public int num;
    public byte color;
    public short id;
    public List<Option> options = new ArrayList<>();
    public Item_wear item;
    public int quantily;
    
    public GiftBox() {
        
    }
    
    public GiftBox(int type, int id, int num) {
        this.id = (short) id;
        this.type = (byte) type;
        this.num = num;
        this.color = 0;
        this.name = "Vật phẩm " + id;
        this.icon = 0;

        switch (type) {
            case 99: { // EXP nhân vật
                this.name = "Kinh nghiệm";
                this.icon = 333;
                this.color = 0;
                break;
            }
            case 3: {
                ItemTemplate3 itemTemplate3 = ItemTemplate3.get_it_by_id(id);
                if (itemTemplate3 != null) {
                    this.name = itemTemplate3.name;
                    this.icon = itemTemplate3.icon;
                    this.color = itemTemplate3.color;
                }
                break;
            }
            case 4: {
                if (id == 0) {
                    this.name = "Beri";
                    this.icon = 0;
                } else if (id == 1) {
                    this.name = "Ruby";
                    this.icon = 1;
                } else if (id == 2 || id == 908) {
                    this.name = "Extol";
                    this.icon = (short) (id == 908 ? 908 : 2);
                } else if (id == 6) {
                    this.name = "Vé";
                    this.icon = 6;
                } else if (id == 333) {
                    this.name = "Kinh nghiệm kỹ năng";
                    this.icon = 333;
                } else if (id == -10) {
                    this.name = "Kinh nghiệm Bang";
                    this.icon = 0;
                } else if (id == -11) {
                    this.name = "Beri Bang";
                    this.icon = 0;
                } else if (id == -12) {
                    this.name = "Ruby Bang";
                    this.icon = 1;
                } else {
                    ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(id);
                    if (itemTemplate4 != null) {
                        this.name = itemTemplate4.name;
                        this.icon = itemTemplate4.icon;
                    }
                }
                break;
            }
            case 7: {
                ItemTemplate7 itemTemplate7 = ItemTemplate7.get_it_by_id(id);
                if (itemTemplate7 != null) {
                    this.name = itemTemplate7.name;
                    this.icon = itemTemplate7.icon;
                }
                break;
            }
            case 105: {
                ItemFashion itemFashion = ItemFashion.get_item(id);
                if (itemFashion != null) {
                    this.name = itemFashion.name;
                    this.icon = itemFashion.idIcon;
                }
                break;
            }
            case 110: {
                model.Pet pet = model.Pet.getTemplate(id);
                if (pet != null) {
                    this.name = pet.name;
                    this.icon = pet.icon;
                }
                break;
            }
            default: {
                ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(id);
                if (itemTemplate4 != null) {
                    this.name = itemTemplate4.name;
                    this.icon = itemTemplate4.icon;
                }
                break;
            }
        }
    }
    
    public GiftBox(ItemTemplate7 it7, int num) {
        this.id = (short) it7.id;
        this.type = (byte) 7;
        this.name = it7.name;
        this.icon = it7.icon;
        this.num = num;
    }
    
    public GiftBox(ItemTemplate4 it4, int num) {
        this.id = (short) it4.id;
        this.type = (byte) 4;
        this.name = it4.name;
        this.icon = it4.icon;
        this.num = num;
    }

    public GiftBox(ItemTemplate3 it3, int num) {
        this.id = (short) it3.id;
        this.type = (byte) 3;
        this.name = it3.name;
        this.icon = it3.icon;
        this.num = num;
        this.color = it3.color;
    }
    
    public GiftBox(ItemTemplate3 it3, int num, List<Option> options) {
        this.id = (short) it3.id;
        this.type = (byte) 3;
        this.name = it3.name;
        this.icon = it3.icon;
        this.num = num;
        this.color = it3.color;
        if (options != null) {
            this.options.addAll(options);
        }
    }

    public static List<GiftBox> consolidateGifts(List<GiftBox> list) {
        if (list == null || list.isEmpty()) return new ArrayList<>();
        List<GiftBox> result = new ArrayList<>();
        for (GiftBox gb : list) {
            if (gb == null || gb.num <= 0) continue;
            boolean merged = false;
            for (GiftBox existing : result) {
                if (existing != null && existing.type == gb.type && existing.id == gb.id && (existing.options == null || existing.options.isEmpty()) && (gb.options == null || gb.options.isEmpty())) {
                    long total = (long) existing.num + gb.num;
                    existing.num = (int) Math.min(Integer.MAX_VALUE, total);
                    merged = true;
                    break;
                }
            }
            if (!merged) {
                result.add(gb);
            }
        }
        return result;
    }
}
