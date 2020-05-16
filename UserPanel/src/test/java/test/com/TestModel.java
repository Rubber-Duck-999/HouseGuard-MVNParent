package test.com;

import static org.junit.Assert.*;

import org.junit.Test;

import com.*;

public class TestModel
{

    @Test
    public void testInit()
    {
        Model myModel = new Model();
        String TestBeforePasscode = "1";
        @SuppressWarnings("unused")
        String[] unneeded = myModel.initModel(TestBeforePasscode);
        Integer TestPasscode = myModel.checkPass();
        Integer expected = 1111;
        assertEquals(expected, TestPasscode);
    }

    @Test
    public void testIncrement()
    {
        Model myModel = new Model();
        String testBeforePasscode = "0";
        @SuppressWarnings("unused")
        String[] unneeded = myModel.initModel(testBeforePasscode);
        @SuppressWarnings("unused")
        Integer testPasscode = myModel.checkPass();
        String[] unneeded2 = myModel.setValue("3");
        Integer actual = myModel.checkPass();
        Integer expected = 3000;
        assertEquals(actual, expected);
    }

    @Test
    public void testFail()
    {
        Model myModel = new Model();
        String testBeforePasscode = "9";
        @SuppressWarnings("unused")
        String[] unneeded = myModel.initModel(testBeforePasscode);
        @SuppressWarnings("unused")
        Integer testPasscode = myModel.checkPass();
        String[] unneeded2 = myModel.setValue("8");
        Integer actual = myModel.checkPass();
        Integer expected = 9999;
        assertNotEquals(actual, expected);
    }
}
