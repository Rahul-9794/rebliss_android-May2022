package com.rebliss.domain.model.signup;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public abstract class CityStateResponse {
    @SerializedName("PostOffice")
    public List<PostOffice> PostOffice;
    @SerializedName("Status")
    public String Status;
    @SerializedName("Message")
    public String Message;

    public static class PostOffice {
        @SerializedName("Country")
        public String Country;
        @SerializedName("State")
        public String State;
        @SerializedName("Region")
        public String Region;
        @SerializedName("Division")
        public String Division;
        @SerializedName("District")
        public String District;
        @SerializedName("Circle")
        public String Circle;
        @SerializedName("Taluk")
        public String Taluk;
        @SerializedName("DeliveryStatus")
        public String DeliveryStatus;
        @SerializedName("BranchType")
        public String BranchType;
        @SerializedName("Description")
        public String Description;
        @SerializedName("Name")
        public String Name;
    }
}
