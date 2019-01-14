/*
 * Copyright 2015 Google Inc. All rights reserved.
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

package com.android.livechannel.bean;

import android.content.ContentValues;
import android.database.Cursor;
import android.media.tv.TvContract;
import android.text.TextUtils;




/**
 * A convenience class to create and insert channel entries into the database.
 */
public final class TifChannelEntity {
    /**
     * @hide
     */
    public static final String[] PROJECTION = getProjection();

    private static final long INVALID_CHANNEL_ID = -1;
    private static final int INVALID_INTEGER_VALUE = -1;
    private static final int IS_SEARCHABLE = 1;
    private String mPackageName;
    private long mId;

    private String mInputId;
    private String mType;
    private String mServiceType;
    private int mOriginalNetworkId;
    private int mTransportStreamId;
    private int mServiceId;
    private String mDisplayNumber;
    private String mDisplayName;
    private String mNetworkAffiliation;
    private String mDescription;
    private String mVideoFormat;

    //browsable
    private int mBrowsable;
    private int mSearchable;
    private int mLocked;
    //lock




    private String mAppLinkText;
    private int mAppLinkColor;
    private String mAppLinkIconUri;
    private String mAppLinkPosterArtUri;
    private String mAppLinkIntentUri;





    //private byte[] mInternalProviderData;

    //private TifChannelInternalDataEntity mTifChannelInternalData;
    private String mInternalProviderId;
    private String mInternalProviderData;
    private String mInternalProviderFlag1;
    private String mInternalProviderFlag2;
    private String mInternalProviderFlag3;
    private String mInternalProviderFlag4;
    private int mVersionNumber;
    private int mTransient;

    private String mChannelLogo;


    public TifChannelEntity() {
        mId = INVALID_CHANNEL_ID;
        mOriginalNetworkId = INVALID_INTEGER_VALUE;
        mServiceType = TvContract.Channels.SERVICE_TYPE_AUDIO_VIDEO;
    }



    public int getSearchable() {
        return mSearchable;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public void setPackageName(String mPackageName) {
        this.mPackageName = mPackageName;
    }

    public void setInputId(String mInputId) {
        this.mInputId = mInputId;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public void setDisplayNumber(String mDisplayNumber) {
        this.mDisplayNumber = mDisplayNumber;
    }

    public void setDisplayName(String mDisplayName) {
        this.mDisplayName = mDisplayName;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public void setChannelLogo(String mChannelLogo) {
        this.mChannelLogo = mChannelLogo;
    }

    public void setVideoFormat(String mVideoFormat) {
        this.mVideoFormat = mVideoFormat;
    }

    public void setOriginalNetworkId(int mOriginalNetworkId) {
        this.mOriginalNetworkId = mOriginalNetworkId;
    }

    public void setTransportStreamId(int mTransportStreamId) {
        this.mTransportStreamId = mTransportStreamId;
    }

    public void setServiceId(int mServiceId) {
        this.mServiceId = mServiceId;
    }

    public void setAppLinkText(String mAppLinkText) {
        this.mAppLinkText = mAppLinkText;
    }

    public void setAppLinkColor(int mAppLinkColor) {
        this.mAppLinkColor = mAppLinkColor;
    }

    public void setAppLinkIconUri(String mAppLinkIconUri) {
        this.mAppLinkIconUri = mAppLinkIconUri;
    }

    public void setAppLinkPosterArtUri(String mAppLinkPosterArtUri) {
        this.mAppLinkPosterArtUri = mAppLinkPosterArtUri;
    }

    public void setAppLinkIntentUri(String mAppLinkIntentUri) {
        this.mAppLinkIntentUri = mAppLinkIntentUri;
    }



    public void setNetworkAffiliation(String mNetworkAffiliation) {
        this.mNetworkAffiliation = mNetworkAffiliation;
    }

    public void setSearchable(int mSearchable) {
        this.mSearchable = mSearchable;
    }

    public void setServiceType(String mServiceType) {
        this.mServiceType = mServiceType;
    }

    /**
     * @return The value of {@link TvContract.Channels#_ID} for the channel.
     */
    public long getId() {
        return mId;
    }

    /**
     * @return The value of {@link TvContract.Channels#COLUMN_PACKAGE_NAME} for the channel.
     */
    public String getPackageName() {
        return mPackageName;
    }

    /**
     * @return The value of {@link TvContract.Channels#COLUMN_INPUT_ID} for the channel.
     */
    public String getInputId() {
        return mInputId;
    }

    /**
     * @return The value of {@link TvContract.Channels#COLUMN_TYPE} for the channel.
     */
    public String getType() {
        return mType;
    }

    /**
     * @return The value of {@link TvContract.Channels#COLUMN_DISPLAY_NUMBER} for the channel.
     */
    public String getDisplayNumber() {
        return mDisplayNumber;
    }

    /**
     * @return The value of {@link TvContract.Channels#COLUMN_DISPLAY_NAME} for the channel.
     */
    public String getDisplayName() {
        return mDisplayName;
    }

    /**
     * @return The value of {@link TvContract.Channels#COLUMN_DESCRIPTION} for the channel.
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * @return The value of {@link TvContract.Channels#COLUMN_VIDEO_FORMAT} for the channel.
     */
    public String getVideoFormat() {
        return mVideoFormat;
    }

    /**
     * @return The value of {@link TvContract.Channels#COLUMN_ORIGINAL_NETWORK_ID} for the channel.
     */
    public int getOriginalNetworkId() {
        return mOriginalNetworkId;
    }

    /**
     * @return The value of {@link TvContract.Channels#COLUMN_TRANSPORT_STREAM_ID} for the channel.
     */
    public int getTransportStreamId() {
        return mTransportStreamId;
    }

    /**
     * @return The value of {@link TvContract.Channels#COLUMN_SERVICE_ID} for the channel.
     */
    public int getServiceId() {
        return mServiceId;
    }

    /**
     * @return The value of {@link TvContract.Channels#COLUMN_APP_LINK_TEXT} for the channel.
     */
    public String getAppLinkText() {
        return mAppLinkText;
    }

    /**
     * @return The value of {@link TvContract.Channels#COLUMN_APP_LINK_COLOR} for the channel.
     */
    public int getAppLinkColor() {
        return mAppLinkColor;
    }

    /**
     * @return The value of {@link TvContract.Channels#COLUMN_APP_LINK_ICON_URI} for the channel.
     */
    public String getAppLinkIconUri() {
        return mAppLinkIconUri;
    }

    /**
     * @return The value of {@link TvContract.Channels#COLUMN_APP_LINK_POSTER_ART_URI} for the
     * channel.
     */
    public String getAppLinkPosterArtUri() {
        return mAppLinkPosterArtUri;
    }

    /**
     * @return The value of {@link TvContract.Channels#COLUMN_APP_LINK_INTENT_URI} for the channel.
     */
    public String getAppLinkIntentUri() {
        return mAppLinkIntentUri;
    }

    /**
     * @return The value of {@link TvContract.Channels.Logo} for the channel.
     */
    public String getChannelLogo() {
        return mChannelLogo;
    }

    /**
     * @return The value of {@link TvContract.Channels#COLUMN_NETWORK_AFFILIATION} for the channel.
     */
    public String getNetworkAffiliation() {
        return mNetworkAffiliation;
    }

    /**
     * @return The value of {@link TvContract.Channels#COLUMN_SEARCHABLE} for the channel.
     */
    public boolean isSearchable() {
        return mSearchable == IS_SEARCHABLE;
    }

    public int getBrowsable() {
        return mBrowsable;
    }

    public void setBrowsable(int browsable) {
        mBrowsable = browsable;
    }

    public int getLocked() {
        return mLocked;
    }

    public void setLocked(int locked) {
        mLocked = locked;
    }

    public int getVersionNumber() {
        return mVersionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        mVersionNumber = versionNumber;
    }

    public int getTransient() {
        return mTransient;
    }

    public void setTransient(int aTransient) {
        mTransient = aTransient;
    }

    public String getInternalProviderId() {
        return mInternalProviderId;
    }

    public void setInternalProviderId(String internalProviderId) {
        mInternalProviderId = internalProviderId;
    }

    public String getInternalProviderData() {
        return mInternalProviderData;
    }

    public void setInternalProviderData(String internalProviderData) {
        mInternalProviderData = internalProviderData;
    }

    public String getInternalProviderFlag1() {
        return mInternalProviderFlag1;
    }

    public void setInternalProviderFlag1(String internalProviderFlag1) {
        mInternalProviderFlag1 = internalProviderFlag1;
    }

    public String getInternalProviderFlag2() {
        return mInternalProviderFlag2;
    }

    public void setInternalProviderFlag2(String internalProviderFlag2) {
        mInternalProviderFlag2 = internalProviderFlag2;
    }

    public String getInternalProviderFlag3() {
        return mInternalProviderFlag3;
    }

    public void setInternalProviderFlag3(String internalProviderFlag3) {
        mInternalProviderFlag3 = internalProviderFlag3;
    }

    public String getInternalProviderFlag4() {
        return mInternalProviderFlag4;
    }

    public void setInternalProviderFlag4(String internalProviderFlag4) {
        mInternalProviderFlag4 = internalProviderFlag4;
    }

    /**
     * @return The value of {@link TvContract.Channels#COLUMN_INTERNAL_PROVIDER_DATA} for the
     * channel.
     */


    /**
     * @return The value of {@link TvContract.Channels#COLUMN_SERVICE_TYPE} for the channel. Returns
     * {@link TvContract.Channels#SERVICE_TYPE_AUDIO},
     * {@link TvContract.Channels#SERVICE_TYPE_AUDIO_VIDEO}, or
     * {@link TvContract.Channels#SERVICE_TYPE_OTHER}.
     */
    public String getServiceType() {
        return mServiceType;
    }

    @Override
    public String toString() {
        return "Channel{"
                + "id=" + mId
                + ", packageName=" + mPackageName
                + ", inputId=" + mInputId
                + ", originalNetworkId=" + mOriginalNetworkId
                + ", type=" + mType
                + ", displayNumber=" + mDisplayNumber
                + ", displayName=" + mDisplayName
                + ", description=" + mDescription
                + ", channelLogo=" + mChannelLogo
                + ", videoFormat=" + mVideoFormat
                + ", appLinkText=" + mAppLinkText + "}";
    }

    /**
     * @return The fields of the Channel in the ContentValues format to be easily inserted into the
     * TV Input Framework database.
     * @hide
     */
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        if (!TextUtils.isEmpty(mPackageName)) {
            values.put(TvContract.Channels.COLUMN_PACKAGE_NAME, mPackageName);
        } else {
            values.putNull(TvContract.Channels.COLUMN_PACKAGE_NAME);
        }
        if (mId != INVALID_CHANNEL_ID) {
            values.put(TvContract.Channels._ID, mId);
        }

        if (!TextUtils.isEmpty(mInputId)) {
            values.put(TvContract.Channels.COLUMN_INPUT_ID, mInputId);
        } else {
            values.putNull(TvContract.Channels.COLUMN_INPUT_ID);
        }
        if (!TextUtils.isEmpty(mType)) {
            values.put(TvContract.Channels.COLUMN_TYPE, mType);
        } else {
            values.putNull(TvContract.Channels.COLUMN_TYPE);
        }
        if (!TextUtils.isEmpty(mServiceType)) {
            values.put(TvContract.Channels.COLUMN_SERVICE_TYPE, mServiceType);
        } else {
            values.putNull(TvContract.Channels.COLUMN_SERVICE_TYPE);
        }
        values.put(TvContract.Channels.COLUMN_ORIGINAL_NETWORK_ID, mOriginalNetworkId);
        values.put(TvContract.Channels.COLUMN_TRANSPORT_STREAM_ID, mTransportStreamId);
        values.put(TvContract.Channels.COLUMN_SERVICE_ID, mServiceId);

        if (!TextUtils.isEmpty(mDisplayNumber)) {
            values.put(TvContract.Channels.COLUMN_DISPLAY_NUMBER, mDisplayNumber);
        } else {
            values.putNull(TvContract.Channels.COLUMN_DISPLAY_NUMBER);
        }
        if (!TextUtils.isEmpty(mDisplayName)) {
            values.put(TvContract.Channels.COLUMN_DISPLAY_NAME, mDisplayName);
        } else {
            values.putNull(TvContract.Channels.COLUMN_DISPLAY_NAME);
        }
        values.put(TvContract.Channels.COLUMN_NETWORK_AFFILIATION, mNetworkAffiliation);
        if (!TextUtils.isEmpty(mDescription)) {
            values.put(TvContract.Channels.COLUMN_DESCRIPTION, mDescription);
        } else {
            values.putNull(TvContract.Channels.COLUMN_DESCRIPTION);
        }
        if (!TextUtils.isEmpty(mVideoFormat)) {
            values.put(TvContract.Channels.COLUMN_VIDEO_FORMAT, mVideoFormat);
        } else {
            values.putNull(TvContract.Channels.COLUMN_VIDEO_FORMAT);
        }
        values.put(TvContract.Channels.COLUMN_BROWSABLE, mBrowsable);
        values.put(TvContract.Channels.COLUMN_SEARCHABLE, mSearchable);
        values.put(TvContract.Channels.COLUMN_LOCKED, mLocked);

        if (!TextUtils.isEmpty(mAppLinkIconUri)) {
            values.put(TvContract.Channels.COLUMN_APP_LINK_ICON_URI, mAppLinkIconUri);
        } else {
            values.putNull(TvContract.Channels.COLUMN_APP_LINK_ICON_URI);
        }
        if (!TextUtils.isEmpty(mAppLinkPosterArtUri)) {
            values.put(TvContract.Channels.COLUMN_APP_LINK_POSTER_ART_URI,
                    mAppLinkPosterArtUri);
        } else {
            values.putNull(TvContract.Channels.COLUMN_APP_LINK_POSTER_ART_URI);
        }

        if (!TextUtils.isEmpty(mAppLinkText)) {
            values.put(TvContract.Channels.COLUMN_APP_LINK_TEXT, mAppLinkText);
        } else {
            values.putNull(TvContract.Channels.COLUMN_APP_LINK_TEXT);
        }
        values.put(TvContract.Channels.COLUMN_APP_LINK_COLOR, mAppLinkColor);
        if (!TextUtils.isEmpty(mAppLinkIntentUri)) {
            values.put(TvContract.Channels.COLUMN_APP_LINK_INTENT_URI, mAppLinkIntentUri);
        } else {
            values.putNull(TvContract.Channels.COLUMN_APP_LINK_INTENT_URI);
        }
        values.put(TvContract.Channels.COLUMN_INTERNAL_PROVIDER_ID,mInternalProviderId);

        if(!TextUtils.isEmpty(mInternalProviderData)) {
            values.put(TvContract.Channels.COLUMN_INTERNAL_PROVIDER_DATA, mInternalProviderData);
        }else{
            values.putNull(TvContract.Channels.COLUMN_INTERNAL_PROVIDER_DATA);
        }
        values.put(TvContract.Channels.COLUMN_INTERNAL_PROVIDER_FLAG1,mInternalProviderFlag1);
        values.put(TvContract.Channels.COLUMN_INTERNAL_PROVIDER_FLAG2,mInternalProviderFlag2);
        values.put(TvContract.Channels.COLUMN_INTERNAL_PROVIDER_FLAG3,mInternalProviderFlag3);
        values.put(TvContract.Channels.COLUMN_INTERNAL_PROVIDER_FLAG4,mInternalProviderFlag4);
        values.put(TvContract.Channels.COLUMN_VERSION_NUMBER, mVersionNumber);
        values.put(TvContract.Channels.COLUMN_TRANSIENT, mTransient);

        return values;
    }

    /*private void copyFrom(TifChannelEntity other) {
        if (this == other) {
            return;
        }
        mId = other.mId;
        mPackageName = other.mPackageName;
        mInputId = other.mInputId;
        mType = other.mType;
        mDisplayNumber = other.mDisplayNumber;
        mDisplayName = other.mDisplayName;
        mDescription = other.mDescription;
        mVideoFormat = other.mVideoFormat;
        mOriginalNetworkId = other.mOriginalNetworkId;
        mTransportStreamId = other.mTransportStreamId;
        mServiceId = other.mServiceId;
        mAppLinkText = other.mAppLinkText;
        mAppLinkColor = other.mAppLinkColor;
        mAppLinkIconUri = other.mAppLinkIconUri;
        mAppLinkPosterArtUri = other.mAppLinkPosterArtUri;
        mAppLinkIntentUri = other.mAppLinkIntentUri;
        mChannelLogo = other.mChannelLogo;
        //mInternalProviderData = other.mInternalProviderData;
        mNetworkAffiliation = other.mNetworkAffiliation;
        mSearchable = other.mSearchable;
        mServiceType = other.mServiceType;
    }*/

    /**
     * Creates a Channel object from a cursor including the fields defined in
     * {@link TvContract.Channels}.
     *
     * @param cursor A row from the TV Input Framework database.
     * @return A channel with the values taken from the cursor.
     * @hide
     */
    public static TifChannelEntity fromCursor(Cursor cursor) {

        TifChannelEntity tifChannelEntity = new TifChannelEntity();

        int index = 0;

        tifChannelEntity.setPackageName(cursor.getString(index++));
        tifChannelEntity.setId(cursor.getLong(index++));
        tifChannelEntity.setInputId(cursor.getString(index++));
        tifChannelEntity.setType(cursor.getString(index++));
        tifChannelEntity.setServiceType(cursor.getString(index++));
        tifChannelEntity.setOriginalNetworkId(cursor.getInt(index++));
        tifChannelEntity.setTransportStreamId(cursor.getInt(index++));
        tifChannelEntity.setServiceId(cursor.getInt(index++));
        tifChannelEntity.setDisplayNumber(cursor.getString(index++));
        tifChannelEntity.setDisplayName(cursor.getString(index++));
        tifChannelEntity.setNetworkAffiliation(cursor.getString(index++));
        tifChannelEntity.setDescription(cursor.getString(index++));
        tifChannelEntity.setVideoFormat(cursor.getString(index++));
        tifChannelEntity.setBrowsable(cursor.getInt(index++));
        tifChannelEntity.setSearchable(cursor.getInt(index++));
        tifChannelEntity.setLocked(cursor.getInt(index++));
        tifChannelEntity.setAppLinkIconUri(cursor.getString(index++));
        tifChannelEntity.setAppLinkPosterArtUri(cursor.getString(index++));
        tifChannelEntity.setAppLinkText(cursor.getString(index++));
        tifChannelEntity.setAppLinkColor(cursor.getInt(index++));
        tifChannelEntity.setAppLinkIntentUri(cursor.getString(index++));
        tifChannelEntity.setInternalProviderId(cursor.getString(index++));
        tifChannelEntity.setInternalProviderData(cursor.getString(index++));
        tifChannelEntity.setInternalProviderFlag1(cursor.getString(index++));
        tifChannelEntity.setInternalProviderFlag2(cursor.getString(index++));
        tifChannelEntity.setInternalProviderFlag3(cursor.getString(index++));
        tifChannelEntity.setInternalProviderFlag4(cursor.getString(index++));
        tifChannelEntity.setVersionNumber(cursor.getInt(index++));
        tifChannelEntity.setTransient(cursor.getInt(index++));

        return tifChannelEntity;
    }

    private static String[] getProjection() {
        String[] baseColumns = new String[] {
                TvContract.Channels.COLUMN_PACKAGE_NAME,
                TvContract.Channels._ID,
                TvContract.Channels.COLUMN_INPUT_ID,
                TvContract.Channels.COLUMN_TYPE,
                TvContract.Channels.COLUMN_SERVICE_TYPE,
                TvContract.Channels.COLUMN_ORIGINAL_NETWORK_ID,
                TvContract.Channels.COLUMN_TRANSPORT_STREAM_ID,
                TvContract.Channels.COLUMN_SERVICE_ID,
                TvContract.Channels.COLUMN_DISPLAY_NUMBER,
                TvContract.Channels.COLUMN_DISPLAY_NAME,
                TvContract.Channels.COLUMN_NETWORK_AFFILIATION,
                TvContract.Channels.COLUMN_DESCRIPTION,
                TvContract.Channels.COLUMN_VIDEO_FORMAT,
                TvContract.Channels.COLUMN_BROWSABLE,
                TvContract.Channels.COLUMN_SEARCHABLE,
                TvContract.Channels.COLUMN_LOCKED,
                TvContract.Channels.COLUMN_APP_LINK_ICON_URI,
                TvContract.Channels.COLUMN_APP_LINK_POSTER_ART_URI,
                TvContract.Channels.COLUMN_APP_LINK_TEXT,
                TvContract.Channels.COLUMN_APP_LINK_COLOR,
                TvContract.Channels.COLUMN_APP_LINK_INTENT_URI,
                TvContract.Channels.COLUMN_INTERNAL_PROVIDER_ID,
                TvContract.Channels.COLUMN_INTERNAL_PROVIDER_DATA,
                TvContract.Channels.COLUMN_INTERNAL_PROVIDER_FLAG1,
                TvContract.Channels.COLUMN_INTERNAL_PROVIDER_FLAG2,
                TvContract.Channels.COLUMN_INTERNAL_PROVIDER_FLAG3,
                TvContract.Channels.COLUMN_INTERNAL_PROVIDER_FLAG4,
                TvContract.Channels.COLUMN_VERSION_NUMBER,
                TvContract.Channels.COLUMN_TRANSIENT,

        };

        return baseColumns;
    }


}
