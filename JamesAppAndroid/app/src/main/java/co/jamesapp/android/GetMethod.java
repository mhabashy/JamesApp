package co.jamesapp.android;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import co.jamesapp.android.Config;

/**
 * Created by michaelhabashy on 2/2/17.
 */

public class GetMethod extends AsyncTask<String, Integer, String> {

    private Config.OnTaskCompleted listener;


    //    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    public static String request(String uri)  throws IOException{
//    }
    Config config = new Config();
    @Override
    protected String doInBackground(String... params) {
        StringBuilder sb = new StringBuilder();
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Basic "+ config.getBasicHttp());
            urlConnection.connect();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bin = new BufferedReader(new InputStreamReader(in));
            String inputLine;
            while ((inputLine = bin.readLine()) != null) {
                sb.append(inputLine);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return sb.toString();
    }

//    protected String doInBackground(String... u)
//    {
//        // do something in background
//        return null;
//    }
//    protected void onPreExecute()
//    {
//        // do something before start
//    }
//    public void onProgressUpdate(Integer... args)
//    {
//
//    }
//    protected void onPostExecute(String result)
//    {
//        //  do something after execution
//
//    }
}
