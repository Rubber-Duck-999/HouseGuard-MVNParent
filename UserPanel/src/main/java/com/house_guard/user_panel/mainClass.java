package com.house_guard.user_panel;

import java.util.logging.Logger;
import java.util.logging.*;
import java.io.*;
import java.util.*;
import com.house_guard.user_panel.*;

import java.io.File;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class mainClass
{
    private static View myView;
    private static Controller myController;
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

    public static void startUI(String password, String pass)
    {
        LOGGER.setLevel(Level.FINEST);
        myView = new View();

        MonitorView monitorView = new MonitorView();
        cons = new ConsumerTopic(LOGGER);
        cons.setConnection(password);
        myController = new Controller(LOGGER, myView, monitorView, cons, pass);
        myController.initmodel(Types.ZERO, Types.ON);
        myView.addController(myController);
        monitorView.addController(myController);
        cons.consumeRequired();
    }

    public static void main(String[] argv) throws Exception
    {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try
        {
            Password strings = mapper.readValue(new File("UP.yml"), Password.class);
            startUI(strings.getRabbitmq(), strings.getPass());
        }
        catch (Exception e)
        {
            System.out.println("File is not there, drop out");
            e.printStackTrace();
        }
    }
}
