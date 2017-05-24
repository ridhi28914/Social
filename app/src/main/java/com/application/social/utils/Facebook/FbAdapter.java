package com.application.social.utils.Facebook;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.application.social.utils.Instagram.InstaImageAdapter;
import com.application.social.utils.Instagram.InstaVersion;
import com.application.social.views.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ridhi on 5/25/2017.
 */

public class FbAdapter extends RecyclerView.Adapter<FbAdapter.ViewHolder>  {
    private ArrayList<FbVersion> fb_versions;
    private Context context;

    public FbAdapter(Context context, ArrayList<FbVersion> fb_versions) {
        this.context = context;
        this.fb_versions = fb_versions;

    }

    @Override
    public FbAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout, viewGroup, false);
        return new FbAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FbAdapter.ViewHolder viewHolder, int i) {
//
//        viewHolder.tv_android.setText(android_versions.get(i).getVersion_name());
        Picasso.with(context).load(fb_versions.get(i).getImage_url()).resize(120, 60).into(viewHolder.img_android);
    }

    @Override
    public int getItemCount() {
        return fb_versions.size();
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
