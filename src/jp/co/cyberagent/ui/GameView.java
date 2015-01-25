package jp.co.cyberagent.ui;

import jp.co.cyberagent.components.Board;
import jp.co.cyberagent.components.Square;
import jp.co.cyberagent.components.exceptions.BoardException;
import jp.co.cyberagent.components.exceptions.BoardOutOfBoundException;
import jp.co.cyberagent.ui.exceptions.ConsoleViewException;
import jp.co.cyberagent.ui.exceptions.ViewException;

import java.io.IOException;
import java.util.Map;

/**
 * Created by phucnh on 15/01/02.
 */
public abstract class GameView {

    public abstract void displayBoard(Board board)
            throws IOException, BoardException;

    public abstract String mainMenu() throws IOException;

    public abstract Map<String, String> gameSetting() throws IOException;

    public abstract void displayChosenSquare(
            Board board,
            int chosenRow,
            int chosenColumn)
            throws IOException, BoardException;

    public abstract void onWin();

    public abstract void onLose();

    public abstract Object chooseSquare() throws IOException;

    public abstract void showMessage(String message) throws IOException;

    public abstract Object chooseSquareMode() throws IOException;

}
