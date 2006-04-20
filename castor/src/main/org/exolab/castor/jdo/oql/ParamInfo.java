/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.jdo.oql;

import java.util.Vector;
import org.exolab.castor.jdo.QueryException;
import org.exolab.castor.mapping.loader.Types;
import org.exolab.castor.mapping.TypeConvertor;
import org.exolab.castor.jdo.engine.SQLTypes;
import org.exolab.castor.jdo.engine.JDOFieldDescriptor;

/**
 * A class to store and check information about numbered query parameters.
 *
 * @author <a href="mailto:nissim@nksystems.com">Nissim Karpenstein</a>
 * @version $Revision$ $Date$
 */
public class ParamInfo {

  private String _userDefinedType;
  private String _systemType;

  private Class _class;

  private Vector _sqlQueryParamMap;

  private Class _fieldType;

  private Class _sqlType;

  /**
   * Convertor that converts from the parameter type to SQL type of the parameter,
   * if the latter is defined and not equal to the parameter class, otherwise
   * equals null.
   */
  private TypeConvertor _convertor;

  /**
   * The type convertor parameter.
   */
  private String        _convertorParam;


  /**
   * Creates a new ParamInfo.  Which checks for incompatibilities between types.
   *
   * @param userDefinedType The user defined type, empty string if undefined.
   * @param systemType The system generated type
   * @throws QueryException if the user defined type cannot be converted to the
   *      systemType or if the type is not found.
   */
  public ParamInfo( String userDefinedType, String systemType, JDOFieldDescriptor desc )
      throws QueryException
  {
    _userDefinedType = userDefinedType;
    _systemType = systemType;

    _sqlQueryParamMap = new Vector();

    Class userClass = null;
    Class systemClass = null;
    try {
      systemClass = Class.forName(systemType);
    }
    catch (Exception e) {
      throw new QueryException( "Error: Could not find system defined class: " + systemType );
    }

    if ( ! userDefinedType.equals("") ) {
      try {
        userClass = Types.typeFromName(getClass().getClassLoader(), userDefinedType);

        if ( userClass.isPrimitive() )
          userClass = Types.typeFromPrimitive( userClass );

      }
      catch (Exception e) {
        throw new QueryException( "The class " + userClass + " could not be found." );
      }

      if ( ! systemClass.isAssignableFrom(userClass) ) {
        if ( ! ( java.lang.Number.class.isAssignableFrom(userClass) &&
                 java.lang.Number.class.isAssignableFrom(systemClass) ) )
          throw new QueryException( "The class " + userClass + " is incompatible with the system defined class " + systemType );
      }

      _class = userClass;
    } else {
      _class = systemClass;
    }
    if (desc != null) {
        _fieldType = desc.getFieldType();
        try {
            _sqlType = SQLTypes.typeFromSQLType(desc.getSQLType()[0]);
        } catch (Exception ex) {
            throw new QueryException( "Can't determine SQL class: " + ex );
        }
        _convertor = desc.getConvertor();
        _convertorParam = desc.getConvertorParam();
    }
  }

  /**
   * Checks whether the userDefinedType and systemType match those previousle
   * specified in the constructor.
   *
   * @param userDefinedType The user defined type, empty string if undefined.
   * @param systemType The system generated type
   * @throws QueryException if the user defined type is not the same as the
   *    one prevuiously specified in the constructor, or if the systemType is
   *    not convertable to the original systemType.
   */
  public void check( String userDefinedType, String systemType )
      throws QueryException
  {
    if ( ! _userDefinedType.equals(userDefinedType) )
      throw new QueryException( "Different types were specified for the same numbered parameter." );

    if ( ! systemType.equals(_systemType) ) {
      Class systemClass = null;
      try {
        systemClass = Class.forName(systemType);
      }
      catch (Exception e) {
        throw new QueryException( "Error: Could notfind system defined class: " + systemType );
      }

      if ( ! userDefinedType.equals("") ) {
        Class userClass = null;
        try {
          userClass = Class.forName(_userDefinedType);
        }
        catch (Exception e) {
          throw new QueryException( "The class " + userClass + " could not be found." );
        }

        if ( ! systemClass.isAssignableFrom(userClass) )
          throw new QueryException( "The class " + userDefinedType + " is incompatible with the system defined class " + systemType );

      }
    }
  }

  /**
   * Specifies whether this parameter was specified in the OQL with a User
   * defined type, like $(int)1.
   *
   * @return True if this parameter whas a user defined type otherwise false
   */
  public boolean isUserDefined() {
    return ! _userDefinedType.equals("");
  }

  /**
   * Maps this numbered parameter to the numbered SQL parameter.
   *
   * @param sqlParamIndex The SQL parameter number.
   */
  public void mapToSQLParam(int sqlParamIndex) {
    _sqlQueryParamMap.addElement(new Integer(sqlParamIndex));
  }

  /**
   * Accessor method for _sqlQueryParamMap.
   *
   * @return private member _sqlQueryParamMap.
   */
  public Vector getParamMap() {
    return _sqlQueryParamMap;
  }

  /**
   * Accessor method for _class.
   *
   * @return private member _class
   */
  public Class getTheClass() {
    return _class;
  }

    /**
     * Returns the Java field type
     *
     * @return Java field type
     */
    public Class getFieldType()
    {
        return _fieldType;
    }


    /**
     * Returns the SQL type
     *
     * @return SQL type
     */
    public Class getSQLType()
    {
        return _sqlType;
    }


    /**
     * @return Convertor that converts from the parameter type to SQL type of the parameter,
     * if the latter is defined and not equal to the parameter class, otherwise
     * returns null.
     */
    public TypeConvertor getConvertor() {
        return _convertor;
    }

    /**
     * Returns the convertor parameter.
     *
     * @return Convertor parameter
     */
    public String getConvertorParam()
    {
        return _convertorParam;
    }


}
