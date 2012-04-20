package org.exolab.castor.xml;

import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;

/**
 * A interface which extends {@link SAX2EventProducer}. {@link SAX2EventProducer} 
 * abstracts anything which can produce SAX 2 events, and invoke the corresponding
 * callback methods on the given {@link ContentHandler}. 
 * 
 * <p>This interface also handles SAX 2 parse exceptions and invokes the 
 * corresponding methods on the given {@link ErrorHandler}.</p>
 * 
 * @author <a href="mailto:philipp DOT erlacher AT gmail DOT com">Philipp
 *         Erlacher</a>
 *         
 * @see SAX2EventProducer 
 * @see ErrorHandler
 * 
 */
public interface SAX2EventAndErrorProducer extends SAX2EventProducer {

	/**
	 * Sets the SAX2 ErrorHandler to send SAX 2 errors to
	 */
	void setErrorHandler(ErrorHandler handler);

}
