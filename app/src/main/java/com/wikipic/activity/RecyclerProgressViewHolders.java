package com.wikipic.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wikipic.R;

public class RecyclerProgressViewHolders extends RecyclerView.ViewHolder {

    public TextView mTitleView = null;
    public ImageView mImageView = null;

    public RecyclerProgressViewHolders(View itemView, ItemClickListener itemClickListener) {
        super(itemView);
        mTitleView = (TextView) itemView.findViewById(R.id.country_name);
        mImageView = (ImageView) itemView.findViewById(R.id.country_photo);
    }
}
