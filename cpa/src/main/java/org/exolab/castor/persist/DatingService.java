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
package org.exolab.castor.persist;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.exolab.castor.jdo.engine.SQLEngine;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.loader.Types;

/**
 * DataService is a helper class for constructing <tt>ClassMolder</tt>s and
 * pairing up ClassMolders which depends and extends the other.
 *
 * @author <a href="mailto:yip@intalio.com">Thomas Yip</a>
 */
public class DatingService {

    private ClassLoader _loader;

    private Hashtable _clsMolders;

    private Vector _needExtendsClassMolder;

    private Vector _needDependsClassMolder;

    private Vector _needFieldClass;

    private Hashtable _javaClasses;

    DatingService(final ClassLoader loader) {
        this._loader = loader;
    }

    /**
     * Indicate that all ClassMolder is registered. DatingService
     * will resolve all the outstanding relation now.
     */
    void close() throws MappingException {

        Enumeration e;
        ClassMolder initiateCm;
        ClassMolder targetCm;
        FieldMolder initiateFm;
        
        // resolve extends
        if (_needExtendsClassMolder != null) {
            e = _needExtendsClassMolder.elements();
            while (e.hasMoreElements()) {
                Pair pair = (Pair) e.nextElement();
                initiateCm = (ClassMolder) pair._value;
                targetCm = (ClassMolder) _clsMolders.get(pair._key);
                if (targetCm == null) {
                    throw new MappingException(
                            "Extended element, \"" + pair._key + "\"  not found!");
                }
                initiateCm.setExtends(targetCm);
                ((SQLEngine) initiateCm.getPersistence()).setExtends(
                        (SQLEngine) targetCm.getPersistence());
            }
        }

        // resolve depends
        if (_needDependsClassMolder != null) {
            e = _needDependsClassMolder.elements();
            while (e.hasMoreElements()) {
                Pair pair = (Pair) e.nextElement();
                initiateCm = (ClassMolder) pair._value;
                targetCm = (ClassMolder) _clsMolders.get(pair._key);
                if (targetCm == null) {
                    throw new MappingException(
                            "Depended element, \"" + pair._key + "\"  not found!");
                }
                initiateCm.setDepends(targetCm);
            }
        }

        // resolve depends field
        if (_needFieldClass != null) {
            e = _needFieldClass.elements();
            while (e.hasMoreElements()) {
                Pair pair = (Pair) e.nextElement();
                initiateFm = (FieldMolder) pair._value;
                targetCm = (ClassMolder) _clsMolders.get(pair._key);
                if (targetCm == null) {
                    throw new MappingException("Field element, \"" + pair._key + "\"  not found!");
                }
                initiateFm.setFieldClassMolder(targetCm);
                
                // initiateFm.getEnclosingClassMolder().resetResolver (initiateFm);
            }
        }

    } 

    /** 
     * Pair up ClassMolder and it extends class.
     * @return true if they can be paired up immediately.
     */
    boolean pairExtends(final ClassMolder me, final String extName) throws MappingException {

        if ((extName == null) || extName.equals("")) {
            throw new IllegalArgumentException("Null classname not allowed!");
        }

        ClassMolder clsMold = (ClassMolder) _clsMolders.get(extName);
        if (clsMold != null) {
            me.setExtends(clsMold);
            SQLEngine sql = ((SQLEngine) me.getPersistence());
            if (sql == null) {
                throw new MappingException(
                        "Class " + me + " extends on " + extName
                        + " which is not persistence capable!");
            }
            sql.setExtends((SQLEngine) clsMold.getPersistence());
            return true;
        }

        if (_needExtendsClassMolder == null) {
            _needExtendsClassMolder = new Vector();
        }

        _needExtendsClassMolder.add(new Pair(extName, me));
        return false;
    }

    /** 
     * Pair up ClassMolder and it depends class.
     * @return true if they can be paired up immediately.
     */
    boolean pairDepends(final ClassMolder me, final String depName) {
        if ((depName == null) || (depName.equals(""))) {
            return true;
        }

        ClassMolder clsMold = (ClassMolder) _clsMolders.get(depName);
        if (clsMold != null) {
            me.setDepends(clsMold);
            return true;
        }

        if (_needDependsClassMolder == null) {
            _needDependsClassMolder = new Vector();
        }

        _needDependsClassMolder.add(new Pair(depName, me));
        return false;
    }

    /**
     * Resolve the java.lang.Class of the fully qualified class name.
     */
    Class resolve(final String className) throws ClassNotFoundException {
        Class resolved;
        if (_javaClasses == null) {
            _javaClasses = new Hashtable();
        } else if (_javaClasses.contains(className)) {
            return (Class) _javaClasses.get(className);
        }
        resolved = Types.typeFromName(_loader, className);
        _javaClasses.put(className, resolved);
        return resolved;
    }

    /**
     * Pair the FieldMolder with the ClassMolder of typeName.
     * 
     * @param fieldMolder the fieldMolder to be paired.
     * @param typeName Type of the field which the FieldMolder represents.
     * @exception MappingException indicates that the pairing failed.
     */
    boolean pairFieldClass(final FieldMolder fieldMolder, final String typeName)
    throws MappingException {
        
        try {
            if ((typeName == null) || typeName.equals("")) {
                return true;
            }

            if (Types.isSimpleType(resolve(typeName))) {
                return true;
            }

            if (Types.isEnumType(resolve(typeName))) {
                return true;
            }

            ClassMolder clsMold = (ClassMolder) _clsMolders.get(typeName);
            if (clsMold != null) {
                fieldMolder.setFieldClassMolder(clsMold);
                // fieldMolder.getEnclosingClassMolder().resetResolver(fieldMolder);
                return true;
            }

            if (_needFieldClass == null) {
                _needFieldClass = new Vector();
            }
            _needFieldClass.add(new Pair(typeName, fieldMolder));
            return false;
        } catch (ClassNotFoundException e) {
            throw new MappingException("ClassNotFound :\n" + e);
        }
    }

    /**
     * Register the name of a ClassMolder which will be pairing
     * up.
     */
    void register(final String name, final ClassMolder clsMold) {
        if (_clsMolders == null) {
            _clsMolders = new Hashtable();
        }
        _clsMolders.put(name, clsMold);
    }

    private final class Pair {
        private Object _key;
        private Object _value;
        
        private Pair(final Object key, final Object value) {
            _key = key;
            _value = value;
        }
    }
}
