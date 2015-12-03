package org.usfirst.frc.team2228.modules;


import org.usfirst.frc.team2228.robot.Robot;

import edu.wpi.first.wpilibj.CANTalon; 
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
/**
 * A class for adjusting lifter based off user input
 * 
 * @author Harry James
 *
 */
public class ToteElevator2
{
	
//	private int state = 1;//where the robot currently is
	//private boolean usingArduino = true;//determines which mode you will be using
	//private int target = 1;//which stage the robot wants to go
//	private double liftSpd = 1;
	boolean on = false;//boolean used to make tote lifter code only run once
	boolean onLast = false;//other boolean in junction with the previous boolean "on"
	public int direction = 1;//the positive or negative direction of the lifter
	//if direction is 1 it is stationary, if direction is 2 it is moving up, if direciton is 3 it is moving down
	boolean manual = true;
	DigitalInput digiIn;//takes in sensor on lifter
	Joystick joy;//joystick being used by class
	CANTalon tal;//the talon being used by the class
	Pneumatics brake1;
	double brakeTime = 0;
	double lastTimeIRecoreded = 0;
	double deltaTime = 100;
	int counter = 0,counter2 = 0;
	double initialTime = Timer.getFPGATimestamp();
	double time = 0;
	
	DigitalInput up;
	DigitalInput down;
	
	/**
	 * The constructor for the ToteElevator2 class
	 * Sets this.joy to be equal to the param joy
	 * 
	 * @param A joystick which will be used by the class
	 */
	public ToteElevator2(int port, Joystick joy, Pneumatics brake1){
		this.joy = joy;
		this.brake1 = brake1;
		tal = new CANTalon(12);
		up = new DigitalInput(0);
		down = new DigitalInput(1);
	}
	
	public void toteAuto(){
		
		
		if(Robot.mode == 1){
			time = Timer.getFPGATimestamp() - initialTime;
		if(time <= 1){
			tal.set(-0.5);
			brake1.brake(false);
			
		}else if(time <= 2){
			tal.set(1);
			if(time >= 1.9){
				brake1.brake(true);
			}
		}else{
			tal.set(0);
		}
		}else if(Robot.mode == 2){
			time = Timer.getFPGATimestamp() - initialTime;
			System.out.println("Initial Time: " + initialTime + " || Time: " + time);
			if(time <= 0.9){
				tal.set(-0.5);
				brake1.brake(false);
			}else if(time < 2){
				tal.set(0);
				brake1.brake(true);
			}else if(time <= 3){
				tal.set(0.5);
				brake1.brake(false);
			}
			
		}
	}
	
	public void testing(double speed)
	{
		tal.set(speed);
	}
	
	public void debugOut()
	{
		System.out.println("UP: " + up.get() + " : " + up.getChannel());
		System.out.println("DOWN: " + down.get() + " : " + down.getChannel());
		System.out.println("----");
	}
	
	public void manaulMove(String direction, double speed)
	{
		if(direction.equals("up"))
		{
			if(!up.get())
			{
				brake1.brake(true);
				tal.set(speed);
			}			
			else
			{
				brake1.brake(false);
				tal.set(0.0);
			}
		}
		else if(direction.equals("down"))
		{
			if(!down.get())
			{
				brake1.brake(true);
				tal.set(-speed);
			}
			else
			{
				brake1.brake(false);
				tal.set(0.0);
			}
		}
		else
		{
			brake1.brake(false);
			tal.set(0.0);
		}
	}
	
	/**
	 * Method used for determining where the lifter is based off when the sensor is activated 
	 * and where the lifter last was.
	 * 
	 */
	
	public void moveLifter(boolean mode){
		
		if(joy.getMagnitude() < 0.1)
		{
			tal.set(0.0);
		}
		else if(joy.getDirectionDegrees() < 90 && joy.getDirectionDegrees() > -90)
		{
			tal.set(joy.getMagnitude());
		}
		else if(joy.getDirectionDegrees() <= -90 || joy.getDirectionDegrees() >= 90)
		{
			tal.set(-joy.getMagnitude());
		}
		
		/*
		//determines if they are in manual or automatic lifting mode
		manual = mode;
		
		if(manual){
			
			//checks if joystick is pointing forward causing the lifter to move upward
			
			if(joy.getDirectionDegrees()< 90 || joy.getDirectionDegrees() >= 270 && joy.getMagnitude() >= 0.3){
				tal.set(joy.getMagnitude());
				//brake1.brake(false);
			}
			
			//checks if joystick is pointing backward causing the lifter to move downward
			else if(joy.getDirectionDegrees() >= 90 && joy.getDirectionDegrees() < 270 && joy.getMagnitude() >= 0.3){
				tal.set(-(joy.getMagnitude()));
				//brake1.brake(false);
				
			//if not moving will brake first then stop the motors
			}else{
				//brake1.brake(true);
				if(brakeTime == 0){
					brakeTime = Timer.getFPGATimestamp();
				}
				double timePassed =  Timer.getFPGATimestamp() - brakeTime;
				if (timePassed > 100){
					tal.set(0);
				}
			}
		
		//automatic mode for lifting
		}else{
			
			//a series of booleans to make sure when the switch is activated,
			//it only moves the stage up or down by 1
			
			//checks if switch activated sets on to true
			if(digiIn.get()){
				on = true;
			}else{
				on = false;
				onLast = false;
			}
			
			//if  on isnt equal to onLast it will run the code switching the 
			//current state based off the direction of the lifter
			if(on!=onLast){
				
				if(direction == 2){
					if(state < 5){
						state += 1;
					}
				}
				if(direction == 3){
					if(state > 1){
						state -= 1;
					}
				}
				
				//sets onLast equal to on in order to make sure that state does not increase 
				//or decrease every time the code is run
				onLast = on;
			}
			
			//if state is less than the target it will tell the talons to run upward
			if(state < target){
				
				tal.set(liftSpd);
				direction = 2;
				brake1.brake(false);
				
			//if state is greater than the target it will tell the talons to run downward	
			}else if(state > target){
				
				tal.set(-liftSpd);
				direction = 3;
				brake1.brake(false);
			
			//if state is equal to the target then the talon will not move at all	
			}else{
				
				brake1.brake(true);
				if(brakeTime == 0){
					brakeTime = Timer.getFPGATimestamp();
				}
				double timePassed =  Timer.getFPGATimestamp() - brakeTime;
				if (timePassed > 100){
					tal.set(0);
				}
				direction = 1;
				
			}
			System.out.println("Target: " + target + "  ||  " + "State: " + state);
			
			//will use these buttons if you are using the arduino
			if(usingArduino){
				if(joy.getRawButton(1)){
					target = 1;
				}
				if(joy.getRawButton(2)){
					target = 2;
				}
				if(joy.getRawButton(3)){
					target = 3;
				}
				if(joy.getRawButton(4)){
					target = 4;
				}
				if(joy.getRawButton(5)){
					target = 5;
				}
				
			//will use this code if you are not using the joystick instead of arduino
			}else if(!usingArduino){
				if(joy.getRawButton(12)){
					target = 1;
				}
				if(joy.getRawButton(11)){
					target = 2;
				}
				if(joy.getRawButton(9)){
					target = 3;
				}
				if(joy.getRawButton(7)){
					target = 4;
				}
				if(joy.getRawButton(6)){
					target = 5;
				}
			}
		}*/
	}
}
