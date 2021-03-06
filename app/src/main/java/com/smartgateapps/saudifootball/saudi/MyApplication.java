package com.smartgateapps.saudifootball.saudi;

import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.smartgateapps.saudifootball.R;
import com.smartgateapps.saudifootball.model.Legue;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Raafat on 04/11/2015.
 */
public class MyApplication extends Application {


    public static AlarmManager alarmManager;
    public static SharedPreferences pref;
    public static NotificationManager notificationManager;
    public static DbHelper dbHelper;
    public static SQLiteDatabase dbw, dbr;

    public static int pageSize = 15;
    public static Typeface font;
    public static final String BASE_URL = "http://m.kooora.com/";
    public static final String SAUDI_EXT_HOME = "?n=0&o=ncsa&pg=";
    public static final String ABD_ALATIF_EXT = "?c=7717";
    public static final String WALI_ALAHID_EXT = "?c=8178";
    public static final String KHADIM_ALHARAMIN_EXT = "?c=11760";
    public static final String FIRST_CLASS_EXT = "?c=7814";
    public static final String KHADIM_ALHARAMIN_NEWS_EXT = "?n=0&o=n11760&pg=";
    public static final String WALI_ALAHID_NEWS_EXT = "?n=0&o=n8178&pg=";
    public static final String ABD_ALATIF_NEWS_EXT = "?n=0&o=n7717&pg=";
    public static final String FIRST_CLASS_NEWS_EXT = "?n=0&o=n7814&pg=";
    public static final String TEAM_NEWS_EXT = "?n=0&o=n1000000";
    public static final String TEAM_MATCHES_EXT = "?region=-6&team=";

    public static final String TEAMS_CM = "&cm=t";
    public static final String POSES_CM = "&cm=i";
    public static final String MATCHES_CM = "&cm=m";
    public static final String SCORERS_CM = "&scorers=true";

    public static Context APP_CTX;
    public static final String LIVE_CAST_APP_PACKAGE_NAME = "com.smartgateapps.livesports";

    public static Picasso picasso;
    public static WebView webView;

    public static final int HEADER_TYPE_GOALERS = 0;

    public static String[] PLAYERS_POS = new String[]{"", "مدرب", "حارس", "دفاع", "وسط", "هجوم", "مساعد مدرب", " مدرب حراس", "مدرب بدني", "طبيب الفريق"};

    public static HashMap<String, Integer> monthOfTheYear = new HashMap<>(12);
    public static MyApplication instance;
    public static HashMap<Integer, Integer> teamsLogos = new HashMap<>();


    public static SimpleDateFormat sourceTimeFormate = new SimpleDateFormat("HH:mm");
    public static SimpleDateFormat destTimeFormate = new SimpleDateFormat("HH:mm");
    public static SimpleDateFormat sourceDateFormat = new SimpleDateFormat("E d MMMM yyy", new Locale("ar"));
    public static SimpleDateFormat destDateFormat = new SimpleDateFormat("E d MMMM yyy", new Locale("ar"));

    public static TimeZone currentTimeZone;

    public static InterstitialAd mInterstitialAd;


    public static Long parseDateTime(String date, String time) {

        Long dateL = 0L;
        Long timeL = MyApplication.getCurretnDateTime();
        try {
            dateL = sourceDateFormat.parse(date).getTime();
            timeL = sourceTimeFormate.parse(time).getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateL + timeL - getCurrentOffset();
    }

    public static String[] formatDateTime(Long dateTime) {
        dateTime += getCurrentOffset();
        String date = sourceDateFormat.format(dateTime);
        String time = sourceTimeFormate.format(dateTime);

        return new String[]{date, time};
    }


    public static String converteDate(String time, String date) throws ParseException {
        try {

            long dated = 0;
            if (date != null && !date.equalsIgnoreCase(""))
                dated = sourceDateFormat.parse(date).getTime();
            long timed = 0;
            if (time != null && !time.equalsIgnoreCase(""))
                timed = sourceTimeFormate.parse(time).getTime();
            long dateTime = timed + dated;
            return destDateFormat.format(dateTime);
        } catch (Exception e) {
            return "";
        }

    }

    public static String converteTime(String time, String date) throws ParseException {
        try {

            long dated = 0;
            if (date != null && !date.equalsIgnoreCase(""))
                dated = sourceDateFormat.parse(date).getTime();
            long timed = 0;
            if (time != null && !time.equalsIgnoreCase(""))
                timed = sourceTimeFormate.parse(time).getTime();
            long dateTime = timed + dated;
            return destTimeFormate.format(dateTime);

        } catch (Exception e) {
            return "";
        }
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.admob_interstitial));
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdOpened() {
                requestNewInterstitial();
            }
        });
        requestNewInterstitial();

        currentTimeZone = TimeZone.getDefault();
        sourceTimeFormate.setTimeZone(TimeZone.getTimeZone("UTC"));
        destTimeFormate.setTimeZone(currentTimeZone);
        sourceDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        destDateFormat.setTimeZone(currentTimeZone);

        teamsLogos.put(1004, R.mipmap.t1004);
        teamsLogos.put(1005, R.mipmap.t1005);
        teamsLogos.put(1006, R.mipmap.t1006);
        teamsLogos.put(1007, R.mipmap.t1007);
        teamsLogos.put(1008, R.mipmap.t1008);
        teamsLogos.put(1009, R.mipmap.t1009);
        teamsLogos.put(1011, R.mipmap.t1011);
        teamsLogos.put(10246, R.mipmap.t10246);
        teamsLogos.put(10247, R.mipmap.t10247);
        teamsLogos.put(10254, R.mipmap.t10254);
        teamsLogos.put(10533, R.mipmap.t10533);
        teamsLogos.put(11572, R.mipmap.t11572);
        teamsLogos.put(11955, R.mipmap.t11955);
        teamsLogos.put(11956, R.mipmap.t11956);
        teamsLogos.put(1259, R.mipmap.t1259);
        teamsLogos.put(1260, R.mipmap.t1260);
        teamsLogos.put(1261, R.mipmap.t1261);
        teamsLogos.put(1263, R.mipmap.t1263);
        teamsLogos.put(1264, R.mipmap.t1264);
        teamsLogos.put(1265, R.mipmap.t1265);
        teamsLogos.put(1266, R.mipmap.t1266);
        teamsLogos.put(1267, R.mipmap.t1267);
        teamsLogos.put(13716, R.mipmap.t13716);
        teamsLogos.put(13777, R.mipmap.t13777);
        teamsLogos.put(144, R.mipmap.t144);
        teamsLogos.put(145, R.mipmap.t145);
        teamsLogos.put(146, R.mipmap.t146);
        teamsLogos.put(147, R.mipmap.t147);
        teamsLogos.put(148, R.mipmap.t148);
        teamsLogos.put(149, R.mipmap.t149);
        teamsLogos.put(150, R.mipmap.t150);
        teamsLogos.put(151, R.mipmap.t151);
        teamsLogos.put(152, R.mipmap.t152);
        teamsLogos.put(153, R.mipmap.t153);
        teamsLogos.put(154, R.mipmap.t154);
        teamsLogos.put(155, R.mipmap.t155);
        teamsLogos.put(15796, R.mipmap.t15796);
        teamsLogos.put(15802, R.mipmap.t15802);
        teamsLogos.put(2011, R.mipmap.t2011);
        teamsLogos.put(294, R.mipmap.t294);
        teamsLogos.put(292, R.mipmap.t292);
        teamsLogos.put(293, R.mipmap.t293);
        teamsLogos.put(6145, R.mipmap.t6145);
        teamsLogos.put(739, R.mipmap.t739);
        teamsLogos.put(740, R.mipmap.t740);
        teamsLogos.put(7851, R.mipmap.t7851);
        teamsLogos.put(7854, R.mipmap.t7854);
        teamsLogos.put(7856, R.mipmap.t7856);
        teamsLogos.put(7857, R.mipmap.t7857);
        teamsLogos.put(9281, R.mipmap.t9281);

        APP_CTX = getApplicationContext();
        font = Typeface.createFromAsset(APP_CTX.getAssets(), "fonts/jf_flat_regular.ttf");
        dbHelper = new DbHelper(APP_CTX);
        dbw = dbHelper.getWritableDatabase();
        dbr = dbHelper.getReadableDatabase();

        picasso = Picasso.with(this);

        Legue saudi = new Legue(0L, "السعودية", "?y=sa", SAUDI_EXT_HOME);
        Legue abdAlatif = new Legue(1L, "دوري عبداللطيف جميل", ABD_ALATIF_EXT, ABD_ALATIF_NEWS_EXT);
        Legue waliAlahid = new Legue(2L, "كأس ولي العد", WALI_ALAHID_EXT, WALI_ALAHID_NEWS_EXT);
        Legue kadimAlaharmin = new Legue(3L, "كأس خادم الحرمين الشريفين", KHADIM_ALHARAMIN_EXT, KHADIM_ALHARAMIN_NEWS_EXT);
        Legue firstClass = new Legue(4L, "دوري الدرجة الاولى", FIRST_CLASS_EXT, FIRST_CLASS_NEWS_EXT);

        saudi.save();
        abdAlatif.save();
        waliAlahid.save();
        kadimAlaharmin.save();
        firstClass.save();

        pref = PreferenceManager.getDefaultSharedPreferences(MyApplication.APP_CTX);
        boolean b = pref.getBoolean(getString(R.string.abd_alatif_notificatin_pref_key), true);
        pref.edit().putBoolean(getString(R.string.abd_alatif_notificatin_pref_key), b).apply();
        notificationManager = (NotificationManager) APP_CTX.getSystemService(NOTIFICATION_SERVICE);

        alarmManager = (AlarmManager) APP_CTX.getSystemService(ALARM_SERVICE);


        monthOfTheYear.put("يناير", 1);
        monthOfTheYear.put("فبراير", 2);
        monthOfTheYear.put("مارس", 3);
        monthOfTheYear.put("أبريل", 4);
        monthOfTheYear.put("مايو", 5);
        monthOfTheYear.put("يونيو", 6);
        monthOfTheYear.put("يوليو", 7);
        monthOfTheYear.put("أغسطس", 8);
        monthOfTheYear.put("سبتمبر", 9);
        monthOfTheYear.put("أكتوبر", 10);
        monthOfTheYear.put("نوفمبر", 11);
        monthOfTheYear.put("ديسمبر", 12);

        //Parse.enableLocalDatastore(this);
        Parse.initialize(this, "VjSgl7C2cr72BqV47qEVE4KGvr69RlDuvdu9lz5R", "1iP6tUe0fLldOS4C9ZCvKRiKlU1weJTmnwwoI5Ct");
        ParseInstallation.getCurrentInstallation().saveInBackground();
//        Map<String, String> dimensions = new HashMap<String, String>();
//        dimensions.put("category", "politics");
//        dimensions.put("dayType", "weekday");
//        ParseAnalytics.trackEventInBackground("read", dimensions);


    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }


    public static void openPlayStor(String appPackageName) {
        try {
            APP_CTX.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(appPackageName)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } catch (android.content.ActivityNotFoundException anfe) {
            APP_CTX.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }


    public static void changeTabsFont(TabLayout tabLayout) {

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(MyApplication.font);
                }
            }
        }
    }

    public static Long getCurretnDateTime(){
        Calendar rightNow = Calendar.getInstance();
        return (rightNow.getTimeInMillis());
    }

    public static Long getCurrentOffset(){
        Calendar rightNow = Calendar.getInstance();
        long offset = rightNow.get(Calendar.ZONE_OFFSET) +
                rightNow.get(Calendar.DST_OFFSET);

        return offset;
    }

}
