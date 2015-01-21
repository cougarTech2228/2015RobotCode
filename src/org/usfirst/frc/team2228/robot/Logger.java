package org.usfirst.frc.team2228.robot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
 
public class Logger{
	
	static double initialTime = Timer.getFPGATimestamp();
	
	static String mod = "DriveBase";
	
	private static String Scrub(String s){
		s = s.replace(":", "-");
		return s;
	}
	
	private static String Scrub(int d){
		
		String s;
		if(d >= 60 && d < 120){
			
			s = "1:" + (d-60);
			
		}else if(d >=120){
			
			s = "2:" + (6-120);
			
		}else{
			
			s = "0:" + d;
			
		}
		
		return s;
	}
	
	public static void resetTime(){
		initialTime = Timer.getFPGATimestamp();
	}
	
	public static int getData(){
		int x = (int)(Math.random()*100)+1;
		return x;
	}

	
	static boolean go = true;
	static long lastTimeILogged = 0;
	public static void log(String s, double inP, int howOften){
			
			long timePassed = System.currentTimeMillis() - lastTimeILogged;
			if(timePassed > howOften){
				lastTimeILogged = System.currentTimeMillis();
				DriverStation ds = DriverStation.getInstance();

				Date nowTime = new Date();
				String g = Scrub(nowTime.toString());
				
				double matchTime = Timer.getFPGATimestamp()-initialTime;
				
				System.out.println(matchTime);
				
				String content = Scrub((int)(matchTime)) + " >>> POWER USAGE: " + inP;
				File file = new File("/U/Data" + s + ".txt");
				boolean append = true;
				
				try (FileOutputStream fop = new FileOutputStream(file, append)) {
					
					// if file doesn't exists, then create it
					if (!file.exists()) {
						file.createNewFile();
					}
					
					// get the content in bytes
					byte[] contentInBytes = content.getBytes();
					
					fop.write(contentInBytes);
					fop.write(System.getProperty("line.separator").getBytes());
					fop.flush();
					fop.close();
					
					System.out.println("Done");
		 
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
	}
}
