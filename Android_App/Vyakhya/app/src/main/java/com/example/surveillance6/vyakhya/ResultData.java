package com.example.surveillance6.vyakhya;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Manas Bhardwaj on 27/7/18.
 */

public class ResultData {
    @SerializedName("Image Data")
    @Expose
    private String data;

    public String getData(){
        return data;
    }

    public void setData(String data){
        this.data = data;
    }
}
