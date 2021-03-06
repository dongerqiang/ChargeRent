# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\adt-bundle-windows-x86_64-20140702\adt-bundle-windows-x86_64-20140702\sdk/tools/proguard/proguard-android.txt
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
# 指定代码的压缩级别
-optimizationpasses 5
# 是否使用大小写混合
-dontusemixedcaseclassnames
#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
 #优化  不优化输入的类文件
-dontoptimize
# 混淆时是否做预校验
-dontpreverify
# 混淆时是否记录日志
-verbose
#保护注解
-keepattributes *Annotation*,InnerClasses
# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable
# 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
# 保持哪些类不被混淆 四大组件
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService

# 保留support下的所有类及其内部类
-keep class android.support.** {*;}
# 保留继承的
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.annotation.**

##如果有引用v4包可以添加下面这行
#-keep public class * extends android.support.v4.app.Fragment

# 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
-keepclassmembers class * {
    void *(**On*Event);
    void *(**On*Listener);
}

#忽略警告
-ignorewarning

##记录生成的日志数据,gradle build时在本项目根目录输出##
#apk 包内所有 class 的内部结构
-dump proguard/class_files.txt
#未混淆的类和成员
-printseeds proguard/seeds.txt
#列出从 apk 中删除的代码
-printusage proguard/unused.txt
#混淆前后的映射
-printmapping proguard/mapping.txt
########记录生成的日志数据，gradle build时 在本项目根目录输出-end######
#如果引用了v4或者v7包
-dontwarn android.support.**

#保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

#保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

#保持 Serializable 不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#保持枚举 enum 类不被混淆
-keepclassmembers enum * {
  public static **[] values();
  public static ** valueOf(java.lang.String);
}

-keepclassmembers class * {
    public void *ButtonClicked(android.view.View);
}

#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}

# ========== webview ========= #
##保留annotation， 例如 @JavascriptInterface 等 annotation
#-keepattributes *Annotation*
#
##保留跟 javascript相关的属性
#-keepattributes JavascriptInterface
#
##保留JavascriptInterface中的方法
#-keepclassmembers class * {
#    @android.webkit.JavascriptInterface <methods>;
#}
# webView处理，项目中没有使用到webView忽略即可
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
    public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
# ========== webview ========= #

#避免混淆泛型 如果混淆报错建议关掉
#-keepattributes Signature

#移除Log类打印各个等级日志的代码，打正式包的时候可以做为禁log使用，这里可以作为禁止log打印的功能使用，另外的一种实现方案是通过BuildConfig.DEBUG的变量来控制
#-assumenosideeffects class android.util.Log {
#    public static *** v(...);
#    public static *** i(...);
#    public static *** d(...);
#    public static *** w(...);
#    public static *** e(...);
#}


##如果用用到Gson解析包的，直接添加下面这几行就能成功混淆，不然会报错。
#-keepattributes Signature
## Gson specific classes
#-keep class sun.misc.Unsafe { *; }
## Application classes that will be serialized/deserialized over Gson
#-keep class com.google.gson.** { *; }
#-keep class com.google.gson.stream.** { *; }
#
## 如果使用了Gson之类的工具要使被它解析的JavaBean类即实体类不被混淆。
#-keep class com.qdigo.chargerent.data.** { *; }


# ========== ping ++ ========== #
-dontwarn com.alipay.**
-keep class com.alipay.** {*;}

-dontwarn  com.ta.utdid2.**
-keep class com.ta.utdid2.** {*;}

-dontwarn  com.ut.device.**
-keep class com.ut.device.** {*;}

-dontwarn  com.tencent.**
-keep class com.tencent.** {*;}

-dontwarn  com.unionpay.**
-keep class com.unionpay.** {*;}

-dontwarn com.pingplusplus.**
-keep class com.pingplusplus.** {*;}

#-dontwarn com.baidu.**
#-keep class com.baidu.** {*;}

#-keepclassmembers class * {
#    @android.webkit.JavascriptInterface <methods>;
#}
# ========== ping ++ ========== #

# ========= library BaseFrame ========= #
-keep class com.yolanda.nohttp.**{*;}

#========== gson ==========#
-keepattributes Signature
-keep class com.google.gson.** {*;}
#-keep class com.google.**{*;}
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
#-keep class com.google.** {
#    <fields>;
#    <methods>;
#}

-keep class com.qdigo.chargerent.entity.data.** { *; }
-keep class com.qdigo.chargerent.entity.net.**{*;}
-keep class com.qdigo.chargerent.entity.net.responseBean.**{*;}
-keep class com.qdigo.chargerent.view.**{*;}
#========== gson ==========#


# bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

# lib
# ============ androidannotations-api-3.3.jar ============ #
-keep class org.androidannotations.**{*;}
# ============ androidannotations-api-3.3.jar ============ #

# ============ commons-io-2.0.jar ============ #
-keep class org.apache.commons.io.**{*;}
# ============ commons-io-2.0.jar ============ #

# ============ zxing ============ #
-keep class com.google.zxing.**{*;}
-keep class com.qdigo.chargerent.code_scan.camera.**{*;}
-keep class com.qdigo.chargerent.code_scan.decoding.**{*;}
-keep class com.qdigo.chargerent.code_scan.view.**{*;}
# ============ zxing ============ #

# ============ httpcore-4.4.4.jar, httpmime-4.5.2.jar============ #
-keep class org.apache.http.**{*;}

-keep class org.apache.http.entity.mime.**{*;}
# ============ httpcore-4.4.4.jar, httpmime-4.5.2.jar============ #

# ============ 极光推送============ #
-dontwarn cn.jpush.**
-keep class cn.jpush.** {*;}
# ============ 极光推送============ #

# ============ mob============ #
-keep class com.mob.commons.**{*;}

-keep class com.mob.tools.**{*;}

-keep class cn.sharesdk.framework.**{*;}
-keep class com.mob.commons.**{*;}

-keep class cn.sharesdk.tencent.**{*;}

-keep class cn.sharesdk.wechat.**{*;}
# ============ mob============ #

# ============ universal-image-loader-1.9.2-with-sources.jar============ #
-keep class com.nostra13.universalimageloader.**{*;}
# ============ universal-image-loader-1.9.2-with-sources.jar============ #

# wheelpicker proguard
-keepattributes InnerClasses,Signature
-keepattributes *Annotation*

-keep class cn.qqtheme.framework.entity.** { *;}