package data;

public class Result {

    private Planner planner;
    private double result;

    public Result(Planner planner, double result) {
        this.planner = planner;
        this.result = result;
    }

    public Planner getPlanner() {
        return planner;
    }

    public double getResult() {
        return result;
    }
}
