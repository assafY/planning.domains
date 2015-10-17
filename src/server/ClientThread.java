package server;

import global.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * A thread for every cluster connected to the server.
 */
public class ClientThread extends Thread {

    // client identifier
    private String name;

    // server streams
    private Socket clientSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public ClientThread(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;

        outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        inputStream = new ObjectInputStream(clientSocket.getInputStream());

    }

    // close all sockets and streams
    public void close() throws IOException {
        if (inputStream != null) inputStream.close();
        if (outputStream != null) outputStream.close();
        if (clientSocket != null) clientSocket.close();
    }

    // send a message out to the client linked to this client thread
    public void sendMessage(Message msg) throws IOException {
        outputStream.writeObject(msg);
    }

    /**
     * onReceiveMessage specifies the action the client needs to take depending
     * on the type of message received from the server.
     *
     * @param msg The message from the server
     */
    public void onReceiveMessage(Message msg) {

        switch (msg.getType()) {
            case Message.CLIENT_CONNECTED:
                name = msg.getMessage();
        }
    }

    // listen for incoming messages from the client and decide what to do with them
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
    }
}
