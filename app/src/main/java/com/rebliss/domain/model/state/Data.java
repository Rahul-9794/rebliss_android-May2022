package com.rebliss.domain.model.state;

        import com.google.gson.annotations.SerializedName;

        import java.io.Serializable;
        import java.util.List;

public class Data implements Serializable {
    public List<State> getState() {
        return state;
    }

    public void setState(List<State> state) {
        this.state = state;
    }

    @SerializedName("state")
    private List<State> state;
}
