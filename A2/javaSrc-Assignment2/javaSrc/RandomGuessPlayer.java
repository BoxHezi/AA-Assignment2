import java.io.*;
import java.util.*;

/**
 * Random guessing player.
 * This player is for task B.
 * <p>
 * You may implement/extend other interfaces or classes, but ensure ultimately
 * that this class implements the Player interface (directly or indirectly).
 */
public class RandomGuessPlayer implements Player {

    /**
     * Loads the game configuration from gameFilename, and also store the chosen
     * person.
     *
     * @param gameFilename Filename of game configuration.
     * @param chosenName Name of the chosen person for this player.
     * @throws IOException If there are IO issues with loading of gameFilename.
     * Note you can handle IOException within the constructor and remove
     * the "throws IOException" method specification, but make sure your
     * implementation exits gracefully if an IOException is thrown.
     */


    private String chosenName;

    /*
     * feature is a hash map that stores the all the features being currently used by
     * the people in the game.
     */
    private Map<String, String> feature = new HashMap<>();
    /*
     * chosenPlayer is a hash map that stores all the features the chosen
     * player has in the game.
     */
    private Map<String, String> chosenPlayer = new HashMap<>();
    /*
     * This hash map will contian all the people in the config file
     */
    private Map<String, String> person = new HashMap<>();
    /*
     * This will contian the values of the opponent that we are trying to guess
     */
    private Map<String, String> opponent = new HashMap<>();


    public RandomGuessPlayer(String gameFilename, String chosenName)
            throws IOException {
        File file = new File(gameFilename);
        Scanner reader = new Scanner(file);
        Scanner ppl = new Scanner(file);
        this.chosenName = chosenName;
        readChosen(reader, chosenName);
        readFeatures(ppl);
    } // end of RandomGuessPlayer()

    /**
     * reads the config file for the chosen player.
     * The method reads through the file and compares it to the chosenName
     * if it matches then it is inserted into the chosenPlayer hash map
     *
     * @param reader     ,the file reader
     * @param chosenName ,the player chosen
     */
    private void readChosen(Scanner reader, String chosenName) {
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            if (line.equals(chosenName)) {
                line = reader.nextLine();
                while (line.length() > 1) {
                    String words[] = line.split("\\s");
                    chosenPlayer.put(words[0], words[1]);
                    line = reader.nextLine();
                }
            }
        }
    }

    /**
     * reads in the features from the config file and then inserts them to the person hash map
     * to avoid over writing the keys, i have attached the char 'P(int)' to the key, which is later
     * split when the program attempts to guess the opponent. While doing this, features that are
     * read in are also inserted to the feature hashmap without the char 'P(int)'.
     *
     * @param ppl  ,the file reader
     */
    private void readFeatures(Scanner ppl) {
        while (ppl.hasNextLine()) {
            String line = ppl.nextLine();
            if (line.length() > 1) {
                if (line.charAt(0) == 'P' && Character.isDigit(line.charAt(1))) {
                    String person = line;
                    line = ppl.nextLine();
                    while (line.length() > 1) {
                        String words[] = line.split("\\s");
                        String key = person + "," + words[0];
                        this.person.put(key, words[1]);
                        boolean append = false;
                        /*
                         * Here the value is appened to the current key value of the feature if they have the same
                         * matching key, if not then a new key is added to the hashmap. They are added with
                         * a speace between each attribute so then it can split it later on in the program
                         */
                        for (String p : feature.keySet()) {
                            if (words[0].equals(p) && !feature.get(p).contains(words[1])) {
                                append = true;
                                break;
                            }
                        }
                        if (append) {
                            feature.put(words[0], feature.get(words[0]) + " " + words[1]);
                        } else {
                            feature.put(words[0], words[1]);
                        }
                        if (ppl.hasNextLine()) {
                            line = ppl.nextLine();
                        } else {
                            break;
                        }
                    }
                }
            }
        }
    }

    public Guess guess() {
        // placeholder, replace
        Random generateRand = new Random();
        if (person.size() <= 2 && opponent.size() + 2 != feature.size()) {
            return guessPerson(generateRand);
        } else {
            if (opponent.size() != feature.size()) {
                return guessFeature(generateRand);
            }
            /*Guessing person*/
            return guessPerson(generateRand);
        }
    } // end of guess()

    /**
     * Uses the random generator to randomly guess a feature that hasn't been identified
     * about the opponent. It first gets the hashmap as an object array which the feature
     * is randomly selected. If the feature already has an attribute, it then keeps
     * randomly generating until the opponent doesn't have an attribute for that feature
     * (or just the feature in general).
     */
    private Guess guessFeature(Random generateRand) {
        Object[] attribK = feature.keySet().toArray();
        int index = generateRand.nextInt(attribK.length);
        Object attribute = (String) attribK[index];
        while (opponent.get(attribute) != null) {
            index = generateRand.nextInt(attribK.length);
            attribute = (String) attribK[index];
        }
        /*
         * splits the string by the spaces inbetween each of the attributes so then the
         * program will guess one of these attributes for the given feature.
         */
        String temp = (String) feature.get(attribute);
        String characteristic[] = temp.split("\\s");
        int randomInt = generateRand.nextInt(characteristic.length);
        /*
      Private attributes used to keep track of the chosen player and the most recent
      attribute that's been passed as a guess. These variables would assist in the
      recieveAnswer method where it will remove unwanted players.
     */ /*
         * Private attributes used to keep track of the chosen player and the most recent
         * attribute that's been passed as a guess. These variables would assist in the
         * recieveAnswer method where it will remove unwanted players.
         */String gattribute = (String) attribute;
        return new Guess(Guess.GuessType.Attribute, (String) attribute, characteristic[randomInt]);
    }

    /**
     * Based on the random generator, this method will randomly guess the person the
     * opponent is likely to be based off the questions asked about it's feature.
     * By the time the program has reached this stage there should only be one player left.
     * which would be the opponenet's chosen person.
     */
    private Guess guessPerson(Random generateRand) {

        /*if there are only two people left, then a person*/
        Object[] pplK = person.keySet().toArray();
        if (pplK.length == 0) {
            System.out.println("Player does not exist");
        }
        int index = generateRand.nextInt(pplK.length);
        Object selector = (String) pplK[index];
        String container = (String) selector;
        String[] word = container.split(",");
        return new Guess(Guess.GuessType.Person, "", word[0]);
    }

    public boolean answer(Guess currGuess) {
        /*
         * returns either true or false if the chosenPlayer of this player
         contains the features/attributes specifed by this guess
         */
        if (chosenName.equals(currGuess.getValue())) {
            return true;
        }
        if (currGuess.getAttribute() != null) {
            for (String s : chosenPlayer.keySet()) {
                if (s.equals(currGuess.getAttribute())) {
                    if (chosenPlayer.get(s).equals(currGuess.getValue())) {
                        return true;
                    }
                }
            }
        }
        return false;
    } // end of answer()

    /**
     * if this was not a person guess return false which would continue the game
     * if this was a person guess and they got it right return true which would end
     * the game
     */

    public boolean receiveAnswer(Guess currGuess, boolean answer) {
        if (!currGuess.getAttribute().equals("")) {
            if (answer) {
                /*guess was right*/
                return correctGuess(currGuess);
            } else {
                /*guesss was wrong*/
                return incorrectGuess(currGuess);
            }
        }
        /*if for some reason it guesses wrong delete the person that doesn't fit in*/
        if (currGuess.getAttribute().equals("") && !answer) {
            person.remove(currGuess.getValue(), person.get(currGuess.getValue()));
            return false;
        }
        return true;
    } // end of receiveAnswer()

    /**
     * Adds the feature/attribute that was returned as true to add to the opponent hashmap.
     * removes players without that specific feature/attribute
     * returns false as we don't know who the opponent is
     */
    private boolean correctGuess(Guess currGuess) {
        /*the guess was correct and is added to the opponent hashMap*/
        opponent.put(currGuess.getAttribute(), currGuess.getValue());
        removePerson(currGuess, "correct");
        return false;
    }

    /**
     * Removes the players depending on the condition of the type
     * if type is "correct" then it will get rid of the people in the person hashmap
     * which don't have that specified feature/attribute.
     * else if the type is "incorrect" the method would get rid of the people
     * in the person hashmap who do have that specified feature/attribute
     * hence leaving only the people the opponent is likely to be left in the hashmap
     */
    private void removePerson(Guess currGuess, String type) {
        String[] contain = new String[person.size()];
        int p = 0;
        for (String character : person.keySet()) {
            String word[] = character.split(",");
            if (word[1].equals(currGuess.getAttribute())) {
                if (!person.get(character).equals(currGuess.getValue()) && type.equals("correct")) {
                    contain[p] = character;
                    p++;
                } else if (person.get(character).equals(currGuess.getValue()) && type.equals("incorrect")) {
                    contain[p] = character;
                    p++;
                }
            }
        }
        Object[] feature = this.feature.keySet().toArray();
        for (int i = 0; i < p; i++) {
            for (int k = 0; k < this.feature.size(); k++) {
                String[] str = contain[i].split(",");
                person.remove(str[0] + "," + feature[k]);
            }
        }
    }

    /**
     * if the guess was incorrect then this method removes the incorrect attribute from
     * feature and then rearranges the hashmap so then only new guesses are being made.
     */
    private boolean incorrectGuess(Guess currGuess) {
        String words = feature.get(currGuess.getAttribute());
        String[] list = words.split("\\s");
        StringBuilder newString = new StringBuilder();
        for (String aList : list) {
            if (aList.equals(currGuess.getValue())) {
                for (int j = 0; j < list.length - 1; j++) {
                    if (!list[j].equals(currGuess.getValue())) {
                        newString.append(list[j]).append(" ");
                    }
                }
                feature.put(currGuess.getAttribute(), newString.toString());
                /*
                 * gets rid of people with the current attribute type
                 */
                removePerson(currGuess, "incorrect");
                break;
            }
        }
        /*
         * if there is only one choice left then this must be what the opponent has
         */
        String[] word = newString.toString().split("\\s");
        if (word.length == 1) {
            opponent.put(currGuess.getAttribute(), newString.toString());
        }
        return false;
    }
} // end of class RandomGuessPlayer
