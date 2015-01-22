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
	
	static long lastTimeILogged = 0;
	static File file;
	
	
	public static boolean disable = true;
	public static boolean verbose = false;
		
	public static void resetTime(){
		initialTime = Timer.getFPGATimestamp();
	}
	
	private static String timeScrub(int d){
		return String.valueOf(d/60) + ":" + String.valueOf(d%60);
	}
	
	private static String dateScrub(String date){
		return date.replaceAll(":", "-");
		
	}
	
	public static void setUpFile(){
		String date = dateScrub(new Date().toString());
		file = new File("/U/Data" + date + ".txt");
		
		// if file doesn't exists, then create it
		if (!file.exists()) {
			try
			{
				file.createNewFile();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void log(String message){
			long timePassed = System.currentTimeMillis() - lastTimeILogged;
			if(timePassed > 1000){
				lastTimeILogged = System.currentTimeMillis();

				StackTraceElement[] stack = Thread.currentThread().getStackTrace();
				String module = stack[-2].getClassName();
				
				String time = timeScrub((int)(Timer.getFPGATimestamp()-initialTime));
				
				String content = time + " " + module + " >>> " + message; 
				
				printf(content);
			}
	}
	
	public static void printf(String message){
		try (FileOutputStream fop = new FileOutputStream(file, true)) {
			
			// get the content in bytes
			byte[] contentInBytes = message.getBytes();
			
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
