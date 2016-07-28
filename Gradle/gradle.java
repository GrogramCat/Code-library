/*
    一个全新的Android项目，一般包含三个相关的gradle配置文件，分别是根目录下的build.gradle、setting.gradle和app目录下的build.gradle文件。
    这里讲app下的build.gradle文件
 */

/*
    apply plugin声明
 */
// 代表该项目是Android项目
apply plugin: 'com.android.application'

// 代表该项目是库或者module
apply plugin: 'com.android.library'

/*
    dependencies节点
 */
dependencies {
    // 编译libs目录下的所有jar包
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile 'com.android.support:support-v4:21.0.2'
    compile 'com.etsy.android.grid:library:1.0.5'
    // 编译extras目录下的ShimmerAndroid模块
    compile project(':extras:ShimmerAndroid')
}

/*
    android节点
 */
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
