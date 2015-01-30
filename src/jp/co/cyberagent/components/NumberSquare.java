package jp.co.cyberagent.components;

import jp.co.cyberagent.components.exceptions.SquareWrongValueException;

/**
 * Created by phucnh on 15/01/01.
 */
public class NumberSquare extends Square {

    private Byte value;

    /**
     * Create the number square instance
     *
     * @param value the number square value
     *
     * @throws SquareWrongValueException when the value is not from 1 to 8
     */
    protected NumberSquare(byte value) throws SquareWrongValueException {

        if (value < 1 || value > 8)
            throw new SquareWrongValueException("Square's value must be from 1 to 8");

        this.value = value;

    }

    /**
     * Get number square's value
     *
     * @return Byte the square's value
     */
    public Byte getValue() {
        return value;
    }

}
