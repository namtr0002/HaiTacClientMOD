import os
import shutil
import time

RANGES = [
    ('item_map', 0, 999, 0),
    ('monster', 1000, 1999, 1000),
    ('potion', 2000, 2999, 2000),
    ('items', 3000, 3999, 3000),
    ('skill', 4000, 4499, 4000),
    ('skill_small', 4500, 4999, 4500),
    ('npc', 5000, 5999, 5000),
    ('questitem', 6000, 6499, 6000),
    ('material', 6500, 6999, 6500),
    ('clan', 7000, 7999, 7000),
    ('boat', 8000, 8999, 8000),
    ('dialog', 9000, 9999, 9000),
    ('char_part', 10000, 19999, 10000),
    ('fashion', 20000, 20999, 20000),
    ('skill_combo', 21000, 21999, 21000),
    ('clan_big', 22000, 22999, 22000),
    ('other_new', 23000, 23999, 23000),
    ('eff', 24000, 24999, 24000),
    ('efflow', 25000, 25999, 25000),
    ('char_part_v2', 26000, 999999, 26000),
]

def map_icon(raw_id):
    for name, lo, hi, off in RANGES:
        if lo <= raw_id <= hi:
            if name == 'char_part_v2':
                return 'char_part', 10000 + (raw_id - 26000)
            return name, raw_id - off
    return None, None

def run_migration(server_dir):
    icon_base = os.path.join(server_dir, 'data', 'icon')
    print(f"Starting icon migration in: {icon_base}")
    t0 = time.time()
    
    total_copied = 0
    total_skipped = 0
    stats = {}

    for zoom in ['1', '2', '3', '4']:
        zoom_src = os.path.join(icon_base, zoom)
        if not os.path.exists(zoom_src):
            print(f"Skipping missing zoom folder: {zoom_src}")
            continue

        files = [f for f in os.listdir(zoom_src) if f.endswith('.png')]
        print(f"\nProcessing zoom {zoom}: {len(files)} files...")
        
        for f in files:
            name = f[:-4]
            if not name.isdigit():
                continue
            raw_id = int(name)
            folder, new_id = map_icon(raw_id)
            if folder is None:
                print(f"Warning: unmapped icon ID {raw_id} in zoom {zoom}")
                continue

            target_dir = os.path.join(icon_base, folder, zoom)
            os.makedirs(target_dir, exist_ok=True)
            target_file = os.path.join(target_dir, f"{new_id}.png")
            src_file = os.path.join(zoom_src, f)

            # Copy if not exists or size differs
            if not os.path.exists(target_file) or os.path.getsize(target_file) != os.path.getsize(src_file):
                shutil.copy2(src_file, target_file)
                total_copied += 1
            else:
                total_skipped += 1

            if folder not in stats:
                stats[folder] = {}
            stats[folder][zoom] = stats[folder].get(zoom, 0) + 1

    elapsed = time.time() - t0
    print(f"\nMigration completed in {elapsed:.2f}s!")
    print(f"Total files copied: {total_copied}, skipped/up-to-date: {total_skipped}")
    print("\nSummary of icons per type:")
    print(f"{'Folder Name':<16} | {'Zoom 1':<8} | {'Zoom 2':<8} | {'Zoom 3':<8} | {'Zoom 4':<8}")
    print("-" * 55)
    for folder in sorted(stats.keys()):
        z1 = stats[folder].get('1', 0)
        z2 = stats[folder].get('2', 0)
        z3 = stats[folder].get('3', 0)
        z4 = stats[folder].get('4', 0)
        print(f"{folder:<16} | {z1:<8} | {z2:<8} | {z3:<8} | {z4:<8}")

if __name__ == '__main__':
    base_dir = r'c:\DepLor\HTTH\Team\HaiTacTiHonServer'
    run_migration(base_dir)
