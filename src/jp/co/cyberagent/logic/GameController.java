package jp.co.cyberagent.logic;

import jp.co.cyberagent.components.Board;
import jp.co.cyberagent.components.PlayStatus;
import jp.co.cyberagent.components.exceptions.*;
import jp.co.cyberagent.ui.GameView;

import java.io.IOException;
import java.util.Map;

/**
 * Created by phucnh on 14/12/31.
 */
public abstract class GameController {

    private Board board;

    private GameController() {}

    protected GameView gameView;

    public GameController(GameView view) {
        this.gameView = view;
    }

    public Board getBoard() {
        return board;
    }

    public void createNewGame(Map<String, ? extends Object> settings)
            throws BoardCreateUnable, SquareWrongValueException {

        // get settings
        int boardWidth = new Integer(settings.get("width").toString());
        int boardHeight = new Integer(settings.get("height").toString());
        long boardMineQuantity = new Long(settings.get("mine_quantity").toString());

        // create new board
        board = new Board(boardHeight, boardWidth, boardMineQuantity);

    }

    public PlayStatus openSquare(int row, int col)
            throws SquareWrongValueException,
            SquareCheckedException,
            SquareOpenedException,
            BoardOutOfBoundException {

        return this.board.openSquare(row, col);
    }

    public void toggleMineCheck(int row, int col)
            throws BoardOutOfBoundException,
                   SquareOpenedException {

        this.board.toggleMineCheckSquare(row, col);
    }

    public abstract void initialize() throws IOException;

}
