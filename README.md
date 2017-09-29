# BindingAdapter
DataBinding + BindingAdapter 用一个 adapter 解决所有列表适配器。

你还在为每个列表专门写一个 `adapter` 和 `ViewHolder` 吗？这里教大家用 `databinding` 打造通用列表适配器 , `adapter * 1` + `ViewHolder * 1` 适配所有列表，支持 `HeaderView`、`FooterView`，以 RecyclerView 为例：

## 效果

![a.gif](http://upload-images.jianshu.io/upload_images/4336778-74d752100febc1d3.gif?imageMogr2/auto-orient/strip)


## 准备工作
- 想要支持 DataBinding， 需要在 module 的 buidle.gradle 的 android 中加上：
```
    dataBinding {
        enabled = true
    }
```

- 引入 RecyclerView 包
`
compile 'com.android.support:recyclerview-v7:25.3.1'
`

- 为方便使用，我们先对 `RecyclerView.Adapter` 做简单封装，通过构造器传递列表数据和布局文件 id，并留出 `onBind(RecyclerHolder<B> holder, T t, int position)` 方法装载数据

 ```
/**
  * RecyclerView 基类列表适配器
  *
  * @author gavin.xiong 2016/12/9
  */
public abstract class RecyclerAdapter<T, B extends ViewDataBinding> extends RecyclerView.Adapter<RecyclerHolder<B>> {

    protected Context mContext;
    protected List<T> mList;
    private int layoutId;

    public RecyclerAdapter(Context context, List<T> list, @LayoutRes int layoutId) {
        this.mContext = context;
        this.mList = list;
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

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    protected abstract void onBind(RecyclerHolder<B> holder, T t, int position);

}
```

- 以及对应的 `RecyclerHolder.java`
```
/**
 * RecyclerHolder
 *
 * @author gavin.xiong 2016/12/9
 */
public class RecyclerHolder<B extends ViewDataBinding> extends RecyclerView.ViewHolder {

    public final B binding;

    public RecyclerHolder(B binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
```

- 接下来就是主角 `BindingAdapter.java` 了（这里除装载数据外只留了一个item点击事件监听器，如有需要可拓展）：
```
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
```

- item点击事件监听器：
```
@FunctionalInterface
public interface IntConsumer {
    void accept(int i);
}
```

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

- 特殊数据绑定如`加载图片`等
 ```
/**
  * 数据绑定适配器
  *
  * @author gavin.xiong 2017/8/15
  */
public class BindingHelper {
     /**
      * 使用DataBinding来加载图片
      * 使用@BindingAdapter注解，注解值（这里的imageUrl）可任取，注解值将成为自定义属性
      * 此自定义属性可在xml布局文件中使用，自定义属性的值就是这里定义String类型url
      * 1. 方法名可与注解名一样，也可不一样
      * 2. 第一个参数必须是View，就是自定义属性所在的View
      * 3. 第二个参数就是自定义属性的值，与注解值对应。这是数组，可多个
      * 这里需要INTERNET权限，别忘了
      *
      * @param imageView ImageView控件
      * @param url       图片网络地址
      */
      @BindingAdapter({"imageUrl"})
      public static void loadImage(ImageView imageView, String url) {
          Glide.with(imageView.getContext())
                .load(url)
                .into(imageView);
    }
 }
 ```

- 代码中使用
```
recyclerView.setAdapter(new BindingAdapter<>(context, list, R.layout.item_test));
```
- Activity 完整代码
```
public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    private String[] names = {
            "处于 25-35 岁的你，开始买奢侈品了吗？",
            "有些法律上的事，非要解释给你听，你才知道理解错了",
            "要是早点知道这些，也不至于当初在校招季踩坑无数……",
            "孩子眼中的爸爸，可不只是「走，我们出去玩儿」就够了",
            "为什么胖这个字写成「月半」，不写成「月圆」或者「月全」？"
    };

    private String[] images = {
            "https://pic2.zhimg.com/v2-f474e73c7aabf49a96cb39f68b81b6e1.jpg",
            "https://pic2.zhimg.com/v2-f474e73c7aabf49a96cb39f68b81b6e1.jpg",
            "https://pic1.zhimg.com/v2-d081c48015edde0e8065709f864ee474.jpg",
            "https://pic3.zhimg.com/v2-4d8dcd6de27f81bc01435e0c6867b60e.jpg",
            "https://pic2.zhimg.com/v2-006da90be72f74b5e7fc49f1658896a1.jpg",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Random random = new Random();
        List<Story> userList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            Story u = new Story();
            int index = random.nextInt(names.length);
            u.setTitle(i + " - " + names[index]);
            u.setImage(images[index]);
            userList.add(u);
        }

        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        BindingAdapter adapter = new BindingAdapter<>(this, userList, R.layout.item_story);
        adapter.setOnItemClickListener(new IntConsumer() {
            @Override
            public void accept(int i) {
                Toast.makeText(MainActivity.this, i + " - 被点击了", Toast.LENGTH_SHORT).show();
            }
        });
        binding.recycler.setAdapter(adapter);
    }
}
```
## 补充
- 可以添加 `HeaderView` 和 `FooterView` 的 `RecyclerHeaderFooterAdapter.java`
```
/**
 * Recycler 基类适配器
 *
 * @author gavin.xiong 2017/8/15
 */
public abstract class RecyclerHeaderFooterAdapter<T, B extends ViewDataBinding> extends RecyclerView.Adapter<RecyclerHolder> {

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_HEADER = 1;
    private static final int TYPE_FOOTER = 2;

    private ViewDataBinding mHeaderBinding;
    private ViewDataBinding mFooterBinding;

    protected Context mContext;
    protected List<T> mList;
    private int layoutId;

    public RecyclerHeaderFooterAdapter(Context context, List<T> list, @LayoutRes int layoutId) {
        this.mContext = context;
        this.mList = list;
        this.layoutId = layoutId;
    }

    public void setHeaderBinding(ViewDataBinding binding) {
        this.mHeaderBinding = binding;
    }

    public void setFooterBinding(ViewDataBinding binding) {
        this.mFooterBinding = binding;
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderBinding != null && position == 0) return TYPE_HEADER;
        if (mFooterBinding != null && position == (mList == null ? 0 : mList.size()) + (mHeaderBinding == null ? 0 : 1))
            return TYPE_FOOTER;
        return TYPE_NORMAL;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderBinding != null && viewType == TYPE_HEADER) {
            return new RecyclerHolder<>(mHeaderBinding);
        } else if (mFooterBinding != null && viewType == TYPE_FOOTER) {
            return new RecyclerHolder<>(mFooterBinding);
        } else {
            B bing = DataBindingUtil.inflate(LayoutInflater.from(mContext), layoutId, parent, false);
            return new RecyclerHolder<>(bing);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        if (getItemViewType(position) == TYPE_NORMAL) {
            final int pos = getRealPosition(holder);
            final T data = mList.get(pos);
            onBind(holder, pos, data);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == TYPE_NORMAL ? 1 : gridManager.getSpanCount();
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp == null) return;
        if (lp.getClass() == RecyclerView.LayoutParams.class) {
            lp.width = RecyclerView.LayoutParams.MATCH_PARENT; // 线性布局头尾全屏 (仅纵向有效，且所有 item 并非头尾有效)
        }
        if (mHeaderBinding != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(holder.getLayoutPosition() == 0 || p.isFullSpan());
        }
        if (mFooterBinding != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(holder.getLayoutPosition() == (mList == null ? 0 : mList.size()) + (mHeaderBinding == null ? 0 : 1) || p.isFullSpan());
        }
    }

    private int getRealPosition(RecyclerHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderBinding == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {
        if (mHeaderBinding != null && mFooterBinding != null) {
            return mList == null ? 2 : mList.size() + 2;
        } else if (mHeaderBinding == null && mFooterBinding == null) {
            return mList == null ? 0 : mList.size();
        } else {
            return mList == null ? 1 : mList.size() + 1;
        }
    }

    protected abstract void onBind(RecyclerHolder<B> holder, int position, T t);

}
```

及对应的万能适配器
```
/**
 * DataBinding 基类适配器
 *
 * @author gavin.xiong 2016/12/28
 */
public class BindingHeaderFooterAdapter<T> extends RecyclerHeaderFooterAdapter<T, ViewDataBinding> {

    private IntConsumer onItemClickListener;

    public BindingHeaderFooterAdapter(Context context, List<T> list, @LayoutRes int layoutId) {
        super(context, list, layoutId);
    }

    public void setOnItemClickListener(IntConsumer listener) {
        this.onItemClickListener = listener;
    }

    @Override
    protected void onBind(RecyclerHolder<ViewDataBinding> holder, final int position, T t) {
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
```

完整代码已上传至github：https://github.com/gavinxxxxxx/BindingAdapter
