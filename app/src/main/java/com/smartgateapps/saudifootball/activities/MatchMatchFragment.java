package com.smartgateapps.saudifootball.activities;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import com.smartgateapps.saudifootball.Adapter.MatchAdapterAdapter;
import com.smartgateapps.saudifootball.R;
import com.smartgateapps.saudifootball.model.Match;
import com.smartgateapps.saudifootball.model.MatchMatch;
import com.smartgateapps.saudifootball.saudi.MyApplication;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Raafat on 15/01/2016.
 */
public class MatchMatchFragment extends Fragment {

    private List<MatchMatch> mathList;
    private MatchAdapterAdapter adapter;
    private String urlExtention;
    private WebView webView;
    private RecyclerView recyclerView;
    private LinearLayout progressBarLL;
    private TextView progressBarTxtV;

    private Timer timer;
    private TimerTask timerTask;
    private String[] waiting = new String[]{"يرجى الإنتظار ", "يرجى الإنتظار .", "يرجى الإنتظار ..", "يرجى الإنتظار ..."};
    private int idx = 0;
    private int prevSpinnerSelected = 0;

    private RelativeLayout relativeLayout;

    public MatchMatchFragment() {
        mathList = new ArrayList<>();

        webView = new WebView(MyApplication.APP_CTX);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(false);
        webView.getSettings().setGeolocationEnabled(true);
        //setListShown(false);
//        featchData();
        timer = new Timer();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        urlExtention = args.getString("URL_EXT");

        adapter = new MatchAdapterAdapter(getActivity(), R.layout.match_match_item_layout, R.layout.match_mathc_header_layut, mathList);

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
        if (mathList.size() == 0) {
            setListShown(false);
            featchData();
        } else
            setListShown(true);
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

    private int getLayoutManagerOrientation(int activityOrientation) {
        if (activityOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            return LinearLayoutManager.VERTICAL;
        } else {
            return LinearLayoutManager.HORIZONTAL;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        recyclerView = ((RecyclerView) view.findViewById(R.id.playerGoalerListView));
        progressBarLL = (LinearLayout) view.findViewById(R.id.playerGoalerProgressBarLL);
        progressBarTxtV = (TextView) view.findViewById(R.id.playerGoalerProgressBarTxtV);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.listRelativeLayout);


        int orientation = getLayoutManagerOrientation(getResources().getConfiguration().orientation);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), orientation, false));

        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(adapter);
        recyclerView.addItemDecoration(headersDecor);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        adapter.registerAdapterDataObserver((new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        }));
        recyclerView.setAdapter(adapter);


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

                        Element matchesTable = doc.getElementById("matchesTable");
                        Element tbody = matchesTable.getElementsByTag("tbody").first();

                        Elements trs = tbody.getElementsByTag("tr");

                        List<MatchMatch> tmpList = new ArrayList<>();
                        List<Match> tmpChildList = new ArrayList<>();

                        int hIdx1 = 0;
                        int hIdx2 = 0;
                        String date = "";
                        String leageName = "";
                        String leagoIconUrl = "";

                        for (int i = 1; i < trs.size(); ++i) {

                            Elements tds = trs.get(i).getElementsByTag("td");

                            String time;
                            String teamR;
                            String resultR;
                            String resultL;
                            String teamL;

                            if (trs.get(i).attr("class").equalsIgnoreCase("wNoteTr"))
                                continue;

                            if (tds.size() == 1) { // this is a date header
                                if (tds.get(0).attr("class").equalsIgnoreCase("match_league")) {
                                    if (tmpChildList.size() > 0) {
                                        MatchMatch matchMatch = new MatchMatch();
                                        matchMatch.setHdId(hIdx1);
                                        matchMatch.setLeague(leageName);
                                        matchMatch.setLeagueImageUrl(leagoIconUrl);
                                        matchMatch.setMatches(tmpChildList);
                                        tmpList.add(matchMatch);
                                    }
                                    hIdx2 = 0;
                                    hIdx1 ++;
                                    leageName = tds.get(0).text();
                                    leagoIconUrl = tds.get(0).getElementsByTag("img").attr("src");
                                    tmpChildList.clear();


                                } else if (tds.get(0).attr("class").equalsIgnoreCase("match_date")) {
                                    hIdx2++;
                                    date = tds.first().text();
                                    Match header = new Match();
                                    header.setIsHeader(true);
                                    header.setDate(date);
                                    tmpChildList.add(header);
                                }
                            } else {

                                time = tds.get(1).text();
                                Element teamD = tds.get(2);
                                Element teamF = teamD.getElementsByTag("font").first();
                                if (teamF != null)
                                    teamR = teamF.text();
                                else
                                    teamR = tds.get(2).text();
                                Element tdRes = tds.get(3);
                                resultR = Html.fromHtml(tdRes.text()).toString();
                                resultL = resultR.split(":")[0].replaceAll("\\s+", "");
                                resultR = resultR.split(":")[1].replaceAll("\\s+", "");

                                teamD = tds.get(4);
                                teamF = teamD.getElementsByTag("font").first();
                                if (teamF != null)
                                    teamL = teamF.text();
                                else
                                    teamL = tds.get(4).text();

                                Match match = new Match();

                                match.setDate(date);
                                match.sethId(hIdx2);
                                match.setTime(time);
                                match.setTeamR(teamR);
                                match.setTeamL(teamL);
                                match.setResultL(resultL);
                                match.setResultR(resultR);

                                tmpChildList.add(match);
                            }
                        }

                        MatchMatch matchMatch = new MatchMatch();
                        matchMatch.setHdId(hIdx1);
                        matchMatch.setLeague(leageName);
                        matchMatch.setLeagueImageUrl(leagoIconUrl);
                        matchMatch.setMatches(tmpChildList);
                        tmpList.add(matchMatch);
                        mathList.clear();
                        mathList.addAll(tmpList);

                    } catch (Exception e) {
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
                    }

                    adapter.notifyDataSetChanged();
                    try {
                        setListShown(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }

    }
}
