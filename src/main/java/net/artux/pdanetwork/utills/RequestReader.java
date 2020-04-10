package net.artux.pdanetwork.utills;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

public class RequestReader {

    public static String getString(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = request.getReader().readLine()) != null) {
            sb.append(s);
        }
        return sb.toString();
    }

    static Map<String, String> getHeaders(HttpServletRequest req){
        Enumeration<String> enumeration = req.getHeaderNames();
        Map<String, String> headers = new LinkedHashMap<>();
        while(enumeration.hasMoreElements()){
            String name = enumeration.nextElement();
            headers.put(name, req.getHeader(name));
        }
        return headers;
    }

    public static Map<String, String> splitQuery(String  query) {
        Map<String, String> query_pairs = new LinkedHashMap<>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8), URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8));
        }
        return query_pairs;
    }

}
