import re

boss_maps = {5, 13, 21, 29, 37, 45, 53, 73, 87, 101, 102, 126, 127, 198}

with open('data/sql/haitacz.sql', 'r', encoding='utf-8', errors='ignore') as f:
    for line in f:
        if 'INSERT INTO `quests`' in line:
            # Match fields
            # VALUES (id, index_server, statusQuest, typeMainSub, typeActionQuest, name, idNpc, talk, typeQ, strNpcMap, lvRequest, strNhacNho, showDialog, data_quest, idMapHelp, idNpcSub, gift, infoFinish)
            # Find the tuples
            idx_values = line.find('VALUES (')
            if idx_values != -1:
                content = line[idx_values + 8 : -3] # strip 'VALUES (' and ');\n'
                # tokenize or regex
                m = re.match(r"(-?\d+),(-?\d+),(-?\d+),(-?\d+),(-?\d+),'([^']*)',(-?\d+),.*?,(-?\d+),(-?\d+),'(\[.*?\])',(-?\d+)", content)
                # Let's just find id and data_quest and idMapHelp
                parts = re.split(r",(?=(?:[^']*'[^']*')*[^']*$)", content)
                if len(parts) >= 16:
                    qid = int(parts[0])
                    qidx = int(parts[1])
                    st = int(parts[2])
                    mainSub = int(parts[3])
                    name = parts[5].strip("'")
                    try:
                        idMapHelp = int(parts[14])
                    except:
                        idMapHelp = -999
                    if mainSub == 0 and idMapHelp in boss_maps:
                        print(f"Boss Map {idMapHelp}: qid={qid}, idx={qidx}, st={st}, name={name}")
