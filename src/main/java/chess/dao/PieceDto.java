package chess.dao;

public record PieceDto(Long gameId, String pieceAppearance, String positionColumn, String positionRow) {
}
