package com.steven.quickindex.commonAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by zhiwenyan on 5/25/17.
 */

public abstract class CommonRecycleAdapter<T> extends RecyclerView.Adapter<CommonViewHolder> {
    private int mLayoutId;
    private List<T> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mItemClickListener;
    private MultipleTypeSupport mTypeSupport;


    public CommonRecycleAdapter(Context context, List<T> mData, int layoutId) {
        this.mData = mData;
        this.mLayoutId = layoutId;
        mInflater = LayoutInflater.from(context);
    }

    public CommonRecycleAdapter(Context context, List<T> data, MultipleTypeSupport typeSupport) {
        this(context, data, -1);
        this.mTypeSupport = typeSupport;
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mTypeSupport != null) {
            //多布局
            mLayoutId = viewType;
        }
        View itemView = mInflater.inflate(mLayoutId, parent, false);
        return new CommonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, final int position) {
        convert(holder, mData.get(position), position);
        if (mItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(position);
                }
            });
        }
    }
    
    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        //多布局
        if (mTypeSupport != null) {
            return mTypeSupport.getLayoutId(mData.get(position));
        }
        return super.getItemViewType(position);
    }

    //点击事件
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    protected abstract void convert(CommonViewHolder holder, T t, int position);

}
