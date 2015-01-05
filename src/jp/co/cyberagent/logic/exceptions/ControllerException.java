package jp.co.cyberagent.logic.exceptions;

import jp.co.cyberagent.exceptions.GameException;

/**
 * Created by phucnh on 15/01/03.
 */
public abstract class ControllerException extends GameException {

    public ControllerException(String s) {
        super(s);
    }

}
