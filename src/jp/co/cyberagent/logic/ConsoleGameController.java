package jp.co.cyberagent.logic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import jp.co.cyberagent.components.Board;
import jp.co.cyberagent.components.exceptions.*;
import jp.co.cyberagent.components.PlayStatus;
import jp.co.cyberagent.exceptions.GameException;
import jp.co.cyberagent.logic.exceptions.ConsoleControllerException;
import jp.co.cyberagent.ui.ConsoleView;

/**
 * Created by phucnh on 15/01/02.
 *
 * Implement game controller for console
 */
public class ConsoleGameController extends GameController {

    // is game exit or not
    private boolean isGameExit;

    // playing status
    private PlayStatus playStatus;

    /**
     * Create the console game controller, set the view
     * Initialize the game attributes
     */
    public ConsoleGameController() {

        super(new ConsoleView(
                new BufferedReader(new InputStreamReader(System.in)),
                new BufferedWriter(new OutputStreamWriter(System.out))
        ));

        this.isGameExit = false;
        this.playStatus = PlayStatus.NORMAL;

    }

    /**
     * Implement the show main menu options
     * Get the user's input
     * @throws IOException raise when interact with user error
     */
    @Override
    protected void showMainMenu() throws IOException {

        // show the main menu and get main menu option
        String mainMenuOpt;
        do {
            mainMenuOpt = this.gameView.mainMenu();
        } while (!this.validateChosenMainMenuInput(mainMenuOpt));

        // when user input is "0", or not in options end game
        if (mainMenuOpt.equals("0")) {
            this.isGameExit = true;
        } else if (mainMenuOpt.equals("1")) {

            // default setting
            Map<String, String> settings = new HashMap<String, String>();
            settings.put(SETTING_HEIGHT, "5");
            settings.put(SETTING_WIDTH, "5");
            settings.put(MINE_QUANTITY, "5");

            // set the game setting is default
            this.setSettings(settings);

            this.isGameExit = false;
        } else if (mainMenuOpt.equals("2")) {

            // get the game setting from user
            Map<String, String> settings;

            do {
                // show setting
                settings = this.gameView.gameSetting();

            } while (!this.validateGameSettingInput(settings));

            // set the game settings
            this.setSettings(settings);

            this.isGameExit = false;

        } else {
            this.isGameExit = true;
        }

    }

    /**
     * Implement play
     * Do the game play logic
     * @throws BoardException raise when have board's exception
     * @throws SquareException raise when have square's exception
     * @throws IOException raise when have interact with user error
     */
    @Override
    protected void play()
            throws BoardException, SquareException, IOException {

        // show user message for choosing the square and get user's input
        String squareChosen;
        do {
            squareChosen = (String) this.gameView.chooseSquare();

        } while (!validateChosenSquareInput(squareChosen));

        // when input "0", play end, back to main menu
        if (squareChosen.equals("0")) {
            playStatus = PlayStatus.EXIT;
        }
        else {

            // get chosen mode from user
            String squareChosenMode;
            do {
                squareChosenMode = (String) this.gameView.chooseSquareMode();
            } while (!validateChosenSquareModeInput(squareChosenMode));

            // get row and column index
            int squareChosenCol = getNumberForChar(squareChosen.charAt(0));
            int squareChosenRow = Integer.valueOf(squareChosen.substring(1));

            // open or toggle mine square checked
            if (squareChosenMode.equals("x")) {

                try {
                    // toggle the square
                    this.getBoard().toggleMineCheckSquare(squareChosenRow,
                                                          squareChosenCol);
                } catch (GameException e) {
                    this.gameView.showMessage(e.getMessage());
                }

            } else if (squareChosenMode.equals("o")) {

                try {
                    // open the square
                    this.playStatus = this.getBoard().openSquare(
                            squareChosenRow,
                            squareChosenCol
                    );
                } catch (GameException e) {
                    this.gameView.showMessage(e.getMessage());
                }

            }

            // display the chosen square
            this.gameView.displayChosenSquare(
                    getBoard(),
                    squareChosenRow,
                    squareChosenCol
            );

        }

    }

    /**
     * Implement the is game end
     * Check is game end or not
     * When play status is lose, win or exit, exit game, return true
     * Otherwise when play status is normal, continue play game, return false
     * @return boolean is game and or not
     */
    @Override
    protected boolean isGameEnd() {

        if (this.playStatus == PlayStatus.LOSE) {

            this.gameView.onLose();

            return true;

        } else if (this.playStatus == PlayStatus.WIN) {

            this.gameView.onWin();

            return true;

        } else if (this.playStatus == PlayStatus.EXIT) {
            return true;
        }

        return false;

    }

    /**
     * Check is user's want to exit the game
     * @return boolean is game exit or not
     */
    @Override
    protected boolean isGameExit() {
        return this.isGameExit;
    }

    /**
     * Implement create the new
     * Correspond with the settings, create new game
     * @param settings the game's settings (width, height, mine quantity)
     * @throws SquareWrongValueException raise when set the wrong number to number square
     * @throws BoardCreateUnable raise when create the wrong board (out of height, width, mine quantity)
     */
    @Override
    protected void createNewGame(Map<String, String> settings)
            throws SquareWrongValueException,
                   BoardCreateUnable,
                   ConsoleControllerException {

        // get settings
        int boardWidth = new Integer(settings.get(SETTING_WIDTH));
        int boardHeight = new Integer(settings.get(SETTING_HEIGHT));
        long boardMineQuantity =
                new Long(settings.get(MINE_QUANTITY));

        if (boardWidth > 26)
            throw new ConsoleControllerException(
                    "In console mode, board width " +
                    "must less than or equal 26 columns"
            );

        // create new board
        this.setBoard(new Board(boardHeight, boardWidth, boardMineQuantity));

    }

    /**
     * Validate the chosen square input
     * @throws IOException
     */
    private boolean validateChosenSquareInput(String input) throws IOException {

        // validate user input
        // ensure user input is not empty
        if (input.isEmpty()) {
            this.gameView.showMessage("Please, choose square for open");

            return false;
        }

        // ensure user input is valid pattern
        if (!input.matches("0$|^[a-z]\\d+$")) {
            this.gameView.showMessage("Please, input 0 or valid square " +
                                      "choose pattern (Ex: a1, b12)");

            return false;
        }

        return true;

    }

    /**
     * Validate the main menu input
     * @param input
     * @return
     * @throws IOException
     */
    private boolean validateChosenMainMenuInput(String input)
            throws IOException {

        // validate user input
        // ensure user input is not empty
        if (input.isEmpty()) {
            this.gameView.showMessage("Please, choose one of main menu option");

            return false;
        }

        // ensure user input is valid pattern
        if (!input.matches("\\d+")) {
            this.gameView.showMessage("Please, input the number");

            return false;
        }

        return true;

    }

    /**
     * Validate the chosen square mode input
     * @throws IOException
     */
    private boolean validateChosenSquareModeInput(String input)
            throws IOException {

        // validate user input
        // ensure user input is not empty
        if (input.isEmpty()) {
            this.gameView.showMessage("Please, choose square for open or" +
                                      " toggle mine check");

            return false;
        }

        // ensure user input is valid pattern
        if (!input.matches("o$|x$")) {
            this.gameView.showMessage("Please, input o or x value");

            return false;
        }

        return true;

    }

    /**
     * Validate the game's settings input
     * @throws IOException
     */
    private boolean validateGameSettingInput(Map<String, String> settings)
            throws IOException {

        // validate height
        if (!validateGameSettingItem("height", settings.get(SETTING_HEIGHT)))
            return false;

        // validate width
        if (!validateGameSettingItem("width", settings.get(SETTING_WIDTH)))
            return false;

        // validate mine quantity
        boolean isMineQtyValid = validateGameSettingItem(
                "mine quantity",
                settings.get(MINE_QUANTITY));
        if (!isMineQtyValid)
            return false;

        return true;

    }

    /**
     * Validate each item in game's settings
     * @throws IOException
     */
    private boolean validateGameSettingItem(String item, String input)
            throws IOException {

        // validate user input
        // ensure user input is not empty
        if (input.isEmpty()) {
            this.gameView.showMessage("Please, give input for game's " + item);

            return false;
        }

        // ensure user input is valid pattern, can input number only
        if (!input.matches("\\d+")) {
            this.gameView.showMessage(
                    "Please, input number for game's " + item);

            return false;
        }

        return true;

    }

    /**
     * When user input, convert the number to integer
     * @param c the user's input first character
     * @return integer the number that convert from input
     */
    private int getNumberForChar(char c) {
        // convert lower alphabet number to integer (a is 0, b is 1 ...)
        return (int) c - 97;
    }

}
