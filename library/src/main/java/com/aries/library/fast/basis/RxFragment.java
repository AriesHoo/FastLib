package com.aries.library.fast.basis;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.aries.library.fast.enums.RxLifeCycle;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.PublishSubject;

/**
 * Created: AriesHoo on 2017/7/19 15:34
 * Function:根据Fragment生命周期判断Retrofit的网络请求与否
 * Desc:
 */
abstract class RxFragment extends Fragment {
    protected final PublishSubject<RxLifeCycle> lifeCycleSubject = PublishSubject.create();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        lifeCycleSubject.onNext(RxLifeCycle.ATTACH);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifeCycleSubject.onNext(RxLifeCycle.CREATE);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lifeCycleSubject.onNext(RxLifeCycle.CREATE_VIEW);
    }

    @Override
    public void onStart() {
        super.onStart();
        lifeCycleSubject.onNext(RxLifeCycle.START);
    }

    @Override
    public void onResume() {
        super.onResume();
        lifeCycleSubject.onNext(RxLifeCycle.RESUME);
    }

    @Override
    public void onPause() {
        lifeCycleSubject.onNext(RxLifeCycle.PAUSE);
        super.onPause();
    }

    @Override
    public void onStop() {
        lifeCycleSubject.onNext(RxLifeCycle.STOP);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        lifeCycleSubject.onNext(RxLifeCycle.DESTROY_VIEW);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        lifeCycleSubject.onNext(RxLifeCycle.DESTROY);
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        lifeCycleSubject.onNext(RxLifeCycle.DETACH);
        super.onDetach();
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
