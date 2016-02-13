package data;

import global.Global;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;

public class Leaderboard {

    private HashMap<Planner, Double> leaderboardMap;

    public Leaderboard() {
        leaderboardMap = new HashMap<>();
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

    public void setLeaderboard(ArrayList<Domain> domainList) {
        for (Domain currentDomain: domainList) {
            for (XmlDomain.Domain.Problems.Problem currentProblem:
                    currentDomain.getXmlDomain().getDomain().getProblems().getProblem()) {
                addProblemResults(Global.getProblemLeaderboard(currentProblem.getResultMap()));
            }
        }
    }
}
