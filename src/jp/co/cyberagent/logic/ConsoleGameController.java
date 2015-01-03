package jp.co.cyberagent.logic;

import jp.co.cyberagent.components.exceptions.BoardCreateUnable;
import jp.co.cyberagent.components.exceptions.SquareWrongValueException;
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

    protected void run() throws IOException, BoardCreateUnable, ViewException, SquareWrongValueException {

        while (!isGameEnd) {
            String mainMenuOpt = this.gameView.mainMenu();

            if (mainMenuOpt.equals("0")) {
                this.isGameEnd = true;
            } else if (mainMenuOpt.equals("1")) {
                this.play();
            }
        }

    }

    protected void play() throws BoardCreateUnable, ViewException, SquareWrongValueException, IOException {

        HashMap<String, String> settings = new HashMap<String, String>();
        boolean isPlayDone = false;
        settings.put("height", "5");
        settings.put("width", "5");
        settings.put("mine_quantity", "5");

        this.createNewGame(settings);

        while (!isPlayDone) {

            this.gameView.displayBoard(this.getBoard());

            String squareChosen = (String) this.gameView.chooseSquare();
            System.out.println(squareChosen);

            if (squareChosen.equals("0"))
                isPlayDone = true;
        }
    }

}
