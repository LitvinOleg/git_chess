package chess.engine.pieces;

import chess.engine.Alliance;
import chess.engine.board.Board;
import chess.engine.board.Move;

import java.util.Collection;

/**
 * This class is for chess piece
 */
public abstract class Piece {

    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final PieceType pieceType;
    protected final boolean isFirstMove;
    private final int cachedHashCode;

    public Piece(int piecePosition, Alliance pieceAlliance, PieceType pieceType) {
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        this.pieceType = pieceType;
        //TODO more work here!!!
        this.isFirstMove = true;
        this.cachedHashCode = calculateHashCode();
    }

    /**
     * @return - the alliance of current piece
     */
    public Alliance getPieceAlliance() {
        return this.pieceAlliance;
    }

    /**
     * The method calculates which move can do a piece in a certain situation.
     *
     * @param board - game board object
     * @return - list of legal moves
     */
    public abstract Collection<Move> calculateLegalMoves(Board board);

    /**
     * Moves the piece on a new position on the chess board
     *
     * @param move of chosen piece
     * @return new piece
     */
    public abstract Piece movePiece(Move move);

    /**
     * @return - true if isFirstMove else false
     */
    public boolean isFirstMove() {
        return this.isFirstMove;
    }
    public int getPiecePosition() {
        return piecePosition;
    }
    public PieceType getPieceType() { return pieceType;}

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        Piece piece;
        if (obj instanceof Piece)
            piece = (Piece) obj;
        else
            return false;

        return (pieceAlliance == piece.getPieceAlliance() && pieceType == piece.getPieceType() &&
                piecePosition == piece.getPiecePosition() && isFirstMove == piece.isFirstMove());
    }

    private int calculateHashCode() {
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (isFirstMove ? 1:0);
        return result;
    }

    @Override
    public int hashCode() {
        return this.cachedHashCode;
    }

    /**
     * Piece type enums
     */
    public enum PieceType {
        PAWN("P"),
        KNIGHT("H"),
        BISHOP("B"),
        ROOK("R"),
        QUEEN("Q"),
        KING("K");


        private String pieceName;

        PieceType(final String pieceName) {
            this.pieceName = pieceName;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }
    }
}
