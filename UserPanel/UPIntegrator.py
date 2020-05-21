'''
Created on 10 Oct 2019

@author: simon
'''

#!/usr/bin/env python
import pika
import sys, time, json

###
# User Panel Simulator Interface 
# This is to show how the DBM could manager
# its necessary pub & sub topics with rabbitmq

## Setup connection and exchange
credentials = pika.PlainCredentials('guest', 'password')
connection = pika.BlockingConnection(pika.ConnectionParameters('localhost', 5672, '/', credentials))
channel = connection.channel()
channel.exchange_declare(exchange='topics', exchange_type='topic', durable=True)
#

# Specific settings for subscribe key
print("## Beginning UP Integrator")
result = channel.queue_declare('', exclusive=False, durable=True)
queue_name = result.method.queue
topic_monitor = "Monitor.State"
topic_request_access = "Request.Access"
topic_response = "Access.Response"
topic_event = "Event.UP"
topic_status_up = "Status.UP"
topic_request = "Status.Request.UP"
channel.queue_bind(exchange='topics', queue=queue_name, routing_key=topic_status_up)
channel.queue_bind(exchange='topics', queue=queue_name, routing_key=topic_request_access)
print(' UP Integrator [*] Waiting for topics. To exit press CTRL+C')

time.sleep(0.5)
#
channel.basic_publish(exchange='topics', routing_key=topic_request, body='')
#print("Sent %r " % topic_data_request)

id = 1
def callback(ch, method, properties, body):
    print(" DBM received an event [x] %r:%r" % (method.routing_key, body))
    if method.routing_key == topic_request_access:
        ##
        global id
        data = {
            "id": str(id), 
            "result":"PASS" 
        }
        id = id + 1
        payload = json.dumps(data)
        print(payload)
        channel.basic_publish(exchange='topics', routing_key=topic_response, body=payload)
        print("Sent %r " % topic_response)


channel.basic_consume(queue=queue_name, on_message_callback=callback, auto_ack=True)

channel.start_consuming()
connection.close()
