package chess.engine.player;

import chess.engine.Alliance;
import chess.engine.board.Board;
import chess.engine.board.Move;
import chess.engine.board.Tile;
import chess.engine.pieces.Piece;
import chess.engine.pieces.Rook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static chess.engine.board.Move.*;

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

    /**
     * Castle move for black player king
     * @param playerLegalMoves - list of black player legal moves
     * @param opponentLegalMoves - list of white player legal moves
     * @return list of castle moves for black king
     */
    @Override
    protected Collection<Move> calculateKingCastles(Collection<Move> playerLegalMoves, Collection<Move> opponentLegalMoves) {
        List<Move> kingCastles = new ArrayList<>();
        if (this.playerKing.isFirstMove() && !this.isInCheck()) {
            // black king side castle
            if (!this.board.getTile(5).isTileOccupied() && !this.board.getTile(6).isTileOccupied()) {
                Tile rookTile = this.board.getTile(7);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove())
                    if (Player.calculateAttacksOnTile(5, opponentLegalMoves).isEmpty() &&
                            Player.calculateAttacksOnTile(6, opponentLegalMoves).isEmpty() &&
                            rookTile.getPiece().getPieceType() == Piece.PieceType.ROOK)
                        kingCastles.add(new KingSideCastleMove(this.board, this.playerKing, 6, (Rook)rookTile.getPiece(), rookTile.getTileCoordinate(), 5));
            }
            if (!this.board.getTile(1).isTileOccupied() &&
                    !this.board.getTile(2).isTileOccupied() &&
                    !this.board.getTile(3).isTileOccupied()) {
                Tile rookTile = this.board.getTile(0);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() &&
                    Player.calculateAttacksOnTile(2, opponentLegalMoves).isEmpty() &&
                    Player.calculateAttacksOnTile(3, opponentLegalMoves).isEmpty() &&
                    rookTile.getPiece().getPieceType() == Piece.PieceType.ROOK)
                    kingCastles.add(new QueenSideCastleMove(this.board, this.playerKing, 2, (Rook)rookTile.getPiece(), rookTile.getTileCoordinate(), 3 ));
            }
        }
        return kingCastles;
    }
}
