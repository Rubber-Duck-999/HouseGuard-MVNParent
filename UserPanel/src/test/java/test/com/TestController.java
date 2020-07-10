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
import com.house_guard.user_panel.*;
import com.house_guard.Common.*;

public class TestController {
    @Test
    public void checkUnlock()
    {
        Model myModel = new Model();
        assertFalse(myModel.checkAttempts());
        assertFalse(myModel.checkAttempts());
        assertTrue(myModel.checkAttempts());
        assertTrue(myModel.checkUnlock());
    }
}