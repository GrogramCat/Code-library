## Toolbar

xml
```xml
<android.support.v7.widget.toolbar
    android:id="@+id/toolbar"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="@color/colorPrimary"
    app:theme="@style/Theme.AppCompat"
    />
```

在代码中
```java
@Override
protected void onCreate(Bundle saveInstanceState){
    super.onCreate(saveInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar mToolbar = (Toolbar)findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);// Activity 继承AppCompatActivity 时使用setSupportActionBar

    // 设置导航图标
    mToolbar.setNavigationIcon(...);
    // 设置标题
    getSupportActionBar.setTitle(...);
}
```

Activity 的Theme 设为NoActionBar;

或者使用Theme.AppCompat 并重写以下
```xml
<item name="windowActionBar">false</item>
<item name="android:windowNoTitle">true></item>
```

Toolbar 支持设置单独的Theme,*设置一个Light主题,title字体颜色就为浅色了*
```xml
 <android.support.v7.widget.Toolbar
    android:id="@+id/toolbar"
    ...
    app:theme="@style/Theme.AppCompat"/>  
```

## Toolbar 的特技

```java
// 设置Toolbar的背景透明度
mActionbarDrawable = new ColorDrawable(getResources().getColor(R.color.red));
getSupportActionBar.setBackgroundDrawable(mActionbarDrawable);
protected void setToolbarAlpha(float alpha) {
    mActionbarDrawable.setAlpha((int)(alpha*255));
}
```

```java
// 动画显示和隐藏Toolbar
public void showToolbar(boolean show) {
    if (show == toolbarShow || toolbar == null) 
        return;
    toolbarShow = show;
    if (show) {
        toolbar.animate()
               .translationY(0)
               .alpha(1)
               .setDuration(300)
               .setInterpolator(new DecelerateInterPolator());
    } else {
         toolbar.animate()
               .translationY(-toolbar.getBottom())
               .alpha(0)
               .setDuration(300)
               .setInterpolator(new DecelerateInterPolator());
    }
}

// 配合RecylerView使用
public abstract class HidingScrollListener extends RecyclerView.OnScrollListener {
    @Override
    public void OnScrolled(RecyclerView recyclerview, int dx, int dy) {
        super.onScroiied(recyclerview, dx, dy);
        if (dy>1) {
            onHide();
        } else if (dy<-1) {
            onShow();
        }   
    }

    public abstract void onHide();
    public abstract void onShow();
}

RecyclerView.setOnScrollListener(new HidingScrollListener() {
    @Override
    public void onHide() {
        showToolbar(false);
    }
    public void onShow() {
        showToolbar(true);
    }
});
```