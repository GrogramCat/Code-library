## TextView

>学习于伯乐在线 PleaseCallMeCoder-TextView实战之你真的懂我么?http://android.jobbole.com/83961/

## TextView 设置基础

```java
// 字符串里变量的替换,使用本地化数据交换格式
// 在资源文件中
<string name="welcome">你好%1$s,欢迎光临!</string>
// 之后在代码中
String welcome = getString(R.string.welcome, "Temoa");

// 如果有多个变量,可以这样写
<string name="welcome">你好%1$s,第%2$d次使用本APP!</string>

String welcome = getString(R.String.welcome, "Temoa", 32);
```

```java
// TextView中设置多种字体的大小,使用spannable相关的类和接口
String text = "您已经连续走了5963步";
int start = text.indexOf('5');
int end = text.length();
Spannable textSpan = new SpannableStringBuilder(text);
textSpan.setSpan(new AbsoluteSizeSpan(16), 0, start, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
textSpan.setSpan(new AbsoluteSizeSpan(26), start, end-1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
textSpan.setSpan(new AbsoluteSizeSpan(16), end-1, end, Spannable,SPAN_INCLUSIVE_INCLUSIVE);
TextView textView = (TextView)findViewById(R.id.text);
textView.setText(textSpan);
// 以上效果为:数字体大小为26,其他都为16
```

## TextView 中设置超链接

布局文件中设置android:autoLink属性,默认为none,该属性的其他几个常量如下:
* web
* email
* phone 
* map
* all

代码中设置为,参数分别对应为 setAutoLinkMask(int);
* Linkify.WEB_URLS
* Linkify.EMAIL_ADDRESSES
* Linkify.PHONE_NUMBERS
* Linkify.MAP_ADDRESSES
* Linkify.ALL

## 自定义超链接

```java
textView.setText(getClickableSpan());
textView.setMovementMethod(LinkMovementMethod.getInstance());// 文本超链接起作用
private SpannableString getClickableSpan() {// 设置超链接的文字
    SpannableString spanStr = new SpannableString("使用该软件,即表示您同意该软件的使用条款和隐私政策");
    spanStr.setSpan(new UnderlineSpan(), 16, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 设置下划线文字
    spanStr.setSpan(new ClickableSpan() {// 设置文字单击事件
        @Override
        public void OnClick(View widget) {
            startActivity(new Intent(MainActivity.this, UsageActivity.class));
        }
    }, 16, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    spanStr.setSpan(// 设置文字的前景色
        new ForegroundColorSpan(Color.GREEN), 16, 20, spanned,SPAN_EXCLUSIVE_EXCLUSIVE);
    
    spanStr.setSpan(new UnderlineSpan(), 21, 25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    spanStr.setSpan(new ClickableSpan() {
        @Override
        public void OnCLick(View widget) {
            startActivity(new Intent(MainActivity.this, PrivacyActivity.class));
        }
    }, 21, 25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    spanStr.setSpan(
        new ForegroundColorSpan(Color.GREEN), 21, 25, spanned,SPAN_EXCLUSIVE_EXCLUSIVE);
    return spanStr;
}
// 以上效果为:"使用条款"和"隐私政策"的字体颜色为绿色,有下划线,分别点击可以跳转页面
```

## TextView 插入图片

在xml中,分别对应在左,上,右,下插入图片:
* android:drawableLeft
* android:drawableTop
* android:drawableRight
* android:drawableBottom

还可意思设置与文字的间距 
* android:drawablePadding

在代码中,left,top 等需要传入资源的id,不需要的话为0 

```java
setCompoundDrawablesWithIntrinsicBounds(int left, int top, int right, int bottom);
```

## 字体的阴影

在xml中
```xml
android:shadowColor="#ffffff"  指定阴影的颜色
android:shadowDx="15.0"        设置阴影横坐标开始的位置
android:shadowDy="5.0"         设置阴影纵坐标开始的位置
android:shadowRadius="2.5"     设置阴影的半径,这只为0.1会变成字体的颜色,设置为3.0效果较好
```

在代码中
```java
public void setShadowLayer(float raduis, float dx, float dy, int color) {
    // done
}
```

## 字体加粗与斜体

* normal
* bold   粗体
* italic 倾斜

```xml
android:textSytle="bold"
```

```java
textView.getPaint().setFakeBoldText(true); // 设置文字为粗体
```

## 文字过长省略号与跑马灯效果

* 1 省略号

```xml
android:maxEms="6"         限制显示的字符长度
android:singleLine="true"  单行显示
android:ellipsize="end"    在结尾用省略号,还有以下值:start, end, middle,marquee
```

* 2 跑马灯效果 <p>
在设置android:ellipsize="marquee"下,可以设置android:marqueeRepeatLimt滚动次数,设置marquee_forever为无限次,需要控件获得焦点

```xml
android:marqueeRepeatLimt="marquee_forever" 滚动次数,这里为无限次
android:ellipsize="marquee"                 设置为滚动弄湿
android:singleLine="true"                   单行显示
android:focusableInTouchMode="true"         
android:focusable="true"                    获取焦点
```

## 行间距

```xml
android:lineSpacingExtra="3dp"
android:lineSpacingMultiplier="1.2"  行间距的倍数
```

## 关于字体

xml中设置typeface 有以下的值
* normal
* sans
* serif     衬线字体
* monospace 等宽字体

```xml
android:typeface="monospace"  设置为等宽字体
```

在代码中设置字体
```java
Typeface mTypeface = Typeface.createFromAsset(getAssets(), "kaiti.ttf");
textView.setTypeface(mTypeface);
// 注意不要使用大量的自定义字体,会消耗更多的性能
```

## TextVIew 中设置HTML

支持的HTML标签
```html
<a herf="...">                            链接内容
<b>                                       粗体
<big>                                     大字体文字 
<blockquote>                              引用块标签
<br>                                      换行
<cite>                                    表示引用的URI
<dfn>                                     定义标签
<div align="...">
<em>                                      强调
<font size="..." color="..." face="...">
<h1> <h2> <h3> <h4> <h5> <h6>
<i>                                       斜体
<img src="...">
<p>                                       段可以标签,可以加入文字,列表,表格
<small>                                   小字体文字
<strike>                                  删除线样式文字
<strong>                                  重点强调
<sub>                                     下标标签
<sup>                                     上标标签
<tt>                                      等宽字体文字,对中文没意义
<u>                                       带有下划线文字
```

```java
// 显示多种颜色的字,通过Html.fromHtml(str)方法转换html格式的字符串
TextView textViwe = (TextView)findViewById(R.id.text);
String textStr1 = "<font color=\"#123569\">如果有一天,</font>";
String textStr2 = "<font color=\"#00ff00\">我悄然离去</font>";
textViwe.setText(Html.fromHtml(textStr1 + textStr2));
// 以上效果为:"如果有一天,"颜色为蓝色,"我悄然离去"的颜色为绿色

// 字体加粗,也可以通过html格式字符串实现
String textStr1 = "<b>hello</b>";
textViwe.setText(Html.fromHtml(textStr1));

// 插入图片,也可以用html格式字符串实现,还需要用到imageGetter类来对图片的src属性进行转换
String imgStr = "<b>hello</b><img src=\"" + R.mipmap.ic_lanucher + "\"/>";
Html.ImageGetter imageGetter = new Html.imageGetter() {
    @Override
    public Drawable getDrawable(String source) {
        int id = Integer.parsetInt(source);
        Drawable draw = getResource().getDrawable(id);
        draw.setBounds(0, 0, 300, 200);
        return draw;
    }
};
textView.append(Html.fromHtml(imgStr,imageGetter, null));
```
