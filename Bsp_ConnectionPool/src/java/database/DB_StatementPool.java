/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import database.DB_StatementType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 *
 * @author Jan
 */
public class DB_StatementPool {

    private DB_ConnectionPool connPool = DB_ConnectionPool.getInstance();
    private Map<Connection, Queue<Statement>> stmtMap = new HashMap<>();
    private Map<Connection, Map<DB_StatementType, PreparedStatement>> pStatMap = new HashMap<>();

    public PreparedStatement getPreparedStatement(DB_StatementType pStatType) throws SQLException {
        Connection connection = connPool.getConnection();
        Map<DB_StatementType, PreparedStatement> map = pStatMap.get(connection);
        if (map == null) {
            map = new HashMap<>();
        }
        PreparedStatement pStat = map.get(pStatType);
        if (pStat == null) {
            pStat = connection.prepareStatement(pStatType.getSqlString());
            map.put(pStatType, pStat);
        }
        return pStat;
    }

    private DB_StatementPool() {
    }

    public static DB_StatementPool getInstance() {
        return DB_StatementPoolHolder.INSTANCE;
    }

    private static class DB_StatementPoolHolder {

        private static final DB_StatementPool INSTANCE = new DB_StatementPool();
    }

    public Statement getStatement() throws SQLException {
        Connection connection = connPool.getConnection();
        Queue<Statement> stmtList = stmtMap.get(connection);
        if (stmtList == null) {
            stmtList = new LinkedList<>();
            stmtMap.put(connection, stmtList);
        }
        if (stmtList.isEmpty()) {
            return connection.createStatement();
        }
        return stmtList.poll();
    }

    public void releaseStatement(Statement statement) throws SQLException {
        Connection connection = statement.getConnection();
        if (statement instanceof Statement) {
            stmtMap.get(connection).offer(statement);
        }
        connPool.realeaseConnection(connection);
    }
}
