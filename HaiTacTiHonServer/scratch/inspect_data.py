import os

base = r"c:\DepLor\HTTH\Team\HaiTacTiHonServer\data"
print("=== SERVER DATA SCAN ===")
for root, dirs, files in os.walk(base):
    # Only show directories that contain files
    if files:
        rel = os.path.relpath(root, base)
        print(f"{rel}: {len(files)} files")
