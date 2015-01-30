package jp.co.cyberagent.test.view;

import static org.mockito.Mockito.*;

import jp.co.cyberagent.components.*;
import jp.co.cyberagent.ui.ConsoleView;

import jp.co.cyberagent.ui.GameView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by phucnh on 15/01/14.
 */
public class ConsoleViewTest {

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        System.setOut(new PrintStream(out));
    }

    @After
    public void cleanUp() {
        System.setOut(null);
    }

    /**
     * Test show main menu function: successful case
     */
    @Test
    public void testShowMainMenuSuccessfully() {

        try {

            // set input
            ByteArrayInputStream in = new ByteArrayInputStream("0".getBytes());
            System.setIn(in);

            // create a view
            ConsoleView view = new ConsoleView(
                    new BufferedReader(new InputStreamReader(System.in)),
                    new BufferedWriter(new OutputStreamWriter(System.out))
            );

            // test function
            String chosen = view.mainMenu();

            // ensure main menu message
            assertEquals(
                    "Failure - Main menu message incorrect",
                    "Please choose below options\n" +
                            "0. Exit game\n" +
                            "1. Create new game\n" +
                            "2. Setting\n",
                    out.toString()
            );

            // ensure chosen value
            assertEquals(
                    "Failure - The input value is not 0",
                    "0",
                    chosen
            );

        } catch (Exception e) {
            // test case not pass
            fail("Failure - Main menu function error " + e.getMessage());
        } finally {
            System.setIn(System.in);
        }

    }

    /**
     * Test game settings function.
     * Show the input request message
     * Get input from user
     */
    @Test
    public void testGameSettingSuccessfully() {

        try {

            // set input
            // input height
            StringBuilder input = new StringBuilder("10\n");

            // input width
            input.append("15\n");

            // input mine quantity
            input.append("20");

            // set input stream
            ByteArrayInputStream in =
                    new ByteArrayInputStream(input.toString().getBytes());
            System.setIn(in);

            // create a view
            ConsoleView view = new ConsoleView(
                    new BufferedReader(new InputStreamReader(System.in)),
                    new BufferedWriter(new OutputStreamWriter(System.out))
            );

            // test function
            Map<String, String> settings = view.gameSetting();

            // ensure game setting message
            assertEquals(
                    "Failure - Setting message wrong",
                    "Please, set the height\n" +
                            "Please, set the width\n" +
                            "Please, set the mine quantity\n",
                    out.toString()
            );

            // ensure input setting height
            assertEquals(
                    "Failure - Input height is not 10",
                    "10",
                    settings.get("height")
            );

            // ensure input setting width
            assertEquals(
                    "Failure - Input width is not 15",
                    "15",
                    settings.get("width")
            );

            // ensure input setting mine quantity
            assertEquals(
                    "Failure - Input mine quantity is not 20",
                    "20",
                    settings.get("mine_quantity")
            );

        } catch (Exception e) {
            // test case not pass
            fail("Failure - Game setting error " + e.getMessage());
        } finally {
            System.setIn(System.in);
        }

    }

    /**
     * Test one win
     */
    @Test
    public void testOnWin() {

        // create a view
        ConsoleView view = new ConsoleView(
                new BufferedReader(new InputStreamReader(System.in)),
                new BufferedWriter(new OutputStreamWriter(System.out))
        );

        try {

            // test function
            view.onWin();

            // ensure main menu message
            assertEquals(
                    "Failure - Win message incorrect",
                    "You win, congratulation!\n",
                    out.toString());

        } catch (Exception e) {
            // test case not pass
            fail("Failure - On win error " + e.getMessage());
        }

    }

    /**
     * Test on lose
     */
    @Test
    public void testOnLose() {

        // create a view
        ConsoleView view = new ConsoleView(
                new BufferedReader(new InputStreamReader(System.in)),
                new BufferedWriter(new OutputStreamWriter(System.out))
        );

        try {

            // test function
            view.onLose();

            // ensure main menu message
            assertEquals(
                    "Failure - Lose message incorrect",
                    "Oops! You opened the mine! Game over\n",
                    out.toString());

        } catch (Exception e) {
            // test case not pass
            fail("Failure - On lose error " + e.getMessage());
        }

    }

    /**
     * Test choose square: successful case
     */
    @Test
    public void testChooseSquareSuccessfully() {

        try {

            // set input
            ByteArrayInputStream in = new ByteArrayInputStream("0".getBytes());
            System.setIn(in);

            // create a view
            ConsoleView view = new ConsoleView(
                    new BufferedReader(new InputStreamReader(System.in)),
                    new BufferedWriter(new OutputStreamWriter(System.out))
            );

            // test function
            String chosen = view.chooseSquare();

            // ensure choose square message
            assertEquals(
                    "Failure - Chosen square message incorrect",
                    "Please, choose square to open or " +
                            "mine check (0 for back to Main Menu)\n",
                    out.toString());

            // ensure chosen value
            assertEquals(
                    "Failure - The input is not 0",
                    "0",
                    chosen);

        } catch (Exception e) {
            // test case not pass
            fail("Failure - Choose square error " + e.getMessage());
        } finally {
            System.setIn(System.in);
        }

    }

    /**
     * Test choose square: failed
     */
    @Test(expected = IOException.class)
    public void testChooseSquareFailed() throws IOException {

        // Mock console view
        ConsoleView view = mock(ConsoleView.class);
        when(view.chooseSquare()).thenThrow(new IOException());

        // test function
        view.chooseSquare();

    }

    /**
     * Test choose square mode: successful case
     */
    @Test
    public void testChooseSquareModeSuccessfully() {

        try {

            // set input
            ByteArrayInputStream in = new ByteArrayInputStream("0".getBytes());
            System.setIn(in);

            // create a view
            ConsoleView view = new ConsoleView(
                    new BufferedReader(new InputStreamReader(System.in)),
                    new BufferedWriter(new OutputStreamWriter(System.out))
            );

            // test function
            String chosen = view.chooseSquareMode();

            // ensure choose square mode
            assertEquals(
                    "Failure - chosen open square mode message incorrect",
                    "Please, choose open (o) or toggle mine checked (x)\n",
                    out.toString()
            );

            // ensure chosen value
            assertEquals(
                    "Failure - input value is not 0",
                    "0",
                    chosen
            );

        } catch (Exception e) {
            // test case not pass
            fail("Failure - Choose square mode error " + e.getMessage());
        } finally {
            System.setIn(System.in);
        }

    }

    /**
     * Test choose square: failed
     */
    @Test(expected = IOException.class)
    public void testChooseSquareModeFailed() throws IOException {

        // Mock console view
        ConsoleView view = mock(ConsoleView.class);
        when(view.chooseSquareMode()).thenThrow(new IOException());

        // test function
        view.chooseSquareMode();

    }

    /**
     * Test show message: successful case
     */
    @Test
    public void testShowMessageSuccessfully() {

        // test show error message
        try {
            // create a view
            ConsoleView view = new ConsoleView(
                    new BufferedReader(new InputStreamReader(System.in)),
                    new BufferedWriter(new OutputStreamWriter(System.out))
            );

            // test function
            view.showMessage("test show error message", GameView.MSG_ERR);

            // ensure choose square mode
            assertEquals(
                    "Failure - Output message is incorrect",
                    ConsoleView.ANSI_RED +
                            "test show error message" +
                            ConsoleView.ANSI_RESET + "\n",
                    out.toString()
            );

        } catch (Exception e) {
            // test case not pass
            fail("Failure - Show error message error " + e.getMessage());
        } finally {
            out.reset();
        }

        // test show warning message
        try {
            // create a view
            ConsoleView view = new ConsoleView(
                    new BufferedReader(new InputStreamReader(System.in)),
                    new BufferedWriter(new OutputStreamWriter(System.out))
            );

            // test function
            view.showMessage("test show warning message", GameView.MSG_WRN);

            // ensure choose square mode
            assertEquals(
                    "Failure - Output message is incorrect",
                    ConsoleView.ANSI_YELLOW +
                            "test show warning message" +
                            ConsoleView.ANSI_RESET + "\n",
                    out.toString());

        } catch (Exception e) {
            // test case not pass
            fail("Failure - Show warning error " + e.getMessage());
        }

    }

    /**
     * Test choose square: failed
     */
    @Test(expected = IOException.class)
    public void testShowMessageFailed() throws IOException {

        // Mock console view
        ConsoleView view = mock(ConsoleView.class);
        doThrow(new IOException()).when(view).showMessage("show failed");

        // test function
        view.showMessage("show failed");

    }

    /**
     * Test show board: successful case
     */
    @Test
    public void testShowBoardSuccessfully() {

        // test display board, all squares are closed
        try {

            // create new board
            Board board = new Board(8, 8, 10);

            // ensure board not null
            assertNotNull("Failure - After create, board is null", board);

            // create the gird
            Square[][] grid = makeBoard();
            // set board
            Field bGrid = Board.class.getDeclaredField("grid");
            bGrid.setAccessible(true);
            bGrid.set(board, grid);

            // create a view
            ConsoleView view = new ConsoleView(
                    new BufferedReader(new InputStreamReader(System.in)),
                    new BufferedWriter(new OutputStreamWriter(System.out))
            );

            // test function
            view.displayBoard(board);

            // ensure display board when all squares are closed
            assertEquals(
                    "Failure - Display board incorrect",
                    "  abcdefgh\n" +
                            "0 ????????\n" +
                            "1 ????????\n" +
                            "2 ????????\n" +
                            "3 ????????\n" +
                            "4 ????????\n" +
                            "5 ????????\n" +
                            "6 ????????\n" +
                            "7 ????????\n",
                    out.toString());

            // reset output
            out.reset();

            // open all squares
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {

                    if (!board.getSquare(r, c).isOpened())
                        board.openSquare(r, c);
                }
            }

            // test function
            view.displayBoard(board);

            // ensure display board when all squares are opened
            assertEquals(
                    "Failure - Display board incorrect",
                    "  abcdefgh\n" +
                            "0     111 \n" +
                            "1     1x1 \n" +
                            "2     1221\n" +
                            "3 11 112x1\n" +
                            "4 x1 1x211\n" +
                            "5 22112321\n" +
                            "6 1x322xx2\n" +
                            "7 12xx223x\n",
                    out.toString());

        } catch (Exception e) {
            // test case not pass
            fail("Failure - Display board error " + e.getMessage());
        } finally {
            // reset out
            out.reset();
        }

        // test display board, open square, toggle check square
        try {

            // create new board
            Board board = new Board(8, 8, 10);

            // ensure board not null
            assertNotNull("Failure - After create, board is null", board);

            // create the gird
            Square[][] grid = makeBoard();
            // set board
            Field bGrid = Board.class.getDeclaredField("grid");
            bGrid.setAccessible(true);
            bGrid.set(board, grid);

            // create a view
            ConsoleView view = new ConsoleView(
                    new BufferedReader(new InputStreamReader(System.in)),
                    new BufferedWriter(new OutputStreamWriter(System.out))
            );

            // open some square
            board.openSquare(0, 0);
            board.openSquare(4, 0);
            board.openSquare(5, 5);

            // toggle mine check
            board.toggleMineCheckSquare(1, 5);
            board.toggleMineCheckSquare(4, 5);

            // test function
            view.displayBoard(board);

            // ensure display board
            assertEquals(
                    "Failure - Display board incorrect",
                    "  abcdefgh\n" +
                            "0     1???\n" +
                            "1     1x??\n" +
                            "2     1???\n" +
                            "3 11 11???\n" +
                            "4 x1 1?x??\n" +
                            "5 ?211?3??\n" +
                            "6 ????????\n" +
                            "7 ????????\n",
                    out.toString());

        } catch (Exception e) {
            // test case not pass
            fail("Failure - Display board error " + e.getMessage());
        } finally {
            out.reset();
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
            fail("Failure - Create mine square error " + e.getMessage());
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
            fail("Failure - Create empty square error " + e.getMessage());
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
            fail("Failure - Create number square error " + e.getMessage());
        }

        // when square is not created successfully, test case not pass
        fail();

        return null;

    }

}
