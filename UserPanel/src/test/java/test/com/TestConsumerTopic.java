package test.com;

import static org.junit.Assert.*;

import org.junit.Test;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.*;
import java.util.Date;
import java.util.logging.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.io.*;
import com.google.gson.Gson;
import java.util.*;
import java.lang.*;
import com.house_guard.user_panel.*;
import com.house_guard.Common.*;

public class TestConsumerTopic
{
    public static Logger getLogger()
    {
        Logger LOGGER = null;
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
        LOGGER = Logger.getLogger(TestConsumerTopic.class.getName());
        return LOGGER;
    }
}
