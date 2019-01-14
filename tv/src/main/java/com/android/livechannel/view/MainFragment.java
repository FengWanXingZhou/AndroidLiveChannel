package com.android.livechannel.view;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.media.tv.TvInputInfo;
import android.media.tv.TvInputManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.livechannel.R;
import com.android.livechannel.adapter.ChannelAdapter;
import com.android.livechannel.adapter.InputListAdapter;
import com.android.livechannel.bean.TifChannelEntity;
import com.android.livechannel.util.CommonUtils;
import com.android.livechannel.util.TifChannelUtils;
import com.android.livechannel.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainFragment extends Fragment  implements InputListAdapter.InputClickListener
        ,ChannelAdapter.ChannelClickListener{

    private TvInputManager mTvInputManager;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private HashMap<String,TvInputInfo> mTvInputInfoHashMap = new HashMap<>();
    private InputListAdapter mInputListAdapter;
    private ChannelAdapter mChannelAdapter;
    private RecyclerView mInputRecyclerView;
    private RecyclerView mChannelRecyclerView;
    private List<TifChannelEntity> mChannelEntities;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_input_list,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }


    private void init(){
        mTvInputManager = (TvInputManager)getActivity().getSystemService(Context.TV_INPUT_SERVICE);
        mTvInputManager.registerCallback(mTvInputCallback,mHandler);

        initInputView();
        initChannelView();

        generateInputList();
        generateChannelList();
    }

    private void initInputView(){
        mInputRecyclerView = getView().findViewById(R.id.rv_input_list);
        mInputListAdapter = new InputListAdapter(getContext());
        mInputListAdapter.registerInputClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        mInputRecyclerView.setLayoutManager(linearLayoutManager);
        mInputRecyclerView.setAdapter(mInputListAdapter);
    }

    private void initChannelView(){
        mChannelRecyclerView = getView().findViewById(R.id.rv_channel_list);
        mChannelAdapter = new ChannelAdapter(getContext());
        mChannelAdapter.registerChannelClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        mInputRecyclerView.setLayoutManager(linearLayoutManager);
        mChannelRecyclerView.setAdapter(mChannelAdapter);
    }


    private void updateInputData(){

        mInputListAdapter.updateData(getInputList());
        mInputListAdapter.notifyDataSetChanged();
    }

    private void updateChannelData(){
        mChannelAdapter.updateData(mChannelEntities);
        mChannelAdapter.notifyDataSetChanged();
    }


    private void generateInputList(){

        List<TvInputInfo> tvInputInfoList = mTvInputManager.getTvInputList();

        if(tvInputInfoList!=null) {
            Util.LOG("tv input size:" + tvInputInfoList.size());
            for(TvInputInfo tvInputInfo:tvInputInfoList){
                Util.LOG("tv input info:" + tvInputInfo.loadLabel(getContext()));
            }

        }
        insertInputList(tvInputInfoList);
        updateInputData();


    }

    private void generateChannelList(){
        mChannelEntities = TifChannelUtils.getChannels(getActivity().getContentResolver());
        if(mChannelEntities== null){
            mChannelEntities = new ArrayList<>();
        }
        updateChannelData();

    }

    private synchronized void insertInput(TvInputInfo tvInputInfo){
        if(tvInputInfo!=null
                &&tvInputInfo.getId()!=null){
            mTvInputInfoHashMap.put(tvInputInfo.getId(),tvInputInfo);
        }
    }
    private synchronized void insertInputList(List<TvInputInfo> tvInputInfos){
        if(tvInputInfos!=null&&tvInputInfos.size()>0){
            for(TvInputInfo tvInputInfo:tvInputInfos){
                insertInput(tvInputInfo);
            }
        }

    }

    private synchronized List<TvInputInfo> getInputList(){

        List<TvInputInfo> tvInputInfoList = new ArrayList<>();
        for(String key:mTvInputInfoHashMap.keySet()){
            TvInputInfo tvInputInfo = mTvInputInfoHashMap.get(key);
            if(tvInputInfo!=null){
                tvInputInfoList.add(tvInputInfo);
            }
        }
        return tvInputInfoList;

    }

    private synchronized void removeInput(TvInputInfo tvInputInfo){
        if(tvInputInfo!=null
                &&tvInputInfo.getId()!=null){
            mTvInputInfoHashMap.remove(tvInputInfo.getId());
        }
    }

    @Override
    public void notifyInputClicked(TvInputInfo tvInputInfo) {
        Intent intent = CommonUtils.createSetupIntent(tvInputInfo);
        if(intent!=null){
            startActivity(intent);
        }
    }

    @Override
    public void notifyChannelClicked(TifChannelEntity tifChannelEntity) {

        ((MainActivity)getActivity()).playChannel(tifChannelEntity);


    }

    private TvInputManager.TvInputCallback mTvInputCallback = new TvInputManager.TvInputCallback() {
        @Override
        public void onInputStateChanged(String inputId, int state) {
            super.onInputStateChanged(inputId, state);
            generateInputList();
        }

        @Override
        public void onInputAdded(String inputId) {
            super.onInputAdded(inputId);
            generateInputList();
        }

        @Override
        public void onInputRemoved(String inputId) {
            super.onInputRemoved(inputId);
            generateInputList();
        }

        @Override
        public void onInputUpdated(String inputId) {
            super.onInputUpdated(inputId);
            generateInputList();
        }

        @Override
        public void onTvInputInfoUpdated(TvInputInfo inputInfo) {
            super.onTvInputInfoUpdated(inputInfo);
            generateInputList();
        }
    };
}
