package com.smartgateapps.saudifootball.services;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.smartgateapps.saudifootball.model.Match;

import java.util.List;

/**
 * Created by Raafat on 13/02/2016.
 */
public class BootCompletReciever extends WakefulBroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        //register update matches for the next matches
        List<Match> matches = Match.getAllNextMatches();
        for(Match m : matches)
            m.registerMatchUpdateFirstTime();

        //register do at 2 AM;


    }
}
