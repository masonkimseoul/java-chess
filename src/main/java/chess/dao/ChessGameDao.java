package chess.dao;

import chess.model.piece.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChessGameDao {

    private static final String TABLE_NAME = "chessgame";

    private final ChessGameConnector connector;

    public ChessGameDao(ChessGameConnector connector) {
        this.connector = connector;
    }

    public void createGame(Color turn) {
        String query = "INSERT INTO " + TABLE_NAME + " (game_turn) VALUES (?)";
        try (Connection connection = connector.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, turn.name());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("게임을 생성할 수 없습니다: ", e);
        }
    }

    public ChessGameDto findLastGame() {
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY id DESC LIMIT 1";
        try (Connection connection = connector.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new RuntimeException("게임을 찾을 수 없습니다");
            }
            Color gameTurn = Color.valueOf(resultSet.getString("game_turn"));
            return new ChessGameDto(resultSet.getLong(1), gameTurn);
        } catch (SQLException e) {
            throw new RuntimeException("게임을 찾을 수 없습니다: ", e);
        }
    }

    public void deleteGameByGameId(Long gameId) {
        String query = "DELETE FROM " + TABLE_NAME + " WHERE id=" + gameId;
        try (Connection connection = connector.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("게임을 삭제할 수 없습니다: ", e);
        }
    }

    public int countGames() {
        String query = "SELECT COUNT(*) AS game_count FROM " + TABLE_NAME;
        try (Connection connection = connector.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("game_count");
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException("게임 수를 계산할 수 없습니다: ", e);
        }
    }
}
