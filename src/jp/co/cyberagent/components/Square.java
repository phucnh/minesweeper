package jp.co.cyberagent.components;

import jp.co.cyberagent.components.exceptions.SquareCheckedException;
import jp.co.cyberagent.components.exceptions.SquareOpenedException;

/**
 * Created by phucnh on 14/12/31.
 */
public abstract class Square {

    private boolean isOpened;
    private boolean isMineChecked;

    public Square() {
        isOpened = false;
        isMineChecked = false;
    }

    public boolean isOpened() {
        return this.isOpened;
    }

    public boolean isMineChecked() {
        return this.isMineChecked;
    }

    protected void open()
            throws SquareOpenedException,
                   SquareCheckedException {

        if (this.isOpened)
            throw new SquareOpenedException("Square has been opened, cannot open square");

        if (this.isMineChecked())
            throw new SquareCheckedException("Square has been mine marked, can not open square");

        this.isOpened = true;

    }

    protected void toggleMineCheck() throws SquareOpenedException {

        // check did square open or not
        if (this.isOpened)
            throw new SquareOpenedException("Square has been opened, cannot toggle mine check");

        // change square check
        this.isMineChecked = !isMineChecked;
    }

}


