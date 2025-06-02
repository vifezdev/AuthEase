package lol.vifez.auth.util;

import java.util.Map;

public class MessageUtil {

    public static String replacePlaceholders(String message, Map<String, String> placeholders) {
        if (message == null || placeholders == null) return message;

        String result = message;
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return CC.translate(result);
    }
}