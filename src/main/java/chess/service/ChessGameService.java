package chess.service;

import chess.dao.ChessGameDao;
import chess.dao.PieceDao;
import chess.dto.BoardDto;
import chess.dto.ChessGameDto;
import chess.dto.PieceDto;
import chess.model.Board;
import chess.model.piece.Color;
import chess.model.piece.Piece;
import chess.model.piece.PieceType;
import java.util.ArrayList;
import java.util.List;

public class ChessGameService {

    private static final char FIRST_ROW_INDEX = 'a';
    private static final int MAX_COLUMN_COUNT = 8;

    private final ChessGameDao chessGameDao;
    private final PieceDao pieceDao;

    public ChessGameService(ChessGameDao chessGameDao, PieceDao pieceDao) {
        this.chessGameDao = chessGameDao;
        this.pieceDao = pieceDao;
    }

    public void saveChessGame(BoardDto boardDto) {
        updateGame(boardDto.getTurn());
        String convertedBoard = boardDto.toString();
        for (int i = 0; i < 8; i++) {
            String pieceRow = String.valueOf(i);
            createPieceInOneRow(convertedBoard, pieceRow, i);
        }
    }

    private void createPieceInOneRow(String convertedBoard, String pieceRow, int rowIndex) {
        ChessGameDto chessGameDto = chessGameDao.findLastGame();
        for (int j = 0; j < 8; j++) {
            int pieceIndex = rowIndex * MAX_COLUMN_COUNT + j;
            String pieceColumn = String.valueOf(FIRST_ROW_INDEX + j);
            String pieceAppearance = String.valueOf(convertedBoard.charAt(pieceIndex));
            PieceType pieceType = PieceType.findPieceTypeByName(pieceAppearance);
            Color color = Color.findColorByName(pieceAppearance);
            Piece piece = Piece.from(pieceType, color);
            pieceDao.create(new PieceDto(chessGameDto.id(), piece, pieceColumn, pieceRow));
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
        char [][] boardArray = new char[8][8];
        for (PieceDto pieceDto : pieceDtos) {
            int rowIndex = Integer.parseInt(pieceDto.positionRow()) - 1;
            int columnIndex = pieceDto.positionColumn().charAt(0) - FIRST_ROW_INDEX;
            boardArray[rowIndex][columnIndex] = pieceDto.piece().toString().charAt(0);
        }
        List<String> customBoard = convertBoard(boardArray);
        return Board.createCustomBoard(customBoard, chessGameDto.turn());
    }

    public static List<String> convertBoard(char[][] chessBoard) {
        List<String> customBoard = new ArrayList<>();
        for (char[] row : chessBoard) {
            String rowAsString = new String(row);
            customBoard.add(rowAsString);
        }
        return customBoard;
    }
}