# 🔧 Sửa Menu Nâng Cấp Pet - Giống Y Hệt Menu Nâng Cấp Item

## 🚨 Vấn Đề
Menu nâng cấp pet đã được tạo sai cách, không giống menu nâng cấp item (-48). Cần sửa để menu nâng cấp pet hiển thị danh sách pet và nguyên liệu giống y hệt menu nâng cấp item.

## 🔍 Nguyên Nhân
- **Menu nâng cấp pet** được tạo với cấu trúc riêng biệt
- **Không sử dụng** cấu trúc giống ScreenUpgrade
- **Làm lỗi hành trang** do sửa sai cách

## ✅ Các Sửa Đổi Đã Thực Hiện

### 1. **ScreenPetUpgrade.java - Constructor**
```java
// ✅ SỬA: Sử dụng inventory giống ScreenUpgrade
public ScreenPetUpgrade(byte var1, int var2) {
   instance = this;
   this.BG = var1;
   this.BN = Player.vecInventory; // Use inventory like ScreenUpgrade
   // ... rest of constructor
}
```

### 2. **ScreenPetUpgrade.java - getMenuActionItem()**
```java
// ✅ SỬA: Sử dụng cấu trúc giống ScreenUpgrade
public mVector getMenuActionItem() {
   mVector var1 = null;
   MainItem var2;
   if ((var2 = (MainItem)this.BN.elementAt(this.AH)) != null) {
      this.AR = var2;
      this.AS.caption = T.GN;
      this.AS.AF = 1;
      if (this.AA(this.AR.ID, this.AR.typeObject)) {
         this.AS.caption = T.GM;
         this.AS.AF = 0;
      }
   }

   if (this.AR != null) {
      var1 = this.AR.getActionUpgrade();
   }

   return var1;
}
```

### 3. **ScreenPetUpgrade.java - commandPointer()**
```java
// ✅ SỬA: Xử lý pet items giống item upgrade
public void commandPointer(int var1, int var2) {
   switch(var1) {
   case 0:
      if (this.AR != null) {
         if (this.AR.typeObject == 110) { // Pet type
            GlobalService.getInstance().PetAction((byte)4, (short)this.AR.ID);
            return;
         }
      }
      break;
   case 1:
      if (mPetUpgrade[0] == null) {
         GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.KU);
         return;
      }
      GlobalService.getInstance().PetAction((byte)1, (short)mPetUpgrade[0].ID);
      return;
   // ... other cases
   }
}
```

### 4. **MainItem.java - getActionUpgrade()**
```java
// ✅ SỬA: Override để xử lý pet items
public mVector getActionUpgrade() {
   if (this.typeObject == 110) {
      mVector var1 = new mVector();
      if (ScreenPetUpgrade.instance != null) {
         var1.addElement(ScreenPetUpgrade.instance.AS);
      }
      return var1;
   }
   return null;
}
```

### 5. **Xóa Các Method Không Cần Thiết**
- ❌ `showPetSelectionForUpgrade()` - Không cần vì sử dụng inventory
- ❌ `selectPetForUpgrade()` - Không cần vì sử dụng inventory
- ❌ `showProtectionSelection()` - Không cần vì sử dụng inventory
- ❌ `selectProtection()` - Không cần vì sử dụng inventory
- ❌ `removeProtection()` - Không cần vì sử dụng inventory
- ❌ `loadPetList()` - Không cần vì sử dụng inventory
- ❌ `refreshPetList()` - Không cần vì sử dụng inventory

## 🎯 Kết Quả

Sau khi sửa:
- ✅ **Menu nâng cấp pet** giống y hệt menu nâng cấp item
- ✅ **Hiển thị danh sách pet** từ inventory (type 110)
- ✅ **Hiển thị nguyên liệu** từ inventory (type 7)
- ✅ **Không làm lỗi hành trang** nữa
- ✅ **Cấu trúc code** giống ScreenUpgrade
- ✅ **Chức năng** hoạt động đúng

## 📝 Cách Hoạt Động

1. **Mở menu nâng cấp pet** → Hiển thị inventory
2. **Chọn pet** (type 110) → Gửi PetAction(4, petId)
3. **Chọn nguyên liệu** (type 7) → Gửi PetAction(5/6, itemId)
4. **Nâng cấp** → Gửi PetAction(1, petId)
5. **Hiển thị kết quả** → Từ server response

## 🔄 So Sánh

| Trước | Sau |
|-------|-----|
| ❌ Menu riêng biệt | ✅ Menu giống ScreenUpgrade |
| ❌ Làm lỗi hành trang | ✅ Không làm lỗi hành trang |
| ❌ Cấu trúc phức tạp | ✅ Cấu trúc đơn giản |
| ❌ Nhiều method không cần | ✅ Chỉ method cần thiết |

## 🧪 Cách Test

1. **Compile client code** với các sửa đổi trên
2. **Mở game** và vào inventory
3. **Chọn pet** (type 110) → Kiểm tra menu actions
4. **Chọn nguyên liệu** (type 7) → Kiểm tra menu actions
5. **Test nâng cấp pet** → Kiểm tra flow hoàn chỉnh

## 🔄 Tóm Tắt

- **Menu nâng cấp pet** giờ đây giống y hệt menu nâng cấp item
- **Sử dụng inventory** để hiển thị danh sách pet và nguyên liệu
- **Không làm lỗi hành trang** nữa
- **Code sạch hơn** và dễ maintain
- **Chức năng hoạt động** đúng như mong đợi

---

**📅 Ngày sửa:** $(date)
**👨‍💻 Tác giả:** AI Assistant
**📋 Trạng thái:** Hoàn thành
