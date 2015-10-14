package client;

import global.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    public Client() {

    }

    public void connect() {
        try {
            Socket clientSocket = new Socket(Settings.HOST_NAME, Settings.PORT_NUMBER);
            System.out.println("Connected to server");
            PrintWriter out =
                    new PrintWriter(clientSocket.getOutputStream());
            BufferedReader in =
                    new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
            BufferedReader stdIn =
                    new BufferedReader(
                        new InputStreamReader(System.in));
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
            }
        } catch (IOException e) {
            //TODO: handle exception
            System.err.println("error connecting to server");
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
    }
}
