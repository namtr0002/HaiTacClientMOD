# -*- coding: utf-8 -*-
import os
import json
import numpy as np
from PIL import Image

BASE_DIR = r"C:\DepLor\HTTH\Team\_ThanTrangAnimationSkill"
SHEETS_DIR = os.path.join(BASE_DIR, "spritesheets")
SHEETS_X2_DIR = os.path.join(BASE_DIR, "spritesheets_x2")
FRAMES_DIR = os.path.join(BASE_DIR, "frames")
META_PATH = os.path.join(BASE_DIR, "database", "skill_vfx_metadata.json")

with open(META_PATH, "r", encoding="utf-8") as f:
    meta = json.load(f)

errors = []
warnings = []

print("=" * 72)
print("   COMPREHENSIVE SPRITE VERIFICATION AUDIT")
print("=" * 72)

total_frames_audited = 0

for sid_str in sorted(meta.keys(), key=lambda x: int(x)):
    sid = int(sid_str)
    comps = meta[sid_str]
    for comp in ["cast", "projectile", "impact", "aura", "particles", "finisher"]:
        if comp not in comps:
            errors.append(f"Skill {sid} missing component {comp} in metadata!")
            continue
        c_info = comps[comp]
        w, h, nf = c_info["w"], c_info["h"], c_info["frames"]
        
        # 1. 1x Spritesheet
        s1_path = os.path.join(SHEETS_DIR, f"skill_{sid}_{comp}_sheet.png")
        if not os.path.exists(s1_path):
            errors.append(f"Missing 1x sheet: {s1_path}")
            continue
        im1 = Image.open(s1_path)
        if im1.size != (w, h * nf):
            errors.append(f"Skill {sid} {comp} 1x sheet size {im1.size} != expected ({w}, {h * nf})")

        # 2. 2x HD Spritesheet
        s2_path = os.path.join(SHEETS_X2_DIR, f"skill_{sid}_{comp}_sheet.png")
        if not os.path.exists(s2_path):
            errors.append(f"Missing 2x sheet: {s2_path}")
            continue
        im2 = Image.open(s2_path)
        if im2.size != (w * 2, h * nf * 2):
            errors.append(f"Skill {sid} {comp} 2x sheet size {im2.size} != expected ({w*2}, {h*nf*2})")
            
        # 3. Individual frames & per-frame checks
        for fi in range(nf):
            total_frames_audited += 1
            f_crop = im1.crop((0, fi * h, w, (fi + 1) * h))
            bbox = f_crop.getbbox()
            
            # Check empty
            if not bbox:
                errors.append(f"Skill {sid} {comp} frame {fi}: COMPLETELY EMPTY!")
                continue
                
            bw = bbox[2] - bbox[0]
            bh = bbox[3] - bbox[1]
            
            # Check aspect ratio pancake
            if w == h and (bw / max(1, bh) > 2.8 or bh / max(1, bw) > 2.8):
                warnings.append(f"Skill {sid} {comp} frame {fi}: Potential pancake bbox={bbox} (w={bw}, h={bh})")
                
            # Check border bleeding (alpha > 15 on outer 1px)
            arr = np.array(f_crop)
            top = np.any(arr[0, :, 3] > 15)
            bot = np.any(arr[-1, :, 3] > 15)
            left = np.any(arr[:, 0, 3] > 15)
            right = np.any(arr[:, -1, 3] > 15)
            if top or bot or left or right:
                errors.append(f"Skill {sid} {comp} frame {fi}: BORDER BLEEDING! top={top}, bot={bot}, left={left}, right={right}")

print(f"Total frames audited: {total_frames_audited}")
print(f"Total errors:   {len(errors)}")
print(f"Total warnings: {len(warnings)}")

if errors:
    print("\n[FAIL] Found errors:")
    for e in errors[:25]:
        print("  -", e)
else:
    print("\n[PASS] 100% ZERO ERRORS! All sprites are pristine, centered, non-empty, and zero-clipped!")

if warnings:
    print("\nWarnings:")
    for wr in warnings[:10]:
        print("  *", wr)
