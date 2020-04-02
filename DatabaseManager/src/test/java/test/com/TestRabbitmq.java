package test.com;

import static org.junit.Assert.*;

import org.junit.Test;

import com.house_guard.database_manager.*;
import com.house_guard.Common.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class TestRabbitmq
{

    @Test
    public void testGetValidity()
    {
        String message = "{ 'component': 'EVM', 'message': 'weather server down', " +
                         "'time': '2020/01/20 12:00:00', 'severity': 2 }";
        TopicRabbitmq topic = new TopicRabbitmq(Types.EVENT_TOPIC_EVM, message);
        assertFalse(topic.getValidity());
    }

    @Test
    public void testSetValidTopic()
    {
        String message = "{ 'component': 'EVM', 'message': 'weather server down', " +
                         "'time': '2020/01/20 12:00:00', 'severity': 2 }";
        TopicRabbitmq topic = new TopicRabbitmq(Types.EVENT_TOPIC_EVM, message);
        topic.setValidTopic();
        assertTrue(topic.getValidity());
    }

    @Test
    public void testTopicConversion()
    {
        String message = "{ 'component': 'EVM', 'message': 'weather server down', " +
                         "'time': '2020/01/20 12:00:00', 'severity': 2 }";
        TopicRabbitmq topic = new TopicRabbitmq(Types.EVENT_TOPIC_EVM, message);
        topic.setValidTopic();
        topic.convertMessage();
        assertTrue(topic.getValidity());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        assertEquals(topic.getTimeSent(), LocalDateTime.parse("2020/01/20 12:00:00", dtf));
        assertEquals(topic.getComponent(), "EVM");
        assertEquals(topic.getTopicMessage(), "weather server down");
    }

}
