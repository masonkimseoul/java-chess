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

    private static final String DATABASE_NAME = "chess";
    private static final String TABLE_NAME = "piece";

    private final ChessGameConnector connector;

    public PieceDao(ChessGameConnector connector) {
        this.connector = connector;
    }

    public void create(PieceDto pieceDto, Long gameId) {
        String query = "INSERT INTO "
                + TABLE_NAME
                + " (game_id, piece_appearance, position_row, position_column) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = connector.getConnection(DATABASE_NAME)) {
            PreparedStatement statement = connection.prepareStatement(query);
            List<String> convertedPieceInfo = convertPieceToPieceDto(pieceDto);
            statement.setLong(1, gameId);
            statement.setString(2, convertedPieceInfo.get(0));
            statement.setString(3, convertedPieceInfo.get(1));
            statement.setString(4, convertedPieceInfo.get(2));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("기물을 생성할 수 없습니다: ", e);
        }
    }

    private List<String> convertPieceToPieceDto(PieceDto pieceDto) {
        Piece piece = pieceDto.piece();
        String pieceAppearance = piece.toString();
        String positionRow = String.valueOf(pieceDto.positionRow());
        String positionColumn = String.valueOf(pieceDto.positionColumn());
        return List.of(pieceAppearance, positionRow, positionColumn);
    }

    public List<PieceDto> findAllPieceByGameId(Long gameId) {
        String query = "SELECT * FROM "
                + TABLE_NAME
                + "WHERE game_id = "
                + gameId;
        try (Connection connection = connector.getConnection(DATABASE_NAME)) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            List<PieceDto> pieces = new ArrayList<>();
            while (resultSet.next()) {
                String pieceAppearance = resultSet.getString("piece_appearance");
                String positionRow = resultSet.getString("position_row");
                String positionColumn = resultSet.getString("position_column");
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

    public void deletePiecesByGameId(Long gameId) {
        String query = "DELETE FROM " + TABLE_NAME + "WHERE game_id = " + gameId;
        try (Connection connection = connector.getConnection(DATABASE_NAME)) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("기물을 삭제할 수 없습니다: ", e);
        }
    }
}
