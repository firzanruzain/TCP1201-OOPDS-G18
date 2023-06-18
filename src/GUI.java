import javafx.scene.text.Font;

import java.awt.GridLayout;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.geom.Rectangle2D;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.concurrent.Flow.Publisher;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.*;

public class GUI extends Application {

	public static Stage mainStage;
	public static Scene mainScreenScene;
	public static Button pauseButton = new Button("Pause");
	public static javafx.geometry.Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
	public static double defHeight = screenSize.getHeight() - 100;
	public static double defWidth = screenSize.getWidth();

	class GameGUI extends Game {

		public static void init() {
			Player.sortCards();
			pauseButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					pause();

				}
			});
		}

		public static void playerTurn(int id, String input) {
			if (Card.valid(input) && Card.playable(input)) {
				Players[id].playCard(input);
				moveToNextPlayer();
				playGame();
			} else {
				System.out.println("Invalid command/card.");
			}
		}

		static class cardButtonSet extends CardSet {
			public HashMap<Button, String> cardButtons = new HashMap<Button, String>();
			public HBox cardsBox = new HBox();

			public cardButtonSet(CardSet cardSet, Boolean isPlayer) {
				for (String card : cardSet.cards) {
					Button btn = new Button();
					String cardImgPathString = System.getProperty("user.dir") + "\\img\\cards\\" + card + ".png";
					FileInputStream input;
					try {
						input = new FileInputStream(cardImgPathString);
						Image image = new Image(input);
						ImageView img = new ImageView(image);
						btn.setGraphic(img);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (isPlayer) {
						btn.setOnAction(new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent event) {
								playerTurn(turns[turn], card);
								mainDisp();

							}
						});
						if (Card.playable(card)) {
							btn.setStyle("-fx-border-color: blue;");
						}
					}

					cardButtons.put(btn, card);
					cardsBox.getChildren().add(btn);
					cardsBox.setAlignment(Pos.CENTER);
				}
			}
		}

		class deckButton extends Deck {
			public static Button button = new Button();
			public static String cardImgPathString = System.getProperty("user.dir") + "\\img\\cards\\deck.png";

			public static Button getDeckButton() {
				button.setStyle("-fx-background-color: transparent;");
				FileInputStream input;
				try {
					input = new FileInputStream(cardImgPathString);
					Image image = new Image(input);
					ImageView img = new ImageView(image);
					button.setGraphic(img);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				button.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						Players[turns[turn]].drawCard();
						playGame();
						mainDisp();

					}
				});
				return button;
			}

		}

		public static void pause() {

		}

		public static void playGame() {
			init();

			if (!Player.maxScore()) {
				if (!Player.emptyCards()) {
					if (turn < 4) {
						// for console
						System.out.println("--Game Commands--");
						System.out.println("s --> Start a new game");
						System.out.println("x --> Exit the game");
						System.out.println("d --> Draw cards from deck");
						System.out.println("card --> A card played by the current player.\n");
						mainDisp();

						// for GUI
						// setting up
						BorderPane playGamePane = new BorderPane(); // root layout

						// --------------top pane--------------
						HBox row1 = new HBox();
						row1.getChildren().add(pauseButton);
						row1.setAlignment(Pos.CENTER_RIGHT);
						playGamePane.setTop(row1);
						BorderPane.setMargin(row1, new Insets(10, 10, 50, 10));

						// center pane
						GridPane centerGridPane = new GridPane();
						centerGridPane.setHgap(10);
						centerGridPane.setAlignment(Pos.TOP_CENTER);

						// Deck button
						centerGridPane.add(deckButton.getDeckButton(), 0, 0);

						// Center Cards
						cardButtonSet centerButtonSet = new cardButtonSet(center, false);
						centerGridPane.add(centerButtonSet.cardsBox, 1, 0);
						GridPane.setHalignment(centerButtonSet.cardsBox, HPos.CENTER);

						playGamePane.setCenter(centerGridPane); // set all center

						// --------------Right Pane--------------

						VBox scoresBox = new VBox();
						Label scoreTitleLabel = new Label("Score");
						scoreTitleLabel.setFont(Font.font("Abyssinica SIL", FontWeight.BOLD, FontPosture.REGULAR, 25));
						scoresBox.setAlignment(Pos.CENTER);
						Label player1ScoreLabel = new Label("Player 1: " + Players[0].getScore());
						Label player2ScoreLabel = new Label("Player 2: " + Players[1].getScore());
						Label player3ScoreLabel = new Label("Player 3: " + Players[2].getScore());
						Label player4ScoreLabel = new Label("Player 4: " + Players[3].getScore());
						player1ScoreLabel
								.setFont(Font.font("Abyssinica SIL", FontWeight.NORMAL, FontPosture.REGULAR, 20));
						player2ScoreLabel
								.setFont(Font.font("Abyssinica SIL", FontWeight.NORMAL, FontPosture.REGULAR, 20));
						player3ScoreLabel
								.setFont(Font.font("Abyssinica SIL", FontWeight.NORMAL, FontPosture.REGULAR, 20));
						player4ScoreLabel
								.setFont(Font.font("Abyssinica SIL", FontWeight.NORMAL, FontPosture.REGULAR, 20));
						scoresBox.getChildren().addAll(scoreTitleLabel, player1ScoreLabel, player2ScoreLabel,
								player3ScoreLabel, player4ScoreLabel);

						playGamePane.setRight(scoresBox);
						BorderPane.setMargin(scoresBox, new Insets(0, 50, 0, 0));

						// --------------Left Pane---------------

						VBox countBox = new VBox();
						Label roundLabel  = new Label("Round: " + round);
						Label trickLabel = new Label("Trick: " + trick);
						roundLabel.setFont(Font.font("Abyssinica SIL", FontWeight.BOLD, FontPosture.REGULAR, 25));
						trickLabel.setFont(Font.font("Abyssinica SIL", FontWeight.BOLD, FontPosture.REGULAR, 25));
						countBox.setAlignment(Pos.CENTER);
						countBox.getChildren().addAll(roundLabel, trickLabel);
						BorderPane.setMargin(countBox, new Insets(0, 0, 0, 50));
						playGamePane.setLeft(countBox);
						

						// --------------Bottom Pane-------------

						// Player Cards
						VBox bottomBox = new VBox();
						Label playerTurnLabel = new Label("Player " + (turns[turn] +1));
						playerTurnLabel.setFont(Font.font("Abyssinica SIL", FontWeight.BOLD, FontPosture.REGULAR, 25));
						cardButtonSet playerButtonSet = new cardButtonSet(Players[turns[turn]].cards, true);
						
						bottomBox.getChildren().addAll(playerTurnLabel, playerButtonSet.cardsBox);
						bottomBox.setAlignment(Pos.CENTER);
						BorderPane.setMargin(bottomBox, new Insets(0,0,10,0));
						playGamePane.setBottom(bottomBox);

						// setting up scene and primary stage
						playGamePane.getStylesheets().add("playGame.css");
						Scene playGameScene = new Scene(playGamePane, defWidth, defHeight);
						mainStage.setScene(playGameScene);
					} else { // trick ends
						System.out.println("Player" + (getTrickWinnerId() + 1) + " wins the trick! \n");
						playGame();
					}
				} else // round ends
				{
					startNewRound();
					playGame();
				}
			}

		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// all nodes
		mainStage = primaryStage; // set mainStage
		Text title = new Text("Go Boom");
		title.setFont(Font.font("Abyssinica SIL", FontWeight.BOLD, FontPosture.REGULAR, 100));

		Button startNewGameButton = new Button("New Game");
		startNewGameButton.setAlignment(Pos.CENTER);
		startNewGameButton.setMinWidth(100);
		startNewGameButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				GameGUI.startNewGame();
				GameGUI.playGame();
			}
		});
		Button loadGameButton = new Button("Load Game");
		loadGameButton.setAlignment(Pos.CENTER);
		loadGameButton.setMinWidth(100);

		// grouping and layouts
		VBox menusBox = new VBox();
		menusBox.getChildren().addAll(startNewGameButton, loadGameButton);
		menusBox.setAlignment(Pos.CENTER);
		menusBox.setSpacing(10);

		VBox centerGroup = new VBox();
		centerGroup.getChildren().addAll(title, menusBox);
		centerGroup.setAlignment(Pos.CENTER);
		centerGroup.setSpacing(100);

		BorderPane mainBorderPane = new BorderPane();
		mainBorderPane.setCenter(centerGroup);

		mainScreenScene = new Scene(mainBorderPane, defWidth, defHeight);
		primaryStage.setScene(mainScreenScene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		Card.main(args); // card init
		launch(args);
	}

}