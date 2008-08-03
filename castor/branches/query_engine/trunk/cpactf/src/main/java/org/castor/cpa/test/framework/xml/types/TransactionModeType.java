/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id$
 */

package org.castor.cpa.test.framework.xml.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.Hashtable;

/**
 * Class TransactionModeType.
 * 
 * @version $Revision$ $Date$
 */
public class TransactionModeType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The local type
     */
    public static final int LOCAL_TYPE = 0;

    /**
     * The instance of the local type
     */
    public static final TransactionModeType LOCAL = new TransactionModeType(LOCAL_TYPE, "local");

    /**
     * The global type
     */
    public static final int GLOBAL_TYPE = 1;

    /**
     * The instance of the global type
     */
    public static final TransactionModeType GLOBAL = new TransactionModeType(GLOBAL_TYPE, "global");

    /**
     * Field _memberTable.
     */
    private static java.util.Hashtable _memberTable = init();

    /**
     * Field type.
     */
    private final int type;

    /**
     * Field stringValue.
     */
    private java.lang.String stringValue = null;


      //----------------/
     //- Constructors -/
    //----------------/

    private TransactionModeType(final int type, final java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate.Returns an enumeration of all possible
     * instances of TransactionModeType
     * 
     * @return an Enumeration over all possible instances of
     * TransactionModeType
     */
    public static java.util.Enumeration enumerate(
    ) {
        return _memberTable.elements();
    }

    /**
     * Method getType.Returns the type of this TransactionModeType
     * 
     * @return the type of this TransactionModeType
     */
    public int getType(
    ) {
        return this.type;
    }

    /**
     * Method init.
     * 
     * @return the initialized Hashtable for the member table
     */
    private static java.util.Hashtable init(
    ) {
        Hashtable members = new Hashtable();
        members.put("local", LOCAL);
        members.put("global", GLOBAL);
        return members;
    }

    /**
     * Method readResolve. will be called during deserialization to
     * replace the deserialized object with the correct constant
     * instance.
     * 
     * @return this deserialized object
     */
    private java.lang.Object readResolve(
    ) {
        return valueOf(this.stringValue);
    }

    /**
     * Method toString.Returns the String representation of this
     * TransactionModeType
     * 
     * @return the String representation of this TransactionModeType
     */
    public java.lang.String toString(
    ) {
        return this.stringValue;
    }

    /**
     * Method valueOf.Returns a new TransactionModeType based on
     * the given String value.
     * 
     * @param string
     * @return the TransactionModeType value of parameter 'string'
     */
    public static org.castor.cpa.test.framework.xml.types.TransactionModeType valueOf(
            final java.lang.String string) {
        java.lang.Object obj = null;
        if (string != null) {
            obj = _memberTable.get(string);
        }
        if (obj == null) {
            String err = "" + string + " is not a valid TransactionModeType";
            throw new IllegalArgumentException(err);
        }
        return (TransactionModeType) obj;
    }

}
