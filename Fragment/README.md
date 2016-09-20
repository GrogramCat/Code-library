# Fragment 防坑指南
>学习于 : YoKey

### 0x00 getActivity() 空指针
解决:在Fragment 的基类中设置一个Activity 全局变量(有可能一起内存的泄漏)
```java
protected Activity mActivity;
@Override
public onAttach(Context context) {
    super.onAttach(context);
    this.mActivity = (Activity)context;
}
// 最好的办法还是不要在onDetach后再调用Activity对象
```
***
### 0x01 Fragment 重叠异常
当发生"内存重启",FragmentManager 会将保存的Fragment 按照栈底到栈顶的
顺序恢复,并且全部为show() 形式.

解决一: findFragmentByTag()
```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    // ... 省略一些方法
    TargetFragment targetFragment;
    HideFragment hideFragment;

    if(savedInstanceState != null) {
        targetFragment = getSupportFragmentManager().findFragmentByTag(targetFragment.getClass().getName());
        hideFragment = getSupportFragmentManager().findFragmentByTag(hideFragment.getClass().getName());

        getFragmentManager.beginTransaction()
                .add(R.id.container, targetFragment, targetFragment.getClass().getName())
                .add(R.id.container, hideFragment, hideFragment.getClass().getName())
                .hide(hideFragment)
                .commit();
    }
}
// 如果还需要恢复到用户离开时的界面,需要在onSaveInstanceState() 里保存离开时的那个界面的tag 或下标,在恢复的时候取出.
```
解决二: 使用getSupportFragmentManager().getFragments() 恢复
```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    // ... 省略一些方法
    TargetFragment targetFragment;
    HideFragment hideFragment;

    if(savedInstanceState != null) {
        List fragmentList = getSupportFragmentManager.getFragments();
        for(Fragment fragment : fragmentList) {
            if(fragment instanceof TargetFragment) {
                targetFragment = (TargetFragment)fragment;
            } else if (fragment instanceof hideFragment) {
                hideFragment = (hideFragment)fragment;
            }
        }

        getFragmentManager.beginTransaction()
                .show(targetFragment)
                .hide(hideFragment)
                .commit();
    } else {
        targetFragment = TargetFragment.newInstance();
        hideFragment = HideFragment.newInstance();

        getFragmentManager.beginTransaction()
                add(R.id.container, targetFragment)
                add(R.id.container, hideFragment)
                hide(hideFragment)
                .commit();
    }
}
```
***
### 0x02 不靠谱的出栈方法remove()
popBackStack() 系列方法才能真正出栈
***
### 0x03 多个Fragment 同时出栈的那些深坑BUG
在Fragment库如下4个方法是有BUG:
* popBackStack(String tag, String flags)
* popBackStack(int id, int flags)
* popBackStackImmediate(String tag, int flags)
* popBackStackImmediate(int id, int flags)

解决:
```java
pubic class FragmentTransactionBugFix {
    public static void reorderIndices(FragmentManager manager) {
        if(!(manager instanceof FragmentManagerImpl)){
            return;
        }
        FragmentManagerImpl fragmentManagerImpl = (FragmentManagerImpl)manager;
        if(fragmentManagerImpl.mAvailIndices != null & fragmentManagerImpl.mAvailIndices.size() > 1) {
            Collections.sort(fragmentManagerImpl.mAvailIndices, Collections.reverseOrder());
        }
    }
}
```
使用方法: 通过popBackStackImmediate(tag/id) 多个Fragment 后,调用(*尽量使用popBackStackImmediate(tag/id) ,使用popBackStack() 有BUG*)
```java
handler.post(new Runnable) {
    @Override
    public void run() {
        FragmentTransactionBugFix.reverseOrder(fragmentManager);
    }
}
```
***
### 0x04 Fragment转场动画
如果通过tag/id同时出栈多个Fragment的情况时,
谨慎使用setCustomAimations(enter, exit, popEnter, popExit) .
因为在出栈多个Fragment 时,伴随着动画,会在某些情况下发生异常
还需要搭配Fragment的onCreateAnimation() 临时取消出栈动画. <p>
1. pop多个Fragment是转场动画带来的问题 <p>
在使用pop(tag/id)出栈多个Fragment ,务必不能设定转场动画. <p>
2. 进入新的Fragment并立刻关闭当前的Fragment是的一些问题 <p>
(1) 如果想从当前的Fragment进去一个新的Fragment,并且同时关闭当前的Fragment ,
由于是数据结构是栈,所以正确的做法是先pop ,再add , 但是转场动画会有覆盖的不正
常现象,需要处理,不是的话会闪屏. <p>
(2) Fragment 的根布局要设置android:clickable=true ,原因是pop后又立刻add 
新的Fragment 时,在转场动画过程中,如果手速太快,在动画结束前你多点击了一下,上
一个Fragment 的可点击区域可能会在下一个Fragment 上依然可用.
***
### 0x05 一些使用建议
1. 对Fragment 传递数据,建议使用setArguments(Bundle args) ,而后在onCreate() 
取出,在"内存重启"前,系统会保存数据,不会造成数据的丢失.和Activity 的intent原理
一致.
2. 使用newInstance(参数) 创建Fragment 对象,优点是调用者只需要关心传递的是哪些
数据,而无需关心传递数据的key 是什么.
3. 如果需要在Fragment中用到宿主Activity 对象,建议在基类Fragment中定义一个Activity 
的全局变量,在onAttach() 中初始化它.
***
### 0x06 add(), show(), hide(), replace()
1. 区别 <p>
show(), hide()最终是让Fragment的View setVisibility(true/false) 不会调用生命周期 <p>
replace() 的话会销毁视图,即调用onDestroyView(), onCreateView() 等一系列生命周期 <p>
 