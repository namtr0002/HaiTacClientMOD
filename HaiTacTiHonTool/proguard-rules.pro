-dontwarn **
-ignorewarnings
-dontnote **

# Keep entry points
-keep class com.deplor.haitactihontool.ui.Bootstrap {
    public static void main(java.lang.String[]);
}
-keep class com.deplor.haitactihontool.ultimate_security.LoginUI {
    public static void main(java.lang.String[]);
}

# Keep dependencies that use reflection
-keep class com.formdev.flatlaf.** { *; }
-keep class org.json.** { *; }
-keep class com.mysql.** { *; }

# Keep Java core stuff
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Optimization and Obfuscation settings
-dontshrink
-dontoptimize
-overloadaggressively
-repackageclasses ''
-allowaccessmodification
-dontusemixedcaseclassnames

# Aggressive Dictionary Obfuscation (The IllI strategy)
-obfuscationdictionary dictionary.txt
-classobfuscationdictionary dictionary.txt
-packageobfuscationdictionary dictionary.txt
-repackageclasses ""
-overloadaggressively
-allowaccessmodification

# Remove debugging information (Local variables, line numbers)
-renamesourcefileattribute SourceFile
-keepattributes Exceptions,Signature,Deprecated,SourceFile,*Annotation*,EnclosingMethod
