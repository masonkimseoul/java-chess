package chess.model;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

import chess.model.piece.Color;
import chess.model.piece.Piece;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Score {

    private static final int MAX_COLUMN_COUNT = 8;
    private static final int UNIQUE_COUNT = 1;

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
        Map<Piece, Integer> pieceWithCount = pieces.stream()
                .collect(groupingBy(Function.identity(), summingInt(e -> UNIQUE_COUNT)));
        for (Piece piece : pieces) {
            boolean isDuplicate = pieceWithCount.get(piece) > UNIQUE_COUNT;
            score += pieceScoreByColor(piece, color, isDuplicate);
        }
        return score;
    }

    private static double pieceScoreByColor(Piece piece, Color color, Boolean isDuplicate) {
        if (piece.isAlly(color)) {
            return piece.getScore(isDuplicate);
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
