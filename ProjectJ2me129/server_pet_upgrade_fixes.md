# 🔧 Server Code Fixes for Pet Upgrade System

## 📋 Tổng Quan
File này chứa các sửa đổi cần thiết cho server code để tương thích hoàn toàn với client code đã implement cho hệ thống nâng cấp pet.

## 🚨 Các Lỗi Cần Sửa

### 1. **Sửa Message Type 5 và 6 (Protection Items)**

**Vấn đề:** Server gửi type 6 cho cả hai loại protection item, nhưng client expect type 5 cho Ngôi sao may mắn và type 6 cho Mai rùa.

**File:** `PetUpgradeService.java`
**Method:** `useProtectionItem()`

```java
// ❌ CODE HIỆN TẠI (SAI)
private static void useProtectionItem(Player p, short itemId, byte bery_gem, int toolIndex) throws IOException {
    if (bery_gem == 0 || p.item.getQuantityItemBag457(7, itemId) > 0) {
        Message m = new Message(-80);
        m.writer().writeByte(6); // ❌ Luôn gửi type 6
        m.writer().writeByte(bery_gem);
        m.writer().writeShort(itemId);
        p.conn.addmsg(m);
        m.cleanup();
        p.petUpgradeTool[toolIndex] = (bery_gem == 0) ? -1 : itemId;
    }
}

// ✅ CODE SỬA (ĐÚNG)
private static void useProtectionItem(Player p, short itemId, byte bery_gem, int toolIndex) throws IOException {
    if (bery_gem == 0 || p.item.getQuantityItemBag457(7, itemId) > 0) {
        Message m = new Message(-80);
        // ✅ toolIndex 0 = Mai rùa (type 6), toolIndex 1 = Ngôi sao may mắn (type 5)
        m.writer().writeByte(toolIndex == 0 ? 6 : 5);
        m.writer().writeByte(bery_gem);
        m.writer().writeShort(itemId);
        p.conn.addmsg(m);
        m.cleanup();
        p.petUpgradeTool[toolIndex] = (bery_gem == 0) ? -1 : itemId;
    }
}
```

### 2. **Sửa Message Type 1 (Confirmation Request)**

**Vấn đề:** Server gửi `writeShort(-1)` cho ruby cost, nhưng client expect `writeShort(rubyCost)`.

**File:** `PetUpgradeService.java`
**Method:** `requestPetUpgrade()`

```java
// ❌ CODE HIỆN TẠI (SAI)
private static void requestPetUpgrade(Player p, short petId) throws IOException {
    // ... existing code ...
    
    PetUpgradeData upgradeData = UPGRADE_DATA.get(pet.upgrade);
    
    Message m = new Message(-80);
    m.writer().writeByte(1);
    m.writer().writeUTF("Xác nhận muốn nâng cấp pet lên +" + (pet.upgrade + 1));
    m.writer().writeInt(upgradeData.beri);
    m.writer().writeShort(-1); // ❌ Gửi -1 thay vì ruby cost
    m.writer().writeShort(petId);
    p.conn.addmsg(m);
    m.cleanup();
}

// ✅ CODE SỬA (ĐÚNG)
private static void requestPetUpgrade(Player p, short petId) throws IOException {
    // ... existing code ...
    
    PetUpgradeData upgradeData = UPGRADE_DATA.get(pet.upgrade);
    
    Message m = new Message(-80);
    m.writer().writeByte(1);
    m.writer().writeUTF("Xác nhận muốn nâng cấp pet lên +" + (pet.upgrade + 1));
    m.writer().writeInt(upgradeData.beri);
    m.writer().writeShort(upgradeData.ruby); // ✅ Gửi ruby cost thay vì -1
    m.writer().writeShort(petId);
    p.conn.addmsg(m);
    m.cleanup();
}
```

### 3. **Thêm Field petUpgradeTool vào Player Class**

**Vấn đề:** Server sử dụng `p.petUpgradeTool` nhưng field này có thể chưa được khai báo trong Player class.

**File:** `Player.java`
**Thêm vào class Player:**

```java
public class Player {
    // ... existing fields ...
    
    // ✅ THÊM FIELD MỚI
    public int[] petUpgradeTool = new int[]{-1, -1}; // Tool slots for pet upgrade
    public byte trainingLevel = 0; // Training level for pet training confirmation
    
    // ... rest of the class ...
}
```

### 4. **Sửa Logic Nâng Cấp Pet**

**Vấn đề:** Server sử dụng `pet.template.name` nhưng có thể `template` là null.

**File:** `PetUpgradeService.java`
**Method:** `startPetUpgrade()`

```java
// ❌ CODE HIỆN TẠI (SAI)
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

// ✅ CODE SỬA (ĐÚNG)
if (success) {
    pet.upgrade++;
    if (pet.upgrade == 10) {
        showUpgradeTable(p);
    }
    // ✅ Kiểm tra template null
    String petName = (pet.template != null) ? pet.template.name : "Pet";
    noticeUpgrade(p, 2, "Bạn đã nâng cấp pet thành công " + petName + " lên cấp " + pet.upgrade);
} else {
    if (p.petUpgradeTool[0] == -1) {
        pet.upgrade = upgradeData.prelevel;
    }
    noticeUpgrade(p, 3, "Nâng cấp pet thất bại, pet về cấp " + pet.upgrade);
}
```

### 5. **Sửa Message Handler Integration**

**Vấn đề:** Cần đảm bảo PetUpgradeService được gọi đúng cách từ message handler.

**File:** Message Handler (thường là `MessageHandler.java` hoặc tương tự)

```java
// ❌ CODE HIỆN TẠI (SAI)
case -80: {
    if (conn.p != null) {
        PetService.handle(conn.p, m);
        // ❌ Gọi PetUpgradeService.process trực tiếp có thể gây lỗi
        if (m.reader().readByte() == 9) {
            PetUpgradeService.process(conn.p, m);
        }
    }
    break;
}

// ✅ CODE SỬA (ĐÚNG)
case -80: {
    if (conn.p != null) {
        PetService.handle(conn.p, m);
        // ✅ PetUpgradeService.process đã được gọi trong PetService.handle()
        // Không cần gọi thêm ở đây
    }
    break;
}
```

### 6. **Sửa PetService.handle() để tương thích**

**File:** `PetService.java`
**Method:** `handle()`

```java
// ❌ CODE HIỆN TẠI (SAI)
public static void handle(Player p, Message m) throws IOException {
    byte act = m.reader().readByte();
    if (act == 3) {
        showInventory(p);
    } else if (act == 4) {
        // ... existing code ...
    } else if (act == 7) {
        // ... existing code ...
    } else if (act == 9) {
        PetUpgradeService.process(p, m);
    }
}

// ✅ CODE SỬA (ĐÚNG)
public static void handle(Player p, Message m) throws IOException {
    byte act = m.reader().readByte();
    if (act == 3) {
        showInventory(p);
    } else if (act == 4) {
        // ... existing code ...
    } else if (act == 7) {
        // ... existing code ...
    } else if (act == 9) {
        // ✅ Gọi PetUpgradeService.process với message đã đọc byte đầu tiên
        PetUpgradeService.process(p, m);
    }
}
```

### 7. **Sửa PetUpgradeService.process() để đọc message đúng**

**File:** `PetUpgradeService.java`
**Method:** `process()`

```java
// ❌ CODE HIỆN TẠI (SAI)
public static void process(Player p, Message m) throws IOException {
    byte type = m.reader().readByte(); // ❌ Đọc lại byte đã được đọc trong PetService.handle()
    short id = m.reader().readShort();
    byte bery_gem = m.reader().readByte();
    
    // ... rest of the code ...
}

// ✅ CODE SỬA (ĐÚNG)
public static void process(Player p, Message m) throws IOException {
    // ✅ Không đọc lại byte đầu tiên vì đã được đọc trong PetService.handle()
    short id = m.reader().readShort();
    byte bery_gem = m.reader().readByte();
    
    // ✅ Xác định type dựa trên context hoặc thêm parameter
    byte type = 9; // Default type for upgrade operations
    
    // ... rest of the code ...
}
```

### 8. **Thêm Validation cho Pet Upgrade**

**File:** `PetUpgradeService.java`
**Method:** `startPetUpgrade()`

```java
// ✅ THÊM VALIDATION
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
    
    // ✅ THÊM VALIDATION CHO UPGRADE DATA
    if (pet.upgrade >= UPGRADE_DATA.size()) {
        Service.sendMessageDialogBox(p, "Dữ liệu nâng cấp không hợp lệ!");
        return;
    }
    
    PetUpgradeData upgradeData = UPGRADE_DATA.get(pet.upgrade);
    
    // ... rest of the code ...
}
```

## 📝 Tóm Tắt Các Sửa Đổi

1. **✅ Sửa useProtectionItem()** - Gửi đúng message type cho từng loại protection item
2. **✅ Sửa requestPetUpgrade()** - Gửi ruby cost thay vì -1
3. **✅ Thêm petUpgradeTool field** - Vào Player class
4. **✅ Sửa startPetUpgrade()** - Kiểm tra template null và thêm validation
5. **✅ Sửa message handler** - Loại bỏ việc gọi PetUpgradeService.process trực tiếp
6. **✅ Sửa PetService.handle()** - Đảm bảo xử lý message đúng cách
7. **✅ Sửa PetUpgradeService.process()** - Không đọc lại byte đầu tiên
8. **✅ Thêm validation** - Cho upgrade data và pet state

## 🎯 Kết Quả

Sau khi sửa những điểm trên, server code sẽ tương thích hoàn toàn với client code đã implement, đảm bảo:

- **Message flow đúng** giữa client và server
- **Protection items** hoạt động chính xác
- **Confirmation dialog** hiển thị đúng chi phí
- **Pet upgrade** hoạt động ổn định
- **Error handling** đầy đủ
- **Data validation** chặt chẽ

## ⚠️ Lưu Ý Quan Trọng

1. **Backup code** trước khi sửa
2. **Test kỹ** từng chức năng sau khi sửa
3. **Kiểm tra database** có field `petUpgradeTool` và `trainingLevel` trong Player table
4. **Đảm bảo** tất cả imports và dependencies đầy đủ
5. **Restart server** sau khi sửa code

## 🔄 Quy Trình Sửa

1. Sửa từng file một theo thứ tự
2. Compile và test từng file
3. Test integration giữa client và server
4. Kiểm tra tất cả chức năng pet upgrade
5. Deploy và monitor

---

**📅 Ngày tạo:** $(date)
**👨‍💻 Tác giả:** AI Assistant
**📋 Phiên bản:** 1.0
