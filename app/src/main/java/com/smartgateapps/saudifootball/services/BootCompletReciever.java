package com.smartgateapps.saudifootball.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.smartgateapps.saudifootball.model.Match;
import com.smartgateapps.saudifootball.saudi.MyApplication;

import java.util.List;

/**
 * Created by Raafat on 13/02/2016.
 */
public class BootCompletReciever extends WakefulBroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        //register update matches for the next matches
        List<Match> matches = Match.getAllUnUpdatedMatches();
        for(Match m : matches) {
            if(m.getNotifyDateTime() <= MyApplication.getCurrentOffset()){
                m.setNotifyDateTime(MyApplication.getCurretnDateTime()+4*60*1000);
                m.update();
            }
            m.registerMatchUpdateFirstTime();
        }

        Intent intentActivationUpateNewsService = new Intent(MyApplication.APP_CTX,GetAllDawriNewsReciever.class);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(MyApplication.APP_CTX, 22, intentActivationUpateNewsService, PendingIntent.FLAG_UPDATE_CURRENT);

        MyApplication.alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, MyApplication.getCurretnDateTime() + 10000,10*60*1000, pendingIntent);
        //register do at 2 AM;


    }
}
