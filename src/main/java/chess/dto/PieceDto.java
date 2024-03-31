package chess.dto;

import chess.model.piece.Piece;

public record PieceDto (Long gameId, Piece piece, String positionColumn, String positionRow){
}
