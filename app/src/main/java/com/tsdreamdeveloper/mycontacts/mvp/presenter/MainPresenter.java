/*
 * Copyright 2019 TSDream Developer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tsdreamdeveloper.mycontacts.mvp.presenter;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.tsdreamdeveloper.mycontacts.api.App;
import com.tsdreamdeveloper.mycontacts.api.NetworkService;
import com.tsdreamdeveloper.mycontacts.common.rxUtils;
import com.tsdreamdeveloper.mycontacts.di.modules.SharedPrefsHelper;
import com.tsdreamdeveloper.mycontacts.mvp.model.Contact;
import com.tsdreamdeveloper.mycontacts.mvp.view.MainView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * @author Timur Seisembayev
 * @since 22.07.2019
 */

@InjectViewState
public class MainPresenter extends BasePresenter<MainView> {

    private final static String TAG = MainPresenter.class.getSimpleName();

    @Inject
    NetworkService networkService;

    @Inject
    SharedPrefsHelper sharedPrefsHelper;

    private PublishSubject<String> publishSubject;

    public MainPresenter() {
        App.getAppComponent().inject(this);
        publishSubject = PublishSubject.create();
    }

    /**
     * Fun for get contacts from api
     */
    public void getContacts() {
        getViewState().onLoadingStart();
        Disposable subscription = networkService
                .getUsers()
                .compose(rxUtils.applySchedulers())
                .subscribe(success -> {
                    result(success);
                }, throwable -> {
                    getViewState().onLoadingFinish();
                    Log.e(TAG, "getContacts: " + throwable.getMessage());
                    getViewState().showMessage("Error");
                });
        unsubscribeOnDestroy(subscription);
    }

    /**
     * Fun for pass received contacts list from api to adapter
     *
     * @param response contact list
     */
    private void result(List<Contact> response) {
        getViewState().onLoadingFinish();
        getViewState().addList(response);
    }

    /**
     * Observable for new query from search edit text
     *
     * @return observable
     */
    Observable<String> getSubject() {
        return publishSubject;
    }

    /**
     * Pass new query text to observable
     */
    public void onNext(CharSequence s, int start, int before, int count) {
        publishSubject.onNext(s.toString());
    }

    /**
     * Fun for subscribe to observable and if text changed pass it to adapter
     */
    public void onTextChanged() {
        // Set up the query listener that executes the search
        Disposable disposable =
                getSubject()
                        .debounce(2, TimeUnit.SECONDS)
                        .distinctUntilChanged()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(text -> text.toLowerCase().trim())
                        .subscribe(
                                text ->
                                        getViewState().textChanged(text),
                                throwable -> {
                                    Log.e(MainPresenter.class.getSimpleName(), "onTextChanged: " + throwable.getMessage());
                                });
        unsubscribeOnDestroy(disposable);
    }

    /**
     * Save filtered items to sph
     *
     * @param contactList filtered contact list
     */
    public void saveFilteredList(List<Contact> contactList) {
        sharedPrefsHelper.putContact(contactList);
        sharedPrefsHelper.putLastTime(System.currentTimeMillis());
    }

    /**
     * Get filtered item from sph
     *
     * @return filtered contacts list
     */
    public List<Contact> getFilteredList() {
        return sharedPrefsHelper.getContact();
    }

    /**
     * Helper fun for check last opened time
     *
     * @return true if activity opened more than 2 hour
     */
    public boolean isMoreThanTwoHour() {
        long lastTime = sharedPrefsHelper.getLastTime();
        return System.currentTimeMillis() - lastTime >= 120 * 60 * 1000;
    }
}
