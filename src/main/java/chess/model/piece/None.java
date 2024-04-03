package chess.model.piece;

import chess.model.position.Position;
import java.util.List;

public class None extends Piece {

    private static final double SCORE = 0;

    protected None(PieceType pieceType) {
        super(pieceType, Color.NONE);
    }

    @Override
    public boolean canMove(Position source, Position target) {
        return false;
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
