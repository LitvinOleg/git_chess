package chess.engine.pieces;

import chess.engine.Alliance;
import chess.engine.board.Board;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static chess.engine.board.Move.*;

/**
 * Created by Олег on 23.03.2016.
 */
public class Pawn extends Piece
{
    private final static int[] CANDIDATE_MOVE_COORDINATE = {7, 8, 9, 16};

    public Pawn(int piecePosition, Alliance pieceAlliance)
    {
        super(piecePosition, pieceAlliance, PieceType.PAWN);
    }

    /**
     * Calculates the legal moves that current pawn can make
     *
     * @param board - game board object
     * @return - list of legal moves
     */
    @Override
    public Collection<Move> calculateLegalMoves(Board board)
    {
        List<Move> legalMoves = new ArrayList<>();

        for (int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {
            int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset * this.pieceAlliance.getDirection();

            if (!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate))
                continue;

            if (currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied())
                //TODO more work here!!!
                legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
            else if (currentCandidateOffset == 16 && this.isFirstMove() &&
                    (BoardUtils.SEVENTH_RANK[this.piecePosition] && this.pieceAlliance.isBlack()) ||
                    (BoardUtils.SECOND_RANK[this.piecePosition] && this.pieceAlliance.isWhite())) {
                int behindCandidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * 8);

                if (!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() && !board.getTile(candidateDestinationCoordinate).isTileOccupied())
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
            } else if (currentCandidateOffset == 7 && !((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) ||
                    (BoardUtils.FIRST_COLUMN[this.piecePosition]) && this.pieceAlliance.isBlack())) {
                if (board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    Piece pieceAtDestination = board.getTile(candidateDestinationCoordinate).getPiece();

                    if (this.pieceAlliance != pieceAtDestination.getPieceAlliance())
                        //TODO more work here!!!
                        legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                }
            } else if (currentCandidateOffset == 9 && !((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()) ||
                    (BoardUtils.FIRST_COLUMN[this.piecePosition]) && this.pieceAlliance.isWhite())) {
                if (board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    Piece pieceAtDestination = board.getTile(candidateDestinationCoordinate).getPiece();

                    if (this.pieceAlliance != pieceAtDestination.getPieceAlliance())
                        //TODO more work here!!!
                        legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                }
            }
        }

        return Collections.unmodifiableList(legalMoves);
    }

    /**
     * Moves the pawn on a new position on the chess board
     *
     * @param move of chosen piece
     * @return new piece
     */
    @Override
    public Pawn movePiece(Move move)
    {
        return new Pawn(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    @Override
    public String toString()
    {
        return PieceType.PAWN.toString();
    }
}