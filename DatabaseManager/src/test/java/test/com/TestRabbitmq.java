package test.com;

import static org.junit.Assert.*;

import org.junit.Test;

import com.house_guard.database_manager.*;
import com.house_guard.Common.*;

public class TestRabbitmq
{

    @Test
    public void testGetValidity()
    {
        TopicRabbitmq topic = new TopicRabbitmq();
        assertTrue(topic.GetValidity());
    }
    
    @Test
    public void testSetInValidTopic()
    {
        TopicRabbitmq topic = new TopicRabbitmq();
        topic.SetInValidTopic();
        assertFalse(topic.GetValidity());
    }

}
