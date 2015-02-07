package org.usfirst.frc.team2228.robot; 

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * A class for controlling a wheel through a talon hooked up using CAN.
 * Assumes use of a black talon motor controller with a quadrature encoder connected  
 * Extends: CANtalon
 *
 * @param name			the name for this wheel ex)"front right"
 * @param ramp 			the rate at which the talon is speed up (units per millisecond^2)
 * @param maxCurrent 	any current value output by the talon which exceeds this value will be logged
 * @param maxSpeed 		speed to drive the motors when set to 100 percent in speed mode (revs per second)
**/
public class Wheel extends CANTalon{
	public String name;			//name for this wheel 
    public int encoderCPR;
	public boolean invert = false;
	
	public static double P = 1000;
	public static double I = 0.01;
	public static double D = 0;
	
	public static final double RAMP = 1; 			//100% percent per seconds
	public static double maxCurrent = 100;
	
	public static double maxSpeed = 30;
	
	private double value;
	private double target;

	public Wheel(int port, int encoderCPR, String name){
		super(port);
		this.name = name;
		this.encoderCPR = encoderCPR;
		this.setVoltageRampRate(RAMP * this.getBusVoltage());
		
		this.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		this.setPID(P, I, D);
	}
		
	/**
	 * Moves the wheel a certain number of revolution.
	 * Will move the talon into positional mode if necessary
	 *
	 * @param revs number of revs to turn
	 **/
	public void moveRotations(double revs){
		if( ! this.getControlMode().equals(CANTalon.ControlMode.Position)){
			this.changeControlMode(CANTalon.ControlMode.Position);
			this.enableControl();
			this.value = this.getPosition();
			SmartDashboard.putString(name + ": mode", "position");
		}

		if(invert){
			revs *= -1;
		}
		
		//push new position to talon
		revs += this.getPosition();
		
		set(revs*encoderCPR);
	}

	/**
	 * Moves the wheel at a certain speed.
	 * Will move the talon into speed mode if necessary
	 *
	 * @param percent percent speed which the motors will be driven at
	 **/	
	public void setSpeed(double percent){
		if( ! this.getControlMode().equals(CANTalon.ControlMode.Speed)){
			this.value = this.getSpeed();
			this.changeControlMode(CANTalon.ControlMode.Speed);
			this.enableControl();
			SmartDashboard.putString(name + ": mode", "speed");
		}

		if(invert){
			percent *= -1;
		}
		
		//push new motor voltages to the talons
		this.set(percent*maxSpeed*1/60*1/100);
	}
	
	/**
	 * Drives the motor at a certain percent voltage.
	 * Will move the talon into percentVBus mode if necessary
	 *
	 * @param volts percent voltage to drive the motor
	 **/
	public void setVoltage(double percent){
		if( ! this.getControlMode().equals(CANTalon.ControlMode.PercentVbus)){
			this.value = this.getOutputVoltage();
			this.changeControlMode(CANTalon.ControlMode.PercentVbus);
			this.enableControl();
			SmartDashboard.putString(name + ": mode", "voltage");
		}

		if(invert){
			percent *= -1;
		}
		
		//push new motor voltages to the talons
		this.set(percent);
	}
	
	/** 
	  * sets the CANtalon value
	  *
	  * @see CANtalon.set(double value)
	  * @param value the value to run the talons at
	  **/
	public void set(double value){
		SmartDashboard.putString(name, String.format("%.2f",value));
		super.set(value);
	}  
	  
	/**
	 * Will set whether or  not to invert all drive signals to this wheel
	 *
	 * @param invert specifies if the wheel is inverted
	 **/
	public void setInvert(boolean invert){
		this.invert = invert;
	}
}
