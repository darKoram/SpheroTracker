package apache.tests;

import junit.framework.TestCase;
import org.apache.commons.collections.buffer.*;

public class CircularBufferTest extends TestCase {

	CircularFifoBuffer<Long> mQueue;
	
	public CircularBufferTest(String name) {
		super(name);
	}

	protected void testCircularBuffer() {
		try {
			Long n;
			for (n=0L; n<7; n++) {
				mQueue.add(n);
				System.out.println(mQueue.toString() );
			}
		} 
		catch ( Exception e ) {
			fail("Error adding 7 numbers to length 4 circular fifo");
		}
	}
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
