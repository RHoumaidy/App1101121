package com.smartgateapps.saudifootball.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smartgateapps.saudifootball.Adapter.EndlessRecyclerOnScrollListener;
import com.smartgateapps.saudifootball.Adapter.NewsRecyclerViewAdapter;
import com.smartgateapps.saudifootball.Adapter.WrappingLinearLayoutMgr;
import com.smartgateapps.saudifootball.R;
import com.smartgateapps.saudifootball.model.LeaguNews;
import com.smartgateapps.saudifootball.model.News;
import com.smartgateapps.saudifootball.model.TeamNews;
import com.smartgateapps.saudifootball.saudi.MyApplication;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Raafat on 17/11/2015.
 */
public class NewsListFragment extends android.support.v4.app.Fragment {

    private SwipeRefreshLayout refreshLayout = new SwipeRefreshLayout(MyApplication.APP_CTX);
    private NewsRecyclerViewAdapter adapter;
    private RecyclerView recyclerView = new RecyclerView(MyApplication.APP_CTX);
    private LinearLayout progressBarLL = new LinearLayout(MyApplication.APP_CTX);
    private TextView progressBarTxtV;
    public String urlExtention;
    private String urlExtentionPg;
    private WebView webView;
    private List<News> allNews;
    public boolean isLeague;

    private Timer timer;
    private TimerTask timerTask;
    private String[] waiting = new String[]{"يرجى الإنتظار ", "يرجى الإنتظار .", "يرجى الإنتظار ..", "يرجى الإنتظار ..."};
    private int idx = 0;

    public int pageIdx = 1;
    public int leaguId;
    private int res;


    private RelativeLayout relativeLayout;


    public NewsListFragment() {
        allNews = new ArrayList<>();
        webView = new WebView(MyApplication.APP_CTX);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(false);

        // featchData();
        timer = new Timer();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        res = args.getInt("RES");
        adapter = new NewsRecyclerViewAdapter(allNews, res);
        urlExtention = args.getString("URL_EXT");
        leaguId = args.getInt("LEAGUE_ID");
        isLeague = args.getBoolean("IS_LEAGUE", true);

    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        timerTask = new TimerTask() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBarTxtV.setText(waiting[idx++]);
                        idx %= 4;
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 800);


    }

    @Override
    public void onResume() {
        super.onResume();


        if (allNews.size() == 0) {
            List<News> tmp;
            if (isLeague)
                tmp = LeaguNews.getAllNewsForLeagu(leaguId, 1);
            else
                tmp = TeamNews.getAllNewsForTeam(leaguId, 1);
            if (tmp.size() > 0)
                allNews.addAll(tmp);
            else
                featchData();
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.news_list_fragment_layout, container, false);
    }

    @Override
    public void onPause() {
        super.onPause();
        refreshLayout.setRefreshing(false);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void checkInternet() {
        if (!MyApplication.instance.isNetworkAvailable()) {
            Snackbar snackbar = Snackbar.make(relativeLayout, "لا يوجد اتصال بالانترنت", Snackbar.LENGTH_INDEFINITE)
                    .setAction("اعد المحاولة", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkInternet();
                        }
                    });
            snackbar.setActionTextColor(Color.RED);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);

            snackbar.show();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.newsSwipList);
        recyclerView = (RecyclerView) view.findViewById(R.id.newsListRycV);
        progressBarLL = (LinearLayout) view.findViewById(R.id.newsListProgressBarLL);
        progressBarTxtV = (TextView) view.findViewById(R.id.newsListProgressBarTxtV);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.listRelativeLayout);


//        recyclerView.addItemDecoration(new SpacesItemDecoration(10));
        recyclerView.setHasFixedSize(true);
        GridLayoutManager lnLayoutMgr;
        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE && res != R.layout.news_card_layout_2)
            lnLayoutMgr = new GridLayoutManager(getActivity(), 2);
        else
            lnLayoutMgr = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(lnLayoutMgr);
        recyclerView.setAdapter(adapter);


        final GestureDetector mGestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && mGestureDetector.onTouchEvent(e)) {
                    int pos = rv.getChildAdapterPosition(child);
                    News news = allNews.get(pos);
                    Intent intent1 = new Intent(getActivity(), NewsDetailsActivity.class);
                    intent1.putExtra("NEW", news);
                    intent1.putExtra("LEAGUE_ID", leaguId);
                    intent1.putExtra("IS_LEAGUE", isLeague);
                    getActivity().startActivity(intent1);

                    return true;
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(lnLayoutMgr) {
            @Override
            public void onLoadMore(int current_page) {
                pageIdx = current_page;
                if (pageIdx > 10)
                    return;


                List<News> tmp = new ArrayList<News>();
                if (isLeague)
                    tmp = LeaguNews.getAllNewsForLeagu(leaguId, pageIdx);
                else
                    tmp = TeamNews.getAllNewsForTeam(leaguId, pageIdx);
                if (tmp.size() > 0)
                    allNews.addAll(tmp);
                else {
                    //current_page = 1;
                    featchData();
                }

                adapter.notifyDataSetChanged();

            }
        });


        refreshLayout.setEnabled(false);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                featchData();
            }
        });

        checkInternet();

    }


    public void featchData() {
        try {

            if (allNews.size() > 0)
                refreshLayout.setRefreshing(true);
            else
                progressBarLL.setVisibility(View.VISIBLE);
        } catch (Exception e) {

        }
        if (!MyApplication.instance.isNetworkAvailable()) {
            try {
                refreshLayout.setRefreshing(false);
                Snackbar snackbar = Snackbar.make(relativeLayout, "لا يوجد اتصال بالانترنت", Snackbar.LENGTH_INDEFINITE)
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
            } catch (Exception e) {


            }
        } else {
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
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    String htm = html;
                    Document doc = Jsoup.parse(html);

                    try {
                        Element newsList = doc.getElementsByClass("newsList").first();
                        List<News> allNewsTmp = new ArrayList<>();
                        Element ul_news_list = newsList.getElementsByTag("ul").first();

                        Elements lis = ul_news_list.getElementsByTag("li");
                        for (int i = lis.size() - 1; i >= 0; i--) {
                            Element li = lis.get(i);

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
                                LeaguNews leaguNews = new LeaguNews();
                                leaguNews.setLeaguId(leaguId);
                                leaguNews.setNewsId(news.getId());
                                leaguNews.setPageIdx(pageIdx);
                                leaguNews.setIsSeen(true);
                                leaguNews.save();
                            } else {
                                TeamNews teamNew = new TeamNews();
                                teamNew.setTeamId(leaguId);
                                teamNew.setNewsId(news.getId());
                                teamNew.setPageIdx(pageIdx);
                                teamNew.setIsSeen(true);
                                teamNew.save();
                            }

                            allNewsTmp.add(news);
                            //adapter.notifyDataSetChanged();
                        }

                        allNews.addAll(allNewsTmp);
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        String st = e.getMessage();
                        Snackbar snackbar = Snackbar.make(relativeLayout, "نأسف حدث حطأ في جلب بعض البيانات!", Snackbar.LENGTH_INDEFINITE)
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

                    } finally {


                    }


                    try {
                        if (progressBarLL.getVisibility() == View.VISIBLE) {
                            progressBarLL.setVisibility(View.GONE);
                            refreshLayout.setVisibility(View.VISIBLE);
                        }
                        refreshLayout.setRefreshing(false);
                    } catch (Exception e) {

                    }
                }
            });


        }

    }

}
