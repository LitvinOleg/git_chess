package engine.player;

import engine.Alliance;
import engine.board.Board;
import engine.board.Move;
import engine.pieces.King;
import engine.pieces.Piece;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static engine.pieces.Piece.*;

/**
 * Created by Олег on 29.03.2016.
 */
public abstract class Player {

    protected Board board;
    protected King playerKing;
    protected Collection<Move> legalMoves;
    private boolean isInCheck;

    public Player(Board board, Collection<Move> legalMoves, Collection<Move> opponentLegalMoves) {
        this.board = board;
        this.playerKing = establishKing();
        this.legalMoves = legalMoves;
        this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentLegalMoves).isEmpty();
    }

    public King getPlayerKing() { return this.playerKing; }
    public Collection<Move> getLegalMoves() { return this.legalMoves; }

    /**
     * Calculates all attacking moves which can be done on piecePosition by the opponent
     * @param piecePosition - position of checked piece
     * @param opponentLegalMoves - opponent moves list
     * @return list of attacking moves
     */
    public static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> opponentLegalMoves) {
        List<Move> attackMoves = new ArrayList<>();

        for (Move move : opponentLegalMoves)
            if (piecePosition == move.getDestinationCoordinate())
                attackMoves.add(move);

        return attackMoves;
    }

    /**
     * The method establishes the king piece for the player
     * @return king piece for the player
     */
    private King establishKing() {
        for (Piece piece : getActivePieces())
            if (piece.getPieceType() == PieceType.KING)
                return (King) piece;
        throw new RuntimeException("Not a valid board!");
    }

    public boolean isMoveLegal(Move move) {
        return this.legalMoves.contains(move);
    }
    public boolean isInCheck() { return this.isInCheck; }
    public boolean isInCheckMate() { return this.isInCheck && !hasEscapeMoves(); }
    public boolean isInStaleMate() { return !this.isInCheck && !hasEscapeMoves(); }

    protected boolean hasEscapeMoves() {
        for (Move move : legalMoves) {
            MoveTransition transition = makeMove(move);
            if (transition.getMoveStatus().isDone())
                return true;
        }
        return false;
    }

    public boolean isCastled() { return false; }

    public MoveTransition makeMove(Move move) {
        if (!isMoveLegal(move))
            return new MoveTransition(move, MoveStatus.ILLEGAL_MOVE, this.board);

        Board transitionBoard = move.execute();

        //Calculates the attacks on the tile for the current player opponent's king
        Collection<Move> kingAttacks = Player.calculateAttacksOnTile(transitionBoard.getCurrentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                                                                     transitionBoard.getCurrentPlayer().getLegalMoves());
        if (!kingAttacks.isEmpty())
            return new MoveTransition(move, MoveStatus.LEAVES_PLAYER_IN_CHECK, this.board);

        return new MoveTransition(move, MoveStatus.DONE, transitionBoard);
    }

    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();
    protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegalMoves, Collection<Move> opponentLegalMoves);
}
