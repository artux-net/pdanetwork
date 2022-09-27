package net.artux.pdanetwork.models.file;

import lombok.Data;

@Data
public class File {

    private String name;
    private Type type;

    public enum Type {
        directory,
        file
    }
}
