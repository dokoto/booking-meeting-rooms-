package rest.sap.structs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataRange
{
	private final static String dateTemplateSAP = "yyyyMMdd";
	private final static String dateTemplate = "yyyy-MM-dd";
	private final static String timeTemplateSAP = "HHmmss";
	private final static String timeTemplate = "HH:mm:ss.S";
	private String beginDate;
	private String endDate;
	private String beginTime;
	private String endTime;
	
	public void beginTime(Date date)
	{
		beginDate = new SimpleDateFormat(timeTemplate).format(date);
	}
	
	public void beginTime(String date) throws ParseException
	{
		beginDate = new SimpleDateFormat(timeTemplate).format(date);
	}
	
	public void endTime(Date date)
	{
		beginDate = new SimpleDateFormat(dateTemplate).format(date);
	}
	
	public void endTime(String date) throws ParseException
	{
		beginDate = new SimpleDateFormat(dateTemplate).format(date);
	}
	
	public void beginDate(Date date)
	{
		beginDate = new SimpleDateFormat(timeTemplate).format(date);
	}
	
	public void beginDate(String date) throws ParseException
	{
		beginDate = new SimpleDateFormat(timeTemplate).format(date);
	}
	
	public void endDate(Date date)
	{
		beginDate = new SimpleDateFormat(dateTemplate).format(date);
	}
	
	public void endDate(String date) throws ParseException
	{
		beginDate = new SimpleDateFormat(dateTemplate).format(date);
	}
	
	
	public String beginDateSAP()
	{
		return new SimpleDateFormat(dateTemplateSAP).format(beginDate);
	}
	
	public String endDateSAP()
	{
		return new SimpleDateFormat(dateTemplateSAP).format(endDate);
	}
	
	public String beginTimeSAP()
	{
		return new SimpleDateFormat(timeTemplateSAP).format(beginTime);
	}
	
	public String endTimeSAP()
	{
		return new SimpleDateFormat(timeTemplateSAP).format(endTime);
	}
}
