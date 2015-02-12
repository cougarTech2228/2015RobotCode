package org.usfirst.frc.team2228.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TalonSRX;

public class Elevator
{
	Jaguar mElevator;
	Joystick joystick;
	
	/*
	 * CanElevator
	 * -----------
	 * @param mElevatorPort - CAN ID for elevator motor
	 * @param rampRate - Voltage to go in 1 second
	 * 
	 * Elevator for lifting and lower cans, voltage is ramped, soft limits are configured via web interface
	 */	
	
	public Elevator(int mElevatorPort, int rampRate)
	{
		mElevator = new Jaguar(mElevatorPort);
		joystick = new Joystick(1);
		//mElevator.SetRampThrottle(rampRate);
	}
	
	public void lift(double speed)
	{
		mElevator.set(speed);
	}
	
	public void update(){
		//Can Elevator
		if(joystick.getPOV() == 0)
		{
			lift(-.8);
		}
		else if(joystick.getPOV() == 180)
		{
			lift(0.35);
		}
		else
		{
			lift(-0.1);
		}
	}
}
