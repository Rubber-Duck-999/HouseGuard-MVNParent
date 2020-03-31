package test.com;

import static org.junit.Assert.*;

import org.junit.Test;

import com.*;
import com.house_guard.Common.*;

public class TestTypes
{
    @Test
    public void testActions()
    {
        assertEquals(Types.State.ON.name(), "ON");
        assertEquals(Types.State.OFF.name(), "OFF");
    }
}
