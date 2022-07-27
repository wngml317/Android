package com.wngml.tab;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.wngml.tab.adapter.MyPostingAdapter;
import com.wngml.tab.api.NetworkClient;
import com.wngml.tab.api.PostingApi;
import com.wngml.tab.config.Config;
import com.wngml.tab.model.Posting;
import com.wngml.tab.model.PostingList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // 화면에 매칭할 멤버변수
    RecyclerView recyclerView;
    MyPostingAdapter adapter;
    ArrayList<Posting> postingList = new ArrayList<>();
    private ProgressBar progressBar;
    private int count;
    private int offset;
    private int limit = 25;

    public FirstFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FirstFragment newInstance(String param1, String param2) {
        FirstFragment fragment = new FirstFragment();
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

    // 프레그먼트는 onCreateView에 화면 연결 처리
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_first, container, false);

        progressBar = rootView.findViewById(R.id.progressBar);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        // MainActivity 에서 FirstFragment를 연결해주었으므로
        // Fragment 에서 액티비티를 알아서 처리해준다.
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        getNetworkData();

        return rootView;
    }

    // 처음으로 네트워크 가져올때
    private void getNetworkData() {
        postingList.clear();
        count = 0;
        offset = 0;
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = NetworkClient.getRetrofitClient(getContext());
        PostingApi api = retrofit.create(PostingApi.class);

//        SharedPreferences sp = getContext().getSharedPreferences(Config.PREFERENCES_NAME, getContext().MODE_PRIVATE);
//        String accessToken = sp.getString("accessToken", "");

        String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJmcmVzaCI6ZmFsc2UsImlhdCI6MTY1ODg5NDU0MiwianRpIjoiY2ZhY2VhMTYtZmIwNi00NzE4LTgxMjAtZDkwYjI4NmViYjIzIiwidHlwZSI6ImFjY2VzcyIsInN1YiI6MiwibmJmIjoxNjU4ODk0NTQyfQ._uDHY8HzeN1uIPf1NwR65euvED1JAcequcCdoZHzj74";

        Call<PostingList> call = api.getMyPosting("Bearer " + accessToken, offset, limit);
        call.enqueue(new Callback<PostingList>() {
            @Override
            public void onResponse(Call<PostingList> call, Response<PostingList> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){

                    count = response.body().getCount();

                    postingList.addAll( response.body().getItems() );

                    offset = offset + count;

                    adapter = new MyPostingAdapter(getActivity(), postingList);

                    recyclerView.setAdapter(adapter);

                }
            }


            @Override
            public void onFailure(Call<PostingList> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}