package jmx;

import server.board.Coordinates;
import server.player.Dificult;

/**
 * Intefrace which is implemented by Player class.
 */
public interface PlayerMBean {
    /**
     * Moves player.
     * @param currentCoordinates, destinationCoordinates
     */
    void move(Coordinates currentCoordinates, Coordinates destinationCoordinates);

    Coordinates getCurrentPosition();

    /**
     * Checking if move is correct using rulesManager class
     * @param currentCoordinates
     * @param destinationCoordinates
     */
    void checkMove(Coordinates currentCoordinates, Coordinates destinationCoordinates);

    /**
     * Adds player to lobby.
     * @param lobbyName name of lobby which player is joining.
     */
    void joinToLobby(String lobbyName);

    /**
     * Removes player from lobby.
     */
    void exitFromLobby();

    /**
     * Creates lobby.
     * @param lobbyName name of new Lobby.
     */
    void createLobby(String lobbyName);

    /**
     * Adds new bot to lobby.
     * @param dificultLevel bot's difficulty.
     */
    void addBot(Dificult dificultLevel);

    /**
     * Pass the move.
     */
    void pass();

    /**
     * Surrender game.
     */
    void surrender();

    /**
     * Returns player's pid.
     * @return pid value
     */
    int getPid();

    /**
     * Sends message.
     * @param message contents of message
     */
    void sendMessage(String message);
}
