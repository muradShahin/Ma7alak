package com.muradit.projectx.Model.others;

import android.os.Parcel;
import android.os.Parcelable;

import com.muradit.projectx.Model.AddItemModel;
import com.muradit.projectx.Model.ContractModel;

import java.io.Serializable;

public class Orders extends ContractModel  {
    private AddItemModel itemInfo;
    private String request_ID;

    public String getRequest_ID() {
        return request_ID;
    }

    public void setRequest_ID(String request_ID) {
        this.request_ID = request_ID;
    }

    public AddItemModel getItemInfo() {
        return itemInfo;
    }

    public void setItemInfo(AddItemModel itemInfo) {
        this.itemInfo = itemInfo;
    }


}
