package chess.model.board;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import chess.model.piece.Color;
import java.util.ArrayList;
import java.util.List;

public class BoardMapper {

    private static final int MAX_INDEX = 7;
    private static final String DELIMITER = "\n";

    private final List<RowMapper> rows;
    private final Color turn;

    public BoardMapper(List<RowMapper> rows, Color turn) {
        this.rows = rows;
        this.turn = turn;
    }

    public static BoardMapper from(Board board, Color turn) {
        List<RowMapper> rows = new ArrayList<>();
        for (int i = 0; i <= MAX_INDEX; i++) {
            rows.add(RowMapper.of(board, i));
        }
        return new BoardMapper(rows, turn);
    }

    public Color getTurn() {
        return turn;
    }

    @Override
    public String toString() {
        return rows.stream()
                .map(RowMapper::toString)
                .collect(collectingAndThen(toList(), list -> String.join(DELIMITER, list)));
    }
}
