package com.wngml.placesapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.wngml.placesapp.MainActivity;
import com.wngml.placesapp.MapActivity;
import com.wngml.placesapp.R;
import com.wngml.placesapp.model.Place;

import org.w3c.dom.Text;

import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {

    Context context;
    List<Place> placeList;

    public PlaceAdapter(Context context, List<Place> placeList) {
        this.context = context;
        this.placeList = placeList;
    }

    @NonNull
    @Override
    public PlaceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_row, parent, false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Place place = placeList.get(position);

        if(place.getName() != null) {
            holder.txtName.setText(place.getName());
        }
        if(place.getVicinity() != null) {
            holder.txtVicinity.setText(place.getVicinity());

        }
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView txtName;
        TextView txtVicinity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtVicinity = itemView.findViewById(R.id.txtVicinity);
            cardView = itemView.findViewById(R.id.cardView);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 지도액티비티 띄운다.
                    int index = getAdapterPosition();

                    ((MainActivity)context).runMapActivity(index);

                }
            });
        }
    }
}
