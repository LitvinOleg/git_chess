package chess;

import chess.engine.board.Board;
import chess.gui.Table;

import javax.swing.*;

/**
 * Created by Олег on 28.03.2016.
 */
public class JChess {
    public static void main(String[] args) {
        Board board = Board.createStandartdBoard();
        System.out.println(board.toString());
        Table table = new Table();
    }
}
