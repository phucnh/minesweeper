package jp.co.cyberagent.ui;

import jp.co.cyberagent.components.Board;
import jp.co.cyberagent.components.Square;
import jp.co.cyberagent.ui.exceptions.ConsoleViewException;
import jp.co.cyberagent.ui.exceptions.ViewException;

import java.io.IOException;

/**
 * Created by phucnh on 15/01/02.
 */
public abstract class GameView {

    public abstract void displayBoard(Board board)
            throws ViewException, IOException;

    public abstract String mainMenu() throws IOException;

    public abstract void displayChosenSquare(
            Board board,
            int chosenRow,
            int chosenColumn)
            throws IOException, ConsoleViewException;

    public abstract void onWin();

    public abstract void onLose();

    public abstract Object chooseSquare() throws IOException;

    public abstract void showMessage(String message) throws IOException;

    public abstract Object chooseSquareMode() throws IOException;

}
