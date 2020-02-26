
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;   

public class TopicRabbitmq
{    
    private String _routingKey;
    private String _message;
    private LocalDateTime _timeOfReceival;
    private boolean _validTopic;
    
    private void SetTime()
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
        _timeOfReceival = LocalDateTime.now(); 
    }
    
    public TopicRabbitmq()
    {
        _routingKey = "";
        _message = "";
        this.SetTime();
        _validTopic = true;
    }

    public TopicRabbitmq(String routingKey, String message)
    {
        _routingKey = routingKey;
        _message = message;
        this.SetTime();
        _validTopic = false;
    }
    
    public boolean GetValidity()
    {
        return _validTopic;
    }
    
    public void SetInValidTopic()
    {
        _validTopic = false;
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
