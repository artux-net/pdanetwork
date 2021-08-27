package net.artux.pdanetwork.models;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@RequiredArgsConstructor
public class Status {

    private boolean success;
    private int code;
    private String description;

    public Status(boolean success, String description) {
        this.success = success;
        if (success)
            code=200;
        else
            code=400;
        this.description = description;
    }

}
