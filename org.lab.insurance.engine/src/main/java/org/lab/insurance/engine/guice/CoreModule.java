package org.lab.insurance.engine.guice;

import java.util.Properties;

import org.apache.bval.guice.ValidationModule;
import org.lab.insurance.Constants;
import org.lab.insurance.engine.camel.CamelModule;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.google.inject.persist.jpa.JpaPersistModule;

public class CoreModule extends AbstractModule {

	@Override
	protected void configure() {
		Properties properties = readApplicationProperties();
		Names.bindProperties(binder(), properties);
		installJpaModule(properties);
		install(new CamelModule());
		install(new ValidationModule());
	}

	private Properties readApplicationProperties() {
		try {
			Properties properties = new Properties();
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			properties.load(classLoader.getResourceAsStream(Constants.APP_PROPERTIES_NAME));
			return properties;
		} catch (Exception ex) {
			throw new RuntimeException("Error reading application properties", ex);
		}
	}

	private void installJpaModule(Properties properties) {
		JpaPersistModule jpaModule = new JpaPersistModule(Constants.PERSISTENCE_UNIT_NAME);
		jpaModule.properties(properties);
		install(jpaModule);
	}
}