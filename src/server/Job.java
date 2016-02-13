package server;

import data.Domain;
import data.Planner;
import data.XmlDomain;

import java.io.Serializable;

public class Job implements Comparable<Job>, Serializable {

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

    public Job (Job job, int priority) {
        this.planner = job.getPlanner();
        this.problem = job.getProblem();
        this.domain = job.getDomain();
        this.priority = priority;
    }

    public XmlDomain.Domain.Problems.Problem getProblem() {
        return problem;
    }

    public Domain getDomain() {
        return domain;
    }

    public String getDomainPath() {
        return domain.getPath() + "/";
    }

    public Planner getPlanner() {
        return planner;
    }

    public String getDomainId() {
        return domain.getXmlDomain().getDomain().getShortId();
        //String domainId = domain.getXmlDomain().getDomain().getId();
        //return domainId.substring(domainId.indexOf(':') + 1).replaceAll("/", "-");
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(Job j) {
        return this.getPriority() - j.getPriority();
    }

    @Override
    public String toString() {
        return planner.getName() + " on " + getDomainId() + ", problem file " + problem;
    }
}
