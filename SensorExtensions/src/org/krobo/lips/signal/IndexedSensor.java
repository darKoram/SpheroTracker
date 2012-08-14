package org.krobo.lips.signal;

import org.krobo.lips.core.HasSize;
public class IndexedSensor <E extends Enum<E> & HasSize > {
	
	public E mSensor;
	public int mFirst;
	public int mLast;
	
	IndexedSensor(E e, int first, int last) {
		mSensor = e;
		mFirst = first;
		mLast = last;
	}
	
	IndexedSensor(E e) {
		mSensor = e;
		mFirst = -1;
		mLast = -1;
	}
	
	IndexedSensor() {
		mSensor = null;
		mFirst = -1;
		mLast = -1;
	}

	public int getSize() {
		return mSensor.getSize();
	}
	
	@Override
	public String toString() {
		String str = "" + mSensor.toString() + " [" + mFirst + ", " + "]";
		return str;
	}
}
