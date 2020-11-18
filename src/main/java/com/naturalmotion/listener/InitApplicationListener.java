package com.naturalmotion.listener;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.naturalmotion.EliteCars;
import com.naturalmotion.csr_api.service.car.CarServiceFileImpl;
import com.naturalmotion.csr_api.service.io.NsbException;

public class InitApplicationListener implements ServletContextListener {

	private final Logger log = LoggerFactory.getLogger(InitApplicationListener.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			List<String> eliteCars = new CarServiceFileImpl(null).getEliteCars();
			EliteCars.getInstance().setEliteCars(eliteCars);
		} catch (NsbException e) {
			log.error("Error initializing server", e);
		}
	}

}
