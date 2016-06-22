package com.example.monday.rxandroid_retrofit_rxbus;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by User on 2016/6/21.
 */
public class HttpUtils {
    public static final String BASE_URL="http://www.tngou.net/";

    private static final int DEFAUT_TIMEOUT=5;

    private Retrofit retrofit;
    private ApiService mApiService;

    /**
     * 构造方法私有
     */
    private HttpUtils(){
        //手动创建一个okhttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder=new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAUT_TIMEOUT, TimeUnit.SECONDS);

        retrofit= new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        mApiService=retrofit.create(ApiService.class);
    }

    /**
     * 在访问HttpUtils时创建单例
     */
    private static class SingleHolder{
        private static final HttpUtils INSTANCE=new HttpUtils();
    }

    /**
     * 获取单例
     */
    public static HttpUtils getInstance(){
        return SingleHolder.INSTANCE;
    }

    /**
     * 获取信息
     * @param subscriber 由调用者传过来的观察者对象
     * @param position
     */
    public void getMessage(Subscriber<TestModel> subscriber,int position){
        mApiService.getNewModel(position)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
