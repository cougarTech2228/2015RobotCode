
package org.usfirst.frc.team2228.robot; 

import edu.wpi.first.wpilibj.CANJaguar;
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
 * @param ramp 			the rate at which the jaguar is speed up (percent per second), defaults to one
 * @param minRamp 		the update function will always increase the motor speed at least this much
 * @param maxCurrent 	any current value output by the jaguar which exceeds this value will be logged
**/
public class Wheel extends CANJaguar{
	public int port;		//CAN id
	public int encoderCPR;	// counts per revolution
	
	public String name;			//name for this wheel 
    
	public boolean invert = false;
	
	public double[] pPID = {1,.01,0};	//pid values for positional control
	public double[] sPID = {1,.01,0};	//pid values for speed control
	
	public double ramp = 1;
	public double minRamp = .01;
	public double maxCurrent = 100;
	
	private double value;
	private double target;

	public Wheel(int port, int encoderCPR, String name){
		super(port);
		this.port = port;
		this.encoderCPR = encoderCPR;
		this.name = name;
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
		if(this.getControlMode().equals(CANJaguar.ControlMode.Position)){
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

		SmartDashboard.putNumber(name, revs);
		
		set(revs);
	}

	/**
	 * Moves the wheel at a certain speed.
	 * Will move the jaguar into speed mode if necessary
	 *
	 * @param speed rate aft which the wheel will turn in revs per minute
	 **/	
	public void setSpeed(double speed){
		if(this.getControlMode().equals(CANJaguar.ControlMode.Speed)){
			this.value = this.getSpeed();
			this.setPositionMode(CANJaguar.kQuadEncoder, encoderCPR, sPID[0], sPID[1], sPID[2]);
			this.enableControl();
			SmartDashboard.putString(name + ": mode", "speed");
		}
		
		SmartDashboard.putNumber(name, speed);

		if(invert){
			speed *= -1;
		}
		
		//push new motor voltages to the Jaguars
		this.set(speed);
	}
	
	/**
	 * Drives the motor at a certain voltage.
	 * Will move the jaguar into percentVBus mode if necessary
	 *
	 * @param volts percent voltage to drive the motor
	 **/
	public void setVoltage(double volts){
		if(this.getControlMode().equals(CANJaguar.ControlMode.PercentVbus)){
			this.value = this.getOutputVoltage();
			this.setPercentMode(kQuadEncoder, encoderCPR);
			this.enableControl();
			SmartDashboard.putString(name + ": mode", "voltage");
		}
		
		SmartDashboard.putNumber(name, volts);

		if(invert){
			volts *= -1;
		}
		
		//push new motor voltages to the Jaguars
		this.set(volts);
	}
	
	/** 
	  * sets the CANJaguar value
	  *
	  * @see CANJaguar.set(double value)
	  * @param value the value to run the jaguars at
	  **/
	public void sett(double value){
		this.target = value;
		super.set(this.value);
	}
	
	/**
	 * updates the motor to the new speed based on this.ramp
	 *
	 *@param time time passed since last call to update (in seconds)
	 **/
	public void update(double time){
		if(this.getOutputCurrent() > maxCurrent){
			Logger.log("Maximum Current Exceeded"); 
		}

		//constant:
		//double increment = Math.sign(target-value)*ramp*time;		
		
		//percent based:
		double increment = (target-value)*time*ramp;
		
		if(increment < minRamp){
			increment = Math.max(minRamp, target-value);
		}
		
		value += increment;
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