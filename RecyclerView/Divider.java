/*
 RecyclerView的分割线Divider
 需要自己实现RecyclerView.ItemDecoration
 一下是封装好的，包括纯色divider，图片divider，设置上下左右间距、宽高设置
 */
public class Divider extends RecyclerView.ItemDecoration {

    private Drawable mDivider;
    private int leftMargin, rightMargin, topMargin, bottomMargin;
    private int width, height;
    private int mOrientation;

    public Divider(Drawable divider, int orientation) {
        setDivider(divider);
        setOrientation(orientation);
    }

    public void setDivider(Drawable divider) {
        this.mDivider = divider;
        if(mDivider == null) {
            mDivider = new ColorDrawable(0xffff0000);
        }
        width = mDivider.getIntrinsicWidth();
        height = mDivider.getIntrinsicHeight();
    }

    private void setOrientation(int orientation) {
        if(orientation != LinearLayoutManager.HORIZONTAL && orientation != LinearLayoutManager.VERICAL) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    public void setMargin(int left, int top, int right, int bottom) {
        this.leftMargin = left;
        this.topMargin = top;
        this.rightMargin = right;
        this.bottomMargin = bottom;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if(mOrientation == LinearLayoutManager.HORIZONTAL) {
            drawHorizontal(c, parent);
        } else {
            drawVertical(c, parent); 
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop() + topMargin;
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for(int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParam)child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin + leftMargin;
            final int right = left + width;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft() + leftMargin;
        final int right = parent.getWidth() - parent.getPaddingRight() - rightMargin;

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin + topMargin;
            final int bottom = top + height;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if(mOrientation == LinearLayoutManager.HORIZONTAL) {
            outRect.set(0 , 0, leftMargin + width + rightMargin, 0);
        } else {
            outRect.set(0, 0, 0, topMargin + height + bottomMargin);
        }
    }
}

// 自定义Divider使用
Divider divider = new Divider(new ColorDrawable(0xffff0000), OrientatinHelper.VERICAL);
// 单位px
divider.setMargin(20, 20, 20, 20);
divider.setHeight(20);
recyclerview.addItemDecoration(divider);