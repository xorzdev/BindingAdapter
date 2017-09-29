package me.gavin.adapter.base.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * Recycler 基类适配器
 *
 * @author gavin.xiong 2017/8/15
 */
public abstract class RecyclerHeaderFooterAdapter<T, B extends ViewDataBinding>
        extends RecyclerView.Adapter<RecyclerHolder> {

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
