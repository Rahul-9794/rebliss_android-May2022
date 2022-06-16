package com.rebliss.domain.model.file;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class File {

    public File(java.io.File file, Integer status, String filemane, Integer delstatus,Integer index) {
        this.file = file;
        this.status = status;
        this.filemane = filemane;
        this.delstatus=delstatus;
        this.index=index;
    }

    @SerializedName("file")
    @Expose
    private java.io.File file;

    public Integer getDelstatus() {
        return delstatus;
    }

    public void setDelstatus(Integer delstatus) {
        this.delstatus = delstatus;
    }

    @SerializedName("delstatus")
    @Expose
    private Integer delstatus;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @SerializedName("index")
    @Expose
    private Integer index;

    @SerializedName("status")
    @Expose
    private Integer status;

    public java.io.File getFile() {
        return file;
    }

    public void setFile(java.io.File file) {
        this.file = file;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getFilemane() {
        return filemane;
    }

    public void setFilemane(String filemane) {
        this.filemane = filemane;
    }

    @SerializedName("filename")
    @Expose
    private String filemane;
}
