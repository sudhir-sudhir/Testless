package com.wikipic.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.wikipic.util.LogUtil;

public class EndlessScrollListener extends RecyclerView.OnScrollListener {

    private static final String TAG = EndlessScrollListener.class.getSimpleName();
    private GridLayoutManager mGridLayoutManager = null;
    private IOnLoadMoreListener mLoadMoreListener = null;

    // The minimum amount of items to have below your current scroll position
    // before mIsLoading more.
    private int mVisibleThreshold = 2;
    // The total number of items in the dataset after the last load
    private int mPreviousTotalItemCount = 0;
    // True if we are still waiting for the last set of data to load.
    private boolean mIsLoading = false;

    public EndlessScrollListener(GridLayoutManager gridLayoutManager,
                                 IOnLoadMoreListener loadMoreListener) {
        mGridLayoutManager = gridLayoutManager;
        mLoadMoreListener = loadMoreListener;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

    }

    /**
     * dx : The amount of horizontal scroll.
     * dy : The amount of vertical scroll.
     */
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (dy <= 0) {
            return;
        }

        final int firstVisibleItem = mGridLayoutManager.findFirstVisibleItemPosition();
        final int lastVisibleItem = mGridLayoutManager.findLastVisibleItemPosition();
        final int totalItemCount = mGridLayoutManager.getItemCount();
        final int visibleItemCount = lastVisibleItem - firstVisibleItem;

        LogUtil.d(TAG, "OnScroll. firstVisibleItem: " + firstVisibleItem
                + " lastVisibleItem: " + lastVisibleItem + " totalItemCount: " + totalItemCount
                + " visibleItemCount: " + visibleItemCount);

        // If it’s still mIsLoading, we check to see if the dataset count has
        // changed, if so we conclude it has finished mIsLoading and update the current page
        // number and total item count.
        if (mIsLoading) {
            if (totalItemCount - 1 > mPreviousTotalItemCount) {
                mIsLoading = false;
                mPreviousTotalItemCount = totalItemCount;
            }
        }

        // If it isn’t currently mIsLoading, we check to see if we have breached
        // the mVisibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        if (!mIsLoading) {
            if ((totalItemCount - visibleItemCount) <= (firstVisibleItem + mVisibleThreshold)) {
                if (mLoadMoreListener != null) {
                    boolean loading = mLoadMoreListener.onLoadMore();
                    if (loading) {
                        mIsLoading = true;
                    }
                }
            }
        }
    }

    public boolean isLoading() {
        return mIsLoading;
    }

    public void reset() {
        mIsLoading = false;
        mPreviousTotalItemCount = 0;
    }
}
