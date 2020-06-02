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
        assertEquals(Types.ON, "ON");
        assertEquals(Types.OFF, "OFF");
    }
}
