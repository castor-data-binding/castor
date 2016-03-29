package org.castor.xml;

import org.exolab.castor.xml.util.DefaultNaming;
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

  @Bean
  XMLNaming xmlNaming() {
    return new DefaultNaming();
  }

}
