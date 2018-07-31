package com.example.surveillance6.vyakhya;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface Api {
    //    String BASE_URL = "http://192.168.0.114:5000/";
    String BASE_URL = "http://10.244.1.42:5000/";


    @POST("uploadimage")
    Call<ResultData> getresult(@Body RequestData requestData);

//    @GET("api/info_back_to_android")
//    Call<ResultData> greetUser();

}
