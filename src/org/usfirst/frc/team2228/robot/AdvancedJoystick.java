//sorry no javadoc yet

public class AdvancedJoystick extends Joystick(){
	double rotation = 0;
	
	Mode rMode;
	Mode lMode;
	
	public AdvancedJoystick(port){
		super(port);
		rMode = new Mode();
		lMode = new Mode();
	}
	
	public double getDirectionRadians(){
		return super.getDirection() + rotation + invert ? Math.PI : 0;
	}
	
	public double getMagnitude(){
		double basic = super.getMagnitude();
		int negative = 1;
		if (basic < 0){
			basic *= -1;
			negative = -1;
		}	
	
		if(basic <= lMode.min){
			return 0;
		}
		else if(basic >= lMode.max){
			return lMode.limit;
		}
		
		basic -= lMode.min;
		basic = basic*(lMode.max - lMode.min)/(1 - lMode.min);
		
		double r = 1 / (Math.sqrt(2)*sin(2*Math.atan(1/(Math.sqrt(2)*lMode.curvature))));
		double xc = 1/2 + (lMode.curvature-r)/Math.sqrt(2);  
		double yc = 1/2 + (r-lMode.curvature)/Math.sqrt(2);

		double output = Math.sqrt(r**2 - (basic - xc)**2) + yc;
		double output *= lMode.limit*negative;
		
		return output;
	}
	
	public double getTwist(){
		double basic = super.getTwist();
		int negative = 1;
		if (basic < 0){
			basic *= -1;
			negative = -1;
		}
		
		if(basic <= rMode.min){
			return 0;
		}
		else if(basic >= rMode.max){
			return rMode.limit;
		}
		
		basic -= rMode.min;
		basic = basic*(rMode.max - rMode.min)/(1 - rMode.min);
		
		double r = 1 / (Math.sqrt(2)*sin(2*Math.atan(1/(Math.sqrt(2)*rMode.curvature))));
		double xc = 1/2 + (rMode.curvature-r)/Math.sqrt(2);  
		double yc = 1/2 + (r-rMode.curvature)/Math.sqrt(2);

		double output = Math.sqrt(r**2 - (basic - xc)**2) + yc;
		double output *= rMode.limit * negative;
		
		return output;
	}
	
	public boolean setLinearMode(double min, double max, double limit, double curvature, boolean invert){
		if(min > max || min < 0 || max > 1 || limit < 0 || curvature > .29){
			return false;
		}
		
		lMode.min = min;
		lMode.max = max;
		lMode.limit = limit;
		lMode.curvature = curvature;
		lMode.invert = invert;
		
		return true;
	}
	
	public boolean setRotationalMode(double min, double max, double limit, double curvature, boolean invert){
		if(min > max || min < 0 || max > 1 || limit < 0 || curvature > .29){
			return false;
		}
		
		rMode.min = min;
		rMode.max = max;
		rMode.limit = limit;
		rMode.curvature = curvature;
		rMode.invert = invert;
		
		return true;
	}
	
	public void defaultMode(){
		setLinearMode(0,.95,1,.05,false);
		setRotationalMode(.1,1,.8,.2,false);
	}
}

class Mode{	
	public double max = 1;
	public double min = 0;
	public double limit = 1;
	public double curvature = 0;
	public boolean invert = false;
	
	public Mode(){
		
	}
	
	public Mode()(double min, double max, double limit, double curvature, boolean invert){
		if(min > max || min < 0 || max > 1 || limit < 0 || curvature > .29){
			return false;
		}
		
		this.min = min;
		this.max = max;
		this.limit = limit;
		this.curvature = curvature;
		this.invert = invert;
		
		return true;
	}	
}

