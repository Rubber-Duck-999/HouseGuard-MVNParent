package com.house_guard.internal_system_controller;

import java.util.logging.Logger;

import java.util.logging.*;
import java.io.*;
import java.util.*;

import java.io.File;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class mainClass
{

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

    public static void startUI()
    {
        LOGGER.setLevel(Level.FINEST);
        View view = new View();
        Menu menu = new Menu();
        Logs logs = new Logs();
        Users users = new Users();
        Devices devices = new Devices();
        Settings settings = new Settings();
        Controller controller = new Controller(LOGGER, view, menu, logs, users, devices, settings);
        view.addController(controller);
        menu.addController(controller);
        logs.addController(controller);
        users.addController(controller);
        devices.addController(controller);
        settings.addController(controller);
    }

    public static void main(String[] argv) throws Exception
    {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try
        {
            startUI();
        }
        catch (Exception e)
        {
            System.out.println("File is not there, drop out");
            e.printStackTrace();
        }
    }
}
