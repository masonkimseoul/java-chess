package chess.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.model.piece.Color;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ScoreTest {

    @DisplayName("체스판이 초기화됐을 때 Black 진영의 점수를 올바르게 계산한다")
    @Test
    void calculateBlackTeamScore() {
        Board board = Board.createInitialBoard();

        Score score = Score.from(board);
        double blackTeamScore = score.getScoreByColor(Color.BLACK);

        assertThat(blackTeamScore).isEqualTo(38.0);
    }

    @DisplayName("체스판이 초기화됐을 때 White 진영의 점수를 올바르게 계산한다")
    @Test
    void calculateWhiteTeamScore() {
        Board board = Board.createInitialBoard();

        Score score = Score.from(board);
        double blackTeamScore = score.getScoreByColor(Color.WHITE);

        assertThat(blackTeamScore).isEqualTo(38.0);
    }

    @DisplayName("White Pawn이 Black Pawn을 공격하여 한 Column 위에 두 White Pawn이 존재할 때 점수를 계산한다")
    @Test
    void calculateWhitePawnKillBlackPawn() {
        List<String> customBoard = List.of(
                "RNBQKBNR",
                ".PPPPPPP",
                "........",
                "p.......",
                "........",
                "........",
                "p.pppppp",
                "rnbqkbnr"
        );
        Board board = Board.createCustomBoard(customBoard, Color.BLACK);

        Score score = Score.from(board);
        double blackTeamScore = score.getScoreByColor(Color.BLACK);
        double whiteTeamScore = score.getScoreByColor(Color.WHITE);

        assertAll(
                //Black Pawn 1개 유실
                () -> assertThat(blackTeamScore).isEqualTo(37.0),

                //White Pawn 2개가 한 Column 위에 존재하여 각 0.5점으로 계산
                () -> assertThat(whiteTeamScore).isEqualTo(37.0)
        );
    }

    @DisplayName("게임이 진행됐을 때의 점수를 올바르게 계산한다")
    @Test
    void calculateProcessedGameScore() {
        List<String> customBoard = List.of(
                ".KR.....",
                "P.PB....",
                ".P..Q...",
                "........",
                ".....nq.",
                ".....p.p",
                ".....pp.",
                "....rk.."
        );
        Board board = Board.createCustomBoard(customBoard, Color.BLACK);

        Score score = Score.from(board);
        double blackTeamScore = score.getScoreByColor(Color.BLACK);
        double whiteTeamScore = score.getScoreByColor(Color.WHITE);

        assertAll(
                //Black Rook 1개, Bishop 1개, Queen 1개, King 1개, Pawn 3개 존재
                () -> assertThat(blackTeamScore).isEqualTo(20.0),
                //White Rook 1개, Knight 1개, Queen 1개, King 1개, Pawn 4개(2개는 같은 Column) 존재
                () -> assertThat(whiteTeamScore).isEqualTo(19.5)
        );
    }
}
