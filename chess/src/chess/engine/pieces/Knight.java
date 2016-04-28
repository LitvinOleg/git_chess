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

import static chess.engine.board.Move.*;

/**
 *  This class is for knight(horse) piece
 */
public class Knight extends Piece {
    private static final int[] CANDIDATE_MOVE_COORDINATE = {-17, -15, -10, -6, 6, 10, 15, 17};

    public Knight(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance, PieceType.KNIGHT, true);
    }

    public Knight(int piecePosition, Alliance pieceAlliance, boolean isFirstMove) {
        super(piecePosition, pieceAlliance, PieceType.KNIGHT, isFirstMove);
    }

    /**
     * Calculates the legal moves that current knight can make
     *
     * @param board - game board object
     * @return - list of legal moves
     */
    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        List<Move> legalMoves = new ArrayList<Move>();

        for (int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {
            int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;

            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                if (isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isSecondColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isSeventhColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isEighthColumnExclusion(this.piecePosition, currentCandidateOffset))
                    continue;

                Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

                if (!candidateDestinationTile.isTileOccupied()) // Empty tile
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate)); // non-attacking move
                else {
                    Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                    if (this.pieceAlliance != pieceAlliance) // Enemy piece
                        legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination)); // attacking move
                }
            }
        }

        return Collections.unmodifiableList(legalMoves);
    }

    private static boolean isFirstColumnExclusion(int currentPosition, int candidatePosition) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidatePosition == -17 || candidatePosition == -10 || candidatePosition == 6 || candidatePosition == 15);
    }
    private static boolean isSecondColumnExclusion(int currentPosition, int candidatePosition) {
        return BoardUtils.SECOND_COLUMN[currentPosition] && (candidatePosition == -10 || candidatePosition == 6);
    }
    private static boolean isSeventhColumnExclusion(int currentPosition, int candidatePosition) {
        return BoardUtils.SEVENTH_COLUMN[currentPosition] && (candidatePosition == -6 || candidatePosition == 10);
    }
    private static boolean isEighthColumnExclusion(int currentPosition, int candidatePosition) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidatePosition == -15 || candidatePosition == -6 || candidatePosition == 10 || candidatePosition == 17);
    }

    /**
     * Moves the knight on a new position on the chess board
     *
     * @param move of chosen piece
     * @return new piece
     */
    @Override
    public Knight movePiece(Move move) {
        return new Knight(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    @Override
    public String toString() {
        return PieceType.KNIGHT.toString();
    }
}
