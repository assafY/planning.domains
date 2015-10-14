package server;

import global.Settings;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private ArrayList<String> clusterList;

    public Server() {
        importClusterList();
        for(String s : clusterList) { System.out.println(s); }
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

    private void importClusterList() {
        clusterList = new ArrayList<String>();
        try {
            String currentCluster;
            BufferedReader br = new BufferedReader(new FileReader("./res/cluster_list.txt"));

            while ((currentCluster = br.readLine()) != null) {
                clusterList.add(currentCluster);
            }
        } catch (IOException e) {
          //TODO: handle exception
            System.out.println("Error importing cluster list");
        }

    }
}
