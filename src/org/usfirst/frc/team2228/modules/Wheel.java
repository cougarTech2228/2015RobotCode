 package org.usfirst.frc.team2228.modules; 

import org.usfirst.frc.team2228.robot.Parameters;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * A class for controlling a wheel through a talon hooked up using CAN.
 * Assumes use of a black talon motor controller with a quadrature encoder connected  
 * Extends: CANTalon
 * 
 * Modified from wheelJaguar
 * 
 * Harrison, this code should be fine, if it isn't, talk to the professor, or send me a message
**/
public class Wheel extends CANTalon{
	/**
	 * the CAN id for the Talon
	 */
	public int port;		//CAN id
	
	/**
	 * the name for this wheel ex)"front right"
	 */
	public String name;		//name for this wheel
   
	/**
	 * the counts per revolution of the encoder
	 */
	public int encoderCPR; //counts per rev
	
	/**
	 * invert the drive signals sent to this wheel (-1 = 1)
	 */
	public boolean invert = false;	//invert this wheel?
	
	/**
	 * when on the wheel wont move, ever 
	 */
	public boolean enabled = true;  //if this is true, the wheel wont move, ever
	
	private double value;	//the current value sent to the Talons
	private double target;  //the target value to ramp the Talons to


	/**
	 * Constructs the wheel
	 * 
	 * @param port the CAN id for the Talon controlling this wheel
	 * @param encoderCPR the counts per revolution of the encoder, used for position and speed control
	 * @param name the name of this wheel, used for logging
	 */
	public Wheel(int port, int encoderCPR, String name){
		super(port);

		this.port = port;
		this.name = name;
		this.encoderCPR = encoderCPR;
		
		//not using this, using my code instead because i know it works
		//this.setVoltageRampRate(RAMP * this.getBusVoltage());
		
		this.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		this.setPID(Parameters.P, Parameters.I,Parameters.D);
	}
		
	/**
	 * Moves the wheel a certain number of revolutions.
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

		//check for invert
		if(invert){
			//invert the signal
			percent *= -1;
		}
		
		//push new motor voltages to the Talons
		this.target(percent);
	}
	
	/** 
	  * sets the CANTalon value, and logs it in the dashboard
	  *
	  * @see CANtalon.set(double value)
	  * @param value the value to run the talons at
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
		
		//uncomment the next line and make no calls to update if you wish to disable ramping
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
			//send the new value to the Talon
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
