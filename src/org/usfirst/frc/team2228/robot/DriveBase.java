package org.usfirst.frc.team2228.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/*
 * DriveBase class for controlling a mechanum wheels using a joystick
 * TODO encoders
 */
public class DriveBase{
	
	//This is the joystick object for getting input from the driver
    AdvancedJoystick joy;
	
    //These are the wheel objects, each represents a single wheel
    Wheel wheelBL;
    Wheel wheelBR;
	Wheel wheelFL;
    Wheel wheelFR;
	
    //specifies which direction is forward for the bot
	double direction = Parameters.initialDirection;
	
	/**
	 * constructs a new Drivebase object
	 * 
	 * @param joyPort the port for the joystick which will be used
	 * @param bR the CAN id for the back right wheel's jaguar
	 * @param bL the CAN id for the back left wheel's jaguar
	 * @param fL the CAN id for the front right wheel's jaguar
	 * @param fR the CAN id for the front left wheel's jaguar
	 */
    public DriveBase(int joyPort, int fR, int fL, int bR, int bL){
    	//creates the AdvancedJoystick object
    	joy = new AdvancedJoystick(joyPort);
    	
    	//makes sure bypass is false so we can use all our super smooth input mapping
		joy.setBypass(false);
		
		//initialise each wheel object with the CAN id, encoder counts, and a name for logging
        wheelFR = new Wheel(fR, Parameters.COUNTS_PER_REV, "front right");
        wheelFL = new Wheel(fL, Parameters.COUNTS_PER_REV, "front left");        
        wheelBR = new Wheel(bR, Parameters.COUNTS_PER_REV, "back right");        
		wheelBL = new Wheel(bL, Parameters.COUNTS_PER_REV, "back left");
		
		//invert this wheel, otherwise these wheels go backwards
		wheelFL.setInvert(true);
		wheelBL.setInvert(true);
		
		//uncomment some of these to test only a few wheels
		//wheelFL.enabled = false;
		//wheelFR.enabled = false;
		//wheelBL.enabled = false;
		//wheelBR.enabled = false;
    }
   
    /**
     * function to drive the robot during tele-op
     * 
     * @param time the time taken for the previous iteration (heart beat of the robot) in seconds 
     */
    public void mecanumDrive(double time){
    	
    	//check if these buttons are pressed, if so set the rotational limit(sensitivity) to the appropriate value
    	if(joy.getRawButton(Parameters.button_setRotation_one)){
    		joy.rLimit = Parameters.rMode_limit_one;
    	}else if(joy.getRawButton(Parameters.button_setRotation_two)){
    		joy.rLimit = Parameters.rMode_limit_two;
    	}else if(joy.getRawButton(Parameters.button_setRotation_three)){
    		joy.rLimit = Parameters.rMode_limit_three;
    	}else if(joy.getRawButton(Parameters.button_setRotation_four)){
    		joy.rLimit = Parameters.rMode_limit_four;
    	}
    		
    	//TODO fix this
    	//check if the invertDrive button has been pressed, if so invert the drive
    	if (joy.getRawButton(Parameters.button_invertDrive)){
    		direction += Math.PI;
    	}
    	
    	double lMag;//linear magnitude (-1 to 1)
    	double dir;//direction (in radians)
    	double rotate;//rotational magnitude (-1 to 1)
    	double v1,v2,v3,v4;//voltages for each motor
    	    	
    	//get mag. dir. and rot. from the joystick
    	lMag = joy.getMagnitude();
    	dir = -joy.getDirectionRadians() + direction;
    	rotate = joy.getTwist();
    	
    	//log values in the dashboard
    	SmartDashboard.putNumber("angle", Math.toDegrees(dir));
    	SmartDashboard.putNumber("rotate", rotate);
    	SmartDashboard.putNumber("magnitude", lMag);

    	//set each motors percent speed based on the direction, magnetude and velocity
    	v1 = lMag*Math.sin(dir + (Math.PI/4)) - rotate;
    	v2 = lMag*Math.cos(dir + (Math.PI/4)) + rotate;
    	v3 = lMag*Math.cos(dir + (Math.PI/4)) - rotate;
    	v4 = lMag*Math.sin(dir + (Math.PI/4)) + rotate;
    	
    	//push new motor speed to the Jaguars
    	wheelFR.setVoltage(v1);
		wheelFL.setVoltage(v2);
		wheelBR.setVoltage(v3);
		wheelBL.setVoltage(v4);
		
		//update ramping
		wheelFL.update(time);
		wheelFR.update(time);
		wheelBR.update(time);
		wheelBL.update(time);
    }
}
