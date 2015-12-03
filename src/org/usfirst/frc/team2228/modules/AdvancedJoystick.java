package org.usfirst.frc.team2228.modules;

import org.usfirst.frc.team2228.robot.RobotMap;
import edu.wpi.first.wpilibj.Joystick;

/**
 * A Class extending joystick meant for modifying how input is viewed 
 **/
public class AdvancedJoystick extends Joystick{
	
	//the current rotational limit (amount to rotate at full twist)
	double rLimit = RobotMap.rMode_limit_four;
	
	boolean bypass = false;

	/**
	 * creates an Advanced Joystick on port <port>
	 * 
	 * @param port the index of the joystick
	 */
	public AdvancedJoystick(int port){
		super(port);  
	}
	
	/**
	 * get the direction of the joystick 
	 * 
	 * @see edu.wpi.first.wpilibj.Joystick#getDirectionRadians()
	 **/
	@SuppressWarnings("deprecation")
	public double getDirectionRadians(){
		//if invert is false, add PI radians 
		//(logically should be the opposite, but isn't so, idk, don't change it unless it stops working)
		return super.getDirectionRadians() + (RobotMap.lMode_invert ? 0 : Math.PI);
	}
	
	/**
	 * get the mapped linear magnitude of the joystick
	 * 
	 * @see org.usfirst.frc.team2228.Parameters#lMode_curvature for info on how input is mapped
	 * @see edu.wpi.first.wpilibj.Joystick#getMagnitude()
	 */
	public double getMagnitude(){
		//this gets the direct input from the joystick
		double basic = super.getMagnitude();
		
		if(bypass){
			return basic;
		}
		
		//this will handle the possiblity of negative magnitudes
		int negative = 1;
		if (basic < 0){
			basic *= -1;
			negative = -1;
		}	
	
		//if the magnitude is less than min, return 0
		if(basic <= RobotMap.lMode_min){
			return 0;
		}
		
		//find the limit using the max and min limits, interpolating between them using the throttle on the joystick
		double limit = (RobotMap.lMode_maxLimit - RobotMap.lMode_minLimit) * (-1*this.getThrottle()+1) + RobotMap.lMode_minLimit;
		
		//if the magnitude is past the max, return the limit
		if(basic >= RobotMap.lMode_max){
			return limit*negative;
		}
		
		//this will turn basic into a percent (out of one) from min to max
		double linear = (basic - RobotMap.lMode_min)/(RobotMap.lMode_max-RobotMap.lMode_min); 
		
		//the value of this input on a unit circle centered at 0,1
		double curve = 1 + -Math.sqrt(1-Math.pow(linear,2));
		
		//calculate the output as a weighted average of the linear and curved models, using curvature as the weight
	    double output = (curve*RobotMap.lMode_curvature + linear*(1-RobotMap.lMode_curvature))*negative*limit;
		
	    return output;
	}
	
	/*
	 * get the mapped rotational magnitude of the joystick
	 * 
	 * get the mapped magnitude of the joystick
	 * 
	 * @see org.usfirst.frc.team2228.Parameters#rMode_curvature for info on how input is mapped
	 * @see edu.wpi.first.wpilibj.Joystick#getTwist()
	 */
	public double getTwist(){
		//this gets the direct input from the joystick
		double basic = super.getTwist();
		
		if(bypass){
			return basic;
		}
		
		//this will handle the possiblity of negative magnitudes
		int negative = RobotMap.rMode_invert?-1:1;
		if (basic < 0){
			basic *= -1;
			negative = -1;
		}
		
		//if the magnitude is less than min, return 0
		if(basic <= RobotMap.rMode_min){
			return 0;
		}
		
		//if the magnitude is past the max, return the limit
		else if(basic >= RobotMap.rMode_max){
			return rLimit*negative;
		}
		
		//this will turn basic into a percent (out of one) from min to max
		double linear = (basic - RobotMap.rMode_min)/(RobotMap.rMode_max-RobotMap.rMode_min);
		
		//the value of this input on a unit circle centered at 0,1
		double curve = 1 + -Math.sqrt(1-Math.pow(linear,2));
		
		//calculate the output as a weighted average of the linear and curved models, using curvature as the weight
	    double output = (curve*RobotMap.rMode_curvature + linear*(1-RobotMap.rMode_curvature))*negative*rLimit;
	    
		return output;
	}
	
	/**
	 * sets the linear modes
	 *
	 * @param min all joystick values up to this will map to zero (range 0-1)
	 * @param max all joystick values past this will map to <limit> (range 0-1)
	 * @param limit the max value for the magnitude (range 0-1)
	 * @param curvature how curved the input to output graph for the joystick is, (percent: range 0-1)
	  *@param invert whether or not to invert the joystick
	 **/
	@SuppressWarnings("deprecation")
	public boolean setLinearMode(double min, double max, double maxLimit, double minLimit, double curvature, boolean invert){
		if(min > max || min < 0 || max > 1 || maxLimit < 0 || minLimit < 0 || curvature > 1 || curvature < 0){
			return false;
		}
		
		RobotMap.lMode_min = min;
		RobotMap.lMode_max = max;
		RobotMap.lMode_maxLimit = maxLimit;
		RobotMap.lMode_minLimit = minLimit;
		RobotMap.lMode_curvature = curvature;
		RobotMap.lMode_invert = invert;
		
		return true;
	}
	
	//TODO fix the documentation of the fallowing methods
	
	/**
	 * sets the rotation modes
	 *
	 * @param min all joystick values up to this will map to zero (range 0-1)
	 * @param max all joystick values past this will map to <limit> (range 0-1)
	 * @param limit the max value for the magnitude (range 0-1)
	 * @param curvature how curved the input to output graph for the joystick is, (percent: range 0-1)
	  *@param invert whether or not to invert the joystick
	 **/
	public boolean setRotationalMode(double min, double max, double limit, double curvature, boolean invert){
		if(min > max || min < 0 || max > 1 || limit < 0 || curvature > 1 || curvature < 0){
			return false;
		}
		
		RobotMap.rMode_min = min;
		RobotMap.rMode_max = max;
		//Parameters.rMode_limit = limit;
		RobotMap.rMode_curvature = curvature;
		RobotMap.rMode_invert = invert;
		
		return true;
	}
	
	/**
	 *sets joystick modes to default values 
	 **/
	public void defaultMode(){
		setLinearMode(.1,.95,1,.2,.2,false);
		setRotationalMode(.3,1,.3,.8,false);
	}
	
	/**
	 *sets joystick modes to basic values (direct input) 
	 **/
	public void basicMode(){
		setLinearMode(0,1,1,1,0, false);
		setRotationalMode(0,1,1,0, false);
	}
	
	/**
	 * set the bypass flag 
	 * if true, getMagnitude() and getTwist() will return their relative values from the superclass, without mapping
	 * 
	 * @param bool new value for bypass
	 */
	public void setBypass(boolean bool){
		bypass = bool;
	}
}