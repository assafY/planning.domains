package server;

import global.Settings;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
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
}
