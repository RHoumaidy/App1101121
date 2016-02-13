package com.smartgateapps.saudifootball.services;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.http.SslError;
import android.support.design.widget.Snackbar;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.text.Html;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.smartgateapps.saudifootball.model.Match;
import com.smartgateapps.saudifootball.model.Stage;
import com.smartgateapps.saudifootball.saudi.MyApplication;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Raafat on 09/02/2016.
 */
public class UpdateMatch extends WakefulBroadcastReceiver {

    private static long MINUTE_IN_MILLIS = 60 * 1000;
    private static long HOUR_IN_MILLIS = MINUTE_IN_MILLIS *60;
    private WebView webView;

    @Override
    public void onReceive(Context context, Intent intent) {
        Long mId = intent.getLongExtra("MATCH_ID", 0);
        Match match = Match.load(mId);

        featchDate(match.getMatchUrl());

        long currDateTime = Calendar.getInstance().getTimeInMillis();
        if (match.getDateTime() < currDateTime && (currDateTime - match.getDateTime())<=HOUR_IN_MILLIS*2) {
            match.registerMatchUpdateDate(currDateTime + MINUTE_IN_MILLIS * 2);
        }

        if(match.getResultL().equalsIgnoreCase("--")){
            Intent toNotification = new Intent(context, MatchNotification.class);
            toNotification.putExtra("MATCH_ID",mId);
            startWakefulService(context, toNotification);
        }

        completeWakefulIntent(intent);
    }


    public void featchDate(String url) {
        webView = new WebView(MyApplication.APP_CTX);
        webView = new WebView(MyApplication.APP_CTX);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(false);
        webView.getSettings().setGeolocationEnabled(true);

        webView.addJavascriptInterface(new MyJavaScriptInterface(), "HtmlViewer");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl("javascript:window.HtmlViewer.showHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
            }

        });
        webView.stopLoading();
        webView.loadUrl(MyApplication.BASE_URL+url);
    }


    class MyJavaScriptInterface {


        @JavascriptInterface
        @SuppressWarnings("unused")
        public void showHTML(final String html) {
            String htm = html;
            Document doc = Jsoup.parse(html);
            try {

                Element mainContent = doc.getElementById("mainContent");
                Element fullcontent = mainContent.getElementById("fullcontent");
                Element matchesTable = fullcontent.getElementById("matchesTable");
                Element tbody = matchesTable.getElementsByTag("tbody").first();
                Elements trs = tbody.getElementsByTag("tr");
                for(Element tr : trs){
                    if(!tr.hasAttr("class")){

                        String time;
                        String teamR;
                        String teamL;
                        String resultR;
                        String resultL;
                        String resultReX;
                        String resultLeX;

                        Elements tds = tr.getElementsByTag("td");

                        Element tdRes = tds.get(2).getElementsByTag("span").first();
                        if (tdRes == null)
                            tdRes = tds.get(2);
                        Element tdResEx = tds.get(2).getElementsByTag("div").first();
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


                        Match match = new Match();

                        match.setResultL(resultL);
                        match.setResultR(resultR);
                        match.update();
                    }
                }

            } catch (Exception e) {

            }

        }

    }
}
