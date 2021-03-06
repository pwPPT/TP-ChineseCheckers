package server.manager;

import server.core.Server;
import server.lobby.Lobby;
import server.player.Player;
import server.player.PlayerTemplate;

import javax.management.*;

public class Manager extends NotificationBroadcasterSupport implements ManagerMBean {
    private Server server;
    public PlayerManager playerManager;
    public LobbyManager lobbyManager;
    private static volatile Manager instance = null;

    public static Manager getInstance() {
        if (instance == null) {
            synchronized (Manager.class) {
                if (instance == null) {
                    instance = new Manager();
                }
            }
        }
        return instance;
    }

    private Manager() {
    }

    public void setServer(Server server) {
        this.server = server;
    }

    @Override
    public PlayerTemplate getPlayer(int pid) {
        return null;
    }

    @Override
    public void setPlayerAsFree(int pid) {

    }

    @Override
    public void setPlayerAsInGame(int pid) {

    }

    @Override
    public void setPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @Override
    public void setLobbyManager(LobbyManager lobbyManager) {
        this.lobbyManager = lobbyManager;
    }

    @Override
    public void InvitePlayer(String lobbyName, String playerName, String invitedPlayerName) {
        Player player = null;
        for(Player p: playerManager.playerFreeList) {
            if(p.name.equals(invitedPlayerName)) {
                player = p;
                break;
            }
        }
        sendNotification(new Notification(String.valueOf(player.pid), this,
                001100101010, "I,"+playerName+","+lobbyName));
    }

    @Override
    public void addPlayerToLobby(String lobbyName, String playerName) {
        Player player = playerManager.getPlayerFromFreeList(playerName);
        if(player != null) {
            lobbyManager.addPlayerToLobby(lobbyName, player);
            playerManager.movePlayerToInGameList(player);
            sendNotification(new Notification(String.valueOf(lobbyName), this,
                    001100101010, "A,"+playerName+","+lobbyName));
            System.out.println("Player " + player.name + " in lobby " + player.lobby.name);
        } else {
            System.out.println("Error, can't add player: " + playerName);
        }
    }

    @Override
    public void removePlayerFromLobby(String lobbyName, String playerName) {
        Player player = playerManager.getPlayerFromInGameList(playerName);
        if(player != null) {
            if(lobbyManager.removePlayerFromLobby(lobbyName, playerName)) {
                playerManager.movePlayerToFreeList(player);
            }
            System.out.println("Player " + player.name + " removed from lobby ");
        } else {
            System.out.println("Error, can't remove player: " + playerName);
        }
    }

    @Override
    public void removePlayerFromGame(String lobbyName, String playerName) {
        Player player = playerManager.getPlayerFromInGameList(playerName);
        if(player != null) {
            lobbyManager.removePlayerFromGame(lobbyName, player);
            playerManager.movePlayerToFreeList(player);
            System.out.println("Player " + player.name + " removed from lobby ");
        } else {
            System.out.println("Error, can't remove player: " + playerName);
        }
    }

    @Override
    public void sendPlayersInLobbyList(String playerName) {
        PlayerTemplate player = null;
        for(PlayerTemplate p: playerManager.playerInGameList) {
            if(p.getName().equals(playerName)) {
                player = p;
                break;
            }
        }
        if(player != null) {
            System.out.println("Player is not NULL");
            String playersNames = player.getPlayersNames();
            sendNotification(new Notification(String.valueOf(player.getPid()), this,
                    001100101010, "P" + playersNames));
        }
        else {
            System.out.println("Player is NULL");
        }
    }

    @Override
    public void sendWaitingLobbyList(String playerName) {
        Player player = null;
        for(Player p: playerManager.playerFreeList) {
            if(p.name.equals(playerName)) {
                player = p;
                break;
            }
        }
        if(player != null) {
            System.out.println("Player is not NULL");
            String waitingLobbyList = lobbyManager.getWaitingLobbyList();
            sendNotification(new Notification(String.valueOf(player.pid), this,
                    001100110001, "W" + waitingLobbyList));
        }
        else {
            System.out.println("Player is NULL");
        }
    }

    @Override
    public void startGame(String lobbyName) {
        Lobby lobby = null;
        for(Lobby l: lobbyManager.waitingLobbyList) {
            if(l.name.equals(lobbyName)) {
                lobby = l;
                break;
            }
        }
        lobby.startGame();
    }

    public void unregisterLobby(Lobby lobby) {
        try {
            String mbeanObjectNameStr =
                    server.connection.getDomain() + "L" + ":type=" + "lobby.Lobby" + ",name=" + lobby.name;
            ObjectName mbeanObjectName =
                    ObjectName.getInstance(mbeanObjectNameStr);
            server.connection.mbs.unregisterMBean(mbeanObjectName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
