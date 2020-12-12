import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Question {
	private static ArrayList<FlashCard> deck,
										usableDeck;
	
	private static String answerKey = new String();
	
	private ArrayList<FlashCard> choices = new ArrayList<>();
	private FlashCard correct;
	
	private VBox body = new VBox();
	
	private ImageView imageView = new ImageView();

	private Separator separator = new Separator(Orientation.HORIZONTAL);
	
	private ToggleGroup radioGroup;
	
	private RadioButton correctButton;
	
	Question(){
		int rand = new Random().nextInt(usableDeck.size());
		
		this.correct = usableDeck.get(rand);
		
		usableDeck.remove(rand);
		
		this.choices.add(this.correct);
		
		while(choices.size() < 4) {
			rand = new Random().nextInt(((deck.size() - 1)) + 1);
			
			if(!this.choices.contains(deck.get(rand))) {
				this.choices.add(deck.get(rand));
			}
		}
			
		Collections.shuffle(this.choices);
		
		VBox vbox;
		
		RadioButton answerA = new RadioButton("A. " + this.choices.get(0).getBackInfo());
		RadioButton answerB = new RadioButton("B. " + this.choices.get(1).getBackInfo());
		RadioButton answerC = new RadioButton("C. " + this.choices.get(2).getBackInfo());
		RadioButton answerD = new RadioButton("D. " + this.choices.get(3).getBackInfo());
		
		int correctChoice = this.choices.indexOf(this.correct);
		
		switch(correctChoice) {
			case 0:
				answerKey = answerKey.concat("A");
				correctButton = answerA;
			break;
			
			case 1:
				answerKey = answerKey.concat("B");
				correctButton = answerB;
			break;
			
			case 2:
				answerKey = answerKey.concat("C");
				correctButton = answerC;
			break;
			
			default:
				answerKey = answerKey.concat("D");
				correctButton = answerD;
		}
				
		this.radioGroup = new ToggleGroup();
		
		answerA.setToggleGroup(radioGroup);
		answerB.setToggleGroup(radioGroup);
		answerC.setToggleGroup(radioGroup);
		answerD.setToggleGroup(radioGroup);

		answerA.getStyleClass().add("questionOption");
		answerB.getStyleClass().add("questionOption");
		answerC.getStyleClass().add("questionOption");
		answerD.getStyleClass().add("questionOption");
		
		Label questionLabel = new Label(correct.toString());
		questionLabel.setStyle("-fx-font-size:18px");
		questionLabel.setWrapText(true);
		
		vbox = new VBox(10, questionLabel,
				answerA, answerB, answerC, answerD);
		
		vbox.setPrefWidth(400);
		
		try {
			imageView.setImage(new Image(correct.getImgUrl()));
			
		} catch(Exception e) {
			imageView.setImage(new Image("file:assets/noimg.png"));

		}
		
		imageView.setFitHeight(100);
		imageView.setPreserveRatio(true);
		
		separator.getStyleClass().add("q-separator");
		
		body = new VBox(10, new HBox(10, vbox, imageView), separator);
		
		body.setPadding(new Insets(5,5,0,5));
	}

	
	/**
	 * Sets the question bank deck
	 * @param d
	 */
	@SuppressWarnings("unchecked")
	
	public static void setDeck(ArrayList<FlashCard> d) {
		deck = (ArrayList<FlashCard>) d.clone();
		usableDeck = (ArrayList<FlashCard>) deck.clone();
	}
	
	/**
	 * @return the javafx node corresponding to the question
	 */
	public VBox getBody() {
		return this.body;
	}
	
	/*
	 * @return the selected radiobox in the question
	 */
	public char getSelected() {
		RadioButton selectedButton = (RadioButton) this.radioGroup.getSelectedToggle();
		
		if(selectedButton == null) {
			return 'N';
		} else {
			return selectedButton.getText().charAt(0);
		}
	}
	
	/**
	 * @return the current answer key
	 */
	public static String getAnswerKey() {
		return answerKey;
	}
	
	/**
	 * Highlights the correct answer
	 */
	public  void highlightCorrect() {
		this.correctButton.setStyle("-fx-background-color: black; -fx-text-fill:white;");
	}
	
	/**
	 * Resets the answer key
	 */
	public static void resetAnswerKey() {
		answerKey = new String();
	}
	
	
	/**
	 * Returns an HTML string to help build the web page quiz
	 * @param num 
	 * @return
	 */
	public String getHtml(int num) {
		int rand = new Random().nextInt((choices.size()));

		StringBuilder boxString = new StringBuilder();

		boxString.append("<div id = 'answerDiv'><h2><u>" +  num + ". " + this.correct.toString() + "</u></h2>");

		while(this.choices.size() < 4) {
			rand = new Random().nextInt(deck.size());
			
			if(!this.choices.contains(deck.get(rand))) {
				this.choices.add(deck.get(rand));				
			}
		}
			
		for(int i = 0; i < 4; i++) {
			switch(i) {
				case 0:	boxString.append("<b>A. </b>"); break;
				case 1: boxString.append("<b>B. </b>"); break;
				case 2: boxString.append("<b>C. </b>"); break;
				case 3: boxString.append("<b>D. </b>"); break;
			}
			boxString.append(this.choices.get(i).getBackInfo() + " <br /><br />");
		}
		

		boxString.append("</div><div id = 'imgDiv'> <img src ='" +  this.correct.getImgUrl() +"'/></div>");
		
		return boxString.toString();
	}
	
	
	/**
	 * @return a formatted answer key for the HTML quiz
	 */
	public static String getHtmlAnswerKey() {
		StringBuilder htmlKey = new StringBuilder();
		
		for(int i = 0; i < answerKey.length(); i++) {
			htmlKey.append((i+1) + ". " + answerKey.charAt(i) + " ");
		}
		
		return htmlKey.toString();
	}
}