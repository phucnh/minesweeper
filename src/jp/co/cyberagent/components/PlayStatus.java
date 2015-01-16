package jp.co.cyberagent.components;

/**
 * Created by phucnh on 15/01/03.
 *
 * The game's status
 * This enum describe the game status: win, lose or normal (the game continue)
 */
public enum PlayStatus {
    WIN, // win status
    LOSE, // lose status
    NORMAL, // normal status, game continue
    EXIT // exit status, exit when play game
}
