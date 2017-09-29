package me.gavin.adapter.base.base;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;

import java.util.List;

import me.gavin.adapter.base.R;

/**
 * DataBinding 基类适配器
 *
 * @author gavin.xiong 2016/12/28
 */
public class BindingAdapter<T> extends RecyclerAdapter<T, ViewDataBinding> {

    private IntConsumer onItemClickListener;

    public BindingAdapter(Context context, List<T> list, @LayoutRes int layoutId) {
        super(context, list, layoutId);
    }

    public void setOnItemClickListener(IntConsumer onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    protected void onBind(RecyclerHolder<ViewDataBinding> holder, T t, final int position) {
        holder.binding.setVariable(BR.item, t);
        holder.binding.executePendingBindings();
        if (onItemClickListener != null) {
            holder.itemView.findViewById(R.id.item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.accept(position);
                }
            });
        }
    }
}
