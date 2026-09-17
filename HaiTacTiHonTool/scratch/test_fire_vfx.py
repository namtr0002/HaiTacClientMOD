import os
import math
import random
import numpy as np
from PIL import Image, ImageDraw, ImageFilter

def create_fire_fist_overlay(width, height, fist_positions, frame_time, is_punch=False, punch_tip=None):
    """
    fist_positions: list of (x, y, scale_factor, intensity)
    """
    # Create high-res RGBA image for smooth rendering
    overlay = Image.new('RGBA', (width, height), (0, 0, 0, 0))
    draw = ImageDraw.Draw(overlay)
    
    # 1. Embers / Sparks layer
    rng = random.Random(int(frame_time * 100))
    for fx, fy, fscale, fintensity in fist_positions:
        # Radial heat glow behind fist
        glow_radius = int(24 * fscale * fintensity)
        glow_surf = Image.new('RGBA', (glow_radius * 2, glow_radius * 2), (0, 0, 0, 0))
        glow_draw = ImageDraw.Draw(glow_surf)
        for r in range(glow_radius, 0, -2):
            alpha = int(45 * (1.0 - r / glow_radius) * fintensity)
            # Warm orange-red
            glow_draw.ellipse([glow_radius - r, glow_radius - r, glow_radius + r, glow_radius + r],
                              fill=(255, 60, 10, alpha))
        overlay.alpha_composite(glow_surf, (int(fx - glow_radius), int(fy - glow_radius)))
        
        # Inner warm gold glow
        gold_radius = int(14 * fscale * fintensity)
        gold_surf = Image.new('RGBA', (gold_radius * 2, gold_radius * 2), (0, 0, 0, 0))
        gold_draw = ImageDraw.Draw(gold_surf)
        for r in range(gold_radius, 0, -1):
            alpha = int(80 * (1.0 - r / gold_radius) * fintensity)
            gold_draw.ellipse([gold_radius - r, gold_radius - r, gold_radius + r, gold_radius + r],
                              fill=(255, 180, 20, alpha))
        overlay.alpha_composite(gold_surf, (int(fx - gold_radius), int(fy - gold_radius)))

    # 2. Procedural Flame Tongues licking upwards
    flame_surf = Image.new('RGBA', (width, height), (0, 0, 0, 0))
    fdraw = ImageDraw.Draw(flame_surf)
    
    for fx, fy, fscale, fintensity in fist_positions:
        # Several flame tongues (5 to 7 tongues per fist)
        num_tongues = 7
        for t in range(num_tongues):
            angle_jitter = (t - (num_tongues - 1) / 2.0) * 0.28
            phase = frame_time * 8.0 + t * 1.3
            tongue_h = (22 + 14 * math.sin(phase) + 6 * math.cos(phase * 1.7)) * fscale * fintensity
            tongue_w = (9 + 3 * math.cos(phase * 0.9)) * fscale
            
            # Root at fist
            rx = fx + math.sin(angle_jitter) * 6 * fscale
            ry = fy + math.cos(angle_jitter) * 4 * fscale
            
            # Tip licks upwards with turbulence
            sway = math.sin(phase * 1.4 + t) * (8 * fscale)
            tx = rx + math.sin(angle_jitter) * tongue_h * 0.4 + sway
            ty = ry - tongue_h
            
            # Control points for curved flame blade
            cp1_x = rx - tongue_w * 0.8
            cp1_y = ry - tongue_h * 0.4
            cp2_x = rx + tongue_w * 0.8
            cp2_y = ry - tongue_h * 0.4
            
            # Outer flame tongue (red/orange)
            poly_outer = [
                (rx - tongue_w, ry),
                (cp1_x, cp1_y),
                (tx, ty),
                (cp2_x, cp2_y),
                (rx + tongue_w, ry)
            ]
            fdraw.polygon(poly_outer, fill=(255, 50, 10, int(180 * fintensity)))
            
            # Inner bright flame tongue (gold/yellow)
            inner_h = tongue_h * 0.65
            inner_w = tongue_w * 0.55
            ity = ry - inner_h
            itx = rx + (tx - rx) * 0.65
            poly_inner = [
                (rx - inner_w, ry),
                (rx - inner_w * 0.7, ry - inner_h * 0.5),
                (itx, ity),
                (rx + inner_w * 0.7, ry - inner_h * 0.5),
                (rx + inner_w, ry)
            ]
            fdraw.polygon(poly_inner, fill=(255, 200, 30, int(220 * fintensity)))
            
            # Core white flame root
            core_h = tongue_h * 0.35
            core_w = tongue_w * 0.3
            poly_core = [
                (rx - core_w, ry),
                (rx, ry - core_h),
                (rx + core_w, ry)
            ]
            fdraw.polygon(poly_core, fill=(255, 255, 200, int(240 * fintensity)))

    # Blur flame slightly for natural organic fire look
    flame_surf = flame_surf.filter(ImageFilter.GaussianBlur(radius=1.2))
    overlay.alpha_composite(flame_surf)
    
    # 3. Floating Embers / Sparks (Rising up)
    for fx, fy, fscale, fintensity in fist_positions:
        for i in range(12):
            seed = i * 17 + int(frame_time * 5)
            r_val = math.sin(seed * 12.3) * 0.5 + 0.5
            progress = (frame_time * 2.5 + i * 0.15) % 1.0
            
            spark_x = fx + (math.sin(seed * 7.1) * 18 + math.sin(progress * 6.28) * 8) * fscale
            spark_y = fy - (progress * 42 * fscale)
            spark_size = max(1, int((3.0 - progress * 2.0) * fscale))
            spark_alpha = int((1.0 - progress) * 230 * fintensity)
            
            # Color shifts from yellow to intense orange/red as it rises
            sr = 255
            sg = int(220 * (1.0 - progress * 0.8))
            sb = int(30 * (1.0 - progress))
            draw.ellipse([spark_x - spark_size, spark_y - spark_size,
                          spark_x + spark_size, spark_y + spark_size],
                         fill=(sr, sg, sb, spark_alpha))

    # 4. If Punch Strike: Add Dragon Fire Burst & Shockwave forward
    if is_punch and punch_tip:
        px, py = punch_tip
        burst_surf = Image.new('RGBA', (width, height), (0, 0, 0, 0))
        bdraw = ImageDraw.Draw(burst_surf)
        
        # Conical fire surge blasting forward to the left
        surge_len = 65
        for s in range(surge_len, 0, -5):
            frac = s / float(surge_len)
            cone_w = int(28 * (1.0 - frac * 0.5))
            bdraw.polygon([
                (px + 10, py - 10),
                (px - s, py - cone_w),
                (px - s - 10, py),
                (px - s, py + cone_w),
                (px + 10, py + 10)
            ], fill=(255, int(60 + 120 * frac), int(10 * frac), int(160 * (1.0 - frac * 0.4))))

        # Shockwave blast rings
        for ring in [20, 35, 52]:
            bdraw.ellipse([px - ring - 15, py - ring, px - ring + 15, py + ring],
                          outline=(255, 230, 100, 180), width=2)

        # Dragon flame teeth / streaks shooting forward
        for k in range(8):
            k_angle = (k - 3.5) * 0.25
            slen = 40 + 25 * math.sin(k * 2.1 + frame_time * 10)
            tx = px - math.cos(k_angle) * slen
            ty = py + math.sin(k_angle) * slen * 0.6
            bdraw.line([(px, py), (tx, ty)], fill=(255, 255, 180, 230), width=3)
            bdraw.line([(px, py), (tx - 5, ty)], fill=(255, 120, 20, 200), width=5)

        burst_surf = burst_surf.filter(ImageFilter.GaussianBlur(radius=1.0))
        overlay.alpha_composite(burst_surf)

    return overlay
