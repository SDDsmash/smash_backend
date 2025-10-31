package SDD.smash.Support.domain;

/**
 * 정책태그(
 */
public enum SupportTag {
    HOUSING_SUPPORT("주거지원"),
    LONG_TERM_UNEMPLOYED_YOUTH("장기미취업청년"),
    INTERN("인턴"),
    LOAN("대출");

    SupportTag(String value) {
        this.value = value;
    }

    private final String value;

    public String getValue()
    {
        return value;
    }
}
