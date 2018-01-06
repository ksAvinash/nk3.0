package smartAmigos.com.nammakarnataka.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import smartAmigos.com.nammakarnataka.GoogleSigninActivity;
import smartAmigos.com.nammakarnataka.R;
import smartAmigos.com.nammakarnataka.SignupActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by avinashk on 05/01/18.
 */

public class BackendHelper {

    /*
    THE SIGNUP HELPER CLASS
    1) When invoked used the sharedpreference to get all user's details
    2) Sends a post request to the backend
    3) Parse the response back from the backend
            if value of user-signup is true
                then the value of isSignedIn is set to true --> used in SplasherActivity
    4) Intents to MainActivity
    */
    public static class user_signup extends AsyncTask<Object, String, String>{
        Context context;
        SharedPreferences sharedPreferences;

        @Override
        protected void onPostExecute(String str) {

            super.onPostExecute(str);
            if(str != null){
                try {
                    JSONObject object = new JSONObject(str);
                    boolean user_signup = object.getBoolean("user_signup");
                    if(user_signup){
                        sharedPreferences = context.getSharedPreferences("nk", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isSignedIn", true);
                        editor.commit();

                        SignupActivity.callMainActivity();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected String doInBackground(Object... objects) {

            context = (Context) objects[0];
            sharedPreferences = context.getSharedPreferences("nk", MODE_PRIVATE);
            String email = sharedPreferences.getString("email", "");
            String name = sharedPreferences.getString("name","");
            String username = sharedPreferences.getString("username", "");
            String gender = sharedPreferences.getString("gender", "M");
            int age = sharedPreferences.getInt("age",20);
            String phoneno = sharedPreferences.getString("phoneno", "");
            String city = sharedPreferences.getString("city","");
            String district = sharedPreferences.getString("district", "");

            try{
                URL url = new URL(context.getResources().getString(R.string.app_signup_url));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("email", email);
                jsonParam.put("name", name);
                jsonParam.put("username", username);
                jsonParam.put("gender", gender);
                jsonParam.put("age", age);
                jsonParam.put("phoneno", phoneno);
                jsonParam.put("city", city);
                jsonParam.put("district", district);


                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(jsonParam.toString());
                os.flush();
                BufferedReader serverAnswer = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String response = serverAnswer.readLine();
                Log.d("NK_BACKEND", "RESPONSE : "+response);

                os.close();
                conn.disconnect();

                return response;
            }catch (Exception e){
                Log.d("NK_BACKEND", "Error registering user");
                Log.d("NK_BACKEND", e.toString());
            }
            return null;
        }
    }



    /*
    THE USERNAME_VALIDATE CLASS
    1) Accepts a username from the signup from
    2) Checks the database for the username in nk_users
    3) Calls the registeruser function to either register the user or display "username exists!"
    */
    public static class username_validate extends AsyncTask<Object, String, String >{

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            if(str != null){
                try {
                    JSONObject object = new JSONObject(str);
                    boolean usernameValidate = object.getBoolean("username_validate");
                    if(usernameValidate){
                        SignupActivity.registeruser(true);
                    }else{
                        SignupActivity.registeruser(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        @Override
        protected String doInBackground(Object... objects) {
            String username = (String)objects[0];
            Context context = (Context) objects[1];
            try{
                URL url = new URL(context.getResources().getString(R.string.validate_username));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("username", username);


                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(jsonParam.toString());
                os.flush();
                BufferedReader serverAnswer = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String response = serverAnswer.readLine();
                Log.d("NK_BACKEND", "RESPONSE : "+response);

                os.close();
                conn.disconnect();

                return response;
            }catch (Exception e){
                Log.d("NK_BACKEND", "Error validating username");
                Log.d("NK_BACKEND", e.toString());
            }
            return null;

        }
    }

    /*
    THE VALIDATE_EXISTING_USER CLASS
    1) Accepts email as parameter
    2) Checks the nk_users table
    3) If user exists --> Intents to Main Activity
    4) Else --> Intents to Signup Activity

    */
    public static class validate_existing_user extends AsyncTask<Object, String, String>{
        Context context;
        SharedPreferences sharedPreferences;
        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            if(str != null){
                try {
                    JSONObject object = new JSONObject(str);
                    boolean validate_existing_user = object.getBoolean("validate_existing_user");
                    if(validate_existing_user){
                        sharedPreferences = context.getSharedPreferences("nk", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isSignedIn", true);
                        editor.commit();
                        GoogleSigninActivity.callSignupActivity(true);
                    }else{
                        GoogleSigninActivity.callSignupActivity(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }

        @Override
        protected String doInBackground(Object... objects) {

            String email = (String)objects[0];
            context = (Context) objects[1];
            try{
                URL url = new URL(context.getResources().getString(R.string.validate_existing_user));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("email", email);


                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(jsonParam.toString());
                os.flush();
                BufferedReader serverAnswer = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String response = serverAnswer.readLine();
                Log.d("NK_BACKEND", "RESPONSE : "+response);

                os.close();
                conn.disconnect();

                return response;
            }catch (Exception e){
                Log.d("NK_BACKEND", "Error validating user email");
                Log.d("NK_BACKEND", e.toString());
            }
            return null;
        }
    }





    public static class firebase_id_update extends AsyncTask <Context, String, String> {

        @Override
        protected String doInBackground(Context... params) {
            //use the context sent as parameter to access the sharedpreference
            Context context = params[0];
            SharedPreferences sharedPreferences = context.getSharedPreferences("nk", MODE_PRIVATE);
            String email = sharedPreferences.getString("email", "");

            try{
                URL url = new URL(context.getResources().getString(R.string.firebase_id_update));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                String token = FirebaseInstanceId.getInstance().getToken();
                Log.d("NK_BACKEND", token);
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("email", email);
                jsonParam.put("firebase_id",token);


                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(jsonParam.toString());
                os.flush();
                BufferedReader serverAnswer = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                Log.d("NK_BACKEND", "RESPONSE : "+serverAnswer.readLine());

                os.close();
                conn.disconnect();

                return serverAnswer.readLine();
            }catch (Exception e){
                Log.d("NK_BACKEND", "Error updating firebase-id");
                Log.d("NK_BACKEND", e.toString());
            }
            return null;
        }
    }



}