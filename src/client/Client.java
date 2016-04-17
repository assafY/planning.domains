package client;

import global.*;
import server.Job;
import data.XmlDomain;

import java.io.*;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Client class to run independently on nodes in the system. Nodes
 * connect to the server and communicate with it using Message objects. Nodes
 * run planners on domains locally and update the server with progress and results.
 */
public class Client {

    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private String clientName;

    private ProcessBuilder pBuilder;

    /**
     * Opens a socket to the server, and gets object input and output streams. Starts
     * a thread listening for messages received from the server.
     */
    public void connect() {
        try {
            // connect to server
            Socket clientSocket = new Socket(Settings.HOST_NAME, Settings.PORT_NUMBER);

            // get input and output streams
            inputStream = new ObjectInputStream(clientSocket.getInputStream());
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());

            // find client name and notify server
            setClientName();
            sendMessage(new Message(clientName, 1));

            // listen for incoming messages from the server
            new ListenFromServer().start();
            sendMessage(new Message(Message.JOB_REQUEST));

        } catch (IOException e) {
            //TODO: handle exception
            System.err.println("Error connecting to server");
        }
    }

    private void setClientName() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            clientName = address.getHostName().replaceAll("\\s", "");
        } catch (UnknownHostException e) {
            //TODO : handle exception
            System.out.println("Cannot resolve hostname");
        }
    }

    private void runPlannerProcess(Job job) {

        String domainPath = job.getDomainPath();
        String domainId = job.getDomainId();
        String plannerPath = job.getPlanner().getPath();
        XmlDomain.Domain.Problems.Problem problem = job.getProblem();
        String resultFile = plannerPath + "/" + job.getPlanner().getName() + "-" + domainId + "-" + problem;
        boolean success = false;

        StringBuilder processInput = new StringBuilder();
        processInput.append(job + " process log\n\n");

        // special case required if running lama - need to run translate
        // and preprocess scripts before plan script
        if (job.getPlanner().getName().equals("seq-sat-lama-2011")) {
            String[] lamaArguments = {Settings.RUN_LAMA_TRANSLATE, domainPath + problem.getDomain_file(),
                    domainPath + problem.getProblem_file()};

            pBuilder = new ProcessBuilder(lamaArguments);
            Process process;

            try {
                process = pBuilder.start();

                String runError = (Global.getProcessOutput(process.getErrorStream()));
                String runInput = (Global.getProcessOutput(process.getInputStream()));

                processInput.append("lama translate.py input:\n" + runInput +
                        "\nlama translate.py error:\n" + runError);

                int result = process.waitFor();

                if (result == 0) {
                    pBuilder = new ProcessBuilder(Settings.RUN_LAMA_PREPROCESS);
                    process = pBuilder.start();
                    process.waitFor();
                }

            } catch (IOException e) {
                sendMessage(new Message(e, Message.JOB_INTERRUPTED));
            } catch (InterruptedException e) {
                sendMessage(new Message(e, Message.JOB_INTERRUPTED));
            }
        }

        /*
            The arguments for the process builder. Run the plan script in the planner path,
            with the following parameters:
                the path and file name of the domain.pddl file
                the path and file name of the problem file
                the name of the result file to create, in the format of domainId:problemNum
         */
        String[] arguments = {Settings.RUN_PLANNER_SCRIPT, plannerPath, domainPath + problem.getDomain_file(),
                domainPath + problem.getProblem_file(), resultFile};

        pBuilder = new ProcessBuilder(arguments);
        Process process;

        try {
            // long startTime = System.currentTimeMillis();
            process = pBuilder.start();

            // print the result, later this must record the result
            String runError = (Global.getProcessOutput(process.getErrorStream()));
            String runInput = (Global.getProcessOutput(process.getInputStream()));

            processInput.append("planner process input:\n" + runInput +
                    "\nplanner process error:\n" + runError);

            int result = process.waitFor();
            // long totalTime = System.currentTimeMillis() - startTime;

            // if the run finished successfully, reset the process builder to run result sending script
            if (result == 0) {
                // check for different requirement incompatibility errors thrown by different planners
                if (runInput.contains("Undeclared requirement") || runInput.contains("Was expecting") ||
                        runInput.contains("Parsing error") || runInput.contains("Segmentation fault") ||
                        runInput.contains("not supported")) {
                    sendMessage(new Message(Message.INCOMPATIBLE_DOMAIN));
                } else {

                    // start process for sending results
                    String[] resultArgs = {Settings.RESULT_COPY_SCRIPT, resultFile, Settings.USER_NAME + "@"
                            + Settings.HOST_NAME + ":" + Settings.REMOTE_RESULT_DIR + job.getPlanner().getName()};
                    pBuilder.command(resultArgs);
                    process = pBuilder.start();
                    String error = Global.getProcessOutput(process.getErrorStream()).toLowerCase();
                    String input = Global.getProcessOutput(process.getInputStream()).toLowerCase();

                    processInput.append("\ncopy process input:\n" + input +
                            "\ncopy process error:\n" + error);

                    int newResult = process.waitFor();

                    // if results file doesn't exist check for
                    // different planners' incompatibility errors
                    if (newResult != 0) {
                        if (error.contains("no such file")) {
                            if (runError.contains("AssertionError") || runError.contains("definition expected") ||
                                    runError.contains("Failed to open domain file") || runError.contains("can't find operator file") ||
                                    runError.contains("not supported") || runError.contains("Segmentation fault")) {

                                sendMessage(new Message(Message.INCOMPATIBLE_DOMAIN));
                            }
                        }
                    } else {
                        success = true;
                    }
                }
            }

            // if the run did not finish successfully
            else {
                // check for different requirement incompatibility errors thrown by different planners
                if (processInput.toString().contains("AssertionError") || runError.contains("AssertionError") || runError.contains("definition expected") ||
                        runError.contains("Failed to open domain file") || runError.contains("can't find operator file") ||
                        runError.contains("not supported") || runError.contains("Segmentation fault")) {

                    sendMessage(new Message(Message.INCOMPATIBLE_DOMAIN));
                } else {
                    sendMessage(new Message(processInput.toString(), Message.JOB_INTERRUPTED));
                }
            }

        } catch (IOException e) {
            sendMessage(new Message(e, Message.JOB_INTERRUPTED));

        } catch (InterruptedException e1) {
            sendMessage(new Message(e1, Message.JOB_INTERRUPTED));
        } finally {
            // if planner is lama, delete lama specific files
            if (job.getPlanner().getName().equals("seq-sat-lama-2011")) {
                pBuilder.command(Settings.LAMA_DEL_SCRIPT);
                try {
                    process = pBuilder.start();

                    String delError = Global.getProcessOutput(process.getErrorStream()).toLowerCase();
                    String delInput = Global.getProcessOutput(process.getInputStream()).toLowerCase();

                    processInput.append("\ndelete lama files input:\n" + delInput +
                            "\ndelete lama files error:\n" + delError);

                    process.waitFor();
                } catch (IOException e) {
                    sendMessage(new Message(e, Message.JOB_INTERRUPTED));
                } catch (InterruptedException e) {
                    sendMessage(new Message(e, Message.JOB_INTERRUPTED));
                }
            }
            // delete all local result files
            pBuilder.command(Settings.RESULT_DEL_SCRIPT, resultFile);
            try {
                process = pBuilder.start();

                String delError = Global.getProcessOutput(process.getErrorStream()).toLowerCase();
                String delInput = Global.getProcessOutput(process.getInputStream()).toLowerCase();

                processInput.append("\ndelete process input:\n" + delInput +
                        "\ndelete process error:\n" + delError);

                process.waitFor();
                if (success) {
                    sendMessage(new Message("success", processInput.toString(), Message.PROCESS_RESULTS));
                } else {
                    sendMessage(new Message("failure", processInput.toString(), Message.PROCESS_RESULTS));
                }
            } catch (IOException e) {
                sendMessage(new Message(e, Message.JOB_INTERRUPTED));
            } catch (InterruptedException e) {
                sendMessage(new Message(e, Message.JOB_INTERRUPTED));
            }
        }
    }

    /**
     * closes all sockets and streams
     */
    public void close() throws IOException {
        if (inputStream != null) inputStream.close();
        if (outputStream != null) outputStream.close();
    }

    /**
     * onReceiveMessage specifies the action the client needs to take depending
     * on the type of message received from the server.
     *
     * @param msg The message from the server
     */
    public void onReceiveMessage(Message msg) {
        switch (msg.getType()) {

            // if a thread is already running for this client
            case Message.DUPLICATE_THREAD:

                try {
                    close();
                } catch (IOException e) {
                    System.err.println("Error closing streams");
                }

                System.out.println("There is already a running thread for this client");
                System.exit(0);

            // if requested to run a planner on a domain
            case Message.RUN_JOB:
                runPlannerProcess(msg.getJob());

                break;
        }
    }

    /**
     * Listener class running an infinite loop on a thread, listening for
     * messages from server.
     */
    public class ListenFromServer extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Message msg = (Message) inputStream.readObject();
                    onReceiveMessage(msg);
                } catch (IOException e) {
                    //TODO: handle exception
                    break;
                } catch (ClassNotFoundException e1) {
                    //TODO: handle exception
                    break;
                }
            }
        }
    }

    /**
     * Send a message to the server
     *
     * @param msg The message to send
     */
    public void sendMessage(Message msg) {
        try {
            outputStream.writeObject(msg);
            outputStream.flush();
        } catch (IOException e) {
            //TODO: handle exception
            System.err.println("Error sending message to server");
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.connect();
    }
}