package ru.crystals.attendflow;

import ru.crystals.attendflow.ds.StateType;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MyActionBarActivity extends ActionBarActivity implements
		ServerListener {

	private boolean activityActive;

	public MyActionBarActivity() {
		super();
		WebController.INSTANCE.addListener(this);
		setActivityActive(true); 
	}

	public String getSetting(String name) {
		SharedPreferences settings = getBaseContext().getSharedPreferences(
				name, 0);
		return settings.getString(name, "");
	}

	public void saveSetting(String name, String value) {
		SharedPreferences settings = getBaseContext().getSharedPreferences(
				name, 0);
		settings = getBaseContext().getSharedPreferences(name, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(name, value);
		editor.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void eventStateChanged(int cashNum, int age, StateType state) {
		// TODO Auto-generated method stub
	}

	public boolean isActivityActive() {
		return activityActive;
	}

	public void setActivityActive(boolean activityActive) {
		this.activityActive = activityActive;
	}

}
