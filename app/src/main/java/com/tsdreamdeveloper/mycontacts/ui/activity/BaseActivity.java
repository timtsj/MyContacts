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

import android.app.ProgressDialog;
import android.support.v7.app.AlertDialog;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.tsdreamdeveloper.mycontacts.R;
import com.tsdreamdeveloper.mycontacts.mvp.view.BaseView;

/**
 * @author Timur Seisembayev
 * @since 22.07.2019
 */

public class BaseActivity extends MvpAppCompatActivity implements BaseView {
    public static final String NEGATIVE_BUTTON_TEXT = "Ok";
    private ProgressDialog mDialog;

    @Override
    public void showMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setNegativeButton(
                NEGATIVE_BUTTON_TEXT,
                (dialog, id) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void nextStep() {

    }

    @Override
    public void onLoadingFinish() {
        mDialog.dismiss();
    }

    @Override
    public void onLoadingStart() {
        mDialog = ProgressDialog.show(this, "",
                getString(R.string.loading_text), true);
    }
}
