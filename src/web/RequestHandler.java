package web;

import data.Domain;
import data.Planner;
import server.Server;

import java.io.*;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class for handling GET and POST requests from web clients.
 * The RequestHandler parses request parameters and returns requested
 * XML files for GET requests and handles planner and domain submissions
 * in POST requests.
 */
public class RequestHandler {

    // an instance of the running server
    private Server server;

    /**
     * Constructor accepts Server instance as parameter
     *
     * @param server the instance of the running server
     */
    public RequestHandler(Server server) {
        this.server = server;
    }

    private void doGet(Socket request, String domainRequested) throws IOException {
        StringBuilder builder = new StringBuilder();

        if (domainRequested.equals("all")) {
            builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<domains>\n");
            for (Domain d: server.getDomainList()) {

                String domainId = d.getXmlDomain().getDomain().getShortId();
                String[] domain = domainId.split("--");

                builder.append("<domain>\n");
                if (domain.length > 2) {
                    String ipc = domain[0].substring(3);
                    String name, formulation;

                    // the information is derived from the domain id. In the
                    // existing XMLs, the order between formulation and name
                    // is reversed for domains from IPC 2008 and 2011. Also,
                    // for domains of a longer id than 3 items, concatenate
                    // the name and size to create a name
                    if (domain.length == 4) {
                        name = domain[1].substring(0, 1).toUpperCase() + domain[1].substring(1) + " - " +
                                domain[2].substring(0, 1).toUpperCase() + domain[2].substring(1);
                        formulation = domain[3].substring(0, 1).toUpperCase() + domain[3].substring(1);
                    }
                    else if (ipc.equals("2008") || ipc.equals("2011")) {
                        name = domain[2].substring(0, 1).toUpperCase() + domain[2].substring(1);
                        formulation = domain[1].substring(0, 1).toUpperCase() + domain[1].substring(1);
                    } else {
                        name = domain[1].substring(0, 1).toUpperCase() + domain[1].substring(1);
                        formulation = domain[2].substring(0, 1).toUpperCase() + domain[2].substring(1);
                    }

                    builder.append(
                            "<id>" + domainId + "</id>\n" +
                            "<ipc>" + ipc + "</ipc>\n" +
                            "<name>" + name + "</name>\n" +
                            "<formulation>" + formulation + "</formulation>\n");
                } else {
                    String ipcCheck = domain[0].substring(3);
                    if (ipcCheck.equals("2002")) {
                        builder.append(
                                "<id>" + domainId + "</id>\n" +
                                "<ipc>" + ipcCheck + "</ipc>\n" +
                                "<name>" + domain[1].substring(0, 1).toUpperCase() + domain[1].substring(1) + "</name>\n");
                    } else {
                        // if the domain was not part of an IPC
                        builder.append(
                                "<id>" + domainId + "</id>\n" +
                                "<name>" + domain[0].substring(0, 1).toUpperCase() + domain[0].substring(1) + "</name>\n" +
                                "<formulation>" + domain[1].substring(0, 1).toUpperCase() + domain[1].substring(1) + "</formulation>\n");
                    }
                }
                builder.append("</domain>\n");
            }
            builder.append("</domains>");
        } else if (domainRequested.startsWith("leaderboard")) {
            builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<leaderboard>\n");
            LinkedHashMap<Planner, Double> leaderboard = server.getLeaderboard().getSortedLeaderboard();

            for (Map.Entry<Planner, Double> currentPlanner: leaderboard.entrySet()) {
                builder.append("<planner>" + currentPlanner.getKey().getName() + "</planner>\n");
                builder.append("<result>" + currentPlanner.getValue() + "</result>\n");
            }

            builder.append("</leaderboard>");
        } else {
            File file = null;

            if (domainRequested.startsWith("pddl-file")) {
                // the web client is requesting a domain pddl file
                String domainId = domainRequested.substring(domainRequested.indexOf('/') + 1, domainRequested.lastIndexOf('/'));
                String fileName = domainRequested.substring(domainRequested.lastIndexOf('/') + 1);

                for (Domain d : server.getDomainList()) {
                    if (d.getXmlDomain().getDomain().getShortId().equals(domainId)) {
                        file = new File(d.getPath() + "/" + fileName);
                        break;
                    }
                }
            } else {
                // the web client is requesting a specific domain
                builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");

                for (Domain d : server.getDomainList()) {
                    if (d.getXmlDomain().getDomain().getShortId().equals(domainRequested)) {
                        file = d.getXmlDomain().getXmlFile();
                        break;
                    }
                }
            }
            if (file != null) {

                BufferedReader buffer = new BufferedReader(new FileReader(file));

                String currentLine;
                while ((currentLine = buffer.readLine()) != null) {
                    builder.append(currentLine + "\n");
                }
            }
        }

        sendResponse(request, builder);
    }

    private void doPost(Socket request, String requestBody) {
        // handle request body
    }

    /**
     * Receives all http requests and parses them to determine their type,
     * then routes the request parameters to either doGet or doPost.
     *
     * @param request Socket object containing the HTTP request
     * @throws IOException
     */
    public void handleRequest(Socket request) throws IOException {
        BufferedReader requestReader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String requestBody;
        String input;

        while (!(input = requestReader.readLine()).equals("")) {
            if (input.startsWith("GET")) {
                input = input.substring(input.indexOf('/') + 1);

                if (input.startsWith(" ")) {
                    requestBody = "all";
                } else {
                    requestBody = input.substring(0, input.indexOf(" "));
                }

                doGet(request, requestBody);
                break;
            }
            if (input.startsWith("POST")) {
                requestBody = "";
                while (!(requestReader.readLine()).startsWith("{")) {}
                while((input = requestReader.readLine()).startsWith(" ")) {
                    requestBody += input.replaceAll(" ", "");
                }
                doPost(request, requestBody);
                requestReader.close();
                break;
            }
        }
    }

    private void sendResponse(Socket request, StringBuilder response) throws IOException {
        PrintWriter responseWriter = new PrintWriter(request.getOutputStream());

        responseWriter.println("HTTP/1.1 200 OK\n" +
            "Content-Type: application/xml; charset=utf-8\n" +
            "Content-Length: " + response.toString().length() + "\n" +
            "");

        responseWriter.println(response.toString());
        responseWriter.close();

    }
}
