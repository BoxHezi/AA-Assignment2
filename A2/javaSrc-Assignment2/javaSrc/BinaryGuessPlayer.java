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
    //count how much time per feature appears
    private Map<String, Map<String, Integer>> featureCountMap = new HashMap<>();
    //store each person's feature value
    private Map<String, Map<String, String>> candidateMap = new HashMap<>();
    //store all possible answer due to previous answer
    private Map<String, Map<String, String>> filterMap;

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
                readCandidate(gameFileReader, configLine);
            } else {
                readFeature(featureValue, configLine);
            }
        }

        filterMap = new HashMap<>(candidateMap);
        calculateFeatureCount();
    } // end of BinaryGuessPlayer()

    /**
     * method to read attribute feature
     *
     * @param featureValue StringBuilder contains both feature and value
     * @param configLine   current reading line of the file
     */
    private void readFeature(StringBuilder featureValue, String configLine) {
        String[] attributeFeature = configLine.split("\\s");
        String attributeValue = attributeFeature[0] + " ";
        featureValue.append(attributeValue);
    }

    /**
     * method to read each candidate
     *
     * @param gameFileReader Scanner to read every line for each person
     * @param configLine     indicate which person is scanning at the monment
     */
    private void readCandidate(Scanner gameFileReader, String configLine) {
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
    }

    public Guess guess() {
        //when only one candidate left, guess the person
        if (filterMap.size() == 1) {
            String person = "";
            for (Map.Entry<String, Map<String, String>> entry : filterMap.entrySet()) {
                person = entry.getKey();
            }
            return new Guess(Guess.GuessType.Person, "", person);
        }
        // placeholder, replace

        int halfNumber = filterMap.size() / 2;
        List<String> toGuess = getGuessAttributeAndValue(halfNumber);

        return new Guess(Guess.GuessType.Attribute, toGuess.get(0), toGuess.get(1));
    } // end of guess()


    public boolean answer(Guess currGuess) {
        if (currGuess.getType() == Guess.GuessType.Person && chosenName.equals(currGuess.getValue())) {
            return true;
        }

        if (null != currGuess.getAttribute()) {
            Map<String, String> tempCandidateMap = candidateMap.get(chosenName);
            return tempCandidateMap.get(currGuess.getAttribute()).equals(currGuess.getValue());
        }

        // placeholder, replace
        return false;
    } // end of answer()


    public boolean receiveAnswer(Guess currGuess, boolean answer) {
        shrinkFilterMap(currGuess, answer);
        return currGuess.getType() == Guess.GuessType.Person && answer;
    } // end of receiveAnswer()

    /**
     * calculate feature appearance
     */
    private void calculateFeatureCount() {
        for (String feature : features) {
            featureCountMap.put(feature, new HashMap<>());
        }
        for (String candidateKey : filterMap.keySet()) {
            Map<String, String> tempCandidateMap = filterMap.get(candidateKey);
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

    /**
     * shrink the possible candidate
     *
     * @param currGuess current guess
     * @param answer    true or false to indicate the guess attribute value in one round
     */
    private void shrinkFilterMap(Guess currGuess, boolean answer) {
        List<String> candidateList = new ArrayList<>();
        for (Object obj : filterMap.entrySet()) {
            Map.Entry mapEntry = (Map.Entry) obj;
            candidateList.add((String) mapEntry.getKey());
        }
        for (String currKey : candidateList) {
            Map<String, String> tempMap = filterMap.get(currKey);
            for (Object obj : tempMap.entrySet()) {
                Map.Entry entry = (Map.Entry) obj;
                if (answer) {
                    if (entry.getKey().equals(currGuess.getAttribute())) {
                        if (!entry.getValue().equals(currGuess.getValue())) {
                            filterMap.remove(currKey);
                        }
                    }
                } else {
                    if (entry.getKey().equals(currGuess.getAttribute())) {
                        if (entry.getValue().equals(currGuess.getValue())) {
                            filterMap.remove(currKey);
                        }
                    }
                }
            }
        }
        calculateFeatureCount();
    }

    /**
     * get best guess attribute and value
     *
     * @param halfCount half filter indicator
     * @return list contains attribute and value to guess
     */
    private List<String> getGuessAttributeAndValue(int halfCount) {
        List<String> attributeValue = new ArrayList<>();
        List<String> mapKey = new ArrayList<>();
        for (Object obj : featureCountMap.entrySet()) {
            Map.Entry entry = (Map.Entry) obj;
            mapKey.add((String) entry.getKey());
        }
        for (String currKey : mapKey) {
            Map<String, Integer> tempMap = featureCountMap.get(currKey);
            for (Object obj : tempMap.entrySet()) {
                Map.Entry entry = (Map.Entry) obj;
                int count = (int) entry.getValue();
                if (count == halfCount) {
                    attributeValue.add(currKey);
                    attributeValue.add((String) entry.getKey());
                    return attributeValue;
                }
            }
        }
        return attributeValue;
    }

} // end of class BinaryGuessPlayer
