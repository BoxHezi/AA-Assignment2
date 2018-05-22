import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Binary-search based guessing player.
 * This player is for task C.
 * <p>
 * You may implement/extend other interfaces or classes, but ensure ultimately
 * that this class implements the Player interface (directly or indirectly).
 */
public class BinaryGuessPlayer implements Player {
    private static final String BINARY_LOG_FILE = "binarylog.txt";

    Map<String, String> featureMap = new HashMap<>();

    Map<String, String> personInfo = new HashMap<>();

    /**
     * Loads the game configuration from gameFilename, and also store the chosen
     * person.
     *
     * @param gameFilename Filename of game configuration.
     * @param chosenName   Name of the chosen person for this player.
     * @throws IOException If there are IO issues with loading of gameFilename.
     *                     Note you can handle IOException within the constructor and remove
     *                     the "throws IOException" method specification, but make sure your
     *                     implementation exits gracefully if an IOException is thrown.
     */
    public BinaryGuessPlayer(String gameFilename, String chosenName) throws IOException {
        Scanner gameFileReader = new Scanner(new File(gameFilename));

        while (gameFileReader.hasNextLine()) {
            String configLine = gameFileReader.nextLine();
            String[] attributeFeature = configLine.split("\\s");
            StringBuilder attributeValue = new StringBuilder();
            for (int i = 1; i < attributeFeature.length; i++) {
                attributeValue.append(attributeFeature[i]).append(" ");
            }
            featureMap.put(attributeFeature[0], String.valueOf(attributeValue));
        }


    } // end of BinaryGuessPlayer()


    public Guess guess() {

        // placeholder, replace
        return new Guess(Guess.GuessType.Person, "", "Placeholder");
    } // end of guess()


    public boolean answer(Guess currGuess) {

        // placeholder, replace
        return false;
    } // end of answer()


    public boolean receiveAnswer(Guess currGuess, boolean answer) {

        // placeholder, replace
        return true;
    } // end of receiveAnswer()

} // end of class BinaryGuessPlayer
