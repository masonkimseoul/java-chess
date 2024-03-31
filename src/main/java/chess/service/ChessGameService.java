package chess.service;

import chess.dao.ChessGameDao;
import chess.dao.PieceDao;
import chess.dto.BoardDto;
import chess.dto.ChessGameDto;
import chess.dto.PieceDto;
import chess.model.Board;
import chess.model.piece.Color;
import java.util.ArrayList;
import java.util.List;

public class ChessGameService {

    private static final char FIRST_ROW_INDEX = 'a';
    private static final int MAX_COLUMN_COUNT = 8;
    private static final int MAX_ROW_COUNT = 8;
    private static final int INDEX_PREFIX = 1;

    private final ChessGameDao chessGameDao;
    private final PieceDao pieceDao;

    public ChessGameService(ChessGameDao chessGameDao, PieceDao pieceDao) {
        this.chessGameDao = chessGameDao;
        this.pieceDao = pieceDao;
    }

    public void saveChessGame(BoardDto boardDto) {
        updateGame(boardDto.getTurn());
        String convertedBoard = boardDto.toString().replaceAll(System.lineSeparator(), "");
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

    public Board loadGame() {
        ChessGameDto chessGameDto = chessGameDao.findLastGame();
        List<PieceDto> pieceDtos = pieceDao.findAllPieceByGameId(chessGameDto.id());
        char[][] boardArray = new char[MAX_ROW_COUNT][MAX_COLUMN_COUNT];
        for (PieceDto pieceDto : pieceDtos) {
            int rowIndex = Integer.parseInt(pieceDto.positionRow()) - INDEX_PREFIX;
            int columnIndex = pieceDto.positionColumn().charAt(0) - FIRST_ROW_INDEX;
            boardArray[rowIndex][columnIndex] = pieceDto.pieceAppearance().charAt(0);
        }
        List<String> customBoard = convertBoard(boardArray);
        return Board.createCustomBoard(customBoard, chessGameDto.turn());
    }

    private List<String> convertBoard(char[][] chessBoard) {
        List<String> customBoard = new ArrayList<>();
        for (char[] row : chessBoard) {
            String convertedRow = String.valueOf(row);
            customBoard.add(convertedRow);
        }
        return customBoard;
    }
}
