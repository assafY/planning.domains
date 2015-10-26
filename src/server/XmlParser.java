package server;

import global.Settings;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class XmlParser {

    public XmlDomain getDomain(String domainName) {

        try {

            File domainMetadata = new File(Settings.DOMAIN_DIR_PATH + domainName + "/metadata.xml");
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
