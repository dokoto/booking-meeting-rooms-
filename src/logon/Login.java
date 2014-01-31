package logon;

import tabs.MainContainer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.stratesys.mbrsTEST.R;

public class Login extends Activity
{
	private Button bt_login;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layo_login);	
		bt_login = (Button) findViewById(R.id.layo_login_bt_login);
		bt_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(v.getContext(), MainContainer.class);
			    startActivity(intent);		
			}});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}	
}
