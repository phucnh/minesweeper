package jp.co.cyberagent.ui;

import jp.co.cyberagent.components.*;
import jp.co.cyberagent.components.exceptions.BoardException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by phucnh on 15/01/02.
 *
 * Implement game view for console
 */
public class ConsoleView extends GameView {

    // ANSI escape code, using for print color message
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    // buffer reader for get user input
    private BufferedReader buffReader;

    // buffer write for show message to user
    private BufferedWriter buffWriter;

    private ConsoleView() {}

    /**
     * Create the console view, set the buffered reader, and writer
     *
     * @param buffReader buffered reader for read user input
     * @param buffWriter buffered writer for show the message to user
     */
    public ConsoleView(BufferedReader buffReader, BufferedWriter buffWriter) {
        // set buffered reader and writer
        this.buffReader = buffReader;
        this.buffWriter = buffWriter;

    }

    /**
     * Implement display board game
     *
     * Display game's board in console
     *
     * @param board the game's board
     *
     * @throws IOException raise when have interact with user error
     * @throws BoardException raise when have the board error, board is
     *         out of bound
     */
    @Override
    public void displayBoard(Board board) throws IOException, BoardException {

        // get board size
        int[] size = board.getSize();
        int height = size[0];
        int width = size[1];

        // print board to screen
        // print white space
        int numberCharOfHeight = Integer.toString(height - 1).length();
        this.printWhiteSpace(numberCharOfHeight + 1);

        // print column index
        for (int c = 0; c < width; c++) {
            // print column index, alphabet
            buffWriter.write(this.getCharForNumber(c));
        }
        buffWriter.newLine();
        buffWriter.flush();

        for (int r = 0; r < height; r++) {
            // print row index
            buffWriter.write(String.format("%d", r));
            buffWriter.flush();

            // print the white space if number character of row
            // is not same with height
            this.printWhiteSpace(
                    numberCharOfHeight - Integer.toString(r).length() + 1);

            // display the square
            for (int c = 0; c < width; c++) {
                displaySquare(board.getSquare(r, c));
            }

            // write new line
            buffWriter.newLine();

        }

        buffWriter.flush();

    }

    /**
     * Implement main menu.
     *
     * Display the main menu, get user input
     *
     * @return String the user input
     *
     * @throws IOException raise when have user interact error
     */
    @Override
    public String mainMenu() throws IOException {

        // show message
        buffWriter.write("Please choose below options");
        buffWriter.newLine();
        buffWriter.write("0. Exit game");
        buffWriter.newLine();
        buffWriter.write("1. Create new game");
        buffWriter.newLine();
        buffWriter.write("2. Setting");
        buffWriter.newLine();
        buffWriter.flush();

        // get user input
        return buffReader.readLine();

    }

    /**
     * Implement game setting
     *
     * Display the setting request input message, get input from user
     *
     * @return Map<String, String> the game's setting that get from user
     *
     * @throws IOException raise when have user interact error
     */
    @Override
    public Map<String, String> gameSetting() throws IOException {

        // settings
        Map<String, String> settings = new HashMap<String, String>();

        // set game height
        settings.put("height", getGameSettingItem("height"));

        // set game width
        settings.put("width", getGameSettingItem("width"));

        // set mine quantity
        settings.put("mine_quantity", getGameSettingItem("mine quantity"));

        return settings;

    }

    /**
     * Get game's setting entry.
     * Display the request input, get input from user
     */
    private String getGameSettingItem(String item) throws IOException {

        buffWriter.write("Please, set the " + item);
        buffWriter.newLine();
        buffWriter.flush();
        return buffReader.readLine();

    }

    /**
     * Implement display the chosen square.
     * In console mode, the implement is re-display the board
     * @param board the board that want to display the chosen square
     * @param chosenRow the square's row index
     * @param chosenCol the square's column index
     * @throws IOException raise when have user interact error
     * @throws BoardException raise when have the board exception
     */
    @Override
    public void displayChosenSquare(Board board, int chosenRow, int chosenCol)
            throws IOException, BoardException {

        // in console mode, when display chosen square, re-display board
        this.displayBoard(board);

    }

    /**
     * Implement on win.
     * Display the win message to user
     */
    @Override
    public void onWin() {
        System.out.println("You win, congratulation!");
    }

    /**
     * Implement on lose
     * Display the lose message to user
     */
    @Override
    public void onLose() {
        System.out.println("Oops! You opened the mine! Game over.");
    }

    /**
     * Implement choose square.
     * Display the choose square request message, get chosen square from user
     * @return String the chosen square from user
     * @throws IOException raise when have the board exception
     */
    @Override
    public String chooseSquare() throws IOException {
        buffWriter.write("Please, choose square to open or mine check (0 for back to Main Menu) ");
        buffWriter.newLine();
        buffWriter.flush();
        return buffReader.readLine();
    }

    /**
     * Implement show message
     *
     * Display the message to user
     *
     * @param message the message that want to display
     * @param type the type of message (error, warning)
     *
     * @throws IOException raise when have the board exception
     */
    @Override
    public void showMessage(String message, String type) throws IOException {

        // message color
        String color = "";

        // get color
        if (type == MSG_ERR)
            color = ANSI_RED;
        else if (type == MSG_WRN)
            color = ANSI_YELLOW;

        buffWriter.write(color + message + ANSI_RESET);
        buffWriter.newLine();
        buffWriter.flush();
    }

    /**
     * Implement choose square mode
     *
     * Display the choose square mode message, get chosen mode from user
     * The square mode is open square or toggle mine check
     *
     * @return String the square mode, open or mine check
     *
     * @throws IOException raise when have the board exception
     */
    @Override
    public String chooseSquareMode() throws IOException {
        buffWriter.write("Please, choose open (o) or toggle mine checked (x)");
        buffWriter.newLine();
        buffWriter.flush();
        return buffReader.readLine();
    }

    /**
     * Convert the number to char.
     * Example: 1 is a, 2 is b ...
     * @param i the number that want to convert
     * @return String the correspond string
     */
    private String getCharForNumber(int i) {
        // alphabet letter is from 97 (a) to 122 (z)
        return i >= 0 && i < 26 ? String.valueOf((char)(i + 97)) : null;
    }

    private void printWhiteSpace(int numberWhiteSpace) throws IOException {
        for (int i = 0; i < numberWhiteSpace; i++) {
            buffWriter.write(' ');
        }
        buffWriter.flush();
    }

    /**
     * Display the square. Empty square is ' ', Number square is number,
     * Mine square is 'x'
     * @param square the square that want to display
     */
    private void displaySquare(Square square) throws IOException {

        if (!square.isOpened()) {

            if (square.isMineChecked())
                buffWriter.write('x');
            else
                buffWriter.write('?');

        } else {

            // display square components, correspond with type of square
            if (square instanceof EmptySquare) {
                buffWriter.write(' ');

            } else if (square instanceof MineSquare) {

                buffWriter.write('x');

            } else if (square instanceof NumberSquare) {

                NumberSquare numSquare = (NumberSquare) square;
                buffWriter.write(numSquare.getValue().toString());

            }

        }

    }
}
