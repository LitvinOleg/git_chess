package engine.player;

import engine.Alliance;
import engine.board.Board;
import engine.board.Move;
import engine.board.Tile;
import engine.pieces.Piece;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Олег on 29.03.2016.
 */
public class BlackPlayer extends Player {

    public BlackPlayer(Board board, Collection<Move> whiteStandardLegalMoves, Collection<Move> blackStandardLegalMoves) {
        super(board, blackStandardLegalMoves, whiteStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }

    @Override
    public Player getOpponent() {
        return this.board.getWhitePlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(Collection<Move> playerLegalMoves, Collection<Move> opponentLegalMoves)
    {
        List<Move> kingCastles = new ArrayList<>();

        if (this.playerKing.isFirstMove() && !this.isInCheck()) {
            // black king side castle
            if (!this.board.getTile(5).isTileOccupied() && !this.board.getTile(6).isTileOccupied()) {
                Tile rookTile = this.board.getTile(7);

                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove())
                    if (Player.calculateAttacksOnTile(5, opponentLegalMoves).isEmpty() &&
                            Player.calculateAttacksOnTile(6, opponentLegalMoves).isEmpty() &&
                            rookTile.getPiece().getPieceType() == Piece.PieceType.ROOK) {
                        //TODO add a castle move!
                        kingCastles.add(null);
                    }
            }

            if (!this.board.getTile(1).isTileOccupied() &&
                    !this.board.getTile(2).isTileOccupied() &&
                    !this.board.getTile(3).isTileOccupied()) {
                Tile rookTile = this.board.getTile(0);

                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove())
                    //TODO add a castle move!
                    kingCastles.add(null);
            }
        }

        return kingCastles;
    }
}
