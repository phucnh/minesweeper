package jp.co.cyberagent.ui;

import jp.co.cyberagent.components.Board;
import jp.co.cyberagent.components.exceptions.BoardException;

import java.io.IOException;
import java.util.Map;

/**
 * Created by phucnh on 15/01/02.
 */
public abstract class GameView {

    // message type
    // error message
    public static final String MSG_ERR = "error";
    // warning message
    public static final String MSG_WRN = "warning";

    /**
     * Display game board
     *
     * @param board the game's board
     *
     * @throws IOException raise when have interact with user error
     * @throws BoardException raise when have the board error, board is
     *         out of bound
     */
    public abstract void displayBoard(Board board)
            throws IOException, BoardException;

    /**
     * Display the main menu, get user input
     *
     * @return String the user input
     *
     * @throws IOException raise when have user interact error
     */
    public abstract String mainMenu() throws IOException;

    /**
     * Display the setting request input message, get input from user
     *
     * @return Map<String, String> the game's setting that get from user
     *
     * @throws IOException raise when have user interact error
     */
    public abstract Map<String, String> gameSetting() throws IOException;

    public abstract void displayChosenSquare(
            Board board,
            int chosenRow,
            int chosenColumn)
            throws IOException, BoardException;

    /**
     * Do action when user win
     */
    public abstract void onWin();

    /**
     * Do action when user lose
     */
    public abstract void onLose();

    /**
     * Display the choose square request message, get chosen square from user
     *
     * @return String the chosen square from user
     *
     * @throws IOException raise when have the board exception
     */
    public abstract Object chooseSquare() throws IOException;

    /**
     * Display the message to user
     *
     * @param message the message that want to display
     *
     * @throws IOException raise when have the board exception
     */
    public void showMessage(String message) throws IOException {
        showMessage(message, MSG_ERR);
    }

    /**
     * Display the message to user
     *
     * @param message the message that want to display
     * @param type the type of message (error, warning)
     *
     * @throws IOException raise when have the board exception
     */
    public abstract void showMessage(String message, String type)
            throws IOException;

    /**
     * Display the choose square mode message, get chosen mode from user
     * The square mode is open square or toggle mine check
     *
     * @return String the square mode, open or mine check
     *
     * @throws IOException raise when have the board exception
     */
    public abstract Object chooseSquareMode() throws IOException;

}
