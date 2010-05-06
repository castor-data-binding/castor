package org.castor.jdo.jpa.info;

import org.castor.jdo.jpa.annotations.Cache;
import org.castor.jdo.jpa.annotations.CacheProperty;

@Cache({
	@CacheProperty(key="type", value="none")
})
public class JpaCacheTestClass {
	public JpaCacheTestClass() {
	}
}
