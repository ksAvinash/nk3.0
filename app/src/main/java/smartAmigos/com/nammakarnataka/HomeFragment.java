package smartAmigos.com.nammakarnataka;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.HashMap;

import smartAmigos.com.nammakarnataka.helper.SQLiteDatabaseHelper;


public class HomeFragment extends Fragment implements RewardedVideoAdListener {


    public HomeFragment() {
        // Required empty public constructor
    }

    View view;
    //private RewardedVideoAd mRewardedVideoAd;
    Context context;
    SliderLayout homeFragmentSlider;
    SQLiteDatabaseHelper helper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_home, container, false);
        context = getActivity().getApplicationContext();

        setImageSlider(view);
//        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(context);
//        mRewardedVideoAd.setRewardedVideoAdListener(this);
//        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
//                new AdRequest.Builder().build());

        return view;
    }



    public void setImageSlider(final View view){
        new Thread(new Runnable() {
            @Override
            public void run() {

                homeFragmentSlider = view.findViewById(R.id.homeFragmentSlider);
                final HashMap<String, Integer> file_maps = new HashMap<>();
                //Positively do not change any images
                file_maps.put("Jog Falls", R.drawable.jog);
                file_maps.put("Mysuru Palace", R.drawable.mysuru);
                file_maps.put("Mullayanagiri", R.drawable.mullayanagiri);
                file_maps.put("Dandeli", R.drawable.dandeli);
                file_maps.put("Wonder La",R.drawable.wonderla);

                for (final String name : file_maps.keySet()) {
                    TextSliderView textSliderView = new TextSliderView(context);
                    // initialize a SliderLayout
                    textSliderView
                            .description(name)
                            .image(file_maps.get(name))
                            .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                @Override
                                public void onSliderClick(BaseSliderView slider) {

                                    String[] str = name.split(" ");
                                    helper = new SQLiteDatabaseHelper(context);
                                    Cursor cursor = helper.getPlaceByString(str[0]);

                                    Fragment fragment = new SearchResults(cursor);
                                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                    ft.replace(R.id.frame_content, fragment);
                                    ft.commit();
                                }
                            })
                            .setScaleType(BaseSliderView.ScaleType.Fit);

                    //add your extra information
                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle()
                            .putString("extra", name);

                    homeFragmentSlider.addSlider(textSliderView);
                }

                homeFragmentSlider.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);
                homeFragmentSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                homeFragmentSlider.setCustomAnimation(new DescriptionAnimation());
                homeFragmentSlider.setDuration(7000);

            }
        }).start();
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
