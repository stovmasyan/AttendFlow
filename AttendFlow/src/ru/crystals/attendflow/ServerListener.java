package ru.crystals.attendflow;

import ru.crystals.attendflow.ds.StateType;

public interface ServerListener {
	public void eventStateChanged(int cashNum, int age, StateType state);
}
