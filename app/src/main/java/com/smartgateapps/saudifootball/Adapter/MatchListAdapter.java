package com.smartgateapps.saudifootball.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartgateapps.saudifootball.R;
import com.smartgateapps.saudifootball.model.Match;

import java.util.List;

/**
 * Created by Raafat on 16/01/2016.
 */
public class MatchListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Match> data;
    private Context ctx;
    private int res;
    private LayoutInflater inflater;
    private int lastPosition = -1;


    public MatchListAdapter(Context context, int resource, List<Match> objects) {
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
        LinearLayout headerLayout = (LinearLayout)convertView.findViewById(R.id.headerLinearLayout);
        LinearLayout childLayout = (LinearLayout)convertView.findViewById(R.id.childLinearLayout);

        if(!currMatch.isHeader()) {
            childLayout.setVisibility(View.VISIBLE);
            headerLayout.setVisibility(View.GONE);
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
        }else{
            headerLayout.setVisibility(View.VISIBLE);
            childLayout.setVisibility(View.GONE);
            TextView matchDateTxtV = (TextView)convertView.findViewById(R.id.matchDateTxtV);
            matchDateTxtV.setText(currMatch.getDate());
        }
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public Match getItem(int position) {
        return this.data.get(position);
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(ctx, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

}
