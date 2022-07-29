package com.wngml.movieapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.wngml.movieapp.LoginActivity;
import com.wngml.movieapp.R;
import com.wngml.movieapp.RegisterActivity;
import com.wngml.movieapp.ReviewAddActivity;
import com.wngml.movieapp.config.Config;
import com.wngml.movieapp.model.Movie;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    Context context;
    List<Movie> movieList;

    public SearchAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_row, parent, false);
        return  new SearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.txtTitle.setText(movie.getTitle());
        holder.txtCnt.setText(""+movie.getCnt());
        holder.txtAvg.setText(""+movie.getAvg());
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView txtTitle;
        TextView txtCnt;
        TextView txtAvg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtCnt = itemView.findViewById(R.id.txtCnt);
            txtAvg = itemView.findViewById(R.id.txtAvg);
            cardView = itemView.findViewById(R.id.cardView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // 로그인 유무 체크
                    SharedPreferences sp = context.getSharedPreferences(Config.PREFERENCES_NAME, Context.MODE_PRIVATE);
                    String accessToken = sp.getString("accessToken", "");

                    if(accessToken.isEmpty()) {
                        Toast.makeText(context.getApplicationContext(), "로그인 후 이용해주세요.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    } else {

                        // 리뷰 작성하는 액티비티 실행
                        int index = getAdapterPosition();
                        Movie movie = movieList.get(index);
                        Intent intent = new Intent(context, ReviewAddActivity.class);
                        intent.putExtra("movie", movie);
                        context.startActivity(intent);

                    }
                }
            });
        }
    }
}
