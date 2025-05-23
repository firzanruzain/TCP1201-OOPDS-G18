import javafx.scene.text.Font;

import java.awt.Desktop;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.scene.control.TextField;

public class GUI extends Application {

	public static Stage mainStage;
	public static Scene mainScreenScene;
	public static javafx.geometry.Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
	public static double defHeight = screenSize.getHeight() - 100;
	public static double defWidth = screenSize.getWidth();
	
	class GameGUI extends Game {
		public static Button pauseButton = new Button("Pause");
		public static Scene playGameScene = new Scene(PlayGameScene.playGamePane, defWidth, defHeight);
		public static Scene pauseGameScene = new Scene(PauseGameScene.pauseGamePane, defWidth, defHeight);

		public static void init() {
			PauseGameScene.init();
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
			} else {
				System.out.println("Invalid command/card.");
			}
		}
		
		public static void playerStartTurn() {
			PlayGameScene.updateCenter();
        	deckButton.button.setDisable(true);
        	Label nextPlayerLabel = new Label("Player " + (turns[turn]+1) + "'s Turn");
			nextPlayerLabel.setFont(Font.font("Abyssinica SIL", FontWeight.BOLD, FontPosture.REGULAR, 100));
			Button proceedButton = new Button("Proceed");
			VBox centerBox = new VBox();
			centerBox.getChildren().addAll(nextPlayerLabel, proceedButton);
			centerBox.setAlignment(Pos.CENTER);
			
			proceedButton.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {
					PlayGameScene.updatePlayerCards();
					deckButton.button.setDisable(false);
				}
			});
			
			PlayGameScene.playGamePane.setBottom(centerBox);
			BorderPane.setMargin(centerBox, new Insets(0, 0, 50, 0));
		}
		
		public static void moveToNextPlayer() {
			if (Player.emptyCards()) {
				startNewRound();
			}
			turn = turn + 1;
	        if (turn == 5) {
	            turn = 0;
	        }
	        if (turn != 4) {
	        	playerStartTurn();
	        }else {
	        	playGame();
	        }
	        
			
		}
		
		public static int getTrickWinnerId() {
	        int winnerId = 0;

	        ArrayList<Integer> sorted = new ArrayList<Integer>(sameSuitCards.keySet());
	        Collections.sort(sorted, Collections.reverseOrder());
	        winnerId = sameSuitCards.get(sorted.get(0));

	        turn = 0;
	        turns[0] = winnerId;
	        updateTurns();
	        center.reset();
	        sameSuitCards.clear();
	        playedCards.clear();
	        trick++;
	        
	        // GUI
	        Scene trickWinnerScene;
	        BorderPane root = new BorderPane();
	        VBox centerBox = new VBox();
	        
	        Label winnerLabel = new Label("Player " + Integer.toString(winnerId+1));
	        winnerLabel.setFont(Font.font("Abyssinica SIL", FontWeight.BOLD, FontPosture.REGULAR, 150));
	        Label titleLabel = new Label("Wins the trick.");
	        titleLabel.setFont(Font.font("Abyssinica SIL", FontWeight.MEDIUM, FontPosture.REGULAR, 70));
	        Button proceedButton = new Button("Proceed");
	        proceedButton.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {
					playGame();
					
				}
			});
	        
	        centerBox.getChildren().addAll(winnerLabel,titleLabel,proceedButton);
	        centerBox.setAlignment(Pos.CENTER);
	        root.setCenter(centerBox);
	        root.getStylesheets().add("playGame.css");
	        trickWinnerScene = new Scene(root, defWidth, defHeight);
	        mainStage.setScene(trickWinnerScene);
	        
	        return winnerId;
	        
	    }
		
		public static void startNewRound() {
	        System.out.println("\n\nPlayer" + (Player.emptyId + 1) + " has cleared their cards!");
	        System.out.println("Next Round Begins.\n\n");
	        Player.calculateScores();
	        
	        Scene startNewRoundScene;
	        BorderPane root = new BorderPane();
	        VBox centerBox = new VBox();
	        centerBox.setAlignment(Pos.CENTER);
	        
	        Label titleLabel = new Label("Player " + (Player.emptyId +1) + " announced Go Boom!");
	        titleLabel.setFont(Font.font("Abyssinica SIL", FontWeight.BOLD, FontPosture.REGULAR, 100));
	        Label subtitleLabel = new Label("Update Scores");
	        subtitleLabel.setFont(Font.font("Abyssinica SIL", FontWeight.BOLD, FontPosture.REGULAR, 50));
	        
	        GridPane scoreTable = new GridPane();
	        scoreTable.setAlignment(Pos.CENTER);
	        for (int i = 0; i<4; i++) {
	        	int id = i + 1;
	        	int score = Players[i].getScore();
	        	Label playerLabel = new Label("Player " + id);
	        	Label playerScoreLabel = new Label(Integer.toString(score));
	        	
	        	playerLabel.setFont(Font.font("Abyssinica SIL", FontWeight.BOLD, FontPosture.REGULAR, 25));
	        	playerScoreLabel.setFont(Font.font("Abyssinica SIL", FontWeight.BOLD, FontPosture.REGULAR, 25));
	        	playerLabel.setAlignment(Pos.CENTER);
	        	playerScoreLabel.setAlignment(Pos.CENTER);
	        	playerLabel.setPadding(new Insets(0, 10, 0, 10));
	        	playerScoreLabel.setPadding(new Insets(0, 10, 0, 10));
	        	playerLabel.setTextFill(Color.WHITE);
	        	playerScoreLabel.setTextFill(Color.WHITE);
	        	scoreTable.add(playerLabel, i, 0);
	        	scoreTable.add(playerScoreLabel, i, 1);
	        	GridPane.setHalignment(playerLabel, HPos.CENTER);
	        	GridPane.setHalignment(playerScoreLabel, HPos.CENTER);
	        }
	        
	        Button proceedButton = new Button("Proceed");
	        
	        centerBox.getChildren().addAll(titleLabel,subtitleLabel,scoreTable,proceedButton);
	        root.setCenter(centerBox);
	        root.getStylesheets().add("playGame.css");
	        startNewRoundScene = new Scene(root, defWidth, defHeight);
	        mainStage.setScene(startNewRoundScene);
	        
	        proceedButton.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {
					turns[0] = 0;
			        updateTurns();
			        round++;
			        trick = 1;
			        turn=0;
			        deck.reset();
			        center.reset();
			        Player.reset();
			        Game.init();
			        init();
			        mainDisp();
					playGame();
				}
			});
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
					btn.setId("card");
					btn.setPadding(new Insets(0, 0, 0, 0));
					if (isPlayer) {
						
						btn.setOnAction(new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent event) {
								playerTurn(turns[turn], card);
								mainDisp();

							}
						});
						if (Card.playable(card)) {
							btn.setId("playableCard");
							btn.setPadding(new Insets(0, 0, 20, 0));
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
				button.setId("deck");
				if (deck.isEmpty()) {
					button.setId("emptyDeck");
					cardImgPathString = System.getProperty("user.dir") + "\\img\\cards\\emptyDeck.png";
				}
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
						if (!deck.isEmpty()) {
							Players[turns[turn]].drawCard();
							PlayGameScene.updatePlayerCards();
						}else {
							moveToNextPlayer();
						}
						mainDisp();

					}
				});
				return button;
			}

		}
		
		class PlayGameScene{
			public static BorderPane playGamePane = new BorderPane();
			public static GridPane centerGridPane;
			
			public static void updateCenter() {
				centerGridPane = new GridPane();
				cardButtonSet centerButtonSet = new cardButtonSet(center, false);
				centerGridPane.add(centerButtonSet.cardsBox, 1, 0);
				GridPane.setHalignment(centerButtonSet.cardsBox, HPos.CENTER);
				centerGridPane.setHgap(10);
				centerGridPane.setAlignment(Pos.TOP_CENTER);
				centerGridPane.add(deckButton.getDeckButton(), 0, 0);

				playGamePane.setCenter(centerGridPane);
			}
			
			public static void updatePlayerCards() {
				VBox bottomBox = new VBox();
				Label playerTurnLabel = new Label("Player " + (turns[turn] +1));
				playerTurnLabel.setFont(Font.font("Abyssinica SIL", FontWeight.BOLD, FontPosture.REGULAR, 25));
				cardButtonSet playerButtonSet = new cardButtonSet(Players[turns[turn]].cards, true);
				
				bottomBox.getChildren().addAll(playerTurnLabel, playerButtonSet.cardsBox);
				bottomBox.setAlignment(Pos.CENTER);
				BorderPane.setMargin(bottomBox, new Insets(0,0,10,0));
				playGamePane.setBottom(bottomBox);
			}
			
			public static void init() {
				// --------------top pane--------------
				HBox row1 = new HBox();
				row1.getChildren().add(pauseButton);
				row1.setAlignment(Pos.CENTER_RIGHT);
				playGamePane.setTop(row1);
				BorderPane.setMargin(row1, new Insets(10, 10, 50, 10));

				// center pane
				// Center Cards
				updateCenter();
				// Deck button
				 // set all center

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
				updatePlayerCards();

				// setting up scene and primary stage
				playGamePane.getStylesheets().add("playGame.css");
				
			}
		}

		static class PauseGameScene extends GameFiles{
			public static BorderPane pauseGamePane = new BorderPane();
			public static String cardImgPathString = System.getProperty("user.dir") + "\\img\\resume.png";
			public static Popup savePopup = new Popup();
			public static VBox saveBox = new VBox();
			public static Popup loadPopup = new Popup();
			public static VBox loadBox = new VBox();
			
			public static void proceedSave(String filename) {
				savePopup.getContent().clear();
				saveBox.getChildren().clear();
				
				Label titleLabel = new Label(filename + ": has been created.");
				Button proceedButton = new Button("Proceed");
				proceedButton.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event) {
						init();
						
					}
				});
				VBox.setMargin(titleLabel, new Insets(50, 0, 0, 0));
				saveBox.setAlignment(Pos.CENTER);
				saveBox.getChildren().addAll(titleLabel,proceedButton);
				savePopup.getContent().add(saveBox);
				updatePath(folderName);
		        createFolder();
		        player.save();
		        deckCenter.save();
		        gameInfo.save();
		        System.out.println("Game saved successfully.");
			}
			
			public static void save(String filename) {
				savePopup.getContent().clear();
				saveBox.getChildren().clear();
				Boolean folderExist;
		        updateFilesNames();
		        
		        folderExist = fileNames.contains(filename);
		        if (folderExist) {
		        	HBox proceedBox = new HBox();
		        	proceedBox.setAlignment(Pos.CENTER);
		        	proceedBox.setSpacing(10);
		        	Label titleLabel = new Label(filename + " already exits.");
		        	Button overwriteButton = new Button("Overwrite File");
		        	overwriteButton.setOnAction(new EventHandler<ActionEvent>() {
						
						@Override
						public void handle(ActionEvent event) {
							proceedSave(filename);
						}
					});
		        	Button cancelButton = new Button("Cancel");
		        	cancelButton.setOnAction(new EventHandler<ActionEvent>() {
						
						@Override
						public void handle(ActionEvent event) {
							init();
							
						}
					});
		        	
		        	proceedBox.getChildren().addAll(overwriteButton,cancelButton);
		        	VBox.setMargin(titleLabel, new Insets(50, 0, 0, 0));
		        	saveBox.getChildren().addAll(titleLabel,proceedBox);
		        	savePopup.getContent().add(saveBox);
		        }else {
		        	proceedSave(filename);
		        }
			}
			
			public static void load(String filename) {
				loadPopup.getContent().clear();
				loadBox.getChildren().clear();
				Boolean valid = false;
		        updateFilesNames();
		        System.out.println(filename);
		        
		        valid = fileNames.contains(filename);
		        if (valid) {
		        	updatePath(folderName);
		            player.load();
		            deckCenter.load();
		            gameInfo.load();
		            
		        	loadPopup.getContent().clear();
		        	loadBox.getChildren().clear();
					
					Label titleLabel = new Label(filename + ": has been loaded.");
					Button proceedButton = new Button("Proceed");
					proceedButton.setOnAction(new EventHandler<ActionEvent>() {
						
						@Override
						public void handle(ActionEvent event) {
							loadPopup.hide();
							playGame();
							
						}
					});
					VBox.setMargin(titleLabel, new Insets(150, 0, 0, 0));
					loadBox.setAlignment(Pos.CENTER);
					loadBox.getChildren().addAll(titleLabel,proceedButton);
					loadPopup.getContent().add(loadBox);
					loadPopup.show(mainStage);
		        }else {
		        	loadPopup.getContent().clear();
					loadBox.getChildren().clear();
					
					Label fileInvaLabel = new Label("File does not exist.");
		        	Label loadLabel = new Label("Enter File Name");
					TextField loadTextField = new TextField();
					Button loadSaveButton = new Button("Proceed");
					loadSaveButton.setOnAction(new EventHandler<ActionEvent>() {
						
						@Override
						public void handle(ActionEvent event) {
							load(loadTextField.getText());
							
						}
					});
					
					loadBox.getChildren().addAll(fileInvaLabel,loadLabel,loadTextField,loadSaveButton);
					VBox.setMargin(fileInvaLabel, new Insets(150, 0, 0, 0));
					loadPopup.getContent().add(loadBox);
		        }
			}
			
			public static void init() {
				// root
				VBox centerBox = new VBox();
				savePopup.getContent().clear();
				saveBox.getChildren().clear();
				savePopup.hide();
				loadPopup.getContent().clear();
				loadBox.getChildren().clear();
				loadPopup.hide();
				
				// resume button
				Button resumeButton = new Button();
				resumeButton.setStyle("-fx-background-color: transparent;");
				resumeButton.setMinHeight(80);
				FileInputStream input;
				try {
					input = new FileInputStream(cardImgPathString);
					Image image = new Image(input);
					ImageView img = new ImageView(image);
					resumeButton.setGraphic(img);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				resumeButton.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event) {
						playGame();
						
					}
				});
				
				// save button
				Button saveButton = new Button("Save");
				saveButton.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event) {
						savePopup.show(mainStage);
						
						
					}
				});
				savePopup.setOnHiding(new EventHandler<WindowEvent>() {
					
					@Override
					public void handle(WindowEvent event) {
						centerBox.setEffect(null);
						
					}
				});
				savePopup.setOnShowing(new EventHandler<WindowEvent>() {
					
					@Override
					public void handle(WindowEvent event) {
						centerBox.setEffect(new GaussianBlur(10));
						
					}
				});
				
				
				saveBox.setAlignment(Pos.CENTER);
				saveBox.setSpacing(10);
				
				
				Label saveLabel = new Label("Enter New File Name");
				TextField saveTextField = new TextField();
				Button proceedSaveButton = new Button("Proceed");
				proceedSaveButton.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event) {
						save(saveTextField.getText());
						
					}
				});
				
				saveBox.getChildren().addAll(saveLabel,saveTextField,proceedSaveButton);
				VBox.setMargin(saveLabel, new Insets(50, 0, 0, 0));
				savePopup.getContent().add(saveBox);
				
				
				// load Button
				Button loadButton = new Button("Load");
				loadButton.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event) {
						loadPopup.show(mainStage);
						
						
					}
				});
				loadPopup.setOnHiding(new EventHandler<WindowEvent>() {
					
					@Override
					public void handle(WindowEvent event) {
						centerBox.setEffect(null);
						
					}
				});
				loadPopup.setOnShowing(new EventHandler<WindowEvent>() {
					
					@Override
					public void handle(WindowEvent event) {
						centerBox.setEffect(new GaussianBlur(10));
						
					}
				});
				
				loadBox.setAlignment(Pos.CENTER);
				loadBox.setSpacing(10);
				
				Label loadLabel = new Label("Enter File Name");
				TextField loadTextField = new TextField();
				Button loadSaveButton = new Button("Proceed");
				loadSaveButton.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event) {
						load(loadTextField.getText());
						
					}
				});
				
				loadBox.getChildren().addAll(loadLabel,loadTextField,loadSaveButton);
				VBox.setMargin(loadLabel, new Insets(150, 0, 0, 0));
				loadPopup.getContent().add(loadBox);
				
				
				// help button
				Button helpButton = new Button("Help");
				helpButton.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event) {
						try {
						    Desktop.getDesktop().browse(new URL("https://github.com/firzanruzain/TCP1201-OOPDS-G18/blob/main/GoBoomRules.pdf").toURI());
						} catch (IOException e) {
						    e.printStackTrace();
						} catch (URISyntaxException e) {
						    e.printStackTrace();
						}
						
					}
				});
				
				// quit button
				Button quitButton = new Button("Quit");
				quitButton.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event) {
						mainStage.setScene(mainScreenScene);
						
					}
				});
				
				saveButton.setFont(Font.font("Abyssinica SIL", FontWeight.BOLD, FontPosture.REGULAR, 30));
				saveButton.setMinWidth(200);
				loadButton.setFont(Font.font("Abyssinica SIL", FontWeight.BOLD, FontPosture.REGULAR, 30));
				loadButton.setMinWidth(200);
				helpButton.setFont(Font.font("Abyssinica SIL", FontWeight.BOLD, FontPosture.REGULAR, 30));
				helpButton.setMinWidth(200);
				quitButton.setFont(Font.font("Abyssinica SIL", FontWeight.BOLD, FontPosture.REGULAR, 30));
				quitButton.setMinWidth(200);
				centerBox.getChildren().addAll(resumeButton,saveButton,loadButton,helpButton,quitButton);
				centerBox.setAlignment(Pos.CENTER);
				centerBox.setSpacing(20);
				pauseGamePane.setCenter(centerBox);
			}
		}
		
		public static void pause() {
			mainStage.setScene(pauseGameScene);
		}

		public static void playGame() {
			init();

			if (!Player.maxScore()) {
				if (!Player.emptyCards()) {
					if (turn < 4) {
						// for console
						mainDisp();
						PlayGameScene.init();
						mainStage.setScene(playGameScene); // set scene to playGame Scene
						playerStartTurn();
					} else { // trick ends
						System.out.println("Player" + (getTrickWinnerId() + 1) + " wins the trick! \n");
					}
				} else // round ends
				{
					startNewRound();
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
		title.setFill(Color.WHITE);

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
		loadGameButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				GUI.GameGUI.PauseGameScene.init();
				GameGUI.startNewGame();
				Scene emptyScene = new Scene(new Group(), defWidth, defHeight);
				primaryStage.setScene(emptyScene);
				GUI.GameGUI.PauseGameScene.loadPopup.show(primaryStage);
			}
		});

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

		mainBorderPane.getStylesheets().add("main.css");
		mainScreenScene = new Scene(mainBorderPane, defWidth, defHeight);
		primaryStage.setScene(mainScreenScene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		Card.main(args); // card init
		launch(args);
	}

}