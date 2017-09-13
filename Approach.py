#This code is for approaching function.
#We use PIR sensor for motion detecting, servo motor for doorlock.
#And we also use MySQL, to save sensing log.
#DB ID is Approach.
#This code is run as a backward process.
#Team Be-Care, by KimJunyeong.
#If speed is too slow, I'm going to change module to class.

import RPi.GPIO as GPIO
import time
import MySQLdb 

def sensing(approach_value, approach_counter):
	'''This module controls PIR sensor.
	This module counts how many times the sensor senses.
	If the sensor detects, it returns approach_counter+1.
	'''
	if GPIO.input(approach_value):
		return approach_counter+1
	else:
		return 0 

def save_value(cur):
	'''This module saves sensing log to database.
	'''
	time_string=time.strftime("%H:%M:%S", time.localtime())
	print(time_string)
	#Doorlock=2 means it is not determined if detected one is the patient or not.
	cur.execute("INSERT INTO Door(Door, Doorlock, Time) VALUES(%s, %s, %s);", (1, 2, time_string))
	db.commit()

def wait(cur):
	'''Wait until positioning over.
	   How do we get to know? 
	   If positioning ends, the value of Doorlock is not 2.'''
	result=cur.execute("SELECT Doorlock from Door where Doorlock==2;")
	return result

def motor_control(cur, power):
	result = cur.execute("SELECT Doorlock from Door LIMIT 1")	#1? 0?
	if '1' in result:	# 1 means it is not patient so open!
		power.ChangeDutyCycle(12.5)	#Door open		
		
#main	
GPIO.setmode(GPIO.BCM)
approach_value = 5	#Pin 5 is for reading approach sensor value.
motor = 26

GPIO.setup(approach_value, GPIO.IN)
GPIO.setup(motor, GPIO.OUT)

approach_counter = 0	#How many times the sensor detects?
power = GPIO.PWM(motor, 50)	#power of motor
power.start(7.5)		#Door is closed

try:
	db=MySQLdb.connect(host='localhost',user='Approach',\
				passwd='approach',db='Be_Care')
	with db:
		cur=db.cursor()
	while True:
		time.sleep(0.5)	#Time can be changed	
		approach_counter = sensing(approach_value, approach_counter)
		if approach_counter==5:		#Something is detected
			save_value(cur)
			while wait(cur):
			motor_control(cur, power)
			while sensing:
			power.ChangeDutyCycle(7.5)
			approach_counter=0	#Initialize
except:
	print('GoodBye')
	db.close()
		

