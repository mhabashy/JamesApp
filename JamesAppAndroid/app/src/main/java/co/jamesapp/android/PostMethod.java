package co.jamesapp.android;

import android.os.AsyncTask;
import android.provider.Settings;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/**
 * Created by michaelhabashy on 2/4/17.
 */

public class PostMethod extends AsyncTask<String, Integer, String> {


    private Config.OnTaskCompleted listener;
    private Map map = new HashMap();


    protected void setList(String k, Object v){
        map.put(k, v);
    };

    protected String queryString(){
        String sb = "";
        Iterator iterator = map.keySet().iterator();
        while(iterator.hasNext()){
            Object k   = iterator.next();
            Object v = map.get(k);
            sb += k.toString() + "=" + v.toString() + "&";
        }
        return sb.substring(0, sb.length() - 1);
    };


    Config config = new Config();
    @Override
    protected String doInBackground(String... params) {
        StringBuilder sb = new StringBuilder();
        HttpURLConnection urlConnection = null;
        String param = queryString();
        byte[] postData;
        try {
            URL url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Basic "+ config.getBasicHttp());
            urlConnection.setDoOutput( true );
            urlConnection.setInstanceFollowRedirects( false );
            urlConnection.setRequestMethod( "POST" );
            urlConnection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty( "charset", "utf-8");
            urlConnection.setRequestProperty("Content-Length", Integer.toString(param.length()));
            //postData =  queryString().getBytes(Charset.forName("UTF-8"));
            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter write = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            write.write(param);
            write.flush();
            urlConnection.connect();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bin = new BufferedReader(new InputStreamReader(in));
            String inputLine;
            while ((inputLine = bin.readLine()) != null) {
                sb.append(inputLine);
            }
            os.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return sb.toString();
    }

}
