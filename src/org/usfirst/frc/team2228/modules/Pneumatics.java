package org.usfirst.frc.team2228.modules;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;

public class Pneumatics
{

	Solenoid brake;//Creates solenoid called sol
	Solenoid pusher;
	Compressor comp;//Creates a compressor called com
	
	public boolean pushActivate = false;//Creates a boolean determining the states of pushActive
	
	
	/**
	 * Initializes the solenoid setting it to the channel based off the parameter.
	 * Initializes the Compressor.
	 * @param channel
	 */
	public Pneumatics(int moduleNumber, int brakePort, int pusherPort){
		pusher = new Solenoid(3, 4);
		brake = new Solenoid(3, 7);
		comp = new Compressor(3);
		
	}
	
	public void clearFaults()
	{
		comp.clearAllPCMStickyFaults();
	}
	
	/**
	 * Activates the compressor.
	 */
	public void compress(){
		comp.start();
	}
	/**
	 * Stops the compressor.
	 */
	public void stopCompress(){
		comp.stop();
	}
	
	/**
	 * Activates the solenoid
	 */
	
	public void brake(boolean state){
		brake.set(state);
		if(state){
			compress();
		}
	}
	
	/**
	 * Activates solenoid based off the variable pushActivate
	 */
	public void doPusher(){
		
		if(pushActivate){
			pusher.set(true);
			compress();
		}else{
			pusher.set(false);
		}
		
	}
}
