package jp.co.cyberagent.test.components;

import jp.co.cyberagent.components.Board;

import jp.co.cyberagent.components.exceptions.BoardCreateUnable;

import org.junit.Test;

import java.lang.reflect.Field;

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

}