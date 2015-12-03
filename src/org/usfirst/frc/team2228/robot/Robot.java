package org.usfirst.frc.team2228.robot;

import java.util.Date;

import org.usfirst.frc.team2228.modules.Camera;
import org.usfirst.frc.team2228.modules.CanElevator;
import org.usfirst.frc.team2228.modules.DriveBase;
import org.usfirst.frc.team2228.modules.Logger;
import org.usfirst.frc.team2228.modules.Pneumatics;
import org.usfirst.frc.team2228.modules.ToteElevator2;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot
{
	//sets all the ports for the modules 
	int cJoyPort = 1, dJoyPort = 0, lJoyPort = 2, pneuPort = 4, brake1Port = 7, gyroPort = 1, lifterPort = 12; 
	public static int mode = 1;
	int speedMode = 1;
	int spdT = 1;
    
	Relay humanPlayerLight;
	RobotMap robotMap;
	Joystick driveJoy;//the joystick for the driver
	Joystick launchPad;//the arduino joystick 
	Joystick controlJoy;//the second joystick being used by the co-pilot
	DriveBase drive;//the drivebase
	CanElevator can;//the can elevator
	Camera cam;//the camera
	ToteElevator2 lifter;//the tote elevator
	Pneumatics pneu;//the pneumatics
	Gyro gyro;//the gyroscope 
	
	//time stuff used for tele-op ramping
	double time;
	double oldTime = 0;
	double newTime = 0;
	
	//initial time for autonomous
	double initialTime;
	
	int counter = 0;
	
	long autoSTime;
	
	public void robotInit()
	{
		humanPlayerLight = new Relay(0, Relay.Direction.kForward);
		launchPad = new Joystick(lJoyPort);//sets arduino joystick to port 3
		driveJoy = new Joystick(dJoyPort);//sets drive joystick to port 1
		controlJoy = new Joystick(cJoyPort);//sets control joystick to port 2
		pneu = new Pneumatics(3, 7, 4);//sets pneumatic 
		lifter = new ToteElevator2(lifterPort, controlJoy, pneu);
		cam = new Camera(controlJoy);
		gyro = new Gyro(gyroPort);
		drive = new DriveBase(dJoyPort , RobotMap.canID_FR, RobotMap.canID_FL, RobotMap.canID_BR, RobotMap.canID_BL);
//		can = new CanElevator(9, 6);

	}

	public void autonomousInit()
	{
		//initialTime = Timer.getFPGATimestamp();
		Date nowTime = new Date();
		autoSTime = nowTime.getTime();
	}
	
	public void autonomousPeriodic()
	{
		Date nowTime = new Date();
		
		if(nowTime.getTime() <= autoSTime + 1500)
		{
			lifter.manaulMove("up", 0.5);
		}
		else
		{
			lifter.manaulMove("none", 0.0);
		}
		
		/*time = Timer.getFPGATimestamp() - initialTime;
		System.out.println("time: " + time + " initialTime: " + initialTime);
		if(time <= 1){
			lifter.manaulMove("up", 0.5);
    	}*/
		
		
		//drive.autoTest();
		//drive.mecanumDrive(time, spdT, 0, 0.3, 0);
		/*if(mode == 1){
		
			drive.autonomous(initialTime);
			System.out.println("Autonomous Activated");
			
		}else if(mode == 2){
			//get the time for the previous iteration
	    	time = Timer.getFPGATimestamp() - initialTime;
	    	System.out.println("Mode 2 active in auto");
	    
	    	if(time <= 1){
	    		lifter.toteAuto();
	    	}
//	    	else if(time <= 2.1){
//	    		drive.autonomous(initialTime);
//	    	}else{
//	    		lifter.toteAuto();
//	    	}
		}*/
	}

	public void teleopInit()
	{
    	Logger.resetTime();	
	}
	
	public void teleopPeriodic()
	{
		
		
    	//check for changes in joystick config
//    	RobotMap.smartdashboard_get();
    	
    	//get the time for the previous iteration
    	newTime = Timer.getFPGATimestamp();
    	time = newTime - oldTime;
    	oldTime = newTime;
    	
    	//log the time (update loop)
    	SmartDashboard.putNumber("time", time);
    	
    	if(controlJoy.getRawButton(9))
    	{
    		System.out.println("LIGHT ON");
    		humanPlayerLight.set(Relay.Value.kOn);
    	}
    	else
    	{
    		humanPlayerLight.set(Relay.Value.kOff);
    	}
    	
//    	if(speedMode == 1){
//    		spdT = 1;
//    	}else if(speedMode == 2){
//    		spdT = 2;
//    	}else{
//    		spdT = 4;
//    	}
    	
//    	if(driveJoy.getRawButton(1)){
//    		speedMode = 2;
//    		if(driveJoy.getRawButton(2)){
//    			speedMode = 1;
//    		}
//    	}else{
//    		speedMode = 3;
//    	}
    	
    	//drive the bot, uses time for ramping
    	drive.mecanumDrive(time, spdT, driveJoy.getTwist(), driveJoy.getMagnitude(), driveJoy.getDirectionRadians());
    	pneu.doPusher();
    	//drive.doJiggleDrive();
    	
    	System.out.println(controlJoy.getMagnitude());
    	
    	/*if(controlJoy.getMagnitude() < 0.1)
    	{
    		System.out.println("dont move");
    		lifter.manaulMove("none", 0.0);
    	}
    	if(controlJoy.getDirectionDegrees() <= 90 && controlJoy.getDirectionDegrees() > -90)
    	{
    		System.out.println("down");
    		if(pneu.pushActivate)
    		{
    			lifter.manaulMove("down", 0.25);
    		}
    		else
    		{
    			lifter.manaulMove("down", 0.5);
    		}
    	}
    	else if(controlJoy.getDirectionDegrees() <= -90 || controlJoy.getDirectionDegrees() > 90)
    	{
    		System.out.println("up");
    		lifter.manaulMove("up", 0.2);
    	}*/
    	if(controlJoy.getMagnitude() < 0.1)
    	{
    		lifter.manaulMove("none", 0.0);
    	}
    	else if(controlJoy.getDirectionDegrees() >= -90 && controlJoy.getDirectionDegrees() < 90)
    	{
    		lifter.manaulMove("up", controlJoy.getMagnitude());
    	}
    	else if(controlJoy.getDirectionDegrees() >= 90 || controlJoy.getDirectionDegrees() < -90)
    	{
    		if(pneu.pushActivate)
    		{
    			lifter.manaulMove("down", 0.2);
    		}
    		else
    		{
    			lifter.manaulMove("down", 0.4);
    		}
    	}
    	else
    	{
    		lifter.manaulMove("none", 0.0);
    	}
    		
    	
    	
    	if(controlJoy.getRawButton(11)){
    		pneu.compress();
    	}
    	
    	if(controlJoy.getRawButton(12))
    	{
    		pneu.stopCompress();
    	}

    	
    	if(controlJoy.getRawButton(1)){
			pneu.pushActivate = true;
		}
		if(controlJoy.getRawButton(2)){
			pneu.pushActivate = false;
		} 
	}

	public void testPeriodic()
	{

	}

}
