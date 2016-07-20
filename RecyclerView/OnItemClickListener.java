/*
 RecyclerView的
 OnItemClickListener
 */
public abstract class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

    protected abstract void onItemClick(View view, int position);
    private GestureDetector mGestureDetector;

    public RecyclerItemClickListener(Context context) {
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        })
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if(childView != null && mGestureDetector.onTouchEvent(e)) {
            onItemClick(childView, view.getChildAdapterPosition(childView));
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptThouchEvent(boolean disallowIntercept) {

    }
}