package database;

public enum DB_StatementType {

    GET_FILM_BY_LENGTH("SELECT * FROM film f WHERE f.length < ?");

    private DB_StatementType(String sqlString) {
        this.sqlString = sqlString;
    }

    private String sqlString;

    public String getSqlString() {
        return sqlString;
    }
}
