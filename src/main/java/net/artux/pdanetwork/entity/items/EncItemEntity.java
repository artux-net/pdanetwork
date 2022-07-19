package net.artux.pdanetwork.entity.items;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EncItemEntity extends ItemEntity {

    protected String desc;
    protected String content;
    protected List<String> dignities;
    protected List<String> disadvantages;

}