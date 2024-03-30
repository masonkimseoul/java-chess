package chess.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ChessGameConnector {

    private static final String SERVER = "localhost:13306";
    private static final String OPTION = "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    private ChessGameConnector() {
    }

    public static Connection getConnection(String database) {
        try {
            return DriverManager.getConnection("jdbc:mysql://" + SERVER + "/" + database + OPTION, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.err.println("DB 연결 오류:" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
