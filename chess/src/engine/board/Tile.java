package engine.board;

import engine.pieces.Piece;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *  Chess board consists of 64 tiles
 */
public abstract class Tile {
    private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();

    /**
     * @return a map, which consists of 64 empty tiles
     */
    private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {
        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();

        for (int i = 0; i < BoardUtils.NUM_TILES; i++)
            emptyTileMap.put(i, new EmptyTile(i));

        return Collections.unmodifiableMap(emptyTileMap); // ImmutableTile
    }

    public static Tile createTile(int tileCoordinate, Piece piece) {
        return piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES_CACHE.get(tileCoordinate);
    }

    private final int tileCoordinate;

    private Tile(int tileCoordinate) {
        this.tileCoordinate = tileCoordinate;
    }

    public abstract boolean isTileOccupied();

    public abstract Piece getPiece();

    public int getTileCoordinate() {
        return tileCoordinate;
    }

    /**
     * Our class for empty tile
     */
    public static final class EmptyTile extends Tile {
        private EmptyTile(int tileCoordinate) {
            super(tileCoordinate);
        }

        @Override
        public boolean isTileOccupied() {
            return false;
        }
        @Override
        public Piece getPiece() {
            return null;
        }
        @Override
        public String toString() {
            return "-";
        }
    }

    /**
     * Our class for ocupied tile
     */
    public static final class OccupiedTile extends Tile {
        private final Piece pieceOnTile;

        private OccupiedTile(int tileCoordinate, Piece pieceOnTile) {
            super(tileCoordinate);
            this.pieceOnTile = pieceOnTile;
        }

        @Override
        public boolean isTileOccupied() {
            return true;
        }
        @Override
        public Piece getPiece() {
            return this.pieceOnTile;
        }
        @Override
        public String toString() { return getPiece().getPieceAlliance().isBlack() ? getPiece().toString().toLowerCase() : getPiece().toString(); }
    }
}
