package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * A thread for every cluster connected to the server.
 */
public class ClientThread extends Thread {

    // server streams
    private Socket clientSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public ClientThread(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;

        outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        inputStream = new ObjectInputStream(clientSocket.getInputStream());

    }
}
