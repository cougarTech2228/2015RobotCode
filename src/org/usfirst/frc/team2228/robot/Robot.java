package org.usfirst.frc.team2228.robot;

import org.usfirst.frc.team2228.modules.Camera;
import org.usfirst.frc.team2228.modules.CanElevator;
import org.usfirst.frc.team2228.modules.Drivebase;
import org.usfirst.frc.team2228.modules.Pneumatics;
import org.usfirst.frc.team2228.modules.ToteElevator2;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;

public class Robot extends IterativeRobot
{

	RobotMap robotMap;
	Joystick driveJoy;
	Joystick launchPad;
	Joystick controlJoy;
	Drivebase drive;
	CanElevator can;
	Camera cam;
	ToteElevator2 lifter;
	Pneumatics pneu;
	
	
	public void robotInit()
	{
		launchPad = new Joystick(3);
		driveJoy = new Joystick(1);
		controlJoy = new Joystick(0);
		pneu = new Pneumatics(0);
		lifter = new ToteElevator2(controlJoy);
		cam = new Camera(controlJoy);
		drive = new Drivebase(10, 13, 11, 12, driveJoy, 6);
//		can = new CanElevator(9, 6);
	}

	public void autonomousInit()
	{
		
	}
	
	public void autonomousPeriodic()
	{

	}

	public void teleopInit()
	{
		
	}
	
	public void teleopPeriodic()
	{
		pneu.doPusher();
		drive.drive();
		cam.moveCam();
		lifter.moveLifter();
		
		
		System.out.println(controlJoy.getPOV());
		
		if(controlJoy.getRawButton(3)){
			pneu.compress();
		}
		if(controlJoy.getRawButton(5)){
			pneu.stopCompress();
		}
		if(controlJoy.getRawButton(1)){
			pneu.pushActivate = true;
		}
		if(controlJoy.getRawButton(2)){
			pneu.pushActivate = false;
		}
		
//		//Can Elevator
//		if(controlJoy.getPOV() == 0)
//		{
//			can.lift(-0.6);
//		}
//		else if(controlJoy.getPOV() == 180)
//		{
//			can.lift(0.25);
//		}
//		else
//		{
//			can.lift(0.0);
//		}
	}

	public void testPeriodic()
	{

	}

}
