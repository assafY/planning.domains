package data;

public class Result {

    private Planner planner;
    private int result;

    public Result(Planner planner, int result) {
        this.planner = planner;
        this.result = result;
    }

    public Planner getPlanner() {
        return planner;
    }

    public int getResult() {
        return result;
    }
}
