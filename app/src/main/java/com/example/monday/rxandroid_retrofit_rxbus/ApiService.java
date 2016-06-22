package com.example.monday.rxandroid_retrofit_rxbus;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiService {
    @GET("tnfs/api/list")
    Call<TestModel> getModel(@Query("id") int id);

    @GET("tnfs/api/list")
    Observable<TestModel> getNewModel(@Query("id") int id);
}
