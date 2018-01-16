package smartAmigos.com.nammakarnataka;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import java.util.Calendar;
import java.util.Date;

import smartAmigos.com.nammakarnataka.helper.BackendHelper;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Context context;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_home:
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_content, new HomeFragment()).commit();
                    return true;

                case R.id.navigation_profile:
                    return true;

                case R.id.navigation_category:
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_content, new CategoriesFragment()).commit();
                    return true;

                case R.id.navigation_trending:
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_content, new TrendingFragment()).commit();
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

        //Initialize the bottom navigation view and it's listener
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Jump to HomeFragment on MainActivity invocation
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_content, new HomeFragment()).commit();

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

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BackendHelper.fetch_category_places fetchCategoryPlaces = new BackendHelper.fetch_category_places();
                        fetchCategoryPlaces.execute(context, "hillstation");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                BackendHelper.fetch_category_places fetchCategoryPlaces = new BackendHelper.fetch_category_places();
                                fetchCategoryPlaces.execute(context, "dam");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        BackendHelper.fetch_category_places fetchCategoryPlaces = new BackendHelper.fetch_category_places();
                                        fetchCategoryPlaces.execute(context, "waterfall");
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                BackendHelper.fetch_category_places fetchCategoryPlaces = new BackendHelper.fetch_category_places();
                                                fetchCategoryPlaces.execute(context, "trekking");
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        BackendHelper.fetch_category_places fetchCategoryPlaces = new BackendHelper.fetch_category_places();
                                                        fetchCategoryPlaces.execute(context, "heritage");
                                                        new Handler().postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                BackendHelper.fetch_category_places fetchCategoryPlaces = new BackendHelper.fetch_category_places();
                                                                fetchCategoryPlaces.execute(context, "beach");
                                                                new Handler().postDelayed(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        BackendHelper.fetch_category_places fetchCategoryPlaces = new BackendHelper.fetch_category_places();
                                                                        fetchCategoryPlaces.execute(context, "other");
                                                                    }
                                                                }, 10000);
                                                            }
                                                        }, 10000);
                                                    }
                                                }, 10000);
                                            }
                                        }, 10000);
                                    }
                                }, 10000);
                            }
                        }, 10000);
                    }
                }, 10000);

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
