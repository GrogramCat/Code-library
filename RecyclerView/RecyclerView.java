// 常用方法
// 在build.gradle文件中加入
compile 'com.android.support:recyclerview-v7:24.0.0'

// 创建对象
RecyclerView recyclerview = (RecyclerView)findViewById(R.id.recyclerview);

// 设置显示的方式
/*
 RecyclerView将显示的方式交由一个叫LayoutManager的类去完成
 系统默认为我们提供了三个实现了类：LinearLayoutManager、GridLayoutManager、StaggeredGridLayoutManager
 分别是线性、网络、瀑布流
 */
recyclerview.setLayoutManager(new LinearLayoutManager(this, linearLayoutManager.VERTICAL, false));

// 设置适配器
recyclerview.setAdapter(adapter)
/*
 创建Adapter首先继承RecyclerView.Adapter<VH>类，实现三个抽象的方法
 并创建一个静态的ViweHolder，继承RecyclerView.ViewHolder类
 */
 public class Adapter entends RecyclerView.Adapter<Adapter.VH> {
     private List<Data> dataList;
     private Context context;

     public Adapter(Context context, ArrayList<Data> datas) {
         this.dataList = datas;
         this.context = context
     }

     @Override
     public VH onCreateViewHolder(ViewGroup parent, int viewType) {
         return new VH(View.inflate(context, android.R.layout.simple_list_item_2,null));
     }

     @Override
     public void onBindViewHolder(VH holder, int position) {
         holder.mTextView.setText(dataList.get(position).getNum());
     }

     @Override
     public int getItemCount() {
         return dataList.size();
     }

     public static class VH entends RecyclerView.ViewHolder {
         TextView mTextView;
         public VH(View itemView) {
             super(itemView);
             mTextView = (TextView)itemView.findViewById(android.R.id.text1);
         }
     }
 }

// 更多的方法
/*
 瀑布流与滚动的方向
 设置瀑布流可以通过一下,第一个参数是列数，第二个参数是横向滚动还是纵向滚动
 */
 recycler.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));

/*
 添加删除item的动画
 通过调用notifyDataSetChanged()刷新item，RecyclerView还支持局部刷新
 RecyclerView还提供了默认的动画效果来，来改变显示。模仿DefaultItemAnimator或直接继承这个类
 */
// 更新item
notifyItemInserted(index);// 插入
notifyItemRemoved(position);// 删除
notifyItemChanged(position);// 修改
// 设置动画
recyclerview.setItemAnimator(new DefaultItemAnimator());

/*
 LayoutManager的常用方法
 */
findFirstVisibleItemPosition();// 返回当前第一个可见item的position
findFirstCompletelyVisibleItemPosition();// 返回当前第一个完全可见Item的position
findLastVisibleItemPosition();// 返回当前最后一个可见item的position
findLastCompletelyVisibleItemPosition();// 返回当前最后一个完全可见Item的position
scrollby();// 滚动到某个位置

/*
 Adapter封装
 RecyclerView的通用适配器
 和滚动时不加载图片的封装
 */

/*
 各部分的分工
 */
RecyclerView.LayoutManager// 负责item视图的布局显示管理
RecyclerView.ItemDecoration// 给每一项item视图添加子view，画分割线
RecyclerView.ItemAnimator// 负责处理动画效果
RecyclerView.Adapter// 为每一项item创建视图
RecyclerView.ViewHolder// 继承item视图的子布局

