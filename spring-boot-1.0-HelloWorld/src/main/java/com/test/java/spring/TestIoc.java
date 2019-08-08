package com.test.java.spring;

import org.junit.Before;
import org.junit.Test;

import com.IoCDemo.IoCContainer;
import com.IoCDemo.car.Audi;
import com.IoCDemo.car.Buick;
import com.IoCDemo.human.Human;
import com.IoCDemo.human.LiSi;
import com.IoCDemo.human.ZhangSan;

public class TestIoc {

	private IoCContainer iocctainer = new IoCContainer();
	
	@Before
	public void before() {
		iocctainer.setBean(Audi.class, "audi");
		iocctainer.setBean(Buick.class, "buick");
		iocctainer.setBean(ZhangSan.class, "zhangsan","buick");
		iocctainer.setBean(LiSi.class, "lisi","audi");
	}
	
	@Test
	public void test() {
		Human zhangsan = (Human)iocctainer.getBean("zhangsan");
		zhangsan.goHome();
		Human lisi = (Human) iocctainer.getBean("lisi");
		lisi.goHome();
		
		
	}
	
	
}
