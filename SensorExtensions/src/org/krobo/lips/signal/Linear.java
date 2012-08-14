package org.krobo.lips.signal;

import org.krobo.lips.signal.SignalFunctor;

public class Linear extends SignalFunctor {

	Double mYIntercept = 0D;
	Double mSlope = 0D;
	
	public Linear(Double slope, Double yIntercept) {
		super.mFunctionType = "LINEAR";
		mSlope = slope;
		mYIntercept = yIntercept;
	}
	
	@Override
	public Double execute(Double time) {
		return mSlope*time+mYIntercept;
	}
}
