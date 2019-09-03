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

package com.tsdreamdeveloper.mycontacts.di;

import android.content.Context;

import com.tsdreamdeveloper.mycontacts.api.NetworkService;
import com.tsdreamdeveloper.mycontacts.di.modules.ContactsModule;
import com.tsdreamdeveloper.mycontacts.di.modules.ContextModule;
import com.tsdreamdeveloper.mycontacts.di.modules.SharedPrefsHelper;
import com.tsdreamdeveloper.mycontacts.di.modules.SharedPrefsModule;
import com.tsdreamdeveloper.mycontacts.mvp.presenter.MainPresenter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Timur Seisembayev
 * @since 20.07.2019
 */

@Singleton
@Component(modules = {ContextModule.class, ContactsModule.class, SharedPrefsModule.class})
public interface AppComponent {
    Context getContext();

    SharedPrefsHelper getSharedPrefsHelper();

    NetworkService getContactsService();

    void inject(MainPresenter mainPresenter);
}
