package client;

import global.*;
import server.Job;
import server.Planner;
import server.XmlDomain;

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

        String domainPath = job.getDomain().getPath() + "/";
        String domainId = job.getDomain().getXmlDomain().getDomain().getId().replaceAll("/", "-");
        String plannerPath = job.getPlanner().getPath();
        XmlDomain.Domain.Problems.Problem problem = job.getProblem();

        /*
            The arguments for the process builder. Run the plan script in the planner path,
            with the following parameters:
                the path and file name of the domain.pddl file
                the path and file name of the problem file
                the name of the result file to create, in the format of domainId:problemNum
         */
        String[] arguments = {plannerPath + "/plan", domainPath + problem.getDomain_file(),
                domainPath + problem.getProblem_file(), plannerPath + "/" + domainId + ":" + problem};

        pBuilder = new ProcessBuilder(arguments);
        Process process;

        try {
            process = pBuilder.start();

            // print the result, later this must record the result
            System.out.println(processOutput(process.getInputStream()));
            int result = process.waitFor();

            // if the run finished successfully, reset the process builder to run result sending script
            if (result == 0) {
                //TODO
                // start process for sending results
                pBuilder.command();
            }

        } catch (IOException e) {
            //TODO: handle exception
            System.err.println("error starting planner");
            e.printStackTrace();

        } catch (InterruptedException e1) {
            sendMessage(new Message(Message.JOB_INTERRUPTED));
        }
    }

    private String processOutput(InputStream is) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is));
            String currentLine = null;
            while ((currentLine = reader.readLine()) != null) {
                stringBuilder.append(currentLine + System.getProperty("line.separator"));
            }
        } finally {
            reader.close();
        }

        return stringBuilder.toString();
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