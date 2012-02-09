package app.fourthink.controllers;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public abstract class ControllerTemplate<T> implements ApplicationContextAware {

	@SuppressWarnings("unused")
	private ApplicationContext ac;
	
	@Override
	public void setApplicationContext(ApplicationContext ac){
		this.ac = ac;
	}

}
