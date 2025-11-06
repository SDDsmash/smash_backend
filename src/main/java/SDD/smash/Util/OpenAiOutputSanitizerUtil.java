package SDD.smash.Util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OpenAiOutputSanitizerUtil {

    public static String sanitize(String raw) {
        if (raw == null) {
            return null;
        }

        StringBuilder sanitized = new StringBuilder(raw.length());
        for (int i = 0; i < raw.length(); i++) {
            char ch = raw.charAt(i);
            switch (ch) {
                case '&' -> sanitized.append("&amp;");
                case '<' -> sanitized.append("&lt;");
                case '>' -> sanitized.append("&gt;");
                case '"' -> sanitized.append("&quot;");
                case '\'' -> sanitized.append("&#39;");
                default -> sanitized.append(ch);
            }
        }
        return sanitized.toString();
    }
}
