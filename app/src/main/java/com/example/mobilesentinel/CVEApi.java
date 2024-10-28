package com.example.mobilesentinel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CVEApi {
    @GET("cve")
    Call<CVEResponse> getVulnerabilities(@Query("keywordSearch") String keyword);
}