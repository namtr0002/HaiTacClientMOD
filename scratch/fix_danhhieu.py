with open('ProjectJ2me129/src/DualTabScreen.java', 'r', encoding='utf-8') as f:
    code = f.read()

old_dh = '''public void initTabDanhHieu() {
int paneX = x + 10;
int paneY = y + 28;
int paneW = w - 20;
int paneH = h - 34;

int listX = paneX + 6;
int listY = paneY + 24;
int listW = isWide ? (paneW * 55 / 100) : (paneW - 12);
int listH = paneH - 30;

int dhCount = (Player.vecDanhHieu != null) ? Player.vecDanhHieu.size() : 0;
int itemH = 34;
int totalH = dhCount * itemH + 6;
int limY = totalH - listH;
if (limY < 0) limY = 0;

if (danhHieuList == null) {
if (Player.vecDanhHieu == null || Player.vecDanhHieu.size() == 0) {
GlobalService.getInstance().Send_DanhHieu((byte)0);
} else {
if (selectedDanhHieuIndex >= 0 && selectedDanhHieuIndex < Player.vecDanhHieu.size()) {
selectedDanhHieuInfo = (DanhHieuInfo)Player.vecDanhHieu.elementAt(selectedDanhHieuIndex);
} else if (Player.vecDanhHieu.size() > 0) {
selectedDanhHieuIndex = 0;
selectedDanhHieuInfo = (DanhHieuInfo)Player.vecDanhHieu.elementAt(0);
}
}
}'''

new_dh = '''    public void initTabDanhHieu() {
        int paneX = x + 10;
        int paneY = y + 28;
        int paneW = w - 20;
        int paneH = h - 34;

        int listX = paneX + 6;
        int listY = paneY + 24;
        int listW = isWide ? (paneW * 55 / 100) : (paneW - 12);
        int listH = paneH - 30;

        int dhCount = (Player.vecDanhHieu != null) ? Player.vecDanhHieu.size() : 0;
        int itemH = 34;
        int totalH = dhCount * itemH + 6;
        int limY = totalH - listH;
        if (limY < 0) limY = 0;

        if (danhHieuList == null) {
            danhHieuList = new ListNew(listX, listY, listW, listH, 0, 0, limY, true);
        } else {
            danhHieuList.x = listX;
            danhHieuList.y = listY;
            danhHieuList.maxW = listW;
            danhHieuList.maxH = listH;
            danhHieuList.AD = limY;
            if (danhHieuList.AB > limY) danhHieuList.AB = limY;
            if (danhHieuList.AB < 0) danhHieuList.AB = 0;
            if (danhHieuList.AC > limY) danhHieuList.AC = limY;
            if (danhHieuList.AC < 0) danhHieuList.AC = 0;
        }

        if (Player.vecDanhHieu == null || Player.vecDanhHieu.size() == 0) {
            GlobalService.getInstance().Send_DanhHieu((byte)0);
        } else {
            if (selectedDanhHieuIndex >= 0 && selectedDanhHieuIndex < Player.vecDanhHieu.size()) {
                selectedDanhHieuInfo = (DanhHieuInfo)Player.vecDanhHieu.elementAt(selectedDanhHieuIndex);
            } else if (Player.vecDanhHieu.size() > 0) {
                selectedDanhHieuIndex = 0;
                selectedDanhHieuInfo = (DanhHieuInfo)Player.vecDanhHieu.elementAt(0);
            }
        }
    }'''

if old_dh in code:
    code = code.replace(old_dh, new_dh)
    print('Fixed initTabDanhHieu!')
else:
    print('old_dh not matched')

with open('ProjectJ2me129/src/DualTabScreen.java', 'w', encoding='utf-8') as out:
    out.write(code)

import subprocess
res = subprocess.run('build_ultimate.bat', cwd=r'c:\DepLor\HTTH\Team\ProjectJ2me129', shell=True, capture_output=True, text=True)
err_lines = [l for l in (res.stdout + res.stderr).splitlines() if 'error:' in l or 'DualTabScreen.java:' in l]
print(f'Total error lines now: {len(err_lines)}')
for l in err_lines[:25]:
    print(l)
