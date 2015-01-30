package jp.co.cyberagent.components;

import jp.co.cyberagent.components.exceptions.SquareCheckedException;
import jp.co.cyberagent.components.exceptions.SquareOpenedException;

/**
 * Created by phucnh on 14/12/31.
 *
 * The game's element: Square
 * This is the abstract class, when using, please implement this class
 */
public abstract class Square {

    private boolean isOpened;
    private boolean isMineChecked;

    /**
     * When create the square, set is opened and is mine checked is false
     */
    public Square() {

        isOpened = false;
        isMineChecked = false;

    }

    /**
     * Get square's open status
     *
     * @return boolean the square' open status. True: opened, False: not opened
     */
    public boolean isOpened() {
        return this.isOpened;
    }

    /**
     * Get square's mine check status
     *
     * @return boolean the mine check status. True: checked, False: unchecked
     */
    public boolean isMineChecked() {
        return this.isMineChecked;
    }

    /**
     * Open the square
     *
     * @throws SquareOpenedException when try to open the opened square
     * @throws SquareCheckedException when try to open the checked square
     */
    protected void open()
            throws SquareOpenedException,
                   SquareCheckedException {

        // when square is opened, raise the exception
        if (this.isOpened)
            throw new SquareOpenedException("Square has been opened, " +
                                            "cannot open square");

        // when square is checked, raise the exception
        if (this.isMineChecked())
            throw new SquareCheckedException("Square has been mine marked, " +
                                             "can not open square");

        // open the square
        this.isOpened = true;

    }

    /**
     * Toggle square is mine check or not
     *
     * @throws SquareOpenedException when try to open the checked square
     */
    protected void toggleMineCheck() throws SquareOpenedException {

        // when square is checked, raise the exception
        if (this.isOpened)
            throw new SquareOpenedException("Square has been opened, " +
                                            "cannot toggle mine check");

        // change square check
        this.isMineChecked = !isMineChecked;
    }

}


