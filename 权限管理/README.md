## Normal permissions
>只需要在xml中申请
* android.permission.ACCESS_LOCATION_EXTRA_COMMANDS 
* android.permission.ACCESS_NETWORK_STATE 
* android.permission.ACCESS_NOTIFICATION_POLICY 
* android.permission.ACCESS_WIFI_STATE 
* android.permission.ACCESS_WIMAX_STATE 
* android.permission.BLUETOOTH 
* android.permission.BLUETOOTH_ADMIN 
* android.permission.BROADCAST_STICKY 
* android.permission.CHANGE_NETWORK_STATE 
* android.permission.CHANGE_WIFI_MULTICAST_STATE 
* android.permission.CHANGE_WIFI_STATE 
* android.permission.CHANGE_WIMAX_STATE 
* android.permission.DISABLE_KEYGUARD 
* android.permission.EXPAND_STATUS_BAR 
* android.permission.FLASHLIGHT 
* android.permission.GET_ACCOUNTS 
* android.permission.GET_PACKAGE_SIZE 
* android.permission.INTERNET 
* android.permission.KILL_BACKGROUND_PROCESSES 
* android.permission.MODIFY_AUDIO_SETTINGS 
* android.permission.NFC 
* android.permission.READ_SYNC_SETTINGS 
* android.permission.READ_SYNC_STATS 
* android.permission.RECEIVE_BOOT_COMPLETED 
* android.permission.REORDER_TASKS 
* android.permission.REQUEST_INSTALL_PACKAGES 
* android.permission.SET_TIME_ZONE 
* android.permission.SET_WALLPAPER 
* android.permission.SET_WALLPAPER_HINTS 
* android.permission.SUBSCRIBED_FEEDS_READ 
* android.permission.TRANSMIT_IR 
* android.permission.USE_FINGERPRINT 
* android.permission.VIBRATE 
* android.permission.WAKE_LOCK 
* android.permission.WRITE_SYNC_SETTINGS 
* com.android.alarm.permission.SET_ALARM 
* com.android.launcher.permission.INSTALL_SHORTCUT 
* com.android.launcher.permission.UNINSTALL_SHORTCUT

## Dangerous permissions
>危险权限,需要在运行时请求.
>危险权限是按组来分的,所以,当你申请了多个同组的危险权限时,运行时只需要申请一个就行

```xml
<!-- 联系人  --> 
<uses-permission android:name="android.permission.WRITE_CONTACTS" /> 
<uses-permission android:name="android.permission.GET_ACCOUNTS" /> 
<uses-permission android:name="android.permission.READ_CONTACTS" /> 
<!-- 录音 --> 
<uses-permission android:name="android.permission.RECORD_AUDIO" /> 
<!-- 电话 --> 
<uses-permission android:name="android.permission.READ_CALL_LOG" /> 
<uses-permission android:name="android.permission.READ_PHONE_STATE" /> 
<uses-permission android:name="android.permission.CALL_PHONE" /> 
<uses-permission android:name="android.permission.WRITE_CALL_LOG" /> 
<uses-permission android:name="android.permission.USE_SIP" /> 
<uses-permission android:name="android.permission.PROCESS_OUTGOING_CALLS" /> 
<uses-permission android:name="com.android.voicemail.permission.ADD_VOICEMAIL" /> 
<!-- 日历 --> 
<uses-permission android:name="android.permission.READ_CALENDAR" /> 
<uses-permission android:name="android.permission.WRITE_CALENDAR" /> 
<!-- 相机 --> 
<uses-permission android:name="android.permission.CAMERA" /> 
<!-- 传感器 --> 
<uses-permission android:name="android.permission.BODY_SENSORS" /> 
<!-- 定位 --> 
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" /> 
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" /> 
<!-- 存储 --> 
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" /> 
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" /> 
<!-- 短信 --> 
<uses-permission android:name="android.permission.READ_SMS" /> 
<uses-permission android:name="android.permission.RECEIVE_WAP_PUSH" /> 
<uses-permission android:name="android.permission.RECEIVE_MMS" /> 
<uses-permission android:name="android.permission.RECEIVE_SMS" /> 
<uses-permission android:name="android.permission.SEND_SMS" /> 
```
对应的java代码
```java
// 联系人 
Manifest.permission.WRITE_CONTACTS, 
Manifest.permission.GET_ACCOUNTS, 
Manifest.permission.READ_CONTACTS, 
// 电话 
Manifest.permission.READ_CALL_LOG, 
Manifest.permission.READ_PHONE_STATE, 
Manifest.permission.CALL_PHONE, 
Manifest.permission.WRITE_CALL_LOG, 
Manifest.permission.USE_SIP, 
Manifest.permission.PROCESS_OUTGOING_CALLS, 
Manifest.permission.ADD_VOICEMAIL, 
// 日历 
Manifest.permission.READ_CALENDAR, 
Manifest.permission.WRITE_CALENDAR, 
// 相机 
Manifest.permission.CAMERA, 
// 传感器 
Manifest.permission.BODY_SENSORS, 
// 定位 
Manifest.permission.ACCESS_FINE_LOCATION, 
Manifest.permission.ACCESS_COARSE_LOCATION, 
// 存储 
Manifest.permission.READ_EXTERNAL_STORAGE, 
Manifest.permission.WRITE_EXTERNAL_STORAGE, 
// 录音 
Manifest.permission.RECORD_AUDIO, 
// 短信 
Manifest.permission.READ_SMS, 
Manifest.permission.RECEIVE_WAP_PUSH, 
Manifest.permission.RECEIVE_MMS, 
Manifest.permission.RECEIVE_SMS, 
Manifest.permission.SEND_SMS, 
```

## 运行时权限请求步骤
1. 在Manifest中注册
2. 在运行时请求权限

```java
// 权限检查帮助类
// 检查权限是否已经请求
public void checkPermissions(String... permissions) { 
    // 版本兼容 
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M 
            // 判断缺失哪些必要权限 
            && lacksPermissions(permissions)) { 
        // 如果缺失,则申请 
        requestPermissions(permissions); 
    } 
} 

// 判断是否缺失权限集合中的权限 
private boolean lacksPermissions(String... permissions) { 
    for (String permission : permissions) { 
        if (lacksPermission(permission)) { 
            return true; 
        } 
    } 
    return false; 
} 
    
// 判断是否缺少某个权限 
private boolean lacksPermission(String permission) { 
    return ContextCompat.checkSelfPermission(context, permission) == 
            PackageManager.PERMISSION_DENIED; 
} 
    
// 请求权限 
private void requestPermissions(String... permissions) { 
    ActivityCompat.requestPermissions(context, permissions, PERMISSION_REQUEST_CODE); 
} 
    
// 启动应用的设置,进入手动配置权限页面 
private void startAppSettings() { 
    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS); 
    Uri uri = Uri.fromParts("package", context.getPackageName(), null); 
    intent.setData(uri); 
    context.startActivity(intent); 
} 
```
>注意: 其中的 requestPermissions 方法,它会弹出权限提示框( 没有点击不再提醒的话 ),然后调用 public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) 方法,该方法在Activity或者Fragment中回调

```java
@Override 
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) { 
    // 版本兼容 
    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M || 
            requestCode != PermissionsChecker.PERMISSION_REQUEST_CODE) 
        return; 
 
    for (int i = 0, len = permissions.length; i < len; i++) { 
        String permission = permissions[i]; 
        //  缺失的权限 
        if (grantResults[i] == PackageManager.PERMISSION_DENIED) { 
            boolean showRationale = shouldShowRequestPermissionRationale(permission); 
            if (!showRationale) { 
                // 用户点击不再提醒 
                // TODO 
                break; 
            } else {  
                // 用户点击了取消... 
                // possibly check more permissions...    
            } 
        } 
    } 
} 
```
*学习于JimmieYang | 2016.09.20*