package org.usfirst.frc.team2228.modules;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;

public class Pneumatics
{

	Solenoid sol;
	Compressor com;
	public boolean pushActivate = false;
	
	public Pneumatics(final int channel){
		
		sol = new Solenoid(channel);
		com = new Compressor();
		
	}
	
	public void compress(){
		com.start();
	}
	
	public void stopCompress(){
		com.stop();
	}
	
	public void activate(boolean state){
		sol.set(state);
	}
	
	public Solenoid getSol(){
		return sol;
	}
	
	public void doPusher(){
		
		if(pushActivate){
			sol.set(true);
		}else{
			sol.set(false);
		}
		
	}
	
	public void setCom(Compressor com){
		this.com = com;
	}
}
