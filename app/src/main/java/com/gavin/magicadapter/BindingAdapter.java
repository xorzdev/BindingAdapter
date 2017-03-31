package com.gavin.magicadapter;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.view.View;

import java.util.List;

/**
 * DataBinding 基类适配器
 *
 * @author gavin.xiong 2016/12/28
 */
public class BindingAdapter<T> extends RecyclerAdapter<T, ViewDataBinding> {

    private int variableId;
    private OnItemClickListener onItemClickListener;

    public BindingAdapter(Context context, List<T> mData, @LayoutRes int layoutId, int variableId) {
        super(context, mData, layoutId);
        this.variableId = variableId;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBind(RecyclerHolder<ViewDataBinding> holder, T t, final int position) {
        holder.binding.setVariable(variableId, t);
        holder.binding.executePendingBindings();
        if (onItemClickListener != null) {
            holder.itemView.findViewById(R.id.item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(position);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
