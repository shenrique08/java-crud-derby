package org.api.util;

import java.sql.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DatabaseUtil {
    private static final String JDBC_URL = "jdbc:derby://localhost:1527//dbs/MeuBancoDeDados;create=true";

    static {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load Derby JDBC driver", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Check if table exists (now with correct single argument)
            if (!tableExists(conn)) {
                String sqlScript = new String(Files.readAllBytes(
                        Paths.get("src/main/resources/init.sql")));

                // Execute each statement separately
                for (String sql : sqlScript.split(";")) {
                    if (!sql.trim().isEmpty()) {
                        stmt.executeUpdate(sql);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Database initialization failed", e.getCause());
        }
    }

    private static boolean tableExists(Connection conn) throws SQLException {
        String tableName = "PRODUTO";
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet rs = meta.getTables(null, null, tableName, new String[]{"TABLE"});
        return rs.next();
    }
}
