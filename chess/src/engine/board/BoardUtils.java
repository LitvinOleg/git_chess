package engine.board;

/**
 * Utils for board(game)
 */
public class BoardUtils {
    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGHTH_COLUMN = initColumn(7);

    public static final boolean[] SECOND_ROW = initRow(1);
    public static final boolean[] SEVENTH_ROW = initRow(6);

    public static final int NUM_TILES = 64;
    public static final int NUM_TILES_PER_ROW = 8;

    private BoardUtils() {
        throw new RuntimeException("Cannot create object of this class");
    }

    /**
     * @param columnNumber
     * @return an array of boolean where elements = true on columnNumber coordinates
     */
    private static boolean[] initColumn(int columnNumber) {
        boolean column[] = new boolean[NUM_TILES];

        do {
            column[columnNumber] = true;
            columnNumber += NUM_TILES_PER_ROW;
        } while (columnNumber < NUM_TILES);

        return column;
    }

    /**
     * @param rowNumber
     * @return an array of boolean where elements = true from rowNumber coordinates to rowNumber + 8 coordinates
     */
    private static boolean[] initRow(int rowNumber) {
        boolean row[] = new boolean[NUM_TILES];

        do {
            row[rowNumber] = true;
            rowNumber++;
        } while (rowNumber % NUM_TILES_PER_ROW !=0);

        return row;
    }

    /**
     * @param coordinate of candidate piece
     * @return true if piece on the board else false
     */
    public static boolean isValidTileCoordinate(int coordinate) {
        return coordinate >= 0 && coordinate < NUM_TILES;
    }
}