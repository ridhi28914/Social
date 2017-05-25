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
import com.squareup.picasso.Target;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static com.application.social.views.R.id.listView;
public class InstaImageAdapter extends RecyclerView.Adapter<InstaImageAdapter.ViewHolder> {
    private ArrayList<InstaVersion> insta_versions;
    private Context context;

    public InstaImageAdapter(Context context, ArrayList<InstaVersion> instaversions) {
        this.context = context;
        insta_versions = instaversions;

    }

    @Override
    public InstaImageAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.tv_username.setText(insta_versions.get(i).getUser_name());
        viewHolder.tv_caption.setText(insta_versions.get(i).getCaption());
        viewHolder.tv_likes.setText(insta_versions.get(i).getLikes());
        viewHolder.tv_comments.setText(insta_versions.get(i).getComments());

//        Picasso.with(context).load(insta_versions.get(i).getProfile_picture()).resize(150,150).into(viewHolder.iv_profile);
        Picasso.with(context).load(insta_versions.get(i).getImage_url()).resize(150,150).into(viewHolder.iv_image);
    }

    @Override
    public int getItemCount() {
        return insta_versions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_username;
        TextView tv_caption;
        ImageView iv_image;
        ImageView iv_profile;
        TextView tv_likes;
        TextView tv_comments;

        public ViewHolder(View view) {
            super(view);
            tv_caption = (TextView)view.findViewById(R.id.tv_caption);
            tv_likes = (TextView)view.findViewById(R.id.tv_likes);
            tv_comments = (TextView)view.findViewById(R.id.tv_comments);
            iv_profile = (ImageView)view.findViewById(R.id.iv_profile);
            tv_username = (TextView)view.findViewById(R.id.tv_username);
            iv_image = (ImageView)view.findViewById(R.id.iv_image);
        }
    }
}