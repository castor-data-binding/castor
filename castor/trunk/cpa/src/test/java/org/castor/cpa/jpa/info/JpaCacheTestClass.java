package org.castor.cpa.jpa.info;

import org.castor.cpa.jpa.annotations.Cache;
import org.castor.cpa.jpa.annotations.CacheProperty;

@Cache({
	@CacheProperty(key="type", value="none")
})
public class JpaCacheTestClass {
	public JpaCacheTestClass() {
	}
}
