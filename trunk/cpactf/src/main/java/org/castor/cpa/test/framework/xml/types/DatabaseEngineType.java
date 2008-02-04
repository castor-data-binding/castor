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
 * Class DatabaseEngineType.
 * 
 * @version $Revision$ $Date$
 */
public class DatabaseEngineType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The db2 type
     */
    public static final int DB2_TYPE = 0;

    /**
     * The instance of the db2 type
     */
    public static final DatabaseEngineType DB2 = new DatabaseEngineType(DB2_TYPE, "db2");

    /**
     * The derby type
     */
    public static final int DERBY_TYPE = 1;

    /**
     * The instance of the derby type
     */
    public static final DatabaseEngineType DERBY = new DatabaseEngineType(DERBY_TYPE, "derby");

    /**
     * The hsql type
     */
    public static final int HSQL_TYPE = 2;

    /**
     * The instance of the hsql type
     */
    public static final DatabaseEngineType HSQL = new DatabaseEngineType(HSQL_TYPE, "hsql");

    /**
     * The mysql type
     */
    public static final int MYSQL_TYPE = 3;

    /**
     * The instance of the mysql type
     */
    public static final DatabaseEngineType MYSQL = new DatabaseEngineType(MYSQL_TYPE, "mysql");

    /**
     * The oracle type
     */
    public static final int ORACLE_TYPE = 4;

    /**
     * The instance of the oracle type
     */
    public static final DatabaseEngineType ORACLE = new DatabaseEngineType(ORACLE_TYPE, "oracle");

    /**
     * The pointbase type
     */
    public static final int POINTBASE_TYPE = 5;

    /**
     * The instance of the pointbase type
     */
    public static final DatabaseEngineType POINTBASE = new DatabaseEngineType(POINTBASE_TYPE, "pointbase");

    /**
     * The postgresql type
     */
    public static final int POSTGRESQL_TYPE = 6;

    /**
     * The instance of the postgresql type
     */
    public static final DatabaseEngineType POSTGRESQL = new DatabaseEngineType(POSTGRESQL_TYPE, "postgresql");

    /**
     * The progress type
     */
    public static final int PROGRESS_TYPE = 7;

    /**
     * The instance of the progress type
     */
    public static final DatabaseEngineType PROGRESS = new DatabaseEngineType(PROGRESS_TYPE, "progress");

    /**
     * The sapdb type
     */
    public static final int SAPDB_TYPE = 8;

    /**
     * The instance of the sapdb type
     */
    public static final DatabaseEngineType SAPDB = new DatabaseEngineType(SAPDB_TYPE, "sapdb");

    /**
     * The sql-server type
     */
    public static final int SQL_SERVER_TYPE = 9;

    /**
     * The instance of the sql-server type
     */
    public static final DatabaseEngineType SQL_SERVER = new DatabaseEngineType(SQL_SERVER_TYPE, "sql-server");

    /**
     * The sybase type
     */
    public static final int SYBASE_TYPE = 10;

    /**
     * The instance of the sybase type
     */
    public static final DatabaseEngineType SYBASE = new DatabaseEngineType(SYBASE_TYPE, "sybase");

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

    private DatabaseEngineType(final int type, final java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate.Returns an enumeration of all possible
     * instances of DatabaseEngineType
     * 
     * @return an Enumeration over all possible instances of
     * DatabaseEngineType
     */
    public static java.util.Enumeration enumerate(
    ) {
        return _memberTable.elements();
    }

    /**
     * Method getType.Returns the type of this DatabaseEngineType
     * 
     * @return the type of this DatabaseEngineType
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
        members.put("db2", DB2);
        members.put("derby", DERBY);
        members.put("hsql", HSQL);
        members.put("mysql", MYSQL);
        members.put("oracle", ORACLE);
        members.put("pointbase", POINTBASE);
        members.put("postgresql", POSTGRESQL);
        members.put("progress", PROGRESS);
        members.put("sapdb", SAPDB);
        members.put("sql-server", SQL_SERVER);
        members.put("sybase", SYBASE);
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
     * DatabaseEngineType
     * 
     * @return the String representation of this DatabaseEngineType
     */
    public java.lang.String toString(
    ) {
        return this.stringValue;
    }

    /**
     * Method valueOf.Returns a new DatabaseEngineType based on the
     * given String value.
     * 
     * @param string
     * @return the DatabaseEngineType value of parameter 'string'
     */
    public static org.castor.cpa.test.framework.xml.types.DatabaseEngineType valueOf(
            final java.lang.String string) {
        java.lang.Object obj = null;
        if (string != null) {
            obj = _memberTable.get(string);
        }
        if (obj == null) {
            String err = "" + string + " is not a valid DatabaseEngineType";
            throw new IllegalArgumentException(err);
        }
        return (DatabaseEngineType) obj;
    }

}
