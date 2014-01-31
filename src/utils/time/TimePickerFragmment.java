package utils.time;

import java.util.Calendar;
import android.app.Dialog;
import android.os.Bundle;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;

public class TimePickerFragmment extends DialogFragment
{

	OnTimeSetListener ontimeSet;
	
	public void setCallBack(OnTimeSetListener ondate)
	{
		ontimeSet = ondate;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		// Use the current time as the default values for the picker
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), ontimeSet, hour, minute, DateFormat.is24HourFormat(getActivity()));
	}
}
