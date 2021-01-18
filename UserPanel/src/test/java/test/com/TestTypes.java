package test.com;

import static org.junit.Assert.*;

import org.junit.Test;

import com.house_guard.user_panel.*;
import com.house_guard.user_panel.*;

public class TestTypes
{
    @Test
    public void testActions()
    {
        assertEquals(Types.ON, "ON");
        assertEquals(Types.OFF, "OFF");
    }
}
