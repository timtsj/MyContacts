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

package com.tsdreamdeveloper.mycontacts.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.tsdreamdeveloper.mycontacts.R;
import com.tsdreamdeveloper.mycontacts.mvp.model.Contact;
import com.tsdreamdeveloper.mycontacts.mvp.presenter.MainPresenter;
import com.tsdreamdeveloper.mycontacts.mvp.view.MainView;
import com.tsdreamdeveloper.mycontacts.ui.adapter.ContactAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * @author Timur Seisembayev
 * @since 09.03.2019
 */

public class MainActivity extends BaseActivity implements MainView {

    private static final String TAG = MainActivity.class.getSimpleName();

    @InjectPresenter
    MainPresenter presenter;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.et_search)
    EditText etSearch;

    private ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        RxJavaPlugins.setErrorHandler(throwable -> {
            Log.e(TAG, "onCreate: " + throwable.getMessage());
        });
        //Get contacts from api
        presenter.getContacts();
        initRecyclerView();
        presenter.onTextChanged();
    }

    /**
     * Implement RecyclerView and Adapter
     */
    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ContactAdapter();
        recyclerView.setAdapter(adapter);
    }

    /**
     * Add contacts list from api and restore filtered list from sph if app opened less than 2 hour ago
     *
     * @param contacts list from api
     */
    @Override
    public void addList(List<Contact> contacts) {
        adapter.clearItems();
        adapter.setItems(contacts);
        if (!presenter.isMoreThanTwoHour()) {
            adapter.setFilteredItems(presenter.getFilteredList());
        }
    }

    /**
     * TextWatcher for search edit query
     *
     * @param query changed query
     */
    @OnTextChanged(R.id.et_search)
    public void onTextChanged(CharSequence query) {
        presenter.onNext(query.toString());
    }

    /**
     * Edit text listener for done action
     *
     * @param actionId The code of the action being performed.
     * @param event    Object used to report key and button events.
     * @return true if action pressed
     */
    @OnEditorAction(R.id.et_search)
    public boolean onEditorAction(int actionId, KeyEvent event) {
        if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
            textChanged(etSearch.getText().toString().trim());
            return true;
        }
        return false;
    }

    /**
     * New query text from edit text passed to adapter for show filtered items
     *
     * @param text from search edit text
     */
    @Override
    public void textChanged(String text) {
        adapter.getFilter().filter(text);
    }

    /**
     * After activity stopped save filtered list to sph
     */
    @Override
    protected void onStop() {
        super.onStop();
        presenter.saveFilteredList(adapter.getFilteredItems());
    }
}
