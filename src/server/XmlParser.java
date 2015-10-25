package server;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class XmlParser {

    public Domain getDomain(String domainPath) {

        try {

            File domainMetadata = new File(domainPath+"/metadata.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Domain.class);

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Domain domain = (Domain) unmarshaller.unmarshal(domainMetadata);

            return domain;

        } catch (JAXBException e) {
            //TODO: handle exception
            e.printStackTrace();
        }

        return null;
    }
}
