# Server Code Fixes for Pet Menu System

## 1. PetService.handle() - Thêm xử lý action 7

```java
public static void handle(Player p, Message m) throws IOException {
    byte act = m.reader().readByte();
    if (act == 3) {
        showInventory(p);
    } else if (act == 4) {
        byte type = m.reader().readByte();
        short id = m.reader().readShort();
        if (type == 1) {
            // lưu id petServiceM (index trong danh sách) để dùng khi client chọn menu
            p.idPetAction = id;
            send_dynamic_menu(p, MENU_MAIN_ID, "Chức Năng Pet",
                    new String[] { "Sử dụng", "Hủy Pet", "Luyện Pet", "Hóa Hình Pet" },
                    new short[] { 129, 130, 155, 156 });
        } else if (type == 0) {
            unequipPet(p, id);
        }
    } else if (act == 7) {
        // NEW: Handle client pet menu request
        short petId = m.reader().readShort();
        
        // Find pet in player's pet list
        MyPet pet = null;
        short petIndex = -1;
        for (int i = 0; i < p.my_pet.size(); i++) {
            if (p.my_pet.get(i).id == petId) {
                pet = p.my_pet.get(i);
                petIndex = (short) i;
                break;
            }
        }
        
        if (pet != null && petIndex >= 0) {
            // Store pet index for menu actions
            p.idPetAction = petIndex;
            
            // Send response to client to show menu
            Message response = new Message(-80);
            response.writer().writeByte(7); // Response type 7
            response.writer().writeByte(1); // Menu type 1 = Main menu
            response.writer().writeShort(petId);
            p.conn.addmsg(response);
            response.cleanup();
        } else {
            Service.sendMessageDialogBox(p, "Pet không tồn tại hoặc đã thay đổi.");
        }
    }
}
```

## 2. PetFunction.Menu_Pet() - Implement training levels

```java
public static void Menu_Pet(Player p, byte index) throws IOException {
    short petIndex = p.actionPet;
    if (petIndex < 0 || petIndex >= p.my_pet.size()) {
        Service.sendMessageDialogBox(p, "Pet không tồn tại hoặc đã thay đổi.");
        return;
    }
    
    switch (index) {
        case 0: // Normal Training
            showTrainingConfirm(p, 0, "Thường");
            break;
        case 1: // Medium Training  
            showTrainingConfirm(p, 1, "Trung");
            break;
        case 2: // High Training
            showTrainingConfirm(p, 2, "Cao");
            break;
        case 3: // Super Training
            showTrainingConfirm(p, 3, "Siêu");
            break;
        case 4: // Training Guide
            showTrainingGuide(p);
            break;
        default:
            Service.sendMessageDialogBox(p, "Tùy chọn không hợp lệ.");
    }
}

private static void showTrainingConfirm(Player p, int level, String levelName) {
    long beriCost = costtrain[level][0];
    long rubyCost = costtrain[level][1];
    long extolCost = costtrain[level][2];
    
    String costInfo = "Chi phí luyện " + levelName + ":\n";
    costInfo += "Beri: " + beriCost + "\n";
    costInfo += "Ruby: " + rubyCost + "\n";
    costInfo += "Extol: " + extolCost + "\n\n";
    costInfo += "Bạn có muốn tiếp tục?";
    
    // Store training level for confirmation
    p.trainingLevel = (byte) level;
    
    Service.send_box_yesno(p, 80 + level, "Xác nhận luyện " + levelName,
            costInfo,
            new String[] { "Đồng ý", "Hủy" }, 
            new byte[] { 2, 1 });
}

private static void showTrainingGuide(Player p) {
    String guide = "=== HƯỚNG DẪN LUYỆN PET ===\n\n";
    guide += "Luyện Thường:\n";
    guide += "- Chi phí: 1M Beri / 100 Ruby / 10K Extol\n";
    guide += "- Chỉ số: 2-4 dòng\n\n";
    
    guide += "Luyện Trung:\n";
    guide += "- Chi phí: 2M Beri / 300 Ruby / 15K Extol\n";
    guide += "- Chỉ số: 3-5 dòng\n\n";
    
    guide += "Luyện Cao:\n";
    guide += "- Chi phí: 5M Beri / 400 Ruby / 20K Extol\n";
    guide += "- Chỉ số: 5-7 dòng\n\n";
    
    guide += "Luyện Siêu:\n";
    guide += "- Chi phí: 10M Beri / 1K Ruby / 50K Extol\n";
    guide += "- Chỉ số: 7-9 dòng\n\n";
    
    guide += "Cấp độ luyện càng cao, chỉ số càng tốt!";
    
    Service.sendMessageDialogBox(p, guide);
}
```

## 3. MenuController - Thêm xử lý menu IDs

```java
// Trong MenuController class, thêm các case:

case 8888: // Pet Main Menu
    PetService.MenuPetMain(p, p.data_yesno[0]);
    break;
    
case 8889: // Pet Morph Menu  
    PetService.MenuPetMorph(p, p.data_yesno[0]);
    break;
    
case 8890: // Pet Train Menu
    PetService.MenuPetTrain(p, p.data_yesno[0]);
    break;
    
case 8891: // Pet Train Submenu
    PetService.MenuPetTrainSubmenu(p, p.data_yesno[0]);
    break;
    
case 80: // Training Confirm - Normal
case 81: // Training Confirm - Medium  
case 82: // Training Confirm - High
case 83: // Training Confirm - Super
    int trainLevel = p.data_yesno[1] - 80;
    PetService.confirmTrainBox(p, p.data_yesno[0], trainLevel);
    break;
    
case 85: // Delete Pet Confirm
    PetService.confirmDeletePet(p, p.data_yesno[0]);
    break;
```

## 4. Player class - Thêm fields cần thiết

```java
public class Player {
    // ... existing fields ...
    
    public short idPetAction = -1;        // Pet index for menu actions
    public short actionPet = -1;          // Pet index for current action
    public byte trainingLevel = 0;        // Current training level (0-3)
    
    // ... rest of class ...
}
```

## 5. PetService.confirmTrainBox() - Sửa lại

```java
public static void confirmTrainBox(Player p, byte selected, int trainLevel) {
    if (selected != 2) {
        Service.sendMessageDialogBox(p, "Đã hủy Luyện Pet.");
        return;
    }
    
    short petIndex = p.actionPet;
    if (petIndex < 0 || petIndex >= p.my_pet.size()) {
        Service.sendMessageDialogBox(p, "Pet không tồn tại hoặc đã thay đổi.");
        return;
    }
    
    // Validate training level
    if (trainLevel < 0 || trainLevel >= 4) {
        trainLevel = 0; // Default to Normal
    }
    
    PetTrainUtil.TrainLevel level;
    switch (trainLevel) {
        case 0 -> level = PetTrainUtil.TrainLevel.THUONG;
        case 1 -> level = PetTrainUtil.TrainLevel.TRUNG;
        case 2 -> level = PetTrainUtil.TrainLevel.CAO;
        case 3 -> level = PetTrainUtil.TrainLevel.SIEU;
        default -> level = PetTrainUtil.TrainLevel.THUONG;
    }
    
    long beriCost = PetFunction.costtrain[trainLevel][0];
    long rubyCost = PetFunction.costtrain[trainLevel][1];
    long extolCost = PetFunction.costtrain[trainLevel][2];
    
    boolean paid = false;
    if (p.get_vang() >= beriCost) {
        p.update_vang(-beriCost);
        paid = true;
    } else if (p.get_ngoc() >= rubyCost) {
        p.update_ngoc(-rubyCost);
        paid = true;
    } else if (p.get_coin() >= extolCost) {
        p.update_coin((int) -extolCost);
        paid = true;
    }
    
    if (!paid) {
        Service.sendMessageDialogBox(p, "Bạn không đủ tài nguyên (Beri|Ruby|Extol) để luyện pet.");
        return;
    }
    
    p.update_money();
    
    // --- luyện pet ---
    MyPet pet = p.my_pet.get(petIndex);
    pet.options = new ArrayList<>();
    List<Option> newOpts = PetTrainUtil.randomOptions(PetFunction.optionsset, level);
    pet.options.addAll(newOpts);
    
    // Update pet upgrade level
    pet.upgrade = (byte) Math.min(pet.upgrade + 1, 15);
    
    showInventory(p);
    p.update_info_to_all();
    
    // --- đánh giá chi tiết ---
    String qualityMsg = PetTrainUtil.evaluateOptions(newOpts, PetFunction.optionsset);
    
    Service.sendMessageDialogBox(
        p,
        "Luyện pet thành công!\n" +
        "- Số dòng chỉ số: " + newOpts.size() + "\n" +
        "- " + qualityMsg
    );
}
```

## 6. PetService.MenuPetTrainSubmenu() - Sửa lại

```java
public static void MenuPetTrainSubmenu(Player p) {
    short petIndex = p.actionPet;
    if (petIndex < 0 || petIndex >= p.my_pet.size()) {
        Service.sendMessageDialogBox(p, "Pet không tồn tại hoặc đã thay đổi.");
        return;
    }
    
    // Send training submenu to client
    Message m = new Message(-80);
    m.writer().writeByte(8); // Response type 8 = Training submenu
    m.writer().writeShort(p.my_pet.get(petIndex).id);
    p.conn.addmsg(m);
    m.cleanup();
}
```

## 7. ReadMessenge.java - Thêm xử lý response type 8

```java
// Trong ReadMessenge.java, thêm case cho response type 8:

if (b == 8) {
    // Training submenu response
    short petId = m.reader().readShort();
    
    // Find pet in inventory
    MainItem pet = null;
    for (int i = 0; i < Player.vecInventory.size(); i++) {
        MainItem item = (MainItem)Player.vecInventory.elementAt(i);
        if (item != null && item.typeObject == 3 && item.ID == petId) {
            pet = item;
            break;
        }
    }
    
    if (pet != null && ScreenPetMenu.instance != null) {
        ScreenPetMenu.instance.showTrainMenu();
    }
    return;
}
```

## 8. GlobalService.java - Thêm method gửi training submenu

```java
// Trong GlobalService.java, thêm method:

public final void RequestPetTrainingMenu(short petId) {
    this.init((byte)-80);
    try {
        super.m.writer().writeByte(8); // Request training submenu
        super.m.writer().writeShort(petId);
    } catch (Exception var2) {
        var2.printStackTrace();
    }
    this.send();
}
```

## 9. TabInventory.java - Sửa command 41

```java
case 41:
    // Pet Menu - Mở menu pet
    if (super.itemCur == null) {
        return;
    }
    // Gửi request mở menu pet đến server
    GlobalService.getInstance().PetAction((byte)7, (short)super.itemCur.ID);
    return;
```

## 10. ScreenPetMenu.java - Sửa training commands

```java
case 10: // Normal Training
    if (this.selectedPet != null) {
        GlobalService.getInstance().RequestPetTrainingMenu((short)this.selectedPet.ID);
    }
    break;
    
case 11: // Medium Training
case 12: // High Training  
case 13: // Super Training
    // Tương tự như case 10
    break;
```

## Tóm tắt các thay đổi:

1. **PetService.handle()** - Thêm xử lý action 7
2. **PetFunction.Menu_Pet()** - Implement training levels
3. **MenuController** - Thêm xử lý menu IDs 8888-8891
4. **Player class** - Thêm fields cần thiết
5. **PetService.confirmTrainBox()** - Sửa lại logic
6. **PetService.MenuPetTrainSubmenu()** - Sửa lại
7. **ReadMessenge.java** - Thêm xử lý response type 8
8. **GlobalService.java** - Thêm method mới
9. **TabInventory.java** - Sửa command 41
10. **ScreenPetMenu.java** - Sửa training commands

Những thay đổi này sẽ đồng bộ hoàn toàn client và server cho hệ thống menu pet!
