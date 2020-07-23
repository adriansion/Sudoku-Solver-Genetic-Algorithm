package main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Public log4j2 logger object for use anywhere in program.
 */
public class Log {
    public static final Logger logger = LogManager.getLogger("Main");

    public static final Logger getLogger() {
        return logger;
    }
}
