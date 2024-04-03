package chess.model.board;

import chess.model.piece.Color;
import chess.model.piece.Piece;
import java.util.List;

public class Score {

    private static final int MAX_COLUMN_COUNT = 8;

    private final double whiteTeamScore;
    private final double blackTeamScore;

    private Score(double whiteTeamScore, double blackTeamScore) {
        this.whiteTeamScore = whiteTeamScore;
        this.blackTeamScore = blackTeamScore;
    }

    public static Score from(Board board) {
        double whiteTeamScore = calculateBoardScore(board, Color.WHITE);
        double blackTeamScore = calculateBoardScore(board, Color.BLACK);
        return new Score(whiteTeamScore, blackTeamScore);
    }

    private static double calculateBoardScore(Board board, Color color) {
        double score = 0;
        for (int i = 0; i < MAX_COLUMN_COUNT; i++) {
            List<Piece> pieces = board.findPiecesInColumn(i);
            score += calculateColumnScore(pieces, color);
        }
        return score;
    }

    private static double calculateColumnScore(List<Piece> pieces, Color color) {
        double score = 0;
        for (Piece piece : pieces) {
            score += pieceScoreByColor(pieces, piece, color);
        }
        return score;
    }

    private static double pieceScoreByColor(List<Piece> sameColumnPieces, Piece piece, Color color) {
        if (piece.isAlly(color)) {
            return piece.getScore(sameColumnPieces);
        }
        return 0;
    }

    public double getScoreByColor(Color color) {
        if (color.isBlack()) {
            return blackTeamScore;
        }
        return whiteTeamScore;
    }
}
