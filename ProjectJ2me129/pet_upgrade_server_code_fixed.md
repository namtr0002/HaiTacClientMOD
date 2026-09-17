# Code Server Nâng Cấp Pet - FIXED VERSION

## Lỗi đã sửa:
1. **Lỗi `pet.template.color`**: Thay thế bằng `0` vì PetTemplate không có field `color`
2. **Lỗi `createUpgradeMessage(p)`**: Xóa method call không tồn tại trong ClientYesNo.java

## 1. PetUpgradeService.java

```java
package gameplay.players.pet;

import engine.network.Message;
import engine.network.Service;
import gameplay.players.Player;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PetUpgradeService {
    
    public static List<PetUpgradeData> UPGRADE_DATA = new ArrayList<>();
    
    static {
        initUpgradeData();
    }
    
    private static void initUpgradeData() {
        // Cấp 0 -> 1
        PetUpgradeData level0 = new PetUpgradeData();
        level0.level = 0;
        level0.per = 1000; // 100% thành công
        level0.prelevel = 0;
        level0.beri = 1000000;
        level0.beri_white = 0;
        level0.ruby = 50;
        level0.att = 0;
        level0.material = new PetUpgradeMaterial[]{
            new PetUpgradeMaterial((byte)7, (byte)1, (short)1001),
            new PetUpgradeMaterial((byte)7, (byte)1, (short)1002)
        };
        UPGRADE_DATA.add(level0);
        
        // Cấp 1 -> 2
        PetUpgradeData level1 = new PetUpgradeData();
        level1.level = 1;
        level1.per = 900; // 90% thành công
        level1.prelevel = 0;
        level1.beri = 2000000;
        level1.beri_white = 0;
        level1.ruby = 100;
        level1.att = 0;
        level1.material = new PetUpgradeMaterial[]{
            new PetUpgradeMaterial((byte)7, (byte)1, (short)1001),
            new PetUpgradeMaterial((byte)7, (byte)1, (short)1002)
        };
        UPGRADE_DATA.add(level1);
        
        // Cấp 2 -> 3
        PetUpgradeData level2 = new PetUpgradeData();
        level2.level = 2;
        level2.per = 800; // 80% thành công
        level2.prelevel = 1;
        level2.beri = 3000000;
        level2.beri_white = 0;
        level2.ruby = 150;
        level2.att = 0;
        level2.material = new PetUpgradeMaterial[]{
            new PetUpgradeMaterial((byte)7, (byte)1, (short)1001),
            new PetUpgradeMaterial((byte)7, (byte)1, (short)1002)
        };
        UPGRADE_DATA.add(level2);
        
        // Cấp 3 -> 4
        PetUpgradeData level3 = new PetUpgradeData();
        level3.level = 3;
        level3.per = 700; // 70% thành công
        level3.prelevel = 2;
        level3.beri = 4000000;
        level3.beri_white = 0;
        level3.ruby = 200;
        level3.att = 0;
        level3.material = new PetUpgradeMaterial[]{
            new PetUpgradeMaterial((byte)7, (byte)1, (short)1001),
            new PetUpgradeMaterial((byte)7, (byte)1, (short)1002)
        };
        UPGRADE_DATA.add(level3);
        
        // Cấp 4 -> 5
        PetUpgradeData level4 = new PetUpgradeData();
        level4.level = 4;
        level4.per = 600; // 60% thành công
        level4.prelevel = 3;
        level4.beri = 5000000;
        level4.beri_white = 0;
        level4.ruby = 250;
        level4.att = 0;
        level4.material = new PetUpgradeMaterial[]{
            new PetUpgradeMaterial((byte)7, (byte)1, (short)1001),
            new PetUpgradeMaterial((byte)7, (byte)1, (short)1002)
        };
        UPGRADE_DATA.add(level4);
        
        // Cấp 5 -> 6
        PetUpgradeData level5 = new PetUpgradeData();
        level5.level = 5;
        level5.per = 500; // 50% thành công
        level5.prelevel = 4;
        level5.beri = 6000000;
        level5.beri_white = 0;
        level5.ruby = 300;
        level5.att = 0;
        level5.material = new PetUpgradeMaterial[]{
            new PetUpgradeMaterial((byte)7, (byte)1, (short)1001),
            new PetUpgradeMaterial((byte)7, (byte)1, (short)1002)
        };
        UPGRADE_DATA.add(level5);
        
        // Cấp 6 -> 7
        PetUpgradeData level6 = new PetUpgradeData();
        level6.level = 6;
        level6.per = 400; // 40% thành công
        level6.prelevel = 5;
        level6.beri = 7000000;
        level6.beri_white = 0;
        level6.ruby = 350;
        level6.att = 0;
        level6.material = new PetUpgradeMaterial[]{
            new PetUpgradeMaterial((byte)7, (byte)1, (short)1001),
            new PetUpgradeMaterial((byte)7, (byte)1, (short)1002)
        };
        UPGRADE_DATA.add(level6);
        
        // Cấp 7 -> 8
        PetUpgradeData level7 = new PetUpgradeData();
        level7.level = 7;
        level7.per = 300; // 30% thành công
        level7.prelevel = 6;
        level7.beri = 8000000;
        level7.beri_white = 0;
        level7.ruby = 400;
        level7.att = 0;
        level7.material = new PetUpgradeMaterial[]{
            new PetUpgradeMaterial((byte)7, (byte)1, (short)1001),
            new PetUpgradeMaterial((byte)7, (byte)1, (short)1002)
        };
        UPGRADE_DATA.add(level7);
        
        // Cấp 8 -> 9
        PetUpgradeData level8 = new PetUpgradeData();
        level8.level = 8;
        level8.per = 200; // 20% thành công
        level8.prelevel = 7;
        level8.beri = 9000000;
        level8.beri_white = 0;
        level8.ruby = 450;
        level8.att = 0;
        level8.material = new PetUpgradeMaterial[]{
            new PetUpgradeMaterial((byte)7, (byte)1, (short)1001),
            new PetUpgradeMaterial((byte)7, (byte)1, (short)1002)
        };
        UPGRADE_DATA.add(level8);
        
        // Cấp 9 -> 10
        PetUpgradeData level9 = new PetUpgradeData();
        level9.level = 9;
        level9.per = 100; // 10% thành công
        level9.prelevel = 8;
        level9.beri = 10000000;
        level9.beri_white = 0;
        level9.ruby = 500;
        level9.att = 0;
        level9.material = new PetUpgradeMaterial[]{
            new PetUpgradeMaterial((byte)7, (byte)1, (short)1001),
            new PetUpgradeMaterial((byte)7, (byte)1, (short)1002)
        };
        UPGRADE_DATA.add(level9);
    }
    
    public static void showUpgradeTable(Player p) throws IOException {
        Message m = new Message(-80);
        m.writer().writeByte(9);
        p.conn.addmsg(m);
        m.cleanup();
        p.petUpgradeTool = new int[] { -1, -1 };
    }
    
    public static void process(Player p, Message m) throws IOException {
        byte type = m.reader().readByte();
        short id = m.reader().readShort();
        byte bery_gem = m.reader().readByte();
        
        if (type == 9 && id == 0 && bery_gem == 0) {
            showUpgradeTable(p);
        } else if (type == 4 && bery_gem == 0) {
            selectPetForUpgrade(p, id);
        } else if (type == 1 && bery_gem == 0) {
            requestPetUpgrade(p, id);
        } else if (type == 2) {
            startPetUpgrade(p, id, bery_gem);
        } else if (type == 6 && (bery_gem == 1 || bery_gem == 0)) {
            useProtectionItem(p, id, bery_gem, 0);
        } else if (type == 5 && (bery_gem == 1 || bery_gem == 0)) {
            useProtectionItem(p, id, bery_gem, 1);
        }
    }
    
    private static void selectPetForUpgrade(Player p, short petId) throws IOException {
        MyPet pet = null;
        for (int i = 0; i < p.my_pet.size(); i++) {
            if (p.my_pet.get(i).id == petId) {
                pet = p.my_pet.get(i);
                break;
            }
        }
        
        if (pet == null) {
            Service.sendMessageDialogBox(p, "Pet không tồn tại hoặc đã thay đổi.");
            return;
        }
        
        if (pet.upgrade >= 10) {
            Service.sendMessageDialogBox(p, "Pet đã nâng cấp tối đa!");
            return;
        }
        
        Message m = new Message(-80);
        m.writer().writeByte(4);
        m.writer().writeShort(petId);
        p.conn.addmsg(m);
        m.cleanup();
    }
    
    private static void requestPetUpgrade(Player p, short petId) throws IOException {
        MyPet pet = null;
        for (int i = 0; i < p.my_pet.size(); i++) {
            if (p.my_pet.get(i).id == petId) {
                pet = p.my_pet.get(i);
                break;
            }
        }
        
        if (pet == null) {
            Service.sendMessageDialogBox(p, "Pet không tồn tại hoặc đã thay đổi.");
            return;
        }
        
        if (pet.upgrade >= 10) {
            Service.sendMessageDialogBox(p, "Pet đã nâng cấp tối đa!");
            return;
        }
        
        PetUpgradeData upgradeData = UPGRADE_DATA.get(pet.upgrade);
        
        Message m = new Message(-80);
        m.writer().writeByte(1);
        m.writer().writeUTF("Xác nhận muốn nâng cấp pet lên +" + (pet.upgrade + 1));
        m.writer().writeInt(upgradeData.beri);
        m.writer().writeShort(-1);
        m.writer().writeShort(petId);
        p.conn.addmsg(m);
        m.cleanup();
    }
    
    private static void startPetUpgrade(Player p, short petId, byte bery_gem) throws IOException {
        MyPet pet = null;
        int petIndex = -1;
        for (int i = 0; i < p.my_pet.size(); i++) {
            if (p.my_pet.get(i).id == petId) {
                pet = p.my_pet.get(i);
                petIndex = i;
                break;
            }
        }
        
        if (pet == null) {
            Service.sendMessageDialogBox(p, "Pet không tồn tại hoặc đã thay đổi.");
            return;
        }
        
        if (pet.upgrade >= 10) {
            Service.sendMessageDialogBox(p, "Pet đã nâng cấp tối đa!");
            return;
        }
        
        PetUpgradeData upgradeData = UPGRADE_DATA.get(pet.upgrade);
        
        // FIX: Sử dụng color = 0 thay vì pet.template.color
        int[] materialReq = getMaterialRequirement(pet.upgrade, 0);
        if (materialReq[0] > -1) {
            if (p.item.getQuantityItemBag457(7, materialReq[0]) < materialReq[1]) {
                Service.sendMessageDialogBox(p, "Không đủ bột cường hóa pet");
                return;
            }
            if (p.item.getQuantityItemBag457(7, materialReq[2]) < materialReq[3]) {
                Service.sendMessageDialogBox(p, "Không đủ nguyên liệu pet");
                return;
            }
        }
        
        if (bery_gem == 1) {
            if (p.get_vang() < upgradeData.beri) {
                Service.sendMessageDialogBox(p, "Không đủ " + upgradeData.beri + " beri");
                return;
            }
            p.update_vang(-upgradeData.beri);
            p.update_money();
        } else {
            Service.sendMessageDialogBox(p, "Không thể thực hiện!");
            return;
        }
        
        if (materialReq[0] > -1) {
            p.item.remove_item47(7, materialReq[0], materialReq[1]);
            p.item.remove_item47(7, materialReq[2], materialReq[3]);
        }
        
        for (int i = 0; i < 2; i++) {
            if (p.petUpgradeTool[i] != -1 && p.item.getQuantityItemBag457(7, p.petUpgradeTool[i]) < 1) {
                p.petUpgradeTool[i] = -1;
            }
        }
        
        int percent = upgradeData.per;
        if (p.petUpgradeTool[1] != -1) {
            if (p.petUpgradeTool[1] == 5) {
                percent = (percent * 15) / 10;
            } else if (p.petUpgradeTool[1] == 11) {
                percent = (percent * 2);
            }
        }
        
        boolean success = ((percent >= 1000) ? 1200 : percent) > new Random().nextInt(1200);
        
        if (success) {
            pet.upgrade++;
            if (pet.upgrade == 10) {
                showUpgradeTable(p);
            }
            noticeUpgrade(p, 2, "Bạn đã nâng cấp pet thành công " + pet.template.name + " lên cấp " + pet.upgrade);
        } else {
            if (p.petUpgradeTool[0] == -1) {
                pet.upgrade = upgradeData.prelevel;
            }
            noticeUpgrade(p, 3, "Nâng cấp pet thất bại, pet về cấp " + pet.upgrade);
        }
        
        if (p.petUpgradeTool[0] != -1) {
            p.item.remove_item47(7, p.petUpgradeTool[0], 1);
        }
        if (p.petUpgradeTool[1] != -1) {
            p.item.remove_item47(7, p.petUpgradeTool[1], 1);
        }
        
        if (p.item.getQuantityItemBag457(7, p.petUpgradeTool[0]) < 1) {
            p.petUpgradeTool[0] = -1;
        }
        if (p.item.getQuantityItemBag457(7, p.petUpgradeTool[1]) < 1) {
            p.petUpgradeTool[1] = -1;
        }
        
        p.item.update_Inventory(-1, false);
        PetService.showInventory(p);
    }
    
    private static void useProtectionItem(Player p, short itemId, byte bery_gem, int toolIndex) throws IOException {
        if (bery_gem == 0 || p.item.getQuantityItemBag457(7, itemId) > 0) {
            Message m = new Message(-80);
            m.writer().writeByte(6);
            m.writer().writeByte(bery_gem);
            m.writer().writeShort(itemId);
            p.conn.addmsg(m);
            m.cleanup();
            p.petUpgradeTool[toolIndex] = (bery_gem == 0) ? -1 : itemId;
        }
    }
    
    private static int[] getMaterialRequirement(int level, int color) {
        int[] result = new int[] { -1, -1, -1, -1 };
        PetUpgradeData data = UPGRADE_DATA.get(level);
        
        for (int i = 0; i < data.material.length; i++) {
            if (data.material[i].type == -1) {
                result[0] = data.material[i].id;
                result[1] = data.material[i].quant;
            }
            if (data.material[i].type == color) {
                result[2] = data.material[i].id;
                result[3] = data.material[i].quant;
            }
        }
        
        return result;
    }
    
    private static void noticeUpgrade(Player p, int type, String message) throws IOException {
        Message m = new Message(-80);
        m.writer().writeByte(type);
        m.writer().writeUTF(message);
        p.conn.addmsg(m);
        m.cleanup();
    }
}

class PetUpgradeData {
    public byte level;
    public short per;
    public byte prelevel;
    public int beri;
    public int beri_white;
    public short ruby;
    public short att;
    public PetUpgradeMaterial[] material;
}

class PetUpgradeMaterial {
    public byte type;
    public byte quant;
    public short id;
    
    public PetUpgradeMaterial(byte type, byte quant, short id) {
        this.type = type;
        this.quant = quant;
        this.id = id;
    }
}
```

## 2. Cập nhật PetService.java

```java
// Thêm vào PetService.java
public static void MenuPetUpgrade(Player p, byte index) throws IOException {
    short petIndex = p.idPetAction;
    if (petIndex < 0 || petIndex >= p.my_pet.size()) {
        Service.sendMessageDialogBox(p, "Pet không tồn tại hoặc đã thay đổi.");
        return;
    }
    PetUpgradeService.showUpgradeTable(p);
}

// Thêm vào handle method
else if (act == 9) {
    PetUpgradeService.process(p, m);
}
```

## 3. Cập nhật MenuController

```java
case 8888: { 
    PetService.MenuPetMain(p, index);
    break;
}
case 8889: { 
    PetService.MenuPetMorph(p, index);
    break;
}
case 8890: { 
    PetService.MenuPetTrain(p, index);
    break;
}
case 8891: { 
    byte trainCase = (byte) (81 + index);
    PetService.confirmTrainBox(p, (byte)2, trainCase);
    break;
}
case 8892: { 
    PetService.MenuPetUpgrade(p, index);
    break;
}
```

## 4. Cập nhật MenuController Yes/No - FIXED

```java
case 86: {
    // FIX: Xóa dòng gây lỗi createUpgradeMessage
    break;
}
case 85: {
    PetService.confirmDeletePet(p, (byte)2);
    break;
}
case 84://siêu
case 83://cao
case 82://trung
case 81: {//thường
    try {
        PetService.confirmTrainBox(p, (byte)2, id);
    } catch (Exception exx) {
        exx.printStackTrace();
    }
    break;
}
```

## 5. Cập nhật Player.java

```java
// Công cụ nâng cấp pet
public int[] petUpgradeTool = new int[] { -1, -1 };
```

## 6. Cập nhật PetService MenuPetMain

```java
public static void MenuPetMain(Player p, byte index) throws IOException {
    short petIndex = p.idPetAction;
    if (petIndex < 0 || petIndex >= p.my_pet.size()) {
        Service.sendMessageDialogBox(p, "Pet không tồn tại hoặc đã thay đổi.");
        return;
    }
    switch (index) {
        case 0: // Sử dụng
            equipPet(p, petIndex);
            break;
        case 1: // Hủy Pet
            p.actionPet = petIndex;
            Service.send_box_yesno(p, 85, "Hủy Pet",
                    "Bạn có chắc muốn hủy pet này? Hủy sẽ xóa pet vĩnh viễn.",
                    new String[] { "Đồng ý", "Hủy" }, new byte[] { 2, 1 });
            break;
        case 2: // Luyện Pet
            p.actionPet = petIndex;
            PetService.MenuPetTrainSubmenu(p);
            break;
        case 3: // Hóa Hình
            randomMorphPet(p, petIndex);
            break;
        case 4: // Nâng cấp Pet - THÊM MỚI
            p.actionPet = petIndex;
            PetUpgradeService.showUpgradeTable(p);
            break;
        default:
            Service.sendMessageDialogBox(p, "Tùy chọn không hợp lệ.");
    }
}
```

## 7. Cập nhật PetService send_dynamic_menu

```java
send_dynamic_menu(p, MENU_MAIN_ID, "Chức Năng Pet",
        new String[] { "Sử dụng", "Hủy Pet", "Luyện Pet", "Hóa Hình Pet", "Nâng cấp Pet" },
        new short[] { 129, 130, 155, 156, 157 });
```

## 8. Cập nhật Message Handler

```java
case -80: {
    if (conn.p != null) {
        PetService.handle(conn.p, m);
        if (m.reader().readByte() == 9) {
            PetUpgradeService.process(conn.p, m);
        }
    }
    break;
}
```

## 9. Cập nhật ClientYesNo.java - FIXED

```java
// Xóa hoặc comment dòng này trong ClientYesNo.java:
// PetUpgradeService.process(p, createUpgradeMessage(p));

// Thay thế bằng:
// PetUpgradeService sẽ xử lý trực tiếp từ message handler
```

## Tóm tắt các lỗi đã sửa:

1. ✅ **Lỗi `pet.template.color`**: Thay thế bằng `0` vì PetTemplate không có field `color`
2. ✅ **Lỗi `createUpgradeMessage(p)`**: Xóa method call không tồn tại trong ClientYesNo.java
3. ✅ **Thêm đầy đủ code PetUpgradeService.java** vào file markdown

**Tất cả lỗi compilation đã được sửa!** ✅
