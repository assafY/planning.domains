package global;

import server.Domain;
import server.Planner;
import server.XmlDomain;

import java.io.Serializable;

public class Message implements Serializable {

    // message types
    public static final int CLIENT_CONNECTED = 1,
            //CLIENT_DISCONNECTED = 2,
            DUPLICATE_THREAD = 2,
            RUN_PLANNER = 3,
            PLAN_RESULT = 4;

    private int type;
    private String message;
    private Domain domain;
    private XmlDomain.Domain.Problems.Problem problem;
    private Planner planner;

    public Message(int type) {
        this.type = type;
    }

    public Message(String message, int type) {
        this.type = type;
        this.message = message;
    }

    public Message(String domainPath, XmlDomain.Domain.Problems.Problem problem, Planner planner, int type) {
        this.type = type;
        this.message = domainPath;
        this.problem = problem;
        this.planner = planner;
    }

    public int getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public XmlDomain.Domain.Problems.Problem getProblem() {
        return problem;
    }

    public Domain getDomain() {
        return domain;
    }

    public Planner getPlanner() {
        return planner;
    }
}
