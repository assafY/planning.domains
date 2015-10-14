package server;

import global.Settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public Server() {
        try {
            ServerSocket serverSocket = new ServerSocket(Settings.PORT_NUMBER);
            System.out.println("waiting for client input");
            Socket clientSocket = serverSocket.accept();
            PrintWriter out =
                    new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;
            while((inputLine = in.readLine()) != null) {
                out.println(inputLine);
            }
        } catch (IOException e) {
            // TODO: handle exception
            System.out.println("Server failed to open socket");
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
    }
}
