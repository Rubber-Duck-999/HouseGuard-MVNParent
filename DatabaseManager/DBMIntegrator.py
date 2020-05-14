'''
Created on 10 Oct 2019

@author: simon
'''

#!/usr/bin/env python
import pika
import sys, time, json

###
# Database Manager Simulator Interface 
# This is to show how the DBM could manager
# its necessary pub & sub topics with rabbitmq

## Setup connection and exchange
credentials = pika.PlainCredentials('guest', 'password')
connection = pika.BlockingConnection(pika.ConnectionParameters('localhost', 5672, '/', credentials))
channel = connection.channel()
channel.exchange_declare(exchange='topics', exchange_type='topic', durable=True)
#

# Specific settings for subscribe key
print("## Beginning DBM Component")
result = channel.queue_declare('', exclusive=False, durable=True)
queue_name = result.method.queue
topic_failure = 'Failure.Database'
topic_data_request = "Request.Database"
topic_device_request = "Device.Request"
topic_device_response = "Device.Response"
topic_data_info = "Data.Info"
topic_event = "Event.*"
channel.queue_bind(exchange='topics', queue=queue_name, routing_key=topic_failure)
channel.queue_bind(exchange='topics', queue=queue_name, routing_key=topic_data_info)
channel.queue_bind(exchange='topics', queue=queue_name, routing_key=topic_device_response)
print(' DBM Integrator [*] Waiting for topics. To exit press CTRL+C')

time.sleep(2)
routing_key = 'Event.SYP'
event = {
    "component": "SYP", 
    "message": "Failure to kill UP", 
    "time": "2020/01/20 15:20:00", 
}
eventJson = json.dumps(event)
channel.basic_publish(exchange='topics', routing_key=routing_key, body=eventJson)
print("Sent %r " % routing_key)
time.sleep(5)
data = {
    "request_id": 10, 
    "time_from":"2020/01/20 14:56:00", 
    "time_to":"2020/01/20 16:00:00", 
    "message":"Failure to kill UP"
}
payload = json.dumps(data)
channel.basic_publish(exchange='topics', routing_key=topic_data_request, body=payload)
print("Sent %r " % topic_data_request)

time.sleep(5)
device = {
    "id": 10, 
    "name":"Iphone", 
    "mac":"00:00:00:00:00:00"
}
payload = json.dumps(device)
channel.basic_publish(exchange='topics', routing_key=topic_device_request, body=payload)
print("Sent %r " % topic_device_request)


def callback(ch, method, properties, body):
    print(" DBM received an event [x] %r:%r" % (method.routing_key, body))
    if method.routing_key == topic_data_request:
        exit(0)

channel.basic_consume(queue=queue_name, on_message_callback=callback, auto_ack=True)

channel.start_consuming()
connection.close()
