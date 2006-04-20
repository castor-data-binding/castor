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
 * $Id: DatingService.java,
 */


package org.exolab.castor.persist;


import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Collection;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.ClassNotPersistenceCapableException;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.ObjectDeletedException;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.ObjectModifiedException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.engine.SQLEngine;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.loader.Types;
import org.exolab.castor.mapping.xml.ClassMapping;
import org.exolab.castor.mapping.xml.FieldMapping;
import org.exolab.castor.mapping.loader.MappingLoader;
import org.exolab.castor.persist.spi.Persistence;
import org.exolab.castor.persist.spi.PersistenceQuery;
import org.exolab.castor.mapping.loader.RelationDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.persist.spi.CallbackInterceptor;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.persist.spi.LogInterceptor;
import org.exolab.castor.mapping.loader.ClassDescriptorImpl;
import org.exolab.castor.jdo.engine.JDOClassDescriptor;
import org.exolab.castor.jdo.engine.JDOFieldDescriptor;
import java.sql.Connection;
import java.util.Vector;
import java.util.ArrayList;


/**
 * DataService is a helper class for constructing <tt>ClassMolder</tt>s and
 * pairing up ClassMolders which depends and extends the other.
 *
 * @author <a href="mailto:yip@intalio.com">Thomas Yip</a>
 */
class DatingService {

    ClassLoader loader;

    Hashtable clsMolders;

    Hashtable relations;

    Hashtable needExtendsClassMolder;

    Hashtable needDependsClassMolder;

    Hashtable needFieldClass;

    Hashtable needRelation;

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
            e = needExtendsClassMolder.keys();
            while ( e.hasMoreElements() ) {
                o = e.nextElement();
                initiateCm = (ClassMolder) needExtendsClassMolder.get( o );
                targetCm = (ClassMolder) clsMolders.get( o );
                if ( targetCm == null )
                    throw new MappingException("Extended element, \""+o+"\"  not found!");
                initiateCm.setExtends( targetCm );
                ((SQLEngine)initiateCm.getPersistence()).setExtends( (SQLEngine) targetCm.getPersistence() );
            }
        }

        // resolve depends
        if ( needDependsClassMolder != null ) {
            e = needDependsClassMolder.keys();
            while ( e.hasMoreElements() ) {
                o = e.nextElement();
                initiateCm = (ClassMolder) needDependsClassMolder.get( o );
                targetCm = (ClassMolder) clsMolders.get( o );
                if ( targetCm == null )
                    throw new MappingException("Depended element, \""+o+"\"  not found!");
                initiateCm.setDepends( targetCm );
            }
        }

        // resolve depends field
        if ( needFieldClass != null ) {
            e = needFieldClass.keys();
            while ( e.hasMoreElements() ) {
                o = e.nextElement();
                initiateFm = (FieldMolder) needFieldClass.get( o );
                targetCm = (ClassMolder) clsMolders.get( o );
                if ( targetCm == null )
                    throw new MappingException("Field element, \""+o+"\"  not found!");
                initiateFm.setFieldClassMolder( targetCm );
            }
        }

    } 

    /** 
     * Pair up ClassMolder and it extends class.
     * @return true if they can be paired up immediately.
     */
    boolean pairExtends( ClassMolder me, String extName ) {
        if ( extName == null || extName.equals("") )
            throw new IllegalArgumentException("Null classname not allowed!");

        ClassMolder clsMold = (ClassMolder) clsMolders.get( extName );
        if ( clsMold != null ) {
            me.setExtends( clsMold );
            SQLEngine sql = ((SQLEngine)me.getPersistence());
            sql.setExtends((SQLEngine)clsMold.getPersistence() );
            return true;
        }

        if ( needExtendsClassMolder == null )
            needExtendsClassMolder = new Hashtable();

        needExtendsClassMolder.put( extName, me );
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
            needDependsClassMolder = new Hashtable();

        needDependsClassMolder.put( depName, me );
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

            if ( needFieldClass == null ) needFieldClass = new Hashtable();
            needFieldClass.put( typeName, fh );
            return false;
        } catch ( ClassNotFoundException e ) {
            throw new MappingException("ClassNotFound :\n"+e);
        }
    }

    boolean pairRelationDescriptor( FieldMolder fm, String fieldType, String encloseClassType ) throws MappingException {
        if ( fm.isManyToMany() ) return true;

        RelationDescriptor rd = getRelated( fieldType, encloseClassType );

        //fm.setRelationDescriptor( rd  );

        return true;
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

    /**
     * Register the RelationDescriptor
     */
    void registerRelation( RelationDescriptor rd ) {

        if ( relations == null )
            relations = new Hashtable();
        relations.put( rd.type1+"+"+rd.type2, rd );
        relations.put( rd.type2+"+"+rd.type1, rd );
    }

    /**
     * Return true if the classnames represents classes are relating
     * to each other.
     */
    boolean isRelated( String a, String b ) {
        RelationDescriptor rd;
        if ( relations == null )
            return false;
        if ( relations.get(a+"+"+b) != null )
            return true;
        return false;
    }

    /**
     * Get the RelationDescriptor given classnames of two classes.
     *
     */
    RelationDescriptor getRelated( String a, String b ) {
        RelationDescriptor rd;
        if ( relations == null )
            return null;
        return (RelationDescriptor) relations.get(a+"+"+b);
    }
}
