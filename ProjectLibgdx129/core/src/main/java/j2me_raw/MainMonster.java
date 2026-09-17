
public class MainMonster extends MainObject {
    
    public boolean isGom = false;  // quái đang bị gom
    public int gomX, gomY;         // vị trí gom


    public int idCatMonster;
    public int AB;
    public int AC;
    public int AD;
    public int AE = 24;
    public int AF;
    public int AG;
    public int AH;
    public int AI;
    private long AS;
    public long AJ;
    public long AK;
    public static MyHashTable AL;
    public boolean AM = false;
    public boolean AN = false;
    public int AO;
    public long AP;
    public mVector vecEffMapSea = new mVector("MainMonster.vecEffMapSea");
    public mVector vecEffElite = new mVector("MainMonster.vecEffElite");
    private int AT = 0;

    static {
        new mVector("MainMonster.vecCatalogyMonSter");
        AL = new MyHashTable();
    }

    public MainMonster() {
    }

    public MainMonster(short var1, int var2, int var3) {
        super.ID = var1;
        super.toX = var2;
        super.toY = var3;
        super.toXNew = var2;
        super.toYNew = var3;
        super.typeObject = 1;
    }

    public static MainMonster createMonster(short var0, int var1, int var2, short var3) {
        CatalogyMonster var4;
        if ((var4 = AD(var3)) != null && var4.AM) {
            if (var4.AE == 1) {
                return new MonsterHuman(var0, var1, var2, var4);
            } else {
                return new MonsterWalk(var0, var1, var2, var4);
            }
        } else {
            return null;
        }
    }

    public final void AA(mGraphics var1, int var2) {
        if (!super.isTanHinh && !GameCanvas.lowGraphic) {
            if (super.MR == 1 || super.MR == 2) {
                byte var3 = 0;
                if (super.MO == 1) {
                    var3 = 2;
                    if (super.type_left_right == 2) {
                        var3 = -2;
                    }
                }

                AvMain.fraEffBoss.drawFrame((super.MR - 1) * 3 + GameCanvas.gameTick / 3 % 3, super.x + var3, super.y + var2, 0, 3, var1);
            }

            byte var10000 = super.BR;
        }
    }

    public void update() {
        if (super.timeBeginUpdateMove >= 0) {
            --super.timeBeginUpdateMove;
        }

        if (super.isDie && super.Action != 4) {
            super.Action = 4;
            this.AS = GameCanvas.timeNow;
        }

        if (super.skillCurrent == null) {
            this.AF();
        }

        if (super.Hp <= 0) {
            if (super.Action != 4) {
                ++this.AT;
            }

            if (this.AT > 20) {
                super.Action = 4;
                this.AS = GameCanvas.timeNow;
                this.AT = 0;
            }
        }

        if (isGom) {
            super.x = gomX;
            super.y = gomY;
            super.toX = gomX;
            super.toY = gomY;
            super.toXNew = gomX;
            super.toYNew = gomY;
            super.vx = 0;
            super.vy = 0;
        } else if (!GameScreen.player.isDongBangQuai111) {
            super.x += super.vx;
            super.y += super.vy;
        } else {
            super.vx = 0;
            super.vy = 0;
        }

        if (super.x < 0 || super.x > GameCanvas.loadmap.maxWMap) {
            super.x = GameCanvas.loadmap.maxWMap / 2;
        }

        if (!super.isInfo && GameCanvas.gameTick % 20 == 0 && (GameCanvas.timeNow - super.timeLoadInfo) / 1000L > 5L) {
            super.timeLoadInfo = GameCanvas.timeNow;
            GlobalService.getInstance().monster_info(super.ID);
        }

        CatalogyMonster var2;
        if (!super.NP && GameCanvas.gameTick % 20 == 0 && (var2 = AD(this.idCatMonster)).AM) {
            this.AA(var2);
        }

        this.AR();
        this.AQ();
        this.AF(super.LG);
    }

    public final void AB() {
        if (super.Action == 1 || super.Action == 0) {
            int var1;
            if ((var1 = MainObject.getDistance(super.x, super.y, GameScreen.player.x, GameScreen.player.y)) < LoadMap.wTile * 3) {
                if (super.x < GameScreen.player.x) {
                    super.type_left_right = 2;
                } else {
                    super.type_left_right = 0;
                }
            }

            if (var1 < 24 && super.Action == 0) {
                if (super.x < GameScreen.player.x) {
                    super.toX = super.x - 24;
                    return;
                }

                super.toX = super.x + 24;
            }
        }

    }

    public final void AF() {
        if (super.vecSkillFires.size() > 0) {
            super.skillCurrent = (Class_HO) super.vecSkillFires.elementAt(0);
            super.LO = super.skillCurrent.AA;
            this.resetBeginFire();
            super.vecSkillFires.removeElement(0);
            this.AP = GameCanvas.timeNow;
            if (!this.AM) {
                super.skillCurrent.AC = super.Skilldefault;
                this.setDataBeginSkill(super.skillCurrent.AC, super.skillCurrent.AB);
            }
        }

    }

    public final void a_(mGraphics var1) {
        var1.drawRegion((mImage) MainObject.imgShadow, super.x, super.y, 3);
    }

    public static CatalogyMonster AD(int var0) {
        CatalogyMonster var1;
        if ((var1 = (CatalogyMonster) AL.get("" + var0)) == null) {
            (var1 = new CatalogyMonster(var0)).AN = GameCanvas.timeNow;
            AL.put("" + var0, var1);
            GlobalService.getInstance().AH((byte) 1, (short) ((short) var0));
        }

        if ((GameCanvas.timeNow - var1.AN) / 1000L > 20L) {
            GlobalService.getInstance().AH((byte) 1, (short) ((short) var0));
        }

        return var1;
    }

    public final void AE(int var1) {
        int var2;
        if (CRes.abs(var2 = CRes.random_Am_0(3)) > 1) {
            --var1;
        }

        switch (super.Dir) {
            case 0:
                super.vy = var2;
                super.vx = -var1;
                break;
            case 1:
                super.vy = -var1;
                super.vx = var2;
                break;
            case 2:
                super.vy = var2;
                super.vx = var1;
                break;
            case 3:
                super.vy = var1;
                super.vx = var2;
        }

        if (super.vx == 0 && CRes.random(3) == 0) {
            this.AJ = 0L;
            super.Action = 0;
            super.vx = 0;
            super.vy = 0;
        }

        if (super.vx > 0) {
            super.Dir = 2;
        } else {
            super.Dir = 0;
        }
    }

    public final void c_() {
        if (super.plashNow != null) {
            int var1;
            if ((var1 = super.plashNow.AA()) != -1) {
                super.frame = var1;
                return;
            }

            super.plashNow = null;
            if (super.skillCurrent == null) {
                this.AF();
            }

            if (super.skillCurrent != null) {
                return;
            }
        }

        super.Action = 0;
        this.resetAction();
    }

    public final void Reveive() {
        if (super.BR != 19) {
            this.AJ();
        }

        this.AM = false;
        super.LO = null;
        super.isFlyDie = false;
        super.vecSkillFires.removeAllElements();
        super.vecEffBuff.removeAllElements();
        super.vecEffspec.removeAllElements();
        this.setResetWearing();
        if (super.MO == 0) {
            this.AY();
        }

        this.AC = 0;
        super.timeBeginUpdateMove = -1;
        super.LB = 0;
        this.AD = 10;
        this.AT = 0;
        super.f = 0;
        super.Action = 0;
        this.AJ = 0L;
        super.isDie = false;
        super.KF = 0;
        super.KG = 0;
        super.Hp = super.maxHp;
        if (isGom) {
            super.x = gomX;
            super.y = gomY;
            super.toX = gomX;
            super.toY = gomY;
            super.toXNew = gomX;
            super.toYNew = gomY;
            super.vx = 0;
            super.vy = 0;
        }
        this.AH = this.AI = 0;
    }

    public void AJ() {
        int var2 = 0;

        boolean var3;
        do {
            super.x = super.xAnchor + CRes.random_Am_0(48);
            super.y = super.yAnchor + CRes.random_Am_0(48);
            int var1;
            var3 = (var1 = GameCanvas.loadmap.AA(super.x, super.y)) != 1 && var1 != -1;
            ++var2;
            if (var2 > 15) {
                var3 = true;
                super.x = super.xAnchor;
                super.y = super.yAnchor;
            }
        } while (!var3);

    }

    public final void AK() {
        if (super.Action == 4) {
            if (GameCanvas.gameTick % 10 == 0 && super.timeRevice != -2) {
                if (super.timeRevice > 0) {
                    if (super.isDie && (GameCanvas.timeNow - this.AS) / 1000L > (long) super.timeRevice) {
                        this.Reveive();
                    }
                } else {
                    super.isRemove = true;
                }
            }

            if (super.BC != null) {
                super.BC = null;
            }
        }

    }

    public void AL() {
        if (super.skillCurrent != null && super.skillCurrent.AC != null && super.skillCurrent.AB != null) {
            GameScreen.AA(super.skillCurrent.AC, this, super.skillCurrent.AB);
            super.Action = 2;
            super.f = 0;
            super.skillCurrent = null;
        }

    }

    public final void beginDie(MainObject var1) {
        this.AD = 10;
        super.Hp = 0;
        super.Action = 4;
        this.AS = GameCanvas.timeNow;
        this.AY();
        if (super.skillCurrent != null && super.skillCurrent.AB != null && super.Skilldefault != null) {
            GameScreen.AA(super.Skilldefault, this, super.skillCurrent.AB);
        }

        for (; 0 < super.vecSkillFires.size(); super.vecSkillFires.removeElement(super.skillCurrent)) {
            super.skillCurrent = (Class_HO) super.vecSkillFires.elementAt(0);
            if (super.skillCurrent != null && super.skillCurrent.AB != null && super.Skilldefault != null) {
                GameScreen.AA(super.Skilldefault, this, super.skillCurrent.AB);
            }
        }

        super.skillCurrent = null;
        if (super.BR == 19) {
            GameScreen.addEffectEnd((short) 110, 0, super.x, super.y - 30, (byte) super.Dir, this);
            GameScreen.addEffectEnd((short) 110, 0, super.x, super.y - 45, (byte) super.Dir, this);
            super.isDie = true;
            super.timeDie = 0L;
        } else {
            if (var1 != null) {
                if (super.MO != 0) {
                    super.beginDie(var1);
                    return;
                }

                short var10001 = var1.ID;
                boolean var6 = false;
                short var2 = var10001;
                MainObject var7;
                if ((var7 = MainObject.get_Object((int) var2, (byte) 0)) != null && this != null) {
                    int var3 = 0;
                    int var4 = 0;
                    if (var7 != null) {
                        var3 = super.x - var7.x << 1;

                        for (var4 = super.y - var7.y << 1; MainObject.AD(var3, var4) > 20; var4 = (var4 << 1) / 3) {
                            var3 = (var3 << 1) / 3;
                        }
                    }

                    int var10 = var3;
                    int var10002 = var4;
                    byte var5 = 0;
                    byte var9 = 10;
                    var3 = var10002;
                    int var8 = var10;
                    this.AS = GameCanvas.timeNow;
                    super.Action = 4;
                    super.vx = 0;
                    super.vy = 0;
                    super.KF = var8;
                    super.KG = var3;
                    this.AH = var5;
                    this.AI = var5;
                    this.AD = var9;
                    super.isDie = false;
                }
            }

        }
    }

    public void setDataBeginSkill(MainSkill var1, mVector var2) {
        this.AL();
    }

    public final void AB(MainSkill var1, mVector var2) {
        if (super.skillCurrent != null) {
            super.skillCurrent.beginSkill();
        }

        super.vecSkillFires.addElement(new Class_HO(this, var2, var1));
        this.AM = true;
    }

    public final void a_() {
        if (super.Action != 4) {
            if (!GameCanvas.loadmap.mapLang() && (!this.BZ() || GameScreen.player.typePK != super.typePK)) {
                GameScreen.player.beginPlayerFirePoint();
            }

        }
    }

    public final void setFireObject(int var1) {
        if (super.Action != 4) {
            if (!GameCanvas.loadmap.mapLang() && (!this.BZ() || GameScreen.player.typePK != super.typePK)) {
                GameScreen.player.beginPlayerFire(var1);
            }

        }
    }

    public final int AM() {
        return super.BR;
    }
}
