package org.usfirst.frc.team2228.modules;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TalonSRX;

public class ToteElevator2
{

	private int state = 1;
	private boolean usingArduino = true;
	private int target = 1;
	enum STATES{floor, score, step, acquire, hold};
	boolean on = false;
	boolean onLast = false;
	int direction = 1;
	DigitalInput digiIn;
	Joystick joy;
	TalonSRX tal;
	
	public ToteElevator2(Joystick joy){
		this.joy = joy;
	}
	
	public void moveLifter(){
		
		if(digiIn.get()){
			on = true;
		}else{
			on = false;
			onLast = false;
		}
		
		if(on!=onLast){
			
			if(direction == 2){
				if(state < 5){
					state += 1;
				}
			}
			if(direction == 3){
				if(state > 1){
					state -= 1;
				}
			}
			onLast = on;
		}
		
		if(state < target){
			
			tal.set(1);
			direction = 2;
			
		}else if(state > target){
			
			tal.set(-1);
			direction = 3;
			
		}else{
			
			tal.set(0);
			direction = 1;
			
		}
		System.out.println("Target: " + target + "\n" + "State: " + state);
		
		if(usingArduino){
			if(joy.getRawButton(1)){
				target = 1;
			}
			if(joy.getRawButton(2)){
				target = 2;
			}
			if(joy.getRawButton(3)){
				target = 3;
			}
			if(joy.getRawButton(4)){
				target = 4;
			}
			if(joy.getRawButton(5)){
				target = 5;
			}
		}else if(!usingArduino){
			if(joy.getRawButton(12)){
				target = 1;
			}
			if(joy.getRawButton(11)){
				target = 2;
			}
			if(joy.getRawButton(9)){
				target = 3;
			}
			if(joy.getRawButton(7)){
				target = 4;
			}
			if(joy.getRawButton(6)){
				target = 5;
			}
		}
	}
}
