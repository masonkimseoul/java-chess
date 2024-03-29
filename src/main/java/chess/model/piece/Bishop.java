package chess.model.piece;

import static chess.model.position.Direction.DOWN_LEFT;
import static chess.model.position.Direction.DOWN_RIGHT;
import static chess.model.position.Direction.UP_LEFT;
import static chess.model.position.Direction.UP_RIGHT;

import chess.model.position.Direction;
import chess.model.position.Position;

public class Bishop extends Piece {

    private static final double SCORE = 3;

    public Bishop(PieceType pieceType, Color color) {
        super(pieceType, color);
    }

    @Override
    public boolean canMove(Position source, Position target) {
        Direction direction = Direction.findDirection(source, target);
        return direction == UP_LEFT || direction == DOWN_LEFT || direction == UP_RIGHT || direction == DOWN_RIGHT;
    }

    @Override
    public boolean canAttack(Position source, Position target) {
        return canMove(source, target);
    }

    @Override
    public boolean canJump() {
        return false;
    }

    @Override
    public boolean lostGoal() {
        return false;
    }

    @Override
    public double getScore(boolean isDuplicate) {
        return SCORE;
    }
}
