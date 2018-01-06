package smartAmigos.com.nammakarnataka;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;


public class SplasherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //make the splashscreen fullscreen activity
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splasher);


        SharedPreferences sharedPreferences = getSharedPreferences("nk", MODE_PRIVATE);
        Boolean isSignedIn = sharedPreferences.getBoolean("isSignedIn", false);

        if(isSignedIn){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplasherActivity.this, MainActivity.class);
                    startActivity(intent);
                    SplasherActivity.this.finish();
                }
            }, 2500);
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplasherActivity.this, GoogleSigninActivity.class);
                    startActivity(intent);
                    SplasherActivity.this.finish();
                }
            }, 2500);
        }


    }

}
