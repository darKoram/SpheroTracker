package org.krobo.lips.utils;

// may want to wrap Android Sensor class in one that includes enumType, streaming
// stats, etc.  Perhaps with a decorator?

public class StreamingStats{
	
	public double mSumOfSquares = 0D;
	public double mMean = 0D;
	public double mVariance = 0D;
	public int mCount = 0;	// N
	public double mSum = 0D;

	

/*
 * Calculate the mean and standard deviation given
 * @param N = number of events including this newData
 * @param mean = mean from N-1 calculation
 * @param variance = variance from N-1 calculation
 * @param squareOfSum = sum( (a_i)^2 ) from N-1 calculation (needed for variance)
 * @param newData = next piece of data in time series
 * 
 */
	private void updateMeanAndVariance(	int N, double mean, 
												double variance, 
												double sumOfSquares,
												double newData)
	{
		double oldMean = mean;
		
		mCount +=1;
		mSum += newData;
		mSumOfSquares += newData*newData;
		
		// watch out! could be masking member variables with local passed vars.
		// these methods called cannot modify member variables.
		mMean = updateMean(mCount, mMean, newData);
		mVariance = updateVariance(mCount, oldMean, mVariance, mSumOfSquares, newData);

	}
	
	public void updateMeanAndVariance( double newData ) {
		updateMeanAndVariance(mCount, mMean, mVariance, mSumOfSquares, newData);
	}
	/*
	 * @param N = number of samples including newData
	 * @param oldMean = mean from N-1 calculation
	 * @param new sample datum from streaming data
	 * @return new mean
	 */
	private double updateMean(int N, double mean, double newData) {
		double result = (1/(double)N*N)*( (N-1)*mean + newData);
		return result;
	}
	
	/*
	 * Updates squareOfSum
	 * @return new variance
	 */
	private double updateVariance(int N, double oldMean, double variance,			
										double sumOfSquares, double newData) {
		double n = (double) N;
		return (1.0D)/(n*n)*( sumOfSquares + (n-2)*newData*newData + (n-1)*(n-1)*variance - 2*(n-1)*newData*oldMean);
	}
	
}