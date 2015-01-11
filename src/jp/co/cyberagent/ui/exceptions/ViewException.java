package jp.co.cyberagent.ui.exceptions;

import jp.co.cyberagent.exceptions.GameException;

/**
 * Created by phucnh on 15/01/02.
 */
public abstract class ViewException extends GameException {

    public ViewException(String s) {
        super(s);
    }

}
