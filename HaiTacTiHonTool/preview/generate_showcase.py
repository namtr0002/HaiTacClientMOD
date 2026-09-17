import os
import glob
from PIL import Image, ImageDraw, ImageFont

all_poses_dir = r"c:\DepLor\HTTH\Team\HaiTacTiHonTool\preview\all_poses"
out_dir = r"c:\DepLor\HTTH\Team\HaiTacTiHonTool\preview\hand_effect_analysis"
os.makedirs(out_dir, exist_ok=True)

# Selected representative poses to showcase
selected_poses = [
    (0, "Pose 00 - Stand", "char_pose_00_*.png", (-15, -39), (-4, -28)),
    (3, "Pose 03 - Run", "char_pose_03_*.png", (-14, -43), (-7, -28)),
    (10, "Pose 10 - Jump Guard", "char_pose_10_*.png", (-22, -46), (-12, -26)),
    (23, "Pose 23 - Punch Prep", "char_pose_23_*.png", (4, -50), (-12, -28)),
    (25, "Pose 25 - Punch Impact", "char_pose_25_*.png", (5, -17), (-18, -27)),
    (43, "Pose 43 - Rapid Punch", "char_pose_43_*.png", (3, -50), (-14, -30)),
]

ox = 110
oy = 165
cell_w = 240
cell_h = 280
cols = 3
rows = 2

showcase = Image.new("RGBA", (cell_w * cols + 20, cell_h * rows + 70), (15, 17, 26, 255))
draw = ImageDraw.Draw(showcase)

# Try default font
try:
    font_title = ImageFont.truetype("arial.ttf", 20)
    font_label = ImageFont.truetype("arial.ttf", 13)
    font_coord = ImageFont.truetype("arial.ttf", 11)
except:
    font_title = font_label = font_coord = ImageFont.load_default()

# Title banner
draw.rectangle([0, 0, cell_w * cols + 20, 50], fill=(24, 28, 44, 255))
draw.text((20, 14), "HTTH Free Fire Fist Effect (msg7476) - Hand Coordinate Tracking", fill=(255, 215, 0, 255), font=font_title)

for idx, (p_id, title, pattern, (r_dx, r_dy), (l_dx, l_dy)) in enumerate(selected_poses):
    c = idx % cols
    r = idx // cols
    pos_x = 10 + c * cell_w
    pos_y = 60 + r * cell_h

    # Card background
    draw.rectangle([pos_x, pos_y, pos_x + cell_w - 10, pos_y + cell_h - 10], fill=(22, 26, 38, 255), outline=(50, 60, 85, 255), width=1)

    # Title of card
    draw.text((pos_x + 12, pos_y + 10), title, fill=(100, 200, 255, 255), font=font_label)

    # Find file
    matches = glob.glob(os.path.join(all_poses_dir, pattern))
    if not matches:
        continue
    img_path = matches[0]
    char_img = Image.open(img_path).convert("RGBA")

    # Character coordinate on canvas:
    # Character origin in PNG is at (110, 165)
    # Right Hand Screen Coordinate:
    rx = ox + r_dx
    ry = oy + r_dy

    # Left Hand Screen Coordinate:
    lx = ox + l_dx
    ly = oy + l_dy

    # Overlay aura on char image
    overlay = Image.new("RGBA", char_img.size, (0, 0, 0, 0))
    ov_draw = ImageDraw.Draw(overlay)

    # Function to draw glowing aura / flame fist
    def draw_fist_aura(od, hx, hy, color_core, color_glow, label):
        # Outer glow layers
        for rad, alpha in [(14, 40), (10, 80), (7, 140), (4, 220)]:
            od.ellipse([hx - rad, hy - rad, hx + rad, hy + rad], fill=(*color_glow, alpha))
        # Inner fiery core
        od.ellipse([hx - 3, hy - 3, hx + 3, hy + 3], fill=(*color_core, 255))
        # Label indicator
        od.line([hx, hy, hx + 16, hy - 14], fill=(255, 255, 255, 200), width=1)
        od.rectangle([hx + 16, hy - 22, hx + 60, hy - 10], fill=(0, 0, 0, 180))
        od.text((hx + 18, hy - 22), label, fill=(255, 255, 255, 255), font=font_coord)

    # Right Hand (Main Hand / Flaming Orange-Red)
    draw_fist_aura(ov_draw, rx, ry, (255, 255, 200), (255, 80, 0), f"R: {r_dx},{r_dy}")

    # Left Hand (Off Hand / Cyan-Blue Lightning)
    draw_fist_aura(ov_draw, lx, ly, (200, 255, 255), (0, 160, 255), f"L: {l_dx},{l_dy}")

    # Composite char with aura
    composed = Image.alpha_composite(char_img, overlay)

    # Paste onto showcase
    showcase.paste(composed, (pos_x + 5, pos_y + 25), composed)

    # Info footer for each card
    footer_y = pos_y + cell_h - 42
    draw.text((pos_x + 12, footer_y), f"Right Fist (Main): dx={r_dx:+d}, dy={r_dy:+d}", fill=(255, 160, 80, 255), font=font_coord)
    draw.text((pos_x + 12, footer_y + 14), f"Left Fist (Off):   dx={l_dx:+d}, dy={l_dy:+d}", fill=(100, 210, 255, 255), font=font_coord)

out_file = os.path.join(out_dir, "00_FreeFire_Fist_Aura_Tracking_Showcase.png")
showcase.save(out_file)
print(f"Showcase image successfully generated at: {out_file}")
