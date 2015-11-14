package global;

import server.Job;

import java.io.Serializable;

public class Message implements Serializable {

    // message types
    public static final int CLIENT_CONNECTED = 1,
            //CLIENT_DISCONNECTED = 2,
            DUPLICATE_THREAD = 2,
            RUN_JOB = 3,
            JOB_INTERRUPTED = 4,
            JOB_REQUEST = 5,
            INCOMPATIBLE_PLANNER = 6,
            PLAN_NOT_FOUND = 7;

    private int type;
    private String message;
    private Job job;


    public Message(int type) {
        this.type = type;
    }

    public Message(String message, int type) {
        this.type = type;
        this.message = message;
    }

    public Message(Job job, int type) {
        this.type = type;
        this.job = job;
    }

    public int getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public Job getJob() {
        return job;
    }

}
