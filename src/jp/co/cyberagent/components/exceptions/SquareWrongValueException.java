package jp.co.cyberagent.components.exceptions;

/**
 * Created by phucnh on 15/01/01.
 */
public class SquareWrongValueException extends SquareException {

    public SquareWrongValueException(String s) {
        super(s);
    }

    public SquareWrongValueException() {
        super("Square's value is wrong");
    }

}
