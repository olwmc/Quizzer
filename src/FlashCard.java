import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class FlashCard {
	private String front,
		   back,
		   image;
	
	private static String deckName = null;
	
	private static String deckInfo = null;
	
	private static ArrayList<FlashCard> deck = new ArrayList<>();
	
	private static int flashPosition = 0;
	
	FlashCard(String f, String b, String img){
		this.front = f;
		this.back = b;
		this.image = img;
	}
	
	/** Sets the position in the flash card deck
	 * @param i
	 */
	public static void setPos(int i) {
		if(i == 0) {
			flashPosition = 0;
		
		} else if (i == -2) {
			flashPosition = deck.size()-1;
			
		} else {
			flashPosition+=i;
		}
	}
	
	/**
	 * @return the current position in the flash card deck
	 */
	public static int getPos() {
		return flashPosition;
	}
	
	/**
	 * @return the front of the flash card
	 */
	@Override
	public String toString(){
		return this.front;
	}
	
	/**
	 * @return the back fo the flash card
	 */
	public String getBackInfo() {
		return this.back;
	}
	
	/**
	 * @return the image url associated with the flash card
	 */
	public String getImgUrl() {
		return this.image;
	}
	
	/**
	 * Sets the flash card deck by name which is then read and parsed
	 * @param dn
	 * @throws FileNotFoundException
	 */
	public static void setDeck(String dn) throws FileNotFoundException {
		deck.clear();
		
		deckName = dn;

		File file = new File("decks/" + deckName);
		Scanner fileReader = new Scanner(file);
		
		String f,b,i;
		fileReader.nextLine();
		
		while(fileReader.hasNext()) {
			f = fileReader.nextLine();
			b = fileReader.nextLine();
			i = fileReader.nextLine();
			
			deck.add(new FlashCard(f,b,i));
		}
		
		fileReader.close();
		
	}
	
	/**
	 * Sets the flash card deck by arraylist as opposed to by name
	 * @param list
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static void setDeckByList(ArrayList<FlashCard> list) throws IOException {
		deck = (ArrayList<FlashCard>) list.clone();
		save();
	}
	
	/**
	 * Sets the deck info
	 * @param di
	 */
	public static void setInfo(String di) {
		deckInfo = di;
	}


	/**
	 * @return the current flash card deck name
	 */
	public static String getDeckName() {
		return deckName;
	}

	/**
	 * Saves the current deck
	 * @throws IOException
	 */
	public static void save() throws IOException {
		
		File file = new File("decks/" + deckName);
		
		if(!file.exists()) {
			file.createNewFile();
		}
		
	    FileWriter fileWriter = new FileWriter("decks/" + deckName);
		PrintWriter pw = new PrintWriter(fileWriter, false);
		pw.print("");

		fileWriter.write(deckInfo);
		
		for(FlashCard f : deck) {
			fileWriter.write("\n" + f.toString() + "\n" +
							f.getBackInfo() + "\n" + f.getImgUrl());
		}

		fileWriter.close();
		pw.close();

	}
	
	/**
	 * @return the current deck
	 */
	public static ArrayList<FlashCard> getCards(){
			return deck;
	}
}