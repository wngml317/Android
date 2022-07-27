package com.wngml.postingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wngml.postingapp.PostingListActivity;
import com.wngml.postingapp.R;
import com.wngml.postingapp.config.Config;
import com.wngml.postingapp.model.Posting;

import java.util.List;

public class FollowPostingAdapter extends RecyclerView.Adapter<FollowPostingAdapter.ViewHolder> {

    Context context;
    List<Posting> postingList;

    public FollowPostingAdapter(Context context, List<Posting> postingList) {
        this.context = context;
        this.postingList = postingList;
    }

    @NonNull
    @Override
    public FollowPostingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.posting_row, parent, false);
        return  new FollowPostingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Posting posting = postingList.get(position);
        holder.txtContent.setText(posting.getContent());
        holder.txtName.setText("작성자 : " + posting.getName());
        holder.txtCreatedAt.setText(posting.getCreatedAt().replace("T", " ").substring(0, 16));
        holder.txtLikeCnt.setText("좋아요 : "+posting.getLikeCnt());

        if (posting.getIsLike() == 1) {
            holder.imgLike.setImageResource(R.drawable.ic_like);
        } else {
            holder.imgLike.setImageResource(R.drawable.ic_unlike);
        }

        Glide.with(context).load(Config.IMAGE_URL+posting.getImgUrl()).placeholder(R.drawable.ic_defalut_photo).into(holder.imgPhoto);

    }

    @Override
    public int getItemCount() {
        return postingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView imgPhoto;
        TextView txtContent;
        TextView txtName;
        TextView txtCreatedAt;
        TextView txtLikeCnt;
        ImageView imgLike;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            txtContent = itemView.findViewById(R.id.txtContent);
            txtName = itemView.findViewById(R.id.txtName);
            txtCreatedAt = itemView.findViewById(R.id.txtCreatedAt);
            txtLikeCnt = itemView.findViewById(R.id.txtLikeCnt);
            imgLike = itemView.findViewById(R.id.imgLike);

            imgLike.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    // 액티비티에 함수 만들어 놓고, 호출
                    int index = getAdapterPosition();
                    ((PostingListActivity) context).setLike(index);
                }
            });

        }
    }
}
