import os
import cv2
import numpy as np
from PIL import Image, ImageFilter, ImageEnhance, ImageOps

# Base artifact directory where model images were generated
ARTIFACT_DIR = r"C:\Users\DELL\.gemini\antigravity\brain\41d9f09e-d895-47a0-817a-a101cd061233"

# Output target directories and their scale factors
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

def save_all_targets(subpath, img_x1):
    for base, scale in OUTPUT_DIRS:
        full = os.path.join(base, subpath)
        ensure_dir(os.path.dirname(full))
        if scale == 1:
            img_x1.save(full, "PNG")
        else:
            w, h = img_x1.size
            # High quality bicubic scaling for scaled artwork
            scaled = img_x1.resize((w * scale, h * scale), Image.Resampling.LANCZOS)
            scaled.save(full, "PNG")

# -------------------------------------------------------------
# Clean AI Background Removal (Flood-Fill + Alpha Antialiasing)
# -------------------------------------------------------------
def remove_white_bg(img_path, tol=15, feather=1.0):
    img = cv2.imread(img_path, cv2.IMREAD_UNCHANGED)
    if img is None:
        raise ValueError(f"Could not open {img_path}")
    bgr = img[:, :, :3] if img.shape[2] >= 3 else cv2.cvtColor(img, cv2.COLOR_GRAY2BGR)
    
    diff = 255 - bgr
    dist = np.max(diff, axis=2) # 0 for pure white, higher for colors
    
    low_thresh = tol
    high_thresh = tol + 25
    alpha = np.clip((dist.astype(np.float32) - low_thresh) / (high_thresh - low_thresh), 0.0, 1.0)
    
    bg_seed_mask = (dist < high_thresh).astype(np.uint8)
    h_img, w_img = bg_seed_mask.shape
    flood_mask = np.zeros((h_img + 2, w_img + 2), np.uint8)
    
    # Flood-fill from all 4 corners
    cv2.floodFill(bg_seed_mask, flood_mask, (0, 0), 2)
    cv2.floodFill(bg_seed_mask, flood_mask, (w_img - 1, 0), 2)
    cv2.floodFill(bg_seed_mask, flood_mask, (0, h_img - 1), 2)
    cv2.floodFill(bg_seed_mask, flood_mask, (w_img - 1, h_img - 1), 2)
    
    is_outer_bg = (bg_seed_mask == 2)
    final_alpha = np.where(is_outer_bg, alpha, 1.0)
    final_alpha_u8 = (final_alpha * 255).astype(np.uint8)
    
    if feather > 0:
        final_alpha_u8 = cv2.GaussianBlur(final_alpha_u8, (3, 3), feather)
        final_alpha_u8 = np.where(is_outer_bg, final_alpha_u8, 255)
    
    rgba = np.dstack([bgr, final_alpha_u8])
    return Image.fromarray(cv2.cvtColor(rgba, cv2.COLOR_BGRA2RGBA))

def crop_transparent(im):
    bbox = im.getbbox()
    return im.crop(bbox) if bbox else im

def sharpen(im):
    return im.filter(ImageFilter.UnsharpMask(radius=1.0, percent=130, threshold=2))

# -------------------------------------------------------------
# 1. Process Pirate Crest -> Window Header Crest
# -------------------------------------------------------------
def process_pirate_crest():
    src = os.path.join(ARTIFACT_DIR, "pirate_crest_1789643247628.jpg")
    print("Processing Pirate Crest:", src)
    cutout = remove_white_bg(src, tol=15, feather=1.0)
    cropped = crop_transparent(cutout)
    
    # Target size x1: 120x24 canvas
    cw, ch = 120, 24
    out_crest = Image.new("RGBA", (cw, ch), (0, 0, 0, 0))
    
    # Scale cropped crest to fit inside 120x24 preserving aspect ratio
    aspect = cropped.width / cropped.height
    new_h = ch
    new_w = int(new_h * aspect)
    if new_w > cw:
        new_w = cw
        new_h = int(new_w / aspect)
    
    scaled = cropped.resize((new_w, new_h), Image.Resampling.LANCZOS)
    scaled = sharpen(scaled)
    
    ox = (cw - new_w) // 2
    oy = (ch - new_h) // 2
    out_crest.paste(scaled, (ox, oy), scaled)
    
    save_all_targets("window/win_header_crest.png", out_crest)
    print("Saved window/win_header_crest.png")

# -------------------------------------------------------------
# 2. Process D-Pad Nautical Helm & Knob
# -------------------------------------------------------------
def process_dpad():
    src = os.path.join(ARTIFACT_DIR, "dpad_helm_1789643268484.jpg")
    print("Processing D-Pad Helm:", src)
    cutout = remove_white_bg(src, tol=15, feather=1.0)
    cropped = crop_transparent(cutout)
    
    # Make square
    size = max(cropped.width, cropped.height)
    sq = Image.new("RGBA", (size, size), (0, 0, 0, 0))
    sq.paste(cropped, ((size - cropped.width) // 2, (size - cropped.height) // 2))
    
    # 1. dpad_ring.png (101x101)
    ring_x1 = sq.resize((101, 101), Image.Resampling.LANCZOS)
    ring_x1 = sharpen(ring_x1)
    save_all_targets("hud/dpad_ring.png", ring_x1)
    
    # 2. dpad_knob.png (40x40 from center boss)
    cx, cy = size // 2, size // 2
    knob_radius = int(size * 0.22)
    knob_crop = sq.crop((cx - knob_radius, cy - knob_radius, cx + knob_radius, cy + knob_radius))
    knob_x1 = knob_crop.resize((40, 40), Image.Resampling.LANCZOS)
    knob_x1 = sharpen(knob_x1)
    save_all_targets("hud/dpad_knob.png", knob_x1)
    print("Saved hud/dpad_ring.png and hud/dpad_knob.png")

# -------------------------------------------------------------
# 3. Process Fire Attack Button
# -------------------------------------------------------------
def process_attack_btn():
    src = os.path.join(ARTIFACT_DIR, "fire_attack_btn_1789643288552.jpg")
    print("Processing Fire Attack Button:", src)
    cutout = remove_white_bg(src, tol=15, feather=1.0)
    cropped = crop_transparent(cutout)
    
    # 50x50 Normal frame
    frame0 = cropped.resize((50, 50), Image.Resampling.LANCZOS)
    frame0 = sharpen(frame0)
    
    # Pressed frame: slightly darker, shifted down 1px, higher fire contrast
    enhancer = ImageEnhance.Color(frame0)
    frame1 = enhancer.enhance(1.3)
    frame1_down = ImageEnhance.Brightness(frame1).enhance(0.9)
    pressed = Image.new("RGBA", (50, 50), (0, 0, 0, 0))
    pressed.paste(frame1_down, (0, 1), frame1_down)
    
    # Stack 50x100
    sheet = Image.new("RGBA", (50, 100), (0, 0, 0, 0))
    sheet.paste(frame0, (0, 0), frame0)
    sheet.paste(pressed, (0, 50), pressed)
    
    save_all_targets("hud/btn_attack.png", sheet)
    print("Saved hud/btn_attack.png")

# -------------------------------------------------------------
# 4. Process Skill Socket
# -------------------------------------------------------------
def process_skill_socket():
    src = os.path.join(ARTIFACT_DIR, "skill_socket_1789643312188.jpg")
    print("Processing Skill Socket:", src)
    cutout = remove_white_bg(src, tol=15, feather=1.0)
    cropped = crop_transparent(cutout)
    
    # 30x30 Normal frame
    frame0 = cropped.resize((30, 30), Image.Resampling.LANCZOS)
    frame0 = sharpen(frame0)
    
    # Active frame: enhanced cyan glow
    frame1 = ImageEnhance.Brightness(frame0).enhance(1.2)
    
    sheet = Image.new("RGBA", (30, 60), (0, 0, 0, 0))
    sheet.paste(frame0, (0, 0), frame0)
    sheet.paste(frame1, (0, 30), frame1)
    
    save_all_targets("hud/btn_skill_slot.png", sheet)
    print("Saved hud/btn_skill_slot.png")

# -------------------------------------------------------------
# 5. Process Window Frame Corner & Edges
# -------------------------------------------------------------
def process_window_corner():
    src = os.path.join(ARTIFACT_DIR, "window_frame_corner_1789643332757.jpg")
    print("Processing Window Corner:", src)
    cutout = remove_white_bg(src, tol=15, feather=1.0)
    cropped = crop_transparent(cutout)
    
    # Corner size: 20x20
    cw, ch = 20, 20
    c_tl = cropped.resize((cw, ch), Image.Resampling.LANCZOS)
    c_tl = sharpen(c_tl)
    save_all_targets("window/win_corner_tl.png", c_tl)
    
    c_tr = c_tl.transpose(Image.Transpose.FLIP_LEFT_RIGHT)
    save_all_targets("window/win_corner_tr.png", c_tr)
    
    c_bl = c_tl.transpose(Image.Transpose.FLIP_TOP_BOTTOM)
    save_all_targets("window/win_corner_bl.png", c_bl)
    
    c_br = c_tl.transpose(Image.Transpose.FLIP_LEFT_RIGHT).transpose(Image.Transpose.FLIP_TOP_BOTTOM)
    save_all_targets("window/win_corner_br.png", c_br)
    
    # Top & Bottom edges: sample top border strip
    top_edge = cropped.crop((int(cropped.width * 0.15), 0, int(cropped.width * 0.85), int(cropped.height * 0.25)))
    edge_t = top_edge.resize((20, 20), Image.Resampling.LANCZOS)
    edge_t = sharpen(edge_t)
    save_all_targets("window/win_edge_t.png", edge_t)
    
    edge_b = edge_t.transpose(Image.Transpose.FLIP_TOP_BOTTOM)
    save_all_targets("window/win_edge_b.png", edge_b)
    
    # Left & Right edges
    left_edge = cropped.crop((0, int(cropped.height * 0.15), int(cropped.width * 0.25), int(cropped.height * 0.85)))
    edge_l = left_edge.resize((20, 20), Image.Resampling.LANCZOS)
    edge_l = sharpen(edge_l)
    save_all_targets("window/win_edge_l.png", edge_l)
    
    edge_r = edge_l.transpose(Image.Transpose.FLIP_LEFT_RIGHT)
    save_all_targets("window/win_edge_r.png", edge_r)
    print("Saved window corners and edges.")

# -------------------------------------------------------------
# 6. Process Potion Icons & Health/Mana Fill
# -------------------------------------------------------------
def process_potions():
    src = os.path.join(ARTIFACT_DIR, "potion_icons_1789643354863.jpg")
    print("Processing Potion Icons:", src)
    cutout = remove_white_bg(src, tol=15, feather=1.0)
    w, h = cutout.size
    
    # Left: HP ruby heart bottle
    hp_crop = cutout.crop((0, 0, w // 2, h))
    hp_crop = crop_transparent(hp_crop)
    icon_hp = hp_crop.resize((14, 14), Image.Resampling.LANCZOS)
    icon_hp = sharpen(icon_hp)
    save_all_targets("hud/icon_hp.png", icon_hp)
    
    # Right: MP sapphire bottle
    mp_crop = cutout.crop((w // 2, 0, w, h))
    mp_crop = crop_transparent(mp_crop)
    icon_mp = mp_crop.resize((14, 14), Image.Resampling.LANCZOS)
    icon_mp = sharpen(icon_mp)
    save_all_targets("hud/icon_mp.png", icon_mp)
    
    # Gauge HP Fill (120x14 ruby texture sampled from hp bottle center)
    hp_center = hp_crop.crop((hp_crop.width // 4, hp_crop.height // 3, hp_crop.width * 3 // 4, hp_crop.height * 2 // 3))
    gauge_hp = hp_center.resize((120, 14), Image.Resampling.LANCZOS)
    gauge_hp = sharpen(gauge_hp)
    save_all_targets("hud/gauge_hp_fill.png", gauge_hp)
    
    # Gauge MP Fill (120x14 azure texture sampled from mp bottle center)
    mp_center = mp_crop.crop((mp_crop.width // 4, mp_crop.height // 3, mp_crop.width * 3 // 4, mp_crop.height * 2 // 3))
    gauge_mp = mp_center.resize((120, 14), Image.Resampling.LANCZOS)
    gauge_mp = sharpen(gauge_mp)
    save_all_targets("hud/gauge_mp_fill.png", gauge_mp)
    print("Saved hud/icon_hp.png, icon_mp.png, gauge_hp_fill.png, gauge_mp_fill.png")

# -------------------------------------------------------------
# 7. Process Close Button Wax Seal
# -------------------------------------------------------------
def process_close_button():
    src = os.path.join(ARTIFACT_DIR, "close_button_seal_1789643383524.jpg")
    print("Processing Close Button Seal:", src)
    cutout = remove_white_bg(src, tol=15, feather=1.0)
    cropped = crop_transparent(cutout)
    
    frame0 = cropped.resize((18, 18), Image.Resampling.LANCZOS)
    frame0 = sharpen(frame0)
    
    # Pressed frame: slightly darker, shifted 1px down
    frame1_down = ImageEnhance.Brightness(frame0).enhance(0.85)
    frame1 = Image.new("RGBA", (18, 18), (0, 0, 0, 0))
    frame1.paste(frame1_down, (0, 1), frame1_down)
    
    sheet = Image.new("RGBA", (18, 36), (0, 0, 0, 0))
    sheet.paste(frame0, (0, 0), frame0)
    sheet.paste(frame1, (0, 18), frame1)
    
    save_all_targets("button/btn_close.png", sheet)
    print("Saved button/btn_close.png")

# -------------------------------------------------------------
# 8. Process Item Slot Frame & Rarities
# -------------------------------------------------------------
def process_item_slot():
    src = os.path.join(ARTIFACT_DIR, "item_slot_frame_1789643410295.jpg")
    print("Processing Item Slot Frame:", src)
    cutout = remove_white_bg(src, tol=15, feather=1.0)
    cropped = crop_transparent(cutout)
    
    # 20x20 Normal frame
    s20_norm = cropped.resize((20, 20), Image.Resampling.LANCZOS)
    s20_norm = sharpen(s20_norm)
    # Focus frame: brighter gold
    s20_foc = ImageEnhance.Brightness(s20_norm).enhance(1.25)
    s20_sheet = Image.new("RGBA", (20, 40), (0, 0, 0, 0))
    s20_sheet.paste(s20_norm, (0, 0), s20_norm)
    s20_sheet.paste(s20_foc, (0, 20), s20_foc)
    save_all_targets("slot/slot_item.png", s20_sheet)
    
    # 28x28 Slot for large showcase
    s28_norm = cropped.resize((28, 28), Image.Resampling.LANCZOS)
    s28_norm = sharpen(s28_norm)
    s28_foc = ImageEnhance.Brightness(s28_norm).enhance(1.25)
    s28_sheet = Image.new("RGBA", (28, 56), (0, 0, 0, 0))
    s28_sheet.paste(s28_norm, (0, 0), s28_norm)
    s28_sheet.paste(s28_foc, (0, 28), s28_foc)
    save_all_targets("slot/slot_item_28.png", s28_sheet)
    
    # 6 Rarity Borders: 20x120 (6 frames of 20x20)
    # Tint outer frame for Common (Silver), Uncommon (Green), Rare (Blue), Epic (Purple), Legendary (Gold), Mythic (Red)
    tint_colors = [
        (180, 190, 195), # Silver
        (60, 220, 120),  # Green
        (60, 170, 255),  # Blue
        (180, 100, 240), # Purple
        (255, 200, 40),  # Gold
        (255, 60, 50),   # Red
    ]
    rarity_sheet = Image.new("RGBA", (20, 120), (0, 0, 0, 0))
    # Extract only border
    slot_np = np.array(s20_norm)
    for idx, (tr, tg, tb) in enumerate(tint_colors):
        frame = s20_norm.copy()
        # Tint with color
        color_layer = Image.new("RGBA", (20, 20), (tr, tg, tb, 140))
        tinted = Image.alpha_composite(frame, color_layer)
        # Re-apply outer alpha
        tinted.putalpha(frame.getchannel("A"))
        rarity_sheet.paste(tinted, (0, idx * 20))
    save_all_targets("slot/slot_rarity.png", rarity_sheet)
    print("Saved slot/slot_item.png, slot_item_28.png, slot_rarity.png")

# -------------------------------------------------------------
# 9. Process RPG Button Bars
# -------------------------------------------------------------
def process_button_bars():
    src = os.path.join(ARTIFACT_DIR, "rpg_button_bars_1789645912243.jpg")
    print("Processing RPG Button Bars:", src)
    cutout = remove_white_bg(src, tol=15, feather=1.0)
    w, h = cutout.size
    
    # 5 buttons arranged horizontally in the image: Gold, Blue, Green, Red, Gray
    btn_w = w // 5
    color_names = ["gold", "blue", "green", "red", "gray"]
    
    for i, name in enumerate(color_names):
        btn_crop = cutout.crop((i * btn_w, 0, (i + 1) * btn_w, h))
        btn_crop = crop_transparent(btn_crop)
        # Rotate 90 degrees clockwise to make horizontal button
        btn_horiz = btn_crop.transpose(Image.Transpose.ROTATE_270)
        
        # 1. btn_frame_<color>.png: 30x60 (3 states x 20px)
        norm_20 = btn_horiz.resize((30, 20), Image.Resampling.LANCZOS)
        norm_20 = sharpen(norm_20)
        foc_20 = ImageEnhance.Brightness(norm_20).enhance(1.2)
        foc_20 = ImageEnhance.Color(foc_20).enhance(1.2)
        press_down = ImageEnhance.Brightness(norm_20).enhance(0.85)
        press_20 = Image.new("RGBA", (30, 20), (0, 0, 0, 0))
        press_20.paste(press_down, (0, 1), press_down)
        
        sheet30 = Image.new("RGBA", (30, 60), (0, 0, 0, 0))
        sheet30.paste(norm_20, (0, 0), norm_20)
        sheet30.paste(foc_20, (0, 20), foc_20)
        sheet30.paste(press_20, (0, 40), press_20)
        save_all_targets(f"button/btn_frame_{name}.png", sheet30)
        
        # 2. btn_<color>.png: 5x60 (5x20 per state)
        norm_5 = norm_20.crop((12, 0, 17, 20))
        foc_5 = foc_20.crop((12, 0, 17, 20))
        press_5 = press_20.crop((12, 0, 17, 20))
        sheet5 = Image.new("RGBA", (5, 60), (0, 0, 0, 0))
        sheet5.paste(norm_5, (0, 0), norm_5)
        sheet5.paste(foc_5, (0, 20), foc_5)
        sheet5.paste(press_5, (0, 40), press_5)
        save_all_targets(f"button/btn_{name}.png", sheet5)
    
    print("Saved all button/btn_frame_*.png and button/btn_*.png")

def main():
    print("=== Processing Model Artwork for Theme 01 Neo Pirate Fantasy ===")
    process_pirate_crest()
    process_dpad()
    process_attack_btn()
    process_skill_socket()
    process_window_corner()
    process_potions()
    process_close_button()
    process_item_slot()
    process_button_bars()
    print("=== All Model-Driven Theme 01 Assets Successfully Deployed! ===")

if __name__ == "__main__":
    main()
