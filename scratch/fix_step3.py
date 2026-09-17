with open('ProjectJ2me129/src/DualTabScreen.java', 'r', encoding='utf-8') as f:
    code = f.read()

# Fix switchTab
old_sw = '''        if (keepTopFocus || focusPane == 2) {
focusPane = 2;
} else {
hieuUngList.AD = limY;
if (hieuUngList.AB > limY) hieuUngList.AB = limY;
if (hieuUngList.AB < 0) hieuUngList.AB = 0;
if (hieuUngList.AC > limY) hieuUngList.AC = limY;
}
if (!isTabInitialized[curMainTab] || isTabDirty[curMainTab] || curMainTab == 2) {'''

new_sw = '''        if (keepTopFocus || focusPane == 2) {
            focusPane = 2;
        } else {
            focusPane = 1;
        }
        if (curMainTab == 4) {
            if (chucNangSubView == 1) {
                GlobalService.getInstance().Send_DanhHieu((byte)0);
            } else if (chucNangSubView == 2) {
                GlobalService.getInstance().Send_Pet((byte)3);
            }
        }
        if (!isTabInitialized[curMainTab] || isTabDirty[curMainTab] || curMainTab == 2) {'''

if old_sw in code:
    code = code.replace(old_sw, new_sw)
    print('Fixed switchTab!')
else:
    print('old_sw not matched')

# Fix corrupted fragment after updateSkillList
frag = '''super.DA = null;
super.center = null;
return;
} else if (curMainTab == 3) {
cmdAction.caption = "Chi Tiết";
cmdMenuAction.caption = "Chi Tiết";
super.DA = cmdAction;
super.center = cmdAction;
return;
} else if (curMainTab == 4) {
if (chucNangSubView == 0) {
cmdAction.caption = "Mở";
cmdMenuAction.caption = "Mở";
super.DA = cmdAction;
super.center = cmdAction;
return;
} else if (chucNangSubView == 1) {
if (selectedDanhHieuInfo != null && selectedDanhHieuInfo.actionButtons != null && selectedDanhHieuInfo.actionButtons.size() > 0) {
TitleActionBtn btn = (TitleActionBtn)selectedDanhHieuInfo.actionButtons.elementAt(0);
cmdAction.caption = btn.name;
cmdMenuAction.caption = btn.name;
super.DA = cmdAction;
super.center = cmdAction;
return;
}
super.DA = null;
super.center = null;
'''

if frag in code:
    code = code.replace(frag, '')
    print('Fixed fragment after updateSkillList!')
else:
    print('frag not matched')

with open('ProjectJ2me129/src/DualTabScreen.java', 'w', encoding='utf-8') as out:
    out.write(code)

import subprocess
res = subprocess.run('build_ultimate.bat', cwd=r'c:\DepLor\HTTH\Team\ProjectJ2me129', shell=True, capture_output=True, text=True)
err_lines = [l for l in (res.stdout + res.stderr).splitlines() if 'error:' in l or 'DualTabScreen.java:' in l]
print(f'Total error lines now: {len(err_lines)}')
for l in err_lines[:25]:
    print(l)
