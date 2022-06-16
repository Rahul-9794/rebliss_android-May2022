package com.rebliss.domain.model;

public class KycResponse {
    public int status;
    public String desc;
    public Data data;

    public class Data {
        public int all_groups;
    }
}

