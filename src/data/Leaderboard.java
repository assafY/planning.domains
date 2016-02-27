package data;

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

        sortLeaderboard();
    }

    public LinkedHashMap<Planner, Double> getSortedLeaderboard() {
        return sortedLeaderboard;
    }

    public void addProblemResults(HashMap<Planner, Double> problemResultsMap) {
        if (problemResultsMap != null && problemResultsMap.size() > 0) {
            Iterator iter = problemResultsMap.entrySet().iterator();
            Map.Entry currentResult = null;
            while (iter.hasNext()) {
                currentResult = (Map.Entry) iter.next();
                increaseResultBy((Planner) currentResult.getKey(), (Double) currentResult.getValue());
            }
        }
    }

    private void increaseResultBy(Planner planner, double result) {
        Double previousResult = leaderboardMap.get(planner);
        if (previousResult != null) {
            leaderboardMap.put(planner, result + previousResult);
        } else {
            leaderboardMap.put(planner, result);
        }
    }

    public void sortLeaderboard() {
        if (leaderboardMap != null) {
            ArrayList<Map.Entry<Planner, Double>> sortedList = new ArrayList<>(leaderboardMap.entrySet());
            Collections.sort(sortedList, new Comparator<Map.Entry<Planner, Double>>() {
                public int compare(Map.Entry<Planner, Double> planner1,
                                   Map.Entry<Planner, Double> planner2) {
                    return planner2.getValue().compareTo(planner1.getValue());
                }
            });

            sortedLeaderboard = new LinkedHashMap<>();
            Iterator iter = sortedList.iterator();
            while (iter.hasNext()) {
                Map.Entry<Planner, Double> currentPlanner = (Map.Entry<Planner, Double>) iter.next();
                sortedLeaderboard.put(currentPlanner.getKey(), currentPlanner.getValue());
            }
        }
    }
}
