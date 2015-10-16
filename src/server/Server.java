package server;

import global.Settings;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    // list of all clusters in system
    private ArrayList<String> clusterList;

    public Server() {
        try {
            startServer();
        } catch (IOException e) {
            // TODO: handle exception
            System.out.println("Server failed to open socket");
        }
        importClusterList();
    }

    // start server and constantly listen for client connections
    private void startServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(Settings.PORT_NUMBER);
        Socket clientSocket;

        while(true) {
            System.out.println("waiting for client input");
            clientSocket = serverSocket.accept();

            ClientThread thread = new ClientThread(clientSocket);
            thread.start();
        }
    }

    /**
     * Import list of clusters from the res folder into arraylist.
     */
    private void importClusterList() {

        clusterList = new ArrayList<String>();
        BufferedReader br = null;
        try {
            String currentCluster;
            br = new BufferedReader(new FileReader(Settings.CLUSTER_LIST_PATH));

            while ((currentCluster = br.readLine()) != null) {
                clusterList.add(currentCluster);
            }
        } catch (IOException e) {
          //TODO: handle exception
            System.err.println("Error importing cluster list file");
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException ex) {
                //TODO: handle exception
                System.err.println("Error closing cluster list file");
            }
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
