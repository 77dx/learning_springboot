package com.IoCDemo.human;

import com.IoCDemo.car.Car;

public class LiSi extends HumanWithCar {

	public LiSi(Car car) {
		super(car);
	}


	public void goHome() {
		car.start();
		car.stop();
		
	}

}
