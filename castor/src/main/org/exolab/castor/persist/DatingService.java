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

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import org.exolab.castor.jdo.engine.SQLEngine;
import org.exolab.castor.mapping.loader.Types;
import org.exolab.castor.mapping.MappingException;

/**
 * DataService is a helper class for constructing <tt>ClassMolder</tt>s and
 * pairing up ClassMolders which depends and extends the other.
 *
 * @author <a href="mailto:yip@intalio.com">Thomas Yip</a>
 */
class DatingService {

    ClassLoader loader;

    Hashtable clsMolders;

    Vector needExtendsClassMolder;

    Vector needDependsClassMolder;

    Vector needFieldClass;

    Hashtable javaClasses;

    DatingService( ClassLoader loader ) {
        this.loader = loader;
    }

    /**
     * Indicate that all ClassMolder is registered. DatingService
     * will resolve all the outstanding relation now.
     */
    void close() throws MappingException{

        Enumeration e;
        ClassMolder initiateCm, targetCm;
        FieldMolder initiateFm;
        ClassMolder molder, extend;
        Object o;

        // resolve extends
        if ( needExtendsClassMolder != null ) {
            e = needExtendsClassMolder.elements();
            while ( e.hasMoreElements() ) {
                Pair pair = (Pair)e.nextElement();
                initiateCm = (ClassMolder) pair.value;
                targetCm = (ClassMolder) clsMolders.get( pair.key );
                if ( targetCm == null )
                    throw new MappingException("Extended element, \""+pair.key+"\"  not found!");
                initiateCm.setExtends( targetCm );
                ((SQLEngine)initiateCm.getPersistence()).setExtends( (SQLEngine) targetCm.getPersistence() );
            }
        }

        // resolve depends
        if ( needDependsClassMolder != null ) {
            e = needDependsClassMolder.elements();
            while ( e.hasMoreElements() ) {
                Pair pair = (Pair)e.nextElement();
                initiateCm = (ClassMolder) pair.value;
                targetCm = (ClassMolder) clsMolders.get( pair.key );
                if ( targetCm == null )
                    throw new MappingException("Depended element, \""+pair.key+"\"  not found!");
                initiateCm.setDepends( targetCm );
            }
        }

        // resolve depends field
        if ( needFieldClass != null ) {
            e = needFieldClass.elements();
            while ( e.hasMoreElements() ) {
                Pair pair = (Pair)e.nextElement();
                initiateFm = (FieldMolder) pair.value;
                targetCm = (ClassMolder) clsMolders.get( pair.key );
                if ( targetCm == null )
                    throw new MappingException("Field element, \""+pair.key+"\"  not found!");
                initiateFm.setFieldClassMolder( targetCm );
            }
        }

    } 

    /** 
     * Pair up ClassMolder and it extends class.
     * @return true if they can be paired up immediately.
     */
    boolean pairExtends( ClassMolder me, String extName ) 
            throws MappingException {

        if ( extName == null || extName.equals("") )
            throw new IllegalArgumentException("Null classname not allowed!");

        ClassMolder clsMold = (ClassMolder) clsMolders.get( extName );
        if ( clsMold != null ) {
            me.setExtends( clsMold );
            SQLEngine sql = ((SQLEngine)me.getPersistence());
            if ( sql == null )
                throw new MappingException("Class "+me+" extends on "+extName+" which is not persistence capable!");
            sql.setExtends((SQLEngine)clsMold.getPersistence() );
            return true;
        }

        if ( needExtendsClassMolder == null )
            needExtendsClassMolder = new Vector();

        needExtendsClassMolder.add( new Pair( extName, me ) );
        return false;
    }

    /** 
     * Pair up ClassMolder and it depends class.
     * @return true if they can be paired up immediately.
     */
    boolean pairDepends( ClassMolder me, String depName ) {
        if ( depName == null || depName.equals("") )
            return true;

        ClassMolder clsMold = (ClassMolder) clsMolders.get( depName );
        if ( clsMold != null ) {
            me.setDepends( clsMold );
            return true;
        }

        if ( needDependsClassMolder == null )
            needDependsClassMolder = new Vector();

        needDependsClassMolder.add( new Pair( depName, me ) );
        return false;
    }

    /**
     * Resolve the java.lang.Class of the fully qualified class name
     *
     */
    Class resolve( String className ) throws ClassNotFoundException {
        Class resolved;
        if ( javaClasses == null )
            javaClasses = new Hashtable();
        else if ( javaClasses.contains( className ) )
            return (Class) javaClasses.get( className );
        resolved = Types.typeFromName( loader, className );
        javaClasses.put( className, resolved );
        return resolved;
    }

    /**
     * Pair the FieldMolder with the ClassMolder of typeName
     * @param fh  the fieldMolder to be paired.
     * @param typeName the type of the field which the FieldMolder
     *        represent
     */
    boolean pairFieldClass( FieldMolder fh, String typeName ) throws MappingException {
        
        try {
            if ( typeName == null || typeName.equals("") )
                return true;

            if ( Types.isSimpleType( resolve( typeName ) ) )
                return true;

            ClassMolder clsMold = (ClassMolder) clsMolders.get( typeName );
            if ( clsMold != null ) {
                fh.setFieldClassMolder( clsMold );
                return true;
            }

            if ( needFieldClass == null ) needFieldClass = new Vector();
            needFieldClass.add( new Pair( typeName, fh ) );
            return false;
        } catch ( ClassNotFoundException e ) {
            throw new MappingException("ClassNotFound :\n"+e);
        }
    }

    /**
     * Register the name of a ClassMolder which will be pairing
     * up.
     */
    void register( String name, ClassMolder clsMold ) {
        if ( clsMolders == null )
            clsMolders = new Hashtable();
        clsMolders.put( name, clsMold );
    }

    private class Pair {
        public Object key;
        public Object value;
        private Pair( Object key, Object value ) {
            this.key = key;
            this.value = value;
        }
    }

}
