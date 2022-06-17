package net.artux.pdanetwork.service.util;

import com.google.gson.Gson;
import lombok.experimental.ExtensionMethod;
import net.artux.pdanetwork.models.communication.MessageEntity;

@ExtensionMethod({Object.class, MessageEntity.class})
public class Utils {

    private static Gson gson = new Gson();

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static String json(Object o) {
        return gson.toJson(o);
    }

}
