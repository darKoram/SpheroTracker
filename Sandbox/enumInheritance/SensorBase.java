package enumInheritance;

import org.krobo.lips.core.HasSize;

class SensorBase {
    enum ArchetypeSensors implements HasSize {   ACCELEROMETER(3), 
					    						 ORIENTATION(3), 
					    						 GYROSCOPE(3),
					    						 AXIS_ONE_MOTOR_ONE(1),
					    						 THERMOMETER(1);
					    						 
    					
    /*
     * parameters will be final if it is likely that changing them could corrupt the 
     * data processing pipeline.  The pipeline should be stable to changes like
     * <UL> gaps in signal
     * <UL> changes in sampling rate
     * <UL> 
     * 
     *  To keep contiguous memory storage, changes in the list of active sensors will trigger constructors.
     *  
     *  Other configuration changes with final parameters
     *  <UL> changes to the basic storage types to tackle floating point rounding errors 
     *  	<IL> Database records could be merged by sophisticated stats packs like R
     *  		<IIL> A snapshot of the current parameters gets stored in header upon change
     *  <UL> dynamic range
     *  	<IL> If it is sufficient to warrant changing the format of basic data types
     *  <UL> change to the signal floor threshold  
     *  
     *  Other factors that can trigger constructors
     *  <UL> catch RebuildConstructorsException which is tried finally after
     *  	<IL> catch ExceededEnergyBudgetException
     *  	<IL> catch CannotReconnectException
     *  	<IL> catch InvalidateDSPException
     *  	<IL> catch InvalidateFinalParameterException
     *  	<IL> catch InvalidateActivityException
     *  	<IL> catch InvalidateViewException
     *  	<IL> catch BadUXException
     *  	<IL> catch InvalidateRichardsonExtrapolationException
     *  	<IL> catch JVMException
     *  	<IL> catch HardwareException
     *  	<IL> catch CollisionThresholdException
     *  	<IL> catch CvException  See javadoc core
     *  	<IL> catch InvalidateIMUException
     *  	<IL> catch ReproducibilityException
     *  	<IL> catch AmericanExceptionalism
     */

	    private int mSize;
		
		public int getSize() {
			return mSize;
		}
	    private ArchetypeSensors(int numAxes) {
	    	mSize = numAxes;
	    }
    }
    private final int mSignalFloor=1;
    	
}

