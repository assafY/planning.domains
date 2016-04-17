package data;

public class Result {

    private String plannerName;
    private int result;

    public Result(String plannerName, int result) {
        this.plannerName = plannerName;
        this.result = result;
    }

    public String getPlannerName() {
        return plannerName;
    }

    public int getResult() {
        return result;
    }
}
