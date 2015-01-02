package jp.co.cyberagent.components;

import jp.co.cyberagent.components.exceptions.*;

import java.util.Random;

/**
 * Created by phucnh on 14/12/31.
 */
public class Board {

    // Board range
    // width range
    public static final int MIN_WIDTH = 3;
    public static final long MAX_WIDTH = Integer.MAX_VALUE;

    // height range
    public static final int MIN_HEIGHT = 3;
    public static final long MAX_HEIGHT = Integer.MAX_VALUE;

    // Board attributes
    // grid, array of square
    private Square[][] grid;
    private Integer height;
    private Integer width;
    private Long mineQty;
    private Long checkedMineCount;

    //
    private boolean isFirstSquareOpened;

    /**
     * Board constructor, create game board
     * @param height board's height
     * @param width board's width
     * @param mineQty board's mine quantity
     * @throws jp.co.cyberagent.components.exceptions.BoardCreateUnable if height, width, mine quantity are within invalid range
     */
    public Board(int height, int width, long mineQty) throws BoardCreateUnable, SquareWrongValueException {

        //.validate the board input
        // validate height
        if (height < Board.MIN_HEIGHT || height > Board.MAX_HEIGHT)
            throw new BoardCreateUnable(
                    String.format("Board's height must be from %d to %d",
                                   Board.MIN_HEIGHT,
                                   Board.MAX_HEIGHT)
            );

        // validate width
        if (width < Board.MIN_WIDTH || width > Board.MAX_WIDTH)
            throw new BoardCreateUnable(
                    String.format("Board's width must be from %d to %d",
                                   Board.MIN_WIDTH,
                                   Board.MAX_WIDTH)
            );

        // validate mine quality
        long maxNumMine = height * width - 1;
        if (mineQty < 1 || mineQty > maxNumMine)
            throw new BoardCreateUnable(
                    String.format("Board's mine must be more than 1 and less than %d",
                                  maxNumMine)
            );

        // set value to board
        this.height = height;
        this.width = width;
        this.mineQty = mineQty;
        this.checkedMineCount = 0l;

        // create gird
        this.grid = new Square[height][width];

        // generate grid
        this.fill();

        // when create new board, set first choose square is false
        this.isFirstSquareOpened = false;

    }

    /**
     * Default constructor, create board with default value:
     * height: 5
     * width: 5
     * mine quantity: 5
     * @throws BoardCreateUnable
     */
    public Board() throws BoardCreateUnable, SquareWrongValueException {
        this(5, 5, 5);
    }

    public int[] getSize() {
        return new int[] {this.height, this.width};
    }

    public Square toggleMineCheckSquare(int row, int col)
            throws BoardOutOfBoundException,
            SquareOpenedException {
        // check choose square's index, throws exception
        checkChooseSquareIndex(row, col);

        // toggle square's mine check
        Square square = this.grid[row][col];
        square.toggleMineCheck();

        // if check square is mine, update opened mine count
        // add 1 to check mine count if checked
        // subtract 1 to check mine count if unchecked
        if (square instanceof MineSquare) {
            if (square.isMineCheck())
                this.checkedMineCount += 1;
            else
                this.checkedMineCount -= 1;
        }

        return square;
    }

    public boolean openSquare(int row, int col)
            throws BoardOutOfBoundException,
                   SquareWrongValueException,
            SquareCheckedException,
                   SquareOpenedException {
        // check choose square's index, throws exception
        checkChooseSquareIndex(row, col);


        // if is the first open square, generate board
        if (this.isFirstSquareOpened) {

            this.isFirstSquareOpened = true;
        }

        // get square
        Square square = this.grid[row][col];

        // open square
        square.open();

        // check is win, return immediately
        if (this.checkedMineCount.equals(this.mineQty)) {
            return true;
        }

        // TODO: implement related square

        return false;

    }

    private void checkChooseSquareIndex(int row, int col)
            throws BoardOutOfBoundException {
        // check input
        // check input row
        if (row < 0 || row >= this.height)
            throw new BoardOutOfBoundException("Chosen row out of bound");

        // check input column
        if (col < 0 || col >= this.width)
            throw new BoardOutOfBoundException("Chosen height out of bound");
    }

    private void fill() throws SquareWrongValueException {
        // fill mine
        fillMine(1);

        // fill another square
        for (int r = 0; r < this.height; r++) {
            for (int c = 0; c < this.width; c++) {

                // continue if square is mine square
                if (this.grid[r][c] instanceof MineSquare)
                    continue;

                byte checkAround = countMineAround(r, c);

                // if check around have no mine, square is empty square
                // otherwise, square is number square with value is checkAround
                if (checkAround == 0)
                    this.grid[r][c] = new EmptySquare();
                else
                    this.grid[r][c] = new NumberSquare(checkAround);
            }
        }
    }

    private void fillMine(int numMine) {

        // terminal if current mines same with board's mine quantity
        if (numMine == this.mineQty) return;

        Random rand = new Random();

        // generate random row and column
        int row = rand.nextInt(this.height - 1);
        int col = rand.nextInt(this.width - 1);

        // if square has been opened, fill mine to another square
        if (this.grid[row][col] instanceof MineSquare) {
            fillMine(numMine);
        } else {
            // set square is mine square
            this.grid[row][col] = new MineSquare();

            // fill mine to another
            fillMine(++numMine);
        }

    }

    private byte countMineAround(int row, int col) {
        byte count = 0;

        // check mine around square
        for (Direction dir : Direction.values()) {
            Square square = getNeighborSquare(row, col, dir);

            if (square != null && square instanceof MineSquare)
                count += 1;
        }

        return count;
    }

    private Square getNeighborSquare(int row, int col, Direction direction) {

        int r = row;
        int c = col;

        // find the square follow direction
        switch (direction) {
            case TOP_LEFT:
                r--; c--;
                break;
            case TOP:
                r--;
                break;
            case TOP_RIGHT:
                r--; c++;
                break;
            case RIGHT:
                c++;
                break;
            case BOTTOM_RIGHT:
                r++; c--;
                break;
            case BOTTOM:
                r++;
                break;
            case BOTTOM_LEFT:
                r++; c--;
                break;
            case LEFT:
                c--;
                break;
            default:
                break;
        }

        // check is row and column out of bound
        // if out of bound, return null
        // otherwise return grid[row][column]
        try {
            // check square's index
            checkChooseSquareIndex(r, c);

            // when square's index valid, return square
            return this.grid[r][c];
        } catch (BoardOutOfBoundException e) {
            // when square's index invalid, return null
            return null;
        }

    }

    public Square getSquare(int row, int col) throws BoardOutOfBoundException {

        checkChooseSquareIndex(row, col);

        return this.grid[row][col];

    }

}

enum Direction {
    TOP_LEFT,
    TOP,
    TOP_RIGHT,
    RIGHT,
    BOTTOM_RIGHT,
    BOTTOM,
    BOTTOM_LEFT,
    LEFT
}
