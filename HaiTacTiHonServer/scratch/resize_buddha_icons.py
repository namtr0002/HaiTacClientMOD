import os
from PIL import Image

buddha_ids = [500, 501, 502, 503, 510, 511, 512, 520, 521, 522, 530, 531]
source_dir = "HaiTacTiHonServer/data/datafromserver/x4/eff"

# Base scale factor: 2.25x for colossal size
SCALE_X4 = 2.25

dest_dirs = [
    "HaiTacTiHonServer/data/icon",
    "HaiTacTiHonServer/release/data/icon"
]

for bid in buddha_ids:
    src_file = os.path.join(source_dir, f"g{bid}.png")
    if not os.path.exists(src_file):
        print(f"Warning: source file {src_file} does not exist!")
        continue
    
    orig_img = Image.open(src_file).convert("RGBA")
    orig_w, orig_total_h = orig_img.size
    num_frames = 4
    orig_frame_h = orig_total_h // num_frames
    
    # Calculate x4 target dimensions with exact frame alignment
    frame_w_x4 = int(round(orig_w * SCALE_X4))
    frame_h_x4 = int(round(orig_frame_h * SCALE_X4))
    # Make sure width and height are even for perfect rendering
    if frame_w_x4 % 2 != 0: frame_w_x4 += 1
    if frame_h_x4 % 2 != 0: frame_h_x4 += 1
    total_h_x4 = frame_h_x4 * num_frames
    
    img_x4 = orig_img.resize((frame_w_x4, total_h_x4), Image.Resampling.LANCZOS)
    
    # Generate x3, x2, x1 from x4 with frame alignment
    tiers = {}
    tiers[4] = img_x4
    
    for z, factor in [(3, 0.75), (2, 0.5), (1, 0.25)]:
        fw = int(round(frame_w_x4 * factor))
        fh = int(round(frame_h_x4 * factor))
        if fw % 2 != 0: fw += 1
        if fh % 2 != 0: fh += 1
        th = fh * num_frames
        tiers[z] = img_x4.resize((fw, th), Image.Resampling.LANCZOS)
    
    print(f"ID {bid}: orig=({orig_w}, {orig_total_h}) -> x4=({frame_w_x4}, {total_h_x4}), x3={tiers[3].size}, x2={tiers[2].size}, x1={tiers[1].size}")
    
    # Save to both server locations and all zoom tiers
    for base_dest in dest_dirs:
        for z in [1, 2, 3, 4]:
            z_dir = os.path.join(base_dest, str(z))
            os.makedirs(z_dir, exist_ok=True)
            
            # 1. Save with request ID (24000 + bid)
            req_path = os.path.join(z_dir, f"{24000 + bid}.png")
            tiers[z].save(req_path, "PNG")
            
            # 2. Save with raw ID (bid)
            raw_path = os.path.join(z_dir, f"{bid}.png")
            tiers[z].save(raw_path, "PNG")

print("All 12 Buddha effects resized and saved to data/icon successfully!")
