package server;

import java.util.ArrayList;

public class Job {

    private Planner planner;
    private ArrayList<XmlDomain.Domain.Problems.Problem> problemList;
    private Domain domain;

    public Job (Planner planner, ArrayList<XmlDomain.Domain.Problems.Problem> problemList,
                    Domain domain) {
        this.planner = planner;
        this.problemList = problemList;
        this.domain = domain;
    }
}
