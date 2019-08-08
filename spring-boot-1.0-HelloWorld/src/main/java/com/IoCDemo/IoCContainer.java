package com.IoCDemo;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
 * 1.实例化bean
 * 2.保存bean
 * 3.提供bean
 * 4.每个bean要产生一个唯一的id与之对应
 * 
 */
public class IoCContainer {

	private Map<String,Object> beans = new ConcurrentHashMap<String,Object>();
	
	/**
	 * 根据beanId 获取一个Bean
	 * @param beanId beanId
	 * @return 返回bean
	 */
	public Object getBean(String beanId) {
		return beans.get(beanId);
	}
	
	/**
	 * 委托ioc容器创建一个bean
	 * @param clazz 要创建的bean的class
	 * @param beanId beanId
	 * @param paramBeanIds 要创建的bean的class的构造方法所需要的参数的beanId们
	 */
	public void setBean(Class<?> clazz,String beanId,String... paramBeanIds) {
		//1.组装构造方法所需要的参数值
		Object[] paramValues = new Object[paramBeanIds.length];
		for (int i = 0; i < paramValues.length; i++) {
			paramValues[i] = beans.get(paramBeanIds[i]);	
		}
		//2.调用构造方法实例化bean
		Object bean = null;
		for (Constructor<?> constructor : clazz.getConstructors()) {
			try {
				bean = constructor.newInstance(paramValues);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//3.将实例化的bean放入beans
		beans.put(beanId, bean);
	}
}
