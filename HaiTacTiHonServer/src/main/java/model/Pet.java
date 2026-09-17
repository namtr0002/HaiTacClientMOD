package model;

import network.Message;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import network.Service;
import java.text.SimpleDateFormat;
import java.util.Date;
import template.Option;

public class Pet {

    public static List<Pet> ENTRY = new ArrayList<>();
    public short id, icon, frame;
    public String name;
    public byte type;
    public List<Option> op;

    public static void process(Player p, Message m2) throws IOException {
        if (p == null || m2 == null) return;
        byte act = m2.reader().readByte();
        // System.out.println(act + " " + m2.reader().available());
        if (act == 3) { // show table
            Pet.show_inven(p);
        } else if (act == 4) {
            try {
                byte type = m2.reader().readByte();
                short id = m2.reader().readShort();
                // System.out.println(type + " " + id);
                if (type == 1) { // mac
                    if (p.my_pet == null) {
                        p.my_pet = new ArrayList<>();
                    }
                    MyPet pet_select = null;
                    for (int i = 0; i < p.my_pet.size(); i++) {
                        MyPet mp = p.my_pet.get(i);
                        if (mp != null && (mp.id == id || (mp.template != null && mp.template.id == id))) {
                            pet_select = mp;
                        } else if (mp != null) {
                            mp.isUse = false;
                        }
                    }

                    // Nếu chưa có trong my_pet (ví dụ mở full danh sách ở test mode) thì khởi tạo MyPet từ template
                    if (pet_select == null) {
                        Pet template = Pet.getTemplate(id);
                        if (template != null) {
                            pet_select = new MyPet();
                            pet_select.id = id;
                            pet_select.template = template;
                            pet_select.isUse = true;
                            pet_select.time = -1;
                            p.my_pet.add(pet_select);
                        }
                    }

                    if (pet_select != null) {
                        pet_select.isUse = true;
                        if (pet_select.template == null) {
                            pet_select.template = Pet.getTemplate(pet_select.id);
                        }
                        Pet.show_inven(p);
                        String petName = (pet_select.template != null && pet_select.template.name != null) ? pet_select.template.name : "Pet";
                        if (p.getService() != null) {
                            p.getService().send_box_ThongBao_OK("Trang bị " + petName + " thành công");
                        }
                        p.update_info_to_all();
                    }
                } else if (type == 0) { // thao
                    MyPet pet_select = null;
                    if (p.my_pet != null) {
                        for (int i = 0; i < p.my_pet.size(); i++) {
                            MyPet mp = p.my_pet.get(i);
                            if (mp != null && (mp.id == id || (mp.template != null && mp.template.id == id))) {
                                pet_select = mp;
                                mp.isUse = false;
                            }
                        }
                    }
                    // Tháo an toàn: đảm bảo không còn pet nào active
                    if (p.my_pet != null) {
                        for (int i = 0; i < p.my_pet.size(); i++) {
                            if (p.my_pet.get(i) != null) {
                                p.my_pet.get(i).isUse = false;
                            }
                        }
                    }
                    Pet.show_inven(p);
                    String petName = "Pet";
                    if (pet_select != null && pet_select.template != null && pet_select.template.name != null) {
                        petName = pet_select.template.name;
                    } else {
                        Pet temp = Pet.getTemplate(id);
                        if (temp != null && temp.name != null) {
                            petName = temp.name;
                        }
                    }
                    if (p.getService() != null) {
                        p.getService().send_box_ThongBao_OK("Tháo " + petName + " thành công");
                    }
                    p.update_info_to_all();
                }
            } catch (IOException e) {
            }
        }
    }

    public static void show_inven(Player p) throws IOException {
        if (p == null) return;
        boolean isTest = core.Manager.gI() != null && core.Manager.gI().isTestMode();
        Message m = new Message(-80);
        m.writer().writeByte(3);

        if (isTest) {
            // Chế độ Test: Gửi toàn bộ pet có trong template Pet.ENTRY
            List<Pet> listTemplate = Pet.ENTRY != null ? Pet.ENTRY : new ArrayList<>();
            m.writer().writeShort(listTemplate.size());
            MyPet curPet = p.get_pet();
            short currentUsedId = (curPet != null && curPet.isUse) ? curPet.id : -1;

            for (int i = 0; i < listTemplate.size(); i++) {
                Pet template = listTemplate.get(i);
                if (template == null) continue;
                m.writer().writeShort(template.id);
                String petName = template.name != null ? template.name : "";
                m.writer().writeUTF(petName + " vĩnh viễn");
                m.writer().writeUTF(petName);
                m.writer().writeShort(template.icon);
                m.writer().writeByte(110); // Category Pet
                m.writer().writeByte((template.id == currentUsedId || (curPet != null && curPet.template != null && curPet.template.id == template.id && curPet.isUse)) ? 1 : 0);
                m.writer().writeByte(0); // Upgrade level
                
                int opSize = template.op != null ? template.op.size() : 0;
                m.writer().writeByte(opSize);
                if (template.op != null) {
                    for (int j = 0; j < template.op.size(); j++) {
                        Option op = template.op.get(j);
                        if (op != null) {
                            m.writer().writeByte(op.id);
                            m.writer().writeShort(op.getParam());
                        } else {
                            m.writer().writeByte(0);
                            m.writer().writeShort(0);
                        }
                    }
                }
            }
        } else {
            // Chế độ bình thường: Gửi theo danh sách p.my_pet
            List<MyPet> listPet = p.my_pet != null ? p.my_pet : new ArrayList<>();
            m.writer().writeShort(listPet.size());
            for (int i = 0; i < listPet.size(); i++) {
                MyPet myPet = listPet.get(i);
                if (myPet == null || myPet.template == null) continue;
                m.writer().writeShort(myPet.id);
                if (myPet.time != -1) {
                    Date d = new Date(myPet.time);
                    String nameShow = myPet.template.name + " HSD "
                            + (new SimpleDateFormat("dd/MM/yyyy HH:mm").format(d));
                    m.writer().writeUTF(nameShow);
                } else {
                    m.writer().writeUTF(myPet.template.name + " vĩnh viễn");
                }
                m.writer().writeUTF(myPet.template.name != null ? myPet.template.name : "");
                m.writer().writeShort(myPet.template.icon);
                m.writer().writeByte(110);
                m.writer().writeByte(myPet.isUse ? 1 : 0);
                m.writer().writeByte(0); // upgrade level
                
                int opSize = myPet.template.op != null ? myPet.template.op.size() : 0;
                m.writer().writeByte(opSize);
                if (myPet.template.op != null) {
                    for (int j = 0; j < myPet.template.op.size(); j++) {
                        Option op = myPet.template.op.get(j);
                        if (op != null) {
                            m.writer().writeByte(op.id);
                            m.writer().writeShort(op.getParam());
                        } else {
                            m.writer().writeByte(0);
                            m.writer().writeShort(0);
                        }
                    }
                }
            }
        }

        p.addmsg(m);
        m.cleanup();

        // Đồng bộ hiển thị pet trên bản đồ
        if (p.getService() != null) {
            p.getService().pet(p, false);
        }
        if (p.map != null && p.map.players != null) {
            for (int i = 0; i < p.map.players.size(); i++) {
                Player p0 = p.map.players.get(i);
                if (p0 != null && p0 != p && p0.getService() != null) {
                    p0.getService().pet(p, false);
                }
            }
        }
    }

    public static Pet getTemplate(int id) {
        if (ENTRY == null) return null;
        for (int i = 0; i < ENTRY.size(); i++) {
            Pet p = ENTRY.get(i);
            if (p != null && p.id == id) {
                return p;
            }
        }
        return null;
    }
}
