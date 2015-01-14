package org.usfirst.frc.team2228.robot;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/*
 * Drive Base Omni-Directional
 */

public class DriveBase{

    Joystick joy;
    Talon mFR;
    Talon mBR;
    Talon mFL;
    Talon mBL;
    
    Jaguar jag1;
    Jaguar jag2;
    Jaguar jag3;
    Jaguar jag4;
    
    CANJaguar canFR;
    CANJaguar canBR;
    CANJaguar canFL;
    CANJaguar canBL;
    
    double d1, d2, d3, d4;
	double maxSpeed;
	double oldTime;
	
	static final int COUNTS_PER_REV = 10;
	static final double P = 1;
	static final double I = 1;
	static final double D = 0;

	
    public DriveBase(int joyPort, int bR, int bL, int fL, int fR){
    	joy = new Joystick(joyPort);
    	
        jag1 = new Jaguar(fL);
        jag2 = new Jaguar(fR);
        jag3 = new Jaguar(bL);
        jag4 = new Jaguar(bR);       
        
        canFR = new CANJaguar(fR);
        canBR = new CANJaguar(bR);
        canFL = new CANJaguar(fL);
        canBL = new CANJaguar(bL);
        
		canFR.setPositionMode(CANJaguar.kQuadEncoder, COUNTS_PER_REV, P, I, D);
		canFR.enableControl();
		
		canBR.setPositionMode(CANJaguar.kQuadEncoder, COUNTS_PER_REV, P, I, D);
		canBR.enableControl();		
		
		canFL.setPositionMode(CANJaguar.kQuadEncoder, COUNTS_PER_REV, P, I, D);
		canFL.enableControl();
		
		canBL.setPositionMode(CANJaguar.kQuadEncoder, COUNTS_PER_REV, P, I, D);
		canBL.enableControl();
		
		d1 = 0;
		d2 = 0;
		d3 = 0;
		d4 = 0;
		
		maxSpeed=10;
		
		oldTime=Timer.getFPGATimestamp();
    }
   
    public void mecanumDrive(){
    	
    	double lMag;
    	double dir;
    	double rotate = 0;;
    	double v1,v2,v3,v4;
    	
    	//get mag. dir. and rot. from the joystick
    	lMag = joy.getMagnitude();
    	dir = joy.getDirectionRadians();
    	rotate = joy.getTwist();
    	
    	SmartDashboard.putNumber("angle", Math.toDegrees(dir));
    	SmartDashboard.putNumber("rotate", rotate);
    	SmartDashboard.putNumber("magnitude", lMag);

    	//set each motors voltage based on the direction, magnetude and velocity
    	v1 = lMag*Math.sin(dir + (Math.PI/4)) + rotate;
    	v2 = lMag*Math.cos(dir + (Math.PI/4)) - rotate;
    	v3 = lMag*Math.cos(dir + (Math.PI/4)) + rotate;
    	v4 = lMag*Math.sin(dir + (Math.PI/4)) - rotate;
    	
    	
    	SmartDashboard.putNumber("v1", v1);
    	SmartDashboard.putNumber("v2", v2);
    	SmartDashboard.putNumber("v3", v3);
    	SmartDashboard.putNumber("v4", v4);
    	
    	//push new motor voltages to the Jaguars
    	jag1.set(v1);
    	jag2.set(-v2);
    	jag3.set(v3);
    	jag4.set(-v4);
    	
    	
    }
    
    public void mecanumDriveCAN(){
    	//mechanum CAN drive with positional control and encoders encoders
    	
    	double lMag;
    	double dir;
    	double rotate = 0;;
    	double v1,v2,v3,v4;
    	
    	//get mag. dir. and rot. from the joystick
    	lMag = joy.getMagnitude();
    	dir = joy.getDirectionRadians();
    	rotate = joy.getTwist();
    	
    	SmartDashboard.putNumber("angle", Math.toDegrees(dir));
    	SmartDashboard.putNumber("rotate", rotate);
    	SmartDashboard.putNumber("magnitude", lMag);

    	//set each motors voltage based on the direction, magnetude and velocity
    	v1 = lMag*Math.sin(dir + (Math.PI/4)) + rotate;
    	v2 = lMag*Math.cos(dir + (Math.PI/4)) - rotate;
    	v3 = lMag*Math.cos(dir + (Math.PI/4)) + rotate;
    	v4 = lMag*Math.sin(dir + (Math.PI/4)) - rotate;
    	
    	
    	SmartDashboard.putNumber("v1", v1);
    	SmartDashboard.putNumber("v2", v2);
    	SmartDashboard.putNumber("v3", v3);
    	SmartDashboard.putNumber("v4", v4);
    	
    	//find displacement
    	double newtime = Timer.getFPGATimestamp();
    	double time = newtime - oldTime;
    	oldTime = newtime;
    	
    	d1 += v1*maxSpeed*time;
    	d2 -= v2*maxSpeed*time;
    	d3 += v3*maxSpeed*time;
    	d4 -= v4*maxSpeed*time;
    	
    	//push new motor voltages to the Jaguars
    	canFL.set(d1);
    	canFR.set(d2);
    	canBL.set(d3);
    	canBR.set(d4);
    }
}
