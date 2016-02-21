package com.smartgateapps.saudifootball.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.text.Html;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.smartgateapps.saudifootball.model.Match;
import com.smartgateapps.saudifootball.saudi.MyApplication;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Calendar;

/**
 * Created by Raafat on 09/02/2016.
 */
public class UpdateMatch extends WakefulBroadcastReceiver {

    private static long MINUTE_IN_MILLIS = 60 * 1000;
    private static long HOUR_IN_MILLIS = MINUTE_IN_MILLIS * 60;
    private WebView webView;
    private Intent intent;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.intent = intent;

        Long mId = intent.getLongExtra("MATCH_ID", 0);
        Match match = Match.load(mId);
        featchDate(match);

        if (!MyApplication.instance.isNetworkAvailable()) {
            match.setNotifyDateTime(match.getDateTime() + 10 * 60 * 1000);
            match.update();
            match.registerMatchUpdateFirstTime();
        }

        boolean isChecked = match.isNotifyMe();
        if (System.currentTimeMillis() - match.getDateTime() < 1000*60 && isChecked) {
            Intent toNotification = new Intent(context, MatchNotification.class);
            toNotification.putExtra("MATCH_ID", mId);
            startWakefulService(context, toNotification);
        }


    }


    public void featchDate(final Match match) {
        webView = new WebView(MyApplication.APP_CTX);
        webView = new WebView(MyApplication.APP_CTX);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(false);

        webView.addJavascriptInterface(new MyJavaScriptInterface(), "HtmlViewer");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl("javascript:window.HtmlViewer.showHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>',"+match.getId()+");");
            }

        });
        webView.loadUrl(MyApplication.BASE_URL + match.getMatchUrl());
    }


    class MyJavaScriptInterface {


        @JavascriptInterface
        @SuppressWarnings("unused")
        public void showHTML(final String html,Long mId) {

            Match match = Match.load(mId);
            String htm = html;
            Document doc = Jsoup.parse(html);
            try {

                Element mainContent = doc.getElementById("mainContent");
                Element fullcontent = mainContent.getElementById("fullcontent");
                Element matchesTable = fullcontent.getElementById("matchesTable");
                Element tbody = matchesTable.getElementsByTag("tbody").first();

                Element tt = tbody.getElementsByClass("tt").first();
                if(tt != null && tt.getAllElements().size()>1) {// the match is going ...

                    String resultR;
                    String resultL;
                    String resultReX;
                    String resultLeX;

                    Element jm5x3 = tbody.getElementById("jm5x3");
                    Element tdRes = jm5x3.getElementsByTag("span").first();
                    Element tdResEx = jm5x3.getElementsByTag("div").first();

                    resultR = Html.fromHtml(tdRes.text()).toString();
                    resultL = resultR.split(":")[0].replaceAll("\\s+", "");
                    resultR = resultR.split(":")[1].replaceAll("\\s+", "");

                    if (tdResEx != null) {
                        resultReX = Html.fromHtml(tdResEx.text()).toString();
                        resultLeX = resultReX.split(":")[0].replaceAll("\\s+", "");
                        resultReX = resultReX.split(":")[1].replaceAll("\\s+", "");
                        int resRex = Integer.valueOf(resultReX);
                        int resLeX = Integer.valueOf(resultLeX);
                        int resR = Integer.valueOf(resultR);
                        int resL = Integer.valueOf(resultR);

                        resultR = resR + resRex + "";
                        resultL = resL + resLeX + "";
                    }

                    if(!resultL.equalsIgnoreCase(match.getResultL()) || !resultR.equalsIgnoreCase(match.getResultR())){
                        Intent toNotification = new Intent(MyApplication.APP_CTX, MatchGoalNotification.class);
                        toNotification.putExtra("MATCH_ID", mId);
                        startWakefulService(MyApplication.APP_CTX, toNotification);
                    }
                    match.setResultL(resultL);
                    match.setResultR(resultR);
                    match.update();


                    long currDateTime = Calendar.getInstance().getTimeInMillis();
                    match.registerMatchUpdateDate(currDateTime + MINUTE_IN_MILLIS * 7);
                }else{ // the match eighter done or have not began yet

                    String resultR;
                    String resultL;
                    String resultReX;
                    String resultLeX;

                    Element jm5x3 = tbody.getElementById("jm5x3");
                    Element tdRes = jm5x3.getElementsByTag("span").first();
                    Element tdResEx = jm5x3.getElementsByTag("div").first();

                    resultR = Html.fromHtml(tdRes.text()).toString();
                    resultL = resultR.split(":")[0].replaceAll("\\s+", "");
                    resultR = resultR.split(":")[1].replaceAll("\\s+", "");

                    if (tdResEx != null) {
                        resultReX = Html.fromHtml(tdResEx.text()).toString();
                        resultLeX = resultReX.split(":")[0].replaceAll("\\s+", "");
                        resultReX = resultReX.split(":")[1].replaceAll("\\s+", "");
                        int resRex = Integer.valueOf(resultReX);
                        int resLeX = Integer.valueOf(resultLeX);
                        int resR = Integer.valueOf(resultR);
                        int resL = Integer.valueOf(resultR);

                        resultR = resR + resRex + "";
                        resultL = resL + resLeX + "";
                    }

                    if(resultL.equalsIgnoreCase("--")|| resultR.equalsIgnoreCase("--")){
                        long currDateTime = Calendar.getInstance().getTimeInMillis();
                        match.registerMatchUpdateDate(currDateTime + MINUTE_IN_MILLIS * 7);
                    }else {
                        match.setResultL(resultL);
                        match.setResultR(resultR);
                        match.setHasBeenUpdated(true);
                        match.update();
                    }

                }

            } catch (Exception e) {
                match.registerMatchUpdateDate(System.currentTimeMillis() + MINUTE_IN_MILLIS * 7);
            }

            completeWakefulIntent(intent);

        }

    }
}
