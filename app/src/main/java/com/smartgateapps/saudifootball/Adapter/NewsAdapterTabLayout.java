package com.smartgateapps.saudifootball.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartgateapps.saudifootball.R;
import com.smartgateapps.saudifootball.model.News;
import com.smartgateapps.saudifootball.saudi.BlurBuilder;
import com.smartgateapps.saudifootball.saudi.MyApplication;
import com.squareup.picasso.Callback;

import java.util.List;

/**
 * Created by Raafat on 21/01/2016.
 */
public class NewsAdapterTabLayout  extends ArrayAdapter<News>{

    private List<News> data;
    private int res;
    private Context ctx;
    private LayoutInflater inflater;

    public NewsAdapterTabLayout(Context context, int resource, List<News> objects) {
        super(context, resource, objects);
        this.ctx =context;
        this.res = resource;
        this.data = objects;
        this.inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return this.data.size();
    }

    @Override
    public News getItem(int position) {
        return this.data.get(position);
    }

    @Override
    public int getPosition(News item) {
        return this.data.indexOf(item);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
            convertView = this.inflater.inflate(res,parent,false);
        final MyHolder holder = new MyHolder(convertView);
        News currNews = this.getItem(position);

        holder.titleTxtView.setText(currNews.getTitle());
        holder.descTxtView.setText(currNews.getSubTitle());

        if(holder.imageView2 != null) {
            MyApplication.picasso
                    .load(currNews.getImgUrl())
                    .into(holder.imageView2, new Callback() {
                        @Override
                        public void onSuccess() {
                            Bitmap bitmap = ((BitmapDrawable) holder.imageView2.getDrawable()).getBitmap();
                            //bitmap = BlurBuilder.blur(MyApplication.APP_CTX, bitmap);
                            holder.imageView.setImageBitmap(bitmap);
                        }

                        @Override
                        public void onError() {

                        }
                    });
        }else{
            MyApplication.picasso
                    .load(currNews.getImgUrl())
                    .into(holder.imageView);
        }
        return convertView;
    }

    public static class MyHolder extends RecyclerView.ViewHolder{
        TextView titleTxtView;
        TextView descTxtView;
        ImageView imageView;
        ImageView imageView2;

        public MyHolder(View itemView) {
            super(itemView);
            titleTxtView = (TextView) itemView.findViewById(R.id.newsItemTitleTxV);
            descTxtView = (TextView) itemView.findViewById(R.id.newsItemSubTitleTxV);
            imageView = (ImageView) itemView.findViewById(R.id.newsItemImgV);
            imageView2 = (ImageView) itemView.findViewById(R.id.newsItemImgV2);
        }
    }
}
