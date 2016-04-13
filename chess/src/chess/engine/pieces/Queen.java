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
 * Created by Олег on 22.03.2016.
 */
public class Queen extends Piece {
    private static final int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-9, -8, -7, -1, 1, 7, 8, 9};

    public Queen(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance, PieceType.QUEEN);
    }

    /**
     * Calculates the legal moves that queen can make
     *
     * @param board - game board object
     * @return - list of legal moves
     */
    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        List<Move> legalMoves = new ArrayList<>();

        for (int currentCandidateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES) {
            int candidateDestinationCoordinate = this.piecePosition;

            while (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                if (isFirstColumnExclusion(candidateDestinationCoordinate, currentCandidateOffset) ||
                        isEighthColumnExclusion(candidateDestinationCoordinate, currentCandidateOffset))
                    break;

                candidateDestinationCoordinate += currentCandidateOffset;

                if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                    Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

                    if (!candidateDestinationTile.isTileOccupied()) // Empty tile
                        legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                    else {
                        Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                        if (this.getPieceAlliance() != pieceAlliance) // Enemy piece
                            legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));

                        break; // obstacle piece
                    }
                }
            }
        }

        return Collections.unmodifiableList(legalMoves);
    }

    private static boolean isFirstColumnExclusion(int currentPosition, int candidatePosition) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidatePosition == -9 || candidatePosition == 7 || candidatePosition == -1);
    }

    private static boolean isEighthColumnExclusion(int currentPosition, int candidatePosition) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidatePosition == -7 || candidatePosition == 9 || candidatePosition == 1);
    }

    /**
     * Moves the queen on a new position on the chess board
     *
     * @param move of chosen piece
     * @return new piece
     */
    @Override
    public Queen movePiece(Move move) {
        return new Queen(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    @Override
    public String toString() {
        return PieceType.QUEEN.toString();
    }
}
