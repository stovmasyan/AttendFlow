package ru.crystals.attendflow;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;

import ru.crystals.attendflow.ds.State;
import ru.crystals.attendflow.ds.StateType;

@WebService
public class AttendFlow {
	StateType currentState = StateType.WAIT_REQ;
	private boolean returnWaitReq = false;
	private int count = 5;
	
	public AttendFlow() {
		String address = "http://0.0.0.0:9000/AttendFlow";
		Endpoint.publish(address, this);
	}

	@WebMethod
	public State getState(String androidId) {
		State age = new State((new Random()).nextInt(15) + 1, 18, StateType.AGE_REQ);
		State wait = new State((new Random()).nextInt(15) + 1, 18, StateType.WAIT_REQ);
		if (returnWaitReq) {
			count--;
			if (count < 0) {
				returnWaitReq = false;
			}
			return wait;
		}
		
		return age;
	}
	
	@WebMethod
	public void androidConfirm(String androidId, int cashNum) {
		System.out.println(Calendar.getInstance().getTime() +  " --- androidConfirm(" + androidId + "," +  cashNum +")");
		returnWaitReq = true;
		count = 5;
	}

	public static void main(String[] args) {
		new AttendFlow();
	}
}
