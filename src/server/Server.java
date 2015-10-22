package server;

import global.Message;
import global.Settings;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    // list of all possible clusters in system
    private ArrayList<Cluster> clusterList;

    public Server() {
        try {
            startServer();
        } catch (IOException e) {
            // TODO: handle exception
            System.out.println("Server failed to open socket");
        }
        importClusterList();
    }

    // start server and constantly listen for client connections
    private void startServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(Settings.PORT_NUMBER);
        Socket clientSocket;

        while(true) {
            System.out.println("waiting for client input");
            clientSocket = serverSocket.accept();

            ClientThread thread = new ClientThread(clientSocket);
            thread.start();
        }
    }

    /**
     * Returns a cluster object matching a String with a name
     *
     * @param clusterName name of the cluster
     * @return cluster if found a match, null if no match is found
     */
    public Cluster getCluster(String clusterName) {
        for (Cluster c: clusterList) {
            if (c.getName().equals(clusterName)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Import list of clusters from the res folder into arraylist.
     */
    private void importClusterList() {

        clusterList = new ArrayList<Cluster>();
        BufferedReader br = null;
        try {
            String currentClusterName;
            br = new BufferedReader(new FileReader(Settings.CLUSTER_LIST_PATH));

            while ((currentClusterName = br.readLine()) != null) {
                clusterList.add(new Cluster(currentClusterName));
            }
        } catch (IOException e) {
          //TODO: handle exception
            System.err.println("Error importing cluster list file");
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException ex) {
                //TODO: handle exception
                System.err.println("Error closing cluster list file");
            }
        }
    }

    /**
     * A thread for every cluster that has a connected thread.
     */
    public class ClientThread extends Thread {

        // client identifier
        private Cluster cluster;
        private boolean inUse;

        // server streams
        private Socket clientSocket;
        private ObjectInputStream inputStream;
        private ObjectOutputStream outputStream;

        public ClientThread(Socket clientSocket) throws IOException {
            this.clientSocket = clientSocket;

            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            inputStream = new ObjectInputStream(clientSocket.getInputStream());

            inUse = false;
        }

        /**
         * closes all sockets and streams
         */
        public void close() throws IOException {
            if (inputStream != null) inputStream.close();
            if (outputStream != null) outputStream.close();
            if (clientSocket != null) clientSocket.close();
        }

        /**
         * sends a message out to the client linked to this client thread
         */
        public void sendMessage(Message msg) {
            try {
                outputStream.writeObject(msg);
            } catch (IOException e) {
                //TODO: handle exception
                System.err.println("Error sending message to client");
            }
        }

        /**
         * onReceiveMessage specifies the action the server needs to take depending
         * on the type of message received from the client.
         *
         * @param msg The message from the client
         */
        public void onReceiveMessage(Message msg) {
            switch (msg.getType()) {
                case Message.CLIENT_CONNECTED: // if a client is trying to connect
                    cluster = getCluster(msg.getMessage());
                    if (!cluster.isConnected()) { // if client is not already connected
                        cluster.setConnected(true);
                        System.out.println("New client connected: " + cluster.getName());
                    } else { // if a client already has a thread running
                        cluster = null;
                        sendMessage(new Message(3)); // notify the client to end
                    }
            }
        }
        /**
         * listens for incoming messages from the client and decide what to do with them
         */
        @Override
        public void run() {
            while (true) {
                // try to read from stream
                Message msg;
                try {
                    msg = (Message) inputStream.readObject();
                    onReceiveMessage(msg);

                } catch (ClassNotFoundException e) {
                    //TODO: handle exception
                    break;
                } catch (IOException e) {
                    //TODO: handle exception
                    break;
                }
            }
            // loop finishes, therefore the client disconnected
            try {
                close();
            } catch (IOException e) {
                System.err.println("Error closing streams");
            }
            if (cluster != null) {
                cluster.setConnected(false);
            }
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
