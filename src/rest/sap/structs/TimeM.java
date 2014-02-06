package rest.sap.structs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeM
{

	private Date valueTimeSAP, valueTimeNormal;
	private String valueStringSAP, valueStringNormal;
	private final static String timeTemplateSAP = "HHmmss";
	private final static String timeTemplate = "HH:mm:ss.S";
	
	public TimeM()
	{
	}
	
	public TimeM(Date value) throws ParseException
	{
		this.set(value);
	}
	
	public TimeM(String value) throws ParseException
	{
		this.set(value);
	}

	public void set(String value) throws ParseException
	{
		this.valueTimeNormal = new SimpleDateFormat(timeTemplate).parse(value);
		this.valueTimeSAP = new SimpleDateFormat(timeTemplateSAP).parse(value);
		this.valueStringNormal = new SimpleDateFormat(timeTemplate).format(value);
		this.valueStringSAP = new SimpleDateFormat(timeTemplateSAP).format(value);
	}
	
	public void set(Date value) throws ParseException
	{
		this.valueTimeNormal = new SimpleDateFormat(timeTemplate).parse(value.toString());
		this.valueTimeSAP = new SimpleDateFormat(timeTemplateSAP).parse(value.toString());
		this.valueStringNormal = new SimpleDateFormat(timeTemplate).format(value);
		this.valueStringSAP = new SimpleDateFormat(timeTemplateSAP).format(value);
	}
	
	public String getSAPAsString()
	{
		return this.valueStringSAP;
	}
	
	public Date getSAPAsTime()
	{
		return this.valueTimeSAP;
	}
	
	public String getNormalAsString()
	{
		return this.valueStringNormal;
	}
	
	public Date getNormalAsTime()
	{
		return this.valueTimeNormal;
	}

}
