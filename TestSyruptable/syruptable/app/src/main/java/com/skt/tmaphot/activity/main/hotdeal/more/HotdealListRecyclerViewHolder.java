package com.skt.tmaphot.activity.main.hotdeal.more;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.skt.tmaphot.R;

public class HotdealListRecyclerViewHolder extends RecyclerView.ViewHolder {

    private ImageView mImgReview = null;

    public HotdealListRecyclerViewHolder(View itemView) {
        super(itemView);

        if(itemView != null)
        {
            mImgReview = (ImageView) itemView.findViewById(R.id.main_img_review_item);
        }
    }

    public ImageView getImageView() {
        return mImgReview;
    }
}