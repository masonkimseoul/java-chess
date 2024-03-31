package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChessGameConnectorTest {

    @DisplayName("DB 접속에 성공한다")
    @Test
    void connectToDB() {
        ChessGameConnector chessGameConnector = new ChessGameConnector();

        Connection connection = chessGameConnector.getConnection();

        assertThat(connection).isNotNull();
    }
}
