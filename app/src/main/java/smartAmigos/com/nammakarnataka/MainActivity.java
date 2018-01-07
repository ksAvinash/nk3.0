package smartAmigos.com.nammakarnataka;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import smartAmigos.com.nammakarnataka.helper.BackendHelper;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Context context;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_profile:
                    return true;
                case R.id.navigation_category:
                    return true;
                case R.id.navigation_trending:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();


        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fetch_places();


    }



    public void fetch_places(){
        if(isNetworkConnected()){

            boolean fetch_again = get_previous_fetch_history();

            if(fetch_again){
                //update last fetch time in sharedpreferences
                Date current_date = new Date();
                sharedPreferences = getSharedPreferences("nk", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong("last_fetch_date", current_date.getTime());
                editor.commit();

                //Start getting the places by category
                BackendHelper.fetch_category_places fetchCategoryPlaces = new BackendHelper.fetch_category_places();
                fetchCategoryPlaces.execute(context, "temple");
//                fetchCategoryPlaces.execute(context, "hillstation");
//                fetchCategoryPlaces.execute(context, "dam");
//                fetchCategoryPlaces.execute(context, "waterfall");
//                fetchCategoryPlaces.execute(context, "other");
//                fetchCategoryPlaces.execute(context, "trekking");

            }
        }
    }

    private boolean get_previous_fetch_history() {
        Date current_date = new Date();

        sharedPreferences = getSharedPreferences("nk", MODE_PRIVATE);
        Date previous_fetch_date = new Date(sharedPreferences.getLong("last_fetch_date", 0));
        int noOfDays = 7;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(previous_fetch_date);
        calendar.add(Calendar.DAY_OF_YEAR, noOfDays);
        Date new_fetch_date = calendar.getTime();

        if(current_date.after(new_fetch_date)){
            Log.d("DATE","FETCH AGAIN YES");
            return true;
        }else{
            Log.d("DATE","FETCH AGAIN NO");
            return false;
        }


    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


}
