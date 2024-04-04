package chess.model.board;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import chess.model.piece.Color;
import java.util.ArrayList;
import java.util.List;

public class BoardFormatter {

    private static final int MAX_INDEX = 7;
    private static final String DELIMITER = "\n";

    private final List<RowFormatter> rows;
    private final Color turn;

    public BoardFormatter(List<RowFormatter> rows, Color turn) {
        this.rows = rows;
        this.turn = turn;
    }

    public static BoardFormatter from(Board board, Color turn) {
        List<RowFormatter> rows = new ArrayList<>();
        for (int i = 0; i <= MAX_INDEX; i++) {
            rows.add(RowFormatter.of(board, i));
        }
        return new BoardFormatter(rows, turn);
    }

    public Color getTurn() {
        return turn;
    }

    @Override
    public String toString() {
        return rows.stream()
                .map(RowFormatter::toString)
                .collect(collectingAndThen(toList(), list -> String.join(DELIMITER, list)));
    }
}
