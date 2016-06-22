package com.example.monday.rxandroid_retrofit_rxbus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by User on 2016/6/21.
 */
public class SecondActivity extends AppCompatActivity {
    Subscription mSubscription;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getMessage();
    }

    private void getMessage() {

        mSubscription = RxBus.getInstance().toObserverable(String.class).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e("啊啊啊","s="+s);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消订阅
        mSubscription.unsubscribe();
    }

}
