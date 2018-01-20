package smartAmigos.com.nammakarnataka;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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

public class CategoryPlacesActivity extends AppCompatActivity {


    Context context;
    TextView category_name;
    String category;
    ListView places_list;
    SQLiteDatabaseHelper helper;
    Cursor placesListCursor;
    private List<place_general_adapter> placeAdapterList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_places);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initializeViews();

    }

    public void initializeViews() {
        context = getApplicationContext();

        Intent intent = getIntent();
        category = intent.getExtras().getString("category");

        category_name = findViewById(R.id.category_name);
        category_name.setText(category);

        places_list = findViewById(R.id.places_list);

        helper = new SQLiteDatabaseHelper(context);
        placesListCursor = helper.getAllPlacesByCategory(category);
        while (placesListCursor.moveToNext()){

            placeAdapterList.add( new place_general_adapter(
                    placesListCursor.getInt(0),
                    placesListCursor.getString(1),
                    placesListCursor.getString(2),
                    placesListCursor.getString(3),
                    placesListCursor.getString(4),
                    placesListCursor.getString(5),
                    placesListCursor.getDouble(6),
                    placesListCursor.getDouble(7),
                    placesListCursor.getString(8)

            ));
        }
        displayList();

    }


    private void displayList() {
        ArrayAdapter<place_general_adapter> adapter = new myPlaceAdapterClass();
        places_list.setAdapter(adapter);
        places_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try{
                    helper = new SQLiteDatabaseHelper(context);
                    place_general_adapter current = placeAdapterList.get(position);

                    Bundle fragment_agruments = new Bundle();
                    fragment_agruments.putInt("id", current.getId());

                    PlaceFragment placeFragment = new PlaceFragment();
                    placeFragment.setArguments(fragment_agruments);

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_category_activity, placeFragment).commit();

                }catch (Exception e){

                }

            }
        });
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
                LayoutInflater inflater = LayoutInflater.from(context);
                itemView = inflater.inflate(R.layout.place_list_item, parent, false);
            }
            place_general_adapter current = placeAdapterList.get(position);


            TextView t_name = itemView.findViewById(R.id.placeList_name);
            t_name.setText(current.getTitle());


            TextView t_district = itemView.findViewById(R.id.placeList_district);
            t_district.setText(current.getDistrict());

            String head_image = getResources().getString(R.string.s3_base_url)+"/"+category+"/"+current.getId()+"/head.jpg";
            Uri uri = Uri.parse(head_image);
            SimpleDraweeView draweeView = itemView.findViewById(R.id.placeList_image);
            draweeView.getHierarchy().setProgressBarImage(new CircleProgressBarDrawable(1));
            draweeView.setImageURI(uri);

            return itemView;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
