package jp.co.cyberagent.logic;

import jp.co.cyberagent.components.Board;
import jp.co.cyberagent.exceptions.GameException;
import jp.co.cyberagent.ui.GameView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by phucnh on 14/12/31.
 *
 * Base Game controller class
 */
public abstract class GameController {

    // the game settings constant
    public final String SETTING_HEIGHT = "height";
    public final String SETTING_WIDTH = "width";
    public final String MINE_QUANTITY = "mine_quantity";

    // the game board
    private Board board;

    // game settings
    private Map<String, String> settings;

    // game view, this view interact with user
    protected GameView gameView;

    private GameController() {}

    /**
     * Base constructor, set the game view and run game
     * @param view
     */
    public GameController(GameView view) {

        // set view
        this.gameView = view;

        // initialize the basic settings
        this.settings = new HashMap<String, String>();
        settings.put(SETTING_HEIGHT, "5");
        settings.put(SETTING_WIDTH, "5");
        settings.put(MINE_QUANTITY, "5");

    }

    /**
     * Get game's board
     * @return Board the game's board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Set the game's board
     * @param board the object that want to set
     */
    public void setBoard(Board board) {
        // set board
        this.board = board;
    }

    /**
     * Get game's settings.
     * Board's height, board's width, board's mine quantity
     * @return Map<String, Stirng> game's settings
     */
    public Map<String, String> getSettings() {
        return settings;
    }

    /**
     * Set the game's settings
     * @param settings the game's settings
     */
    public void setSettings(Map<String, String> settings) {
        this.settings = settings;
    }

    /**
     * Begin run game
     * @throws Exception raise when game have error
     */
    public void run() throws Exception {

        // while game is not exit, run the game
        while (!this.isGameExit()) {

            try {

                // display the main menu
                this.showMainMenu();

                // while game is not exit, begin play game
                if (!this.isGameExit()) {

                    // create new game
                    this.createNewGame(settings);

                    // display game's board
                    this.gameView.displayBoard(board);

                    // play game
                    do {
                        this.play();
                    } while (!this.isGameEnd());

                }
            } catch (GameException e) {
                // show the game message when have game error
                this.gameView.showMessage(e.getMessage());
            }

        }

    }

    /**
     * Show the game menu
     * @throws IOException raise when interact with user error
     */
    protected abstract void showMainMenu() throws IOException;

    /**
     * Create the new game
     * @param settings the game's settings
     * @throws GameException
     */
    protected abstract void createNewGame(Map<String, String> settings)
            throws GameException;

    /**
     * The play function
     * @throws GameException raise when have in game error
     * @throws IOException raise when interact with user error
     */
    protected abstract void play() throws GameException, IOException;

    /**
     * Check is game end or not
     * @return boolean is game and or not
     */
    protected abstract boolean isGameEnd();

    /**
     * Check is user's want to exit the game
     * @return boolean is game exit or not
     */
    protected abstract boolean isGameExit();

}
