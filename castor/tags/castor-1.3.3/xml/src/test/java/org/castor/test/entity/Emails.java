package org.castor.test.entity;

/**
 *
 *
 * @author <a herf="mailto:jmnarloch AT gmail DOT com">Jakub Narloch</a>
 * @version 1.3.3
 * @since 1.3.3
 */
public class Emails {

    /**
     * Represents the array of emails.
     */
    private Email[] email;

    /**
     * Creates new instance of Emails class.
     */
    public Emails() {
        // empty constructor
    }

    /**
     * Retrieves the emails
     *
     * @return the emails
     */
    public Email[] getEmail() {
        return email;
    }

    /**
     * Sets the emails
     *
     * @param email the emails
     */
    public void setEmail(Email[] email) {
        this.email = email;
    }
}
