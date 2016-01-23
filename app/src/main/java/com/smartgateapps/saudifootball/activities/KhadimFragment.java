package com.smartgateapps.saudifootball.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartgateapps.saudifootball.Adapter.ViewPagerAdapter;
import com.smartgateapps.saudifootball.R;
import com.smartgateapps.saudifootball.saudi.MyApplication;

/**
 * Created by Raafat on 30/12/2015.
 */
public class KhadimFragment extends Fragment {
    private Fragment newsListFragment;
    private Fragment playerGoalerFragment;
    private Fragment matchFragmetn ;
    private Fragment teamListFragment;
    private ViewPagerAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            newsListFragment = new NewsListFragment();
            playerGoalerFragment = new PlayerGoalerFragment();
            matchFragmetn  = new MatchFragment();
            teamListFragment = new TeamListFragment();

            Bundle args = new Bundle();
            args.putString("URL_EXT", MyApplication.KHADIM_ALHARAMIN_NEWS_EXT);
            args.putInt("RES", R.layout.news_card_layout_2);
            args.putInt("LEAGUE_ID",3);
            newsListFragment.setArguments(args);

            Bundle args2 = new Bundle();
            args2.putInt("RES", R.layout.fragment_player_goaler_item);
            args2.putString("URL_EXT", MyApplication.KHADIM_ALHARAMIN_EXT + MyApplication.SCORERS_CM);
            playerGoalerFragment.setArguments(args2);

            Bundle args3 = new Bundle();
            args3.putString("URL_EXT",MyApplication.KHADIM_ALHARAMIN_EXT+MyApplication.MATCHES_CM);
            matchFragmetn.setArguments(args3);

            Bundle args5 = new Bundle();
            args5.putString("URL_EXT",MyApplication.KHADIM_ALHARAMIN_EXT+MyApplication.TEAMS_CM);
            args5.putInt("LEAGUE_ID", 3);
            teamListFragment.setArguments(args5);

            adapter = new ViewPagerAdapter(getChildFragmentManager()); // not the parent fragmetmanager

            adapter.addFrag(newsListFragment, "أخبار");
            adapter.addFrag(matchFragmetn, "المباريات");
            adapter.addFrag(teamListFragment, "الفرق");
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
        return view;

    }
}
