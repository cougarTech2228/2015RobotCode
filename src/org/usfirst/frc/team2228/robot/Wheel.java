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
 * @param ramp 			the rate at which the jaguar is speed up (units per millisecond^2)
 * @param maxCurrent 	any current value output by the jaguar which exceeds this value will be logged
 * @param maxSpeed 		speed to drive the motors when set to 100 percent in speed mode (revs per second)
**/
public class Wheel extends CANJaguar{
	public int port;		//CAN id
	
	public String name;		//name for this wheel 
    
	public boolean invert = false;	//invert this wheel?
	public boolean enabled = true;  //if this is true, the wheel wont move, ever
	
	private double value;	//the current value sent to the jaguars
	private double target;  //the target value to ramp the jaguars to

	/**
	 * Constructs the wheel
	 * 
	 * @param port the CAN id for the jaguar controlling this wheel
	 * @param encoderCPR the counts per revolution of the encoder, used for position and speed control
	 * @param name the name of this wheel, used for logging
	 */
	public Wheel(int port, int encoderCPR, String name){
		super(port);
		this.port = port;
    	this.name = name;
	}
		
	/**
	 * Drives the motor at a certain percent voltage.
	 * Will move the jaguar into percentVBus mode if necessary
	 *
	 * @param volts percent voltage to drive the motor
	 **/
	public void setVoltage(double percent){
		//check if the control mode is correct
		if( ! this.getControlMode().equals(CANJaguar.ControlMode.PercentVbus)){
			//set <value> to an appropriate value, set the correct control mode, and enable it
			this.value = this.getOutputVoltage();
			this.setPercentMode();
			this.enableControl();
			SmartDashboard.putString(name + ": mode", "voltage");
		}

		//check for invert
		if(invert){
			//invert the signal
			percent *= -1;
		}
		
		//push new motor voltages to the Jaguars
		this.target(percent);
	}
	
	/** 
	  * sets the CANJaguar value, and logs it in the dashboard
	  *
	  * @see CANJaguar.set(double value)
	  * @param value the value to run the jaguars at
	  **/
	public void set(double value){
		//check if motor is enabled
		if(enabled){
			//print formated value to the dashboard
			SmartDashboard.putString(name, String.format("%.2f",value));
			super.set(value);
		}
	}  
	  
	/**
	 * simply sets the target value for ramping
	 * 
	 * @param value the new target value
	 */
	public void target(double value){
		target = value;
		//set(this.value);
	}
	
	/**
	 * updates the motor to the new speed based on Parameters.ramp
	 *
	 *@param time time passed since last call to update (in seconds)
	 **/
	public void update(double time){
		if(this.getOutputCurrent() > Parameters.maxCurrent){
			Logger.log("Maximum Current Exceeded"); 
		}

		//this will set increment to the correct amount based on ramp and time and the sign (direction) of the change
		double increment = Math.signum(target-value) * time * Parameters.ramp;
		
		//this will check if increment will overshoot the target
		if(Math.abs(target-value) < Math.abs(increment)){
			//and if so the new value will be set to target
			value = target;
		}else{
			//otherwise increment value
			value += increment;	
		}
		
		//log value, target, and increment, formated to 2 decimal places
		SmartDashboard.putString(name, String.format("%.2f",value) + "/" + String.format("%.2f",target) + " " +String.format("%.2f",increment));
		
		//if the motor is enabled
		if(enabled){
			//send the new value to the jaguar
			super.set(value);
		}
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
