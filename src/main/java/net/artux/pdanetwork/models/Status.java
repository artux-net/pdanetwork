package net.artux.pdanetwork.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class Status {

    private boolean success;
    private int code;
    private String description;

    public Status(){
        code = 200;
    }

    public Status(boolean success, String description) {
        this.success = success;
        if (success)
            code=200;
        else
            code=400;
        this.description = description;
    }

    public Status(boolean success) {
        this.success = success;
    }

}
