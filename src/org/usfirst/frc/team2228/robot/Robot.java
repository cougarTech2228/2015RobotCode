
package org.usfirst.frc.team2228.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

/**
 * MainClass
 */
public class Robot extends IterativeRobot {
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
	
	DriveBase drive;
	PowerDistributionPanel panel = new PowerDistributionPanel();
	
    public void robotInit() {
    	panel.clearStickyFaults();
    	drive = new DriveBase(0/*joy*/, 6/*BR*/, 7/*BL*/, 8/*FL*/, 9/*FR*/);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	drive.mecanumDrive();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    	
    }
    
}
