import socket
from datetime import datetime

address = (192.168.1.17, 12345)
max_size = 1024

print('starting the client at', datetime.now())
client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client.connect(address)
client.sendall(b'hey')
data = client.recv(max_size)
print('At', datetime.now(), 'someone replied', data)
client.close()
