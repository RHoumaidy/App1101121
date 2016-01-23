package com.smartgateapps.saudifootball.activities;

import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseAnalytics;
import com.smartgateapps.saudifootball.R;
import com.smartgateapps.saudifootball.saudi.MyApplication;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public ActionBar actionBar;
    public Toolbar toolbar;
    public DrawerLayout drawer;
    private CoordinatorLayout coordinatorLayout;

    public TabLayout tabLayout;
    public LinearLayout matchFilterLL;
    private int prevSelectedId = R.id.newsItemId;

    private android.support.v4.app.FragmentManager supportManager;
    private FragmentManager manager;
    private Fragment newsListFragment;
    private Fragment abdAlatifFragment;
    private Fragment waliAlahidFragment;
    private Fragment kadimAlharminFragment;
    private Fragment firstClassFragment;
    private Fragment aboutfraFragment;
    private Fragment contactUsFragment;
    private PreferenceFragment prefFragment;

    private AdView mAdView;
    private Fragment prevFragment = null;
    private PreferenceFragment prevFragment1 = null;

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);

    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mAdView.loadAd(adRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        matchFilterLL = (LinearLayout) findViewById(R.id.choseMathcFilterLL);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        mAdView = (AdView) findViewById(R.id.adView);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mAdView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                requestNewInterstitial();
                mAdView.setVisibility(View.GONE);
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();

            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        actionBar.setTitle(getString(R.string.news_menu_item));

        supportManager = getSupportFragmentManager();
        manager = getFragmentManager();

        newsListFragment = new NewsListFragment();
        abdAlatifFragment = new AbdAlatifFragment();
        waliAlahidFragment = new WaliAlahidFragment();
        kadimAlharminFragment = new KhadimFragment();
        firstClassFragment = new FirstClassFragment();
        contactUsFragment = new SendEmailFragment();
        aboutfraFragment = new AboutFragment();
        prefFragment = new PrefFragment();

        Bundle args = new Bundle();
        args.putString("URL_EXT", MyApplication.SAUDI_EXT_HOME);
        args.putInt("RES", R.layout.news_card_layout);
        args.putInt("LEAGUE_ID", 0);
        newsListFragment.setArguments(args);

        supportManager.beginTransaction()
                .add(R.id.fragmentContainer, newsListFragment)
                .commit();
        prevFragment = newsListFragment;
        navigationView.setCheckedItem(R.id.newsItemId);

    }

    public void checkInternet() {
        if (!MyApplication.instance.isNetworkAvailable()) {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "لا يوجد اتصال بالانترنت", Snackbar.LENGTH_INDEFINITE)
                    .setAction("اعد المحاولة", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkInternet();
                        }
                    });
            snackbar.setActionTextColor(Color.RED);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);

            snackbar.show();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        android.support.v4.app.Fragment fragment = null;
        PreferenceFragment fragment1 = null;

        toolbar.setTitle(item.getTitle());

        if (id == prevSelectedId) {
            drawer.closeDrawer(GravityCompat.START);
            return false;
        }
        prevSelectedId = id;

        switch (id) {
            case R.id.newsItemId:
                fragment = newsListFragment;
                break;

            case R.id.abdAlatifItemId:
                fragment = abdAlatifFragment;
                if(MyApplication.mInterstitialAd.isLoaded())
                    MyApplication.mInterstitialAd.show();
                break;

            case R.id.waliItemId:
                fragment = waliAlahidFragment;
                break;
            case R.id.alHaraminItemId:
                fragment = kadimAlharminFragment;
                break;
            case R.id.firstClassItemId:
                fragment = firstClassFragment;
                break;
            case R.id.settingItemId:
                fragment1 = prefFragment;
                break;
            case R.id.contactUsItemId:
                fragment = contactUsFragment;
                break;
            case R.id.aboutItmeId:
                fragment = aboutfraFragment;
                break;

        }

        if (fragment != null) {
            supportManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit();
            prevFragment = fragment;
            if (prevFragment1 != null)
                manager.beginTransaction()
                        .remove(prevFragment1)
                        .commit();
        }

        if (fragment1 != null) {
            prevFragment1 = fragment1;
            if (prevFragment != null)
                supportManager.beginTransaction()
                        .remove(prevFragment)
                        .commit();
            manager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment1)
                    .commit();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private boolean backPressed = false;

    @Override
    public void onBackPressed() {

        if(!backPressed) {
            backPressed = true;
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    backPressed = false;
                }
            }, 3500);
            Toast.makeText(this, ("اضغط مرة اخرى للاغلاق"), Toast.LENGTH_LONG).show();
        }else {
            if(MyApplication.mInterstitialAd.isLoaded())
                MyApplication.mInterstitialAd.show();

            Intent intentActivationUpateNewsService = new Intent(MyApplication.ACTION_ACTIVATION);

            PendingIntent pendingIntent =
                    PendingIntent.getBroadcast(MyApplication.APP_CTX, 0, intentActivationUpateNewsService, PendingIntent.FLAG_UPDATE_CURRENT);

            MyApplication.alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, pendingIntent);
            super.onBackPressed();
        }
    }
}
