package org.usfirst.frc.team2228.modules;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.hal.CanTalonSRX;

public class ToteElevator
{
	CanTalonSRX tal;
	static DigitalInput di1;
	static DigitalInput di2;
	static DigitalInput di3;
	static DigitalInput di4;
	static DigitalInput di5;
	double curPos = 0;
	double status = 0;

	/*
	 * UP = 1 DOWN = -1 NO MOVE = 0 MAN_UP = 0.5 MAN_DOWN = -0.5
	 */
	public ToteElevator()
	{
		tal = new CanTalonSRX(16);
		di1 = new DigitalInput(1);
		di2 = new DigitalInput(2);
		di3 = new DigitalInput(3);
		di4 = new DigitalInput(4);
		di5 = new DigitalInput(5);
	}

	public static int checkStage()
	{
		if (di1.get())
		{
			return 1;
		}
		else if (di2.get())
		{
			return 2;
		}
		else if (di3.get())
		{
			return 3;
		}
		else if (di4.get())
		{
			return 4;
		}
		else if (di5.get())
		{
			return 5;
		}
		else
		{
			return 0;
		}
	}

	public void manualMove(int s)
	{
		trackLocation();
		if (s == 1)
		{
			status = 0.5;
			tal.Set(1.0);
		}
		else if (s == -1)
		{
			status = -0.5;
			tal.Set(-1.0);
		}
		else
		{
			status = 0;
			tal.Set(0.0);
		}
	}

	public boolean autoMove(int p)
	{
		if (status != 0.5 && status != -0.5)
		{
			if (p > curPos)
			{
				if (checkStage() == p)
				{
					tal.Set(0);
					return true;
				}
				else
				{
					tal.Set(1);
					return false;
				}
			}
			else if (p < curPos)
			{
				if (checkStage() == p)
				{
					tal.Set(0);
					return true;
				}
				else
				{
					tal.Set(-1);
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}

	private void trackLocation()
	{
		if (checkStage() == (int) (checkStage()))
		{
			curPos = checkStage();
		}
		else if (status == 1 || status == 0.5)
		{
			if (curPos - (int) (curPos) != 0.5)
			{
				curPos += 0.5;
			}
		}
		else if (status == -1 || status == -0.5)
		{
			if (curPos - (int) (curPos) != 0.5)
			{
				curPos -= 0.5;
			}
		}
	}
}