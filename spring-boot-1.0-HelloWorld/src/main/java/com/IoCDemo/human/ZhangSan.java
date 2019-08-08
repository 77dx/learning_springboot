package com.IoCDemo.human;

import com.IoCDemo.car.Car;

public class ZhangSan extends HumanWithCar {
	public ZhangSan(Car car) {
		super(car);
		
	}

	@Override
	public void goHome() {
		car.start();
		car.turnLeft();
		car.turnRight();
		car.stop();

	}

}
