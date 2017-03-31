/**
 * This class is created to support ThreadLocal in Castor. This class is introduced to support
 * different xml document requests SOAP/JMS etc. having the same xml name as root element. If xml names are same with
 * different incoming xml requests causes issues with Castor when Castor caching is used as Castor only expecting
 * one-to-one mapping between Xml element and corresponding ComplexType. The required context is set by the application
 * as required to set in the ThreadLocal.
 * mappingInfo.put("class", "com.org.Customer");
 * mappingInfo.put("map-to", "cust");
 * mappingInfo.put("class", "com.org.SpecialCustomer");
 * mappingInfo.put("map-to", "cust");
 *
 */
package org.exolab.castor.xml;

import java.util.WeakHashMap;

/**
 * This class provides thread-local variables. These
 * variables differ from their normal counterparts in that each thread that
 * accesses one (via its get or set method) has its own, independently
 * initialised copy of the variable.
 * 
 */
public class CastorThreadLocal {
	private static ThreadLocal<WeakHashMap<String, Object>> threadLocal = new ThreadLocal<WeakHashMap<String, Object>>();
	private static final String REQ_CONTEXT = "REQ_CONTEXT";

	private static WeakHashMap<String, Object> getDetailsMap() {
		WeakHashMap<String, Object> detailsMap = threadLocal.get();
		if (detailsMap == null) {
			detailsMap = new WeakHashMap<String, Object>();
			threadLocal.set(detailsMap);
		}
		return detailsMap;
	}

	/**
	 * This request Context is MF ID. Appending this to the xml element will make the element
	 * unique across web services calls.
	 * @return
	 */
	public static String getReqContext() {
		if (getDetailsMap().get(REQ_CONTEXT) != null) {
			return (String) getDetailsMap().get(REQ_CONTEXT);
		}
		return null;
	}

	public static void setReqContext(String reqContext) {
		getDetailsMap().put(REQ_CONTEXT, reqContext);
	}
}
