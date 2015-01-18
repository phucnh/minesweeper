package jp.co.cyberagent.test.logic;

import jp.co.cyberagent.components.Board;
import jp.co.cyberagent.components.PlayStatus;
import jp.co.cyberagent.components.exceptions.BoardCreateUnable;
import jp.co.cyberagent.logic.ConsoleGameController;
import jp.co.cyberagent.logic.exceptions.ConsoleControllerException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

/**
 * Created by phucnh on 15/01/17.
 */
public class ConsoleGameControllerTest {

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
     * Test create Controller object successfully
     */
    @Test
    public void testCreateConsoleControllerSuccessfully() {

        try {

            // create the game controller
            ConsoleGameController controller = new ConsoleGameController();

            // ensure controller not null
            assertNotNull(controller);

            // ensure controller attribute
            // ensure is not exit game
            Field isGameExit =
                    ConsoleGameController.class.getDeclaredField("isGameExit");
            isGameExit.setAccessible(true);

            assertFalse(isGameExit.getBoolean(controller));

            // ensure play status is normal
            Field playStatus =
                    ConsoleGameController.class.getDeclaredField("playStatus");
            playStatus.setAccessible(true);

            assertEquals(PlayStatus.NORMAL, playStatus.get(controller));

        } catch (Exception e) {
            // test case not pass
            fail();
        } finally {
            System.setIn(System.in);
        }

    }

    /**
     * Test create new game successfully
     */
    @Test
    public void testCreateNewGameSuccessfully() {

        try {

            // create the game controller
            ConsoleGameController controller = new ConsoleGameController();

            // ensure controller not null
            assertNotNull(controller);

            // ensure game board is not created
            assertNull(controller.getBoard());

            // prepare settings for create new game
            HashMap<String, String> settings = new HashMap<String, String>();
            settings.put("height", "15");
            settings.put("width", "25");
            settings.put("mine_quantity", "15");

            // get create new game function
            Method createNewGame = ConsoleGameController
                    .class
                    .getDeclaredMethod("createNewGame", Map.class);
            createNewGame.setAccessible(true);

            // run create new game
            createNewGame.invoke(controller, settings);

            // ensure the board has been created
            assertNotNull(controller.getBoard());

            // get board
            Board board = controller.getBoard();

            // ensure board size
            // get board size
            int[] boardSize = board.getSize();

            // ensure height
            assertEquals(15, boardSize[0]);

            // ensure width
            assertEquals(25, boardSize[1]);

            // ensure mine quantity
            Field bMineQty = Board.class.getDeclaredField("mineQty");
            bMineQty.setAccessible(true);
            assertEquals(15l, bMineQty.get(board));

        } catch (Exception e) {
            // test case not pass
            fail();
        }

    }

    /**
     * Test create new game failed,
     * height and width out of bound
     */
    @Test
    public void testCreateNewGameFailedSizeOutOfBound() {

        // create the game controller
        ConsoleGameController controller = new ConsoleGameController();

        // ensure controller not null
        assertNotNull(controller);

        // ensure game board is not created
        assertNull(controller.getBoard());

        // create new game with settings height less than 0
        try {

            // prepare settings for create new game
            HashMap<String, String> settings = new HashMap<String, String>();
            settings.put("height", "-1");
            settings.put("width", "25");
            settings.put("mine_quantity", "15");

            // get create new game function
            Method createNewGame = ConsoleGameController
                    .class
                    .getDeclaredMethod("createNewGame", Map.class);
            createNewGame.setAccessible(true);

            // run create new game
            createNewGame.invoke(controller, settings);

            // test case not pass
            fail();

        } catch (Exception e) {

            // ensure exception is InvocationTargetException
            assertTrue(e instanceof InvocationTargetException);
            InvocationTargetException itEx = (InvocationTargetException) e;
            Throwable targetEx = itEx.getTargetException();

            // ensure target is instance of BoardCreateUnable
            assertTrue(targetEx instanceof BoardCreateUnable);

            // ensure exception message
            assertEquals(
                    String.format("Board's height must be from %d to %d",
                            Board.MIN_HEIGHT,
                            Board.MAX_HEIGHT),
                    targetEx.getMessage()
            );

        }

        // create new game with settings height is board's max height
        try {

            // prepare settings for create new game
            HashMap<String, String> settings = new HashMap<String, String>();
            settings.put("height", "60000");
            settings.put("width", "25");
            settings.put("mine_quantity", "15");

            // get create new game function
            Method createNewGame = ConsoleGameController
                    .class
                    .getDeclaredMethod("createNewGame", Map.class);
            createNewGame.setAccessible(true);

            // run create new game
            createNewGame.invoke(controller, settings);

            // test case not pass
            fail();

        } catch (Exception e) {

            // ensure exception is InvocationTargetException
            assertTrue(e instanceof InvocationTargetException);
            InvocationTargetException itEx = (InvocationTargetException) e;
            Throwable targetEx = itEx.getTargetException();

            // ensure target is instance of BoardCreateUnable
            assertTrue(targetEx instanceof BoardCreateUnable);

            // ensure exception message
            assertEquals(
                    String.format("Board's height must be from %d to %d",
                            Board.MIN_HEIGHT,
                            Board.MAX_HEIGHT),
                    targetEx.getMessage()
            );

        }

        // create new game with settings width less than 0
        try {

            // prepare settings for create new game
            HashMap<String, String> settings = new HashMap<String, String>();
            settings.put("height", "50");
            settings.put("width", "-1");
            settings.put("mine_quantity", "15");

            // get create new game function
            Method createNewGame = ConsoleGameController
                    .class
                    .getDeclaredMethod("createNewGame", Map.class);
            createNewGame.setAccessible(true);

            // run create new game
            createNewGame.invoke(controller, settings);

            // test case not pass
            fail();

        } catch (Exception e) {

            // ensure exception is InvocationTargetException
            assertTrue(e instanceof InvocationTargetException);
            InvocationTargetException itEx = (InvocationTargetException) e;
            Throwable targetEx = itEx.getTargetException();

            // ensure target is instance of BoardCreateUnable
            assertTrue(targetEx instanceof BoardCreateUnable);

            // ensure exception message
            assertEquals(
                    String.format("Board's width must be from %d to %d",
                            Board.MIN_HEIGHT,
                            Board.MAX_HEIGHT),
                    targetEx.getMessage()
            );

        }

        // create new game with settings width is 27
        try {

            // prepare settings for create new game
            HashMap<String, String> settings = new HashMap<String, String>();
            settings.put("height", "50");
            settings.put("width", "27");
            settings.put("mine_quantity", "15");

            // get create new game function
            Method createNewGame = ConsoleGameController
                    .class
                    .getDeclaredMethod("createNewGame", Map.class);
            createNewGame.setAccessible(true);

            // run create new game
            createNewGame.invoke(controller, settings);

            // test case not pass
            fail();

        } catch (Exception e) {

            // ensure exception is InvocationTargetException
            assertTrue(e instanceof InvocationTargetException);
            InvocationTargetException itEx = (InvocationTargetException) e;
            Throwable targetEx = itEx.getTargetException();

            // ensure target is instance of ConsoleControllerException
            assertTrue(targetEx instanceof ConsoleControllerException);

            // ensure exception message
            assertEquals(
                    "In console mode, board width " +
                            "must less than or equal 26 columns",
                    targetEx.getMessage()
            );

        }

    }

    /**
     * Test show main menu and get the user's input.
     * When user input 0, exit game
     */
    @Test
    public void testShowMainMenuInput0GameExit() {

        // test show main menu, when input 0, game exit
        try {

            // set input
            ByteArrayInputStream in = new ByteArrayInputStream("0".getBytes());
            System.setIn(in);

            // create the game controller
            ConsoleGameController controller = new ConsoleGameController();

            // ensure is not exit game
            Field isGameExit =
                    ConsoleGameController.class.getDeclaredField("isGameExit");
            isGameExit.setAccessible(true);

            assertFalse(isGameExit.getBoolean(controller));

            // get show main menu function
            Method showMainMenu = ConsoleGameController
                    .class
                    .getDeclaredMethod("showMainMenu");
            showMainMenu.setAccessible(true);

            // run show main menu
            showMainMenu.invoke(controller);

            // ensure output
            // ensure main menu message
            assertEquals(
                    "Please choose below options\n" +
                            "0. Exit game\n" +
                            "1. Create new game\n",
                    out.toString());

            // ensure game is exit
            assertTrue(isGameExit.getBoolean(controller));

        } catch (Exception e) {
            // test case not pass
            fail();
        } finally {
            out.reset();
        }

    }

    /**
     * Test show main menu and get the user's input.
     * When user input 1, game continue
     */
    @Test
    public void testShowMainMenuInput1GameContinue() {

        // test show main menu, when input 1, game continue
        try {

            // set input
            ByteArrayInputStream in = new ByteArrayInputStream("1".getBytes());
            System.setIn(in);

            // create the game controller
            ConsoleGameController controller = new ConsoleGameController();

            // ensure is not exit game
            Field isGameExit =
                    ConsoleGameController.class.getDeclaredField("isGameExit");
            isGameExit.setAccessible(true);

            // get show main menu function
            Method showMainMenu = ConsoleGameController
                    .class
                    .getDeclaredMethod("showMainMenu");
            showMainMenu.setAccessible(true);

            // run show main menu
            showMainMenu.invoke(controller);

            // ensure output
            // ensure main menu message
            assertEquals(
                    "Please choose below options\n" +
                            "0. Exit game\n" +
                            "1. Create new game\n",
                    out.toString());

            // ensure game is exit
            assertFalse(isGameExit.getBoolean(controller));

        } catch (Exception e) {
            // test case not pass
            fail();
        }

    }

    /**
     * Test show main menu and get the user's input.
     * When user input another option (not 0 or 1), game exit
     */
    @Test
    public void testShowMainMenuInputOtherGameExit() {

        // test show main menu, when input another option, game exit
        try {

            // set input
            ByteArrayInputStream in = new ByteArrayInputStream("5".getBytes());
            System.setIn(in);

            // create the game controller
            ConsoleGameController controller = new ConsoleGameController();

            // get show main menu function
            Method showMainMenu = ConsoleGameController
                    .class
                    .getDeclaredMethod("showMainMenu");
            showMainMenu.setAccessible(true);

            // run show main menu
            showMainMenu.invoke(controller);

            // ensure output
            // ensure main menu message
            assertEquals(
                    "Please choose below options\n" +
                            "0. Exit game\n" +
                            "1. Create new game\n",
                    out.toString());

            // ensure game is exit
            Field isGameExit =
                    ConsoleGameController.class.getDeclaredField("isGameExit");
            isGameExit.setAccessible(true);
            assertTrue(isGameExit.getBoolean(controller));

        } catch (Exception e) {
            // test case not pass
            fail();
        }

    }

    /**
     * Test show main menu and get the user's input.
     * When user input empty, show the input error message
     */
    @Test
    public void testShowMainMenuInputEmpty() {

        // test show main menu, when input another option, game exit
        try {

            // set input
            // input the empty
            StringBuilder input = new StringBuilder("\n");

            // input 0 for terminal application
            input.append("0");

            // create input stream and set input
            ByteArrayInputStream in =
                    new ByteArrayInputStream(input.toString().getBytes());
            System.setIn(in);

            // create the game controller
            ConsoleGameController controller = new ConsoleGameController();

            // get show main menu function
            Method showMainMenu = ConsoleGameController
                    .class
                    .getDeclaredMethod("showMainMenu");
            showMainMenu.setAccessible(true);

            // run show main menu
            showMainMenu.invoke(controller);

            // the expected message
            // show main menu request input message
            StringBuilder expected = new StringBuilder(
                    "Please choose below options\n" +
                            "0. Exit game\n" +
                            "1. Create new game\n");

            // show error message when input the empty
            expected.append("Please, choose one of main menu option\n");

            // re-display main menu request input message
            expected.append(
                    "Please choose below options\n" +
                            "0. Exit game\n" +
                            "1. Create new game\n"
            );

            // ensure output
            // ensure main menu message
            assertEquals(expected.toString(), out.toString());

        } catch (Exception e) {
            // test case not pass
            fail();
        }

    }

    /**
     * Test show main menu and get the user's input.
     * When user input non numeric, show the input error message
     */
    @Test
    public void testShowMainMenuInputNotNumeric() {

        // test show main menu, when input another option, game exit
        try {

            // set input
            // input aa
            StringBuilder input = new StringBuilder("aa\n");

            // input 0 for terminal application
            input.append("0");

            // create input stream and set input
            ByteArrayInputStream in =
                    new ByteArrayInputStream(input.toString().getBytes());
            System.setIn(in);

            // create the game controller
            ConsoleGameController controller = new ConsoleGameController();

            // get show main menu function
            Method showMainMenu = ConsoleGameController
                    .class
                    .getDeclaredMethod("showMainMenu");
            showMainMenu.setAccessible(true);

            // run show main menu
            showMainMenu.invoke(controller);

            // the expected message
            // show main menu request input message
            StringBuilder expected = new StringBuilder(
                    "Please choose below options\n" +
                    "0. Exit game\n" +
                    "1. Create new game\n");

            // show error message when input the non numeric
            expected.append("Please, input the number\n");

            // re-display main menu request input message
            expected.append(
                    "Please choose below options\n" +
                    "0. Exit game\n" +
                    "1. Create new game\n"
            );

            // ensure output
            // ensure main menu message
            assertEquals(expected.toString(), out.toString());

        } catch (Exception e) {
            // test case not pass
            fail();
        }

    }

}
