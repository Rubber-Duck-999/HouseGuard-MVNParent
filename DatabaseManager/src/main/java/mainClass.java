package com.house_guard.database_manager;

import java.util.Date;
import java.util.logging.*;
import java.io.*;  
import java.util.*;  


import java.io.File;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

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

    public static void start(String password, String rabbitmq) {
        LOGGER.setLevel(Level.FINEST);
        TopicsBuffer buffer = new TopicsBuffer(LOGGER, password);
        cons = new ConsumerTopic(rabbitmq, buffer, LOGGER);
        cons.ConsumeRequired();
    }

    public static void main(String[] argv) throws Exception {
        // Get environment variable

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try 
        {
            Password strings = mapper.readValue(new File("DBM.yml"), Password.class);
            start(strings.getSQL(), strings.getRabbitmq());
        } 
        catch (Exception e) 
        {
            System.out.println("File is not there, drop out");
            e.printStackTrace();
        }
    }
}
