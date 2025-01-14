package pl.edu.agh.to2.cleaner.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingExample {
    private static final Logger logger = LoggerFactory.getLogger(LoggingExample.class);

    public static void main(String[] args) {
        logger.info("Normal message.");
        logger.warn("Warning message.");
        logger.error("Error message.");
    }
}
