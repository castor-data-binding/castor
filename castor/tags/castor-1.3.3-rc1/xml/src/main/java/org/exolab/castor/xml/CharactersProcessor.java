/*
 * Copyright 2010 Philipp Erlacher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exolab.castor.xml;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * A processor that assists {@link UnmarshalHandler} in dealing with the SAX 2
 * {@link ContentHandler#characters(char[], int, int)} callback method.
 * 
 * @author <a href=" mailto:philipp.erlacher AT gmail DOT com">Philipp
 *         Erlacher</a>
 */
public class CharactersProcessor {

    /**
     * Standard logger to use.
     */
    private static final Log LOG = LogFactory.getLog(CharactersProcessor.class);

    /**
     * resource bundle
     */
    protected static ResourceBundle resourceBundle;

    /**
     * Callback {@link UnmarshalHandler} reference to set the actual state on
     * this instance.
     */
    private final UnmarshalHandler _unmarshalHandler;

    static {
        resourceBundle = ResourceBundle.getBundle("UnmarshalHandlerMessages",
                Locale.getDefault());
    }

    /**
     * Creates an instance of this class, with a reference to the actual
     * {@link UnmarshalHandler} for which this processor deals with the SAX 2
     * characters() callback method.
     * 
     * @param unmarshalHandler
     *            The {@link UnmarshalHandler} instance on which the results of
     *            processing the characters method will be 'persisted'/set.
     */
    public CharactersProcessor(final UnmarshalHandler unmarshalHandler) {
        _unmarshalHandler = unmarshalHandler;
    }

    public void compute(char[] ch, int start, int length) throws SAXException {
    	String string = new String(ch, start, length);
        if (LOG.isTraceEnabled()) {
            String trace = MessageFormat.format(resourceBundle
                    .getString("unmarshalHandler.log.trace.characters"),
                    new Object[] { string });
            LOG.trace(trace);
        }

        // -- If we are skipping elements that have appeared in the XML but for
        // -- which we have no mapping, skip the text and return
        if (_unmarshalHandler.getStrictElementHandler().skipElement()) {
            return;
        }

        if (_unmarshalHandler.getStateStack().isEmpty()) {
            return;
        }

        if (_unmarshalHandler.getAnyNodeHandler().hasAnyUnmarshaller()) {
            _unmarshalHandler.getAnyNodeHandler().characters(ch, start, length);
            return;
        }

        UnmarshalState state = _unmarshalHandler.getStateStack().getLastState();
        // -- handle whitespace
        boolean removedTrailingWhitespace = false;
        boolean removedLeadingWhitespace = false;
        if (!state.isWhitespacePreserving() && !ArrayUtils.isEmpty(ch)) {
        	removedTrailingWhitespace = Character.isWhitespace(ch[start+length-1]);
        	removedLeadingWhitespace = Character.isWhitespace(ch[start]);
        	string = string.trim();
        }

        if (state.getBuffer() == null) {
            state.setBuffer(new StringBuffer());
        } else {
        	if (state.isWhitespacePreserving()) {
				state.setTrailingWhitespaceRemoved(false);
				state.getBuffer().append(string);
				return;
			} else if (StringUtils.isEmpty(string)) {
				state.setTrailingWhitespaceRemoved(removedTrailingWhitespace);
				return;
			} else if (state.isTrailingWhitespaceRemoved()
					|| removedLeadingWhitespace) {
				state.getBuffer().append(' ');
			}
		}
        state.setTrailingWhitespaceRemoved(removedTrailingWhitespace);
        state.getBuffer().append(string);
    }
}
