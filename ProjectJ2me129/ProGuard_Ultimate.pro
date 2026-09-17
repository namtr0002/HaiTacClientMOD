-injars      build/raw.jar
-outjars     dist/HaiTacTiHon129.jar

-libraryjars lib/jsr120_1.1.jar
-libraryjars lib/jsr135_1.2.jar
-libraryjars lib/cldc_1.1.jar
-libraryjars lib/midp_2.1.jar

-dontwarn **
-dontnote **
-verbose
-dontwarn java.nio.file.**
-ignorewarnings
-dontpreverify
-target 1.6

# Không shrink/optimize để đảm bảo an toàn tuyệt đối 100% cho J2ME KVM
-dontshrink
-dontoptimize

# Cho phép overload thành phần cùng tên khác kiểu: int a; boolean a; void a(); int a();
-overloadaggressively

# Tối ưu hóa tên và tương thích hệ điều hành/giả lập J2ME
-dontusemixedcaseclassnames
-repackageclasses ""
-renamesourcefileattribute ""
-keepattributes Exceptions

# Giữ nguyên duy nhất MIDlet entry point để J2ME AMS khởi chạy game
-keep public class * extends javax.microedition.midlet.MIDlet {
    public <init>();
    public void startApp();
    public void pauseApp();
    public void destroyApp(boolean);
}

-keep public class GameMidlet {
    public *;
    public static *;
}

# Áp dụng từ điển mapping a, b, c, d... tăng dần cho toàn bộ class, method và field
-obfuscationdictionary      OBF_ULTIMATE.txt
-classobfuscationdictionary OBF_ULTIMATE.txt
-packageobfuscationdictionary OBF_ULTIMATE.txt

-printmapping build/mapping.txt



