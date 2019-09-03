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

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tsdreamdeveloper.mycontacts.R;

/**
 * @author Timur Seisembayev
 * @since 09.03.2019
 */

public class SplashActivity extends BaseActivity {

    private static final int DELAY_MILLIS = 1000;
    private Handler mWaitHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mWaitHandler.postDelayed(() -> {
            //The following code will execute after the 5 seconds.
            try {
                //Go to next page i.e, start the next activity.
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                //Let's Finish Splash Activity since we don't want to show this when user press back button.
                finish();
            } catch (Exception e) {
                Log.e(SplashActivity.class.getSimpleName(), "run: " + e.getMessage());
            }
        }, DELAY_MILLIS);  // Give a 1 seconds delay.
    }

    @Override
    protected void onDestroy() {
        //Remove all the callbacks otherwise navigation will execute even after activity is killed or closed.
        mWaitHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
