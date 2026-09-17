import sqlite3, sys
sys.stdout.reconfigure(encoding='utf-8')
con = sqlite3.connect('data/game_data_cache.db')
cur = con.cursor()

ids = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 29, 158, 324, 415, 690, 691, 732, 754, 777, 802, 854, 864, 910, 911]
print('=== KNOWN ITEM4 IDS ===')
for i in ids:
    cur.execute('SELECT id, name, type FROM item4 WHERE id = ?', (i,))
    r = cur.fetchone()
    if r:
        print(f'item4 {r[0]}: {r[1]} (type={r[2]})')

print('\n=== ITEM7 IDS (0..20) ===')
for i in range(20):
    cur.execute('SELECT id, name FROM item7 WHERE id = ?', (i,))
    r = cur.fetchone()
    if r:
        print(f'item7 {r[0]}: {r[1]}')

print('\n=== ITEMS WITH "Thần" ===')
cur.execute('SELECT id, name FROM item4 WHERE name LIKE ?', ('%Thần%',))
for r in cur.fetchall():
    print(f'item4 {r[0]}: {r[1]}')

print('\n=== ITEMS WITH "Khảm" ===')
cur.execute('SELECT id, name FROM item4 WHERE name LIKE ?', ('%Khảm%',))
for r in cur.fetchall():
    print(f'item4 {r[0]}: {r[1]}')

print('\n=== ITEMS WITH "Vô Cực" ===')
cur.execute('SELECT id, name FROM item4 WHERE name LIKE ?', ('%Vô Cực%',))
for r in cur.fetchall():
    print(f'item4 {r[0]}: {r[1]}')

print('\n=== ITEMS WITH "Bảo Vệ" or "Khiên" ===')
cur.execute('SELECT id, name FROM item4 WHERE name LIKE ? OR name LIKE ?', ('%bảo vệ%', '%Khiên%'))
for r in cur.fetchall():
    print(f'item4 {r[0]}: {r[1]}')
cur.execute('SELECT id, name FROM item7 WHERE name LIKE ? OR name LIKE ?', ('%bảo vệ%', '%Khiên%'))
for r in cur.fetchall():
    print(f'item7 {r[0]}: {r[1]}')

print('\n=== ITEMS WITH "Rương" ===')
cur.execute('SELECT id, name FROM item4 WHERE name LIKE ?', ('%Rương%Đá%',))
for r in cur.fetchall():
    print(f'item4 {r[0]}: {r[1]}')
