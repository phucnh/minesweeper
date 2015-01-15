package jp.co.cyberagent.view;

import static org.mockito.Mockito.*;

import jp.co.cyberagent.ui.ConsoleView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

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
    public void cleanUP() {
        System.setOut(null);
    }

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
                    "Please choose below options\n" +
                            "0. Exit game\n" +
                            "1. Create new game\n",
                    out.toString());

            // ensure chosen value
            assertEquals("0", chosen);

        } catch (Exception e) {
            // test case not pass
            fail();
        } finally {
            System.setIn(System.in);
        }

    }

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
            assertEquals("You win, congratulation!\n", out.toString());

        } catch (Exception e) {
            // test case not pass
            fail();
        }

    }

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
            assertEquals("Oops! You opened the mine! Game over.\n",
                         out.toString());

        } catch (Exception e) {
            // test case not pass
            fail();
        }

    }

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
                    "Please, choose square to open or " +
                            "mine check (0 for back to Main Menu) \n",
                    out.toString());

            // ensure chosen value
            assertEquals("0", chosen);

        } catch (Exception e) {
            // test case not pass
            fail();
        } finally {
            System.setIn(System.in);
        }

    }

    @Test(expected = IOException.class)
    public void testChooseSquareFailed() throws IOException {

        // Mock console view
        ConsoleView view = mock(ConsoleView.class);
        when(view.chooseSquare()).thenThrow(new IOException());

        // test function
        view.chooseSquare();

    }

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
                    "Please, choose open (o) or toggle mine checked (x)\n",
                    out.toString());

            // ensure chosen value
            assertEquals("0", chosen);

        } catch (Exception e) {
            // test case not pass
            fail();
        } finally {
            System.setIn(System.in);
        }

    }

    @Test(expected = IOException.class)
    public void testChooseSquareModeFailed() throws IOException {

        // Mock console view
        ConsoleView view = mock(ConsoleView.class);
        when(view.chooseSquareMode()).thenThrow(new IOException());

        // test function
        view.chooseSquareMode();

    }

    @Test
    public void testShowMessageSuccessfully() {

        try {
            // create a view
            ConsoleView view = new ConsoleView(
                    new BufferedReader(new InputStreamReader(System.in)),
                    new BufferedWriter(new OutputStreamWriter(System.out))
            );

            // test function
            view.showMessage("test show message");

            // ensure choose square mode
            assertEquals(
                    "test show message\n",
                    out.toString());

        } catch (Exception e) {
            // test case not pass
            fail();
        }

    }

    @Test(expected = IOException.class)
    public void testShowMessageFailed() throws IOException {

        // Mock console view
        ConsoleView view = mock(ConsoleView.class);
        doThrow(new IOException()).when(view).showMessage("show failed");

        // test function
        view.showMessage("show failed");

    }

    // TODO: write test show board and show square

}
