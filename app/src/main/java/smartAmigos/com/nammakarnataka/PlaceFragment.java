package smartAmigos.com.nammakarnataka;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import smartAmigos.com.nammakarnataka.helper.SQLiteDatabaseHelper;
import smartAmigos.com.nammakarnataka.helper.nearby_places_adapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceFragment extends Fragment {


    public PlaceFragment() {
        // Required empty public constructor
    }

    View view;
    Context context;
    int place_id;
    SQLiteDatabaseHelper helper;
    Cursor placeCursor;
    String placename;
    ListView list;
    String places[];


    private List<nearby_places_adapter> nearby_adapterList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_place, container, false);

        initializeViews(view);

        return view;
    }

    private void initializeViews(View view) {
        context = getActivity().getApplicationContext();
        place_id = getArguments().getInt("id");
//        list = view.findViewById(R.id.nearbyPlaceList);

        //inflate the header and footer layouts
//        View header = getActivity().getLayoutInflater().inflate(R.layout.place_display_header, null);
//        View footer = getActivity().getLayoutInflater().inflate(R.layout.place_display_footer, null);
//        list.addHeaderView(header);
//        list.addFooterView(footer);



        populateData(place_id);
    }

    private void populateData(int place_id) {
        helper = new SQLiteDatabaseHelper(context);
        placeCursor = helper.getPlaceById(place_id);
//
//        while (placeCursor.moveToNext()){
//            placename = placeCursor.getString(1);
//            place_textView.setText(placename);
//            description_textView.setText(placeCursor.getString(2));
//            location_textView.setText(placeCursor.getString(3));
//            season_textView.setText(placeCursor.getString(4));
//            additionalInformation.setText(placeCursor.getString(5));
//            nearPlaces = placeCursor.getString(6);
//            latitude = placeCursor.getDouble(7);
//            longitude = placeCursor.getDouble(8);
//        }
    }


}
