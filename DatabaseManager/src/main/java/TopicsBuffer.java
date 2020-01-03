import java.util.*;
import java.util.logging.Logger;
import java.lang.*;

public class TopicsBuffer
{    
    private List<TopicRabbitmq> _topics;
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private int _index_list;
    private boolean _listNotEmpty;

    public void AddToList(TopicRabbitmq topic)
    {
        _topics.add(_index_list, topic);
        LOGGER.info("Added topic to list");
        _index_list++;
        _listNotEmpty = true;
    }

    public void SortList()
    {
        if(_listNotEmpty)
        {
            LOGGER.fine("List isn't empty, will sort");
            int i = _index_list;
            while(i > 0)
            {
                try
                {
                    TopicRabbitmq local = _topics.get(i);
                    LOGGER.fine(local.GetRoutingKey());
                    i--;
                }
                catch(IndexOutOfBoundsException e)
                {
                    LOGGER.severe("We are out of bounds on topics list: " + e);
                }
                
            }   
        }
    }

    public TopicsBuffer()
    {
        _topics = new ArrayList<TopicRabbitmq>();
        _listNotEmpty = false;
        _index_list = 0;
    }
}
