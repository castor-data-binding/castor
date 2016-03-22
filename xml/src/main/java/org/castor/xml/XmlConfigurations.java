package org.castor.xml;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XmlConfigurations {

	@Bean(name = "javaNamingImpl")
	JavaNaming javaNamingImpl() {
		return new JavaNamingImpl();
	}

	@Bean(name = "javaNamingNG")
	JavaNaming javaNamingNGImpl() {
		return new JavaNamingNGImpl();
	}

}
