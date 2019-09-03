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

import com.tsdreamdeveloper.mycontacts.api.Api;
import com.tsdreamdeveloper.mycontacts.api.NetworkService;

import dagger.Module;
import dagger.Provides;

/**
 * @author Timur Seisembayev
 * @since 20.07.2019
 */

@Module(includes = {ApiModule.class})
public class ContactsModule {
    @Provides
    public NetworkService provideService(Api api) {
        return new NetworkService(api);
    }
}
