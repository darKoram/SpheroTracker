package kesten.sandbox;

import kesten.sandbox.*;

public enum SensorEnum implements HeaderInterface { 
	Acc, Att, Therm; 
	public int getSize() {
		return 1;
	}
};


