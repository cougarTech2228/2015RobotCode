
package org.usfirst.frc.team2228.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Timer;

/**
 * MainClass
 */
public class Robot extends IterativeRobot {
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
	
	DriveBase drive;
	PowerDistributionPanel panel;
	Logger logger;
	
    public void robotInit() {
    	panel = new PowerDistributionPanel();
    	drive = new DriveBase(0/*joy*/, 12/*BR*/, 13/*BL*/, 10/*FL*/, 11/*FR*/);
    	logger = new Logger();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopInit(){
    	Logger.resetTime();
    }
    
    public void teleopPeriodic() {
    	panel.clearStickyFaults();
    	drive.mecanumDrive();
    	logger.log("1", drive.panel, 1000);
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    	
    }
    
}
