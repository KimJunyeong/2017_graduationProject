#This code is about approaching sensor.
#We use PIR sensor for motion detecting, servo motor for doorlock.
#And we also use MySQL, to save sensing log.
#DB ID is Approach.
#This code is run as a background process.
#Team Be-Care, by KimJunyeong.
#If speed is too slow, I'm going to change module to class.

#detected =0? not detected =1?

import RPi.GPIO as GPIO
import time
import MySQLdb 

GPIO.setmode(GPIO.BCM)

approach_pin = 6	#Pin 6 is for reading approach sensor value.
motor = 26

GPIO.setup(approach_pin, GPIO.IN)
GPIO.setup(motor, GPIO.OUT)

approach_counter = 0	#How many times the sensor detects?
power = GPIO.PWM(motor, 50)	#power of motor
power.start(12.5)		#Door is open

#sensor_value = 0
pre_state = 0


def sensing():
	'''This module controls PIR sensor.
	This module counts how many times the sensor senses.
	If the sensor detects, it returns approach_counter+1.
	'''
	sensor_value = GPIO.input(approach_pin)
	global pre_state
	if sensor_value == 1:
		if pre_state == 0:
			pre_state = 1
			return 1
		else:
			return 0
	if sensor_value == 0:
		if pre_state == 1:
			pre_state = 0
		return 0 

def save_value(cur):
	'''This module saves sensing log to database.
	'''
	time_string=time.strftime("%H:%M:%S", time.localtime())
	#Doorlock=2 means it is not determined if detected one is the patient or not.
	cur.execute("INSERT INTO Door(Door, Doorlock, Time) VALUES(%s, %s, %s);", (1, 2, time_string))
	db.commit()

def waitwait(cur):
	'''Wait until positioning over.
	   How do we get to know? 
	   If positioning ends, the value of Doorlock is not 2.'''
	#cur.execute("SELECT Doorlock from Door where Doorlock=2;")
	DBOpen()
	result = cur.execute("SELECT Doorlock from Door where Doorlock=2;")
	#result = cur.fetchall()
	#result is the number of queries which Doorlock==2
	#print 'result = ',result
	cur.close()
	db.close()
	return result

def motor_control(power):
	'''This module is related to motor. 
		Motor is operated by the result of DB'''
	#result vaule weird!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	#result = cur.execute("SELECT Doorlock from Door LIMIT 1")	#1? 0?
	DBOpen()
	cur.execute("SELECT Doorlock from Door ORDER BY Time DESC LIMIT 1;")
	result = cur.fetchone()	
	if result[0] == 1:	# 1 means it is patient so close!
		power.ChangeDutyCycle(7.5)	#Door close
		time.sleep(0.5)
	cur.close()
	db.close()		

def DBOpen():
	global db
	global cur
	db=MySQLdb.connect(host='localhost',user='Approach',\
				passwd='approach', db='Be_Care')
	with db:
		cur = db.cursor()
		
#main	
try:
	while True:
		time.sleep(0.5)	#Time can be changed
		DBOpen()	
		if sensing() == 1:
			save_value(cur)
			print '1111'
			cur.close()
			db.close()
			print '2222'
			while waitwait(cur):
				time.sleep(0.5)
			print 'Boom!'
			motor_control(power)
			while sensing() == 0:
				time.sleep(0.5)
			print 'BoomBoom'
			#no one detected
			power.ChangeDutyCycle(12.5)
			time.sleep(0.5)
			#Everything is finished
			DBOpen()
			cur.execute("UPDATE Door SET Door = REPLACE(Door, 1, 0);")
			db.commit()
			cur.close()
			db.close()
		else:
			cur.close()
			db.close()
		
except Exception as error:
	print error
	#db.close()
	GPIO.cleanup()
		

