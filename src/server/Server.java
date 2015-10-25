package server;

import global.Message;
import global.Settings;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    // lists of all possible nodes, domains and planners in system
    private ArrayList<Node> nodeList;
    private ArrayList<Domain> domainList;
    private ArrayList<Planner> plannerList;

    // process builder to run OS jobs
    ProcessBuilder pBuilder;

    public Server() {

        // import text files into lists of objects
        importList(Settings.NODE_LIST_PATH);
        importList(Settings.DOMAIN_LIST_PATH);
        importList(Settings.PLANNER_LIST_PATH);

        // start the server
        startServer();

        // start all nodes
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

            // if no errors, count online nodes and notify
            if (resultCode == 0) {
                int numOfNodes = 0;
                for (Node n: nodeList) {
                    if (n.isConnected()) { ++numOfNodes; }
                }

                // wait for final node to connect
                Thread.sleep(1000);
                System.out.println("Successfully started " + numOfNodes +
                        " out of " + nodeList.size() + " nodes.");
            }

            // error running script
            else {
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
     * This method imports text files to lists of objects.
     *
     * @param filePath the path to the file being imported
     */
    private void importList(String filePath) {

        BufferedReader br = null;
        try {
            String currentLine;
            br = new BufferedReader(new FileReader(filePath));

            // determine what list this is based on list path
            switch (filePath) {

                // if importing the node list
                case Settings.NODE_LIST_PATH:
                    nodeList = new ArrayList<>();
                    while ((currentLine = br.readLine()) != null) {
                        nodeList.add(new Node(currentLine.replaceAll("\\s", "")));
                        System.out.println(currentLine);
                    }
                    break;

                // if importing the domain list
                case Settings.DOMAIN_LIST_PATH:
                    domainList = new ArrayList<>();
                    while ((currentLine = br.readLine()) != null) {
                        //domainList.add(new Domain(currentLine.replaceAll("\\s", "")));
                        System.out.println(currentLine);
                    }
                    break;

                // if importing the planner list
                case Settings.PLANNER_LIST_PATH:
                    plannerList = new ArrayList<>();
                    while ((currentLine = br.readLine()) != null) {
                        plannerList.add(new Planner(currentLine.replaceAll("\\s", "")));
                        System.out.println(currentLine);
                    }
                    break;
            }
        } catch (IOException e) {
          //TODO: handle exception
            System.err.println("Error importing " + filePath + ".");
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException ex) {
                //TODO: handle exception
                System.err.println("Error closing " + filePath + ".");
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

                // if a client is trying to connect
                case Message.CLIENT_CONNECTED:
                    node = getNode(msg.getMessage());

                    // if client is not already connected
                    if (!node.isConnected()) {
                        node.setClientThread(this);
                        System.out.println("New client connected: " + node.getName());

                    // if a client already has a thread running
                    } else {
                        node = null;
                        sendMessage(new Message(3)); // notify the client to end
                    }
                    break;
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

            // if this isn't an already connected client that tried to open another thread
            if (node != null) {
                node.removeClientThread();
                System.out.println(node.getName() + " disconnected.");
                node = null;
            }
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
