package server;

/**
 * Class for every single node in the cluster.
 */
public class Node {

    private String name;
    private Server.ClientThread clientThread = null;

    public Node(String name) {
        this.name = name;
    }

    public boolean isConnected() {
        return clientThread != null;
    }

    public void setClientThread(Server.ClientThread thread) {
        this.clientThread = thread;
    }

    public void removeClientThread() {
        this.clientThread = null;
    }

    public Server.ClientThread getClientThread() {
        return clientThread;
    }

    public String getName() {
        return name;
    }
}
