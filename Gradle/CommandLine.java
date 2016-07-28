// 命令行编译项目

// 1)切换到项目的跟目录,执行一下指令查看当前项目的Gradle版本
gradlew -v
// 输出结果
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

// 2)接着执行
gradlew clean
// 输出结果
:clean
:app:clean

BUILD SUCCESSFUL

// 3)最后执行
gradlew build
// 出现一下字样,表示编译成功
BUILD SUCCESSFUL

Total time: 1 mins 26.737 secs
// 在项目的目录/app/build/outputs/apk中就看到
// app-debug-unaligned.apk, app-release-unsigned.apk
// 然后就可以安装APK查看运行的效果


// Gradle常用的指令

// gradlew代表了gradle wrapper,意思是gradle的一层包装
// 可以理解为本地就封装了gradle,在项目的目录/gradl/wrapper/gradle-wrapper.properties文件中升名字它指向的目录和版本
// 只要下载成功即可用gradle wrapper的命令代替全局的gradle命令

gradlew -v //               版本号
gradlew clean //            清除项目目录下的build文件夹
gradlew build //            检查并编译打包
gradlew installRelease //   Release模式打包并安装
gradlew uninstallRelease // 卸载Release模式包

// 因为build命令会把debug,release环境的包都打出来
// 如果只需要Release的包,可以使用**assemble**命令
gradlew assembleDebug // 编译并打Debug包
gradlew assembleRelease

// 还可以和productFlavors结合使用
// 在AndroidManifest.xml中配置PlaceHolder
<meta-data
    android:name="UMENG_CHANNEL"
    android:value="${UMENG_CHANNEL_VALUE}"/>

// 在build.gradle设置productFlavors 
android {
    productFlavors {
        wandoujia {
            manifestPlaceholders = [UMENG_CHANNEL_VALUE: "wandoujia"]
        }
        xiami {
            manifestPlaceholders = [UMENG_CHANNEL_VALUE: "xiami"]
        }
    }
}

// 设置完成后就可以执行命令,打包出wandoujia的Release包
gradlew assembleWandoujiaRelease
