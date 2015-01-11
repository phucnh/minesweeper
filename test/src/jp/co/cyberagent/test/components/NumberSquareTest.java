package jp.co.cyberagent.test.components;


import jp.co.cyberagent.components.NumberSquare;
import jp.co.cyberagent.components.exceptions.SquareWrongValueException;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;

public class NumberSquareTest {

    /**
     * Test create NumberSquare's object
     */
    @Test
    public void testConstructor() {

        NumberSquare numSquare;

        try {
            // get NumberSquare constructor
            Constructor<NumberSquare> constructor =
                    NumberSquare.class.getDeclaredConstructor(byte.class);
            constructor.setAccessible(true);

            // create new number square object
            numSquare = constructor.newInstance((byte) 1);

            // ensure number square object create successfully
            assertNotNull(numSquare);

            // ensure number square object's elements
            assertNotNull(numSquare.getValue());
            assertTrue(numSquare.getValue() == 1);
            assertFalse(numSquare.isOpened());
            assertFalse(numSquare.isMineChecked());

        } catch (Exception e) {
            // test case not pass
            fail();
        }

    }

    /**
     * Test create NumberSquare's object failed
     */
    @Test
    public void testConstructorFailed() {

        // create object with value is 0
        try {
            // get NumberSquare constructor
            Constructor<NumberSquare> constructor =
                    NumberSquare.class.getDeclaredConstructor(byte.class);
            constructor.setAccessible(true);

            // create new number square object
            constructor.newInstance((byte) 0);

            // test case not pass
            fail();

        } catch (ReflectiveOperationException e) {

            // get InvocationTargetException's target
            assertTrue(e instanceof InvocationTargetException);
            InvocationTargetException itEx = (InvocationTargetException) e;
            Throwable targetEx = itEx.getTargetException();

            // ensure SquareWrongValueException
            assertTrue(targetEx instanceof SquareWrongValueException);

            // ensure exception message
            assertEquals("Square's value must be from 1 to 8",
                         targetEx.getMessage());

        }

        // create object with value is 9
        try {
            // get NumberSquare constructor
            Constructor<NumberSquare> constructor =
                    NumberSquare.class.getDeclaredConstructor(byte.class);
            constructor.setAccessible(true);

            // create new number square object
            constructor.newInstance((byte) 9);

            // test case not pass
            fail();

        } catch (ReflectiveOperationException e) {

            // get InvocationTargetException's target
            assertTrue(e instanceof InvocationTargetException);
            InvocationTargetException itEx = (InvocationTargetException) e;
            Throwable targetEx = itEx.getTargetException();

            // ensure SquareWrongValueException
            assertTrue(targetEx instanceof SquareWrongValueException);

            // ensure exception message
            assertEquals("Square's value must be from 1 to 8",
                         targetEx.getMessage());

        }

    }
}
