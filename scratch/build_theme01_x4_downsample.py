import os
import cv2
import numpy as np
from PIL import Image, ImageFilter, ImageEnhance, ImageOps

ARTIFACT_DIR = r"C:\Users\DELL\.gemini\antigravity\brain\41d9f09e-d895-47a0-817a-a101cd061233"

OUTPUT_DIRS = [
    (r"C:\DepLor\HTTH\Team\HaiTacTiHonServer\data\datafromserver\x1\theme01", 1),
    (r"C:\DepLor\HTTH\Team\HaiTacTiHonServer\data\datafromserver\x2\theme01", 2),
    (r"C:\DepLor\HTTH\Team\HaiTacTiHonServer\data\datafromserver\x3\theme01", 3),
    (r"C:\DepLor\HTTH\Team\HaiTacTiHonServer\data\datafromserver\x4\theme01", 4),
    (r"C:\DepLor\HTTH\Team\ProjectUnity129\Assets\Resources\res\x1\theme01", 1),
    (r"C:\DepLor\HTTH\Team\ProjectUnity129\Assets\Resources\res\x2\theme01", 2),
    (r"C:\DepLor\HTTH\Team\ProjectUnity129\Assets\Resources\res\x3\theme01", 3),
    (r"C:\DepLor\HTTH\Team\ProjectUnity129\Assets\Resources\res\x4\theme01", 4),
    (r"C:\DepLor\HTTH\Team\ProjectJ2me129\res\x1\theme01", 1),
    (r"C:\DepLor\HTTH\Team\ProjectJ2me129\src\x1\theme01", 1),
]

def ensure_dir(path):
    os.makedirs(path, exist_ok=True)

def remove_white_bg(img_path, tol=14, feather=1.2):
    img = cv2.imread(img_path, cv2.IMREAD_UNCHANGED)
    if img is None:
        raise ValueError(f"Could not open {img_path}")
    bgr = img[:, :, :3] if img.shape[2] >= 3 else cv2.cvtColor(img, cv2.COLOR_GRAY2BGR)
    
    diff = 255 - bgr
    dist = np.max(diff, axis=2)
    
    low_thresh = tol
    high_thresh = tol + 25
    alpha = np.clip((dist.astype(np.float32) - low_thresh) / (high_thresh - low_thresh), 0.0, 1.0)
    
    bg_seed_mask = (dist < high_thresh).astype(np.uint8)
    h_img, w_img = bg_seed_mask.shape
    flood_mask = np.zeros((h_img + 2, w_img + 2), np.uint8)
    
    # Flood-fill outer boundaries from corners & borders
    cv2.floodFill(bg_seed_mask, flood_mask, (0, 0), 2)
    cv2.floodFill(bg_seed_mask, flood_mask, (w_img - 1, 0), 2)
    cv2.floodFill(bg_seed_mask, flood_mask, (0, h_img - 1), 2)
    cv2.floodFill(bg_seed_mask, flood_mask, (w_img - 1, h_img - 1), 2)
    for x in range(0, w_img, 40):
        if bg_seed_mask[0, x] == 1:
            cv2.floodFill(bg_seed_mask, flood_mask, (x, 0), 2)
        if bg_seed_mask[h_img - 1, x] == 1:
            cv2.floodFill(bg_seed_mask, flood_mask, (x, h_img - 1), 2)
    for y in range(0, h_img, 40):
        if bg_seed_mask[y, 0] == 1:
            cv2.floodFill(bg_seed_mask, flood_mask, (0, y), 2)
        if bg_seed_mask[y, w_img - 1] == 1:
            cv2.floodFill(bg_seed_mask, flood_mask, (w_img - 1, y), 2)
            
    is_outer_bg = (bg_seed_mask == 2)
    final_alpha = np.where(is_outer_bg, alpha, 1.0)
    final_alpha_u8 = (final_alpha * 255).astype(np.uint8)
    
    if feather > 0:
        blurred = cv2.GaussianBlur(final_alpha_u8, (5, 5), feather)
        final_alpha_u8 = np.where(is_outer_bg, blurred, 255)
    
    rgba = np.dstack([bgr, final_alpha_u8])
    return Image.fromarray(cv2.cvtColor(rgba, cv2.COLOR_BGRA2RGBA))

def crop_transparent(im):
    bbox = im.getbbox()
    return im.crop(bbox) if bbox else im

def im_sharpen(im, radius=0.8, percent=120):
    return im.filter(ImageFilter.UnsharpMask(radius=radius, percent=percent, threshold=1))

def save_master_x4(subpath, img_x4, w_x1, h_x1):
    """
    Takes a master image at x4 resolution (w_x1*4, h_x1*4),
    and downsamples it via Lanczos supersampling to x3, x2, x1.
    Saves to all target directories.
    """
    w_x4, h_x4 = w_x1 * 4, h_x1 * 4
    if img_x4.size != (w_x4, h_x4):
        img_x4 = img_x4.resize((w_x4, h_x4), Image.Resampling.LANCZOS)
    
    # Master x4: subtle crispness
    img_x4_sharp = im_sharpen(img_x4, radius=1.0, percent=110)
    
    # x3 downsample from x4
    img_x3 = img_x4.resize((w_x1 * 3, h_x1 * 3), Image.Resampling.LANCZOS)
    img_x3_sharp = im_sharpen(img_x3, radius=0.8, percent=115)
    
    # x2 downsample from x4
    img_x2 = img_x4.resize((w_x1 * 2, h_x1 * 2), Image.Resampling.LANCZOS)
    img_x2_sharp = im_sharpen(img_x2, radius=0.7, percent=120)
    
    # x1 downsample from x4 (4x SSAA supersampling downscale for crispness)
    img_x1 = img_x4.resize((w_x1, h_x1), Image.Resampling.LANCZOS)
    img_x1_sharp = im_sharpen(img_x1, radius=0.6, percent=125)
    
    for base, scale in OUTPUT_DIRS:
        full = os.path.join(base, subpath)
        ensure_dir(os.path.dirname(full))
        if scale == 4:
            img_x4_sharp.save(full, "PNG")
        elif scale == 3:
            img_x3_sharp.save(full, "PNG")
        elif scale == 2:
            img_x2_sharp.save(full, "PNG")
        elif scale == 1:
            img_x1_sharp.save(full, "PNG")

# -------------------------------------------------------------
# 1. Process Pirate Crest -> Window Header Crest
# -------------------------------------------------------------
def process_pirate_crest():
    src = os.path.join(ARTIFACT_DIR, "pirate_crest_1789643247628.jpg")
    print("Processing Pirate Crest:", src)
    cutout = remove_white_bg(src, tol=14, feather=1.2)
    cropped = crop_transparent(cutout)
    
    # Master x4: 480x96 (from canonical 120x24)
    cw, ch = 480, 96
    out_crest_x4 = Image.new("RGBA", (cw, ch), (0, 0, 0, 0))
    aspect = cropped.width / cropped.height
    new_h = ch
    new_w = int(new_h * aspect)
    if new_w > cw:
        new_w = cw
        new_h = int(new_w / aspect)
    
    scaled = cropped.resize((new_w, new_h), Image.Resampling.LANCZOS)
    ox = (cw - new_w) // 2
    oy = (ch - new_h) // 2
    out_crest_x4.paste(scaled, (ox, oy), scaled)
    
    save_master_x4("window/win_header_crest.png", out_crest_x4, 120, 24)
    print("Saved window/win_header_crest.png (x4 -> x3, x2, x1)")

# -------------------------------------------------------------
# 2. Process D-Pad Nautical Helm & Knob
# -------------------------------------------------------------
def process_dpad():
    src = os.path.join(ARTIFACT_DIR, "dpad_helm_1789643268484.jpg")
    print("Processing D-Pad Helm:", src)
    cutout = remove_white_bg(src, tol=14, feather=1.2)
    cropped = crop_transparent(cutout)
    
    size = max(cropped.width, cropped.height)
    sq = Image.new("RGBA", (size, size), (0, 0, 0, 0))
    sq.paste(cropped, ((size - cropped.width) // 2, (size - cropped.height) // 2))
    
    # 1. dpad_ring.png (Master x4: 404x404 from canonical 101x101)
    ring_x4 = sq.resize((404, 404), Image.Resampling.LANCZOS)
    save_master_x4("hud/dpad_ring.png", ring_x4, 101, 101)
    
    # 2. dpad_knob.png (Master x4: 160x160 from canonical 40x40)
    cx, cy = size // 2, size // 2
    knob_radius = int(size * 0.22)
    knob_crop = sq.crop((cx - knob_radius, cy - knob_radius, cx + knob_radius, cy + knob_radius))
    knob_x4 = knob_crop.resize((160, 160), Image.Resampling.LANCZOS)
    save_master_x4("hud/dpad_knob.png", knob_x4, 40, 40)
    print("Saved hud/dpad_ring.png & hud/dpad_knob.png (x4 -> x3, x2, x1)")

# -------------------------------------------------------------
# 3. Process Fire Attack Button
# -------------------------------------------------------------
def process_attack_btn():
    src = os.path.join(ARTIFACT_DIR, "fire_attack_btn_1789643288552.jpg")
    print("Processing Fire Attack Button:", src)
    cutout = remove_white_bg(src, tol=14, feather=1.2)
    cropped = crop_transparent(cutout)
    
    # Master x4: 200x400 (from canonical 50x100, 2 frames of 200x200 each)
    frame0_x4 = cropped.resize((200, 200), Image.Resampling.LANCZOS)
    
    enhancer = ImageEnhance.Color(frame0_x4)
    frame1 = enhancer.enhance(1.25)
    frame1_down = ImageEnhance.Brightness(frame1).enhance(0.88)
    pressed_x4 = Image.new("RGBA", (200, 200), (0, 0, 0, 0))
    pressed_x4.paste(frame1_down, (0, 2), frame1_down)
    
    sheet_x4 = Image.new("RGBA", (200, 400), (0, 0, 0, 0))
    sheet_x4.paste(frame0_x4, (0, 0), frame0_x4)
    sheet_x4.paste(pressed_x4, (0, 200), pressed_x4)
    
    save_master_x4("hud/btn_attack.png", sheet_x4, 50, 100)
    print("Saved hud/btn_attack.png (x4 -> x3, x2, x1)")

# -------------------------------------------------------------
# 4. Process Skill Socket
# -------------------------------------------------------------
def process_skill_socket():
    src = os.path.join(ARTIFACT_DIR, "skill_socket_1789643312188.jpg")
    print("Processing Skill Socket:", src)
    cutout = remove_white_bg(src, tol=14, feather=1.2)
    cropped = crop_transparent(cutout)
    
    # Master x4: 120x240 (from canonical 30x60, 2 frames of 120x120 each)
    frame0_x4 = cropped.resize((120, 120), Image.Resampling.LANCZOS)
    frame1_x4 = ImageEnhance.Brightness(frame0_x4).enhance(1.2)
    
    sheet_x4 = Image.new("RGBA", (120, 240), (0, 0, 0, 0))
    sheet_x4.paste(frame0_x4, (0, 0), frame0_x4)
    sheet_x4.paste(frame1_x4, (0, 120), frame1_x4)
    
    save_master_x4("hud/btn_skill_slot.png", sheet_x4, 30, 60)
    print("Saved hud/btn_skill_slot.png (x4 -> x3, x2, x1)")

# -------------------------------------------------------------
# 5. Process Window Frame Corner & Edges
# -------------------------------------------------------------
def process_window_corner():
    src = os.path.join(ARTIFACT_DIR, "window_frame_corner_1789643332757.jpg")
    print("Processing Window Corner:", src)
    cutout = remove_white_bg(src, tol=14, feather=1.2)
    cropped = crop_transparent(cutout)
    
    # Master x4: 80x80 (from canonical 20x20)
    c_tl_x4 = cropped.resize((80, 80), Image.Resampling.LANCZOS)
    save_master_x4("window/win_corner_tl.png", c_tl_x4, 20, 20)
    
    c_tr_x4 = c_tl_x4.transpose(Image.Transpose.FLIP_LEFT_RIGHT)
    save_master_x4("window/win_corner_tr.png", c_tr_x4, 20, 20)
    
    c_bl_x4 = c_tl_x4.transpose(Image.Transpose.FLIP_TOP_BOTTOM)
    save_master_x4("window/win_corner_bl.png", c_bl_x4, 20, 20)
    
    c_br_x4 = c_tl_x4.transpose(Image.Transpose.FLIP_LEFT_RIGHT).transpose(Image.Transpose.FLIP_TOP_BOTTOM)
    save_master_x4("window/win_corner_br.png", c_br_x4, 20, 20)
    
    # Edges Master x4: 80x80
    top_edge = cropped.crop((int(cropped.width * 0.15), 0, int(cropped.width * 0.85), int(cropped.height * 0.25)))
    edge_t_x4 = top_edge.resize((80, 80), Image.Resampling.LANCZOS)
    save_master_x4("window/win_edge_t.png", edge_t_x4, 20, 20)
    
    edge_b_x4 = edge_t_x4.transpose(Image.Transpose.FLIP_TOP_BOTTOM)
    save_master_x4("window/win_edge_b.png", edge_b_x4, 20, 20)
    
    left_edge = cropped.crop((0, int(cropped.height * 0.15), int(cropped.width * 0.25), int(cropped.height * 0.85)))
    edge_l_x4 = left_edge.resize((80, 80), Image.Resampling.LANCZOS)
    save_master_x4("window/win_edge_l.png", edge_l_x4, 20, 20)
    
    edge_r_x4 = edge_l_x4.transpose(Image.Transpose.FLIP_LEFT_RIGHT)
    save_master_x4("window/win_edge_r.png", edge_r_x4, 20, 20)
    print("Saved window corners and edges (x4 -> x3, x2, x1)")

# -------------------------------------------------------------
# 6. Process Window Background Pattern
# -------------------------------------------------------------
def process_window_bg():
    src = os.path.join(ARTIFACT_DIR, "win_bg_pattern_1789646902062.jpg")
    print("Processing Window BG Pattern:", src)
    im = Image.open(src)
    # Master x4: 128x128 (from canonical 32x32)
    bg_x4 = im.resize((128, 128), Image.Resampling.LANCZOS)
    save_master_x4("window/win_bg_pattern.png", bg_x4, 32, 32)
    print("Saved window/win_bg_pattern.png (x4 -> x3, x2, x1)")

# -------------------------------------------------------------
# 7. Process Potion Icons & Health/Mana Fill
# -------------------------------------------------------------
def process_potions():
    src = os.path.join(ARTIFACT_DIR, "potion_icons_1789643354863.jpg")
    print("Processing Potion Icons:", src)
    cutout = remove_white_bg(src, tol=14, feather=1.2)
    w, h = cutout.size
    
    # Left: HP ruby heart bottle (Master x4: 56x56 from canonical 14x14)
    hp_crop = cutout.crop((0, 0, w // 2, h))
    hp_crop = crop_transparent(hp_crop)
    icon_hp_x4 = hp_crop.resize((56, 56), Image.Resampling.LANCZOS)
    save_master_x4("hud/icon_hp.png", icon_hp_x4, 14, 14)
    
    # Right: MP sapphire bottle (Master x4: 56x56 from canonical 14x14)
    mp_crop = cutout.crop((w // 2, 0, w, h))
    mp_crop = crop_transparent(mp_crop)
    icon_mp_x4 = mp_crop.resize((56, 56), Image.Resampling.LANCZOS)
    save_master_x4("hud/icon_mp.png", icon_mp_x4, 14, 14)
    
    # Gauge HP Fill (Master x4: 480x56 from canonical 120x14)
    hp_center = hp_crop.crop((hp_crop.width // 4, hp_crop.height // 3, hp_crop.width * 3 // 4, hp_crop.height * 2 // 3))
    fill_hp_x4 = hp_center.resize((480, 56), Image.Resampling.LANCZOS)
    save_master_x4("hud/gauge_hp_fill.png", fill_hp_x4, 120, 14)
    
    # Gauge MP Fill (Master x4: 480x56 from canonical 120x14)
    mp_center = mp_crop.crop((mp_crop.width // 4, mp_crop.height // 3, mp_crop.width * 3 // 4, mp_crop.height * 2 // 3))
    fill_mp_x4 = mp_center.resize((480, 56), Image.Resampling.LANCZOS)
    save_master_x4("hud/gauge_mp_fill.png", fill_mp_x4, 120, 14)
    print("Saved hud/icon_hp.png, icon_mp.png, gauge_hp_fill.png, gauge_mp_fill.png (x4 -> x3, x2, x1)")

# -------------------------------------------------------------
# 8. Process Gauge Frame Casing Bar
# -------------------------------------------------------------
def process_gauge_casing():
    src = os.path.join(ARTIFACT_DIR, "gauge_casing_bar_1789646809643.jpg")
    print("Processing Gauge Casing:", src)
    cutout = remove_white_bg(src, tol=14, feather=1.2)
    cropped = crop_transparent(cutout)
    
    # Master x4: 480x56 (from canonical 120x14)
    casing_x4 = cropped.resize((480, 56), Image.Resampling.LANCZOS)
    save_master_x4("hud/gauge_hp_bg.png", casing_x4, 120, 14)
    print("Saved hud/gauge_hp_bg.png (x4 -> x3, x2, x1)")

# -------------------------------------------------------------
# 9. Process Close Button Wax Seal
# -------------------------------------------------------------
def process_close_button():
    src = os.path.join(ARTIFACT_DIR, "close_button_seal_1789643383524.jpg")
    print("Processing Close Button Seal:", src)
    cutout = remove_white_bg(src, tol=14, feather=1.2)
    cropped = crop_transparent(cutout)
    
    # Master x4: 72x144 (from canonical 18x36, 2 frames of 72x72 each)
    frame0_x4 = cropped.resize((72, 72), Image.Resampling.LANCZOS)
    frame1_down = ImageEnhance.Brightness(frame0_x4).enhance(0.85)
    frame1_x4 = Image.new("RGBA", (72, 72), (0, 0, 0, 0))
    frame1_x4.paste(frame1_down, (0, 2), frame1_down)
    
    sheet_x4 = Image.new("RGBA", (72, 144), (0, 0, 0, 0))
    sheet_x4.paste(frame0_x4, (0, 0), frame0_x4)
    sheet_x4.paste(frame1_x4, (0, 72), frame1_x4)
    
    save_master_x4("button/btn_close.png", sheet_x4, 18, 36)
    print("Saved button/btn_close.png (x4 -> x3, x2, x1)")

# -------------------------------------------------------------
# 10. Process Item Slot Frame & Rarities
# -------------------------------------------------------------
def process_item_slot():
    src = os.path.join(ARTIFACT_DIR, "item_slot_frame_1789643410295.jpg")
    print("Processing Item Slot Frame:", src)
    cutout = remove_white_bg(src, tol=14, feather=1.2)
    cropped = crop_transparent(cutout)
    
    # 1. slot_item.png (Master x4: 80x160 from canonical 20x40, 2 frames of 80x80)
    s20_norm_x4 = cropped.resize((80, 80), Image.Resampling.LANCZOS)
    s20_foc_x4 = ImageEnhance.Brightness(s20_norm_x4).enhance(1.25)
    s20_sheet_x4 = Image.new("RGBA", (80, 160), (0, 0, 0, 0))
    s20_sheet_x4.paste(s20_norm_x4, (0, 0), s20_norm_x4)
    s20_sheet_x4.paste(s20_foc_x4, (0, 80), s20_foc_x4)
    save_master_x4("slot/slot_item.png", s20_sheet_x4, 20, 40)
    
    # 2. slot_item_28.png (Master x4: 112x224 from canonical 28x56, 2 frames of 112x112)
    s28_norm_x4 = cropped.resize((112, 112), Image.Resampling.LANCZOS)
    s28_foc_x4 = ImageEnhance.Brightness(s28_norm_x4).enhance(1.25)
    s28_sheet_x4 = Image.new("RGBA", (112, 224), (0, 0, 0, 0))
    s28_sheet_x4.paste(s28_norm_x4, (0, 0), s28_norm_x4)
    s28_sheet_x4.paste(s28_foc_x4, (0, 112), s28_foc_x4)
    save_master_x4("slot/slot_item_28.png", s28_sheet_x4, 28, 56)
    
    # 3. slot_rarity.png (Master x4: 80x480 from canonical 20x120, 6 frames of 80x80)
    tint_colors = [
        (180, 190, 195), # Silver
        (60, 220, 120),  # Green
        (60, 170, 255),  # Blue
        (180, 100, 240), # Purple
        (255, 200, 40),  # Gold
        (255, 60, 50),   # Red
    ]
    rarity_sheet_x4 = Image.new("RGBA", (80, 480), (0, 0, 0, 0))
    for idx, (tr, tg, tb) in enumerate(tint_colors):
        frame = s20_norm_x4.copy()
        color_layer = Image.new("RGBA", (80, 80), (tr, tg, tb, 140))
        tinted = Image.alpha_composite(frame, color_layer)
        tinted.putalpha(frame.getchannel("A"))
        rarity_sheet_x4.paste(tinted, (0, idx * 80))
    save_master_x4("slot/slot_rarity.png", rarity_sheet_x4, 20, 120)
    print("Saved slot/slot_item.png, slot_item_28.png, slot_rarity.png (x4 -> x3, x2, x1)")

# -------------------------------------------------------------
# 11. Process RPG Button Bars
# -------------------------------------------------------------
def process_button_bars():
    src = os.path.join(ARTIFACT_DIR, "rpg_button_bars_1789645912243.jpg")
    print("Processing RPG Button Bars:", src)
    cutout = remove_white_bg(src, tol=14, feather=1.2)
    w, h = cutout.size
    
    btn_w = w // 5
    color_names = ["gold", "blue", "green", "red", "gray"]
    
    for i, name in enumerate(color_names):
        btn_crop = cutout.crop((i * btn_w, 0, (i + 1) * btn_w, h))
        btn_crop = crop_transparent(btn_crop)
        btn_horiz = btn_crop.transpose(Image.Transpose.ROTATE_270)
        
        # btn_frame_<color>.png (Master x4: 120x240 from canonical 30x60, 3 frames of 120x80)
        norm_80 = btn_horiz.resize((120, 80), Image.Resampling.LANCZOS)
        foc_80 = ImageEnhance.Brightness(norm_80).enhance(1.2)
        foc_80 = ImageEnhance.Color(foc_80).enhance(1.2)
        press_down = ImageEnhance.Brightness(norm_80).enhance(0.85)
        press_80 = Image.new("RGBA", (120, 80), (0, 0, 0, 0))
        press_80.paste(press_down, (0, 2), press_down)
        
        sheet120_x4 = Image.new("RGBA", (120, 240), (0, 0, 0, 0))
        sheet120_x4.paste(norm_80, (0, 0), norm_80)
        sheet120_x4.paste(foc_80, (0, 80), foc_80)
        sheet120_x4.paste(press_80, (0, 160), press_80)
        save_master_x4(f"button/btn_frame_{name}.png", sheet120_x4, 30, 60)
        
        # btn_<color>.png (Master x4: 20x240 from canonical 5x60, 3 frames of 20x80)
        norm_20 = norm_80.crop((50, 0, 70, 80))
        foc_20 = foc_80.crop((50, 0, 70, 80))
        press_20 = press_80.crop((50, 0, 70, 80))
        sheet20_x4 = Image.new("RGBA", (20, 240), (0, 0, 0, 0))
        sheet20_x4.paste(norm_20, (0, 0), norm_20)
        sheet20_x4.paste(foc_20, (0, 80), foc_20)
        sheet20_x4.paste(press_20, (0, 160), press_20)
        save_master_x4(f"button/btn_{name}.png", sheet20_x4, 5, 60)
    
    print("Saved all button/btn_frame_*.png & btn_*.png (x4 -> x3, x2, x1)")

# -------------------------------------------------------------
# 12. Process Pirate Notification Banner Scroll
# -------------------------------------------------------------
def process_banner():
    src = os.path.join(ARTIFACT_DIR, "pirate_banner_scroll_1789646829737.jpg")
    print("Processing Banner Scroll:", src)
    cutout = remove_white_bg(src, tol=14, feather=1.2)
    cropped = crop_transparent(cutout)
    
    # Master x4: 480x96 (from canonical 120x24)
    banner_x4 = cropped.resize((480, 96), Image.Resampling.LANCZOS)
    save_master_x4("control/noti_banner.png", banner_x4, 120, 24)
    print("Saved control/noti_banner.png (x4 -> x3, x2, x1)")

# -------------------------------------------------------------
# 13. Process Stepper Plus/Minus and Checkbox
# -------------------------------------------------------------
def process_stepper_and_checkbox():
    src = os.path.join(ARTIFACT_DIR, "stepper_and_check_1789646853076.jpg")
    print("Processing Stepper & Checkbox:", src)
    cutout = remove_white_bg(src, tol=14, feather=1.2)
    w, h = cutout.size
    hw, hh = w // 2, h // 2
    
    # Top-Left: Plus button
    plus_crop = crop_transparent(cutout.crop((0, 0, hw, hh)))
    # Top-Right: Minus button
    minus_crop = crop_transparent(cutout.crop((hw, 0, w, hh)))
    # Bottom-Left: Checkbox Checked
    check_crop = crop_transparent(cutout.crop((0, hh, hw, h)))
    # Bottom-Right: Checkbox Unchecked
    uncheck_crop = crop_transparent(cutout.crop((hw, hh, w, h)))
    
    # 1. btn_plus_minus.png (Master x4: 120x336 from canonical 30x84, 3 frames of 120x112)
    pm_f0 = plus_crop.resize((120, 112), Image.Resampling.LANCZOS)
    pm_f1 = ImageEnhance.Brightness(pm_f0).enhance(0.85)
    pm_f2 = minus_crop.resize((120, 112), Image.Resampling.LANCZOS)
    
    sheet_pm_x4 = Image.new("RGBA", (120, 336), (0, 0, 0, 0))
    sheet_pm_x4.paste(pm_f0, (0, 0), pm_f0)
    sheet_pm_x4.paste(pm_f1, (0, 112), pm_f1)
    sheet_pm_x4.paste(pm_f2, (0, 224), pm_f2)
    save_master_x4("button/btn_plus_minus.png", sheet_pm_x4, 30, 84)
    
    # 2. checkbox.png (Master x4: 56x112 from canonical 14x28, 2 frames of 56x56)
    cb_f0 = uncheck_crop.resize((56, 56), Image.Resampling.LANCZOS)
    cb_f1 = check_crop.resize((56, 56), Image.Resampling.LANCZOS)
    sheet_cb_x4 = Image.new("RGBA", (56, 112), (0, 0, 0, 0))
    sheet_cb_x4.paste(cb_f0, (0, 0), cb_f0)
    sheet_cb_x4.paste(cb_f1, (0, 56), cb_f1)
    save_master_x4("control/checkbox.png", sheet_cb_x4, 14, 28)
    print("Saved button/btn_plus_minus.png & control/checkbox.png (x4 -> x3, x2, x1)")

# -------------------------------------------------------------
# 14. Process Tab Headers
# -------------------------------------------------------------
def process_tab_headers():
    src = os.path.join(ARTIFACT_DIR, "pirate_tabs_1789646878815.jpg")
    print("Processing Tab Headers:", src)
    cutout = remove_white_bg(src, tol=14, feather=1.2)
    w, h = cutout.size
    
    active_crop = crop_transparent(cutout.crop((0, 0, w // 2, h)))
    inactive_crop = crop_transparent(cutout.crop((w // 2, 0, w, h)))
    
    # Master x4: 80x160 (from canonical 20x40, 2 frames of 80x80)
    tab_f0 = inactive_crop.resize((80, 80), Image.Resampling.LANCZOS)
    tab_f1 = active_crop.resize((80, 80), Image.Resampling.LANCZOS)
    
    sheet_tab_x4 = Image.new("RGBA", (80, 160), (0, 0, 0, 0))
    sheet_tab_x4.paste(tab_f0, (0, 0), tab_f0)
    sheet_tab_x4.paste(tab_f1, (0, 80), tab_f1)
    save_master_x4("tab/tab_header.png", sheet_tab_x4, 20, 40)
    print("Saved tab/tab_header.png (x4 -> x3, x2, x1)")

# -------------------------------------------------------------
# 15. Process Equipment Silhouettes
# -------------------------------------------------------------
def process_equipment_silhouettes():
    src = os.path.join(ARTIFACT_DIR, "equip_silhouettes_1789646928053.jpg")
    print("Processing Equipment Silhouettes:", src)
    cutout = remove_white_bg(src, tol=14, feather=1.2)
    w, h = cutout.size
    
    # 4 cols x 2 rows
    cw = w // 4
    ch = h // 2
    
    slots = [
        (0, 0), (1, 0), (2, 0), (3, 0),
        (0, 1), (1, 1), (2, 1), (3, 1)
    ]
    
    # Master x4: 72x608 (from canonical 18x152, 8 frames of 72x76 each)
    equip_sheet_x4 = Image.new("RGBA", (72, 608), (0, 0, 0, 0))
    
    for idx, (c, r) in enumerate(slots):
        sub = cutout.crop((c * cw, r * ch, (c + 1) * cw, (r + 1) * ch))
        sub_crop = crop_transparent(sub)
        
        # Fit inside 72x76
        frame_box = Image.new("RGBA", (72, 76), (0, 0, 0, 0))
        asp = sub_crop.width / sub_crop.height
        fh = 72
        fw = int(fh * asp)
        if fw > 68:
            fw = 68
            fh = int(fw / asp)
        scaled = sub_crop.resize((fw, fh), Image.Resampling.LANCZOS)
        ox = (72 - fw) // 2
        oy = (76 - fh) // 2
        frame_box.paste(scaled, (ox, oy), scaled)
        
        equip_sheet_x4.paste(frame_box, (0, idx * 76))
    
    save_master_x4("slot/slot_equip_bg.png", equip_sheet_x4, 18, 152)
    print("Saved slot/slot_equip_bg.png (x4 -> x3, x2, x1)")

# -------------------------------------------------------------
# 16. Process Text Field Border
# -------------------------------------------------------------
def process_tf_border():
    src = os.path.join(ARTIFACT_DIR, "text_field_border_1789646963179.jpg")
    print("Processing Text Field Border:", src)
    cutout = remove_white_bg(src, tol=14, feather=1.2)
    cropped = crop_transparent(cutout)
    
    # Master x4: 24x72 (from canonical 6x18, 3 frames of 24x24 each)
    tf_f0 = cropped.resize((24, 24), Image.Resampling.LANCZOS)
    tf_f1 = ImageEnhance.Brightness(tf_f0).enhance(1.25)
    tf_f2 = ImageEnhance.Brightness(tf_f0).enhance(0.85)
    
    sheet_tf_x4 = Image.new("RGBA", (24, 72), (0, 0, 0, 0))
    sheet_tf_x4.paste(tf_f0, (0, 0), tf_f0)
    sheet_tf_x4.paste(tf_f1, (0, 24), tf_f1)
    sheet_tf_x4.paste(tf_f2, (0, 48), tf_f2)
    save_master_x4("control/tf_border.png", sheet_tf_x4, 6, 18)
    print("Saved control/tf_border.png (x4 -> x3, x2, x1)")

def main():
    print("=================================================================")
    print(" BUILDING THEME 01 MASTER ASSETS AT x4 RESOLUTION -> DOWNSCALING ")
    print("=================================================================")
    process_pirate_crest()
    process_dpad()
    process_attack_btn()
    process_skill_socket()
    process_window_corner()
    process_window_bg()
    process_potions()
    process_gauge_casing()
    process_close_button()
    process_item_slot()
    process_button_bars()
    process_banner()
    process_stepper_and_checkbox()
    process_tab_headers()
    process_equipment_silhouettes()
    process_tf_border()
    print("=================================================================")
    print(" ALL 39 ASSETS SUCCESSFULLY BUILT AT x4 AND DOWNSAMPLED TO x3, x2, x1! ")
    print("=================================================================")

if __name__ == "__main__":
    main()
