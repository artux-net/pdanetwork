package net.artux.pdanetwork.service.util;

public interface S3Service {

    boolean putString(String key, String content);

    String getString(String key);

}
