package org.castor.test.entity;

/**
 *
 *
 * @author <a herf="mailto:jmnarloch AT gmail DOT com">Jakub Narloch</a>
 * @version 1.3.3
 * @since 1.3.3
 */
public class Email {

    private String from;

    private String to;

    /**
     * Retrieves the sender address.
     *
     * @return the sender address
     */
    public String getFrom() {
        return from;
    }

    /**
     * Sets the sender address
     *
     * @param from the sender address
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * Retrieves the recipient address.
     *
     * @return the recipient address
     */
    public String getTo() {
        return to;
    }

    /**
     * Sets the recipient address.
     *
     * @param to the recipient address
     */
    public void setTo(String to) {
        this.to = to;
    }
}
