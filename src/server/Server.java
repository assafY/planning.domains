package server;

import global.Message;
import global.Settings;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    // list of all possible nodes in system
    private ArrayList<Node> nodeList;
    // process builder to run OS jobs
    ProcessBuilder pBuilder;

    public Server() {
        importNodeList();
        startServer();
        startNodes();
    }

    // start server and constantly listen for client connections
    private void startServer() {
        try {
            final ServerSocket serverSocket = new ServerSocket(Settings.PORT_NUMBER);
            System.out.println("Server running. Waiting for clients to connect");
            Thread awaitConnections = new Thread() {
                public void run() {
                    while (true) {
                        try {
                            Socket clientSocket = serverSocket.accept();
                            ClientThread thread = new ClientThread(clientSocket);
                            thread.start();
                        } catch (IOException e) {
                            // TODO: handle exception
                            System.out.println("Server failed to open client socket");
                            System.err.println(e.getStackTrace());
                        }
                    }
                }
            };
            awaitConnections.start();
        } catch (IOException e) {
            //TODO: handle exception
            System.err.println("Error starting server");
        }
    }

    /**
     * Uses process builder to run a script SSHing into all nodes in list
     * and running the client Java files on them.
     */
    private void startNodes() {
        pBuilder = new ProcessBuilder(Settings.NODE_START_SCRIPT, Settings.NODE_LIST_PATH);
        System.out.println("Starting up nodes...");
        try {
            // start the process
            Process process = pBuilder.start();

            // wait for process to finish running and get result code
            int resultCode = process.waitFor();

            // if no errors, notify and get number of online nodes
            if (resultCode == 0) {
                int numOfNodes = 0;
                for (Node n: nodeList) {
                    if (n.isConnected()) { ++numOfNodes; }
                }
                Thread.sleep(1000); // wait for final node to connect
                System.out.println("Successfully started " + numOfNodes +
                        " out of " + nodeList.size() + " nodes.");
            } else {
                // error running script
                System.err.println("Error starting nodes. Check for existence" +
                        " of script and node list as well as paths in Settings.java");
            }
        } catch (IOException e) {
            //TODO: handle exception
            System.err.println("Error running node starting process");
        } catch (InterruptedException e1) {
            //TODO: handle exception
            System.err.println("Waiting for process to end interrupted");
        }
    }

    /**
     * Returns a node object matching a String with a name
     *
     * @param nodeName String containing name of the node
     * @return node if found a match, null if no match is found
     */
    public Node getNode(String nodeName) {
        for (Node n: nodeList) {
            if (n.getName().equals(nodeName)) {
                return n;
            }
        }
        return null;
    }

    /**
     * Import list of nodes from text file in res folder into arraylist.
     */
    private void importNodeList() {

        nodeList = new ArrayList<>();
        BufferedReader br = null;
        try {
            String currentNodeName;
            br = new BufferedReader(new FileReader(Settings.NODE_LIST_PATH));

            while ((currentNodeName = br.readLine()) != null) {
                nodeList.add(new Node(currentNodeName.replaceAll("\\s", "")));
            }
        } catch (IOException e) {
          //TODO: handle exception
            System.err.println("Error importing node list file");
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException ex) {
                //TODO: handle exception
                System.err.println("Error closing node list file");
            }
        }
    }

    /**
     * A thread for every node that has a connected thread.
     */
    public class ClientThread extends Thread {

        // client identifier
        private Node node;

        // server streams
        private Socket clientSocket;
        private ObjectInputStream inputStream;
        private ObjectOutputStream outputStream;

        public ClientThread(Socket clientSocket) throws IOException {
            this.clientSocket = clientSocket;

            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            inputStream = new ObjectInputStream(clientSocket.getInputStream());
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
                    node = getNode(msg.getMessage());
                    if (!node.isConnected()) { // if client is not already connected
                        node.setConnected(true);
                        System.out.println("New client connected: " + node.getName());
                    } else { // if a client already has a thread running
                        node = null;
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
            if (node != null) {
                node.setConnected(false);
                System.out.println(node.getName() + " disconnected.");
                node = null;
            }
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
