import os
from PIL import Image

base = r'C:\DepLor\HTTH\Team\ProjectUnity129\Assets\Resources\res'
check_files = [
    'window/win_corner_tl.png',
    'window/win_header_crest.png',
    'hud/dpad_ring.png',
    'hud/btn_attack.png',
    'button/btn_frame_gold.png',
    'hud/gauge_hp_bg.png',
    'control/noti_banner.png',
    'button/btn_plus_minus.png',
    'slot/slot_equip_bg.png',
    'tab/tab_header.png',
    'control/checkbox.png'
]

for f in check_files:
    sizes = []
    for s in [1, 2, 3, 4]:
        p = os.path.join(base, f'x{s}', 'theme01', f)
        if os.path.exists(p):
            im = Image.open(p)
            sizes.append(f'x{s}={im.size}')
        else:
            sizes.append(f'x{s}=MISSING')
    print(f"{f}: {', '.join(sizes)}")
