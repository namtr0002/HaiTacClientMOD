import os
import sys

sys.stdout.reconfigure(encoding='utf-8')

sql_path = 'c:/DepLor/HTTH/Team/HaiTacTiHonServer/data/sql/haitac (1).sql'

with open(sql_path, 'r', encoding='utf-8', errors='ignore') as f:
    lines = f.readlines()

current_table = ""
item4_rows = {}
item7_rows = {}
item4_info_rows = {}

for line in lines:
    line_s = line.strip()
    if line_s.startswith("CREATE TABLE `"):
        tname = line_s.split("`")[1]
        current_table = tname
    elif line_s.startswith("INSERT INTO `"):
        tname = line_s.split("`")[1]
        current_table = tname
    elif line_s.startswith("(") and current_table == "item4":
        parts = [p.strip().strip("'") for p in line_s.rstrip("),;").lstrip("(").split(",")]
        if len(parts) >= 12:
            try:
                iid = int(parts[0])
                name = parts[1]
                icon = int(parts[2])
                info_id = int(parts[3])
                nameuse = parts[11]
                item4_rows[iid] = (name, icon, info_id, nameuse)
            except Exception:
                pass
    elif line_s.startswith("(") and current_table == "item7":
        parts = [p.strip().strip("'") for p in line_s.rstrip("),;").lstrip("(").split(",")]
        if len(parts) >= 4:
            try:
                iid = int(parts[0])
                name = parts[1]
                item7_rows[iid] = name
            except Exception:
                pass
    elif line_s.startswith("(") and current_table == "item4_info":
        parts = line_s.rstrip("),;").lstrip("(").split(",", 1)
        if len(parts) >= 2:
            try:
                iid = int(parts[0].strip())
                info_text = parts[1].strip().strip("'")
                item4_info_rows[iid] = info_text
            except Exception:
                pass

print(f"Loaded {len(item4_rows)} item4, {len(item7_rows)} item7, {len(item4_info_rows)} item4_info")

print("\n--- Event 20.10 Items (590-596) in item4 ---")
for iid in range(590, 597):
    if iid in item4_rows:
        name, icon, info_id, nameuse = item4_rows[iid]
        info_text = item4_info_rows.get(info_id, "<NO INFO>")
        print(f"ID {iid}: {name} (Icon: {icon}, InfoID: {info_id}, NameUse: {nameuse})")
        print(f"   Description in item4_info:\n   {repr(info_text)}")
    else:
        print(f"ID {iid}: NOT FOUND in item4")

print("\n--- Potential reward items ---")
for iid, (name, icon, info_id, nameuse) in sorted(item4_rows.items()):
    info_text = item4_info_rows.get(info_id, "")
    keywords = ['hải thạch', 'khảm', 'x3', 'skill', 'chiêu', 'vé', 'kinh nghiệm', 'rương đại ác quỷ', 'khóa exp', 'xu hành trình', 'sao 8', 'gấu', 'hoa']
    if any(k in name.lower() for k in keywords):
        print(f"Item4 ID {iid}: {name} | InfoID: {info_id} | info: {info_text[:60]}")

print("\n--- Item7 matches ---")
for iid, name in sorted(item7_rows.items()):
    if any(k in name.lower() for k in ['sao', 'hải thạch', 'đá', 'bột', 'vé']):
        print(f"Item7 ID {iid}: {name}")
