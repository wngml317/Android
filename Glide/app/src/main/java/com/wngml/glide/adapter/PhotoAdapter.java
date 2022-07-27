package com.wngml.glide.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.wngml.glide.PhotoActivity;
import com.wngml.glide.R;
import com.wngml.glide.model.Photo;

import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    Context context;
    List<Photo> photoList;

    public PhotoAdapter(Context context, ArrayList<Photo> photoList) {
        this.context = context;
        this.photoList = photoList;
    }

    @NonNull
    @Override
    public PhotoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_row, parent, false);
        return new PhotoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Photo photo = photoList.get(position);

        holder.txtTitle.setText(photo.title);
        holder.txtId.setText("id : "+photo.id);
        holder.txtAlbumId.setText("albumId : "+photo.albumId);

        // Glide 라이브러리  사용
        GlideUrl url = new GlideUrl(photo.thumbnailUrl, new LazyHeaders.Builder().addHeader("User-Agent", "Android").build());
        Glide.with(context).load(url).into(holder.imgThumb);
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        TextView txtId;
        TextView txtAlbumId;
        ImageView imgThumb;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtId = itemView.findViewById(R.id.txtId);
            txtAlbumId = itemView.findViewById(R.id.txtAlbumId);
            imgThumb = itemView.findViewById(R.id.imgThumb);

            imgThumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 썸네일 이미지 누르면 큰 이미지 나오는 액티비티 실행
                    int index = getAdapterPosition();

                    Photo photo = photoList.get(index);

                    Intent intent = new Intent(context, PhotoActivity.class);

                    intent.putExtra("url", photo.url);

                    context.startActivity(intent);

                }
            });

        }
    }
}
