import re, sys
sys.stdout.reconfigure(encoding='utf-8')

with open('HaiTacTiHonServer/data/sql/haitacz.sql', 'r', encoding='utf-8', errors='ignore') as f:
    for line in f:
        if 'INSERT INTO `maps`' in line:
            m_id = re.search(r'VALUES \((\d+),\'([^\']+)\'', line)
            if m_id:
                map_id = m_id.group(1)
                map_name = m_id.group(2)
                for match in re.finditer(r'\[(-?\d+),\"([^\"]*)\",\"([^\"]*)\"', line):
                    n_id, n_name, n_title = match.group(1), match.group(2), match.group(3)
                    if any(w in n_title.lower() or w in n_name.lower() for w in ['quán ăn', 'thức ăn']):
                        print(f'Map {map_id:>3} ({map_name}): NPC {n_id:>5} [{n_name}] [{n_title}]')
