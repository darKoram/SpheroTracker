package org.krobo.hips.kinematicbody;

import org.opencv.core.Mat;
import org.opencv.core.CvType;

public class sptMotionState {

	public Mat sptGetOrientation() {
		Mat mat = new Mat(3, 3, CvType.CV_64F);
		return mat;
	}
}
