package rest.sap.structs;

import java.text.ParseException;
import java.util.Date;

public class DateRange
{
	private DateM beginDate = new DateM();
	private DateM endDate = new DateM();
	private TimeM beginTime = new TimeM();
	private TimeM endTime = new TimeM();
	
	public void beginTime(Date time) throws ParseException
	{
		beginDate.set(time);
	}
	
	public void beginTime(String time) throws ParseException
	{
		beginDate.set(time);
	}
	
	public void endTime(Date time) throws ParseException
	{
		beginDate.set(time);
	}
	
	public void endTime(String time) throws ParseException
	{
		beginDate.set(time);
	}
	
	public void beginDate(Date date) throws ParseException
	{
		beginDate.set(date);
	}
	
	public void beginDate(String date) throws ParseException
	{
		beginDate.set(date);
	}
	
	public void endDate(Date date) throws ParseException
	{
		beginDate.set(date);
	}
	
	public void endDate(String date) throws ParseException
	{
		beginDate.set(date);
	}
	
	
	public String beginDateSAP()
	{
		return beginDate.getSAPAsString();
	}
	
	public String endDateSAP()
	{
		return endDate.getSAPAsString();
	}
	
	public String beginTimeSAP()
	{
		return beginTime.getSAPAsString();
	}
	
	public String endTimeSAP()
	{
		return endTime.getSAPAsString();
	}
}
