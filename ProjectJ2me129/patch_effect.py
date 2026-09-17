import re
import sys

with open(r'C:\DepLor\HTTH\Team\ProjectJ2me129\src\Effect_Skill.java', 'r', encoding='utf-8') as f:
    content = f.read()

# 1. Replace inline cases 404-407
pattern_inline = re.compile(r'case 404: \{\s*if \(this\.f > this\.fRemove\).*?case 408:', re.DOTALL)
new_inline = """case 404:
            this.updateEff404();
            return;
         case 405:
            this.updateEff405();
            return;
         case 406:
            this.updateEff406();
            return;
         case 407:
            this.updateEff407();
            return;
         case 412:
            this.updateEff412();
            return;
         case 408:"""

if pattern_inline.search(content):
    content = pattern_inline.sub(new_inline, content)
    print("Replaced inline cases 404-407.")
else:
    print("WARNING: Could not find inline cases 404-407!")

# 2. Find where createEff404 starts and remove everything from there to the end
idx = content.find("private void createEff404()")
if idx == -1:
    print("Could not find createEff404!")
    sys.exit(1)

content = content[:idx]

new_methods = """    private void createEff404() {
        DataSkillEff var25 = new DataSkillEff((short)16, 0);
        super.fRemove = var25.sequence.length;
        var25 = new DataSkillEff((short)17, 0);
        super.mframe = new int[1];
        super.mframe[0] = super.fRemove - var25.sequence.length;
        super.objFireMain.addEffSpec((short)14, (short)2000);
        super.objFireMain.addEffSpec((short)15, (short)2000);
        super.objFireMain.addEffSpec((short)16, (short)0);
        super.objFireMain.addEffSpec((short)13, (short)0);
        LoadMap.timeVibrateScreen = CRes.random(6, 20);
        GameScreen.addEffectEnd((short)112, 0, super.objFireMain.x, super.objFireMain.y, super.Dir, super.objFireMain);
    }

    private void updateEff404() {
        if (super.f > super.fRemove) {
            this.removeEff();
        }
        if (super.f == super.mframe[0]) {
            for (short var14 = 17; var14 < 19; var14++) {
                GameScreen.addHightDataeff(var14, this.objFireMain.x, this.objFireMain.y);
            }
        }
        if (super.f > super.mframe[0] + 6) {
            GameScreen.addEffectEnd((short)108, 5, this.objFireMain.x + CRes.random_Am_0(60), this.objFireMain.y - CRes.random(30), super.Dir, this.objFireMain);
            LoadMap.timeVibrateScreen = CRes.random(6, 20);
            GameScreen.addEffectEnd((short)112, 0, this.objFireMain.x, this.objFireMain.y, super.Dir, this.objFireMain);
        }
        if (this.isStop) {
            mSound.playSound();
            return;
        }
    }

    private void createEff405() {
        super.frame = 5;
        super.mframe = new int[super.frame];
        super.mframe[0] = 0;
        for (short var40 = 1; var40 < 5; var40++) {
            DataSkillEff var23 = new DataSkillEff((short)(var40 + 18), 0);
            super.mframe[var40] = var23.sequence.length + super.mframe[var40 - 1] + 1;
        }
        for (int var41 = 0; var41 < super.vecObjsBeFire.size(); var41++) {
            Object_Effect_Skill var51 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var41);
            if (var51 != null) {
                MainObject var59 = MainObject.get_Object(var51.ID, var51.tem);
                if (var59 != null) {
                    Point var24 = new Point();
                    var24.AZ = var59;
                    var24.frame = var41;
                    var24.f = 0 - var41 * 5;
                    var24.fRe = super.mframe[super.frame - 1];
                    this.VecSubEff.addElement(var24);
                }
            }
        }
    }

    private void updateEff405() {
        for (int var13 = 0; var13 < this.VecSubEff.size(); var13++) {
            Point var38 = (Point)this.VecSubEff.elementAt(var13);
            for (short var56 = 0; var56 < super.frame - 1; var56++) {
                if (var38.f == super.mframe[var56]) {
                    GameScreen.addHightDataeff((short)(var56 + 19), var38.AZ.x, var38.AZ.y);
                    if (var56 == 1) {
                        GameScreen.addEffectEnd((short)110, 0, var38.AZ.x + CRes.random_Am_0(15), var38.AZ.y + CRes.random_Am_0(5), super.Dir, var38.AZ);
                        GameScreen.addEffectEnd((short)112, 0, var38.AZ.x, var38.AZ.y, super.Dir, var38.AZ);
                        LoadMap.timeVibrateScreen = CRes.random(1, 5);
                    }
                    if (var56 == 3) {
                        LoadMap.timeVibrateScreen = CRes.random(6, 20);
                        GameScreen.addEffectEnd((short)112, 0, var38.AZ.x, var38.AZ.y, super.Dir, var38.AZ);
                    }
                }
            }
            if (var38.f > super.mframe[1]) {
                if (var38.f % 3 == 0) {
                    GameScreen.addEffectEnd((short)108, 5, var38.AZ.x + CRes.random_Am_0(10), var38.AZ.y - CRes.random(240), super.Dir, var38.AZ);
                    mSound.playSound();
                }
                if (var38.f > super.mframe[super.frame - 2] + 4) {
                    GameScreen.addEffectEnd((short)108, 5, var38.AZ.x + CRes.random_Am_0(60), var38.AZ.y - CRes.random(30), super.Dir, var38.AZ);
                }
            }
            var38.f++;
            if (var38.f >= var38.fRe) {
                this.VecSubEff.removeElement(var38);
                var13--;
            }
        }
    }

    private void createEff406() {
        DataSkillEff var26 = new DataSkillEff((short)16, 0);
        super.fRemove = var26.sequence.length;
        var26 = new DataSkillEff((short)17, 0);
        super.mframe = new int[3];
        super.mframe[0] = super.fRemove - var26.sequence.length;
        super.mframe[1] = super.mframe[0] + 5;
        super.mframe[2] = super.mframe[1] + 5;
        super.objFireMain.addEffSpec((short)14, (short)2000);
        super.objFireMain.addEffSpec((short)15, (short)2000);
        super.objFireMain.addEffSpec((short)16, (short)0);
        super.objFireMain.addEffSpec((short)13, (short)0);
        LoadMap.timeVibrateScreen = CRes.random(6, 20);
        GameScreen.addEffectEnd((short)112, 0, super.objFireMain.x, super.objFireMain.y, super.Dir, super.objFireMain);
    }

    private void updateEff406() {
        if (super.f > super.fRemove) {
            this.removeEff();
        }
        if (super.f == super.mframe[0]) {
            for (short var12 = 17; var12 < 19; var12++) {
                GameScreen.addHightDataeff(var12, this.objFireMain.x, this.objFireMain.y);
            }
        }
        if (super.f == super.mframe[1]) {
            GameScreen.addHightDataeff((short)38, this.objFireMain.x, this.objFireMain.y);
        }
        if (super.f == super.mframe[2]) {
            GameScreen.addHightDataeff((short)39, this.objFireMain.x, this.objFireMain.y);
            GameScreen.addHightDataeff((short)40, this.objFireMain.x, this.objFireMain.y);
        }
        if (super.f > super.mframe[0] + 6) {
            GameScreen.addEffectEnd((short)108, 5, this.objFireMain.x + CRes.random_Am_0(60), this.objFireMain.y - CRes.random(30), super.Dir, this.objFireMain);
            LoadMap.timeVibrateScreen = CRes.random(6, 20);
            GameScreen.addEffectEnd((short)112, 0, this.objFireMain.x, this.objFireMain.y, super.Dir, this.objFireMain);
        }
        if (this.isStop) {
            mSound.playSound();
            return;
        }
    }

    private void createEff407() {
        super.frame = 5;
        super.mframe = new int[super.frame];
        super.mframe[0] = 0;
        for (short var38 = 1; var38 < 5; var38++) {
            DataSkillEff var17 = new DataSkillEff((short)(var38 + 45), 0);
            super.mframe[var38] = var17.sequence.length + super.mframe[var38 - 1] + 1;
        }
        for (int var39 = 0; var39 < super.vecObjsBeFire.size(); var39++) {
            Object_Effect_Skill var50 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var39);
            if (var50 != null) {
                MainObject var58 = MainObject.get_Object(var50.ID, var50.tem);
                if (var58 != null) {
                    Point var18 = new Point();
                    var18.AZ = var58;
                    var18.frame = var39;
                    var18.f = 0 - var39 * 5;
                    var18.fRe = super.mframe[super.frame - 1];
                    this.VecSubEff.addElement(var18);
                }
            }
        }
    }

    private void updateEff407() {
        for (int var11 = 0; var11 < this.VecSubEff.size(); var11++) {
            Point var37 = (Point)this.VecSubEff.elementAt(var11);
            for (short var55 = 0; var55 < super.frame - 1; var55++) {
                if (var37.f == super.mframe[var55]) {
                    GameScreen.addHightDataeff((short)(var55 + 46), var37.AZ.x, var37.AZ.y);
                    if (var55 == 1) {
                        GameScreen.addEffectEnd((short)110, 0, var37.AZ.x + CRes.random_Am_0(15), var37.AZ.y + CRes.random_Am_0(5), super.Dir, var37.AZ);
                        GameScreen.addEffectEnd((short)112, 0, var37.AZ.x, var37.AZ.y, super.Dir, var37.AZ);
                        LoadMap.timeVibrateScreen = CRes.random(1, 5);
                    }
                    if (var55 == 3) {
                        LoadMap.timeVibrateScreen = CRes.random(6, 20);
                        GameScreen.addEffectEnd((short)112, 0, var37.AZ.x, var37.AZ.y, super.Dir, var37.AZ);
                    }
                }
            }
            if (var37.f > super.mframe[1]) {
                if (var37.f % 3 == 0) {
                    GameScreen.addEffectEnd((short)108, 5, var37.AZ.x + CRes.random_Am_0(10), var37.AZ.y - CRes.random(240), super.Dir, var37.AZ);
                    mSound.playSound();
                }
                if (var37.f > super.mframe[super.frame - 2] + 4) {
                    GameScreen.addEffectEnd((short)108, 5, var37.AZ.x + CRes.random_Am_0(60), var37.AZ.y - CRes.random(30), super.Dir, var37.AZ);
                }
            }
            var37.f++;
            if (var37.f >= var37.fRe) {
                this.VecSubEff.removeElement(var37);
                var11--;
            }
        }
    }

    private void createEff408() {
        DataSkillEff var14 = new DataSkillEff((short)26, 0);
        super.frame = var14.sequence.length / 2;
        this.VecSubEff.removeAllElements();
        int var57 = 0;
        for (int var15 = 0; var15 < super.vecObjsBeFire.size(); var15++) {
            Object_Effect_Skill var36 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var15);
            if (var36 != null) {
                MainObject var37 = MainObject.get_Object(var36.ID, var36.tem);
                if (var37 != null) {
                    if (this.objFireMain.x < var37.x) {
                        var57 = (var37.x - this.objFireMain.x - this.objFireMain.hOne) / 18 + 3;
                    } else {
                        var57 = (this.objFireMain.x - var37.x) / 18 + 3;
                    }
                }
                Point var49 = new Point();
                var49.x = this.objFireMain.x;
                var49.y = this.objFireMain.y;
                var49.fRe = var57;
                var49.dis = this.objFireMain.x < var37.x ? 2 : 0;
                var49.levelPaint = var15;
                this.VecSubEff.addElement(var49);
            }
        }
        super.fRemove = super.frame + var57 + 3 + var14.sequence.length;
        super.objFireMain.addEffSpec((short)25, (short)0);
        super.objFireMain.addEffSpec((short)26, (short)0);
    }

    private void updateEff408() {
        if (super.f > super.fRemove) {
            this.removeEff();
        }
        if (super.f > super.frame) {
            for (int var10 = 0; var10 < this.VecSubEff.size(); var10++) {
                Point var36 = (Point)this.VecSubEff.elementAt(var10);
                var36.x = var36.x + (var36.dis - 1) * 20;
                if (var36.dis > 0) {
                    GameScreen.addHightDataeff((short)(var36.levelPaint == 0 ? 36 : 34), var36.x, var36.y);
                } else {
                    GameScreen.addHightDataeff((short)(var36.levelPaint == 0 ? 36 : 34), var36.x, var36.y, true);
                }
                if (var36.f == var36.fRe - 2) {
                    for (int var54 = 0; var54 < super.vecObjsBeFire.size(); var54++) {
                        Object_Effect_Skill var64 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var54);
                        if (var64 != null) {
                            MainObject var68 = MainObject.get_Object(var64.ID, var64.tem);
                            if (var68 != null && var36.levelPaint == var54) {
                                var68.x = var36.x + (var36.dis - 1) * 10;
                                GameScreen.addHightDataeff((short)33, var68.x, var68.y);
                                GameScreen.addEffectEnd((short)112, 0, var68.x, var68.y, super.Dir, var68);
                                this.updateObjBeFire(var68);
                            }
                        }
                    }
                }
                if (var36.f == var36.fRe) {
                    GameScreen.addHightDataeff((short)28, var36.x, var36.y - 40);
                    GameScreen.addHightDataeff((short)28, var36.x, var36.y);
                    this.VecSubEff.removeElement(var36);
                    var10--;
                }
                var36.f++;
            }
        }
        if (this.isStop) {
            mSound.playSound();
            return;
        }
    }

    private void createEff409() {
        super.frame = 10;
        super.mframe = new int[super.frame];
        super.mframe[0] = 5;
        super.fRemove = super.frame * 2 + 10;
        for (short var9 = 1; var9 < super.frame; var9++) {
            super.mframe[var9] = super.mframe[var9 - 1] + 2;
        }
    }

    private void updateEff409() {
        if (super.f > super.fRemove) {
            this.removeEff();
        }
        for (short var9 = 0; var9 < super.frame; var9++) {
            if (super.f == super.mframe[var9]) {
                super.objFireMain.addEffSpec((short)(var9 + 30), (short)0);
                if (var9 == 0) {
                    LoadMap.timeVibrateScreen = CRes.random(1, 5);
                    GameScreen.addEffectEnd((short)112, 0, super.objFireMain.x, super.objFireMain.y, super.Dir, super.objFireMain);
                    super.am_duong = super.objFireMain.Dir == 2 ? 1 : -1;
                    for (int var35 = 0; var35 < super.vecObjsBeFire.size(); var35++) {
                        Object_Effect_Skill var53 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var35);
                        if (var53 != null) {
                            MainObject var63 = MainObject.get_Object(var53.ID, var53.tem);
                            if (var63 != null) {
                                GameScreen.addHightDataeff((short)33, var63.x, var63.y);
                                GameScreen.addEffectEnd((short)112, 0, var63.x, var63.y, super.Dir, var63);
                                var63.x = var63.x + super.am_duong * 20;
                                this.updateObjBeFire(var63);
                            }
                        }
                    }
                }
                if (var9 == super.frame - 1) {
                    super.objFireMain.addEffSpec((short)32, (short)0);
                    LoadMap.timeVibrateScreen = CRes.random(1, 5);
                }
            }
        }
    }

    private void createEff410() {
        DataSkillEff var11 = new DataSkillEff((short)26, 0);
        super.frame = var11.sequence.length / 2;
        this.VecSubEff.removeAllElements();
        int var55 = 0;
        for (int var38 = 0; var38 < super.vecObjsBeFire.size(); var38++) {
            Object_Effect_Skill var48 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var38);
            if (var48 != null) {
                MainObject var59 = MainObject.get_Object(var48.ID, var48.tem);
                if (var59 != null) {
                    if (this.objFireMain.x < var59.x) {
                        var55 = (var59.x - this.objFireMain.x - this.objFireMain.hOne) / 18 + 3;
                    } else {
                        var55 = (this.objFireMain.x - var59.x) / 18 + 3;
                    }
                }
                Point var18 = new Point();
                var18.x = this.objFireMain.x;
                var18.y = this.objFireMain.y;
                var18.fRe = var55;
                var18.dis = this.objFireMain.x < var59.x ? 2 : 0;
                var18.levelPaint = var38;
                this.VecSubEff.addElement(var18);
            }
        }
        super.fRemove = super.frame + var55 + 3 + var11.sequence.length;
        super.objFireMain.addEffSpec((short)25, (short)0);
        super.objFireMain.addEffSpec((short)26, (short)0);
    }

    private void updateEff410() {
        if (super.f > super.fRemove) {
            this.removeEff();
        }
        if (super.f > super.frame) {
            for (int var8 = 0; var8 < this.VecSubEff.size(); var8++) {
                Point var34 = (Point)this.VecSubEff.elementAt(var8);
                var34.x = var34.x + (var34.dis - 1) * 20;
                if (var34.dis > 0) {
                    GameScreen.addHightDataeff((short)(var34.levelPaint == 0 ? 35 : 36), var34.x, var34.y);
                } else {
                    GameScreen.addHightDataeff((short)(var34.levelPaint == 0 ? 35 : 36), var34.x, var34.y, true);
                }
                if (var34.f == var34.fRe - 2) {
                    for (int var52 = 0; var52 < super.vecObjsBeFire.size(); var52++) {
                        Object_Effect_Skill var62 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var52);
                        if (var62 != null) {
                            MainObject var5 = MainObject.get_Object(var62.ID, var62.tem);
                            if (var5 != null && var34.levelPaint == var52) {
                                var5.x = var34.x + (var34.dis - 1) * 10;
                                GameScreen.addHightDataeff((short)33, var5.x, var5.y);
                                GameScreen.addEffectEnd((short)112, 0, var5.x, var5.y, super.Dir, var5);
                                this.updateObjBeFire(var5);
                            }
                        }
                    }
                }
                if (var34.f == var34.fRe) {
                    GameScreen.addHightDataeff((short)28, var34.x, var34.y - 40);
                    GameScreen.addHightDataeff((short)28, var34.x, var34.y);
                    GameScreen.addHightDataeff((short)28, var34.x + (var34.dis - 1) * 15, var34.y - 60);
                    GameScreen.addHightDataeff((short)28, var34.x + (var34.dis - 1) * 15, var34.y + 20);
                    this.VecSubEff.removeElement(var34);
                    var8--;
                }
                var34.f++;
            }
        }
        if (this.isStop) {
            mSound.playSound();
            return;
        }
    }

    private void createEff411() {
        super.frame = 10;
        super.mframe = new int[super.frame];
        super.mframe[0] = 5;
        super.fRemove = super.frame * 2 + 10;
        for (short var7 = 1; var7 < super.frame; var7++) {
            super.mframe[var7] = super.mframe[var7 - 1] + 2;
        }
    }

    private void updateEff411() {
        if (super.f > super.fRemove) {
            this.removeEff();
        }
        for (short var7 = 0; var7 < super.frame; var7++) {
            if (super.f == super.mframe[var7]) {
                super.objFireMain.addEffSpec((short)(var7 + 42), (short)0);
                if (var7 == 0) {
                    LoadMap.timeVibrateScreen = CRes.random(1, 5);
                    GameScreen.addEffectEnd((short)112, 0, super.objFireMain.x, super.objFireMain.y, super.Dir, super.objFireMain);
                    super.am_duong = super.objFireMain.Dir == 2 ? 1 : -1;
                    for (int var2 = 0; var2 < super.vecObjsBeFire.size(); var2++) {
                        Object_Effect_Skill var3 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var2);
                        if (var3 != null) {
                            MainObject var4 = MainObject.get_Object(var3.ID, var3.tem);
                            if (var4 != null) {
                                GameScreen.addHightDataeff((short)45, var4.x, var4.y);
                                GameScreen.addEffectEnd((short)112, 0, var4.x, var4.y, super.Dir, var4);
                                var4.x = var4.x + super.am_duong * 20;
                                this.updateObjBeFire(var4);
                            }
                        }
                    }
                }
                if (var7 == super.frame - 1) {
                    super.objFireMain.addEffSpec((short)44, (short)0);
                    LoadMap.timeVibrateScreen = CRes.random(1, 5);
                }
            }
        }
    }

    private void createEff412() {
        super.frame = 10;
        super.mframe = new int[super.frame];
        super.mframe[0] = 5;
        super.fRemove = super.frame * 2 + 10;
        for (short var7 = 1; var7 < super.frame; var7++) {
            super.mframe[var7] = super.mframe[var7 - 1] + 2;
        }
    }

    private void updateEff412() {
        if (super.f > super.fRemove) {
            this.removeEff();
        }
        for (short var7 = 0; var7 < super.frame; var7++) {
            if (super.f == super.mframe[var7]) {
                super.objFireMain.addEffSpec((short)(var7 + 42), (short)0);
                if (var7 == 0) {
                    LoadMap.timeVibrateScreen = CRes.random(1, 5);
                    GameScreen.addEffectEnd((short)112, 0, super.objFireMain.x, super.objFireMain.y, super.Dir, super.objFireMain);
                    super.am_duong = super.objFireMain.Dir == 2 ? 1 : -1;
                    for (int var2 = 0; var2 < super.vecObjsBeFire.size(); var2++) {
                        Object_Effect_Skill var3 = (Object_Effect_Skill)super.vecObjsBeFire.elementAt(var2);
                        if (var3 != null) {
                            MainObject var4 = MainObject.get_Object(var3.ID, var3.tem);
                            if (var4 != null) {
                                GameScreen.addHightDataeff((short)45, var4.x, var4.y);
                                GameScreen.addEffectEnd((short)112, 0, var4.x, var4.y, super.Dir, var4);
                                var4.x = var4.x + super.am_duong * 20;
                                this.updateObjBeFire(var4);
                            }
                        }
                    }
                }
                if (var7 == super.frame - 1) {
                    super.objFireMain.addEffSpec((short)44, (short)0);
                    LoadMap.timeVibrateScreen = CRes.random(1, 5);
                }
            }
        }
    }
}
"""

content = content + new_methods

with open(r'C:\DepLor\HTTH\Team\ProjectJ2me129\src\Effect_Skill.java', 'w', encoding='utf-8') as f:
    f.write(content)

print("Patch applied successfully.")
