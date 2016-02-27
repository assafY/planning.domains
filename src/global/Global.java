package global;

import data.Planner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class contains static methods shared by the server
 * and clients.
 */
public class Global {

    /**
     * Reads a process' input stream and returns a string
     * containing the output.
     *
     * @param is the process' input stream
     * @return String containing the output
     * @throws IOException
     */
    public static synchronized String getProcessOutput(InputStream is) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is));
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                stringBuilder.append(currentLine + System.getProperty("line.separator"));
            }
        } finally {
            reader.close();
        }

        return stringBuilder.toString();
    }

    /**
     * Iterates a map of planners and plan results of a single problem
     * and computes the factorized leader board. The planner with the best
     * result gets a score of 1.0, and every other planner gets a result
     * where 0 < result < 1, calculated by bestResult / plannerResult.
     *
     * @param resultMap the map containing planners and corresponding plan value
     * @return the leader board map calculated in the method
     */
    public static synchronized HashMap<Planner, Double> getProblemLeaderboard(HashMap<Planner, Integer> resultMap) {
        if (resultMap != null && resultMap.size() > 0) {
            System.out.println("running problem leaderboard getter");
            HashMap<Planner, Double> leaderBoard = new HashMap<>();
            Iterator iter = resultMap.entrySet().iterator();
            Map.Entry bestResult = (Map.Entry) iter.next();
            System.out.println("Looking at result: " + bestResult.getKey() + ", " + bestResult.getValue());
            while (iter.hasNext()) {
                Map.Entry currentResult = (Map.Entry) iter.next();
                System.out.println("Looking at result: " + currentResult.getKey() + ", " + currentResult.getValue());
                if ((Integer) currentResult.getValue() > 0 &&
                        ((Integer) currentResult.getValue() < (Integer) bestResult.getValue() ||
                                (Integer) bestResult.getValue() == 0)) {
                    bestResult = currentResult;
                }
            }

            // put the best result in the leader board
            if ((Integer) bestResult.getValue() == 0) {
                leaderBoard.put((Planner) bestResult.getKey(), 0.0);
            } else {
                leaderBoard.put((Planner) bestResult.getKey(), 1.0);
            }

            // reset the iterator
            iter = resultMap.entrySet().iterator();

            while (iter.hasNext()) {
                Map.Entry currentResult = (Map.Entry) iter.next();
                if (!currentResult.equals(bestResult)) {
                    if ((Integer) currentResult.getValue() == 0) {
                        leaderBoard.put((Planner) currentResult.getKey(), 0.0);
                    } else {
                        double result = (double) (int) bestResult.getValue() / (double) (int) currentResult.getValue();
                        System.out.println(currentResult.getValue());
                        System.out.println(bestResult.getValue());
                        System.out.println(result);
                        leaderBoard.put((Planner) currentResult.getKey(), result);
                    }
                }
            }

            return leaderBoard;
        }

        return null;

    }
}
