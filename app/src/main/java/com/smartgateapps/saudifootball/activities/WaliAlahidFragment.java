package com.smartgateapps.saudifootball.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseAnalytics;
import com.smartgateapps.saudifootball.Adapter.ViewPagerAdapter;
import com.smartgateapps.saudifootball.R;
import com.smartgateapps.saudifootball.model.Legue;
import com.smartgateapps.saudifootball.saudi.MyApplication;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class WaliAlahidFragment extends Fragment {

    private Fragment newsListFragment;
    private Fragment playerGoalerFragment;
    private Fragment matchFragmetn;
    private Fragment teamListFragment;
    private ViewPagerAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private int leagueId = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {

            newsListFragment = new NewsListFragment();
            playerGoalerFragment = new PlayerGoalerFragment();
            matchFragmetn = new MatchFragment();
            teamListFragment = new TeamListFragment();


            Bundle args = new Bundle();
            args.putString("URL_EXT", MyApplication.WALI_ALAHID_NEWS_EXT);
            args.putInt("RES", R.layout.news_card_layout_2);
            args.putInt("LEAGUE_ID", 2);
            newsListFragment.setArguments(args);

            Bundle args2 = new Bundle();
            args2.putInt("RES", R.layout.fragment_player_goaler_item);
            args2.putString("URL_EXT", MyApplication.WALI_ALAHID_EXT + MyApplication.SCORERS_CM);
            playerGoalerFragment.setArguments(args2);

            Bundle args3 = new Bundle();
            args3.putString("URL_EXT", MyApplication.WALI_ALAHID_EXT + MyApplication.MATCHES_CM);
            args3.putInt("LEAGUE_ID", 2);
            matchFragmetn.setArguments(args3);

            Bundle args5 = new Bundle();
            args5.putString("URL_EXT", MyApplication.WALI_ALAHID_EXT + MyApplication.TEAMS_CM);
            args5.putInt("LEAGUE_ID", 2);
            teamListFragment.setArguments(args5);

            adapter = new ViewPagerAdapter(getChildFragmentManager()); // not the parent fragmetmanager

            adapter.addFrag(newsListFragment, "أخبار");
            adapter.addFrag(teamListFragment, "الفرق");
            adapter.addFrag(matchFragmetn, "المباريات");
            adapter.addFrag(playerGoalerFragment, "الهدافين");

            tabLayout = ((MainActivity) getActivity()).tabLayout;
            tabLayout.removeAllTabs();

        }
    }

    @Override
    public void onPause() {
        tabLayout.setVisibility(View.GONE);
        super.onPause();

    }

    @Override
    public void onResume() {
        tabLayout.setVisibility(View.VISIBLE);
        super.onResume();

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tabs_layout, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position == 2) {
                    Map<String, String> dimensions = new HashMap<>();
                    dimensions.put("category", "استعراض مباريات : " + Legue.load((long) leagueId).get(0).getName());
                    ParseAnalytics.trackEventInBackground("open_match", dimensions);
                    dimensions.put("category", "استعراض مباريات");
                    ParseAnalytics.trackEventInBackground("open_match", dimensions);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Map<String, String> dimensions = new HashMap<>();
        dimensions.put("category", "كأس ولي العهد");
        dimensions.put("dayType", "weekday");
        ParseAnalytics.trackEventInBackground("open_league", dimensions);

        return view;

    }

}
