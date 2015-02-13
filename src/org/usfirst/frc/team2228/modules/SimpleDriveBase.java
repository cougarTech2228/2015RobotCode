package org.usfirst.frc.team2228.modules;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;

public class SimpleDriveBase
{
	
	CANJaguar mLF;
	CANJaguar mLB;
	CANJaguar mRF;
	CANJaguar mRB;
	Joystick joy;
	
	/*
	 * Drivebase
	 * ---------
	 * @param mLFPort - CAN ID for left front motor
	 * @param mLBPort - CAN ID for left back motor
	 * @param mRFPort - CAN ID for right front motor
	 * @param mRBPort - CAN ID for right back motor
	 * @param joy - Drive joystick
	 * 
	 * Initializes the class and necessary devices
	 */
	public SimpleDriveBase(int mLFPort, int mLBPort, int mRFPort, int mRBPort, Joystick joy, int rampRate)
	{
		mLF = new CANJaguar(mLFPort);
		mLB = new CANJaguar(mLBPort);
		mRF = new CANJaguar(mRFPort);
		mRB = new CANJaguar(mRBPort);
		this.joy = joy;
		
		//mLF.SetRampThrottle(rampRate);
		//mLB.SetRampThrottle(rampRate);
		//mRF.SetRampThrottle(rampRate);
		//mRB.SetRampThrottle(rampRate);
	}
	
	/*
	 * drive()
	 * Utilizing the joystick sets the appropriate values to each motor
	 */	
	public void drive()
	{
		double v1, v2, v3, v4;
		double lMag = joy.getMagnitude();
		double dir = -joy.getDirectionRadians();
		double rotate = -joy.getTwist();
		
		v1 = lMag*Math.sin(dir + (Math.PI/4)) + rotate;
		v2 = lMag*Math.cos(dir + (Math.PI/4)) - rotate;
		v3 = lMag*Math.cos(dir + (Math.PI/4)) + rotate;
		v4 = lMag*Math.sin(dir + (Math.PI/4)) - rotate;
		
		mRF.set(v1);
		mLF.set(-v2);
		mRB.set(v3);
		mLB.set(-v4);
		
		/*double lMag = joy.getMagnitude();
		double dir = -joy.getDirectionRadians();
		double rotate = joy.getTwist();		
		double rF = lMag*Math.sin(dir + (Math.PI/4)) + rotate;
		double lF = lMag*Math.cos(dir + (Math.PI/4)) - rotate;
		double rB = lMag*Math.cos(dir + (Math.PI/4)) + rotate;
		double lB = lMag*Math.sin(dir + (Math.PI/4)) - rotate;
		
		mLF.set(lF);
		mLB.set(lB);
		mRF.set(rF);
		mRB.set(rB);*/
	}
}
