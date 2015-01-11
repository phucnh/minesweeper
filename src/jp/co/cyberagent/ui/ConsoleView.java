package jp.co.cyberagent.ui;

import jp.co.cyberagent.components.*;
import jp.co.cyberagent.components.exceptions.BoardException;
import jp.co.cyberagent.ui.exceptions.ConsoleViewException;

import java.io.*;

/**
 * Created by phucnh on 15/01/02.
 */
public class ConsoleView extends GameView {

    private BufferedReader buffReader;
    private BufferedWriter buffWriter;

    private ConsoleView() {}

    public ConsoleView(BufferedReader buffReader, BufferedWriter buffWriter) {
        this.buffReader = buffReader;
        this.buffWriter = buffWriter;

    }

    @Override
    public void displayBoard(Board board) throws ConsoleViewException, IOException {

        // get board size
        int[] size = board.getSize();
        int height = size[0];
        int width = size[1];

        if (width > 26)
            throw new ConsoleViewException("In console mode, board width must less than or equal 26 columns");

        // print board to screen
        buffWriter.write(' ');
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

            for (int c = 0; c < width; c++) {

                try {
                    displaySquare(board.getSquare(r, c));
                } catch (BoardException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            // write new line
            buffWriter.newLine();

        }

        buffWriter.flush();

    }

    @Override
    public String mainMenu() throws IOException {

        buffWriter.write("Please choose below options");
        buffWriter.newLine();
        buffWriter.write("0. Exit game");
        buffWriter.newLine();
        buffWriter.write("1. Create new game");
        buffWriter.newLine();
        buffWriter.flush();

        return buffReader.readLine();

    }

    @Override
    public void displaySquare(Square square) throws IOException {

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

    @Override
    public void onWin() {
        System.out.println("You win, congratulation!");
    }

    @Override
    public void onLose() {
        System.out.println("Oops! You opened the mine! Game over.");
    }

    @Override
    public String chooseSquare() throws IOException {
        buffWriter.write("Please, choose square to open or mine check (0 for back to Main Menu) ");
        buffWriter.newLine();
        buffWriter.flush();
        return buffReader.readLine();
    }

    @Override
    public void showMessage(String message) throws IOException {
        buffWriter.write(message);
        buffWriter.newLine();
        buffWriter.flush();
    }

    @Override
    public String chooseSquareMode() throws IOException {
        buffWriter.write("Please, choose open (o) or toggle mine checked (x)");
        buffWriter.newLine();
        buffWriter.flush();
        return buffReader.readLine();
    }

    private String getCharForNumber(int i) {
        // alphabet letter is from 97 (a) to 122 (z)
        return i >= 0 && i < 26 ? String.valueOf((char)(i + 97)) : null;
    }
}
