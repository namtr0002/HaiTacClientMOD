# -*- coding: utf-8 -*-
clean_sql = "INSERT INTO `item4_info` VALUES (425,'giảm 1.5% chí mạng đối thủ\\ngiảm 1.5% xuyên giáp đối thủ\\ngiảm 1.5% phản đòn đối thủ\\ngiảm 1.5% né đòn đối thủ\\ngiảm 1.5% hiệu ứng đối thủ\\n1% sát thương % máu.');\n"

for path in ['../haitacz.sql', 'data/sql/haitacz.8sql.sql']:
    try:
        with open(path, 'r', encoding='utf-8', errors='ignore') as f:
            lines = f.readlines()
        mod = False
        for i, line in enumerate(lines):
            if 'USER_REQUEST' in line:
                print(f"Found corruption in {path} at line {i}: {line[:60]}")
                if '425' in line:
                    lines[i] = clean_sql
                    mod = True
                else:
                    # Strip everything starting from USER_REQUEST
                    idx = line.find('</USER_REQUEST>')
                    if idx == -1:
                        idx = line.find('<USER_REQUEST>')
                    if idx != -1:
                        lines[i] = line[:idx] + "');\n"
                        mod = True
        if mod:
            with open(path, 'w', encoding='utf-8') as f:
                f.writelines(lines)
            print(f"Successfully cleaned {path}")
        else:
            print(f"No corruption in {path}")
    except Exception as e:
        print(f"Error processing {path}: {e}")
