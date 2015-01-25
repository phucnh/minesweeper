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
                            "1. Create new game\n" +
                            "2. Setting\n",
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
                            "1. Create new game\n" +
                            "2. Setting\n",
                    out.toString());

            // ensure game is not exit
            assertFalse(isGameExit.getBoolean(controller));

        } catch (Exception e) {
            // test case not pass
            fail();
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
            assertEquals(expected.toString(), out.toString());

            // ensure game is not exit
            assertFalse(isGameExit.getBoolean(controller));

            // ensure game setting has been set
            Map<String, String> settings = controller.getSettings();

            // ensure setting height
            assertEquals("25", settings.get("height"));

            // ensure setting width
            assertEquals("15", settings.get("width"));

            // ensure setting mine quantity
            assertEquals("20", settings.get("mine_quantity"));

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
                            "1. Create new game\n" +
                            "2. Setting\n",
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
                            "1. Create new game\n" +
                            "2. Setting\n"
            );

            // show error message when input the empty
            expected.append("Please, choose one of main menu option\n");

            // re-display main menu request input message
            expected.append(
                    "Please choose below options\n" +
                            "0. Exit game\n" +
                            "1. Create new game\n" +
                            "2. Setting\n"
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
                    "1. Create new game\n" +
                    "2. Setting\n"
            );

            // show error message when input the non numeric
            expected.append("Please, input the number\n");

            // re-display main menu request input message
            expected.append(
                    "Please choose below options\n" +
                    "0. Exit game\n" +
                    "1. Create new game\n" +
                    "2. Setting\n"
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
        assertNotNull(controller);

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
            fail();
        }

        try {

            // test validate chosen square input, input 0
            assertEquals(
                    true,
                    validateChosenSquareInput.invoke(controller, "0")
            );

            // test validate chosen square input, input a1
            assertEquals(
                    true,
                    validateChosenSquareInput.invoke(controller, "a1")
            );

            // test validate chosen square input, input b10
            assertEquals(
                    true,
                    validateChosenSquareInput.invoke(controller, "b10")
            );

        } catch (Exception e) {
            // test case not pass
            fail();
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
        assertNotNull(controller);

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
            fail();
        }

        // test validate chosen square input, input 1
        try {

            assertEquals(
                    false,
                    validateChosenSquareInput.invoke(controller, "1")
            );

            // ensure the error message
            assertEquals(
                    "Please, input 0 or valid square " +
                            "choose pattern (Ex: a1, b12)\n",
                    out.toString()
            );

        } catch (Exception e) {
            // test case not pass
            fail();
        } finally {
            out.reset();
        }

        // test validate chosen square input, input a
        try {

            assertEquals(
                    false,
                    validateChosenSquareInput.invoke(controller, "a")
            );

            // ensure the error message
            assertEquals(
                    "Please, input 0 or valid square " +
                            "choose pattern (Ex: a1, b12)\n",
                    out.toString()
            );

        } catch (Exception e) {
            // test case not pass
            fail();
        } finally {
            out.reset();
        }

        // test validate chosen square input, input $
        try {

            assertEquals(
                    false,
                    validateChosenSquareInput.invoke(controller, "$")
            );

            // ensure the error message
            assertEquals(
                    "Please, input 0 or valid square " +
                            "choose pattern (Ex: a1, b12)\n",
                    out.toString()
            );

        } catch (Exception e) {
            // test case not pass
            fail();
        } finally {
            out.reset();
        }

        // test validate chosen square input, input ?a10
        try {

            assertEquals(
                    false,
                    validateChosenSquareInput.invoke(controller, "?a10")
            );

            // ensure the error message
            assertEquals(
                    "Please, input 0 or valid square " +
                            "choose pattern (Ex: a1, b12)\n",
                    out.toString()
            );

        } catch (Exception e) {
            // test case not pass
            fail();
        } finally {
            out.reset();
        }

        // test validate chosen square input, input empty
        try {

            assertEquals(
                    false,
                    validateChosenSquareInput.invoke(controller, "")
            );

            // ensure the error message
            assertEquals("Please, choose square for open\n", out.toString()
            );

        } catch (Exception e) {
            // test case not pass
            fail();
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
        assertNotNull(controller);

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
            fail();
        }

        try {

            // test validate chosen square input, input 'o'
            assertEquals(
                    true,
                    validateChosenSquareModeInput.invoke(controller, "o")
            );

            // test validate chosen square input, input 'x'
            assertEquals(
                    true,
                    validateChosenSquareModeInput.invoke(controller, "x")
            );

        } catch (Exception e) {
            // test case not pass
            fail();
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
        assertNotNull(controller);

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
            fail();
        }

        // test validate chosen square input, input 'o123'
        try {

            assertEquals(
                    false,
                    validateChosenSquareModeInput.invoke(controller, "o123")
            );

            // ensure the error message
            assertEquals("Please, input o or x value\n", out.toString());

        } catch (Exception e) {
            // test case not pass
            fail();
        } finally {
            out.reset();
        }

        // test validate chosen square input, input 'a1'
        try {

            assertEquals(
                    false,
                    validateChosenSquareModeInput.invoke(controller, "a1")
            );

            // ensure the error message
            assertEquals("Please, input o or x value\n", out.toString());

        } catch (Exception e) {
            // test case not pass
            fail();
        } finally {
            out.reset();
        }

        // test validate chosen square input, input 'ox'
        try {

            assertEquals(
                    false,
                    validateChosenSquareModeInput.invoke(controller, "ox")
            );

            // ensure the error message
            assertEquals("Please, input o or x value\n", out.toString());

        } catch (Exception e) {
            // test case not pass
            fail();
        } finally {
            out.reset();
        }

        // test validate chosen square input, input empty
        try {

            assertEquals(
                    false,
                    validateChosenSquareModeInput.invoke(controller, "")
            );

            // ensure the error message
            assertEquals(
                    "Please, choose square for open or" +
                            " toggle mine check\n",
                    out.toString()
            );

        } catch (Exception e) {
            // test case not pass
            fail();
        } finally {
            out.reset();
        }

    }

    @Test
    public void testValidateGameSettingInputValidCase() {

        // create the game controller
        ConsoleGameController controller = this.createTheController();

        // ensure controller is not null
        assertNotNull(controller);

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
            fail();
        }

        // test validate game setting input, input numeric
        try {

            Map<String, String> settings = new HashMap<String, String>();
            settings.put("height", "150");
            settings.put("width", "20");
            settings.put("mine_quantity", "200");

            assertEquals(
                    true,
                    validateGameSettingInput.invoke(controller, settings)
            );

        } catch (Exception e) {
            // test case not pass
            fail();
        } finally {
            out.reset();
        }

    }

    @Test
    public void testValidateGameSettingInputInValidHeight() {

        // create the game controller
        ConsoleGameController controller = this.createTheController();

        // ensure controller is not null
        assertNotNull(controller);

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
            fail();
        }

        // test validate game setting input, input height is empty
        try {

            Map<String, String> settings = new HashMap<String, String>();
            settings.put("height", "");
            settings.put("width", "16");
            settings.put("mine_quantity", "200");

            assertEquals(
                    false,
                    validateGameSettingInput.invoke(controller, settings)
            );

            // ensure the error message
            assertEquals(
                    "Please, give input for game's height\n",
                    out.toString()
            );

        } catch (Exception e) {
            // test case not pass
            fail();
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
                    false,
                    validateGameSettingInput.invoke(controller, settings)
            );

            // ensure the error message
            assertEquals(
                    "Please, input number for game's height\n",
                    out.toString()
            );

        } catch (Exception e) {
            // test case not pass
            fail();
        } finally {
            out.reset();
        }

    }

    @Test
    public void testValidateGameSettingInputInValidWidth() {

        // create the game controller
        ConsoleGameController controller = this.createTheController();

        // ensure controller is not null
        assertNotNull(controller);

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
            fail();
        }

        // test validate game setting input, input width is empty
        try {

            Map<String, String> settings = new HashMap<String, String>();
            settings.put("height", "105");
            settings.put("width", "");
            settings.put("mine_quantity", "200");

            assertEquals(
                    false,
                    validateGameSettingInput.invoke(controller, settings)
            );

            // ensure the error message
            assertEquals(
                    "Please, give input for game's width\n",
                    out.toString()
            );

        } catch (Exception e) {
            // test case not pass
            fail();
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
                    false,
                    validateGameSettingInput.invoke(controller, settings)
            );

            // ensure the error message
            assertEquals(
                    "Please, input number for game's width\n",
                    out.toString()
            );

        } catch (Exception e) {
            // test case not pass
            fail();
        } finally {
            out.reset();
        }

    }

    @Test
    public void testValidateGameSettingInputInValidMineQuantity() {

        // create the game controller
        ConsoleGameController controller = this.createTheController();

        // ensure controller is not null
        assertNotNull(controller);

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
            fail();
        }

        // test validate game setting input, input mine quantity is empty
        try {

            Map<String, String> settings = new HashMap<String, String>();
            settings.put("height", "10");
            settings.put("width", "20");
            settings.put("mine_quantity", "");

            assertEquals(
                    false,
                    validateGameSettingInput.invoke(controller, settings)
            );

            // ensure the error message
            assertEquals(
                    "Please, give input for game's mine quantity\n",
                    out.toString()
            );

        } catch (Exception e) {
            // test case not pass
            fail();
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
                    false,
                    validateGameSettingInput.invoke(controller, settings)
            );

            // ensure the error message
            assertEquals(
                    "Please, input number for game's mine quantity\n",
                    out.toString()
            );

        } catch (Exception e) {
            // test case not pass
            fail();
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
            fail();
        }

         return controller;

    }

}
