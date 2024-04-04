package chess.dao;

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
                + " (game_id, piece_type, piece_color, position_column, position_row) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = connector.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            List<String> convertedPieceInfo = convertPieceToPieceDto(pieceDto);
            statement.setLong(1, pieceDto.gameId());
            statement.setString(2, convertedPieceInfo.get(0));
            statement.setString(3, convertedPieceInfo.get(1));
            statement.setString(4, convertedPieceInfo.get(2));
            statement.setString(5, convertedPieceInfo.get(3));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("기물을 생성할 수 없습니다: ", e);
        }
    }

    private List<String> convertPieceToPieceDto(PieceDto pieceDto) {
        Piece piece = pieceDto.piece();
        String pieceType = PieceType.findPieceTypeByName(piece.toString()).name();
        String pieceColor = Color.findColorByPieceName(piece.toString()).name();
        String positionColumn = pieceDto.positionColumn();
        String positionRow = pieceDto.positionRow();
        return List.of(pieceType, pieceColor, positionColumn, positionRow);
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
                String pieceType = resultSet.getString("piece_type");
                String pieceColor = resultSet.getString("piece_color");
                String positionRow = resultSet.getString("position_column");
                String positionColumn = resultSet.getString("position_row");
                Piece piece = convertPieceDtoToPiece(pieceType, pieceColor);
                pieces.add(new PieceDto(gameId, piece, positionRow, positionColumn));
            }
            return pieces;
        } catch (SQLException e) {
            throw new RuntimeException("기물을 찾을 수 없습니다: ", e);
        }
    }

    private Piece convertPieceDtoToPiece(String pieceType, String pieceColor) {
        PieceType type = PieceType.convertToPiece(pieceType);
        Color color = Color.convertToColor(pieceColor);
        return Piece.from(type, color);
    }
}
