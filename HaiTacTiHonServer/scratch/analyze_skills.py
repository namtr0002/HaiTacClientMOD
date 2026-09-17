import re

with open('c:/DepLor/HTTH/Team/HaiTacTiHonServer/data/sql/haitacz16.sql', 'r', encoding='utf8') as f:
    text = f.read()

pattern = r"INSERT INTO `skill` VALUES \((\d+), (-?\d+), (-?\d+), (-?\d+), (-?\d+), (-?\d+), '([^']*)', (-?\d+), (-?\d+), (-?\d+), (-?\d+), (-?\d+), (-?\d+), (-?\d+), (-?\d+), '([^']*)', (-?\d+)"
matches = re.findall(pattern, text)
print(f"Total matches: {len(matches)}")

skills = {}
for m in matches:
    id_index = int(m[1])
    id_2 = int(m[2])
    name = m[6]
    lv_rq = int(m[16])
    if id_index not in skills:
        skills[id_index] = []
    skills[id_index].append((id_2, name, lv_rq))

for idx in sorted(skills.keys()):
    sample = skills[idx][0]
    lv_rqs = [x[2] for x in skills[idx]]
    print(f"Index {idx:4d} | ID_2: {sample[0]:4d} | Name: {sample[1]:30s} | Lv_RQs: {lv_rqs}")
