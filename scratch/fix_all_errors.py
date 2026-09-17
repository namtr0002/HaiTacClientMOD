import sys, subprocess

with open('ProjectJ2me129/src/DualTabScreen.java', 'r', encoding='utf-8') as f:
    code = f.read()

# Fix 1: duplicate partial fields around line 21..27
dup_fields = '''public iCommand cmdClose;
public iCommand cmdAction;
public iCommand cmdMenuAction;

// Focus: 0 = Left Pane (Equip / Stats), 1 = Right/Content (Inven / Potential / Skills / Quests / Pets), 2 = Top Nav Tabs, 3 = Filter Bar (Tab 0)
public int focusPane
'''
if dup_fields in code:
    code = code.replace(dup_fields, '')
    print('Fixed duplicate fields!')
else:
    print('dup_fields not matched')

# Fix 2: getDefaultAttributeMInfo cases 0..4
old_def_att = '''public static MainInfoItem[] getDefaultAttributeMInfo(int index, short totalVal) {
switch (index) {
case 0: // Sức mạnh: Tăng tấn công, Xuyên giáp, Chí mạng
return new MainInfoItem[] {
new MainInfoItem((byte)1, totalVal * 5),
new MainInfoItem((byte)13, totalVal),
new MainInfoItem((byte)10, totalVal * 2)
case 4: // Nhanh nhẹn: Né tránh, Giảm hồi chiêu
return new MainInfoItem[] {
new MainInfoItem((byte)12, totalVal),
new MainInfoItem((byte)25, totalVal)
};
default:
return new MainInfoItem[0];
}
}'''

new_def_att = '''public static MainInfoItem[] getDefaultAttributeMInfo(int index, short totalVal) {
    switch (index) {
        case 0:
            return new MainInfoItem[] {
                new MainInfoItem((byte)1, totalVal * 5),
                new MainInfoItem((byte)13, totalVal),
                new MainInfoItem((byte)10, totalVal * 2)
            };
        case 1:
            return new MainInfoItem[] {
                new MainInfoItem((byte)4, totalVal * 5),
                new MainInfoItem((byte)26, totalVal),
                new MainInfoItem((byte)27, totalVal)
            };
        case 2:
            return new MainInfoItem[] {
                new MainInfoItem((byte)15, totalVal * 50),
                new MainInfoItem((byte)23, totalVal)
            };
        case 3:
            return new MainInfoItem[] {
                new MainInfoItem((byte)16, totalVal * 30),
                new MainInfoItem((byte)11, totalVal * 10),
                new MainInfoItem((byte)14, totalVal)
            };
        case 4:
            return new MainInfoItem[] {
                new MainInfoItem((byte)25, totalVal),
                new MainInfoItem((byte)12, totalVal)
            };
        default:
            return new MainInfoItem[0];
    }
}'''
if old_def_att in code:
    code = code.replace(old_def_att, new_def_att)
    print('Fixed getDefaultAttributeMInfo!')
else:
    print('old_def_att not matched')

# Fix 3: duplicated skillList block in initTab2
old_skill_dup = '''if (skillList == null) {
skillList = new ListNew(x + 10, y + 42, leftPaneW, h - 56, 0, 0, limSkillY, true);
} else {
skillList.x = x + 10;
skillList.y = y + 42;
skillList.maxW = leftPaneW;
skillList.maxH = h - 56;
if (skillList == null) {
skillList = new ListNew(x + 10, y + 42, leftPaneW, h - 56, 0, 0, limSkillY, true);
} else {'''

new_skill_dup = '''if (skillList == null) {
skillList = new ListNew(x + 10, y + 42, leftPaneW, h - 56, 0, 0, limSkillY, true);
} else {'''

if old_skill_dup in code:
    code = code.replace(old_skill_dup, new_skill_dup)
    print('Fixed skillList duplicate in initTab2!')
else:
    print('old_skill_dup not matched')

# Fix 4: duplicate corrupted getFeatureItemCount and getFeatureTitle
old_feat_corrupt = '''public int getFeatureItemCount() {
return 12;
}

public String getFeatureTitle(int index) {
switch (index) {
GlobalService.getInstance().Send_DanhHieu((byte)0);
}
if (Player.vecPet == null || Player.vecPet.size() == 0) {
GlobalService.getInstance().Send_Pet((byte)3);
}
}
'''
if old_feat_corrupt in code:
    code = code.replace(old_feat_corrupt, '')
    print('Fixed corrupted feature header!')
else:
    print('old_feat_corrupt not matched')

with open('ProjectJ2me129/src/DualTabScreen.java', 'w', encoding='utf-8') as out:
    out.write(code)

print('Saved! Testing compilation...')
res = subprocess.run('build_ultimate.bat', cwd=r'c:\DepLor\HTTH\Team\ProjectJ2me129', shell=True, capture_output=True, text=True)
err_lines = [l for l in (res.stdout + res.stderr).splitlines() if 'error:' in l or 'DualTabScreen.java:' in l]
print(f'Total error lines now: {len(err_lines)}')
for l in err_lines[:25]:
    print(l)
