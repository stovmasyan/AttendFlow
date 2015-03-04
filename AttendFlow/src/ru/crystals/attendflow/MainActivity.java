package ru.crystals.attendflow;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ru.crystals.attendflow.ds.StateType;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends MyActionBarActivity {
	private TextView txCashNum;
	private Integer cashNum;
	private Integer age;
	private TextView txBirthay;
	private Calendar date;
	private SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy",
			Locale.getDefault());

	private GestureDetectorCompat mDetector;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		this.mDetector.onTouchEvent(event);
		return super.onTouchEvent(event); 
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WebController.INSTANCE.addListener(this);
		if (getSupportActionBar() != null) {
			getSupportActionBar().hide();
		}
		mDetector = new GestureDetectorCompat(this, new MyGestureListener());

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
		setContentView(R.layout.activity_main);

		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(500);

		try {
			Uri notification = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
					notification);
			r.play();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Intent myIntent = getIntent();

		setCashNum(myIntent.getIntExtra("cashNum", -1));
		setAge(myIntent.getIntExtra("age", 0));

		txCashNum = (TextView) findViewById(R.id.textView1);
		txCashNum.setText("КАССА №" + cashNum);

		txCashNum = (TextView) findViewById(R.id.TextView01);
		txCashNum.setText("старше " + age + " лет, дата рождения");

		txBirthay = (TextView) findViewById(R.id.TextView02);
		txBirthay.setText("ранее " + getBirthDate());
	}

	public void sendMessage(View view) {
		WebController.INSTANCE.confirmAge(String.valueOf(cashNum));
	}

	@Override
	public void eventStateChanged(int cashNum, int age, StateType state) {
		if (isActivityActive() && StateType.WAIT_REQ.equals(state)) {
			setActivityActive(false);
			Intent intent = new Intent(this, WaitReqActivity.class);
			MainActivity.this.startActivity(intent);
		}
	}

	@Override
	public void onBackPressed() {
	}

	private String getBirthDate() {
		date = Calendar.getInstance();
		date.add(Calendar.YEAR, -1 * age.intValue());
		return formatter.format(date.getTime());
	}

	public Integer getCashNum() {
		return cashNum;
	}

	public void setCashNum(Integer cashNum) {
		this.cashNum = cashNum;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
		private static final String DEBUG_TAG = "Gestures";

		@Override
		public boolean onDown(MotionEvent event) {
			Log.d(DEBUG_TAG, "onDown: " + event.toString());
			return true;
		}

		@Override
		public boolean onFling(MotionEvent event1, MotionEvent event2,
				float velocityX, float velocityY) {
			WebController.INSTANCE.confirmAge(String.valueOf(cashNum));
			Log.d(DEBUG_TAG,
					"onFling: " + event1.toString() + event2.toString());
			return true;
		}
	}

}
