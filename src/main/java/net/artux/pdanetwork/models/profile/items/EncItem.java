package net.artux.pdanetwork.models.profile.items;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class EncItem extends Item{

    protected String desc;
    protected String content;
    protected List<String> dignities;
    protected List<String> disadvantages;

}