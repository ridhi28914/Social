package com.application.social.utils.Instagram;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.social.views.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.application.social.views.R.id.listView;
public class InstaImageAdapter extends RecyclerView.Adapter<InstaImageAdapter.ViewHolder> {
    private ArrayList<InstaVersion> android_versions;
    private Context context;

    public InstaImageAdapter(Context context, ArrayList<InstaVersion> android_versions) {
        this.context = context;
        this.android_versions = android_versions;

    }

    @Override
    public InstaImageAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

//        viewHolder.tv_android.setText(android_versions.get(i).getVersion_name());
        Picasso.with(context).load(android_versions.get(i).getImage_url()).resize(120, 60).into(viewHolder.img_android);
    }

    @Override
    public int getItemCount() {
        return android_versions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
//        TextView tv_android;
        ImageView img_android;
        public ViewHolder(View view) {
            super(view);

//            tv_android = (TextView)view.findViewById(R.id.tv_android);
            img_android = (ImageView)view.findViewById(R.id.img_android);
        }
    }
}