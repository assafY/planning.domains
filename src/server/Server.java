package server;

import data.Domain;
import data.Planner;
import data.XmlDomain;
import global.Global;
import global.Message;
import global.Settings;
import web.RequestHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.PriorityBlockingQueue;

public class Server {

    // lists of all possible nodes, domains and planners in system
    private ArrayList<Node> nodeList;
    private ArrayList<Domain> domainList;
    private ArrayList<Planner> plannerList;

    // process builder to run OS jobs
    private ProcessBuilder pBuilder;

    // Xml file parser
    private XmlParser xmlParser = new XmlParser();

    // Queue for jobs waiting to run on nodes
    private PriorityBlockingQueue<Job> jobQueue;

    private RequestHandler requestHandler = new RequestHandler(this);

    public Server() {

        // import text files into lists of objects
        importList(Settings.NODE_LIST_PATH);
        importList(Settings.PLANNER_LIST_PATH);

        // import all domains in the domain directory
        domainList = xmlParser.getDomainList();

        // initialise job queue and add listener
        jobQueue = new PriorityBlockingQueue();



        // start the server
        startServer();

        Domain someDomain = domainList.get(20);
        ArrayList<XmlDomain.Domain.Problems.Problem> someprobs = new ArrayList<>();
        System.out.println("Problems added to list:");
        for (XmlDomain.Domain.Problems.Problem p: someDomain.getXmlDomain().getDomain().getProblems().getProblem()) {
            someprobs.add(p);
            System.out.println(p);
        }
        createJob(plannerList.get(1), someprobs, someDomain);

        // start all nodes
        //startNodes();

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

                            //check if client is internal node or external web client
                            if (clientSocket.getInetAddress().toString().startsWith("/137.73")) {
                                ClientThread thread = new ClientThread(clientSocket);
                                thread.start();
                            } else {
                                requestHandler.handleRequest(clientSocket);
                            }
                        }
                        // catch exception if a web client sends server request
                        catch (StreamCorruptedException e1) {
                            // TODO: handle exception
                        } catch (IOException e) {
                            // TODO: handle exception
                            System.out.println("Server failed to open client socket");
                            e.printStackTrace();
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
     * Returns a node object matching a String with a name.
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
     * Returns the list of all domains
     *
     * @return list of domains
     */
    public ArrayList<Domain> getDomainList() {
        return domainList;
    }

    /**
     * Create one new job and add it to the job queue.
     *
     * @param planner the planner to run
     * @param problem the problem to run it on
     * @param domain the domain the problem belongs to
     */
    public void createJob(Planner planner, XmlDomain.Domain.Problems.Problem problem, Domain domain) {
        jobQueue.put(new Job(planner, problem, domain));
    }

    /**
     * Create new jobs from a list of problems and add them to the
     * job queue.
     *
     * @param planner the planner to run
     * @param problems a list of problems to run it on
     * @param domain the domain the problems belong to
     */
    public void createJob(Planner planner, ArrayList<XmlDomain.Domain.Problems.Problem> problems, Domain domain) {
        System.out.println("Creating jobs...");
        for (XmlDomain.Domain.Problems.Problem p: problems) {
            jobQueue.put(new Job(planner, p, domain));
            System.out.println("Created job for problem " + p);
        }
    }

    /**
     * Processes result files sent to the server by a node after
     * receiving a notification that a job completed successfully.
     * Gets the best result and adds it to the problem's result map.
     *
     * @param job the job which was completed
     */
    public void processResults(Job job) {
        pBuilder = new ProcessBuilder(Settings.RUN_VALIDATION_SCRIPT, Settings.VAL_FILES_DIR, job.getDomainPath() + job.getProblem().getDomain_file(),
                job.getDomainPath() + job.getProblem().getProblem_file(), Settings.LOCAL_RESULT_DIR + job.getPlanner().getName() + "/" + job.getDomainId() + "-" + job.getProblem());
        try {
            Process process = pBuilder.start();
            int processResult = process.waitFor();

            String results = Global.getProcessOutput(process.getInputStream());
            if (processResult == 0) {
                ArrayList<String> resultList = new ArrayList<>(Arrays.asList(results.split(System.getProperty("line.separator"))));
                int bestResult = -1;
                for (String s: resultList) {
                    int result = Integer.parseInt(s.substring(s.indexOf(' ') + 1));
                    if (bestResult == -1 || result < bestResult) {
                        bestResult = result;
                    }
                }
                System.out.println(job.getPlanner().getName() + " - " + job.getDomainId() + " - " + job.getProblem() + ": best result: " + bestResult);
                job.getProblem().addResult(job.getPlanner(), bestResult);
            } else {
                System.out.println(Global.getProcessOutput(process.getErrorStream()));
            }
        } catch (IOException e) {
            //TODO: handle excpetion
            e.printStackTrace();
        } catch (InterruptedException e1) {
            //TODO: handle exception
            e1.printStackTrace();
        }
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
                    }
                    break;

                // if importing the planner list
                case Settings.PLANNER_LIST_PATH:
                    plannerList = new ArrayList<>();
                    while ((currentLine = br.readLine()) != null) {
                        String plannerName = currentLine.replaceAll("\\s", "");
                        plannerList.add(new Planner(plannerName));
                        // create results folder if it does not exist
                        (new ProcessBuilder("mkdir", "-p", Settings.LOCAL_RESULT_DIR + plannerName)).start();
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

        // current job being executed
        private Job currentJob;

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
         * checks whether this node is currently processing a job
         *
         * @return true if node is busy
         */
        public boolean isBusy() {
            return (currentJob != null);
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
         * Removes a job from the queue or waits for the queue
         * to contain a job if it is currently empty, then sends the
         * job to the thread's node.
         */
        public void takeJob() {
            try {
                // this method blocks the thread if the queue is empty
                currentJob = jobQueue.take();

                // if the planner is not known to be
                // incompatible with this job's domain
                if (!currentJob.getPlanner().getIncompatibleDomains().contains(currentJob.getDomain())) {
                    System.out.println("Attempting to run " + currentJob + " on " + node.getName());
                    sendMessage(new Message(currentJob, Message.RUN_JOB));
                }
                else {
                    System.err.println(currentJob.getPlanner().getName() + " is incompatible with " + currentJob.getDomainId());
                    currentJob = null;
                    takeJob();
                }
            } catch(InterruptedException e){
                System.err.println("Error getting a job from the queue:\n");
                e.printStackTrace();
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
                        sendMessage(new Message(Message.DUPLICATE_THREAD)); // notify the client to end
                    }
                    break;

                case Message.JOB_INTERRUPTED:
                    if (currentJob != null) {
                        System.out.println("Error running " + currentJob + " on " + node.getName()
                                + ".\nadding job back to queue with higher priority");
                        jobQueue.put(new Job(currentJob, 2));
                        currentJob = null;

                        // if an exception was sent, print it.
                        if (msg.getException() != null) {
                            System.out.println("The client produced the following exception:\n");
                            msg.getException().printStackTrace();
                        }
                    }

                    takeJob();
                    break;

                case Message.JOB_REQUEST:
                    // if currentJob is not null, the node completed a job successfully
                    if (currentJob != null) {
                        System.out.println("Successfully completed running " + currentJob + " on " + node.getName());
                        processResults(currentJob);
                        currentJob = null;
                    }

                    takeJob();
                    break;

                case Message.INCOMPATIBLE_DOMAIN:
                    if (!currentJob.getPlanner().getIncompatibleDomains().contains(currentJob.getDomain())) {
                        currentJob.getPlanner().addIncompatibleDomain(currentJob.getDomain());
                        System.err.println(currentJob.getPlanner().getName() + " is incompatible with " + currentJob.getDomainId());
                    }
                    currentJob = null;

                    takeJob();
                    break;

                case Message.PLAN_NOT_FOUND:
                    currentJob.getProblem().addResult(currentJob.getPlanner(), 0);
                    currentJob = null;

                    takeJob();
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

                // if there was a job assigned to this thread when it ended
                // the job did not complete and is sent back to the queue
                // with a higher priority
                if (currentJob != null) {
                    System.out.println("Error running " + currentJob + " on " + node.getName()
                            + ".\nadding job back to queue with higher priority");
                    jobQueue.put(new Job(currentJob, 2));
                }

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
