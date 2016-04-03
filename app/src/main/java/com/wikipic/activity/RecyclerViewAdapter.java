package com.wikipic.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wikipic.R;
import com.wikipic.model.Pages;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int ITEM_TYPE_PAGE = 0;
    public static final int ITEM_TYPE_FOOTER = 1;

    private ArrayList<Pages> mPages = null;
    private Context mContext = null;

    private int mItemHeight = 0;
    private int mNumColumns = 0;

    private RelativeLayout.LayoutParams mImageViewLayoutParams = null;
    private LayoutInflater mInflater = null;
    private ItemClickListener mItemClickListener = null;

    public RecyclerViewAdapter(Context context, List<Pages> itemList,
                               ItemClickListener itemClickListener) {
        mContext = context;
        mPages = new ArrayList<Pages>(itemList);
        mItemClickListener = itemClickListener;

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mImageViewLayoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public int getItemCount() {
        return mPages.size();
    }

    public Pages getItem(int position) {
        return mPages.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_FOOTER) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_progress, null);
            return new RecyclerProgressViewHolders(layoutView, mItemClickListener);
        } else if (viewType == ITEM_TYPE_PAGE) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, null);
            return new RecyclerItemViewHolders(layoutView, mItemClickListener);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == ITEM_TYPE_FOOTER) {
            RecyclerProgressViewHolders progressHolder = (RecyclerProgressViewHolders) holder;

        } else if (getItemViewType(position) == ITEM_TYPE_PAGE) {
            Pages page = getItem(position);

            RecyclerItemViewHolders itemHolder = (RecyclerItemViewHolders) holder;
            itemHolder.mTitleView.setText(page.getTitle());
            itemHolder.mImageView.setLayoutParams(mImageViewLayoutParams);
            if (page.hasValidThumnail()) {
                Glide.with(this.mContext)
                        .load(page.getThumbnail().getSource())
                        .placeholder(R.drawable.image_placeholder)
                        .error(R.drawable.image_placeholder)
                        .animate(R.anim.zoom_anim)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(itemHolder.mImageView);
            } else {
                // Set placeholder if no valid image url found
                itemHolder.mImageView.setImageResource(R.drawable.image_placeholder);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return isPositionFooter(position) ? ITEM_TYPE_FOOTER : ITEM_TYPE_PAGE;
    }

    private boolean isPositionFooter(int position) {
        return getItem(position) == null;
    }

    public void addItems(List<Pages> pages) {
        mPages.addAll(pages);
        notifyDataSetChanged();
    }

    public void resetItems(List<Pages> pages) {
        mPages = new ArrayList<Pages>(pages);
        notifyDataSetChanged();
    }

    public void addItem(Pages page) {
        if (!mPages.contains(page)) {
            ArrayList<Pages> p =new ArrayList<Pages>();
            p.add(page);
            mPages.addAll(p);
            notifyDataSetChanged();
            //notifyItemInserted(mPages.size() - 1);
        }
    }

    public void removeItem(Pages item) {
        int indexOfItem = mPages.indexOf(item);
        if (indexOfItem != -1) {
            mPages.remove(indexOfItem);
            notifyDataSetChanged();
            //notifyItemRemoved(indexOfItem);
        }
    }

    // Set number of columns to display
    public void setNumColumns(int numColumns) {
        mNumColumns = numColumns;
    }

    public int getNumColumns() {
        return mNumColumns;
    }

    // Set image height
    public void setItemHeight(int height) {
        if (height == mItemHeight) {
            return;
        }
        mItemHeight = height;
        mImageViewLayoutParams = new RelativeLayout.LayoutParams(
                GridLayout.LayoutParams.MATCH_PARENT, mItemHeight);
        notifyDataSetChanged();
    }
}
