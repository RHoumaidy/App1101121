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
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseAnalytics;
import com.smartgateapps.saudifootball.Adapter.DividerItemDecoration;
import com.smartgateapps.saudifootball.Adapter.MatchesAdapter;
import com.smartgateapps.saudifootball.Adapter.SpinnerAdapter;
import com.smartgateapps.saudifootball.R;
import com.smartgateapps.saudifootball.model.Legue;
import com.smartgateapps.saudifootball.model.Match;
import com.smartgateapps.saudifootball.model.Stage;
import com.smartgateapps.saudifootball.saudi.MyApplication;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Raafat on 16/12/2015.
 */
public class MatchFragment extends Fragment {

    private List<Match> mathList;
    private MatchesAdapter adapter;
    private String urlExtention;
    private WebView webView;
    private RecyclerView recyclerView;
    private LinearLayout progressBarLL;
    private LinearLayout matchFilterLL;
    private TextView progressBarTxtV;
    private SpinnerAdapter spinnerAdapter;
    private Spinner stageSpinner;
    private List<Stage> stageList;

    private Timer timer;
    private TimerTask timerTask;
    private String[] waiting = new String[]{"يرجى الإنتظار ", "يرجى الإنتظار .", "يرجى الإنتظار ..", "يرجى الإنتظار ..."};
    private int idx = 0;
    private int prevSpinnerSelected = 0;
    private int leagueId;

    private RelativeLayout relativeLayout;

    public MatchFragment() {
        mathList = new ArrayList<>();
        stageList = new ArrayList<>();


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
        leagueId = args.getInt("LEAGUE_ID");

        adapter = new MatchesAdapter(getActivity(), R.layout.fragment_match_item, mathList);
        spinnerAdapter = new SpinnerAdapter(getActivity(), android.R.layout.simple_list_item_checked, stageList);

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
        matchFilterLL.setVisibility(listViewVisibility);
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
        matchFilterLL = (LinearLayout) view.findViewById(R.id.choseMathcFilterLL);
        stageSpinner = (Spinner) view.findViewById(R.id.matchStageSpinner);
        relativeLayout = (RelativeLayout)view.findViewById(R.id.listRelativeLayout);
        stageSpinner.setAdapter(spinnerAdapter);

        int orientation = getLayoutManagerOrientation(getResources().getConfiguration().orientation);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), orientation, false));

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



        stageSpinner.post(new Runnable() {
            @Override
            public void run() {
                stageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Stage selStage = ((Stage) parent.getItemAtPosition(position));
                        urlExtention = "?" + selStage.getUrl();
                        if (position != prevSpinnerSelected) {
                            featchData();
                            setListShown(false);
                        }

                        prevSpinnerSelected = position;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });

    }

    private void featchData() {

        Map<String, String> dimensions = new HashMap<>();
        dimensions.put("category", "استعراض مباريات : "+Legue.load(Long.valueOf(leagueId)).get(0).getName());
        ParseAnalytics.trackEventInBackground("read", dimensions);
        dimensions.put("category", "استعراض مباريات");
        ParseAnalytics.trackEventInBackground("read", dimensions);

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
            }catch (Exception e){

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

                        Element contentTable = doc.getElementById("contentTable");
                        Element seasonpicker = contentTable.getElementsByClass("seasonpicker").get(1);

                        if (stageList.size() == 0) {
                            int idx = 0;
                            boolean b = false;
                            for (Element option : seasonpicker.getElementsByTag("option")) {
                                String url = option.attr("value");
                                String name = option.text();
                                if (name.equalsIgnoreCase("المراكز"))
                                    continue;
                                Stage stage = new Stage(name, url);
                                String selected = option.attr("selected");
                                if (!option.hasAttr("selected") && !b) {
                                    idx++;
                                } else
                                    b = true;
                                stageList.add(stage);
                            }
                            prevSpinnerSelected = idx;
                            stageSpinner.setSelection(idx);
                            spinnerAdapter.notifyDataSetChanged();
                        }


                        Element tbody = contentTable.getElementsByTag("tbody").first();

                        Elements trs = tbody.getElementsByTag("tr");

                        List<Match> tmpList = new ArrayList<>();

                        int hIdx = 0;
                        String date = "";
                        for (int i = 4; i < trs.size(); ++i) {

                            Elements tds = trs.get(i).getElementsByTag("td");

                            String time;
                            String teamR;
                            String resultR;
                            String resultReX = "0";
                            String resultL;
                            String resultLeX = "0";
                            String teamL;

                            if (trs.get(i).attr("class").equalsIgnoreCase("wNoteTr"))
                                continue;

                            if (tds.size() == 1) { // this is a date header
                                hIdx++;
                                date = tds.first().text();
                            } else {

                                time = tds.get(0).text();
                                Element teamD = tds.get(1);
                                Element teamF = teamD.getElementsByTag("font").first();
                                if (teamF != null)
                                    teamR = teamF.text();
                                else
                                    teamR = tds.get(1).text();
                                Element tdRes = tds.get(2).getElementsByTag("span").first();
                                if(tdRes == null)
                                    tdRes=tds.get(2);
                                Element tdResEx = tds.get(2).getElementsByTag("div").first();
                                resultR = Html.fromHtml(tdRes.text()).toString();
                                resultL = resultR.split(":")[0].replaceAll("\\s+", "");
                                resultR = resultR.split(":")[1].replaceAll("\\s+", "");

                                if(tdResEx != null){
                                    resultReX = Html.fromHtml(tdResEx.text()).toString();
                                    resultLeX = resultReX.split(":")[0].replaceAll("\\s+", "");
                                    resultReX = resultReX.split(":")[1].replaceAll("\\s+", "");
                                    int resRex = Integer.valueOf(resultReX);
                                    int resLeX = Integer.valueOf(resultLeX);
                                    int resR = Integer.valueOf(resultR);
                                    int resL = Integer.valueOf(resultR);

                                    resultR = resR + resRex +"";
                                    resultL = resL +resLeX +"";
                                }

                                teamD = tds.get(3);
                                teamF = teamD.getElementsByTag("font").first();
                                if (teamF != null)
                                    teamL = teamF.text();
                                else
                                    teamL = tds.get(3).text();

                                Match match = new Match();

                                match.setDate(date);
                                match.sethId(hIdx);
                                match.setTime(time);
                                match.setTeamR(teamR);
                                match.setTeamL(teamL);
                                match.setResultL(resultL);
                                match.setResultR(resultR);

                                tmpList.add(match);
                            }

                        }
                        Match m = new Match();
                        m.setDate("جدول مباريات المسابقة غير متوفر حالياً");
                        if (tmpList.size() == 0)
                            tmpList.add(m);
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
