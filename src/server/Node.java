package server;

/**
 * Class for every single node in the cluster.
 */
public class Node {

    private String name;
    private boolean connected = false;

    public Node(String name) {
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
