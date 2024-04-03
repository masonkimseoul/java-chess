package chess.view;

public final class OutputView {

    private static final String NEWLINE = System.lineSeparator();
    private static final String ERROR_PREFIX = "[ERROR] ";
    private static final String WHITE_TEAM_NAME = "WHITE ";
    private static final String BLACK_TEAM_NAME = "BLACK ";
    private static final String TEAM_PREFIX = " 팀";
    private static final String DELIMITER = " : ";
    private static final String TEAM_WINNING_MESSAGE = "이 더 우세합니다.";
    private static final String TEAM_SAME_SCORE_MESSAGE = "두 팀의 점수가 같습니다.";
    private static final String WINNER_MESSAGE = "이 승리했습니다.";

    private OutputView() {
    }

    public static void printException(IllegalArgumentException e) {
        System.out.println(ERROR_PREFIX + e.getMessage() + NEWLINE);
    }

    public static void printChessBoard(String convertedBoard) {
        System.out.println(convertedBoard + NEWLINE);
    }

    public static void printGameScore(double whiteScore, double blackScore) {
        System.out.println(WHITE_TEAM_NAME + TEAM_PREFIX + DELIMITER + whiteScore);
        System.out.println(BLACK_TEAM_NAME + TEAM_PREFIX + DELIMITER + blackScore);
    }

    public static void printDominatingTeam(double whiteScore, double blackScore) {
        if (whiteScore > blackScore) {
            System.out.println(WHITE_TEAM_NAME + TEAM_PREFIX + TEAM_WINNING_MESSAGE);
        }
        if (whiteScore < blackScore) {
            System.out.println(BLACK_TEAM_NAME + TEAM_PREFIX + TEAM_WINNING_MESSAGE);
        }
        if (whiteScore == blackScore) {
            System.out.println(TEAM_SAME_SCORE_MESSAGE);
        }
        System.out.print(NEWLINE);
    }

    public static void printWinnerTeam(String colorName) {
        System.out.println(colorName + TEAM_PREFIX + WINNER_MESSAGE);
    }
}
