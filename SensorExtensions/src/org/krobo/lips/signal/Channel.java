package org.krobo.lips.signal;

import java.util.List;

public class Channel {
	
	private List<SignalFunctor> mSignalFunctorList;
	
	// Constructor
	public Channel(List<SignalFunctor> sflist) {
		mSignalFunctorList = sflist;
	}
	
	public List<SignalFunctor> getSignalFunctorList() {
		return mSignalFunctorList;
	}
}
