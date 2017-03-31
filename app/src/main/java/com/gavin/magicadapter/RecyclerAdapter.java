package com.gavin.magicadapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * RecyclerView 基类列表适配器
 *
 * @author gavin.xiong 2016/12/9  2016/12/9
 */
public abstract class RecyclerAdapter<T, B extends ViewDataBinding> extends RecyclerView.Adapter<RecyclerHolder<B>> {

    protected Context mContext;
    protected List<T> mList;
    private int layoutId;

    public RecyclerAdapter(Context context, List<T> mData, @LayoutRes int layoutId) {
        this.mContext = context;
        this.mList = mData;
        this.layoutId = layoutId;
    }

    @Override
    public RecyclerHolder<B> onCreateViewHolder(ViewGroup parent, int viewType) {
        B bing = DataBindingUtil.inflate(LayoutInflater.from(mContext), layoutId, parent, false);
        return new RecyclerHolder<>(bing);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder<B> holder, int position) {
        final T t = mList.get(position);
        onBind(holder, t, position);
    }

    public abstract void onBind(RecyclerHolder<B> holder, T t, int position);

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

}
