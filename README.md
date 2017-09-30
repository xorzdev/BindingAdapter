# BindingAdapter
DataBinding + BindingAdapter 用一个 adapter 解决所有列表适配器。

你还在为每个列表专门写一个 `adapter` 和 `ViewHolder` 吗？这里教大家用 `databinding` 打造通用列表适配器 , `adapter * 1` + `ViewHolder * 1` 适配所有列表，支持 `HeaderView`、`FooterView`，以 RecyclerView 为例：

## 效果

![a.gif](http://upload-images.jianshu.io/upload_images/4336778-74d752100febc1d3.gif?imageMogr2/auto-orient/strip)


## 使用
- `Story.java`
```
/**
 * 测试数据
 *
 * @author gavin.xiong 2017/9/29
 */
public class Story {

    private String title;
    private String image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

```

- `item_story.xml`
 ```
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <data>

        <variable
            name="item"
            type="me.gavin.adapter.base.Story" />

    </data>

    <LinearLayout
        android:id="@+id/item"
        android:layout_width="match_parent"
        android:layout_height="82dp"
        android:clickable="true"
        android:foreground="?attr/selectableItemBackground"
        android:gravity="center_vertical"
        android:paddingLeft="16dp"
        android:paddingRight="16dp">

        <ImageView
            android:layout_width="50dp"
            android:layout_height="50dp"
            android:scaleType="centerCrop"
            app:imageUrl="@{item.image}"
            tools:ignore="contentDescription"
            tools:src="@mipmap/ic_launcher" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginStart="12dp"
            android:layout_marginLeft="12dp"
            android:lineSpacingMultiplier="1.2"
            android:maxLines="2"
            android:text="@{item.title}"
            android:textColor="#99000000"
            android:textSize="14sp"
            tools:text="这就是理想主义，对彼此，对现实的妥协" />

    </LinearLayout>

</layout>
 ```


- 代码中使用(只有一行代码，传入泛型数据列表和资源id，通用所有数据，做列表只需要写一个 item layout 就可以了)
```
recyclerView.setAdapter(new BindingAdapter<>(context, list, R.layout.item_test));
```
