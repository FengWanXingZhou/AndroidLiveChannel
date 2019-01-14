package com.android.livechannel.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.livechannel.R;
import com.android.livechannel.bean.TifChannelEntity;
import com.android.livechannel.util.Util;

import java.util.ArrayList;
import java.util.List;

public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.MyViewHolder> {
    private Context mContext;
    private List<TifChannelEntity> mTifChannelEntityList;
    private ChannelClickListener mChannelClickListener;



    public ChannelAdapter(Context context) {
        mContext = context;
    }
    public void updateData(List<TifChannelEntity> tifChannelEntities){
        mTifChannelEntityList = tifChannelEntities;
        if(mTifChannelEntityList == null){
            mTifChannelEntityList = new ArrayList<>();
        }
        Util.LOG("InputListAdapter updateData channel size:"+mTifChannelEntityList.size());
    }

    public void registerChannelClickListener(ChannelClickListener channelClickListener){
        mChannelClickListener = channelClickListener;
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.input_detail_item,viewGroup,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {


        myViewHolder.mTextView.setText(mTifChannelEntityList.get(position).getDisplayName());
        myViewHolder.itemView.setTag(position);
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mChannelClickListener!=null){
                    mChannelClickListener.notifyChannelClicked(mTifChannelEntityList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        return mTifChannelEntityList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.detail);
        }
    }


    public static interface ChannelClickListener{
        void notifyChannelClicked(TifChannelEntity tifChannelEntity);
    }


}
