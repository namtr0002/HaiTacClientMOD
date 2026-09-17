with open('test_DualTabScreen.java', 'r', encoding='utf-8') as f:
    lines = f.readlines()

new_lines = lines[:100] + [
    '    public DualTabScreen() {\n',
    '        cmdClose = new iCommand(T.close, 0, this);\n',
    '        cmdAction = new iCommand(T.select, 1, this);\n',
    '        cmdMenuAction = new iCommand("Menu", 2, this);\n',
    '        super.backCMD = cmdClose;\n',
    '        super.DB = cmdClose;\n',
    '        super.DA = cmdAction;\n',
    '        super.center = cmdMenuAction;\n',
    '    }\n'
] + lines[106:]

depth = 0
for idx, line in enumerate(new_lines):
    # ignore chars in strings/comments
    in_str = False
    for ch in line:
        if ch == '"': in_str = not in_str
        if not in_str:
            if ch == '{': depth += 1
            elif ch == '}':
                depth -= 1
                if depth == 0 and idx < len(new_lines) - 2:
                    print(f'Premature close at line {idx+1}: {repr(line)}')

print('Final depth:', depth)
with open('test_DualTabScreen.java', 'w', encoding='utf-8') as out:
    out.writelines(new_lines)
