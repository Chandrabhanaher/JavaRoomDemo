package com.chan.javaroomdemo.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiService {
    @GET("images/I/71HCGJ+-3hL._SL1200_.jpg")
    Call<ResponseBody> downloadImage();
}
