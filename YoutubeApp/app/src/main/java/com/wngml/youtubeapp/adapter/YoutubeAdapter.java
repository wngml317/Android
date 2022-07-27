package com.wngml.youtubeapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.wngml.youtubeapp.MainActivity;
import com.wngml.youtubeapp.PhotoActivity;
import com.wngml.youtubeapp.R;
import com.wngml.youtubeapp.config.Config;
import com.wngml.youtubeapp.model.Youtube;

import java.util.ArrayList;
import java.util.List;

public class YoutubeAdapter extends RecyclerView.Adapter<YoutubeAdapter.ViewHolder> {

    Context context;
    List<Youtube> youtubeList;

    public YoutubeAdapter(Context context, ArrayList<Youtube> youtubeList) {
        this.context = context;
        this.youtubeList = youtubeList;
    }

    @NonNull
    @Override
    public YoutubeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.youtube_row, parent, false);
        return new YoutubeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Youtube youtube = youtubeList.get(position);

        holder.txtTitle.setText(youtube.title);
        holder.txtDescription.setText(youtube.description);

        Glide.with(context).load(youtube.imgUrl).into(holder.imgThumb);
    }

    @Override
    public int getItemCount() {
        return youtubeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        TextView txtDescription;
        ImageView imgThumb;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            imgThumb = itemView.findViewById(R.id.imgThumb);
            cardView = itemView.findViewById(R.id.cardView);

            imgThumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int index = getAdapterPosition();

                    Youtube youtube = youtubeList.get(index);

                    Intent intent = new Intent(context, PhotoActivity.class);

                    intent.putExtra("imgUrl", youtube.imgUrl);
                    context.startActivity(intent);
                }
            });

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = getAdapterPosition();
                    Youtube youtube = youtubeList.get(index);

                    // videoId를 URL로 만들어서  웹브라우저 실행시킨다
                    String url = Config.PLAY_URL + youtube.videoId;
                    openWebPage(url);
                }
            });
        }

        void openWebPage(String url) {
            Uri webPage = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
            context.startActivity(intent);
        }
    }
}
