package com.rebliss.domain.model.bankit;


import com.google.gson.annotations.SerializedName;

public class BankItResponse {

    @SerializedName("action")
    private String action;
    @SerializedName("agentId")
    private String agentId;
    @SerializedName("emailId")
    private String emailId;
    @SerializedName("checksum")
    private String checksum;
    @SerializedName("mdId")
    private String mdId;

    /**
     * No args constructor for use in serialization
     *
     */
    public BankItResponse() {
    }

    /**
     *
     * @param emailId
     * @param action
     * @param agentId
     * @param checksum
     * @param mdId
     */
    public BankItResponse(String action, String agentId, String emailId, String checksum, String mdId) {
        super();
        this.action = action;
        this.agentId = agentId;
        this.emailId = emailId;
        this.checksum = checksum;
        this.mdId = mdId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getMdId() {
        return mdId;
    }

    public void setMdId(String mdId) {
        this.mdId = mdId;
    }

}