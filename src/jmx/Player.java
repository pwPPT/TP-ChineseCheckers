package jmx;

import client.logic.MoveType;
import javafx.scene.paint.Color;
import server.board.Coordinates;
import server.lobby.Lobby;
import server.player.Difficult;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import java.io.Serializable;

public class Player extends NotificationBroadcasterSupport implements PlayerMBean, Serializable{
    public int pid;
    public String name;
    public Lobby lobby;
    public Color color;
    public int corner;

    public Player(int pid, String name){
        this.pid = pid;
        this.name = name;
        System.out.println(">> Create player with pid= " + pid);
    }

    @Override
    public void checkMove(Coordinates currentCoordinates, Coordinates destinationCoordinates, MoveType moveType) {
        if (lobby.mediator.checkMove(currentCoordinates, destinationCoordinates, pid, moveType) == true) {
            sendNotification(new Notification(String.valueOf(pid), this, 110011110, "R CorrectMove"));
        } else {
            sendNotification(new Notification(String.valueOf(pid), this, 110011110, "R IncorrectMove"));
        }
    }

    @Override
    public void move(Coordinates currentCoordinates, Coordinates destinationCoordinates) {
        System.out.println(">> Invoke move from player: " + pid);
    }

    @Override
    public Coordinates getCurrentPosition() {
        return null;
    }

    @Override
    public void joinToLobby(Lobby lobby, int corner) {
        this.lobby = lobby;
        this.corner = corner;
        sendNotification(new Notification(String.valueOf(pid), this, 110011110, "C " + corner));
    }

    @Override
    public void exitFromLobby() {

    }

    @Override
    public void createLobby(String lobbyName) {

    }

    @Override
    public void addBot(Difficult difficultLevel) {

    }

    @Override
    public void pass() {

    }

    @Override
    public void surrender() {

    }

    @Override
    public int getPid() {
        return pid;
    }

    @Override
    public void sendMessage(String message) {

    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }

    public String getPlayersNames() {
        String playersList = "";
        for(Player p: lobby.players) {
            if (p == null) {
                continue;
            }
            playersList = playersList.concat(","+p.name);
        }
        return playersList;
    }

}
