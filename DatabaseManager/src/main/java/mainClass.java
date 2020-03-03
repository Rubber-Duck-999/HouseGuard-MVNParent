package com.house_guard.database_manager;

import java.util.Date;
import java.util.logging.*;

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

    public static void start() {
        LOGGER.setLevel(Level.FINEST);
        TopicsBuffer buffer = new TopicsBuffer(LOGGER);
        cons = new ConsumerTopic(buffer, LOGGER);
        cons.ConsumeRequired();
    }

    public static void main(String[] argv) throws Exception {
        start();
    }
}
