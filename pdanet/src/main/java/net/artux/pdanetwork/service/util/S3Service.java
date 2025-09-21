package net.artux.pdanetwork.service.util;

import java.util.List;

public interface S3Service<T> {

    boolean put(String key, T content);

    T get(String key);

    void delete(String key);

    List<String> getEntries(String prefix);
}
