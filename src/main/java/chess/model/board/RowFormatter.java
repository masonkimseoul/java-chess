package chess.model.board;

import chess.model.position.Position;
import java.util.ArrayList;
import java.util.List;

public class RowFormatter {

    private static final int MAX_INDEX = 7;
    private static final String DELIMITER = "";

    private final List<String> row;

    public RowFormatter(List<String> row) {
        this.row = row;
    }

    public static RowFormatter of(Board board, int rowIndex) {
        List<String> row = new ArrayList<>();
        for (int i = 0; i <= MAX_INDEX; i++) {
            Position position = new Position(rowIndex, i);
            row.add(findPieceName(board, position));
        }
        return new RowFormatter(row);
    }

    private static String findPieceName(Board board, Position position) {
        return board.findPiece(position)
                .toString();
    }

    @Override
    public String toString() {
        return String.join(DELIMITER, row);
    }
}