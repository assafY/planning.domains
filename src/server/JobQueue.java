package server;

import java.util.concurrent.PriorityBlockingQueue;

public class JobQueue extends PriorityBlockingQueue<Job> {

    private JobCreatedListener listener;

    @Override
    public void put(Job newJob) {
        super.put(newJob);
        if (listener != null) {
            listener.onJobCreated(newJob);
        }
    }

    public void addJobCreatedListener(JobCreatedListener listener) {
        this.listener = listener;
    }
}
