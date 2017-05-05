#PIR sensor with db ver1
#Be-Care graduate project KimJunyeong

import RPi.GPIO as GPIO
from time import localtime, strftime
import time
import MySQLdb

GPIO.setmode(GPIO.BCM)
sensor_value = 5

GPIO.setup(sensor_value, GPIO.IN)

current_state = 0
prev_state = 0
count = 0

try:
	while True:
		current_state = GPIO.input(sensor_value)
		if current_state != prev_state:
			count+=1
		else : count = 0

		if prev_state == 0 and count == 5:
			time_string = strftime("%Y-%m-%d %H:%M:%S",localtime())
			db = MySQLdb.connect(host='localhost', user='root', passwd='jsy123', db='test')
			with db:
				cur = db.cursor()
				cur.execute("INSERT INTO approach(sensor_name, time, value) VALUES(%s, %s, %s)", ('PIR sensor', time_string, 0))
				db.commit()
			prev_state = 1
			count = 0
			print "Motion Detected!"
		elif prev_state == 1 and count == 5:
			prev_state = 0
			count = 0
			
		time.sleep(0.01)	
except KeyboardInterrupt:
	print "Quit"
	GPIO.cleanup()
