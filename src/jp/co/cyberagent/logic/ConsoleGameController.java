package jp.co.cyberagent.logic;

import jp.co.cyberagent.components.PlayStatus;
import jp.co.cyberagent.components.exceptions.*;
import jp.co.cyberagent.exceptions.GameException;
import jp.co.cyberagent.ui.exceptions.ViewException;
import jp.co.cyberagent.ui.ConsoleView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by phucnh on 15/01/02.
 */
public class ConsoleGameController extends GameController {

    private boolean isGameEnd;

    public ConsoleGameController() {
        super(new ConsoleView(
                new BufferedReader(new InputStreamReader(System.in)),
                new BufferedWriter(new OutputStreamWriter(System.out))
        ));

        this.isGameEnd = false;
    }

    @Override
    public void initialize() {

        try {
            this.run();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void run() throws IOException {

        while (!isGameEnd) {
            String mainMenuOpt = this.gameView.mainMenu();

            if (mainMenuOpt.equals("0")) {
                this.isGameEnd = true;
            } else if (mainMenuOpt.equals("1")) {
                try {
                    this.play();
                } catch (GameException e) {
                    this.gameView.showMessage(e.getMessage());
                }
            }
        }

    }

    protected void play() throws BoardCreateUnable, ViewException, SquareWrongValueException, IOException {

        // default game settings
        HashMap<String, String> settings = new HashMap<String, String>();
        boolean isPlayDone = false;
        settings.put("height", "5");
        settings.put("width", "5");
        settings.put("mine_quantity", "5");

        // create new game
        this.createNewGame(settings);

        // begin play
        while (!isPlayDone) {

            // display board
            this.gameView.displayBoard(this.getBoard());

            String squareChosen;

            // validate user input
            do {
                // display choose square
                squareChosen = (String) this.gameView.chooseSquare();

            } while (!validateChooseSquareInput(squareChosen));

            String squareChosenMode;

            // when input "0", play end, back to main menu
            if (squareChosen.equals("0"))
                isPlayDone = true;
            else {


                do {
                    squareChosenMode = (String) this.gameView.chooseSquareMode();
                } while (!validateChooseSquareModeInput(squareChosenMode));

                // get row and column index
                int squareChosenCol = getNumberForChar(squareChosen.charAt(0));
                int squareChosenRow = Integer.valueOf(squareChosen.substring(1));

                // game status
                PlayStatus status = PlayStatus.NORMAL;

                // open or toggle mine square checked
                if (squareChosenMode.equals("x")) {
                    try {
                        this.getBoard().toggleMineCheckSquare(squareChosenRow, squareChosenCol);
                    } catch (GameException e) {
                        this.gameView.showMessage(e.getMessage());
                    }
                }
                else if (squareChosenMode.equals("o")) {
                    try {
                        status = this.getBoard().openSquare(squareChosenRow, squareChosenCol);
                    } catch (GameException e) {
                        this.gameView.showMessage(e.getMessage());
                    }
                }

                // check game status
                if (status == PlayStatus.LOSE) {
                    isPlayDone = true;
                    this.gameView.onLose();
                } else if (status == PlayStatus.WIN) {
                    isPlayDone = true;
                    this.gameView.onWin();
                }
            }

        }
    }

    private boolean validateChooseSquareInput(String input) throws IOException {
        // validate user input
        // ensure user input is not empty
        if (input.isEmpty()) {
            this.gameView.showMessage("Please, choose square for open");

            return false;
        }

        // ensure user input is valid pattern
        if (!input.matches("0$|^[a-z]\\d+$")) {
            this.gameView.showMessage("Please, input 0 or valid square choose pattern (Ex: a1, b12)");

            return false;
        }

        return true;
    }

    private boolean validateChooseSquareModeInput(String input) throws IOException {
        // validate user input
        // ensure user input is not empty
        if (input.isEmpty()) {
            this.gameView.showMessage("Please, choose square for open or toggle mine check");

            return false;
        }

        // ensure user input is valid pattern
        if (!input.matches("o$|x$")) {
            this.gameView.showMessage("Please, input o or x value");

            return false;
        }

        return true;
    }

    private int getNumberForChar(char c) {
        // convert lower alphabet number to integer (a is 0, b is 1 ...)
        return (int) c - 97;
    }

}
