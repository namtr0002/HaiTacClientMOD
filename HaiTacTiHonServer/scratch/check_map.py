import re

with open('data/sql/haitacz0.sql', 'r', encoding='utf-8', errors='ignore') as f:
    for line in f:
        if line.startswith('INSERT INTO `maps` VALUES (1, '):
            npcs = re.findall(r'\[(-?\d+),\\"([^\\"]+)\\",\\"([^\\"]+)\\"', line)
            for n in npcs:
                print(f"ID: {n[0]}, Name: {n[1]}, Role: {n[2]}")
            break
