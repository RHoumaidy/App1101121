package com.smartgateapps.saudifootball.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smartgateapps.saudifootball.R;
import com.smartgateapps.saudifootball.model.Team;
import com.smartgateapps.saudifootball.saudi.MyApplication;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {

    private static final boolean AUTO_HIDE = true;


    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    private static final int UI_ANIMATION_DELAY = 300;

    private View mContentView;
    private boolean mVisible;
    private LinearLayout splashLL;
    private List<Team> teamList = new ArrayList<>();
    private WebView webView1, webView2, webView3, webView4;

    int done = 1;

    private Set<Integer> keys;

    @Override
    protected void onPause() {
        super.onPause();

    }

    public void checkInternet() {
        if (!MyApplication.instance.isNetworkAvailable()) {

        }
    }

    private void featchData() {
        if (!MyApplication.instance.isNetworkAvailable()) {
            Snackbar snackbar = Snackbar.make(splashLL, "لا يوجد اتصال بالانترنت", Snackbar.LENGTH_INDEFINITE)
                    .setAction("اعد المحاولة", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            featchData();
                        }
                    });
            snackbar.setActionTextColor(Color.RED);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);

            snackbar.show();
        } else {

            webView1 = new WebView(MyApplication.APP_CTX);
            webView1.getSettings().setJavaScriptEnabled(true);
            webView1.getSettings().setAppCacheEnabled(true);
            webView1.getSettings().setGeolocationEnabled(true);
            webView1.getSettings().setLoadsImagesAutomatically(false);

            webView2 = new WebView(MyApplication.APP_CTX);
            webView2.getSettings().setJavaScriptEnabled(true);
            webView2.getSettings().setAppCacheEnabled(true);
            webView2.getSettings().setGeolocationEnabled(true);
            webView2.getSettings().setLoadsImagesAutomatically(false);

            webView3 = new WebView(MyApplication.APP_CTX);
            webView3.getSettings().setJavaScriptEnabled(true);
            webView3.getSettings().setAppCacheEnabled(true);
            webView3.getSettings().setGeolocationEnabled(true);
            webView3.getSettings().setLoadsImagesAutomatically(false);

            webView4 = new WebView(MyApplication.APP_CTX);
            webView4.getSettings().setJavaScriptEnabled(true);
            webView4.getSettings().setAppCacheEnabled(true);
            webView4.getSettings().setGeolocationEnabled(true);
            webView4.getSettings().setLoadsImagesAutomatically(false);

            featchData1(MyApplication.ABD_ALATIF_EXT + MyApplication.TEAMS_CM);
            featchData2(MyApplication.WALI_ALAHID_EXT + MyApplication.TEAMS_CM);
            featchData3(MyApplication.KHADIM_ALHARAMIN_EXT + MyApplication.TEAMS_CM);
            featchData4(MyApplication.FIRST_CLASS_EXT + MyApplication.TEAMS_CM);

            mVisible = true;
            mContentView = findViewById(R.id.fullscreen_content);

            mContentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toggle();
                }
            });
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        keys = MyApplication.teamsLogos.keySet();
        splashLL = (LinearLayout) findViewById(R.id.splashLinearLayout);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Intent i = new Intent(Splash.this, MainActivity.class);
                    Splash.this.startActivity(i);
                    Splash.this.finish();
                }
            }, 3000);
    }

    private void featchData1(String urlExtention) {

        webView1.addJavascriptInterface(new MyJavaScriptInterface1(), "HtmlViewer");
        webView1.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webView1.loadUrl("javascript:window.HtmlViewer.showHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>',1);");
            }
        });

        //String url = MyApplication.BASE_URL+MyApplication.ABD_ALATIF_EXT;
        webView1.stopLoading();
        webView1.loadUrl(MyApplication.BASE_URL + urlExtention);
    }

    private void featchData2(String urlExtention) {

        webView2.addJavascriptInterface(new MyJavaScriptInterface1(), "HtmlViewer");
        webView2.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webView2.loadUrl("javascript:window.HtmlViewer.showHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>',2);");
            }
        });

        //String url = MyApplication.BASE_URL+MyApplication.ABD_ALATIF_EXT;
        webView2.stopLoading();
        webView2.loadUrl(MyApplication.BASE_URL + urlExtention);
    }

    private void featchData3(String urlExtention) {

        webView3.addJavascriptInterface(new MyJavaScriptInterface1(), "HtmlViewer");
        webView3.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webView3.loadUrl("javascript:window.HtmlViewer.showHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>',3);");
            }
        });

        //String url = MyApplication.BASE_URL+MyApplication.ABD_ALATIF_EXT;
        webView3.stopLoading();
        webView3.loadUrl(MyApplication.BASE_URL + urlExtention);
    }

    private void featchData4(String urlExtention) {

        webView4.addJavascriptInterface(new MyJavaScriptInterface1(), "HtmlViewer");
        webView4.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webView4.loadUrl("javascript:window.HtmlViewer.showHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>',4);");
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
            }
        });

        //String url = MyApplication.BASE_URL+MyApplication.ABD_ALATIF_EXT;
        webView4.stopLoading();
        webView4.loadUrl(MyApplication.BASE_URL + urlExtention);
    }


    class MyJavaScriptInterface1 {


        @JavascriptInterface
        @SuppressWarnings("unused")
        public void showHTML(final String html, final int leagueId) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String htm = html;
                    Document doc = Jsoup.parse(html);

                    try {
                        Element content = doc.getElementById("content");
                        Element div_mb20 = content.getElementsByClass("mb20").get(0);
                        Element table = div_mb20.getElementsByTag("table").first();
                        Element table_body = table.getElementsByTag("tbody").first();


                        for (Element tr : table_body.getElementsByTag("tr")) {

                            Element tdl = tr.getElementsByTag("td").get(0);
                            Element tdr = tr.getElementsByTag("td").get(1);

                            Element al = tdl.getElementsByTag("a").first();
                            Element ar = tdr.getElementsByTag("a").first();

                            Team team1 = new Team();
                            Team team2 = new Team();

                            String team1N = al.text();
                            String team2N = ar.text();

                            String team1U = al.attr("href");
                            String team2U = ar.attr("href");

                            String[] stA1 = team1U.split("\\|");
                            String[] stA2 = team2U.split("\\|");

                            int team1Id = Integer.parseInt(stA1[1]);
                            int team2Id = Integer.parseInt(stA2[1]);

                            String team1Url = MyApplication.BASE_URL + "?team=" + team1Id;
                            String team2Url = MyApplication.BASE_URL + "?team=" + team2Id;

                            team1.setTeamName(team1N);
                            team1.setId(team1Id);
                            team1.setTeamUrl(team1Url);
                            team1.setLeagueId(leagueId);

                            team2.setTeamName(team2N);
                            team2.setId(team2Id);
                            team2.setTeamUrl(team2Url);
                            team2.setLeagueId(leagueId);

                            int logo1 = keys.contains(new Integer(team1Id)) ? MyApplication.teamsLogos.get(team1Id) : R.drawable.water_mark;
                            int logo2 = keys.contains(new Integer(team2Id)) ? MyApplication.teamsLogos.get(team2Id) : R.drawable.water_mark;

                            team1.setTeamLogo(logo1);
                            team2.setTeamLogo(logo2);

                            team1.save();
                            team2.save();

                        }
                    } catch (Exception e) {
                        Snackbar snackbar = Snackbar.make(splashLL, "نأسف حدث حطأ في جلب بعض البيانات", Snackbar.LENGTH_INDEFINITE)
                                .setAction("اعد المحاولة", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        featchData();
                                    }
                                });
                        snackbar.setActionTextColor(Color.RED);

                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.YELLOW);

                        snackbar.show();
                    }


                    done++;
                    if (done == 5) {
                        Intent i = new Intent(Splash.this, MainActivity.class);
                        Splash.this.startActivity(i);
                        Splash.this.finish();
                        MyApplication.pref.edit()
                                .putBoolean("TEAMS_LOADED", true)
                                .apply();
                    }

                }
            });
        }
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


        delayedHide(100);
    }

    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            try {
                mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            } catch (Exception e) {

            }
        }
    };

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
    };

    private final Handler mHideHandler = new Handler();
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
