package smartAmigos.com.nammakarnataka;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import smartAmigos.com.nammakarnataka.helper.BackendHelper;


public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    static Context context;
    Button signup_button;
    EditText signup_username, signup_age, signup_phoneno, signup_name, signup_email, signup_city, signup_district;
    ImageView signup_male, signup_female;
    static View view;

    static String name, username, phoneno, email, city, district, gender;
    static int age;
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);

        initializeViews();

        //Send firebase_instance_id to BackendHelper systems
        BackendHelper.firebase_id_update firebaseIdUpdate = new BackendHelper.firebase_id_update();
        firebaseIdUpdate.execute(context);
    }


    public void initializeViews()
    {
        signup_button = findViewById(R.id.signup_button);
        signup_username = findViewById(R.id.signup_username);
        signup_age = findViewById(R.id.signup_age);
        signup_name = findViewById(R.id.signup_name);
        signup_email = findViewById(R.id.signup_email);
        signup_phoneno = findViewById(R.id.signup_phoneno);
        signup_male = findViewById(R.id.signup_male);
        signup_female = findViewById(R.id.signup_female);
        signup_city = findViewById(R.id.signup_city);
        signup_district = findViewById(R.id.signup_district);
        view = findViewById(android.R.id.content);
        context = getApplicationContext();

        //populating the name and email-id values from the sharedpreferences
        SharedPreferences sharedPreferences = getSharedPreferences("nk", MODE_PRIVATE);
        signup_name.setText(sharedPreferences.getString("name", ""));
        signup_email.setText(sharedPreferences.getString("email", ""));

        //make these two fields non-editable
        signup_name.setKeyListener(null);
        signup_email.setKeyListener(null);


        signup_female.setOnClickListener(this);
        signup_male.setOnClickListener(this);
        signup_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signup_male:
                    signup_male.setAlpha((float)1);
                    signup_female.setAlpha((float)0.3);
                    gender = "M";
                break;

            case R.id.signup_female:
                    signup_female.setAlpha((float)1);
                    signup_male.setAlpha((float)0.3);
                    gender = "F";
                break;

            case R.id.signup_button:
                    validateFields();
                break;

        }
    }


    private void validateFields() {

        username = signup_username.getText().toString();
        name = signup_name.getText().toString();
        email = signup_email.getText().toString();
        phoneno = signup_phoneno.getText().toString();
        String s_age = signup_age.getText().toString();
        if(s_age.length() > 0){
            age = Integer.parseInt(s_age);
        }
        city = signup_city.getText().toString();
        district = signup_district.getText().toString();

        if(s_age.length() == 0){
            Snackbar.make(view,"Empty age field!", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }else if(name.length() == 0){
            Snackbar.make(view,"Empty name field!", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }else if(email.length() == 0){
            Snackbar.make(view,"Empty email field!", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }else if(username.length() == 0){
            Snackbar.make(view,"Empty username field!", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }else if(phoneno.length() == 0){
            Snackbar.make(view,"Empty phoneno field!", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }else if(city.length() == 0){
            Snackbar.make(view,"Empty city field!", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }else if(district.length() == 0){
            Snackbar.make(view,"Empty district field!", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }else if(gender == null){
            Snackbar.make(view,"Please pick your gender", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }else{
            username_validate();
        }

    }

    private void username_validate() {
        if(username.contains(" ") || username.contains("@") || username.contains("#") ||
                username.contains("$") || username.contains("%") || username.contains("^") ||
                username.contains("&") || username.contains("*") || username.contains("(") ||
                username.contains(")") || username.contains("+") || username.contains("=") ||
                username.contains("!") || username.contains("~") || username.contains("`") ||
                username.contains("[") || username.contains("]") || username.contains("{") ||
                username.contains("}") || username.contains("\\") || username.contains("|") ||
                username.contains(";") || username.contains(":") || username.contains("\"") ||
                username.contains("'") || username.contains(",") || username.contains(".") ||
                username.contains("<") || username.contains(">") || username.contains("?") ||
                username.contains("/")
                ){
            Snackbar.make(view,"Username cannot have special characters", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }else{
            Log.d("USERNAME", "ENTERTED VALID USERNAME");

            BackendHelper.username_validate validate_username = new BackendHelper.username_validate();
            validate_username.execute(username, context);
        }
    }


    public static void registeruser(boolean value) {

        if(value){
            SharedPreferences sharedPreferences = context.getSharedPreferences("nk", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username",username);
            editor.putString("phoneno", phoneno);
            editor.putString("city", city);
            editor.putString("district", district);
            editor.putInt("age", age);
            editor.putString("gender", gender);
            editor.commit();

            BackendHelper.user_signup helper = new BackendHelper.user_signup();
            helper.execute(context);
        }else{
            Snackbar.make(view,"Username exists!\nChoose a new one more wisely", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }
    }


    public static void callMainActivity(){
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }


}
