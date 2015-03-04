package ru.crystals.attendflow;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import ru.crystals.attendflow.ds.StateType;
import android.os.AsyncTask;
import android.util.Log;

public enum WebController {
	INSTANCE;

	private String ip;
	private String port;
	private String password;

	private List<ServerListener> listeners;

	private String nameSpace = "http://attendflow.crystals.ru/";

	private String SOAP_ACTION_STATE = "\""
			+ "http://attendflow.crystals.ru/AttendFlow/getStateRequest" + "\"";
	private String methodState = "getState";
	
	private String SOAP_ACTION_CONFIRM = "\""
			+ "http://attendflow.crystals.ru/AttendFlow/androidConfirmRequest" + "\"";
	private String methodConfirm = "androidConfirm";

	private StateType stateType;
	private StateType previousState;

	private WebController() {
		listeners = new CopyOnWriteArrayList<ServerListener>();

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			AsyncCallGetState task;

			@Override
			public void run() {
				task = new AsyncCallGetState();
				task.execute();

			}
		}, 1000, 1000);

	}
	
	

	public List<ServerListener> getListeners() {
		return listeners;
	}

	public void addListener(ServerListener listener) {
		listeners.add(listener);
	}

	public void removeListener(ServerListener listener) {
		listeners.remove(listener);
	}
	
	public void confirmAge(String cashNum) {
		AsyncCallConfirm task = new AsyncCallConfirm(cashNum);
		task.execute();
		
	}
	
	private class AsyncCallConfirm extends AsyncTask<String, Void, Void> {
		private String TAG = "ATTENDFLOW";
		private String cashNum;
		
		
		public AsyncCallConfirm(String cashNum) {
			super();
			this.cashNum = cashNum;
		}

		@Override
		protected Void doInBackground(String... params) {
			Log.i(TAG, "doInBackground");
			confirmToServer();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Log.i(TAG, "onPostExecute");
		}

		@Override
		protected void onPreExecute() {
			Log.i(TAG, "onPreExecute");
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			Log.i(TAG, "onProgressUpdate");
		}

		private void confirmToServer() {
			SoapObject request = new SoapObject(nameSpace, methodConfirm);
			request.addProperty("arg0", AndroidID.INSTANCE.getId());
			request.addProperty("arg1", cashNum);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					"http://" + ip + ":" + port + "/AttendFlow?wsdl");
			
			

			try {
				androidHttpTransport.call(SOAP_ACTION_CONFIRM, envelope);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private class AsyncCallGetState extends AsyncTask<String, Void, Void> {
		private String TAG = "ATTENDFLOW";
		private Integer age;
		private Integer cashNum;

		@Override
		protected Void doInBackground(String... params) {
			Log.i(TAG, "doInBackground");
			reqStateToServer();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Log.i(TAG, "onPostExecute");
		}

		@Override
		protected void onPreExecute() {
			Log.i(TAG, "onPreExecute");
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			Log.i(TAG, "onProgressUpdate");
		}

		private void reqStateToServer() {
			SoapObject request = new SoapObject(nameSpace, methodState);
			request.addProperty("arg0", AndroidID.INSTANCE.getId());

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					"http://" + ip + ":" + port + "/AttendFlow?wsdl");
			Log.i(TAG, port);

			try {
				androidHttpTransport.call(SOAP_ACTION_STATE, envelope);
				SoapObject resultsRequestSOAP = (SoapObject) envelope
						.getResponse();

				stateType = StateType.valueOf(resultsRequestSOAP.getProperty(
						"stateType").toString());
				cashNum = Integer.valueOf(resultsRequestSOAP.getProperty(
						"cashNum").toString());
				age = Integer.valueOf(resultsRequestSOAP.getProperty("age")
						.toString());
				Log.i(TAG, "state = " + stateType + " cashNum = " + cashNum
						+ " age = " + age);

				if (previousState != stateType) {
					for (ServerListener listener : listeners) {
						listener.eventStateChanged(cashNum, age, stateType);
					}
					previousState = stateType;

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}



	

}
