package com.rebliss.domain.model.login;



import java.io.Serializable;
import java.util.List;

public class validationError implements Serializable {
    public List<String> getPassword() {
        return password;
    }

    public void setPassword(List<String> password) {
        this.password = password;
    }
    private List<String> password;

}
