package client.logic;

import client.core.ClientGUI;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import server.board.Coordinates;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

public class ClientGUIController {
	private ClientGUI client;
	private Node currentPosition;
	private Node destinationPosition;

	//LOGIN---------------------------------------
	@FXML
	private StackPane playerNickNamePanel;
	@FXML
	private Button loginButton;
	@FXML
	private TextField nickNameField;

	//MENU----------------------------------------
	@FXML
	private StackPane menu;
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
	private Button player2Button;
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
	//JOIN LOBBY----------------------------------
	@FXML
	private StackPane joinLobby;
	@FXML
	private Button cancelJoinLobbyButton;
	@FXML
	private Button joinLobbyButton;	
	//LOBBY---------------------------------------
	@FXML
	private StackPane lobby;
	@FXML
	private TextField invitePlayerField;
	@FXML
	private Button addBotButton;
	@FXML
	private Button removePlayerButton;
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
	//GAME----------------------------------------
	@FXML
	private StackPane game;
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
	private GridPane board;
	
	
	public ClientGUIController(ClientGUI client) {
		this.client = client;
	}
	
	@FXML
	void initialize() {
	}

	//LOGIN
	public void loginButtonOnClick(ActionEvent exent) {
		this.playerNickNamePanel.setVisible(false);
		this.playerNickNamePanel.setDisable(true);

		createPlayer();

		this.menu.setVisible(true);
		this.menu.setDisable(false);
	}
	
	//MENU
	public void newGameButtonOnClick(ActionEvent exent) {
		this.menu.setVisible(false);
		this.menu.setDisable(true);

		this.createLobby.setVisible(true);
		this.createLobby.setDisable(false);
	}
	
	public void exitButtonOnClick(ActionEvent event) {
		Platform.exit();
	}
	
	public void joinGameButtonOnClick(ActionEvent event) {
		this.menu.setVisible(false);
		this.menu.setDisable(true);

		this.joinLobby.setVisible(true);
		this.joinLobby.setDisable(false);
		
	}
	//CREATE LOBBY
	public void createLobbyButtonOnClick(ActionEvent event) {		
		this.createLobby.setVisible(false);
		this.createLobby.setDisable(true);

		client.lobbyName = lobbyNameField.getText();
		client.rowForPlayerPawn = (int)boardSizeSpinner.getValue();
		client.connection.invokeCreateLobbyMethod(client.factory, "createLobby", client.playerInLobby , client.rowForPlayerPawn, client.lobbyName, client.pid);

		this.lobby.setVisible(true);
		this.lobby.setDisable(false);
	}
	
	public void cancelCreateLobbyButtonOnClick(ActionEvent event) {
		this.createLobby.setVisible(false);
		this.createLobby.setDisable(true);
		
		this.menu.setVisible(true);
		this.menu.setDisable(false);
	}
	
	//JOIN LOBBY
	public void joinLobbyButtonOnClick(ActionEvent event) {
		this.joinLobby.setVisible(false);
		this.joinLobby.setDisable(true);
		
		this.lobby.setVisible(true);
		this.lobby.setDisable(false);
	}
	
	public void cancelJoinLobbyButtonOnClick(ActionEvent event) {
		this.joinLobby.setVisible(false);
		this.joinLobby.setDisable(true);
		
		this.menu.setVisible(true);
		this.menu.setDisable(false);
	}
	
	//LOBBY
	public void readyButtonOnClick(ActionEvent event) {
		this.lobby.setVisible(false);
		this.lobby.setDisable(true);
		
		this.game.setVisible(true);
		this.game.setDisable(false);
	}
	
	public void exitLobbyButtonOnClick(ActionEvent event) {
		this.lobby.setVisible(false);
		this.lobby.setDisable(true);
		
		this.menu.setVisible(true);
		this.menu.setDisable(false);
	}
	
	//GAME
	public void surrenderButtonOnClick(ActionEvent event) {
		this.game.setVisible(false);
		this.game.setDisable(true);
		
		this.menu.setVisible(true);
		this.menu.setDisable(false);
	}
	
    public void fieldsHandleTest(MouseEvent event) {
		System.out.println("mouse click detected! "+ event.getSource());
    }

    public void boardClick(MouseEvent event) {
		System.out.println("Row: " + GridPane.getRowIndex((Node)event.getTarget())
				+ "\nColumn: " + GridPane.getColumnIndex((Node)event.getTarget()));
	}

	public void createPlayer() {
		if (client.player == null) {
			client.connection.invokeCreatePlayerMethod(client.factory, "createPlayer", client.pid, nickNameField.getText());
			try {
				client.player = new ObjectName(client.domain+ client.pid +":type=jmx.Player,name=Player" + client.pid);
			} catch (MalformedObjectNameException e) {
				e.printStackTrace();
			}
		}
	}

	public void setPlayerNumberOn2ButtonOnClick(ActionEvent event) {
		client.playerInLobby = 2;
	}

	public void setPlayerNumberOn4ButtonOnClick(ActionEvent event) {
		client.playerInLobby = 4;
	}

	public void setPlayerNumberOn6ButtonOnClick(ActionEvent event) {
		client.playerInLobby = 6;
	}

	public void doMoveOnClick(ActionEvent event) {
		if (currentPosition != null && destinationPosition != null) {
			Paint fill;
			Coordinates cCoordinates = new Coordinates(board.getColumnIndex(currentPosition), board.getRowIndex(currentPosition));
			Coordinates dCoordinates = new Coordinates(board.getColumnIndex(destinationPosition), board.getRowIndex(destinationPosition));
			client.connection.invokeMovePlayerMethod(client.player, "move", cCoordinates, dCoordinates);
			Circle circleC = (Circle)currentPosition;
			circleC.setStrokeWidth(1);
			circleC.setStroke(Color.BLACK);
			fill = circleC.getFill();
			circleC.setFill(Color.WHITE);
			currentPosition = null;
			Circle circleD = (Circle)destinationPosition;
			circleD.setStrokeWidth(1);
			circleD.setFill(fill);
			circleD.setStroke(Color.BLACK);
			destinationPosition = null;
		}
	}

	//TEST
	public void submitButtonOnClick(ActionEvent event) {
	}

	public void chooseCircleOnClick(MouseEvent event) {
		ObservableList<Node> childrens = board.getChildren();
		for (Node node : childrens) {
			if(board.getRowIndex(node) == GridPane.getRowIndex((Node)event.getTarget()) &&
					board.getColumnIndex(node) == GridPane.getColumnIndex((Node)event.getTarget())) {
				if (currentPosition == null && node != destinationPosition) {
					Circle circle = (Circle)node;
					circle.setStrokeWidth(4);
					circle.setStroke(Color.GREEN);
					currentPosition = node;
				} else if (node == currentPosition) {
					Circle circle = (Circle)node;
					circle.setStrokeWidth(1);
					circle.setStroke(Color.BLACK);
					currentPosition = null;
				} else if (destinationPosition == null) {
					Circle circle = (Circle)node;
					circle.setStrokeWidth(4);
					circle.setStroke(Color.RED);
					destinationPosition = node;
				} else if (node == destinationPosition) {
					Circle circle = (Circle)node;
					circle.setStrokeWidth(1);
					circle.setStroke(Color.BLACK);
					destinationPosition = null;
				}
				break;
			}
		}
	}
}
