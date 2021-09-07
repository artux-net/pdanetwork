package net.artux.pdanetwork.service.files;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ItemType {

    private int id;
    private String title;
    private String desc;
    private String icon;

}
