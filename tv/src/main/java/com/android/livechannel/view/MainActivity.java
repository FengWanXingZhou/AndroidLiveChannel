/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.android.livechannel.view;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.media.tv.TvContract;
import android.media.tv.TvInputInfo;
import android.media.tv.TvInputManager;
import android.media.tv.TvView;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.android.livechannel.R;
import com.android.livechannel.adapter.ChannelAdapter;
import com.android.livechannel.adapter.InputListAdapter;
import com.android.livechannel.bean.TifChannelEntity;
import com.android.livechannel.util.CommonUtils;
import com.android.livechannel.util.TifChannelUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/*
 * MainActivity class that loads {@link MainFragment}.
 */
public class MainActivity extends AppCompatActivity{

    private TvView mTvView;
    private FragmentManager mFragmentManager;
    private MainFragment mMainFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvView = findViewById(R.id.tvView);
        //init();
        setDefaultFragment();
    }

    private void setDefaultFragment(){

        mFragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        mMainFragment = new MainFragment();
        fragmentTransaction.replace(R.id.main_browse_fragment,mMainFragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if(event.getKeyCode() == KeyEvent.KEYCODE_INFO){
            if(!mMainFragment.isVisible()){
                setDefaultFragment();
                return true;
            }
        }
        return super.dispatchKeyEvent(event);


    };

    public void playChannel(TifChannelEntity tifChannelEntity){
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.remove(mMainFragment);
        fragmentTransaction.commit();

        mTvView.setVisibility(View.VISIBLE);
        mTvView.setZOrderMediaOverlay(true);
        mTvView.setZOrderOnTop(true);
        mTvView.tune(tifChannelEntity.getInputId(),TvContract.buildChannelUri(tifChannelEntity.getId()));

    }




}
