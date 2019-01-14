/*
 * Copyright 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.livechannel.util;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.media.tv.TvContentRating;
import android.media.tv.TvContract;
import android.media.tv.TvContract.Channels;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.SparseArray;


import com.android.livechannel.BuildConfig;
import com.android.livechannel.bean.TifChannelEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Static helper methods for working with {@link TvContract}.
 */
public class TifChannelUtils {

    /** Indicates that no source type has been defined for this video yet */
    public static final int SOURCE_TYPE_INVALID = -1;
    /** Indicates that the video will use MPEG-DASH (Dynamic Adaptive Streaming over HTTP) for
     * playback.
     */
    public static final int SOURCE_TYPE_MPEG_DASH = 0;
    /** Indicates that the video will use SS (Smooth Streaming) for playback. */
    public static final int SOURCE_TYPE_SS = 1;
    /** Indicates that the video will use HLS (HTTP Live Streaming) for playback. */
    public static final int SOURCE_TYPE_HLS = 2;
    /** Indicates that the video will use HTTP Progressive for playback. */
    public static final int SOURCE_TYPE_HTTP_PROGRESSIVE = 3;

    private static final String TAG = "TvContractUtils";
    private static final boolean DEBUG = BuildConfig.DEBUG;
    private static final SparseArray<String> VIDEO_HEIGHT_TO_FORMAT_MAP = new SparseArray<>();

    static {
        VIDEO_HEIGHT_TO_FORMAT_MAP.put(480, Channels.VIDEO_FORMAT_480P);
        VIDEO_HEIGHT_TO_FORMAT_MAP.put(576, Channels.VIDEO_FORMAT_576P);
        VIDEO_HEIGHT_TO_FORMAT_MAP.put(720, Channels.VIDEO_FORMAT_720P);
        VIDEO_HEIGHT_TO_FORMAT_MAP.put(1080, Channels.VIDEO_FORMAT_1080P);
        VIDEO_HEIGHT_TO_FORMAT_MAP.put(2160, Channels.VIDEO_FORMAT_2160P);
        VIDEO_HEIGHT_TO_FORMAT_MAP.put(4320, Channels.VIDEO_FORMAT_4320P);
    }

    public static void deleteAllChannels(Context context, String inputId){




            Uri channelsUri = TvContract.buildChannelsUriForInput(inputId);
            ContentResolver resolver = context.getContentResolver();
            resolver.delete(channelsUri,null,null);
            if (DEBUG) {
                Log.e(TAG, "deleteAllChannels");
            }

    }

    public static void deleteChannels(Context context, String inputId, List<TifChannelEntity> channels){

        Util.LOG("deleteChannels ");

        if(channels!=null&&channels.size()>0){

            ArrayList<ContentProviderOperation> ops = new ArrayList<>();
            Uri channelsUri = TvContract.buildChannelsUriForInput(inputId);
            String selection = Channels._ID +" = ?";

            for(int i = 0;i<channels.size();i++){
                Util.LOG("deleteChannels id="+channels.get(i).getId());
                String[] selectionArgs = new String[]{String.valueOf(channels.get(i).getId())};
                ops.add(ContentProviderOperation.newDelete(Channels.CONTENT_URI)
                        .withSelection(selection,selectionArgs)
                        .build());
            }

            ContentResolver resolver = context.getContentResolver();
            try {
                resolver.applyBatch(TvContract.AUTHORITY, ops);
            }catch (Exception e){
                e.printStackTrace();
            }

        }


    }

    /**
     * Updates the list of available channels.
     *
     * @param context The application's context.
     * @param inputId The ID of the TV input service that provides this TV channel.
     * @param channels The updated list of channels.
     * @hide
     */
    public static void updateChannels(Context context, String inputId, List<TifChannelEntity> channels) {
        // Create a map from original network ID to channel row ID for existing channels.

        List<TifChannelEntity> tifChannelCacheList = new ArrayList<>();

        Uri channelsUri = TvContract.buildChannelsUriForInput(inputId);
        String[] projection = {Channels._ID, Channels.COLUMN_ORIGINAL_NETWORK_ID
                                ,Channels.COLUMN_SERVICE_ID
                                ,Channels.COLUMN_TRANSPORT_STREAM_ID};
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = null;
        try {
            cursor = resolver.query(channelsUri, projection, null, null, null);

            while (cursor != null && cursor.moveToNext()) {
                long rowId = cursor.getLong(0);
                int originalNetworkId = cursor.getInt(1);
                //channelMap.put(originalNetworkId, rowId);
                if (DEBUG) {
                    Log.d(TAG, "Putting  channel originalNetworkId " + originalNetworkId + " rowId " + rowId);
                }
                int serviceId = cursor.getInt(2);
                int tsId = cursor.getInt(3);
                TifChannelEntity tifChannelCache = new TifChannelEntity();
                tifChannelCache.setId(rowId);
                tifChannelCache.setServiceId(serviceId);
                tifChannelCache.setTransportStreamId(tsId);
                tifChannelCache.setOriginalNetworkId(originalNetworkId);
                tifChannelCacheList.add(tifChannelCache);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        // If a channel exists, update it. If not, insert a new one.
        Map<Uri, String> logos = new HashMap<>();
        for (TifChannelEntity channel : channels) {
            ContentValues values = new ContentValues();
            values.put(Channels.COLUMN_INPUT_ID, inputId);
            values.putAll(channel.toContentValues());
            // If some required fields are not populated, the app may crash, so defaults are used
            if (channel.getPackageName() == null) {
                // If channel does not include package name, it will be added
                values.put(Channels.COLUMN_PACKAGE_NAME, context.getPackageName());
            }
            if (channel.getInputId() == null) {
                // If channel does not include input id, it will be added
                values.put(Channels.COLUMN_INPUT_ID, inputId);
            }
            if (channel.getType() == null) {
                // If channel does not include type it will be added
                values.put(Channels.COLUMN_TYPE, Channels.TYPE_OTHER);
            }
            if (DEBUG) {
                Log.d(TAG, "channel originalNetworkId" + channel.getOriginalNetworkId());
            }
            boolean channelExist  = false;
            TifChannelEntity tifChannel = null;
            if(tifChannelCacheList.size()>0){

                for(TifChannelEntity tifChannelCache:tifChannelCacheList){
                    if(tifChannelCache.getServiceId() == channel.getServiceId()
                            && tifChannelCache.getTransportStreamId() == channel.getTransportStreamId()
                            && tifChannelCache.getOriginalNetworkId() == channel.getOriginalNetworkId()){
                        channelExist = true;
                        tifChannel = tifChannelCache;
                        break;
                    }
                }
                tifChannelCacheList.remove(tifChannel);
            }


            //Long rowId = channelMap.get(channel.getOriginalNetworkId());

            Uri uri;
            if (!channelExist) {
                uri = resolver.insert(Channels.CONTENT_URI, values);
                if (DEBUG) {
                    Log.d(TAG, "Adding channel " + channel.getDisplayName() + " at " + uri);
                }
            }
            else {
                Long rowId = tifChannel.getId();
                values.put(Channels._ID, rowId);
                uri = TvContract.buildChannelUri(rowId);
                if (DEBUG) {
                    Log.d(TAG, "Updating channel " + channel.getDisplayName() + " at " + uri);
                }
                resolver.update(uri, values, null, null);

            }
            if (channel.getChannelLogo() != null && !TextUtils.isEmpty(channel.getChannelLogo())) {
                logos.put(TvContract.buildChannelLogoUri(uri), channel.getChannelLogo());
            }
        }
        if (!logos.isEmpty()) {
            new InsertLogosTask(context).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, logos);
        }

        // Deletes channels which don't exist in the new feed.
        //todo delete
        /*int size = channelMap.size();
        for (int i = 0; i < size; ++i) {
            Long rowId = channelMap.valueAt(i);
            if (DEBUG) {
                Log.d(TAG, "Deleting channel " + rowId);
            }
            resolver.delete(TvContract.buildChannelUri(rowId), null, null);
            SharedPreferences.Editor editor = context.getSharedPreferences(
                    BaseTvInputService.PREFERENCES_FILE_KEY, Context.MODE_PRIVATE).edit();
            editor.remove(BaseTvInputService.SHARED_PREFERENCES_KEY_LAST_CHANNEL_AD_PLAY + rowId);
            editor.apply();
        }*/
    }

    /**
     * Builds a map of available channels.
     *
     * @param resolver Application's ContentResolver.
     * @param inputId The ID of the TV input service that provides this TV channel.
     * @return LongSparseArray mapping each channel's {@link Channels#_ID} to the
     * Channel object.
     * @hide
     */
    public static LongSparseArray<TifChannelEntity> buildChannelMap(@NonNull ContentResolver resolver,
                                                                    @NonNull String inputId) {
        Uri uri = TvContract.buildChannelsUriForInput(inputId);
        LongSparseArray<TifChannelEntity> channelMap = new LongSparseArray<>();
        Cursor cursor = null;
        try {
            cursor = resolver.query(uri, TifChannelEntity.PROJECTION, null, null, null);
            if (cursor == null || cursor.getCount() == 0) {
                if (DEBUG) {
                    Log.d(TAG, "Cursor is null or found no results");
                }
                return null;
            }

            while (cursor.moveToNext()) {
                TifChannelEntity nextChannel = TifChannelEntity.fromCursor(cursor);
                channelMap.put(nextChannel.getId(), nextChannel);
            }
        } catch (Exception e) {

            Log.d(TAG, "Content provider query: " + Arrays.toString(e.getStackTrace()));
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return channelMap;
    }

    public static List<TifChannelEntity> getChannelsByInputId(Context context,String inputId) {
        List<TifChannelEntity> channels = new ArrayList<>();
        // TvProvider returns programs in chronological order by default.
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = null;
        String selection = Channels.COLUMN_INPUT_ID+"=?";
        String[] selectionArgs = new String[]{inputId};
        try {
            cursor = resolver.query(Channels.CONTENT_URI, TifChannelEntity.PROJECTION, selection, selectionArgs, null);
            if (cursor == null || cursor.getCount() == 0) {
                return channels;
            }
            while (cursor.moveToNext()) {
                channels.add(TifChannelEntity.fromCursor(cursor));
            }
        } catch (Exception e) {
            Log.w(TAG, "Unable to get channels", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return channels;
    }

    /**
     * Returns the current list of channels your app provides.
     *
     * @param resolver Application's ContentResolver.
     * @return List of channels.
     */
    public static List<TifChannelEntity> getChannels(ContentResolver resolver) {
        List<TifChannelEntity> channels = new ArrayList<>();
        // TvProvider returns programs in chronological order by default.
        Cursor cursor = null;
        try {
            cursor = resolver.query(Channels.CONTENT_URI, TifChannelEntity.PROJECTION, null, null, null);
            if (cursor == null || cursor.getCount() == 0) {
                return channels;
            }
            while (cursor.moveToNext()) {
                channels.add(TifChannelEntity.fromCursor(cursor));
            }
        } catch (Exception e) {
            Log.w(TAG, "Unable to get channels", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return channels;
    }

    /**
     * Returns the {@link TifChannelEntity} with specified channel URI.
     * @param resolver {@link ContentResolver} used to query database.
     * @param channelUri URI of channel.
     * @return An channel object with specified channel URI.
     * @hide
     */
    public static TifChannelEntity getChannel(ContentResolver resolver, Uri channelUri) {
        Cursor cursor = null;
        try {
            cursor = resolver.query(channelUri, TifChannelEntity.PROJECTION, null, null, null);
            if (cursor == null || cursor.getCount() == 0) {
                Log.w(TAG, "No channel matches " + channelUri);
                return null;
            }
            cursor.moveToNext();
            return TifChannelEntity.fromCursor(cursor);
        } catch (Exception e) {
            Log.w(TAG, "Unable to get the channel with URI " + channelUri, e);
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();

            }
        }
    }

    public static TifChannelEntity getChannelById(Context  context,String inputId,int channelId) {

        Uri channelsUri = TvContract.buildChannelsUriForInput(inputId);
        String selection = Channels._ID +"=?";
        String[] selectionArgs = {String.valueOf(channelId)};
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = null;
        try {
            cursor = resolver.query(channelsUri, TifChannelEntity.PROJECTION, selection, selectionArgs, null);
            if (cursor == null || cursor.getCount() == 0) {
                Log.w(TAG, "No channel matches " + channelsUri);
                return null;
            }
            cursor.moveToNext();
            return TifChannelEntity.fromCursor(cursor);
        } catch (Exception e) {
            Log.w(TAG, "Unable to get the channel with URI " + channelsUri, e);
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();

            }
        }
    }

    public static TifChannelEntity getChannelByThreeId(Context  context,String inputId,int serviceId,int networkId,int tsId) {

        Uri channelsUri = TvContract.buildChannelsUriForInput(inputId);
        String selection = Channels.COLUMN_SERVICE_ID+"=?"
                    +" AND "+Channels.COLUMN_ORIGINAL_NETWORK_ID+"=?"
                    +" AND "+Channels.COLUMN_TRANSPORT_STREAM_ID+"=?";
        String[] selectionArgs = {String.valueOf(serviceId),String.valueOf(networkId),String.valueOf(tsId)};
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = null;
        try {
            cursor = resolver.query(channelsUri, TifChannelEntity.PROJECTION, selection, selectionArgs, null);
            if (cursor == null || cursor.getCount() == 0) {
                Log.w(TAG, "No channel matches " + channelsUri);
                return null;
            }
            cursor.moveToNext();
            return TifChannelEntity.fromCursor(cursor);
        } catch (Exception e) {
            Log.w(TAG, "Unable to get the channel with URI " + channelsUri, e);
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();

            }
        }
    }


    /*public static List<TifChannelEntity> getChannelByTs(Context  context, String inputId, TifChannelInternalDataEntity internalDataEntity) {

        Uri channelsUri = TvContract.buildChannelsUriForInput(inputId);
        *//*String selection = Channels.COLUMN_SERVICE_ID+"=?"
                +" AND "+Channels.COLUMN_ORIGINAL_NETWORK_ID+"=?"
                +" AND "+Channels.COLUMN_TRANSPORT_STREAM_ID+"=?";
        String[] selectionArgs = {String.valueOf(serviceId),String.valueOf(networkId),String.valueOf(tsId)};*//*
        ContentResolver resolver = context.getContentResolver();
        List<TifChannelEntity> tifChannelEntities = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = resolver.query(channelsUri, TifChannelEntity.PROJECTION, null, null, null);
            if (cursor == null || cursor.getCount() == 0) {
                Log.w(TAG, "No channel matches " + channelsUri);
                return tifChannelEntities;
            }
            Gson gson = new Gson();
            final Type userEntityType = new TypeToken<TifChannelInternalDataEntity>() {}.getType();
            while(cursor.moveToNext()){
                TifChannelEntity tifChannelEntity = TifChannelEntity.fromCursor(cursor);
                if(tifChannelEntity!=null) {
                    TifChannelInternalDataEntity tifChannelInternalDataEntity =
                            gson.fromJson(tifChannelEntity.getInternalProviderData(), userEntityType);
                    if(tifChannelInternalDataEntity!=null
                            &&tifChannelInternalDataEntity.getFrequencyKhz()
                            == internalDataEntity.getFrequencyKhz()
                            &&tifChannelInternalDataEntity.getSymbolRateKbps()==internalDataEntity.getSymbolRateKbps()
                            &&tifChannelInternalDataEntity.getModulation() == internalDataEntity.getModulation()
                            &&tifChannelInternalDataEntity.getSatelliteId() == internalDataEntity.getSatelliteId()){

                        tifChannelEntities.add(tifChannelEntity);

                    }
                }

            }



            return tifChannelEntities;
        } catch (Exception e) {
            Log.w(TAG, "Unable to get the channel with URI " + channelsUri, e);
            return tifChannelEntities;
        } finally {
            if (cursor != null) {
                cursor.close();

            }
        }
    }*/


    private static void insertUrl(Context context, Uri contentUri, URL sourceUrl) {
        if (DEBUG) {
            Log.d(TAG, "Inserting " + sourceUrl + " to " + contentUri);
        }
        InputStream is = null;
        OutputStream os = null;
        try {
            is = sourceUrl.openStream();
            os = context.getContentResolver().openOutputStream(contentUri);
            copy(is, os);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to write " + sourceUrl + "  to " + contentUri, ioe);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // Ignore exception.
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    // Ignore exception.
                }
            }
        }
    }

    private static void copy(InputStream is, OutputStream os) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
    }

    /**
     * Parses a string of comma-separated ratings into an array of {@link TvContentRating}.
     *
     * @param commaSeparatedRatings String containing various ratings, separated by commas.
     * @return An array of TvContentRatings.
     * @hide
     */
    public static TvContentRating[] stringToContentRatings(String commaSeparatedRatings) {
        if (TextUtils.isEmpty(commaSeparatedRatings)) {
            return null;
        }
        String[] ratings = commaSeparatedRatings.split("\\s*,\\s*");
        TvContentRating[] contentRatings = new TvContentRating[ratings.length];
        for (int i = 0; i < contentRatings.length; ++i) {
            contentRatings[i] = TvContentRating.unflattenFromString(ratings[i]);
        }
        return contentRatings;
    }

    /**
     * Flattens an array of {@link TvContentRating} into a String to be inserted into a database.
     *
     * @param contentRatings An array of TvContentRatings.
     * @return A comma-separated String of ratings.
     * @hide
     */
    public static String contentRatingsToString(TvContentRating[] contentRatings) {
        if (contentRatings == null || contentRatings.length == 0) {
            return null;
        }
        final String DELIMITER = ",";
        StringBuilder ratings = new StringBuilder(contentRatings[0].flattenToString());
        for (int i = 1; i < contentRatings.length; ++i) {
            ratings.append(DELIMITER);
            ratings.append(contentRatings[i].flattenToString());
        }
        return ratings.toString();
    }

    private TifChannelUtils() {
    }

    private static class InsertLogosTask extends AsyncTask<Map<Uri, String>, Void, Void> {
        private final Context mContext;

        InsertLogosTask(Context context) {
            mContext = context;
        }

        @Override
        public Void doInBackground(Map<Uri, String>... logosList) {
            for (Map<Uri, String> logos : logosList) {
                for (Uri uri : logos.keySet()) {
                    try {
                        insertUrl(mContext, uri, new URL(logos.get(uri)));
                    } catch (MalformedURLException e) {
                        Log.e(TAG, "Can't load " + logos.get(uri), e);
                    }
                }
            }
            return null;
        }
    }
}
