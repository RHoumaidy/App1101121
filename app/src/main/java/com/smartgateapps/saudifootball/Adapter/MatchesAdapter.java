package com.smartgateapps.saudifootball.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.smartgateapps.saudifootball.R;
import com.smartgateapps.saudifootball.model.Match;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.List;

/**
 * Created by Raafat on 16/12/2015.
 */
public class MatchesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StickyRecyclerHeadersAdapter {

    private List<Match> data;
    private Context ctx;
    private int res;
    private LayoutInflater inflater;
    private int lastPosition = -1;


    public MatchesAdapter(Context context, int resource, List<Match> objects) {
        this.data = objects;
        this.ctx = context;
        this.res = resource;
        this.inflater = LayoutInflater.from(ctx);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.inflater.inflate(res, parent, false);

        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        View convertView = holder.itemView;
        Match currMatch = this.getItem(position);


        TextView matchTimeTxtV = (TextView) convertView.findViewById(R.id.matchTimeTxtV);
        TextView matchTeamRTxtV = (TextView) convertView.findViewById(R.id.matchTeamRTxtV);
        TextView matchTeamLTxtV = (TextView) convertView.findViewById(R.id.matchTeamLTxtV);
        TextView matchReslutRTxtV = (TextView) convertView.findViewById(R.id.matchResultRTxtV);
        TextView matchResultLTxtV = (TextView) convertView.findViewById(R.id.matchResultLTxtV);

        matchTimeTxtV.setText((currMatch.getTime()));
        matchTeamRTxtV.setText((currMatch.getTeamR()));


        matchReslutRTxtV.setText((currMatch.getResultR()));
        matchResultLTxtV.setText((currMatch.getResultL()));
        matchTeamLTxtV.setText((currMatch.getTeamL()));
        setAnimation(convertView, position);

    }

    @Override
    public long getHeaderId(int position) {
        return this.getItem(position).gethId();
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = this.inflater.inflate(R.layout.fragment_header_layout, parent, false);

        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView matchDateTxtV = (TextView) holder.itemView.findViewById(R.id.matchDateTxtV);
        matchDateTxtV.setText(this.getItem(position).getDate());

    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public Match getItem(int position) {
        return this.data.get(position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(ctx, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

//    @Override
//    public int getCount() {
//        return this.data.size();
//    }
//

//
//    @Override
//    public int getPosition(Match item) {
//        return this.data.indexOf(item);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        Match currMatch = this.getItem(position);
//
//        if (convertView == null)
//            convertView = this.inflater.inflate(res, null);
//
//
//        TextView matchTimeTxtV = (TextView) convertView.findViewById(R.id.matchTimeTxtV);
//        TextView matchTeamRTxtV = (TextView) convertView.findViewById(R.id.matchTeamRTxtV);
//        TextView matchTeamLTxtV = (TextView) convertView.findViewById(R.id.matchTeamLTxtV);
//        TextView matchReslutRTxtV = (TextView) convertView.findViewById(R.id.matchResultRTxtV);
//        TextView matchResultLTxtV = (TextView) convertView.findViewById(R.id.matchResultLTxtV);
//
//        matchTimeTxtV.setText((currMatch.getTime()));
//        matchTeamRTxtV.setText((currMatch.getTeamR()));
//
//
//        matchReslutRTxtV.setText((currMatch.getResultR()));
//        matchResultLTxtV.setText((currMatch.getResultL()));
//        matchTeamLTxtV.setText((currMatch.getTeamL()));
//
//
//        return convertView;
//    }
//
//    @Override
//    public View getHeaderView(int position, View convertView, ViewGroup parent) {
//        if (convertView == null)
//            convertView = this.inflater.inflate(R.layout.fragment_header_layout, null);
//
//        TextView matchDateTxtV = (TextView) convertView.findViewById(R.id.matchDateTxtV);
//        matchDateTxtV.setText(this.getItem(position).getDate());
//        return convertView;
//    }
//
//    @Override
//    public long getHeaderId(int position) {
//        return this.getItem(position).gethId();
//    }
}
