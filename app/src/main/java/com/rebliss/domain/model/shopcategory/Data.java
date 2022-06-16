
package com.rebliss.domain.model.shopcategory;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("all_shops")
    @Expose
    private List<AllShop> allShops = null;

    public List<AllShop> getAllShops() {
        return allShops;
    }

    public void setAllShops(List<AllShop> allShops) {
        this.allShops = allShops;
    }

}
