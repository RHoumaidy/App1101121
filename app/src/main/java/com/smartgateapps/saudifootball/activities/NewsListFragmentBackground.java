package com.smartgateapps.saudifootball.activities;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.smartgateapps.saudifootball.model.LeaguNews;
import com.smartgateapps.saudifootball.model.News;
import com.smartgateapps.saudifootball.saudi.MyApplication;

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
    private WebView webView;
    public boolean isLeague;


    public int pageIdx = 1;
    public int leaguId;

    public NewsListFragmentBackground() {
        webView = new WebView(MyApplication.APP_CTX);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(false);

    }


    public void featchData() {

        if (MyApplication.instance.isNetworkAvailable()) {
            urlExtentionPg = urlExtention + pageIdx;
            final String url = MyApplication.BASE_URL + urlExtentionPg;

            webView.addJavascriptInterface(new MyJavaScriptInterface(), "HtmlViewer");
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    webView.loadUrl("javascript:window.HtmlViewer.showHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                }
            });

            //String url = MyApplication.BASE_URL+MyApplication.ABD_ALATIF_EXT;
            webView.stopLoading();
            webView.loadUrl(url);
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
                    if(isLeague) {
                        LeaguNews leaguNews1 = new LeaguNews();
                        leaguNews1.setLeaguId(leaguId);
                        leaguNews1.setNewsId(news.getId());
                        leaguNews1.setPageIdx(pageIdx);
                        leaguNews1.setIsSeen(false);
                        leaguNews1.save();
                    }else{

                    }
                    LeaguNews leaguNews2 = new LeaguNews();
                    leaguNews2.setLeaguId(0);
                    leaguNews2.setNewsId(news.getId());
                    leaguNews2.setPageIdx(pageIdx);
                    leaguNews2.setIsSeen(false);
                    leaguNews2.save();


                    //adapter.notifyDataSetChanged();
                }

            } catch (Exception e) {
                String st = e.getMessage();


            } finally {


            }


        }


    }

}


