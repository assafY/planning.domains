package server;

import java.io.Serializable;

public class Job implements Comparable<Job>, Serializable {

    private static final int DEFAULT_PRIORITY = 1;

    private String plannerPath;
    private XmlDomain.Domain.Problems.Problem problem;
    private String domainPath;
    private String domainId;
    private int priority;

    public Job (Planner planner, XmlDomain.Domain.Problems.Problem problem,
                    Domain domain) {
        this.plannerPath = planner.getPath();
        this.problem = problem;
        this.domainPath = domain.getPath();
        this.domainId = domain.getXmlDomain().getDomain().getId();
        this.priority = DEFAULT_PRIORITY;
    }

    public Job (Planner planner, XmlDomain.Domain.Problems.Problem problem,
                Domain domain, int priority) {
        this.plannerPath = planner.getPath();
        this.problem = problem;
        this.domainPath = domain.getPath();
        this.domainId = domain.getXmlDomain().getDomain().getId();
        this.priority = priority;
    }

    public Job (Job job, int priority) {
        this.plannerPath = job.getPlannerPath();
        this. problem = job.getProblem();
        this.domainPath = job.getDomainPath();
        this.domainId = job.getDomainId();
        this.priority = priority;
    }

    public String getPlannerPath() {
        return plannerPath;
    }

    public XmlDomain.Domain.Problems.Problem getProblem() {
        return problem;
    }

    public String getDomainPath() {
        return domainPath;
    }

    public String getDomainId() {
        return domainId;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(Job j) {
        return this.getPriority() - ((Job) j).getPriority();
    }
}
