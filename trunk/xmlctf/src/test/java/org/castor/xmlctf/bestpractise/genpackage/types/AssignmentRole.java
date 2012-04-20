/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id$
 */

package org.castor.xmlctf.bestpractise.genpackage.types;

/**
 * Class AssignmentRole.
 * 
 * @version $Revision$ $Date$
 */
public class AssignmentRole implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The Transferee type
     */
    public static final int TRANSFEREE_TYPE = 0;

    /**
     * The instance of the Transferee type
     */
    public static final AssignmentRole TRANSFEREE = new AssignmentRole(TRANSFEREE_TYPE, "Transferee");

    /**
     * The Transferor type
     */
    public static final int TRANSFEROR_TYPE = 1;

    /**
     * The instance of the Transferor type
     */
    public static final AssignmentRole TRANSFEROR = new AssignmentRole(TRANSFEROR_TYPE, "Transferor");

    /**
     * The RemainingParty type
     */
    public static final int REMAININGPARTY_TYPE = 2;

    /**
     * The instance of the RemainingParty type
     */
    public static final AssignmentRole REMAININGPARTY = new AssignmentRole(REMAININGPARTY_TYPE, "RemainingParty");

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

    private AssignmentRole(final int type, final java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate.Returns an enumeration of all possible
     * instances of AssignmentRole
     * 
     * @return an Enumeration over all possible instances of
     * AssignmentRole
     */
    public static java.util.Enumeration enumerate(
    ) {
        return _memberTable.elements();
    }

    /**
     * Method getType.Returns the type of this AssignmentRole
     * 
     * @return the type of this AssignmentRole
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
        java.util.Hashtable members = new java.util.Hashtable();
        members.put("Transferee", TRANSFEREE);
        members.put("Transferor", TRANSFEROR);
        members.put("RemainingParty", REMAININGPARTY);
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
     * AssignmentRole
     * 
     * @return the String representation of this AssignmentRole
     */
    public java.lang.String toString(
    ) {
        return this.stringValue;
    }

    /**
     * Method valueOf.Returns a new AssignmentRole based on the
     * given String value.
     * 
     * @param string
     * @return the AssignmentRole value of parameter 'string'
     */
    public static org.castor.xmlctf.bestpractise.genpackage.types.AssignmentRole valueOf(
            final java.lang.String string) {
        java.lang.Object obj = null;
        if (string != null) {
            obj = _memberTable.get(string);
        }
        if (obj == null) {
            String err = "" + string + " is not a valid AssignmentRole";
            throw new IllegalArgumentException(err);
        }
        return (AssignmentRole) obj;
    }

}
