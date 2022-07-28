package com.wngml.movieapp;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wngml.movieapp.adapter.MovieAdapter;
import com.wngml.movieapp.api.MovieApi;
import com.wngml.movieapp.api.NetworkClient;
import com.wngml.movieapp.model.Movie;
import com.wngml.movieapp.model.MovieList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // 화면에 매칭할 멤버변수
    RecyclerView recyclerView;
    MovieAdapter adapter;
    ArrayList<Movie> movieList = new ArrayList<>();
    private ProgressBar progressBar;
    Button btnCntSort;
    Button btnAvgSort;

    // 페이징 처리를 위한 멤버 변수
    private int count;
    private int offset;
    private int limit = 25;

    // 정렬과 관련된 멤버 변수
    private String order = "cnt";
    
    public MovieFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MovieFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieFragment newInstance(String param1, String param2) {
        MovieFragment fragment = new MovieFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_movie, container, false);

        btnCntSort = rootView.findViewById(R.id.btnCntSort);
        btnCntSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 0. 현재 눌러져 있는 버튼이 무엇인지 먼저 확인하고
                // 현재 눌러져 있으면 새로 가져오지 않는다.
                if(order.equals("cnt")) {
                    return;
                }

                // 1. 버튼의 색을 바꿔준다.
                btnCntSort.setTextColor(Color.parseColor("#2196F3"));
                btnAvgSort.setTextColor(Color.parseColor("#8C8F91"));

                // 2. 네트워크로부터 데이터를 새로 받아온다. 정렬은 cnt로
                order = "cnt";
                getNetworkData();
            }
        });
        btnAvgSort = rootView.findViewById(R.id.btnAvgSort);
        btnAvgSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(order.equals("avg")) {
                    return;
                }

                btnAvgSort.setTextColor(Color.parseColor("#2196F3"));
                btnCntSort.setTextColor(Color.parseColor("#8C8F91"));
                order = "avg";
                getNetworkData();
            }
        });

        progressBar = rootView.findViewById(R.id.progressBar);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int totalCount = recyclerView.getAdapter().getItemCount();

                if(  lastPosition+1  == totalCount  ){

                    if(count == limit){
                        // 네트워크 통해서, 남아있는 데이터를 추가로 가져오기
                        addNetworkData();
                    }
                }
            }
        });
        getNetworkData();

        return rootView;
    }

    private void getNetworkData() {

        movieList.clear();
        count = 0;
        offset = 0;
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = NetworkClient.getRetrofitClient(getContext());
        MovieApi api = retrofit.create(MovieApi.class);

        Call<MovieList> call = api.movieList(offset, limit, order);
        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){

                    count = response.body().getCount();

                    movieList.addAll( response.body().getItems() );

                    offset = offset + count;

                    adapter = new MovieAdapter(getActivity(), movieList);

                    recyclerView.setAdapter(adapter);

                }
            }


            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    // 처음이 아니라 더 가져오는 경우
    private void addNetworkData() {
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = NetworkClient.getRetrofitClient(getContext());
        MovieApi api = retrofit.create(MovieApi.class);

        Call<MovieList> call = api.movieList(offset, limit, order);

        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if(response.isSuccessful()) {
                    MovieList data = response.body();

                    count = data.getCount();
                    movieList.addAll(data.getItems());
                    offset = offset + count;
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "에러 발생", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

}