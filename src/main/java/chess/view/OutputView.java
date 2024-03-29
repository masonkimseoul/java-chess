package chess.view;

import chess.dto.BoardDto;
import chess.model.piece.Color;

public final class OutputView {

    private static final String NEWLINE = System.lineSeparator();
    private static final String ERROR_PREFIX = "[ERROR] ";
    private static final String WHITE_TEAM = "WHITE 팀";
    private static final String BLACK_TEAM = "BLACK 팀";
    private static final String DELIMITER = " : ";
    private static final String TEAM_WINNING_MESSAGE = "이 더 우세합니다.";
    private static final String TEAM_SAME_SCORE_MESSAGE = "두 팀의 점수가 같습니다.";
    private static final String WINNER_MESSAGE = "이 승리했습니다.";

    private OutputView() {
    }

    public static void printException(IllegalArgumentException e) {
        System.out.println(ERROR_PREFIX + e.getMessage() + NEWLINE);
    }

    public static void printChessBoard(BoardDto boardDto) {
        System.out.println(boardDto.toString() + NEWLINE);
    }

    public static void printGameScore(double whiteScore, double blackScore) {
        System.out.println(WHITE_TEAM + DELIMITER + whiteScore);
        System.out.println(BLACK_TEAM + DELIMITER + blackScore);
    }

    public static void printDominatingTeam(double whiteScore, double blackScore) {
        if (whiteScore > blackScore) {
            System.out.println(WHITE_TEAM + TEAM_WINNING_MESSAGE);
        }
        if (whiteScore < blackScore) {
            System.out.println(BLACK_TEAM + TEAM_WINNING_MESSAGE);
        }
        if (whiteScore == blackScore) {
            System.out.println(TEAM_SAME_SCORE_MESSAGE);
        }
        System.out.print(NEWLINE);
    }

    public static void printWinnerTeam(Color color) {
        if (color.isBlack()) {
            System.out.println(BLACK_TEAM + WINNER_MESSAGE);
        }
        if (color.isWhite()) {
            System.out.println(WHITE_TEAM + WINNER_MESSAGE);
        }
    }
}
