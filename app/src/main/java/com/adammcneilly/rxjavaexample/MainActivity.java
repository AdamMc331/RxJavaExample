package com.adammcneilly.rxjavaexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> test = new ArrayList<>();
        test.add("Adam");
        test.add("TJ");
        test.add("Fred");
        test.add("Michael");
        test.add("Ron");
        test.add("Jacob");
        createObservableWithTake(test);
    }

    //region PART 1
    private void createHelloWorldObservable() {
        // Create observable
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello, world!");
                subscriber.onCompleted();
            }
        });

        // Create subscriber
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.v(LOG_TAG, s);
            }
        };

        // Subscribe to observable
        observable.subscribe(subscriber);
    }

    private void createHelloWorldObservableSimpler() {
        // Simplify using Observable.just() which emits a single item then completes:
        Observable<String> observable = Observable.just("Hello, world!");

        // Since we don't care about onCompleted() or onError(), use a simpler class to define what to do in onNext().
        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.v(LOG_TAG, s);
            }
        };

        // Observable.subscribe() can handle one, two, or three Action parameter for onNExt, onError, and onComplete
        // Since we just have onNext, ew only need the first one.
        observable.subscribe(onNextAction);
    }

    private void createHelloWorldObservableChained() {
        // Get rid of variables by chaining
        Observable.just("Hello, world!").subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.v(LOG_TAG, s);
            }
        });
    }

    private void createHelloWorldObservableWithMaps() {
        // Use map operator to transform one emitted item into another
        Observable.just("Hello, world!")
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return s + " - Adam";
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.v(LOG_TAG, s);
                    }
                });

        // Note that type doesn't ahve to be the same
        // Use map to send hash code for string instead
        Observable.just("Hello, world!")
                .map(new Func1<String, Integer>() {
                    @Override
                    public Integer call(String s) {
                        return s.hashCode();
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.v(LOG_TAG, String.valueOf(integer));
                    }
                });
    }
    //endregion

    //region PART 2
    private void createHelloWorldObservableWithFlatMap(List<String> strings) {
        Observable.just(strings)
                .flatMap(new Func1<List<String>, Observable<String>>() {
                    @Override
                    public Observable<String> call(List<String> strings) {
                        return Observable.from(strings);
                    }
                }).subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.v(LOG_TAG, s);
                    }
                });
    }

    private void createFlatMapFromObservableToString(List<String> strings) {
        Observable.just(strings)
                .flatMap(new Func1<List<String>, Observable<String>>() {
                    @Override
                    public Observable<String> call(List<String> strings) {
                        return Observable.from(strings);
                    }
                })
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        return Observable.just(s);
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.v(LOG_TAG, s);
                    }
                });
    }

    private void createObservableWithFilter(List<String> strings) {
        Observable.just(strings)
                .flatMap(new Func1<List<String>, Observable<String>>() {
                    @Override
                    public Observable<String> call(List<String> strings) {
                        return Observable.from(strings);
                    }
                })
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        return Observable.just(s);
                    }
                })
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return (!s.equals("Adam"));
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.v(LOG_TAG, s);
                    }
                });
    }

    private void createObservableWithTake(List<String> strings) {
        Observable.just(strings)
                .flatMap(new Func1<List<String>, Observable<String>>() {
                    @Override
                    public Observable<String> call(List<String> strings) {
                        return Observable.from(strings);
                    }
                })
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        return Observable.just(s);
                    }
                })
                .take(5)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.v(LOG_TAG, s);
                    }
                });
    }
    //endregion
}
