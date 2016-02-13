package data;

import global.Settings;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class for every planner used in the system.
 */
public class Planner implements Serializable {

    private String name;
    private File plannerDir;
    private ArrayList<Domain> incompatibleDomains;

    public Planner(String name) {
        this.name = name;
        plannerDir = new File(Settings.PLANNER_DIR_PATH + "/" + name);
        incompatibleDomains = new ArrayList<>();
    }

    public void addIncompatibleDomain(Domain domain) {
        incompatibleDomains.add(domain);
    }

    public ArrayList<Domain> getIncompatibleDomains() {
        return incompatibleDomains;
    }

    public String getPath() {
        return plannerDir.getPath();
    }

    public File getFile() {
        return plannerDir;
    }

    public String getName() {
        return name;
    }
}
