package smartAmigos.com.nammakarnataka;


import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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

import smartAmigos.com.nammakarnataka.helper.CircleProgressBarDrawable;
import smartAmigos.com.nammakarnataka.helper.SQLiteDatabaseHelper;
import smartAmigos.com.nammakarnataka.helper.place_general_adapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchResults extends Fragment {

    Cursor PlaceCursor;
    View view;
    Context context;
    ListView list;
    SQLiteDatabaseHelper helper;
    private List<place_general_adapter> placeAdapterList = new ArrayList<>();

    public SearchResults() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public SearchResults(Cursor PlaceCursor) {
        this.PlaceCursor = PlaceCursor;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_search_results, container, false);

        initializeViews(view);

        return view;
    }


    private void initializeViews(View view){
        context = getActivity().getApplicationContext();
        list = view.findViewById(R.id.searchList);

        while (PlaceCursor.moveToNext()){

            placeAdapterList.add( new place_general_adapter(
                    PlaceCursor.getInt(0),
                    PlaceCursor.getString(1),
                    PlaceCursor.getString(2),
                    PlaceCursor.getString(3),
                    PlaceCursor.getString(4),
                    PlaceCursor.getString(5),
                    PlaceCursor.getDouble(6),
                    PlaceCursor.getDouble(7),
                    PlaceCursor.getString(8)
            ));
        }
        displayList();
    }


    private void displayList() {
        ArrayAdapter<place_general_adapter> adapter = new mySearchPlaceAdapterClass();
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try{
                    helper = new SQLiteDatabaseHelper(context);
                    place_general_adapter current = placeAdapterList.get(position);

                    //Handle clicks to navigate to place_fragment along with the ID value
                    PlaceFragment place_fragment = new PlaceFragment();
                    Bundle fragment_agruments = new Bundle();
                    fragment_agruments.putInt("id", current.getId());
                    place_fragment.setArguments(fragment_agruments);
                    getFragmentManager().beginTransaction().replace(R.id.frame_content, place_fragment).commit();

                }catch (Exception e){

                }

            }
        });
    }


    public class mySearchPlaceAdapterClass extends ArrayAdapter<place_general_adapter> {

        mySearchPlaceAdapterClass() {
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

            String category = current.getCategory();
            String head_image = getResources().getString(R.string.s3_base_url)+"/"+category+"/"+current.getId()+"/head.jpg";
            Uri uri = Uri.parse(head_image);
            SimpleDraweeView draweeView = itemView.findViewById(R.id.placeList_image);
            draweeView.getHierarchy().setProgressBarImage(new CircleProgressBarDrawable(1));
            draweeView.setImageURI(uri);

            return itemView;
        }

    }

}
