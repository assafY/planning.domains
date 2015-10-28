package server;

import java.io.File;

/**
 * Wrapper class containing an XmlDomain and its directory
 */

public class Domain {

    private File domainDir;
    private XmlDomain domain;

    public Domain(File domainDir, XmlDomain domain) {
        this.domainDir = domainDir;
        this.domain = domain;
    }

    public File getFile() {
        return domainDir;
    }

    public String getPath() {
        return domainDir.getPath();
    }

    public XmlDomain getDomain() {
        return domain;
    }

    @Override
    public String toString() {
        return domain.getDomain().getTitle();
    }
}
