package server;

import java.io.File;
import java.io.Serializable;

/**
 * Wrapper class containing an XmlDomain and its directory
 */
public class Domain implements Serializable, Comparable<Domain> {

    private File domainDir;
    private XmlDomain xmlDomain;

    public Domain(File domainDir, XmlDomain domain) {
        this.domainDir = domainDir;
        this.xmlDomain = domain;
    }

    public File getFile() {
        return domainDir;
    }

    public String getPath() {
        return domainDir.getPath();
    }

    public XmlDomain getXmlDomain() {
        return xmlDomain;
    }

    @Override
    public int compareTo(Domain d) {
        return d.getXmlDomain().getDomain().getShortId().compareTo(this.getXmlDomain().getDomain().getShortId());
    }

    @Override
    public String toString() {
        return xmlDomain.getDomain().getTitle();
    }
}
