package jp.co.cyberagent.test.components;

import jp.co.cyberagent.components.Square;
import jp.co.cyberagent.components.exceptions.SquareCheckedException;
import jp.co.cyberagent.components.exceptions.SquareOpenedException;
import org.junit.Test;
import static org.junit.Assert.*;

import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class SquareTest {

    /**
     * Test create Square's object (using mock)
     */
    @Test
    public void testConstructor() {

        // mock square object for test constructor
        Square square = Mockito.mock(Square.class);

        // test square's object elements
        assertFalse(
                "Failure - After create square, square had been opened",
                square.isOpened()
        );
        assertFalse(
                "Failure - After create square, square had been checked",
                square.isMineChecked()
        );

    }

    /**
     * Test open square function: successful case
     */
    @Test
    public void testOpenSquareSuccessfully() {

        // mock square object
        Square square = Mockito.mock(Square.class, Mockito.CALLS_REAL_METHODS);

        // ensure square is closed
        assertFalse(
                "Failure - After create square, square had been opened",
                square.isOpened()
        );

        // test square open method
        try {
            // get square open method
            Method method = Square.class.getDeclaredMethod("open");
            method.setAccessible(true);

            // run open
            method.invoke(square);

            // ensure square is opened
            assertTrue(
                    "Failure - After open square, square is not open",
                    square.isOpened()
            );
        } catch (Exception e) {
            // test case not pass
            fail("Failure - Open square error " + e.getMessage());
        }

    }

    /**
     * Test open square function: failed case, square has been opened
     */
    @Test
    public void testOpenSquareFailedSquareOpened() {

        // mock square object
        Square square = Mockito.mock(Square.class, Mockito.CALLS_REAL_METHODS);

        // ensure square is closed
        assertFalse(
                "Failure - After create square, square had been opened",
                square.isOpened()
        );

        // test square open method
        try {
            // get square open method
            Method method = Square.class.getDeclaredMethod("open");
            method.setAccessible(true);

            // run open
            method.invoke(square);

            // ensure square is opened
            assertTrue(
                    "Failure - Set square open attribute fail",
                    square.isOpened()
            );

            // open the opened square
            method.invoke(square);

            // test case not pass
            fail("Failure - Open the opened square not throw exception");

        } catch (ReflectiveOperationException e) {
            // get InvocationTargetException's target
            assertTrue(e instanceof InvocationTargetException);
            InvocationTargetException itEx = (InvocationTargetException) e;
            Throwable targetEx = itEx.getTargetException();

            // ensure SquareOpenedException
            assertTrue(
                    "Failure - Exception instance is not SquareOpenedException",
                    targetEx instanceof SquareOpenedException
            );

            // ensure exception message
            assertEquals(
                    "Failure - Open the opened square exception message wrong",
                    "Square has been opened, cannot open square",
                    targetEx.getMessage()
            );

        }

    }

    /**
     * Test open square function: failed case, square has been mine checked
     */
    @Test
    public void testOpenSquareFailedSquareChecked() {

        // mock square object
        Square square = Mockito.mock(Square.class, Mockito.CALLS_REAL_METHODS);

        // ensure square is closed
        assertFalse(
                "Failure - After create square, square had been opened",
                square.isOpened()
        );

        // ensure square is not mine checked
        assertFalse(
                "Failure - After create square, square had been checked",
                square.isMineChecked()
        );

        // test square open method
        try {
            // set square is mine checked
            Field field = Square.class.getDeclaredField("isMineChecked");
            field.setAccessible(true);
            field.set(square, true);

            // ensure square is checked
            assertTrue(
                    "Failure - Set square check fail",
                    square.isMineChecked()
            );

            // get square open method
            Method method = Square.class.getDeclaredMethod("open");
            method.setAccessible(true);

            // open the checked square
            method.invoke(square);

            // test case not pass
            fail("Failure - Open the checked square not throw exception");

        } catch (ReflectiveOperationException e) {
            // get InvocationTargetException's target
            assertTrue(e instanceof InvocationTargetException);
            InvocationTargetException itEx = (InvocationTargetException) e;
            Throwable targetEx = itEx.getTargetException();

            // ensure SquareCheckedException
            assertTrue(
                    "Failure - Exception object is not SquareCheckedException",
                    targetEx instanceof SquareCheckedException
            );

            // ensure exception message
            assertEquals(
                    "Failure - Open the checked square exception message wrong",
                    "Square has been mine marked, can not open square",
                    targetEx.getMessage());

            // ensure square is not open
            assertFalse(
                    "Failure - Open the square error, but square is opened",
                    square.isOpened()
            );

        }

    }

    /**
     * Test square toggle mine check function, successful case
     */
    @Test
    public void testToggleMineCheckSuccessfully() {

        // mock square object
        Square square = Mockito.mock(Square.class, Mockito.CALLS_REAL_METHODS);

        // ensure square is not checked
        assertFalse(
                "Failure - After create square, square had been checked",
                square.isMineChecked()
        );

        // test square toggle square mine check method
        try {
            // get square toggleMineCheck method
            Method method = Square.class.getDeclaredMethod("toggleMineCheck");
            method.setAccessible(true);

            // run function, mark square is checked
            method.invoke(square);

            // ensure square is checked
            assertTrue(
                    "Failure - After check square, square not check",
                    square.isMineChecked()
            );

            // run function, mark square is unchecked
            method.invoke(square);

            // ensure square is unchecked
            assertFalse(
                    "Failure - After un-check square, square is checked",
                    square.isMineChecked()
            );

        } catch (Exception e) {
            // test case not pass
            fail("Failure - Toggle square error " + e.getMessage());
        }

    }

    /**
     * Test square toggle mine check function, failed case throw SquareOpenedException
     */
    @Test
    public void testToggleMineCheckFailedSquareOpened() {

        // mock square object
        Square square = Mockito.mock(Square.class, Mockito.CALLS_REAL_METHODS);

        // ensure square is not opened
        assertFalse(
                "Failure - After create square, square had been opened",
                square.isOpened());

        // ensure square is not checked
        assertFalse(
                "Failure - After create square, square had been checked",
                square.isMineChecked());

        // test square toggle square mine check method
        try {
            // set square opened
            Field field = Square.class.getDeclaredField("isOpened");
            field.setAccessible(true);
            field.set(square, true);

            // get square toggleMineCheck method
            Method method = Square.class.getDeclaredMethod("toggleMineCheck");
            method.setAccessible(true);

            // run function, mark square is checked
            method.invoke(square);

            // test case not pass
            fail("Failure - Toggle a opened square not throw exception");

        } catch (Exception e) {
            // get InvocationTargetException's target
            assertTrue(e instanceof InvocationTargetException);
            InvocationTargetException itEx = (InvocationTargetException) e;
            Throwable targetEx = itEx.getTargetException();

            // ensure SquareOpenedException
            assertTrue(
                    "Failure - Toggle an opened square " +
                            "not throw SquareOpenedException",
                    targetEx instanceof SquareOpenedException);

            // ensure exception message
            assertEquals(
                    "Failure - Toggle an opened square exception message wrong",
                    "Square has been opened, cannot toggle mine check",
                    targetEx.getMessage()
            );
        }

    }
}