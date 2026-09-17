import os
import re

base_dir = r"c:\DepLor\HTTH\Team\HaiTacTiHonServer\src\main\java"

def search_text(pattern):
    print(f"=== Searching for: {pattern} ===")
    regex = re.compile(pattern, re.IGNORECASE)
    for root, dirs, files in os.walk(base_dir):
        for file in files:
            if file.endswith(".java"):
                full_path = os.path.join(root, file)
                try:
                    with open(full_path, "r", encoding="utf-8", errors="ignore") as f:
                        lines = f.readlines()
                        for idx, line in enumerate(lines):
                            if regex.search(line):
                                print(f"{file}:{idx+1}: {line.strip()}")
                except Exception as e:
                    pass

print("Search script ready.")
search_text("isWaitingOrUnstartedMatch")
