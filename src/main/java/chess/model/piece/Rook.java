package chess.model.piece;

import static chess.model.position.Direction.DOWN;
import static chess.model.position.Direction.LEFT;
import static chess.model.position.Direction.RIGHT;
import static chess.model.position.Direction.UP;

import chess.model.position.Direction;
import chess.model.position.Position;
import java.util.List;

public class Rook extends Piece {

    private static final double SCORE = 5;

    public Rook(PieceType pieceType, Color color) {
        super(pieceType, color);
    }

    @Override
    public boolean canMove(Position source, Position target) {
        Direction direction = Direction.findDirection(source, target);
        return direction == UP || direction == DOWN || direction == LEFT || direction == RIGHT;
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
    public double getScore(List<Piece> pieces) {
        return SCORE;
    }
}
