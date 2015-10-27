package server;

import java.util.HashMap;

/**
 * Wrapper class containing a list of one or more
 * formulations of a single domain.
 */

public class Domain {

    private String name;
    private HashMap<String,XmlDomain> domainMap;

    public Domain(String name) {
        this.name = name;
        domainMap = new HashMap<>();
    }

    public void addDomain(String name, XmlDomain domain) {
        domainMap.put(name, domain);
    }

    public HashMap<String, XmlDomain> getDomainList() {
        return domainMap;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return domainMap.size();
    }
}
