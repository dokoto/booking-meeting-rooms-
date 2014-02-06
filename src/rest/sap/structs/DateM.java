package rest.sap.structs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateM
{
	private Date valueDateSAP, valueDateNormal;
	private String valueStringSAP, valueStringNormal;
	private final static String dateTemplateSAP = "yyyyMMdd";
	private final static String dateTemplate = "yyyy-MM-dd";
	
	public DateM()
	{
	}
	
	public DateM(Date value) throws ParseException
	{
		this.set(value);
	}
	
	public DateM(String value) throws ParseException
	{
		this.set(value);
	}

	public void set(String value) throws ParseException
	{
		this.valueDateNormal = new SimpleDateFormat(dateTemplate).parse(value);
		this.valueDateSAP = new SimpleDateFormat(dateTemplateSAP).parse(value);
		this.valueStringNormal = new SimpleDateFormat(dateTemplate).format(value);
		this.valueStringSAP = new SimpleDateFormat(dateTemplateSAP).format(value);
	}
	
	public void set(Date value) throws ParseException
	{
		this.valueDateNormal = new SimpleDateFormat(dateTemplate).parse(value.toString());
		this.valueDateSAP = new SimpleDateFormat(dateTemplateSAP).parse(value.toString());
		this.valueStringNormal = new SimpleDateFormat(dateTemplate).format(value);
		this.valueStringSAP = new SimpleDateFormat(dateTemplateSAP).format(value);
	}
	
	public String getSAPAsString()
	{
		return this.valueStringSAP;
	}
	
	public Date getSAPAsDate()
	{
		return this.valueDateSAP;
	}
	
	public String getNormalAsString()
	{
		return this.valueStringNormal;
	}
	
	public Date getNormalAsDate()
	{
		return this.valueDateNormal;
	}
}
