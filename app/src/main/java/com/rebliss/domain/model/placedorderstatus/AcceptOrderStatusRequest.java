package com.rebliss.domain.model.placedorderstatus;

import com.google.gson.annotations.SerializedName;

public class AcceptOrderStatusRequest {

    @SerializedName("activity_id")
    private int activity_id;

    public int getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(int activity_id) {
        this.activity_id = activity_id;
    }

    public int getActivity_status() {
        return activity_status;
    }

    public void setActivity_status(int activity_status) {
        this.activity_status = activity_status;
    }

    @SerializedName("activity_status")
    private int activity_status;
    @SerializedName("order_id")
    private int order_id;


    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }
}
