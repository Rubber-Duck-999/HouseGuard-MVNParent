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
    public void testTopicConversion()
    {
        String message = "{ 'component': 'EVM', 'message': 'weather server down', " +
                         "'time': '2020/01/20 12:00:00', 'event_type_id': 'EVM3' }";
        TopicRabbitmq topic = new TopicRabbitmq(Types.EVENT_TOPIC_EVM, message);
        assertTrue(topic.convertMessage());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        assertEquals(topic.getTimeSent(), LocalDateTime.parse("2020/01/20 12:00:00", dtf));
        assertEquals(topic.getComponent(), "EVM");
        assertEquals(topic.getTopicMessage(), "weather server down");
    }

}
