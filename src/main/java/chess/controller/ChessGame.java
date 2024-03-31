package chess.controller;

import chess.dto.BoardDto;
import chess.model.Board;
import chess.model.Score;
import chess.model.piece.Color;
import chess.model.piece.Piece;
import chess.model.position.Position;
import chess.view.Command;
import chess.view.InputView;
import chess.view.OutputView;
import java.util.List;
import java.util.function.Supplier;

public final class ChessGame {

    private static final int COMMAND_INDEX = 0;
    private static final int SOURCE_INDEX = 1;
    private static final int TARGET_INDEX = 2;

    public void run() {
        InputView.printGameIntro();
        Board board = Board.createCustomBoard(List.of(""), Color.NONE);
        do {
            board = executeGame(board);
        } while (board.canContinue());
    }

    private Board executeGame(Board board) {
        return retryOnException(() -> executeCommand(board));
    }

    private Board executeCommand(Board board) {
        List<String> commands = InputView.askGameCommands();
        Command command = Command.findCommand(commands.get(COMMAND_INDEX));
        if (command.isEnd()) {
            board.stopGame();
        }
        if (command.isStart()) {
            return executeStart();
        }
        if (command.isMove() && board.canContinue()) {
            executeMove(commands, board);
        }
        if (command.isStatus() && board.canContinue()) {
            executeStatus(board);
        }
        return board;
    }

    private Board executeStart() {
        Board board = Board.createInitialBoard();
        BoardDto boardDto = BoardDto.from(board, board.getTurn());
        OutputView.printChessBoard(boardDto);
        return board;
    }

    private void executeMove(List<String> commands, Board board) {
        String source = commands.get(SOURCE_INDEX);
        String target = commands.get(TARGET_INDEX);
        Piece targetPiece = board.findPiece(Position.from(target));
        board.move(source, target);
        BoardDto boardDto = BoardDto.from(board, board.getTurn());
        OutputView.printChessBoard(boardDto);
        if (targetPiece.lostGoal()) {
            printWinnerIfEnd(targetPiece);
        }
    }

    private void printWinnerIfEnd(Piece targetPiece) {
        if (targetPiece.isEnemy(Color.BLACK)) {
            OutputView.printWinnerTeam(Color.BLACK);
        }
        if (targetPiece.isEnemy(Color.WHITE)) {
            OutputView.printWinnerTeam(Color.WHITE);
        }
    }

    private void executeStatus(Board board) {
        Score score = Score.from(board);
        double whiteTeamScore = score.getScoreByColor(Color.WHITE);
        double blackTeamScore = score.getScoreByColor(Color.BLACK);
        OutputView.printGameScore(whiteTeamScore, blackTeamScore);
        OutputView.printDominatingTeam(whiteTeamScore, blackTeamScore);
    }

    private <T> T retryOnException(Supplier<T> operation) {
        try {
            return operation.get();
        } catch (IllegalArgumentException e) {
            OutputView.printException(e);
            return retryOnException(operation);
        }
    }
}
