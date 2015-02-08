package org.usfirst.frc.team2228.robot;

public class Parameters {
	
	//button mappings
	public static int button_setRotation_one = 7;
	public static int button_setRotation_two = 8;
	public static int button_setRotation_three = 9;
	public static int button_setRotation_four = 10;
	
	public static int button_invertDrive = 3;
	
	//Advanced Joystick
	public static double rMode_limit_one = 0;
	public static double rMode_limit_two= .2;
	public static double rMode_limit_three = .3;
	public static double rMode_limit_four = .7;
	
	public static double rMode_max = 1;
	public static double rMode_min = .3;
	public static double rMode_curvature = .8;
	public static boolean rMode_invert = false;
	
	public static double lMode_max = .95;
	public static double lMode_min = .1;
	public static double lMode_maxLimit = 1;
	public static double lMode_minLimit = .2;
	public static double lMode_curvature = .2;
	public static boolean lMode_invert = false;
	
	//DriveBase
	public static double initialDirection = Math.PI;
	public static int COUNTS_PER_REV = 256;
	
	//wheel
	public static double ramp = 8; 		//time constant (percent (out of 1) per second)
	public static double maxCurrent = 100;
	
	//logger
	public static String logLocation = "/U/Data";
	
}
