package org.krobo.lips.signal;

import java.lang.Math;

public class Sinusoid extends SignalFunctor {
	
	private final Double mAmplitude;
	private final Double mFrequency;
	private final Double mPhase;
	
	public Sinusoid(Double amplitude, Double frequency, Double phase) {
		super.mFunctionType = "SINUSOID";
		mAmplitude = amplitude;
		mFrequency = frequency;
		mPhase = phase;
	}

	public Double execute(Double time) {
		Double input = 2*Math.PI*(mFrequency*time + mPhase);
		Double result = mAmplitude*Math.sin(input);
		return result;
	}
}
