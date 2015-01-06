package jp.co.cyberagent.test.components;

import jp.co.cyberagent.components.*;

import jp.co.cyberagent.components.exceptions.BoardCreateUnable;

import jp.co.cyberagent.components.exceptions.BoardOutOfBoundException;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

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
            assertNotNull(board);

            // ensure board's size: height = 5; width = 5
            // get board size
            int[] boardSize = board.getSize();

            // ensure height
            assertEquals(5, boardSize[0]);

            // ensure width
            assertEquals(15, boardSize[1]);

            // ensure board's elements
            // ensure board's private height
            Field bHeight = Board.class.getDeclaredField("height");
            bHeight.setAccessible(true);
            assertEquals(5, bHeight.get(board));

            // ensure board's private width
            Field bWidth = Board.class.getDeclaredField("width");
            bWidth.setAccessible(true);
            assertEquals(15, bWidth.get(board));

            // ensure board's mine quantity
            Field bMineQty = Board.class.getDeclaredField("mineQty");
            bMineQty.setAccessible(true);
            assertEquals(6l, bMineQty.get(board));

            // ensure board's checked mine count
            Field bCheckedMineCount =
                    Board.class.getDeclaredField("checkedMineCount");
            bCheckedMineCount.setAccessible(true);
            assertEquals(0l, bCheckedMineCount.get(board));

            // ensure board's opened mine count
            Field bOpenedMineCount =
                    Board.class.getDeclaredField("openedMineCount");
            bOpenedMineCount.setAccessible(true);
            assertEquals(0l, bOpenedMineCount.get(board));

            // TODO: test board's grid
            // ensure board's grid
            Field bGrid = Board.class.getDeclaredField("grid");
            bGrid.setAccessible(true);
            Square[][] grid = (Square[][]) bGrid.get(board);

            // ensure grid size
            // ensure grid's length
            assertEquals(5, grid.length);

            // ensure grid length of each row
            for (Square[] gridRow : grid) {
                assertEquals(15, gridRow.length);
            }

            // ensure grid's squares
            // mine count
            long mineCount = 0l;

            // count number of mine
            for (int r = 0; r < 5; r++) {
                for (int c = 0; c < 15; c++) {
                    // ensure each square in grid is same with board's get square
                    assertEquals(board.getSquare(r, c), grid[r][c]);

                    // get square
                    Square square = grid[r][c];

                    // update mine count when square is mine square
                    if (square instanceof MineSquare) {
                        ++mineCount;
                    } else {
                        // count mine around square
                        byte mineAround = countMineAroundSquare(grid, r, c);

                        // when count mine around is 0, ensure square is empty square
                        // otherwise, ensure square is number square, ensure square's value
                        if (mineAround == 0) {
                            // ensure square is empty square
                            assertTrue(square instanceof EmptySquare);
                        } else {
                            // ensure square is number square
                            assertTrue(square instanceof NumberSquare);

                            // convert to number square
                            NumberSquare numSquare = (NumberSquare) square;

                            // ensure number square value
                            assertEquals(mineAround,
                                         (byte) numSquare.getValue());
                        }

                    }


                }
            }

            // ensure number of mine
            assertEquals(6l, mineCount);


        } catch (Exception e) {
            fail();
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
        } catch (Exception e) {
            // ensure exception is instance of BoardCreateUnable
            assertTrue(e instanceof BoardCreateUnable);

            // ensure exception message
            assertEquals(
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
            new Board(5, 0, 5);
        } catch (Exception e) {
            // ensure exception is instance of BoardCreateUnable
            assertTrue(e instanceof BoardCreateUnable);

            // ensure exception message
            assertEquals(
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
        } catch (Exception e) {
            // ensure exception is instance of BoardCreateUnable
            assertTrue(e instanceof BoardCreateUnable);

            // ensure exception message
            assertEquals(
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
        } catch (Exception e) {
            // ensure exception is instance of BoardCreateUnable
            assertTrue(e instanceof BoardCreateUnable);

            // ensure exception message
            assertEquals(
                    String.format(
                            "Board's mine must be more than 1 and less than %d",
                            5 * 15 - 1),
                    e.getMessage()
            );
        }
    }

    @Test
    public void testToggleSquareSuccessfully() {

    }

    private Square[][] createBoard() {

        Square[][] grid = new Square[8][8];

        // put mine
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


        return grid;
    }

    private MineSquare createMineSquare() {

        try {
            Constructor<MineSquare> constructor =
                    MineSquare.class.getDeclaredConstructor();
            constructor.setAccessible(true);

            // create new mine square object
            return constructor.newInstance();
        } catch (Exception e) {
            fail();
        }

        fail();
        return null;
    }

    private EmptySquare createEmptySquare() {

        try {
            Constructor<EmptySquare> constructor =
                    EmptySquare.class.getDeclaredConstructor();
            constructor.setAccessible(true);

            // create new empty square object
            return constructor.newInstance();
        } catch (Exception e) {
            fail();
        }

        fail();
        return null;
    }

    private NumberSquare createNumberSquare(byte value) {

        try {
            Constructor<NumberSquare> constructor =
                    NumberSquare.class.getDeclaredConstructor(byte.class);
            constructor.setAccessible(true);

            // create new number square object
            return constructor.newInstance(value);
        } catch (Exception e) {
            fail();
        }

        fail();
        return null;
    }

    private byte countMineAroundSquare(
            Square[][] grid,
            int row,
            int col) {
        // mine count
        byte count = 0;

        // check mine around square
        for (Direction dir : Direction.values()) {
            Square neighbor = getNeighborSquare(grid, row, col, dir);

            // when square is mine square, update mine count
            if (neighbor != null && neighbor instanceof MineSquare)
                count += 1;
        }

        return count;
    }

    private Square getNeighborSquare(Square[][] grid,
                                     int row,
                                     int col,
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
        if (isInGridSizeBound(r, c)) {
            // when square's index valid, return square
            return grid[r][c];
        } else {
            return null;
        }

    }

    private boolean isInGridSizeBound(int row, int col) {
        // check row
        if (row < 0 || row >= 5)
            return false;

        // check column
        if (col < 0 || col >= 15)
            return false;

        return true;
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
