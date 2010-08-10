package org.exolab.castor.xml.parsing;

import org.exolab.castor.xml.UnmarshalHandler;

/**
 * A helper class for {@link UnmarshalHandler}.
 * 
 * Keeps track if elements, which appear in XML but for which there is no
 * mapping, should be allowed.
 * 
 * @author <a href="mailto:philipp DOT erlacher AT gmail DOT com">Philipp
 *         Erlacher</a>
 * 
 * @since 1.3.2
 */
public class StrictElementHandler {

    /**
     * A boolean that indicates element processing should be strict and an error
     * should be flagged if any extra elements exist.
     **/
    private boolean _strictElements = true;

    /**
     * A depth counter that increases as we skip elements ( in startElement )
     * and decreases as we process and endElement. Only active if _strictElements is 
     * set.
     */
    private int _ignoreElementDepth = 0;

    /**
     * Sets whether or not elements that do not match a specific field should
     * simply be ignored or reported as an error. By default, extra elements 
     * are ignored.
     * 
     * @param ignoreExtraElems
     *            a boolean that when true will allow non-matched attributes to
     *            simply be ignored.
     **/
    public void setIgnoreExtraElements(boolean ignoreExtraElems) {
        _strictElements = (!ignoreExtraElems);
    }

    /**
     * Checks if extra elements will be ignored.
     * 
     * @return true if we ignore extra elements, false otherwise
     */
    public boolean areElementsIgnorable() {
        return !_strictElements;
    }

    /**
     * Skip element that appear in XML but for which we have no mapping
     * 
     * @return
     */
    public boolean skipElement() {
        return _ignoreElementDepth > 0;
    }

    /**
     * Checks if a start element can be skipped
     * 
     * @return true if start element can be skipped, false otherwise
     */
    public boolean skipStartElement() {
        if (areElementsIgnorable() && skipElement()) {
            addIgnorableElement();
            return true;
        }
        return false;
    }

    /**
     * Checks if an end element can be skipped.
     * 
     * @return true if end element can be skipped, false otherwise
     */
    public boolean skipEndElement() {
        if (skipElement()) {
            remIgnorableElement();
            return true;
        }
        return false;
    }

    /**
     * Decreases the depth counter for elements which should be skipped.
     */
    private void remIgnorableElement() {
        --_ignoreElementDepth;
    }

    /**
     * Increases the depth counter for elements for which we have no mapping.
     */
    private void addIgnorableElement() {
        ++_ignoreElementDepth;
    }
}
