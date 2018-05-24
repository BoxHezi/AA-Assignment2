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
    private String chosenName;
    //list to store all feature that one candidate might have
    private List<String> features = new ArrayList<>();
    //store attribute value of the chosen person
    private Map<String, String> chosenFeature;
    //count how much time per feature appears
    private Map<String, Map<String, Integer>> featureCountMap = new HashMap<>();
    //store each person's feature value
    private Map<String, Map<String, String>> candidateMap = new HashMap<>();
    int candidateCount = 0;

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
        this.chosenName = chosenName;
        Scanner gameFileReader = new Scanner(new File(gameFilename));

        StringBuilder featureValue = new StringBuilder();
        while (gameFileReader.hasNextLine()) {
            String configLine = gameFileReader.nextLine();
            if (configLine.matches("[P]\\d+")) {
                if (features.size() <= 0) {
                    storeAttributeType(String.valueOf(featureValue));
                }

                //get each person's attribute and store in a hash map
                HashMap<String, String> candidateInnerMap = new HashMap<>();
                do {
                    String candidateFeature;
                    if (gameFileReader.hasNextLine()) {
                        candidateFeature = gameFileReader.nextLine();
                        String[] candidateFeatureAndValue = candidateFeature.split("\\s");
                        if (!candidateFeature.matches("")) {
                            candidateInnerMap.put(candidateFeatureAndValue[0], candidateFeatureAndValue[1]);
                        }
                    } else {
                        break;
                    }
                    if (candidateFeature.matches("") || !gameFileReader.hasNextLine()) {
                        candidateMap.put(configLine, candidateInnerMap);
                        break;
                    }
                } while (true);
            } else {
                String[] attributeFeature = configLine.split("\\s");
                String attributeValue = attributeFeature[0] + " ";
                featureValue.append(attributeValue);
            }
        }

        candidateCount = candidateMap.size();
        chosenFeature = candidateMap.get(chosenName);

        calculateFeatureCount();
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

    /**
     * count feature appearance
     */
    private void calculateFeatureCount() {
        for (String feature : features) {
            featureCountMap.put(feature, new HashMap<>());
        }
        for (String candidateKey : candidateMap.keySet()) {
            Map<String, String> tempCandidateMap = candidateMap.get(candidateKey);
            for (String attribute : tempCandidateMap.keySet()) {
                String attributeValue = tempCandidateMap.get(attribute);
                int attributeCount = 0;
                if (null != featureCountMap.get(attribute).get(tempCandidateMap.get(attribute))) {
                    attributeCount = featureCountMap.get(attribute).get(tempCandidateMap.get(attribute));
                }
                Map<String, Integer> tempCountMap = featureCountMap.get(attribute);
                tempCountMap.put(attributeValue, ++attributeCount);
                featureCountMap.put(attribute, tempCountMap);
            }
        }
        for (String s : featureCountMap.keySet() ) {
            System.out.println(s + " " + featureCountMap.get(s));
        }
    }

    /**
     * put all features in a list
     *
     * @param features features that need to be stored
     */
    private void storeAttributeType(String features) {
        String[] featureArray = features.split("\\s");
        this.features.addAll(Arrays.asList(featureArray));
    }

} // end of class BinaryGuessPlayer