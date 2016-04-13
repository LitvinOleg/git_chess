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
public class WhitePlayer extends Player {

    public WhitePlayer(Board board, Collection<Move> whiteStandardLegalMoves, Collection<Move> blackStandardLegalMoves) {
        super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }
    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }
    @Override
    public Player getOpponent() {
        return this.board.getBlackPlayer();
    }

    /**
     * Castle move for white player king
     * @param playerLegalMoves - list of white player legal moves
     * @param opponentLegalMoves - list of black player legal moves
     * @return list of castle moves for white king
     */
    @Override
    protected Collection<Move> calculateKingCastles(Collection<Move> playerLegalMoves, Collection<Move> opponentLegalMoves) {
        List<Move> kingCastles = new ArrayList<>();
        if (this.playerKing.isFirstMove() && !this.isInCheck()) {
            // white king side castle
            if (!this.board.getTile(61).isTileOccupied() && !this.board.getTile(62).isTileOccupied()) {
                Tile rookTile = this.board.getTile(63);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove())
                    if (Player.calculateAttacksOnTile(61, opponentLegalMoves).isEmpty() &&
                            Player.calculateAttacksOnTile(62, opponentLegalMoves).isEmpty() &&
                            rookTile.getPiece().getPieceType() == Piece.PieceType.ROOK)
                        kingCastles.add(new KingSideCastleMove(this.board, this.playerKing, 62, (Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 61));
            }
            if (!this.board.getTile(59).isTileOccupied() &&
                    !this.board.getTile(58).isTileOccupied() &&
                    !this.board.getTile(57).isTileOccupied()) {
                Tile rookTile = this.board.getTile(56);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() &&
                    Player.calculateAttacksOnTile(58, opponentLegalMoves).isEmpty() &&
                    Player.calculateAttacksOnTile(59, opponentLegalMoves).isEmpty() &&
                    rookTile.getPiece().getPieceType() == Piece.PieceType.ROOK)
                    kingCastles.add(new QueenSideCastleMove(this.board, this.playerKing, 58, (Rook)rookTile.getPiece(), rookTile.getTileCoordinate(), 59));
            }
        }
        return kingCastles;
    }
}
