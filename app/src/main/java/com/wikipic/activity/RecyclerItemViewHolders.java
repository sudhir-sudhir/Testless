package com.wikipic.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wikipic.R;

public class RecyclerItemViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView mTitleView = null;
    public ImageView mImageView = null;
    private ItemClickListener mItemClickListener = null;

    public RecyclerItemViewHolders(View itemView, ItemClickListener itemClickListener) {
        super(itemView);
        itemView.setOnClickListener(this);
        mItemClickListener = itemClickListener;
        mTitleView = (TextView) itemView.findViewById(R.id.country_name);
        mImageView = (ImageView) itemView.findViewById(R.id.country_photo);
    }

    @Override
    public void onClick(View view) {
        mItemClickListener.onClick(view, getAdapterPosition());
    }
}