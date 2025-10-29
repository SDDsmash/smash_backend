package SDD.smash.Util;


public class BatchUtil {
    public static String addLeadingZero(String code) {
        if (code == null) return null;
        code = code.replace("\uFEFF", "").trim();

        if (code.matches("\\d+")) {
            int num = Integer.parseInt(code);
            if (num < 10) {
                return String.format("%02d", num);
            }
            return String.valueOf(num);
        }
        return code;
    }

    public static String clean(String s) {
        if (s == null) return null;
        return s.replace("\uFEFF", "")
                .replaceAll("^\"|\"$", "")
                .trim();
    }
}
