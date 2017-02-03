package co.jamesapp.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.AsyncListUtil;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import co.jamesapp.android.Class.Users;


public class LoginActivity extends AppCompatActivity {
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public String getTheStatus(String c) {

        String status = "";
        try {
            JSONObject jsonObject = new JSONObject(c);
            status = jsonObject.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return status;
    }

    public String getTheData(String c) {

        String data = "";
        try {
            JSONObject jsonObject = new JSONObject(c);
            data = jsonObject.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);        // Set up the login form.

        SharedPreferences sharedPref = LoginActivity.this.getPreferences(Context.MODE_PRIVATE);
        if (!sharedPref.contains("email")) {
            setContentView(R.layout.activity_login);

            Button register = (Button) findViewById(R.id.register);
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                }
            });

            //NEED TO REMOVE LATER
            Button skip_to_map = (Button) findViewById(R.id.skip_to_map);
            skip_to_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, MapsActivity.class));
                }
            });

            //TEST
            Button sign = (Button) findViewById(R.id.sign);
            sign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //GetMethod.class.;
                    String tempString;
                    AsyncTask<String, Integer, String> tempA = new GetMethod().execute("http://159.203.131.184/api/users/select/?id=1");
                    try {
                        tempString = tempA.get();
                        Log.d("STATUS", tempString.toString());
                        Log.d("JSONSTATUS", getTheStatus(tempString));
                        Gson gson = new Gson();
                        Users myUsers = gson.fromJson(getTheData(tempString), Users.class);
                        Log.d("Email", myUsers.getEmail());
                        String jo = gson.toJson(myUsers);
                        Log.d("Back to list", jo);
                        Toast.makeText(LoginActivity.this, myUsers.getEmail(), Toast.LENGTH_SHORT).show();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    //(getTheStatus(temp.toString()));
                }
            });




        } else {
            Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
            startActivity(intent);
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Login Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
