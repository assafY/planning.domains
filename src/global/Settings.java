package global;

/**
 * Collection of constants
 */
public class Settings {

    // server
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
    public static final String VAL_FILES_DIR = "res/validation/";

    // serialization
    public static final String SERIALIZATION_DIR = "res/saved_files/";
    public static final String DOMAINLIST_FILE = "domainList.ser";
    public static final String PLANNERLIST_FILE = "plannerList.ser";

    // scripts
    // the majority of scripts are environment specific
    // and are likely to require modification if environment
    // is changed
    public static final String NODE_START_SCRIPT = "res/scripts/start_all_nodes.sh";
    public static final String SINGLE_NODE_START_SCRIPT = "res/scripts/start_node.sh";
    public static final String RSA_COPY_SCIPRT = "res/scripts/copy_rsa.sh";
    public static final String RESULT_COPY_SCRIPT = "res/scripts/copy_result_files.sh";
    public static final String RESULT_DEL_SCRIPT = "res/scripts/del_result_files.sh";
    public static final String LAMA_DEL_SCRIPT = "res/scripts/del_lama_files.sh";
    public static final String RUN_PLANNER_SCRIPT = "res/scripts/run_planner.sh";
    public static final String KILL_LAMA_PROCESS_SCRIPT = "res/scripts/get_lama_pid.sh";
    public static final String RUN_VALIDATION_SCRIPT = "res/scripts/validate_plan.sh";
    public static final String RUN_LAMA_TRANSLATE = "../res/planners/seq-sat-lama-2011/translate";
    public static final String RUN_LAMA_PREPROCESS = "../res/planners/seq-sat-lama-2011/preprocess";

    // ansi colour codes
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
}
