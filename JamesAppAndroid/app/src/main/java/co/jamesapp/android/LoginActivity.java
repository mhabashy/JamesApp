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
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import co.jamesapp.android.Class.GetJson;
import co.jamesapp.android.Class.Users;


public class LoginActivity extends AppCompatActivity {
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    EditText email;
    EditText password;

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

            email = (EditText) findViewById(R.id.email);
            password = (EditText) findViewById(R.id.password);

            // GET EXAMPLE

//            sign.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //GetMethod.class.;
//                    String tempString;
//                    AsyncTask<String, Integer, String> tempA = new GetMethod().execute("http://159.203.131.184/api/users/select/?id=1");
//                    try {
//                        tempString = tempA.get();
//                        Log.d("STATUS", tempString.toString());
//                        Log.d("JSONSTATUS", getStatus(tempString));
//                        Gson gson = new Gson();
//                        Users myUsers = gson.fromJson(getTheData(tempString), Users.class);
//                        Log.d("Email", myUsers.getEmail());
//                        String jo = gson.toJson(myUsers);
//                        Log.d("Back to list", jo);
//                        Toast.makeText(LoginActivity.this, myUsers.getEmail(), Toast.LENGTH_SHORT).show();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    } catch (ExecutionException e) {
//                        e.printStackTrace();
//                    }
//                    //(getStatus(temp.toString()));
//                }
//            });

            // POST EXAMPLE

            sign.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
//                    //GetMethod.class.;
                    String tempString;
                    PostMethod pm = new PostMethod();
                    GetJson gj = new GetJson();
                    Gson gs = new Gson();
                    Log.i("EMAIL", email.getText().toString());
                    Log.i("Password", password.getText().toString());
                    if( !(email.getText()).toString().equals("") && !(password.getText().toString()).equals("")) {
                        pm.setList("email", email.getText().toString());
                        pm.setList("password", password.getText().toString());
                        AsyncTask<String, Integer, String> tempA = pm.execute("http://159.203.131.184/api/users/post/");                    try {
                            tempString = tempA.get();
                            if(gj.getStatus(tempString).equals("success")){
                                SharedPreferences sharedPref = LoginActivity.this.getPreferences(Context.MODE_PRIVATE);

                                SharedPreferences.Editor e = sharedPref.edit();
                                int i = gj.getId(tempString);
                                e.putInt("id", i);
                                Users u = gs.fromJson(gj.getData(tempString), Users.class);
                                e.putString("email", u.getEmail());

                                Toast.makeText(LoginActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                
                                Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                                startActivity(intent);

                                //Toast.makeText(LoginActivity.this, ), Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(LoginActivity.this, "Incorrect Login", Toast.LENGTH_SHORT).show();
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Toast.makeText(LoginActivity.this, "Please Enter a Email and Password", Toast.LENGTH_SHORT).show();
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
