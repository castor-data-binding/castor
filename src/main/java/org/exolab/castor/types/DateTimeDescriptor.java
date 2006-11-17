/*
 * Copyright 2006 (C) Edward Kuns, All Rights Reserved.
 *
 * $Id: DateTimeDescriptor.java 0000 $
 */
package org.exolab.castor.types;

import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.ValidityException;
import org.exolab.castor.xml.NodeType;
import org.exolab.castor.xml.XMLFieldDescriptor;
import org.exolab.castor.xml.XMLFieldHandler;
import org.exolab.castor.xml.util.XMLFieldDescriptorImpl;

/**
 * The DateTime Descriptor.  Note:  Under normal circumstances, this descriptor
 * will never be used.  Castor treats XSD DateTime values as java.util.Date and
 * parses these values into java.util.Date.
 *
 * @author <a href="edward.kuns@aspect.com">Edward Kuns</a>
 * @version $Revision: 0000 $ $Date: $
 */
public class DateTimeDescriptor extends BaseDescriptor {

    /** The name of the XML element. */
    private static final String                 _xmlName = "dateTime";
    /** Our field descriptor. */
    private static final XMLFieldDescriptorImpl _contentDescriptor;
    /** Our field descriptor array.  Lists the fields we describe. */
    private static final FieldDescriptor[]      _fields;

    static {
        _contentDescriptor = new XMLFieldDescriptorImpl(String.class,"content", "content", NodeType.Text);
        _contentDescriptor.setHandler((new DateTimeDescriptor()).new DateTimeFieldHandler());
        _fields = new FieldDescriptor[1];
        _fields[0] = _contentDescriptor;
    }

    // ----------------/
    // - Constructors -/
    // ----------------/

    public DateTimeDescriptor() {
        super(_xmlName, DateTime.class);
    } // -- DateDescriptor

    // ------------------/
    // - Public Methods -/
    // ------------------/

    /**
     * Returns the XMLFieldDescriptor for the member that should be marshalled
     * as text content.
     *
     * @return the XMLFieldDescriptor for the member that should be marshalled
     *         as text content.
     */
    public XMLFieldDescriptor getContentDescriptor() {
        return _contentDescriptor;
    } // getContentDescriptor

    /**
     * Returns a list of fields represented by this descriptor.
     *
     * @return A list of fields
     */
    public FieldDescriptor[] getFields() {
        return _fields;
    } // -- getFields

    /**
     * A specialized FieldHandler for the XML Schema DateTime related types.
     *
     * @author <a href="edward.kuns@aspect.com">Edward Kuns</a>
     * @version $Revision: 0000 $ $Date: $
     */
    class DateTimeFieldHandler extends XMLFieldHandler {

        // ----------------/
        // - Constructors -/
        // ----------------/

        /**
         * Creates a new TimeFieldHandler.
         */
        public DateTimeFieldHandler() {
            super();
        } // -- TimeFieldHandler

        // ------------------/
        // - Public Methods -/
        // ------------------/

        /**
         * Returns the value of the field associated with this descriptor from
         * the given target object.
         *
         * @param target
         *            the object to get the value from
         * @return the value of the field associated with this descriptor from
         *         the given target object.
         * @throws IllegalStateException
         *             if the target value is an inappropriate class, is null,
         *             or returns a String() on toString() that fails validation.
         */
        public Object getValue(Object target) throws java.lang.IllegalStateException {
            // -- check for DateTime class -- add later
            DateTime date = (DateTime) target;
            return date.toString();
        } // -- getValue

        /**
         * Sets the value of the field associated with this descriptor.
         *
         * @param target
         *            the object in which to set the value.
         * @param value
         *            the value of the field.
         * @throws IllegalStateException
         *             if the target value is an inappropriate class, is null,
         *             or returns a String() on toString() that fails validation.
         */
        public void setValue(Object target, Object value) throws java.lang.IllegalStateException {
            if (!(target instanceof DateTime)) {
                String err = "DateTimeDescriptor#setValue: expected DateTime, received instead:"
                        + target.getClass();
                throw new IllegalStateException(err);
            }

            DateTime dateTarget = (DateTime) target;

            if (value == null) {
                String err = "DateTimeDescriptor#setValue: null value.";
                throw new IllegalStateException(err);
            }

            // -- update current instance of time with new time
            try {
                DateTime temp = DateTime.parseDateTime(value.toString());
                dateTarget.setCentury(temp.getCentury());
                dateTarget.setYear(temp.getYear());
                dateTarget.setMonth(temp.getMonth());
                dateTarget.setDay(temp.getDay());
                dateTarget.setHour(temp.getHour());
                dateTarget.setMinute(temp.getMinute());
                dateTarget.setSecond(temp.getSeconds(), temp.getMilli());
                if (temp.isUTC()) {
                    dateTarget.setUTC();
                    dateTarget.setZone(temp.getZoneHour(), temp.getZoneMinute());
                    if (temp.isZoneNegative()) {
                        dateTarget.setZoneNegative(true);
                    }
                }
            } catch (java.text.ParseException ex) {
                String err = "DateDescriptor#setValue: wrong value\n" + ex.getMessage();
                throw new IllegalStateException(err);
            }
        } // -- setValue

        public void resetValue(Object target)
                throws java.lang.IllegalStateException {
            // nothing to do?
        }

        /**
         * Checks the field validity. Returns successfully if the field can be
         * stored, is valid, etc, throws an exception otherwise.
         *
         * @param object
         *            The object
         * @throws ValidityException
         *             The field is invalid, is required and null, or any other
         *             validity violation
         * @throws IllegalStateException
         *             The Java object has changed and is no longer supported by
         *             this handler, or the handler is not compatiable with the
         *             Java object
         */
        public void checkValidity(Object object) throws ValidityException, IllegalStateException {
            // nothing to do?
        } // -- checkValidity

        /**
         * Creates a new instance of the object described by this field.
         *
         * @param parent
         *            The object for which the field is created
         * @return A new instance of the field's value
         * @throws IllegalStateException never
         */
        public Object newInstance(Object parent) throws IllegalStateException {
            return new DateTime();
        } // -- newInstance

    } // -- DateFieldHandler

} // -- DateDescriptor
