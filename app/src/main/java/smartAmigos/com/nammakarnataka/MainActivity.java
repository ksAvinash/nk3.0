package smartAmigos.com.nammakarnataka;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import android.app.SearchManager;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import smartAmigos.com.nammakarnataka.helper.BackendHelper;
import smartAmigos.com.nammakarnataka.helper.SQLiteDatabaseHelper;


public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Context context;
    SQLiteDatabaseHelper myDBHelper;
    ProgressDialog pd;

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

                case R.id.navigation_maps:
                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                    startActivity(intent);
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
        pd = new ProgressDialog(MainActivity.this);


        //Initialize the bottom navigation view and it's listener
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        fetch_places(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_content, new HomeFragment()).commit();
            }

        }, 150);

    }


    public void fetch_places(boolean forcefetch){
        if(isNetworkConnected()){

            boolean fetch_again = get_previous_fetch_history();

            if(fetch_again || forcefetch){
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_share:
                    String str = "https://play.google.com/store/apps/details?id=" + getPackageName();
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT,
                            "All you need to know about Karnataka\n\nDownload:\n" + str);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                break;

            case R.id.action_refresh:
                    if(isNetworkConnected()){
                        Toast.makeText(getApplicationContext(), "Please wait..", Toast.LENGTH_SHORT).show();
                        fetch_places(true);
                    }else{
                        Toast.makeText(context, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                    }
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        final SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener(){
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        myDBHelper = new SQLiteDatabaseHelper(getApplicationContext());
                        Cursor cursor = myDBHelper.getPlaceByString(query);

                        Fragment fragment = new SearchResults(cursor);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.frame_content, fragment);
                        ft.commit();
                       return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        myDBHelper = new SQLiteDatabaseHelper(getApplicationContext());
                        Cursor cursor = myDBHelper.getPlaceByString(newText);

                        Fragment fragment = new SearchResults(cursor);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.frame_content, fragment);
                        ft.commit();
                        return false;
                    }
                }
        );

        return true;
    }
}
