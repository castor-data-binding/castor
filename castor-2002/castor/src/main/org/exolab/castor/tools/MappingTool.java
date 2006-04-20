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


package org.exolab.castor.tools;


import java.io.Writer;
import java.io.FileWriter;
import java.util.Hashtable;
import java.util.Enumeration;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Introspector;
import org.exolab.castor.xml.XMLFieldDescriptor;
import org.exolab.castor.xml.XMLClassDescriptor;
import org.exolab.castor.mapping.xml.types.NodeType;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.xml.MappingRoot;
import org.exolab.castor.mapping.xml.ClassMapping;
import org.exolab.castor.mapping.xml.FieldMapping;
import org.exolab.castor.mapping.xml.MapTo;
import org.exolab.castor.mapping.xml.types.CollectionType;
import org.exolab.castor.mapping.xml.BindXml;
import org.exolab.castor.mapping.loader.Types;
import org.exolab.castor.util.Messages;


/**
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class MappingTool
{


    private Hashtable _mappings = new Hashtable();

    private Introspector _intro = new Introspector();

    public static void main( String[] args )
    {
        MappingTool tool;

        try {
            tool = new MappingTool();
            tool.addClass( "myapp.Product" );
            tool.write( new FileWriter( "test.xml" ) );
        } catch ( Exception except ) {
            System.out.println( except );
            except.printStackTrace();
        }
    }


    public void addClass( String name )
        throws MappingException
    {
        try {
            addClass( Class.forName( name ) );
        } catch ( ClassNotFoundException except ) {
            throw new MappingException( except );
        }
    }


    public void addClass( Class cls )
        throws MappingException
    {
        ClassMapping clsMap;
        Method[]     methods;

        if ( _mappings.get( cls ) != null )
            return;
        if ( ! Types.isConstructable( cls ) )
            throw new MappingException( "mapping.classNotConstructable", cls.getName() );

        XMLClassDescriptor xmlClass;
        FieldDescriptor[]  fields;
        ClassMapping       classMap;
        FieldMapping       fieldMap;

        try {
            xmlClass = _intro.generateClassDescriptor( cls );
        } catch ( MarshalException except ) {
            throw new MappingException( except );
        }
        classMap = new ClassMapping();
        classMap.setName( cls.getName() );
        classMap.setDescription( "Default mapping for class " + cls.getName() );
        classMap.setMapTo( new MapTo() );
        classMap.getMapTo().setXml( xmlClass.getXMLName() );
        classMap.getMapTo().setNsUri( xmlClass.getNameSpaceURI() );
        classMap.getMapTo().setNsPrefix( xmlClass.getNameSpacePrefix() );
        _mappings.put( cls, classMap );
        fields = xmlClass.getFields();
        for ( int i = 0 ; i < fields.length ; ++i ) {
            fieldMap = new FieldMapping();
            fieldMap.setName( fields[ i ].getFieldName() );
            fieldMap.setType( fields[ i ].getFieldType().getName() );
            fieldMap.setRequired( fields[ i ].isRequired() );
            fieldMap.setTransient( fields[ i ].isTransient() );
            if ( fields[ i ].isMultivalued() )
                fieldMap.setCollection( CollectionType.ENUMERATE );
            fieldMap.setBindXml( new BindXml() );
            fieldMap.getBindXml().setName( ( (XMLFieldDescriptor) fields[ i ] ).getXMLName() );
            fieldMap.getBindXml().setNode( NodeType.valueOf( ((XMLFieldDescriptor) fields[ i ]).getNodeType().toString() ) );
            classMap.addFieldMapping( fieldMap );

            if ( ! Types.isSimpleType( fields[ i ].getFieldType() ) )
                addClass( fields[ i ].getFieldType() );
        }
    }


    public void write( Writer writer )
        throws MappingException
    {
        Marshaller  marshal;
        MappingRoot mapping;
        Enumeration enum;

        try {
            mapping = new MappingRoot();
            mapping.setDescription( "Castor generated mapping file" );
            enum = _mappings.elements();
            while ( enum.hasMoreElements() )
                mapping.addClassMapping( (ClassMapping) enum.nextElement() );
            marshal = new Marshaller( writer );
            marshal.marshal( mapping );
        } catch ( Exception except ) {
            throw new MappingException( except );
        }
    }


}






