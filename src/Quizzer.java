import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Quizzer extends Application{
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage window) {
		
		window.setTitle("Quizzer!!");
		
		final int WINDOW_HEIGHT = 500;
		final int WINDOW_WIDTH = 800;
		
		Alert alert = new Alert(AlertType.WARNING);
	    alert.setHeaderText("Please choose a deck");
		alert.setResizable(true);
		
		ArrayList<Question> questionList = new ArrayList<>();
		
		/*
		 * Back and forth buttons for the main menu
		 */
		Button editMainMenuButton = new Button("Main Menu");
		editMainMenuButton.getStyleClass().add("mainMenuButton");
		
		Button flashMainMenuButton = new Button("Main Menu");
		flashMainMenuButton.getStyleClass().add("mainMenuButton");

		Button quizMainMenuButton = new Button("Main Menu");
		quizMainMenuButton.getStyleClass().add("mainMenuButton");

		Button chooseMainMenuButton = new Button("Main Menu");
		chooseMainMenuButton.getStyleClass().add("mainMenuButton");
		
		
		/*
		 * Main menu buttons
		 */
		
		Button editModeButton = new Button("Add Card / Edit Deck");
		editModeButton.getStyleClass().add("auxButton");
		
		Button chooseModeButton = new Button("Choose / Create Deck");
		chooseModeButton.getStyleClass().add("auxButton");
		
		Button flashModeButton = new Button("Flash Card Mode");	
		flashModeButton.getStyleClass().add("flashButton");
		
		Button quizModeButton = new Button("Quiz Yourself!");
		quizModeButton.getStyleClass().add("quizButton");


		/*
		 * Main menu section
		 */
		HBox modeSelectorBox = new HBox(5, flashModeButton, quizModeButton);
		HBox deckEditNewBox = new HBox(5, editModeButton, chooseModeButton);
		modeSelectorBox.setAlignment(Pos.CENTER);
		deckEditNewBox.setAlignment(Pos.CENTER);
		
		Label selectedDeckLabel = new Label("No deck selected!");
		selectedDeckLabel.setPrefHeight(90);
		selectedDeckLabel.setStyle("-fx-font-size: 35px; -fx-underline: true;");
		
		VBox mainMenuSelectorBox = new VBox(5, selectedDeckLabel, modeSelectorBox, deckEditNewBox);
		mainMenuSelectorBox.setAlignment(Pos.CENTER);
		
		Scene mainMenuScene = new Scene(mainMenuSelectorBox,WINDOW_WIDTH,WINDOW_HEIGHT);
		mainMenuScene.getStylesheets().add("style.css");
		
		/*
		 * Edit mode section
		 */
		GridPane editGrid = new GridPane();
		
		ListView<FlashCard> cardList = new ListView<>();
		cardList.setPrefSize(275, 500);
				
		ImageView cardImage = new ImageView(new Image("file:assets/noimg.png"));
		cardImage.setFitHeight(300);
		cardImage.setPreserveRatio(true);
		
		TextArea frontSideCardInfo = new TextArea("Front side info");
		TextArea backSideCardInfo = new TextArea("Back side info");
		TextField cardImageUrl = new TextField("Image Url");
	
		Button saveCardButton = new Button("Save Card");
		saveCardButton.setStyle("-fx-background-color: #35A7FF; -fx-text-fill: white;");
		
		Button newCardButton = new Button("New Card");
		newCardButton.setStyle("-fx-background-color: #1B998B; -fx-text-fill: white;");

		Button deleteCardButton = new Button("Delete Card");
		deleteCardButton.setStyle("-fx-background-color: #FF5964; -fx-text-fill: white;");
		
		Button loadImgButton = new Button("Reload Image");
		loadImgButton.setStyle("-fx-background-color: #1B998B; -fx-text-fill: white;");

		VBox cardInfoBox = new VBox(5, cardImage, frontSideCardInfo, backSideCardInfo,
									cardImageUrl, new HBox(newCardButton , deleteCardButton, saveCardButton, loadImgButton));
		
		cardInfoBox.setAlignment(Pos.CENTER);
		
		editGrid.add(editMainMenuButton, 0, 0);
		editGrid.add(cardList, 0, 1);
		editGrid.add(cardInfoBox, 1, 1);
		
		editGrid.setVgap(5);
		editGrid.setHgap(5);
		
		editGrid.setPadding(new Insets(5));
		Scene editModeScene = new Scene(editGrid,WINDOW_WIDTH,WINDOW_HEIGHT);
		editModeScene.getStylesheets().add("style.css");
		
		
		/*
		 * Flash mode section
		 */
		ImageView flashModeImg = new ImageView(new Image("file:assets/noimg.png"));
		flashModeImg.setFitHeight(300);
		flashModeImg.setPreserveRatio(true);
		
		Label flashFrontInfo = new Label("Stuff and things?");
		TextArea flashBackInfo = new TextArea("No, things and stuff!");
				
		flashFrontInfo.setMaxWidth(WINDOW_WIDTH - 50);
		flashFrontInfo.setStyle("-fx-font-size:18px;");
		flashFrontInfo.setMaxHeight(50);
		flashFrontInfo.setWrapText(true);
		flashFrontInfo.setAlignment(Pos.CENTER);
		
		flashBackInfo.setMaxWidth(WINDOW_WIDTH - 50);
		flashBackInfo.setMaxHeight(50);
		flashBackInfo.setEditable(false);
		flashBackInfo.setVisible(false);
		flashBackInfo.setMaxWidth(WINDOW_WIDTH/2);
		flashBackInfo.setStyle("-fx-control-inner-background: #f4f4f4; -fx-font-size: 15px;");


		Button nextCardButton = new Button(">");
		nextCardButton.setPrefSize(100, 15);
		nextCardButton.getStyleClass().add("changeButton");

		Button prevCardButton = new Button("<");
		prevCardButton.setPrefSize(100, 15);
		prevCardButton.getStyleClass().add("changeButton");

		Label flashPositionLabel = new Label();
		
		Button flashModeRevealButton = new Button("Reveal");
		flashModeRevealButton.setPrefSize(90, 15);
		flashModeRevealButton.getStyleClass().add("posButton");
		
		HBox flashControlBox = new HBox(10, prevCardButton, flashModeRevealButton, nextCardButton);
		VBox flashDisplayBox = new VBox(5, flashModeImg, flashFrontInfo, flashBackInfo,
										flashPositionLabel, flashControlBox);
		
		flashDisplayBox.setAlignment(Pos.CENTER);
		flashControlBox.setAlignment(Pos.CENTER);
		
		VBox flashInfoBox = new VBox(5, flashMainMenuButton, flashDisplayBox);
		flashInfoBox.setPadding(new Insets(5));
		
		Scene flashModeScene = new Scene(flashInfoBox,WINDOW_WIDTH, WINDOW_HEIGHT);
		flashModeScene.getStylesheets().add("style.css");
		
		/*
		 * Quiz mode section
		 */
		
		// Use a srollpane to make it fit the size
		ScrollPane sp = new ScrollPane();
		
		Label numQsLabel = new Label("Number of questions: ");
		numQsLabel.getStyleClass().add("fifteenPxFont");
		
		TextField numQsField = new TextField("4");
		numQsField.setPrefWidth(60);
		
		Button genQs = new Button("Generate questions");
		genQs.getStyleClass().add("posButton");
		
		Button genHTML = new Button("Generate HTML quiz");
		genHTML.getStyleClass().add("genHtml");

		
		HBox genQsBox = new HBox(10, new VBox(5, genQs, genHTML), numQsLabel, numQsField);
		
		GridPane quizGrid = new GridPane();
				
		Button submitButton = new Button("Submit Answers");
		submitButton.getStyleClass().add("posButton");
		
		Label numCorrectLabel = new Label();
		numCorrectLabel.getStyleClass().add("fifteenPxFont");
		
		CheckBox checkWantsAns = new CheckBox("Show Correct?");
		checkWantsAns.getStyleClass().add("fifteenPxFont");

		Button goHomeQuizButton = new Button("Main Menu");
		goHomeQuizButton.getStyleClass().add("mainMenuButton");
		
		goHomeQuizButton.setOnAction(e1 -> {
			quizMainMenuButton.setVisible(true);
			quizMainMenuButton.getOnAction().handle(null);
		});
		
		goHomeQuizButton.setVisible(false);
		
		HBox submitQuizBox = new HBox(15, submitButton, checkWantsAns, numCorrectLabel, goHomeQuizButton);
		submitQuizBox.setPadding(new Insets(10,0,0,0));
		
		genHTML.setOnAction(e -> {
			String numQsString = numQsField.getText();
			Question.resetAnswerKey();

			if(numQsString.matches("[0-9]*")) {
				int numQs = Integer.parseInt((numQsString));

				if(numQs <= FlashCard.getCards().size() && numQs != 0) {
					String bodyBefore = "<html lang = \"en\"> <head><meta charset=\"utf-8\">"
							+ "<style>body { font-family: \"Courier New\", Courier, monospace; }"
							+ "#imgDiv, #answerDiv{ align: center; display: inline-block; width: 45%; height: auto; word-wrap: break-word; vertical-align:top; margin: 10px;}"
							+ "img {height: 300px; width: auto; } "
							+ "#answerKey {  -webkit-transform: rotateX(180deg); transform: rotateX(180deg); -moz-transform: scale(-1, 1);-webkit-transform: scale(-1, 1);-o-transform: scale(-1, 1);-ms-transform: scale(-1, 1);transform: scale(-1, 1);} "
							+ "</style></head><body>";
					
					String bodyAfter  = "</body></html>";
					
					genQsBox.setVisible(false);
					Question.setDeck(FlashCard.getCards());
					questionList.clear();
					Question.resetAnswerKey();
					
					PrintWriter printWriter = null;
					
					try {
						printWriter = new PrintWriter(new FileWriter("HTML_Quizzes/" + FlashCard.getDeckName() + ".html"));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
					printWriter.print(bodyBefore);
					
					for(int i = 0; i < numQs; i++) {
						questionList.add(new Question());

						printWriter.print(questionList.get(i).getHtml(i+1));
						
						printWriter.print("<hr>");
					}
					
					printWriter.print("<div id='answerKey'> " + Question.getHtmlAnswerKey() + "</div>");
					printWriter.print(bodyAfter);

					printWriter.close();
					
					Hyperlink link = new Hyperlink();
					link.setText("Click here to open the quiz in your browser!");
					
					String pathString = new File("HTML_Quizzes/" + FlashCard.getDeckName() + ".html").getAbsolutePath();
					
					quizGrid.add(new VBox(10, link, new Label("It can also be found in: " + pathString )), 0, 1);
					
					link.setOnAction(event -> {
						getHostServices().showDocument(new File("HTML_Quizzes/" + FlashCard.getDeckName() + ".html").getAbsolutePath());
					});
					
				} else if(numQs == 0){
					Alert numZeroAlert = new Alert(AlertType.ERROR, "You can not have a quiz with zero questions!");
					numZeroAlert.setResizable(true);
					numZeroAlert.show();
					
				} else {
					Alert numTooHighAlert = new Alert(AlertType.ERROR, "You do not have that many cards in your deck!");
					numTooHighAlert.setResizable(true);
					numTooHighAlert.show();
				}
				
			} else {
				Alert badInputAlert = new Alert(AlertType.ERROR, "This is not a number!");
				badInputAlert.setResizable(true);
				badInputAlert.show();
			}
		});
		
		genQs.setOnAction(e -> {
			String numQsString = numQsField.getText();
			Question.resetAnswerKey();

			if(numQsString.matches("[0-9]*")) {
				int numQs = Integer.parseInt((numQsString));

				if(numQs <= FlashCard.getCards().size() && numQs != 0) {
					quizMainMenuButton.setVisible(false);

					genQsBox.setVisible(false);
					
					Question.setDeck(FlashCard.getCards());
					
					questionList.clear();
					
					int counter = 0;
					
					for(int i = 0; i < numQs; i++) {
						questionList.add(new Question());
						
						quizGrid.add(questionList.get(i).getBody(), 0, i);
						
						counter = i;
					}
					
					quizGrid.add(new HBox(10, submitQuizBox) , 0, counter + 2);
									
				} else if(numQs == 0){
					Alert numZeroAlert = new Alert(AlertType.ERROR, "You can not have a quiz with zero questions!");
					numZeroAlert.setResizable(true);
					numZeroAlert.show();
					
				} else {
					Alert numTooHighAlert = new Alert(AlertType.ERROR, "You do not have that many cards in your deck!");
					numTooHighAlert.setResizable(true);
					numTooHighAlert.show();
				}
			} else {
				Alert badInputAlert = new Alert(AlertType.ERROR, "This is not a number!");
				badInputAlert.setResizable(true);
				badInputAlert.show();
			}
		});
		
		submitButton.setOnAction(e -> {
			String answerKey = Question.getAnswerKey();
			
			int totalCorrect = 0;
			
			int numQs = questionList.size();

			for(int i = 0; i < questionList.size(); i++) {
				if(questionList.get(i).getSelected() == answerKey.charAt(i)) {
					totalCorrect++;
					questionList.get(i).getBody().setStyle("-fx-background-color:#73BFB8");
					
				} else if(questionList.get(i).getSelected() == 'N') {
					questionList.get(i).getBody().setStyle("-fx-background-color:#FDCA40");
					
					if(checkWantsAns.isSelected()) {
						questionList.get(i).highlightCorrect();
					}
					
				} else {
					questionList.get(i).getBody().setStyle("-fx-background-color:#FF6B6C");
					
					if(checkWantsAns.isSelected()) {
						questionList.get(i).highlightCorrect();
					}				
				}
			}
			
			numCorrectLabel.setText("You scored: " + 
									new DecimalFormat("###.##").format(100* ((double)totalCorrect/(double)numQs))
									+ "%");
			
			goHomeQuizButton.setVisible(true);
		});

		quizGrid.setVgap(5);
		
		sp.setContent(quizGrid);
		sp.setPadding(new Insets(5));
		Scene quizModeScene = new Scene(sp, WINDOW_WIDTH, WINDOW_HEIGHT);
		quizModeScene.getStylesheets().add("style.css");
		
		/*
		 *  Choose Deck Section
		 */
		GridPane chooseDeckGrid = new GridPane();
		ListView<String> deckChooser = new ListView<String>();
		deckChooser.setPrefSize(390,900);
		
		Label deckTitleLabel = new Label("Select a deck!");
		deckTitleLabel.setStyle("-fx-font-size:25px;");
		deckTitleLabel.setPrefWidth(300);
		deckTitleLabel.setWrapText(true);
		
		Label deckInfoLabel = new Label();
		deckInfoLabel.setPrefWidth(300);
		deckInfoLabel.setWrapText(true);
		deckInfoLabel.setVisible(false);
		
		Label cardNumberLabel = new Label();
		cardNumberLabel.setPrefHeight(50);
		cardNumberLabel.setAlignment(Pos.TOP_LEFT);
		cardNumberLabel.setVisible(false);
		
		Button addDeckButton = new Button("Create Deck");
		addDeckButton.getStyleClass().add("posButton");
		
		TextField createDeckName = new TextField("Deck name");
		TextField createDeckInfo = new TextField("Deck description");
		
		addDeckButton.setOnAction(e -> {
			Alert tempAlert = new Alert(AlertType.WARNING);
			tempAlert.setResizable(true);
			
			if(createDeckName.getText().contains(" ")) {
				tempAlert.setContentText("Deck names must not contain spaces!");
				tempAlert.show();
				
			} else if(createDeckInfo.getText().equals("")){
				tempAlert.setContentText("You need a description!");
			
			} else {
				File f = new File("decks/" + createDeckName.getText());
				try {
					f.createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				try {
					FileWriter fw = new FileWriter(f);
					fw.write(createDeckInfo.getText());
					fw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				chooseModeButton.getOnAction().handle(null);
				
				try {
					FlashCard.setDeck(createDeckName.getText());
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		VBox createDeckBox = new VBox(10, new Label("Create a new deck"), createDeckName, createDeckInfo,
										addDeckButton);
		
		Button deckDeleteButton = new Button("Delete");
		deckDeleteButton.getStyleClass().add("deckDeleteButton");
		
		deckDeleteButton.setOnAction(e -> {
			Alert deleteDeckAlert = new Alert(AlertType.CONFIRMATION);
			deleteDeckAlert.setTitle("Are you sure?");
			deleteDeckAlert.setContentText("Are you sure you would like to delete this deck?");
			deleteDeckAlert.setResizable(true);
			
			ButtonType yesButton  = new ButtonType("Yes", ButtonData.YES);
			ButtonType cancelButton  = new ButtonType("Cancel", ButtonData.NO);

			deleteDeckAlert.getButtonTypes().setAll(yesButton, cancelButton);
			
			if(deckChooser.getSelectionModel().getSelectedItem() != null) {
				deleteDeckAlert.showAndWait().ifPresent(type -> {				
					if(type.getButtonData().equals(ButtonData.YES)) {
						File f = new File("decks/" + deckChooser.getSelectionModel().getSelectedItem());
						f.delete();
						chooseModeButton.getOnAction().handle(null);
						
						String defDeck = new String();
						try {
							Scanner s = new Scanner(new File("src/deckConf"));
							
							defDeck = s.nextLine();
							
							s.close();
							
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}
						
						if(FlashCard.getDeckName().equals(defDeck)) {
							PrintWriter w = null;
							try {
								w = new PrintWriter(new File("src/deckConf"));
							} catch (FileNotFoundException e1) {
								e1.printStackTrace();
							}
							
							w.print("");
							w.close();
						}
					}
				});
			}
		});
			
		HBox titleDelBox = new HBox(10, deckTitleLabel, deckDeleteButton);
		
		// Horizontal separator
		Separator horizontalSeparator = new Separator();
		horizontalSeparator.setOrientation(Orientation.HORIZONTAL);
		
		VBox infoBox = new VBox(10, titleDelBox, deckInfoLabel, cardNumberLabel, horizontalSeparator, createDeckBox);
		
		chooseDeckGrid.add(chooseMainMenuButton, 0, 0);
		chooseDeckGrid.add(deckChooser, 0, 1);
		chooseDeckGrid.add(infoBox, 1, 1);
		chooseDeckGrid.setPadding(new Insets(5));
		chooseDeckGrid.setHgap(5);
		chooseDeckGrid.setVgap(5);
		
		Scene chooseDeckScene = new Scene(chooseDeckGrid, WINDOW_WIDTH, WINDOW_HEIGHT);
		chooseDeckScene.getStylesheets().add("style.css");
		
		/*
		 * Event handlers for buttons
		 */
		
		// Mode buttons for main menu
		editModeButton.setOnAction(e -> 	{
			if(FlashCard.getDeckName() == null) {
				alert.show();
			
			} else {
				ArrayList<FlashCard> cards = null;
				
				cards = FlashCard.getCards();
				
				window.setScene(editModeScene);
				cardList.getItems().clear();
				
				for(FlashCard c : cards) {
					cardList.getItems().add(c);
				}
				
				frontSideCardInfo.setText("");
				backSideCardInfo.setText("");
				cardImageUrl.setText("");
				cardImage.setImage(new Image("file:assets/noimg.png"));
			}
			
		});
		
		// Listview listener
		cardList.getSelectionModel().selectedItemProperty().addListener(e -> {
			FlashCard tempCard = cardList.getSelectionModel().getSelectedItem();
						
			if(tempCard != null) {
				frontSideCardInfo.setText(tempCard.toString());
				backSideCardInfo.setText(tempCard.getBackInfo());
				
				cardImageUrl.setText(tempCard.getImgUrl());
				
				try {
					cardImage.setImage(new Image(tempCard.getImgUrl()));
				} catch(Exception e1) {
					cardImage.setImage(new Image("file:assets/noimg.png"));
				}
			}
		});
		
		newCardButton.setOnAction(e -> {
			cardList.getSelectionModel().clearSelection();
			
			frontSideCardInfo.setText("");
			backSideCardInfo.setText("");
			cardImageUrl.setText("");
			
			cardImage.setImage(new Image("file:assets/noimg.png"));
		});
		
		saveCardButton.setOnAction(e -> {
			Alert cardErr = new Alert(AlertType.WARNING);
			cardErr.setResizable(true);
			
			if(frontSideCardInfo.getText().equals("") && backSideCardInfo.getText().equals("") && cardImageUrl.getText().equals("")) {
				cardErr.setHeaderText("This card is totally empty!");
				cardErr.show();
				
			} else if(frontSideCardInfo.getText().equals("") && cardImageUrl.getText().equals("")) {
				cardErr.setHeaderText("This card is missing something to flip from!");
				cardErr.show();
				
			} else if(backSideCardInfo.getText().equals("")) {
				cardErr.setHeaderText("This card is missing something to flip to!");
				cardErr.show();

			} else {
				String front, back, img;
				
				front = frontSideCardInfo.getText();
				back = backSideCardInfo.getText();
				img = cardImageUrl.getText();

				if(img.equals("")) { img = "no"; }
				
				if(cardList.getSelectionModel().getSelectedItem() == null)  {
					FlashCard.getCards().add(new FlashCard(front, back, img));
					
					try {
						FlashCard.save();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
					cardList.getItems().clear();
					cardList.getItems().addAll(FlashCard.getCards());
					
					frontSideCardInfo.setText("");
					backSideCardInfo.setText("");
					cardImageUrl.setText("");
					cardImage.setImage(new Image("file:assets/noimg.png"));
					
				} else {
					ArrayList<FlashCard> temp = new ArrayList<>();
					cardList.getItems().remove(cardList.getSelectionModel().getSelectedItem());
					cardList.getItems().add(new FlashCard(front, back, img));
					
					for(FlashCard f : cardList.getItems()) {
						temp.add(f);
					}
					
					try {
						FlashCard.setDeckByList(temp);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		deleteCardButton.setOnAction(e -> {
			Alert delCardAlert = new Alert(AlertType.INFORMATION, "Card deleted!");
			delCardAlert.setResizable(true);
			delCardAlert.show();
			
			FlashCard tempCard = cardList.getSelectionModel().getSelectedItem();
			
			if(tempCard != null) {
				frontSideCardInfo.setText("");
				backSideCardInfo.setText("");
				cardImageUrl.setText("");
				
				loadImgButton.getOnAction().handle(null);
				
				cardList.getItems().remove(cardList.getSelectionModel().getSelectedItem());
				
				ArrayList<FlashCard> temp = new ArrayList<>();
				
				for(FlashCard f : cardList.getItems()) {
					temp.add(f);
				}
				
				try {
					FlashCard.setDeckByList(temp);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				try {
					FlashCard.save();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
			
		});
		
		deckChooser.getSelectionModel().selectedItemProperty().addListener(e -> {
			String deckName = deckChooser.getSelectionModel().getSelectedItem();
			
			if(deckName != null && new File("decks/" + deckName).exists()) {
				File f = new File("decks/" + deckName);
				Scanner fileReader = null;
				
				int cardNum = 0;
				
				try {
					fileReader = new Scanner(f);
				} catch (FileNotFoundException e1) {
					selectedDeckLabel.setText("No deck selected");
				}
		
				String deckInfo = fileReader.nextLine();
								
				try {
					FlashCard.setDeck(deckName);
				} catch (FileNotFoundException e2) {
					e2.printStackTrace();
				}
				
				
				FlashCard.setInfo(deckInfo);
				
				cardNum = FlashCard.getCards().size();
				
				selectedDeckLabel.setText(FlashCard.getDeckName());
				
				deckTitleLabel.setText(deckName);
				deckInfoLabel.setText(deckInfo);
				deckInfoLabel.setVisible(true);
				
				cardNumberLabel.setText("This deck contains " + cardNum + " cards.");
				cardNumberLabel.setVisible(true);

				fileReader.close();
				
				try {
					FileWriter tempFw = new FileWriter("src/deckConf");
					tempFw.write(FlashCard.getDeckName());
					
					tempFw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			} else {
				deckTitleLabel.setText("Select a deck!");
				deckInfoLabel.setVisible(false);
				cardNumberLabel.setVisible(false);
			}
		});
				
		flashModeRevealButton.setOnAction(e -> {
			if(flashBackInfo.isVisible()) {
				flashBackInfo.setVisible(false);
			} else {
				flashBackInfo.setVisible(true);
			}
		});
		
		nextCardButton.setOnAction(e -> {

			ArrayList<FlashCard> tempDeck = null;
			tempDeck = FlashCard.getCards();
			
			int index = FlashCard.getPos();
			
			if(index == tempDeck.size()-1) {
				FlashCard.setPos(0);
				index = FlashCard.getPos();
			
			} else {
				FlashCard.setPos(1);
				index = FlashCard.getPos();
			}
			
			try {
				flashModeImg.setImage(new Image(tempDeck.get(index).getImgUrl()));
			} catch(Exception e1) {
				flashModeImg.setImage(new Image("file:assets/noimg.png"));
			}
			flashFrontInfo.setText(tempDeck.get(index).toString());
			flashBackInfo.setText(tempDeck.get(index).getBackInfo());
			
			flashPositionLabel.setText(index+1 + "/" + tempDeck.size());

			if(flashBackInfo.isVisible()) { flashBackInfo.setVisible(false); }
		});
		
		prevCardButton.setOnAction(e -> {
			ArrayList<FlashCard> tempDeck = null;
			tempDeck = FlashCard.getCards();
			
			int index = FlashCard.getPos();
			
			if(index == 0) {
				FlashCard.setPos(-2);
				index = FlashCard.getPos();
			
			} else {
				FlashCard.setPos(-1);
				index = FlashCard.getPos();
			}
			
			try {
				flashModeImg.setImage(new Image(tempDeck.get(index).getImgUrl()));
			} catch(Exception e1) {
				flashModeImg.setImage(new Image("file:assets/noimg.png"));
			}
			
			flashFrontInfo.setText(tempDeck.get(index).toString());
			flashBackInfo.setText(tempDeck.get(index).getBackInfo());
			
			flashPositionLabel.setText(index+1 + "/" + tempDeck.size());

			if(flashBackInfo.isVisible()) { flashBackInfo.setVisible(false); }
		});
		
		flashModeButton.setOnAction(e -> {	
			FlashCard.setPos(0);
			if(FlashCard.getDeckName() == null) {
				alert.show();
			
			} else if (FlashCard.getCards().size() == 0) {
				Alert noCardsAlert = new Alert(AlertType.WARNING, "Your deck must have at least one card to do this");
				noCardsAlert.setResizable(true);
				noCardsAlert.show();
				
				
			} else {
				ArrayList<FlashCard> deck = null;
				deck = FlashCard.getCards();
				
				try {
					flashModeImg.setImage(new Image(deck.get(0).getImgUrl()));
				} catch(Exception e1) {
					flashModeImg.setImage(new Image("file:assets/noimg.png"));
				}
				flashFrontInfo.setText(deck.get(0).toString());
				flashBackInfo.setText(deck.get(0).getBackInfo());
	
				flashPositionLabel.setText("1/" + deck.size());
				window.setScene(flashModeScene);
			}
		});
		
		quizModeButton.setOnAction(e ->	{
			genQsBox.setVisible(true);

			goHomeQuizButton.setVisible(false);
	
			quizGrid.getChildren().clear();
			numCorrectLabel.setText("");
			
			quizGrid.add(quizMainMenuButton, 0,0);
			quizGrid.add(genQsBox, 0, 1);

			if(FlashCard.getDeckName() == null) {
				alert.show();
			
			} else if(FlashCard.getCards().size() < 4) {
				Alert noCards = new Alert(AlertType.WARNING, "Your deck must have at least four cards for this!");
				noCards.setResizable(true);
				noCards.show();
				
			} else {
				window.setScene(quizModeScene);
			}
		});
					
		chooseModeButton.setOnAction(e ->	{
			File dir = new File("decks");
			String[] dirList = dir.list();
			
			deckChooser.getItems().clear();
			
			for(String s : dirList) {
				deckChooser.getItems().add(s);
			}
			window.setScene(chooseDeckScene);
		
		});
		
		loadImgButton.setOnAction(e -> {
			try {
				cardImage.setImage(new Image(cardImageUrl.getText()));
			
			} catch(Exception e1) {
				cardImage.setImage(new Image("file:assets/noimg.png"));
			}
		});
		
		cardImageUrl.textProperty().addListener(e -> {
			loadImgButton.getOnAction().handle(null);
		});
		
		//Flash mode keybind
		
		flashModeScene.setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.Q) {	prevCardButton.getOnAction().handle(null);	}
			if(e.getCode() == KeyCode.W) {	flashModeRevealButton.getOnAction().handle(null);	}
			if(e.getCode() == KeyCode.E) {	nextCardButton.getOnAction().handle(null);	}
		});
		
		// Main menu buttons for scenes
		editMainMenuButton.setOnAction(e ->   {	window.setScene(mainMenuScene);	});
		flashMainMenuButton.setOnAction(e ->  {	window.setScene(mainMenuScene);	});
		quizMainMenuButton.setOnAction(e ->   {	window.setScene(mainMenuScene);	});
		chooseMainMenuButton.setOnAction(e -> { window.setScene(mainMenuScene); });
		
		try {
			File f = new File("src/deckConf");
			Scanner s = new Scanner(f);
			String tempDeck ;
			
			try {
				tempDeck = s.nextLine();
			} catch(Exception e) {
				tempDeck = "";
			}
			
			if(!tempDeck.equals("")) {
				try { 
				deckChooser.getSelectionModel().select(tempDeck);
				} catch(Exception e) {
					
				}
			}
			s.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		window.setScene(mainMenuScene);
		window.show();
	}		
}