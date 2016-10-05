# Fragment 防坑指南
>学习于 : YoKey

## 0x00 getActivity() 空指针

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

## 0x01 Fragment 重叠异常

当发生"内存重启",FragmentManager 会将保存的Fragment 按照栈底到栈顶的顺序恢复,并且全部为show() 形式.

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

## 0x02 不靠谱的出栈方法remove()

popBackStack() 系列方法才能真正出栈

## 0x03 多个Fragment 同时出栈的那些深坑BUG
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
        FragmentTransactionBugFix.reorderIndices(fragmentManager);
    }
}
```

## 0x04 一些使用建议

1. 对Fragment 传递数据,建议使用setArguments(Bundle args) ,然后在onCreate() 取出,在"内存重启"前,系统会保存数据,不会造成数据的丢失.和Activity 的intent原理一致.
2. 使用newInstance(参数) 创建Fragment 对象,优点是调用者只需要关心传递的是哪些数据,而无需关心传递数据的key 是什么.
3. 如果需要在Fragment中用到宿主Activity 对象,建议在基类Fragment中定义一个Activity 的全局变量,在onAttach() 中初始化它.

## 0x05 add(), show(), hide(), replace()

1. 区别 <p>
show(), hide()最终是让Fragment的View setVisibility(true/false) 不会调用生命周期 <p>
replace() 的话会销毁视图,即调用onDestroyView(), onCreateView() 等一系列生命周期 <p>
add()和replace()不要在同一个阶段的FragmentManager 里混搭使用.

2. 使用场景 <p>
如果你有一个很高的概率会再次使用当前的Fragment ,建议使用show(),hide(),可以提高性能.

3. onHiddenChanged 的回调时机 <p>
当使用add()+show(),hide()跳转时,旧的Fragment 回调onHiddenChanged(), 不会回调opStop()等声明周期,
而新的Fragment 在创建时不会回调onHiddenChanged().

4. Fragment 重叠问题 <p>
使用show(),hide()带来的问题.不作处理的话,在"内存重启"后,Fragment 会重叠

## 0x06 关于FragmentManager

1. FragmentManager <p>
对于宿主Activity,getSupportFragmentManager()获取的是FragmentActivity的FragmentManager对象.对于Fragment ,getFragmentManager()是获取父Fragment(如果没有则是FragmentActivity)的FragmentManager对象,而getChildFragmentManager()是获取自己的FragmentManager 对象.

2. 恢复Fragment时(防止Fragment重叠),选择getFragments()还是findFragmentByTag() <p>
(1)getFragments().
对于一个Activity内有多个Fragment,如果关系是流程的 <p>
```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if(savedInstanceState != null) {
        List fragments = getSupportFragmentManager().getFragments();
        if(fragments != null & fragment.size() > 0) {
            boolean showFlag = false;

            FragmentTrasaction ft = getSupportFragmentManager().beginTransaction();
            for(int i = fragments.size() - 1;i >= 0; i--) {
                Fragment fragment = fragments.get(i);
                if(fragment != null) {
                    if(!showFlag){
                        ft.show(fragments.get(i));
                        showFlag = true;
                    } else {
                        ft.hide(ftagments.get(i));
                    }
                }
            }
            ft.commit();
        }
    }
}
```

(2)findFragmentByTag()恢复.不是流程关系,正确做法是在onSaveInstanceState()内保存当前在Fragment的tag或者下标,在onCreate()恢复的时候,隐藏其他的Fragment.
```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    FirstFragment firstFragment;
    SecondFragment secondFragment;
    OtherFragment otherFragment;
    if(savedInstanceState != null) {
        firstFragment = getFragmentManager.findFragmentByTag(firstFragment.getCLass().getName());
        secondFragment = getFragmentManager.findFragmentByTag(secondFragment.getCLass().getName());
        otherFragment = getFragmentManager.findFragmentByTag(otherFragment.getCLass().getName());

        index = savedInstanceState.getInt(KEY_INDEX);
        getSupportFragmentManager.beginTransaction()
        .show(otherFragment)
        .hide(secondFragment)
        .hide(firstFragment)
        .commit()
    } else {
        firstFragment = FirstFragment.newInstance();
        secondFragment = SecondFragment.newInstance();
        otherFragment = OtherFragment.newInstance();

        getSupportFragmentManager.beginTransaction()
        .add(R.id.container, firstFragment, firstFragment.getClass().getName())
        .add(R.id.container, secondFragment, firstFragment.getClass().getName())
        .add(R.id.container, otherFragment, firstFragment.getClass().getName())
        .hide(secondFragment)
        .hide(otherFragment)
        .commit();
    }
}

@Override
public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(KEY_INDEX, index);
}
```

## 0x07 使用ViewPager + Fragmet 的注意事项

1. 使用ViewPager + Fragment 时,切换不用ViewPager页面,不会回调任何的生命周期的方法以及onHiddenChanged(),只有setUserVisibleHint(boolean isVisibleToUser)会被回调.如果需要进行一些懒加载,需要在这里处理

2. 在给ViewPager绑定FragmentPagerAdapter时,new FragmentPagerAdapter(fragmentManager)的FragmentManger 一定要保证正确.

3. 如果使用ViewPager + Fragment, 不需要在"内存重启"的情况下,去恢复Fragments,有FragmentPagerAdapter的存在,不需要你去做恢复工作.

## 0x08 Fragment 事务

1. 如果你在使用popBackStackImmdiate()方法后,紧接着直接调用事务的方法,因为他们运行在消息队列的问题,还没来得及出栈就运行事务的方法了,这可能会导致不正常现象.
正确做法:
```java
getSupportFragmentManager().popBackStackImmediate();
new Handler().post(new Runnable() {
    @Override
    public void run() {
        //这里执行事务方法
    }
})
```

2. 给Fragment设定Fragment转场动画时，如果你没有一整套解决方案,只使用setCustomAnimations(enter, exit)这个方法.

## 0x09 作者给出的Fragmentation 库
[Fragmentation](https://github.com/YoKeyword/Fragmentation)
