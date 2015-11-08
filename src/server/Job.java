package server;

public class Job implements Comparable<Job> {

    private static final int DEFAULT_PRIORITY = 1;

    private Planner planner;
    private XmlDomain.Domain.Problems.Problem problem;
    private Domain domain;
    private int priority;

    public Job (Planner planner, XmlDomain.Domain.Problems.Problem problem,
                    Domain domain) {
        this.planner = planner;
        this.problem = problem;
        this.domain = domain;
        this.priority = DEFAULT_PRIORITY;
    }

    public Job (Planner planner, XmlDomain.Domain.Problems.Problem problem,
                Domain domain, int priority) {
        this.planner = planner;
        this.problem = problem;
        this.domain = domain;
        this.priority = priority;
    }

    public Job (Job job, int priority) {
        this.planner = job.getPlanner();
        this. problem = job.getProblem();
        this.domain = job.getDomain();
        this.priority = priority;
    }

    public Planner getPlanner() {
        return planner;
    }

    public XmlDomain.Domain.Problems.Problem getProblem() {
        return problem;
    }

    public Domain getDomain() {
        return domain;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(Job j) {
        return this.getPriority() - ((Job) j).getPriority();
    }
}