package chess.model.board;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

public class BoardFormatter {

    private static final int MAX_INDEX = 7;
    private static final String DELIMITER = "\n";

    private final List<RowFormatter> rows;

    public BoardFormatter(List<RowFormatter> rows) {
        this.rows = rows;
    }

    public static BoardFormatter from(Board board) {
        List<RowFormatter> rows = new ArrayList<>();
        for (int i = 0; i <= MAX_INDEX; i++) {
            rows.add(RowFormatter.of(board, i));
        }
        return new BoardFormatter(rows);
    }

    @Override
    public String toString() {
        return rows.stream()
                .map(RowFormatter::toString)
                .collect(collectingAndThen(toList(), list -> String.join(DELIMITER, list)));
    }
}
