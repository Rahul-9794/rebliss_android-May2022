package com.rebliss.domain.model.payment;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvoiceListResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Datum {

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
        private Object tax;
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
        @SerializedName("created_at")
        @Expose
        private String createdAt;

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

        public Object getTax() {
            return tax;
        }

        public void setTax(Object tax) {
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

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

    }

}
