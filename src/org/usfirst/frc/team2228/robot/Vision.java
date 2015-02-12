package org.usfirst.frc.team2228.robot;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ShapeMode;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.vision.AxisCamera;

public class Vision
{
	int session;
    Image frame;
    AxisCamera camera;
    NIVision.Rect rect = new NIVision.Rect(10, 10, 100, 100);
	
	public Vision()
	{
		frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		camera = new AxisCamera("10.22.28.21");		
	}
	
	public void drawTarget()
	{
		camera.getImage(frame);
        NIVision.imaqDrawShapeOnImage(frame, frame, rect, DrawMode.DRAW_VALUE, ShapeMode.SHAPE_OVAL, 0.0f);
        CameraServer.getInstance().setImage(frame);
	}
}
