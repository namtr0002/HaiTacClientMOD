# -*- coding: utf-8 -*-
"""
build_16_skills_vfx_pipeline.py
===============================
Complete VFX & Animation Spritesheet Pipeline for ALL 16 Than Trang Skills.
Builds the 6 standard VFX components for each skill:
- cast (tụ lực)
- projectile (đạn đạo / sóng kình)
- impact (va chạm / nổ điểm)
- aura (vòng chân thân / hào quang)
- particles (tàn tích / mảnh vỡ / tia lửa)
- finisher (đại chiêu dứt điểm / AOE)

Outputs:
- spritesheets/ (Standard 1x resolution)
- spritesheets_x2/ (HD 2x resolution)
- frames/{id}/{component}/ (Individual PNG frames)
- database/skill_vfx_metadata.json (Metadata for all 16 skills)
"""

import os
import sys
import glob
import math
import shutil
import json
import numpy as np
from PIL import Image, ImageDraw, ImageFilter, ImageOps, ImageEnhance

sys.stdout.reconfigure(encoding='utf-8')
sys.stderr.reconfigure(encoding='utf-8')

BASE_DIR = r"C:\DepLor\HTTH\Team\_ThanTrangAnimationSkill"
IMG_RES_DIR = os.path.join(BASE_DIR, "img_res")
MASTER_ART_DIR = os.path.join(IMG_RES_DIR, "master_art")
STRIPS_DIR = os.path.join(IMG_RES_DIR, "reference_strips")
GIFS_DIR = os.path.join(IMG_RES_DIR, "previews_gif")

SHEETS_DIR = os.path.join(BASE_DIR, "spritesheets")
SHEETS_X2_DIR = os.path.join(BASE_DIR, "spritesheets_x2")
FRAMES_DIR = os.path.join(BASE_DIR, "frames")
DB_DIR = os.path.join(BASE_DIR, "database")
TOOLS_DIR = os.path.join(BASE_DIR, "tools")

for d in [SHEETS_DIR, SHEETS_X2_DIR, FRAMES_DIR, DB_DIR, TOOLS_DIR]:
    os.makedirs(d, exist_ok=True)

# -------------------------------------------------------------------------
# Image Processing & Alpha Extraction Utilities
# -------------------------------------------------------------------------
def vfx_alpha(img_rgb: Image.Image, black_th=12, power=0.85, boost=1.25) -> Image.Image:
    """Luminance-based alpha extraction with contrast enhancement."""
    arr = np.array(img_rgb).astype(np.float32)
    arr = np.clip(arr * boost, 0, 255)
    max_val = np.max(arr, axis=2)
    alpha = np.clip((max_val - black_th) / (255.0 - black_th), 0.0, 1.0)
    alpha = np.power(alpha, power) * 255.0
    norm_a = np.maximum(alpha / 255.0, 1e-3)[:, :, np.newaxis]
    unpremult = np.clip(arr / norm_a, 0, 255)
    return Image.fromarray(np.dstack((unpremult.astype(np.uint8), alpha.astype(np.uint8))), 'RGBA')

def sanitize_frame(canvas_rgba: Image.Image) -> Image.Image:
    """Zeroes outer 1px perimeter and softens 2nd pixel to prevent sprite sheet bleeding."""
    arr = np.array(canvas_rgba)
    arr[0, :, 3] = 0; arr[-1:, :, 3] = 0; arr[:, 0, 3] = 0; arr[:, -1:, 3] = 0
    if arr.shape[0] > 4 and arr.shape[1] > 4:
        arr[1, :, 3] = (arr[1, :, 3].astype(np.float32) * 0.4).astype(np.uint8)
        arr[-2, :, 3] = (arr[-2, :, 3].astype(np.float32) * 0.4).astype(np.uint8)
        arr[:, 1, 3] = (arr[:, 1, 3].astype(np.float32) * 0.4).astype(np.uint8)
        arr[:, -2, 3] = (arr[:, -2, 3].astype(np.float32) * 0.4).astype(np.uint8)
    return Image.fromarray(arr, "RGBA")

def fit_canvas(rgba: Image.Image, tw: int, th: int, anchor="center", pad=3) -> Image.Image:
    """Fits an RGBA sprite into a standardized canvas size with correct anchor."""
    bbox = rgba.getbbox()
    if not bbox:
        return Image.new("RGBA", (tw, th), (0, 0, 0, 0))
    crp = rgba.crop(bbox)
    cw, ch = crp.size
    sc = min((tw - pad * 2) / float(cw), (th - pad * 2) / float(ch), 1.0)
    if sc < 1.0:
        crp = crp.resize((max(1, int(cw * sc)), max(1, int(ch * sc))), Image.LANCZOS)
        cw, ch = crp.size
    res = Image.new("RGBA", (tw, th), (0, 0, 0, 0))
    dx = (tw - cw) // 2
    dy = th - ch - pad if anchor == "bottom" else (th - ch) // 2
    res.paste(crp, (max(0, dx), max(0, dy)), crp)
    return sanitize_frame(res)

def feather_cell_edges(cell_rgba: Image.Image, feather_px=6) -> Image.Image:
    """Feathers the outer border of a cropped cell so hard cut borders dissolve smoothly."""
    arr = np.array(cell_rgba).astype(np.float32)
    h, w, _ = arr.shape
    for p in range(min(feather_px, h // 2, w // 2)):
        factor = float(p) / float(feather_px)
        arr[p, :, 3] *= factor
        arr[h - 1 - p, :, 3] *= factor
        arr[:, p, 3] *= factor
        arr[:, w - 1 - p, 3] *= factor
    return Image.fromarray(arr.astype(np.uint8), "RGBA")

# -------------------------------------------------------------------------
# Dynamic Procedural Sequence Generators from Base VFX
# -------------------------------------------------------------------------
def generate_aura_sequence(base_rgba: Image.Image, n_frames=6, tw=120, th=100) -> list:
    bbox = base_rgba.getbbox()
    crp = base_rgba.crop(bbox) if bbox else base_rgba
    cw, ch = crp.size
    base_sc = min((tw - 8) / float(max(1, cw)), (th - 8) / float(max(1, ch)))
    frames = []
    for i in range(n_frames):
        t = i / float(n_frames)
        pulse = 0.92 + 0.08 * math.sin(t * math.pi * 2.0)
        sw = max(4, int(cw * base_sc * pulse))
        sh = max(4, int(ch * base_sc * pulse))
        scaled = crp.resize((sw, sh), Image.LANCZOS)
        arr = np.array(scaled).astype(np.float32)
        alpha_mod = 0.85 + 0.15 * math.sin(t * math.pi * 2.0 + math.pi/4)
        arr[:, :, 3] = np.clip(arr[:, :, 3] * alpha_mod, 0, 255)
        arr[:, :, :3] = np.clip(arr[:, :, :3] * (0.95 + 0.15 * math.sin(t * math.pi * 2.0)), 0, 255)
        f_img = Image.fromarray(arr.astype(np.uint8), 'RGBA')
        res = Image.new('RGBA', (tw, th), (0,0,0,0))
        res.paste(f_img, ((tw - sw) // 2, th - sh - 4), f_img)
        frames.append(sanitize_frame(res))
    return frames

def generate_projectile_sequence(base_rgba: Image.Image, n_frames=6, tw=180, th=80, flip_x=False) -> list:
    if flip_x:
        base_rgba = base_rgba.transpose(Image.FLIP_LEFT_RIGHT)
    bbox = base_rgba.getbbox()
    crp = base_rgba.crop(bbox) if bbox else base_rgba
    cw, ch = crp.size
    base_sc = min((tw - 10) / float(max(1, cw)), (th - 10) / float(max(1, ch)))
    frames = []
    scales = [0.84, 0.92, 1.00, 1.05, 1.00, 0.96]
    offsets_x = [-10, -5, 0, 3, 6, 10]
    boosts = [0.90, 1.00, 1.15, 1.30, 1.15, 1.05]
    for i in range(n_frames):
        idx = i % len(scales)
        s = scales[idx] * base_sc
        sw = max(4, int(cw * s))
        sh = max(4, int(ch * s))
        scaled = crp.resize((sw, sh), Image.LANCZOS)
        arr = np.array(scaled).astype(np.float32)
        arr[:, :, :3] = np.clip(arr[:, :, :3] * boosts[idx], 0, 255)
        f_img = Image.fromarray(arr.astype(np.uint8), 'RGBA')
        res = Image.new('RGBA', (tw, th), (0,0,0,0))
        dx = (tw - sw) // 2 + offsets_x[idx]
        dy = (th - sh) // 2
        res.paste(f_img, (max(0, dx), max(0, dy)), f_img)
        frames.append(sanitize_frame(res))
    return frames

def generate_impact_sequence(base_rgba: Image.Image, n_frames=8, tw=240, th=240) -> list:
    bbox = base_rgba.getbbox()
    crp = base_rgba.crop(bbox) if bbox else base_rgba
    cw, ch = crp.size
    base_sc = min((tw - 12) / float(max(1, cw)), (th - 12) / float(max(1, ch)))
    frames = []
    timeline = [
        (0.25, 0.70, 1.40, 2),
        (0.55, 0.90, 1.30, 1),
        (0.85, 1.00, 1.25, 0),
        (1.05, 1.00, 1.40, 0),  # PEAK
        (1.10, 0.85, 1.20, 0),
        (1.12, 0.65, 1.00, 1),
        (1.15, 0.40, 0.85, 2),
        (1.18, 0.18, 0.70, 3),
    ]
    for i in range(n_frames):
        idx = int(i / float(n_frames) * len(timeline))
        idx = min(idx, len(timeline) - 1)
        sc, alpha, bst, blur = timeline[idx]
        s = sc * base_sc
        sw = max(4, int(cw * s))
        sh = max(4, int(ch * s))
        scaled = crp.resize((sw, sh), Image.LANCZOS)
        if blur > 0:
            scaled = scaled.filter(ImageFilter.GaussianBlur(blur))
        arr = np.array(scaled).astype(np.float32)
        arr[:, :, :3] = np.clip(arr[:, :, :3] * bst, 0, 255)
        arr[:, :, 3] = np.clip(arr[:, :, 3] * alpha, 0, 255)
        f_img = Image.fromarray(arr.astype(np.uint8), 'RGBA')
        res = Image.new('RGBA', (tw, th), (0,0,0,0))
        dx = (tw - sw) // 2
        dy = (th - sh) // 2
        res.paste(f_img, (max(0, dx), max(0, dy)), f_img)
        frames.append(sanitize_frame(res))
    return frames

def generate_particles_sequence(base_rgba: Image.Image, n_frames=8, tw=90, th=140) -> list:
    bbox = base_rgba.getbbox()
    crp = base_rgba.crop(bbox) if bbox else base_rgba
    cw, ch = crp.size
    base_sc = min((tw - 8) / float(max(1, cw)), (th - 8) / float(max(1, ch)))
    frames = []
    timeline = [
        (0.35, 0.80, 0.30, 1.3),
        (0.65, 0.95, 0.15, 1.2),
        (0.90, 1.00, 0.05, 1.2),
        (1.02, 0.95, 0.00, 1.1),
        (1.05, 0.80, -0.05, 1.0),
        (1.08, 0.60, -0.10, 0.9),
        (1.10, 0.40, -0.15, 0.8),
        (1.12, 0.18, -0.20, 0.7),
    ]
    for i in range(n_frames):
        idx = int(i / float(n_frames) * len(timeline))
        idx = min(idx, len(timeline) - 1)
        sc, alpha, dy_f, bst = timeline[idx]
        s = sc * base_sc
        sw = max(4, int(cw * s))
        sh = max(4, int(ch * s))
        scaled = crp.resize((sw, sh), Image.LANCZOS)
        arr = np.array(scaled).astype(np.float32)
        arr[:, :, :3] = np.clip(arr[:, :, :3] * bst, 0, 255)
        arr[:, :, 3] = np.clip(arr[:, :, 3] * alpha, 0, 255)
        f_img = Image.fromarray(arr.astype(np.uint8), 'RGBA')
        res = Image.new('RGBA', (tw, th), (0,0,0,0))
        dx = (tw - sw) // 2
        dy = int(th - sh - 6 + dy_f * th)
        res.paste(f_img, (max(0, dx), max(0, min(th - sh, dy))), f_img)
        frames.append(sanitize_frame(res))
    return frames

def generate_finisher_sequence(base_rgba: Image.Image, n_frames=8, tw=260, th=160) -> list:
    bbox = base_rgba.getbbox()
    crp = base_rgba.crop(bbox) if bbox else base_rgba
    cw, ch = crp.size
    base_sc = min((tw - 10) / float(max(1, cw)), (th - 10) / float(max(1, ch)))
    frames = []
    timeline = [
        (0.50, 0.20, 0.60, 1.30, 0.35),
        (0.75, 0.45, 0.85, 1.25, 0.20),
        (0.92, 0.80, 0.95, 1.20, 0.08),
        (1.02, 1.05, 1.00, 1.35, 0.00),  # PEAK
        (1.00, 1.00, 0.95, 1.15, 0.00),
        (0.98, 0.96, 0.80, 1.05, 0.00),
        (0.96, 0.90, 0.55, 0.90, 0.00),
        (0.94, 0.85, 0.25, 0.80, 0.00),
    ]
    for i in range(n_frames):
        idx = int(i / float(n_frames) * len(timeline))
        idx = min(idx, len(timeline) - 1)
        sx, sy, alpha, bst, dy_off = timeline[idx]
        sw = max(4, int(cw * base_sc * sx))
        sh = max(4, int(ch * base_sc * sy))
        scaled = crp.resize((sw, sh), Image.LANCZOS)
        arr = np.array(scaled).astype(np.float32)
        arr[:, :, :3] = np.clip(arr[:, :, :3] * bst, 0, 255)
        arr[:, :, 3] = np.clip(arr[:, :, 3] * alpha, 0, 255)
        f_img = Image.fromarray(arr.astype(np.uint8), 'RGBA')
        res = Image.new('RGBA', (tw, th), (0,0,0,0))
        dx = (tw - sw) // 2
        dy = int(th - sh - 4 + dy_off * th)
        res.paste(f_img, (max(0, dx), max(0, min(th - sh, dy))), f_img)
        frames.append(sanitize_frame(res))
    return frames

def generate_cast_sequence(base_rgba: Image.Image, n_frames=6, tw=240, th=180) -> list:
    bbox = base_rgba.getbbox()
    crp = base_rgba.crop(bbox) if bbox else base_rgba
    cw, ch = crp.size
    base_sc = min((tw - 10) / float(max(1, cw)), (th - 10) / float(max(1, ch)))
    frames = []
    for i in range(n_frames):
        t = i / float(n_frames)
        sc = base_sc * (0.85 + 0.15 * t)
        sw = max(4, int(cw * sc))
        sh = max(4, int(ch * sc))
        scaled = crp.resize((sw, sh), Image.LANCZOS)
        arr = np.array(scaled).astype(np.float32)
        arr[:, :, 3] = np.clip(arr[:, :, 3] * (0.5 + 0.5 * t), 0, 255)
        arr[:, :, :3] = np.clip(arr[:, :, :3] * (0.9 + 0.4 * t), 0, 255)
        f_img = Image.fromarray(arr.astype(np.uint8), 'RGBA')
        res = Image.new('RGBA', (tw, th), (0,0,0,0))
        dx = (tw - sw) // 2
        dy = th - sh - 4
        res.paste(f_img, (max(0, dx), max(0, dy)), f_img)
        frames.append(sanitize_frame(res))
    return frames

# -------------------------------------------------------------------------
# Exporter
# -------------------------------------------------------------------------
ALL_METADATA = {}

def export_component(sid: int, comp: str, frames: list, target_w: int, target_h: int, anchor="center"):
    n = len(frames)
    comp_dir = os.path.join(FRAMES_DIR, str(sid), comp)
    os.makedirs(comp_dir, exist_ok=True)

    sheet = Image.new("RGBA", (target_w, target_h * n), (0, 0, 0, 0))
    for i, f in enumerate(frames):
        f.save(os.path.join(comp_dir, f"frame_{i}.png"), "PNG")
        sheet.paste(f, (0, i * target_h))

    out_x1 = os.path.join(SHEETS_DIR, f"skill_{sid}_{comp}_sheet.png")
    sheet.save(out_x1, "PNG")

    out_x2 = os.path.join(SHEETS_X2_DIR, f"skill_{sid}_{comp}_sheet.png")
    sheet_x2 = sheet.resize((target_w * 2, target_h * n * 2), Image.NEAREST)
    sheet_x2.save(out_x2, "PNG")

    if str(sid) not in ALL_METADATA:
        ALL_METADATA[str(sid)] = {}

    ALL_METADATA[str(sid)][comp] = {
        "w": target_w,
        "h": target_h,
        "frames": n,
        "anchor": anchor,
        "sheet_x1": f"../spritesheets/skill_{sid}_{comp}_sheet.png",
        "sheet_x2": f"../spritesheets_x2/skill_{sid}_{comp}_sheet.png"
    }

def find_file(directory: str, patterns: list) -> str:
    for pat in patterns:
        matches = glob.glob(os.path.join(directory, pat))
        if matches:
            return sorted(matches)[-1]
    return None

# -------------------------------------------------------------------------
# Main Pipeline for All 16 Skills
# -------------------------------------------------------------------------
def main():
    print("=" * 70)
    print("BUILDING COMPLETE VFX SPRITESHEETS FOR ALL 16 THAN TRANG SKILLS")
    print("=" * 70)

    # 1. Copy pre-built 4013 if exists in TaiLieuHaiTac
    src_4013_sheets = r"C:\DepLor\HTTH\Team\TaiLieuHaiTac\SkillThanTrangNew\spritesheets"
    if os.path.exists(src_4013_sheets):
        for f in os.listdir(src_4013_sheets):
            if f.startswith("skill_4013_"):
                shutil.copy2(os.path.join(src_4013_sheets, f), os.path.join(SHEETS_DIR, f))
    src_4013_x2 = r"C:\DepLor\HTTH\Team\TaiLieuHaiTac\SkillThanTrangNew\spritesheets_x2"
    if os.path.exists(src_4013_x2):
        for f in os.listdir(src_4013_x2):
            if f.startswith("skill_4013_"):
                shutil.copy2(os.path.join(src_4013_x2, f), os.path.join(SHEETS_X2_DIR, f))
    src_4013_frames = r"C:\DepLor\HTTH\Team\TaiLieuHaiTac\SkillThanTrangNew\frames\4013"
    if os.path.exists(src_4013_frames):
        dst_4013_frames = os.path.join(FRAMES_DIR, "4013")
        if not os.path.exists(dst_4013_frames):
            shutil.copytree(src_4013_frames, dst_4013_frames)
    print("[OK] Synchronized Masterpiece 4013 Sengoku assets")

    # Read existing metadata if present
    meta_src = r"C:\DepLor\HTTH\Team\TaiLieuHaiTac\SkillThanTrangNew\skill_vfx_metadata.json"
    if os.path.exists(meta_src):
        with open(meta_src, "r", encoding="utf-8") as f:
            base_meta = json.load(f)
            for k, v in base_meta.items():
                ALL_METADATA[k] = v

    # 16 Skills Definitions
    skills_info = [
        (4001, "Hỏa Diễm Thần Quyền", "hoa_diem_than_quyen_clean", ["*fire_fist*", "*anim_4001*", "*hoa_diem*"]),
        (4002, "Đại Phún Hỏa Volcano", "dai_phun_hoa_clean", ["*4002*", "*magma*", "*dai_phun*"]),
        (4003, "Kỷ Băng Hà Tuyệt Đối", "ky_bang_ha_clean", ["*4003*", "*ice*", "*ky_bang*"]),
        (4004, "Bát Xích Quỳnh Khúc Ngọc", "bat_xich_quynh_khuc_clean", ["*4004*"]),
        (4005, "Hắc Ám Thôn Phệ Vô Tận", "hac_am_clean", ["*4005*", "*darkness*", "*hac_am*"]),
        (4006, "200 Triệu Volt Thần Lôi", "than_loi_200m_clean", ["*enel*", "*4006*", "*lightning*"]),
        (4007, "Hải Chấn Toái Địa Cầu", "hai_chan_toai_dia_clean", ["*4007*", "*quake*", "*hai_chan*", "*Whitebeard*"]),
        (4008, "ROOM Gamma Knife", "room_gamma_knife_clean", ["*4008*", "*gamma*", "*room*"]),
        (4009, "Từ Trường Bộc Phá Đại Pháo", "tu_truong_dai_phao_clean", ["*4009*", "*magnetic*", "*tu_truong*"]),
        (4010, "Cổ Độc Phán Quyết Venom", "cu_doc_venom_clean", ["*4010*", "*venom*"]),
        (4011, "Mũi Tên Mê Hoặc Thạch Hóa", "mui_ten_thach_hoa_clean", ["*4011*", "*heart_arrow*"]),
        (4012, "Phượng Hoàng Bất Tử Bộc Phá", "phuong_hoang_bat_tu_clean", ["*4012*", "*phoenix*", "*phuong_hoang*"]),
        (4013, "Đại Phật Sóng Xung Kích", "dai_phat_clean", ["*4013*", "*buddha*"]),
        (4014, "Bát Quái Cửu Long Thiên", "bat_quai_cuu_long_clean", ["*4014*", "*dragon_hassaikai*"]),
        (4015, "Long Trảo Viêm Long Toái Địa", "long_trao_clean", ["*4015*", "*dragon_claw*"]),
        (4016, "Vận Thạch Thiên Giáng", "van_thach_thien_giang_clean", ["*4016*", "*meteor*"]),
    ]

    for sid, name, art_prefix, ref_patterns in skills_info:
        # If already fully exported in 4013, keep metadata
        if sid == 4013 and "4013" in ALL_METADATA:
            print(f"[OK] Skill 4013: {name} already has Masterpiece 72 frames")
            continue

        print(f"\n>> Processing Skill {sid}: {name}...")

        # 1. Locate Master Art
        m_art_path = find_file(MASTER_ART_DIR, [f"{art_prefix}*.jpg", f"*{art_prefix}*.jpg"])
        if not m_art_path:
            m_art_path = find_file(MASTER_ART_DIR, [f"*{sid}*.jpg"])

        master_rgba = None
        if m_art_path:
            src_m = Image.open(m_art_path).convert("RGB")
            master_rgba = vfx_alpha(src_m, black_th=15, boost=1.3)

        # 2. Check for component strips in STRIPS_DIR
        cast_strip = find_file(STRIPS_DIR, [f"{sid}_cast_strip*.jpg", f"*anim_{sid}_f1*.jpg", f"*anim_{sid}_prepare*.jpg", f"*enel_colossal*.jpg", f"*fire_fist_sheet*.jpg"])
        proj_strip = find_file(STRIPS_DIR, [f"{sid}_projectile_strip*.jpg", f"*anim_{sid}_f2*.jpg", f"*anim_{sid}*proj*.jpg", f"*enel_bolt_column*.jpg", f"*fire_fist_fx*.jpg"])
        imp_strip  = find_file(STRIPS_DIR, [f"{sid}_impact_*.jpg", f"*anim_{sid}_f4*.jpg", f"*anim_{sid}_f5*.jpg", f"*anim_{sid}*impact*.jpg", f"*enel_flash*.jpg"])
        all_ref    = find_file(STRIPS_DIR, [f"{sid}_all_components*.jpg", f"*{sid}*comp*.jpg", f"*{sid}*ref*.jpg"])

        # Base elements
        base_cast = None
        base_proj = None
        base_imp = None
        base_aura = None
        base_part = None
        base_fin = None

        # Try to slice from all_components if available
        if all_ref:
            try:
                im_all = Image.open(all_ref).convert("RGB")
                w_a, h_a = im_all.size
                pw, ph = w_a // 2, h_a // 3
                # Panel 0: Cast, 1: Proj, 2: Impact, 3: Part, 4: Aura, 5: Finisher
                p0 = im_all.crop((0, 0, pw, ph))
                p1 = im_all.crop((pw, 0, w_a, ph))
                p2 = im_all.crop((0, ph, pw, ph * 2))
                p3 = im_all.crop((pw, ph, w_a, ph * 2))
                p4 = im_all.crop((0, ph * 2, pw, h_a))
                p5 = im_all.crop((pw, ph * 2, w_a, h_a))

                base_cast = vfx_alpha(p0, black_th=12, boost=1.3)
                base_proj = vfx_alpha(p1, black_th=12, boost=1.3)
                base_imp  = vfx_alpha(p2, black_th=12, boost=1.3)
                base_part = vfx_alpha(p3, black_th=12, boost=1.3)
                base_aura = vfx_alpha(p4, black_th=12, boost=1.3)
                base_fin  = vfx_alpha(p5, black_th=12, boost=1.3)
            except Exception as e:
                print(f"  [WARN] Failed to slice all_ref for {sid}: {e}")

        # Fallback to master art or specific strips
        if base_cast is None:
            if cast_strip:
                im_cs = Image.open(cast_strip).convert("RGB")
                base_cast = vfx_alpha(im_cs, black_th=12, boost=1.3)
            elif master_rgba:
                base_cast = master_rgba

        if base_proj is None:
            if proj_strip:
                im_ps = Image.open(proj_strip).convert("RGB")
                base_proj = vfx_alpha(im_ps, black_th=12, boost=1.3)
            elif master_rgba:
                base_proj = master_rgba

        if base_imp is None:
            if imp_strip:
                im_is = Image.open(imp_strip).convert("RGB")
                base_imp = vfx_alpha(im_is, black_th=12, boost=1.3)
            elif master_rgba:
                base_imp = master_rgba

        if base_aura is None:
            base_aura = base_cast if base_cast else master_rgba
        if base_part is None:
            base_part = base_imp if base_imp else master_rgba
        if base_fin is None:
            base_fin = base_imp if base_imp else master_rgba

        # Fallback safeguard: if anything is still None, create colored placeholder
        if base_cast is None:
            base_cast = Image.new("RGBA", (200, 200), (255, 200, 0, 200))
        if base_proj is None:
            base_proj = base_cast
        if base_imp is None:
            base_imp = base_cast
        if base_aura is None:
            base_aura = base_cast
        if base_part is None:
            base_part = base_cast
        if base_fin is None:
            base_fin = base_imp

        # Check if we have pre-existing cast strip with multiple frames
        cast_frames = []
        if cast_strip and "strip_6f" in cast_strip:
            try:
                im_cs = Image.open(cast_strip).convert("RGB")
                h_cs = im_cs.height
                for fi in range(6):
                    y0 = int(fi * h_cs / 6) + 4
                    y1 = int((fi + 1) * h_cs / 6) - 4
                    c_cell = im_cs.crop((8, y0, im_cs.width - 8, y1))
                    cast_frames.append(fit_canvas(vfx_alpha(c_cell), 240, 180, anchor="bottom"))
            except Exception:
                cast_frames = []

        if not cast_frames:
            cast_frames = generate_cast_sequence(base_cast, n_frames=6, tw=240, th=180)

        # Check if we have pre-existing proj strip
        proj_frames = []
        if proj_strip and "strip_6f" in proj_strip:
            try:
                im_ps = Image.open(proj_strip).convert("RGB")
                h_ps = im_ps.height
                for fi in range(6):
                    y0 = int(fi * h_ps / 6) + 4
                    y1 = int((fi + 1) * h_ps / 6) - 4
                    p_cell = im_ps.crop((8, y0, im_ps.width - 8, y1))
                    proj_frames.append(fit_canvas(vfx_alpha(p_cell), 180, 80, anchor="center"))
            except Exception:
                proj_frames = []

        if not proj_frames:
            proj_frames = generate_projectile_sequence(base_proj, n_frames=6, tw=180, th=80)

        # Check if we have pre-existing impact strip
        imp_frames = []
        if imp_strip and "strip_8f" in imp_strip:
            try:
                im_is = Image.open(imp_strip).convert("RGB")
                h_is = im_is.height
                for fi in range(8):
                    y0 = int(fi * h_is / 8) + 2
                    y1 = int((fi + 1) * h_is / 8) - 2
                    i_cell = feather_cell_edges(vfx_alpha(im_is.crop((8, y0, im_is.width - 8, y1))), feather_px=6)
                    imp_frames.append(fit_canvas(i_cell, 240, 240, anchor="center"))
            except Exception:
                imp_frames = []

        if not imp_frames:
            imp_frames = generate_impact_sequence(base_imp, n_frames=8, tw=240, th=240)

        # Procedural aura, particles, finisher
        aura_frames = generate_aura_sequence(base_aura, n_frames=6, tw=120, th=100)
        part_frames = generate_particles_sequence(base_part, n_frames=8, tw=90, th=140)
        fin_frames  = generate_finisher_sequence(base_fin, n_frames=8, tw=260, th=160)

        # Export all 6 components
        export_component(sid, "cast", cast_frames, 240, 180, anchor="bottom")
        export_component(sid, "projectile", proj_frames, 180, 80, anchor="center")
        export_component(sid, "impact", imp_frames, 240, 240, anchor="center")
        export_component(sid, "aura", aura_frames, 120, 100, anchor="bottom")
        export_component(sid, "particles", part_frames, 90, 140, anchor="bottom")
        export_component(sid, "finisher", fin_frames, 260, 160, anchor="bottom")
        print(f"  [OK] Exported 6 components for Skill {sid} ({name})")

    # Save complete metadata JSON
    meta_json_path = os.path.join(DB_DIR, "skill_vfx_metadata.json")
    with open(meta_json_path, "w", encoding="utf-8") as f:
        json.dump(ALL_METADATA, f, indent=2)
    print(f"\n[OK] Successfully saved metadata for all {len(ALL_METADATA)} skills to {meta_json_path}")

    # Copy script to tools
    shutil.copy2(__file__, os.path.join(TOOLS_DIR, "build_16_skills_vfx_pipeline.py"))
    print(f"[OK] Saved script to {os.path.join(TOOLS_DIR, 'build_16_skills_vfx_pipeline.py')}")

if __name__ == '__main__':
    main()
