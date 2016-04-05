package engine.player;

import engine.board.Board;
import engine.board.Move;

/**
 * Represents the transition between one board and another
 */
public class MoveTransition {

    private Move move;
    private MoveStatus moveStatus;
    private Board transitionBoard;

    public MoveTransition(Move move,
                          MoveStatus moveStatus,
                          Board transitionBoard) {
        this.move = move;
        this.moveStatus = moveStatus;
        this.transitionBoard = transitionBoard;
    }

    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }
}
