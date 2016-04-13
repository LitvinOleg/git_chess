package chess.gui;

import chess.engine.board.Board;
import chess.engine.board.BoardUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created by Олег on 09.04.2016.
 */
public class Table {
    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600);
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);
    private static String defaultPieceImagePath = "src/chess/gui/image/";

    private final Color lightTileColor = Color.decode("#FFFACD");
    private final Color darkTileColor = Color.decode("#593E1A");


    private final Board chessBoard;
    private final BoardPanel boardPanel;
    private final JFrame gameFrame;

    public Table() {
        this.chessBoard = Board.createStandartdBoard();
        this.gameFrame = new JFrame("JChess");
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar = createTableMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.boardPanel = new BoardPanel();
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.setVisible(true);
    }


    private JMenuBar createTableMenuBar() {
        JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        return tableMenuBar;
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");
        JMenuItem loadMenuItem = new JMenuItem("Load");
        loadMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Open up that file");
            }
        });
        fileMenu.add(loadMenuItem);

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);
        return fileMenu;
    }

    //Board panel class
    private class BoardPanel extends JPanel {
        List<TilePanel> boardTiles;

        BoardPanel() {
            super(new GridLayout(8, 8));
            this.boardTiles = new ArrayList<>();
            for (int i=0; i< BoardUtils.NUM_TILES; i++) {
                TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                this.add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            this.validate();
        }
    }

    private class TilePanel extends JPanel {
        private int tileCoordinate;

        TilePanel(BoardPanel boardPanel, int tileCoordinate) {
            super(new GridBagLayout());
            this.tileCoordinate = tileCoordinate;
            this.setPreferredSize(TILE_PANEL_DIMENSION);
            this.assignTileColour();
            this.assignTilePieceIcon(chessBoard);
            this.validate();
        }

        private void assignTileColour() {
            if (BoardUtils.EIGHTH_RANK[this.tileCoordinate] ||
                    BoardUtils.SIXTH_RANK[this.tileCoordinate] ||
                    BoardUtils.FOURTH_RANK[this.tileCoordinate] ||
                    BoardUtils.SECOND_RANK[this.tileCoordinate])
                this.setBackground(this.tileCoordinate % 2 == 0 ? lightTileColor:darkTileColor);
            else if (BoardUtils.SEVENTH_RANK[this.tileCoordinate] ||
                    BoardUtils.FIFTH_RANK[this.tileCoordinate] ||
                    BoardUtils.THIRD_RANK[this.tileCoordinate] ||
                    BoardUtils.FIRST_RANK[this.tileCoordinate])
                this.setBackground(this.tileCoordinate % 2 != 0 ? lightTileColor:darkTileColor);
        }

        private void assignTilePieceIcon(Board board) {
            this.removeAll();
            if (board.getTile(this.tileCoordinate).isTileOccupied()) {
                try {
                    BufferedImage image = ImageIO.read(new File(defaultPieceImagePath +
                                          board.getTile(this.tileCoordinate).getPiece().getPieceAlliance().toString().substring(0, 1) +
                    board.getTile(this.tileCoordinate).getPiece().toString() + ".gif"));
                    this.add(new JLabel(new ImageIcon(image)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
