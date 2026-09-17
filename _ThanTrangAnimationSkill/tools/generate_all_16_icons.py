# -*- coding: utf-8 -*-
"""
generate_all_16_icons.py
========================
Generates 100% authentic circular alpha masked icons for all 16 Than Trang Skills.
Zero black border, anti-aliased circular alpha edge, crystal clear art.
Generates:
- Skill Zoom 4 (88x88), Zoom 3 (66x66), Zoom 2 (44x44), Zoom 1 (22x22)
- Mini Buff Zoom 4 (44x44), Zoom 3 (33x33), Zoom 2 (22x22), Zoom 1 (11x11)
Exports to both icon ID (201..216), skill ID (4001..4016), and extended (4201..4216).
"""

import os
import sys
import glob
import shutil
from PIL import Image

sys.stdout.reconfigure(encoding='utf-8')
sys.stderr.reconfigure(encoding='utf-8')

BASE_DIR = r"C:\DepLor\HTTH\Team\_ThanTrangAnimationSkill"
IMG_RES_DIR = os.path.join(BASE_DIR, "img_res")
MASTER_ART_DIR = os.path.join(IMG_RES_DIR, "master_art")
ICONS_DIR = os.path.join(BASE_DIR, "icons")
TOOLS_DIR = os.path.join(BASE_DIR, "tools")

MASK_PATH = os.path.join(IMG_RES_DIR, "authentic_circular_mask_88.png")

# 16 Skills mapping
SKILL_MAP = [
    {"id": 4001, "icon": 201, "key": "hoa_diem_than_quyen", "name": "Hỏa Diễm Thần Quyền", "prefix": "hoa_diem_than_quyen_clean"},
    {"id": 4002, "icon": 202, "key": "dai_phun_hoa_volcano", "name": "Đại Phún Hỏa Volcano", "prefix": "dai_phun_hoa_clean"},
    {"id": 4003, "icon": 203, "key": "ky_bang_ha_tuyet_doi", "name": "Kỷ Băng Hà Tuyệt Đối", "prefix": "ky_bang_ha_clean"},
    {"id": 4004, "icon": 204, "key": "bat_xich_quynh_khuc_ngoc", "name": "Bát Xích Quỳnh Khúc Ngọc", "prefix": "bat_xich_quynh_khuc_clean"},
    {"id": 4005, "icon": 205, "key": "hac_am_thon_phe", "name": "Hắc Ám Thôn Phệ Vô Tận", "prefix": "hac_am_clean"},
    {"id": 4006, "icon": 206, "key": "than_loi_200m_volt", "name": "200 Triệu Volt Thần Lôi", "prefix": "than_loi_200m_clean"},
    {"id": 4007, "icon": 207, "key": "hai_chan_toai_dia", "name": "Hải Chấn Toái Địa Cầu", "prefix": "hai_chan_toai_dia_clean"},
    {"id": 4008, "icon": 208, "key": "room_gamma_knife", "name": "ROOM Gamma Knife", "prefix": "room_gamma_knife_clean"},
    {"id": 4009, "icon": 209, "key": "tu_truong_dai_phao", "name": "Từ Trường Bộc Phá Đại Pháo", "prefix": "tu_truong_dai_phao_clean"},
    {"id": 4010, "icon": 210, "key": "cu_doc_venom", "name": "Cổ Độc Phán Quyết Venom", "prefix": "cu_doc_venom_clean"},
    {"id": 4011, "icon": 211, "key": "mui_ten_thach_hoa", "name": "Mũi Tên Mê Hoặc Thạch Hóa", "prefix": "mui_ten_thach_hoa_clean"},
    {"id": 4012, "icon": 212, "key": "phuong_hoang_bat_tu", "name": "Phượng Hoàng Bất Tử Bộc Phá", "prefix": "phuong_hoang_bat_tu_clean"},
    {"id": 4013, "icon": 213, "key": "dai_phat_xung_kich", "name": "Đại Phật Sóng Xung Kích", "prefix": "dai_phat_clean"},
    {"id": 4014, "icon": 214, "key": "bat_quai_cuu_long", "name": "Bát Quái Cửu Long Thiên", "prefix": "bat_quai_cuu_long_clean"},
    {"id": 4015, "icon": 215, "key": "long_trao_viem_long", "name": "Long Trảo Viêm Long Toái Địa", "prefix": "long_trao_clean"},
    {"id": 4016, "icon": 216, "key": "van_thach_thien_giang", "name": "Vận Thạch Thiên Giáng", "prefix": "van_thach_thien_giang_clean"},
]

def main():
    print("=" * 70)
    print("GENERATING 16 CIRCULAR ALPHA ICONS ACROSS ALL ZOOM LEVELS")
    print("=" * 70)

    # 1. Load mask
    if not os.path.exists(MASK_PATH):
        raise FileNotFoundError(f"Missing mask: {MASK_PATH}")
    mask_img = Image.open(MASK_PATH).convert("RGBA")
    alpha_mask_88 = mask_img.split()[3]
    print(f"[OK] Loaded authentic circular mask 88x88 from {MASK_PATH}")

    # 2. Setup output directories
    zoom_dirs = {
        "x4": os.path.join(ICONS_DIR, "x4"),
        "x3": os.path.join(ICONS_DIR, "x3"),
        "x2": os.path.join(ICONS_DIR, "x2"),
        "x1": os.path.join(ICONS_DIR, "x1"),
        "buff_x4": os.path.join(ICONS_DIR, "buff_x4"),
        "buff_x3": os.path.join(ICONS_DIR, "buff_x3"),
        "buff_x2": os.path.join(ICONS_DIR, "buff_x2"),
        "buff_x1": os.path.join(ICONS_DIR, "buff_x1"),
    }
    for d in zoom_dirs.values():
        os.makedirs(d, exist_ok=True)

    by_skill_base = os.path.join(ICONS_DIR, "by_skill")
    os.makedirs(by_skill_base, exist_ok=True)

    total_generated = 0

    for idx, item in enumerate(SKILL_MAP):
        sid = item["id"]
        icon_id = item["icon"]
        key = item["key"]
        name = item["name"]
        prefix = item["prefix"]

        # Locate clean source image
        matches = glob.glob(os.path.join(MASTER_ART_DIR, f"{prefix}_*.jpg"))
        if not matches:
            matches = glob.glob(os.path.join(MASTER_ART_DIR, f"*{prefix}*.jpg"))
        if not matches:
            matches = glob.glob(os.path.join(MASTER_ART_DIR, f"*{key}*.jpg"))
        if not matches:
            print(f"[ERR] Missing master art for {name} ({prefix})")
            continue

        src_path = sorted(matches)[-1]
        src_art = Image.open(src_path).convert("RGBA")

        # Square center crop
        w, h = src_art.size
        min_side = min(w, h)
        left = (w - min_side) // 2
        top = (h - min_side) // 2
        cropped = src_art.crop((left, top, left + min_side, top + min_side))

        # Resize to 88x88 and apply circular alpha mask
        icon_88 = cropped.resize((88, 88), Image.Resampling.LANCZOS)
        icon_88.putalpha(alpha_mask_88)

        # Generate skill zooms
        skill_zooms = {
            "x4": icon_88,
            "x3": icon_88.resize((66, 66), Image.Resampling.LANCZOS),
            "x2": icon_88.resize((44, 44), Image.Resampling.LANCZOS),
            "x1": icon_88.resize((22, 22), Image.Resampling.LANCZOS),
        }

        # Generate mini buff zooms
        buff_zooms = {
            "buff_x4": icon_88.resize((44, 44), Image.Resampling.LANCZOS),
            "buff_x3": icon_88.resize((33, 33), Image.Resampling.LANCZOS),
            "buff_x2": icon_88.resize((22, 22), Image.Resampling.LANCZOS),
            "buff_x1": icon_88.resize((11, 11), Image.Resampling.LANCZOS),
        }

        # Save into by_skill folder
        sk_folder = os.path.join(by_skill_base, f"{sid}_{key}")
        os.makedirs(sk_folder, exist_ok=True)
        shutil.copy2(src_path, os.path.join(sk_folder, "master_art.jpg"))
        for z_name, img in skill_zooms.items():
            img.save(os.path.join(sk_folder, f"skill_{z_name}.png"))
        for b_name, img in buff_zooms.items():
            img.save(os.path.join(sk_folder, f"mini_{b_name.replace('buff_', '')}.png"))

        # Save to categorized icon zoom directories
        # Scheme A: icon ID (201..216) & buff (701..716)
        # Scheme B: skill ID (4001..4016) & buff (4501..4516)
        # Scheme C: extended ID (4201..4216) & buff (4701..4716)
        id_triplets = [
            (icon_id, icon_id + 500),         # (201, 701)
            (sid, sid + 500),                 # (4001, 4501)
            (sid + 200, sid + 700),           # (4201, 4701)
        ]

        for z_key, img in skill_zooms.items():
            z_dir = zoom_dirs[z_key]
            for sk_id, _ in id_triplets:
                img.save(os.path.join(z_dir, f"{sk_id}.png"))
                total_generated += 1

        for b_key, img in buff_zooms.items():
            b_dir = zoom_dirs[b_key]
            for _, bf_id in id_triplets:
                img.save(os.path.join(b_dir, f"{bf_id}.png"))
                total_generated += 1

        print(f"[{idx+1:02d}/16] OK: {name} (Icon ID {icon_id} | Skill {sid}) -> 4 Zooms + 4 Buff Zooms generated")

    # Copy script into tools
    shutil.copy2(__file__, os.path.join(TOOLS_DIR, "generate_all_16_icons.py"))
    print(f"\n[SUCCESS] Generated {total_generated} icon PNG RGBA files across all zoom levels!")
    print(f"[OK] Saved script to {os.path.join(TOOLS_DIR, 'generate_all_16_icons.py')}")

if __name__ == '__main__':
    main()
