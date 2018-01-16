package smartAmigos.com.nammakarnataka;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import smartAmigos.com.nammakarnataka.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesFragment extends Fragment implements View.OnClickListener{


    public CategoriesFragment() {
        // Required empty public constructor
    }

    View view;
    Button category_temples, category_beaches, category_hillstations, category_waterfalls, category_trekking, category_dams, category_heritage, category_others;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_categories, container, false);

        initializeViews();

        return view;
    }

    private void initializeViews() {
        category_temples = view.findViewById(R.id.category_temples);
        category_temples.setOnClickListener(this);

        category_beaches = view.findViewById(R.id.category_beaches);
        category_beaches.setOnClickListener(this);

        category_hillstations = view.findViewById(R.id.category_hillstations);
        category_hillstations.setOnClickListener(this);

        category_waterfalls = view.findViewById(R.id.category_waterfalls);
        category_waterfalls.setOnClickListener(this);

        category_dams = view.findViewById(R.id.category_dams);
        category_dams.setOnClickListener(this);

        category_heritage = view.findViewById(R.id.category_heritage);
        category_heritage.setOnClickListener(this);

        category_others = view.findViewById(R.id.category_others);
        category_others.setOnClickListener(this);

        category_trekking = view.findViewById(R.id.category_trekking);
        category_trekking.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        PlacesListFragment placesListFragment = new PlacesListFragment();
        Bundle fragment_agruments = new Bundle();

        switch (view.getId()){
            case R.id.category_temples:
                fragment_agruments.putString("category", "temple");
                placesListFragment.setArguments(fragment_agruments);
                getFragmentManager().beginTransaction().replace(R.id.frame_content, placesListFragment).commit();
                break;

            case R.id.category_beaches:
                fragment_agruments.putString("category", "beach");
                placesListFragment.setArguments(fragment_agruments);
                getFragmentManager().beginTransaction().replace(R.id.frame_content, placesListFragment).commit();
                break;

            case R.id.category_hillstations:
                fragment_agruments.putString("category", "hillstation");
                placesListFragment.setArguments(fragment_agruments);
                getFragmentManager().beginTransaction().replace(R.id.frame_content, placesListFragment).commit();
                break;

            case R.id.category_waterfalls:
                fragment_agruments.putString("category", "waterfall");
                placesListFragment.setArguments(fragment_agruments);
                getFragmentManager().beginTransaction().replace(R.id.frame_content, placesListFragment).commit();
                break;

            case R.id.category_dams:
                fragment_agruments.putString("category", "dam");
                placesListFragment.setArguments(fragment_agruments);
                getFragmentManager().beginTransaction().replace(R.id.frame_content, placesListFragment).commit();
                break;

            case R.id.category_heritage:
                fragment_agruments.putString("category", "heritage");
                placesListFragment.setArguments(fragment_agruments);
                getFragmentManager().beginTransaction().replace(R.id.frame_content, placesListFragment).commit();
                break;

            case R.id.category_trekking:
                fragment_agruments.putString("category", "trekking");
                placesListFragment.setArguments(fragment_agruments);
                getFragmentManager().beginTransaction().replace(R.id.frame_content, placesListFragment).commit();
                break;

            case R.id.category_others:
                fragment_agruments.putString("category", "other");
                placesListFragment.setArguments(fragment_agruments);
                getFragmentManager().beginTransaction().replace(R.id.frame_content, placesListFragment).commit();
                break;


        }
    }
}
