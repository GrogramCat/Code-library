# Android 动画

## 属性动画

### 相关API

几个属性

* Duration 动画时长
* Time interpolation 时间差值
* Repeat count, behavior 重读次数和重复模式(模式:重复时从头开始, 还是反向)
* Animator sets 动画集合
* Frame refresh delay　帧刷新延迟(默认10ms)

几个相关的类

* ObjectAnimator 动画的执行类
* ValueAnimator 动画的执行类
* AnimatorSet 用于控制一组动画的执行
* AnimatorInflater 用户加载属性动画的xml文件
* TypeEvaluator  类型估, 主要用于设置动画操作属性的值
* TimeInterpolator 时间插值

### ObjectAnimator

* translationX, translationY 平移
* rotation, roationX, rotationY 2D, 3D旋转
* scaleX, scaleY 伸缩
* alpha 透明度

* pivotX, pivotY view 对象的支点, 默认情况下是view 的中心点

栗子:

```java
ObjectAnimator animator = ObjectAnimator
    .ofFloat(view, "roationX", 0.0f, 359.0f)
    .setDuration(300)
    .start();
```

提供了ofInt、ofFloat、ofObject, 这几个方法都是设置动画作用的元素, 作用的属性, 动画开始, 结束, 以及中间的任意个属性值.

对于属性值, 如果只设置一个, 会从对象当面的状态开始.

动画过程中会不断调用setPropName()方法来更新元素的属性, 所有使用的属性都必须带有getter 和setter 方法

如果一个属性没有get, set方法, 我们可以通过以下两种方法解决

方法一: 自定义一个属性类

```java
private static class WrapperView {
    private View mTarget;

    public WrapperView(View target) {
        mTarget = target;
    }

    public int getWidth() {
        return mTarget.getLayoutParams().width;
    }

    public void setWidth() {
        mTarget.getLayoutParams.width = width;
        mTarget.requestLayout();
    }
}

// ...
WrapperView wrapperView = new WrapperView(mBtn);
ObjectAnimator.ofFloat(wrapperView, "width", 500).setDuration(500).start();
```

方法二: 使用ValueAnimation

```java
// 设置属性随便填入任何没有的属性, 只需要它按照时间插值和持续时间计算的那个值，我们自己手动调用
ObjectAnimator anim = ObjectAnimator
    .ofFloat(view, "zhy", 1.0F,  0.0F).setDuration(500);
anim.start();  
anim.addUpdateListener(new AnimatorUpdateListener() {
    @Override  
    public void onAnimationUpdate(ValueAnimator animation) {
        float cVal = (Float) animation.getAnimatedValue();
        view.setAlpha(cVal);
        view.setScaleX(cVal);
        view.setScaleY(cVal);
    }
});
```

### PropertyValuesHolder

类似于动画集合, 针对一个对象要同时作用多种动画

```java
PropertyValuesHolder pvh1 = PropertyValuesHolder.ofFloat("alpha", 1f,  0f, 1f);  
PropertyValuesHolder pvh2 = PropertyValuesHolder.ofFloat("scaleX", 1f,  0, 1f);  
PropertyValuesHolder pvh3 = PropertyValuesHolder.ofFloat("scaleY", 1f,  0, 1f);  
ObjectAnimator.ofPropertyValuesHolder(view, pvh1, pvh2, pvh3)
    .setDuration(1000).start();
```

### ValueAnimator

ValueAnimator 本身不提供任何的属性效果, 更像一个数值发生器, 在AnimatorUpdateListener 中监听数值的变化, 在完成动画

栗子:

```java
ValueAnimator animator = ValueAnimator.ofFloat(0, 100);
animator.setTarget(view);
animator.setDuration(1000);
animator.addUpdateListener(new AnimatorUpdateListener() {

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        Float value = (Float)animation.getAnimatedValue();
        // todo animation use the value
        view.setTranslationY(value);
    }
});
```

### 动画时间监听

```java
ObjectAnimator anim = ObjectAnimator.ofFloat(view, "alpha", 0.5f);
anim.addListener(new AnimatorListener() {

    @Override
    public void onAnimationStart(Animator animation) {
        Log.e(TAG, "onAnimationStart");
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
        Log.e(TAG, "onAnimationRepeat");
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        Log.e(TAG, "onAnimationEnd");
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        Log.e(TAG, "onAnimationCancel");
    }
});
anim.start();
```

一般我们也只是关心动画是否完成,所有有以下(这么长的代码, 很不开心的)

```java
anim.addListener(new AnimatorListenerAdpater() {

    @Override
    public void onAnimationEnd(Animator animation) {
        Log.e(TAG, "onAnimationEnd");
    }
});
```

animator还有cancel()和end()方法: cancel动画立即停止, 停在当前的位置, end动画直接到最终状态

### AnimatorSet

上面已经使用PropertyValuesHolder 实现了作用多个动画, 而AnimationSet 能够更加精确的控制动画的顺序.

栗子:

```java
ObjectAnimator anim1 = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 2f);
ObjectAnimator anim2 = ObjectAnimator.ofFloat(view, "scaleY",  1.0f, 2f);
AnimatorSet animSet = new AnimatorSet();
animSet.setDuration(2000);
animSet.setInterpolator(new LinearInterpolator());
//两个动画同时执行
animSet.playTogether(anim1, anim2);
animSet.start()

float x = view.getX();  
ObjectAnimator anim1 = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 2f);
ObjectAnimator anim2 = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 2f);
ObjectAnimator anim3 = ObjectAnimator.ofFloat(view, "x", x, 0f);
ObjectAnimator anim4 = ObjectAnimator.ofFloat(view, "x", x);
// anim1，anim2,anim3同时执行 
// anim4接着执行 
AnimatorSet animSet = new AnimatorSet();
animSet.play(anim1).with(anim2);
animSet.play(anim2).with(anim3);
animSet.play(anim4).after(anim3);
animSet.setDuration(1000);
animSet.start();
```

AnimatorSet 通过playTogether(), playSequentially()(依次执行), play().with(), before()和after() 方法来控制多个动画.

### 在XML中使用属性动画

栗子: 

首先在res下建立animator文件夹, 然后建立res/animator/scalex.xml

```xml
<objectAnimator xmlns:android="http://schemas.android.com/apk/res/android"
    android:duration="1000"
    android:propertyName="scaleX"
    android:valueFrom="1.0"
    android:valueTo="2.0"
    android:valueType="floatType">
</objectAnimator>
```

然后在java代码中

```java
Animator anim = AnimatorInflater.loadAnimator(this, R.animator.scalex)
anim.setTarget(view);
anim.start();
```

如果是同时执行多个动画, 使用set 的ordering 标签, 设置为together, 还有sequentially (依次执行).

```xml
<set xmlns:android="http://schemas.android.com/apk/res/android"
    android:ordering="together" >
  
    <objectAnimator  
        android:duration="1000"
        android:propertyName="scaleX"
        android:valueFrom="1"  
        android:valueTo="0.5" >
    </objectAnimator>

    <objectAnimator
        android:duration="1000"
        android:propertyName="scaleY"
        android:valueFrom="1"
        android:valueTo="0.5" >
    </objectAnimator>
</set>  
```

```java
Animator anim = AnimatorInflater.loadAnimator(this, R.animator.scale);
// 设置缩放的中心
mMv.setPivotX(0.5f); 
mMv.setPivotY(0.5f);
// 显示的调用invalidate
mMv.invalidate();
anim.setTarget(mMv);
anim.start();
```

### View的animate 方法

在SDK11的时候, 给View添加了animate方法, 更加方便的实现动画效果

```java
view.animate()
    .alpha(0)
    .y(300)
    .setDuration(300)
    .withStartAction(new Runnable() {
        @Override
        public void run() {

        }
    })
    .withEndAction(new Runnable() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                // todo
            });
        }
    })
    .start();
```

### 布局动画

指作用在ViewGroup 上, 给子View 发生变化时的动画效果

最简单可以通过在ViewGroup 的xml 中添加以下属性

```xml
android:animateLayoutChanges="true"
```

这是Android默认的显示过渡效果, 无法使用自定义的动画来替换这个效果.

#### 使用LayoutAnimationController 类来自定义一个子View 的过渡效果.

栗子:

```java
LinearLayout ll = (LinearLayout)findViewById(R.id.ll);
ScaleAnimation sa = new SacleAnimation(0, 1, 0, 1);
sa.setDuration(2000);
layoutAnimationController lac = new layoutAnimationController(sa, 0.5f);
lac.setOrder(layoutAnimationController.ORDER_NORMAL);
ll.setLayoutAnimation(lac)
```

以上代码, 给LinearLayout 增加视图效果, 让子View 出现时带有缩放的效果.第一个参数是作用的动画, 第二个参数每个View 显示的delay 时间, 当delay 不为0时, 可以设置子View 显示的顺序

* layoutAnimationController.ORDER_NORMAL 顺序
* layoutAnimationController.ORDER_RANDOM 随机
* layoutAnimationController.ORDER_REVERSE 反序

#### 使用LayoutTranstion

```java
LayoutTransition transition = new LayoutTransition();
transition.setAnimator(LayoutTransition.CHANGE_APPEARING, transition.getAnimator(LayoutTransition.CHANGE_APPEARING));
transition.setAnimator(LayoutTransition.APPEARING,null);
transition.setAnimator(LayoutTransition.DISAPPEARING,null);
transition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING,null);
mGridLayout.setLayoutTransition(transition);
```

过渡的类型一共有四种：
* LayoutTransition.APPEARING 当一个View在ViewGroup中出现时，对此View设置的动画

* LayoutTransition.CHANGE_APPEARING 当一个View在ViewGroup中出现时，对此View对其他View位置造成影响，对其他View设置的动画

* LayoutTransition.DISAPPEARING  当一个View在ViewGroup中消失时，对此View设置的动画

* LayoutTransition.CHANGE_DISAPPEARING 当一个View在ViewGroup中消失时，对此View对其他View位置造成影响，对其他View设置的动画

* LayoutTransition.CHANGE 不是由于View出现或消失造成对其他View位置造成影响，然后对其他View设置的动画





