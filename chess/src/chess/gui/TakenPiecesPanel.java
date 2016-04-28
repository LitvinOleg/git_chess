package chess.gui;

import chess.engine.board.Move;
import chess.engine.pieces.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static chess.gui.Table.*;

/**
 * Created by Олег on 19.04.2016.
 */
public class TakenPiecesPanel extends JPanel {
    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);
    private static final Color PANEL_COLOR = Color.decode("0xFDFE6");
    private static final Dimension TAKEN_PIECES_DIMENSION = new Dimension(40, 80);

    private JPanel northPanel;
    private JPanel southPanel;

    public TakenPiecesPanel() {
        super(new BorderLayout());
        this.setBackground(PANEL_COLOR);
        this.setBorder(PANEL_BORDER);
        this.northPanel = new JPanel(new GridLayout(8, 2));
        this.southPanel = new JPanel(new GridLayout(8, 2));
        this.northPanel.setBackground(PANEL_COLOR);
        this.southPanel.setBackground(PANEL_COLOR);
        this.add(this.northPanel, BorderLayout.NORTH);
        this.add(this.southPanel, BorderLayout.SOUTH);
        this.setPreferredSize(TAKEN_PIECES_DIMENSION);
    }

    public void redo(MoveLog moveLog) {
        this.northPanel.removeAll();
        this.southPanel.removeAll();

        List<Piece> whiteTakenPieces = new ArrayList<>();
        List<Piece> blackTakenPieces = new ArrayList<>();

        for (Move move : moveLog.getMoves()) {
            if (move.isAttack()) {
                Piece takenPiece = move.getAttackedPiece();
                if (takenPiece.getPieceAlliance().isWhite())
                    whiteTakenPieces.add(takenPiece);
                else if (takenPiece.getPieceAlliance().isBlack())
                    blackTakenPieces.add(takenPiece);
                else
                    throw new RuntimeException("Should not reach here!");
            }
        }

        Collections.sort(whiteTakenPieces, new Comparator<Piece>() {
            @Override
            public int compare(Piece o1, Piece o2) {
                return o1.getPieceValue() - o2.getPieceValue();
            }
        });
        Collections.sort(blackTakenPieces, new Comparator<Piece>() {
            @Override
            public int compare(Piece o1, Piece o2) {
                return o1.getPieceValue() - o2.getPieceValue();
            }
        });

        for (Piece takenPiece : whiteTakenPieces) {
            try {
                BufferedImage image = ImageIO.read(new File("/src/chess/gui/image/" +
                        takenPiece.getPieceAlliance().toString().substring(0, 1) +
                        takenPiece.toString()));
                ImageIcon icon = new ImageIcon(image);
                JLabel imageLable = new JLabel();
                this.southPanel.add(imageLable);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        for (Piece takenPiece : blackTakenPieces) {
            try {
                BufferedImage image = ImageIO.read(new File("/src/chess/gui/image/" +
                        takenPiece.getPieceAlliance().toString().substring(0, 1) +
                        takenPiece.toString()));
                ImageIcon icon = new ImageIcon(image);
                JLabel imageLable = new JLabel();
                this.southPanel.add(imageLable);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        validate();
    }
}
