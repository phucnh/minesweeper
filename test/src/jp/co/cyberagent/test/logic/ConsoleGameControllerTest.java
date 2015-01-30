package jp.co.cyberagent.test.logic;

import jp.co.cyberagent.components.Board;
import jp.co.cyberagent.components.PlayStatus;
import jp.co.cyberagent.components.exceptions.BoardCreateUnable;
import jp.co.cyberagent.logic.ConsoleGameController;
import jp.co.cyberagent.logic.exceptions.ConsoleControllerException;
import jp.co.cyberagent.ui.ConsoleView;

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
            assertNotNull(
                    "Failure - Create controller error, null object",
                    controller
            );

            // ensure controller attribute
            // ensure is not exit game
            Field isGameExit =
                    ConsoleGameController.class.getDeclaredField("isGameExit");
            isGameExit.setAccessible(true);

            assertFalse(
                    "Failure - After create controller, game exit",
                    isGameExit.getBoolean(controller)
            );

            // ensure play status is normal
            Field playStatus =
                    ConsoleGameController.class.getDeclaredField("playStatus");
            playStatus.setAccessible(true);

            assertEquals(
                    "Failure - After create controller, status is not normal",
                    PlayStatus.NORMAL, playStatus.get(controller));

        } catch (Exception e) {
            // test case not pass
            fail("Failure - Create controller error " + e.getMessage());
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
            assertNotNull(
                    "Failure - Create controller error, null object",
                    controller
            );

            // ensure game board is not created
            assertNull(
                    "Failure - Create controller error, board is null",
                    controller.getBoard());

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
            assertNotNull(
                    "Failure - After create new game, board still null",
                    controller.getBoard());

            // get board
            Board board = controller.getBoard();

            // ensure board size
            // get board size
            int[] boardSize = board.getSize();

            // ensure height
            assertEquals(
                    "Failure - Get board size height is not 15",
                    15,
                    boardSize[0]);

            // ensure width
            assertEquals(
                    "Failure - Get board size height is not 25",
                    25,
                    boardSize[1]);

            // ensure mine quantity
            Field bMineQty = Board.class.getDeclaredField("mineQty");
            bMineQty.setAccessible(true);
            assertEquals(
                    "Failure - Board mine quantity is not 15",
                    15l,
                    bMineQty.get(board));

        } catch (Exception e) {
            // test case not pass
            fail("Failure - Create new game error " + e.getMessage());
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
        assertNotNull(
                "Failure - Create controller error, null object",
                controller
        );

        // ensure game board is not created
        assertNull(
                "Failure - Create controller error, board is null",
                controller.getBoard()
        );

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
            fail("Failure - Create new game with height -1 " +
                    "not throw exception");

        } catch (Exception e) {

            // ensure exception is InvocationTargetException
            assertTrue(e instanceof InvocationTargetException);
            InvocationTargetException itEx = (InvocationTargetException) e;
            Throwable targetEx = itEx.getTargetException();

            // ensure target is instance of BoardCreateUnable
            assertTrue(
                    "Failure - The exception is not BoardCreateUnable",
                    targetEx instanceof BoardCreateUnable);

            // ensure exception message
            assertEquals(
                    "Failure - Create new game with height is -1 message wrong",
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
            settings.put("height", "60001");
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
            fail("Failure - Create new game with height 60001 " +
                    "not throw exception");

        } catch (Exception e) {

            // ensure exception is InvocationTargetException
            assertTrue(e instanceof InvocationTargetException);
            InvocationTargetException itEx = (InvocationTargetException) e;
            Throwable targetEx = itEx.getTargetException();

            // ensure target is instance of BoardCreateUnable
            assertTrue(
                    "Failure - The exception is not BoardCreateUnable",
                    targetEx instanceof BoardCreateUnable);

            // ensure exception message
            assertEquals(
                    "Failure - Create new game with height is " +
                            "60001 message wrong",
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
            fail("Failure - Create new game with width -1 " +
                    "not throw exception");

        } catch (Exception e) {

            // ensure exception is InvocationTargetException
            assertTrue(e instanceof InvocationTargetException);
            InvocationTargetException itEx = (InvocationTargetException) e;
            Throwable targetEx = itEx.getTargetException();

            // ensure target is instance of BoardCreateUnable
            assertTrue(
                    "Failure - The exception is not BoardCreateUnable",
                    targetEx instanceof BoardCreateUnable
            );

            // ensure exception message
            assertEquals(
                    "Failure - Create new game with width is -1 message wrong",
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
            fail("Failure - Create new game with width 27 " +
                    "not throw exception");

        } catch (Exception e) {

            // ensure exception is InvocationTargetException
            assertTrue(e instanceof InvocationTargetException);
            InvocationTargetException itEx = (InvocationTargetException) e;
            Throwable targetEx = itEx.getTargetException();

            // ensure target is instance of ConsoleControllerException
            assertTrue(
                    "Failure - The exception is not ConsoleControllerException",
                    targetEx instanceof ConsoleControllerException);

            // ensure exception message
            assertEquals(
                    "Failure - Create new game with width is 27 message wrong",
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

            assertFalse(
                    "Failure - After create controller, game is exit",
                    isGameExit.getBoolean(controller));

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
                    "Failure - Main menu message wrong",
                    "Please choose below options\n" +
                            "0. Exit game\n" +
                            "1. Create new game\n" +
                            "2. Setting\n",
                    out.toString());

            // ensure game is exit
            assertTrue(isGameExit.getBoolean(controller));

        } catch (Exception e) {
            // test case not pass
            fail("Failure - Main menu error " + e.getMessage());
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
                    "Failure - Main menu message wrong",
                    "Please choose below options\n" +
                            "0. Exit game\n" +
                            "1. Create new game\n" +
                            "2. Setting\n",
                    out.toString());

            // ensure game is not exit
            assertFalse(
                    "Failure - Main menu is 1 but game is exit",
                    isGameExit.getBoolean(controller));

        } catch (Exception e) {
            // test case not pass
            fail("Failure - Main menu error " + e.getMessage());
        }

    }

    /**
     * Test show main menu and get the user's input.
     * When user input 2, game setting
     */
    @Test
    public void testShowMainMenuInput2GameSetting() {

        // test show main menu, when input 2, game setting
        try {

            // set input
            // input choose game setting
            StringBuilder input = new StringBuilder("2\n");

            // input height
            input.append("25\n");

            // input width
            input.append("15\n");

            // input mine quantity
            input.append("20");

            // set input stream
            ByteArrayInputStream in =
                    new ByteArrayInputStream(input.toString().getBytes());
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

            // ensure expected message
            // build the expected message
            StringBuilder expected = new StringBuilder(
                    "Please choose below options\n" +
                    "0. Exit game\n" +
                    "1. Create new game\n" +
                    "2. Setting\n"
            );

            // ensure choose height message
            expected.append("Please, set the height\n");

            // ensure choose width message
            expected.append("Please, set the width\n");

            // ensure choose mine quantity message
            expected.append("Please, set the mine quantity\n");

            // ensure output message
            assertEquals(
                    "Failure - Main menu message wrong",
                    expected.toString(), out.toString());

            // ensure game is not exit
            assertFalse(
                    "Failure - Main menu is 2 but game is exit",
                    isGameExit.getBoolean(controller));

            // ensure game setting has been set
            Map<String, String> settings = controller.getSettings();

            // ensure setting height
            assertEquals(
                    "Failure - Setting height is not 25",
                    "25",
                    settings.get("height"));

            // ensure setting width
            assertEquals(
                    "Failure - Setting width is not 15",
                    "15",
                    settings.get("width"));

            // ensure setting mine quantity
            assertEquals(
                    "Failure - Setting mine quantity is not 20",
                    "20",
                    settings.get("mine_quantity"));

        } catch (Exception e) {
            // test case not pass
            fail("Failure - Main menu error " + e.getMessage());
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
                    "Failure - Main menu message wrong",
                    "Please choose below options\n" +
                            "0. Exit game\n" +
                            "1. Create new game\n" +
                            "2. Setting\n",
                    out.toString());

            // ensure game is exit
            Field isGameExit =
                    ConsoleGameController.class.getDeclaredField("isGameExit");
            isGameExit.setAccessible(true);
            assertTrue(
                    "Failure - Chosen option is not 1 or 2, " +
                            "but game is not exit",
                    isGameExit.getBoolean(controller));

        } catch (Exception e) {
            // test case not pass
            fail("Failure - Main menu error " + e.getMessage());
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
                            "1. Create new game\n" +
                            "2. Setting\n"
            );

            // show error message when input the empty
            expected.append(
                    ConsoleView.ANSI_RED +
                    "Please, choose one of main menu option"+
                    ConsoleView.ANSI_RESET +"\n");

            // re-display main menu request input message
            expected.append(
                    "Please choose below options\n" +
                            "0. Exit game\n" +
                            "1. Create new game\n" +
                            "2. Setting\n"
            );

            // ensure output
            // ensure main menu message
            assertEquals(
                    "Failure - Main menu message wrong",
                    expected.toString(),
                    out.toString());

        } catch (Exception e) {
            // test case not pass
            fail("Failure - Main menu error " + e.getMessage());
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
                    "1. Create new game\n" +
                    "2. Setting\n"
            );

            // show error message when input the non numeric
            expected.append(
                    ConsoleView.ANSI_RED +
                    "Please, input the number" +
                    ConsoleView.ANSI_RESET + "\n");

            // re-display main menu request input message
            expected.append(
                    "Please choose below options\n" +
                    "0. Exit game\n" +
                    "1. Create new game\n" +
                    "2. Setting\n"
            );

            // ensure output
            // ensure main menu message
            assertEquals(
                    "Failure - Main menu message wrong",
                    expected.toString(),
                    out.toString());

        } catch (Exception e) {
            // test case not pass
            fail("Failure - Main menu error " + e.getMessage());
        }

    }

    /**
     * Test validate chosen square input
     * When user input match with pattern, return true
     * Valid pattern: "0$|^[a-z]\\d+$".
     * Input 0 for exit choose square
     * Input match with "^[a-z]\d+$" for chosen square
     * Example:
     *  Valid: 0, a1, b10...
     *  Invalid: 1, a, $, ?a10
     */
    @Test
    public void testValidateChosenSquareInputValidCase() {

        // create the game controller
        ConsoleGameController controller = this.createTheController();

        // ensure controller is not null
        assertNotNull(
                "Failure - Controller create fail, null object",
                controller);

        // get function
        Method validateChosenSquareInput = null;
        try {
            validateChosenSquareInput =
                    ConsoleGameController.class.getDeclaredMethod(
                            "validateChosenSquareInput",
                            String.class
                    );
            validateChosenSquareInput.setAccessible(true);
        } catch (Exception e) {
            // test case not pass
            fail("Failure - Get function error " + e.getMessage());
        }

        try {

            // test validate chosen square input, input 0
            assertEquals(
                    "Failure - Input 0, but not valid",
                    true,
                    validateChosenSquareInput.invoke(controller, "0")
            );

            // test validate chosen square input, input a1
            assertEquals(
                    "Failure - Input a1, but not valid",
                    true,
                    validateChosenSquareInput.invoke(controller, "a1")
            );

            // test validate chosen square input, input b10
            assertEquals(
                    "Failure - Input b10, but not valid",
                    true,
                    validateChosenSquareInput.invoke(controller, "b10")
            );

        } catch (Exception e) {
            // test case not pass
            fail("Failure - Validate error " + e.getMessage());
        }

    }

    /**
     * Test validate chosen square input
     * When user input match with pattern, return true
     * Valid pattern: "0$|^[a-z]\\d+$".
     * Input 0 for exit choose square
     * Input match with "^[a-z]\d+$" for chosen square
     * Example:
     *  Valid: 0, a1, b10...
     *  Invalid: 1, a, $, ?a10
     */
    @Test
    public void testValidateChosenSquareInputInValidCase() {

        // create the game controller
        ConsoleGameController controller = this.createTheController();

        // ensure controller is not null
        assertNotNull(
                "Failure - Controller create fail, null object",
                controller);

        // get function
        Method validateChosenSquareInput = null;
        try {
            validateChosenSquareInput =
                    ConsoleGameController.class.getDeclaredMethod(
                            "validateChosenSquareInput",
                            String.class
                    );
            validateChosenSquareInput.setAccessible(true);
        } catch (Exception e) {
            // test case not pass
            fail("Failure - Get function error " + e.getMessage());
        }

        // test validate chosen square input, input 1
        try {

            assertEquals(
                    "Failure - Input 1, but valid",
                    false,
                    validateChosenSquareInput.invoke(controller, "1")
            );

            // ensure the error message
            assertEquals(
                    "Failure - Input 1 error message wrong",
                    ConsoleView.ANSI_RED +
                            "Please, input 0 or valid square " +
                            "choose pattern (Ex: a1, b12)" +
                            ConsoleView.ANSI_RESET + "\n",
                    out.toString()
            );

        } catch (Exception e) {
            // test case not pass
            fail("Failure - Validate error " + e.getMessage());
        } finally {
            out.reset();
        }

        // test validate chosen square input, input a
        try {

            assertEquals(
                    "Failure - Input a, but valid",
                    false,
                    validateChosenSquareInput.invoke(controller, "a")
            );

            // ensure the error message
            assertEquals(
                    "Failure - Input a error message wrong",
                    ConsoleView.ANSI_RED +
                            "Please, input 0 or valid square " +
                            "choose pattern (Ex: a1, b12)" +
                            ConsoleView.ANSI_RESET + "\n",
                    out.toString()
            );

        } catch (Exception e) {
            // test case not pass
            fail("Failure - Validate error " + e.getMessage());
        } finally {
            out.reset();
        }

        // test validate chosen square input, input $
        try {

            assertEquals(
                    "Failure - Input $, but valid",
                    false,
                    validateChosenSquareInput.invoke(controller, "$")
            );

            // ensure the error message
            assertEquals(
                    "Failure - Input $ error message wrong",
                    ConsoleView.ANSI_RED +
                            "Please, input 0 or valid square " +
                            "choose pattern (Ex: a1, b12)" +
                            ConsoleView.ANSI_RESET + "\n",
                    out.toString()
            );

        } catch (Exception e) {
            // test case not pass
            fail("Failure - Validate error " + e.getMessage());
        } finally {
            out.reset();
        }

        // test validate chosen square input, input ?a10
        try {

            assertEquals(
                    "Failure - Input ?a10, but valid",
                    false,
                    validateChosenSquareInput.invoke(controller, "?a10")
            );

            // ensure the error message
            assertEquals(
                    "Failure - Input ?a10 error message wrong",
                    ConsoleView.ANSI_RED +
                            "Please, input 0 or valid square " +
                            "choose pattern (Ex: a1, b12)"+
                            ConsoleView.ANSI_RESET +"\n",
                    out.toString()
            );

        } catch (Exception e) {
            // test case not pass
            fail("Failure - Validate error " + e.getMessage());
        } finally {
            out.reset();
        }

        // test validate chosen square input, input empty
        try {

            assertEquals(
                    "Failure - Input empty, but valid",
                    false,
                    validateChosenSquareInput.invoke(controller, "")
            );

            // ensure the error message
            assertEquals(
                    "Failure - Input empty error message wrong",
                    ConsoleView.ANSI_RED +
                            "Please, choose square for open" +
                            ConsoleView.ANSI_RESET +
                            "\n",
                    out.toString()
            );

        } catch (Exception e) {
            // test case not pass
            fail("Failure - Validate error " + e.getMessage());
        } finally {
            out.reset();
        }

    }

    /**
     * Test validate chosen square mode input
     * When user input match with pattern, return true
     * Valid pattern: "o$|x$". Input must be 'o' or 'x'
     * Example:
     *  Valid: 'o' or 'x'
     *  Invalid: when input different 'o' or 'x'
     */
    @Test
    public void testValidateChosenSquareModeInputValidCase() {

        // create the game controller
        ConsoleGameController controller = this.createTheController();

        // ensure controller is not null
        assertNotNull(
                "Failure - Controller create fail, null object",
                controller);

        // get function
        Method validateChosenSquareModeInput = null;
        try {
            validateChosenSquareModeInput =
                    ConsoleGameController.class.getDeclaredMethod(
                            "validateChosenSquareModeInput",
                            String.class
                    );
            validateChosenSquareModeInput.setAccessible(true);
        } catch (Exception e) {
            // test case not pass
            fail("Failure - Get function error " + e.getMessage());
        }

        try {

            // test validate chosen square input, input 'o'
            assertEquals(
                    "Failure - Input o but invalid",
                    true,
                    validateChosenSquareModeInput.invoke(controller, "o")
            );

            // test validate chosen square input, input 'x'
            assertEquals(
                    "Failure - Input x but invalid",
                    true,
                    validateChosenSquareModeInput.invoke(controller, "x")
            );

        } catch (Exception e) {
            // test case not pass
            fail("Failure - Validate error " + e.getMessage());
        }

    }

    /**
     * Test validate chosen square mode input
     * When user input match with pattern, return true
     * Valid pattern: "o$|x$". Input must be 'o' or 'x'
     * Example:
     *  Valid: 'o' or 'x'
     *  Invalid: when input different 'o' or 'x'
     */
    @Test
    public void testValidateChosenSquareModeInputInValidCase() {

        // create the game controller
        ConsoleGameController controller = this.createTheController();

        // ensure controller is not null
        assertNotNull(
                "Failure - Controller create fail, null object",
                controller
        );

        // get function
        Method validateChosenSquareModeInput = null;
        try {
            validateChosenSquareModeInput =
                    ConsoleGameController.class.getDeclaredMethod(
                            "validateChosenSquareModeInput",
                            String.class
                    );
            validateChosenSquareModeInput.setAccessible(true);
        } catch (Exception e) {
            // test case not pass
            fail("Failure - Get function error " + e.getMessage());
        }

        // test validate chosen square input, input 'o123'
        try {

            assertEquals(
                    "Failure - Input o123, but valid",
                    false,
                    validateChosenSquareModeInput.invoke(controller, "o123")
            );

            // ensure the error message
            assertEquals(
                    "Failure - Input o123 error message wrong",
                    ConsoleView.ANSI_RED +
                            "Please, input o or x value" +
                            ConsoleView.ANSI_RESET + "\n",
                    out.toString()
            );

        } catch (Exception e) {
            // test case not pass
            fail("Failure - Validate error " + e.getMessage());
        } finally {
            out.reset();
        }

        // test validate chosen square input, input 'a1'
        try {

            assertEquals(
                    "Failure - Input a1, but valid",
                    false,
                    validateChosenSquareModeInput.invoke(controller, "a1")
            );

            // ensure the error message
            assertEquals(
                    "Failure - Input a1 error message wrong",
                    ConsoleView.ANSI_RED +
                            "Please, input o or x value" +
                            ConsoleView.ANSI_RESET + "\n",
                    out.toString());

        } catch (Exception e) {
            // test case not pass
            fail("Failure - Validate error " + e.getMessage());
        } finally {
            out.reset();
        }

        // test validate chosen square input, input 'ox'
        try {

            assertEquals(
                    "Failure - Input ox, but valid",
                    false,
                    validateChosenSquareModeInput.invoke(controller, "ox")
            );

            // ensure the error message
            assertEquals(
                    "Failure - Input ox error message wrong",
                    ConsoleView.ANSI_RED +
                            "Please, input o or x value" +
                            ConsoleView.ANSI_RESET + "\n",
                    out.toString());

        } catch (Exception e) {
            // test case not pass
            fail("Failure - Validate error " + e.getMessage());
        } finally {
            out.reset();
        }

        // test validate chosen square input, input empty
        try {

            assertEquals(
                    "Failure - Input empty, but valid",
                    false,
                    validateChosenSquareModeInput.invoke(controller, "")
            );

            // ensure the error message
            assertEquals(
                    "Failure - Input empty error message wrong",
                    ConsoleView.ANSI_RED +
                            "Please, choose square for open or" +
                            " toggle mine check" +
                            ConsoleView.ANSI_RESET + "\n",
                    out.toString()
            );

        } catch (Exception e) {
            // test case not pass
            fail("Failure - Validate error " + e.getMessage());
        } finally {
            out.reset();
        }

    }

    /**
     * Test validate game setting input in valid case.
     * In game setting, user can choose board's height, width and mine quantity.
     * These setting is accept only numeric input
     */
    @Test
    public void testValidateGameSettingInputValidCase() {

        // create the game controller
        ConsoleGameController controller = this.createTheController();

        // ensure controller is not null
        assertNotNull(
                "Failure - Controller create fail, null object",
                controller);

        // get function
        Method validateGameSettingInput = null;
        try {
            validateGameSettingInput =
                    ConsoleGameController.class.getDeclaredMethod(
                            "validateGameSettingInput",
                            Map.class
                    );
            validateGameSettingInput.setAccessible(true);
        } catch (Exception e) {
            // test case not pass
            fail("Failure - Get function error " + e.getMessage());
        }

        // test validate game setting input, input numeric
        try {

            Map<String, String> settings = new HashMap<String, String>();
            settings.put("height", "150");
            settings.put("width", "20");
            settings.put("mine_quantity", "200");

            assertEquals(
                    "Failure - Input numeric, but invalid",
                    true,
                    validateGameSettingInput.invoke(controller, settings)
            );

        } catch (Exception e) {
            // test case not pass
            fail("Failure - Validate error " + e.getMessage());
        } finally {
            out.reset();
        }

    }

    /**
     * Test validate game setting's height input in invalid case.
     * In game setting, user can choose board's height, width and mine quantity.
     * These setting is accept only numeric input
     */
    @Test
    public void testValidateGameSettingInputInValidHeight() {

        // create the game controller
        ConsoleGameController controller = this.createTheController();

        // ensure controller is not null
        assertNotNull(
                "Failure - Controller create fail, null object",
                controller);

        // get function
        Method validateGameSettingInput = null;
        try {
            validateGameSettingInput =
                    ConsoleGameController.class.getDeclaredMethod(
                            "validateGameSettingInput",
                            Map.class
                    );
            validateGameSettingInput.setAccessible(true);
        } catch (Exception e) {
            // test case not pass
            fail("Failure - Get function error " + e.getMessage());
        }

        // test validate game setting input, input height is empty
        try {

            Map<String, String> settings = new HashMap<String, String>();
            settings.put("height", "");
            settings.put("width", "16");
            settings.put("mine_quantity", "200");

            assertEquals(
                    "Failure - Input height empty but valid",
                    false,
                    validateGameSettingInput.invoke(controller, settings)
            );

            // ensure the error message
            assertEquals(
                    "Failure - Input height empty error message error",
                    ConsoleView.ANSI_RED +
                            "Please, give input for game's height" +
                            ConsoleView.ANSI_RESET + "\n",
                    out.toString()
            );

        } catch (Exception e) {
            // test case not pass
            fail("Failure - Validate error " + e.getMessage());
        } finally {
            out.reset();
        }

        // test validate game setting input, input height is not numeric
        try {

            Map<String, String> settings = new HashMap<String, String>();
            settings.put("height", "a120");
            settings.put("width", "24");
            settings.put("mine_quantity", "200");

            assertEquals(
                    "Failure - Input height a120 but valid",
                    false,
                    validateGameSettingInput.invoke(controller, settings)
            );

            // ensure the error message
            assertEquals(
                    "Failure - Input height a120 error message error",
                    ConsoleView.ANSI_RED +
                            "Please, input numeric for game's height" +
                            ConsoleView.ANSI_RESET + "\n",
                    out.toString()
            );

        } catch (Exception e) {
            // test case not pass
            fail("Failure - Validate error " + e.getMessage());
        } finally {
            out.reset();
        }

    }

    /**
     * Test validate game setting's width input in invalid case.
     * In game setting, user can choose board's height, width and mine quantity.
     * These setting is accept only numeric input
     */
    @Test
    public void testValidateGameSettingInputInValidWidth() {

        // create the game controller
        ConsoleGameController controller = this.createTheController();

        // ensure controller is not null
        assertNotNull(
                "Failure - Controller create fail, null object",
                controller);

        // get function
        Method validateGameSettingInput = null;
        try {
            validateGameSettingInput =
                    ConsoleGameController.class.getDeclaredMethod(
                            "validateGameSettingInput",
                            Map.class
                    );
            validateGameSettingInput.setAccessible(true);
        } catch (Exception e) {
            // test case not pass
            fail("Failure - Get function error " + e.getMessage());
        }

        // test validate game setting input, input width is empty
        try {

            Map<String, String> settings = new HashMap<String, String>();
            settings.put("height", "105");
            settings.put("width", "");
            settings.put("mine_quantity", "200");

            assertEquals(
                    "Failure - Input width empty but valid",
                    false,
                    validateGameSettingInput.invoke(controller, settings)
            );

            // ensure the error message
            assertEquals(
                    "Failure - Input width empty error message wrong",
                    ConsoleView.ANSI_RED +
                            "Please, give input for game's width" +
                            ConsoleView.ANSI_RESET + "\n",
                    out.toString()
            );

        } catch (Exception e) {
            // test case not pass
            fail("Failure - Get function error " + e.getMessage());
        } finally {
            out.reset();
        }

        // test validate game setting input, input width is not numeric
        try {

            Map<String, String> settings = new HashMap<String, String>();
            settings.put("height", "120");
            settings.put("width", "11a@");
            settings.put("mine_quantity", "200");

            assertEquals(
                    "Failure - Input width a120 but valid",
                    false,
                    validateGameSettingInput.invoke(controller, settings)
            );

            // ensure the error message
            assertEquals(
                    "Failure - Input width a120 error message wrong",
                    ConsoleView.ANSI_RED +
                            "Please, input numeric for game's width" +
                            ConsoleView.ANSI_RESET + "\n",
                    out.toString()
            );

        } catch (Exception e) {
            // test case not pass
            fail("Failure - Validate error " + e.getMessage());
        } finally {
            out.reset();
        }

    }

    /**
     * Test validate game setting's mine quantity input in invalid case.
     * In game setting, user can choose board's height, width and mine quantity.
     * These setting is accept only numeric input
     */
    @Test
    public void testValidateGameSettingInputInValidMineQuantity() {

        // create the game controller
        ConsoleGameController controller = this.createTheController();

        // ensure controller is not null
        assertNotNull(
                "Failure - Controller create fail, null object",
                controller);

        // get function
        Method validateGameSettingInput = null;
        try {
            validateGameSettingInput =
                    ConsoleGameController.class.getDeclaredMethod(
                            "validateGameSettingInput",
                            Map.class
                    );
            validateGameSettingInput.setAccessible(true);
        } catch (Exception e) {
            // test case not pass
            fail("Failure - Get function error " + e.getMessage());
        }

        // test validate game setting input, input mine quantity is empty
        try {

            Map<String, String> settings = new HashMap<String, String>();
            settings.put("height", "10");
            settings.put("width", "20");
            settings.put("mine_quantity", "");

            assertEquals(
                    "Failure - Input mine quantity empty but valid",
                    false,
                    validateGameSettingInput.invoke(controller, settings)
            );

            // ensure the error message
            assertEquals(
                    "Failure - Input mine quantity empty error message wrong",
                    ConsoleView.ANSI_RED +
                            "Please, give input for game's mine quantity"+
                            ConsoleView.ANSI_RESET + "\n",
                    out.toString()
            );

        } catch (Exception e) {
            // test case not pass
            fail("Failure - Get function error " + e.getMessage());
        } finally {
            out.reset();
        }

        // test validate game setting input, input mine quantity is not numeric
        try {

            Map<String, String> settings = new HashMap<String, String>();
            settings.put("height", "12");
            settings.put("width", "15");
            settings.put("mine_quantity", "a$$$");

            assertEquals(
                    "Failure - Input mine quantity a$$$ but valid",
                    false,
                    validateGameSettingInput.invoke(controller, settings)
            );

            // ensure the error message
            assertEquals(
                    "Failure - Input mine quantity a$$$ error message wrong",
                    ConsoleView.ANSI_RED +
                            "Please, input numeric for game's mine quantity" +
                            ConsoleView.ANSI_RESET + "\n",
                    out.toString()
            );

        } catch (Exception e) {
            // test case not pass
            fail("Failure - Get function error " + e.getMessage());
        } finally {
            out.reset();
        }

    }

    private ConsoleGameController createTheController() {

        // create the game board
        // create the game controller
        ConsoleGameController controller = new ConsoleGameController();

        // ensure controller not null
        assertNotNull(controller);

        // ensure game board is not created
        assertNull(controller.getBoard());

        // prepare settings for create new game
        HashMap<String, String> settings = new HashMap<String, String>();
        settings.put("height", "15");
        settings.put("width", "20");
        settings.put("mine_quantity", "15");

        try {
            // get create new game function
            Method createNewGame = ConsoleGameController
                    .class
                    .getDeclaredMethod("createNewGame", Map.class);
            createNewGame.setAccessible(true);

            // run create new game
            createNewGame.invoke(controller, settings);
        } catch (Exception e) {
            // test case not pass
            fail("Failure - Create controller error " + e.getMessage());
        }

         return controller;

    }

}
