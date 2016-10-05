# Gradle

## 命令行编译项目
1. 切换到项目的跟目录,执行以下指令查看当面项目的Gradle版本 
> gradlew -v

输出结果:
```
------------------------------------------------------------
Gradle 2.10
------------------------------------------------------------

Build time:   2015-12-21 21:15:04 UTC
Build number: none
Revision:     276bdcded730f53aa8c11b479986aafa58e124a6

Groovy:       2.4.4
Ant:          Apache Ant(TM) version 1.9.3 compiled on December 23 2013
JVM:          1.8.0_45 (Oracle Corporation 25.45-b02)
OS:           Windows 8.1 6.3 amd64
```

2. 接着清理项目
> gradlew clean

输出结果
```
:clean
:app:clean

BUILD SUCCESSFUL
```

3. 最后重新Build 项目
> gradlew build

出现一下字样 , 则表示编译成功
```
BUILD SUCCESSFUL

Total time: 1 mins 26.737 secs
```

## gradle 常用的指令
首先解释一下:gradlew 代表了gradle wrapper ,意思是gradle的一层包装.可以理解为本地封装了gradle ,在项目的目录 /gradle/wrapper/gradle-wrapper.properties 文件中声明指向目录和版本.只要下载成功即可使用gradle wrapper 的名字代替全局的gradle 命令.

* gradlew -v                版本号
* gradlew clean             清除项目目录下的build文件夹
* gradlew build             检查并编译打包
* gradlew installRelease    Release模式打包并安装
* gradlew uninstallRelease  卸载Release模式包

因为build命令会把debug ,release 环境的包都打出来,如果只需要Release 包,可以使用**assemble** 命令.

* gradlew assembleDebug 编译并打Debug包
* gradlew assembleRelease

和productFlavors 结合使用,需要在AndroidManifest.xml 中配置PlaceHodler

```xml
<meta-data
    android:name="UMENG_CHANNEL"
    android:value="${UMENG_CHANNEL_VALUE}"/>
```

在build.gradle 设置productFlavors
```java
android {
    productFlavors {
        wandoujia {
            manifestPlaceholder = [UMENG_CHANNEL_VALUE: "wandoujia"]
        }
        xiaomi {
            manifestPlaceholder = [UMENG_CHANNEL_VALUE: "xiaomi"]
        }
    }
}
```

设置完成后就可以执行命令,例如打包出wandoujia 的Release 包
> gradlew assemableWandoujiaRelease

## Gradle 的一些知识

一个全新的Android 项目,一般包含三个相关的gradle 配置文件,分别是根目录下的build.gradle,setting.gradle 和app 目录下的build.gradle 文件.这里讲app 下的build.gradle 文件

### apply plugin 声明

```java
// 代表该项目是Android 项目
apply plugin: 'com.android.application'
// 代表该项目是库或者module
apply plugin: 'com.android.library'
```

### dependenceies 节点

```java
dependencies {
    // 编译libs目录下的所有jar包
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile 'com.android.support:support-v4:21.0.2'
    compile 'com.etsy.android.grid:library:1.0.5'
    // 编译extras目录下的ShimmerAndroid模块
    compile project(':extras:ShimmerAndroid')
}
```

### android 节点
```java
android {
    // 编译SDK版本
    compileSdkVersion 23
    // build tools 版本
    buildToolsVersion "23.0.3"
    
    defaultConfig {
        // 应用包名
        applicationId "com.temoa.Sunshine"
        // 最小支持的SDk版本
        minSdkVersion 14
        // 目标SDK版本
        targetSdkVersion 23
        // 版本号
        versionCode 1
        // 版本名字
        versionName "1.0"
    }

    signingConfig {
        debug {
            // No config
        }
        release {
            // 签名的一些信息
            storeFile file("../demo.keystore")
            stroePassword "demo"
            keyAlias "demo"
            keyPassword "demo"
        }
    }

// buildTypes意为编译的类型，下面声明了debug和release两种类型
// 两种的签名所用的配置不一样,在signingConfig节点指定
// 执行gradlew assembleDehug或者gradlew assembleRelease
    buildTypes {
        debug {
            // 版本名加上后缀 如 1.0-debug
            versionNameSuffix "-debug"
            minifyEnabled false
            zipAlignEnabled false
            shrinkResources false
            signingConfig signingConfig.debug
        }

        release {
            // 是否开启混淆
            minifyEnabled true
            // 是否zip优化
            zipAlignEnabled true
            // 删除一些无用的资源
            shrinkResources true
            // 签名所用的配置文件
            signingConfig signingConfig.release
            // 混淆所用的文件
            proguardFile getDufaultProguardFile('proguardFile-android.txt'), 'proguard-reles.pro'

        }
    }
}
```