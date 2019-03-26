package database;

import database.DB_Config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

public class DB_ConnectionPool implements DB_Config {

    private static DB_ConnectionPool instance;
    private Queue<Connection> connPool = new LinkedList<>();
    private final int MAX_CONNECTION = 100;
    private int numConnection = 0;

    private DB_ConnectionPool() {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static DB_ConnectionPool getInstance() {
        if (instance == null) {
            synchronized (DB_ConnectionPool.class) {
                instance = new DB_ConnectionPool();
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        synchronized (connPool) {
            if (connPool.isEmpty()) {
                if (numConnection < MAX_CONNECTION) {
                    Connection connection = DriverManager.getConnection(DB_URL + DB_DATABASE_NAME, DB_USERNAME, DB_PASSWORD);
                    numConnection++;
                    return connection;
                }
                throw new RuntimeException("Try again later");
            }
            return connPool.poll();
        }
    }

    public void realeaseConnection(Connection connection) {
        synchronized (connPool) {
            connPool.offer(connection);
        }
    }

    public static void main(String[] args) {
        try {
            DB_ConnectionPool pool = DB_ConnectionPool.getInstance();
            Connection conn = pool.getConnection();
            pool.realeaseConnection(conn);
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
    }

}
