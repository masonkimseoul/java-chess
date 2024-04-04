package chess.dao;

import chess.model.piece.Color;

public record ChessGameDto(Long id, Color turn) {
}
