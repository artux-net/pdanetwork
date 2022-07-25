package net.artux.pdanetwork.models.user;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class CommandBlock {

    private HashMap<String, List<String>> actions;

}
