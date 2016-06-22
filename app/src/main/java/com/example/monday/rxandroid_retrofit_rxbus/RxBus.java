package com.example.monday.rxandroid_retrofit_rxbus;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by Zoey on 2016/6/21.
 */
public class RxBus {
    private final Subject<Object, Object> rx_bus;

    //单例
    private static class RxBusHolder {
        private static final RxBus instance = new RxBus();
    }

    private RxBus() {
        rx_bus = new SerializedSubject<>(PublishSubject.create());
    }

    public static synchronized RxBus getInstance() {
        return RxBusHolder.instance;
    }

    public void post(Object o) {
        rx_bus.onNext(o);
    }

    public <T> Observable<T> toObserverable(Class<T> eventType) {
        return rx_bus.ofType(eventType);
    }
}
