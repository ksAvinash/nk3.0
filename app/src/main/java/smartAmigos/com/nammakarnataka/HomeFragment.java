package smartAmigos.com.nammakarnataka;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.ArrayList;
import java.util.List;

import smartAmigos.com.nammakarnataka.helper.CircleProgressBarDrawable;
import smartAmigos.com.nammakarnataka.helper.MyLocationHelper;
import smartAmigos.com.nammakarnataka.helper.SQLiteDatabaseHelper;
import smartAmigos.com.nammakarnataka.helper.nearby_places_adapter;

import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment implements RewardedVideoAdListener {


    public HomeFragment() {
        // Required empty public constructor
    }

    View view;
    //private RewardedVideoAd mRewardedVideoAd;
    Context context;
    ListView nearme_list;
    private List<nearby_places_adapter> nearbyPlacesAdapterList = new ArrayList<>();
    Cursor nearme_cursor;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_home, container, false);
        context = getActivity().getApplicationContext();
        nearme_list = view.findViewById(R.id.nearme_list);

        fetchNearestSixPlaces();
//        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(context);
//        mRewardedVideoAd.setRewardedVideoAdListener(this);
//        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
//                new AdRequest.Builder().build());

        return view;
    }


    private void fetchNearestSixPlaces(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                    MyLocationHelper.LocationResult locationResult = new MyLocationHelper.LocationResult(){
                        @Override
                        public void gotLocation(Location source){

                            if(source!=null){
                                calculateDistances(source);

                            }else{
                                Log.d("LOCATION : NULL = ", source.toString());
                            }
                        }
                    };
                    MyLocationHelper myLocation = new MyLocationHelper();
                    myLocation.getLocation(context, locationResult);
                }

        }, 300);
    }


    /*
        The function takes user's current location as an agrument
        calcluates an distances to all other places
        If a place is less than 60kms from user location
            If you've not visited that place then the place will be displayed in the nearme list

     */
    private void calculateDistances(Location source) {
        SQLiteDatabaseHelper helper = new SQLiteDatabaseHelper(context);
        nearme_cursor = helper.getAllPlaces();

        while (nearme_cursor.moveToNext()){
            Double latitude = nearme_cursor.getDouble(6);
            Double longitude = nearme_cursor.getDouble(7);

            Location destination = new Location("");
            destination.setLatitude(latitude);
            destination.setLongitude(longitude);

            float distance = source.distanceTo(destination); //the result is in metres
            distance = distance/1000; //converted into Kms
            distance = (float)Math.round(distance * 100) / 100;

            if(distance < 60){
                nearbyPlacesAdapterList.add( new nearby_places_adapter(nearme_cursor.getInt(0), nearme_cursor.getString(8), nearme_cursor.getString(1), distance, latitude, longitude));
            }
        }

        displayNearPlacesList();
    }


    private void displayNearPlacesList(){
        ArrayAdapter<nearby_places_adapter> adapter = new nearbyPlaceAdapterClass();
        nearme_list.setAdapter(adapter);
        nearme_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                nearby_places_adapter current = nearbyPlacesAdapterList.get(position);

                String placename = current.getPlacename();
                Double latitude = current.getLatitude();
                Double longitide = current.getLongitude();

                startActivity(
                            new Intent(
                            android.content.Intent.ACTION_VIEW,
                            Uri.parse("geo:" + latitude
                                    + "," + longitide + "?q=(" + placename + ")@"
                                    + latitude + "," + longitide)));



            }
        });

    }



    public class nearbyPlaceAdapterClass extends ArrayAdapter<nearby_places_adapter> {

        nearbyPlaceAdapterClass() {
            super(context, R.layout.place_list_item, nearbyPlacesAdapterList);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                itemView = inflater.inflate(R.layout.place_list_item, parent, false);
            }
            nearby_places_adapter current = nearbyPlacesAdapterList.get(position);


            TextView t_name = itemView.findViewById(R.id.placeList_name);
            t_name.setText(current.getPlacename());

            TextView t_district = itemView.findViewById(R.id.placeList_district);
            t_district.setText(current.getDistance()+" Kms");

            String category = current.getCategory();

            String head_image = getResources().getString(R.string.s3_base_url)+"/"+category+"/"+current.getId()+"/head.jpg";
            Uri uri = Uri.parse(head_image);
            SimpleDraweeView draweeView = itemView.findViewById(R.id.placeList_image);
            draweeView.getHierarchy().setProgressBarImage(new CircleProgressBarDrawable(1));
            draweeView.setImageURI(uri);

            return itemView;
        }

    }






















































    //Reward videos implemented methods ---

    @Override
    public void onRewardedVideoAdLoaded() {
        Log.d("REWARD VIDEO", "AD LOADED");
//        if(mRewardedVideoAd.isLoaded())
//            mRewardedVideoAd.show();
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Log.d("REWARD VIDEO", "AD OPENED");

    }

    @Override
    public void onRewardedVideoStarted() {
        Log.d("REWARD VIDEO", "AD STARTED");

    }

    @Override
    public void onRewardedVideoAdClosed() {
        Log.d("REWARD VIDEO", "AD CLOSED");

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        Log.d("REWARD VIDEO", rewardItem.getAmount() +" REWARDS RECEIVED");
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Log.d("REWARD VIDEO", "AD OPENED THE INTENT");

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        Log.d("REWARD VIDEO", "AD FAILED");

    }
}
