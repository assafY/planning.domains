package server;

/**
 * Class for every single cluster in the distributed system.
 */
public class Cluster {

    private String name;
    private boolean connected = false;

    public Cluster(String name) {
        this.name = name;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public String getName() {
        return name;
    }
}
