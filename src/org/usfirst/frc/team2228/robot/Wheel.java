package org.usfirst.frc.team2228.robot; 

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * A class for controlling a wheel through a jaguar hooked up using CAN.
 * Assumes use of a black jaguar motor controller with a quadrature encoder connected  
 * Extends: CANJaguar
 *
 * @param port 			the CAN id for the Jaguar
 * @param encoderCPR 	the counts per revolution of the encoder
 * @param name			the name for this wheel ex)"front right"
 * @param pPID			pid values for positional control
 * @param sPID			pid values for speed control
 * @param ramp 			the rate at which the jaguar is speed up (units per millisecond^2)
 * @param maxCurrent 	any current value output by the jaguar which exceeds this value will be logged
 * @param maxSpeed 		speed to drive the motors when set to 100 percent in speed mode (revs per second)
**/
public class Wheel extends CANTalon{
	public int port;		//CAN id
	public int encoderCPR;	// counts per revolution
	
	public String name;			//name for this wheel 
    
	public boolean invert = false;
	
	public static double[] pPID = {1,.01,0};	//pid values for positional control
	public static double[] sPID = {1,.01,0};	//pid values for speed control
	
	public static final RAMP = 1; 			//100% percent per seconds
	public static double maxCurrent = 100;
	
	public static double maxSpeed = 30;
	
	private double value;
	private double target;

	public Wheel(int port, int encoderCPR, String name){
		super(port);
		this.port = port;
		this.encoderCPR = encoderCPR;
		this.name = name;
		this.setVoltageRampRate(RAMP * this.getBusVoltage());
	}
		
	/**
	 * sets the pid values for positional control
	 *
	 * @param p	positional gain
	 * @param i	integral gain
	 * @param d	derivative gain
	 **/
	public void setPidPositional(double p, double i, double d){
		this.pPID[0] = p;
		this.pPID[1] = i;
		this.pPID[2] = d;
	}

	/**
	 * sets the pid values for speed control
	 *
	 * @param p	positional gain
	 * @param i	integral gain
	 * @param d	derivative gain
	 **/	
	public void setPidVelocital(double p, double i, double d){
		this.pPID[0] = p;
		this.pPID[1] = i;
		this.pPID[2] = d;		
	}
	
	/**
	 * Moves the wheel a certain number of revolution.
	 * Will move the jaguar into positional mode if necessary
	 *
	 * @param revs number of revs to turn
	 **/
	public void moveRotations(double revs){
		if( ! this.getControlMode().equals(CANJaguar.ControlMode.Position)){
			this.setPositionMode(CANJaguar.kQuadEncoder, encoderCPR, pPID[0], pPID[1], pPID[2]);
			this.enableControl();
			this.value = this.getPosition();
			SmartDashboard.putString(name + ": mode", "position");
		}

		if(invert){
			revs *= -1;
		}
		
		//push new position to jaguar
		revs += this.getPosition();
		
		set(revs);
	}

	/**
	 * Moves the wheel at a certain speed.
	 * Will move the jaguar into speed mode if necessary
	 *
	 * @param percent percent speed which the motors will be driven at
	 **/	
	public void setSpeed(double percent){
		if( ! this.getControlMode().equals(CANJaguar.ControlMode.Speed)){
			this.value = this.getSpeed();
			this.setPositionMode(CANJaguar.kQuadEncoder, encoderCPR, sPID[0], sPID[1], sPID[2]);
			this.enableControl();
			SmartDashboard.putString(name + ": mode", "speed");
		}

		if(invert){
			percent *= -1;
		}
		
		//push new motor voltages to the Jaguars
		this.set(percent*maxSpeed);
	}
	
	/**
	 * Drives the motor at a certain percent voltage.
	 * Will move the jaguar into percentVBus mode if necessary
	 *
	 * @param volts percent voltage to drive the motor
	 **/
	public void setVoltage(double percent){
		if( ! this.getControlMode().equals(CANJaguar.ControlMode.PercentVbus)){
			this.value = this.getOutputVoltage();
			this.setPercentMode(kQuadEncoder, encoderCPR);
			this.enableControl();
			SmartDashboard.putString(name + ": mode", "voltage");
		}

		if(invert){
			percent *= -1;
		}
		
		//push new motor voltages to the Jaguars
		this.set(percent);
	}
	
	/** 
	  * sets the CANJaguar value
	  *
	  * @see CANJaguar.set(double value)
	  * @param value the value to run the jaguars at
	  **/
	public void set(double value){
		SmartDashboard.putString(name, String.format("%.2f",value));
		super.set(double value)
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
