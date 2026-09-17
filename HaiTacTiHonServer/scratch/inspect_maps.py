import pymysql
import json
import os

conn = pymysql.connect(host='localhost', user='root', password='', database='haitacz')
cur = conn.cursor(pymysql.cursors.DictCursor)
cur.execute('SELECT * FROM maps ORDER BY id')
maps = cur.fetchall()

print(f"Total maps in DB: {len(maps)}")
print(f"Min ID: {maps[0]['id']}, Max ID: {maps[-1]['id']}")

all_ids = [m['id'] for m in maps]
missing_ids = [i for i in range(all_ids[0], all_ids[-1] + 1) if i not in all_ids]
print(f"Missing IDs in range {all_ids[0]}..{all_ids[-1]}: {missing_ids}")

# Analyze binary files
data_dir = 'data/map/data'
bin_files = set(os.listdir(data_dir)) if os.path.exists(data_dir) else set()

tilesets = set()
backgrounds = set()
spec_maps = {}
map_categories = []

with open('scratch/maps_summary.txt', 'w', encoding='utf-8') as out:
    out.write(f"ID | Name | Zones | MaxP | Spec | TypeChg | IDBack | TileID | WxH | VgoCount | MobCount | NPCCount | HasBinary\n")
    out.write("-" * 120 + "\n")
    for m in maps:
        mid = m['id']
        name = m['name'] or ""
        vgos = json.loads(m['vgos']) if m.get('vgos') else []
        mobs = json.loads(m['mobs']) if m.get('mobs') else []
        npcs = json.loads(m['npcs']) if m.get('npcs') else []
        
        # Check map back
        map_back = m.get('MapBack')
        id_back = None
        if map_back:
            try:
                parsed_back = json.loads(map_back)
                if parsed_back:
                    id_back = parsed_back[0]
                    backgrounds.add(id_back)
            except:
                pass
        
        # Check binary file
        data_f = f"{mid}_data"
        item_f = f"{mid}_item"
        has_bin = (data_f in bin_files) and (item_f in bin_files)
        
        w, h, tile_id = 0, 0, 0
        if data_f in bin_files:
            try:
                with open(os.path.join(data_dir, data_f), 'rb') as fp:
                    content = fp.read()
                    if len(content) >= 3:
                        w = content[0]
                        h = content[1]
                        tile_id = content[2]
                        tilesets.add(tile_id)
            except Exception as e:
                pass
                
        spec = m.get('specMap', 0)
        spec_maps[spec] = spec_maps.get(spec, 0) + 1
        
        out.write(f"{mid:3d} | {name:30s} | {m.get('maxzone', 0):2d} | {m.get('maxplayer', 0):2d} | {spec:2d} | {m.get('typeChangeMap', 0):2d} | {str(id_back):6s} | {tile_id:3d} | {w:2d}x{h:2d} | {len(vgos):3d} | {len(mobs):3d} | {len(npcs):3d} | {has_bin}\n")

print(f"Summary written to scratch/maps_summary.txt")
print(f"Unique Tile IDs found: {sorted(list(tilesets))}")
print(f"Unique Background IDs found: {sorted(list(backgrounds))}")
print(f"SpecMap distribution: {spec_maps}")
