package web;

import server.Domain;
import server.Server;

import java.io.*;
import java.net.Socket;

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

        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");

        if (domainRequested.equals("all")) {
            builder.append("<domains>\n");
            for (Domain d: server.getDomainList()) {

                String domainId = d.getXmlDomain().getDomain().getShortId();
                String[] domain = domainId.split("--");

                builder.append("<domain>\n");
                if (domain.length == 3) {
                    String ipc = domain[0].substring(3);
                    String name, formulation;

                    // the information is derived from the domain id. In the
                    // existing XMLs, the order between formulation and name
                    // is reversed for domains from IPC 2008 and 2011
                    if (ipc.equals("2008") || ipc.equals("2011")) {
                        name = domain[2].substring(0, 1).toUpperCase() + domain[2].substring(1);
                        formulation = domain[1].substring(0, 1).toUpperCase() + domain[1].substring(1);
                    } else {
                        name = domain[1].substring(0, 1).toUpperCase() + domain[1].substring(1);
                        formulation = domain[2].substring(0, 1).toUpperCase() + domain[2].substring(1);
                    }

                    builder.append(
                            "<ipc>" + ipc + "</ipc>\n" +
                            "<name>" + name + "</name>\n" +
                            "<formulation>" + formulation + "</formulation>\n");
                } else {
                    builder.append(
                            "<name>" + domain[0].substring(0, 1).toUpperCase() + domain[0].substring(1) + "</name>\n" +
                            "<formulation>" + domain[1].substring(0, 1).toUpperCase() + domain[1].substring(1) + "</formulation>\n");
                }
                builder.append("</domain>\n");
            }
            builder.append("</domains>");
        } else {
            File xmlFile = null;
            for (Domain d: server.getDomainList()) {
                if (d.getXmlDomain().getDomain().getShortId().equals(domainRequested)) {
                    xmlFile = d.getXmlDomain().getXmlFile();
                    break;
                }
            }
            if (xmlFile != null) {

                BufferedReader xmlBuffer = new BufferedReader(new FileReader(xmlFile));

                String currentLine;
                while ((currentLine = xmlBuffer.readLine()) != null) {
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
