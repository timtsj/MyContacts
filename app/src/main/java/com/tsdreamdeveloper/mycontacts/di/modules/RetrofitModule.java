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

package com.tsdreamdeveloper.mycontacts.di.modules;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tsdreamdeveloper.mycontacts.BuildConfig;
import com.tsdreamdeveloper.mycontacts.common.ConnectivityInterceptor;
import com.tsdreamdeveloper.mycontacts.common.LiveNetworkMonitor;
import com.tsdreamdeveloper.mycontacts.common.Utils;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Observable;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.tsdreamdeveloper.mycontacts.common.Utils.AUTHORIZATION_HEADER;
import static com.tsdreamdeveloper.mycontacts.common.Utils.BEARER;
import static com.tsdreamdeveloper.mycontacts.di.modules.SharedPrefsHelper.PREFS_CURRENT_USER;

/**
 * @author Timur Seisembayev
 * @since 20.07.2019
 */

@Module
public class RetrofitModule {

    private static final int TIMEOUT = 1;
    private Retrofit retrofit;
    private Context mContext;
    private String mHttpUrlBasement;

    public RetrofitModule(Application app) {
        mHttpUrlBasement = Utils.BASE_URL;
        mContext = app.getApplicationContext();
    }

    @Provides
    public Retrofit provideRetrofit() {
        OkHttpClient httpClient = provideHttpClient();
        Gson gson = provideGson();

        if (retrofit == null) {
            Converter.Factory gsonConverterFactory = GsonConverterFactory.create(gson);
            retrofit = new Retrofit.Builder()
                    .baseUrl(mHttpUrlBasement)
                    .client(httpClient)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    @Provides
    public OkHttpClient provideHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client;
        // provideInterceptorIsConnected checked is network available
        client = new OkHttpClient.Builder()
                .addInterceptor(provideInterceptorIsConnected(mContext))
                .connectTimeout(TIMEOUT, TimeUnit.MINUTES)
                .readTimeout(TIMEOUT, TimeUnit.MINUTES)
                .writeTimeout(TIMEOUT, TimeUnit.MINUTES);

        if (BuildConfig.DEBUG) {
            client.addInterceptor(logging);
        }
        return client.build();
    }

    @Provides
    public Interceptor provideInterceptorIsConnected(Context getCurrentContext) {
        Observable<Boolean> rxNetworkMonitor =
                Observable.fromCallable(new LiveNetworkMonitor(getCurrentContext));
        return new ConnectivityInterceptor(rxNetworkMonitor);
    }


    @Provides
    public Gson provideGson() {
        return new GsonBuilder()
                .setLenient()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }
}
