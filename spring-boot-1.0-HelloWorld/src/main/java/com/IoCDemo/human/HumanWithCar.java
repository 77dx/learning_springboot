package com.IoCDemo.human;

import com.IoCDemo.car.Car;

public abstract class HumanWithCar implements Human {

	protected Car car;
	public HumanWithCar(Car car) {
		this.car  = car;
	}
	public abstract void goHome() ;

}
