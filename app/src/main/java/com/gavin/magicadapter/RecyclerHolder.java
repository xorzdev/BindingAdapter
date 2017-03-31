package com.gavin.magicadapter;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

public class RecyclerHolder<B extends ViewDataBinding> extends RecyclerView.ViewHolder {

    public final B binding;

    public RecyclerHolder(B binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}