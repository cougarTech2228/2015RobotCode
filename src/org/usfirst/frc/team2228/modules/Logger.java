package org.usfirst.frc.team2228.modules;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import edu.wpi.first.wpilibj.Timer;

public class Logger
{
	static double initialTime = Timer.getFPGATimestamp(); //gets the inital time based off the time stamp
	static long lastTimeILogged = 0;//gets the last time the data was logged
	static File file;//the file being created
	public static boolean disable = true;
	public static boolean verbose = false;

	/**
	 * Resets the initial time.
	 */
	public static void resetTime()
	{
		initialTime = Timer.getFPGATimestamp();
	}

	/**
	 * Gets the time of the match
	 * 
	 * @param d
	 * @return the scrubbed string 
	 */
	
	private static String timeScrub(int d)
	{
		return String.valueOf(d / 60) + ":" + String.valueOf(d % 60);
	}
	
	/**
	 * Replaces all of the ";" with "-" to make things easier to read
	 * @param the date 
	 * @return the scrubbed date
	 */
	
	private static String dateScrub(String date)
	{
		return date.replaceAll(":", "-");
	}
	
	/**
	 * Creates the file that will have data uploaded into it later
	 * 
	 */
	
	public static void setUpFile()
	{
		String date = dateScrub(new Date().toString());
		file = new File("/U/Data" + date + ".txt");
		// if file doesn't exists, then create it
		if (!file.exists())
		{
			try
			{
				file.createNewFile();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Takes the message you want to add to the file
	 * 
	 * @param message
	 */
	
	public static void log(String message)
	{
		long timePassed = System.currentTimeMillis() - lastTimeILogged;
		if (timePassed > 1000)
		{
			lastTimeILogged = System.currentTimeMillis();
			StackTraceElement[] stack = Thread.currentThread().getStackTrace();
			String module = stack[-2].getClassName();
			String time = timeScrub((int) (Timer.getFPGATimestamp() - initialTime));
			String content = time + " " + module + " >>> " + message;
			printf(content);
		}
	}

	public static void printf(String message)
	{
		try (FileOutputStream fop = new FileOutputStream(file, true))
		{
			// get the content in bytes
			byte[] contentInBytes = message.getBytes();
			fop.write(contentInBytes);
			fop.write(System.getProperty("line.separator").getBytes());
			fop.flush();
			fop.close();
			System.out.println("Done");
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}