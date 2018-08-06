package com.skt.tmaphot.activity.blog;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.skt.tmaphot.BaseApplication;
import com.skt.tmaphot.R;
import com.skt.tmaphot.common.CommonUtil;

import java.util.ArrayList;
import java.util.List;

public class MyBlog {

    public Context mContext;
    public View rootView;
    public android.os.Handler handler;

    public NestedScrollView nestedScrollView;

    // 내리뷰리스트
    public RecyclerView myReviewRecyclerView;
    public MyReviewRecyclerViewAdapter myReviewRecyclerViewAdapter;
    public List<MyReviewItem> myReviewItemList;
    public List<MyblogReviewImageItem> myblogReviewImageItemList;


    public MyBlog(Context mContext, android.os.Handler handler, View rootView) {
        this.mContext = mContext;
        this.handler = handler;
        this.rootView = rootView;
        init();
    }

    private class MyReviewItem {
        private String loginImageUrl;
        private String nickname;
        private String content;
        private String sympathyImage;
        private String sympathy;

        private List<MyblogReviewImageItem> myblogReviewImageItems;


        public MyReviewItem(String loginImageUrl, String nickname, String content, String sympathyImage, String sympathy, List<MyblogReviewImageItem> myblogReviewImageItems) {
            this.loginImageUrl = loginImageUrl;
            this.nickname = nickname;
            this.content = content;
            this.sympathyImage = sympathyImage;
            this.sympathy = sympathy;
            this.myblogReviewImageItems = myblogReviewImageItems;
        }

        public String getLoginImageUrl() {
            return loginImageUrl;
        }

        public String getNickname() {
            return nickname;
        }

        public String getContent() {
            return content;
        }

        public String getSympathyImage() {
            return sympathyImage;
        }

        public String getSympathy() {
            return sympathy;
        }

        public List<MyblogReviewImageItem> getMyblogReviewImageItem() {
            return myblogReviewImageItems;
        }
    }

    private class ReviewRecyclerViewHolder extends RecyclerView.ViewHolder {
        private ImageView loginImageView;
        private TextView nicknameTextview;
        private TextView contentTextview;
        private ImageView sympathyImageView;
        private TextView sympathyTextView;
        public RecyclerView myReviewImageRecyclerView;

        public ReviewRecyclerViewHolder(View itemView) {
            super(itemView);
//            loginImageView = (ImageView) itemView.findViewById(R.id.coupon_recyler_item_image);
            myReviewImageRecyclerView = (RecyclerView) itemView.findViewById(R.id.myblog_review_image_recyler);
            myReviewImageRecyclerView.setFocusable(false);
            myReviewImageRecyclerView.scrollToPosition(0);
            myReviewImageRecyclerView.setNestedScrollingEnabled(false);
            myReviewImageRecyclerView.setHasFixedSize(true);
            myReviewImageRecyclerView.addItemDecoration(CommonUtil.getInstance().new SpacesItemDecoration(0, 15, 0, 0));
        }
    }

    private class MyReviewRecyclerViewAdapter extends RecyclerView.Adapter<ReviewRecyclerViewHolder> {

        private List<MyReviewItem> mItems;
        private Context mContext;

        public MyReviewRecyclerViewAdapter(List<MyReviewItem> reviewItemList) {
            mItems = reviewItemList;
        }

        @Override
        public ReviewRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            mContext = parent.getContext();
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View itemView = layoutInflater.inflate(R.layout.myblog_review_recycler_item, parent, false);
            ReviewRecyclerViewHolder ret = new ReviewRecyclerViewHolder(itemView);
            return ret;
        }

        @Override
        public void onBindViewHolder(ReviewRecyclerViewHolder holder, final int position) {
//            MainApplication.loadUrlImage(mContext, mItems.get(position).getLoginImageUrl(), holder.loginImageView);

            //////////// test /////////////////
            setReviewImage(mItems.get(position).getMyblogReviewImageItem(), holder.myReviewImageRecyclerView);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, String.format("%d 선택", position + 1), Toast.LENGTH_SHORT).show();
                    BaseApplication.getInstance().ActivityStart(new Intent(mContext, MyBlogStoreReviewActivity.class), null);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
    }


    public class MyblogReviewImageItem {
        public String id;
        public String imageUrl;

        public MyblogReviewImageItem(String id, String imageUrl) {
            this.id = id;
            this.imageUrl = imageUrl;
        }
    }

    public class MyblogReviewImageItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImgReview = null;

        public MyblogReviewImageItemViewHolder(View itemView) {
            super(itemView);

            if (itemView != null) {
                mImgReview = (ImageView) itemView.findViewById(R.id.coupon_image);

                Drawable drawable = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    drawable = (Drawable) itemView.getContext().getDrawable(R.drawable.round_main_item);
                    itemView.setBackground(drawable);
                    itemView.setClipToOutline(true);
                }

            }
        }

        public ImageView getImageView() {
            return mImgReview;
        }

    }

    public class MyblogReviewImageRecyclerViewDataAdapter extends RecyclerView.Adapter<MyblogReviewImageItemViewHolder> implements View.OnClickListener {
        private Context mContext;
        private List<MyblogReviewImageItem> viewItemList;


        public MyblogReviewImageRecyclerViewDataAdapter(List myblogReviewImageItem) {
            viewItemList = myblogReviewImageItem;
        }

        @Override
        public MyblogReviewImageItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            mContext = parent.getContext();

            // Get LayoutInflater object.
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            // Inflate the RecyclerView item layout xml.
            View itemView = layoutInflater.inflate(R.layout.storeinfo_image_review_recycler_item, parent, false);

            // Create and return our customRecycler View Holder object.
            MyblogReviewImageItemViewHolder ret = new MyblogReviewImageItemViewHolder(itemView);
            itemView.setOnClickListener(this);
            return ret;
        }

        @Override
        public void onBindViewHolder(MyblogReviewImageItemViewHolder holder, int position) {
            if (viewItemList != null) {
                // Get car item dto in list.
                MyblogReviewImageItem viewItem = viewItemList.get(position);

                if (viewItem != null) {
                    // Set car item title.
                    BaseApplication.getInstance().loadImage(mContext, viewItem.imageUrl, holder.getImageView(), false);
                }
            }
        }

        @Override
        public int getItemCount() {
            int ret = 0;
            if (viewItemList != null) {
                ret = viewItemList.size();
            }
            return ret;
        }

        @Override
        public void onClick(View v) {

        }
    }

    private void init(){
        nestedScrollView = (NestedScrollView)rootView.findViewById(R.id.scroll_view);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY > oldScrollY) {
                    Log.i("TAAA", "Scroll DOWN");
                }
                if (scrollY < oldScrollY) {
                    Log.i("TAAA", "Scroll UP");
                }

                if (scrollY == 0) {
                    Log.i("TAAA", "TOP SCROLL");
                }

                if ( scrollY == ((v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()))) {
                    Log.i("TAAA", "BOTTOM SCROLL");
                    setDummyData(); //review more
                }
            }
        });

        setDummyData();
        setRecyler();
    }

    private void setReviewImage(final List<MyblogReviewImageItem> myblogReviewImageItemList, RecyclerView myReviewImageRecyclerView) {

        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 1);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        myReviewImageRecyclerView.setLayoutManager(layoutManager);

        final MyblogReviewImageRecyclerViewDataAdapter myblogReviewImageRecyclerViewDataAdapter = new MyblogReviewImageRecyclerViewDataAdapter(myblogReviewImageItemList);
        myReviewImageRecyclerView.setAdapter(myblogReviewImageRecyclerViewDataAdapter);

        myReviewImageRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                Log.d("YYY", "onScrolled");

                super.onScrolled(recyclerView, dx, dy);

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

                int firstCompleteVisibleItemPosition = -1;
                int lastCompleteVisibleItemPosition = -1;
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();

                if (layoutManager instanceof GridLayoutManager) {
                    GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                    firstCompleteVisibleItemPosition = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
                    lastCompleteVisibleItemPosition = gridLayoutManager.findLastCompletelyVisibleItemPosition();
                } else if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                    firstCompleteVisibleItemPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    lastCompleteVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                }

                String message = "";

                if (lastCompleteVisibleItemPosition == (totalItemCount - 1 - 2)) {
                    Log.d("YYY", "lastCompleteVisibleItemPosition : " + lastCompleteVisibleItemPosition);
                    Log.d("YYY", "totalItemCount : " + (totalItemCount - 1));
                    if (dy > 0 || dx > 0) {

                        if (dy > 0) {
                            Log.d("YYY", "Scroll to top");
                            loadReviewImageDataMore(false, myblogReviewImageItemList, myblogReviewImageRecyclerViewDataAdapter);
                        }


                        if (dx > 0) {
                            Log.d("YYY", "Scroll to left");
                            loadReviewImageDataMore(false, myblogReviewImageItemList, myblogReviewImageRecyclerViewDataAdapter);
                        }
                    }
                }
            }

        });
    }

    private void loadReviewImageDataMore(final boolean insertDataAtBeginning, final List<MyblogReviewImageItem> myblogReviewImageItemList, final MyblogReviewImageRecyclerViewDataAdapter myblogReviewImageRecyclerViewDataAdapter) {
        Log.d("YYY", "loadData");

        handler.post(new Runnable() {
            @Override
            public void run() {
                int currItemListSize = myblogReviewImageItemList.size();
                int newItemIndex = 0;
                int loadMoreItemCount = 5;

                // Only add one RecyclerView item.
                for (int i = currItemListSize; i < currItemListSize + loadMoreItemCount; i++) {

                    String newViewItem = "https://www.lwt.co.kr/datas/factory/main_img/006059";


                    if (insertDataAtBeginning) {
//                        myblogReviewImageItemList.add(i - currItemListSize, newViewItem);
                        newItemIndex = 0;
                    } else {
                        myblogReviewImageItemList.add(new MyblogReviewImageItem("id", newViewItem));
                        newItemIndex = myblogReviewImageItemList.size() - 1;
                    }

                }

                Log.d("YYY", "loadData END");
                myblogReviewImageRecyclerViewDataAdapter.notifyDataSetChanged();
            }
        });

//        mContext.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                int currItemListSize = myblogReviewImageItemList.size();
//                int newItemIndex = 0;
//                int loadMoreItemCount = 5;
//
//                // Only add one RecyclerView item.
//                for (int i = currItemListSize; i < currItemListSize + loadMoreItemCount; i++) {
//
//                    String newViewItem = "https://www.lwt.co.kr/datas/factory/main_img/006059";
//
//
//                    if (insertDataAtBeginning) {
////                        myblogReviewImageItemList.add(i - currItemListSize, newViewItem);
//                        newItemIndex = 0;
//                    } else {
//                        myblogReviewImageItemList.add(new MyblogReviewImageItem("id", newViewItem));
//                        newItemIndex = myblogReviewImageItemList.size() - 1;
//                    }
//
//                }
//
//                Log.d("YYY", "loadData END");
//                myblogReviewImageRecyclerViewDataAdapter.notifyDataSetChanged();
//            }
//        });
    }


    private void setRecyler() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        myReviewRecyclerView = (RecyclerView) rootView.findViewById(R.id.myblog_review_recycler);
        myReviewRecyclerView.setLayoutManager(layoutManager);
        myReviewRecyclerViewAdapter = new MyReviewRecyclerViewAdapter(myReviewItemList);
        myReviewRecyclerView.setAdapter(myReviewRecyclerViewAdapter);
        myReviewRecyclerView.setFocusable(false);
        myReviewRecyclerView.setHasFixedSize(true);
        myReviewRecyclerView.addItemDecoration(CommonUtil.getInstance().new SpacesItemDecoration(0, 0, 30, 0));
    }

    private void setDummyData(){

        if(myReviewItemList == null)
            myReviewItemList = new ArrayList<MyReviewItem>();

        myblogReviewImageItemList = new ArrayList<>();

        myblogReviewImageItemList.add(new MyblogReviewImageItem("id","https://pbs.twimg.com/profile_images/794909411789574144/aabzetJx.jpg"));
        myblogReviewImageItemList.add(new MyblogReviewImageItem("id","https://pbs.twimg.com/profile_images/794909411789574144/aabzetJx.jpg"));
        myblogReviewImageItemList.add(new MyblogReviewImageItem("id","https://pbs.twimg.com/profile_images/794909411789574144/aabzetJx.jpg"));
        myblogReviewImageItemList.add(new MyblogReviewImageItem("id","https://pbs.twimg.com/profile_images/794909411789574144/aabzetJx.jpg"));
        myblogReviewImageItemList.add(new MyblogReviewImageItem("id","https://pbs.twimg.com/profile_images/794909411789574144/aabzetJx.jpg"));
        myblogReviewImageItemList.add(new MyblogReviewImageItem("id","https://pbs.twimg.com/profile_images/794909411789574144/aabzetJx.jpg"));
        myblogReviewImageItemList.add(new MyblogReviewImageItem("id","https://pbs.twimg.com/profile_images/794909411789574144/aabzetJx.jpg"));
        myblogReviewImageItemList.add(new MyblogReviewImageItem("id","https://pbs.twimg.com/profile_images/794909411789574144/aabzetJx.jpg"));
        //
        myReviewItemList.add(new MyReviewItem("http://image.chosun.com/sitedata/image/201711/28/2017112802484_0.jpg", "", "", "", "", myblogReviewImageItemList));


        myblogReviewImageItemList = new ArrayList<>(); //새로 생성하는거 엄청 중요!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        myblogReviewImageItemList.add(new MyblogReviewImageItem("id","http://img.sbs.co.kr/newimg/news/20170622/201061239_1280.jpg"));
        myblogReviewImageItemList.add(new MyblogReviewImageItem("id","http://img.sbs.co.kr/newimg/news/20170622/201061239_1280.jpg"));
        myblogReviewImageItemList.add(new MyblogReviewImageItem("id","http://img.sbs.co.kr/newimg/news/20170622/201061239_1280.jpg"));
        myblogReviewImageItemList.add(new MyblogReviewImageItem("id","http://img.sbs.co.kr/newimg/news/20170622/201061239_1280.jpg"));
        myblogReviewImageItemList.add(new MyblogReviewImageItem("id","http://img.sbs.co.kr/newimg/news/20170622/201061239_1280.jpg"));
        myblogReviewImageItemList.add(new MyblogReviewImageItem("id","http://img.sbs.co.kr/newimg/news/20170622/201061239_1280.jpg"));
        //
        myReviewItemList.add(new MyReviewItem("http://image.chosun.com/sitedata/image/201711/28/2017112802484_0.jpg", "", "", "", "", myblogReviewImageItemList));


        handler.post(new Runnable() {
            @Override
            public void run() {
                myReviewRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }
}