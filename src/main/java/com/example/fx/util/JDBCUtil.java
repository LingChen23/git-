package com.example.fx.util;

import lombok.NoArgsConstructor;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
@NoArgsConstructor
public class JDBCUtil {
    private static final JDBCUtil instance = new JDBCUtil();

    private static Properties prop;
    private static Connection conn;

    static {
        try {
            prop = new Properties();
            prop.load(JDBCUtil.class.getClassLoader().getResourceAsStream("JdbcConfig.properties"));
            conn = instance.getConn();
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static JDBCUtil getInstance() {
        return instance;
    }

    private static Connection getConn() throws SQLException {
        try {
            Class.forName(prop.getProperty("driver"));
            return DriverManager.getConnection(
                    prop.getProperty("url"),
                    prop.getProperty("name"),
                    prop.getProperty("password"));
        } catch (ClassNotFoundException | SQLException e) {
            throw new SQLException("Failed to create database connection.", e);
        }
    }

    public static CachedRowSet execSql(String sql, Object... args) throws SQLException {
        try (Connection connection = getConn();
             PreparedStatement pstm = connection.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) {
                pstm.setObject(i + 1, args[i]);
            }
            pstm.execute();

            try (ResultSet resultSet = pstm.getResultSet()) {
                RowSetFactory rowSetFactory = RowSetProvider.newFactory();
                CachedRowSet rowSet = rowSetFactory.createCachedRowSet();

                if (resultSet != null) {
                    rowSet.populate(resultSet);
                }

                return rowSet;
            }
        } catch (SQLException e) {
            throw new SQLException("SQL execution failed.", e);
        }
    }
}
