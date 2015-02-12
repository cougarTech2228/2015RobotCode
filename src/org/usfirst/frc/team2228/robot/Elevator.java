package org.usfirst.frc.team2228.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.TalonSRX;

public class Elevator
{
	Jaguar mElevator;
	
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
		//mElevator.SetRampThrottle(rampRate);
	}
	
	public void lift(double speed)
	{
		mElevator.set(speed);
	}
	
	public void update(){
		//Can Elevator
		if(controlJoy.getPOV() == 0)
		{
			lift(-0.6);
		}
		else if(controlJoy.getPOV() == 180)
		{
			lift(0.25);
		}
		else
		{
			lift(0.0);
		}
	}
}
