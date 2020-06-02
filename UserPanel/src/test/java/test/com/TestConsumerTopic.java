package test.com;

import static org.junit.Assert.*;

import org.junit.Test;

import com.*;

public class TestConsumerTopic
{

    @Test
    public void testGetAccessRequested()
    {
        ConsumerTopic topic = new ConsumerTopic();
        assertFalse(topic.getAccessRequested());
    }

    @Test
    public void testSetAccessRequested()
    {
        ConsumerTopic topic = new ConsumerTopic();
        assertFalse(topic.getAccessState());
        topic.setAccessRequestedOff();
        assertFalse(topic.getAccessRequested());
    }
}
