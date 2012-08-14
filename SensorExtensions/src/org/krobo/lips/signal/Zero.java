package org.krobo.lips.signal;

public class Zero extends SignalFunctor {
	
	public Zero() {	
		super.mFunctionType = "ZERO";
	}
	
	public Double execute(Double t) {
		return 0D;
	}
}
