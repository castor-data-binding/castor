package org.exolab.castor.xml.parsing;

import org.castor.xml.UnmarshalListener;
import org.castor.xml.UnmarshalListenerAdapter;
import org.exolab.castor.xml.UnmarshalHandler;

/**
 * This class handles delegates methods call to {@link UnmarshalListener}. Even if
 * {@link UnmarshalListener} is null, all of the delegating methods can be invoke.
 * 
 * @author <a href="mailto:philipp DOT erlacher AT gmail DOT com">Philipp
 *         Erlacher</a>
 * 
 * @since 1.3.2
 */
public class UnmarshalListenerDelegate implements UnmarshalListener {

    /**
     * The unmarshaller listener.
     */
    private org.castor.xml.UnmarshalListener _unmarshalListener = null;

    /**
     * Sets an {@link org.castor.xml.UnmarshalListener}.
     * 
     * @param listener
     *            the {@link org.castor.xml.UnmarshalListener} to use with this
     *            instance of the {@link UnmarshalHandler}.
     */
    public void setUnmarshalListener(org.castor.xml.UnmarshalListener listener) {
        _unmarshalListener = listener;
    }

    /**
     * Sets an {@link org.exolab.castor.xml.UnmarshalListener}.
     * 
     * @param listener
     *            the {@link org.exolab.castor.xml.UnmarshalListener} to use
     *            with this instance of the UnmarshalHandler.
     * @deprecated please move to the new
     *             {@link org.castor.xml.UnmarshalListener} interface
     */
    public void setUnmarshalListener(
            org.exolab.castor.xml.UnmarshalListener listener) {
        if (listener == null) {
            listener = null;
        } else {
            UnmarshalListenerAdapter adapter = new UnmarshalListenerAdapter();
            adapter.setOldListener(listener);
            _unmarshalListener = adapter;
        }
    }

    /**
     * @see org.castor.xml.UnmarshalListener.unmarshalled
     * @param object
     * @param parentObject
     */
    public void unmarshalled(Object object, Object parentObject) {
        // -- We're finished processing the object, so notify the
        // -- Listener (if any).
        if (_unmarshalListener != null && object != null) {
            _unmarshalListener.unmarshalled(object, parentObject);
        }

    }

    /**
     * @see org.castor.xml.UnmarshalListener.fieldAdded
     * @param object
     * @param parentObject
     */
    public void fieldAdded(String fieldName, Object stateObject,
            Object fieldStateObject) {
        // If there is a parent for this object, pass along
        // a notification that we've finished adding a child
        if (_unmarshalListener != null) {
            _unmarshalListener.fieldAdded(fieldName, stateObject,
                    fieldStateObject);
        }
    }

    /**
     * @see org.castor.xml.UnmarshalListener.initialized
     * @param object
     * @param parentObject
     */
    public void initialized(Object stateObject, Object parentObject) {
        if (_unmarshalListener != null)
            _unmarshalListener.initialized(stateObject, parentObject);
    }

    /**
     * @see org.castor.xml.UnmarshalListener.attributesProcessed
     * @param object
     * @param parentObject
     */
    public void attributesProcessed(Object stateObject, Object parentObject) {
        if (_unmarshalListener != null)
            _unmarshalListener.attributesProcessed(stateObject, parentObject);
    }
}
