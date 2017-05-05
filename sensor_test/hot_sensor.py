import serial
import time
#import RPi.GPIO as GPIO

#GPIO.setmode(GPIO.BCM)
#GPIO.setup(15, GPIO.IN)
#GPIO.setup(14, GPIO.OUT)
ser = serial.Serial(
	port='/dev/ttyAMA0',
	baudrate = 9600,
	parity = serial.PARITY_NONE,
	stopbits = serial.STOPBITS_ONE,
	bytesize = serial.EIGHTBITS,
	timeout=1
)

try:
	print 'connect ' + str(ser.isOpen())
	while 1:
		response = ser.read(10)
		print response
		#time.sleep(0.5)
except KeyboardInterrupt:
	ser.close()
