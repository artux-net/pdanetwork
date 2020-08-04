
package net.artux.pdanetwork.models.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Battles {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("wins")
    @Expose
    private Integer wins;
    //@SerializedName("history")
    //@Expose
    //private List<Object> history = new ArrayList<>();

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }



}
