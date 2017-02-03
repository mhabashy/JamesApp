package co.jamesapp.android;

/**
 * Created by michaelhabashy on 2/2/17.
 */

public class Config {
    private String basicHttp = "cGFja2FnZTpqb3NlcGhwZXRlcm1pY2hhZWw=";

    public String getBasicHttp(){
        return basicHttp;
    }

    public interface OnTaskCompleted{
        void onTaskCompleted();
    }
}
