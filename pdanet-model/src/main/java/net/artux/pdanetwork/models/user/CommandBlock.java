package net.artux.pdanetwork.models.user;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;

@Data
public class CommandBlock {

    private LinkedHashMap<String, List<String>> actions;

}
