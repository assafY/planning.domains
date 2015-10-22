package client;

import global.*;
import java.io.*;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Client class to run independently on clusters in the system. Clusters
 * connect to the server and communicate with it using Message objects. Clusters
 * run planners on domains locally and update the server with progress and results.
 */
public class Client {

    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private String clientName;

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
            case Message.DUPLICATE_THREAD: // if a thread is already running for this client
                try {
                    close();
                } catch (IOException e) {
                    System.err.println("Error closing streams");
                }
                System.exit(0);
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