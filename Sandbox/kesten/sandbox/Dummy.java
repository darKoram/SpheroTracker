package kesten.sandbox;

import kesten.sandbox.SensorEnum;
import java.util.List;

public class Dummy {

	public void Dummy(List<HeaderObject> list) {
		for(HeaderObject h: list) {
			System.out.println(h.getSize());
		}
	}
}
