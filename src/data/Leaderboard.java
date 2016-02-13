package data;

import com.sun.tools.jdi.DoubleValueImpl;
import global.Global;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Leaderboard {

    private HashMap<Planner, Double> leaderboardMap;
    private LinkedHashMap<Planner, Double> sortedLeaderboard;

    public Leaderboard() {
        leaderboardMap = new HashMap<>();
        sortedLeaderboard = new LinkedHashMap<>();
    }

    /**
     * Iterates over all problems in all domains known by the server
     * and stores a hashmap of all known planners and their total
     * score over all problems.
     *
     * @param domainList - list of all domains known by server
     */
    public void setLeaderboard(ArrayList<Domain> domainList) {
        for (Domain currentDomain: domainList) {
            for (XmlDomain.Domain.Problems.Problem currentProblem:
                    currentDomain.getXmlDomain().getDomain().getProblems().getProblem()) {
                addProblemResults(Global.getProblemLeaderboard(currentProblem.getResultMap()));
            }
        }

        // sort leaderboard and store copy
        sortedLeaderboard = sortLeaderboard();
    }

    public LinkedHashMap<Planner, Double> getSortedLeaderboard() {
        return sortedLeaderboard;
    }

    private void addProblemResults(HashMap<Planner, Double> problemResultsMap) {
        Iterator iter = problemResultsMap.entrySet().iterator();
        Map.Entry currentResult = null;
        while (iter.hasNext()) {
            currentResult = (Map.Entry) iter.next();
            increaseResultBy((Planner) currentResult.getKey(), (Double) currentResult.getValue());
        }
    }

    private void increaseResultBy(Planner planner, double result) {
        double previousResult = leaderboardMap.get(planner);
        leaderboardMap.put(planner, result + previousResult);
    }

    private LinkedHashMap<Planner, Double> sortLeaderboard() {
        ArrayList<Map.Entry<Planner, Double>> sortedList = new ArrayList<>(leaderboardMap.entrySet());
        Collections.sort(sortedList, new Comparator<Map.Entry<Planner, Double>>() {
            public int compare(Map.Entry<Planner, Double> planner1,
                               Map.Entry<Planner, Double> planner2) {
                return planner1.getValue().compareTo(planner2.getValue());
            }
        });

        LinkedHashMap<Planner, Double> sortedMap = new LinkedHashMap<>();
        Iterator iter = sortedList.iterator();
        while (iter.hasNext()) {
            Map.Entry<Planner, Double> currentPlanner = (Map.Entry<Planner, Double>) iter.next();
            sortedMap.put(currentPlanner.getKey(), currentPlanner.getValue());
        }

        return sortedMap;
    }
}
