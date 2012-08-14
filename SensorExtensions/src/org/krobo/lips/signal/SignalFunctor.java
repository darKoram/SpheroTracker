package org.krobo.lips.signal;

import org.krobo.lips.core.OneVariableStrategy;

/*
 * All my signal generating functions will derive from SignalFunctor.  However, custom
 * functions may simply implement OneVariableStrategy interface  May include things like
 * name, budget(time, energy, memory) later.
 */
public abstract class SignalFunctor implements OneVariableStrategy {
	protected String mFunctionType;
}
