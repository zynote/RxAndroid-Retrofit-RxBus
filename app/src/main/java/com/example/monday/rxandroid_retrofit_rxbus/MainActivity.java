package com.example.monday.rxandroid_retrofit_rxbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.plugins.RxJavaErrorHandler;
import rx.plugins.RxJavaPlugins;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    Subscriber<TestModel> subscriber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        RxJavaPlugins.getInstance().registerErrorHandler(new RxJavaErrorHandler() {
//            @Override
//            public void handleError(Throwable e) {
//                Log.e("TAG","handleError=="+e);
//            }
//        });
        init3();
        Button button=(Button)findViewById(R.id.bt_button);
        //判断短时间内不能重复点击
        RxView.clicks(button).debounce(300, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

                Intent intent =new Intent(MainActivity.this,SecondActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //轮询操作，每两秒执行一次
        Observable.interval(2,2,TimeUnit.SECONDS).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                //TODO EVERTHING YOU WANT
        //                RxBus.getInstance().post("我喜欢你");

            }
        });

        //定时操作，两秒后执行
        Observable.timer(2, TimeUnit.SECONDS).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                //TODO WHAT YOU WANT
                RxBus.getInstance().post("我喜欢你");
            }
        });
    }

/**
 * 用Retrofit请求网络
 */
private void init() {
    //拦截器
    HttpLoggingInterceptor http=new HttpLoggingInterceptor();
    http.setLevel(HttpLoggingInterceptor.Level.BODY);
    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(http).build();

    Retrofit retrofit=new Retrofit.Builder()
            .baseUrl("http://www.tngou.net/")   //这里用的天狗云的公共api
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();
    ApiService apiService = retrofit.create(ApiService.class);

    Call<TestModel> call=apiService.getModel(1);
    call.enqueue(new Callback<TestModel>() {
        @Override
        public void onResponse(Call<TestModel> call, Response<TestModel> response) {
            Log.e("TAG","成功");
        }

        @Override
        public void onFailure(Call<TestModel> call, Throwable t) {

        }
    });
}

/**
 * 用rxAndroid + Retrofit请求网络数据
 */
public void init2(){
    Retrofit retrofit=new Retrofit.Builder()
            .baseUrl("http://www.tngou.net/")   //这里用的天狗云的公共api
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();
    ApiService apiService = retrofit.create(ApiService.class);

    apiService.getNewModel(1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<TestModel>() {
                @Override
                public void onCompleted() {
                    Log.e("TAG","onNext");
                }

                @Override
                public void onError(Throwable e) {
                    Log.e("TAG","onError="+e);
                }

                @Override
                public void onNext(TestModel testModel) {
                    Log.e("TAG","onNext");
                    Log.e("TAG","onNext"+testModel.getTngou().get(0).getTitle());
                }
            });
}


    private void init3(){
        subscriber = new Subscriber<TestModel>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(TestModel testModel) {
                Log.e("TAG","onNext"+testModel.getTngou().get(0).getTitle());
            }
        };
        HttpUtils.getInstance().getMessage(subscriber,1);
    }

}
