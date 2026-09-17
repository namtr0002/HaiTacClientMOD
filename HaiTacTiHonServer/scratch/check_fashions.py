import sys, json, os, csv, io
sys.stdout.reconfigure(encoding='utf-8')

icon_dir = r'c:\DepLor\HTTH\Team\HaiTacTiHonServer\data\icon\4'
parts = {}
fashions = []

with open(r'c:\DepLor\HTTH\Team\HaiTacTiHonServer\data\sql\haitacz0.sql', 'r', encoding='utf-8', errors='ignore') as f:
    for line in f:
        line_str = line.strip()
        if line_str.startswith('INSERT INTO `parts` VALUES'):
            val_str = line_str[len('INSERT INTO `parts` VALUES'):].strip()
            if val_str.startswith('(') and val_str.endswith(');'):
                val_str = val_str[1:-2]
                parts_split = val_str.split(',', 2)
                if len(parts_split) == 3:
                    pid = int(parts_split[0].strip())
                    ptype = int(parts_split[1].strip())
                    pdata_str = parts_split[2].strip()
                    if pdata_str.startswith("'") and pdata_str.endswith("'"):
                        pdata_str = pdata_str[1:-1]
                    try:
                        pdata = json.loads(pdata_str)
                        parts[pid] = (ptype, pdata)
                    except Exception as e:
                        pass
        elif line_str.startswith('INSERT INTO `fashiontemplate` VALUES'):
            val_str = line_str[len('INSERT INTO `fashiontemplate` VALUES'):].strip()
            if val_str.startswith('(') and val_str.endswith(');'):
                val_str = val_str[1:-2]
                r = csv.reader(io.StringIO(val_str), delimiter=',', quotechar="'")
                for row in r:
                    if len(row) >= 5:
                        try:
                            fid = int(row[0].strip())
                            icon = int(row[1].strip())
                            name = row[2].strip()
                            mwear = json.loads(row[4].strip())
                            fashions.append((fid, icon, name, mwear))
                        except Exception as e:
                            pass

print(f'Parsed {len(parts)} parts, {len(fashions)} fashions')

for fid, icon, name, wear in fashions:
    part_status = []
    has_all = True
    cnt = 0
    for p in wear:
        if p > 0:
            if p in parts:
                ptype, pdata = parts[p]
                all_ic = True
                for item in pdata:
                    ic = item[0]
                    cnt += 1
                    if not os.path.exists(os.path.join(icon_dir, f'{ic}.png')):
                        all_ic = False
                        has_all = False
                part_status.append(f'P{p}(type={ptype},all_exist={all_ic})')
            else:
                part_status.append(f'P{p}(MISSING_IN_PARTS_TABLE)')
                has_all = False
    if has_all and cnt > 0:
        print(f'COMPLETE Fashion {fid} [{name}]: wear={wear}')
    elif any('type=' in s for s in part_status):
        print(f'PARTIAL Fashion {fid} [{name}]: {part_status}')
