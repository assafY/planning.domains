package server;

import global.Settings;

import java.io.File;

/**
 * Class for every planner used in the system.
 */
public class Planner {

    private String name;
    private File plannerDir;

    public Planner(String name) {
        this.name = name;
        plannerDir = new File(Settings.PLANNER_DIR_PATH + "/" + name);
    }

    public String getPath() {
        return plannerDir.getPath();
    }

    public File getFile() {
        return plannerDir;
    }
}
