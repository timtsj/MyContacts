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

package com.tsdreamdeveloper.mycontacts.api;

import com.tsdreamdeveloper.mycontacts.mvp.model.Contact;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

import static com.tsdreamdeveloper.mycontacts.common.Utils.USERS;


/**
 * @author Timur Seisembayev
 * @since 20.07.2019
 */
public interface Api {

    @GET(USERS)
    Single<List<Contact>> getUsers();
}
