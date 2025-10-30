package SDD.smash.Util;


public class BatchTextUtil {
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

    /**
     * 보이지 않는 공백들 제거
     * */
    public static String normalize(String s) {
        if (s == null) return null;
        // BOM 제거
        s = s.replace("\uFEFF", "");
        // 제로폭 문자 제거 (ZWSP/ZWNJ/ZWJ/WORD JOINER 등)
        s = s.replaceAll("[\\u200B-\\u200D\\u2060]", "");
        // NBSP→공백
        s = s.replace('\u00A0', ' ');
        // 탭/CR 등 컨트롤 공백 정규화
        s = s.replace('\r', ' ').replace('\t', ' ');
        // 따옴표 삭제
        s = s.replaceAll("^\"|\"$", "").trim();
        return s.isEmpty() ? "" : s;
    }

    public static boolean isBlank(String s) {
        return s == null || normalize(s).isEmpty();
    }

    public static String digitsOnly(String s) {
        if (s == null) return null;
        s = normalize(s).replace(",", "");
        // 숫자 이외 제거 (필요하면 주석해제)
        // s = s.replaceAll("[^0-9]", "");
        return s;
    }

}
