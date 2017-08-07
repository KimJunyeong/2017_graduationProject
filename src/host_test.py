from datetime import datetime
import socket

address = (socket.gethostname(), 12345)
max_size = 1024

print('Starting the server at', datetime.now())
print('Waiting for a client to call')
server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.bind(address)
server.listen(5)

client, addr = server.accept()
data = client.recv(max_size)

print('At', datetime.now(), client, 'said', data)
client.sendall('Are you talking to me?')
client.close()
server.close()
