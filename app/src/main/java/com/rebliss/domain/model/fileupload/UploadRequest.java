package com.rebliss.domain.model.fileupload;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UploadRequest implements Serializable {
    @SerializedName("id_proof")
    private String id_proof;

    public String getId_proof() {
        return id_proof;
    }

    public void setId_proof(String id_proof) {
        this.id_proof = id_proof;
    }

    public String getId_proof_file_type() {
        return id_proof_file_type;
    }

    public void setId_proof_file_type(String id_proof_file_type) {
        this.id_proof_file_type = id_proof_file_type;
    }

    @SerializedName("id_proof_file_type")
    private String id_proof_file_type;

}
