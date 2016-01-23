package com.smartgateapps.saudifootball.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smartgateapps.saudifootball.Adapter.DividerItemDecoration;
import com.smartgateapps.saudifootball.Adapter.PlayerGoalerAdapter;
import com.smartgateapps.saudifootball.R;
import com.smartgateapps.saudifootball.model.Player;
import com.smartgateapps.saudifootball.saudi.MyApplication;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Raafat on 26/12/2015.
 */
public class PlayersFragment extends Fragment {


    private List<Player> playerList;
    private PlayerGoalerAdapter adapter;
    private String urlExtention;
    private WebView webView;
    private RecyclerView recyclerView;
    private LinearLayout progressBarLL;
    private TextView progressBarTxtV;
    private RelativeLayout relativeLayout;

    private Timer timer;
    private TimerTask timerTask;
    private String[] waiting = new String[]{"يرجى الإنتظار ", "يرجى الإنتظار .", "يرجى الإنتظار ..", "يرجى الإنتظار ..."};
    private int idx = 0;

    public PlayersFragment() {

        playerList = new ArrayList<>();
        webView = new WebView(MyApplication.APP_CTX);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(false);
        //setListShown(false);
//        featchData();
        timer = new Timer();


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        urlExtention = args.getString("URL_EXT");
        adapter = new PlayerGoalerAdapter(getActivity(), R.layout.fragment_player_goaler_item,
                R.layout.fragment_header_layout, playerList);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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

        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    @Override
    public void onResume() {
        super.onResume();
//        featchData();
    }

    private void setListShown(boolean b) {
        int listViewVisibility = b ? View.VISIBLE : View.GONE;
        int progressBarVisibility = b ? View.GONE : View.VISIBLE;


        recyclerView.setVisibility(listViewVisibility);
        progressBarLL.setVisibility(progressBarVisibility);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_player_goaler_layout, container, false);
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

        recyclerView = ((RecyclerView) view.findViewById(R.id.playerGoalerListView));
        progressBarLL = (LinearLayout) view.findViewById(R.id.playerGoalerProgressBarLL);
        progressBarTxtV = (TextView) view.findViewById(R.id.playerGoalerProgressBarTxtV);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.listRelativeLayout);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setNestedScrollingEnabled(true);
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(adapter);
        recyclerView.addItemDecoration(headersDecor);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        adapter.registerAdapterDataObserver((new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        }));
        recyclerView.setAdapter(adapter);
        if (playerList.size() == 0) {
            setListShown(false);
            featchData();
        } else
            setListShown(true);

        checkInternet();

    }

    private void featchData() {
        if (!MyApplication.instance.isNetworkAvailable()) {
            try {
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
            webView.addJavascriptInterface(new MyJavaScriptInterface(), "HtmlViewer");
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    webView.loadUrl("javascript:window.HtmlViewer.showHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                }
            });

            //String url = MyApplication.BASE_URL+MyApplication.ABD_ALATIF_EXT;
            webView.stopLoading();
            webView.loadUrl(MyApplication.BASE_URL + urlExtention);
        }
    }

    class MyJavaScriptInterface {


        @JavascriptInterface
        @SuppressWarnings("unused")
        public void showHTML(final String html) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String htm = html;
                    Document doc = Jsoup.parse(html);
                    try {
                        Element mb20 = doc.getElementsByClass("mb20").first();
                        Element center = mb20.getElementsByTag("center").first();
                        Element table = center.getElementsByTag("table").first();
                        Element tbody = table.getElementsByTag("tbody").first();
                        Elements trs = tbody.getElementsByTag("tr");

                        int pos = 0;
                        List<Player> tmpList = new ArrayList<>();

                        for (Element tr : trs) {
                            Player player = new Player();
                            Elements tds = tr.getElementsByTag("td");
                            if (tds.size() == 2) {
                                pos++;
                            } else {
                                Element pNtd = tds.get(0);
                                Element pNatd = tds.get(2);
                                Element pMItd = tds.get(4);
                                Element pMtd = tds.get(5);

                                Element font = pNtd.getElementsByTag("font").first();
                                player.setNumber(font.text());

                                Element a = pNatd.getElementsByTag("a").first();
                                player.setPlayerUrl(a.attr("href"));
                                player.setPlayerName(a.text());
                                player.setPos(pos);

                                Element img = pMItd.getElementsByTag("img").first();
                                if (img != null)
                                    player.setMonImgUrl(img.attr("src"));

                                a = pMtd.getElementsByTag("a").first();
                                if (a != null)
                                    player.setMontakhab(a.text());

                                tmpList.add(player);

                            }
                        }

                        Collections.sort(tmpList);
                        playerList.clear();
                        playerList.addAll(tmpList);
                    } catch (Exception e) {
                        Toast.makeText(MyApplication.APP_CTX, R.string.toast_featch_data_error, Toast.LENGTH_LONG).show();
                    } finally {


                        adapter.notifyDataSetChanged();
                        try {
                            setListShown(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
        }

    }
}
