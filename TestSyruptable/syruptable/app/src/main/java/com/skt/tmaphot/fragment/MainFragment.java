package com.skt.tmaphot.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.skt.tmaphot.MainApplication;
import com.skt.tmaphot.R;
import com.skt.tmaphot.activity.IRecyclerItem;
import com.skt.tmaphot.activity.IRecyclerViewDataAdapter;
import com.skt.tmaphot.activity.main.banner.RollingAdapter;
import com.skt.tmaphot.activity.main.banner.RollingAutoManager;
import com.skt.tmaphot.activity.main.banner.RollingIndicatorView;
import com.skt.tmaphot.activity.main.banner.RollingModel;
import com.skt.tmaphot.activity.main.coupon.CouponRecyclerViewDataAdapter;
import com.skt.tmaphot.activity.main.coupon.CouponRecyclerViewItem;
import com.skt.tmaphot.activity.main.coupon.more.CouponListActivity;
import com.skt.tmaphot.activity.main.hotdeal.HotdealRecyclerViewDataAdapter;
import com.skt.tmaphot.activity.main.hotdeal.HotdealRecyclerViewItem;
import com.skt.tmaphot.activity.main.hotdeal.more.HotdealListActivity;
import com.skt.tmaphot.activity.main.hotplace.ExpandableHeightGridView;
import com.skt.tmaphot.activity.main.hotplace.HotplaceGridAdapter;
import com.skt.tmaphot.activity.main.hotplace.HotplaceGridViewItem;
import com.skt.tmaphot.activity.main.menu.MainMenuRecyclerViewDataAdapter;
import com.skt.tmaphot.activity.main.menu.MainMenuRecyclerViewHolder;
import com.skt.tmaphot.activity.main.menu.MainMenuRecyclerViewItem;
import com.skt.tmaphot.activity.main.review.RealReviewRecyclerViewDataAdapter;
import com.skt.tmaphot.activity.main.review.RealReviewRecyclerViewItem;
import com.skt.tmaphot.activity.main.review.more.RealReviewActivity;
import com.skt.tmaphot.activity.main.store.StoreInfoActivity;
import com.skt.tmaphot.common.CommonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainFragment extends Fragment {

    public int MESSAGE_REVIEW_COUPON_NOTIFY = 1004;
    public int MESSAGE_HOTPLACE_NOTIFY = 1004;
    public int MESSAGE_RECYLER_NOTIFY = 1004;



    private NestedScrollView nestedScrollView;

    // 리얼리뷰
    private RecyclerView realReviewRecyclerView;
    private ArrayList<IRecyclerItem> realReviewItemList;
    private RealReviewRecyclerViewDataAdapter realReviewRecyclerViewDataAdapter;
    private ProgressBar realReviewLoadingProgressBar;

    // 주변 할인 쿠폰
    private RecyclerView couponRecyclerView;
    private ArrayList<IRecyclerItem> couponItemList;
    private CouponRecyclerViewDataAdapter couponRecyclerViewDataAdapter;
    private ProgressBar couponLoadingProgressBar;

    // 주변 할딧 맛집
    private RecyclerView hotdealRecyclerView;
    private ArrayList<IRecyclerItem> hotdealItemList;
    private HotdealRecyclerViewDataAdapter hotdealRecyclerViewDataAdapter;
    private ProgressBar hotdealLoadingProgressBar;

    // 핫플레이스
    private List<HotplaceGridViewItem> hotplace_Pop_ItemList, hotplace_Distance_ItemList;
    private ExpandableHeightGridView hotplaceGridview;
    private boolean ishotplaceItemLoad;
    private HotplaceGridAdapter hotplaceGridAdapter;
    private boolean ishotplaceModeDistance;

    // 메뉴
    private RecyclerView menuRecyclerView;
    private ArrayList<MainMenuRecyclerViewItem> mainMenuRecyclerViewItems;
    private MainMenuRecyclerViewDataAdapter menuRecyclerViewDataAdapter;

    ///////////////////배너/////////////////
    private RollingAdapter rollingAdapter;
    private RollingAutoManager rollingAutoManager;
    private ViewPager rollingViewPager;
    private RollingIndicatorView rollingIndicatorView;
    private TextView rollingTextView;
    private ImageView searchbarImgView;
    //더보기
    private TextView reviewMoreTextView, couponMoreTextview, hotdealMoreTextview;
    private View view;



    private TextView hotplace_pop, hotplace_distance;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        view = inflater.inflate(R.layout.fragment_main_layout, container, false);

        //초기 View 세팅
        initView();

        //GPS 주소변환// 임시
        initGPSTransferAddress();

        //banner
        initEventBannerSet();

        // 주변할인 쿠폰
        initRecylerViewSet(couponRecyclerView, 100);

        // 리얼후기
        initRecylerViewSet(realReviewRecyclerView, 100);

        // 주변 핫딜 맛집
        initRecylerViewSet(hotdealRecyclerView, 100);

        // 메뉴
        initRecylerViewSet(menuRecyclerView, 100);

        // 핫플레이스
        initHotplaceSet();

        return view;
    }


    private void initRecylerViewSet(final RecyclerView recyclerView, final int position) {


        getActivity().runOnUiThread(

        (new Runnable() {
            @Override
            public void run() {

                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(layoutManager);
                if(menuRecyclerViewDataAdapter == null)
                    recyclerView.addItemDecoration(CommonUtil.getInstance().new SpacesItemDecoration(0, 15, 0, 0));

                switch (recyclerView.getId()) {

                    case R.id.menu_recycler_view:

                        if (mainMenuRecyclerViewItems == null)
                            mainMenuRecyclerViewItems = new ArrayList<MainMenuRecyclerViewItem>();

                        mainMenuRecyclerViewItems.add(new MainMenuRecyclerViewItem("55", "", R.drawable.img_main_menu_1));
                        mainMenuRecyclerViewItems.add(new MainMenuRecyclerViewItem("88", "", R.drawable.img_main_menu_2));
                        mainMenuRecyclerViewItems.add(new MainMenuRecyclerViewItem("22", "", R.drawable.img_main_menu_3));
                        mainMenuRecyclerViewItems.add(new MainMenuRecyclerViewItem("222", "", R.drawable.img_main_menu_4));
                        mainMenuRecyclerViewItems.add(new MainMenuRecyclerViewItem("545", "", R.drawable.img_main_menu_5));
                        mainMenuRecyclerViewItems.add(new MainMenuRecyclerViewItem("235235", "", R.drawable.img_main_menu_6));
                        mainMenuRecyclerViewItems.add(new MainMenuRecyclerViewItem("2355", "", R.drawable.img_main_menu_7));
                        mainMenuRecyclerViewItems.add(new MainMenuRecyclerViewItem("23525", "", R.drawable.img_main_menu_8));

                        menuRecyclerViewDataAdapter = new MainMenuRecyclerViewDataAdapter(getActivity(), mainMenuRecyclerViewItems);
                        menuRecyclerViewDataAdapter.setOnMenuCilckListener(onMenuCilckListener); // 메뉴 리스너 등록
                        recyclerView.setAdapter(menuRecyclerViewDataAdapter);
      //                loadData(false, recyclerView);
                        recyclerView.addItemDecoration(CommonUtil.getInstance().new SpacesItemDecoration(0));
                        break;

                    case R.id.coupon_recycler_view:

                        couponItemList = new ArrayList<IRecyclerItem>();

                        couponRecyclerViewDataAdapter = new CouponRecyclerViewDataAdapter(getActivity(), couponItemList);
                        recyclerView.setAdapter(couponRecyclerViewDataAdapter);
                        loadData(position, recyclerView);
                        break;


                    case R.id.review_recycler_view:


                        realReviewItemList = new ArrayList<IRecyclerItem>();

                        realReviewRecyclerViewDataAdapter = new RealReviewRecyclerViewDataAdapter(getActivity(), realReviewItemList);
                        recyclerView.setAdapter(realReviewRecyclerViewDataAdapter);
                        loadData(position, recyclerView);
                        break;


                    case R.id.hotdeal_recycler_view:

                        hotdealItemList = new ArrayList<IRecyclerItem>();

                        hotdealRecyclerViewDataAdapter = new HotdealRecyclerViewDataAdapter(getActivity(), hotdealItemList);
                        recyclerView.setAdapter(hotdealRecyclerViewDataAdapter);
                        loadData(position, recyclerView);
                        break;
                }

                // Scroll RecyclerView a little to make later scroll take effect.
                recyclerView.scrollToPosition(0);
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        Log.d("RRR", "onScrolled");

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

                        // Means scroll at beginning ( top to bottom or left to right).
//                    if (firstCompleteVisibleItemPosition == 0) {
//                        // dy < 0 means scroll to bottom, dx < 0 means scroll to right at beginning.
//                        if (dy < 0 || dx < 0) {
//                            // Means scroll to bottom.
//                            if (dy < 0) {
//                                loadData(true);
//                            }
//
//                            // Means scroll to right.
//                            if (dx < 0) {
//                                loadData(true);
//                            }
//                        }
//                    }
//                     Means scroll at ending ( bottom to top or right to left )
                        ////////////////////////////// 미리 로드하기 위해서 꼼 수를 써보자 마지막이 아니라, 앞전에
                        if (lastCompleteVisibleItemPosition == (totalItemCount - 1 - 2)) {
                            Log.d("RRR", "lastCompleteVisibleItemPosition");
                            // dy > 0 means scroll to up, dx > 0 means scroll to left at ending.
                            if (dy > 0 || dx > 0) {
                                // Scroll to top
//                        Log.d("RRR","Scroll to top");
//                        if (dy > 0) {
//                            loadData(false, recyclerView);
//                        }

                                // Scroll to left
                                if (dx > 0) {
                                    Log.d("ABCD", "Scroll to left");
                                    loadData(position, recyclerView);
                                }
                            }
                        }

                        if (message.length() > 0) {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        }));


    }

    private class CustomRunnable implements Runnable{

        ArrayList<IRecyclerItem> iRecyclerItems;
        IRecyclerViewDataAdapter iRecyclerViewDataAdapter;

        public CustomRunnable(ArrayList<IRecyclerItem> iRecyclerItems){
            this.iRecyclerItems = iRecyclerItems;
        }

        public CustomRunnable(IRecyclerViewDataAdapter iRecyclerViewDataAdapter){
            this.iRecyclerViewDataAdapter = iRecyclerViewDataAdapter;
        }

        @Override
        public void run(){ }

    }

    private void loadData(final int position, final RecyclerView recyclerView) {
        if (recyclerView.getId() == R.id.menu_recycler_view) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                ArrayList<IRecyclerItem> iRecyclerItems = null;
                IRecyclerViewDataAdapter iRecyclerViewDataAdapter = null;

                int currItemListSize = 0;   // 현재 리스트 사이즈
                int newItemIndex = 0;       // 아이템 끝 index
                int loadMoreItemCount = 0;  // 로드할 아이템 개수
                int id = recyclerView.getId();

                switch (recyclerView.getId()) {

                    case R.id.coupon_recycler_view:
                        iRecyclerItems = couponItemList;
                        loadMoreItemCount = 5;
                        iRecyclerViewDataAdapter = couponRecyclerViewDataAdapter;
                        break;

                    case R.id.review_recycler_view:
                        iRecyclerItems = realReviewItemList;
                        loadMoreItemCount = 5;
                        iRecyclerViewDataAdapter = realReviewRecyclerViewDataAdapter;
                        break;

                    case R.id.hotdeal_recycler_view:
                        iRecyclerItems = hotdealItemList;
                        loadMoreItemCount = 5;
                        iRecyclerViewDataAdapter = hotdealRecyclerViewDataAdapter;
                        break;
                }


                currItemListSize = iRecyclerItems.size();
                IRecyclerItem newViewItem = null;


//                if (insertDataNewBeginning)
//                {
//                    view.post(new CustomRunnable(iRecyclerItems){
//
//                        @Override
//                        public void run(){
//                            this.iRecyclerItems.clear();
//                        }
//                    });

//                    iRecyclerItems = null;
//                    currItemListSize = 0;
//                }



                Log.d("ABCD", "currItemListSize: " +  currItemListSize);
                Log.d("ABCD", "currItemListSize + currItemListSize: " +  currItemListSize + loadMoreItemCount);

                for (int i = currItemListSize; i < currItemListSize + loadMoreItemCount; i++) {
                    Log.d("ABCD", "i: " +  i);


                    // 임시 더미 데이터 및 메뉴 클릭시 테스트 아직 정보가 없으니 갱신만 테스트

                    if (position == 100) {

                        if(id == R.id.coupon_recycler_view)
                        {
                            int temp = 10 + i;
                            newViewItem = new CouponRecyclerViewItem("http://img.kormedi.com/news/article/__icsFiles/artimage/2015/05/23/c_km601/432212_540.jpg",
                                    "황제짜장", "수제피자", "200m", String.valueOf(temp));
                        }

                        if(id == R.id.review_recycler_view)
                        {
                            newViewItem = new RealReviewRecyclerViewItem("https://www.lwt.co.kr/datas/factory/main_img/006059");
                        }

                        if(id == R.id.hotdeal_recycler_view)
                        {
                            newViewItem = new HotdealRecyclerViewItem("http://trendinsight.biz/wp-content/uploads/2014/05/file291298583404-1024x682.jpg",
                                    "황제짜장", "수제피자", "200m", "50%");
                        }

                    }


                    if (position == 0) {

                        if(id == R.id.review_recycler_view)
                        {
                            Log.d("ABCD","data sdfsdfsdf");
                            newViewItem = new RealReviewRecyclerViewItem("https://i.ytimg.com/vi/SZvTL5A_tNQ/maxresdefault.jpg");
                        }

                        if(id == R.id.hotdeal_recycler_view)
                        {
                            newViewItem = new HotdealRecyclerViewItem("http://4.bp.blogspot.com/-R29WyCcMomw/VIq-UhEndUI/AAAAAAAAAdo/vJZBIUKUAto/s1600/%EC%88%98%EC%A7%801.jpg",
                                    "황제짜장", "수제피자", "200m", "50%");
                        }

                    }


                    if (position == 1) {

                        if(id == R.id.review_recycler_view)
                        {
                            Log.d("ABCD","data sdfsdfsdf");
                            newViewItem = new RealReviewRecyclerViewItem("http://cfile10.uf.tistory.com/image/266D213B56A51A172FC174");
                        }

                        if(id == R.id.hotdeal_recycler_view)
                        {
                            newViewItem = new HotdealRecyclerViewItem("https://fimg4.pann.com/new/download.jsp?FileID=42599762",
                                    "황제짜장", "수제피자", "200m", "50%");
                        }

                    }


                    iRecyclerItems.add(newViewItem);
                }
                newItemIndex = iRecyclerItems.size() - 1;


                view.post(new CustomRunnable(iRecyclerViewDataAdapter){

                    @Override
                    public void run(){
                        this.iRecyclerViewDataAdapter.notifyData();
                    }
                });
            }
        }).start();
        Log.d("ABCD","Thead start");
    }


    private void initEventBannerSet() {

        rollingTextView.post(new Runnable() {
            @Override
            public void run() {
                rollingTextView.setSingleLine(false);
                rollingTextView.setText(Html.fromHtml("어서오세요.<br/><b>여름에 가야할 새로운 매장!</b>"));
                rollingTextView.setTextColor(Color.WHITE);
                rollingTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            }
        });


        rollingAdapter = new RollingAdapter(getActivity(), setEventBannerData(), new RollingAdapter.OnAdapterItemClickListener() {
            @Override
            public void onItemClick(RollingModel object, int position) {
                Toast.makeText(getActivity(), position + " items click!", Toast.LENGTH_SHORT).show();
            }
        });

        rollingViewPager.setAdapter(rollingAdapter);
        rollingIndicatorView.setViewPager(rollingViewPager);
        rollingAutoManager = new RollingAutoManager(rollingViewPager, rollingAdapter, rollingIndicatorView);
        rollingAutoManager.setRollingTime(5500);
    }

    private List<RollingModel> setEventBannerData() {

        List<RollingModel> list = new ArrayList<>();
        list.add(new RollingModel("1", "https://media-cdn.tripadvisor.com/media/photo-s/0b/ef/52/df/caption.jpg"));
        list.add(new RollingModel("2", "http://samtanartmine.com/img/cont8_8.jpg"));
        list.add(new RollingModel("3", "https://img.siksinhot.com/place/1480735356478788.jpg?w=307&h=300&c=Y"));
        list.add(new RollingModel("4", "http://magazine.hankyung.com/magazinedata/images/photo/201402/2dd2a176d727991d2fc7fd59d7449899.jpg"));
        return list;
    }

    private void initGPSTransferAddress() {
        Intent intent = getActivity().getIntent();
        String cityName = null;

        Geocoder gcd = new Geocoder(getActivity(), Locale.getDefault());

        List<Address> addresses = null;

        try {

            addresses = gcd.getFromLocation(intent.getDoubleExtra("latitude", 0), intent.getDoubleExtra("longitude", 0), 1);

            if (addresses.size() > 0)

//            Log.d("GEO", addresses.get(0).getLocality());   // 구 메인,
//            Log.d("GEO", addresses.get(0).getAddressLine(0).toString());    // 국가, 시 군 구 동 번지
//            Log.d("GEO", addresses.get(0).getAdminArea());  //시
//            Log.d("GEO", addresses.get(0).getThoroughfare());   //동


//            String tt = addresses.get(0).getAddressLine(0).toString();
//            String result = tt.substring(tt.star(" "), tt.lastIndexOf(" "));

                //서울시 강남구 삼성동
                //하남시 덕풍동

                Log.d("GEO", addresses.get(0).getAdminArea() + " " + addresses.get(0).getSubLocality() + " " + addresses.get(0).getThoroughfare());   //동


        } catch (IOException e) {

            e.printStackTrace();

        }

        //임시 테스트중
        String t = null;
        if (addresses.get(0).getSubLocality() != null) {
            t = addresses.get(0).getSubLocality();   //구
        } else {
            t = addresses.get(0).getAdminArea();   //시
        }

        String tt = addresses.get(0).getThoroughfare();     //동
        TextView mGPSTextView = (TextView) view.findViewById(R.id.main_text_gps);
        MainApplication.LOCATION_ADDRESS = t + " " + tt + "▼";
        mGPSTextView.setText(MainApplication.LOCATION_ADDRESS);

    }

    private void initView() {

        nestedScrollView = (NestedScrollView) view.findViewById(R.id.main_nestedscrollview);
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

                if (scrollY == ((v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()))) {
                    Log.i("TAAA", "BOTTOM SCROLL");
                    loadHotplaceItem(ishotplaceModeDistance);
                }
            }
        });

        // 아직 할지 말지 모름 여기는 text 서버에서 받아오는건지.. 임시
        rollingTextView = (TextView) view.findViewById(R.id.main_text_banner);
        rollingIndicatorView = (RollingIndicatorView) view.findViewById(R.id.indicator_view);
        rollingViewPager = (ViewPager) view.findViewById(R.id.viewPager);

        searchbarImgView = (ImageView) view.findViewById(R.id.main_img_searchbar);
//        searchbarImgView.setScaleType(ImageView.ScaleType.FIT_XY);
        //searchbar
        MainApplication.loadImage(getActivity(), R.drawable.img_main_searchbar, searchbarImgView);

        realReviewRecyclerView = (RecyclerView) view.findViewById(R.id.review_recycler_view);
        realReviewLoadingProgressBar = (ProgressBar) view.findViewById(R.id.review_recycler_view_progressbar);

        couponRecyclerView = (RecyclerView) view.findViewById(R.id.coupon_recycler_view);
        couponLoadingProgressBar = (ProgressBar) view.findViewById(R.id.coupon_recycler_view_progressbar);

        hotdealRecyclerView = (RecyclerView) view.findViewById(R.id.hotdeal_recycler_view);
        menuRecyclerView = (RecyclerView) view.findViewById(R.id.menu_recycler_view);
        hotplaceGridview = (ExpandableHeightGridView) view.findViewById(R.id.hotplace_gridview);


        reviewMoreTextView = (TextView) view.findViewById(R.id.main_txt_review_more);
        reviewMoreTextView.setOnClickListener(onClickListenerMore);

        couponMoreTextview = (TextView) view.findViewById(R.id.main_txt_coupon_more);
        couponMoreTextview.setOnClickListener(onClickListenerMore);

        hotdealMoreTextview = (TextView) view.findViewById(R.id.main_txt_hotdeal_more);
        hotdealMoreTextview.setOnClickListener(onClickListenerMore);

        hotplace_pop = (TextView) view.findViewById(R.id.main_hotplace_pop);
        hotplace_pop.setOnClickListener(onClickListenerHotplaceType);

        hotplace_distance = (TextView) view.findViewById(R.id.main_hotplace_distance);
        hotplace_distance.setOnClickListener(onClickListenerHotplaceType);

    }

    private void initHotplaceSet() {

        hotplaceGridview.setExpanded(true);
        hotplaceGridview.setOnItemClickListener(onItemClickListenerHotplace);

        if (hotplace_Pop_ItemList == null)
            hotplace_Pop_ItemList = new ArrayList<HotplaceGridViewItem>();

        hotplaceGridAdapter = new HotplaceGridAdapter(getContext(), hotplace_Pop_ItemList);
        hotplaceGridview.setAdapter(hotplaceGridAdapter);
        hotplaceGridview.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub

                Log.e("GridView", "onScrollStateChanged : " + scrollState);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
                Log.e("GridView", "firstVisibleItem: " + firstVisibleItem + "\nvisibleItemCount: " + visibleItemCount + "\ntotalItemCount: " + totalItemCount);

                // 현재 가장 처음에 보이는 셀번호와 보여지는 셀번호를 더한값이
                // 전체의 숫자와 동일해지면 가장 아래로 스크롤 되었다고 가정합니다.
                int count = totalItemCount - visibleItemCount;


                if (firstVisibleItem >= count && totalItemCount != 0 && ishotplaceItemLoad == false) {
                    //loadGridItem();
                    Log.e("GridView", "XXXXXXXXX loadGridItem : ");
                }
            }
        });

        //데이터 로드
        loadHotplaceItem(ishotplaceModeDistance);
    }

    private void loadHotplaceItem(final boolean mode) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                ishotplaceItemLoad = true;

                if (!mode) {

                    Log.d("QQQ", "pop dataload");
                    hotplace_Pop_ItemList.add(new HotplaceGridViewItem("http://trendinsight.biz/wp-content/uploads/2014/05/file291298583404-1024x682.jpg",
                            "몰라짜장", "수제피자", "200m", "50%", "맛이어요"));
                    hotplace_Pop_ItemList.add(new HotplaceGridViewItem("http://trendinsight.biz/wp-content/uploads/2014/05/file291298583404-1024x682.jpg",
                            "황제짜장", "수제피자", "200m", "50%", "맛이어요"));

                    hotplace_Pop_ItemList.add(new HotplaceGridViewItem("http://trendinsight.biz/wp-content/uploads/2014/05/file291298583404-1024x682.jpg",
                            "황제짜장", "수제피자", "200m", "50%", "맛이어요"));

                    hotplace_Pop_ItemList.add(new HotplaceGridViewItem("http://trendinsight.biz/wp-content/uploads/2014/05/file291298583404-1024x682.jpg",
                            "황제짜장", "수제피자", "200m", "50%", "맛이어요"));
                    hotplace_Pop_ItemList.add(new HotplaceGridViewItem("http://trendinsight.biz/wp-content/uploads/2014/05/file291298583404-1024x682.jpg",
                            "황제짜장", "수제피자", "200m", "50%", "맛이어요"));

                    hotplace_Pop_ItemList.add(new HotplaceGridViewItem("http://trendinsight.biz/wp-content/uploads/2014/05/file291298583404-1024x682.jpg",
                            "황제짜장", "수제피자", "200m", "50%", "맛이어요"));

                    hotplace_Pop_ItemList.add(new HotplaceGridViewItem("http://trendinsight.biz/wp-content/uploads/2014/05/file291298583404-1024x682.jpg",
                            "황제짜장", "수제피자", "200m", "50%", "맛이어요"));

                    hotplace_Pop_ItemList.add(new HotplaceGridViewItem("http://trendinsight.biz/wp-content/uploads/2014/05/file291298583404-1024x682.jpg",
                            "황제짜장", "수제피자", "200m", "50%", "맛이어요"));

                    hotplace_Pop_ItemList.add(new HotplaceGridViewItem("http://trendinsight.biz/wp-content/uploads/2014/05/file291298583404-1024x682.jpg",
                            "황제짜장", "수제피자", "200m", "50%", "맛이어요"));
                    hotplace_Pop_ItemList.add(new HotplaceGridViewItem("http://trendinsight.biz/wp-content/uploads/2014/05/file291298583404-1024x682.jpg",
                            "황제짜장", "수제피자", "200m", "50%", "맛이어요"));
                } else {

                    Log.d("QQQ", "distance dataload");

                    hotplace_Distance_ItemList.add(new HotplaceGridViewItem("http://spnimage.edaily.co.kr/images/photo/files/NP/S/2016/10/PS16102100005.jpg",
                            "황제짜장", "수제피자", "200m", "50%", "맛이어요"));
                    hotplace_Distance_ItemList.add(new HotplaceGridViewItem("http://spnimage.edaily.co.kr/images/photo/files/NP/S/2016/10/PS16102100005.jpg",
                            "황제짜장", "수제피자", "200m", "50%", "맛이어요"));
                    hotplace_Distance_ItemList.add(new HotplaceGridViewItem("http://spnimage.edaily.co.kr/images/photo/files/NP/S/2016/10/PS16102100005.jpg",
                            "황제짜장", "수제피자", "200m", "50%", "맛이어요"));
                    hotplace_Distance_ItemList.add(new HotplaceGridViewItem("http://spnimage.edaily.co.kr/images/photo/files/NP/S/2016/10/PS16102100005.jpg",
                            "황제짜장", "수제피자", "200m", "50%", "맛이어요"));
                    hotplace_Distance_ItemList.add(new HotplaceGridViewItem("http://spnimage.edaily.co.kr/images/photo/files/NP/S/2016/10/PS16102100005.jpg",
                            "황제짜장", "수제피자", "200m", "50%", "맛이어요"));
                    hotplace_Distance_ItemList.add(new HotplaceGridViewItem("http://spnimage.edaily.co.kr/images/photo/files/NP/S/2016/10/PS16102100005.jpg",
                            "황제짜장", "수제피자", "200m", "50%", "맛이어요"));
                    hotplace_Distance_ItemList.add(new HotplaceGridViewItem("http://spnimage.edaily.co.kr/images/photo/files/NP/S/2016/10/PS16102100005.jpg",
                            "황제짜장", "수제피자", "200m", "50%", "맛이어요"));
                    hotplace_Distance_ItemList.add(new HotplaceGridViewItem("http://spnimage.edaily.co.kr/images/photo/files/NP/S/2016/10/PS16102100005.jpg",
                            "황제짜장", "수제피자", "200m", "50%", "맛이어요"));
                    hotplace_Distance_ItemList.add(new HotplaceGridViewItem("http://spnimage.edaily.co.kr/images/photo/files/NP/S/2016/10/PS16102100005.jpg",
                            "황제짜장", "수제피자", "200m", "50%", "맛이어요"));
                    hotplace_Distance_ItemList.add(new HotplaceGridViewItem("http://spnimage.edaily.co.kr/images/photo/files/NP/S/2016/10/PS16102100005.jpg",
                            "황제짜장", "수제피자", "200m", "50%", "맛이어요"));
                    hotplace_Distance_ItemList.add(new HotplaceGridViewItem("http://spnimage.edaily.co.kr/images/photo/files/NP/S/2016/10/PS16102100005.jpg",
                            "황제짜장", "수제피자", "200m", "50%", "맛이어요"));
                    hotplace_Distance_ItemList.add(new HotplaceGridViewItem("http://spnimage.edaily.co.kr/images/photo/files/NP/S/2016/10/PS16102100005.jpg",
                            "황제짜장", "수제피자", "200m", "50%", "맛이어요"));
                    hotplace_Distance_ItemList.add(new HotplaceGridViewItem("http://spnimage.edaily.co.kr/images/photo/files/NP/S/2016/10/PS16102100005.jpg",
                            "황제짜장", "수제피자", "200m", "50%", "맛이어요"));
                    hotplace_Distance_ItemList.add(new HotplaceGridViewItem("http://spnimage.edaily.co.kr/images/photo/files/NP/S/2016/10/PS16102100005.jpg",
                            "황제짜장", "수제피자", "200m", "50%", "맛이어요"));
                    hotplace_Distance_ItemList.add(new HotplaceGridViewItem("http://spnimage.edaily.co.kr/images/photo/files/NP/S/2016/10/PS16102100005.jpg",
                            "황제짜장", "수제피자", "200m", "50%", "맛이어요"));
                    hotplace_Distance_ItemList.add(new HotplaceGridViewItem("http://spnimage.edaily.co.kr/images/photo/files/NP/S/2016/10/PS16102100005.jpg",
                            "황제짜장", "수제피자", "200m", "50%", "맛이어요"));

                }

                ishotplaceItemLoad = false;
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        hotplaceGridAdapter.notifyDataSetChanged();
                    }
                });

                hotplaceGridview.setFocusable(false);
            }
        }).start();
    }

    private void reloadHotPlace(boolean mode) {
        if (mode) {
            hotplace_Distance_ItemList = new ArrayList<HotplaceGridViewItem>();
            hotplaceGridview.invalidateViews();
            hotplaceGridAdapter = new HotplaceGridAdapter(getActivity(), hotplace_Distance_ItemList);
            hotplaceGridview.setAdapter(hotplaceGridAdapter);

            loadHotplaceItem(mode);
        } else {

            hotplace_Pop_ItemList = new ArrayList<HotplaceGridViewItem>();
            hotplaceGridview.invalidateViews();
            hotplaceGridAdapter = new HotplaceGridAdapter(getActivity(), hotplace_Pop_ItemList);
            hotplaceGridview.setAdapter(hotplaceGridAdapter);

            loadHotplaceItem(mode);
        }
    }


    View.OnClickListener onClickListenerMore = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.main_txt_coupon_more:
                    startActivity(new Intent(getActivity(), CouponListActivity.class));
                    break;
                case R.id.main_txt_review_more:
                    startActivity(new Intent(getActivity(), RealReviewActivity.class));
                    break;
                case R.id.main_txt_hotdeal_more:
                    startActivity(new Intent(getActivity(), HotdealListActivity.class));
                    break;
            }
        }
    };

    View.OnClickListener onClickListenerHotplaceType = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Log.d("ABC", "click");

            view.setSelected(!view.isSelected());
//            view.setFocusable(!view.isSelected());
            switch (view.getId()) {
                case R.id.main_hotplace_pop:
                    ishotplaceModeDistance = false;
                    reloadHotPlace(ishotplaceModeDistance);
                    break;
                case R.id.main_hotplace_distance:
                    ishotplaceModeDistance = true;
                    reloadHotPlace(ishotplaceModeDistance);
                    break;
            }
        }
    };


    AdapterView.OnItemClickListener onItemClickListenerHotplace = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            //이렇게 아이디 등 값을 가져와서 전달한다.
            //TextView tv = (TextView)view.findViewById(R.id.hotplace_grid_txt_title);
            Intent intent = new Intent(getActivity(), StoreInfoActivity.class);
            Bundle bundle = new Bundle();
            getActivity().startActivity(intent);
        }
    };

    MainMenuRecyclerViewHolder.OnMenuCilckListener onMenuCilckListener = new MainMenuRecyclerViewHolder.OnMenuCilckListener() {
        @Override
        public void menuOnClick(View v, int id, int position) {
            // 임시구현
//            Toast.makeText(getContext(), "position : " + position, Toast.LENGTH_SHORT).show();

            Log.d("ABCD","CLICKKKKKKKKKKKKKKKKKKKKKKKKK");


                    initRecylerViewSet(realReviewRecyclerView, position);
                    initRecylerViewSet(hotdealRecyclerView, position);

//            loadData(true, realReviewRecyclerView);
//            loadData(true, hotdealRecyclerView);
                    loadHotplaceItem(ishotplaceItemLoad);


        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        rollingAutoManager.onRollingStart();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();
        rollingAutoManager.onRollingStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rollingAutoManager.onRollingDestroy();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }
}
