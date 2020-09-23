package test.com;

import static org.junit.Assert.*;

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
import com.house_guard.database_manager.*;
import com.house_guard.Common.*;

public class TestConsumerTopic
{
    //@Rule
    //public final ExpectedException exception = ExpectedException.none();

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

    @Test
    public void testConvertTopicsValid()
    {
        String routingKey = "Event.FH";
        String message = "{ 'component': 'FH', 'message': 'Test', 'time': '14/03/2020 12:00:40' }";
        TopicRabbitmq local = new TopicRabbitmq(routingKey, message);
        //
        Logger LOGGER = getLogger();
        TopicsBuffer buffer = new TopicsBuffer(LOGGER, "blank");
        ConsumerTopic consumer = new ConsumerTopic("blank", buffer, LOGGER);
        boolean valid = consumer.ConvertTopics(routingKey, message);
        assertTrue(valid);
    }
/*
    @Test
    public void testRequestDatabase()
    {
        String routingKey = "Request.Database";
        String message = "{ 'request_id': 1, 'time_from': '14:56:00', 'time_to': '16:00:00', 'event_type_id': 'faults' }";
        //
        Logger LOGGER = getLogger();
        TopicsBuffer buffer = new TopicsBuffer(LOGGER, "blank");
        ConsumerTopic consumer = new ConsumerTopic("blank", buffer, LOGGER);
        Gson gson = new Gson();
        Vector<DataInfoTopic> vector = buffer.GetEventData(gson.fromJson(message, RequestDatabase.class));
        assertEquals(vector.size(), 0);
    }

    @Test
    public void DataInfoException()
    {
        Vector<DataInfoTopic> localVector = new Vector<>();
        DataInfoTopic data = new DataInfoTopic();
        data.setId(1);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse("2020/02/01 12:10:10", dtf);
        data.setTimeSent(dateTime.toString());
        localVector.add(data);
        Logger LOGGER = getLogger();
        TopicsBuffer buffer = new TopicsBuffer(LOGGER, "blank");
        ConsumerTopic consumer = new ConsumerTopic("blank", buffer, LOGGER);
        //exception.expect(IOException.class);
        consumer.PublishDataInfo(localVector);
    }*/
}
