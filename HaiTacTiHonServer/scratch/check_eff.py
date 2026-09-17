import os
ids = [2, 4, 8, 40, 41, 42, 43, 50, 51, 52, 53, 54, 55, 400, 401, 402, 403, 1050, 1051, 1052, 1053, 1054, 1055, 3108, 3109, 3110]
for i in ids:
    p = f"HaiTacTiHonServer/data/Effect/data/{i}"
    img = f"HaiTacTiHonServer/data/Effect/x1/{i}.png"
    print(f"ID {i:4d}: data={os.path.exists(p)} ({os.path.getsize(p) if os.path.exists(p) else 0:5d} B), img={os.path.exists(img)} ({os.path.getsize(img) if os.path.exists(img) else 0:6d} B)")
