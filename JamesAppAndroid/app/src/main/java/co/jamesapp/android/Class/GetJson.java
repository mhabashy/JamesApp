package co.jamesapp.android.Class;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by michaelhabashy on 2/4/17.
 */

public class GetJson {

    public String getStatus(String c) {

        String status = "";
        try {
            JSONObject jsonObject = new JSONObject(c);
            status = jsonObject.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return status;
    }

    public String getData(String c) {

        String data = "";
        try {
            JSONObject jsonObject = new JSONObject(c);
            data = jsonObject.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    public int getId(String c){
        int data = 0;
        try {
            JSONObject jsonObject = new JSONObject(c);
            data = jsonObject.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

}
