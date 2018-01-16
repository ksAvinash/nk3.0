package smartAmigos.com.nammakarnataka;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import smartAmigos.com.nammakarnataka.helper.SQLiteDatabaseHelper;
import smartAmigos.com.nammakarnataka.helper.place_general_adapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlacesListFragment extends Fragment {
    String category;

    public PlacesListFragment() {
        // Required empty public constructor
    }

    View view;
    Context context;
    SQLiteDatabaseHelper helper;
    Cursor placesListCursor;
    TextView category_name;
    ListView places_list;

    private List<place_general_adapter> placeAdapterList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_places_list, container, false);

        initializeViews(view);

        return view;
    }


    public void initializeViews(View view){
        context = getActivity().getApplicationContext();
        category = getArguments().getString("category");

        category_name = view.findViewById(R.id.category_name);
        category_name.setText(category);

        places_list = view.findViewById(R.id.places_list);

        helper = new SQLiteDatabaseHelper(context);
        placesListCursor = helper.getAllPlacesByCategory(category);

        while (placesListCursor.moveToNext()){

            placeAdapterList.add( new place_general_adapter(
                        placesListCursor.getString(1),
                        placesListCursor.getString(2),
                        placesListCursor.getString(3),
                        placesListCursor.getString(4),
                        placesListCursor.getString(5),
                        placesListCursor.getDouble(6),
                        placesListCursor.getDouble(7)
                    ));
        }
        displayList();

    }



    private void displayList() {
        ArrayAdapter<place_general_adapter> adapter = new myPlaceAdapterClass();
        places_list.setAdapter(adapter);
    }


    public class myPlaceAdapterClass extends ArrayAdapter<place_general_adapter> {

        myPlaceAdapterClass() {
            super(context, R.layout.place_list_item, placeAdapterList);
        }


        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                itemView = inflater.inflate(R.layout.place_list_item, parent, false);

            }
            place_general_adapter current = placeAdapterList.get(position);


            TextView t_name = itemView.findViewById(R.id.placeList_name);
            t_name.setText(current.getTitle());


            TextView t_district = itemView.findViewById(R.id.placeList_district);
            t_district.setText(current.getDistrict());

            String head_image = getResources().getString(R.string.s3_base_url)+"/"+category+"/"+position+"/head.jpg";
            Log.d("S3 URL : ", head_image);
            Uri uri = Uri.parse(head_image);
            SimpleDraweeView draweeView = itemView.findViewById(R.id.placeList_image);
            draweeView.setImageURI(uri);

            return itemView;
        }

    }




}
