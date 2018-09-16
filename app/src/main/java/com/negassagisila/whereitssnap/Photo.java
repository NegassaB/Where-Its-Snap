package com.negassagisila.whereitssnap;

/**
 * Created by Negassa Berhanu on 9/14/18.
 */

import android.net.Uri;

public class Photo {
    private String mTitle;
    private Uri mStorageLocation;
    private String mTag1;
    private String mTag2;
    private String mTag3;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String Title) {
        this.mTitle = Title;
    }

    public Uri getmStorageLocation() {
        return mStorageLocation;
    }

    public void setStorageLocation(Uri StorageLocation) {
        this.mStorageLocation = StorageLocation;
    }

    public String getTag1() {
        return mTag1;
    }

    public void setTag1(String Tag1) {
        this.mTag1 = Tag1;
    }

    public String getTag2() {
        return mTag2;
    }

    public void setTag2(String Tag2) {
        this.mTag2 = Tag2;
    }

    public String getTag3() {
        return mTag3;
    }

    public void setTag3(String Tag3) {
        this.mTag3 = Tag3;
    }
}
