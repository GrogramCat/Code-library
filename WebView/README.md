# WebView
>学习于[WebView·开车指南](https://jiandanxinli.github.io/2016-08-31.html)

## 基本用法
```java
// 记得在AndroidManifset 声明权限
String url = "https://www.google.com";
WebView webView = (WebView) findViewById(R.id.web_view);
webView.loadUrl(url);
```

常用方法
* getUrl()
* reload()
* canGoBack()
* canGoFroward()
* canBackOrForward(int steps)
* goBack()
* goForward()
* goBackOrForward(int steps)
* clearCache(boolean includeDiskFiles) 由于缓存是全局的，所以只要是WebView 用到的缓存都会被清空
* clearHistory()
* clearFormData() 仅仅清除当前表单域自动完成填充的表单数据
* onPause() 需要注意的是该方法并不会暂停JavaScript 的执行
* onResume()
* pauseTimers() 该方法面向全局整个应用程序的webview，它会暂停所有webview 的layout，parsing，JavaScript Timer。当程序进入后台时，该方法的调用可以降低CPU功耗
* resumeTimer() 恢复pauseTimers 时的所有操作
* destroy() 需要注意的是：这个方法的调用应在WebView 从父容器中被remove 掉之后。我们可以手动地调用

```java
rootLayout.removeView(webView);
webView.destroy();
```

* getScrollY() 获取当前内容滑动过的距离
* getHeight() 返回当前webView 这个容器的高度
* getContentHeight() 该方法返回整个HTML页面的高度，但该高度值并不等同于当前整个页面的高度，因为WebView 有缩放功能， 所以当前整个页面的高度实际上应该是原始HTML 的高度再乘上缩放比例。因此，准确的判断方法应该是

```java
if (webView.getContentHeight() * webView.getScale() == (webView.getHeight() + webView.getScrollY())) {
    //已经处于底端
}

if(webView.getScrollY() == 0){
    //处于顶端
}
```

* pageUP(bollean top) 滑动到顶部
* pageDown(bollean bottom)

## WebSetting
WebSetting 是用来管理WebView 配置类的. 当WebView第一次创建时, 会包含一个默认的配置集合.

WebSetting 对象可以通过WebView.getSettings()获得, 与WebView 的生命周期相同.

常用方法

* setJavaScripEnabled(boolean flag)

* setJavaScriptCanOpenWindowsAutomatically(boolean flag)：设置WebView 是否可以由JavaScript 自动打开窗口，默认为false，通常与JavaScript 的window.open()配合使用。

* setAllowFileAccess(boolean allow) 启用或禁用WebView 访问文件数据

* setBlocakNetworkImage(boolean flag) 禁止或允许WebView 从网络上加载图片。需要注意的是，如果设置是从禁止到允许的转变的话，图片数据并不会在设置改变后立刻去获取，而是在WebView 调用reload()的时候才会生效。
这个时候，需要确保这个app 拥有访问Internet 的权限，否则会抛出安全异常。
通常没有禁止图片加载的需求的时候，完全不用管这个方法，因为当我们的app 拥有访问Internet 的权限时，这个flag的默认值就是false

* setSupportZoom(bollean support)

* setBuiltInZoomControls(boolean enabled) 是否显示缩放按钮(wap 网页不支持)

* setSupportMultipleWindows(boolean support) 设置WebView 是否支持多窗口

* setLayoutAlgorithm(WebSettings.LayoutAlgorithm l)：指定WebView 的页面布局显示形式，调用该方法会引起页面重绘。默认值为LayoutAlgorithm#NARROW_COLUMNS

* setNeedInitialFocus(boolean flag)：通知WebView 是否需要设置一个节点获取焦点当WebView#requestFocus(int,android.graphics.Rect)被调用时，默认为true

* setAppCacheEnabled(boolean flag)

* setAppCachePath(String appCachePath) 重复调用无效

* setCacheMode(int mode)：用来设置WebView 的缓存模式。当我们加载页面或从上一个页面返回的时候，会按照设置的缓存模式去检查并使用（或不使用）缓存

>四种缓存模式:
* LOAD_DEFAULT：默认的缓存使用模式。在进行页面前进或后退的操作时，如果缓存可用并未过期就优先加载缓存，否则从网络上加载数据。这样可以减少页面的网络请求次数。
* LOAD_CACHE_ELSE_NETWORK：只要缓存可用就加载缓存，哪怕它们已经过期失效。如果缓存不可用就从网络上加载数据。
* LOAD_NO_CACHE：不加载缓存，只从网络加载数据。
* LOAD_CACHE_ONLY：不从网络加载数据，只从缓存加载数据。

>*(通常我们可以根据网络情况将这几种模式结合使用，比如有网的时候使用LOAD_DEFAULT，离线时使用LOAD_CACHE_ONLY、LOAD_CACHE_ELSE_NETWORK，让用户不至于在离线时啥都看不到)*

* setDatabaseEnabled(boolean flag) 
* setDomStorageEnabled(boolean flag) 启用或禁用DOM缓存
* setUserAgentString(String ua)
* setDefaultEncodingName(String encoding) 通常为"UTF-8"
* setStandardFontFamily(String font) 设置标准的字体族,默认"sans-serif"
* setCursiveFontFamily：设置草书字体族,默认"cursive"
* setFixedFontFamily：设置混合字体族,默认"monospace"
* setSansSerifFontFamily：设置梵文字体族,默认"sans-serif"
* setSerifFontFamily：设置衬线字体族,默认"sans-serif"
* setDefaultFixedFontSize(int size)：设置默认填充字体大小,默认16,取值区间为[1-72]
* setDefaultFontSize(int size)：设置默认字体大小，默认16，取值区间[1-72]
* setMinimumFontSize：设置最小字体，默认8. 取值区间[1-72]
* setMinimumLogicalFontSize：设置最小逻辑字体，默认8. 取值区间[1-72]

## WebViewClient

WebViewClient 是帮助WebView 处理各种通知和请求事件的

常用方法

* onLoadResource(WebView view, String url)：该方法在加载页面资源时会回调，每一个资源（比如图片）的加载都会调用一次

* onPageStarted(WebView view, String url, Bitmap favicon)：该方法在WebView 开始加载页面且仅在Main frame loading（即整页加载）时回调，一次Main frame 的加载只会回调该方法一次。我们可以在这个方法里设定开启一个加载的动画，告诉用户程序在等待网络的响应

* onPageFinished(WebView view, String url)：该方法只在WebView 完成一个页面加载时调用一次（同样也只在Main frame loading时调用）

* onReceivedError(WebView view, WebResourceRequest request, WebResourceError error)：该方法在web 页面加载错误时回调，这些错误通常都是由于无法与服务器正常连接引起的，最常见的就是网络问题。 这个方法有两个地方需要注意:

>1.这个方法只在与服务器无法正常连接时调用

>2.这个方法是新版本的onReceivedError()方法，从API23开始引进，与旧方法onReceivedError(WebView view,int errorCode,String description,String failingUrl)不同的是，新方法在页面局部加载发生错误时也会被调用

* onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) 需要捕捉HTTP ERROR, 任何资源的加载引发HTTP ERROR都会引起该方法的回调

* onReceivedSslError(WebView view, SslErrorHandler handler, SslError error)：当WebView加载某个资源引发SSL错误时会回调该方法，这时WebView要么执行handler.cancel()取消加载，要么执行handler.proceed()方法继续加载（默认为cancel）。需要注意的是，这个决定可能会被保留并在将来再次遇到SSL错误时执行同样的操作

* WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request)：当WebView需要请求某个数据时，这个方法可以拦截该请求来告知app并且允许app本身返回一个数据来替代我们原本要加载的数据

* boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) 

* onScaleChanged(WebView view, float oldScale, float newScale)

* boolean shouldOverrideKeyEvent(WebView view, KeyEvent event)：默认值为false，重写此方法并return true可以让我们在WebView内处理按键事件

## WebChromeClient 

WebChromeClient 是辅助WebView 处理Javascript 的对话框,网站图标，网站title，加载进度等

* onProgressChanged(WebView view, int newProgress)：当页面加载的进度发生改变时回调，用来告知主程序当前页面的加载进度

* onReceivedIcon(WebView view, Bitmap icon)：用来接收web页面的icon，我们可以在这里将该页面的icon设置到Toolbar

* onReceivedTitle(WebView view, String title)：用来接收web页面的title

* onShowCustomView(View view, WebChromeClient.CustomViewCallback callback)：该方法在当前页面进入全屏模式时回调，主程序必须提供一个包含当前web内容（视频等）的自定义的View

* onHideCustomView()

* Bitmap getDefaultVideoPoster()：当我们的Web页面包含视频时，我们可以在HTML里为它设置一个预览图，WebView会在绘制页面时根据它的宽高为它布局。而当我们处于弱网状态下时，我们没有比较快的获取该图片，那WebView绘制页面时的gitWidth()方法就会报出空指针异常~ 于是app就crash了.这时我们就需要重写下面的方法，在我们尚未获取web页面上的video预览图时，给予它一个本地的图片，避免空指针的发生

* View getVideoLoadingProgressView()：重写该方法可以在视频loading时给予一个自定义的View，可以是加载圆环等

* boolean onJsAlert(WebView view, String url, String message, JsResult result)：处理Javascript中的Alert对话框

* boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result)：处理Javascript中的Prompt对话框

* boolean onJsConfirm(WebView view, String url, String message, JsResult result)：处理Javascript中的Confirm对话框

* boolean onShowFileChooser(WebView webView, ValueCallback filePathCallback, WebChromeClient.FileChooserParams fileChooserParams)：该方法在用户进行了web上某个需要上传文件的操作时回调。我们应该在这里打开一个文件选择器，如果要取消这个请求我们可以调用filePathCallback.onReceiveValue(null)并返回true

* onPermissionRequest(PermissionRequest request)：该方法在web页面请求某个尚未被允许或拒绝的权限时回调，主程序在此时调用grant(String [])或deny()方法。如果该方法没有被重写，则默认拒绝web页面请求的权限

* onPermissionRequestCanceled(PermissionRequest request)：该方法在web权限申请权限被取消时回调，这时应该隐藏任何与之相关的UI界面

## Js 与WebView 交互

在WebView中调用Js的基本格式
```java
webView.loadUrl("javascript:methodName(parameterValues)");
```

现有以下JavaScript 代码
```JavaScript
function readyToGo() {
      alert("Hello")
}

function alertMessage(message) {
    alert(message)
}

function getYourCar(){
    return "Car";
}
```

* WebView 调用JavaScript 无参无返回值函数

```java
String call = "javascript:readyToGo()";
webView.loadUrl(call);
```

* WebView 调用JavScript 有参无返回值函数

```java
String call = "javascript:alertMessage(\"" + "content" + "\")";
webView.loadUrl(call);
```

* WebView 调用JavaScript 有参数有返回值的函数

```java
@TargetApi(Build.VERSION_CODES.KITKAT)
private void evaluateJavaScript(WebView webView){
    webView.evaluateJavascript("getYourCar()", new ValueCallback<String>() {
        @Override
        public void onReceiveValue(String s) {
            Log.d("findCar",s);
        }
    });
}
```

### JavaScript 通过WebView 调用Java 代码

>从API19开始，Android提供了@JavascriptInterface 对象注解的方式来建立起Javascript 对象和Android 原生对象的绑定，提供给JavScript 调用的函数必须带有@JavascriptInterface

* JavaScript 调用Android Toast 方法

原生Android java 方法, 并使用@JavascriptInterface 注解
```java
@JavascriptInterface
public void show(String s){
    Toast.makeText(getApplication(), s, Toast.LENGTH_SHORT).show();
}
```

注册JavaScriptInterface
```java
webView.addJavascriptInterface(this, "android");
```

编写JavaScript 代码
```JavaScript
function toastClick(){
    window.android.show("JavaScript called~!");
}
```

### JavaScript 调用有返回值的Java 方法

```java
@JavaInterface
public String getMessage(){
    return "Hello,boy~";
}
```

```java
webView.addJavaScriptInterface(this,"Android");
```

```JavaScript
function showHello(){
    var str=window.Android.getMessage();
    console.log(str);
}
```

## WebView 加载优化

### 资源本地化 --- "存","取","更"
[caching-web-resources-in-the-android-device](http://tutorials.jenkov.com/android/android-web-apps-using-android-webview.html#caching-web-resources-in-the-android-device)

### 缓存
```java
WebSettings settings = webView.getSettings();
settings.setAppCacheEnabled(true);
settings.setDatabaseEnabled(true);
settings.setDomStorageEnabled(true);//开启DOM缓存
settings.setCacheMode(WebSettings.LOAD_DEFAULT);
```
WebView在加载页面时检测网络变化，倘若在加载页面时用户的网络突然断掉，我们应当更改WebView的缓存策略
```java
ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
if(networkInfo.isAvailable()) {
    settings.setCacheMode(WebSettings.LOAD_DEFAULT);//网络正常时使用默认缓存策略
} else {
    settings.setCacheMode(WebSettings.LOAD_CACHE_ONLY);//网络不可用时只使用缓存
}
```

### 延迟加载和执行js
在WebView中，onPageFinished()的回调意味着页面加载的完成。但该方法会在JavScript脚本执行完成后才会触发

*当然这部分是Web前端的工作*

**JsBridge一律不得滥用**
