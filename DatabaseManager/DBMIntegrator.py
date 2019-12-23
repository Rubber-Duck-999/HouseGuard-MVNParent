'''
Created on 10 Oct 2019

@author: simon
'''

#!/usr/bin/env python
import pika
import sys, time

###
# Database Manager Simulator Interface 
# This is to show how the DBM could manager
# its necessary pub & sub topics with rabbitmq

## Setup connection and exchange
connection = pika.BlockingConnection(pika.ConnectionParameters(host='localhost'))
channel = connection.channel()
channel.exchange_declare(exchange='topics', exchange_type='topic', durable=True)
#

# Specific settings for subscribe key
print("## Beginning DBM Component")
result = channel.queue_declare('', exclusive=False, durable=True)
queue_name = result.method.queue
topic_failure = 'Failure.Database'
topic_data_request = "Request.Database"
topic_data_info = "Data.Info"
topic_event = "Event.*"
channel.queue_bind(exchange='topics', queue=queue_name, routing_key=topic_failure)
channel.queue_bind(exchange='topics', queue=queue_name, routing_key=topic_data_info)
print(' DBM Integrator [*] Waiting for topics. To exit press CTRL+C')

while True:
    time.sleep(2)
    routing_key = 'Event.SYP'
    channel.basic_publish(exchange='topics', routing_key=routing_key, body='failure:SYP')
    time.sleep(2)
    channel.basic_publish(exchange='topics', routing_key=topic_data_request, body='Now')


def callback(ch, method, properties, body):
    print(" DBM received an event [x] %r:%r" % (method.routing_key, body))
    time.sleep(2)
    routing_key = 'Event.SYP'
    channel.basic_publish(exchange='topics', routing_key=routing_key, body='failure:SYP')
    time.sleep(2)
    channel.basic_publish(exchange='topics', routing_key=topic_data_request, body='Now')

channel.basic_consume(queue=queue_name, on_message_callback=callback, auto_ack=True)

channel.start_consuming()
connection.close()
