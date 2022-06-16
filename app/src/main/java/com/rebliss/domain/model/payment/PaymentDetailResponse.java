package com.rebliss.domain.model.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentDetailResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private Data data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    public class Data {

        @SerializedName("Onboarding_fee")
        @Expose
        private Integer onboardingFee;
        @SerializedName("SGST")
        @Expose
        private Double sGST;
        @SerializedName("CGST")
        @Expose
        private Double cGST;
        @SerializedName("IGST")
        @Expose
        private Double iGST;
        @SerializedName("total")
        @Expose
        private Integer total;

        public Integer getOnboardingFee() {
            return onboardingFee;
        }

        public void setOnboardingFee(Integer onboardingFee) {
            this.onboardingFee = onboardingFee;
        }

        public Double getSGST() {
            return sGST;
        }

        public void setSGST(Double sGST) {
            this.sGST = sGST;
        }

        public Double getCGST() {
            return cGST;
        }

        public void setCGST(Double cGST) {
            this.cGST = cGST;
        }

        public Double getIGST() {
            return iGST;
        }

        public void setIGST(Double iGST) {
            this.iGST = iGST;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

    }

}