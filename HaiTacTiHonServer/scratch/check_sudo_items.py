import os, glob, re

items = [623, 624, 625, 626, 627, 628]
print(f"Checking items: {items}")

# 1. Search Java files
print("\n--- JAVA FILES ---")
src_dir = r"c:\DepLor\HTTH\Team\HaiTacTiHonServer\src\main\java"
for root, dirs, files in os.walk(src_dir):
    for f in files:
        if f.endswith(".java"):
            fpath = os.path.join(root, f)
            with open(fpath, "r", encoding="utf-8", errors="ignore") as fp:
                for line_idx, line in enumerate(fp, 1):
                    for it in items:
                        # match it as distinct number
                        if re.search(rf"\b{it}\b", line):
                            # filter out unrelated large numbers or dates
                            rel_path = os.path.relpath(fpath, src_dir)
                            print(f"{rel_path}:{line_idx} (Item {it}) -> {line.strip()[:120]}")

# 2. Search SQL files
print("\n--- SQL FILES ---")
sql_dir = r"c:\DepLor\HTTH\Team\HaiTacTiHonServer\data\sql"
sql_files = glob.glob(os.path.join(sql_dir, "*.sql"))
for sfile in sql_files:
    fname = os.path.basename(sfile)
    with open(sfile, "r", encoding="utf-8", errors="ignore") as fp:
        current_table = None
        for line_idx, line in enumerate(fp, 1):
            if "INSERT INTO" in line:
                m = re.search(r"INSERT INTO `?(\w+)`?", line)
                if m:
                    current_table = m.group(1)
            for it in items:
                if re.search(rf"\b{it}\b", line):
                    # Check if it looks like item reference
                    if current_table and current_table not in ['account', 'player', 'item4', 'item4_team', 'clan']:
                        print(f"{fname}:{line_idx} [{current_table}] (Item {it}) -> {line.strip()[:100]}")
                    elif "store" in line.lower() or "shop" in line.lower() or "drop" in line.lower():
                        print(f"{fname}:{line_idx} [Matched store/shop/drop] (Item {it}) -> {line.strip()[:100]}")
