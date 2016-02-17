package com.smartgateapps.saudifootball.activities;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.smartgateapps.saudifootball.model.LeaguNews;
import com.smartgateapps.saudifootball.model.Legue;
import com.smartgateapps.saudifootball.model.News;
import com.smartgateapps.saudifootball.model.TeamNews;
import com.smartgateapps.saudifootball.saudi.MyApplication;
import com.smartgateapps.saudifootball.services.GetAllDawriNewsReciever;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raafat on 12/01/2016.
 */
public class NewsListFragmentBackground {

    public String urlExtention;
    private String urlExtentionPg;
    private WebView webView1;
    private WebView webView2;
    private WebView webView3;
    private WebView webView4;
    private WebView webView5;
    private WebView webView6;

    public boolean isLeague;
    public static int number = 0;


    public int pageIdx = 1;
    public int leaguId;

    public NewsListFragmentBackground() {
        webView1 = new WebView(MyApplication.APP_CTX);
        webView1.getSettings().setJavaScriptEnabled(true);
        webView1.getSettings().setLoadsImagesAutomatically(false);
        webView1.addJavascriptInterface(new MyJavaScriptInterface(), "HtmlViewer");
        webView1.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webView1.loadUrl("javascript:window.HtmlViewer.showHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                featchData2();
            }
        });

        webView2 = new WebView(MyApplication.APP_CTX);
        webView2.getSettings().setJavaScriptEnabled(true);
        webView2.getSettings().setLoadsImagesAutomatically(false);
        webView2.addJavascriptInterface(new MyJavaScriptInterface(), "HtmlViewer");
        webView2.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webView2.loadUrl("javascript:window.HtmlViewer.showHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                featchData3();
            }
        });

        webView3 = new WebView(MyApplication.APP_CTX);
        webView3.getSettings().setJavaScriptEnabled(true);
        webView3.getSettings().setLoadsImagesAutomatically(false);
        webView3.addJavascriptInterface(new MyJavaScriptInterface(), "HtmlViewer");
        webView3.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webView3.loadUrl("javascript:window.HtmlViewer.showHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                featchData4();
            }
        });

        webView4 = new WebView(MyApplication.APP_CTX);
        webView4.getSettings().setJavaScriptEnabled(true);
        webView4.getSettings().setLoadsImagesAutomatically(false);
        webView4.addJavascriptInterface(new MyJavaScriptInterface(), "HtmlViewer");
        webView4.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webView4.loadUrl("javascript:window.HtmlViewer.showHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                featchData5();
            }
        });

        webView5 = new WebView(MyApplication.APP_CTX);
        webView5.getSettings().setJavaScriptEnabled(true);
        webView5.getSettings().setLoadsImagesAutomatically(false);
        webView5.addJavascriptInterface(new MyJavaScriptInterface(), "HtmlViewer");
        webView5.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webView5.loadUrl("javascript:window.HtmlViewer.showHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                featchData6();
            }
        });

        webView6 = new WebView(MyApplication.APP_CTX);
        webView6.getSettings().setJavaScriptEnabled(true);
        webView6.getSettings().setLoadsImagesAutomatically(false);
        webView6.addJavascriptInterface(new MyJavaScriptInterface(), "HtmlViewer");
        webView6.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webView6.loadUrl("javascript:window.HtmlViewer.showHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
            }
        });

    }


    public void featchData() {

        if (MyApplication.instance.isNetworkAvailable()) {
            urlExtentionPg = urlExtention + pageIdx;
            final String url = MyApplication.BASE_URL + urlExtentionPg;
            webView1.loadUrl(url);
        } else {
            number++;
//            Toast.makeText(MyApplication.APP_CTX,number+"",Toast.LENGTH_LONG).show();
            if (number == 5)
                GetAllDawriNewsReciever.completeWakefulIntent(GetAllDawriNewsReciever.instance.intent);
            else {
                urlExtention = Legue.load((long) (number)).get(0).getNewsUrl();
                featchData();
            }
        }

    }
    public void featchData2() {
        urlExtention = Legue.load((long) (number)).get(0).getNewsUrl();
        leaguId = number;
        if (MyApplication.instance.isNetworkAvailable()) {
            urlExtentionPg = urlExtention + pageIdx;
            final String url = MyApplication.BASE_URL + urlExtentionPg;
            webView2.loadUrl(url);
        } else {
            number++;
//            Toast.makeText(MyApplication.APP_CTX,number+"",Toast.LENGTH_LONG).show();
            if (number == 5)
                GetAllDawriNewsReciever.completeWakefulIntent(GetAllDawriNewsReciever.instance.intent);
            else {
                urlExtention = Legue.load((long) (number)).get(0).getNewsUrl();
                featchData2();
            }
        }

    }

    public void featchData3() {
        urlExtention = Legue.load((long) (number)).get(0).getNewsUrl();
        leaguId = number;
        if (MyApplication.instance.isNetworkAvailable()) {
            urlExtentionPg = urlExtention + pageIdx;
            final String url = MyApplication.BASE_URL + urlExtentionPg;
            webView3.loadUrl(url);
        } else {
            number++;
//            Toast.makeText(MyApplication.APP_CTX,number+"",Toast.LENGTH_LONG).show();
            if (number == 5)
                GetAllDawriNewsReciever.completeWakefulIntent(GetAllDawriNewsReciever.instance.intent);
            else {
                urlExtention = Legue.load((long) (number)).get(0).getNewsUrl();
                featchData3();
            }
        }

    }

    public void featchData4() {
        urlExtention = Legue.load((long) (number)).get(0).getNewsUrl();
        leaguId = number;
        if (MyApplication.instance.isNetworkAvailable()) {
            urlExtentionPg = urlExtention + pageIdx;
            final String url = MyApplication.BASE_URL + urlExtentionPg;
            webView4.loadUrl(url);
        } else {
            number++;
//            Toast.makeText(MyApplication.APP_CTX,number+"",Toast.LENGTH_LONG).show();
            if (number == 5)
                GetAllDawriNewsReciever.completeWakefulIntent(GetAllDawriNewsReciever.instance.intent);
            else {
                urlExtention = Legue.load((long) (number)).get(0).getNewsUrl();
                featchData4();
            }
        }

    }

    public void featchData5() {
        urlExtention = Legue.load((long) (number)).get(0).getNewsUrl();
        leaguId = number;
        if (MyApplication.instance.isNetworkAvailable()) {
            urlExtentionPg = urlExtention + pageIdx;
            final String url = MyApplication.BASE_URL + urlExtentionPg;
            webView5.loadUrl(url);
        } else {
            number++;
//            Toast.makeText(MyApplication.APP_CTX,number+"",Toast.LENGTH_LONG).show();
            if (number == 5)
                GetAllDawriNewsReciever.completeWakefulIntent(GetAllDawriNewsReciever.instance.intent);
            else {
                urlExtention = Legue.load((long) (number)).get(0).getNewsUrl();
                featchData5();
            }
        }

    }

    public void featchData6() {
        urlExtention = Legue.load((long) (number)).get(0).getNewsUrl();
        leaguId = number;
        if (MyApplication.instance.isNetworkAvailable()) {
            urlExtentionPg = urlExtention + pageIdx;
            final String url = MyApplication.BASE_URL + urlExtentionPg;
            webView6.loadUrl(url);
        } else {
            number++;
//            Toast.makeText(MyApplication.APP_CTX,number+"",Toast.LENGTH_LONG).show();
            if (number == 5)
                GetAllDawriNewsReciever.completeWakefulIntent(GetAllDawriNewsReciever.instance.intent);
            else {
                urlExtention = Legue.load((long) (number)).get(0).getNewsUrl();
                featchData6();
            }
        }

    }



    class MyJavaScriptInterface {


        @JavascriptInterface
        @SuppressWarnings("unused")
        public void showHTML(final String html) {
            String htm = html;
            Document doc = Jsoup.parse(html);

            try {
                Element newsList = doc.getElementsByClass("newsList").first();
                List<News> allNewsTmp = new ArrayList<>();
                Element ul_news_list = newsList.getElementsByTag("ul").first();

                for (Element li : ul_news_list.getElementsByTag("li")) {

                    Element a = li.getElementsByTag("a").first();
                    Element img = a.getElementsByTag("img").first();
                    String imgUrl = img.attr("src");

                    Element div = li.getElementsByTag("div").first();
                    a = div.getElementsByTag("a").first();
                    Element p = div.getElementsByTag("p").first();
                    Element strong = a.getElementsByTag("strong").first();
                    String title = strong.text();
                    String subTitle = p.text();


                    News news = new News();
                    news.setUrl(a.attr("href"));
                    news.setImgUrl(imgUrl.substring(0, imgUrl.indexOf("&")));
                    news.setSubTitle(subTitle);
                    news.setTitle(title);
                    news.save();
                    if (isLeague) {
                        LeaguNews leaguNews1 = new LeaguNews();
                        leaguNews1.setLeaguId(leaguId);
                        leaguNews1.setNewsId(news.getId());
                        leaguNews1.setPageIdx(pageIdx);
                        leaguNews1.setIsSeen(false);
                        leaguNews1.save();
                    } else {

                        TeamNews teamNew = new TeamNews();
                        teamNew.setTeamId(leaguId);
                        teamNew.setNewsId(news.getId());
                        teamNew.setPageIdx(pageIdx);
                        teamNew.setIsSeen(false);
                        teamNew.save();
                    }
                    //adapter.notifyDataSetChanged();
                }

            } catch (Exception e) {
                String st = e.getMessage();


            } finally {

                number++;
//                Toast.makeText(MyApplication.APP_CTX,number+"",Toast.LENGTH_LONG).show();
                if (number == 6)
                    GetAllDawriNewsReciever.completeWakefulIntent(GetAllDawriNewsReciever.instance.intent);


            }


        }


    }

}


