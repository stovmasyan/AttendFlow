package ru.crystals.attendflow.ds;

public class State {

	private int cashNum;
	private int age;
	private StateType stateType;

	public State() {
		// TODO Auto-generated constructor stub
	}

	public State(int cashNum, int age, StateType stateType) {
		this.cashNum = cashNum;
		this.age = age;
		this.stateType = stateType;
		
	}

	public int getCashNum() {
		return cashNum;
	}

	public void setCashNum(int cashNum) {
		this.cashNum = cashNum;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public StateType getStateType() {
		return stateType;
	}

	public void setStateType(StateType stateType) {
		this.stateType = stateType;
	}

	

	
}
