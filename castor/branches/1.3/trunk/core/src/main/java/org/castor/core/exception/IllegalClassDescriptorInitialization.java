package org.castor.core.exception;

/**
 * {@link RuntimeException} descendant to signal problems during 
 * class descriptor instantiation.
 * 
 * @author Werner Guttmann
 * @since 1.2.1
 *
 */
public class IllegalClassDescriptorInitialization extends RuntimeException {

    /**
     * serial version UID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates an instance of {@link IllegalClassDescriptorInitialization}.
     */
    public IllegalClassDescriptorInitialization() {
        super();
    }

    /**
     * Creates an instance of {@link IllegalClassDescriptorInitialization}.
     * @param message error message
     */
    public IllegalClassDescriptorInitialization(final String message) {
        super(message);
    }

    /**
     * Creates an instance of {@link IllegalClassDescriptorInitialization}.
     * @param cause Root exception 
     */
    public IllegalClassDescriptorInitialization(final Throwable cause) {
        super(cause);
    }

    /**
     * Creates an instance of {@link IllegalClassDescriptorInitialization}.
     * @param message error message
     * @param cause Root exception 
     */
    public IllegalClassDescriptorInitialization(final String message,
            final Throwable cause) {
        super(message, cause);
    }

}
