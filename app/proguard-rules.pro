# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/muddvayne/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontwarn org.apache.**
-dontwarn com.sun.mail.**
-dontwarn org.slf4j.**
-dontwarn java.**
-dontoptimize
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-optimizations !field/removal/writeonly,!field/marking/private,!class/merging/*,!code/allocation/variable

-allowaccessmodification
-keepattributes *Annotation*
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
-dontwarn
-ignorewarnings

-keep class * implements android.os.Parcelable {
public static final android.os.Parcelable$Creator *;
}

## -- for serialization -- ##
-keepclassmembers class * implements java.io.Serializable {
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
## -- serialization end -- ##

-keepclassmembers class * implements com.SmartTech.teasyNew.Unobfuscable {
    <fields>;
}

-keep class com.SmartTech.teasyNew.view.PinView$AnimatedView {
    *;
}

-keep class com.SmartTech.teasyNew.NetworkOperator {
    *;
}

-keep class com.SmartTech.teasyNew.activity.make_payment.ActivityMakePaymentRegular$* {
    *;
}

-keep class com.SmartTech.teasyNew.activity.make_payment.kirs.ActivityMakePaymentKIRS$* {
    *;
}

# libraries


-keep class com.google.** {*;}

