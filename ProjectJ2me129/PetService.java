package gameplay.players.pet;

import static engine.kernel.MenuController.send_dynamic_menu;
import engine.network.Message;
import engine.network.Service;
import gameplay.players.Player;
import item.Option;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * PetService: xử lý packet liên quan pet + show menu + API để MenuController gọi.
 *
 * - Luyện: show box yes/no id=80, xử lý kết quả ở confirmTrainBox(...)
 * - Hóa hình: random template trực tiếp (không show list)
 *
 * Public API để MenuController gọi:
 *   PetService.MenuPetMain(p, index);
 *   PetService.MenuPetMorph(p, index);
 *   PetService.MenuPetTrain(p, index);
 *   PetService.confirmTrainBox(p, selectedByte);
 */
public class PetService {

    // Menu IDs (cài lại nếu cần)
    public static final int MENU_MAIN_ID = 8888;
    public static final int MENU_MORPH_ID = 8889; // giữ để tương thích nếu bạn muốn hiện list
    public static final int MENU_TRAIN_ID = 8890;

    private static final Random R = new Random();

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
        new String[] { "Sử dụng", "Hủy Pet", "Luyện Pet", "Hóa Hình Pet", "Nâng cấp Pet" },
        new short[] { 129, 130, 155, 156, 157 });
            } else if (type == 0) {
                unequipPet(p, id);
            }
        }
    }

    // ========================= PUBLIC API (MenuController gọi) =========================

    /**
     * Gọi khi client trả selection menu chính (menuId = 8888)
     * signature giữ throws IOException để tương thích nơi gọi nếu cần.
     */
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
            // TODO: Implement pet upgrade functionality
            Service.sendMessageDialogBox(p, "Chức năng nâng cấp pet sẽ được triển khai sau.");
            break;
        default:
            Service.sendMessageDialogBox(p, "Tùy chọn không hợp lệ.");
    }
}
    
    public static void MenuPetTrainSubmenu(Player p) {
        try {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void confirmDeletePet(Player p, byte selected) {
        if (selected != 2) {
            Service.sendMessageDialogBox(p, "Đã hủy thao tác Hủy Pet.");
            return;
        }
        short petIndex = p.actionPet; // đã lưu lúc show hộp
        if (petIndex < 0 || petIndex >= p.my_pet.size()) {
            Service.sendMessageDialogBox(p, "Pet không tồn tại hoặc đã thay đổi.");
            return;
        }

        MyPet pet = p.my_pet.get(petIndex);
        // nếu đang sử dụng thì bỏ sử dụng
        if (pet.isUse) pet.isUse = false;

        String petName = (pet.template != null ? pet.template.name : ("Pet#" + petIndex));

        // Xóa petServiceM ra khỏi danh sách
        p.my_pet.remove(petIndex);

        // Cập nhật UI/player
        showInventory(p);
        p.update_info_to_all();

        Service.sendMessageDialogBox(p, "Đã hủy pet: " + petName);
    }


    /**
     * Gọi khi client trả selection menu Morph (menuId = 8889) - wrapper public
     * Nếu bạn dùng submenu chọn template -> sẽ morph theo template được chọn.
     */
    public static void MenuPetMorph(Player p, byte index) {
        short petIndex = p.idPetAction;
        if (petIndex < 0 || petIndex >= p.my_pet.size()) {
            Service.sendMessageDialogBox(p, "Pet không tồn tại hoặc đã thay đổi.");
            return;
        }
        List<PetTemplate> list = PetManager.TEMPLATES;
        if (list.isEmpty()) {
            Service.sendMessageDialogBox(p, "Hiện không có template để chọn.");
            return;
        }
        if (index < 0 || index >= list.size()) {
            Service.sendMessageDialogBox(p, "Lựa chọn không hợp lệ.");
            return;
        }
        short newTempId = list.get(index).id;
        morphPet(p, petIndex, newTempId);
    }

    /**
     * Gọi khi client trả selection menu Train (menuId = 8890) - wrapper public
     * Ở đây chuyển tiếp tới PetFunction để bạn triển khai chi tiết nếu muốn.
     */
    public static void MenuPetTrain(Player p, byte index) {
        try {
            PetFunction.Menu_Pet(p, index);
        } catch (IOException e) {
            e.printStackTrace();
            Service.sendMessageDialogBox(p, "Lỗi khi gọi chức năng Luyện Pet.");
        }
    }

    /**
     * Gọi khi box confirm id=80 trả về (MenuController case 81 / case 80 tùy project),
     * selected: giá trị nút client trả về. (Bạn dùng p.data_yesno[0] như ví dụ)
     */
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

    // ========================= internal helpers =========================

    // RANDOM morph (không chọn) — chọn 1 template khác ngẫu nhiên và thực hiện morphTo()
    private static void randomMorphPet(Player p, short petIndex) {
        if (petIndex < 0 || petIndex >= p.my_pet.size()) {
            Service.sendMessageDialogBox(p, "Pet không tồn tại.");
            return;
        }
        MyPet pet = p.my_pet.get(petIndex);
        if (pet.isHoaHinh) {
            Service.sendMessageDialogBox(p, "Pet đã hóa hình trước đó.");
            return;
        }
        List<PetTemplate> list = PetManager.TEMPLATES;
        if (list.isEmpty()) {
            Service.sendMessageDialogBox(p, "Hiện chưa có hình để hóa.");
            return;
        }

        // chọn ngẫu nhiên template khác template hiện tại
        List<PetTemplate> candidates = new ArrayList<>();
        for (PetTemplate t : list) {
            if (pet.template == null || t.id != pet.template.id) candidates.add(t);
        }
        if (candidates.isEmpty()) {
            candidates.addAll(list);
        }
        PetTemplate chosen = candidates.get(R.nextInt(candidates.size()));
        boolean ok = pet.morphTo(chosen.id);
        if (ok) {
            Service.sendMessageDialogBox(p, "Hóa hình thành công! Pet đã đổi thành: " + chosen.name);
            // TODO: persist player if needed
            showInventory(p);
            p.update_info_to_all();
        } else {
            Service.sendMessageDialogBox(p, "Hóa hình thất bại.");
        }
    }

    // ---------- helper hàm equip/unequip/showInventory/tick (giữ tương thích client) ----------
    private static void equipPet(Player p, short index) throws IOException {
        if (index < 0 || index >= p.my_pet.size()) {
            Service.sendMessageDialogBox(p, "Pet không tồn tại.");
            return;
        }
        for (MyPet pet : p.my_pet) pet.isUse = false;
        MyPet pet = p.my_pet.get(index);
        pet.isUse = true;
        showInventory(p);
        Service.sendMessageDialogBox(p, "Trang bị " + pet.template.name + " thành công");
        p.update_info_to_all();
    }

    private static void unequipPet(Player p, short index) throws IOException {
        if (index < 0 || index >= p.my_pet.size()) {
            Service.sendMessageDialogBox(p, "Pet không tồn tại.");
            return;
        }
        MyPet pet = p.my_pet.get(index);
        if (!pet.isUse) {
            Service.sendMessageDialogBox(p, "Pet chưa đang được sử dụng.");
            return;
        }
        pet.isUse = false;
        showInventory(p);
        Service.sendMessageDialogBox(p, "Tháo " + pet.template.name + " thành công");
        p.update_info_to_all();
    }

    public static void showInventory(Player p) {
        try {
            Message m = new Message(-80); // act = 3
            m.writer().writeByte(3);
            m.writer().writeShort(p.my_pet.size());
            for (int i = 0; i < p.my_pet.size(); i++) {
                MyPet pet = p.my_pet.get(i);
                pet.id = (short) i;
                m.writer().writeShort(pet.id);
                m.writer().writeUTF(pet.template.name);
                m.writer().writeUTF(pet.template.name);
                m.writer().writeShort(pet.template.icon);
                m.writer().writeByte(110);
                m.writer().writeByte(pet.isUse ? 1 : 0);
                m.writer().writeByte(pet.upgrade);
                // KHÔNG gửi expire/isHoaHinh để tương thích client hiện tại
                List<Option> opts = pet.getOptions();
                m.writer().writeByte(opts.size());
                for (Option opt : opts) {
                    m.writer().writeByte(opt.id);
                    m.writer().writeShort((short) Math.min(opt.getParam(), 32000));
                }
            }
            p.conn.addmsg(m);
            m.cleanup();

            for (Player p0 : p.map.players) {
                Service.petServiceM(p, p0, false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void tickPetsForPlayer(Player p) {
        boolean changed = false;
        Iterator<MyPet> it = p.my_pet.iterator();
        while (it.hasNext()) {
            MyPet pet = it.next();
            if (pet.expire <= -1) {
                pet.expire = -1;
                continue;
            }
            if (pet.expire > 0) {
                pet.expire--;
                if (pet.expire <= 0) {
                    if (pet.isUse) pet.isUse = false;
                    it.remove();
                    changed = true;
                }
            } else if (pet.expire == 0) {
                if (pet.isUse) pet.isUse = false;
                it.remove();
                changed = true;
            }
        }
        if (changed) {
            showInventory(p);
            p.update_info_to_all();
        } else {
            showInventory(p);
        }
    }

    // morphPet (dùng nếu cần morph theo id cụ thể)
    public static void morphPet(Player p, short petIndex, short newTemplateId) {
        if (petIndex < 0 || petIndex >= p.my_pet.size()) {
            Service.sendMessageDialogBox(p, "Pet không tồn tại.");
            return;
        }
        MyPet pet = p.my_pet.get(petIndex);
        if (pet.isHoaHinh) {
            Service.sendMessageDialogBox(p, "Pet đã hóa hình trước đó.");
            return;
        }
        boolean ok = pet.morphTo(newTemplateId);
        if (ok) {
            Service.sendMessageDialogBox(p, "Hóa hình thành công!");
            showInventory(p);
            p.update_info_to_all();
        } else {
            Service.sendMessageDialogBox(p, "Hóa hình thất bại (template không hợp lệ).");
        }
    }
}
