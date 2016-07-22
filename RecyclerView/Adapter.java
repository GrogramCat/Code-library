/**
  Adapter封装
  RecyclerView的通用适配器
  和滚动时不加载图片的封装
 */
 public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerHolder> {

     protected List<T> realDatas;
     protected final int mItemLayoutId;
     protected boolean isScrolling;
     protected Context context;
     private OnItemClickListener listener;

     public interface OnItemClickListener {
         void onItemClick(View view, Object data, int position)
     }

     public BaseRecyclerAdapter(RecyclerView v, Collection<T> datas, int itemLayoutId) {
         if(datas == null) {
             realDatas = new ArratList<>();
         } else if(datas instanceof List) {
             realDatas = (List<T>) datas;
         } else {
             realDatas = new ArratList<>(datas);
         }
         mItemLayoutId = itemLayoutId;
         context = v.getContext();

         v.addOnScrollListener(new RecyclerView.OnScrollListener() {

             @Override
             public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                 super.onScrollStateChanged(recyclerView, newState);
                 isScrolling =! (newState == RecyclerView.SCROLL_STATE_IDLE);
                 if(!isScrolling) {
                     notifyDataSetChanged();
                 }
             }
         })
     }

     public abstract void convert(RecyclerHolder holder, int position, boolean isScrolling);

     @Override 
     public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         LayoutInflater inflater = LayoutInflater.from(context);
         View root = infalter.inflate(mItemLayoutId, parent, false);
         return new RecyclerHolder(root);
     }

     @Override
     public void onBindViewHolder(RecyclerHolder holder, int position) {
         convert(holder, realDatas.get(position), position, isScrolling);
         holder.itemView.setOnClickListeren(getOnClickListener(position));
     }

     @Overide
     public int getItemCount() {
         return realDatas.size();
     }

     public void setOnClickListeren(onItemClickListener l) {
         listener = l;
     }

     public View.OnclickListener getOnClickListener(final int position) {
         return new View.OnClickListener() {
             @Override
             public void OnClick(@Nullable View v) {
                 if(listener != null && v != null) {
                     listener.onItenmClick(v, realDatas.get(position), position);
                 }
             }
         };
     }

     public BaseRecyclerAdapter<T> refresh(Collection<T> datas) {
         if(datas == null) {
             realDatas = new ArrayList<>();
         } else if(datas instanceof List) {
             realDatas = (List<T>) datas;
         } else {
             realDatas = new ArraryList<>(datas);
         }
         return this;
     }

     /**
       适配一切的RecyclerView.ViewHolder
      */
     public class RecyclerHolder extends RecyclerView.ViewHolder {
        
        private final SparseArray<View> mViews;

        public RecyclerHolder(View itemView) {
            super(itemView);
            this.mViews = new SparseArray<View>(8);
        }

        public SparseArray<View> getAllView() {
            return mViews;
        }

        // 通过控件的id获取对应的控件，如果没有则加入views
        @SupperssWarnings("unchecked")
        public <T extens View> T getView(int viewId) {
            View view = mViews.get(viewId);
            if(view == null) {
                view = itemView.findViewById(viewId);
                mViews.put(viewId,view);
            }
            return (T)view;
        }

        public RecyclerHolder setText(int viewId, String text) {
            TextView view = getView(viewId);
            view.setText(text);
            return this;
        }

        public RecyclerHolder setImageResource(int viewId, int drawableId) {
            ImageView view = getView(viewId);
            view.setImageResource(drawableId);
            return this;
        }

        public RecyclerHolder setImageBitmap(int viewId, Bitmap bm) {
            ImageView view = getView(viewId);
            view.setImageBitmap(bm);
            return this;
        }

        public RrcyclerHolder setImageByUrl(KJBitmap bitmap, int viewId, String url) {
            // 这里可替换成Glide
            bitmap.display(getView(viewId), url);
            return this;
        }
     }
 }
 