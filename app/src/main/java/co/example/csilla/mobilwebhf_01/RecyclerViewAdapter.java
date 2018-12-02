package co.example.csilla.mobilwebhf_01;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    public static final String TAG = "Recycler View Adapter";
    private AppCompatActivity activity;

    public final List<Place> places;

    public RecyclerViewAdapter(List<Place> places, AppCompatActivity activity) {
        this.places = places;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_place, parent, false);
        return new ViewHolder(view);
    }

    //ViewHolder binding
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mPlace = places.get(position);
        holder.placeName.setText(places.get(position).getPlaceName());
        //holder.placeCoord.setText(places.get(position).getPlaceCoordinates());
        holder.placeAddr.setText(places.get(position).getPlaceAddress());
        String str = activity.getResources().getString(R.string.card_alarm_distance);
        str = String.format(str, places.get(position).getAlarmDistance());
        holder.alarmDistance.setText(str);
    }

    //Elemek száma
    @Override
    public int getItemCount() {
        return places.size();
    }

    public Place getAPlace(int i){ return places.get(i); }

    //ViewHolder implementáció
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        private final TextView placeName;
        //private final TextView placeCoord;
        private final TextView placeAddr;
        private final TextView alarmDistance;

        public Place mPlace;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            placeName = (TextView) view.findViewById(R.id.textViewPlaceName);
            //placeCoord = (TextView) view.findViewById(R.id.textViewCoordinates);
            placeAddr = (TextView) view.findViewById(R.id.textViewAddress);
            alarmDistance = (TextView) view.findViewById(R.id.textViewAlarmDistance);
        }
    }

}
