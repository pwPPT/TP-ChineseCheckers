package client.logic;

import client.core.ClientGUI;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import server.player.Difficult;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientGUIController {

	public ClientGUI client;
	public BoardUpdate boardUpdate;
	private PlayerLogic playerLogic;

	public Node currentPosition;
	public Node destinationPosition;
	public Node previousNode;
	public Node waitingNode;
	public ArrayList<Node> jumpPositions;
	public boolean destinationSelected;

	private ObservableList<String> playersList;
	private ObservableList<String> lobbyList;
	public ClientListener clientListener;
	public GridPane board;

	private Difficult botDifficult;

	//LOGIN---------------------------------------
	@FXML
	public StackPane playerNickNamePanel;
	@FXML
	private Button loginButton;
	@FXML
	public TextField nickNameField;
	@FXML
	private Label nickNameErrorLabel;

	//MENU----------------------------------------
	@FXML
	public StackPane menu;
	@FXML
	private Button newGameButton;
	@FXML
	private Button exitButton;
	@FXML
	private Button joinGameButton;
	//CREATE LOBBY--------------------------------
	@FXML
	private StackPane createLobby;
	@FXML
	private TextField lobbyNameField;
	@FXML
	private Label choosedPlayerInLobbyLabel;
	@FXML
	private Button player2Button;
	@FXML
	private Button player3Button;
	@FXML
	private Button player4Button;
	@FXML
	private Button player6Button;
	@FXML
	private Spinner boardSizeSpinner;
	@FXML
	private Button createLobbyButton;
	@FXML
	private Button cancelCreateLobbyButton;
	@FXML
	private Label lobbyNameErrorLabel;
	//JOIN LOBBY----------------------------------
    @FXML
    private TableView<String> lobbyListTable;
    @FXML
    private TableColumn<String, String> lobbyListColumn;
	@FXML
	private StackPane joinLobby;
	@FXML
	private Button cancelJoinLobbyButton;
	@FXML
	private Button joinLobbyButton;
	@FXML
    private Button refreshLobbyListButton;
	//LOBBY---------------------------------------
	@FXML
	public StackPane lobby;
	@FXML
	private Label playernumberInLobbyStatusLabel;
	@FXML
	private TableView<String> playersInLobbyList;
	@FXML
	private TableColumn<String, String> playersInLobbyColumn;
	@FXML
	public TextField invitePlayerField;
	@FXML
	private Button addBotButton;
	@FXML
	private Button removePlayerButton;
	@FXML
	private Button addPlayerButton;
	@FXML
	private Button easyBotButton;
	@FXML
	private Button mediumBotButton;
	@FXML
	private Button hardBotButton;
	@FXML
	private Button readyButton;
	@FXML
	private Button exitLobbyButton;
	@FXML
    private Button refreshPlayersListButton;
	@FXML
	private Label startGameInfoLabel;
	//GAME----------------------------------------
	@FXML
	public StackPane game;
	@FXML
    public GridPane board4;
    @FXML
    public GridPane board3;
    @FXML
    public GridPane board2;
	@FXML
	private ProgressBar turnTimeBar;
	@FXML
	private Button moveButton;
	@FXML
	private Button passButton;
	@FXML
	private Button surrenderButton;
	@FXML
	private Button sendMsgButton;
	@FXML
	private Label turnLabel;
	@FXML
	private Label chatMessageBox;
	@FXML
	private TextField messageTextField;
	@FXML
	private TableView<String> playersInGameTable;
	@FXML
	private TableColumn<String, String> playersInGameColumn;

	//POPUP-INVITE--------------------------------------
	@FXML
	private StackPane invitePopUp;
	@FXML
	private Button invitePopButtonAccept;
	@FXML
	private Button invitePopButtonDecline;
	@FXML
	private Label popUpPlayerNick;
	//POPUP-WINNER-LOOSER--------------------------------------
	@FXML
	private StackPane winnerPopUp;
	@FXML
	private Button okButton;
	@FXML
	private Label popUpWinnerNick;
	@FXML
	private Label popUpLooserNick;
	//WINNER----------------------------------------------------
	@FXML
	public StackPane winner;
	@FXML
	private Button winnerPlayAgainButton;
	@FXML
	private Button winnerExitButton;
	@FXML
	public Label looserNickLabel;



	//LOOSER-----------------------------------------------------
	@FXML
	public StackPane looser;
	@FXML
	private Button looserPlayAgainButton;
	@FXML
	private Button looserExitButton;
	@FXML
	public Label winnerNickLabel;



	//------------------------------------------------------------



	public ClientGUIController(ClientGUI client) {
		this.client = client;
	}

	public void setListener(ClientListener clientListener) {
		this.clientListener = clientListener;
	}

	@FXML
	void initialize() {
		boardUpdate = new BoardUpdate(this, client);
		playerLogic = new PlayerLogic(this, client);

		game.setStyle("-fx-background-image: url('/client/wood-background.jpeg');");
		jumpPositions = new ArrayList<>();
		destinationSelected = false;

		playersList = FXCollections.observableArrayList();
		playersInLobbyColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
		playersInLobbyList.setItems(playersList);

        lobbyList = FXCollections.observableArrayList();
        lobbyListColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
        lobbyListTable.setItems(lobbyList);
        board = board4;


        playersInGameColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
        playersInGameTable.setItems(playersList);
		client.playerInLobby = 0;
	}

	//LOGIN---------------------------------------

	public void loginButtonOnClick(ActionEvent exent) {
		playerLogic.createPlayer();
	}

	public void playerCreated() {
		playerNickNamePanel.setVisible(false);
		playerNickNamePanel.setDisable(true);

		try {
			client.player = new ObjectName(client.domain+ client.pid +":type=server.player.Player,name=Player" + client.pid);

			client.addNotificationListenerToPlayer();
			client.playerName = nickNameField.getText();
		} catch (MalformedObjectNameException e) {
			e.printStackTrace();
		}

		menu.setVisible(true);
		menu.setDisable(false);
	}

	public void showPlayerNameErrorMessage(String message) {
		run(() -> {
			nickNameErrorLabel.setText(message);
		});
	}


	//MENU----------------------------------------

	public void newGameButtonOnClick(ActionEvent exent) {
		this.menu.setVisible(false);
		this.menu.setDisable(true);

		this.createLobby.setVisible(true);
		this.createLobby.setDisable(false);
	}

	public void joinGameButtonOnClick(ActionEvent event) {
		this.menu.setVisible(false);
		this.menu.setDisable(true);

		Object  opParams[] = {client.playerName};
		String  opSig[] = {String.class.getName()};
		client.connection.invokeMethod(client.manager, "sendWaitingLobbyList", opParams, opSig);

		this.joinLobby.setVisible(true);
		this.joinLobby.setDisable(false);

	}

	public void exitButtonOnClick(ActionEvent event) {
		Platform.exit();
	}






	//CREATE LOBBY--------------------------------

	public void createLobbyButtonOnClick(ActionEvent event) {
		if(!lobbyNameField.getText().equals("")) {
			if(!lobbyNameField.getText().contains("#")) {
				if (client.playerInLobby ==  0) {
					client.playerInLobby = 2;
				}
				client.lobbyName = lobbyNameField.getText();
				client.rowOfPawn = (int) boardSizeSpinner.getValue();

				Object opParams[] = {client.playerInLobby, client.rowOfPawn, client.lobbyName, client.pid};
				String opSig[] = {int.class.getName(), int.class.getName(), String.class.getName(), int.class.getName()};
				client.connection.invokeMethod(client.factory, "createLobby", opParams, opSig);
				showLobbyNameErrorMessage("");
			} else {
				//ERROR - wrong name
				showLobbyNameErrorMessage("ERROR, lobby name can't contains '#' characters!");
			}
		} else {
			//ERROR - Empty lobby name
			showLobbyNameErrorMessage("ERROR, lobby name can't be empty!");
		}
	}

	public void lobbyCreated() {
		this.createLobby.setVisible(false);
		this.createLobby.setDisable(true);

		if (client.playerInLobby == 0) {
			client.playerInLobby = 2;
		}

		Object opParams1[] = {client.playerName};
		String opSig1[] = {String.class.getName()};
		client.connection.invokeMethod(client.manager, "sendPlayersInLobbyList", opParams1, opSig1);

		boardUpdate.setCorner(0);
		clientListener.setCorner(0);
		try {
			client.lobbyObject = new ObjectName(client.domain+"L" +":type=lobby.Lobby,name=" + client.lobbyName);
			client.addNotificationListenerToLobby();
		} catch (MalformedObjectNameException e) {
			e.printStackTrace();
		}

		this.lobby.setVisible(true);
		this.lobby.setDisable(false);
	}

	public void showLobbyNameErrorMessage(String message) {
		run(() -> {
			lobbyNameErrorLabel.setText(message);
		});
	}

	public void cancelCreateLobbyButtonOnClick(ActionEvent event) {
		this.createLobby.setVisible(false);
		this.createLobby.setDisable(true);

		this.menu.setVisible(true);
		this.menu.setDisable(false);
	}

	public void setPlayerNumberOn2ButtonOnClick(ActionEvent event) {
		client.playerInLobby = 2;
		choosedPlayerInLobbyLabel.setText("2 players");
	}

	public void setPlayerNumberOn3ButtonOnClick(ActionEvent event) {
		client.playerInLobby = 3;
		choosedPlayerInLobbyLabel.setText("3 players");
	}

	public void setPlayerNumberOn4ButtonOnClick(ActionEvent event) {
		client.playerInLobby = 4;
		choosedPlayerInLobbyLabel.setText("4 players");
	}

	public void setPlayerNumberOn6ButtonOnClick(ActionEvent event) {
		client.playerInLobby = 6;
		choosedPlayerInLobbyLabel.setText("6 players");
	}





	//JOIN LOBBY----------------------------------

	public void joinLobbyButtonOnClick(ActionEvent event) {
		this.joinLobby.setVisible(false);
		this.joinLobby.setDisable(true);

		String lobbyName;
		lobbyName = lobbyListTable.getSelectionModel().getSelectedItem();

		Object  opParams[] = {lobbyName, client.playerName};
		String  opSig[] = {String.class.getName(), String.class.getName()};
		client.connection.invokeMethod(client.manager, "addPlayerToLobby", opParams, opSig );

		System.out.println("Player: " + client.playerName + "joined to lobby: " + lobbyName);

		Object opParams1[] = {client.playerName};
		String opSig1[] = {String.class.getName()};
		client.connection.invokeMethod(client.manager, "sendPlayersInLobbyList", opParams1, opSig1);

		client.lobbyName = lobbyName;
		try {
			client.lobbyObject = new ObjectName(client.domain+"L" +":type=lobby.Lobby,name=" + client.lobbyName);
		} catch (MalformedObjectNameException e) {
			e.printStackTrace();
		}
		client.addNotificationListenerToLobby();

		this.lobby.setVisible(true);
		this.lobby.setDisable(false);
	}

	public void refreshLobbyListOnClick(ActionEvent event) {
		Object  opParams[] = {client.playerName};
		String  opSig[] = {String.class.getName()};
		client.connection.invokeMethod(client.manager, "sendWaitingLobbyList", opParams, opSig);
	}

	public void updateLobbyList(String[] list) {
		run(() -> {
			lobbyList.setAll(list);
			lobbyListTable.setItems(lobbyList);
		});
	}

	public void cancelJoinLobbyButtonOnClick(ActionEvent event) {
		this.joinLobby.setVisible(false);
		this.joinLobby.setDisable(true);

		this.menu.setVisible(true);
		this.menu.setDisable(false);
	}




	//LOBBY---------------------------------------

	public void readyButtonOnClick(ActionEvent event) {
		Object  opParams[] = {client.lobbyName};
		String  opSig[] = {String.class.getName()};
		client.connection.invokeMethod(client.manager, "startGame", opParams, opSig);

		startGameInfoLabel.setText("");
	}

	public void startGame() {
		this.lobby.setVisible(false);
		this.lobby.setDisable(true);

		this.game.setVisible(true);
		this.game.setDisable(false);
	}

	public void showLobbyInfo() {
		run(() -> {
			startGameInfoLabel.setText("Not enough players in lobby - " + playersList.size() + "/" + boardUpdate.getNumberOfPlayers() + " players.");
		});
	}

	public void fillPlayersInGameTable() {
		run(() -> {
			ObservableList<String> newPlayersList = FXCollections.observableArrayList();
			if(playersList.size() == 2) {
				newPlayersList.add(playersList.get(0));
				newPlayersList.add("");
				newPlayersList.add("");
				newPlayersList.add(playersList.get(1));
				newPlayersList.add("");
				newPlayersList.add("");
			} else if(playersList.size() == 3) {
				newPlayersList.add(playersList.get(0));
				newPlayersList.add("");
				newPlayersList.add(playersList.get(1));
				newPlayersList.add("");
				newPlayersList.add(playersList.get(2));
				newPlayersList.add("");
			} else if(playersList.size() == 4) {
				newPlayersList.add(playersList.get(0));
				newPlayersList.add(playersList.get(1));
				newPlayersList.add("");
				newPlayersList.add(playersList.get(2));
				newPlayersList.add(playersList.get(3));
				newPlayersList.add("");
			} else if(playersList.size() == 6) {
				newPlayersList.add(playersList.get(0));
				newPlayersList.add(playersList.get(1));
				newPlayersList.add(playersList.get(2));
				newPlayersList.add(playersList.get(3));
				newPlayersList.add(playersList.get(4));
				newPlayersList.add(playersList.get(5));
			}
			playersInGameTable.setItems(newPlayersList);
		});
	}

	public void addPlayerButtonOnClick(ActionEvent event) {
		playerLogic.addPlayerButtonOnClick(event);
	}

	public void removePlayerButtonOnClick(ActionEvent event) {
		playerLogic.removePlayerButtonOnClick(event);
	}


	public void setBotDifficultEASYButtonOnClick(ActionEvent event) {
		botDifficult = Difficult.EASY;
	}

	public void setBotDifficultMEDIUMButtonOnClick(ActionEvent event) {
		botDifficult = Difficult.MEDIUM;
	}

	public void setBotDifficultHARDButtonOnClick(ActionEvent event) {
		botDifficult = Difficult.HARD;
	}

	public void addBotButtonOnClick(ActionEvent event) {
		Object  opParams[] = {botDifficult};
		String  opSig[] = {Difficult.class.getName()};
		client.connection.invokeMethod(client.player, "addBot", opParams, opSig);
	}

	public void exitLobbyButtonOnClick(ActionEvent event) {
		this.lobby.setVisible(false);
		this.lobby.setDisable(true);

//		client.connection.invokeExitFromLobbyMethod(client.player, "exitFromLobby");
		Object  opParams[] = {client.lobbyName, client.playerName};
		String  opSig[] = {String.class.getName(), String.class.getName()};
		client.connection.invokeMethod(client.manager, "removePlayerFromLobby", opParams, opSig);
		client.lobbyName = "";

		this.menu.setVisible(true);
		this.menu.setDisable(false);
	}

	public void refreshPlayersListButtonOnClick(ActionEvent event) {
		Object  opParams[] = {client.playerName};
		String  opSig[] = {String.class.getName()};
		client.connection.invokeMethod(client.manager, "sendPlayersInLobbyList", opParams, opSig);
	}

	public void updatePlayersList(String[] list) {
		run(() -> {
			boardUpdate.setNumberOfPlayers(Integer.parseInt(list[0]));
			String[] playersNames = Arrays.copyOfRange(list, 1, list.length);
			playersList.setAll(playersNames);
			playersInLobbyList.setItems(playersList);
			playernumberInLobbyStatusLabel.setText( client.lobbyName + ":  " + playersNames.length + " / " + client.playerInLobby);
		});
	}

	//GAME----------------------------------------

	public void surrenderButtonOnClick(ActionEvent event) {
//		this.game.setVisible(false);
//		this.game.setDisable(true);

		Object  opParams1[] = {};
		String  opSig1[] = {};
		client.connection.invokeMethod(client.player, "surrender", opParams1, opSig1);

//		client.connection.invokeExitFromLobbyMethod(client.player, "exitFromLobby");
		Object  opParams[] = {client.lobbyName, client.playerName};
		String  opSig[] = {String.class.getName(), String.class.getName()};
		client.connection.invokeMethod(client.manager, "removePlayerFromGame", opParams, opSig);
		client.lobbyName = "";

//		this.looser.setVisible(true);
//		this.looser.setDisable(false);
	}

	public void passButtonOnClick(ActionEvent event) {
		if (boardUpdate.checkRound()) {
			client.connection.invokeMethod(client.player, "nextRound", null, null);
		}
	}

	public void sendMsgButtonOnClick(ActionEvent event) {
		String message = messageTextField.getText();
		messageTextField.clear();
		Object  opParams[] = {message};
		String  opSig[] = {String.class.getName()};
		client.connection.invokeMethod(client.player, "sendMessage", opParams, opSig);
	}

	public void showTurnLabel(boolean show) {
		turnLabel.setVisible(show);
	}

	public void addMessage(String message) {
		chatMessageBox.setText(message);
	}

	public void doMoveOnClick(ActionEvent event) {
		boardUpdate.doMoveOnClick(event);
	}

	public void chooseCircleOnClick(MouseEvent event) {
		boardUpdate.chooseCircleOnClick(event);
	}






	//POPUP-INVITE--------------------------------------

	public void showInvitation(String[] PlayerAndLobbyName) {
		run(() -> {
			invitePopUp.setStyle("-fx-background-image: url('/client/popupBackground.png');");
			this.popUpPlayerNick.setText(PlayerAndLobbyName[0]);
			this.invitePopUp.setVisible(true);
			this.invitePopUp.setDisable(false);
			client.lobbyName = PlayerAndLobbyName[1];
		});
	}

	public void acceptInviteOnClick(ActionEvent event) {
		try {
			client.lobbyObject = new ObjectName(client.domain+"L" +":type=lobby.Lobby,name=" + client.lobbyName);
		} catch (MalformedObjectNameException e) {
			e.printStackTrace();
		}
		client.addNotificationListenerToLobby();
		playerLogic.addPlayerToLobby();

		this.invitePopUp.setVisible(false);
		this.invitePopUp.setDisable(true);
		this.menu.setVisible(false);
		this.menu.setDisable(true);
		this.joinLobby.setVisible(false);
		this.joinLobby.setDisable(true);
		this.createLobby.setVisible(false);
		this.createLobby.setDisable(true);

		this.lobby.setVisible(true);
		this.lobby.setDisable(false);
	}

	public void declineInviteOnClick(ActionEvent event) {
		client.lobbyName = "";
		this.invitePopUp.setVisible(false);
		this.invitePopUp.setDisable(true);
	}




	//POPUP-WINNER/LOOSER--------------------------------------

	//TODO

	public void showWinnerPopUp(String[] PlayersNames) {
		run(() -> {
			winnerPopUp.setStyle("-fx-background-image: url('/client/popupBackground.png');");
			this.popUpWinnerNick.setText(PlayersNames[0]);
			this.popUpLooserNick.setText(PlayersNames[1]);
			this.winnerPopUp.setVisible(true);
			this.winnerPopUp.setDisable(false);
		});
	}

	public void okOnClick(ActionEvent event) {
		winnerPopUp.setDisable(true);
		winnerPopUp.setVisible(false);

	}




	//WINNER----------------------------------------------------

	//TODO

	public void showWinnerScreen(String[] winner) {
		run(() -> {
			looserNickLabel.setText(winner[1]);
			game.setVisible(false);
			game.setDisable(true);

			this.winner.setDisable(false);
			this.winner.setVisible(true);
		});
	}

	public void playAgainButtononClick(ActionEvent event) {
		winner.setVisible(false);
		winner.setDisable(true);
		looser.setVisible(false);
		looser.setDisable(true);
		boardUpdate = new BoardUpdate(this, client);
		playerLogic = new PlayerLogic(this, client);

		menu.setDisable(false);
		menu.setVisible(true);
	}

	public void exitOnClick(ActionEvent event) {
		Platform.exit();
	}




//LOOSER-----------------------------------------------------

	public void showLooserScreen(String[] looser) {
		run(() -> {
			winnerNickLabel.setText(looser[1]);
			game.setVisible(false);
			game.setDisable(true);

			this.looser.setDisable(false);
			this.looser.setVisible(true);
		});
	}





	//=========================================================================


	//UPDATE GUI GAME----------------------------------------------------------

	//Set methods are invoked by notification listener after acceptation received
	public void setStartNode(Node node) {
		Circle circle = (Circle)node;
		circle.setStrokeWidth(4);
		circle.setStroke(Color.GREEN);
		currentPosition = node;
		previousNode = node;
	}

	public void setDestinationNode() {
		destinationSelected = true;
		Circle circle = (Circle)waitingNode;
		circle.setStrokeWidth(4);
		circle.setStroke(Color.RED);
		destinationPosition = waitingNode;
	}

	public void setJumpNode() {
		Circle circle = (Circle)waitingNode;
		circle.setStrokeWidth(4);
		circle.setStroke(Color.BLUE);
		jumpPositions.add(waitingNode);
		previousNode = waitingNode;
	}

	public void removeStartNode(Node node) {
		Circle circle = (Circle)node;
		circle.setStrokeWidth(1);
		circle.setStroke(Color.BLACK);
		currentPosition = null;
	}

	public void removeDestinationNode(Node node) {
		destinationSelected = false;
		Circle circle = (Circle)node;
		circle.setStrokeWidth(1);
		circle.setStroke(Color.BLACK);
		destinationPosition = null;
	}

	public void removeJumpNode(Node node) {
		Circle circle = (Circle)node;
		circle.setStrokeWidth(1);
		circle.setStroke(Color.BLACK);
		jumpPositions.remove(node);
		if (!jumpPositions.isEmpty()) {
			previousNode = jumpPositions.get(jumpPositions.size()-1);
		} else {
			previousNode = currentPosition;
		}
	}




	//OTHERS------------------------------------------------

	public static void run(Runnable treatment) {
		if(treatment == null) throw new IllegalArgumentException("The treatment to perform can not be null");
		if(Platform.isFxApplicationThread()) treatment.run();
		else Platform.runLater(treatment);
	}




	//TEST---------------------------------------------------------------------------
	public void submitButtonOnClick(ActionEvent event) {
	}

	public void fieldsHandleTest(MouseEvent event) {
		//System.out.println("mouse click detected! "+ event.getSource());
	}

    public void boardClick(MouseEvent event) {
//		System.out.println("Row: " + GridPane.getRowIndex((Node)event.getTarget())
//				+ "\nColumn: " + GridPane.getColumnIndex((Node)event.getTarget()));
	}
}
