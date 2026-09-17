package itemz;


import model.MyPet;
import model.Pet;
import model.Player;
import network.Message;
import java.io.IOException;
import java.util.List;
import template.DataTemplate;
import template.ItemFashion;
import template.ItemFashionP2;
import template.ItemTemplate3;
import template.ItemTemplate4;
import template.ItemTemplate7;
import template.Item_wear;
import template.Option;
import java.util.ArrayList;

public class MainItem {
    public short id;
    public short idIcon;
    public String name;
    public String info;
    public byte cat;
    public int num;
    public int price;
    
    public int limitNumBuy;
    public int indexShop;
    
    public long time;
    public int limitDay;
    
    public List<Option> option1;
    public List<Option> option2;
    
    public MainItem(int id, int cat, int num) {
        this.id = (short) id;
        this.cat = (byte) cat;
        this.num = num;
        this.name = "";
        this.info = "";
        setup();
    }
    
    public MainItem(int id, int cat, int num, int price, int limit, int index) {
        this.id = (short) id;
        this.cat = (byte) cat;
        this.num = num;
        this.price = price;
        this.limitNumBuy = limit;
        this.indexShop = index;
        this.name = "";
        this.info = "";
        setup();
    }

    public MainItem setName(String name) {
        this.name = name;
        return this;
    }

    public MainItem setIcon(short icon) {
        this.idIcon = icon;
        return this;
    }

    public MainItem setIcon(int icon) {
        this.idIcon = (short) icon;
        return this;
    }

    public MainItem setInfo(String info) {
        this.info = info;
        return this;
    }

    public MainItem setNum(int num) {
        this.num = num;
        return this;
    }

    public MainItem setPrice(int price) {
        this.price = price;
        return this;
    }

    public MainItem setLimit(int limit) {
        this.limitNumBuy = limit;
        return this;
    }

    public MainItem setIndexShop(int index) {
        this.indexShop = index;
        return this;
    }

    public MainItem clearOptions() {
        if (this.option1 != null) {
            this.option1.clear();
        } else {
            this.option1 = new ArrayList<>();
        }
        if (this.option2 != null) {
            this.option2.clear();
        }
        return this;
    }

    public MainItem addOption(int opId, int param) {
        if (this.option1 == null) {
            this.option1 = new ArrayList<>();
        }
        this.option1.add(new Option(opId, param));
        return this;
    }

    public MainItem addOption(Option op) {
        if (op != null) {
            if (this.option1 == null) {
                this.option1 = new ArrayList<>();
            }
            this.option1.add(new Option(op.id, op.getParam()));
        }
        return this;
    }

    public MainItem addOption2(int opId, int param) {
        if (this.option2 == null) {
            this.option2 = new ArrayList<>();
        }
        this.option2.add(new Option(opId, param));
        return this;
    }

    public MainItem setExpireDay(int day) {
        this.limitDay = day;
        this.time = (long) day * 86400000L;
        return this;
    }

    public MainItem setTime(long millis) {
        this.time = millis;
        return this;
    }

    public MainItem setPermanent() {
        this.time = -1;
        this.limitDay = 0;
        return this;
    }

    public MainItem cloneItem() {
        MainItem clone = new MainItem(this.id, this.cat, this.num, this.price, this.limitNumBuy, this.indexShop);
        clone.name = this.name;
        clone.idIcon = this.idIcon;
        clone.info = this.info;
        clone.time = this.time;
        clone.limitDay = this.limitDay;
        if (this.option1 != null) {
            clone.option1 = new ArrayList<>();
            for (Option op : this.option1) {
                clone.option1.add(new Option(op.id, op.getParam()));
            }
        }
        if (this.option2 != null) {
            clone.option2 = new ArrayList<>();
            for (Option op : this.option2) {
                clone.option2.add(new Option(op.id, op.getParam()));
            }
        }
        return clone;
    }
    
    public void setup() {
        switch (cat) {
            case 3:
                ItemTemplate3 temp3 = ItemTemplate3.get_it_by_id(id);
                if (temp3 != null) {
                    name = temp3.name;
                    idIcon = temp3.icon;
                    if (temp3.option_item != null) {
                        option1 = new ArrayList<>();
                        for (Option o : temp3.option_item) {
                            option1.add(new Option(o.id, o.getParam()));
                        }
                    }
                    if (temp3.option_item_2 != null) {
                        option2 = new ArrayList<>();
                        for (Option o : temp3.option_item_2) {
                            option2.add(new Option(o.id, o.getParam()));
                        }
                    }
                } else {
                    name = "Trang bị " + id;
                    idIcon = 0;
                }
                break;
            case 4:
                ItemTemplate4 temp4 = ItemTemplate4.get_it_by_id(id);
                if (temp4 != null) {
                    name = temp4.name;
                    idIcon = temp4.icon;
                } else {
                    name = "Vật phẩm " + id;
                    idIcon = 0;
                }
                break;
            case 7:
                ItemTemplate7 temp7 = ItemTemplate7.get_it_by_id(id);
                if (temp7 != null) {
                    name = temp7.name;
                    idIcon = temp7.icon;
                }
                break;
            case 105:
                ItemFashion itemFashion = ItemFashion.get_item(id);
                if (itemFashion != null) {
                    name = itemFashion.name;
                    idIcon = itemFashion.idIcon;
                    if (itemFashion.op != null) {
                        option1 = new ArrayList<>();
                        for (Option o : itemFashion.op) {
                            option1.add(new Option(o.id, o.getParam()));
                        }
                    }
                } else {
                    name = "Thời trang " + id;
                    idIcon = 0;
                }
                break;
            case 110:
                Pet pet = Pet.getTemplate(id);
                if (pet != null) {
                    name = pet.name;
                    idIcon = pet.icon;
                    if (pet.op != null) {
                        option1 = new ArrayList<>();
                        for (Option o : pet.op) {
                            option1.add(new Option(o.id, o.getParam()));
                        }
                    }
                } else {
                    name = "Thú cưỡi " + id;
                    idIcon = 0;
                }
                break;
        }
    }
    
    public static List<template.GiftBox> toGiftBoxList(List<MainItem> items) {
        List<template.GiftBox> gifts = new ArrayList<>();
        if (items == null) return gifts;
        for (MainItem mi : items) {
            if (mi == null) continue;
            template.GiftBox gb = new template.GiftBox();
            gb.id = mi.id;
            gb.type = mi.cat;
            gb.name = mi.name != null ? mi.name : "";
            gb.icon = mi.idIcon;
            gb.num = mi.num;
            gb.color = 0;
            gifts.add(gb);
        }
        return gifts;
    }

    public static void showGiftBox(Player p, String name, String info, List<MainItem> listItem, boolean isAdd, boolean isShow) {
        if (p == null || listItem == null || listItem.isEmpty()) return;
        List<template.GiftBox> gifts = new ArrayList<>();
        for (MainItem mi : listItem) {
            if (mi == null || mi.num <= 0) continue;
            template.GiftBox gb = new template.GiftBox();
            gb.id = mi.id;
            gb.type = mi.cat;
            gb.num = mi.num;
            gb.color = 0;
            gb.name = mi.name != null ? mi.name : "";
            gb.icon = mi.idIcon;
            if (mi.option1 != null && !mi.option1.isEmpty()) {
                gb.options.addAll(mi.option1);
            }
            gifts.add(gb);
        }
        if (gifts.isEmpty()) return;
        if (isAdd) {
            core.RewardService.sendGiftOrMail(p, 1, name != null ? name : "Phần thưởng", info != null ? info : "", gifts, isShow);
        } else {
            try {
                network.Service.send_gift(p, 1, name != null ? name : "Phần thưởng", info != null ? info : "", gifts, isShow);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static MainItem createItemTimeLimit(int id, int cat, int num, int time, int limitDay) {
        MainItem mainItem = new MainItem(id, cat, num);
        mainItem.time = time;
        mainItem.limitDay = limitDay;
        return mainItem;
    }
    
}
