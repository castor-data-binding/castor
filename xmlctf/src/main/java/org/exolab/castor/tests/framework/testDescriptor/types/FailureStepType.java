/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1-M2</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.tests.framework.testDescriptor.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.Hashtable;

/**
 * Class FailureStepType.
 * 
 * @version $Revision$ $Date$
 */
public class FailureStepType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The parse-schema type
     */
    public static final int PARSE_SCHEMA_TYPE = 0;

    /**
     * The instance of the parse-schema type
     */
    public static final FailureStepType PARSE_SCHEMA = new FailureStepType(PARSE_SCHEMA_TYPE, "parse-schema");

    /**
     * The write-schema type
     */
    public static final int WRITE_SCHEMA_TYPE = 1;

    /**
     * The instance of the write-schema type
     */
    public static final FailureStepType WRITE_SCHEMA = new FailureStepType(WRITE_SCHEMA_TYPE, "write-schema");

    /**
     * The source-generation type
     */
    public static final int SOURCE_GENERATION_TYPE = 2;

    /**
     * The instance of the source-generation type
     */
    public static final FailureStepType SOURCE_GENERATION = new FailureStepType(SOURCE_GENERATION_TYPE, "source-generation");

    /**
     * The source-compilation type
     */
    public static final int SOURCE_COMPILATION_TYPE = 3;

    /**
     * The instance of the source-compilation type
     */
    public static final FailureStepType SOURCE_COMPILATION = new FailureStepType(SOURCE_COMPILATION_TYPE, "source-compilation");

    /**
     * The load-generated-classes type
     */
    public static final int LOAD_GENERATED_CLASSES_TYPE = 4;

    /**
     * The instance of the load-generated-classes type
     */
    public static final FailureStepType LOAD_GENERATED_CLASSES = new FailureStepType(LOAD_GENERATED_CLASSES_TYPE, "load-generated-classes");

    /**
     * The unmarshal-reference type
     */
    public static final int UNMARSHAL_REFERENCE_TYPE = 5;

    /**
     * The instance of the unmarshal-reference type
     */
    public static final FailureStepType UNMARSHAL_REFERENCE = new FailureStepType(UNMARSHAL_REFERENCE_TYPE, "unmarshal-reference");

    /**
     * The marshal-to-disk type
     */
    public static final int MARSHAL_TO_DISK_TYPE = 6;

    /**
     * The instance of the marshal-to-disk type
     */
    public static final FailureStepType MARSHAL_TO_DISK = new FailureStepType(MARSHAL_TO_DISK_TYPE, "marshal-to-disk");

    /**
     * The compare-to-reference type
     */
    public static final int COMPARE_TO_REFERENCE_TYPE = 7;

    /**
     * The instance of the compare-to-reference type
     */
    public static final FailureStepType COMPARE_TO_REFERENCE = new FailureStepType(COMPARE_TO_REFERENCE_TYPE, "compare-to-reference");

    /**
     * The second-compare type
     */
    public static final int SECOND_COMPARE_TYPE = 8;

    /**
     * The instance of the second-compare type
     */
    public static final FailureStepType SECOND_COMPARE = new FailureStepType(SECOND_COMPARE_TYPE, "second-compare");

    /**
     * The listener-comparison type
     */
    public static final int LISTENER_COMPARISON_TYPE = 9;

    /**
     * The instance of the listener-comparison type
     */
    public static final FailureStepType LISTENER_COMPARISON = new FailureStepType(LISTENER_COMPARISON_TYPE, "listener-comparison");

    /**
     * The second-unmarshal type
     */
    public static final int SECOND_UNMARSHAL_TYPE = 10;

    /**
     * The instance of the second-unmarshal type
     */
    public static final FailureStepType SECOND_UNMARSHAL = new FailureStepType(SECOND_UNMARSHAL_TYPE, "second-unmarshal");

    /**
     * The custom-test type
     */
    public static final int CUSTOM_TEST_TYPE = 11;

    /**
     * The instance of the custom-test type
     */
    public static final FailureStepType CUSTOM_TEST = new FailureStepType(CUSTOM_TEST_TYPE, "custom-test");

    /**
     * Field _memberTable.
     */
    private static java.util.Hashtable _memberTable = init();

    /**
     * Field type.
     */
    private int type = -1;

    /**
     * Field stringValue.
     */
    private java.lang.String stringValue = null;


      //----------------/
     //- Constructors -/
    //----------------/

    private FailureStepType(final int type, final java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate.Returns an enumeration of all possible
     * instances of FailureStepType
     * 
     * @return an Enumeration over all possible instances of
     * FailureStepType
     */
    public static java.util.Enumeration enumerate(
    ) {
        return _memberTable.elements();
    }

    /**
     * Method getType.Returns the type of this FailureStepType
     * 
     * @return the type of this FailureStepType
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
        members.put("parse-schema", PARSE_SCHEMA);
        members.put("write-schema", WRITE_SCHEMA);
        members.put("source-generation", SOURCE_GENERATION);
        members.put("source-compilation", SOURCE_COMPILATION);
        members.put("load-generated-classes", LOAD_GENERATED_CLASSES);
        members.put("unmarshal-reference", UNMARSHAL_REFERENCE);
        members.put("marshal-to-disk", MARSHAL_TO_DISK);
        members.put("compare-to-reference", COMPARE_TO_REFERENCE);
        members.put("second-compare", SECOND_COMPARE);
        members.put("listener-comparison", LISTENER_COMPARISON);
        members.put("second-unmarshal", SECOND_UNMARSHAL);
        members.put("custom-test", CUSTOM_TEST);
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
     * FailureStepType
     * 
     * @return the String representation of this FailureStepType
     */
    public java.lang.String toString(
    ) {
        return this.stringValue;
    }

    /**
     * Method valueOf.Returns a new FailureStepType based on the
     * given String value.
     * 
     * @param string
     * @return the FailureStepType value of parameter 'string'
     */
    public static org.exolab.castor.tests.framework.testDescriptor.types.FailureStepType valueOf(
            final java.lang.String string) {
        java.lang.Object obj = null;
        if (string != null) {
            obj = _memberTable.get(string);
        }
        if (obj == null) {
            String err = "" + string + " is not a valid FailureStepType";
            throw new IllegalArgumentException(err);
        }
        return (FailureStepType) obj;
    }

}
