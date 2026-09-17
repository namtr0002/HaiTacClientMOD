public class MainObject extends AvMain {
   public String BA = "";
   public String name = "";
   public String BC = null;
   public String BD = "";
   public boolean BE;
   public String BF = "";
   public static String[] BG;
   public static int BH = 0;
   public short ID;
   public short IdIcon;
   public short IDMainShiper;
   public short BL;
   public int[][] BM;
   public int[] BN;
   public short[] BO;
   public int[] BP;
   public Item_Drop[] BQ;
   public byte BR;
   public short BS = -1;
   public short BT = -1;
   public short BU = -1;
   public short BV = -1;
   public short BW = -1;
   public short BX = -1;
   public short BY = -1;
   private short AA = -1;
   private short AB = -1;
   private short AC = -1;
   private short AD = -1;
   public int x;
   public int y;
   public int CB;
   public int CC;
   public int dx;
   public int dy;
   public int CF;
   public int CG;
   public int CH;
   public int vySea;
   public int vx;
   public int vy;
   public int wOne;
   public int hOne;
   public int CN;
   public int CO;
   public int ySort;
   public int CQ;
   public int frame;
   public int CS = -1;
   public int toX;
   public int toY;
   public int toXNew;
   public int toYNew;
   public int CX;
   public int CY;
   public int xAnchor;
   public int yAnchor;
   public int IM;
   public int IN;
   public int IO;
   public int hIconFocus;
   public int IQ;
   public int IR;
   public int IS;
   private int AE = 60;
   public int IT;
   public int timeBeginUpdateMove = 0;
   private int AF;
   public int IV;
   public int IW = 0;
   public int IX;
   public int IY;
   public int IZ;
   public int KA = 0;
   private int AG;
   public int tickAfterSkill = 50;
   public int KC;
   public int KD;
   public int KE;
   public int KF;
   public int KG;
   public int Hp;
   public int KI;
   public int Mp;
   public int maxHp;
   public int maxMp;
   public int Lv;
   public int KN;
   public int KO;
   public int KP;
   public int KQ;
   public int LvThongThao;
   public int KS;
   public int rankWanted = -1;
   public int Action;
   public int Dir;
   public int type_left_right;
   public int KX;
   private int AH = 0;
   public int timeRevice;
   public int KZ = 0;
   public int LA;
   public int LB;
   public int LC;
   public int LD;
   private int AI = 0;
   public int LE;
   public int f;
   public int LG = 30;
   private int AJ = 0;
   public int timeSafe;
   public long timeBeginSafe = 0L;
   public long LJ = 0L;
   public int LK = 60;
   public int LL = -1;
   public byte LM = 0;
   public MainSkill Skilldefault;
   public MainObject LO;
   public BigBossLittleGraden LP;
   public static mImage imgShadow;
   private static mImage AK;
   private static mImage AL;
   public MyHashTable LR = new MyHashTable();
   public PopupChat LS;
   public Class_HO skillCurrent = null;
   public Plash plashNow;
   public byte downSpeedWater;
   private byte AM;
   public byte LW;
   public byte levelPerfect = 0;
   public byte LY = -1;
   public byte LZ = 0;
   public byte MA;
   public byte clazz;
   public byte typeObject;
   public byte typeSpecMonSter;
   public byte colorName;
   public byte typePK = -1;
   public byte typePirate = -1;
   public byte MH;
   public byte MI;
   public byte MJ;
   public byte typePlayer;
   public byte indexTeam = 0;
   public byte MM = 0;
   public byte typeActionBoat;
   public byte MO;
   public byte MP;
   public byte isDonotShowHat = 0;
   public byte MR;
   public byte MS;
   public byte MT = 0;
   public byte MU = 0;
   public byte isDonotShowWeaponF = 0;
   public byte MW = 0;
   public byte MX = 0;
   private byte AN;
   private byte AO;
   private byte AP;
   public byte MY = 0;
   public byte thanhtichPvP = -1;
   public byte thanhtichLv = -1;
   public byte typeEfffashion = -1;
   public long timeLoadInfo;
   public long timeDie;
   public boolean AQ;
   public boolean isTanHinh;
   public boolean NF = true;
   public boolean NG = false;
   public boolean NH = true;
   public boolean NI = false;
   public boolean NJ = false;
   public boolean isFlyDie = false;
   private boolean AR = false;
   public boolean isDie = false;
   public boolean isRemove = false;
   public boolean NN = false;
   public boolean isInfo = false;
   public boolean NP = false;
   public boolean NQ;
   public boolean NR;
   public short[] posTransRoad;
   public short[] NT;
   public mVector vecSkillFires = new mVector("Mainobject.vecSkillFires");
   public mVector vecEffBuff = new mVector("Mainobject.vecEffBuff");
   public mVector vecEffBuffNew = new mVector("Mainobject.vecEffBuffNew");
   public mVector vecEffspec = new mVector("Mainobject.vecEffspec");
   public mVector vecTrade = new mVector("Mainobject.vecTrade");
   private mVector veceffFas = new mVector("Mainobject.veceffFas");
   public static DataHairServer[] dataEffHair;
   public static DataHairServer[] PA;
   public byte PB = 0;
   public int Ruby = 0;
   public int PD = 0;
   public long Beri = 0L;
   public int PF = 0;
   public int PG = 0;
   public static int[][] mPosMapTrain = new int[][]{{5, 10}, {18, 9}, {24, 12}};
   public MainClan clan;
   public MainSudo PJ;
   public Boat boatSea;
   public mVector PL = new mVector();
   private int AT = 0;
   private int AU = 0;
   private int AV = 0;
   public int PM = 0;
   public long PN;
   private int AW = 0;
   private int AX;
   private static int[][][] AY = new int[][][]{{{0, -5, -42}, {0, -11, -18}, {0, -8, -30}, {0, -15, -39}, {0, -9, -44}, {0, -8, -44}, {0, -29, -57}}, {{0, -5, -41}, {0, -11, -18}, {0, -8, -29}, {0, -15, -38}, {0, -9, -43}, {0, -8, -43}, {0, -29, -57}}, {{0, -11, -40}, {1, -18, -18}, {1, -11, -31}, {1, -18, -42}, {0, -15, -42}, {0, -14, -42}, {0, -29, -57}}, {{0, -11, -41}, {2, -6, -18}, {2, -7, -30}, {2, -14, -43}, {0, -15, -43}, {0, -14, -43}, {0, -29, -57}}, {{0, -11, -43}, {3, -6, -22}, {3, -17, -37}, {3, -17, -45}, {0, -15, -45}, {0, -14, -45}, {0, -29, -57}}, {{0, -11, -41}, {4, -14, -20}, {3, -17, -35}, {4, -17, -43}, {0, -15, -43}, {0, -14, -43}, {0, -29, -57}}, {{0, -11, -42}, {5, -5, -19}, {2, -7, -31}, {2, -14, -44}, {0, -15, -44}, {0, -14, -44}, {0, -29, -57}}, {{0, -11, -44}, {6, -9, -23}, {4, -13, -35}, {5, -17, -46}, {0, -15, -46}, {0, -14, -46}, {0, -29, -57}}, {{1, -7, -40}, {7, -12, -17}, {5, -15, -29}, {6, -22, -47}, {0, -11, -42}, {0, -10, -42}, {0, -29, -57}}, {{1, -7, -39}, {7, -12, -17}, {5, -15, -28}, {6, -22, -46}, {0, -11, -41}, {0, -10, -41}, {0, -29, -57}}, {{3, -6, -39}, {7, -12, -17}, {5, -15, -28}, {6, -22, -46}, {0, -10, -41}, {0, -9, -41}, {0, -29, -57}}, {{2, -7, -42}, {6, -9, -18}, {5, -15, -31}, {7, -22, -43}, {0, -11, -44}, {0, -10, -44}, {0, -29, -57}}, {{1, -7, -41}, {5, -4, -16}, {5, -15, -30}, {8, -22, -42}, {0, -11, -43}, {0, -10, -43}, {0, -29, -57}}, {{1, -10, -41}, {8, -17, -18}, {2, -6, -30}, {9, -18, -32}, {0, -14, -43}, {0, -13, -43}, {0, -29, -57}}, {{1, -10, -42}, {6, -8, -18}, {2, -6, -31}, {9, -18, -33}, {0, -14, -44}, {0, -13, -44}, {0, -29, -57}}, {{2, -13, -39}, {9, -21, -18}, {6, -19, -28}, {10, -1, -31}, {0, -17, -41}, {0, -16, -41}, {0, -29, -57}}, {{2, -13, -39}, {14, -9, -18}, {6, -19, -28}, {10, -1, -31}, {0, -17, -41}, {0, -16, -41}, {0, -29, -57}}, {{4, -13, -40}, {9, -21, -18}, {7, -19, -29}, {11, 5, -17}, {1, -17, -42}, {1, -16, -42}, {0, -29, -57}}, {{4, -13, -40}, {14, -9, -18}, {7, -19, -29}, {11, 5, -17}, {1, -17, -42}, {1, -16, -42}, {0, -29, -57}}, {{1, -10, -41}, {8, -17, -18}, {8, -2, -46}, {12, -9, -55}, {0, -14, -43}, {0, -13, -43}, {0, -29, -57}}, {{1, -10, -41}, {6, -8, -18}, {8, -2, -46}, {12, -9, -55}, {0, -14, -43}, {0, -13, -43}, {0, -29, -57}}, {{1, -10, -41}, {8, -17, -18}, {9, -2, -48}, {13, -4, -56}, {0, -14, -43}, {0, -13, -43}, {0, -29, -57}}, {{1, -10, -41}, {6, -8, -18}, {9, -2, -48}, {13, -4, -56}, {0, -14, -43}, {0, -13, -43}, {0, -29, -57}}, {{1, -12, -39}, {8, -17, -18}, {10, -4, -32}, {14, 4, -50}, {0, -16, -41}, {0, -15, -41}, {0, -29, -57}}, {{1, -12, -39}, {6, -8, -18}, {10, -4, -32}, {14, 4, -50}, {0, -16, -41}, {0, -15, -41}, {0, -29, -57}}, {{2, -10, -40}, {9, -21, -18}, {11, -18, -29}, {15, 5, -17}, {0, -14, -42}, {0, -13, -42}, {0, -29, -57}}, {{2, -10, -40}, {14, -9, -18}, {11, -18, -29}, {15, 5, -17}, {0, -14, -42}, {0, -13, -42}, {0, -29, -57}}, {{1, -5, -44}, {10, -21, -27}, {12, -9, -32}, {16, -16, -41}, {0, -9, -46}, {0, -8, -46}, {0, -29, -57}}, {{2, -7, -44}, {11, -23, -29}, {13, -12, -32}, {17, -17, -41}, {0, -11, -46}, {0, -10, -46}, {0, -29, -57}}, {{2, -7, -44}, {12, -28, -25}, {13, -12, -32}, {17, -17, -41}, {0, -11, -46}, {0, -10, -46}, {0, -29, -57}}, {{2, -7, -44}, {13, -17, -37}, {13, -12, -32}, {17, -17, -41}, {0, -11, -46}, {0, -10, -46}, {0, -29, -57}}, {{1, -7, -41}, {8, -17, -18}, {14, -16, -29}, {18, -25, -42}, {0, -11, -43}, {0, -10, -43}, {0, -29, -57}}, {{1, -7, -41}, {14, -9, -17}, {14, -16, -29}, {18, -25, -42}, {0, -11, -43}, {0, -10, -43}, {0, -29, -57}}, {{1, -4, -41}, {8, -17, -18}, {15, -11, -34}, {19, -16, -47}, {0, -8, -43}, {0, -7, -43}, {0, -29, -57}}, {{1, -4, -41}, {14, -9, -17}, {15, -11, -34}, {19, -16, -47}, {0, -8, -43}, {0, -7, -43}, {0, -29, -57}}, {{1, -7, -41}, {8, -17, -18}, {16, -19, -30}, {20, -28, -43}, {0, -11, -43}, {0, -10, -43}, {0, -29, -57}}, {{1, -7, -41}, {14, -9, -17}, {16, -19, -30}, {20, -28, -43}, {0, -11, -43}, {0, -10, -43}, {0, -29, -57}}, {{1, -4, -41}, {8, -17, -18}, {17, -15, -34}, {21, -20, -48}, {0, -8, -43}, {0, -7, -43}, {0, -29, -57}}, {{1, -4, -41}, {14, -9, -17}, {17, -15, -34}, {21, -20, -48}, {0, -8, -43}, {0, -7, -43}, {0, -29, -57}}, {{2, -7, -41}, {8, -17, -18}, {18, -13, -29}, {22, -26, -31}, {0, -11, -43}, {0, -10, -43}, {0, -29, -57}}, {{2, -7, -41}, {14, -9, -17}, {18, -13, -29}, {22, -26, -31}, {0, -11, -43}, {0, -10, -43}, {0, -29, -57}}, {{2, -2, -41}, {9, -19, -18}, {19, -9, -29}, {23, -19, -39}, {0, -6, -43}, {0, -5, -43}, {0, -29, -57}}, {{2, -2, -41}, {14, -8, -17}, {19, -9, -29}, {23, -19, -39}, {0, -6, -43}, {0, -5, -43}, {0, -29, -57}}, {{1, -13, -39}, {1, -18, -17}, {10, -5, -32}, {14, 3, -50}, {0, -17, -41}, {0, -16, -41}, {0, -29, -57}}, {{1, -13, -40}, {2, -6, -18}, {10, -5, -33}, {14, 3, -51}, {0, -17, -42}, {0, -16, -42}, {0, -29, -57}}, {{1, -13, -42}, {3, -6, -22}, {10, -5, -35}, {14, 3, -53}, {0, -17, -44}, {0, -16, -44}, {0, -29, -57}}, {{1, -13, -40}, {4, -14, -19}, {10, -5, -33}, {14, 3, -51}, {0, -17, -42}, {0, -16, -42}, {0, -29, -57}}, {{1, -13, -41}, {5, -5, -19}, {10, -5, -34}, {14, 3, -52}, {0, -17, -43}, {0, -16, -43}, {0, -29, -57}}, {{1, -13, -44}, {6, -9, -23}, {10, -5, -37}, {14, 3, -55}, {0, -17, -46}, {0, -16, -46}, {0, -29, -57}}, {{3, -3, -41}, {8, -17, -18}, {17, -14, -34}, {21, -19, -48}, {0, -7, -43}, {0, -6, -43}, {0, -29, -57}}, {{3, -4, -41}, {14, -9, -17}, {17, -15, -34}, {21, -20, -48}, {0, -8, -43}, {0, -7, -43}, {0, -29, -57}}, {{3, -4, -42}, {5, -5, -19}, {17, -15, -35}, {21, -20, -49}, {0, -8, -44}, {0, -7, -44}, {0, -29, -57}}, {{2, -13, -39}, {1, -18, -17}, {6, -19, -28}, {10, -1, -31}, {0, -17, -41}, {0, -16, -41}, {0, -29, -57}}, {{4, -13, -40}, {1, -18, -17}, {7, -19, -29}, {11, 5, -17}, {1, -17, -42}, {1, -16, -42}, {0, -29, -57}}, {{2, -10, -40}, {1, -18, -17}, {11, -18, -29}, {15, 5, -17}, {0, -14, -42}, {0, -13, -42}, {0, -29, -57}}, {{0, -17, -44}, {11, -23, -29}, {4, -19, -35}, {5, -23, -46}, {0, -21, -46}, {0, -20, -46}, {0, -29, -57}}, {{2, -15, -44}, {12, -28, -25}, {4, -18, -34}, {5, -22, -45}, {0, -19, -46}, {0, -18, -46}, {0, -29, -57}}, {{2, -20, -43}, {12, -28, -25}, {6, -26, -32}, {10, -8, -35}, {0, -24, -45}, {0, -23, -45}, {0, -29, -57}}, {{4, -19, -44}, {12, -28, -25}, {7, -25, -33}, {11, -1, -21}, {1, -23, -46}, {1, -22, -46}, {0, -29, -57}}, {{4, -19, -44}, {11, -23, -29}, {7, -25, -33}, {11, -1, -21}, {1, -23, -46}, {1, -22, -46}, {0, -29, -57}}, {{1, -11, -40}, {14, -9, -18}, {1, -11, -31}, {1, -18, -42}, {0, -15, -42}, {0, -14, -42}, {0, -29, -57}}, {{1, -8, -42}, {0, -11, -18}, {5, -16, -31}, {6, -23, -49}, {0, -12, -44}, {0, -11, -44}, {0, -29, -57}}};
   public int[] feStand = new int[]{0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0};
   public int[] PP = new int[]{2, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6, 7, 7, 7};
   public int[] PQ = new int[]{8, 8, 10, 10, 10, 10};
   private static byte[][] AZ = new byte[][]{{0, -3, 0, -3, 0, -5, 0, -5, 0, -5, 0, -5, 0, -5, 0, -5, 0, -5, 0, -5, 0, -5, 0, -5, 0, -5, 3, -6, 3, -6, 0, 0, 0, 0, 0, 0, 0, 0, 0, -11, 0, -11, -1, -9, -1, -9, -2, -8, -2, -8, 0, 0, 0, 0, 0, -5, 0, -5, 0, -5, 0, -5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -2, -8, -2, -8, -2, -8, -2, -8, -2, -8, -2, -8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -5, 0, -5, 0, 0, 0, 0, 0, 0, 0, -5, 0, -5}, {-1, -2, -1, -2, -3, -5, -2, -10, -2, -5, -2, -6, 0, -9, 0, -9, -6, -5, -6, -5, -6, -5, -5, -5, -5, -5, -2, -4, -2, -4, -2, -7, -2, -7, 0, 0, 0, 0, 0, -9, 0, -9, 0, 0, 0, 0, -2, -5, -2, -5, -2, -7, -2, -7, -2, -5, -2, -5, -2, -5, -2, -5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -2, -5, -2, -5, -2, -5, -2, -5, -2, -5, -2, -5, 0, 0, 0, 0, 0, 0, -2, -7, 0, 0, -2, -7, 0, -9, 0, -9, 0, 0, 0, 0, 0, 0, -2, -4, -5, -5}, {0, -9, 0, -9, 0, -7, 0, -7, 0, -7, 0, -7, 0, -7, 0, -9, 0, -6, 0, -6, 0, -6, 0, -8, 0, -7, -5, -11, -5, -11, 0, 0, 0, 0, 0, 0, 0, 0, 0, -7, 0, -7, 0, -7, 0, -7, 0, -6, 0, -6, 0, 0, 0, 0, -2, -7, -2, -7, -2, -7, -2, -7, -3, -5, -3, -5, -1, -5, -1, -5, -3, -6, -3, -6, -3, -6, -3, -6, -2, -6, -2, -6, 0, -5, 0, -5, 0, -6, 0, -6, 0, -6, 0, -6, 0, -6, 0, -6, -3, -6, -3, -6, -3, -6, 0, 0, 0, 0, 0, 0, 0, -6, 0, -6, 0, 0, 0, 0, 0, 0, 0, 9, 0, 6}, {2, -6, 2, -6, -4, -4, -4, -4, -1, -5, -1, -5, -4, -4, -3, -6, -4, -6, -4, -6, -4, -6, -4, -6, -4, -6, -3, -6, -3, -6, -4, -6, -4, -6, 0, 0, 0, 0, 0, -9, 0, -9, 0, 0, 0, 0, 4, -2, 4, -2, 0, 0, 0, 0, -2, -6, -2, -6, -2, -6, -2, -6, -2, -6, -2, -6, -1, -5, -1, -5, -1, -6, -1, -6, -2, -6, -2, -6, 0, -5, 0, -5, -1, -5, -1, -5, 4, -2, 4, -2, 4, -2, 4, -2, 4, -2, 4, -2, -2, -6, -2, -6, -2, -6, -4, -6, 0, 0, 0, 0, -3, -6, -3, -6, -4, -6, 0, 0, 0, 0, -4, -4, -4, -6}};
   private static FrameImage[] QA = new FrameImage[10];
   public int PR;
   private int QB;
   private int QC;
   private int QD;
   private int QE;
   public int PS;
   private int QF;
   private int QG;
   private int QH;
   private int QI;
   private int QJ;
   private FrameImage QK;
   private MainImage QL;
   private MainImage QM;
   private MainImage QN;
   private MainImage QO;
   private MainImage QP;
   private int[][] QQ;
   private int QR;
   private int QS;
   private int QT;
   private int QU;
   private int QV;
   private static byte QW;
   public static short[] PT;
   private short QX;
   private short QY;
   private short QZ;
   private short RA;
   public static short[] PU;
   private int[] RB;
   private int[] RC;
   public static short[] PV;
   private static int[] RD;
   public static short[] PW;
   public static short[] PX;
   private boolean RE;
   private boolean RF;
   private int RG;
   private int RH;
   private int RI;
   private int RJ;
   private int RK;
   public byte PY;
   public byte PZ;
   private int RL;
   private int RM;

   static {
      byte[][] var10000 = new byte[][]{new byte[0], new byte[0], new byte[0]};
      QW = 0;
      PT = new short[]{798, 799, 801, 802};
      PU = new short[]{815};
      PV = new short[]{803, 804, 805, 806};
      RD = new int[]{0, 5, 4};
      PW = new short[]{719, 748, 751, 756, 798, 799, 801, 802, 849, 851, 894, 896, 950, 963, 972};
      PX = new short[]{893, 889};
      new MyHashTable();
   }

   public MainObject() {
      int[][] var10000 = new int[][]{{3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 6, 6, 6, 6, 6, 6, 6, 1, 1, 1, 1, 0, 0, 0, 0, 7, 7, 7, 7, 7, 7, 7, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 6, 6, 6, 6, 6, 6, 6, 1, 1, 1, 1, 0, 0, 0, 0}, {0, 0, 27, 27, 27, 27, 27, 27, 28, 28, 28, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 29, 29, 29, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1}, {0, 0, 14, 14, 14, 13, 13, 13, 39, 39, 39, 41, 41, 41, 39, 39, 39, 41, 41, 41, 39, 39, 39, 41, 41, 41}};
      this.PR = 0;
      this.QB = 0;
      this.QC = 0;
      this.QD = 0;
      this.QE = 0;
      this.PS = 0;
      this.QF = 0;
      this.QG = 0;
      this.QH = 0;
      this.QI = 0;
      this.QJ = 0;
      this.QK = null;
      this.QL = null;
      this.QM = null;
      this.QN = null;
      this.QO = null;
      this.QP = null;
      this.QQ = new int[][]{{0, 0, 1, 1, 2, 2, 0, 0, 1, 1, 2, 2, 1, 1, 0, 0}, {0, 0, 1, 1, 2, 2, 1, 1, 0, 0, 1, 1, 2, 2, 1, 1, 0, 0}, {0, 0, 1, 1, 2, 2, 2, 2, 1, 1, 1, 1, 2, 2, 2, 2, 1, 1, 2, 2, 1, 1, 2, 2, 1, 1, 0, 0, 0, 0}, {0, 0, 1, 1, 2, 2, 1, 1, 0, 0, 1, 1, 2, 2, 1, 1, 0, 0}, {0, 0, 1, 1, 2, 2, 1, 1, 0, 0, 0, 0}};
      this.QX = -1;
      this.QY = -1;
      this.QZ = -1;
      this.RA = -1;
      this.RB = new int[]{1, 2, 0, 5, 4, 3, 6};
      this.RC = new int[]{1, 2, 3, 6, 0, 5, 4};
      this.RE = false;
      this.RF = false;
      this.RG = 0;
      this.RH = 0;
      this.RJ = 2;
      this.PY = 0;
      this.RL = 0;
      this.RM = 0;
   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case 0:
         mVector var6 = this.CD();
         mVector var7 = new mVector();

         for(int var8 = 0; var8 < var6.size(); ++var8) {
            MainQuest var4;
            if ((var4 = (MainQuest)var6.elementAt(var8)).AB == 2) {
               GameCanvas.menuCur.isShowMenu = false;
               GameCanvas.menuCur.AL = null;
               var4.AB(this.ID);
               return;
            }

            iCommand var5 = new iCommand(var4.AH + (var4.AA == 0 ? var4.AA() : ""), 1, var4.AE, this);
            if (var4.AC == this.ID) {
               var5.setFraCaption((FrameImage)AvMain.fraQuest, 1, var4.AB + 1, (int)3);
            } else if (var4.AB == 2 && var4.AD == this.ID) {
               var5.setFraCaption((FrameImage)AvMain.fraQuest, 1, 2, (int)3);
            }

            var7.addElement(var5);
         }

         GameCanvas.menu.startAt(var7, 2, T.BU);
         return;
      case 1:
         MainQuest var3;
         if ((var3 = MainQuest.AA((short)var2)) != null) {
            var3.AB(this.ID);
            return;
         }
         break;
      case 2:
         GlobalService.getInstance().AD(this.ID);
         GameCanvas.menuCur.isShowMenu = false;
         return;
      case 3:
         GlobalService.getInstance().AD(this.ID);
         GameCanvas.menuCur.isShowMenu = false;
         return;
      case 4:
         GlobalService.getInstance().AJ(this.ID);
         return;
      case 5:
         GlobalService.getInstance().AJ(this.ID);
      }

   }

   public void paint(mGraphics var1) {
   }

   public void AC(mGraphics var1) {
   }

   public void a_(mGraphics var1) {
      var1.drawRegion((mImage)AvMain.imgHinhnhan, this.x, this.y, 33);
      if (GameScreen.objFocus != null && this == GameScreen.objFocus) {
         byte var2 = this.colorName;
         int var3;
         if (Player.vecParty.size() > 0) {
            for(var3 = 0; var3 < Player.vecParty.size(); ++var3) {
               if (((Class_DS)Player.vecParty.elementAt(var3)).AC.compareTo(this.name) == 0) {
                  var2 = 4;
                  break;
               }
            }
         }

         if (Player.AY.length > 0) {
            for(var3 = 0; var3 < Player.AY.length; ++var3) {
               if (this.ID == Player.AY[var3]) {
                  var2 = 6;
               }
            }
         }

         this.AA(var1, (byte)var2, 0);
      }

   }

   public final void AB(mGraphics var1, int var2) {
      if (GameScreen.CN == 0 && this == GameScreen.objFocus) {
         byte var3 = 2;
         if (this.type_left_right == 2) {
            var3 = -2;
         }

         AvMain.fraShadowFocus.drawFrame(GameCanvas.BJ % AvMain.fraShadowFocus.nFrame, this.x + var3, this.y, 0, 3, var1);
      }

   }

   public final void AC(mGraphics var1, int var2) {
      this.AC(var1, var2, this.y);
   }

   public final void AC(mGraphics var1, int var2, int var3) {
      byte var4 = 2;
      if (this.type_left_right == 2) {
         var4 = -2;
      }

      var1.drawRegion((mImage)imgShadow, var2 + var4, var3, 3);
   }

   public final void AA(mGraphics var1, int var2, int var3, int var4) {
      if (var4 == 0) {
         var1.drawRegion((mImage)imgShadow, var2, this.y + var3, 3);
      } else if (var4 == 1) {
         var1.drawRegion((mImage)AvMain.imgShadowSmall, var2, this.y + var3, 3);
      } else if (var4 == 2) {
         if (AK == null) {
            AK = LoadImageStatic.LoadNewInterface("/shadow2.png");
         }

         var1.drawRegion((mImage)AK, var2, this.y + var3, 3);
      } else if (var4 == 3) {
         try {
            if (AL == null) {
               AL = LoadImageStatic.LoadNewInterface("/shadow3.png");
            }

            var1.drawRegion((mImage)AL, var2, this.y + var3, 3);
         } catch (Exception var5) {
            var5.printStackTrace();
         }
      } else {
         if (var4 == 4) {
            var1.drawRegion(imgShadow, 0, 0, 18, 10, 0, var2 - 9, this.y + var3, 3);
            var1.drawRegion(imgShadow, 0, 0, 18, 10, 2, var2 + 9, this.y + var3, 3);
         }

      }
   }

   public final void AD(mGraphics var1, int var2) {
      this.AB(var1, (int)0);
      int var3 = this.y - this.dy;
      if (this.BV() && this.boatSea != null && this.boatSea.ID == this.ID) {
         this.boatSea.AB(var1);
         if (this.dy == 0) {
            var3 = this.y - this.CH / 10;
         }

         this.boatSea.AE(var1);
      } else if (!this.isTanHinh) {
         if (this.typeObject == 1) {
            this.AA(var1, this.x - this.dx, this.KA, var2);
         } else {
            this.AC(var1, this.x - this.dx);
         }
      }

      if (!this.isTanHinh) {
         this.AA(var1, -1);
         this.AD(var1, this.x - this.dx, var3);
         this.AI(var1);
         int var10002 = this.x - this.dx;
         int var6 = var3;
         int var5 = var10002;
         mGraphics var4 = var1;
         MainObject var9 = this;

         for(int var7 = 0; var7 < var9.vecEffBuffNew.size(); ++var7) {
            MainBuff var8;
            if ((var8 = (MainBuff)var9.vecEffBuffNew.elementAt(var7)).AD != -1) {
               var8.paint(var4, var5, var6);
            }

            var8.AA(var4, var5, var6, var9);
         }

         this.AG(var1, this.x - this.dx, var3);
      }

      if (this.BV() && this.boatSea != null && this.boatSea.ID == this.ID) {
         this.boatSea.AG(var1);
      }

      if (!this.isTanHinh) {
         this.AA(var1, 0);
         this.AE(var1, this.x - this.dx, var3);
         this.AH(var1);
      }

   }

   public final void AD(mGraphics var1, int var2, int var3) {
      for(int var4 = 0; var4 < this.vecEffBuff.size(); ++var4) {
         MainBuff var5;
         if ((var5 = (MainBuff)this.vecEffBuff.elementAt(var4)).AD == -1) {
            var5.paint(var1, var2, var3);
         }
      }

   }

   public final void AE(mGraphics var1, int var2, int var3) {
      for(int var4 = 0; var4 < this.vecEffBuff.size(); ++var4) {
         MainBuff var5;
         if ((var5 = (MainBuff)this.vecEffBuff.elementAt(var4)).AD != -1) {
            var5.paint(var1, var2, var3);
         }

         var5.AA(var1, var2, var3, this);
      }

   }

   public final void AH(mGraphics var1) {
      for(int var2 = 0; var2 < this.vecEffspec.size(); ++var2) {
         Class_BR var3;
         if ((var3 = (Class_BR)this.vecEffspec.elementAt(var2)).levelPaint != -1) {
            var3.paint(var1);
         }
      }

   }

   public final void AI(mGraphics var1) {
      for(int var2 = 0; var2 < this.vecEffspec.size(); ++var2) {
         Class_BR var3;
         if ((var3 = (Class_BR)this.vecEffspec.elementAt(var2)).levelPaint == -1) {
            var3.paint(var1);
         }

         var3.AB(var1);
      }

   }

   public void AA(mGraphics var1, byte var2, int var3) {
      if (!GameScreen.getIsOffAdmin((byte)0)) {
         if (GameScreen.isShowNameSUPER_BOSS || GameScreen.player != null && this == GameScreen.player || GameScreen.objFocus != null && this == GameScreen.objFocus || var3 < 0) {
            byte var4 = 0;
            if (this.Action == 4) {
               var4 = 5;
            }

            int var9 = this.y - this.dy - this.hOne - 18 + var4;
            String var5 = this.name;
            if (GameScreen.IU) {
               var5 = this.name + " " + this.ID + " f=" + this.frame;
            }

            if (GameScreen.isShowNameXpArena && this.typeObject == 0 && var3 >= 0) {
               var5 = String.valueOf(this.IV);
            }

            byte var6 = 0;
            if (GameScreen.isShowNameSetting || var3 < 0) {
               int var7 = 0;
               if (this.clan != null && (GameCanvas.currentScreen == GameCanvas.CG || LoadMap.specMap != 4)) {
                  var7 = 1;
               }

               if (this.rankWanted < 100 && this.rankWanted >= 0 && var3 >= 0) {
                  var7 += 2;
               }

               int var8;
               if (var7 > 0) {
                  var8 = mFont.tahoma_7b_black.getWidth(this.name) / 2;
                  if (!this.checkMapChiemDao() && var3 >= 0) {
                     var8 = 0;
                  }

                  if (var7 == 1) {
                     var6 = 10;
                     this.AH(var1, this.x - var8, var9 + 5);
                  } else if (var7 == 2) {
                     var6 = -10;
                     this.AI(var1, this.x + var8, var9 + 6);
                  } else if (var7 == 3) {
                     this.AI(var1, this.x + var8 + 10, var9 + 6);
                     this.AH(var1, this.x - var8 - 10, var9 + 5);
                  }
               }

               if (this.checkMapChiemDao() || var3 < 0) {
                  if (var3 >= 0 && !GameScreen.isShowNameXpArena && this != GameScreen.player) {
                     if (Player.vecParty.size() > 0) {
                        for(var8 = 0; var8 < Player.vecParty.size(); ++var8) {
                           if (((Class_DS)Player.vecParty.elementAt(var8)).AC.compareTo(this.name) == 0) {
                              var2 = 4;
                              break;
                           }
                        }
                     }

                     if (Player.AY.length > 0) {
                        for(var8 = 0; var8 < Player.AY.length; ++var8) {
                           if (this.ID == Player.AY[var8]) {
                              var2 = 6;
                              break;
                           }
                        }
                     }
                  }

                  if (GameScreen.isShowNameXpArena && this.typeObject == 0 && var3 >= 0) {
                     var2 = this.LW;
                  }

                  if (GameScreen.isShowNameWW && this.typeObject == 0 && var3 >= 0 && this.IX <= 0 && this.typePK >= 11 && this.typePK <= 13) {
                     mFont.tahoma_7b_black.drawString(var1, var5, this.x + var6, var9, 2);
                  } else if (var3 == 1) {
                     AvMain.FontBorderColor(var1, var5, this.x + var6, var9, 2, (int)var2, (int)7);
                  } else if (this.MT == 1 && var3 == 0) {
                     AvMain.AA(var1, var5, this.x + var6, var9, 2, (int)-1);
                  } else if (GameCanvas.lowGraphic) {
                     AvMain.AC(var2).drawString(var1, var5, this.x + var6, var9, 2);
                  } else {
                     AvMain.AA(var1, var5, this.x + var6, var9, 2, (byte)var2);
                  }

                  if (var3 >= 0) {
                     this.AA(var1, this.x + var6, var9 + 14);
                  }

                  if (this.LL > 0) {
                     AvMain.AA(var1, Interface_Game.AB(this.LL), this.x + var6, var9 - 15, 2, (byte)5);
                  }
               }
            }
         }

         if (var3 >= 0) {
            this.AJ(var1);
         }

      }
   }

   public final void paintThanhTich(mGraphics g, int yBegin, int xBegin) {
      int num = 0;
      if (this.thanhtichPvP >= 0) {
         if (this.thanhtichPvP == 0) {
            g.drawRegion(AvMain.mImgThanhTich[0], 0, GameCanvas.BJ % 3 * 15, 63, 15, 0, xBegin, yBegin, 3);
            num += 14;
         } else if (this.thanhtichPvP > 0 && this.thanhtichPvP <= 3) {
            g.drawRegion(AvMain.mImgThanhTich[0], 0, (this.thanhtichPvP + 2) * 15, 63, 15, 0, xBegin, yBegin, 3);
            num += 12;
         }
      }
      if (this.thanhtichLv >= 0) {
         if (this.thanhtichLv == 0) {
            g.drawRegion(AvMain.mImgThanhTich[1], 0, GameCanvas.BJ % 3 * 15, 52, 15, 0, xBegin, yBegin - num, 3);
            return;
         }
         if (this.thanhtichLv > 0 && this.thanhtichLv <= 3) {
            g.drawRegion(AvMain.mImgThanhTich[1], 0, (this.thanhtichLv + 2) * 15, 52, 15, 0, xBegin, yBegin - num, 3);
         }
      }
   }

   private boolean checkMapChiemDao() {
      if (LoadMap.specMap != 11) {
         return true;
      } else if (GameScreen.player != null && GameScreen.player != this) {
         if (this.clan != null && GameScreen.player.clan != null) {
            return GameScreen.player.clan.ID == this.clan.ID;
         } else {
            return false;
         }
      } else {
         return true;
      }
   }

   private void AA(mGraphics var1, int var2, int var3) {
      if (this.levelPerfect > 0) {
         byte var4 = (byte)(GameCanvas.gameTick % 40 / 4);
         byte var5 = (byte)(GameCanvas.gameTick / 20 % (AvMain.fraEffItem.nFrame / 2));

         for(int var6 = 0; var6 < this.levelPerfect; ++var6) {
            int var7 = (var5 << 1) + (var4 == var6 ? 1 : 0);
            AvMain.fraEffItem.drawFrame(var7, var2 - (this.levelPerfect - 1) * 3 + var6 * 6, var3, 0, 3, var1);
         }
      }

   }

   public final void AB(mGraphics var1, byte var2) {
      AvMain.AA(var1, T.gohome + " " + this.Lv, this.x, this.y - this.dy - this.hOne - 28, 2, (byte)var2);
   }

   public final void AJ(mGraphics var1) {
      int var2 = 8;
      int var3 = this.y - this.hOne - this.dy - 25;
      byte var4 = 0;
      if (LoadMap.specMap == 11) {
         var3 -= 3;
      }

      if (this.typePirate >= 0 && this.typePirate <= 2) {
         var2 -= 8;
      }

      if (this.typePK >= 0) {
         if (this.typePK > 10) {
            var2 -= 9;
         } else {
            var2 -= 8;
         }
      }

      if (this.timeSafe > 0) {
         var2 -= 8;
      }

      if (!GameScreen.isShowNameSetting && this.clan != null) {
         var2 -= 10;
         var3 += 10;
         this.AH(var1, this.x + var2, var3);
         var2 += 16;
      }

      if (this.typePirate >= 0 && this.typePirate <= 2) {
         AvMain.fraPirate.drawFrame(this.typePirate, this.x + var2, var3, 0, 3, var1);
         var2 += 16;
         var4 = 14;
      }

      if (this.typePK >= 0) {
         if (this.typePK > 10) {
            if (AvMain.fraPk2 == null) {
               AvMain.fraPk2 = LoadImageStatic.AA("/interface/iconpk2.png", 16, 16);
            } else {
               AvMain.fraPk2.drawFrame((this.typePK - 11) * 3 + GameCanvas.gameTick / 6 % 3, this.x + var2, var3 - 1, 0, 3, var1);
            }

            var2 += 18;
         } else {
            AvMain.fraPk.drawFrame(this.typePK * 3 + GameCanvas.gameTick / 4 % 3, this.x + var2, var3 + 1, 0, 3, var1);
            var2 += 16;
         }

         var4 = 14;
      }

      if (this.timeSafe > 0) {
         if (AvMain.EJ == null) {
            AvMain.EJ = mImage.createImage("/interface/safe.png");
         } else {
            var1.drawRegion((mImage)AvMain.EJ, this.x + var2, var3, 3);
         }

         var4 = 14;
      }

      this.paintThanhTich(var1, var3 - var4 - 2, this.x);
   }

   private void AH(mGraphics var1, int var2, int var3) {
      MainImage var4;
      if ((var4 = Potion.getIconClan(this.clan.idIcon)) != null && var4.img != null) {
         if (this.clan.AT > 0) {
            AvMain.fraBorderClan2.drawFrameNew((this.clan.AT - 1) * AvMain.fraBorderClan2.maxNumFrame + GameCanvas.gameTick / 3 % AvMain.fraBorderClan2.maxNumFrame, var2, var3, 0, 3, var1);
         }

         byte var5 = -1;
         if (this.clan.AJ == 0) {
            var5 = 10;
         } else if (this.clan.AJ == 1) {
            var5 = 2;
         }

         var4.AA();
         if (var5 != -1) {
            MainItem.CJ.AA(var2 - 1, var3, var5, var4.AB <= 15 ? 15 : var4.AB + 2, var1, 0, true);
         }

         if (var4.frame == -1) {
            var4.set_Frame();
         }

         if (var4.frame <= 1) {
            var1.drawRegion((mImage)var4.img, var2, var3 + 1, 3);
            return;
         }

         byte var6;
         if (this.AU >= var4.frame - 1) {
            var6 = 15;
         } else {
            var6 = 3;
         }

         if (CRes.abs(GameCanvas.gameTick - this.AT) > var6) {
            ++this.AU;
            if (this.AU >= var4.frame) {
               this.AU = 0;
            }

            this.AT = GameCanvas.gameTick;
         }

         var1.drawRegion(var4.img, 0, this.AU * var4.AB, var4.AB, var4.AB, 0, var2, var3 + 1, 3);
      }

   }

   private void AI(mGraphics var1, int var2, int var3) {
      int var4;
      if (this.rankWanted == 0) {
         if ((var4 = GameCanvas.gameTick / 6 % 10) > 3) {
            var4 = 0;
         }

         AvMain.fraIconWanted.drawFrame(var4, var2, var3, 0, 3, var1);
      } else if (this.rankWanted == 1) {
         if ((var4 = GameCanvas.gameTick / 6 % 10) > 2) {
            var4 = 0;
         }

         AvMain.fraIconWanted.drawFrame(var4 + 4, var2, var3, 0, 3, var1);
      } else if (this.rankWanted == 2) {
         AvMain.fraIconWanted.drawFrame(7, var2, var3, 0, 3, var1);
      } else if (this.rankWanted < 10) {
         AvMain.fraIconWanted.drawFrame(8, var2, var3, 0, 3, var1);
      } else {
         AvMain.fraIconWanted.drawFrame(9, var2, var3, 0, 3, var1);
      }
   }

   public final void AA(mGraphics var1, int var2, int var3, boolean var4) {
      this.AC(var1, var2, var3, 1);
      this.AJ(var1, var2 + 1, var3);
      int var5 = this.feStand[0];
      if (var4) {
         var5 = this.feStand[GameCanvas.gameTick % this.feStand.length];
      }

      this.BO();
      this.AA(var1, var2, var3, var5, 0, var4);
      this.AK(var1, var2 + 1, var3 + 1);
      this.AC(var1, var2, var3, 0);
   }

   public final void AI(mGraphics var1, int var2, int var3, int var4, int var5) {
      this.BO();
      this.AA(var1, var2, var3, var5, var4, true);
   }

   public void update() {
      if (this.typeActionBoat != 0) {
         ++this.PM;
         if (this.PM >= 250) {
            this.BX();
            this.PM = 0;
         }
      }

      if (this.LD > 0) {
         --this.LD;
         if (this.LD == 0) {
            this.CS = 7;
         }
      }

      if (this.timeBeginUpdateMove >= 0) {
         --this.timeBeginUpdateMove;
      }

      this.x += this.vx;
      this.y += this.vy;
      this.AF(this.LG);
      if (this.AJ > 0) {
         --this.AJ;
         if (this.AJ == 1) {
            this.x = this.toX;
            this.y = this.toY;
         }

         if (this.AJ == 0) {
            this.isTanHinh = false;
         }
      }

      this.AR();
      this.BO();
      this.AQ();
      if (this.BV() && this.boatSea != null && this.boatSea.ID == this.ID) {
         this.boatSea.AF();
         this.AG();
         if (this.Action != 2 && this.Action != 4) {
            this.AV = this.type_left_right;
         }

         if (this.Action != 2) {
            this.boatSea.AA(this.x, this.y, this.CH / 10, (byte)this.AV);
         }

         if (this.typeActionBoat != 0 && GameCanvas.loadmap.AA(this.x, this.y) == 0) {
            this.setSpeed(7, 7);
            this.typeActionBoat = 0;
         }
      }

      if (this.KI > this.Hp) {
         ++this.RM;
         if (this.RL <= 0) {
            this.RL = 20;
         }

         if (this.RL < (this.KI - this.Hp) / 10) {
            this.RL = (this.KI - this.Hp) / 10;
         }

         if (this.RM > 10) {
            this.KI -= this.RL;
         }
      } else {
         this.KI = this.Hp;
         this.RL = 0;
         this.RM = 0;
      }

      this.AP();
      if (this.Action == 1) {
         ++this.AF;
         if (this.AF >= 20) {
            this.AF = 0;
            if (this == GameScreen.player || CRes.random(5) == 0) {
               float var10000;
               if (LoadMap.specMap == 4) {
                  var10000 = mSound.volumeSound;
                  mSound.playSound();
               } else {
                  var10000 = mSound.volumeSound;
                  mSound.playSound();
               }
            }
         }
      }

      if (GameCanvas.gameTick % 10 == 0) {
         this.updateTimeSafe();
      }

      MainObject var1 = this;

      int var2;
      for(var2 = 0; var2 < var1.veceffFas.size(); ++var2) {
         MainEffect var3;
         (var3 = (MainEffect)var1.veceffFas.elementAt(var2)).update();
         if (var3.isStop) {
            var1.veceffFas.removeElement(var2);
            --var2;
         }
      }

      switch(var1.typeEfffashion) {
      case 1:
         if (var1.Action == 0) {
            --var1.AW;
            if (var1.AW < 0) {
               var1.AW = 10 + CRes.random(25);
               var1.veceffFas.addElement(GameScreen.AB((short)161, (byte)(var1.clazz == 4 ? 1 : 0), var1.x, var1.y, var1.ID, var1.typeObject, (byte)var1.Dir, var1));
            }
         } else if (var1.Action == 1 && GameCanvas.gameTick % 2 == 0) {
            var1.veceffFas.addElement(GameScreen.AB((short)160, (byte)(var1.clazz == 4 ? 1 : 0), var1.x, var1.y, var1.ID, var1.typeObject, (byte)var1.Dir, var1));
         }
      default:
         var1 = this;

         for(var2 = 0; var2 < var1.PL.size(); ++var2) {
            DataSkillEff var4;
            if ((var4 = (DataSkillEff)var1.PL.elementAt(var2)) != null) {
               var4.AA();
               if (var4.wantDestroy) {
                  var1.PL.removeElement(var2);
                  --var2;
               }
            }
         }

         if (this.LL >= 0 && GameCanvas.timeNow - this.PN >= 1000L) {
            this.PN += 1000L;
            --this.LL;
         }

      }
   }

   private void AC(mGraphics var1, int var2, int var3, int var4) {
      for(int var5 = 0; var5 < this.PL.size(); ++var5) {
         DataSkillEff var6;
         if ((var6 = (DataSkillEff)this.PL.elementAt(var5)) != null) {
            if (var4 == 0) {
               var6.paintTopEff(var1, var2, var3, this.hOne);
            }

            if (var4 == 1) {
               var6.AB(var1, var2, var3, this.hOne);
            }
         }
      }

   }

   private void AA(mGraphics var1, int var2) {
      for(int var3 = 0; var3 < this.veceffFas.size(); ++var3) {
         MainEffect var4;
         if ((var4 = (MainEffect)this.veceffFas.elementAt(var3)).levelPaint == var2) {
            var4.paint(var1);
         }
      }

   }

   public void updateTimeSafe() {
      if (this.timeSafe > 0 && (GameCanvas.timeNow - this.timeBeginSafe) / 1000L > (long)this.timeSafe) {
         this.timeSafe = 0;
      }

   }

   public final void AO() {
      this.x += this.vx;
      this.y += this.vy;
      this.BB();
      ++this.f;
      switch(this.Action) {
      case 0:
         this.actionStand();
         break;
      case 1:
         int[] var2 = this.PP;
         if (this.BV() && this.boatSea != null) {
            var2 = this.feStand;
         }

         if (this.f > var2.length - 1) {
            this.f = 0;
         }

         if (this.vx == 0 && this.vy == 0 && this.posTransRoad == null) {
            this.Action = 0;
            this.f = 0;
         }

         this.frame = var2[this.f];
         break;
      case 2:
         this.c_();
      }

      this.BL();
      if (this.vx == 0 && this.vy == 0 && this.Action == 1) {
         this.Action = 0;
         this.f = 0;
      }

      int var1;
      if (this.boatSea != null) {
         if (this.Action == 0 && CRes.random(20) == 0) {
            var1 = CRes.random(40, 120);
            this.toX = this.x + (this.xAnchor < 0 ? var1 : -var1);
            this.toY = this.y + CRes.random_Am(24, 48);
            if (this.toY < MotherCanvas.h - 170) {
               this.toY = MotherCanvas.h - 170;
            }

            if (this.toY > MotherCanvas.h - 100) {
               this.toY = MotherCanvas.h - 100;
            }

            this.Action = 1;
         }
      } else if (this.Action == 0 && CRes.random(40) == 0) {
         if (CRes.random(2) == 0) {
            MainSkill var3 = new MainSkill((short)0, this.BL);
            this.plashNow = new Plash(var3, this, (mVector)null, true);
            this.resetBeginFire();
            this.Action = 2;
         } else {
            var1 = CRes.random(40, 120);
            this.toX = this.x + (this.xAnchor < 0 ? var1 : -var1);
            this.toY = this.y + CRes.random_Am(24, 48);
            if (this.toY < MotherCanvas.h - 70) {
               this.toY = MotherCanvas.h - 70;
            }

            if (this.toY > MotherCanvas.h) {
               this.toY = MotherCanvas.h;
            }

            this.Action = 1;
         }
      }

      if (this.xAnchor < 0 && this.x > MotherCanvas.w + 30) {
         this.isRemove = true;
      } else {
         if (this.xAnchor > 0 & this.x < -30) {
            this.isRemove = true;
         }

      }
   }

   public final void AP() {
      if (this.Action == 4 && !this.isDie) {
         if (this.KF > 0) {
            this.KF += 2;
         } else {
            this.KF -= 2;
         }

         this.KC += this.KF;
         this.KD += this.KG;
         this.KE += 10;
         ++this.timeDie;
         if (this.timeDie >= 20L) {
            this.isDie = true;
            this.timeDie = 0L;
         }
      }

   }

   public void AG() {
      if (CRes.random(40) == 0) {
         if (CRes.random(2) == 0) {
            this.vySea = 4;
         } else {
            this.vySea = -4;
         }
      }

      if (this.CH > 0 && this.vySea > 0) {
         this.vySea = -4;
      } else if (this.CH < -50 && this.vySea < 0) {
         this.vySea = 4;
         if (this.Action == 1) {
            this.boatSea.AA(this.x, this.y, 0, (byte)(this.type_left_right == 0 ? 2 : 0), 0);
         } else {
            this.boatSea.AA(this.x, this.y, 1, (byte)0, 0);
         }
      }

      this.CH += this.vySea;
      if (this.Action == 1) {
         if (GameCanvas.gameTick % 8 == 0) {
            this.boatSea.AA(this.x, this.y, -1, (byte)this.type_left_right, 0);
         }

         if (CRes.random(8) == 0) {
            this.boatSea.AA(this.x, this.y, 0, (byte)(this.type_left_right == 0 ? 2 : 0), 0);
         }

         if (GameCanvas.gameTick % 6 == 0) {
            this.boatSea.AA(this.x, this.y, 2, (byte)this.type_left_right, 0);
         }
      } else if (CRes.random(20) == 0) {
         this.boatSea.AA(this.x, this.y, 1, (byte)0, 0);
      }

      if (this.vy != 0) {
         this.boatSea.AH();
      }

   }

   public final void AQ() {
      for(int var1 = 0; var1 < this.vecEffspec.size(); ++var1) {
         Class_BR var2;
         (var2 = (Class_BR)this.vecEffspec.elementAt(var1)).update();
         if (var2.BI) {
            this.vecEffspec.removeElement(var2);
            if (var2.typeEffect == 15) {
               this.CE();
            }
         }
      }

   }

   public final void AR() {
      int var1;
      MainBuff var2;
      for(var1 = 0; var1 < this.vecEffBuff.size(); ++var1) {
         (var2 = (MainBuff)this.vecEffBuff.elementAt(var1)).AA();
         if (var2.isRemove) {
            if (var2.AG) {
               this.setResetWearing();
               this.addEffBuff((byte)3, (short)166, (short)0);
            }

            this.vecEffBuff.removeElement(var2);
            --var1;
         }
      }

      for(var1 = 0; var1 < this.vecEffBuffNew.size(); ++var1) {
         (var2 = (MainBuff)this.vecEffBuffNew.elementAt(var1)).AA();
         if (var2.isRemove) {
            if (var2.AG) {
               this.setResetWearing();
               this.addEffBuff((byte)3, (short)166, (short)0);
            }

            this.vecEffBuffNew.removeElement(var2);
            --var1;
         }
      }

   }

   public final void AF(int var1) {
      if (this.BC != null) {
         this.AA(this.BC, true);
         this.BC = null;
      }

      if (this.LS != null) {
         this.LS.AB(this.x, this.y - this.hOne - var1);
         if (this.LS.AA() || this.typeObject == 1 && this.Action == 4) {
            this.LS = null;
         }
      }

   }

   public final void AU() {
      if (this.dy > 0) {
         this.AX -= 2;
         this.dy += this.AX;
      }

      if (this.dy < 0) {
         this.dy = -this.dy;
         this.AX = 0;
      }

      if (this.dy < 3) {
         this.dy = 0;
      }

   }

   public final void AV() {
      if (!this.returnAction() && this.IQ != 0 && this.x + this.IQ > 0 && this.x + this.IQ < GameCanvas.loadmap.maxWMap) {
         this.x += this.IQ;
         this.IQ /= 2;
         if (this.IQ == 0) {
            this.toX = this.x;
            this.toY = this.y;
         }
      }

   }

   public final void AG(int var1) {
      if (this.typeActionBoat == 0) {
         if ((var1 == 1 || var1 == -1) && this.AH < 12) {
            this.vx = 0;
            this.vy = 0;
            this.AQ = !this.AQ;
            ++this.AH;
            if (this.AH >= 10) {
               this.AH = 25;
               return;
            }
         } else if (this.AH > 0) {
            --this.AH;
         }

      }
   }

   public static boolean AA(int var0, int var1, int var2, int var3) {
      return var0 >= MainScreen.cameraMain.xCam - var2 && var0 <= MainScreen.cameraMain.xCam + MotherCanvas.w + var2 && var1 >= MainScreen.cameraMain.yCam - var3 / 2 && var1 <= MainScreen.cameraMain.yCam + MotherCanvas.h + var3 * 3 / 2;
   }

   public static boolean AB(MainObject var0) {
      return var0.x >= MainScreen.cameraMain.xCam - var0.wOne && var0.x <= MainScreen.cameraMain.xCam + MotherCanvas.w + var0.wOne && var0.y >= MainScreen.cameraMain.yCam - var0.hOne && var0.y <= MainScreen.cameraMain.yCam + MotherCanvas.h + var0.hOne * 3 / 2;
   }

   public static int getDistance(int var0, int var1, int var2, int var3) {
      return AD(var0 - var2, var1 - var3);
   }

   public static int AD(int var0, int var1) {
      return CRes.AG(var0 * var0 + var1 * var1);
   }

   public static MainObject get_Object(int var0, byte var1) {
      for(int var2 = GameScreen.vecPlayers.size() - 1; var2 >= 0; --var2) {
         MainObject var3;
         if (var2 != GameScreen.vecPlayers.size() && (var3 = (MainObject)GameScreen.vecPlayers.elementAt(var2)) != null && var3.typeObject == var1) {
            short var4 = var3.ID;
            if (var1 == 10) {
               var4 = var3.IDMainShiper;
            }

            if (var4 == var0) {
               if (var3.isRemove) {
                  return null;
               }

               return var3;
            }
         }
      }

      return null;
   }

   public final void AA(String var1, boolean var2) {
      if (this.LS == null) {
         this.LS = new PopupChat();
      }

      short var3;
      if ((var3 = GlobalService.AC(var1)) == -1) {
         this.LS.AA(var1, true);
      } else {
         this.LS.AA(var3);
      }

      this.LS.AB(this.x, this.y - this.hOne - 30);
   }

   public final void AA(short var1, byte var2, short var3) {
      try {
         if (this.LS == null) {
            this.LS = new PopupChat();
         }

         this.LS.AA(var1, var2, var3);
         this.LS.AB(this.x, this.y - this.hOne - 30);
      } catch (Exception var4) {
      }
   }

   public final void AW() {
      if (CRes.abs(this.x - this.toX) > this.CN) {
         this.vy = 0;
         this.Action = 1;
         if (CRes.abs(this.x - this.toX) > this.CN) {
            if (this.x > this.toX) {
               this.vx = -this.CN;
               this.Dir = 0;
            } else {
               this.vx = this.CN;
               this.Dir = 2;
            }
         } else {
            this.vx = this.toX - this.x;
         }
      } else if (CRes.abs(this.y - this.toY) <= this.CN) {
         this.vx = 0;
         this.vy = 0;
      } else {
         this.vx = 0;
         this.Action = 1;
         if (CRes.abs(this.y - this.toY) > this.CN) {
            if (this.y > this.toY) {
               if (this.BV()) {
                  this.vy = -this.CO;
               } else {
                  this.vy = -this.CN;
               }

               this.Dir = 1;
            } else {
               if (LoadMap.specMap != 4 && this.typeActionBoat == 0 && GameCanvas.currentScreen != GameCanvas.loginScr) {
                  this.vy = this.CN;
               } else {
                  this.vy = this.CO;
               }

               this.Dir = 3;
            }
         } else {
            this.vy = this.toY - this.y;
         }
      }
   }

   public final boolean returnAction() {
      return this.isRemove || this.typeObject == 0 && !this.isInfo;
   }

   public final void AK(mGraphics var1) {
      if (!this.isTanHinh) {
         int var3 = this.y - this.dy - this.hOne - 4;
         if (this.levelPerfect > 0) {
            var3 += 4;
         }

         int var10000 = this.x;
         int var2 = this.x - 15;
         byte var4 = 102;
         if (this.Lv >= 100) {
            var4 = 106;
         }

         Interface_Game.AA(var1, var4, this.Hp, this.maxHp, var2, var3, 0, 4, 30, -1, false, 0, false, this.MA);
      }
   }

   public final void AY() {
      this.toX = this.x;
      this.toY = this.y;
      this.toXNew = this.x;
      this.toYNew = this.y;
      this.vx = 0;
      this.vy = 0;
   }

   public void beginDie(MainObject var1) {
      this.resetAction();
      if (this.isTanHinh) {
         this.isTanHinh = false;
      }

      if (this.Action != 4) {
         this.Action = 4;
      }

      if (!this.isFlyDie) {
         this.isFlyDie = true;
         this.isDie = false;
         this.timeDie = 0L;
         this.KC = this.x;
         this.KD = this.y;
         this.KG = -12;
         this.KE = 0;
         if (var1 == null || var1.x < this.x) {
            this.KF = 12;
            GameScreen.addEffectEnd((short)82, 0, this.x + 10, this.y, (byte)2, this);
            return;
         }

         this.KF = -12;
         GameScreen.addEffectEnd((short)82, 0, this.x - 10, this.y, (byte)0, this);
      }

   }

   public void resetAction() {
      this.f = 0;
      this.vx = this.vy = 0;
      this.toX = this.x;
      this.toY = this.y;
      if (this.Action != 2 && this.Action != 4 && this.Action != 3) {
         this.Action = 0;
      }

   }

   public final void resetBeginFire() {
      this.vx = this.vy = 0;
      this.toX = this.x;
      this.toY = this.y;
   }

   public final void BB() {
      if (this.AQ) {
         if (CRes.abs(this.x - this.toX) >= this.CN) {
            this.AF();
         } else if (CRes.abs(this.y - this.toY) >= this.CN) {
            this.AJ();
         } else {
            this.vx = 0;
            this.vy = 0;
         }
      } else if (CRes.abs(this.y - this.toY) >= this.CN) {
         this.AJ();
      } else if (CRes.abs(this.x - this.toX) >= this.CN) {
         this.AF();
      } else {
         this.vx = 0;
         this.vy = 0;
      }
   }

   private void AF() {
      this.vy = 0;
      this.Action = 1;
      if (CRes.abs(this.x - this.toX) >= this.CN) {
         if (this.x >= this.toX) {
            this.vx = -this.CN;
            this.Dir = 0;
         } else {
            this.vx = this.CN;
            this.Dir = 2;
         }
      } else {
         this.vx = this.toX - this.x;
      }
   }

   private void AJ() {
      this.vx = 0;
      this.Action = 1;
      if (CRes.abs(this.y - this.toY) >= this.CN) {
         if (this.y > this.toY) {
            if (this.BV()) {
               this.vy = -this.CO;
            } else {
               this.vy = -this.CN;
            }

            this.Dir = 1;
         } else {
            if (this.BV()) {
               this.vy = this.CO;
            } else {
               this.vy = this.CN;
            }

            this.Dir = 3;
         }
      } else {
         this.vy = this.toY - this.y;
      }
   }

   public void AB(MainSkill var1, mVector var2) {
   }

   public void setDataBeginSkill(MainSkill var1, mVector var2) {
   }

   public void Reveive() {
      this.isFlyDie = false;
      this.vecSkillFires.removeAllElements();
      this.vecEffBuff.removeAllElements();
      this.vecEffBuffNew.removeAllElements();
      this.vecEffspec.removeAllElements();
      this.setResetWearing();
      this.Action = 0;
      this.timeBeginUpdateMove = -1;
      this.Mp = this.maxMp;
      this.Hp = this.maxHp;
      GameScreen.addEffectEnd_ObjTo((short)83, 0, 0, 0, (short)this.ID, (byte)this.typeObject, (byte)this.Dir, this);
      if (this == GameScreen.player) {
         GlobalService.getInstance().Obj_Move((short)this.x, (short)this.y);
      }

   }

   public void AB(mGraphics var1, int var2, int var3) {
      byte var4 = 0;
      byte var5 = 3;
      byte var6 = 0;
      if (GameCanvas.isTaiTho) {
         var6 = 5;
      }

      if (this.typePK >= 11 && this.typePK <= 13) {
         AvMain.AB(var1, MotherCanvas.w - this.AE - 4 - var6, var3 - 1, this.AE, 45);
         mFont.tahoma_7_green.drawString(var1, "" + this.IY, MotherCanvas.w - this.AE / 2 - 3 - 4 - var6, var3 + 32, 1);
         mFont.tahoma_7_red.drawString(var1, "" + this.IZ, MotherCanvas.w - this.AE / 2 - 3 + 4 - var6, var3 + 32, 0);
         mFont.tahoma_7_white.drawString(var1, "-", MotherCanvas.w - this.AE / 2 - 3 - var6, var3 + 32, 2);
      } else {
         AvMain.AB(var1, MotherCanvas.w - this.AE - 4 - var6, var3 - 1, this.AE, 36);
      }

      byte var7 = 102;
      int var10;
      if (GameCanvas.isSmallScreen && this.typeObject != 2) {
         AvMain.AA(var1, this.name + " (" + this.Lv + ")", var2 + 48, var3, 1);
         var3 += 13;
      } else {
         String var8 = this.name;
         if (this.BQ() == 99) {
            var8 = T.EU + " " + LoadMap.AA();
         }

         if (this.typeObject != 2) {
            byte var9 = 0;
            if (this.typeObject == 1 && (var10 = this.Lv - GameScreen.player.Lv) > 0) {
               if (var10 <= 2) {
                  var9 = 5;
               } else if (var10 <= 4) {
                  var9 = 3;
               } else {
                  var9 = 6;
               }
            }

            var5 = 2;
            AvMain.AA(var1, var8, MotherCanvas.w - this.AE / 2 - 3 - var6, var3 + 1, 2, (byte)var9);
            var3 += GameCanvas.hText;
            if (this.Lv >= 100) {
               var1.drawRegion(Interface_Game.imgIconMPHP2, 0, 22, 10, 11, 0, var2 - 14, var3 + 5, 3);
               AvMain.AA(var1, "" + this.LvThongThao, var2 - 1, var3 - 3, 2, (byte)1);
               var7 = 106;
            } else {
               AvMain.AA(var1, T.gohome + this.Lv, var2 - 6, var3 - 3, 2);
            }
         } else {
            var4 = 14;
            AvMain.AA(var1, var8, MotherCanvas.w - this.AE / 2 - 3 - var6, var3 + 1, 2, (byte)2);
            var3 += GameCanvas.hText;
         }
      }

      if (this.BQ() == 99) {
         var1.setColor(-3426402);
         var1.fillRect(var2 + 8 - var4 - 2, var3 - 1, 46, 8);
         Interface_Game.AA(var1, (byte)102, LoadMap.AZ, LoadMap.BA, var2 + 8 - var4 - 1, var3, 10, 6, 44, var5, false, this.KI, false, 0);
      } else {
         if (this.BQ() != 0 && this.BQ() != 2) {
            var1.setColor(-3426402);
            var1.fillRect(var2 + 9 - var4 - 2, var3 - 2, 46, 8);
            Interface_Game.AA(var1, GameCanvas.lowGraphic ? 104 : var7, this.Hp, this.maxHp, var2 + 9 - var4 - 1, var3 - 1, 10, 6, 44, var5, false, this.KI, false, this.MA);
         } else if (LoadMap.BB != null) {
            byte var11 = 0;
            int var10000 = GameCanvas.hText;
            if (LoadMap.BB.length >= 2) {
               var11 = 3;
            }

            for(var10 = 0; var10 < LoadMap.BB.length; ++var10) {
               AvMain.AB(var1, LoadMap.BB[var10], var2 + 17, var3 - var11 + (GameCanvas.hText - var11) * var10, 2, (byte)0);
            }

            return;
         }

      }
   }

   public final void AG(mGraphics var1, int var2, int var3) {
      boolean var4 = false;
      byte var5 = 1;
      if (this.AI >= 40 || this.AI > 34 && this.AI % 3 != 2 || this.MY >= 4) {
         if (this.type_left_right == 2) {
            var5 = -1;
         }

         var4 = true;
      }

      int var10002;
      int var10004;
      if (var4) {
         var10002 = var2 + var5;
         var10004 = this.type_left_right;
         this.AJ(var1, var10002, var3);
      }

      this.AC(var1, var2, var3, 1);
      MainObject var6 = this;
      boolean var10000;
      if (this.PL.size() == 0) {
         var10000 = false;
      } else {
         int var7 = 0;

         while(true) {
            if (var7 >= var6.PL.size()) {
               var10000 = false;
               break;
            }

            DataSkillEff var8;
            if ((var8 = (DataSkillEff)var6.PL.elementAt(var7)) != null && var8.typeMove == 2) {
               var10000 = true;
               break;
            }

            ++var7;
         }
      }

      if (!var10000) {
         this.AA(var1, var2, var3, this.frame, this.type_left_right, true);
      }

      if (var4) {
         var10002 = var2 + var5;
         int var10003 = var3 + 1;
         var10004 = this.type_left_right;
         this.AK(var1, var10002, var10003);
      }

      this.AC(var1, var2, var3, 0);
   }

   private void AJ(mGraphics var1, int var2, int var3) {
      if (this.MY > 0) {
         int var4 = GameCanvas.gameTick / 5 % 3;
         byte var5 = 0;
         if (this.MY == 5) {
            for(int var6 = 2; var6 >= 0; --var6) {
               if (QA[var6 + 2] != null && QA[var6 + 2].imgFrame != null && QA[var6 + 2].imgFrame.image != null) {
                  if (CRes.random(10) != 2) {
                     if (this.LZ != 0) {
                        var5 = 6;
                        var1.drawRegion(QA[var6 + 2].imgFrame, 0, QA[var6 + 2].frameHeight - 8 + var4 * QA[var6 + 2].frameHeight, QA[var6 + 2].frameWidth, 8, this.type_left_right, var2, var3, 33);
                     }

                     QA[var6 + 2].drawFrame(var4, var2, var3 - var5, this.type_left_right, 33, var1);
                  }
               } else {
                  QA[var6 + 2] = new FrameImage(var6 + 373, 3);
               }
            }

            return;
         }

         if (QA[this.MY - 1] == null || QA[this.MY - 1].imgFrame == null || QA[this.MY - 1].imgFrame.image == null) {
            QA[this.MY - 1] = new FrameImage(370 + this.MY, 3);
            return;
         }

         if (this.LZ != 0) {
            var5 = 6;
            var1.drawRegion(QA[this.MY - 1].imgFrame, 0, QA[this.MY - 1].frameHeight - 8 + var4 * QA[this.MY - 1].frameHeight, QA[this.MY - 1].frameWidth, 8, this.type_left_right, var2, var3, 33);
         }

         QA[this.MY - 1].drawFrame(var4, var2, var3 - var5, this.type_left_right, 33, var1);
      }

   }

   private void AK(mGraphics var1, int var2, int var3) {
      if (this.MY > 0) {
         int var4 = GameCanvas.BJ % 3;
         if (QA[5 + this.MY - 1] == null || QA[5 + this.MY - 1].imgFrame == null || QA[5 + this.MY - 1].imgFrame.image == null) {
            QA[5 + this.MY - 1] = new FrameImage(375 + this.MY, 3);
            return;
         }

         if (this.LZ != 0) {
            var1.drawRegion(QA[5 + this.MY - 1].imgFrame, 0, QA[5 + this.MY - 1].frameHeight - 8 + var4 * QA[5 + this.MY - 1].frameHeight, QA[5 + this.MY - 1].frameWidth, 8, this.type_left_right, var2, var3, 33);
         }

         QA[5 + this.MY - 1].drawFrame(var4, var2, var3, this.type_left_right, 33, var1);
      }

   }

   public final void AA(mGraphics var1, int var2, int var3, int var4, int var5, boolean var6) {
      var5 = var5 == 2 ? 2 : 0;
      var4 = var4;
      if (this.CS != -1) {
         var4 = this.CS;
         ++this.QB;
         if (this.QB >= 2) {
            this.QB = 0;
            this.CS = -1;
         }
      }

      byte var8 = 0;
      byte var9 = 0;
      int[] var7;
      int var10;
      if (var5 == 0) {
         var7 = this.RB;
         var10 = 3 + this.AN;
      } else {
         var7 = this.RC;
         var8 = 24;
         var9 = 2;
         var10 = -8 - this.AN;
      }

      int var12;
      for(int var11 = 0; var11 < var7.length; ++var11) {
         var12 = var7[var11];
         mPart var13;
         mPart var10000;
         if (this != GameScreen.player && GameCanvas.isLowGraOrWP_PvP() && GameCanvas.gameScr != null && GameCanvas.currentScreen == GameCanvas.gameScr) {
            label465: {
               var13 = null;
               short var14;
               if (this.typeObject == 1) {
                  if (this.MR != 0) {
                     var10000 = this.AE(var12);
                     break label465;
                  }

                  if (var12 == 0) {
                     var14 = 8;
                  } else if (var12 == 1) {
                     var14 = 10;
                  } else if (var12 == 2) {
                     var14 = 9;
                  } else if (var12 == 3) {
                     var14 = this.BV;
                  } else {
                     var14 = -1;
                  }
               } else if (var12 == 0) {
                  var14 = 0;
               } else {
                  switch(this.clazz) {
                  case 1:
                     if (var12 == 1) {
                        var14 = 4;
                     } else if (var12 == 2) {
                        var14 = 3;
                     } else if (var12 == 5) {
                        var14 = 1;
                     } else if (var12 == 3) {
                        var14 = this.BV;
                     } else {
                        var14 = -1;
                     }
                     break;
                  case 2:
                     if (var12 == 1) {
                        var14 = 27;
                     } else if (var12 == 2) {
                        var14 = 26;
                     } else if (var12 == 5) {
                        var14 = 24;
                     } else if (var12 == 3) {
                        var14 = this.BV;
                     } else {
                        var14 = -1;
                     }
                     break;
                  case 3:
                     if (var12 == 1) {
                        var14 = 31;
                     } else if (var12 == 2) {
                        var14 = 30;
                     } else if (var12 == 5) {
                        var14 = 28;
                     } else if (var12 == 3) {
                        var14 = this.BV;
                     } else {
                        var14 = -1;
                     }
                     break;
                  case 4:
                     if (var12 == 1) {
                        var14 = 35;
                     } else if (var12 == 2) {
                        var14 = 34;
                     } else if (var12 == 5) {
                        var14 = 32;
                     } else if (var12 == 3) {
                        var14 = this.BV;
                     } else {
                        var14 = -1;
                     }
                     break;
                  case 5:
                     if (var12 == 1) {
                        var14 = 39;
                     } else if (var12 == 2) {
                        var14 = 38;
                     } else if (var12 == 5) {
                        var14 = 36;
                     } else if (var12 == 3) {
                        var14 = this.BV;
                     } else {
                        var14 = -1;
                     }
                     break;
                  default:
                     if (var12 == 1) {
                        var14 = 4;
                     } else if (var12 == 2) {
                        var14 = 3;
                     } else if (var12 == 5) {
                        var14 = 1;
                     } else if (var12 == 3) {
                        var14 = this.BV;
                     } else {
                        var14 = -1;
                     }
                  }
               }

               if (var14 >= 0) {
                  var13 = CharPartInfo.getPart(var14);
               }

               var10000 = var13;
            }
         } else {
            var10000 = this.AE(var12);
         }

         var13 = var10000;
         int var26 = var12;
         if (var12 == 6) {
            var26 = 3;
         }

         if ((this.isDonotShowHat != 0 || !this.AR || (this.LD > 0 || var26 != 5) && (this.LD <= 0 || var26 != 4)) && var13 != null && var13.pi != null) {
            int var16 = var3 + AY[var4][var26][2] + var13.pi[AY[var4][var26][0]].dy;
            int var15;
            if (var5 == 0) {
               var15 = var2 + AY[var4][var26][1] + var13.pi[AY[var4][var26][0]].dx;
            } else {
               var15 = var2 - AY[var4][var26][1] - var13.pi[AY[var4][var26][0]].dx;
            }

            if (var12 == 3 && this.LZ != 0 && this.clazz != 1 && this.clazz != 0) {
               if (var5 == 0) {
                  var15 += AZ[this.clazz - 2][var4 << 1];
               } else {
                  var15 -= AZ[this.clazz - 2][var4 << 1];
               }

               var16 += AZ[this.clazz - 2][(var4 << 1) + 1];
            }

            if ((var26 == 0 || var26 == 5 || var26 == 4) && (var26 != 0 || !this.RE)) {
               var16 += this.LZ;
            }

            if (var26 == 5 && (this.BT == 950 || this.BT == 963 || this.BT == 972)) {
               var16 -= 20;
               if (var5 == 0) {
                  var15 -= 5;
               } else {
                  var15 += 5;
               }
            }

            short var17 = this.AL();
            short var18 = this.CA();
            short var19 = this.QZ;
            short var20 = this.RA;
            short var21 = this.CB();
            boolean var22 = true;
            MainImage var25;
            if (var12 == 5 && var17 >= 0 && AD(var4)) {
               if (this.QL == null || this.QL.img == null) {
                  this.QL = ObjectData.getImageAll(var17, ObjectData.HashImageOtherNew, (short)23000);
               }

               if (this.QL.img != null && (var25 = ObjectData.getImageAll(var13.pi[AY[var4][var26][0]].id, ObjectData.HashImageCharPart, (short)10000)).img != null) {
                  if (this.PR == 0) {
                     this.QL.img.width = mImage.getImageWidth(var25.img.image);
                     this.QL.img.height = mImage.getImageHeight(var25.img.image);
                     this.PR = mImage.getImageHeight(this.QL.img.image) / this.QL.img.height;
                  } else if (GameCanvas.BJ % (this.PR + 1) < this.PR) {
                     var1.drawRegion(this.QL.img, 0, this.QL.img.height * (GameCanvas.BJ % (this.PR + 1)), this.QL.img.width, this.QL.img.height, var5, var15, var16, var8);
                     var22 = false;
                  }
               }
            } else {
               label483: {
                  if (var12 == 2 && var18 >= 0) {
                     if (GameCanvas.gameTick % 4 == 0) {
                        ++this.QE;
                     }

                     if (this.QE >= this.QQ[this.QD].length) {
                        this.QE = 0;
                        this.QD = CRes.random(this.QQ.length);
                     }

                     if ((var4 == 0 || var4 == 1) && !this.RF) {
                        if (this.QN == null || this.QN.img == null) {
                           this.QN = ObjectData.getImageAll(var18, ObjectData.HashImageOtherNew, (short)23000);
                        }

                        if (this.QN.img != null && (var25 = ObjectData.getImageAll(var13.pi[AY[var4][var26][0]].id, ObjectData.HashImageCharPart, (short)10000)).img != null) {
                           if (this.QC == 0) {
                              this.QN.img.width = mImage.getImageWidth(this.QN.img.image);
                              this.QN.img.height = mImage.getImageHeight(var25.img.image);
                              this.QC = mImage.getImageHeight(this.QN.img.image) / this.QN.img.height;
                           } else if (this.QQ[this.QD][this.QE] < this.QC) {
                              var1.drawRegion(this.QN.img, 0, this.QN.img.height * this.QQ[this.QD][this.QE], this.QN.img.width, this.QN.img.height, var5, var15, var16, var8);
                              var22 = false;
                           }
                        }
                        break label483;
                     }
                  }

                  if (var12 == 1 && var19 >= 0 && (var4 == 0 || var4 == 1)) {
                     if (this.QO == null || this.QO.img == null) {
                        this.QO = ObjectData.getImageAll(var19, ObjectData.HashImageOtherNew, (short)23000);
                     }

                     if (this.QO.img != null && (var25 = ObjectData.getImageAll(var13.pi[AY[var4][var26][0]].id, ObjectData.HashImageCharPart, (short)10000)).img != null) {
                        if (this.QI == 0) {
                           this.QO.img.width = mImage.getImageWidth(this.QO.img.image);
                           this.QO.img.height = mImage.getImageHeight(var25.img.image);
                           this.QI = mImage.getImageHeight(this.QO.img.image) / this.QO.img.height;
                        } else if (this.QQ[this.QD][this.QE] < this.QI) {
                           var1.drawRegion(this.QO.img, 0, this.QO.img.height * this.QQ[this.QD][this.QE], this.QO.img.width, this.QO.img.height, var5, var15, var16, var8);
                           var22 = false;
                        }
                     }
                  } else if (var12 == 0 && var21 >= 0 && var4 <= 7) {
                     if (this.QM == null || this.QM.img == null) {
                        this.QM = ObjectData.getImageAll(var21, ObjectData.HashImageOtherNew, (short)23000);
                     }

                     if (this.QM.img != null && (var25 = ObjectData.getImageAll(var13.pi[AY[var4][var26][0]].id, ObjectData.HashImageCharPart, (short)10000)).img != null) {
                        if (this.PS == 0) {
                           this.QM.img.width = mImage.getImageWidth(var25.img.image);
                           this.QM.img.height = mImage.getImageHeight(var25.img.image);
                           this.PS = mImage.getImageHeight(this.QM.img.image) / this.QM.img.height;
                        } else if (this.QH > 0) {
                           var1.drawRegion(this.QM.img, 0, this.QM.img.height * (this.QH - 1), this.QM.img.width, this.QM.img.height, var5, var15, var16, var8);
                           var22 = false;
                        }
                     }
                  } else if ((var12 == 3 && this.BY == -1 || var12 == 6 && this.BY != -1) && var20 >= 0) {
                     if (GameCanvas.gameTick % 4 == 0) {
                        ++this.QF;
                     }

                     if (this.QF >= this.QQ[this.QG].length) {
                        this.QF = 0;
                        this.QG = CRes.random(this.QQ.length);
                     }

                     if (var4 == 0 || var4 == 1) {
                        if (this.QP == null || this.QP.img == null) {
                           this.QP = ObjectData.getImageAll(var20, ObjectData.HashImageOtherNew, (short)23000);
                        }

                        if (this.QP.img != null && ObjectData.getImageAll(var13.pi[AY[var4][var26][0]].id, ObjectData.HashImageCharPart, (short)10000).img != null) {
                           if (this.QJ == 0) {
                              this.QP.img.width = mImage.getImageWidth(this.QP.img.image);
                              this.QP.img.height = mImage.getImageHeight(this.QP.img.image) / 3;
                              this.QJ = mImage.getImageHeight(this.QP.img.image) / this.QP.img.height;
                           } else if (this.QQ[this.QG][this.QF] < this.QJ) {
                              var1.drawRegion(this.QP.img, 0, this.QP.img.height * this.QQ[this.QG][this.QF], this.QP.img.width, this.QP.img.height, var5, var15, var16, var8);
                              var22 = false;
                           }
                        }
                     }
                  }
               }
            }

            if (var22) {
               SmallImage.AA(var1, var13.pi[AY[var4][var26][0]].id, var15, var16, var5, var8);
            }

            if (!GameCanvas.lowGraphic && var6 && AD(var4) && var26 == 0 && this.AP >= 0 && this.RJ < 2) {
               var1.drawRegion(AvMain.imgEye, this.AP * 5, this.RJ * 5, 5, 5, var9, var15 + var10, var16 + 6 + this.AO, 0);
               if (this.BS == 770) {
                  var1.drawRegion(AvMain.imgEye, this.AP * 5 + 2, this.RJ * 5, 2, 5, var9, var15 + var10 + (this.type_left_right == 0 ? -3 : 6), var16 + 6 + this.AO, 0);
               } else if (this.BS == 769 && mGraphics.zoomLevel > 1) {
                  var1.drawRegion(AvMain.imgEye, this.AP * 5 + 2, this.RJ * 5, 2, 5, var9, var15 + var10 + (this.type_left_right == 0 ? -2 : 5), var16 + 6 + this.AO, 0);
               }
            }
         }
      }

      short var24;
      if ((var24 = this.AK()) >= 0) {
         if (this.QK == null) {
            this.QK = new FrameImage(var24, this.QT);
            return;
         }

         if (this.QU == 0) {
            var12 = var2 + this.QR;
            if (this.type_left_right == 2) {
               var12 = var2 - this.QR;
            }

            this.QK.drawFrame(this.QV, var12, var3 + this.QS, this.type_left_right, 3, var1);
         }
      }

   }

   private static boolean AD(int var0) {
      return var0 != 17 && var0 != 18 && var0 != 53 && var0 != 58 && var0 != 59;
   }

   private short AK() {
      for(int var1 = 0; var1 < PT.length; ++var1) {
         if (this.BT == PT[var1]) {
            this.QR = 3;
            this.QS = -47;
            this.QT = 4;
            this.QU = 0;
            if (GameCanvas.gameTick % 5 == 0 && CRes.random(6) != 0) {
               ++this.QV;
               if (this.QV >= 4) {
                  this.QV = 0;
               }
            }

            return 384;
         }
      }

      return -1;
   }

   private short AL() {
      if (dataEffHair == null) {
         return this.QX;
      } else {
         try {
            for(int var1 = 0; var1 < dataEffHair.length; ++var1) {
               if (this.BW == dataEffHair[var1].id) {
                  return (short)dataEffHair[var1].idIcon;
               }
            }

            return this.QX;
         } catch (Exception var2) {
            var2.printStackTrace();
            return this.QX;
         }
      }
   }

   private short CA() {
      switch(this.BT) {
      case 719:
         return 301;
      case 748:
         return 300;
      case 756:
         return 302;
      default:
         return this.QY;
      }
   }

   private short CB() {
      for(int var1 = 0; var1 < PU.length; ++var1) {
         if (this.BS == PU[var1]) {
            ++this.AG;
            if (this.QH == 0) {
               if (this.AG > 400 && CRes.random(60) == 0) {
                  this.QH = 1;
                  this.AG = 0;
               }
            } else if (this.QH == 1) {
               if (this.AG > 4) {
                  this.QH = 2;
                  this.AG = 0;
               }
            } else if (this.QH != 2 || this.AG > 120 && CRes.random(30) == 0) {
               this.QH = 0;
               this.AG = 0;
            }

            return 303;
         }
      }

      return -1;
   }

   public final void BC() {
      if (GameScreen.player != null) {
         this.BS = GameScreen.player.BS;
         this.BU = GameScreen.player.BU;
         this.BT = GameScreen.player.BT;
         this.BV = GameScreen.player.BV;
         this.BX = GameScreen.player.BX;
         this.BW = GameScreen.player.BW;
         this.BY = GameScreen.player.BY;
         this.clazz = GameScreen.player.clazz;
         this.PR = 0;
         this.PS = 0;
         this.BH();
      }
   }

   private mPart AE(int var1) {
      mPart var2 = null;
      short var3 = -1;
      switch(var1) {
      case 0:
         var3 = this.BS;
         break;
      case 1:
         if (this.NH) {
            var3 = this.BU;
         }
         break;
      case 2:
         var3 = this.BT;
         break;
      case 3:
         if (this.NF && (this.isDonotShowWeaponF == 1 || this.CC())) {
            var3 = this.BV;
         }
         break;
      case 4:
         if (this.isDonotShowHat != 1 && (this.isDonotShowHat == 0 || this.AR)) {
            var3 = this.BX;
         }
         break;
      case 5:
         var3 = this.BW;
         break;
      case 6:
         if (this.CC()) {
            if (this.isDonotShowWeaponF == 0) {
               var3 = this.BY;
            }
         } else if (this.NF && this.isDonotShowWeaponF == 0) {
            var3 = this.BY;
         }
      }

      if (var3 >= 0) {
         var2 = CharPartInfo.getPart(var3);
      }

      return var2;
   }

   private boolean CC() {
      for(int var1 = 0; var1 < PV.length; ++var1) {
         if (this.BY == PV[var1]) {
            return false;
         }
      }

      return true;
   }

   public static void AF(mGraphics var0, int var1, int var2, int var3, int var4, int var5) {
      mPart var7 = null;
      if (var1 >= 0) {
         var7 = CharPartInfo.getPart((short)var1);
      }

      mPart var6 = var7;
      if (var7 != null && var7.pi != null) {
         boolean var8 = false;
         var5 = AY[0][var2][1];
         SmallImage.AA(var0, var6.pi[AY[0][var2][0]].id, var3 + AY[0][var2][1] + var6.pi[AY[0][var2][0]].dx, var4 + var5 + var6.pi[AY[0][var2][0]].dy, 0, 0);
      }

   }

   public void AB(mGraphics var1, int var2, int var3, int var4) {
      short var5 = -1;
      if (this.isDonotShowHat == 0) {
         var5 = this.BX;
      }

      AA(var1, this.BS, this.BW, var5, var2, var3 + 38, var4);
   }

   public static void AA(mGraphics var0, short var1, short var2, short var3, int var4, int var5, int var6) {
      for(int var7 = 0; var7 < 3; ++var7) {
         int var8 = RD[var7];
         mPart var9 = null;
         if (var7 == 0 && var1 >= 0) {
            var9 = CharPartInfo.getPart(var1);
         } else if (var7 == 1 && var2 >= 0) {
            var9 = CharPartInfo.getPart(var2);
         } else if (var7 == 2 && var3 >= 0) {
            var9 = CharPartInfo.getPart(var3);
         }

         if (var9 != null && var9.pi != null) {
            if (var6 == 0) {
               SmallImage.AA(var0, var9.pi[AY[0][var8][0]].id, var4 + AY[0][var8][1] + var9.pi[AY[0][var8][0]].dx, var5 + AY[0][var8][2] + var9.pi[AY[0][var8][0]].dy, var6, 0);
            } else {
               SmallImage.AA(var0, var9.pi[AY[0][var8][0]].id, var4 - AY[0][var8][1] - var9.pi[AY[0][var8][0]].dx, var5 + AY[0][var8][2] + var9.pi[AY[0][var8][0]].dy, var6, 24);
            }
         }
      }

   }

   public static void AB(mGraphics var0, short var1, short var2, short var3, int var4, int var5, int var6) {
      for(var6 = 0; var6 < 3; ++var6) {
         int var7 = RD[var6];
         mPart var8 = null;
         if (var6 == 0 && var1 >= 0) {
            var8 = CharPartInfo.getPart(var1);
         } else if (var6 == 1 && var2 >= 0) {
            var8 = CharPartInfo.getPart(var2);
         } else if (var6 == 2 && var3 >= 0) {
            var8 = CharPartInfo.getPart(var3);
         }

         if (var8 != null && var8.pi != null) {
            SmallImage.AA(var0, var8.pi[AY[0][var7][0]].id, var4 + AY[0][var7][1] + var8.pi[AY[0][var7][0]].dx, var5 + AY[0][var7][2] + (var8.pi[AY[0][var7][0]].dy < -10 ? var8.pi[AY[0][var7][0]].dy / 4 : var8.pi[AY[0][var7][0]].dy), 0, 0);
         }
      }

   }

   public void BD() {
   }

   public final void BE() {
      mVector var1;
      (var1 = new mVector()).addElement(new iCommand(this.BF, 2, this));
      if (this.CD().size() > 0) {
         var1.addElement(new iCommand(T.BU, 0, this));
      }

      GameCanvas.menu.AA(var1, this.BA, this.ID, this.typeObject, false, 0, false);
   }

   private mVector CD() {
      mVector var1 = new mVector();

      for(int var2 = 0; var2 < Player.QI.size(); ++var2) {
         MainQuest var3;
         if ((var3 = (MainQuest)Player.QI.elementAt(var2)).AC == this.ID || var3.AD == this.ID) {
            var1.addElement(var3);
         }
      }

      return var1;
   }

   public iCommand getCenterCmd() {
      return null;
   }

   public final void setResetWearing() {
      this.AA(this.AA);
      this.BT = this.AB;
      this.BU = this.AC;
      this.AB(this.AD);
      this.RF = false;
      this.BH();
   }

   public final void AA(short[] var1) {
      this.BV = var1[0];
      this.BX = var1[1];
      if (this.BX == 0) {
         this.BX = -1;
      }

      this.BT = var1[3];
      this.BU = var1[5];
      this.AA = this.BS;
      this.AB = this.BT;
      this.AC = this.BU;
      this.AD = this.BW;
      this.BI();
      if (this.clazz == 4) {
         switch(this.BV) {
         case 6:
         case 227:
         case 463:
            this.MW = 0;
            break;
         case 177:
         case 246:
            this.MW = 2;
            break;
         case 182:
            this.MW = 1;
            break;
         case 262:
            this.MW = 3;
            break;
         case 467:
            this.MW = 4;
            break;
         default:
            this.MW = 0;
         }
      } else if (this.clazz == 2) {
         switch(this.BV) {
         case 5:
         case 225:
         case 244:
         case 260:
         case 461:
            this.MW = 0;
            break;
         case 183:
            this.MW = 1;
            break;
         case 465:
            this.MW = 2;
         }
      }

      this.BH();
   }

   public final void AA(short var1) {
      this.BS = var1;
      this.PS = 0;
      var1 = this.CB();
      this.QL = null;
      if (var1 >= 0) {
         this.QM = ObjectData.getImageAll(var1, ObjectData.HashImageOtherNew, (short)23000);
      }

      if (this.BX != 460 && (this.BS == 0 || this.BS == 487 || this.BS == 488 || this.BS == 489 || this.BS == 573 || this.BS == 574 || this.BS == 575 || this.BS == 614 || this.BS == 769 || this.BS == 770 || this.BS == 768 || this.BS == 731)) {
         if (mGraphics.zoomLevel <= 1) {
            if (this.BS == 573) {
               this.AN = 3;
               this.AO = 0;
               this.AP = 0;
            } else if (this.BS == 574) {
               this.AN = 1;
               this.AO = -1;
               this.AP = 1;
            } else if (this.BS == 575) {
               this.AN = 1;
               this.AO = 0;
               this.AP = 0;
            } else if (this.BS != 731 && this.BS != 614) {
               if (this.BS == 768) {
                  this.AN = 5;
                  this.AO = -2;
                  this.AP = 3;
               } else if (this.BS == 769) {
                  this.AN = 1;
                  this.AO = -1;
                  this.AP = 4;
               } else if (this.BS == 770) {
                  this.AN = 1;
                  this.AO = 0;
                  this.AP = 0;
               } else {
                  this.AN = 0;
                  this.AO = 0;
                  this.AP = 0;
               }
            } else {
               this.AN = 2;
               this.AO = -1;
               this.AP = 2;
            }
         } else if (this.BS == 573) {
            this.AN = 2;
            this.AO = 0;
            this.AP = 0;
         } else if (this.BS == 574) {
            this.AN = 0;
            this.AO = 0;
            this.AP = 1;
         } else if (this.BS == 575) {
            this.AN = 0;
            this.AO = -1;
            this.AP = 0;
         } else if (this.BS != 731 && this.BS != 614) {
            if (this.BS == 768) {
               this.AN = 4;
               this.AO = -2;
               this.AP = 3;
            } else if (this.BS == 769) {
               this.AN = 1;
               this.AO = -1;
               this.AP = 4;
            } else if (this.BS == 770) {
               this.AN = 1;
               this.AO = -1;
               this.AP = 0;
            } else {
               this.AN = 0;
               this.AO = 0;
               this.AP = 0;
            }
         } else {
            this.AN = 0;
            this.AO = -1;
            this.AP = 2;
         }
      } else {
         this.AP = -1;
      }
   }

   public final void AB(short var1) {
      this.PR = 0;
      this.BW = var1;
      var1 = this.AL();
      this.QL = null;
      if (var1 >= 0) {
         this.QL = ObjectData.getImageAll(var1, ObjectData.HashImageOtherNew, (short)23000);
      }

   }

   public final void BH() {
      this.QK = null;
      this.typeEfffashion = -1;
      this.QC = 0;
      this.QN = null;
      boolean var1 = false;

      int var2;
      for(var2 = 0; var2 < PW.length; ++var2) {
         if (this.BT == PW[var2]) {
            var1 = true;
         }
      }

      this.RE = false;

      for(var2 = 0; var2 < PX.length; ++var2) {
         if (this.BS == PX[var2]) {
            this.RE = true;
         }
      }

      if (this.typeObject == 0) {
         short var3;
         label57: {
            this.AR = false;
            if (!this.RF) {
               if (this.BT == 950 || this.BT == 963 || this.BT == 972) {
                  this.hOne = 72;
                  break label57;
               }

               if (var1) {
                  this.LZ = -6;
                  this.hOne = 62;
                  if (this.BT == 748) {
                     this.AR = true;
                  }

                  if (this.BT == 798 && (var3 = this.AK()) >= 0) {
                     this.QK = new FrameImage(var3, this.QT);
                  }
                  break label57;
               }
            }

            this.LZ = 0;
            this.hOne = 52;
         }

         if (this.BT == 810 || this.BT == 813) {
            this.typeEfffashion = 1;
         }

         if ((var3 = this.CA()) != -1) {
            this.QN = ObjectData.getImageAll(var3, ObjectData.HashImageOtherNew, (short)23000);
         }
      }

   }

   public final void AB(short[] var1) {
      this.AA(var1[0]);
      this.BT = var1[1];
      this.BU = var1[2];
      this.BV = var1[3];
   }

   public final void BI() {
      for(int var1 = 0; var1 < this.vecEffBuff.size(); ++var1) {
         MainBuff var2;
         if ((var2 = (MainBuff)this.vecEffBuff.elementAt(var1)).AG && !var2.isRemove) {
            this.AA(var2.AI);
            this.BT = var2.AJ;
            this.BU = var2.AK;
            this.AB((short)-1);
            this.RF = true;
            this.BH();
            return;
         }
      }

   }

   public final void AC(short[] var1) {
      if (var1[0] >= 0) {
         this.BV = var1[0];
      }

      if (var1[1] == -2) {
         this.BX = -1;
      } else if (var1[1] >= 0) {
         this.BX = var1[1];
         if (this.BX == 0) {
            this.BX = -1;
         }
      }

      if (var1[3] >= 0) {
         this.BT = var1[3];
      }

      if (var1[5] >= 0) {
         this.BU = var1[5];
      }

      if (var1[1] == -2) {
         this.AB((short)-1);
      } else if (var1[7] >= 0) {
         this.AB(var1[7]);
      }

      if (var1[6] >= 0) {
         this.AA(var1[6]);
      }

   }

   public void a_() {
   }

   public void setFireObject(int var1) {
   }

   public void setImgMonSterforOtherPlayer(byte var1) {
   }

   public final void setSpeed(int var1, int var2) {
      this.CN = var1 * (100 + GameCanvas.percentUPCV) / 100;
      this.CO = var2 * (100 + GameCanvas.percentUPCV) / 100;
   }

   public final void AB(byte var1) {
      if (var1 == 2) {
         if (this.x < 100) {
            this.toX = this.x;
            this.x = 0;
            return;
         }

         if (this.x > GameCanvas.loadmap.maxWMap - 100) {
            this.toX = this.x;
            this.x = GameCanvas.loadmap.maxWMap;
            return;
         }
      } else if (var1 == 1) {
         this.isTanHinh = true;
         this.LC = 6;
         if (GameScreen.isShowNameSUPER_BOSS) {
            GameScreen.addEffectEnd_ObjTo((short)32, 0, this.x, this.y, (short)this.ID, (byte)this.typeObject, (byte)this.type_left_right, (MainObject)null);
            return;
         }
      } else if (var1 == 3) {
         int var5 = 0;
         int var2 = -1;

         for(int var3 = 0; var3 < LoadMap.mSea.length; ++var3) {
            if (LoadMap.mSea[var3][0] == GameCanvas.loadmap.idMapLoadMap) {
               var5 = LoadMap.mSea[var3][3];
               var2 = var3;
               break;
            }
         }

         if (var2 == -1) {
            return;
         }

         if (GameScreen.vecBoat.size() > 0) {
            byte var6 = 40;
            if (GameScreen.vecBoat.size() > 6) {
               var6 = 20;
            }

            if (GameScreen.vecBoat.size() > 10) {
               var6 = 5;
            }

            if (GameScreen.vecBoat.size() > 0) {
               Boat var4 = (Boat)GameScreen.vecBoat.elementAt(GameScreen.vecBoat.size() - 1);
               if (var2 != -1) {
                  var5 = var4.x + LoadMap.mSea[var2][5] * var6;
               }
            }
         }

         this.AA((byte)((byte)LoadMap.mSea[var2][2]), (int)var5, (int)LoadMap.mSea[var2][4]);
      }

   }

   public final void BJ() {
      ++this.f;
      if (this.Action != 0) {
         this.AI = 0;
      } else if (this.AI < 100) {
         ++this.AI;
      }

      switch(this.Action) {
      case 0:
         this.actionStand();
         break;
      case 1:
         int[] var1 = this.PP;
         if (this.BV() && this.boatSea != null) {
            var1 = this.feStand;
         } else if (GameCanvas.gameTick % 3 == 0) {
            int var10001;
            int var10002;
            int var10003;
            if (GameCanvas.loadmap.AA(this.x, this.y) == 0 && this.isInfo) {
               var10001 = this.x;
               var10002 = this.y;
               var10003 = this.type_left_right;
               byte var10006 = this.AM;
               this.AM = (byte)(var10006 + 1);
               GameScreen.AA((byte)0, var10001, var10002, var10003, var10006);
            }

            if (this.MY == 5) {
               var10001 = this.x;
               var10002 = this.y + 4;
               var10003 = this.type_left_right;
               ++this.AM;
               GameScreen.AA((short)157, var10001, var10002, var10003);
            }

            if (this.AM > 100) {
               this.AM = 0;
            }
         }

         if (this.f > var1.length - 1) {
            this.f = 0;
         }

         if (this.vx == 0 && this.vy == 0 && this.posTransRoad == null && !this.NR) {
            this.Action = 0;
            this.f = 0;
         }

         this.frame = var1[this.f];
         break;
      case 2:
         this.c_();
         break;
      case 3:
         this.updateAva();
         break;
      case 4:
         this.frame = 38;
         this.AU();
         return;
      case 5:
         this.frame = 60;
      }

      this.BL();
      if (this.Action != 2) {
         this.AU();
         if (!this.NF) {
            this.NF = true;
         }

         if (!this.NH) {
            this.NH = true;
         }

         if (this.isTanHinh) {
            if (this.LC > 0) {
               --this.LC;
               return;
            }

            this.isTanHinh = false;
         }
      }

   }

   public void actionStand() {
      if (this.f > this.feStand.length - 1) {
         this.f = 0;
      }

      this.frame = this.feStand[this.f];
   }

   public final void AA(boolean var1) {
      ++this.f;
      if (this.BM != null) {
         int var2 = this.Action;
         if (this.BV() && this.boatSea != null && this.Action == 1) {
            var2 = 0;
         }

         boolean var3 = false;
         if (var1) {
            if (this.NI && this.Action == 0) {
               if (this.f > this.BN.length - 1) {
                  var3 = true;
               }
            } else if (this.f > this.BM[this.Action].length - 1) {
               var3 = true;
            }
         } else if (this.f > this.BM[var2].length - 1) {
            var3 = true;
         }

         if (var3) {
            this.f = 0;
            if (this.Action == 3 || this.Action == 2) {
               this.Action = 0;
               this.vx = 0;
               this.vy = 0;
            }
         }

         if (this.Action == 1 && this.vx == 0 && this.vy == 0) {
            this.Action = 0;
            this.f = 0;
            if (var1) {
               this.setSpeed(5, 5);
               this.LY = 1;
               this.CG = 0;
            }
         }

         if (this.LB > 0) {
            --this.LB;
         }

      }
   }

   public void c_() {
      if (this.plashNow != null) {
         ++this.RH;
         int var1;
         if ((var1 = this.plashNow.AA()) == -1 || this.RH >= 200) {
            if (GameScreen.player != null && this == GameScreen.player) {
               ++this.RG;
               if (this.RG >= 3) {
                  GlobalService.getInstance().Obj_Move((short)this.x, (short)this.y);
                  this.RG = 0;
               }
            }

            this.plashNow = null;
            this.Action = 0;
            this.resetAction();
            this.RH = 0;
            return;
         }

         this.frame = var1;
      }

   }

   public final void BL() {
      if (this.Dir == 1 || this.Dir == 2) {
         if (this.toX > this.x) {
            this.type_left_right = 2;
         } else if (this.toX < this.x) {
            this.type_left_right = 0;
         }
      }

      if (this.Dir == 0 && this.type_left_right == 2) {
         this.type_left_right = 0;
      } else {
         if (this.Dir == 2 && this.type_left_right == 0) {
            this.type_left_right = 2;
         }

      }
   }

   public void updateAva() {
      this.Action = 0;
   }

   public final void BN() {
      this.MI = 0;

      for(int var1 = 0; var1 < Player.QI.size(); ++var1) {
         MainQuest var2;
         if ((var2 = (MainQuest)Player.QI.elementAt(var1)).AC == this.ID) {
            if (this.MI == 0) {
               if (var2.AB == 0) {
                  this.MI = 1;
               }

               if (var2.AB == 1) {
                  this.MI = 2;
               }

               if (var2.AB == 2) {
                  this.MI = 3;
               }
            } else if (this.MI == 1) {
               if (var2.AB == 2) {
                  this.MI = 3;
               }
            } else if (this.MI == 2) {
               if (var2.AB == 0) {
                  this.MI = 1;
               }

               if (var2.AB == 2) {
                  this.MI = 3;
               }
            }
         } else if (var2.AD == this.ID && this.MI == 0) {
            this.MI = 2;
         }

         if (this.MI == 3) {
            return;
         }
      }

   }

   public final void BO() {
      if (this == GameScreen.player && Player.isGhost) {
         this.RJ = 1;
      } else {
         if (this.RJ < 2) {
            ++this.RK;
            if (this.RK > 1 && this.RK < 6) {
               this.RJ = 1;
            } else {
               this.RJ = 0;
            }

            if (this.RK >= 8) {
               this.RK = 0;
               this.RJ = 2;
               if (this.clazz != 4) {
                  this.RI = CRes.random(50, 100);
                  return;
               }

               this.RI = CRes.random(20, 70);
               return;
            }
         } else {
            ++this.RK;
            if (this.RK >= this.RI) {
               this.RK = 0;
               this.RJ = 0;
            }
         }

      }
   }

   public void AA(MainBuff var1) {
      for(int var2 = 0; var2 < this.vecEffBuff.size(); ++var2) {
         if (((MainBuff)this.vecEffBuff.elementAt(var2)).IdBuff == var1.IdBuff) {
            this.vecEffBuff.removeElement(var2);
            --var2;
         }
      }

      var1.setYlech(this);
      this.vecEffBuff.addElement(var1);
   }

   public void AB(MainBuff var1) {
      for(int var2 = 0; var2 < this.vecEffBuffNew.size(); ++var2) {
         if (((MainBuff)this.vecEffBuffNew.elementAt(var2)).IdBuff == var1.IdBuff) {
            this.vecEffBuffNew.removeElement(var2);
            --var2;
         }
      }

      var1.setYlech(this);
      this.vecEffBuffNew.addElement(var1);
   }

   public final void BP() {
      this.NT = new short[]{21, 34, 38};
      mPosMapTrain = new int[3][];
      this.LA = CRes.random(70);

      for(int var1 = 0; var1 < MainPlayer.AA[0].length; ++var1) {
         mPosMapTrain[var1] = new int[2];

         for(int var2 = 0; var2 < MainPlayer.AA[0][var1].length; ++var2) {
            mPosMapTrain[var1][var2] = MainPlayer.AA[0][var1][var2] + CRes.random_Am_0(2);
         }
      }

   }

   public int BQ() {
      return 1;
   }

   public final void addEffSpec(short var1, short var2) {
      for(int var3 = 0; var3 < this.vecEffspec.size(); ++var3) {
         Class_BR var4;
         if ((var4 = (Class_BR)this.vecEffspec.elementAt(var3)).typeEffect == var1) {
            if (var4.AE() > var2) {
               return;
            }

            this.vecEffspec.removeElement(var4);
            break;
         }
      }

      Class_BR var5 = new Class_BR(this, var1, var2);
      this.vecEffspec.addElement(var5);
      if (var5.typeEffect == 15) {
         this.CE();
      }

   }

   public final void BR() {
      if (this.timeBeginUpdateMove == 0) {
         this.toX = this.toXNew;
         this.toY = this.toYNew;
      }

      if (this.x != this.toX || this.y != this.toY) {
         if (CRes.abs(this.x - this.toX) <= this.CN && CRes.abs(this.y - this.toY) <= this.CN) {
            if (this.Action != 2 && this.Action != 3 && this.Action != 4) {
               this.toX = this.x;
               this.toY = this.y;
               this.vx = 0;
               this.vy = 0;
               this.Action = 0;
            }

         } else {
            this.BB();
         }
      }
   }

   public void BS() {
   }

   public void AA(byte var1, int var2, int var3) {
   }

   public final boolean BT() {
      return this.typePlayer != 2 && this.typePlayer != 3;
   }

   public int AM() {
      return -1;
   }

   public final void BU() {
      this.IT = mFont.tahoma_7b_white.getWidth(this.name) + 8;
      this.IT += this.IT % 2;
      this.AE = this.IT + 6;
      if (this.AE < 74) {
         this.AE = 74;
      }

   }

   public final boolean BV() {
      return LoadMap.specMap == 4 || this.typeActionBoat != 0 || GameCanvas.currentScreen == GameCanvas.loginScr || !LoadMap.isOnlineMap && (LoadMap.specMap == 5 || LoadMap.specMap == 8 || LoadMap.specMap == 12);
   }

   public void addEffBuff(byte var1, short var2, short var3) {
      mVector var4 = new mVector();
      Object_Effect_Skill var5;
      (var5 = new Object_Effect_Skill(this.ID, this.typeObject)).AA(0, this.Hp, 0);
      var4.addElement(var5);
      MainSkill var6;
      (var6 = new MainSkill((short)-1, var2)).setTypeBuff(var1, var2, var3);
      this.AB(var6, var4);
   }

   public static MainObject AC(short var0) {
      for(int var1 = 0; var1 < GameScreen.vecPlayers.size(); ++var1) {
         MainObject var2;
         if ((var2 = (MainObject)GameScreen.vecPlayers.elementAt(var1)).typeObject == 10 && var2.IDMainShiper == var0) {
            return var2;
         }
      }

      return null;
   }

   public final void AF(int var1, int var2) {
      this.x = var1;
      this.y = var2;
      this.toX = var1;
      this.toY = var2;
      this.dy = 0;
      this.Action = 0;
      this.f = 0;
   }

   public final void AA(short var1, int var2) {
      for(int var3 = 0; var3 < this.PL.size(); ++var3) {
         DataSkillEff var4;
         if ((var4 = (DataSkillEff)this.PL.elementAt(var3)) != null && var4.idEff == var1) {
            this.PL.removeElement(var3);
         }
      }

      DataSkillEff var5 = new DataSkillEff(var1, var2);
      this.PL.addElement(var5);
   }

   public final void addDataEff(short var1, int var2, byte var3, byte var4) {
      for(int var5 = 0; var5 < this.PL.size(); ++var5) {
         DataSkillEff var6;
         if ((var6 = (DataSkillEff)this.PL.elementAt(var5)) != null && var6.idEff == var1) {
            this.PL.removeElement(var5);
         }
      }

      DataSkillEff var7 = new DataSkillEff(var1, var2, var3, var4);
      this.PL.addElement(var7);
   }

   public final void removeDataEff(short var1) {
      for(int var2 = 0; var2 < this.PL.size(); ++var2) {
         DataSkillEff var3;
         if ((var3 = (DataSkillEff)this.PL.elementAt(var2)) != null && var3.idEff == var1) {
            this.PL.removeElement(var2);
         }
      }

   }

   public void AA(short var1, short var2, byte var3) {
   }

   public void BW() {
   }

   public void BX() {
   }

   public void BY() {
   }

   public void AA(CatalogyMonster var1) {
   }

   public final void AE(short var1) {
      if (var1 == -1 || var1 >= 300) {
         this.QY = var1;
      }

      this.QC = 0;
      short var2;
      if ((var2 = this.CA()) != -1) {
         this.QN = ObjectData.getImageAll(var2, ObjectData.HashImageOtherNew, (short)23000);
      }

   }

   public final void AF(short var1) {
      if (var1 == -1 || var1 >= 300) {
         this.QZ = var1;
      }

      this.QI = 0;
      short var2;
      if ((var2 = this.QZ) != -1) {
         this.QO = ObjectData.getImageAll(var2, ObjectData.HashImageOtherNew, (short)23000);
      }

   }

   public final void AG(short var1) {
      if (var1 == -1 || var1 >= 300) {
         this.RA = var1;
      }

      this.QJ = 0;
      short var2;
      if ((var2 = this.RA) != -1) {
         this.QP = ObjectData.getImageAll(var2, ObjectData.HashImageOtherNew, (short)23000);
      }

   }

   public final void AH(short var1) {
      this.QX = var1;
      var1 = this.AL();
      this.QL = null;
      if (var1 >= 0) {
         this.QL = ObjectData.getImageAll(var1, ObjectData.HashImageOtherNew, (short)23000);
      }

   }

   public boolean BZ() {
      return false;
   }

   private void CE() {
      if (this.vecEffspec.size() != 0) {
         mVector var1 = new mVector();

         for(int var2 = 0; var2 < this.vecEffspec.size(); ++var2) {
            Class_BR var3;
            if ((var3 = (Class_BR)this.vecEffspec.elementAt(var2)).typeEffect == 15) {
               var1.addElement(var3);
            }
         }

         if (var1.size() >= 2) {
            ((Class_BR)this.vecEffspec.elementAt(0)).AA(-7);
            ((Class_BR)this.vecEffspec.elementAt(1)).AA(7);
         } else {
            if (var1.size() > 0) {
               ((Class_BR)this.vecEffspec.elementAt(0)).AA(0);
            }

         }
      }
   }
}
