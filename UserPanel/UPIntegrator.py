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
topic_status_up = "Status.UP"
topic_request = "Status.Request.UP"
topic_device_request = "Device.Request"
topic_device_response = "Device.Response"
channel.queue_bind(exchange='topics', queue=queue_name, routing_key=topic_status_up)
channel.queue_bind(exchange='topics', queue=queue_name, routing_key=topic_device_response)
channel.queue_bind(exchange='topics', queue=queue_name, routing_key=topic_monitor)
print(' UP Integrator [*] Waiting for topics. To exit press CTRL+C')

time.sleep(0.5)
#
data = {
    "id": 1,
    "name": "Samsung",
    "mac": "23:DF:56:89:89:67"
}
channel.basic_publish(exchange='topics', routing_key=topic_device_request, body=json.dumps(data))
print("Sent %r " % topic_device_request)
data = {
    "id": 2,
    "name": "Samsung",
    "mac": "AA:AA:AA:AA:AA:AA"
}
channel.basic_publish(exchange='topics', routing_key=topic_device_request, body=json.dumps(data))
print("Sent %r " % topic_device_request)

id = 1
def callback(ch, method, properties, body):
    print(" UPIntegrator received an event [x] %r:%r" % (method.routing_key, body))
    '''
    if method.routing_key == topic_device_response:
        ##
        global id
        data = {
            "id": str(id), 
            "result":"PASS",
            "user": "Gatis"
        }
        id = id + 1
        payload = json.dumps(data)
        print(payload)
        channel.basic_publish(exchange='topics', routing_key=topic_response, body=payload)
        print("Sent %r " % topic_response)'''

channel.basic_consume(queue=queue_name, on_message_callback=callback, auto_ack=True)

channel.start_consuming()
connection.close()
