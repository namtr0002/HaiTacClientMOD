import json
import os
import glob

with open('HaiTacTiHonTool/Data/Map/ServerData/maps.json', 'r', encoding='utf-8') as f:
    maps = json.load(f)

print(f"Total maps: {len(maps)}")

# Analyze binary tile files in Data/Map/ServerData/binary/
bin_dir = 'HaiTacTiHonTool/Data/Map/ServerData/binary'
data_files = glob.glob(os.path.join(bin_dir, '*_data'))
print(f"Total _data binary files: {len(data_files)}")

# Check template/98 item templates
template_dir = 'HaiTacTiHonTool/Data/Map/ServerData/template/98'
template_files = glob.glob(os.path.join(template_dir, '*'))
print(f"Total item template files: {len(template_files)}")

# Let's inspect some binary maps to understand tile indices and patterns
tiles_used_per_tileset = {}
item_usage_per_tileset = {}

for m in maps:
    mid = m.get('id')
    ts = m.get('tile_id', 0)
    data_file = os.path.join(bin_dir, f"{mid}_data")
    item_file = os.path.join(bin_dir, f"{mid}_item")
    
    if os.path.exists(data_file):
        with open(data_file, 'rb') as bf:
            content = bf.read()
            if len(content) >= 3:
                w, h, tid = content[0], content[1], content[2]
                tiles = list(content[3:])
                if ts not in tiles_used_per_tileset:
                    tiles_used_per_tileset[ts] = set()
                tiles_used_per_tileset[ts].update(tiles)
                
    if os.path.exists(item_file):
        with open(item_file, 'rb') as bf:
            content = bf.read()
            if len(content) >= 2:
                num_items = int.from_bytes(content[0:2], 'big')
                pos = 2
                for _ in range(num_items):
                    if pos + 6 <= len(content):
                        item_id = int.from_bytes(content[pos:pos+2], 'big', signed=True)
                        pos += 6
                        if ts not in item_usage_per_tileset:
                            item_usage_per_tileset[ts] = {}
                        item_usage_per_tileset[ts][item_id] = item_usage_per_tileset[ts].get(item_id, 0) + 1

print("\n--- Tiles Used Per Tileset ---")
for ts in sorted(tiles_used_per_tileset.keys()):
    print(f"Tileset {ts}: unique tiles = {sorted(tiles_used_per_tileset[ts])}")

print("\n--- Top Items Used Per Tileset ---")
for ts in sorted(item_usage_per_tileset.keys()):
    top_items = sorted(item_usage_per_tileset[ts].items(), key=lambda x: x[1], reverse=True)[:10]
    print(f"Tileset {ts}: top items = {top_items}")
