package com.wngml.movieapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wngml.movieapp.adapter.SearchAdapter;
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
 * Use the {@link SecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecondFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText editSearch;
    ImageView imgSearch;

    ProgressBar progressBar;
    RecyclerView recyclerView;
    SearchAdapter adapter;
    ArrayList<Movie> movieList = new ArrayList<>();

    String keyword;

    int count = 0;
    int offset = 0;
    int limit = 25;

    public SecondFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SecondFragment newInstance(String param1, String param2) {
        SecondFragment fragment = new SecondFragment();
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
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_second, container, false);

        progressBar = rootView.findViewById(R.id.progressBar);
        editSearch = rootView.findViewById(R.id.editSearch);
        imgSearch = rootView.findViewById(R.id.imgSearch);
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editSearch.getText().toString().trim().isEmpty()) {
                    getNetworkData();
                }
            }
        });
//
//        if(editSearch.getText().toString().trim().isEmpty()) {
//            getNetworkData();
//        }

        keyword = "";
        getNetworkData();
        return rootView;
    }

    private void getNetworkData() {

        movieList.clear();
        count = 0;
        offset = 0;
        progressBar.setVisibility(View.VISIBLE);

        // 1. 에디트 텍스트에서 검색 키워드 가져온다.
        keyword = editSearch.getText().toString().trim();

        // 2. 네트워크로 호출하여  결과를  가져오고 화면에 표시
        Retrofit retrofit = NetworkClient.getRetrofitClient(getContext());
        MovieApi api = retrofit.create(MovieApi.class);
        Call<MovieList> call = api.search(keyword, offset, limit);
        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){

                    count = response.body().getCount();

                    movieList.addAll( response.body().getResult_list() );

                    offset = offset + count;

                    adapter = new SearchAdapter(getActivity(), movieList);

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

        Call<MovieList> call = api.search(keyword, offset, limit);

        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if(response.isSuccessful()) {
                    MovieList data = response.body();

                    count = data.getCount();
                    movieList.addAll(data.getResult_list());
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