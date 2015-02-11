package org.usfirst.frc.team2228.modules;

import edu.wpi.first.wpilibj.Jaguar;

public class CanElevator
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
	public CanElevator(int mElevatorPort, int rampRate)
	{
		mElevator = new Jaguar(mElevatorPort);
		//mElevator.SetRampThrottle(rampRate);
	}
	
	public void lift(double speed)
	{
		mElevator.set(speed);
	}
}
