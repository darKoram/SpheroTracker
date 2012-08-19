package kesten.sandbox;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import kesten.sandbox.*;

public class SandboxClass {

	/**
	 * @param args
	 */
	enum PhoneSensor { A, B };
	enum RobotSensor { A, C };	
	
	public <E extends Enum<E>> void registerSensor(E e) {
		System.out.print(e);
	}
	public <E extends Enum<E>> void registerSensor(EnumSet<E> eSet, Class<E> clazz) {
//		for (E e : eSet) {
//			System.out.print("value is ");
//			// Cannot switch on a value of type E. Only convertible int values or enum variables are permitted
//			Enum en = (Enum)e;
//			switch(en) {
//			case PhoneSensor.A:
//				System.out.print(e);
//			default:
//				return;
//			}
//		}
	}
	
	
	public static void main(String[] args) {
		SensorEnum mySensor = SensorEnum.Acc;
		String myName = mySensor.name();
		
		
		System.out.println("Mic Check");
		System.out.println(mySensor);
		System.out.println(mySensor.name());
		System.out.println(SensorEnum.values()[2]);
		
		List<String> intList = new ArrayList<String>();
		
		List<HeaderObject> enList = new ArrayList<HeaderObject>();

		List<SensorEnum>  senList = new ArrayList<SensorEnum>();
		senList.add(SensorEnum.Acc);
		senList.add(SensorEnum.Therm);
		
		// Dummy dummy = new Dummy((List<HeaderObject>)senList);
		
		doSomething(senList);
		doSomething(SensorEnum.Acc);
		System.out.println("here");
		process(senList); 
		for (int i=0; i< SensorEnum.values().length; i++) {
			System.out.println( (SensorEnum.values())[i].toString());
		}
	}
	
	public static void doSomething(List<? extends HeaderInterface> list) {
		for(HeaderInterface h: list)
			System.out.println(h);
	}
	public static void doSomething(HeaderInterface h) {
		System.out.println(h);
	}
	
	public static <E extends Enum<E>> void process(List<E> list) {
		for(E h: list)
			System.out.println(h);
	}

}

