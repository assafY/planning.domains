package web;

import data.Domain;
import data.Planner;
import data.Result;
import data.XmlDomain;
import server.Server;

import java.io.*;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.*;

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

    /**
     * Handles GET requests. Five request types are possible:
     * all: the client requests an XML response with all domain names, ipc years and formulations
     * leaderboard: the client requests the current leaderboard for all planners
     * upload: a new domain or planner were uploaded, copy all new server files to one of the nodes
     * pddl: the client requests the pddl source of a domain file
     * domain: the client requests the XML file of a certain domain
     *
     * @param request the client socket
     * @param domainRequested the request data
     * @throws IOException
     */
    private void doGet(Socket request, String domainRequested) throws IOException {
        StringBuilder builder = new StringBuilder();

        if (domainRequested.equals("all")) {
            builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<domains>\n");
            for (Domain d : server.getDomainList()) {

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
                    } else if (ipc.equals("2008") || ipc.equals("2011")) {
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
                    String ipcCheck = "";
                    try {
                        ipcCheck = domain[0].substring(3);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        // this is an uploaded domain with fewer than 3 characters
                    }
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
            LinkedHashMap<String, Double> leaderboard = server.getLeaderboard();

            int rank = 1;
            for (Map.Entry<String, Double> currentPlanner : leaderboard.entrySet()) {

                // format result to two decimal place
                DecimalFormat df = new DecimalFormat("#.00");
                double score = Double.parseDouble(df.format(currentPlanner.getValue()));

                builder.append("<entry>\n");
                builder.append("<planner>" + currentPlanner.getKey() + "</planner>\n");
                builder.append("<rank>" + rank + "</rank>\n");
                builder.append("<score>" + score + "</score>\n");
                builder.append("</entry>\n");
                ++rank;
            }

            builder.append("</leaderboard>");
        } else if (domainRequested.contains("upload")) {
            String dir = domainRequested.substring(domainRequested.indexOf('/'));

            if (domainRequested.startsWith("planner")) {
                server.copyFilesToNodes(true, dir);
            } else if (domainRequested.startsWith("domain")) {
                server.copyFilesToNodes(false, dir);
                server.setDomainAdditionSafety(true);
            }

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
                        builder.append("<planning:metadata xmlns:planning=\"http://planning.domains/\">\n");
                        builder.append("<results>\n");
                        for (XmlDomain.Domain.Problems.Problem p: d.getXmlDomain().getDomain().getProblems().getProblem()) {
                            // get best result
                            Result currentResult = p.getBestResult();
                            builder.append("<problem>\n");
                            builder.append("<num>" + p + "</num>\n");
                            if (currentResult != null) {
                                builder.append("<actions>" + currentResult.getResult() + "</actions>\n");
                                builder.append("<planner>" + currentResult.getPlannerName() + "</planner>\n");
                            }
                            builder.append("</problem>\n");
                        }
                        builder.append("</results>\n");
                        file = d.getXmlDomain().getXmlFile();
                        break;
                    }
                }
            }
            if (file != null) {

                BufferedReader buffer = new BufferedReader(new FileReader(file));

                String currentLine;
                while ((currentLine = buffer.readLine()) != null) {
                    if (!currentLine.contains("xmlns:planning")) {
                        builder.append(currentLine + "\n");
                    }
                }
            }
        }

        sendResponse(request, builder);
    }

    /**
     * Handles POST requests. POST requests are only sent when a new
     * a new domain is uploaded. The method parses the request and
     * build an attribute and file maps which are later used to
     * create a new XmlDomain object and marshal an XML file. The response
     * to the client is the directory where the domain files should be uploaded.
     *
     * @param request the client socket
     * @param requestBody the request data
     * @throws IOException
     */
    private void doPost(Socket request, String requestBody) throws IOException {

        server.setDomainAdditionSafety(false);

        requestBody = requestBody.replaceAll("%5B", "[");
        requestBody = requestBody.replaceAll("%5D", "]");
        requestBody = requestBody.replaceAll("%20", "");
        requestBody = requestBody.replaceAll(".000Z", "");
        String[] formData = requestBody.split("&");

        Map<String, String> attributeMap = new HashMap<>();
        //ArrayList<Map<String, ArrayList<String>>> fileMapList = new ArrayList<>();
        int fileIndex = 0;

        for (int i = 0; i < formData.length; ++i) {
            String[] currentField = formData[i].split("=");

            if (!currentField[0].startsWith("domainFiles")) {
                // make sure to map connection to domain name
                if (currentField[0].startsWith("Connection")) {
                    attributeMap.put("name", currentField[1]);
                } else {
                    if (currentField.length > 1) {
                        attributeMap.put(currentField[0], currentField[1]);
                    }
                }
            } else {
                fileIndex = i;
                break;
            }
        }

        int domainFileCounter = -1;
        Map<String, ArrayList<String>> fileMap = new HashMap<>();
        String currentDomainFile = "";
        ArrayList<String> currentProblemFiles = new ArrayList<>();

        for (int i = fileIndex; i < formData.length; ++i) {
            String[] currentFile = formData[i].split("=");
            int currentFileIndex = Integer.parseInt(currentFile[0].substring(
                    12, currentFile[0].indexOf(']')));
            if (currentFileIndex != domainFileCounter) {
                if (domainFileCounter != -1) {
                    ArrayList<String> pFilesCopy = currentProblemFiles;
                    fileMap.put(currentDomainFile, pFilesCopy);
                }
                domainFileCounter = currentFileIndex;
                currentProblemFiles = new ArrayList<>();
                currentDomainFile = currentFile[1];
            } else {
                currentProblemFiles.add(currentFile[1]);
            }
        }
        ArrayList<String> pFilesCopy = currentProblemFiles;
        fileMap.put(currentDomainFile, pFilesCopy);

        StringBuilder builder = new StringBuilder();
        /*for (Map.Entry<String, String> m: attributeMap.entrySet()) {
            builder.append(m.getKey() + ": " + m.getValue() + "\n");
        }
        builder.append("files:\n");
        for (Map.Entry<String, ArrayList<String>> e: fileMap.entrySet()) {
            builder.append(e.getKey() + ":\n");
            for (String s: e.getValue()) {
                builder.append(s + "\n");
            }
        }
        sendResponse(request, builder);*/

        // create xml file for this domain and send directory name to client
        builder.append(server.getXmlParser().addXmlDomain(attributeMap, fileMap));
        sendResponse(request, builder);
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
        String requestBody = "";
        String input;

        int contentLength = 0;
        boolean isPostRequest = false;

        if (requestReader != null) {
            while ((input = requestReader.readLine()) != null && !input.equals("")) {
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
                    isPostRequest = true;

                    while ((input = requestReader.readLine()) != null) {
                        if (input.startsWith("content-length")) {
                            contentLength = Integer.valueOf(input.substring(input.indexOf(' ') + 1));
                            break;
                        }
                    }
                    break;
                }
            }

            if (isPostRequest) {
                if (contentLength > 0) {
                    int read;
                    while ((read = requestReader.read()) != -1) {
                        requestBody += (char) read;
                        if (requestBody.length() == contentLength + 21) {
                            break;
                        }
                    }

                    doPost(request, requestBody);
                }
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
