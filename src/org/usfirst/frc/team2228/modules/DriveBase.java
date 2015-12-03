package org.usfirst.frc.team2228.modules;

import java.util.Date;

import org.usfirst.frc.team2228.robot.Robot;
import org.usfirst.frc.team2228.robot.RobotMap;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/*
 * DriveBase class for controlling a mechanum wheels using a joystick
 * 
 * IF you have questions, send me a message: 754-3818
 * 
 * TODO encoders
 */
public class DriveBase{
	
	double autoDistanceMove = 9;
	
	boolean backUp = false;
	
	//This is the joystick object for getting input from the driver
    AdvancedJoystick joy;
	
    
    //These are the wheel objects, each represents a single wheel
    Wheel wheelBL;
    Wheel wheelBR;
	Wheel wheelFL;
    Wheel wheelFR;
	int counter = 0;
	int dank = 0;
    Gyro gyro;
	
    //specifies which direction is forward for the bot
	double direction = RobotMap.initialDirection;
	double time = 0;
	double initialTime = Timer.getFPGATimestamp();
	
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
//    	gyro = new Gyro(RobotMap.gyroPort);
    	
    	joy = new AdvancedJoystick(joyPort);
    	
    	//makes sure bypass is false so we can use all our super smooth input mapping
		joy.setBypass(false);
		
		//Initialize each wheel object with the CAN id, encoder counts, and a name for logging
        wheelFR = new Wheel(fR, RobotMap.COUNTS_PER_REV, "front right");
        wheelFL = new Wheel(fL, RobotMap.COUNTS_PER_REV, "front left");        
        wheelBR = new Wheel(bR, RobotMap.COUNTS_PER_REV, "back right");        
		wheelBL = new Wheel(bL, RobotMap.COUNTS_PER_REV, "back left");
		
		//invert this wheel, otherwise these wheels go backwards
		wheelFL.setInvert(true);
		wheelBL.setInvert(true);
		
		//uncomment some of these to test only a few wheels
		//wheelFL.enabled = false;
		//wheelFR.enabled = false;
		//wheelBL.enabled = false;
		//wheelBR.enabled = false;
    }
   
    int jiggleState = 0;
    long sTime = 0;
    boolean jiggleActivate = false;
    boolean lastJiggleActivate = false;
    
    public void doJiggleDrive()
    {
    	Date nowTime = new Date();
    	
    	if(joy.getRawButton(4))
    	{
    		jiggleActivate = true;
    	}
    	else
    	{
    		jiggleActivate = false;
    	}
    	
    	
    	if(lastJiggleActivate != jiggleActivate && jiggleActivate)
    	{
    		jiggleState = 0;
    	}
    	
    	if(jiggleActivate)
    	{
    		switch(jiggleState)
    		{
    			case 0:
    				sTime = nowTime.getTime();
    				jiggleState = 1;
    				break;
    				
    			case 1:
    				if(nowTime.getTime() >= sTime + 100)
    				{
    					jiggleState = 2;
    					sTime = nowTime.getTime();
    				}
    				else
    				{
    					wheelFL.setVoltage(0.3);
    					wheelFR.setVoltage(-0.3);
    					wheelBL.setVoltage(-0.3);
    					wheelBR.setVoltage(0.3);
    				}
    				break;
    				
    			case 2:
    				if(nowTime.getTime() >= sTime + 100)
    				{
    					jiggleState = 1;
    					sTime = nowTime.getTime();
    				}
    				else
    				{
    					wheelFL.setVoltage(-0.3);
    					wheelFR.setVoltage(0.3);
    					wheelBL.setVoltage(0.3);
    					wheelBR.setVoltage(-0.3);
    				}
    				break;
    				
    			default:
    				jiggleActivate = false;
    				break;
    		}
    	}
    	else
    	{
    		if(lastJiggleActivate != jiggleActivate)
    		{
    			wheelFL.setVoltage(0);
				wheelFR.setVoltage(0);
				wheelBL.setVoltage(0);
				wheelBR.setVoltage(0);
    		}
    	}
    	
    	lastJiggleActivate = jiggleActivate;
    }
    
    /**
     * function to drive the robot during tele-op
     * 
     * @param time the time taken for the previous iteration (heart beat of the robot) in seconds 
     */
    public void mecanumDrive(double time, int speedT, double rotateInput, double magInput, double dirInput){
    	
    	
    	//check if these buttons are pressed, if so set the rotational limit(sensitivity) to the appropriate value
    	if(joy.getRawButton(RobotMap.button_setRotation_one)){
    		joy.rLimit = RobotMap.rMode_limit_one;
    	}else if(joy.getRawButton(RobotMap.button_setRotation_two)){
    		joy.rLimit = RobotMap.rMode_limit_two;
    	}else if(joy.getRawButton(RobotMap.button_setRotation_three)){
    		joy.rLimit = RobotMap.rMode_limit_three;
    	}else if(joy.getRawButton(RobotMap.button_setRotation_four)){
    		joy.rLimit = RobotMap.rMode_limit_four;
    	}
    		
    	//TODO fix this
    	//check if the invertDrive button has been pressed, if so invert the drive
    	if (joy.getRawButton(RobotMap.button_invertDrive) /*&& RobotMap.useGyro_flg*/){
    		direction += Math.PI;
    	}
    	
    	double lMag;//linear magnitude (-1 to 1)
    	double dir;//direction (in radians)
    	double rotate;//rotational magnitude (-1 to 1)
    	    	
    	//get mag. dir. and rot. from the joystick
    	lMag = magInput;
    	dir = -dirInput;
    	rotate = rotateInput;
    	//add offset to rotate
    	/*if(rotate == 0){
    		rotate += getOffset(rotate);	
    	}*/
    	
    	
    	//log values in the dashboard
    	SmartDashboard.putNumber("angle", Math.toDegrees(dir));
    	SmartDashboard.putNumber("rotate", rotate);
    	SmartDashboard.putNumber("magnitude", lMag);
    	
    	/*if(joy.getPOV() == 0){
    		lMag = 0.5;
    		dir = 2*Math.PI;
    		
    	}else if(joy.getPOV() == 90){
    		lMag = 1;
    		dir = Math.PI/2;
    		
    	}else if(joy.getPOV() == 180){
    		lMag = 0.5;
    		dir = Math.PI;
    		
    	}else if(joy.getPOV() == 270){
    		lMag = 1;
    		dir = (3*Math.PI)/2;
    	}*/
    	//rotate = joy.getTwist();
    	moveDriveBase(lMag, dir, rotate);
		System.out.println("Mag: " + lMag + " Dir: " + dir + " Rotate: " + rotate);
		//update ramping
		wheelFL.update(time);
		wheelFR.update(time);
		wheelBR.update(time);
		wheelBL.update(time);
    }
    
    public void autoTest()
    {
    	wheelFR.setVoltage(0.3);
    	wheelFL.setVoltage(0.3);
    	wheelBR.setVoltage(0.3);
    	wheelBL.setVoltage(0.3);
    }
    
    /**
     * Autonomous update function
     * Moves robot and tote forward into auto zone
     */
    public void autonomous(double initialTime){
    	this.initialTime = initialTime;
    	if(Robot.mode == 1){
    		System.out.println("In mode 1 for Auto");
    		
    		wheelFR.setVoltage(0.3);
        	wheelFL.setVoltage(0.3);
        	wheelBR.setVoltage(0.3);
        	wheelBL.setVoltage(0.3);
    		
	    	time = Timer.getFPGATimestamp() - initialTime;
	    	double autoLMag = 0.5;
        	double autoDir = Math.PI*2;
        	double autoRotate = 0;
        	
        	
	    	/*if(time <= 1){
	        	//get mag. dir. and rot. from the joystick
	        	double autoLMag = 0.5;
	        	double autoDir = 90;
	        	double autoRotate = 0;
	        	
	        	moveDriveBase(autoLMag, autoDir, autoRotate);
	        	
	    	}
	    	else{
		    	wheelFL.setPosition(autoDistanceMove*(4*Math.PI));
		    	wheelFR.setPosition(autoDistanceMove*(4*Math.PI));
		    	wheelBL.setPosition(autoDistanceMove*(4*Math.PI));
		    	wheelBR.setPosition(autoDistanceMove*(4*Math.PI));
    		}*/
    	}else if(Robot.mode == 2){
    		time = Timer.getFPGATimestamp() - initialTime;
    		double autoLMag = 0.5;
        	double autoDir = 0;
        	double autoRotate = 0;
    		if(time >= 2){
	        	autoLMag = 0;
	    	}
    		moveDriveBase(autoLMag, autoDir, autoRotate);
    	}
    }
    
    public void moveDriveBase(double lMag, double dir, double rotate){
    	double v1,v2,v3,v4;//voltages for each motor
    	
    	//set each motors percent speed based on the direction, magnitude and velocity
    	v1 = -(lMag*Math.sin(dir + (Math.PI/4)) - rotate);
    	v2 = -(lMag*Math.cos(dir + (Math.PI/4)) + rotate);
    	v3 = -(lMag*Math.cos(dir + (Math.PI/4)) - rotate);
    	v4 = -(lMag*Math.sin(dir + (Math.PI/4)) + rotate);
    	
    	System.out.println("ROTATE: " + rotate);
    	
    	//push new motor speed to the Talons
    	wheelFR.setVoltage(v1);
    	wheelFL.setVoltage(v2);
    	wheelBR.setVoltage(v3);
    	wheelBL.setVoltage(v4);
    	
    }
    
    public void setDriveSpeed(double setting)
    {
    	
    }
    
    /**
     * generates the offset used to compensate for unwanted rotation on the robot
     * 
     * @param desired the desired amount of rotation 
     * 
     * @return The rotational offset to apply(add) to the wheels rotation. In  
     */
    public double getOffset(double desired){
    	//grab the current rate of rotation of the bot
    	double rate = 0;/*gyro.getRate();*/
    	
    	//Simplest form of compensating
    	if(rate > 0){
    		return -.1;
    	}
    	
    	if(rate < 0){
    		return .1;
    	}
    	
    	return 0;
    }
}