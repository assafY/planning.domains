package server;

import data.Domain;
import data.Leaderboard;
import data.Planner;
import data.XmlDomain;
import global.Global;
import global.Message;
import global.Settings;
import web.RequestHandler;

import java.io.*;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;

public class Server {

    // lists of all possible nodes, domains and planners in system
    private ArrayList<Node> nodeList;
    private ArrayList<Domain> domainList;
    private ArrayList<Planner> plannerList;

    // date format object to use for displaying time of server output
    DateFormat dateFormat;

    // leaderboard of all existing planners
    private Leaderboard leaderboard;

    // process builder to run OS jobs
    private ProcessBuilder pBuilder;

    // Xml file parser
    private XmlParser xmlParser;

    // Queue for jobs waiting to run on nodes
    private PriorityBlockingQueue<Job> jobQueue;

    // request handler for http requests
    private RequestHandler requestHandler;

    // serializer for domain and planner list
    private Serializer serializer;

    // boolean determines whether it is safe to add domain to list
    private boolean domainAdditionSafe;

    public Server() {

        // initialise serializer and attempt to serialize
        // domain and planner lists
        serializer = new Serializer();
        plannerList = serializer.deserializePlannerList();
        domainList = serializer.deserializeDomainList();

        // set the date format to show time only for server outputs
        dateFormat = new SimpleDateFormat("HH:mm:ss");

        // initialise XML parser
        xmlParser = new XmlParser(this);

        // if serializer returns null domain list,
        // import all domains in the domain directory
        if (domainList == null) {
            domainList = xmlParser.getDomainList();
            serializer.serializeDomainList(domainList);
        } else {
            domainList = xmlParser.updateDomainList(domainList);
        }

        // import all new planners in planner text file
        importList(Settings.PLANNER_LIST_PATH);

        // import all nodes in text file
        importList(Settings.NODE_LIST_PATH);

        // initialise job queue and add listener
        jobQueue = new PriorityBlockingQueue();

        // initialise and set the leaderboard
        leaderboard = new Leaderboard();

        // initialise request handler
        requestHandler = new RequestHandler(this);
        domainAdditionSafe = true;

        // start the server
        startServer();

        // start all nodes
        startNodes();
        checkNodeStatus();

        // add all jobs
        addAllToQueue();

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
                        catch (StreamCorruptedException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            System.out.println("Server failed to open client socket\n" +
                                    "Check whether an instance of the server is already running");
                            e.printStackTrace();
                        }
                    }
                }
            };
            awaitConnections.start();

        } catch (IOException e) {
            //TODO: handle exception
            System.err.println("Error starting server");
            e.printStackTrace();
        }
    }

    /**
     * Uses process builder to run a script SSHing into all nodes in list
     * and running the client Java files on them.
     */
    private void startNodes() {
        // first copy RSA to all clients that don't have it
        pBuilder = new ProcessBuilder(Settings.RSA_COPY_SCIPRT, Settings.NODE_LIST_PATH);
        System.out.println("Verifying key existence on clients. This will take a few minutes");
        try {
            pBuilder.start().waitFor();
        } catch (IOException e) {

        } catch (InterruptedException e) {

        }

        // then ssh to all clients and start them
        pBuilder = new ProcessBuilder(Settings.NODE_START_SCRIPT, Settings.NODE_LIST_PATH);
        System.out.println(Settings.ANSI_YELLOW + "Starting up nodes..." + Settings.ANSI_GREEN);
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
                System.out.println(Settings.ANSI_GREEN + "Successfully started " + numOfNodes +
                        " out of " + nodeList.size() + " nodes." + Settings.ANSI_RESET);
            }

            // error running script
            else {
                System.err.println(Settings.ANSI_RED + "Error starting nodes. Check for existence" +
                        " of script and node list as well as paths in Settings.java" + Settings.ANSI_RESET);
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
     * Start a single node
     *
     * @param n the node to start
     */
    private void startNode(Node n) {
        pBuilder = new ProcessBuilder(Settings.SINGLE_NODE_START_SCRIPT, n.getName());
        System.out.println(Settings.ANSI_YELLOW + "Starting up node " + n.getName() + Settings.ANSI_RESET);
        try {
            Process process = pBuilder.start();
            int resultCode = process.waitFor();

            if (resultCode == 0) {
                if (n.isConnected()) {
                    System.out.print(Settings.ANSI_GREEN + n.getName() + " connected succesfully" + Settings.ANSI_RESET);
                } else {
                    System.err.print(Settings.ANSI_RED + n.getName() + " is still down" + Settings.ANSI_RESET);
                }
            } else {
                System.err.print(Settings.ANSI_RED + "An error occured while attempting to start " + n.getName() + Settings.ANSI_RESET);
            }
        } catch (IOException e) {
            System.err.print("Error starting node " + n.getName());
        } catch (InterruptedException e) {
            System.err.print("Error starting node " + n.getName());
        } finally {
            Date date = new Date();
            System.out.println(" " + dateFormat.format(date));
        }
    }

    /**
     * A thread that checks if nodes that are down
     * can be reconnected every 6 hours
     */
    private void checkNodeStatus() {
        Thread nodeStatusThread = new Thread() {

            @Override
            public void run() {
                while (true) {
                    try {
                        sleep(21600000);
                        for (Node n: nodeList) {
                            if (!n.isConnected()) {
                                startNode(n);
                            }
                        }
                    } catch (InterruptedException e) {
                        System.err.println(Settings.ANSI_RED + "Node status check interrupted. Restarting " + dateFormat.format(new Date()) + Settings.ANSI_RESET);
                        checkNodeStatus();
                    }
                }
            }
        };

        nodeStatusThread.start();
    }

    /**
     * Adds a new domain to the domain list and creates
     * jobs for all its files on all planners
     *
     * @param newDomain the domain to be added
     */
    public void addNewDomain(final Domain newDomain) {

        Thread domainAdditionThread = new Thread() {

            @Override
            public void run() {
                while (!domainAdditionSafe) {
                    // wait until domain addition is safe
                }
                if (!domainList.contains(newDomain)) {
                    domainList.add(newDomain);
                    serializer.serializeDomainList(domainList);

                    for (Planner currentPlanner: plannerList) {
                        createJob(currentPlanner, newDomain.getXmlDomain().getDomain().getProblems().getProblem(), newDomain);
                    }
                }
            }
        };

        domainAdditionThread.start();
    }

    /**
     * Adds jobs for all domains on all planners. This
     * results in days of planning computation so should
     * run once for initial result collection
     */
    private void addAllToQueue() {
        for (Domain currentDomain: domainList) {
            for (Planner currentPlanner: plannerList) {

                createJob(currentPlanner, currentDomain.getXmlDomain().getDomain().getProblems().getProblem(), currentDomain);
            }
        }
    }

    /**
     * Calculate and return the planner leaderboard
     *
     * @return Leaderboard object
     */
    public LinkedHashMap<String, Double> getLeaderboard() {
        return leaderboard.getLeaderboard(domainList);
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
     * Copy files uploaded to the server from the web,
     * either domain or planner, to a live node
     *
     * @param plannerFiles true if this is a planner upload
     * @param dirname the name of the local directory containing the files
     */
    public void copyFilesToNodes(boolean plannerFiles, String dirname) {
        String baseDir = "";

        if (plannerFiles) {
            baseDir = Settings.PLANNER_DIR_PATH;
        } else {
            baseDir = Settings.DOMAIN_DIR_PATH;
        }

        baseDir = baseDir + "uploads/";

        // find a live node to send to
        Node selecteNode = null;
        for (Node n: nodeList) {

            if (n.isConnected()) {
                selecteNode = n;
                break;
            }
        }

        // build file copy process
        pBuilder = new ProcessBuilder("scp", "-r", baseDir + dirname,
                Settings.USER_NAME + "@" + selecteNode.getName() +
            ":~/planning.domains/" + baseDir + dirname);

        try {
            pBuilder.start();
        } catch (IOException e) {
            System.err.println(Settings.ANSI_RED + "Error copying uploaded files" + Settings.ANSI_RESET);
        }
    }

    /**
     * Get method for this server's XML parser
     *
     * @return the XML parser
     */
    public XmlParser getXmlParser() {
        return xmlParser;
    }

    /**
     * Set whether safe to add a new domain to the server list
     *
     * @param safe boolean that determines safety
     */
    public void setDomainAdditionSafety(boolean safe) {
        this.domainAdditionSafe = safe;
    }

    /**
     * Create one new job and add it to the job queue.
     *
     * @param planner the planner to run
     * @param p the problem to run it on
     * @param domain the domain the problem belongs to
     */
    public void createJob(Planner planner, XmlDomain.Domain.Problems.Problem p, Domain domain) {
        if (!planner.getIncompatibleDomains().contains(domain.getXmlDomain().getDomain().getId()) &&
                p.getRunCount(planner) < 3 && !p.hasResult(planner)) {
            Job newJob = new Job(planner, p, domain);
            if (!jobQueue.contains(newJob)) {
                jobQueue.put(new Job(newJob, 2));
            }
        }
    }

    /**
     * Create new jobs from a list of problems and add them to the
     * job queue. For each problem checks whether a result already exists
     *
     * @param planner the planner to run
     * @param problems a list of problems to run it on
     * @param domain the domain the problems belong to
     */
    public void createJob(Planner planner, ArrayList<XmlDomain.Domain.Problems.Problem> problems, Domain domain) {
        for (XmlDomain.Domain.Problems.Problem p: problems) {
                // if the planner in the job is not incompatible with the domain in the job
                // and it didn't fail to find a plan for the problem in the job 3 times
                // and it didn't already find a valid plan for the problem in the job
                // and the queue does not already contain the job
            if (!planner.getIncompatibleDomains().contains(domain.getXmlDomain().getDomain().getId()) &&
                    p.getRunCount(planner) < 3 && !p.hasResult(planner)) {
                Job newJob = new Job(planner, p, domain);
                if (!jobQueue.contains(newJob)) {
                    jobQueue.put(newJob);
                }
            }
        }
    }

    /**
     * Processes result files sent to the server by a node after
     * receiving a notification that a job completed successfully.
     * Gets the best result and adds it to the problem's result map.
     *
     * @param job the job which was completed
     */
    public void processResults(Job job, String nodeName) {
        pBuilder = new ProcessBuilder(Settings.RUN_VALIDATION_SCRIPT, Settings.VAL_FILES_DIR, job.getDomainPath() + job.getProblem().getDomain_file(),
                job.getDomainPath() + job.getProblem().getProblem_file(), Settings.LOCAL_RESULT_DIR + job.getPlanner().getName() + "/" + job.getPlanner().getName() + "-" + job.getDomainId() + "-" + job.getProblem());
        try {
            Process process = pBuilder.start();
            int processResult = process.waitFor();

            String results = Global.getProcessOutput(process.getInputStream());
            if (processResult == 0) {
                if (results.contains("Value")) {
                    System.out.println(Settings.ANSI_GREEN + nodeName + ": " + job + " success " + dateFormat.format(new Date()) + Settings.ANSI_RESET);
                    ArrayList<String> resultList = new ArrayList<>(Arrays.asList(results.split(System.getProperty("line.separator"))));
                    int bestResult = -1;
                    for (String s : resultList) {
                        int result;
                        try {
                            result = Integer.parseInt(s.substring(s.indexOf(' ') + 1));
                        } catch (NumberFormatException e) {
                            // if result is in scientific notation
                            result = new BigDecimal(s.substring(s.indexOf(' ') + 1)).intValue();
                        }
                        if (bestResult == -1 || result < bestResult) {
                            bestResult = result;
                        }
                    }
                    job.getProblem().addResult(job.getPlanner(), bestResult);
                    serializer.serializeDomainList(domainList);
                }
            } else {
                System.err.println(Settings.ANSI_RED + nodeName + ": " + job + ": invalid plan" + Settings.ANSI_RESET);
                createErrorLog("Invalid Plan", job);
            }
        } catch (IOException e) {
            //TODO: handle excpetion
            e.printStackTrace();
        } catch (InterruptedException e1) {
            //TODO: handle exception
            e1.printStackTrace();
        } finally {

        }
    }

    /**
     * This method imports text files to lists of objects. If
     * the input is the planner directory and a Planner object list
     * already exists, the method checks the directory for planners
     * that don't already exist in the list.
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
                    // if a previous planner list did not exist
                    // create a new one from text file
                    if (plannerList == null) {
                        plannerList = new ArrayList<>();
                    }
                    while ((currentLine = br.readLine()) != null) {
                        boolean exists = false;
                        String plannerName = currentLine.replaceAll("\\s", "");

                        for (Planner planner: plannerList) {
                            if (planner.getName().equals(plannerName)) {
                                exists = true;
                                //break;
                            }
                        }

                        if (!exists) {
                            System.out.println("Added planner " + plannerName);
                            plannerList.add(new Planner(plannerName));
                            // create results folder if it does not exist
                            (new ProcessBuilder("mkdir", "-p", Settings.LOCAL_RESULT_DIR + plannerName)).start();
                        }
                    }
                    // remove planners not in the planner file
                    ArrayList<Planner> toRemove = new ArrayList<>();
                    for (Planner planner: plannerList) {
                        boolean exists = false;
                        br = new BufferedReader(new FileReader(filePath));
                        while ((currentLine = br.readLine()) != null) {
                            String plannerName = currentLine.replaceAll("\\s", "");
                            if (plannerName.equals(planner.getName())) {
                                exists = true;
                            }
                        }
                        if (!exists) {
                            toRemove.add(planner);
                        }
;                   }

                    for (Planner planner: toRemove) {
                        System.out.println("Removed planner " + planner.getName() + ": Not on planner list");
                        plannerList.remove(planner);
                    }


                    serializer.serializePlannerList(plannerList);
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

    private void createErrorLog(String error, Job job) {
        try {
            PrintWriter writer = new PrintWriter(Settings.LOCAL_RESULT_DIR + job.getPlanner().getName() + "/errors/"
                    + job.getPlanner().getName() + "-" + job.getDomainId() + "-" + job.getProblem() + "_errorlog");
            if (job.getPlanner().getIncompatibleDomains().contains(job.getDomain().getXmlDomain().getDomain().getId())) {
                writer.println("\nINCOMPATIBALE DOMAIN\n");
            }
            writer.println(error);
            writer.close();
        } catch (FileNotFoundException e) {

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
                System.err.println(Settings.ANSI_RED + "Error sending message to client" + Settings.ANSI_RESET);
            }
        }

        /**
         * Removes a job from the queue or waits for the queue
         * to contain a job if it is currently empty, then sends the
         * job to the thread's node.
         */
        public void takeJob() {

            try {
                // remove a job from the queue or wait for a job to be added
                currentJob = jobQueue.take();

                // if the planner in the job is not incompatible with the domain in the job
                // and it didn't fail to find a plan for the problem in the job 3 times
                // and it didn't already find a valid plan for the problem in the job
                if (!currentJob.getPlanner().getIncompatibleDomains().contains(currentJob.getDomain().getXmlDomain().getDomain().getId()) &&
                        currentJob.getProblem().getRunCount(currentJob.getPlanner()) < 3 &&
                        !currentJob.getProblem().hasResult(currentJob.getPlanner())) {

                    System.out.println(Settings.ANSI_YELLOW + node.getName() + ": Running job (" + currentJob + ") " + dateFormat.format(new Date()) + Settings.ANSI_RESET);
                    sendMessage(new Message(currentJob, Message.RUN_JOB));

                } else {
                    takeJob();
                }
            } catch (InterruptedException e) {
                System.err.println(Settings.ANSI_RED + "Error taking a job from the queue. Retrying.");
                takeJob();
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
                        System.out.println(Settings.ANSI_GREEN + node.getName() + ": Client connected" +  Settings.ANSI_RESET);

                    // if a client already has a thread running
                    } else {
                        node = null;
                        sendMessage(new Message(Message.DUPLICATE_THREAD)); // notify the client to end
                    }
                    break;

                case Message.JOB_INTERRUPTED:
                    if (msg.getMessage() != null) {
                        if(msg.getMessage().contains("Undeclared requirement")) {
                            onReceiveMessage(new Message(Message.INCOMPATIBLE_DOMAIN));
                            break;
                        }
                    }

                    Planner p = currentJob.getPlanner();
                    XmlDomain.Domain.Problems.Problem prob = currentJob.getProblem();
                    Domain d = currentJob.getDomain();
                    createJob(p, prob, d);
                    break;

                case Message.JOB_REQUEST:
                    takeJob();
                    break;

                case Message.INCOMPATIBLE_DOMAIN:
                    if (!currentJob.getPlanner().getIncompatibleDomains().contains(currentJob.getDomain().getXmlDomain().getDomain().getId())) {
                        currentJob.getPlanner().addIncompatibleDomain(currentJob.getDomain().getXmlDomain().getDomain().getId());
                    }
                    break;

                case Message.PROCESS_RESULTS:
                    try {
                        if (msg.getMessage().equals("success")) {
                            processResults(currentJob, node.getName());
                        } else {
                            createErrorLog(msg.getInput(), currentJob);
                            // if the planner is not incompatible with the problem in the current job
                            if (!currentJob.getPlanner().getIncompatibleDomains().contains(currentJob.getDomain().getXmlDomain().getDomain().getId())) {
                                System.err.println(Settings.ANSI_RED + node.getName() + ": " + currentJob + " failure " + dateFormat.format(new Date()) + Settings.ANSI_RESET);
                                // if the planner failed on the current problem less than 5 times
                                // put the job back in the queue with a higher priority
                                if (currentJob.getProblem().getRunCount(currentJob.getPlanner()) < 3) {
                                    currentJob.getProblem().increaseRunCount(currentJob.getPlanner());
                                    Planner planner = currentJob.getPlanner();
                                    XmlDomain.Domain.Problems.Problem problem = currentJob.getProblem();
                                    Domain domain = currentJob.getDomain();
                                    jobQueue.put(new Job(new Job(planner, problem, domain), 2));
                                }
                            }
                        }
                    } finally {
                        serializer.serializePlannerList(plannerList);
                        takeJob();
                    }
                    break;

                case Message.CLIENT_DISCONNECTED:
                    File resultFile = new File(Settings.LOCAL_RESULT_DIR + currentJob.getPlanner().getName() + "/" + currentJob.getPlanner().getName() + "-" + currentJob.getDomainId() + "-" + currentJob.getProblem());
                    if (resultFile.exists()) {
                        processResults(currentJob, node.getName());
                    } else {
                        if (!currentJob.getPlanner().getIncompatibleDomains().contains(currentJob.getDomain().getXmlDomain().getDomain().getId())) {
                            Planner planner = currentJob.getPlanner();
                            XmlDomain.Domain.Problems.Problem problem = currentJob.getProblem();
                            Domain domain = currentJob.getDomain();
                            createJob(planner, problem, domain);
                            break;
                        }
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

            // if this isn't an already connected client that tried to open another thread
            if (node != null) {

                // if there was a job assigned to this thread when it ended
                // the job did not complete and is sent back to the queue
                // with a higher priority
                if (currentJob != null) {
                    System.out.println(Settings.ANSI_RED + node.getName() + ": " + currentJob + " failed because client disconnected" +
                            "\nadding job back to queue with higher priority " + dateFormat.format(new Date()) + Settings.ANSI_RESET);
                    createJob(currentJob.getPlanner(), currentJob.getProblem(), currentJob.getDomain());
                }

                node.removeClientThread();
                System.out.println(Settings.ANSI_YELLOW + node.getName() + ": Client disconnected" + Settings.ANSI_RESET);
                node = null;
            }
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
