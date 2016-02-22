package com.smartgateapps.saudifootball.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;

import com.smartgateapps.saudifootball.R;
import com.smartgateapps.saudifootball.activities.NewsListFragment;
import com.smartgateapps.saudifootball.activities.NewsListFragmentBackground;
import com.smartgateapps.saudifootball.model.News;
import com.smartgateapps.saudifootball.saudi.MyApplication;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Raafat on 11/01/2016.
 */
public class GetAllDawriNewsReciever extends WakefulBroadcastReceiver {

    private NewsListFragmentBackground newsListFragment1 = new NewsListFragmentBackground();

    public static GetAllDawriNewsReciever instance;
    public Intent intent;

    public GetAllDawriNewsReciever() {
        super();
        instance = this;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.intent = intent;
//        Toast.makeText(context,"started News",Toast.LENGTH_LONG).show();
        Intent intentActivationUpateNewsService = new Intent(MyApplication.ACTION_ACTIVATION);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(MyApplication.APP_CTX, 22, intentActivationUpateNewsService, PendingIntent.FLAG_NO_CREATE);
        if (pendingIntent != null)
            pendingIntent.cancel();
        pendingIntent =
                PendingIntent.getBroadcast(MyApplication.APP_CTX, 22, intentActivationUpateNewsService, PendingIntent.FLAG_ONE_SHOT);

        MyApplication.alarmManager.set(
                AlarmManager.RTC_WAKEUP, MyApplication.getCurretnDateTime() + 10*60*1000, pendingIntent);


        newsListFragment1.urlExtention = MyApplication.SAUDI_EXT_HOME;
        newsListFragment1.leaguId = 0;
        newsListFragment1.pageIdx = 1;
        newsListFragment1.isLeague = true;

        if(NewsListFragmentBackground.number >=5 || NewsListFragmentBackground.number ==0) {
            NewsListFragmentBackground.number = 0;
            newsListFragment1.featchData();
        }
//        newsListFragment2.featchData();
//        newsListFragment3.featchData();
//        newsListFragment4.featchData();
//        newsListFragment5.featchData();


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

       // completeWakefulIntent(intent);

    }


}
