import pika
import Handler
import json

class MainRPCServer(object):
    def __init__(self, address="localhost", port=5672, queue_name="rpc_queue"):
        self.address = address
        self.port = port
        self.queue_name = queue_name
        self.handler = Handler.Handler()

    def on_request(self, ch, method, props, body):
        print body
        request = json.loads(body)
        method = request['method']
        response = {}

        if method == "register":
            response = self.handler.register(request['username'], request['password'])
        elif method == "login":
            response = self.handler.login(request['username'], request['password'])
        elif method == "add_friend":
            response = self.handler.addFriend(request['username'], request['friend'])
        elif method == "create_group":
            response = self.handler.createGroup(request['username'], request['groupname'])
        elif method == "get_groups":
            response = self.handler.getGroups(request['username'])
        elif method == "get_friends":
            response = self.handler.getFriends(request['username'])
        elif method == "left_group":
            response = self.handler.removeUserFromGroup(request['username'], request['groupname'])
        else:
            response = {'status' : 'noop'}

        print response

        ch.basic_publish(exchange='',
                        routing_key=props.reply_to,
                        properties=pika.BasicProperties(correlation_id=props.correlation_id),
                        body=json.dumps(response))

    def run(self):
        self.connection = pika.BlockingConnection(pika.ConnectionParameters(host=self.address, port=self.port))
        self.channel = self.connection.channel()

        self.channel.queue_declare(queue=self.queue_name)

        self.channel.basic_qos(prefetch_count=1)
        self.channel.basic_consume(self.on_request, queue=self.queue_name, no_ack=True)

        print "[v] RPC Server is running."
        self.channel.start_consuming()

def main():
    serv = MainRPCServer()
    serv.run()

if __name__ == '__main__':
    main()
