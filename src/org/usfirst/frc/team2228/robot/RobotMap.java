package org.usfirst.frc.team2228.robot;

public class RobotMap
{
	//Joysticks
	public static int driveJoy = 0;
	public static int controlJoy = 1;
	
	//ToteElevator
	public static int mTote = 15;
	public static int limit1 = 0;
	public static int limit2 = 1;
	public static int limit3 = 2;
	public static int limit4 = 3;
	public static int limit5 = 4;
	
	//CanElevator
	public static int mElevator = 14;
	
	//Drivebase
	/**
	 * CAN id for the FR wheel's jaguar
	 */
	public static int canID_FR  = 11;

	/**
	 * CAN id for the FL wheel's jaguar
	 */	
	public static int canID_FL  = 10;
	
	/**
	 * CAN id for the BR wheel's jaguar
	 */
	public static int canID_BR  = 12;
	
	/**
	 * CAN id for the BL wheel's jaguar
	 */
	public static int canID_BL  = 13;

	
	public RobotMap()
	{
		
	}
}
