package data;

public class PlannerResultPair {

    private Planner planner;
    private double result;

    public PlannerResultPair(Planner planner) {
        this.planner = planner;
        result = 0;
    }

    public void incrementResultBy(double problemResult) {
        result += problemResult;
    }

    public double getResult() {
        return result;
    }

    public Planner getPlanner() {
        return planner;
    }
}
