package util;

public class PlatformUtils {

    public static final String OS_NAME = getSystemProperty("os.name");

    public static final String USER_HOME = getSystemProperty("user.home");

    public static final String USER_DIR = getSystemProperty("user.dir");

    public static final boolean IS_OS_LINUX =  getOS("nux");
    public static final boolean IS_OS_MAC =  getOS("mac") || getOS("darwin");
    public static final boolean IS_OS_WIN =  getOS("win");

    private static String getSystemProperty(String osName) { return System.getProperty(osName); }

    private static boolean getOS(String os) {
        if(OS_NAME == null){ return false; }
        return OS_NAME.toLowerCase().contains(os);
    }
}
