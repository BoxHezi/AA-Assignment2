import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Binary-search based guessing player.
 * This player is for task C.
 * <p>
 * You may implement/extend other interfaces or classes, but ensure ultimately
 * that this class implements the Player interface (directly or indirectly).
 */
public class BinaryGuessPlayer implements Player {
    //list to store all feature that one candidate might have
    List<String> featureList = new ArrayList<>();
    //count how time per feature appears
    Map<String, Map<String, Integer>> featureCount = new HashMap<>();
    //store each person's feature value
    Map<String, Map<String, String>> candidateMap = new HashMap<>();

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

        StringBuilder featureValue = new StringBuilder();
        while (gameFileReader.hasNextLine()) {
            String configLine = gameFileReader.nextLine();
            if (configLine.matches("[P]\\d+")) {
                if (featureList.size() <= 0) {
                    getFeatureType(String.valueOf(featureValue));
                }

                //get each person's attribute and store in a hash map
                HashMap<String, String> candidateInnerMap = new HashMap<>();
                do {
                    String candidateFeature;
                    if (gameFileReader.hasNextLine()) {
                        candidateFeature = gameFileReader.nextLine();
                    } else {
                        break;
                    }
                    if (candidateFeature.matches("")) {
                        candidateMap.put(configLine, candidateInnerMap);
                        System.out.println("--------");
                        break;
                    }
                    String[] candidateFeatureAndValue = candidateFeature.split("\\s");
                    candidateInnerMap.put(candidateFeatureAndValue[0], candidateFeatureAndValue[1]);

                } while (true);
                candidateMap.put(configLine, new HashMap<>());
            } else {
                String[] attributeFeature = configLine.split("\\s");
                String attributeValue = attributeFeature[0] + " ";
                featureValue.append(attributeValue);
            }
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

    private void calculateFeatureCount() {

    }

    private void getFeatureType(String allFeature) {
        String[] featureArray = allFeature.split("\\s");
        featureList.addAll(Arrays.asList(featureArray));
    }

} // end of class BinaryGuessPlayer