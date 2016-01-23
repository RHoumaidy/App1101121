package com.smartgateapps.saudifootball.activities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseAnalytics;
import com.smartgateapps.saudifootball.Adapter.ViewPagerAdapter;
import com.smartgateapps.saudifootball.R;
import com.smartgateapps.saudifootball.saudi.MyApplication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Raafat on 03/12/2015.
 */
public class AbdAlatifFragment extends Fragment {

    private Fragment newsListFragment;
    private Fragment teamsFragment;
    private Fragment playerGoalerFragment;
    private Fragment placesFragmend;
    private Fragment matchFragment;

    private ViewPagerAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {

            Map<String, String> dimensions = new HashMap<>();
            dimensions.put("category", "عبداللطيف جميل");
            dimensions.put("dayType", "weekday");
            ParseAnalytics.trackEventInBackground("read", dimensions);

            newsListFragment = new NewsListFragment();
            teamsFragment = new TeamListFragment();
            playerGoalerFragment = new PlayerGoalerFragment();
            placesFragmend = new PlacedListFragment();
            matchFragment = new MatchFragment();

            Bundle args = new Bundle();
            args.putString("URL_EXT", MyApplication.ABD_ALATIF_NEWS_EXT);
            args.putInt("RES", R.layout.news_card_layout_2);
            args.putInt("LEAGUE_ID", 1);
            newsListFragment.setArguments(args);

            Bundle args2 = new Bundle();
            args2.putString("URL_EXT", MyApplication.ABD_ALATIF_EXT + MyApplication.SCORERS_CM);
            playerGoalerFragment.setArguments(args2);

            Bundle args3 = new Bundle();
            args3.putString("URL_EXT", MyApplication.ABD_ALATIF_EXT + MyApplication.POSES_CM);
            placesFragmend.setArguments(args3);

            Bundle args4 = new Bundle();
            args4.putString("URL_EXT", MyApplication.ABD_ALATIF_EXT + MyApplication.MATCHES_CM);
            args4.putInt("LEAGUE_ID", 1);
            matchFragment.setArguments(args4);

            Bundle args5 = new Bundle();
            args5.putString("URL_EXT", MyApplication.ABD_ALATIF_EXT + MyApplication.TEAMS_CM);
            args5.putInt("LEAGUE_ID", 1);
            teamsFragment.setArguments(args5);

            adapter = new ViewPagerAdapter(getChildFragmentManager()); // not the parent fragmetmanager

            adapter.addFrag(newsListFragment, "أخبار");
            adapter.addFrag(playerGoalerFragment, "الهدافين");
            adapter.addFrag(teamsFragment, "الفرق");
            adapter.addFrag(matchFragment, "المباريات");
            adapter.addFrag(placesFragmend, "المراكز");

            tabLayout = ((MainActivity) getActivity()).tabLayout;

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

        return view;

    }

}
