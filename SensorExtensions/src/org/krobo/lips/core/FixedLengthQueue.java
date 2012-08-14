package org.krobo.lips.core;

import java.util.LinkedList;

public class FixedLengthQueue<E> extends LinkedList<E> {

	private static final long serialVersionUID = 1L;
	// Default length of list is 5
	private int mMaxLength=5;
	
	public FixedLengthQueue(int n) {
		super();
		setMaxLength(n);		
	}
	
	public void setMaxLength(int maxLength) {
		mMaxLength = maxLength;
	}
	
	public int getMaxLength() {
		return mMaxLength;
	}
	
	@Override
	public void addFirst(E e) {
		if (this.size() >= mMaxLength) {
			// TODO Would like to set the removed object to null.  How?
			E removed;
			removed = this.removeLast(); 
			removed = null;
		}
		super.addFirst(e);
	}
	
}

