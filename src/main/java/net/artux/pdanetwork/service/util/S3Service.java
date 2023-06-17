package net.artux.pdanetwork.service.util;

import java.util.List;

public interface S3Service {

    boolean putString(String key, String content);

    String getString(String key);

    List<String> getEntries(String prefix);
}
