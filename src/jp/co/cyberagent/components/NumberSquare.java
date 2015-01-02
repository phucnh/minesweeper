package jp.co.cyberagent.components;

import jp.co.cyberagent.exceptions.SquareWrongValueException;

/**
 * Created by phucnh on 15/01/01.
 */
public class NumberSquare extends Square {

    private byte value;

    protected NumberSquare(byte value) throws SquareWrongValueException {

        if (value < 1 || value > 8)
            throw new SquareWrongValueException("Square's value must be from 1 to 8");

        this.value = value;

    }

    public Byte getValue() {
        return value;
    }

}
