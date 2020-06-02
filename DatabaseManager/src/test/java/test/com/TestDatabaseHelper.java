package test.com;

import static org.junit.Assert.*;

import org.junit.Test;

import com.house_guard.database_manager.*;
import com.house_guard.Common.*;
import java.util.logging.*;
import java.io.File;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class TestDatabaseHelper
{

    @Test
    public void testGetConnection()
    {
        Logger LOGGER = TestConsumerTopic.getLogger();
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            Password strings = mapper.readValue(new File("DBM.yml"), Password.class);
            DatabaseHelper db = new DatabaseHelper(LOGGER, strings.getSQL());
            int count = db.getTotalComponentCount("UP");
            assertNotEquals(count, 1);
        } catch (Exception e) {
            System.out.println("File is not there, drop out");
            e.printStackTrace();
        }
    }

    @Test
    public void testFindDevice()
    {
        Logger LOGGER = TestConsumerTopic.getLogger();
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            Password strings = mapper.readValue(new File("DBM.yml"), Password.class);
            DatabaseHelper db = new DatabaseHelper(LOGGER, strings.getSQL());
            DeviceRequest device = new DeviceRequest();
            device.setId(1);
            device.setName("Iphone");
            device.setMac("00-00-00-00-00-00");
            DeviceResponse response = db.getDevice(device.getId(), device.getName(),
                                                   device.getMac());
            assertEquals(device.getName(), response.getName());
            assertEquals(response.getStatus(), "BLOCKED");
        } catch (Exception e) {
            System.out.println("File is not there, drop out");
            e.printStackTrace();
        }
    }

}
