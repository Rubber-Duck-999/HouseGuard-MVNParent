package com.house_guard.database_manager;

import java.util.Date;
import java.util.logging.*;
import java.io.*;  
import java.util.*;  

public class mainClass {
    private static ConsumerTopic cons;
    private static Logger LOGGER = null;

    static {
        Logger mainLogger = Logger.getLogger("com.logicbig");
        mainLogger.setUseParentHandlers(false);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter() {
            private static final String format = "[%1$tF %1$tT] [%2$-7s] %3$s %n";

            @Override
            public synchronized String format(LogRecord lr) {
                return String.format(format,
                                     new Date(lr.getMillis()),
                                     lr.getLevel().getLocalizedName(),
                                     lr.getMessage()
                                    );
            }
        });
        mainLogger.addHandler(handler);
        LOGGER = Logger.getLogger(mainClass.class.getName());
    }

    public static void start(String password) {
        LOGGER.setLevel(Level.FINEST);
        TopicsBuffer buffer = new TopicsBuffer(LOGGER, password);
        cons = new ConsumerTopic(buffer, LOGGER);
        cons.ConsumeRequired();
    }

    public static void main(String[] argv) throws Exception {
        Properties prop = new Properties();
        String fileName = "app.config";
        InputStream is = null;
        try {
            is = new FileInputStream(fileName);
        } catch (FileNotFoundException ex) {
            System.out.println("File failed");
        }
        try {
            prop.load(is);
        } catch (IOException ex) {
            System.out.println("File failed");
        }
        String password = prop.getProperty("password");
        start(password);
    }
}
