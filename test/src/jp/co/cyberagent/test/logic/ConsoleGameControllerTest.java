package jp.co.cyberagent.test.logic;

import jp.co.cyberagent.components.PlayStatus;
import jp.co.cyberagent.logic.ConsoleGameController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
    public void cleanUP() {
        System.setOut(null);
    }

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
