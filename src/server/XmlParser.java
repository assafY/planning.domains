package server;

import global.Settings;

import java.io.File;
import java.util.Arrays;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class XmlParser {

    public Domain getDomain(String domainName) {

        File domainRoot = new File(Settings.DOMAIN_DIR_PATH + domainName);
        String[] childFiles = domainRoot.list();

        Domain domain = new Domain(domainName);

        if (Arrays.asList(childFiles).contains("metadata.xml")) {
            domain.addDomain("", unmarshal(domainRoot.getPath()));
            return domain;
        }

        for (String child: childFiles) {
            File childFile = new File(domainRoot.getPath() + "/" + child);
            if (childFile.isDirectory()) {
                domain.addDomain(child, unmarshal(childFile.getPath()));
            }
        }

        if (domain.getSize() > 0) {
            return domain;
        } else {
            return null;
        }
    }

    private XmlDomain unmarshal(String path) {

        try {
            File domainMetadata = new File(path + "/metadata.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(XmlDomain.class);

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            XmlDomain domain = (XmlDomain) unmarshaller.unmarshal(domainMetadata);

            return domain;

        } catch (JAXBException e) {
            //TODO: handle exception
            e.printStackTrace();
        }

        return null;
    }
}
