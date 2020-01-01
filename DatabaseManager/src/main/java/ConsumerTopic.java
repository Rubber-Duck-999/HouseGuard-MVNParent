

import com.google.gson.Gson;
import com.house_guard.Common.*;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;




public class ConsumerTopic
{
    private static final String EXCHANGE_NAME = "topics";
    private static ConnectionFactory factory;
    private static Connection connection;
    private static Channel channel;
    private String subscribeQueueName;
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    
    private void eventsTopicSubscribe(String delivery, String routingKey)
    {
    	LOGGER.info("Attempting to split message up by key");
    }

	public void consumeRequired()
    {
        try 
        {
        	Gson gson = new Gson();
            subscribeQueueName = channel.queueDeclare().getQueue();
            //
            channel.queueBind(subscribeQueueName, EXCHANGE_NAME, Types.EVENT_TOPIC_ALL);
            channel.queueBind(subscribeQueueName, EXCHANGE_NAME, Types.REQUEST_DATABASE_TOPIC);
            //
            LOGGER.info("Beginning consumption of topics, please ctrl+c to escape");
            //
            DeliverCallback deliverCallback = (consumerTag, delivery) -> 
            {
	        	String received = new String(delivery.getBody());
                String key = delivery.getEnvelope().getRoutingKey();
                LOGGER.info("Message received, key: " + key);
	        	this.eventsTopicSubscribe(received, key);
	        };
	        channel.basicConsume(subscribeQueueName, true, deliverCallback, consumerTag -> { });
		} 
        catch (IOException e) 
        {
			e.printStackTrace();
		}
    }
    
    
    public ConsumerTopic()
    {
        
        factory = new ConnectionFactory();
        factory.setHost("localhost");
        try 
        {
			connection = factory.newConnection();
			channel = connection.createChannel();
	        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
		} 
        catch (IOException | TimeoutException e) 
        {
			System.out.println("We have had trouble setting up the required connection");
			e.printStackTrace();
		}
    }

}
