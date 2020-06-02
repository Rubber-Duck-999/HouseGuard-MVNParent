package com;

import java.util.logging.Logger;
import com.house_guard.Common.*;

import java.io.File;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class mainClass
{
    private static Model myModel;
    private static View myView;
    private static Controller myController;
    private static ConsumerTopic cons;

    public static void startUI(String password)
    {
        myView = new View();

        MonitorView monitorView = new MonitorView();
        cons = new ConsumerTopic();
        cons.setConnection(password);
        myController = new Controller(myView, monitorView, cons, new RequestTable());
        myController.initmodel(Types.EMPTY, Types.ON);
        myView.addController(myController);
        monitorView.addController(myController);
        cons.consumeRequired();
    }

    public static void main(String[] argv) throws Exception
    {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try
        {
            Password strings = mapper.readValue(new File("UP.yml"), Password.class);
            startUI(strings.getRabbitmq());
        }
        catch (Exception e)
        {
            System.out.println("File is not there, drop out");
            e.printStackTrace();
        }
    }
}
