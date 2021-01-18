package test.com;

import static org.junit.Assert.*;

import org.junit.Test;

import com.house_guard.user_panel.*;
import com.house_guard.user_panel.*;

public class TestJsonTypes
{

    @Test
    public void testGetId()
    {
        AccessResponse access = new AccessResponse();
        Integer id = 100;
        access.setId(id);
        assertEquals(id, access.getId());
    }

    @Test
    public void testGetResult()
    {
        AccessResponse access = new AccessResponse();
        String result = "FAIL";
        access.setResult(result);
        assertEquals(result, access.getResult());
    }

    @Test
    public void testGetErrorChecks()
    {
        EventTopic up = new EventTopic();
        String component = "FAIL";
        up.setComponent(component);
        assertEquals(component, up.getComponent());
        //
        String event = "UP1";
        up.setEventTypeId(event);
        assertEquals(event, up.getEventTypeId());
        //
        String time = "14:00";
        up.setTime(time);
        assertEquals(time, up.getTime());
    }

    @Test
    public void testMonitorState()
    {
        MonitorState monitor_state = new MonitorState();
        monitor_state.setState(false);
        assertFalse(monitor_state.isState());
        //
        monitor_state.setState(true);
        assertTrue(monitor_state.isState());
    }
}
