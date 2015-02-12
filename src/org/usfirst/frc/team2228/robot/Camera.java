package org.usfirst.frc.team2228.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;

public class Camera
{
	Servo s1, s2;
	double pos;
	double throt;
	Joystick joy;
	
	public Camera(Joystick joy){
		s1 = new Servo(8);
		s2 = new Servo(9);
		this.joy = joy;
	}
	
	public void update(){
		
		
		switch(joy.getPOV()){
			default:
				//no move
				s2.set(0.5);
				s1.set(0.51);
				break;
			case 0:
				s2.set(0.48);
				break;
			case 315:
				s2.set(0.48);
				s1.set(0.52);
				break;
			case 270:
				s1.set(0.53);
				break;
			case 225:
				s1.set(0.48);
				s2.set(0.53);
				break;
			case 180:
				s2.set(0.53);
				break;
			case 135:
				s2.set(0.53);
				s1.set(0.48);
				break;
			case 90:
				s1.set(0.49);
				break;
			case 45:
				s1.set(0.48);
				s2.set(0.48);
				break;
		}
	}
}
