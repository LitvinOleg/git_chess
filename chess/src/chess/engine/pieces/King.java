package chess.engine.pieces;

import chess.engine.Alliance;
import chess.engine.board.Board;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move;
import chess.engine.board.Tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Олег on 24.03.2016.
 */
public class King extends Piece {
    private final static int[] CANDIDATE_MOVE_COORDINATE = {-9, -8, -7, -1, 1, 7, 8, 9};

    public King(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance, PieceType.KING, true);
    }

    public King(int piecePosition, Alliance pieceAlliance, boolean isFirstMove) {
        super(piecePosition, pieceAlliance, PieceType.KING, isFirstMove);
    }

    /**
     * Calculates the legal moves that king can make.
     *
     * @param board - game board object
     * @return - list of legal moves
     */
    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        List<Move> legalMoves = new ArrayList<>();

        for (int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {
            int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;

            if (isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) || isEighthColumnExclusion(this.piecePosition, currentCandidateOffset))
                continue;

            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

                if (!candidateDestinationTile.isTileOccupied()) // Empty tile
                    legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate)); // non-attacking move
                else {
                    Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                    if (this.pieceAlliance != pieceAlliance) // Enemy piece
                        legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination)); // attacking move
                }
            }
        }

        return Collections.unmodifiableList(legalMoves);
    }

    private static boolean isFirstColumnExclusion(int currentPosition, int candidatePosition) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidatePosition == -9 || candidatePosition == -1 || candidatePosition == 7);
    }

    private static boolean isEighthColumnExclusion(int currentPosition, int candidatePosition) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidatePosition == 9 || candidatePosition == 1 || candidatePosition == -7);
    }

    /**
     * Moves the king on a new position on the chess board
     *
     * @param move of chosen piece
     * @return new piece
     */
    @Override
    public King movePiece(Move move) {
        return new King(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    @Override
    public String toString() { return PieceType.KING.toString(); }
}
