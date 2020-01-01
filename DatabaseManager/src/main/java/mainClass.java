import java.util.logging.Level;
import java.util.logging.Logger;

public class mainClass
{
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static ConsumerTopic cons;

    public static void start()
    {
        LOGGER.setLevel(Level.INFO);
        cons = new ConsumerTopic();
        cons.consumeRequired();
    }

    public static void main(String[] argv) throws Exception
    {
        start();
    }
}
