package chess.service;

import chess.dao.ChessGameDao;
import chess.dao.ChessGameDto;
import chess.dao.PieceDao;
import chess.dao.PieceDto;
import chess.model.board.Board;
import chess.model.board.BoardMapper;
import chess.model.board.Score;
import chess.model.piece.Color;
import chess.model.piece.Piece;
import chess.model.piece.PieceType;
import chess.model.position.Position;
import java.util.ArrayList;
import java.util.List;

public class ChessGameService {

    private static final char FIRST_ROW_INDEX = 'a';
    private static final int MAX_COLUMN_COUNT = 8;
    private static final int MAX_ROW_COUNT = 8;
    private static final int INDEX_PREFIX = 1;

    private final ChessGameDao chessGameDao;
    private final PieceDao pieceDao;
    private Board board;
    private Piece lastTargetPiece;

    public ChessGameService(ChessGameDao chessGameDao, PieceDao pieceDao) {
        this.chessGameDao = chessGameDao;
        this.pieceDao = pieceDao;
        this.board = Board.createCustomBoard(List.of(""), Color.NONE);
        this.lastTargetPiece = Piece.from(PieceType.NONE, Color.NONE);
    }

    public boolean canContinueChessGame() {
        return board.canContinue();
    }

    public void startChessGame() {
        board = Board.createInitialBoard();
    }

    public void endChessGame() {
        board.stopGame();
    }

    public void moveChessPiece(String sourcePosition, String targetPosition) {
        lastTargetPiece = board.findPiece(Position.from(targetPosition));
        board.move(sourcePosition, targetPosition);
    }

    public boolean judgeGameEnd() {
        return lastTargetPiece.lostGoal();
    }

    public List<Double> calculateAllTeamScore() {
        Score score = Score.from(board);
        double whiteTeamScore = score.getScoreByColor(Color.WHITE);
        double blackTeamScore = score.getScoreByColor(Color.BLACK);
        return List.of(whiteTeamScore, blackTeamScore);
    }

    public void saveChessGame() {
        BoardMapper boardMapper = BoardMapper.from(board, board.getTurn());
        updateGame(boardMapper.getTurn());
        String convertedBoard = boardMapper.toString().replaceAll(System.lineSeparator(), "");
        for (int i = 0; i < MAX_ROW_COUNT; i++) {
            int pieceRow = i + INDEX_PREFIX;
            createPieceInOneRow(convertedBoard, String.valueOf(pieceRow), i);
        }
    }

    private void createPieceInOneRow(String convertedBoard, String pieceRow, int rowIndex) {
        ChessGameDto chessGameDto = chessGameDao.findLastGame();
        for (int j = 0; j < MAX_COLUMN_COUNT; j++) {
            int pieceIndex = rowIndex * MAX_COLUMN_COUNT + j;
            String pieceColumn = String.valueOf((char) (FIRST_ROW_INDEX + j));
            String pieceAppearance = String.valueOf(convertedBoard.charAt(pieceIndex));
            pieceDao.create(new PieceDto(chessGameDto.id(), pieceAppearance, pieceColumn, pieceRow));
        }
    }

    private void updateGame(Color turn) {
        if (chessGameDao.countGames() > 0) {
            ChessGameDto chessGameDto = chessGameDao.findLastGame();
            chessGameDao.deleteGameByGameId(chessGameDto.id());
        }
        chessGameDao.createGame(turn);
    }

    public void loadGame() {
        ChessGameDto chessGameDto = chessGameDao.findLastGame();
        List<PieceDto> pieceDtos = pieceDao.findAllPieceByGameId(chessGameDto.id());
        char[][] boardArray = new char[MAX_ROW_COUNT][MAX_COLUMN_COUNT];
        for (PieceDto pieceDto : pieceDtos) {
            int rowIndex = Integer.parseInt(pieceDto.positionRow()) - INDEX_PREFIX;
            int columnIndex = pieceDto.positionColumn().charAt(0) - FIRST_ROW_INDEX;
            boardArray[rowIndex][columnIndex] = pieceDto.pieceAppearance().charAt(0);
        }
        List<String> customBoard = convertBoard(boardArray);
        board = Board.createCustomBoard(customBoard, chessGameDto.turn());
    }

    private List<String> convertBoard(char[][] chessBoard) {
        List<String> customBoard = new ArrayList<>();
        for (char[] row : chessBoard) {
            String convertedRow = String.valueOf(row);
            customBoard.add(convertedRow);
        }
        return customBoard;
    }

    public String getChessBoard() {
        BoardMapper boardMapper = BoardMapper.from(board, board.getTurn());
        return boardMapper.toString();
    }

    public String getWinnerTeam() {
        if (lastTargetPiece.isEnemy(Color.BLACK)) {
            return Color.BLACK.name();
        }
        if (lastTargetPiece.isEnemy(Color.WHITE)) {
            return Color.WHITE.name();
        }
        return Color.NONE.name();
    }
}
