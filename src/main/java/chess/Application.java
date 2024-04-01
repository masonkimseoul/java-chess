package chess;

import chess.controller.ChessGameController;

public final class Application {

    public static void main(String[] args) {
        ChessGameController chessGameController = new ChessGameController();
        chessGameController.run();
    }
}
