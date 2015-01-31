package org.usfirst.frc.team2228.robot;

import edu.wpi.first.wpilibj.Joystick;

/**
 *A Class extending joystick meant for modifying how input is viewed
 *
 *@param rMode settings for modifying rotation input
 *@param lMode settings for modifying rotation input
 *@param bypass set this flag to true to disable input mapping 
 **/

public class AdvancedJoystick extends Joystick{
	double rotation = Math.PI;
	
	Mode rMode;
	Mode lMode;
	
	boolean bypass = false;

	/**
	 * creates an Advanced Joystick on port <port>
	 * 
	 * @param port the index of the joystick
	 */
	public AdvancedJoystick(int port){
		super(port);
		rMode = new Mode();
		lMode = new Mode();
	}
	
	/**
	 * get the direction of the joystick
	 * 
	 * @see edu.wpi.first.wpilibj.Joystick#getDirectionRadians()
	 **/
	public double getDirectionRadians(){
		return super.getDirectionRadians() + rotation + (lMode.invert ? Math.PI : 0);
	}
	
	/**
	 * get the mapped magnitude of the joystick
	 * 
	 * @see edu.wpi.first.wpilibj.Joystick#getMagnitude()
	 */
	public double getMagnitude(){
		double basic = super.getMagnitude();
		
		if(bypass){
			return basic;
		}
		
		int negative = 1;
		if (basic < 0){
			basic *= -1;
			negative = -1;
		}	
	
		if(basic <= lMode.min){
			return 0;
		}
		
		else if(basic >= lMode.max){
			return lMode.limit*negative;
		}
		
		//this will turn basic into a percent (out of one) from min to max
		double linear = (basic - lMode.min)/(lMode.max-lMode.min); 
		
		//the value of this input on a unit circle centered at 0,1
		double curve = 1 + -Math.sqrt(1-Math.pow(linear,2));
		
	    double output = (curve*lMode.curvature + linear*(1-lMode.curvature))*negative*lMode.limit;
		
	    return output;
	}
	
	/*
	 * get the mapped rotational magnitude of the joystick
	 * 
	 * @see edu.wpi.first.wpilibj.Joystick#getTwist()
	 */
	public double getTwist(){
		double basic = super.getTwist();
		
		if(bypass){
			return basic;
		}
		
		int negative = rMode.invert?-1:1;
		if (basic < 0){
			basic *= -1;
			negative = -1;
		}
		
		if(basic <= rMode.min){
			return 0;
		}
		
		else if(basic >= rMode.max){
			return rMode.limit*negative;
		}
		
		//this will turn basic into a percent (out of one) from min to max
		double linear = (basic - rMode.min)/(rMode.max-rMode.min);
		
		//the value of this input on a unit circle centered at 0,1
		double curve = 1 + -Math.sqrt(1-Math.pow(linear,2));
		
	    double output = (curve*rMode.curvature + linear*(1-rMode.curvature))*negative*rMode.limit;
	    
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
	public boolean setLinearMode(double min, double max, double limit, double curvature, boolean invert){
		if(min > max || min < 0 || max > 1 || limit < 0 || curvature > 1 || curvature < 0){
			return false;
		}
		
		lMode.min = min;
		lMode.max = max;
		lMode.limit = limit;
		lMode.curvature = curvature;
		lMode.invert = invert;
		
		return true;
	}
	
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
		
		rMode.min = min;
		rMode.max = max;
		rMode.limit = limit;
		rMode.curvature = curvature;
		rMode.invert = invert;
		
		return true;
	}
	
	/**
	 *sets joystick modes to default values 
	 **/
	public void defaultMode(){
		setLinearMode(.1,.9,1,.2,false);
		setRotationalMode(.3,1,.65,.8,false);
	}
	
	/**
	 *sets joystick modes to basic values (direct input) 
	 **/
	public void basicMode(){
		setLinearMode(0,1,1,0,false);
		setRotationalMode(0,1,1,0,false);
	}
	
	/**
	 * set the bypass flag
	 * 
	 * @param bool new value for bypass
	 */
	public void setBypass(boolean bool){
		bypass = bool;
	}
}

class Mode{	
	public double max = 1;
	public double min = 0;
	public double limit = .5;
	public double curvature = 0;
	public boolean invert = false;
	
	public Mode(){
		
	}
	
	public Mode(double min, double max, double limit, double curvature, boolean invert){
		if(min > max || min < 0 || max > 1 || limit < 0 || curvature > 1){
			return;
		}
		
		this.min = min;
		this.max = max;
		this.limit = limit;
		this.curvature = curvature;
		this.invert = invert;
	}	
}
