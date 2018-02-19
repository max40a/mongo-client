package app.core;

public enum OptionName {

    SELECT("PROJECTIONS"),
    FROM("TARGET"),
    WHERE("CONDITION"),
    ORDER_BY("FIELDS"),
    SKIP("SKIP_RECORDS"),
    LIMIT("MAX_RECORDS");

    private String propertyName;

    OptionName(String property) {
        this.propertyName = property;
    }

    public String getPropertyName() {
        return propertyName;
    }
}