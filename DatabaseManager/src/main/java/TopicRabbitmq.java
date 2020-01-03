

public class TopicRabbitmq
{    
    private String _routingKey;
    private String _message;
    public TopicRabbitmq()
    {
        _routingKey = "";
        _message = "";
    }

    public TopicRabbitmq(String routingKey, String message)
    {
        _routingKey = routingKey;
        _message = message;
    }

    public String GetMessage()
    {
        return _message;
    }

    public String GetRoutingKey()
    {
        return _routingKey;
    }
}
