#server socket code written by python ver.
#used on Raspberry pi

import socket

#Rasp's IP address and port number
server_address = (socket.gethostname(), 12345)

#max data size? 
maxsize = 1024

print('start')

try:
    #socket(IPver4, socket stream==TCP)
    server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    print('Starting up on {} port {}'.format(*server_address))
    server.bind(server_address)
    server.listen(1)    #5 connection max 여기까진 되는듯
    (clientsocket, address) = server.accept()
    print('I\'m waiting client call!')
    data = clientsocket.recv(maxsize)
    print(data)

except KeyboardInterrupt:
    print('Connection close')
    clientsocket.close()
    server.close()
except :
    print('Connection is closed unintendly')
    clientsocketclose()
    server.close()
