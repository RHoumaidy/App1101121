package com.smartgateapps.saudifootball.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.smartgateapps.saudifootball.R;
import com.smartgateapps.saudifootball.activities.NewsListFragmentBackground;
import com.smartgateapps.saudifootball.saudi.MyApplication;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Raafat on 11/01/2016.
 */
public class GetAllDawriNewsReciever extends WakefulBroadcastReceiver {

    private NewsListFragmentBackground newsListFragment1 = new NewsListFragmentBackground();
    private NewsListFragmentBackground newsListFragment2 = new NewsListFragmentBackground();
    private NewsListFragmentBackground newsListFragment3 = new NewsListFragmentBackground();
    private NewsListFragmentBackground newsListFragment4 = new NewsListFragmentBackground();

    @Override
    public void onReceive(Context context, Intent intent) {


        Intent intentActivationUpateNewsService = new Intent(MyApplication.ACTION_ACTIVATION);

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(MyApplication.APP_CTX, 0, intentActivationUpateNewsService, PendingIntent.FLAG_UPDATE_CURRENT);

        MyApplication.alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60 * 10 * 1000, pendingIntent);

        newsListFragment1.urlExtention = MyApplication.SAUDI_EXT_HOME;
        newsListFragment1.leaguId = 0;
        newsListFragment1.pageIdx = 1;
        newsListFragment1.isLeague = true;

        newsListFragment2.urlExtention = MyApplication.ABD_ALATIF_NEWS_EXT;
        newsListFragment2.leaguId = 1;
        newsListFragment2.pageIdx = 1;
        newsListFragment2.isLeague=true;

        newsListFragment3.urlExtention = MyApplication.WALI_ALAHID_NEWS_EXT;
        newsListFragment3.leaguId = 2;
        newsListFragment3.pageIdx = 1;
        newsListFragment3.isLeague = true;

        newsListFragment4.urlExtention = MyApplication.KHADIM_ALHARAMIN_NEWS_EXT;
        newsListFragment4.leaguId = 3;
        newsListFragment4.pageIdx = 1;
        newsListFragment4.isLeague = true;


        newsListFragment1.featchData();
        newsListFragment2.featchData();
        newsListFragment3.featchData();
        newsListFragment4.featchData();


        Set<String> selectedLeagues = new HashSet<>();
        if (MyApplication.pref.getBoolean(MyApplication.APP_CTX.getString(R.string.abd_alatif_notificatin_pref_key), false))
            selectedLeagues.add("1");
        if (MyApplication.pref.getBoolean(MyApplication.APP_CTX.getString(R.string.wali_notification_pref_key), false))
            selectedLeagues.add("2");
        if (MyApplication.pref.getBoolean(MyApplication.APP_CTX.getString(R.string.khadim_notification_pref_key), false))
            selectedLeagues.add("3");
        if (MyApplication.pref.getBoolean(MyApplication.APP_CTX.getString(R.string.first_notification_pref_key), false))
            selectedLeagues.add("4");

        MyApplication.pref.edit()
                .putStringSet(MyApplication.APP_CTX.getString(R.string.selected_leagues_pref_key), selectedLeagues)
                .commit();

        if (selectedLeagues.size() > 0) {
            Intent toNotification = new Intent(context, NotificationService.class);
            startWakefulService(context, toNotification);
        }


    }


}
