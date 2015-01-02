package jp.co.cyberagent.components;

import jp.co.cyberagent.components.exceptions.SquareCheckedException;
import jp.co.cyberagent.components.exceptions.SquareOpenedException;

/**
 * Created by phucnh on 14/12/31.
 */
public abstract class Square {

    private boolean isOpen;
    private boolean isMineCheck;

    public Square() {
        isOpen = false;
        isMineCheck = false;
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    public boolean isMineCheck() {
        return this.isMineCheck;
    }

    protected void open()
            throws SquareOpenedException,
                   SquareCheckedException {

        if (this.isOpen)
            throw new SquareOpenedException("Square has been opened, cannot open square");

        if (this.isMineCheck())
            throw new SquareCheckedException("Square has been mine marked, can not mark again");

        this.isOpen = true;

    }

    protected void toggleMineCheck() throws SquareOpenedException {

        // check did square open or not
        if (this.isOpen)
            throw new SquareOpenedException("Square has been opened, cannot toggle mine check");

        // change square check
        this.isMineCheck = !isMineCheck;
    }

}


