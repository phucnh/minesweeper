package jp.co.cyberagent.test.components;

import jp.co.cyberagent.components.Square;
import org.junit.Test;
import static org.junit.Assert.*;

import org.mockito.Mockito;


public class SquareTest {

    /**
     * Test create Square's object (using mock)
     */
    @Test
    public void testConstructor() {

        // mock square object for test constructor
        Square square = Mockito.mock(Square.class);

        // test square's object elements
        assertFalse(square.isOpen());
        assertFalse(square.isMineCheck());

    }

    public void testOpen() throws Exception {

    }

    public void testToggleMineCheck() throws Exception {

    }
}