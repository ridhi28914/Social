package com.application.social.utils.Facebook;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout_facebook, viewGroup, false);
        return new FbAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FbAdapter.ViewHolder viewHolder, int i) {
        viewHolder.tv_message.setText(fb_versions.get(i).getMessage());
        viewHolder.tv_story.setText(fb_versions.get(i).getStory());
        viewHolder.tv_likes.setText(fb_versions.get(i).getLikes());
        viewHolder.tv_comments.setText(fb_versions.get(i).getComments());
        if(fb_versions.get(i).getImage_url() != "null") {
            Picasso.with(context).load(fb_versions.get(i).getImage_url()).resize(200, 200).into(viewHolder.iv_image);
        }
    }

    @Override
    public int getItemCount() {
        return fb_versions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_message;
        TextView tv_story;
        TextView tv_likes;
        TextView tv_comments;
        ImageView iv_image;

        public ViewHolder(View view) {
            super(view);
            tv_message = (TextView)view.findViewById(R.id.tv_message);
            tv_story = (TextView)view.findViewById(R.id.tv_story);
            tv_likes = (TextView)view.findViewById(R.id.tv_likes);
            tv_comments = (TextView)view.findViewById(R.id.tv_comments);
            iv_image = (ImageView)view.findViewById(R.id.iv_image);
        }
    }

}
