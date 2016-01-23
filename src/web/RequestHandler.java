package web;

import server.Domain;
import server.Server;

import java.io.*;
import java.net.Socket;

public class RequestHandler {

    // an instance of the running server
    private Server server;

    public RequestHandler(Server server) {
        this.server = server;
    }

    public void handleRequest(Socket request) throws IOException {
        StringBuilder builder = new StringBuilder();
        String domainRequested = parseRequest(request);

        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");

        if (domainRequested.equals("all")) {
            builder.append("<domains>\n");
            for (Domain d: server.getDomainList()) {
                builder.append("<domain>\n" +
                        "<name>" + d + "</name>\n" +
                        "<id>" + d.getXmlDomain().getDomain().getShortId() + "</id>\n" +
                        "</domain>\n");
            }
            builder.append("</domains>\n");
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

    private String parseRequest(Socket request) throws IOException {
        BufferedReader requestReader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String domainRequested = null;
        String input;

        while (!(input = requestReader.readLine()).equals("")) {
            if (input.startsWith("GET")) {
                input = input.substring(input.indexOf('/') + 1);

                if (input.startsWith(" ")) {
                    domainRequested = "all";
                } else {
                    domainRequested = input.substring(0, input.indexOf(" "));
                }
            }
        }

        return domainRequested;
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
