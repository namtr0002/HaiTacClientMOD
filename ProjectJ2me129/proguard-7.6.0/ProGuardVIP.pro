-injars 'dist\148_Ban_Dep.jar'
-outjars 'NSO_148.jar'

-libraryjars 'proguard-7.6.0\lib\retrace.jar'
-libraryjars 'proguard-7.6.0\j2me.jar'

-dontskipnonpubliclibraryclassmembers
-allowaccessmodification
-obfuscationdictionary 'proguard-7.6.0\OBF.txt'
-classobfuscationdictionary 'proguard-7.6.0\OBF.txt'
-packageobfuscationdictionary 'proguard-7.6.0\OBF.txt'
-repackageclasses ''
-microedition
-verbose
-dontnote
-dontwarn



# Keep - Applications. Keep all application classes, along with their 'main' methods.
-keepclasseswithmembers public class * {
    public static void main(java.lang.String[]);
}

# Keep - Midlets. Keep all extensions of javax.microedition.midlet.MIDlet.
-keep public class * extends javax.microedition.midlet.MIDlet

# Keep - Native method names. Keep all native class/method names.
-keepclasseswithmembers,includedescriptorclasses,allowshrinking class * {
    native <methods>;
}
