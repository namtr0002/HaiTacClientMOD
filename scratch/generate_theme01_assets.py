import os
import math
from PIL import Image, ImageDraw, ImageFilter

def ensure_dir(path):
    os.makedirs(path, exist_ok=True)

# Output target roots
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

def save_all_targets(subpath, img):
    for base, scale in OUTPUT_DIRS:
        full = os.path.join(base, subpath)
        ensure_dir(os.path.dirname(full))
        if scale == 1:
            img.save(full, "PNG")
        else:
            w, h = img.size
            scaled = img.resize((w * scale, h * scale), Image.Resampling.NEAREST)
            scaled.save(full, "PNG")

# -------------------------------------------------------------
# Color Palette
# -------------------------------------------------------------
# Metal / Gold
C_GOLD_HI = (255, 235, 130, 255)
C_GOLD_MID = (212, 175, 55, 255)
C_GOLD_DARK = (140, 100, 25, 255)
C_BRONZE = (100, 65, 20, 255)
C_RIVET = (240, 215, 120, 255)

# Obsidian / Dark Navy Wood
C_BG_DARKEST = (12, 16, 24, 255)
C_BG_DARK = (18, 24, 36, 255)
C_BG_MID = (26, 36, 52, 255)
C_BG_LIGHT = (38, 52, 74, 255)
C_BORDER_STEEL = (45, 62, 88, 255)

# -------------------------------------------------------------
# 1. WINDOW ASSETS (9-Slice + Header Crest + Pattern)
# -------------------------------------------------------------
def gen_window_assets():
    # Corner size: 20x20
    cw, ch = 20, 20

    # Top-Left Corner
    im_tl = Image.new("RGBA", (cw, ch), (0, 0, 0, 0))
    d = ImageDraw.Draw(im_tl)
    # Background fill inside window area
    d.rectangle([3, 3, cw-1, ch-1], fill=C_BG_DARK)
    # Outer dark border
    d.line([(3, 0), (cw-1, 0)], fill=C_BRONZE, width=1)
    d.line([(0, 3), (0, ch-1)], fill=C_BRONZE, width=1)
    d.line([(0, 3), (3, 0)], fill=C_BRONZE, width=1)
    # Gold trim 1px in
    d.line([(4, 1), (cw-1, 1)], fill=C_GOLD_HI, width=1)
    d.line([(1, 4), (1, ch-1)], fill=C_GOLD_MID, width=1)
    d.line([(1, 4), (4, 1)], fill=C_GOLD_HI, width=1)
    # Second inner border
    d.line([(4, 2), (cw-1, 2)], fill=C_GOLD_DARK, width=1)
    d.line([(2, 4), (2, ch-1)], fill=C_GOLD_DARK, width=1)
    # Steel frame
    d.line([(4, 3), (cw-1, 3)], fill=C_BORDER_STEEL, width=1)
    d.line([(3, 4), (3, ch-1)], fill=C_BORDER_STEEL, width=1)
    # Ornate Brass Corner Bracket & Rivet
    d.polygon([(0, 0), (9, 0), (0, 9)], fill=C_GOLD_DARK)
    d.polygon([(1, 1), (7, 1), (1, 7)], fill=C_GOLD_MID)
    d.polygon([(2, 2), (5, 2), (2, 5)], fill=C_GOLD_HI)
    # Rivet at (5, 5)
    d.ellipse([4, 4, 7, 7], fill=C_BRONZE, outline=C_GOLD_HI)
    d.point((5, 5), fill=(255, 255, 220, 255))
    save_all_targets("window/win_corner_tl.png", im_tl)

    # Top-Right Corner (flip TL horizontally)
    im_tr = im_tl.transpose(Image.Transpose.FLIP_LEFT_RIGHT)
    save_all_targets("window/win_corner_tr.png", im_tr)

    # Bottom-Left Corner (flip TL vertically)
    im_bl = im_tl.transpose(Image.Transpose.FLIP_TOP_BOTTOM)
    save_all_targets("window/win_corner_bl.png", im_bl)

    # Bottom-Right Corner (flip TL both)
    im_br = im_tl.transpose(Image.Transpose.FLIP_LEFT_RIGHT).transpose(Image.Transpose.FLIP_TOP_BOTTOM)
    save_all_targets("window/win_corner_br.png", im_br)

    # Top Edge: 20x20
    im_et = Image.new("RGBA", (cw, ch), (0, 0, 0, 0))
    d = ImageDraw.Draw(im_et)
    d.rectangle([0, 3, cw-1, ch-1], fill=C_BG_DARK)
    d.line([(0, 0), (cw-1, 0)], fill=C_BRONZE, width=1)
    d.line([(0, 1), (cw-1, 1)], fill=C_GOLD_HI, width=1)
    d.line([(0, 2), (cw-1, 2)], fill=C_GOLD_DARK, width=1)
    d.line([(0, 3), (cw-1, 3)], fill=C_BORDER_STEEL, width=1)
    # Subtle middle filigree notch
    d.point((cw//2 - 1, 1), fill=(255, 255, 240, 255))
    d.point((cw//2, 1), fill=(255, 255, 240, 255))
    save_all_targets("window/win_edge_t.png", im_et)

    # Bottom Edge (flip ET vertically)
    im_eb = im_et.transpose(Image.Transpose.FLIP_TOP_BOTTOM)
    save_all_targets("window/win_edge_b.png", im_eb)

    # Left Edge: 20x20
    im_el = Image.new("RGBA", (cw, ch), (0, 0, 0, 0))
    d = ImageDraw.Draw(im_el)
    d.rectangle([3, 0, cw-1, ch-1], fill=C_BG_DARK)
    d.line([(0, 0), (0, ch-1)], fill=C_BRONZE, width=1)
    d.line([(1, 0), (1, ch-1)], fill=C_GOLD_MID, width=1)
    d.line([(2, 0), (2, ch-1)], fill=C_GOLD_DARK, width=1)
    d.line([(3, 0), (3, ch-1)], fill=C_BORDER_STEEL, width=1)
    # Rivet on left edge
    d.ellipse([1, ch//2 - 2, 4, ch//2 + 1], fill=C_GOLD_DARK, outline=C_GOLD_HI)
    save_all_targets("window/win_edge_l.png", im_el)

    # Right Edge (flip EL horizontally)
    im_er = im_el.transpose(Image.Transpose.FLIP_LEFT_RIGHT)
    save_all_targets("window/win_edge_r.png", im_er)

    # Background pattern tile: 32x32 seamless navy naval wood
    im_bg = Image.new("RGBA", (32, 32), C_BG_DARKEST)
    d = ImageDraw.Draw(im_bg)
    for y in range(32):
        shade = 14 + int(math.sin(y * 0.4) * 3)
        col = (shade, shade + 5, shade + 14, 255)
        d.line([(0, y), (31, y)], fill=col, width=1)
        if y % 8 == 0:
            d.line([(0, y), (31, y)], fill=(col[0]-4, col[1]-3, col[2]-2, 255), width=1)
    save_all_targets("window/win_bg_pattern.png", im_bg)

    # Header Crest: 120x24 (Royal Pirate Crest for Window Title)
    im_crest = Image.new("RGBA", (120, 24), (0, 0, 0, 0))
    d = ImageDraw.Draw(im_crest)
    hw, hh = 120, 24
    d.polygon([(10, 3), (hw-11, 3), (hw-4, hh//2), (hw-11, hh-4), (10, hh-4), (3, hh//2)], fill=C_BG_MID)
    d.polygon([(11, 4), (hw-12, 4), (hw-5, hh//2), (hw-12, hh-5), (11, hh-5), (4, hh//2)], fill=(20, 28, 42, 255))
    # Border
    d.line([(10, 3), (hw-11, 3)], fill=C_GOLD_HI, width=1)
    d.line([(10, hh-4), (hw-11, hh-4)], fill=C_GOLD_DARK, width=1)
    # Center emblem (Anchor & Wings)
    cx, cy = hw // 2, hh // 2
    d.ellipse([cx - 5, cy - 8, cx + 4, cy + 1], outline=C_GOLD_HI, width=1)
    d.line([(cx, cy - 8), (cx, cy + 7)], fill=C_GOLD_HI, width=2)
    d.line([(cx - 7, cy + 1), (cx + 6, cy + 1)], fill=C_GOLD_MID, width=1)
    d.arc([cx - 9, cy - 2, cx + 8, cy + 8], start=20, end=160, fill=C_GOLD_HI, width=2)
    # Wings left and right
    for i in range(1, 4):
        d.line([(cx - 12 - i*5, cy), (cx - 16 - i*5, cy - 4)], fill=C_GOLD_MID, width=1)
        d.line([(cx + 11 + i*5, cy), (cx + 15 + i*5, cy - 4)], fill=C_GOLD_MID, width=1)
    save_all_targets("window/win_header_crest.png", im_crest)

# -------------------------------------------------------------
# 2. BUTTON ASSETS (5x60 classic & 30x60 3-piece stretchable)
# -------------------------------------------------------------
def gen_button_assets():
    styles = {
        "gold": {
            "norm": ((250, 205, 75), (185, 135, 15), (255, 240, 150), (120, 80, 5)),
            "focus": ((255, 235, 110), (220, 165, 30), (255, 255, 200), (160, 110, 10)),
            "press": ((160, 115, 10), (210, 160, 25), (110, 70, 5), (240, 195, 60)),
        },
        "blue": {
            "norm": ((45, 135, 215), (25, 80, 140), (100, 190, 255), (15, 50, 95)),
            "focus": ((70, 165, 245), (35, 110, 180), (145, 220, 255), (20, 70, 125)),
            "press": ((20, 65, 115), (35, 110, 180), (10, 40, 75), (65, 155, 235)),
        },
        "green": {
            "norm": ((45, 185, 95), (25, 120, 55), (95, 235, 145), (15, 80, 35)),
            "focus": ((65, 215, 120), (35, 150, 75), (140, 255, 185), (20, 100, 45)),
            "press": ((20, 95, 45), (35, 150, 70), (10, 60, 25), (60, 205, 110)),
        },
        "red": {
            "norm": ((230, 65, 55), (150, 30, 25), (255, 130, 120), (95, 15, 15)),
            "focus": ((250, 90, 80), (180, 40, 35), (255, 170, 160), (120, 20, 20)),
            "press": ((125, 20, 20), (180, 45, 40), (80, 10, 10), (240, 85, 75)),
        },
        "gray": {
            "norm": ((85, 100, 120), (50, 60, 75), (135, 155, 180), (30, 38, 48)),
            "focus": ((110, 130, 150), (70, 85, 105), (165, 185, 210), (45, 55, 68)),
            "press": ((45, 55, 68), (75, 90, 110), (25, 32, 40), (105, 125, 145)),
        },
    }

    # Generate 5x60 (Direct replacement for button0.png: 3 states x 20px height)
    for name, cols in styles.items():
        im5 = Image.new("RGBA", (5, 60), (0, 0, 0, 0))
        d5 = ImageDraw.Draw(im5)
        for s_idx, state_key in enumerate(["norm", "focus", "press"]):
            y_base = s_idx * 20
            t_col, b_col, h_col, d_col = cols[state_key]
            d5.rectangle([0, y_base, 4, y_base + 19], outline=d_col)
            d5.line([(1, y_base + 1), (3, y_base + 1)], fill=h_col, width=1)
            for row in range(2, 18):
                ratio = (row - 2) / 15.0
                r = int(t_col[0] + (b_col[0] - t_col[0]) * ratio)
                g = int(t_col[1] + (b_col[1] - t_col[1]) * ratio)
                b = int(t_col[2] + (b_col[2] - t_col[2]) * ratio)
                d5.line([(1, y_base + row), (3, y_base + row)], fill=(r, g, b, 255), width=1)
            d5.line([(1, y_base + 18), (3, y_base + 18)], fill=d_col, width=1)
        save_all_targets(f"button/btn_{name}.png", im5)

    # Generate 30x60 Frame Buttons (left 8px, middle 14px, right 8px stretchable)
    for name, cols in styles.items():
        im30 = Image.new("RGBA", (30, 60), (0, 0, 0, 0))
        d30 = ImageDraw.Draw(im30)
        for s_idx, state_key in enumerate(["norm", "focus", "press"]):
            y_base = s_idx * 20
            t_col, b_col, h_col, d_col = cols[state_key]
            d30.rounded_rectangle([0, y_base, 29, y_base + 19], radius=3, outline=d_col)
            d30.line([(2, y_base + 1), (27, y_base + 1)], fill=h_col, width=1)
            for row in range(2, 18):
                ratio = (row - 2) / 15.0
                r = int(t_col[0] + (b_col[0] - t_col[0]) * ratio)
                g = int(t_col[1] + (b_col[1] - t_col[1]) * ratio)
                b = int(t_col[2] + (b_col[2] - t_col[2]) * ratio)
                d30.line([(1, y_base + row), (28, y_base + row)], fill=(r, g, b, 255), width=1)
            d30.line([(1, y_base + 2), (1, y_base + 17)], fill=h_col, width=1)
            d30.line([(28, y_base + 2), (28, y_base + 17)], fill=d_col, width=1)
            d30.line([(2, y_base + 18), (27, y_base + 18)], fill=d_col, width=1)
        save_all_targets(f"button/btn_frame_{name}.png", im30)

    # Close Button: 18x36 (2 frames 18x18: Normal, Pressed)
    im_close = Image.new("RGBA", (18, 36), (0, 0, 0, 0))
    dc = ImageDraw.Draw(im_close)
    # Frame 0: Normal
    dc.ellipse([1, 1, 16, 16], fill=(180, 30, 25, 255), outline=C_GOLD_MID, width=1)
    dc.ellipse([3, 3, 14, 14], outline=(240, 80, 70, 255), width=1)
    dc.line([(5, 5), (12, 12)], fill=(255, 255, 255, 255), width=2)
    dc.line([(12, 5), (5, 12)], fill=(255, 255, 255, 255), width=2)
    # Frame 1: Pressed
    dc.ellipse([1, 19, 16, 34], fill=(120, 15, 15, 255), outline=C_GOLD_DARK, width=1)
    dc.line([(6, 24), (13, 31)], fill=(220, 220, 220, 255), width=2)
    dc.line([(13, 24), (6, 31)], fill=(220, 220, 220, 255), width=2)
    save_all_targets("button/btn_close.png", im_close)

    # Plus / Minus Buttons: 30x84 (3 frames of 30x28: Normal, Pressed, Disabled)
    im_pm = Image.new("RGBA", (30, 84), (0, 0, 0, 0))
    dpm = ImageDraw.Draw(im_pm)
    for idx, (b_fill, b_out, b_txt) in enumerate([
        ((220, 175, 45, 255), C_GOLD_MID, (40, 25, 5, 255)),
        ((160, 115, 15, 255), C_GOLD_DARK, (255, 245, 200, 255)),
        ((70, 80, 95, 255), (45, 55, 68, 255), (130, 140, 150, 255)),
    ]):
        yb = idx * 28
        dpm.rounded_rectangle([1, yb + 1, 28, yb + 26], radius=3, fill=b_fill, outline=b_out, width=1)
        dpm.line([(3, yb + 3), (26, yb + 3)], fill=(255, 255, 240, 180), width=1)
        dpm.line([(14, yb + 8), (14, yb + 20)], fill=b_txt, width=2)
        dpm.line([(8, yb + 14), (20, yb + 14)], fill=b_txt, width=2)
    save_all_targets("button/btn_plus_minus.png", im_pm)

# -------------------------------------------------------------
# 3. TAB ASSETS (tab_header.png)
# -------------------------------------------------------------
def gen_tab_assets():
    im_tab = Image.new("RGBA", (20, 40), (0, 0, 0, 0))
    dt = ImageDraw.Draw(im_tab)
    # Frame 0: Inactive
    dt.rounded_rectangle([0, 0, 19, 19], radius=3, fill=C_BG_MID, outline=C_BORDER_STEEL, width=1)
    dt.line([(1, 1), (18, 1)], fill=(50, 68, 95, 255), width=1)
    # Frame 1: Active
    dt.rounded_rectangle([0, 20, 19, 39], radius=3, fill=C_BG_LIGHT, outline=C_GOLD_MID, width=1)
    dt.line([(1, 21), (18, 21)], fill=C_GOLD_HI, width=1)
    dt.line([(1, 38), (18, 38)], fill=C_GOLD_MID, width=2)
    save_all_targets("tab/tab_header.png", im_tab)

# -------------------------------------------------------------
# 4. SLOTS & RARITIES
# -------------------------------------------------------------
def gen_slot_assets():
    # Canonical x1 slot: 20x40 (2 frames of 20x20)
    im_slot = Image.new("RGBA", (20, 40), (0, 0, 0, 0))
    ds = ImageDraw.Draw(im_slot)
    # Frame 0: Normal / Empty slot
    ds.rectangle([0, 0, 19, 19], fill=(14, 18, 28, 255), outline=C_BORDER_STEEL, width=1)
    ds.line([(1, 1), (18, 1)], fill=(8, 10, 16, 255), width=1)
    ds.line([(1, 1), (1, 18)], fill=(8, 10, 16, 255), width=1)
    for p in [(1, 1), (18, 1), (1, 18), (18, 18)]:
        ds.point(p, fill=C_GOLD_DARK)
    # Frame 1: Focus / Selected slot
    ds.rectangle([0, 20, 19, 39], fill=(22, 32, 50, 255), outline=C_GOLD_HI, width=1)
    ds.rectangle([1, 21, 18, 38], outline=C_GOLD_MID, width=1)
    for p in [(1, 21), (18, 21), (1, 38), (18, 38)]:
        ds.point(p, fill=(255, 255, 220, 255))
    save_all_targets("slot/slot_item.png", im_slot)

    # Large 28x56 slot for showcase
    im_slot28 = Image.new("RGBA", (28, 56), (0, 0, 0, 0))
    ds28 = ImageDraw.Draw(im_slot28)
    ds28.rectangle([0, 0, 27, 27], fill=(14, 18, 28, 255), outline=C_BORDER_STEEL, width=1)
    ds28.rectangle([1, 1, 26, 26], outline=(8, 12, 20, 255), width=1)
    ds28.ellipse([2, 2, 4, 4], fill=C_GOLD_DARK)
    ds28.ellipse([23, 2, 25, 4], fill=C_GOLD_DARK)
    ds28.ellipse([2, 23, 4, 25], fill=C_GOLD_DARK)
    ds28.ellipse([23, 23, 25, 25], fill=C_GOLD_DARK)
    ds28.rectangle([0, 28, 27, 55], fill=(22, 32, 50, 255), outline=C_GOLD_HI, width=1)
    ds28.rectangle([1, 29, 26, 54], outline=C_GOLD_MID, width=1)
    ds28.ellipse([2, 30, 4, 32], fill=(255, 255, 200, 255))
    ds28.ellipse([23, 30, 25, 32], fill=(255, 255, 200, 255))
    ds28.ellipse([2, 51, 4, 53], fill=(255, 255, 200, 255))
    ds28.ellipse([23, 51, 25, 53], fill=(255, 255, 200, 255))
    save_all_targets("slot/slot_item_28.png", im_slot28)

    # 6 Rarity Borders: 20x120 (6 frames of 20x20)
    rarity_colors = [
        ((149, 165, 166), (200, 210, 210)), # Common
        ((46, 204, 113), (120, 240, 160)),  # Uncommon
        ((52, 152, 219), (130, 210, 255)),  # Rare
        ((155, 89, 182), (215, 155, 245)),  # Epic
        ((243, 156, 18), (255, 220, 120)),  # Legendary
        ((231, 76, 60), (255, 145, 135)),   # Mythic
    ]
    im_rarity = Image.new("RGBA", (20, 120), (0, 0, 0, 0))
    dr = ImageDraw.Draw(im_rarity)
    for idx, (col_main, col_hi) in enumerate(rarity_colors):
        yb = idx * 20
        dr.rectangle([0, yb, 19, yb + 19], outline=col_main + (255,), width=1)
        dr.line([(1, yb + 1), (4, yb + 1)], fill=col_hi + (255,), width=1)
        dr.line([(1, yb + 1), (1, yb + 4)], fill=col_hi + (255,), width=1)
        dr.line([(15, yb + 1), (18, yb + 1)], fill=col_hi + (255,), width=1)
        dr.line([(18, yb + 1), (18, yb + 4)], fill=col_hi + (255,), width=1)
        dr.line([(1, yb + 18), (4, yb + 18)], fill=col_main + (255,), width=1)
        dr.line([(1, yb + 15), (1, yb + 18)], fill=col_main + (255,), width=1)
        dr.line([(15, yb + 18), (18, yb + 18)], fill=col_main + (255,), width=1)
        dr.line([(18, yb + 15), (18, yb + 18)], fill=col_main + (255,), width=1)
    save_all_targets("slot/slot_rarity.png", im_rarity)

    # 8 Equip Silhouette Icons: 18x152 (8 frames of 18x19)
    im_equip = Image.new("RGBA", (18, 152), (0, 0, 0, 0))
    de = ImageDraw.Draw(im_equip)
    sil_col = (85, 115, 155, 180)
    for idx in range(8):
        yb = idx * 19
        cx, cy = 9, yb + 9
        if idx == 0: # Cutlass
            de.line([(cx - 5, cy + 5), (cx + 4, cy - 4)], fill=sil_col, width=2)
            de.arc([cx + 1, cy - 7, cx + 6, cy - 2], start=180, end=360, fill=sil_col, width=2)
            de.line([(cx - 6, cy + 4), (cx - 4, cy + 6)], fill=sil_col, width=2)
        elif idx == 1: # Pirate Hat
            de.polygon([(cx - 7, cy + 3), (cx + 7, cy + 3), (cx + 5, cy - 1), (cx, cy - 5), (cx - 5, cy - 1)], fill=sil_col)
        elif idx == 2: # Coat
            de.polygon([(cx - 5, cy - 5), (cx + 5, cy - 5), (cx + 7, cy + 5), (cx - 7, cy + 5)], fill=sil_col)
            de.line([(cx, cy - 5), (cx, cy + 5)], fill=(15, 20, 30, 180), width=1)
        elif idx == 3: # Pants
            de.polygon([(cx - 5, cy - 4), (cx + 5, cy - 4), (cx + 4, cy + 6), (cx + 1, cy + 6), (cx, cy), (cx - 1, cy + 6), (cx - 4, cy + 6)], fill=sil_col)
        elif idx == 4: # Ring
            de.ellipse([cx - 4, cy - 4, cx + 4, cy + 4], outline=sil_col, width=2)
            de.point((cx, cy - 4), fill=C_GOLD_HI)
        elif idx == 5: # Necklace / Amulet
            de.arc([cx - 6, cy - 5, cx + 6, cy + 3], start=0, end=180, fill=sil_col, width=1)
            de.polygon([(cx, cy + 2), (cx + 3, cy + 5), (cx, cy + 7), (cx - 3, cy + 5)], fill=sil_col)
        elif idx == 6: # Glove
            de.polygon([(cx - 4, cy - 3), (cx + 4, cy - 3), (cx + 5, cy + 5), (cx - 5, cy + 5)], fill=sil_col)
            de.ellipse([cx - 5, cy, cx - 2, cy + 3], fill=sil_col)
        elif idx == 7: # Boots
            de.polygon([(cx - 4, cy - 5), (cx + 1, cy - 5), (cx + 1, cy + 2), (cx + 6, cy + 3), (cx + 6, cy + 6), (cx - 4, cy + 6)], fill=sil_col)
    save_all_targets("slot/slot_equip_bg.png", im_equip)

# -------------------------------------------------------------
# 5. BATTLE HUD ASSETS
# -------------------------------------------------------------
def gen_hud_assets():
    # 1. D-Pad Ring: 101x101
    im_ring = Image.new("RGBA", (101, 101), (0, 0, 0, 0))
    dr = ImageDraw.Draw(im_ring)
    cx, cy = 50, 50
    dr.ellipse([cx - 46, cy - 46, cx + 46, cy + 46], outline=C_BRONZE, width=3)
    dr.ellipse([cx - 43, cy - 43, cx + 43, cy + 43], outline=C_GOLD_MID, width=2)
    dr.ellipse([cx - 41, cy - 41, cx + 41, cy + 41], outline=C_GOLD_HI, width=1)
    dr.ellipse([cx - 38, cy - 38, cx + 38, cy + 38], fill=(12, 18, 30, 160), outline=C_BORDER_STEEL, width=1)
    for deg in [0, 45, 90, 135, 180, 225, 270, 315]:
        rad = math.radians(deg)
        x1 = cx + math.cos(rad) * 20
        y1 = cy + math.sin(rad) * 20
        x2 = cx + math.cos(rad) * 44
        y2 = cy + math.sin(rad) * 44
        dr.line([(x1, y1), (x2, y2)], fill=C_GOLD_DARK, width=2)
        px = cx + math.cos(rad) * 46
        py = cy + math.sin(rad) * 46
        dr.ellipse([px - 3, py - 3, px + 3, py + 3], fill=C_GOLD_MID, outline=C_GOLD_HI)
    save_all_targets("hud/dpad_ring.png", im_ring)

    # 2. D-Pad Knob: 40x40
    im_knob = Image.new("RGBA", (40, 40), (0, 0, 0, 0))
    dk = ImageDraw.Draw(im_knob)
    kx, ky = 20, 20
    dk.ellipse([kx - 18, ky - 18, kx + 18, ky + 18], fill=C_GOLD_DARK, outline=C_BRONZE, width=1)
    dk.ellipse([kx - 16, ky - 16, kx + 16, ky + 16], fill=C_GOLD_MID, outline=C_GOLD_HI, width=1)
    dk.ellipse([kx - 12, ky - 14, kx + 4, ky - 4], fill=(255, 245, 180, 140))
    dk.ellipse([kx - 8, ky - 8, kx + 8, ky + 8], fill=(220, 40, 40, 255), outline=C_GOLD_HI, width=1)
    dk.ellipse([kx - 5, ky - 6, kx - 1, ky - 2], fill=(255, 180, 180, 220))
    save_all_targets("hud/dpad_knob.png", im_knob)

    # 3. Fire Attack Button: 50x100
    im_fire = Image.new("RGBA", (50, 100), (0, 0, 0, 0))
    df = ImageDraw.Draw(im_fire)
    for idx, is_press in enumerate([False, True]):
        yb = idx * 50
        fx, fy = 25, yb + 25
        b_col = (140, 25, 25, 255) if is_press else (210, 45, 35, 255)
        r_col = C_GOLD_DARK if is_press else C_GOLD_HI
        df.ellipse([fx - 22, fy - 22, fx + 22, fy + 22], fill=b_col, outline=r_col, width=2)
        df.ellipse([fx - 19, fy - 19, fx + 19, fy + 19], outline=(255, 140, 80, 255), width=1)
        df.line([(fx - 12, fy + 12), (fx + 12, fy - 12)], fill=(255, 255, 255, 255), width=3)
        df.line([(fx + 12, fy + 12), (fx - 12, fy - 12)], fill=(255, 255, 255, 255), width=3)
        df.ellipse([fx - 3, fy - 3, fx + 3, fy + 3], fill=C_GOLD_HI)
    save_all_targets("hud/btn_attack.png", im_fire)

    # 4. Skill Slot: 30x60
    im_skill = Image.new("RGBA", (30, 60), (0, 0, 0, 0))
    ds = ImageDraw.Draw(im_skill)
    ds.ellipse([2, 2, 27, 27], fill=(16, 22, 34, 255), outline=C_BORDER_STEEL, width=2)
    ds.ellipse([5, 5, 24, 24], outline=C_GOLD_DARK, width=1)
    ds.ellipse([2, 32, 27, 57], fill=(28, 40, 62, 255), outline=C_GOLD_HI, width=2)
    ds.ellipse([5, 35, 24, 54], outline=(100, 210, 255, 255), width=1)
    save_all_targets("hud/btn_skill_slot.png", im_skill)

    # 5. Combat Gauges (HP, MP, EXP: 120x14)
    im_gbg = Image.new("RGBA", (120, 14), (0, 0, 0, 0))
    dg = ImageDraw.Draw(im_gbg)
    dg.rounded_rectangle([0, 0, 119, 13], radius=3, fill=(10, 14, 20, 255), outline=C_BORDER_STEEL, width=1)
    dg.line([(1, 1), (118, 1)], fill=(4, 6, 10, 255), width=1)
    save_all_targets("hud/gauge_hp_bg.png", im_gbg)

    im_ghp = Image.new("RGBA", (120, 14), (0, 0, 0, 0))
    dgh = ImageDraw.Draw(im_ghp)
    for y in range(2, 12):
        ratio = (y - 2) / 9.0
        r = int(255 - ratio * 110)
        g = int(60 - ratio * 45)
        b = int(75 - ratio * 55)
        dgh.line([(2, y), (117, y)], fill=(r, g, b, 255), width=1)
    dgh.line([(2, 2), (117, 2)], fill=(255, 180, 190, 220), width=1)
    save_all_targets("hud/gauge_hp_fill.png", im_ghp)

    im_gmp = Image.new("RGBA", (120, 14), (0, 0, 0, 0))
    dgm = ImageDraw.Draw(im_gmp)
    for y in range(2, 12):
        ratio = (y - 2) / 9.0
        r = int(20 - ratio * 15)
        g = int(210 - ratio * 100)
        b = int(255 - ratio * 60)
        dgm.line([(2, y), (117, y)], fill=(r, g, b, 255), width=1)
    dgm.line([(2, 2), (117, 2)], fill=(180, 240, 255, 220), width=1)
    save_all_targets("hud/gauge_mp_fill.png", im_gmp)

    im_ihp = Image.new("RGBA", (14, 14), (0, 0, 0, 0))
    dih = ImageDraw.Draw(im_ihp)
    dih.polygon([(7, 12), (1, 5), (3, 1), (7, 4), (11, 1), (13, 5)], fill=(230, 40, 50, 255), outline=C_GOLD_HI)
    save_all_targets("hud/icon_hp.png", im_ihp)

    im_imp = Image.new("RGBA", (14, 14), (0, 0, 0, 0))
    dim = ImageDraw.Draw(im_imp)
    dim.polygon([(7, 1), (12, 5), (10, 12), (4, 12), (2, 5)], fill=(0, 190, 245, 255), outline=C_GOLD_HI)
    save_all_targets("hud/icon_mp.png", im_imp)

# -------------------------------------------------------------
# 6. CONTROLS & WANTED
# -------------------------------------------------------------
def gen_control_assets():
    # Textfield border: 6x18 (3 frames 6x6)
    im_tf = Image.new("RGBA", (6, 18), (0, 0, 0, 0))
    dtf = ImageDraw.Draw(im_tf)
    dtf.rectangle([0, 0, 5, 5], fill=(12, 16, 24, 255), outline=C_BORDER_STEEL, width=1)
    dtf.rectangle([0, 6, 5, 11], fill=(18, 26, 40, 255), outline=C_GOLD_MID, width=1)
    dtf.rectangle([0, 12, 5, 17], fill=(8, 10, 14, 255), outline=(30, 40, 55, 255), width=1)
    save_all_targets("control/tf_border.png", im_tf)

    # Checkbox: 14x28 (2 frames 14x14)
    im_chk = Image.new("RGBA", (14, 28), (0, 0, 0, 0))
    dc = ImageDraw.Draw(im_chk)
    dc.rectangle([1, 1, 12, 12], fill=(16, 22, 34, 255), outline=C_GOLD_DARK, width=1)
    dc.rectangle([1, 15, 12, 26], fill=(24, 36, 54, 255), outline=C_GOLD_HI, width=1)
    dc.line([(3, 20), (6, 24)], fill=C_GOLD_HI, width=2)
    dc.line([(6, 24), (11, 17)], fill=C_GOLD_HI, width=2)
    save_all_targets("control/checkbox.png", im_chk)

    # Notification Banner: 120x24
    im_noti = Image.new("RGBA", (120, 24), (0, 0, 0, 0))
    dn = ImageDraw.Draw(im_noti)
    dn.rounded_rectangle([0, 0, 119, 23], radius=4, fill=(18, 25, 38, 240), outline=C_GOLD_MID, width=1)
    dn.line([(2, 2), (117, 2)], fill=C_GOLD_HI, width=1)
    save_all_targets("control/noti_banner.png", im_noti)

def main():
    print("Generating Theme 01 Neo Pirate Fantasy Assets...")
    gen_window_assets()
    gen_button_assets()
    gen_tab_assets()
    gen_slot_assets()
    gen_hud_assets()
    gen_control_assets()
    print("Theme 01 Assets successfully generated across all server and client folders!")

if __name__ == "__main__":
    main()
