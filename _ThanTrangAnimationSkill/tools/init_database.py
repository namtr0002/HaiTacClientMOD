# -*- coding: utf-8 -*-
import json
import os
import shutil

target_dir = r"C:\DepLor\HTTH\Team\_ThanTrangAnimationSkill"
db_dir = os.path.join(target_dir, "database")
tools_dir = os.path.join(target_dir, "tools")
os.makedirs(db_dir, exist_ok=True)
os.makedirs(tools_dir, exist_ok=True)

SKILLS = [
    {
        "id": 4001, "id_index": 4001, "id_2": 4001, "icon": 201, "typeSkill": 1, "typeBuff": 0,
        "name": "Hỏa Diễm Thần Quyền", "typeEffSkill": 4001, "range": 220, "rangeLan": 140, "nTarget": 5,
        "damage": 5000, "manaLost": 50, "timeDelay": 3000, "nKick": 1,
        "info": "Tuyệt kỹ Hỏa Long Thần Trang giải phóng biển lửa thiêu rụi mục tiêu.",
        "Lv_RQ": 1, "percentLv": 0, "typeDevil": 0,
        "option": "[[1,350],[5,150],[10,350],[13,400],[14,300],[29,500]]", "EffSpec": "[2,50,30]",
        "LvDevilSkill": 0, "phanTramDevilSkill": 0,
        "char": "Nika / Ace", "set_name": "Hỏa Long Thần Trang", "element": "Hỏa Thần / Nika Sun God", "color": "#ff5722", "color2": "#ffeb3b",
        "status_tag": "THIÊU ĐỐT", "art_prefix": "hoa_diem_than_quyen_clean"
    },
    {
        "id": 4002, "id_index": 4002, "id_2": 4002, "icon": 202, "typeSkill": 1, "typeBuff": 0,
        "name": "Đại Phún Hỏa Volcano", "typeEffSkill": 4002, "range": 220, "rangeLan": 140, "nTarget": 5,
        "damage": 5000, "manaLost": 50, "timeDelay": 3000, "nKick": 1,
        "info": "Tuyệt kỹ Hải Vương Thần Trang triệu hồi nham thạch phun trào hủy diệt.",
        "Lv_RQ": 1, "percentLv": 0, "typeDevil": 0,
        "option": "[[1,350],[2,350],[10,350],[13,400],[14,300],[29,500]]", "EffSpec": "[2,50,30]",
        "LvDevilSkill": 0, "phanTramDevilSkill": 0,
        "char": "Akainu (Sakazuki)", "set_name": "Hải Vương Thần Trang", "element": "Nham Thạch / Dung Nham (Magma)", "color": "#d32f2f", "color2": "#ff9800",
        "status_tag": "NHAM THẠCH", "art_prefix": "dai_phun_hoa_clean"
    },
    {
        "id": 4003, "id_index": 4003, "id_2": 4003, "icon": 203, "typeSkill": 1, "typeBuff": 0,
        "name": "Kỷ Băng Hà Tuyệt Đối", "typeEffSkill": 4003, "range": 220, "rangeLan": 140, "nTarget": 5,
        "damage": 5000, "manaLost": 50, "timeDelay": 3000, "nKick": 1,
        "info": "Tuyệt kỹ Lôi Thần Thần Trang đóng băng vạn vật trong chớp mắt.",
        "Lv_RQ": 1, "percentLv": 0, "typeDevil": 0,
        "option": "[[1,350],[3,350],[10,350],[13,400],[28,10],[29,500]]", "EffSpec": "[3,50,30]",
        "LvDevilSkill": 0, "phanTramDevilSkill": 0,
        "char": "Aokiji (Kuzan)", "set_name": "Lôi Thần Thần Trang", "element": "Băng Cực Hàn / Băng Hà (Absolute Zero Ice)", "color": "#00b0ff", "color2": "#e0f7fa",
        "status_tag": "ĐÓNG BĂNG", "art_prefix": "ky_bang_ha_clean"
    },
    {
        "id": 4004, "id_index": 4004, "id_2": 4004, "icon": 204, "typeSkill": 1, "typeBuff": 0,
        "name": "Bát Xích Quỳnh Khúc Ngọc", "typeEffSkill": 4004, "range": 220, "rangeLan": 140, "nTarget": 5,
        "damage": 5000, "manaLost": 50, "timeDelay": 3000, "nKick": 1,
        "info": "Tuyệt kỹ Phong Ma Thần Trang phóng mưa ngọc quang tử tốc độ ánh sáng.",
        "Lv_RQ": 1, "percentLv": 0, "typeDevil": 0,
        "option": "[[1,350],[7,150],[10,350],[13,400],[9,200],[29,500]]", "EffSpec": "[4,50,30]",
        "LvDevilSkill": 0, "phanTramDevilSkill": 0,
        "char": "Kizaru (Borsalino)", "set_name": "Phong Ma Thần Trang", "element": "Quang Tử / Thánh Quang (Holy Light)", "color": "#ffd700", "color2": "#fff59d",
        "status_tag": "CHÓA MẮT", "art_prefix": "bat_xich_quynh_khuc_clean"
    },
    {
        "id": 4005, "id_index": 4005, "id_2": 4005, "icon": 205, "typeSkill": 1, "typeBuff": 0,
        "name": "Hắc Ám Thôn Phệ Vô Tận", "typeEffSkill": 4005, "range": 220, "rangeLan": 140, "nTarget": 5,
        "damage": 5000, "manaLost": 50, "timeDelay": 3000, "nKick": 1,
        "info": "Tuyệt kỹ Tử Thần Thần Trang mở ra lỗ đen nuốt chửng mọi kẻ thù, chặn di chuyển mục tiêu.",
        "Lv_RQ": 1, "percentLv": 0, "typeDevil": 0,
        "option": "[[1,350],[2,350],[10,350],[13,400],[29,500],[30,50]]", "EffSpec": "[8,100,35]",
        "LvDevilSkill": 0, "phanTramDevilSkill": 0,
        "char": "Râu Đen (Blackbeard)", "set_name": "Tử Thần Thần Trang", "element": "Hắc Ám / Lỗ Đen Hư Không (Black Hole Void)", "color": "#7b1fa2", "color2": "#212121",
        "status_tag": "LỖ ĐEN NUỐT CHỬNG", "art_prefix": "hac_am_clean"
    },
    {
        "id": 4006, "id_index": 4006, "id_2": 4006, "icon": 206, "typeSkill": 1, "typeBuff": 0,
        "name": "200 Triệu Volt Thần Lôi", "typeEffSkill": 4006, "range": 220, "rangeLan": 140, "nTarget": 5,
        "damage": 5000, "manaLost": 50, "timeDelay": 3000, "nKick": 1,
        "info": "Tuyệt kỹ Băng Đế Thần Trang giáng sấm sét 200 triệu volt hủy diệt.",
        "Lv_RQ": 1, "percentLv": 0, "typeDevil": 0,
        "option": "[[1,350],[5,150],[10,350],[13,400],[28,10],[29,500]]", "EffSpec": "[6,50,30]",
        "LvDevilSkill": 0, "phanTramDevilSkill": 0,
        "char": "Enel (Raijin)", "set_name": "Băng Đế Thần Trang", "element": "Lôi Thần / 200 Triệu Volt (Thunder God Raijin)", "color": "#00e5ff", "color2": "#ffea00",
        "status_tag": "TÊ LIỆT", "art_prefix": "than_loi_200m_clean"
    },
    {
        "id": 4007, "id_index": 4007, "id_2": 4007, "icon": 207, "typeSkill": 1, "typeBuff": 0,
        "name": "Hải Chấn Toái Địa Cầu", "typeEffSkill": 4007, "range": 220, "rangeLan": 140, "nTarget": 5,
        "damage": 5000, "manaLost": 50, "timeDelay": 3000, "nKick": 1,
        "info": "Tuyệt kỹ Chấn Động Thần Trang đập vỡ không gian tạo đại hải chấn kinh thiên động địa. Kèm 50% tỷ lệ gây Choáng trong 3.0s.",
        "Lv_RQ": 1, "percentLv": 0, "typeDevil": 0,
        "option": "[[1,350],[10,350],[13,400],[28,10],[29,500],[30,50]]", "EffSpec": "[1,50,30]",
        "LvDevilSkill": 0, "phanTramDevilSkill": 0,
        "char": "Râu Trắng (Whitebeard)", "set_name": "Chấn Động Thần Trang", "element": "Chấn Động / Rạn Nứt Không Gian (Gura Gura Quake)", "color": "#4dd0e1", "color2": "#ff7043",
        "status_tag": "CHOÁNG CHẤN ĐỘNG", "art_prefix": "hai_chan_toai_dia_clean"
    },
    {
        "id": 4008, "id_index": 4008, "id_2": 4008, "icon": 208, "typeSkill": 1, "typeBuff": 0,
        "name": "ROOM Gamma Knife", "typeEffSkill": 4008, "range": 220, "rangeLan": 140, "nTarget": 5,
        "damage": 5000, "manaLost": 50, "timeDelay": 3000, "nKick": 1,
        "info": "Tuyệt kỹ Kim Cương Thần Trang phân cắt và phá hủy nội tạng đối thủ.",
        "Lv_RQ": 1, "percentLv": 0, "typeDevil": 0,
        "option": "[[1,350],[10,350],[13,400],[28,10],[29,500],[30,50]]", "EffSpec": "[7,50,30]",
        "LvDevilSkill": 0, "phanTramDevilSkill": 0,
        "char": "Trafalgar Law", "set_name": "Kim Cương Thần Trang", "element": "Không Gian / Phẫu Thuật Điện Từ (Ope Ope Room)", "color": "#26a69a", "color2": "#80cbc4",
        "status_tag": "PHÁ NỘI TẠNG", "art_prefix": "room_gamma_knife_clean"
    },
    {
        "id": 4009, "id_index": 4009, "id_2": 4009, "icon": 209, "typeSkill": 1, "typeBuff": 0,
        "name": "Từ Trường Bộc Phá Đại Pháo", "typeEffSkill": 4009, "range": 220, "rangeLan": 140, "nTarget": 5,
        "damage": 5000, "manaLost": 50, "timeDelay": 3000, "nKick": 1,
        "info": "Tuyệt kỹ Hắc Ám Thần Trang tụ lực từ tính bắn đại pháo hủy diệt.",
        "Lv_RQ": 1, "percentLv": 0, "typeDevil": 0,
        "option": "[[1,350],[10,350],[13,400],[28,10],[29,500],[30,50]]", "EffSpec": "[1,55,30]",
        "LvDevilSkill": 0, "phanTramDevilSkill": 0,
        "char": "Eustass Kid", "set_name": "Hắc Ám Thần Trang", "element": "Từ Tính / Đại Pháo Phế Liệu (Damned Punk Railgun)", "color": "#e91e63", "color2": "#b0bec5",
        "status_tag": "BỘC PHÁ TỪ TRƯỜNG", "art_prefix": "tu_truong_dai_phao_clean"
    },
    {
        "id": 4010, "id_index": 4010, "id_2": 4010, "icon": 210, "typeSkill": 1, "typeBuff": 0,
        "name": "Cổ Độc Phán Quyết Venom", "typeEffSkill": 4010, "range": 220, "rangeLan": 140, "nTarget": 5,
        "damage": 5000, "manaLost": 50, "timeDelay": 3000, "nKick": 1,
        "info": "Tuyệt kỹ Quang Minh Thần Trang phóng độc dược ăn mòn sinh mệnh.",
        "Lv_RQ": 1, "percentLv": 0, "typeDevil": 0,
        "option": "[[1,350],[10,350],[13,400],[28,10],[29,500],[30,50]]", "EffSpec": "[8,50,30]",
        "LvDevilSkill": 0, "phanTramDevilSkill": 0,
        "char": "Magellan", "set_name": "Quang Minh Thần Trang", "element": "Cự Độc / Ăn Mòn Axit (Venom Demon Hydra)", "color": "#ab47bc", "color2": "#76ff03",
        "status_tag": "KỊCH ĐỘC", "art_prefix": "cu_doc_venom_clean"
    },
    {
        "id": 4011, "id_index": 4011, "id_2": 4011, "icon": 211, "typeSkill": 1, "typeBuff": 0,
        "name": "Mũi Tên Mê Hoặc Thạch Hóa", "typeEffSkill": 4011, "range": 220, "rangeLan": 140, "nTarget": 5,
        "damage": 5000, "manaLost": 50, "timeDelay": 3000, "nKick": 1,
        "info": "Tuyệt kỹ Tu La Thần Trang hóa đá mọi kẻ thù trúng phải.",
        "Lv_RQ": 1, "percentLv": 0, "typeDevil": 0,
        "option": "[[1,350],[10,350],[13,400],[28,10],[29,500],[30,50]]", "EffSpec": "[9,50,30]",
        "LvDevilSkill": 0, "phanTramDevilSkill": 0,
        "char": "Boa Hancock", "set_name": "Tu La Thần Trang", "element": "Mê Hoặc / Hóa Đá (Mero Mero Slave Arrow)", "color": "#ec407a", "color2": "#f48fb1",
        "status_tag": "HÓA ĐÁ", "art_prefix": "mui_ten_thach_hoa_clean"
    },
    {
        "id": 4012, "id_index": 4012, "id_2": 4012, "icon": 212, "typeSkill": 1, "typeBuff": 0,
        "name": "Phượng Hoàng Bất Tử Bộc Phá", "typeEffSkill": 4012, "range": 220, "rangeLan": 140, "nTarget": 5,
        "damage": 5000, "manaLost": 50, "timeDelay": 3000, "nKick": 1,
        "info": "Tuyệt kỹ Thánh Linh Thần Trang tung cánh phượng hoàng lam hỏa thiêu rụi.",
        "Lv_RQ": 1, "percentLv": 0, "typeDevil": 0,
        "option": "[[1,350],[3,500],[10,350],[13,400],[29,500],[30,50]]", "EffSpec": "[2,50,30]",
        "LvDevilSkill": 0, "phanTramDevilSkill": 0,
        "char": "Marco Phượng Hoàng", "set_name": "Thánh Linh Thần Trang", "element": "Lam Hỏa / Bất Tử Điểu (Phoenix Blue Fire)", "color": "#29b6f6", "color2": "#ffd54f",
        "status_tag": "LAM HỎA BẤT TỬ", "art_prefix": "phuong_hoang_bat_tu_clean"
    },
    {
        "id": 4013, "id_index": 4013, "id_2": 4013, "icon": 213, "typeSkill": 1, "typeBuff": 0,
        "name": "Đại Phật Sóng Xung Kích", "typeEffSkill": 4013, "range": 220, "rangeLan": 140, "nTarget": 5,
        "damage": 5000, "manaLost": 50, "timeDelay": 3000, "nKick": 1,
        "info": "Tuyệt kỹ Huyết Long Thần Trang chưởng sóng xung kích uy lực vô song.",
        "Lv_RQ": 1, "percentLv": 0, "typeDevil": 0,
        "option": "[[1,350],[2,500],[10,350],[13,400],[28,10],[29,500]]", "EffSpec": "[1,55,30]",
        "LvDevilSkill": 0, "phanTramDevilSkill": 0,
        "char": "Sengoku Đại Phật", "set_name": "Huyết Long Thần Trang", "element": "Hoàng Kim / Đại Phật Chưởng (Golden Daibutsu)", "color": "#ffa000", "color2": "#fff176",
        "status_tag": "CHẤN ĐỘNG PHẬT QUANG", "art_prefix": "dai_phat_clean"
    },
    {
        "id": 4014, "id_index": 4014, "id_2": 4014, "icon": 214, "typeSkill": 1, "typeBuff": 0,
        "name": "Bát Quái Cửu Long Thiên", "typeEffSkill": 4014, "range": 220, "rangeLan": 140, "nTarget": 5,
        "damage": 5000, "manaLost": 50, "timeDelay": 3000, "nKick": 1,
        "info": "Tuyệt kỹ Ma Thần Thần Trang quét chùy sấm sét bách thú vô địch.",
        "Lv_RQ": 1, "percentLv": 0, "typeDevil": 0,
        "option": "[[1,350],[2,500],[10,350],[13,400],[28,10],[29,500]]", "EffSpec": "[1,50,30]",
        "LvDevilSkill": 0, "phanTramDevilSkill": 0,
        "char": "Kaido Bách Thú", "set_name": "Ma Thần Thần Trang", "element": "Lôi Long / Hắc Lôi Ma Thần (Nine Thunder Dragons)", "color": "#5e35b1", "color2": "#00e5ff",
        "status_tag": "HẮC LÔI TÊ LIỆT", "art_prefix": "bat_quai_cuu_long_clean"
    },
    {
        "id": 4015, "id_index": 4015, "id_2": 4015, "icon": 215, "typeSkill": 1, "typeBuff": 0,
        "name": "Long Trảo Viêm Long Toái Địa", "typeEffSkill": 4015, "range": 220, "rangeLan": 140, "nTarget": 5,
        "damage": 5000, "manaLost": 50, "timeDelay": 3000, "nKick": 1,
        "info": "Tuyệt kỹ Thiên Thần Thần Trang trảo rồng lửa phá hủy mặt đất.",
        "Lv_RQ": 1, "percentLv": 0, "typeDevil": 0,
        "option": "[[1,350],[5,150],[10,350],[13,400],[14,300],[29,500]]", "EffSpec": "[2,50,30]",
        "LvDevilSkill": 0, "phanTramDevilSkill": 0,
        "char": "Sabo Quân Cách Mạng", "set_name": "Thiên Thần Thần Trang", "element": "Hỏa Long / Long Trảo Toái Địa (Dragon Claw Flame)", "color": "#ff6d00", "color2": "#ffab00",
        "status_tag": "THIÊU RỤI ĐỊA CHẤN", "art_prefix": "long_trao_clean"
    },
    {
        "id": 4016, "id_index": 4016, "id_2": 4016, "icon": 216, "typeSkill": 1, "typeBuff": 0,
        "name": "Vận Thạch Thiên Giáng", "typeEffSkill": 4016, "range": 220, "rangeLan": 140, "nTarget": 5,
        "damage": 5000, "manaLost": 50, "timeDelay": 3000, "nKick": 1,
        "info": "Tuyệt kỹ Hỗn Độn Thần Trang kéo thiên thạch rơi tự do từ vũ trụ.",
        "Lv_RQ": 1, "percentLv": 0, "typeDevil": 0,
        "option": "[[1,350],[10,350],[13,400],[28,10],[29,500],[30,50]]", "EffSpec": "[1,50,30]",
        "LvDevilSkill": 0, "phanTramDevilSkill": 0,
        "char": "Fujitora (Issho)", "set_name": "Hỗn Độn Thần Trang", "element": "Trọng Lực / Thiên Thạch Vũ Trụ (Gravity Meteor)", "color": "#7c4dff", "color2": "#ea80fc",
        "status_tag": "ĐÈ NÉN TRỌNG LỰC", "art_prefix": "van_thach_thien_giang_clean"
    }
]

def main():
    # 1. Export JSON
    json_path = os.path.join(db_dir, "skills_database.json")
    with open(json_path, "w", encoding="utf-8") as f:
        json.dump(SKILLS, f, ensure_ascii=False, indent=2)
    print(f"[OK] Exported {len(SKILLS)} skills to {json_path}")

    # 2. Export SQL
    sql_path = os.path.join(db_dir, "insert_16_skills.sql")
    values = []
    for s in SKILLS:
        val = (
            f"({s['id']}, {s['id_index']}, {s['id_2']}, {s['icon']}, "
            f"{s['typeSkill']}, {s['typeBuff']}, '{s['name']}', {s['typeEffSkill']}, "
            f"{s['range']}, {s['rangeLan']}, {s['nTarget']}, {s['damage']}, "
            f"{s['manaLost']}, {s['timeDelay']}, {s['nKick']}, '{s['info']}', "
            f"{s['Lv_RQ']}, {s['percentLv']}, {s['typeDevil']}, '{s['option']}', "
            f"'{s['EffSpec']}', {s['LvDevilSkill']}, {s['phanTramDevilSkill']})"
        )
        values.append(val)

    sql_content = (
        "-- =====================================================================\n"
        "-- SQL INSERT: 16 SKILL THẦN TRANG CHỦ ĐỘNG (ACTIVE ATTACK SKILLS)\n"
        "-- Bảng dữ liệu chính thức game Hải Tặc Tí Hon\n"
        "-- =====================================================================\n\n"
        "DELETE FROM `skill` WHERE (`id` BETWEEN 4001 AND 4016 OR `id_index` BETWEEN 4001 AND 4016);\n\n"
        "INSERT INTO `skill` (`id`, `id_index`, `id_2`, `icon`, `typeSkill`, `typeBuff`, `name`, `typeEffSkill`, `range`, `rangeLan`, `nTarget`, `damage`, `manaLost`, `timeDelay`, `nKick`, `info`, `Lv_RQ`, `percentLv`, `typeDevil`, `option`, `EffSpec`, `LvDevilSkill`, `phanTramDevilSkill`) VALUES\n"
        + ",\n".join(values) + ";\n"
    )

    with open(sql_path, "w", encoding="utf-8") as f:
        f.write(sql_content)
    print(f"[OK] Exported SQL insert to {sql_path}")

    # Also copy this script into tools/init_database.py
    shutil.copy2(__file__, os.path.join(tools_dir, "init_database.py"))
    print("[OK] Copied script to tools/init_database.py")

if __name__ == '__main__':
    main()
