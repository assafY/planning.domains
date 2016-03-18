package server;

import com.sun.xml.internal.bind.marshaller.NamespacePrefixMapper;
import data.Domain;
import data.XmlDomain;
import global.Settings;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class XmlParser {

    private ArrayList<Domain> domainList;

    /**
     * Instantiates local domain list, and calls method that iterates
     * over all directories under the root domain directory, importing
     * XML files found to XmlDomain objects, and adds them to the list.
     *
     * @return list of all domains imported
     */
    public ArrayList<Domain> getDomainList() {
        domainList = new ArrayList<>();
        createXmlDomains(Settings.DOMAIN_DIR_PATH);
        Collections.sort(domainList);
        return domainList;
    }

    /**
     * Recursive method which finds all 'metadata.xml' files
     * under a given directory, imports them as XmlDomain objects,
     * and adds them to the domain list.
     *
     * @param path the path being searched for xml files
     */
    private void createXmlDomains(String path) {
        File file = new File(path);
        String[] childFiles = file.list();

        if (Arrays.asList(childFiles).contains("metadata.xml")) {
            domainList.add(new Domain(file, unmarshal(file.getPath())));
            return;
        }

        for (String child: childFiles) {
            File childFile = new File(file.getPath() + "/" + child);
            if (childFile.isDirectory()) {
                createXmlDomains(childFile.getPath());
            }
        }
    }

    /**
     * Unmarshals a metadata.xml files and returns an object
     * created from the file.
     *
     * @param path the path of the directory containing the XML file
     * @return XmlDomain object created from that file
     */
    private XmlDomain unmarshal(String path) {

        try {
            File domainMetadata = new File(path + "/metadata.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(XmlDomain.class);

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            XmlDomain domain = (XmlDomain) unmarshaller.unmarshal(domainMetadata);

            domain.setXmlFile(domainMetadata);
            return domain;

        } catch (JAXBException e) {
            //TODO: handle exception
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Creates a new XML file for an uploaded domain
     *
     * @param attributeMap all domain attributes apart from file names
     * @param fileMap map object of domain names and corresponding file lists
     */
    public String addXmlDomain(Map<String, String> attributeMap, Map<String, ArrayList<String>> fileMap) {
        XmlDomain newXmlDomain = new XmlDomain();
        newXmlDomain.setDomain(new XmlDomain.Domain());

        // capitalize first letter of domain name
        String domainName = attributeMap.remove("name").toLowerCase();
        String formulation = attributeMap.remove("formulation").toLowerCase();
        String ipc = attributeMap.remove("ipc"); // can be null as ipc year is optional
        String link = attributeMap.remove("link"); // can be null as link is optional
        String publishDate = attributeMap.remove("publishDate");
        String complexityText = attributeMap.remove("properties[complexityText]"); // can be null

        // build domain id and title from form data
        String domainId = "planning.domains:";
        String title = "The " + domainName + " domain from the " + formulation + " track";

        // if an IPC year was submitted
        if (ipc != null) {
            domainId += "ipc" + ipc + "/";
            title += " IPC" + ipc;
        }
        domainId += domainName + "/" + formulation;

        newXmlDomain.getDomain().setId(domainId);
        newXmlDomain.getDomain().setTitle(title);

        // set the link
        if (link != null) {
            if (!link.contains("http")) {
                link = "http://" + link;
            }
            newXmlDomain.getDomain().setLink(link);
        }

        // set the required dates for this domain
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        publishDate = publishDate.substring(0, publishDate.indexOf('T') + 1) + "12:00:00";
        String currentDate = sdf.format(new Date());

        try {
            newXmlDomain.getDomain().setMetadata_last_modified(sdf.parse(currentDate));
            newXmlDomain.getDomain().setFiles_last_modified(sdf.parse(currentDate));
            newXmlDomain.getDomain().setPublished(sdf.parse(publishDate));
        } catch (ParseException e) {
            //TODO: handle exception
        }

        // at this point all that should remain in the attribute
        // map are requirements and properties
        ArrayList<String> requirements = new ArrayList<>();
        ArrayList<String> properties = new ArrayList<>();
        for (Map.Entry<String, String> m : attributeMap.entrySet()) {
            String currentKey = m.getKey();
            if (currentKey.startsWith("req")) {
                if (m.getValue().equals("true")) {
                    requirements.add(currentKey.substring(currentKey.indexOf('[') + 1, currentKey.indexOf(']')));
                }
            } else if (currentKey.startsWith("pro")) {
                if (m.getValue().equals("true")) {
                    properties.add(currentKey.substring(currentKey.indexOf('[') + 1, currentKey.indexOf(']')));
                }
            }
        }

        // use Java reflection to set requirements
        newXmlDomain.getDomain().setRequirements((XmlDomain.Domain.Requirements)
                                                    xmlDomainReflection(true, requirements, null));

        // repeat for properties is there are any
        if (properties.size() > 0) {
            newXmlDomain.getDomain().setProperties((XmlDomain.Domain.Properties)
                    xmlDomainReflection(false, properties, complexityText));
        }

        // set all domain and problem files
        ArrayList<XmlDomain.Domain.Problems.Problem> problemList = new ArrayList<>();
        int problemCounter = 1;

        for (Map.Entry<String, ArrayList<String>> currentEntry: fileMap.entrySet()) {
            String currentDomainFile = currentEntry.getKey();
            for (String problemFile: currentEntry.getValue()) {
                XmlDomain.Domain.Problems.Problem currentProblem = new XmlDomain.Domain.Problems.Problem();
                currentProblem.setDomain_file(currentDomainFile);
                currentProblem.setProblem_file(problemFile);
                currentProblem.setNumber(problemCounter);

                problemList.add(currentProblem);
                ++problemCounter;
            }
        }

        XmlDomain.Domain.Problems problems = new XmlDomain.Domain.Problems();
        problems.setProblem(problemList);
        newXmlDomain.getDomain().setProblems(problems);

        return marshal(newXmlDomain);
    }

    private Object xmlDomainReflection(boolean isRequirements, ArrayList<String> list,
                                       String complexityText) {
        XmlDomain.Domain.Requirements domainRequirements = new XmlDomain.Domain.Requirements();
        XmlDomain.Domain.Properties domainProperties = new XmlDomain.Domain.Properties();

        for (String s: list) {
            String methodName = "";
            if (!s.contains("_")) {
                methodName = "set" + s.substring(0, 1).toUpperCase() + s.substring(1);
            } else {
                methodName = "set" + s.substring(0, 1).toUpperCase() + s.substring(1, s.indexOf("_"));
            }
            Method method = null;
            try {
                if (isRequirements) {
                    method = domainRequirements.getClass().getMethod(methodName, String.class);
                } else {
                    method = domainProperties.getClass().getMethod(methodName, String.class);
                }
            } catch (NoSuchMethodException e) {
                //TODO: handle exception
                e.printStackTrace();
            }
            if (method != null) {
                try {
                    if (isRequirements) {
                        method.invoke(domainRequirements, s);
                    } else if (s.equals("complexity")) {
                        if (complexityText != null) {
                            method.invoke(domainProperties, complexityText);
                        } else {
                            method.invoke(domainProperties, "");
                        }
                    } else {
                        method.invoke(domainProperties, s);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        if (isRequirements) {
            return domainRequirements;
        }

        return domainProperties;
    }

    public static String marshal(XmlDomain domain) {

        // create a new directory to store the file
        File newDomainDir = new File(Settings.DOMAIN_DIR_PATH + "uploads/" + domain.getDomain().getShortId());
        int counter = 0;

        // if the directory exists append a number and check again
        while (newDomainDir.exists()) {
            newDomainDir = new File(Settings.DOMAIN_DIR_PATH + "uploads/" +
                    domain.getDomain().getShortId() +  ++counter);
        }

        newDomainDir.mkdir();

        try {

            File newXmlFile = new File(newDomainDir.getPath() + "/metadata.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(XmlDomain.class);
            Marshaller marshaller = jaxbContext.createMarshaller();

            // create mapper to change default namespace to 'planning'
            // this internal package must be compiled using '-XDignore.symbol.file'
            NamespacePrefixMapper mapper = new NamespacePrefixMapper() {
                @Override
                public String getPreferredPrefix(String s, String s1, boolean b) {
                    return "planning";
                }
            };

            marshaller.setProperty("com.sun.xml.internal.bind.namespacePrefixMapper", mapper);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(domain, newXmlFile);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return newDomainDir.getName();
    }
}
