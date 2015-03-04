package ru.crystals.attendflow;

import ru.crystals.attendflow.ds.StateType;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.View;
import android.view.WindowManager;

public class WaitReqActivity extends MyActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		

		if (getSupportActionBar() != null) {
			getSupportActionBar().hide();
		}

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);

		WebController.INSTANCE.setIp(getSetting("ip"));
		WebController.INSTANCE.setPort(getSetting("port"));
		WebController.INSTANCE.setPassword(getSetting("pass"));

		setContentView(R.layout.activity_wait_req);

		if (AndroidID.INSTANCE.getId() == null) { 
			AndroidID.INSTANCE.setId(Secure.getString(
					this.getContentResolver(), Secure.ANDROID_ID));
		}

	}

	public void openSettings(View view) {
		Intent intent = new Intent(this, SettingsActivity.class);
		WaitReqActivity.this.startActivity(intent);
	}

//	@Override
//	protected void onStop() {
//		super.onStop();
//		WebController.INSTANCE.removeListener(this);
//		setActivityActive(false);
//	}

	@Override
	public void onBackPressed() {
	}

	@Override
	public void eventStateChanged(int cashNum, int age, StateType state) {
		if (isActivityActive() && StateType.AGE_REQ.equals(state)) {
			setActivityActive(false);
			Intent intent = new Intent(this, MainActivity.class);

			intent.putExtra("cashNum", cashNum);
			intent.putExtra("age", age);
			WaitReqActivity.this.startActivity(intent);
		}
	}

}
