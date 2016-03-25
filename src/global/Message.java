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
            INCOMPATIBLE_DOMAIN = 6,
            PLAN_NOT_FOUND = 7,
            PLAN_FOUND = 8,
            RUN_FINISHED = 9,
            FILES_COPIED = 10,
            PROCESS_RESULTS = 11;

    private int type;
    private String message;
    private String input;
    private Job job;
    private Exception exception;


    public Message(int type) {
        this.type = type;
    }

    public Message(String message, int type) {
        this.type = type;
        this.message = message;
    }

    public Message(String message, String input, int type) {
        this.type = type;
        this.message = message;
        this.input = input;
    }

    public Message(Job job, int type) {
        this.type = type;
        this.job = job;
    }

    public Message(Exception e, int type) {
        this.type = type;
        exception = e;
    }

    public int getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getInput() {
        return input;
    }

    public Job getJob() {
        return job;
    }

    public Exception getException() {
        return exception;
    }

}
