package app.parser;

public enum SqlReservedWord {

    SELECT("SELECT", "PROJECTIONS"),
    FROM("FROM", "TARGET"),
    WHERE("WHERE", "CONDITION"),
    ORDER("ORDER BY", "FIELDS"),
    SKIP("SKIP", "SKIP_RECORDS"),
    LIMIT("LIMIT", "MAX_RECORDS");

    private String reservedWord;
    private String adjective;

    SqlReservedWord(String property, String adjective) {
        this.reservedWord = property;
        this.adjective = adjective;
    }

    public String getReservedWord() {
        return reservedWord;
    }

    public String getAdjective() {
        return adjective;
    }
}