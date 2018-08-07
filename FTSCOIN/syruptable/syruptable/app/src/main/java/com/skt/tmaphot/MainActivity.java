package com.skt.tmaphot;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.skt.tmaphot.activity.bottom.EventFragment;
import com.skt.tmaphot.activity.bottom.MainFragment;
import com.skt.tmaphot.activity.bottom.MyBlogFragment;
import com.skt.tmaphot.activity.bottom.RealReviewFragment;
import com.skt.tmaphot.activity.bottom.ShopFragment;
import com.skt.tmaphot.location.GPSData;
import com.skt.tmaphot.location.GPSTracker;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends BaseActivity {

    private FragmentManager fragmentManager;
    private DrawerLayout drawer;
    public BottomNavigationView navigation;
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    private TextView navi_shop;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ABCDE", "MainActivity onCreate");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main_layout);
        baceContext = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        topAppbarText = (TextView) findViewById(R.id.appbar_location_txt);

        fragmentManager = getSupportFragmentManager();

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(0);

        fragmentManager.beginTransaction().add(R.id.fragment_content, new MainFragment()).commit();

        setNavigation();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            if (toolbar.getVisibility() == View.GONE) {
                toolbar.setVisibility(View.VISIBLE);
            }

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragmentManager.beginTransaction().replace(R.id.fragment_content, new MainFragment()).commit();
                    return true;
                case R.id.navigation_review:
                    fragmentManager.beginTransaction().replace(R.id.fragment_content, new RealReviewFragment()).commit();
                    return true;
                case R.id.navigation_event:
                    fragmentManager.beginTransaction().replace(R.id.fragment_content, new EventFragment()).commit();
                    return true;
                case R.id.navigation_shop:
                    toolbar.setVisibility(View.GONE);
                    fragmentManager.beginTransaction().replace(R.id.fragment_content, new ShopFragment()).commit();
                    return true;
                case R.id.navigation_myblog:
                    fragmentManager.beginTransaction().replace(R.id.fragment_content, new MyBlogFragment()).commit();
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

    }

    @Override
    public void onBackPressed() {
//        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            long tempTime = System.currentTimeMillis();
            long intervalTime = tempTime - backPressedTime;

            if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(-1);
            } else {
                backPressedTime = tempTime;
                if (navigation.getVisibility() == View.GONE) {
                    slideUp(navigation);
                }
                Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("ABCDE", "MainActivity onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("ABCDE", "MainActivity onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setNavigation() {
        ImageView test = (ImageView) findViewById(R.id.nav_image_profile);
        loadImage(baceContext, R.drawable.img_default_login_user, test, true);
        Button button = (Button) findViewById(R.id.main_navigation_login);

        final RelativeLayout off = (RelativeLayout) findViewById(R.id.main_navigation_off);
        final ScrollView on = (ScrollView) findViewById(R.id.main_navigation_on);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("navi", "OnClickListener");
                off.setVisibility(View.GONE);
                on.setVisibility(View.VISIBLE);
            }
        });

        navi_shop = (TextView) findViewById(R.id.main_navigation_shop);
        navi_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                navigation.setSelectedItemId(R.id.navigation_shop);

                if (drawer.isDrawerOpen(GravityCompat.START))
                    drawer.closeDrawer(GravityCompat.START);
            }
        });

    }


    public void slideDown(final View view) {
        view.animate()
                .translationY(view.getHeight())
                .alpha(0.f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // superfluous restoration
                        view.setVisibility(View.GONE);
                        view.setAlpha(1.f);
                        view.setTranslationY(0.f);
                    }
                });
    }

    public void slideUp(final View view) {
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0.f);

        if (view.getHeight() > 0) {
            slideUpNow(view);
        } else {
            // wait till height is measured
            view.post(new Runnable() {
                @Override
                public void run() {
                    slideUpNow(view);
                }
            });
        }
    }

    private void slideUpNow(final View view) {
        view.setTranslationY(view.getHeight());
        view.animate()
                .translationY(0)
                .alpha(1.f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.VISIBLE);
                        view.setAlpha(1.f);
                    }
                });
    }

}
