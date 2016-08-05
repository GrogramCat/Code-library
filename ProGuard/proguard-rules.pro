# 代码混淆压缩比例 在0~7之间,默认为5,一般需要修改
-optimizationpasses 5

# 混淆时不使用大小写混合,混淆后的类名为小写
-dontusemixedcaseclassnames

# 指定不去忽略非公共的库的类的成员
-dontskipnonpubliclibraryclassmembers

# 不做预校捡,preverify是proguard的4个步骤之一
# Android不需要preverify,去掉一部可以加快混淆速度
-dontpreverify

# 有了verbose这句话,混淆后就会生成映射文件
# 包含有类名 -> 混淆后类名的映射关系
# 然后使用printmapping指定映射文件的名称
-verbose
-printmapping proguardMapping.txt

# 指定混淆时采用的算法,后面的参数是一个过滤器
# 这个过滤器是谷歌推荐的算法,一般不改变
-optimizations !code/simplification/arithmetic, !field/*, !class/merging/*

# 保护代码中的Anntation不被混淆
# 这在JSON实体映射时非常重要,比如fastjson
-keeppattributes *Anntation*

# 避免混淆泛型
-keeppattributes Signature

# 抛出异常时保留代码的行号
-keeppattributes SourceFile,LineNumberTable

# -----------需要保留的东西----------
# 保留所有本地native方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

# 保留了继承自Activity,Application这类的子类
# 因为这些子类都有可能被外部调用
# 不如说,第一行就保证了所有的Activity的子类不要被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends andorid.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentPrivider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.preference
-keep public class * extends android.view.view
-keep public class com.android.vending.licensing.ILicensingService

# 如果有引用android-support-v4.jar包,可以添加下面这行
-keep public class com.tuniu.app.ui.fragment.** {*;}

# 保留在Activity中的方法参数是view的方法
# 从而我们在Layout中编写onClick就不会被影响
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}

# 枚举类不能被混淆
-keepclassmembers enum * {
    public static ** [] values();
    public static ** valueOf(java.lang,String);
}

# 保留自定义控件(继承自View)不被混淆
-leep public class * extends android.view.View {
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 保留Parcelable序列化的类不被混淆
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# 保留Serizlizable序列化的类不被混淆
-keepclassmembers class * implememts java.io.Serializeable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# 对于R下所有类及其方法,都不能被混淆
-keep class **.R$* {
    *;
}

# 对于带有回调函数onXXEvent的,不能被混淆
-keepclassmembers class * {
    void *{**On*Event);
}

# -----------针对App定制---------
# 保留实体类和成员不被混淆
-keep public class com.temoa.entity.** {
    public void set*(***);
    public *** get*();
    public *** is*();
}

# 保留内嵌类,列入MainActivity中所有内嵌类
-keep class com.temoa.MainActivity$* { *; }

# 对WebView的处理
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String)
}

-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, java.lang.String)
}

# 对JavaScript的处理
# 例如有以下App与JavaScript交互的代码
class JSInterface {
    @JavascriptInterface
    public void callAndroidMethod(int a, float b, String c, boolean d) {
    if(d) {
        String strMessage = "-" + (a+1) + "-" + (b+1) + "-" + c + "-" + d;
        new AlertDialog.Builder(MainActivity.this).setTitle("title")
                .setMessage(strMessage).show();
    }
}

-keepclassmembers class com.temoa.MainActivity$JSInterface {
    <methods>;
}

# -----------针对第三方jar包的解决方案---------
# 针对android-support-v4.jar的解决方案
-libraryjars libs/android-support-v4.jar
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep public class * exntends android.support.v4.**
-keep public class * entends android.app.fragment

# 其他第三方jar包,例如支付宝
-libraryjars libs/alipayskd.jar
-dontwarn com.alipay.android.app.**
-keep class com.alipay.** { *; }
# 不是所有SDk都需要-dontwarn智能,取决于第三方SDK是否会出现警告,需要时再加上
