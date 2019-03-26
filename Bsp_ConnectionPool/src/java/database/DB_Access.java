package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class DB_Access {
    
    private static DB_Access instance;
    
    private DB_Access() {
        
    }
    
    private static DB_Access getInstance() {
        if (instance == null) {
            instance = new DB_Access();
        }
        return instance;
    }
    private DB_StatementPool stmtPool = DB_StatementPool.getInstance();
    
    public List<String> getGenres() throws SQLException {
        Statement stat = stmtPool.getStatement();
        String sqlString = "SELECT * FROM category;";
        ResultSet rs = stat.executeQuery(sqlString);
        List<String> genreNames = new LinkedList<>();
        while (rs.next()) {
            genreNames.add(rs.getString("name"));
        }
        stmtPool.releaseStatement(stat);
        return genreNames;
    }
    
    public List<String> getFilmsByLength(int length) throws SQLException {
        PreparedStatement pStat = stmtPool.getPreparedStatement(DB_StatementType.GET_FILM_BY_LENGTH);
        pStat.setInt(1, length);
        ResultSet rs = pStat.executeQuery();
        List<String> filmNames = new LinkedList<>();
        while (rs.next()) {
            filmNames.add(rs.getString("title"));
        }
        stmtPool.releaseStatement(pStat);
        return filmNames;
    }
    
    public static void main(String[] args) {
        try {
            DB_Access dba = new DB_Access();
            for (String name : dba.getFilmsByLength(60)) {
                System.out.println(name);
            }
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
    }
}
