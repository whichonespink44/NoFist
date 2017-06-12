package nofist.api;


import nofist.api.config.NFConfig;

public class NoFistAPI {

    public static final String NAME = "nofistapi";
    public static final String OWNER = "nofist";
    public static final String VERSION = "1.0.0";
    public static NFConfig nfConfig;
    public static String configPath;

    /*
     * This method is currently unused, but we're leaving it here for when we start
     * supporting multiple dimensions.
     */
    public static NFConfig config(int dimension) {
        return nfConfig;
    }

    public static NFConfig config() {
        return config(0);
    }
}
