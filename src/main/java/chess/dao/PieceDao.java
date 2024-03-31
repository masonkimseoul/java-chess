package chess.dao;

import chess.dto.PieceDto;
import chess.model.piece.Color;
import chess.model.piece.Piece;
import chess.model.piece.PieceType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PieceDao {

    private static final String TABLE_NAME = "piece";

    private final ChessGameConnector connector;

    public PieceDao(ChessGameConnector connector) {
        this.connector = connector;
    }

    public void create(PieceDto pieceDto) {
        String query = "INSERT INTO "
                + TABLE_NAME
                + " (game_id, piece_appearance, position_column, position_row) VALUES (?, ?, ?, ?)";
        try (Connection connection = connector.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, pieceDto.gameId());
            statement.setString(2, pieceDto.piece().toString());
            statement.setString(3, pieceDto.positionColumn());
            statement.setString(4, pieceDto.positionRow());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("기물을 생성할 수 없습니다: ", e);
        }
    }

    public List<PieceDto> findAllPieceByGameId(Long gameId) {
        String query = "SELECT * FROM "
                + TABLE_NAME
                + " WHERE game_id = "
                + gameId;
        try (Connection connection = connector.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            List<PieceDto> pieces = new ArrayList<>();
            while (resultSet.next()) {
                String pieceAppearance = resultSet.getString("piece_appearance");
                String positionRow = resultSet.getString("position_column");
                String positionColumn = resultSet.getString("position_row");
                Piece piece = convertPieceDtoToPiece(pieceAppearance);
                pieces.add(new PieceDto(gameId, piece, positionRow, positionColumn));
            }
            return pieces;
        } catch (SQLException e) {
            throw new RuntimeException("기물을 찾을 수 없습니다: ", e);
        }
    }

    private Piece convertPieceDtoToPiece(String pieceAppearance) {
        PieceType pieceType = PieceType.findPieceTypeByName(pieceAppearance);
        Color color = Color.findColorByName(pieceAppearance);
        return Piece.from(pieceType, color);
    }
}
