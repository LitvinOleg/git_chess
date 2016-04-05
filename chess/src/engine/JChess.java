package engine;

import engine.board.Board;

/**
 * Created by Олег on 28.03.2016.
 */
public class JChess {
    public static void main(String[] args) {
        Board board = Board.createStandartdBoard();

        System.out.println(board.toString());
    }
}
