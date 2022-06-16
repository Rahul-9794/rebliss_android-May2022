package com.rebliss.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CheckCPOrderModel {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("message")
    @Expose
    private String message;
    private final static long serialVersionUID = -1665194789442759011L;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public class Data implements Serializable
    {

        @SerializedName("order_id")
        @Expose
        private Integer orderId;
        @SerializedName("invoice_id")
        @Expose
        private String invoiceId;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("order_title")
        @Expose
        private String orderTitle;
        @SerializedName("tax")
        @Expose
        private String tax;
        @SerializedName("discount")
        @Expose
        private Object discount;
        @SerializedName("total")
        @Expose
        private String total;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("invoice_url")
        @Expose
        private Object invoiceUrl;
        @SerializedName("reference_number")
        @Expose
        private String referenceNumber;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        private final static long serialVersionUID = -7445766467719400763L;

        public Integer getOrderId() {
            return orderId;
        }

        public void setOrderId(Integer orderId) {
            this.orderId = orderId;
        }

        public String getInvoiceId() {
            return invoiceId;
        }

        public void setInvoiceId(String invoiceId) {
            this.invoiceId = invoiceId;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getOrderTitle() {
            return orderTitle;
        }

        public void setOrderTitle(String orderTitle) {
            this.orderTitle = orderTitle;
        }

        public String getTax() {
            return tax;
        }

        public void setTax(String tax) {
            this.tax = tax;
        }

        public Object getDiscount() {
            return discount;
        }

        public void setDiscount(Object discount) {
            this.discount = discount;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Object getInvoiceUrl() {
            return invoiceUrl;
        }

        public void setInvoiceUrl(Object invoiceUrl) {
            this.invoiceUrl = invoiceUrl;
        }

        public String getReferenceNumber() {
            return referenceNumber;
        }

        public void setReferenceNumber(String referenceNumber) {
            this.referenceNumber = referenceNumber;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

    }

}
