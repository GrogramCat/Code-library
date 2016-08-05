package com.temoa.gankio.tools;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 通用ViewHolder的写法
 * Created by Temoa
 * on 2016/8/1 18:28
 */
public class RecyclerHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> views;

    public RecyclerHolder(View itemView) {
        super(itemView);
        views = new SparseArray<>(8); // 假设一个itemView中有8个子控件(应该都不会有更多)
    }

    public SparseArray<View> getAllViews() {
        return views;
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    public RecyclerHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public RecyclerHolder setImageResource(int viewId, int drawableId) {
        ImageView iv = getView(viewId);
        iv.setImageResource(drawableId);
        return this;
    }

    public RecyclerHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView iv = getView(viewId);
        iv.setImageBitmap(bitmap);
        return this;
    }

    public RecyclerHolder setImageByUrl() {
        // TODO: 用图片缓存库
        return this;
    }
}
