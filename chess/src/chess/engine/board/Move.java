package chess.engine.board;

import chess.engine.pieces.Pawn;
import chess.engine.pieces.Piece;
import chess.engine.pieces.Rook;

import static chess.engine.board.Board.*;

/**
 *
 */
public abstract class Move {
    protected final Board board;
    protected final Piece movedPiece;
    protected final int destinationCoordinate;
    protected final boolean isFirstMove;

    public static final Move NULL_MOVE = new NullMove();

    private Move(Board board, int destinationCoordinate) {
        this.board = board;
        this.movedPiece = null;
        this.destinationCoordinate = destinationCoordinate;
        this.isFirstMove = false;
    }

    private Move(Board board, Piece movedPiece, int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
        this.isFirstMove = this.movedPiece.isFirstMove();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + this.destinationCoordinate;
        result = 31 * result + this.movedPiece.hashCode();
        result = 31 * result + this.movedPiece.getPiecePosition();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Move))
            return false;
        Move otherMove = (Move) obj;
        return getCurrentCoordinate() == otherMove.getCurrentCoordinate() &&
               getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
               getMovedPiece().equals(otherMove.getMovedPiece());
    }

    public int getCurrentCoordinate() {
        return this.movedPiece.getPiecePosition();
    }

    public Piece getMovedPiece() {
        return this.movedPiece;
    }

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    public boolean isAttack() {
        return false;
    }

    public boolean isCastlingMove() {
        return false;
    }

    public Piece getAttackedPiece() {
        return null;
    }


    /**
     * Executes the major move
     * @return new board, after moving
     */
    public Board execute() {
        Builder builder = new Builder();

        for (Piece piece : this.board.getCurrentPlayer().getActivePieces())
            if (!this.movedPiece.equals(piece))
                builder.setPiece(piece);

        for (Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces())
            builder.setPiece(piece);

        //move the moved piece!
        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());

        return builder.build();
    }

    /**
     * Major move class
     */
    public static final class MajorMove extends Move {
        public MajorMove(Board board,
                         Piece movedPiece,
                         int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj || obj instanceof MajorMove && super.equals(obj);
        }

        @Override
        public String toString() {
            return movedPiece.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    /**
     * Attack move class
     */
    public static class AttackMove extends Move {
        private final Piece attackedPiece;

        public AttackMove(Board board,
                          Piece movedPiece,
                          int destinationCoordinate,
                          Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public int hashCode() {
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (!(obj instanceof AttackMove))
                return false;
            AttackMove otherAttackMove = (AttackMove) obj;
            return super.equals(otherAttackMove) &&
                   getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }

        @Override
        public Board execute() {
            return null;
        }

        @Override
        public boolean isAttack() {
            return true;
        }

        @Override
        public Piece getAttackedPiece() {
            return this.attackedPiece;
        }
    }

    /**
     * Major pawn move class
     */
    public final static class PawnMove extends Move {
        public PawnMove(Board board,
                         Piece movedPiece,
                         int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }

    /**
     * Pawn jump move class
     */
    public static final class PawnJumpMove extends Move {
        public PawnJumpMove(Board board,
                        Piece movedPiece,
                        int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public Board execute() {
            Builder builder = new Builder();

            for (Piece piece : this.board.getCurrentPlayer().getActivePieces())
                if (!this.movedPiece.equals(piece))
                    builder.setPiece(piece);

            for (Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces())
                builder.setPiece(piece);

            Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());

            return builder.build();
        }
    }

    /**
     * Attack pawn move class
     */
    public static class PawnAttackMove extends AttackMove {
        public PawnAttackMove(Board board,
                        Piece movedPiece,
                        int destinationCoordinate,
                        Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }
    }

    /**
     * Attack pawn move
     */
    public static final class PawnEnPassantAttackMove extends PawnAttackMove {
        public PawnEnPassantAttackMove(Board board,
                                       Piece movedPiece,
                                       int destinationCoordinate,
                                       Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }
    }

    /**
     * Castle move
     */
    static abstract class CastleMove extends Move {
        protected Rook castleRook;
        protected int castleRookStart;
        protected int castleRookDestination;

        public CastleMove(Board board,
                          Piece movedPiece,
                          int destinationCoordinate,
                          Rook castleRook,
                          int castleRookStart,
                          int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }

        public Rook getCastleRook() {
            return castleRook;
        }

        @Override
        public boolean isCastlingMove() {
            return true;
        }

        /**
         * Executes the castle move
         *
         * @return new board, after moving
         */
        @Override
        public Board execute() {
            Builder builder = new Builder();

            for (Piece piece : this.board.getCurrentPlayer().getActivePieces())
                if (!this.movedPiece.equals(piece) && !this.castleRook.equals(piece))
                    builder.setPiece(piece);

            for (Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces())
                builder.setPiece(piece);

            builder.setPiece(this.movedPiece.movePiece(this));
            //TODO is first move!
            builder.setPiece(new Rook(this.castleRookDestination, this.castleRook.getPieceAlliance()));
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());

            return builder.build();
        }
    }

    public static final class KingSideCastleMove extends CastleMove {
        public KingSideCastleMove(Board board,
                                  Piece movedPiece,
                                  int destinationCoordinate,
                                  Rook castleRook,
                                  int castleRookStart,
                                  int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }

        @Override
        public String toString() {
            return  "0-0";
        }
    }

    public static final class QueenSideCastleMove extends CastleMove {
        public QueenSideCastleMove(Board board,
                                   Piece movedPiece,
                                   int destinationCoordinate,
                                   Rook castleRook,
                                   int castleRookStart,
                                   int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }

        @Override
        public String toString() {
            return  "0-0-0";
        }
    }

    public static final class NullMove extends Move {
        public NullMove() {
            super(null, null, -1);
        }

        @Override
        public Board execute() {
            throw new RuntimeException("Cannot execute the null move!");
        }
    }

    public static class MoveFactory {
        private MoveFactory() { throw new RuntimeException("Not instantiable!"); }

        public static Move createMove(Board board,
                                      int currentCoordinate,
                                      int destinationCoordinate) {
            for (Move move : board.getAllLegalMoves())
                if (move.getCurrentCoordinate() == currentCoordinate &&
                    move.getDestinationCoordinate() == destinationCoordinate)
                        return move;
            return NULL_MOVE;
        }
    }
}