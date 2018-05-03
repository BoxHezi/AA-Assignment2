import java.io.*;
import java.util.*;

/**
 * Random guessing player.
 * This player is for task B.
 *
 * You may implement/extend other interfaces or classes, but ensure ultimately
 * that this class implements the Player interface (directly or indirectly).
 */
public class RandomGuessPlayer implements Player
{

    /**
     * Loads the game configuration from gameFilename, and also store the chosen
     * person.
     *
     * @param gameFilename Filename of game configuration.
     * @param chosenName Name of the chosen person for this player.
     * @throws IOException If there are IO issues with loading of gameFilename.
     *    Note you can handle IOException within the constructor and remove
     *    the "throws IOException" method specification, but make sure your
     *    implementation exits gracefully if an IOException is thrown.
     */
	 
	/*this will contian the various attributes and features in config*/
	/*Attributes is stored as it's Attribute in one word, and features will be
	stored as a full length string with spaces, i would need to split them soon when guessing*/
    //public Map<String, Map<String, String>> Attributes = new HashMap<String, Map<String, String>>();
    
	/*Currently only using Features*/
	private Map<String, String> Feature = new HashMap<String, String>();
	/*this will contian the chosen player's features*/
    private Map<String, String> ChosenPlayer = new HashMap<String, String>();
	/*This hash map will contian all players in the config*/
	private Map<String, Map<String,String>> Person = new HashMap<String, Map<String,String>>();
	private Map<String, String> element = new HashMap<String, String>();
	/*This will contian the values of the oopenent that we are rying to guess*/
	private Map<String, String> Opponent = new HashMap<String, String>();
	
	
	
	public RandomGuessPlayer(String gameFilename, String chosenName)
        throws IOException
    {
		try {
			File file = new File(gameFilename);
			Scanner reader = new Scanner(System.in);
			reader = new Scanner(file);
			Scanner attReader = new Scanner(System.in);
			attReader = new Scanner(file);
			Scanner ppl = new Scanner(System.in);
			ppl = new Scanner(file);
			this.chosenName = chosenName;
			while (reader.hasNextLine()) {
				String line = reader.nextLine();
				if (line.equals(chosenName)) {
				line = reader.nextLine();
					while(line.length()>1) {
						String words[] = line.split(" ");
						/*System.out.println(words[0]+" "+words[1]);*/
						ChosenPlayer.put(words[0],words[1]);
						line = reader.nextLine();
					}
				}
			}
			while (attReader.hasNextLine()) {
				String line = attReader.nextLine();
				if (line.length()==0) {
					break;
				}
				String words[] = line.split(" ",2);
				/*System.out.println("It works: "+words[0]+" "+words[1]);*/
				Feature.put(words[0], words[1]);
			}
			while (ppl.hasNextLine()) {
				String line = ppl.nextLine();
				int i=0;
				if (line.length()>1) {
					if (line.charAt(0) == 'P' &&  Character.isDigit(line.charAt(1))) {
						String person = line;
						line = ppl.nextLine();
						while(line.length()>1) {	
							String words[] = line.split(" ");
							/*System.out.println("It works: "+words[0]+" "+words[1]);*/
							element.put(words[0],words[1]);
							if (ppl.hasNextLine()) {
								line = ppl.nextLine();
							} else {
								break;
							}
						}
						Person.put(person,element);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/*print out the player's features*/
		/*for (String s : ChosenPlayer.keySet()) {
			System.out.println(s+": "+ChosenPlayer.get(s));
		}*/
		
    } // end of RandomGuessPlayer()
	
    public Guess guess() {
        // placeholder, replace
		/*randomly guess*/
		/*this random generator works but you need a way to store it's choices*/
		Random generateRand = new Random();
		
		/*you would probably need to store how many characteristics there are 
		that are needed to be stored, no you just delete them after you have found it
		and store it somewhere*/
		if (Opponent.size()!=Feature.size()) {
		Object[] attribK = Feature.keySet().toArray();
		int index = generateRand.nextInt(attribK.length);
		Object attribute = (String) attribK[index];

		/*System.out.println(attribute+" "+Feature.get(attribute));*/
		String temp = (String) Feature.get(attribute);
		String characteristic[] = temp.split(" ");
		int randomInt = generateRand.nextInt(characteristic.length);
		while (randomInt >characteristic.length) {
			randomInt = generateRand.nextInt(characteristic.length);
		}
		/*System.out.println(characteristic[randomInt]);*/
		Gval = characteristic[randomInt];
		Gattribute = (String) attribute;
		return new Guess(Guess.GuessType.Attribute,(String) attribute,characteristic[randomInt]);
		}
		/*Guessing person*/
		
		Object[] pplK = Person.keySet().toArray();
		
		int index = generateRand.nextInt(pplK.length);
		Object selector = (String) pplK[index];
		/*System.out.println(selector);*/
		Gval = (String) selector;
        return new Guess(Guess.GuessType.Person,"", (String) selector);
		
		
    } // end of guess()

	private String Gattribute;
	private String Gval;
	private String chosenName;

    public boolean answer(Guess currGuess) {
		/*System.out.println("printing: "+currGuess.toString());*/
		//check if my person has these attributes
		if (chosenName.equals(currGuess.getValue())) {
			return true;
		}
		
		if (currGuess.getAttribute() != null) {
			for (String s : ChosenPlayer.keySet()) {
				if (s.equals(currGuess.getAttribute())) {
					if (ChosenPlayer.get(s).equals(currGuess.getValue())) {
					/*you are just answering the question here no need to do anything*/
						return true;
					}
				}
			}
		}
		
        return false;
    } // end of answer()


	public boolean receiveAnswer(Guess currGuess, boolean answer) {
		//if this was not a person guess return false which woudl continue the game
		//if this was a person guess and they got it right return true which would end the game
        
		/*implement later*/
		/*if (!currGuess.getAttribute().equals("")) {
			return false;
		}*/
		if (!currGuess.getAttribute().equals("")) {
			/*the guess was correct and is added to the opponent hashMap*/
			if (answer == true) {
				Opponent.put(currGuess.getAttribute(),currGuess.getValue());
				for (String ele : element.keySet()) {
					for (String character : Person.keySet()) {
						if (ele.equals(currGuess.getAttribute())) {
							if (!Person.get(character).get(ele).equals(currGuess.getValue())) {
								//find a way to remove invalid players
							}
						}
						//System.out.println(character+" "+ele+" : "+Person.get(character).get(ele));
					}
				}
				return false;
			}
			String words = (String) Feature.get(currGuess.getAttribute());
			String[] list = words.split(" ");
			if (list.length==1) {
				Opponent.put(currGuess.getAttribute(),words);
			}
			for (int i =0 ;i <list.length ; i++) {
				if (list[i].equals(currGuess.getValue())) {
					String newString="";
					for (int j =0; j <list.length-1;j++) {
						if (!list[j].equals(currGuess.getValue())) {
							newString += list[j]+" ";
						}
					}
					Feature.put(currGuess.getAttribute(),newString);
					/*System.out.println("modified: "+newString+" shouldn't have "+currGuess.getValue());
					for (String p : Feature.keySet()) {
					System.out.println("debugger: "+p+": "+Feature.get(p));
					}*/
					break;
				}
			}
			return false;
		}
		
		if (currGuess.getAttribute().equals("") && answer == false) {
			/*delete the character that doesn't fit in*/
			Person.remove(currGuess.getValue(),Person.get(currGuess.getValue()));
			/*for (String s : ChosenPlayer.keySet()) {
				System.out.println(s+": "+ChosenPlayer.get(s));
			}*/
			return false;
		}
		return true;
    } // end of receiveAnswer()

} // end of class RandomGuessPlayer

