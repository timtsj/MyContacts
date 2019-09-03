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

package com.tsdreamdeveloper.mycontacts.common;


import android.util.Log;

import com.tsdreamdeveloper.mycontacts.common.error_handler.NoNetworkException;

import java.io.IOException;

import io.reactivex.Observable;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * @author Timur Seisembayev
 * @since 20.07.2019
 */

public class ConnectivityInterceptor implements Interceptor {

    private boolean isNetworkActive;

    public ConnectivityInterceptor(Observable<Boolean> isNetworkActive) {
        isNetworkActive.subscribe(
                _isNetworkActive -> this.isNetworkActive = _isNetworkActive,
                _error -> Log.e("", "NetworkActive error " + _error.getMessage()));
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!isNetworkActive) {
            throw new NoNetworkException();
        } else {
            Response response = chain.proceed(chain.request());
            return response;
        }
    }
}

