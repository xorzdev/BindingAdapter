package me.gavin.adapter.base.base;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * 数据绑定适配器
 *
 * @author gavin.xiong 2017/8/15
 */
public class BindingHelper {

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .into(imageView);
    }

}