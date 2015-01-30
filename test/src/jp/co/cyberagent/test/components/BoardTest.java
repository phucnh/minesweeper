package jp.co.cyberagent.test.components;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

import jp.co.cyberagent.components.*;
import jp.co.cyberagent.components.exceptions.*;

import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest {

    /**
     * Test create Board object: successfully case
     */
    @Test
    public void testCreateBoardSuccessfully() {

        try {
            Board board = new Board(5, 15, 6);

            // ensure board not null
            assertNotNull("Failure - Board create fail, null object", board);

            // ensure board's size: height = 5; width = 5
            // get board size
            int[] boardSize = board.getSize();

            // ensure height
            assertEquals(
                    "Failure - Board get size fail height is not equal 5",
                    5,
                    boardSize[0]);

            // ensure width
            assertEquals(
                    "Failure - Board get size fail width is not equal 15",
                    15,
                    boardSize[1]);

            // ensure board's elements
            // ensure board's private height
            Field bHeight = Board.class.getDeclaredField("height");
            bHeight.setAccessible(true);
            assertEquals(
                    "Failure - Board's height is not equal 5",
                    5,
                    bHeight.get(board)
            );

            // ensure board's private width
            Field bWidth = Board.class.getDeclaredField("width");
            bWidth.setAccessible(true);
            assertEquals(
                    "Failure - Board's width is not equal 15",
                    15, bWidth.get(board));

            // ensure board's mine quantity
            Field bMineQty = Board.class.getDeclaredField("mineQty");
            bMineQty.setAccessible(true);
            assertEquals(
                    "Failure - Board's mine quantity is not equal 6",
                    6l,
                    bMineQty.get(board)
            );

            // ensure board's opened mine count
            Field bOpenedMineCount =
                    Board.class.getDeclaredField("openedMineCount");
            bOpenedMineCount.setAccessible(true);
            assertEquals(
                    "Failure - Board's opened mine count is not equal 0",
                    0l,
                    bOpenedMineCount.get(board)
            );

            // ensure board's grid
            Field bGrid = Board.class.getDeclaredField("grid");
            bGrid.setAccessible(true);
            Square[][] grid = (Square[][]) bGrid.get(board);

            // ensure grid size
            // ensure grid's number of row
            assertEquals(
                    "Failure - Board's number of grid row is not equal 5",
                    5,
                    grid.length
            );

            // ensure grid length of each row
            for (Square[] gridRow : grid) {
                assertEquals(
                        "Failure - Length of row is not equal 15",
                        15,
                        gridRow.length
                );
            }

            // ensure grid's squares
            // mine count
            long mineCount = 0l;

            // count number of mine
            for (int r = 0; r < 5; r++) {
                for (int c = 0; c < 15; c++) {
                    // ensure each square in grid is same with board's get square
                    assertEquals(
                            String.format(
                                    "Failure - Board's get square at %d %d " +
                                            "is not same with " +
                                            "grid row %d, column %d",
                                    r, c,
                                    r, c
                            ),
                            board.getSquare(r, c),
                            grid[r][c]);

                    // get square
                    Square square = grid[r][c];

                    // update mine count when square is mine square
                    if (square instanceof MineSquare) {
                        ++mineCount;
                    } else {

                        // count mine around square
                        byte mineAround = countMineAroundSquare(
                                grid, r, c, 5, 15
                        );

                        // when count mine around is 0, ensure square is empty square
                        // otherwise, ensure square is number square, ensure square's value
                        if (mineAround == 0) {
                            // ensure square is empty square
                            assertTrue(
                                    String.format(
                                            "Failure - Square at row %d, " +
                                                    "column %d is not " +
                                                    "EmptySquare instance",
                                            r,
                                            c
                                    ),
                                    square instanceof EmptySquare
                            );

                        } else {
                            // ensure square is number square
                            assertTrue(
                                    String.format(
                                            "Failure - Square at row %d, " +
                                                    "column %d is not " +
                                                    "NumberSquare instance",
                                            r,
                                            c
                                    ),
                                    square instanceof NumberSquare);

                            // convert to number square
                            NumberSquare numSquare = (NumberSquare) square;

                            // ensure number square value
                            assertEquals(
                                    String.format(
                                            "Failure - Square's value at " +
                                                    "row %d, column %d " +
                                                    "is not %d",
                                            r,
                                            c,
                                            mineAround
                                    ),
                                    mineAround,
                                    (byte) numSquare.getValue());
                        }

                    }


                }
            }

            // ensure number of mine
            assertEquals(
                    "Failure - Board's number of mine is not equal 6",
                    6l,
                    mineCount
            );

        } catch (Exception e) {
            // test case not pass
            fail("Failure - " + e.getMessage());
        }

    }

    /**
     * Test create Board object: failed case, height is out of bound
     */
    @Test
    public void testCreateBoardFailedHeightOutOfBound() {

        // height is less than bound
        try {
            // create new board with height less than bound Board.MIN_HEIGHT
            new Board(1, 5, 5);

            // test case not pass
            fail("Failure - Create board with height is 1 " +
                    "not throw BoardCreateUnable exception");
        } catch (Exception e) {
            // ensure exception is instance of BoardCreateUnable
            assertTrue(
                    "Failure - Exception is not BoardCreateUnable",
                    e instanceof BoardCreateUnable);

            // ensure exception message
            assertEquals(
                    "Failure - BoardCreateUnable height out of bound wrong",
                    String.format("Board's height must be from %d to %d",
                        Board.MIN_HEIGHT,
                        Board.MAX_HEIGHT),
                    e.getMessage()
            );
        }

        // height is greater than bound
        try {
            // create new board with height greater than bound Board.MAX_HEIGHT
            new Board(60001, 5, 5);

            // test case not pass
            fail("Failure - Create board with height is 60001 " +
                    "not throw BoardCreateUnable exception");
        } catch (Exception e) {
            // ensure exception is instance of BoardCreateUnable
            assertTrue(
                    "Failure - Exception is not BoardCreateUnable",
                    e instanceof BoardCreateUnable);

            // ensure exception message
            assertEquals(
                    "Failure - BoardCreateUnable height out of bound wrong",
                    String.format("Board's height must be from %d to %d",
                            Board.MIN_HEIGHT,
                            Board.MAX_HEIGHT),
                    e.getMessage()
            );
        }

    }

    /**
     * Test create Board object: failed case, width is out of bound
     */
    @Test
    public void testCreateBoardFailedWidthOutOfBound() {

        // width is less than bound
        try {
            // create new board with width less than bound Board.MIN_WIDTH
            new Board(5, 1, 5);

            // test case not pass
            fail("Failure - Create board with width is 1 " +
                    "not throw BoardCreateUnable exception");
        } catch (Exception e) {
            // ensure exception is instance of BoardCreateUnable
            assertTrue(
                    "Failure - Exception is not BoardCreateUnable",
                    e instanceof BoardCreateUnable);

            // ensure exception message
            assertEquals(
                    "Failure - BoardCreateUnable width out of bound wrong",
                    String.format("Board's width must be from %d to %d",
                            Board.MIN_WIDTH,
                            Board.MAX_WIDTH),
                    e.getMessage()
            );
        }

        // width is greater than bound
        try {
            // create new board with width greater than bound Board.MAX_WIDTH
            new Board(5, 60001, 5);

            // test case not pass
            fail("Failure - Create board with width is 60001 " +
                    "not throw BoardCreateUnable exception");
        } catch (Exception e) {
            // ensure exception is instance of BoardCreateUnable
            assertTrue(
                    "Failure - Exception is not BoardCreateUnable",
                    e instanceof BoardCreateUnable);

            // ensure exception message
            assertEquals(
                    "Failure - BoardCreateUnable width out of bound wrong",
                    String.format("Board's width must be from %d to %d",
                            Board.MIN_WIDTH,
                            Board.MAX_WIDTH),
                    e.getMessage()
            );
        }

    }

    /**
     * Test create Board object: failed case, mine quantity is out of bound
     */
    @Test
    public void testCreateBoardFailedMineOutOfBound() {

        // mine out of bound < 1
        try {
            // create new board with mine quantity is 0
            new Board(5, 15, 0);

            // test case not pass
            fail("Failure - Create board with mine quantity is 0 " +
                    "not throw BoardCreateUnable");
        } catch (Exception e) {
            // ensure exception is instance of BoardCreateUnable
            assertTrue(
                    "Failure - Exception is not BoardCreateUnable",
                    e instanceof BoardCreateUnable
            );

            // ensure exception message
            assertEquals(
                    "Failure - BoardCreateUnable mine quantity" +
                            " out of bound wrong",
                    String.format(
                            "Board's mine must be more than 1 and less than %d",
                            5 * 15 - 1),
                    e.getMessage()
            );
        }

        // mine out of bound > height*width - 1
        try {
            // create new board with mine quantity out of bound > height*width - 1
            new Board(5, 15, 5 * 15);

            // test case not pass
            fail("Failure - Create board with mine quantity is 75 " +
                    "not throw BoardCreateUnable");
        } catch (Exception e) {
            // ensure exception is instance of BoardCreateUnable
            assertTrue(
                    "Failure - Exception is not BoardCreateUnable",
                    e instanceof BoardCreateUnable
            );

            // ensure exception message
            // ensure exception message
            assertEquals(
                    "Failure - BoardCreateUnable mine quantity" +
                            " out of bound wrong",
                    String.format(
                            "Board's mine must be more than 1 and less than %d",
                            5 * 15 - 1),
                    e.getMessage()
            );
        }

    }

    /**
     * Test toggle square: successfully case
     */
    @Test
    public void testToggleSquareSuccessfully() {

        try {
            // create board
            Board board = new Board(7, 10, 10);

            // ensure board not null
            assertNotNull("Failure - Board instance is null", board);

            // get square at row is 5, column is 5
            Square square = board.getSquare(5, 5);

            // ensure square is not toggle mine check
            assertFalse(
                    "Failure - After create board, the square had been checked",
                    square.isMineChecked()
            );

            // toggle square is mine check
            board.toggleMineCheckSquare(5, 5);

            // ensure square is mine checked
            assertTrue(
                    "Failure - After checked, the square is not check",
                    square.isMineChecked()
            );

            // toggle square again, square is not mine check
            board.toggleMineCheckSquare(5, 5);

            // ensure square is unchecked
            assertFalse(
                    "Failure - After checked, the square is still checked",
                    square.isMineChecked());

        } catch (Exception e) {
            fail("Failure - Toggle square has error " + e.getMessage());
        }

    }

    /**
     * Test toggle square: failed. Chosen square is out of bound
     */
    @Test
    public void testToggleSquareFailedIndexOutOfBound() {

        // toggle square that have row less than 0
        try {
            // create board
            Board board = new Board(7, 5, 10);

            // ensure board is not null
            assertNotNull(board);

            // toggle out of bound, row index less than 0
            board.toggleMineCheckSquare(-1, 3);

            // test case failed
            fail("Failure - Toggle square with row is -1 " +
                    "not throw BoardOutOfBoundException");

        } catch (Exception e) {

            // ensure exception is BoardOutOfBoundException
            assertTrue(
                    "Failure - Exception is not BoardOutOfBoundException",
                    e instanceof BoardOutOfBoundException
            );

            // ensure exception message
            assertEquals(
                    "Failure - Chosen row -1 case exception message wrong",
                    "Chosen row out of bound",
                    e.getMessage()
            );

        }

        // toggle square that have row greater than board's height
        try {
            // create board
            Board board = new Board(7, 5, 10);

            // ensure board is not null
            assertNotNull(board);

            // toggle out of bound, row index greater than board's height
            board.toggleMineCheckSquare(7, 3);

            // test case failed
            fail("Failure - Toggle square with row is 7 " +
                    "not throw BoardOutOfBoundException");

        } catch (Exception e) {

            // ensure exception is BoardOutOfBoundException
            assertTrue(
                    "Failure - Exception is not BoardOutOfBoundException",
                    e instanceof BoardOutOfBoundException
            );

            // ensure exception message
            assertEquals(
                    "Failure - Chosen row 7 case exception message wrong",
                    "Chosen row out of bound",
                    e.getMessage()
            );

        }

        // toggle square that have column less than 0
        try {
            // create board
            Board board = new Board(7, 5, 10);

            // ensure board is not null
            assertNotNull(board);

            // toggle out of bound, column index less than 0
            board.toggleMineCheckSquare(1, -1);

            // test case failed
            fail("Failure - Toggle square with column is -1 " +
                    "not throw BoardOutOfBoundException");

        } catch (Exception e) {

            // ensure exception is BoardOutOfBoundException
            assertTrue(
                    "Failure - Exception is not BoardOutOfBoundException",
                    e instanceof BoardOutOfBoundException
            );

            // ensure exception message
            assertEquals(
                    "Failure - Chosen column -1 case exception message wrong",
                    "Chosen column out of bound",
                    e.getMessage()
            );

        }

        // toggle square that have column greater than board's width
        try {
            // create board
            Board board = new Board(7, 5, 10);

            // ensure board is not null
            assertNotNull(board);

            // toggle out of bound, column index greater than board's width
            board.toggleMineCheckSquare(1, 5);

            // test case failed
            fail("Failure - Toggle square with column is 5 " +
                    "not throw BoardOutOfBoundException");

        } catch (Exception e) {

            // ensure exception is BoardOutOfBoundException
            assertTrue(
                    "Failure - Exception is not BoardOutOfBoundException",
                    e instanceof BoardOutOfBoundException
            );

            // ensure exception message
            assertEquals(
                    "Failure - Chosen column 5 case exception message wrong",
                    "Chosen column out of bound",
                    e.getMessage()
            );

        }

    }

    /**
     * Test toggle square: failed. Chosen square has been opened
     */
    @Test
    public void testToggleSquareFailedSquareOpened() {

        // create board
        Board board = null;
        try {
            board = new Board(7, 5, 10);
        } catch (Exception e) {
            // test case not pass
            fail("Failure - Cannot create board " + e.getMessage());
        }

        // ensure board is not null
        assertNotNull("Failure - Board is null", board);

        try {

            // get square at row is 2, column is 3
            Square square = board.getSquare(2, 3);

            // ensure square is not opened
            assertFalse(
                    "Failure - When create board, the square is opened",
                    square.isOpened()
            );

            // open a square
            board.openSquare(2, 3);

            // ensure square opened
            assertTrue(
                    "Failure - After open square, the square still not open",
                    square.isOpened());

            // toggle an opened square
            board.toggleMineCheckSquare(2, 3);

            // test case failed
            fail("Failure - Not throw to SquareOpenedException exception");

        } catch (Exception e) {

            // ensure exception is SquareOpenedException
            assertTrue(
                    "Failure - The exception is not instance " +
                            "of SquareOpenedException",
                    e instanceof SquareOpenedException);

            // ensure exception message
            assertEquals(
                    "The exception message is wrong",
                    "Square has been opened, cannot toggle mine check",
                    e.getMessage()
            );

            // ensure square is not checked
            try {
                assertFalse(
                        "Failure - The square is opened but mine check is true",
                        board.getSquare(2, 3).isMineChecked());
            } catch (Exception e1) {
                // test case not pass
                fail("Failure - " + e.getMessage());
            }

        }

    }

    /**
     * Test open square successfully
     */
    @Test
    public void testOpenSquareSuccessfully() {

        // test open a empty square, the related will be opened
        try {
            // create board
            Board board = new Board(8, 8, 10);

            // ensure board not null
            assertNotNull(
                    "Failure - Board create fail, board is null",
                    board
            );

            // set board
            Field bGrid = Board.class.getDeclaredField("grid");
            bGrid.setAccessible(true);
            bGrid.set(board, makeBoard());

            // test open a empty square
            // get empty square that row is 0, column is 7
            Square emptySquare = board.getSquare(0, 7);

            // ensure square is empty square object
            assertTrue(
                    "Failure - Square instance at row is 0, column is 7 " +
                            "is not empty square",
                    emptySquare instanceof EmptySquare
            );

            // ensure square is not opened
            assertFalse(
                    "Failure - When create board, square is opened",
                    emptySquare.isOpened()
            );

            // open square
            PlayStatus playStatus = board.openSquare(0, 7);

            // ensure square is opened
            assertTrue(
                    "Failure - After open square, square still not open",
                    emptySquare.isOpened()
            );

            // ensure play status is normal
            assertEquals(
                    "Failure - Open 1 square only, play status is not normal",
                    PlayStatus.NORMAL,
                    playStatus
            );

            // ensure related square is opened,
            // another square is not open
            int[] boardSize = board.getSize();
            int height = boardSize[0];
            int width = boardSize[1];

            for (int r = 0; r < height; r++) {
                for (int c = 0; c < width; c++) {

                    if ((r == 0 && c == 7) ||
                            (r == 1 && c == 7) ||
                            (r == 0 && c == 6) ||
                            (r == 1 && c == 6) ||
                            (r == 2 && c == 6) ||
                            (r == 2 && c == 7)) {
                        assertTrue(
                                String.format(
                                        "Failure - Square at row %d, " +
                                                "column %d is not open",
                                        r,
                                        c
                                ),
                                board.getSquare(r, c).isOpened()
                        );
                    } else {
                        assertFalse(
                                String.format(
                                        "Failure - Square at row %d, " +
                                                "column %d is opened",
                                        r,
                                        c
                                ),
                                board.getSquare(r, c).isOpened());
                    }

                }
            }

        } catch (Exception e) {
            // test case not pass
            fail("Failure - Open empty square error " + e.getMessage());
        }

        // test open a mine square
        try {

            // create board
            Board board = new Board(8, 8, 10);

            // ensure board not null
            assertNotNull(
                    "Failure - Board create fail, board is null",
                    board
            );

            // get grid attribute
            Field bGrid = Board.class.getDeclaredField("grid");

            // set access to grid
            bGrid.setAccessible(true);

            // set grid the defined game board
            bGrid.set(board, makeBoard());

            // get mine square that row is 1, column is 5
            Square mineSquare = board.getSquare(1, 5);

            // ensure square is mine square
            assertTrue(
                    "Failure - Square instance at row is 1, column is 5 " +
                            "is not mine square",
                    mineSquare instanceof MineSquare
            );

            // ensure square is not opened
            assertFalse(
                    "Failure - When create board, square is opened",
                    mineSquare.isOpened()
            );

            // open square
            PlayStatus playStatus = board.openSquare(1, 5);

            // ensure mine square is opened,
            // another square is not open
            int[] boardSize = board.getSize();
            int height = boardSize[0];
            int width = boardSize[1];

            for (int r = 0; r < height; r++) {
                for (int c = 0; c < width; c++) {

                    if (r == 1 && c == 5) {
                        assertTrue(
                                String.format(
                                        "Failure - Square at row %d, " +
                                                "column %d is not open",
                                        r,
                                        c
                                ),
                                board.getSquare(r, c).isOpened()
                        );
                    } else {
                        assertFalse(
                                String.format(
                                        "Failure - Square at row %d, " +
                                                "column %d is opened",
                                        r,
                                        c
                                ),
                                board.getSquare(r, c).isOpened()
                        );
                    }

                }
            }

            // ensure play status is lose
            assertEquals(
                    "Failure - Open mine square but play status is not lose",
                    PlayStatus.LOSE,
                    playStatus
            );

        } catch (Exception e) {
            // test case not pass
            fail("Failure - Open mine square error " + e.getMessage());
        }

        // test open a number square
        try {

            // create board
            Board board = new Board(8, 8, 10);

            // ensure board not null
            assertNotNull(
                    "Failure - Board create fail, board is null",
                    board
            );

            // get grid attribute
            Field bGrid = Board.class.getDeclaredField("grid");

            // set access to grid
            bGrid.setAccessible(true);

            // set grid the defined game board
            bGrid.set(board, makeBoard());

            // get number square that row is 0, column is 4
            Square numSquare = board.getSquare(0, 4);

            // ensure square is number square
            assertTrue(
                    "Failure - Square instance at row is 0, column is 4 " +
                            "is not number square",
                    numSquare instanceof NumberSquare
            );

            // ensure square ís not opened
            assertFalse(
                    "Failure - When create board, square is opened",
                    numSquare.isOpened()
            );

            // open square
            PlayStatus playStatus = board.openSquare(0, 4);

            // ensure number square is opened,
            // another square is not open
            int[] boardSize = board.getSize();
            int height = boardSize[0];
            int width = boardSize[1];

            for (int r = 0; r < height; r++) {
                for (int c = 0; c < width; c++) {

                    if (r == 0 && c == 4) {
                        assertTrue(
                                String.format(
                                        "Failure - Square at row %d, " +
                                                "column %d is not open",
                                        r,
                                        c
                                ),
                                board.getSquare(r, c).isOpened()
                        );
                    } else {
                        assertFalse(
                                String.format(
                                        "Failure - Square at row %d, " +
                                                "column %d is opened",
                                        r,
                                        c
                                ),
                                board.getSquare(r, c).isOpened()
                        );
                    }

                }
            }

            // ensure play status is normal
            assertEquals(
                    "Failure - Open 1 square only, play status is not normal",
                    PlayStatus.NORMAL, playStatus
            );

        } catch (Exception e) {
            // test case not pass
            fail("Failure - Open number square error " + e.getMessage());
        }

    }

    /**
     * Test open square successfully. When open the last non mine square, game win
     */
    @Test
    public void testOpenSquareSuccessfullyGameWin() {

        try {
            // create board
            Board board = new Board(8, 8, 10);

            // ensure board not null
            assertNotNull(
                    "Failure - Board create fail, board is null",
                    board
            );

            // set board
            Field bGrid = Board.class.getDeclaredField("grid");
            bGrid.setAccessible(true);
            bGrid.set(board, makeBoard());

            // define do not open squares
            int[][] doNotOpen = new int[11][2];
            doNotOpen[0] = new int[] {1, 5};
            doNotOpen[1] = new int[] {3, 6};
            doNotOpen[2] = new int[] {4, 0};
            doNotOpen[3] = new int[] {4, 4};
            doNotOpen[4] = new int[] {6, 1};
            doNotOpen[5] = new int[] {6, 5};
            doNotOpen[6] = new int[] {6, 6};
            doNotOpen[7] = new int[] {7, 2};
            doNotOpen[8] = new int[] {7, 3};
            doNotOpen[9] = new int[] {7, 7};

            // add a square to do not open list, if when open this, win game
            doNotOpen[10] = new int[] {0, 5};

            // open the remaining squares
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {

                    int[] index = {r, c};

                    if (!isInList(doNotOpen, index)) {

                        // open square
                        if (!board.getSquare(r, c).isOpened()) {
                            board.openSquare(r, c);
                        }

                        // ensure each square is opened
                        assertTrue(
                                String.format(
                                        "The square at row %d, " +
                                                "column %d is not open",
                                        r,
                                        c),
                                board.getSquare(r, c).isOpened());

                    }

                }
            }

            // open the last number square that row is 0, column is 5
            PlayStatus status = board.openSquare(0, 5);

            // ensure square is opened
            assertTrue(
                    "The square at row 0, column 5 is not open",
                    board.getSquare(0, 5).isOpened());

            // ensure play status is win
            assertEquals(PlayStatus.WIN, status);

        } catch (Exception e) {
            // test case not pass
            fail("Failure - The square error " + e.getMessage());
        }

    }

    /**
     * Test open square failed. The chosen square's index is out of bound
     */
    @Test
    public void testOpenSquareFailedIndexOutOfBound() {

        // open square that have row less than 0
        try {
            // create board
            Board board = new Board(7, 5, 10);

            // ensure board is not null
            assertNotNull(
                    "Failure - Board create fail, board is null",
                    board
            );

            // open out of bound, row index less than 0
            board.openSquare(-1, 3);

            // test case failed
            fail("Failure - Open square with row -1 is not throw exception");

        } catch (Exception e) {

            // ensure exception is BoardOutOfBoundException
            assertTrue(
                    "Failure - The exception is not BoardOutOfBoundException",
                    e instanceof BoardOutOfBoundException
            );

            // ensure exception message
            assertEquals(
                    "Failure - Open square at row -1 exception message wrong",
                    "Chosen row out of bound",
                    e.getMessage()
            );

        }

        // open square that have row greater than board's height
        try {
            // create board
            Board board = new Board(7, 5, 10);

            // ensure board is not null
            assertNotNull(
                    "Failure - Board create fail, board is null",
                    board
            );

            // open out of bound, row index greater than board's height
            board.openSquare(7, 3);

            // test case failed
            fail("Failure - Open square with row 7 is not throw exception");

        } catch (Exception e) {

            // ensure exception is BoardOutOfBoundException
            assertTrue(
                    "Failure - The exception is not BoardOutOfBoundException",
                    e instanceof BoardOutOfBoundException
            );

            // ensure exception message
            assertEquals(
                    "Failure - Open square at row 7 exception message wrong",
                    "Chosen row out of bound",
                    e.getMessage()
            );

        }

        // open square that have column less than 0
        try {
            // create board
            Board board = new Board(7, 5, 10);

            // ensure board is not null
            assertNotNull(
                    "Failure - Board create fail, board is null",
                    board
            );

            // open out of bound, column index less than 0
            board.openSquare(1, -1);

            // test case failed
            fail("Failure - Open square with column -1 is not throw exception");

        } catch (Exception e) {

            // ensure exception is BoardOutOfBoundException
            assertTrue(
                    "Failure - The exception is not BoardOutOfBoundException",
                    e instanceof BoardOutOfBoundException
            );

            // ensure exception message
            assertEquals(
                    "Failure - Open square at column -1 exception msg wrong",
                    "Chosen column out of bound",
                    e.getMessage());

        }

        // open square that have column greater than board's width
        try {
            // create board
            Board board = new Board(7, 5, 10);

            // ensure board is not null
            assertNotNull(
                    "Failure - Board create fail, board is null",
                    board
            );

            // open out of bound, column index greater than board's width
            board.openSquare(1, 5);

            // test case failed
            fail("Failure - Open square with column -1 is not throw exception");

        } catch (Exception e) {

            // ensure exception is BoardOutOfBoundException
            assertTrue(e instanceof BoardOutOfBoundException);

            // ensure exception message
            assertEquals(
                    "Failure - Open square at column -1 exception msg wrong",
                    "Chosen column out of bound",
                    e.getMessage());

        }

    }

    /**
     * Test open square failed. The chosen square is checked
     */
    @Test
    public void testOpenSquareFailedSquareChecked() {

        // create board
        Board board = null;
        try {
            board = new Board(7, 5, 10);
        } catch (Exception e) {
            // test case not pass
            fail("Failure - Create board fail " + e.getMessage());
        }

        // ensure board is not null
        assertNotNull(
                "Failure - Board create fail, board is null",
                board
        );

        try {

            // get square at row is 2, column is 3
            Square square = board.getSquare(2, 3);

            // ensure square is not opened
            assertFalse(
                    "Failure - After create, square has been opened",
                    square.isOpened());

            // ensure square is not checked
            assertFalse(
                    "Failure - After create, square has been checked",
                    square.isMineChecked()
            );

            // check square
            board.toggleMineCheckSquare(2, 3);

            // ensure square is checked
            assertTrue(
                    "Failure - After checked, square is not check",
                    square.isMineChecked()
            );

            // open an checked square
            board.openSquare(2, 3);

            // test case failed
            fail("Failure - Open the checked square not throw exception");

        } catch (Exception e) {

            // ensure exception is SquareCheckedException
            assertTrue(
                    "Failure - The exception is not SquareCheckedException",
                    e instanceof SquareCheckedException);

            // ensure exception message
            assertEquals(
                    "Failure - Open a checked square exception message wrong",
                    "Square has been mine marked, can not open square",
                    e.getMessage());

            // ensure square is not opened
            try {
                assertFalse(
                        "Failure - Open square fail, but square is opened",
                        board.getSquare(2, 3).isOpened()
                );
            } catch (Exception e1) {
                // test case not pass
                fail();
            }

        }

    }

    /**
     * Test open square failed. The chosen square is opened
     */
    @Test
    public void testOpenSquareFailedSquareOpened() {

        // create board
        Board board = null;
        try {
            board = new Board(7, 5, 10);
        } catch (Exception e) {
            // test case not pass
            fail("Failure - Create board fail " + e.getMessage());
        }

        // ensure board is not null
        assertNotNull(
                "Failure - Board create fail, board is null",
                board
        );

        try {

            // get square at row is 2, column is 3
            Square square = board.getSquare(2, 3);

            // ensure square is not opened
            assertFalse(
                    "Failure - After create board, square had been opened",
                    square.isOpened()
            );

            // open square
            board.openSquare(2, 3);

            // ensure square is opened
            assertTrue(
                    "Failure - After open square, square is not open",
                    square.isOpened()
            );

            // open an opened square
            board.openSquare(2, 3);

            // test case failed
            fail("Failure - Open the opened square not throw exception");

        } catch (Exception e) {

            // ensure exception is SquareCheckedException
            assertTrue(
                    "Failure - The exception is not SquareOpenedException",
                    e instanceof SquareOpenedException
            );

            // ensure exception message
            assertEquals(
                    "Failure - Open a opened square exception message wrong",
                    "Square has been opened, cannot open square",
                    e.getMessage()
            );

        }

    }

    /**
     * Create a specific game board
     */
    private Square[][] makeBoard() {

        Square[][] grid = new Square[8][8];

        // first, create grid is empty square
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                grid[r][c] = createEmptySquare();
            }
        }

        // put mine, 10 mine squares
        grid[1][5] = createMineSquare();
        grid[3][6] = createMineSquare();
        grid[4][0] = createMineSquare();
        grid[4][4] = createMineSquare();
        grid[6][1] = createMineSquare();
        grid[6][5] = createMineSquare();
        grid[6][6] = createMineSquare();
        grid[7][2] = createMineSquare();
        grid[7][3] = createMineSquare();
        grid[7][7] = createMineSquare();

        // put number square
        grid[0][4] = createNumberSquare((byte) 1);
        grid[0][5] = createNumberSquare((byte) 1);
        grid[0][6] = createNumberSquare((byte) 1);

        grid[1][4] = createNumberSquare((byte) 1);
        grid[1][6] = createNumberSquare((byte) 1);

        grid[2][4] = createNumberSquare((byte) 1);
        grid[2][5] = createNumberSquare((byte) 2);
        grid[2][6] = createNumberSquare((byte) 2);
        grid[2][7] = createNumberSquare((byte) 1);

        grid[3][0] = createNumberSquare((byte) 1);
        grid[3][1] = createNumberSquare((byte) 1);
        grid[3][3] = createNumberSquare((byte) 1);
        grid[3][4] = createNumberSquare((byte) 1);
        grid[3][5] = createNumberSquare((byte) 2);
        grid[3][7] = createNumberSquare((byte) 1);

        grid[4][1] = createNumberSquare((byte) 1);
        grid[4][3] = createNumberSquare((byte) 1);
        grid[4][5] = createNumberSquare((byte) 2);
        grid[4][5] = createNumberSquare((byte) 2);
        grid[4][6] = createNumberSquare((byte) 1);
        grid[4][7] = createNumberSquare((byte) 1);

        grid[5][0] = createNumberSquare((byte) 2);
        grid[5][1] = createNumberSquare((byte) 2);
        grid[5][2] = createNumberSquare((byte) 1);
        grid[5][3] = createNumberSquare((byte) 1);
        grid[5][4] = createNumberSquare((byte) 2);
        grid[5][5] = createNumberSquare((byte) 3);
        grid[5][6] = createNumberSquare((byte) 2);
        grid[5][7] = createNumberSquare((byte) 1);

        grid[6][0] = createNumberSquare((byte) 1);
        grid[6][2] = createNumberSquare((byte) 3);
        grid[6][3] = createNumberSquare((byte) 2);
        grid[6][4] = createNumberSquare((byte) 2);
        grid[6][7] = createNumberSquare((byte) 2);

        grid[7][0] = createNumberSquare((byte) 1);
        grid[7][1] = createNumberSquare((byte) 2);
        grid[7][4] = createNumberSquare((byte) 2);
        grid[7][5] = createNumberSquare((byte) 2);
        grid[7][6] = createNumberSquare((byte) 3);

        return grid;

    }

    /**
     * Call to non public function, create a mine square
     */
    private MineSquare createMineSquare() {

        try {
            // get MineSquare's constructor
            Constructor<MineSquare> constructor =
                    MineSquare.class.getDeclaredConstructor();
            constructor.setAccessible(true);

            // create new mine square object
            return constructor.newInstance();
        } catch (Exception e) {
            fail();
        }

        // when square is not created successfully, test case not pass
        fail();

        return null;

    }

    /**
     * Call to non public function, create a empty square
     */
    private EmptySquare createEmptySquare() {

        try {
            // get EmptySquare's constructor
            Constructor<EmptySquare> constructor =
                    EmptySquare.class.getDeclaredConstructor();
            constructor.setAccessible(true);

            // create new empty square object
            return constructor.newInstance();
        } catch (Exception e) {
            fail();
        }

        // when square is not created successfully, test case not pass
        fail();

        return null;

    }

    /**
     * Call to non public function, create a number square
     */
    private NumberSquare createNumberSquare(byte value) {

        try {
            // get NumberSquare constructor
            Constructor<NumberSquare> constructor =
                    NumberSquare.class.getDeclaredConstructor(byte.class);
            constructor.setAccessible(true);

            // create new number square object
            return constructor.newInstance(value);
        } catch (Exception e) {
            fail();
        }

        // when square is not created successfully, test case not pass
        fail();

        return null;

    }

    /**
     * Count mine around a square
     */
    private byte countMineAroundSquare(
            Square[][] grid,
            int row,
            int col,
            int maxHeight,
            int maxWidth) {

        // mine count
        byte count = 0;

        // check mine around square
        for (Direction dir : Direction.values()) {
            Square neighbor = getNeighborSquare(
                    grid,
                    row,
                    col,
                    maxHeight,
                    maxWidth,
                    dir
            );

            // when square is mine square, update mine count
            if (neighbor != null && neighbor instanceof MineSquare)
                count += 1;
        }

        return count;

    }

    /**
     * Get the neighbour of square
     */
    private Square getNeighborSquare(
            Square[][] grid,
            int row,
            int col,
            int maxHeight,
            int maxWidth,
            Direction direction) {

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
                r++; c++;
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
        if (isInGridSizeBound(r, c, maxHeight, maxWidth)) {
            // when square's index valid, return square
            return grid[r][c];
        } else {
            return null;
        }

    }

    /**
     * Check the index is in grid size bound or not
     */
    private boolean isInGridSizeBound(
            int row,
            int col,
            int maxHeight,
            int maxWidth) {

        // check row
        if (row < 0 || row >= maxHeight)
            return false;

        // check column
        if (col < 0 || col >= maxWidth)
            return false;

        return true;

    }

    /**
     * Check is the array element in list array or not
     */
    private boolean isInList(int[][] list, int[] element) {

        for (int[] e : list) {
            if (Arrays.equals(e, element))
                return true;
        }

        return false;

    }


    // direction for get square neighbour, use for test only
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

}
