package com.example.mobilesentinel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CVEApi {
    @GET("cves/1.0") // Endpoint to fetch CVEs (modify as per the API provider)
    Call<CVEResponse> getVulnerabilities(@Query("keyword") String keyword);
}