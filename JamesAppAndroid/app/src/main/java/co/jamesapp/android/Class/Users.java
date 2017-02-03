package co.jamesapp.android.Class;

/**
 * Created by michaelhabashy on 2/2/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Users {
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("fname")
    @Expose
    private String fname;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("lname")
    @Expose
    private String lname;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

}
