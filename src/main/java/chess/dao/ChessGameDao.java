package chess.dao;

import chess.dto.ChessGameDto;
import chess.model.piece.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChessGameDao {

    private static final String DATABASE_NAME = "chess";
    private static final String TABLE_NAME = "chessgame";

    private final ChessGameConnector connector;

    public ChessGameDao(ChessGameConnector connector) {
        this.connector = connector;
    }

    public void createGame(Color turn) {
        String query = "INSERT INTO " + TABLE_NAME + " (game_turn) VALUES (?)";
        try (Connection connection = connector.getConnection(DATABASE_NAME)) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, turn.name());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("게임을 생성할 수 없습니다: ", e);
        }
    }

    public ChessGameDto findLastGame() {
        String query = "SELECT * FROM " + TABLE_NAME + "WHERE id = MAX(id)";
        try (Connection connection = connector.getConnection(DATABASE_NAME)) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                return null;
            }
            Color gameTurn = Color.valueOf(resultSet.getString("game_turn"));
            return new ChessGameDto(resultSet.getLong(1), gameTurn);
        } catch (SQLException e) {
            throw new RuntimeException("게임을 찾을 수 없습니다: ", e);
        }
    }
}
