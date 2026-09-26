import java.io.DataInputStream;

public final class Plash {
   private static MyHashTable hashPlash = new MyHashTable();
   private MainSkill skill;
   private Plashdata plashdata;
   private short typePlash;
   private short timeEndPlash;
   private int f;
   private int removef;
   private int fAddEff;
   private mVector vecObj;
   private MainObject objFire;
   private boolean isShow;
   private byte isNextf;
   private long timebeginSkill;

   static {
      short[][] var10000 = new short[][]{{8, 8, 8, 9, 9, 9, 10, 10, 10, 13, 13, 15, 15, 13, 13, 17, 17, 13, 13, 15, 15, 13, 13}, {37, 37, 37, 37, 37, 37, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17}, {8, 8, 9, 10, 27, 27, 27, 27, 27, 27, 27, 27, 30, 30, 29, 29, 27, 27}, {8, 9, 10, 21, 25, 25}, {8, 9, 10, 10, 10, 10, 10, 10, 10, 35, 37, 31, 31, 33, 35}, {8, 8, 9, 9, 10, 35, 35, 37}, {8, 8, 9, 9, 39, 39, 23, 23, 25, 25}, {31, 39, 31, 39, 31, 39, 31, 39, 31, 39, 31, 39}, {31, 31, 31, 39, 39, 31, 31, 31, 39, 39, 31, 31, 31, 39, 39}, {8, 8, 9, 9, 27, 27, 27, 27, 27, 27, 30, 30, 28, 28, 28, 28, 29, 29, 28, 28, 30, 30}, {28, 29, 29, 28, 28, 29, 29, 28, 28, 29, 29, 28}, {8, 8, 17, 17, 17, 14, 14, 15, 15, 15, 14, 14, 56, 56, 56}, {8, 8, 9, 10, 27, 27, 29, 29, 29, 27, 30, 30, 30, 13, 15, 15, 15}, {13, 13, 13, 13, 13, 13, 15, 15, 15, 13, 13, 15, 15, 15, 13, 13, 17, 17, 17}, {8, 8, 9, 9, 31, 31, 31, 31, 31, 31, 39, 39, 31, 31, 31, 31, 39, 39, 31, 31, 31, 31, 31}, {8, 8, 9, 9, 31, 31, 31, 31, 33, 33, 33, 35, 35, 37, 37, 37, 31, 33, 35, 37, 37}, {28, 28, 28, 30, 30, 30, 30, 30, 30, 30, 30, 55, 55, 55, 56, 56, 56, 27}, {10, 10, 8, 8, 8, 8, 9, 9, 8, 8, 8, 10, 10, 22, 22, 22, 22, 22, 22, 25, 25}, {23, 23, 23, 24, 22, 22, 22, 22, 26, 26, 25, 25, 25, 25}, {8, 8, 9, 10, 17, 17, 17, 14, 14, 15, 15, 15}, {8, 8, 9, 10, 45, 45, 43, 44, 44, 45, 46, 46, 47, 26, 26, 25, 25, 25, 25}, {8, 8, 9, 9, 17, 17, 17, 14, 14, 15, 15, 15, 45, 45, 45, 45, 43, 43, 44, 45, 45, 46, 47, 26, 26, 25, 25, 25, 25, 25}, {8, 8, 9, 9, 11, 11, 11, 18, 18, 18, 14, 14, 16, 16, 16, 12, 12}, {8, 8, 9, 9, 11, 11, 11, 18, 18, 18, 14, 14, 16, 16, 16, 45, 45, 45, 43, 43, 44, 45, 46, 47, 26, 26, 25, 25, 25}, {13, 13, 13, 19, 19, 15, 15, 15, 13, 13, 17, 17, 17}, {14, 14, 14, 20, 20, 16, 16, 16, 14, 14, 18, 18, 18}, {25, 25, 25, 25, 25}, {8, 8, 9, 9, 10, 10, 23, 23, 23, 23, 25, 25, 25, 25, 25}, {8, 8, 9, 9, 31, 31, 31, 31, 31, 39, 39, 31, 31, 31, 31, 31, 31, 39, 39, 31, 31, 31, 31, 31}, {8, 8, 9, 10, 45, 45, 43, 44, 45, 46, 47, 26, 26, 25, 25, 25, 25}, {13, 13, 13, 13, 13, 13, 13, 15, 15, 15, 13, 13, 17, 17, 17}, {8, 9, 10, 10, 10, 10, 10, 10, 10, 10}, {8, 8, 9, 9, 39, 39, 23, 23, 25, 25, 8, 8, 9, 9, 39, 39, 23, 23, 25, 25}, {8, 8, 9, 9, 10, 35, 35, 37, 31, 31, 33}, {8, 8, 8, 9, 9, 9, 9, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 37, 9, 9, 8, 8}, {8, 8, 9, 9, 10, 13, 15, 13, 17, 13, 15, 13, 15, 13, 17, 13, 15, 13, 15, 13, 17, 13, 15, 13, 17, 13, 15, 13, 10, 10}, {8, 8, 9, 9, 15, 15, 15, 19, 19, 19}, {8, 9, 13, 13, 15, 15, 15}, {8, 9, 35, 35, 35, 37, 37}, {8, 9, 21, 21, 21, 25, 25}, {8, 9, 21, 21, 21, 21, 21, 21, 21, 21, 21, 25, 25}, {8, 8, 9, 9, 15, 15, 15, 27, 27, 28, 28, 28, 27}, {8, 8, 9, 9, 17, 17, 17, 13, 13, 16, 16}, {8, 8, 9, 9, 27, 28, 28, 28, 27, 27, 29, 29, 13, 13, 15, 15, 15, 15}, {8, 8, 9, 10, 17, 17, 17, 13, 13, 15, 15, 15, 13, 13, 17, 17, 17, 13, 13, 15, 15, 15, 8, 8, 8, 9, 9, 9, 10, 10, 10, 8, 8, 8, 9, 9, 9, 10, 10, 13, 13, 17, 18, 17, 17}, {8, 9, 10, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 25, 25, 25, 25, 25, 8, 8, 8, 8, 8, 8, 8, 8, 45, 43, 44, 45, 46, 47, 26, 26, 25, 25, 25, 25, 8}, {8, 8, 8, 8, 9, 9, 9, 9, 8, 8, 8, 8, 9, 9, 9, 9, 10, 10, 10, 10, 10, 13, 13, 15, 15, 13, 13, 17, 17, 13, 13, 15, 15, 13, 13}, {13, 13, 13, 13, 13, 13, 13, 15, 15, 15, 13, 13, 17, 17, 17, 13, 13, 13, 13, 13, 13, 13, 15, 15, 21, 21, 13, 13, 14, 14, 14, 14, 14, 14, 16, 16, 16, 14, 14, 18, 18, 18, 14, 14, 22, 22, 26, 26, 26, 14, 14, 14, 14, 13, 13}, {13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 14, 14, 14, 16, 16, 14, 14, 14, 14, 14, 18, 18, 18, 22, 22, 22, 22, 26, 26, 26, 23, 23, 23, 23, 23, 23, 25, 25, 25, 25}, {21, 21, 21, 21, 21, 21, 21, 21, 35, 35, 35}, {8, 8, 9, 9, 10, 10, 13, 13, 23, 23, 31, 31, 35}, {13, 13, 13, 13, 15, 15, 15}, {49, 49, 49, 49, 17, 17, 17}, {8, 9, 39, 39, 39, 41, 41}, {8, 8, 9, 9, 10, 10, 13, 13, 13, 13, 13, 13, 15, 15, 15, 15, 13, 13, 13, 13, 17, 17, 17, 17, 13, 13, 13, 13}, {8, 8, 9, 9, 10, 17, 17, 17, 13, 13, 15, 15, 15, 13, 13}, {8, 8, 9, 9, 10, 35, 35, 15, 15, 15, 35}, {10, 10, 9, 9, 8, 8, 8, 8, 8, 8, 8, 8, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 35, 35, 35, 35, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 10, 10, 10, 10, 9, 9, 9, 8, 8, 8, 8, 8}, {8, 8, 9, 9, 10, 10, 25, 25, 26, 26, 8}, {8, 8, 9, 9, 10, 10, 49, 49, 50, 50, 50, 50, 50, 50, 50, 50, 39, 39, 40, 40, 8}, {8, 8, 9, 9, 10, 10, 15, 15, 15, 15, 10, 10, 17, 17, 17, 17}, {8, 8, 9, 9, 10, 10, 22, 22, 22, 22, 22, 61, 61, 61, 61, 61, 61}, {8, 8, 9, 9, 10, 10, 11, 11, 11, 11, 11, 11, 15, 15, 15, 15, 10, 10, 17, 17, 17, 17, 11, 11, 11, 11, 11, 11, 11, 11, 11}, {8, 8, 9, 9, 10, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 11, 11, 16, 16, 16, 16, 16, 11, 11, 11}, {8, 8, 9, 9, 10, 0, 0, 0, 0, 0, 0, 0, 0, 16, 16, 18, 18, 28, 28, 16, 16, 18, 18, 28, 28, 16, 16, 18, 18, 28, 28, 16, 16, 16, 16, 18, 18, 28, 28, 16, 16, 18, 18, 0, 0}, {8, 8, 9, 9, 10, 10, 23, 23, 23, 23, 23, 23, 23, 11, 11, 25, 25, 25}, {8, 8, 9, 9, 10, 10, 10, 10, 10, 10, 13, 13, 15, 15, 13, 13, 15, 15, 13, 13, 15, 15, 13, 13, 15, 15, 13, 13, 15, 15, 13, 13, 10, 10}, {8, 8, 9, 9, 11, 11, 17, 17, 17, 17}, {8, 8, 9, 9, 10, 10, 10, 10, 10, 10, 10, 10, 43, 43, 44, 44, 45, 45, 46, 46, 47, 47, 48, 48, 43, 43, 44, 44, 45, 45, 8, 8}, {8, 8, 9, 9, 13, 13, 15, 15, 15, 15, 15, 15, 15, 13, 13, 15, 15, 15}, {8, 8, 9, 9, 10, 10, 10, 10, 10, 10, 21, 21, 15, 15, 15, 15, 15, 15, 15}, {8, 8, 9, 9, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 12, 12, 12, 12}, {8, 8, 9, 9, 10, 10, 10, 10, 11, 11, 13, 13, 11, 11, 13, 13, 11, 11, 13, 13, 10, 10}, {8, 8, 9, 9, 10, 10, 10, 10, 11, 11, 13, 13, 11, 11, 13, 13, 11, 11, 13, 13, 11, 11, 13, 13, 11, 11, 13, 13, 11, 11, 13, 13, 11, 11, 13, 13, 11, 11, 13, 13, 10, 10}, {8, 8, 9, 9, 10, 10, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 14, 15, 15, 15, 15, 13, 13, 13, 13, 13, 13, 13, 13, 13, 11, 17, 17, 17, 17, 10, 10}, {8, 8, 9, 9, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 11, 11, 11, 11, 26, 26, 26, 26, 26, 26, 51, 51, 51, 51, 51, 51, 51, 56, 56, 56, 60, 60, 10}, {8, 8, 9, 9, 10, 10, 16, 16, 16, 16, 16, 16, 16, 16, 10, 10}, {8, 8, 9, 9, 11, 11, 11, 11}, {8, 8, 9, 9, 10, 10, 10, 10, 19, 19, 13, 13, 15, 15, 10, 10}, {8, 8, 9, 9, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25}, {8, 8, 9, 9, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25}, {8, 9, 25, 25, 25, 25, 26, 54, 25, 26, 54, 25, 26, 54, 25, 26, 54, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25}, {8, 8, 9, 9, 10, 10}, {22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22}, {8, 8, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 10, 10}, {22, 22, 22, 25, 25, 25, 25, 25}, {10, 10}, {2, 3, 3, 4, 5, 6, 6, 7}, {8, 8, 9, 9, 10, 10, 10, 10, 10, 10, 27, 27, 29, 29, 27, 27, 27, 27, 30, 30, 14, 14, 14, 14, 14, 14, 14, 14, 11, 11, 11, 11, 11, 11, 11, 11, 11, 18, 18, 18, 18, 14, 14, 14, 14, 14, 14, 14, 14}, {8, 8, 9, 9, 10, 10, 10, 10, 10, 10, 27, 27, 29, 29, 13, 13, 13, 13, 15, 15, 11, 11, 17, 17, 7, 7, 7, 7, 30, 30, 27, 27, 29, 29, 7, 7, 7, 7, 15, 15, 15, 15, 10, 10}, {8, 8, 9, 9, 10, 35, 35, 37, 31, 33, 1, 1, 35, 35, 35, 37, 31, 33, 1, 1, 35, 35, 35, 37, 31, 33, 1, 1, 35, 35, 35, 37, 31, 33}, {8, 8, 9, 9, 10, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 37, 37}, {15, 15}, {8, 8, 9, 9, 10, 10, 10, 10, 17, 17, 13, 13, 13, 13, 13, 16, 16}, {8, 8, 9, 9, 10, 10, 10, 10, 15, 15, 27, 27, 27, 27, 27, 27, 29, 29, 27, 27, 27, 27, 28, 28}, {8, 8, 9, 9, 27, 27, 27, 27, 28, 28, 13, 13, 13, 13, 13, 15, 15, 10, 10, 10, 10, 10, 10, 17, 17}, {8, 8, 9, 9, 19, 19, 19, 25, 25, 25, 13, 13, 13, 25, 25, 25}, {8, 8, 9, 9, 19, 19, 19, 25, 25, 25, 13, 13, 13, 25, 25, 13, 13, 13, 25, 25}, {8, 9, 10, 10, 13, 13, 15, 15, 15, 13, 13, 13, 13, 13, 13, 15, 15, 15, 13, 13, 13}, {13, 13, 15, 15, 13, 13, 13, 17, 17, 13, 13, 14, 14, 14, 16, 16, 14, 14, 14, 13}, {8, 9, 10, 10, 13, 13, 13, 13, 13, 13, 13, 13, 15, 15, 15, 13, 13, 13, 13, 13, 13, 13, 17, 17, 17, 13, 13, 13, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 16, 16, 16, 14, 14, 14, 13}, {10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 22, 22, 22, 25, 25}, {21, 21, 21, 22, 22, 25, 25, 21, 21, 21, 21, 21, 21, 21, 21, 22, 22, 22, 25, 25}, {14, 14, 14, 14, 10, 22, 22, 26, 26, 22, 22, 22, 26, 26, 10, 10, 10, 10, 9, 8}, {11, 11, 11, 11, 27, 29, 29, 27, 30, 30, 11, 11, 11, 11, 10}, {11, 11, 11, 11, 27, 29, 29, 27, 30, 30, 11, 11, 11, 11, 27, 29, 29, 27, 30, 30}, {8, 9, 10, 11, 11, 11, 11, 11, 27, 27, 29, 29, 29, 29, 27, 30, 30, 30, 11, 11, 11, 11, 11, 27, 27, 29, 29, 29, 29, 27, 27, 30, 30, 30, 11, 11, 11, 11, 11, 10}, {31, 31, 31, 39, 39, 31, 31, 31, 39, 39, 31, 31, 31, 39, 39, 31, 31, 31, 39, 39}, {31, 31, 31, 39, 39, 31, 31, 31, 39, 39, 31, 31, 31, 39, 39, 31, 31, 31, 39, 39}, {10, 10, 10, 35, 35, 37, 37, 31, 31, 33, 33, 33, 35, 35, 37, 37, 37, 31, 31, 37}, {35, 35, 37, 37, 31, 31, 33, 33, 35, 35, 37, 37, 31, 31, 33, 33, 35, 35, 37, 37}, {9, 10, 10, 10, 10, 10, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38}, {8, 9, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10}, {8, 9, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10}, {8, 9, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 23, 23, 23, 23, 23, 22, 21, 21, 26, 25, 25, 25, 25, 25, 25, 25, 10, 10, 10, 10}, {8, 9, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 23, 23, 23, 23, 23, 23, 23, 25, 25, 25, 25, 9, 8}, {8, 9, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 9, 8}, {8, 9, 10, 10, 10, 10, 10, 10, 10, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 10, 9, 8}, {8, 9, 10, 10, 10, 10, 10, 10, 10, 13, 13, 13, 15, 15, 15, 15, 15, 15, 15, 15, 15, 10, 9, 8}, {8, 9, 21, 21, 21, 25, 25, 13, 15, 15, 15}, {8, 9, 21, 21, 21, 25, 25, 27, 27, 27, 28, 28}, {8, 9, 10, 10, 19, 19, 18, 18, 21, 21, 15, 15, 10, 10}, {8, 8, 9, 10, 17, 17, 8, 8, 8, 9, 9, 10, 10, 18, 17, 17}, {13, 13, 13, 13, 13, 13, 13, 15, 22, 22, 26, 26, 20, 20, 16, 16}, {8, 8, 9, 9, 10, 27, 28, 28, 27, 30, 30, 27, 29, 29, 27, 10}, {31, 31, 31, 39, 39, 31, 31, 31, 31, 39, 39, 31, 31, 31, 31, 31}, {35, 35, 37, 31, 33, 1, 1, 35, 35, 35, 37, 31, 33, 1, 1, 35}, {10, 10, 25, 25, 25, 8, 8, 8, 8, 8, 8, 8, 8, 45, 43, 44, 45, 46, 47, 26, 26, 25, 25, 25, 8}, {13, 13, 13, 13, 13, 13, 13, 14, 14, 14, 16, 16, 14, 14, 14, 14, 14, 18, 18, 18, 22, 22, 22, 22}, {27, 27, 29, 27, 27, 29, 13, 13, 15, 15, 11, 11, 17, 17, 7, 7, 7, 7, 30, 30, 27, 27, 29, 10}, {8, 8, 8, 9, 9, 9, 10, 10, 10, 10, 39, 39, 39, 39, 8, 9, 9, 39, 39, 39, 39, 9, 9, 8}, {8, 9, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 9, 8}, {8, 9, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 9, 8}, {8, 8, 8, 8, 8, 8, 8, 8, 8, 13, 13, 15, 15, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8}, {8, 8, 9, 9, 27, 27, 27, 27, 27, 27, 30, 30, 28, 28, 28, 28, 29, 29, 28, 28, 30, 30, 28, 28, 29, 29, 28, 28, 30, 30}, {8, 8, 9, 9, 31, 31, 31, 31, 31, 39, 39, 31, 31, 31, 39, 39, 31, 31, 31, 31, 31, 31, 39, 39, 31, 31, 31, 31, 31, 31}, {8, 8, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 10, 10, 10, 9}, {13, 13, 13, 13, 13, 15, 15, 15}, {8, 9, 9, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 13, 13, 13, 15, 15}, {8, 9, 9, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10}, {8, 9, 10, 35, 35, 35, 35, 35, 37, 37}, {8, 9, 9, 10, 10, 21, 21, 21, 15, 15}, {8, 9, 10, 23, 22, 22, 22}, {8, 9, 10, 25, 25, 19, 19, 19}, {8, 9, 10, 10, 31, 31, 31, 31, 31, 33, 31, 31, 33, 31, 31, 33, 31, 31, 33, 31, 31, 33, 31, 31, 33}, {8, 9, 10, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35}, {8, 9, 10, 10, 10, 10, 10, 10, 10, 10, 23, 23, 25, 25}, {8, 9, 10, 10, 23, 23, 25, 25, 0, 0, 1, 1, 0, 0, 1, 1, 23, 23, 25, 25}, {8, 9, 10, 10, 17, 17, 19, 19, 15, 15, 21, 21, 10}, {37, 37, 37, 37, 37, 37, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22}, {15, 15, 15, 17, 17, 17}, {8, 9, 10, 10, 10, 10, 10, 10, 10, 10, 10, 25, 25, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21}, {8, 8, 9, 9, 10, 10, 10, 10, 10, 21, 21, 21, 21, 21, 21, 21, 25, 25, 25, 25, 10, 10, 9}, {8, 8, 9, 9, 10, 10, 10, 10, 10, 10, 10, 10, 10, 21, 21, 21, 21, 21, 21, 10, 10, 10, 10, 10}, {8, 9, 30, 30, 28, 28, 29, 29, 29, 29, 28, 28, 30, 30, 10, 8}, {8, 9, 10, 23, 23, 23, 25, 25, 25, 25, 25, 25, 10, 10}, {8, 9, 9, 10, 10, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 10}, {8, 9, 10, 21, 21, 21, 21, 21, 26, 26, 26, 9, 10, 10}, {8, 8, 9, 9, 10, 10, 10, 10, 10, 10, 27, 27, 29, 29, 27, 27, 27, 27, 30, 30, 14, 14, 14, 14, 14, 14, 14, 14, 27, 27, 27, 27, 27, 27, 27, 27, 27, 28, 28, 28, 28, 14, 14, 14, 14, 14, 14, 14, 14}, {8, 8, 9, 10, 17, 17, 17, 14, 15, 15, 27, 27, 28, 28}, {8, 8, 9, 10, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 25, 25, 25, 10, 10}, {8, 10, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 10}, {8, 8, 9, 10, 17, 17, 8, 8, 8, 9, 9, 10, 15, 15, 10, 17, 17}, {8, 8, 9, 10, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 22, 22, 31, 22, 22, 31, 22, 22, 31, 22, 22, 31, 10}, {8, 8, 9, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10}, {8, 8, 9, 10, 23, 23, 22, 22, 31, 31, 31, 31, 31, 31, 31, 31, 8}, {8, 8, 9, 10, 39, 39, 39, 39, 39, 39, 39, 39, 23, 23, 25, 25, 25, 10, 8}, {8, 8, 9, 10, 0, 0, 0, 0, 35, 35, 35, 49, 49, 0, 0, 0, 0, 9, 8}, {8, 9, 10, 35, 35, 35, 37, 37, 35, 35, 37, 37, 35, 35, 37, 37, 35, 35, 37, 37, 35, 35, 37, 37, 10, 8}, {8, 9, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 9}, {8, 9, 10, 10, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 26, 25, 25, 10, 8}, {8, 9, 10, 10, 14, 14, 14, 14, 13, 13, 13, 13, 13, 13, 13, 13, 13, 15, 15, 15, 10}, {8, 9, 10, 10, 10, 10, 10, 10, 10, 10, 10, 21, 21, 21, 35, 35, 35, 10, 9}, {8, 9, 10, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 10, 9}, {8, 9, 10, 39, 39, 39, 39, 40, 41, 41, 39}, new short[4], {8, 9, 10, 10, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25}, {8, 9, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 23, 23, 25, 25}, {8, 9, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10}, {11, 11, 11, 11, 9, 9, 9, 9, 8, 8, 8, 8, 9, 9, 9, 9, 10, 10, 10, 10, 10, 13, 13, 15, 15, 13, 13, 17, 17, 13, 13, 15, 15, 13, 13}, {27, 27, 27, 27, 27, 27, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 27, 27, 27, 27}, {35, 35, 37, 31, 33, 35, 35, 37, 31, 33, 35, 35, 37, 31, 33, 34, 34, 34}, {9, 10, 10, 10, 10, 10, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4}, {8, 9, 10, 10, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 17, 17, 21, 21, 21, 21, 21, 17, 17, 10}, {8, 9, 10, 10, 10, 10, 10, 10, 10, 10, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 10, 10, 10}, {8, 10, 10, 10, 10, 10, 14, 14, 21, 25, 25, 30, 29, 28, 25, 19, 19, 17, 19, 18, 10, 10, 10, 10}, {35, 35, 35, 35, 35, 35, 35, 35, 35, 37, 37, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 37, 37, 35, 35, 35, 35, 35, 35, 35, 35, 37, 37, 35, 35, 35, 35, 35, 35, 35}, {35, 35, 35, 35, 35, 35, 35, 35, 35, 37, 37, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35}, {1, 1, 1, 1, 1, 1, 8, 8, 8, 8, 8, 8, 8, 8, 9, 9, 9, 9, 9, 9, 9, 9, 10, 10, 10, 10, 10, 10, 10, 10, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {8, 8, 8, 9, 9, 9, 10, 10, 10, 13, 13, 17, 18, 17, 17}, {13, 13, 13, 13, 13, 13, 13, 14, 14, 14, 16, 16, 14, 14, 14, 14, 14, 18, 18, 18, 22, 22, 22, 22, 26, 26, 26, 23, 23, 23, 23, 23, 23, 25, 25, 25, 25}, {8, 8, 8, 9, 9, 9, 10, 10, 10, 13, 13, 17, 18, 17, 17}, {13, 13, 13, 13, 13, 13, 13, 14, 14, 14, 16, 16, 14, 14, 14, 14, 14, 18, 18, 18, 22, 22, 22, 22, 26, 26, 26, 23, 23, 23, 23, 23, 23, 25, 25, 25, 25}, {8, 8, 9, 10, 27, 27, 30, 30, 29, 29, 27, 27}};
   }

   public Plash() {
   }

   public Plash(MainSkill skill, MainObject obj, mVector vec) {
      this.skill = skill;
      this.vecObj = vec;
      this.objFire = obj;
      this.timeEndPlash = skill.AF;
      this.isNextf = 0;
      if (skill.AK == 0L) {
         this.timebeginSkill = GameCanvas.timeNow;
      } else {
         this.timebeginSkill = skill.AK;
      }

      if (vec != null && vec.size() > 0) {
         Object_Effect_Skill var4;
         if ((obj = MainObject.get_Object((int)(var4 = (Object_Effect_Skill)this.vecObj.elementAt(0)).ID, (byte)var4.tem)) == null) {
            return;
         }

         if (this.objFire != obj) {
            if (this.objFire.x < obj.x) {
               this.objFire.Dir = 2;
               this.objFire.type_left_right = 2;
            } else {
               this.objFire.Dir = 0;
               this.objFire.type_left_right = 0;
            }
         }
      }

      this.getTypePlash(skill.AA);
      this.plashdata = getPlashData(this.typePlash);
      this.f = -1;
      this.removef = this.plashdata.mDataPlash.length;
   }

   public static Plashdata getPlashData(short var0) {
      Plashdata var1;
      if ((var1 = (Plashdata)hashPlash.get("" + var0)) == null) {
         GlobalService.getInstance().AH((byte)97, (short)var0);
         var1 = new Plashdata();
      }

      return var1;
   }

   public Plash(MainSkill var1, MainObject var2, mVector var3, boolean var4) {
      this.skill = var1;
      this.vecObj = null;
      this.objFire = var2;
      this.timeEndPlash = var1.AF;
      this.isShow = true;
      this.isNextf = 0;
      if (var1.AK == 0L) {
         this.timebeginSkill = GameCanvas.timeNow;
      } else {
         this.timebeginSkill = var1.AK;
      }

      this.getTypePlash(var1.AA);
      this.plashdata = getPlashData(this.typePlash);
      this.f = -1;
      this.removef = this.plashdata.mDataPlash.length;
   }

   public final short getTypePlash(short var1) {
      switch(var1) {
      case 1:
      case 37:
      case 112:
      case 270:
         this.typePlash = 0;
         this.fAddEff = 9;
         break;
      case 2:
         this.typePlash = 111;
         this.fAddEff = 0;
         break;
      case 3:
      case 229:
      case 262:
      case 263:
      case 264:
         this.typePlash = 112;
         this.fAddEff = 0;
         break;
      case 4:
      case 230:
      case 246:
      case 253:
         this.typePlash = 113;
         this.fAddEff = 4;
         break;
      case 5:
      case 231:
         this.typePlash = 114;
         this.fAddEff = 4;
         break;
      case 6:
      case 232:
         this.typePlash = 115;
         this.fAddEff = 0;
         break;
      case 7:
         this.typePlash = 4;
         this.fAddEff = 0;
         break;
      case 9:
      case 53:
      case 163:
         this.typePlash = 6;
         this.fAddEff = 6;
         break;
      case 10:
      case 234:
         this.typePlash = 116;
         this.fAddEff = 0;
         break;
      case 11:
         this.typePlash = 8;
         this.fAddEff = 0;
         break;
      case 12:
      case 188:
      case 220:
      case 293:
      case 493:
         this.typePlash = 134;
         this.fAddEff = 6;
         break;
      case 14:
      case 44:
         this.typePlash = 12;
         this.fAddEff = 6;
         break;
      case 15:
         this.typePlash = 13;
         this.fAddEff = 2;
         break;
      case 16:
      case 51:
         this.typePlash = 14;
         this.fAddEff = 6;
         break;
      case 17:
         this.typePlash = 133;
         this.fAddEff = 1000;
         this.isNextf = 2;
         break;
      case 18:
         this.typePlash = 118;
         this.fAddEff = 4;
         break;
      case 19:
         this.typePlash = 103;
         this.fAddEff = 0;
         break;
      case 20:
         this.typePlash = 136;
         this.fAddEff = 0;
         break;
      case 21:
      case 33:
         this.typePlash = 19;
         this.fAddEff = 4;
         break;
      case 23:
         this.typePlash = 137;
         this.fAddEff = 5;
         break;
      case 24:
      case 80:
         this.typePlash = 43;
         this.fAddEff = 4;
         break;
      case 25:
      case 235:
         this.typePlash = 138;
         this.fAddEff = 5;
         break;
      case 26:
      case 236:
         this.typePlash = 139;
         this.fAddEff = 0;
         break;
      case 27:
         this.typePlash = 140;
         this.fAddEff = 8;
         break;
      case 28:
         this.typePlash = 141;
         this.fAddEff = 8;
         break;
      case 29:
         this.typePlash = 27;
         this.fAddEff = 10;
         break;
      case 30:
         this.typePlash = 36;
         this.fAddEff = 4;
         break;
      case 31:
      case 55:
         this.typePlash = 28;
         this.fAddEff = 4;
         break;
      case 32:
         this.typePlash = 142;
         this.fAddEff = 4;
         break;
      case 34:
         this.typePlash = 20;
         this.fAddEff = 6;
         break;
      case 35:
         this.typePlash = 29;
         this.fAddEff = 6;
         break;
      case 36:
         this.typePlash = 143;
         this.fAddEff = 3;
         break;
      case 38:
         this.typePlash = 30;
         this.fAddEff = 3;
         break;
      case 39:
         this.typePlash = 144;
         this.fAddEff = 0;
         break;
      case 40:
         this.typePlash = 145;
         this.fAddEff = 0;
         break;
      case 41:
         this.typePlash = 121;
         this.fAddEff = 4;
         break;
      case 42:
         this.typePlash = 101;
         this.fAddEff = 0;
         break;
      case 43:
         this.typePlash = 102;
         this.fAddEff = 0;
         break;
      case 45:
         this.typePlash = 146;
         this.fAddEff = 0;
         break;
      case 46:
         this.typePlash = 31;
         this.fAddEff = 0;
         break;
      case 47:
      case 48:
         this.typePlash = 2;
         this.fAddEff = 6;
         break;
      case 49:
      case 50:
      case 266:
      case 276:
      case 277:
      case 278:
      case 279:
         this.typePlash = 9;
         this.fAddEff = 6;
         break;
      case 52:
      case 189:
      case 221:
      case 311:
      case 511:
         this.typePlash = 125;
         this.fAddEff = 0;
         break;
      case 54:
         this.typePlash = 147;
         this.fAddEff = 5;
         break;
      case 56:
      case 191:
      case 223:
      case 313:
      case 513:
         this.typePlash = 135;
         this.fAddEff = 4;
         break;
      case 57:
      case 58:
         this.typePlash = 33;
         this.fAddEff = 6;
         break;
      case 59:
      case 60:
         this.typePlash = 148;
         this.fAddEff = 4;
         break;
      case 63:
      case 190:
      case 222:
      case 312:
         this.typePlash = 130;
         this.fAddEff = 0;
         break;
      case 64:
      case 66:
         this.typePlash = 5;
         this.fAddEff = 6;
         break;
      case 65:
      case 70:
      case 107:
         this.typePlash = 67;
         this.fAddEff = 6;
         break;
      case 67:
      case 68:
      case 69:
      case 194:
      case 226:
      case 303:
      case 503:
         this.typePlash = 34;
         this.fAddEff = 10;
         break;
      case 71:
         this.typePlash = 37;
         this.fAddEff = 4;
         break;
      case 72:
         this.typePlash = 38;
         this.fAddEff = 6;
         break;
      case 73:
         this.typePlash = 120;
         this.fAddEff = 6;
         break;
      case 74:
         this.typePlash = 119;
         this.fAddEff = 6;
         break;
      case 75:
      case 76:
         this.typePlash = 39;
         this.fAddEff = 6;
         break;
      case 77:
         this.typePlash = 40;
         this.fAddEff = 2;
         break;
      case 78:
         this.typePlash = 42;
         this.fAddEff = 4;
         break;
      case 79:
         this.typePlash = 41;
         this.fAddEff = 4;
         break;
      case 83:
         this.typePlash = 122;
         this.fAddEff = 0;
         break;
      case 84:
      case 181:
      case 213:
      case 272:
      case 472:
         this.typePlash = 127;
         this.fAddEff = 0;
         break;
      case 85:
      case 182:
         this.typePlash = 46;
         this.fAddEff = 0;
         break;
      case 86:
      case 183:
      case 215:
      case 281:
      case 481:
         this.typePlash = 123;
         this.fAddEff = 0;
         break;
      case 87:
      case 184:
      case 216:
         this.typePlash = 48;
         this.fAddEff = 0;
         break;
      case 88:
         this.typePlash = 49;
         this.fAddEff = 0;
         break;
      case 89:
         this.typePlash = 50;
         this.fAddEff = 0;
         break;
      case 90:
         this.typePlash = 51;
         this.fAddEff = 4;
         break;
      case 91:
         this.typePlash = 52;
         this.fAddEff = 4;
         break;
      case 92:
         this.typePlash = 53;
         this.fAddEff = 6;
         break;
      case 93:
         this.typePlash = 54;
         this.fAddEff = 0;
         break;
      case 94:
         this.typePlash = 55;
         this.fAddEff = 6;
         break;
      case 95:
         this.typePlash = 56;
         this.fAddEff = 6;
         break;
      case 96:
         this.typePlash = 57;
         this.fAddEff = 0;
         break;
      case 97:
         this.typePlash = 58;
         this.fAddEff = 5;
         break;
      case 98:
      case 102:
         this.typePlash = 59;
         this.fAddEff = 6;
         break;
      case 99:
         this.typePlash = 60;
         this.fAddEff = 6;
         break;
      case 100:
         this.typePlash = 61;
         this.fAddEff = 6;
         break;
      case 101:
         this.typePlash = 62;
         this.fAddEff = 6;
         break;
      case 103:
         this.typePlash = 63;
         this.fAddEff = 6;
         break;
      case 104:
         this.typePlash = 64;
         this.fAddEff = 6;
         break;
      case 105:
         this.typePlash = 65;
         this.fAddEff = 6;
         break;
      case 106:
         this.typePlash = 66;
         this.fAddEff = 0;
         break;
      case 108:
         this.typePlash = 68;
         this.fAddEff = 0;
         break;
      case 109:
      case 110:
         this.typePlash = 69;
         this.fAddEff = 4;
         break;
      case 111:
         this.typePlash = 70;
         this.fAddEff = 0;
         break;
      case 113:
         this.typePlash = 71;
         this.fAddEff = 0;
         break;
      case 114:
         this.typePlash = 72;
         this.fAddEff = 0;
         break;
      case 115:
         this.typePlash = 73;
         this.fAddEff = 0;
         break;
      case 116:
         this.typePlash = 74;
         this.fAddEff = 6;
         break;
      case 117:
         this.typePlash = 75;
         this.fAddEff = 4;
         break;
      case 118:
         this.typePlash = 76;
         this.fAddEff = 6;
         break;
      case 119:
         this.typePlash = 77;
         this.fAddEff = 4;
         break;
      case 120:
         this.typePlash = 78;
         this.fAddEff = 0;
         break;
      case 121:
         this.typePlash = 79;
         this.fAddEff = 4;
         break;
      case 122:
         this.typePlash = 80;
         this.fAddEff = 4;
         break;
      case 123:
      case 185:
      case 217:
      case 283:
      case 483:
         this.typePlash = 81;
         this.fAddEff = 0;
         break;
      case 124:
      case 186:
      case 218:
      case 291:
      case 491:
         this.typePlash = 124;
         this.fAddEff = 0;
         break;
      case 125:
      case 187:
         this.typePlash = 129;
         this.fAddEff = 0;
         break;
      case 126:
      case 192:
         this.typePlash = 126;
         this.fAddEff = 0;
         break;
      case 127:
      case 193:
      case 225:
      case 302:
      case 502:
         this.typePlash = 91;
         this.fAddEff = 6;
         break;
      case 128:
         this.typePlash = 93;
         this.fAddEff = 7;
         break;
      case 129:
         this.typePlash = 94;
         this.fAddEff = 7;
         break;
      case 130:
         this.typePlash = 95;
         this.fAddEff = 7;
         break;
      case 131:
         this.typePlash = 96;
         this.fAddEff = 6;
         break;
      case 132:
         this.typePlash = 97;
         this.fAddEff = 6;
         break;
      case 133:
         this.typePlash = 98;
         this.fAddEff = 0;
         break;
      case 134:
      case 135:
         this.typePlash = 99;
         this.fAddEff = 0;
         break;
      case 136:
         this.typePlash = 104;
         this.fAddEff = 0;
         break;
      case 137:
      case 138:
         this.typePlash = 105;
         this.fAddEff = 0;
         break;
      case 139:
         this.typePlash = 107;
         this.fAddEff = 0;
         break;
      case 140:
         this.typePlash = 108;
         this.fAddEff = 0;
         break;
      case 141:
         this.typePlash = 109;
         this.fAddEff = 0;
         break;
      case 142:
         this.typePlash = 110;
         this.fAddEff = 0;
         break;
      case 143:
         this.typePlash = 150;
         this.fAddEff = 0;
         break;
      case 155:
         this.typePlash = 3;
         this.fAddEff = 3;
         break;
      case 156:
         this.typePlash = 44;
         this.fAddEff = 4;
         break;
      case 157:
         this.typePlash = 47;
         this.fAddEff = 0;
         break;
      case 158:
         this.typePlash = 88;
         this.fAddEff = 0;
         break;
      case 159:
         this.typePlash = 90;
         this.fAddEff = 6;
         break;
      case 160:
         this.typePlash = 45;
         this.fAddEff = 4;
         break;
      case 161:
         this.typePlash = 128;
         this.fAddEff = 0;
         break;
      case 162:
         this.typePlash = 89;
         this.fAddEff = 0;
         break;
      case 164:
      case 227:
         this.typePlash = 35;
         this.fAddEff = 5;
         break;
      case 165:
         this.typePlash = 131;
         this.fAddEff = 0;
         break;
      case 166:
         this.typePlash = 132;
         this.fAddEff = 0;
         break;
      case 167:
         this.typePlash = 149;
         this.fAddEff = 6;
         break;
      case 168:
      case 258:
         this.typePlash = 154;
         this.fAddEff = 2;
         break;
      case 169:
      case 237:
         this.typePlash = 151;
         this.fAddEff = 2;
         break;
      case 170:
      case 238:
         this.typePlash = 152;
         this.fAddEff = 4;
         break;
      case 171:
      case 172:
      case 239:
      case 240:
         this.typePlash = 153;
         this.fAddEff = 6;
         break;
      case 173:
         this.typePlash = 155;
         this.fAddEff = 6;
         break;
      case 174:
         this.typePlash = 156;
         this.fAddEff = 2;
         break;
      case 175:
         this.typePlash = 157;
         this.fAddEff = 6;
         break;
      case 176:
         this.typePlash = 159;
         this.fAddEff = 4;
         break;
      case 177:
         this.typePlash = 158;
         this.fAddEff = 0;
         break;
      case 178:
         this.typePlash = 160;
         this.fAddEff = 4;
         break;
      case 179:
      case 241:
         this.typePlash = 161;
         this.fAddEff = 1;
         break;
      case 180:
      case 212:
         this.typePlash = 162;
         this.fAddEff = 0;
         break;
      case 195:
         this.typePlash = 163;
         this.fAddEff = 4;
         break;
      case 196:
         this.typePlash = 164;
         this.fAddEff = 0;
         break;
      case 197:
      case 267:
      case 274:
      case 275:
         this.typePlash = 165;
         this.fAddEff = 8;
         break;
      case 198:
         this.typePlash = 166;
         this.fAddEff = 4;
         break;
      case 199:
         this.typePlash = 167;
         this.fAddEff = 4;
         break;
      case 200:
         this.typePlash = 168;
         this.fAddEff = 4;
         break;
      case 201:
         this.typePlash = 169;
         this.fAddEff = 4;
         break;
      case 202:
         this.typePlash = 170;
         this.fAddEff = 4;
         break;
      case 203:
         this.typePlash = 171;
         this.fAddEff = 4;
         break;
      case 204:
         this.typePlash = 172;
         this.fAddEff = 4;
         break;
      case 205:
         this.typePlash = 173;
         this.fAddEff = 3;
         break;
      case 206:
      case 207:
         this.fAddEff = 6;
         this.typePlash = 174;
         break;
      case 208:
         this.typePlash = 175;
         this.fAddEff = 0;
         break;
      case 209:
      case 242:
         this.typePlash = 176;
         this.fAddEff = 6;
         break;
      case 210:
      case 243:
         this.typePlash = 177;
         this.fAddEff = 4;
         break;
      case 211:
      case 244:
         this.typePlash = 177;
         this.fAddEff = 4;
         break;
      case 214:
      case 273:
      case 473:
         this.typePlash = 179;
         this.fAddEff = 0;
         break;
      case 219:
      case 292:
      case 492:
         this.typePlash = 180;
         this.fAddEff = 0;
         break;
      case 224:
      case 301:
      case 501:
         this.typePlash = 181;
         this.fAddEff = 0;
         break;
      case 228:
      case 259:
      case 260:
      case 261:
         this.typePlash = 182;
         this.fAddEff = 0;
         break;
      case 245:
      case 249:
      case 251:
      case 252:
         this.typePlash = 185;
         this.fAddEff = 0;
         break;
      case 247:
      case 254:
         this.typePlash = 183;
         this.fAddEff = 4;
         break;
      case 248:
      case 255:
         this.typePlash = 184;
         this.fAddEff = 4;
         break;
      case 256:
         this.typePlash = 186;
         this.fAddEff = 0;
         break;
      case 257:
         this.typePlash = 187;
         this.fAddEff = 0;
         break;
      case 265:
         this.typePlash = 188;
         this.fAddEff = 0;
         break;
      case 271:
      case 471://Quả đấm tốc độ
         this.typePlash = 189;
         this.fAddEff = 0;
         break;
      case 280:
         this.typePlash = 191;
         break;
      case 282:
      case 482:
         this.typePlash = 190;
         this.fAddEff = 0;
         break;
      case 400://Giải phóng
      case 401://Xoáy đen
      case 402:
      case 403:
         this.typePlash = 151;
         this.fAddEff = 0;
         break;
      case 512:
         this.typePlash = 130;
         this.fAddEff = 0;
         break;
      case 2000:
         this.typePlash = 194;
         this.fAddEff = 0;
         break;
      case 10001:
      case 10005:
      case 10008:
      case 10010:
      case 10013:
      case 10017:
      case 10018:
      case 10020:
      case 10021:
      case 10022:
      case 10024:
      case 10026:
      case 10027:
         this.typePlash = 82;
         this.fAddEff = 0;
         break;
      case 10002:
         this.typePlash = 83;
         this.fAddEff = 0;
         break;
      case 10003:
         this.typePlash = 84;
         this.fAddEff = 0;
         break;
      case 10004:
      case 10007:
      case 10009:
         this.typePlash = 85;
         this.fAddEff = 0;
         break;
      case 10006:
      case 10011:
      case 10012:
      case 10015:
      case 10023:
         this.typePlash = 86;
         this.fAddEff = 0;
         break;
      case 10019:
         this.typePlash = 87;
         this.fAddEff = 0;
         break;
      case 10025:
         this.typePlash = 92;
         this.fAddEff = 0;
      }

      return this.typePlash;
   }

   public final int AA() {
      try {
         if (GameCanvas.gameTick % 25 == 0 && (GameCanvas.timeNow - this.timebeginSkill) / 1000L > 30L) {
            return -1;
         } else {
            if (this.isNextf == 0) {
               ++this.f;
            } else {
               if (this.isNextf == 1) {
                  return this.plashdata.mDataPlash[this.f];
               }

               if (this.isNextf == 2) {
                  ++this.f;
                  if (this.f >= this.plashdata.mDataPlash.length) {
                     this.f = 0;
                  }

                  if ((GameCanvas.timeNow - this.timebeginSkill) / 1000L > (long)this.timeEndPlash) {
                     return -1;
                  }
               }
            }

            if (this.f >= this.removef) {
               return this.skill.AM == 2 && GameCanvas.timeNow - this.timebeginSkill < (long)this.timeEndPlash ? this.plashdata.mDataPlash[this.removef - 1] : -1;
            } else {
               if (this.isShow) {
                  if (this.f != this.fAddEff) {
                     return this.plashdata.mDataPlash[this.f];
                  }

                  LoginScreen.AA(this.skill.AA, 0, this.objFire.x, this.objFire.y, (byte)this.objFire.Dir, this.objFire);
               } else {
                  if (this.f != this.fAddEff) {
                     return this.plashdata.mDataPlash[this.f];
                  }

                  if (this.skill.AM == 2) {
                     GameScreen.AB(this.skill, this.objFire);
                  } else {
                     byte var10000 = this.skill.AM;
                     boolean var10001 = true;
                     GameScreen.AA(this.skill, this.objFire, this.vecObj);
                  }
               }

               this.fAddEff = 10000;
               return this.plashdata.mDataPlash[this.f];
            }
         }
      } catch (Exception var1) {
         return 0;
      }
   }

   public final void AA(byte var1) {
      this.isNextf = var1;
   }

   public static void readDataPlash(DataInputStream dis) {
      try {
         short id = dis.readShort();
         byte b = dis.readByte();
         Plashdata plashData;
         (plashData = new Plashdata()).mDataPlash = new short[b];

         for(int i = 0; i < b; ++i) {
            plashData.mDataPlash[i] = dis.readByte();
         }

         hashPlash.put("" + id, plashData);
      } catch (Exception var5) {
      }
   }
}
