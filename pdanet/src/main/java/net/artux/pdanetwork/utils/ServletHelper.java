package net.artux.pdanetwork.utils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class ServletHelper {

    public static Map<String, String> splitQuery(String  query) {
        Map<String, String> query_pairs = new LinkedHashMap<>();
        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                query_pairs.put(URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8),
                     URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8));
            }
        }
        return query_pairs;
    }
}
