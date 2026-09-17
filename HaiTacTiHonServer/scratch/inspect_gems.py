import re

# Read data_items.txt or sql
items = {}
with open(r'c:\DepLor\HTTH\Team\HaiTacTiHonServer\data_items.txt', 'r', encoding='utf-8', errors='ignore') as f:
    header = f.readline()
    for line in f:
        parts = line.strip().split('\t')
        if len(parts) >= 2:
            try:
                item_id = int(parts[0])
                name = parts[1]
                items[item_id] = name
            except:
                pass

# Let's see all gems mentioned in Rebuild_Item.java:
gem_ranges = [
    (44, 49, "Cẩm thạch (Tăng P.Thủ / op 4)"),
    (50, 55, "Đá Topaz (Tăng Tấn công / op 1)"),
    (56, 61, "Tinh thể Ruby (Chí mạng / op 10)"),
    (62, 67, "Ngọc Lục Bảo (Xuyên giáp / op 13)"),
    (68, 73, "Đá Saphia (Kháng vật lý + Kháng phép / op 26, 27)"),
    (74, 79, "Thạch anh tím (Phản đòn / op 14)"),
    (221, 226, "Đá Hải Thạch"),
    (241, 270, "Đá Siêu Cấp C1-C30"),
    (324, 326, "Đá đặc biệt?"),
    (362, 373, "Đá Né tránh / op 12"),
    (647, 682, "Đá Siêu Cấp Cao Cấp (hoặc đá mới)"),
    (778, 789, "Đá 778-789?"),
    (12001, 12017, "Đá 12001-12017?")
]

print("=== GEMS FOUND IN DATA_ITEMS.TXT ===")
for start, end, label in gem_ranges:
    print(f"\n--- {label} ({start}-{end}) ---")
    for gid in range(start, end + 1):
        name = items.get(gid, "UNKNOWN")
        print(f"ID {gid}: {name}")
