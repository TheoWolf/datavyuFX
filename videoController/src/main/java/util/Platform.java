package util;

public enum Platform {
    MAC,
    WINDOWS,
    LINUX,
    UNKNOWN;

    public static final Platform current;

    static {
        String os = System.getProperty("os.name", "generic").toLowerCase();

        if ((os.contains("mac")) || (os.contains("darwin")))
            current = MAC;
        else if (os.contains("win"))
            current = WINDOWS;
        else if (os.contains("nux"))
            current = LINUX;
        else
            current = UNKNOWN;
    }
}
