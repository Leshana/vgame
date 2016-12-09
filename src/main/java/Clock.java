///////////////////////////////////////////////////////////////////////////////////////////////////
//
// Clock.java
//
///////////////////////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////////////////////////
public class Clock
{
	// Clock which stores time in minutes.  Uses a uniform calendar, too.  Since we're counting
	//  minutes, this clock will take approximately 1 million years to wrap a signed int.
	private int m_time;
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	// Conversion values
	public static final int MONTHS_PER_YEAR = 12;
	
	public static final int DAYS_PER_MONTH = 30;
	public static final int DAYS_PER_YEAR = DAYS_PER_MONTH * MONTHS_PER_YEAR;
	
	public static final int HOURS_PER_DAY = 24;
	public static final int HOURS_PER_MONTH = HOURS_PER_DAY * DAYS_PER_MONTH;
	public static final int HOURS_PER_YEAR = HOURS_PER_DAY * DAYS_PER_YEAR;
	
	public static final int MINUTES_PER_HOUR = 60;
	public static final int MINUTES_PER_DAY = MINUTES_PER_HOUR * HOURS_PER_DAY;
	public static final int MINUTES_PER_MONTH = MINUTES_PER_HOUR * HOURS_PER_MONTH;
	public static final int MINUTES_PER_YEAR = MINUTES_PER_HOUR * HOURS_PER_YEAR;
	
	public static final String[] MONTH_NAME = new String[] {
		"Carnuary", "Feedruary", "Beltch", "Eatpril", "Mmmmmay",
		"Drooln", "Jawly", "Smorgust", "Slurptember", "Gulptober",
		"Nomvember", "Digestember" };
	
	// Start time is factored in for display purposes, and represents the date/time at the start
	//  of the game (complete guess on my part).  Totally a compatibility hack.
	public static final int START_YEAR = 588;
	public static final int START_MONTH = 4;
	public static final int START_DAY = 12;
	public static final int START_MINUTES = MINUTES_PER_DAY / 4; // The game starts at dawn
	
	public static final int START_TIME = START_YEAR * MINUTES_PER_YEAR
										+ START_MONTH * MINUTES_PER_MONTH
										+ START_DAY * MINUTES_PER_DAY
										+ START_MINUTES;
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public Clock()
	{
		this( 1, 1, 1, 0, 0 );
	}
	
	public Clock( int time )
	{
		m_time = time;
	}
	
	public Clock( int year, int month, int day, int hour, int minute )
	{
		setTime( year, month, day, hour, minute );
	}
	
	public Clock( Clock set )
	{
		m_time = set.m_time;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void addTime( int time )
	{
		m_time += time;
	}
	
	public void addTime( int year, int month, int day, int hour, int minute )
	{
		m_time += year * MINUTES_PER_YEAR
				+ month * MINUTES_PER_MONTH
				+ day * MINUTES_PER_DAY
				+ hour * MINUTES_PER_HOUR
				+ minute;
	}
	
	public void setTime( int time )
	{
		m_time = time;
	}
	
	public void setTime( int year, int month, int day, int hour, int minute )
	{
		m_time =  year * MINUTES_PER_YEAR
				+ month * MINUTES_PER_MONTH
				+ day * MINUTES_PER_DAY
				+ hour * MINUTES_PER_HOUR
				+ minute;
	}
	
	public int getTime()
	{
		return m_time;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public int getDayMinutes()
	{
		// The day-minute is how the rest of the game saw this object up until recently.
		return m_time % MINUTES_PER_DAY;
	}
	
	public int getDisplayMinutes()
	{
		// Hack for display purposes due to how time is treated in the engine.
		return (m_time + START_TIME) % MINUTES_PER_DAY;
	}
	
	public int getMinutes()
	{
		return m_time % MINUTES_PER_HOUR;
	}
	
	public int getHours()
	{
		return (m_time % MINUTES_PER_DAY) / MINUTES_PER_HOUR;
	}
	
	public int getDays()
	{
		return (m_time % MINUTES_PER_MONTH) / MINUTES_PER_DAY;
	}
	
	public int getMonths()
	{
		return (m_time % MINUTES_PER_YEAR) / MINUTES_PER_MONTH;
	}
	
	public int getYears()
	{
		return m_time / MINUTES_PER_YEAR;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public int getNextDawn()
	{
		return MINUTES_PER_DAY - (m_time % MINUTES_PER_DAY);
		//return MINUTES_PER_DAY - ((m_time + ((MINUTES_PER_DAY * 3) / 4)) % MINUTES_PER_DAY);
	}
	
	public int getTimeUntilDawn()
	{
		return getNextDawn() - m_time;
	}
	
	public boolean isDay()
	{
		return getNextDawn() > (MINUTES_PER_DAY / 2);
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public int getNextDusk()
	{
		return MINUTES_PER_DAY - ((m_time + MINUTES_PER_DAY / 2) % MINUTES_PER_DAY);
		//return MINUTES_PER_DAY - ((m_time + (MINUTES_PER_DAY / 4)) % MINUTES_PER_DAY);
	}
	
	public int getTimeUntilDusk()
	{
		return getNextDusk() - m_time;
	}
	
	public boolean isNight()
	{
		return getNextDusk() > (MINUTES_PER_DAY / 2);
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public String getTimeString()
	{
		java.text.NumberFormat nf = java.text.NumberFormat.getInstance(); 
		nf.setMinimumIntegerDigits( 2 );
		
		m_time += START_TIME;
		
		int h = getHours();
		
		// Determine if we're AM or PM.
		String post = (h >= HOURS_PER_DAY / 2) ? " PM" : " AM";
		
		// Hours loop at the low end (0:00 displays as 12:00, etc);
		h %= HOURS_PER_DAY / 2;
		if( h == 0 )
		{
			h = HOURS_PER_DAY / 2;
		}
		
		String ret = h + ":" + nf.format( getMinutes() ) + post;
		
		m_time -= START_TIME;

		return ret;
	}
	
	public String getDateString()
	{
		m_time += START_TIME;
		String ret = MONTH_NAME[getMonths()] + " " + getDays() + ", " + getYears() + " PD";
		m_time -= START_TIME;

		return ret;
	}
}
