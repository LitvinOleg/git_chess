package engine.board;

import engine.Alliance;
import engine.pieces.*;
import engine.player.BlackPlayer;
import engine.player.Player;
import engine.player.WhitePlayer;

import java.util.*;

/**
 * Created by Олег on 17.03.2016.
 */
public class Board {
    private List<Tile> gameBoard;
    private Collection<Piece> whitePieces;
    private Collection<Piece> blackPieces;

    private WhitePlayer whitePlayer;
    private BlackPlayer blackPlayer;
    private Player currentPlayer;

    private Board(Builder builder) {
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(this.gameBoard, Alliance.WHITE);
        this.blackPieces = calculateActivePieces(this.gameBoard, Alliance.BLACK);

        Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
        Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);

        this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.blackPlayer = new BlackPlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);
    }

    public Collection<Piece> getBlackPieces() {
        return this.blackPieces;
    }
    public Collection<Piece> getWhitePieces() {
        return this.whitePieces;
    }

    public Player getWhitePlayer() { return whitePlayer; }
    public Player getBlackPlayer() { return blackPlayer; }
    public Player getCurrentPlayer() { return currentPlayer; }

    /**
     * The method calculates legal moves for a given alliance pieces
     * @param pieces of given alliance
     * @return list of legal moves
     */
    private Collection<Move> calculateLegalMoves(Collection<Piece> pieces) {
        List<Move> legalMoves = new ArrayList<>();

        for (Piece piece : pieces)
            legalMoves.addAll(piece.calculateLegalMoves(this)); // add legal moves of current piece

        return Collections.unmodifiableList(legalMoves);
    }

    /**
     * The method calculate the amount of active pieces that have the same colour
     * @param gameBoard - our chess board
     * @param alliance - colour of pieces
     * @return - list of pieces that have the same colour
     */
    private static Collection<Piece> calculateActivePieces(List<Tile> gameBoard, Alliance alliance) {
        List<Piece> activePieces = new ArrayList<>();

        for (Tile tile : gameBoard)
            if (tile.isTileOccupied()) {
                Piece piece = tile.getPiece();

                if (piece.getPieceAlliance() == alliance)
                    activePieces.add(piece);
            }

        return Collections.unmodifiableList(activePieces);
    }

    /**
     * The method returns a tile on the tileCoordinate
     * @param tileCoordinate
     * @return tile on the tileCoordinate
     */
    public Tile getTile(int tileCoordinate) {
        return gameBoard.get(tileCoordinate);
    }

    /**
     *  The method creates chess board using the builder
     * @param builder - our chess board builder
     * @return chess board list
     */
    private static List<Tile> createGameBoard(Builder builder) {
        List<Tile> tiles = new ArrayList<>();

        for (int i=0;i<BoardUtils.NUM_TILES;i++)
            tiles.add(Tile.createTile(i, builder.boardConfig.get(i)));

        return tiles;
    }

    /**
     * The method creates standard chess board
     * @return new game board
     */
    public static Board createStandartdBoard() {
        Builder builder = new Builder();
        //Black layout
        builder.setPiece(new Rook(0, Alliance.BLACK));
        builder.setPiece(new Knight(1, Alliance.BLACK));
        builder.setPiece(new Bishop(2, Alliance.BLACK));
        builder.setPiece(new Queen(3, Alliance.BLACK));
        builder.setPiece(new King(4, Alliance.BLACK));
        builder.setPiece(new Bishop(5, Alliance.BLACK));
        builder.setPiece(new Knight(6, Alliance.BLACK));
        builder.setPiece(new Rook(7, Alliance.BLACK));
        builder.setPiece(new Pawn(8, Alliance.BLACK));
        builder.setPiece(new Pawn(9, Alliance.BLACK));
        builder.setPiece(new Pawn(10, Alliance.BLACK));
        builder.setPiece(new Pawn(11, Alliance.BLACK));
        builder.setPiece(new Pawn(12, Alliance.BLACK));
        builder.setPiece(new Pawn(13, Alliance.BLACK));
        builder.setPiece(new Pawn(14, Alliance.BLACK));
        builder.setPiece(new Pawn(15, Alliance.BLACK));
        //White layout
        builder.setPiece(new Pawn(48, Alliance.WHITE));
        builder.setPiece(new Pawn(49, Alliance.WHITE));
        builder.setPiece(new Pawn(50, Alliance.WHITE));
        builder.setPiece(new Pawn(51, Alliance.WHITE));
        builder.setPiece(new Pawn(52, Alliance.WHITE));
        builder.setPiece(new Pawn(53, Alliance.WHITE));
        builder.setPiece(new Pawn(54, Alliance.WHITE));
        builder.setPiece(new Pawn(55, Alliance.WHITE));
        builder.setPiece(new Rook(56, Alliance.WHITE));
        builder.setPiece(new Knight(57, Alliance.WHITE));
        builder.setPiece(new Bishop(58, Alliance.WHITE));
        builder.setPiece(new Queen(59, Alliance.WHITE));
        builder.setPiece(new King(60, Alliance.WHITE));
        builder.setPiece(new Bishop(61, Alliance.WHITE));
        builder.setPiece(new Knight(62, Alliance.WHITE));
        builder.setPiece(new Rook(63, Alliance.WHITE));

        builder.setMoveMaker(Alliance.WHITE);

        return builder.build();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int i=0;i<BoardUtils.NUM_TILES;i++) {
            String tileText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s", tileText));

            if ((i+1) % BoardUtils.NUM_TILES_PER_ROW == 0)
                builder.append("\n");
        }

        return builder.toString();
    }

    //TODO check something here!
    public Collection<Move> getAllLegalMoves() {
        List<Move> list = new ArrayList<>();
        list.addAll(this.whitePlayer.getLegalMoves());
        list.addAll(this.blackPlayer.getLegalMoves());
        return list;
    }

    /**
     * Builder pattern class for building our chess board
     */
    public static class Builder {

        Map<Integer, Piece> boardConfig;
        Alliance nextMoveMaker;
        Pawn enPassantPawn;

        public Builder() {
            this.boardConfig = new HashMap<>();
        }

        /**
         *
         * @param piece - this piece we put on our chess board
         * @return builder
         */
        public Builder setPiece(Piece piece) {
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }

        /**
         * The method says who is going to do next move
         * @param nextMoveMaker colour of chess, who isgoing to do next move
         * @return builder
         */
        public Builder setMoveMaker(Alliance nextMoveMaker) {
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }

        public Board build() {
            return new Board(this);
        }

        public void setEnPassantPawn(Pawn enPassantPawn) {
            this.enPassantPawn = enPassantPawn;
        }
    }
}
