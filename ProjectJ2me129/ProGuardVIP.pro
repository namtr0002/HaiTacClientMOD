-injars      dist/HaiTacTiHon129.jar
-outjars     HaiTacTiHon129.jar

-libraryjars lib/jsr120_1.1.jar
-libraryjars lib/cldc_1.0.jar
-libraryjars lib/midp_2.0.jar

-dontwarn **
-dontnote **
-verbose
-dontwarn java.nio.file.**
-ignorewarnings
-dontpreverify
-target 1.6

# Không shrink/optimize để tránh loại nhầm class runtime (an toàn cho MIDlet)
-dontshrink
-dontoptimize

# Vẫn obfuscate tên method/fields để rối, nhưng giữ class critical
-overloadaggressively
-useuniqueclassmembernames

# Giữ nguyên tất cả class extends MIDlet (entry points)
-keep class * extends javax.microedition.midlet.MIDlet {
    public <init>();
    public void startApp();
    public void pauseApp();
    public void destroyApp(boolean);
}

# Giữ nguyên main class và các class trực tiếp tham chiếu bởi MIDlet
-keep class GameMidlet { *; }

-obfuscationdictionary      OBF.txt
-classobfuscationdictionary OBF.txt
-packageobfuscationdictionary OBF.txt
