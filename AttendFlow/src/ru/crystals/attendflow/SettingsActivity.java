package ru.crystals.attendflow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class SettingsActivity extends MyActionBarActivity {

	private TextView etIP;
	private TextView etPort;
	private TextView etPass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getSupportActionBar() != null) {
			getSupportActionBar().hide();
		}
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

		setContentView(R.layout.activity_settings);

		etIP = (TextView) findViewById(R.id.etIP);
		etPort = (TextView) findViewById(R.id.etPort);
		etPass = (TextView) findViewById(R.id.etPass);

		loadSettings();		
	}
	
//	@Override
//	protected void onStop() {
//		super.onStop();
//		setActivityActive(true);
//	}

	public void loadSettings() {
		etIP.setText(getSetting("ip"));
		etPort.setText(getSetting("port"));
		etPass.setText(getSetting("pass"));
	}

	public void saveSettings(View view) {
		saveSetting("ip", etIP.getText().toString());
		saveSetting("port", etPort.getText().toString());
		saveSetting("pass", etPass.getText().toString());

		Intent intent = new Intent(this, WaitReqActivity.class);
		SettingsActivity.this.startActivity(intent);
	}

	public void returnToWait(View view) {
		Intent intent = new Intent(this, WaitReqActivity.class);
		SettingsActivity.this.startActivity(intent);
	}

}
