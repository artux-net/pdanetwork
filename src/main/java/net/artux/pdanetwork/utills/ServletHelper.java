package net.artux.pdanetwork.utills;

import com.google.gson.Gson;
import net.artux.pdanetwork.authentication.Member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

public class ServletHelper {

    static Gson gson = new Gson();

    public static String getString(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = request.getReader().readLine()) != null) {
            sb.append(s);
        }
        if (sb.charAt(0) == '"')
            sb.deleteCharAt(0);
        if (sb.charAt(sb.length() - 1) == '"')
            sb.deleteCharAt(sb.length() - 1);
        String s1 = sb.toString();

        return s1.replaceAll("\\\\", "");
    }

    public static Map<String, String> getHeaders(HttpServletRequest req){
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
        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                query_pairs.put(URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8), URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8));
            }
        }
        return query_pairs;
    }

    public static Member getMember(HttpServletRequest req) {
        String token = req.getHeader("t");
        return ServletContext.mongoUsers.getByToken(token);
    }

    public static <T> T getBody(HttpServletRequest req, Class<T> object) throws IOException {
        return gson.fromJson(getString(req), object);
    }

    public static void setResponse(HttpServletResponse response, Object body) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(gson.toJson(body));
    }

    public static void setStringResponse(HttpServletResponse response, String body) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(body);
    }

}
