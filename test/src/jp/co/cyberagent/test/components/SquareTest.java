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
        assertFalse(square.isOpened());
        assertFalse(square.isMineChecked());

    }

    /**
     * Test open square function: successful case
     */
    @Test
    public void testOpenSquareSuccessfully() {

        // mock square object
        Square square = Mockito.mock(Square.class, Mockito.CALLS_REAL_METHODS);

        // ensure square is closed
        assertFalse(square.isOpened());

        // test square open method
        try {
            // get square open method
            Method method = Square.class.getDeclaredMethod("open");
            method.setAccessible(true);

            // run open
            method.invoke(square);

            // ensure square is opened
            assertTrue(square.isOpened());
        } catch (Exception e) {
            // test case not pass
            fail();
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
        assertFalse(square.isOpened());

        // test square open method
        try {
            // get square open method
            Method method = Square.class.getDeclaredMethod("open");
            method.setAccessible(true);

            // run open
            method.invoke(square);

            // ensure square is opened
            assertTrue(square.isOpened());

            // open the opened square
            method.invoke(square);

            // test case not pass
            fail();

        } catch (ReflectiveOperationException e) {
            // get InvocationTargetException's target
            assertTrue(e instanceof InvocationTargetException);
            InvocationTargetException itEx = (InvocationTargetException) e;
            Throwable targetEx = itEx.getTargetException();

            // ensure SquareOpenedException
            assertTrue(targetEx instanceof SquareOpenedException);

            // ensure exception message
            assertEquals("Square has been opened, cannot open square",
                         targetEx.getMessage());

            // ensure square is opened
            assertTrue(square.isOpened());
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
        assertFalse(square.isOpened());

        // ensure square is not mine checked
        assertFalse(square.isMineChecked());

        // test square open method
        try {
            // set square is mine checked
            Field field = Square.class.getDeclaredField("isMineChecked");
            field.setAccessible(true);
            field.set(square, true);

            // ensure square is checked
            assertTrue(square.isMineChecked());

            // get square open method
            Method method = Square.class.getDeclaredMethod("open");
            method.setAccessible(true);

            // run open
            method.invoke(square);

            // ensure square is opened
            assertTrue(square.isOpened());

            // open the opened square
            method.invoke(square);

            // test case not pass
            fail();

        } catch (ReflectiveOperationException e) {
            // get InvocationTargetException's target
            assertTrue(e instanceof InvocationTargetException);
            InvocationTargetException itEx = (InvocationTargetException) e;
            Throwable targetEx = itEx.getTargetException();

            // ensure SquareCheckedException
            assertTrue(targetEx instanceof SquareCheckedException);

            // ensure exception message
            assertEquals("Square has been mine marked, can not open square",
                         targetEx.getMessage());
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
        assertFalse(square.isMineChecked());

        // test square toggle square mine check method
        try {
            // get square toggleMineCheck method
            Method method = Square.class.getDeclaredMethod("toggleMineCheck");
            method.setAccessible(true);

            // run function, mark square is checked
            method.invoke(square);

            // ensure square is checked
            assertTrue(square.isMineChecked());

            // run function, mark square is unchecked
            method.invoke(square);

            // ensure square is unchecked
            assertFalse(square.isMineChecked());
        } catch (Exception e) {
            // test case not pass
            fail();
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
        assertFalse(square.isOpened());

        // ensure square is not checked
        assertFalse(square.isMineChecked());

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
            fail();

        } catch (Exception e) {
            // get InvocationTargetException's target
            assertTrue(e instanceof InvocationTargetException);
            InvocationTargetException itEx = (InvocationTargetException) e;
            Throwable targetEx = itEx.getTargetException();

            // ensure SquareOpenedException
            assertTrue(targetEx instanceof SquareOpenedException);

            // ensure exception message
            assertEquals("Square has been opened, cannot toggle mine check",
                         targetEx.getMessage());
        }

    }
}