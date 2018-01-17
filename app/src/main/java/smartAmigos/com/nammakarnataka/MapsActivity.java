package smartAmigos.com.nammakarnataka;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import smartAmigos.com.nammakarnataka.helper.SQLiteDatabaseHelper;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Context context;
    SQLiteDatabaseHelper myDBHelper;
    Cursor PlaceCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        context = getApplicationContext();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        myDBHelper = new SQLiteDatabaseHelper(context);

        LatLng karnataka = new LatLng(12.94,75.37);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(karnataka,  (float)8.5));

        addMarkers();

    }


    public void addMarkers(){

        PlaceCursor = myDBHelper.getAllTemples();
        while (PlaceCursor.moveToNext()){
            LatLng place = new LatLng(PlaceCursor.getDouble(6),
                    PlaceCursor.getDouble(7));
            mMap.addMarker(new MarkerOptions()
                    .position(place)
                    .title(PlaceCursor.getString(1)+" Temple")
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("maps_temple"))));
        }
        PlaceCursor = myDBHelper.getAllDams();
        while (PlaceCursor.moveToNext()){
            LatLng place = new LatLng(PlaceCursor.getDouble(6), PlaceCursor.getDouble(7));
            mMap.addMarker(new MarkerOptions().position(place).
                    title(PlaceCursor.getString(1)+" Dam")
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("maps_dam"))));
        }
        PlaceCursor = myDBHelper.getAllTrekkings();
        while (PlaceCursor.moveToNext()){
            LatLng place = new LatLng(PlaceCursor.getDouble(6), PlaceCursor.getDouble(7));
            mMap.addMarker(new MarkerOptions().position(place)
                    .title(PlaceCursor.getString(1)+" Trek")
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("maps_trekking"))));
        }
        PlaceCursor = myDBHelper.getAllHillstations();
        while (PlaceCursor.moveToNext()){
            LatLng place = new LatLng(PlaceCursor.getDouble(6), PlaceCursor.getDouble(7));
            mMap.addMarker(new MarkerOptions().position(place)
                    .title(PlaceCursor.getString(1)+" Hillstation")
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("maps_hillstation"))));
        }

        PlaceCursor = myDBHelper.getAllWaterfalls();
        while (PlaceCursor.moveToNext()){
            LatLng place = new LatLng(PlaceCursor.getDouble(6), PlaceCursor.getDouble(7));
            mMap.addMarker(new MarkerOptions().position(place)
                    .title(PlaceCursor.getString(1)+" Falls")
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("maps_waterfall"))));
        }
        PlaceCursor = myDBHelper.getAllBeaches();
        while (PlaceCursor.moveToNext()){
            LatLng place = new LatLng(PlaceCursor.getDouble(6), PlaceCursor.getDouble(7));
            mMap.addMarker(new MarkerOptions().position(place)
                    .title(PlaceCursor.getString(1)+" Beach")
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("maps_beach"))));
        }
        PlaceCursor = myDBHelper.getAllHeritages();
        while (PlaceCursor.moveToNext()){
            LatLng place = new LatLng(PlaceCursor.getDouble(6), PlaceCursor.getDouble(7));
            mMap.addMarker(new MarkerOptions().position(place)
                    .title(PlaceCursor.getString(1))
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("maps_heritage"))));
        }


    }


    public Bitmap resizeMapIcons(String iconName){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
        return Bitmap.createScaledBitmap(imageBitmap, 50, 50, false);
    }
}
