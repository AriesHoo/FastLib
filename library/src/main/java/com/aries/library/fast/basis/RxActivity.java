package com.aries.library.fast.basis;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.aries.library.fast.enums.RxLifeCycle;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.PublishSubject;

/**
 * Created: AriesHoo on 2017/7/19 15:31
 * Function:根据Activity生命周期判断Retrofit的网络请求与否
 * Desc:
 */
abstract class RxActivity extends AppCompatActivity {
    protected final PublishSubject<RxLifeCycle> lifeCycleSubject = PublishSubject.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifeCycleSubject.onNext(RxLifeCycle.CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        lifeCycleSubject.onNext(RxLifeCycle.START);
    }

    @Override
    protected void onResume() {
        super.onResume();
        lifeCycleSubject.onNext(RxLifeCycle.RESUME);
    }

    @Override
    protected void onPause() {
        lifeCycleSubject.onNext(RxLifeCycle.PAUSE);
        super.onPause();
    }

    @Override
    protected void onStop() {
        lifeCycleSubject.onNext(RxLifeCycle.STOP);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        lifeCycleSubject.onNext(RxLifeCycle.DESTROY);
        super.onDestroy();
    }

    @NonNull
    public <T> Observable.Transformer<T, T> bindLifeCycle(@NonNull final RxLifeCycle event) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> sourceObservable) {
                Observable<RxLifeCycle> compareLifecycleObservable =
                        lifeCycleSubject.takeFirst(new Func1<RxLifeCycle, Boolean>() {
                            @Override
                            public Boolean call(RxLifeCycle lifeCycle) {
                                return lifeCycle.equals(event);
                            }
                        });
                return sourceObservable.takeUntil(compareLifecycleObservable);
            }
        };
    }
}
