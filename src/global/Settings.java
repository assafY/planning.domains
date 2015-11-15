package global;

/**
 * Collection of constants
 */
public class Settings {

    //server
    public static final String USER_NAME = "k1333702";
    public static final String HOST_NAME = "calcium.inf.kcl.ac.uk";
    public static final int PORT_NUMBER = 8080;

    // resources
    public static final String NODE_LIST_PATH = "res/node_list.txt";
    public static final String PLANNER_LIST_PATH = "res/planner_list.txt";
    public static final String PLANNER_DIR_PATH = "res/planners/";
    public static final String DOMAIN_DIR_PATH = "res/domains/";
    public static final String LOCAL_RESULT_DIR = "res/results/";
    public static final String REMOTE_RESULT_DIR = "~/planning_domains/res/results/";

    // scripts
    public static final String NODE_START_SCRIPT = "res/start_all_nodes.sh";
    public static final String RESULT_COPY_SCRIPT = "res/copy_result_files.sh";
    public static final String RESULT_DEL_SCRIPT = "res/del_result_files";
    public static final String RUN_PLANNER_SCRIPT = "res/run_planner.sh";
}
