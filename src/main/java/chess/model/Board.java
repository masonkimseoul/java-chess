package chess.model;

import chess.model.piece.Piece;
import chess.model.piece.PieceType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {

    private static final List<String> INITIAL_BOARD = List.of(
            "RNBQKBNR",
            "PPPPPPPP",
            "........",
            "........",
            "........",
            "........",
            "pppppppp",
            "rnbqkbnr"
    );
    private static final Piece EMPTY_PIECE =  Piece.from(PieceType.NONE);

    private final Map<Position, Piece> board;
    private int turnCount;

    private Board(Map<Position, Piece> board, int turnCount) {
        this.board = board;
        this.turnCount = turnCount;
    }

    public static Board createInitialBoard() {
        return new Board(generateBoard(INITIAL_BOARD), 0);
    }

    public static Board createCustomBoard(List<String> customBoard) {
        return new Board(generateBoard(customBoard), 0);
    }

    private static Map<Position, Piece> generateBoard(List<String> customBoard) {
        Map<Position, Piece> board = new HashMap<>();
        for (int i = 0; i < customBoard.size(); i++) {
            String row = customBoard.get(i);
            putPiecesInRow(board, row, i);
        }
        return board;
    }

    private static void putPiecesInRow(Map<Position, Piece> board, String row, int rowIndex) {
        for (int j = 0; j < row.length(); j++) {
            Position position = new Position(rowIndex, j);
            char pieceName = row.charAt(j);
            PieceType pieceType = PieceType.findPieceTypeByName(String.valueOf(pieceName));
            Piece piece = Piece.from(pieceType);
            board.put(position, piece);
        }
    }

    public void move(String sourceCoordinate, String targetCoordinate) {
        Position source = Position.from(sourceCoordinate);
        Position target = Position.from(targetCoordinate);
        Piece sourcePiece = findPiece(source);
        Piece targetPiece = findPiece(target);

        validate(sourcePiece, targetPiece, source, target);

        board.put(target, sourcePiece);
        board.put(source, EMPTY_PIECE);
        turnCount++;
    }

    private void validate(Piece sourcePiece, Piece targetPiece, Position source, Position target) {
        validatePiecesPosition(sourcePiece, targetPiece);
        validateTurn(sourcePiece);
        validatePieceCanMove(sourcePiece, targetPiece, source, target);
        validatePieceRoute(sourcePiece, source, target);
    }

    private void validatePiecesPosition(Piece sourcePiece, Piece targetPiece) {
        if (sourcePiece.isNone()) {
            throw new IllegalArgumentException("source위치에 기물이 존재하지 않습니다.");
        }
        if (targetPiece.isAlly(turnCount)) {
            throw new IllegalArgumentException("target위치에 내 기물이 존재합니다.");
        }
    }

    private void validateTurn(Piece sourcePiece) {
        boolean isEnemy = sourcePiece.isEnemy(turnCount);
        if (isEnemy && sourcePiece.isWhite()) {
            throw new IllegalArgumentException("지금은 Black 차례입니다.");
        }
        if (isEnemy && sourcePiece.isBlack()) {
            throw new IllegalArgumentException("지금은 White 차례입니다.");
        }
    }

    private void validatePieceCanMove(Piece sourcePiece, Piece targetPiece, Position source, Position target) {
        if (targetPiece.isEnemy(turnCount) && sourcePiece.canAttack(source, target)) {
            return;
        }
        if (!sourcePiece.canMove(source, target)) {
            throw new IllegalArgumentException("target위치로 기물을 이동할 수 없습니다.");
        }
    }

    private void validatePieceRoute(Piece sourcePiece, Position source, Position target) {
        if (sourcePiece.canJump()) {
            return;
        }
        int rowDifference = target.getRow() - source.getRow();
        int columnDifference = target.getColumn() - source.getColumn();

        while (Math.abs(rowDifference) > 1 || Math.abs(columnDifference) > 1) {
            rowDifference = consumeRow(rowDifference);
            columnDifference = consumeColumn(columnDifference);
            Position position = source.makeRemotePosition(rowDifference, columnDifference);
            validateEmptyPosition(position);
        }
    }

    private int consumeRow(int rowDifference) {
        if (rowDifference > 0) {
            rowDifference--;
        }
        if (rowDifference < 0) {
            rowDifference++;
        }
        return rowDifference;
    }

    private int consumeColumn(int columnDifference) {
        if (columnDifference > 0) {
            columnDifference--;
        }
        if (columnDifference < 0) {
            columnDifference++;
        }
        return columnDifference;
    }

    private void validateEmptyPosition(Position position) {
        Piece targetPiece = findPiece(position);
        if (targetPiece.isExist()) {
            throw new IllegalArgumentException("경로 상에 다른 기물이 존재합니다.");
        }
    }

    public Piece findPiece(Position position) {
        return board.get(position);
    }
}
