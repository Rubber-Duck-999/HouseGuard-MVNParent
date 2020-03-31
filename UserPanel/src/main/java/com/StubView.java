package com;

import java.awt.event.ActionListener;

public class StubView extends View
{

    public StubView()
    {

    }

    @Override
    public < E > void setDigits(String[] v)
    {

    }

    @Override
    public void addController(ActionListener listenerButtons)
    {
        System.out.println("addController call");
    }

    @Override
    public void displayErrorMessage(String message)
    {

    }

    @Override
    public void displayPassMessage(String message)
    {

    }

}
