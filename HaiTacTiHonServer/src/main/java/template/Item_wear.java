package template;

import java.util.ArrayList;
import java.util.List;

import core.ZUtil;

public class Item_wear {
    public ItemTemplate3 template;
    public byte levelUp;
    public byte color;
    public byte typelock;
    public byte numHoleDaDuc;
    public int timeUse;
    public short valueChetac;
    public byte isHoanMy;
    public byte valueKichAn = -1;
    public List<Option> option_item = new ArrayList<>();
    public List<Option> option_item_2 = new ArrayList<>();
    public byte numLoKham;
    public short[] mdakham = new short[0];
    public short index;

    public byte getColor() {
        if (this.isThanTrang()) return 8;
        if (this.color > 0) return this.color;
        if (this.template != null) return this.template.color;
        return 0;
    }

    public void setColor(byte color) {
        this.color = color;
    }

    public boolean isThanTrang() {
        if (this.template == null) return false;
        return this.template.isThanTrang();
    }

    public static boolean isThanTrang(Item_wear it) {
        return it != null && it.isThanTrang();
    }

    public boolean canHaveKichAn() {
        if (this.template == null) return false;
        if (this.isThanTrang()) return false;
        if (this.template.typeEquip > 7) return false;
        if (this.getColor() < 2 && this.template.typeEquip < 6) return false;
        return true;
    }

    public byte getSanitizedValueKichAn() {
        if (!canHaveKichAn()) {
            return -1;
        }
        if (this.valueKichAn < 0 || this.valueKichAn > 12) {
            return -1;
        }
        return this.valueKichAn;
    }

    public Item_wear() {
    }

    public Item_wear(ItemTemplate3 it_temp) {
        setup_template_by_id(it_temp);
    }

    public Item_wear(ItemTemplate3 it_temp, short id) {
        if (it_temp != null) {
            setup_template_by_id(it_temp);
        } else {
            setup_template_by_id(id);
        }
    }

    public void setup_template_by_id(ItemTemplate3 it_temp) {
        if (it_temp != null) {
            this.template = it_temp;
            this.levelUp = 0;
            this.color = it_temp.color;
            this.typelock = it_temp.typelock;
            this.numHoleDaDuc = it_temp.numHoleDaDuc;
            this.timeUse = 0;
            this.valueChetac = (short) ZUtil.random(50, 100);
            this.isHoanMy = it_temp.isHoanMy;
            this.valueKichAn = -1;
            this.option_item = new ArrayList<>();
            for (int i = 0; i < it_temp.option_item.size(); i++) {
                int param_random = it_temp.option_item.get(i).getParam();
                if (param_random > 5 && param_random <= 10) {
                    param_random -= ZUtil.random(5);
                    if (param_random < 5) {
                        param_random = 5;
                    }
                } else if (param_random > 10) {
                    param_random = (param_random * ZUtil.random(90, 101)) / 100;
                }
                this.option_item.add(new Option(it_temp.option_item.get(i).id, param_random));
            }
            this.option_item_2 = new ArrayList<>();
            if (this.isThanTrang()) {
                this.color = 8;
                this.numLoKham = 0;
                this.numHoleDaDuc = 0;
                this.mdakham = new short[0];
                this.isHoanMy = 0;
                this.valueKichAn = -1;
                this.valueChetac = 0;
            } else {
                this.numLoKham = it_temp.numLoKham;
                this.mdakham = new short[it_temp.mdakham.length];
                for (int i = 0; i < it_temp.mdakham.length; i++) {
                    this.mdakham[i] = it_temp.mdakham[i];
                }
            }
            this.index = 0;
            initOptions();
        }
    }

    public void setup_template_by_id(int id) {
        ItemTemplate3 it_temp = ItemTemplate3.get_it_by_id(id);
        if (it_temp != null) {
            this.template = it_temp;
            this.levelUp = 0;
            this.color = it_temp.color;
            this.typelock = it_temp.typelock;
            this.numHoleDaDuc = it_temp.numHoleDaDuc;
            this.timeUse = 0;
            this.valueChetac = (short) ZUtil.random(50, 100);
            this.isHoanMy = it_temp.isHoanMy;
            this.valueKichAn = -1;
            this.option_item = new ArrayList<>();
            for (int i = 0; i < it_temp.option_item.size(); i++) {
                int param_random = it_temp.option_item.get(i).getParam();
                if (param_random > 5 && param_random <= 10) {
                    param_random -= ZUtil.random(5);
                    if (param_random < 5) {
                        param_random = 5;
                    }
                } else if (param_random > 10) {
                    param_random = (param_random * ZUtil.random(90, 101)) / 100;
                }
                this.option_item.add(new Option(it_temp.option_item.get(i).id, param_random));
            }
            this.option_item_2 = new ArrayList<>();
            if (this.isThanTrang()) {
                this.color = 8;
                this.numLoKham = 0;
                this.numHoleDaDuc = 0;
                this.mdakham = new short[0];
                this.isHoanMy = 0;
                this.valueKichAn = -1;
                this.valueChetac = 0;
            } else {
                this.numLoKham = it_temp.numLoKham;
                this.mdakham = new short[it_temp.mdakham.length];
                for (int i = 0; i < it_temp.mdakham.length; i++) {
                    this.mdakham[i] = it_temp.mdakham[i];
                }
            }
            this.index = 0;
            initOptions();
        }
    }
    
    public void setupItemMCS(int id) {
        ItemTemplate3 it_temp = ItemTemplate3.get_it_by_id(id);
        if (it_temp != null) {
            this.template = it_temp;
            this.levelUp = 0;
            this.color = it_temp.color;
            this.typelock = it_temp.typelock;
            this.numHoleDaDuc = it_temp.numHoleDaDuc;
            this.timeUse = 0;
            this.valueChetac = (short) ZUtil.random(50, 100);
            this.isHoanMy = it_temp.isHoanMy;
            this.valueKichAn = -1;
            this.option_item = new ArrayList<>();
            for (int i = 0; i < it_temp.option_item.size(); i++) {
                int param_random = it_temp.option_item.get(i).getParam();
                this.option_item.add(new Option(it_temp.option_item.get(i).id, param_random));
            }
            this.option_item_2 = new ArrayList<>();
            if (this.isThanTrang()) {
                this.color = 8;
                this.numLoKham = 0;
                this.numHoleDaDuc = 0;
                this.mdakham = new short[0];
                this.isHoanMy = 0;
                this.valueKichAn = -1;
                this.valueChetac = 0;
            } else {
                this.numLoKham = it_temp.numLoKham;
                this.mdakham = new short[it_temp.mdakham.length];
                for (int i = 0; i < it_temp.mdakham.length; i++) {
                    this.mdakham[i] = it_temp.mdakham[i];
                }
            }
            this.index = 0;
            initOptions();
        }
    }

    public void clone_obj(Item_wear it_temp) {
        if (it_temp != null) {
            this.template = it_temp.template;
            this.levelUp = it_temp.levelUp;
            this.color = it_temp.getColor();
            this.typelock = it_temp.typelock;
            this.numHoleDaDuc = it_temp.numHoleDaDuc;
            this.timeUse = it_temp.timeUse;
            this.valueChetac = it_temp.valueChetac;
            this.isHoanMy = it_temp.isHoanMy;
            this.valueKichAn = it_temp.getSanitizedValueKichAn();
            this.option_item = new ArrayList<>();
            for (int i = 0; i < it_temp.option_item.size(); i++) {
                this.option_item.add(new Option(it_temp.option_item.get(i).id, it_temp.option_item.get(i).getParam()));
            }
            this.option_item_2 = new ArrayList<>();
            for (int i = 0; i < it_temp.option_item_2.size(); i++) {
                this.option_item_2
                        .add(new Option(it_temp.option_item_2.get(i).id,
                                it_temp.option_item_2.get(i).getParam()));
            }
            this.numLoKham = it_temp.numLoKham;
            this.mdakham = new short[it_temp.mdakham.length];
            for (int i = 0; i < it_temp.mdakham.length; i++) {
                this.mdakham[i] = it_temp.mdakham[i];
            }
            this.index = it_temp.index;
        }
    }

    public void clone_obj(ItemMarket it) {
        if (it == null) return;
        this.template = it.template;
        this.levelUp = it.levelUp;
        this.color = it.getColor();
        this.typelock = it.typelock;
        this.numHoleDaDuc = it.numHoleDaDuc;
        this.timeUse = it.timeUse;
        this.valueChetac = it.valueChetac;
        this.isHoanMy = it.isHoanMy;
        this.valueKichAn = it.getSanitizedValueKichAn();
        this.option_item = new ArrayList<>();
        for (int i = 0; i < it.option_item.size(); i++) {
            this.option_item.add(new Option(it.option_item.get(i).id, it.option_item.get(i).getParam()));
        }
        this.option_item_2 = new ArrayList<>();
        for (int i = 0; i < it.option_item_2.size(); i++) {
            this.option_item_2.add(new Option(it.option_item_2.get(i).id,
                    it.option_item_2.get(i).getParam()));
        }
        this.numLoKham = it.numLoKham;
        this.mdakham = new short[it.mdakham.length];
        for (int i = 0; i < it.mdakham.length; i++) {
            this.mdakham[i] = it.mdakham[i];
        }
        this.index = 0;
    }
    
    public void initOptions() {
        if (this.template == null) return;
        if (this.option_item == null) {
            this.option_item = new ArrayList<>();
        }
        if (this.option_item_2 == null) {
            this.option_item_2 = new ArrayList<>();
        }
        if (this.option_item.isEmpty()) {
            if (this.isThanTrang()) {
                List<Option> defaultOps = ThanTrangConfig.getThanTrangOptions(this.template.id);
                if (!defaultOps.isEmpty()) {
                    for (Option op : defaultOps) {
                        this.option_item.add(new Option(op.id, op.getParam()));
                    }
                }
                return;
            }

            int levelFactor = Math.max(1, (int) this.template.level);
            double colorMultiplier = 1.0;
            switch (this.getColor()) {
                case 1: colorMultiplier = 1.2; break;
                case 2: colorMultiplier = 1.4; break;
                case 3: colorMultiplier = 1.6; break;
                case 4: colorMultiplier = 1.8; break;
                case 5:
                case 7:
                case 8: colorMultiplier = 2.0; break;
            }
            switch (this.template.typeEquip) {
                case 0: // Weapon
                    if (this.template.clazz == 1 || this.template.clazz == 3 || this.template.clazz == 5) {
                        this.option_item.add(new Option(1, (int) (levelFactor * 10 * colorMultiplier)));
                    } else {
                        this.option_item.add(new Option(0, (int) (levelFactor * 10 * colorMultiplier)));
                    }
                    break;
                case 1: // Helmet
                    this.option_item.add(new Option(2, (int) (levelFactor * 3 * colorMultiplier)));
                    this.option_item.add(new Option(4, (int) (levelFactor * 15 * colorMultiplier)));
                    break;
                case 2: // Armor
                    this.option_item.add(new Option(2, (int) (levelFactor * 5 * colorMultiplier)));
                    this.option_item.add(new Option(3, (int) (levelFactor * 5 * colorMultiplier)));
                    this.option_item.add(new Option(4, (int) (levelFactor * 25 * colorMultiplier)));
                    break;
                case 3: // Ring
                    this.option_item.add(new Option(4, (int) (levelFactor * 10 * colorMultiplier)));
                    break;
                case 4: // Necklace
                    this.option_item.add(new Option(4, (int) (levelFactor * 10 * colorMultiplier)));
                    break;
                case 5: // Shoes
                    this.option_item.add(new Option(4, (int) (levelFactor * 12 * colorMultiplier)));
                    break;
                case 7: // Dial
                    List<Option> defaultOps = ItemTemplate3.getDefaultDialOptions(this.template.id);
                    if (defaultOps != null && !defaultOps.isEmpty()) {
                        for (Option op : defaultOps) {
                            this.option_item.add(new Option(op.id, op.getParam()));
                        }
                    } else {
                        this.option_item.add(new Option(1, (int) (200 * colorMultiplier)));
                    }
                    break;
            }
        }
        if (this.template != null && this.template.typeEquip == 7) {
            itemz.UpgradeDial.sanitizeDialOptions(this);
        }
    }
    
    public void initOptionDakham() {
        //khởi tạo chỉ số cho đá khảm khi gắn vào item3 sẽ dùng nó 
    }

    public static Item_wear createDummyEmptyItem(int index) {
        Item_wear it = new Item_wear();
        it.index = (short) index;
        it.template = new ItemTemplate3();
        it.template.id = -1;
        it.template.name = "";
        it.template.clazz = 0;
        it.template.typeEquip = (byte) index;
        it.template.icon = -1;
        it.template.level = 0;
        it.template.color = 0;
        it.template.part = -1;
        it.levelUp = 0;
        it.color = 0;
        it.typelock = 0;
        it.numHoleDaDuc = 0;
        it.timeUse = 0;
        it.valueChetac = 0;
        it.isHoanMy = 0;
        it.valueKichAn = -1;
        it.option_item = new java.util.ArrayList<>();
        it.option_item_2 = new java.util.ArrayList<>();
        it.numLoKham = 0;
        it.mdakham = new short[0];
        return it;
    }
}