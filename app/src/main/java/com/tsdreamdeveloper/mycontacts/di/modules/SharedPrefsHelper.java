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

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tsdreamdeveloper.mycontacts.api.App;
import com.tsdreamdeveloper.mycontacts.mvp.model.Contact;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Seisembayev Timur
 * @since 22.07.2019
 */

public class SharedPrefsHelper {

    public static final String PREFS_CURRENT_USER = "CurrentContact";
    public static final String LAST_TIME = "lastTime";
    private SharedPreferences mSharedPreferences;
    private Context mContext;
    private App app;

    public SharedPrefsHelper(SharedPreferences sharedPreferences, Context context) {
        this.mContext = context;
        mSharedPreferences = sharedPreferences;
        app = (App) context;
    }

    public void put(String key, String value) {
        mSharedPreferences.edit().putString(key, value).apply();
    }

    public void put(String key, int value) {
        mSharedPreferences.edit().putInt(key, value).apply();
    }

    public void put(String key, long value) {
        mSharedPreferences.edit().putLong(key, value).apply();
    }

    public void put(String key, float value) {
        mSharedPreferences.edit().putFloat(key, value).apply();
    }

    public void put(String key, boolean value) {
        mSharedPreferences.edit().putBoolean(key, value).apply();
    }

    public String get(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    public Integer get(String key, int defaultValue) {
        return mSharedPreferences.getInt(key, defaultValue);
    }

    public Long get(String key, long defaultValue) {
        return mSharedPreferences.getLong(key, defaultValue);
    }

    public Float get(String key, float defaultValue) {
        return mSharedPreferences.getFloat(key, defaultValue);
    }

    public Boolean get(String key, boolean defaultValue) {
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    public void putContact(List<Contact> contact) {
        SharedPreferences.Editor prefsEditor = mSharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(contact);
        prefsEditor.putString(PREFS_CURRENT_USER, json);
        prefsEditor.apply();
    }

    public List<Contact> getContact() {
        Gson gson = new Gson();
        String json = mSharedPreferences.getString(PREFS_CURRENT_USER, "");
        Type listType = new TypeToken<List<Contact>>(){}.getType();
        List<Contact> contact = gson.fromJson(json, listType);
        return contact;
    }

    public void putLastTime(long value) {
        mSharedPreferences.edit().putLong(LAST_TIME, value).apply();
    }

    public long getLastTime() {
        return mSharedPreferences.getLong(LAST_TIME, 0);
    }

    public void deleteSavedData(String key) {
        mSharedPreferences.edit().remove(key).apply();
    }

    public boolean containsKey(String key) {
        return mSharedPreferences.contains(key);
    }

}
