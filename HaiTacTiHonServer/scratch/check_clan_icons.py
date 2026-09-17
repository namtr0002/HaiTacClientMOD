import re

with open('HaiTacTiHonServer/src/main/java/template/ClanIcon.java', 'r', encoding='utf-8') as f:
    code = f.read()

entries = re.findall(r'ENTRY\.add\(new ClanIcon\((\d+),\s*"([^"]+)",\s*"([^"]+)",\s*(\d+),\s*\(byte\)\s*(\d+),\s*(true|false)\)\);', code)
print('Total entries in Java:', len(entries))
thuong = [e for e in entries if e[4] == '0' and e[5] == 'true']
vip = [e for e in entries if e[4] == '1' and e[5] == 'true']
nosell = [e for e in entries if e[5] == 'false']
print('Thuong (type 0, sell true):', len(thuong))
print('VIP (type 1, sell true):', len(vip))
print('No sell:', len(nosell))
for v in vip:
    print(f'VIP {v[0]}: {v[1]} ({v[3]} ruby)')
