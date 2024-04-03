package chess.controller;

import chess.dao.ChessGameConnector;
import chess.dao.ChessGameDao;
import chess.dao.PieceDao;
import chess.service.ChessGameService;
import chess.view.Command;
import chess.view.InputView;
import chess.view.OutputView;
import java.util.List;

public final class ChessGameController {

    private static final int COMMAND_INDEX = 0;
    private static final int SOURCE_INDEX = 1;
    private static final int TARGET_INDEX = 2;
    private static final int WHITE_TEAM_SCORE_INDEX = 0;
    private static final int BLACK_TEAM_SCORE_INDEX = 1;

    private final ChessGameService chessGameService;

    public ChessGameController() {
        ChessGameConnector chessGameConnector = new ChessGameConnector();
        ChessGameDao chessGameDao = new ChessGameDao(chessGameConnector);
        PieceDao pieceDao = new PieceDao(chessGameConnector);
        this.chessGameService = new ChessGameService(chessGameDao, pieceDao);
    }

    public void run() {
        InputView.printGameIntro();
        do {
            executeGame();
        } while (chessGameService.canContinueChessGame());
    }

    private void executeGame() {
        retryOnException(this::executeCommand);
    }

    private void executeCommand() {
        List<String> commands = InputView.askGameCommands();
        Command command = Command.findCommand(commands.get(COMMAND_INDEX));
        boolean isGameCanContinue = chessGameService.canContinueChessGame();
        if (command.isStart()) {
            executeStart();
        }
        if (command.isEnd()) {
            executeEnd();
        }
        if (command.isMove() && isGameCanContinue) {
            executeMove(commands);
        }
        if (command.isStatus() && isGameCanContinue) {
            executeStatus();
        }
        if (command.isSave() && isGameCanContinue) {
            executeSave();
        }
        if (command.isLoad()) {
            executeLoad();
        }
    }

    private void executeStart() {
        chessGameService.startChessGame();
        OutputView.printChessBoard(chessGameService.getChessBoard());
    }

    private void executeEnd() {
        chessGameService.endChessGame();
    }

    private void executeMove(List<String> commands) {
        String source = commands.get(SOURCE_INDEX);
        String target = commands.get(TARGET_INDEX);

        chessGameService.moveChessPiece(source, target);
        OutputView.printChessBoard(chessGameService.getChessBoard());

        if (chessGameService.judgeGameEnd()) {
            OutputView.printWinnerTeam(chessGameService.getWinnerTeam());
        }
    }

    private void executeStatus() {
        List<Double> allTeamScore = chessGameService.calculateAllTeamScore();
        double whiteTeamScore = allTeamScore.get(WHITE_TEAM_SCORE_INDEX);
        double blackTeamScore = allTeamScore.get(BLACK_TEAM_SCORE_INDEX);

        OutputView.printGameScore(whiteTeamScore, blackTeamScore);
        OutputView.printDominatingTeam(whiteTeamScore, blackTeamScore);
    }

    private void executeSave() {
        chessGameService.saveChessGame();
    }

    private void executeLoad() {
        chessGameService.loadGame();
        OutputView.printChessBoard(chessGameService.getChessBoard());
    }

    private void retryOnException(Runnable operation) {
        try {
            operation.run();
        } catch (IllegalArgumentException e) {
            OutputView.printException(e);
            retryOnException(operation);
        }
    }
}
