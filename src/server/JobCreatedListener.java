package server;

/**
 * Triggered when a new job is added to the job queue
 */
public interface JobCreatedListener {

    public void onJobCreated(Job newJob);
}
