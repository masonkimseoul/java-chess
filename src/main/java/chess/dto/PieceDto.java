package chess.dto;

public record PieceDto(Long gameId, String pieceAppearance, String positionColumn, String positionRow) {
}
