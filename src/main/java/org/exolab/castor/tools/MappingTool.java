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
 * Copyright 1999-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.tools;


//-- Castor Imports
import org.exolab.castor.builder.util.ConsoleDialog;
import org.exolab.castor.mapping.BindingType;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.loader.CollectionHandlers;
import org.exolab.castor.mapping.loader.AbstractMappingLoader;
import org.exolab.castor.mapping.loader.Types;
import org.exolab.castor.mapping.xml.BindXml;
import org.exolab.castor.mapping.xml.ClassChoice;
import org.exolab.castor.mapping.xml.ClassMapping;
import org.exolab.castor.mapping.xml.FieldMapping;
import org.exolab.castor.mapping.xml.MappingRoot;
import org.exolab.castor.mapping.xml.MapTo;
import org.exolab.castor.mapping.xml.types.BindXmlNodeType;
import org.exolab.castor.mapping.xml.types.FieldMappingCollectionType;
import org.exolab.castor.util.CommandLineOptions;
import org.exolab.castor.xml.JavaNaming;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Introspector;
import org.exolab.castor.xml.XMLFieldDescriptor;
import org.exolab.castor.xml.XMLClassDescriptor;
import org.exolab.castor.xml.util.XMLClassDescriptorResolverImpl;

//-- Java Imports
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Properties;
import java.lang.reflect.Array;

/**
 * A tool which uses the introspector to automatically
 * create mappings for a given set of classes.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @author <a href="keith AT kvisco DOT com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-01-30 14:37:08 -0700 (Mon, 30 Jan 2006) $
 */
public class MappingTool
{
    
    /**
     * Used for checking field names to see if they
     * begin with an underscore '_'
    **/
    private static final String UNDERSCORE = "_";
    
    /**
     * Hashtable of already generated mappings
    **/
    private Hashtable _mappings = null;

    /**
     * ClassDescriptorResolver for loading compiled
     * descriptors
    **/
    private XMLClassDescriptorResolverImpl _resolver = null;

    /**
     * Introspector to use if _forceIntrospection is enabled.
    **/
    private Introspector _introspector = null;
    
    /**
     * The internal MappingLoader to use for checking
     * whether or not we can find the proper accessor methods.
    **/
    private InternalLoader _mappingLoader = null;
    
    /**
     * Boolean to indicate that we should always
     * perform introspection for each class
     * even if a ClassDescriptor may exist.
    **/
    private boolean _forceIntrospection = false;
    
    public MappingTool() {
        _mappings      = new Hashtable();
        _resolver      = new XMLClassDescriptorResolverImpl();
        _mappingLoader = new InternalLoader();
    } //--MappingTool

    /**
     * Command line method
    **/
    public static void main( String[] args )
    {
        CommandLineOptions allOptions = new CommandLineOptions();

        //-- Input classname flag
        allOptions.addFlag("i", "classname", "Sets the input class");

        //-- Output filename flag
        String desc = "Sets the output mapping filename";
        allOptions.addFlag("o", "filename", desc, true);

        //-- Force flag
        desc = "Force overwriting of files.";
        allOptions.addFlag("f", "", desc, true);

        //-- Help flag
        desc = "Displays this help screen.";
        allOptions.addFlag("h", "", desc, true);

		//-- Process the specified command line options
        Properties options = allOptions.getOptions(args);

        //-- check for help option
        if (options.getProperty("h") != null) {
            PrintWriter pw = new PrintWriter(System.out, true);
            allOptions.printHelp(pw);
            pw.flush();
            return;
        }

        String  classname       = options.getProperty("i");
        String  mappingName     = options.getProperty("o");
        boolean force           = (options.getProperty("f") != null);
        
        
        if (classname == null) {
            PrintWriter pw = new PrintWriter(System.out, true);
            allOptions.printUsage(pw);
            pw.flush();
            return;
        }
        
        MappingTool tool;

        try {
            
            tool = new MappingTool();
            tool.addClass( classname );
            
            Writer writer = null;
            
            if ((mappingName == null) || (mappingName.length() == 0)) {
                writer = new PrintWriter( System.out, true );
            }
            else {
                File file = new File( mappingName );
                if (file.exists() && (!force)) {
                    ConsoleDialog dialog = new ConsoleDialog();
                    String message = "The file already exists. Do you wish "+
                        "to overwrite '" + mappingName + "'?";
                    if (!dialog.confirm(message)) return;
                }
                writer = new FileWriter( file );
            }
                
            tool.write( writer );
            
        } 
        catch ( Exception except ) {
            System.out.println( except );
            except.printStackTrace();
        }
    } //-- main

    /**
     * Adds the Class, specified by the given name, to the mapping file
     * 
     * @param name the name of the Class to add
    **/
    public void addClass( String name )
        throws MappingException
    {
        addClass( name, true);
    } //-- addClass

    /**
     * Adds the Class, specified by the given name, to the mapping file
     * 
     * @param name the name of the Class to add
     * @param deep, a flag to indicate that recursive processing
     * should take place and all classes used by the given
     * class should also be added to the mapping file. This
     * flag is true by default.
    **/
    public void addClass( String name, boolean deep )
        throws MappingException
    {
        if (name == null) 
            throw new MappingException("Cannot introspect a null class.");
            
        try {
            addClass( Class.forName( name ), deep );
        } 
        catch ( ClassNotFoundException except ) {
            throw new MappingException( except );
        }
    } //-- addClass

    /**
     * Adds the given Class to the mapping file
     *
     * @param cls the Class to add
    **/
    public void addClass( Class cls )
        throws MappingException
    {
        addClass( cls, true );
        
    } //-- addClass

    /**
     * Adds the given Class to the mapping file. If the
     * deep flag is true, all mappings for Classes used by
     * the given Class will also be added to the mapping file.
     *
     * @param cls the Class to add
     * @param deep, a flag to indicate that recursive processing
     * should take place and all classes used by the given
     * class should also be added to the mapping file. This
     * flag is true by default.
    **/
    public void addClass( Class cls, boolean deep )
        throws MappingException
    {
        if (cls == null) 
            throw new MappingException("Cannot introspect a null class.");
            
        if ( _mappings.get( cls ) != null )
            return;
            
        if ( cls.isArray() ) {
            
            Class cType = cls.getComponentType();
            if ( _mappings.get(cType) != null) return;
            if (Types.isSimpleType(cType)) return;                
            //-- handle component type
            addClass( cType );
        }
            
        if ( _forceIntrospection && (!Types.isConstructable( cls )))
            throw new MappingException( "mapping.classNotConstructable", cls.getName() );

        XMLClassDescriptor xmlClass;
        FieldDescriptor[]  fields;
        ClassMapping       classMap;
        FieldMapping       fieldMap;

        boolean introspected = false;
        try {
            if (_forceIntrospection) {
                xmlClass = _introspector.generateClassDescriptor( cls );
                introspected = true;
            }
            else {
                xmlClass = _resolver.resolve( cls );
                introspected = Introspector.introspected(xmlClass);
            }
        } 
        catch ( Exception except ) {
            throw new MappingException( except );
        }
        classMap = new ClassMapping();
        classMap.setName( cls.getName() );
        classMap.setDescription( "Default mapping for class " + cls.getName() );
        
        //-- prevent default access from showing up in the mapping
        classMap.setAccess(null); 
        
        //-- map-to
        MapTo mapTo = new MapTo();
        mapTo.setXml( xmlClass.getXMLName() );
        mapTo.setNsUri( xmlClass.getNameSpaceURI() );
        mapTo.setNsPrefix( xmlClass.getNameSpacePrefix() );
        classMap.setMapTo( mapTo );
        
        //-- add mapping to hashtable before processing
        //-- fields so we can do recursive processing
        _mappings.put( cls, classMap );
        
        fields = xmlClass.getFields();
        for ( int i = 0 ; i < fields.length ; ++i ) {
            
            FieldDescriptor fdesc = fields[ i ];
            
            String fieldName = fdesc.getFieldName();
            
            boolean isContainer = false;
            //-- check for collection wrapper
            if (introspected && fieldName.startsWith("##container")) {
                fdesc = fdesc.getClassDescriptor().getFields()[0];
                fieldName = fdesc.getFieldName();
                isContainer = true;
            }
            
            
            Class fieldType = fdesc.getFieldType();
            
            
            //-- check to make sure we can find the accessors...
            //-- if we used introspection we don't need to
            //-- enter this block...only when descriptors
            //-- were generated using the source code generator
            //-- or by hand.
            if ((!introspected) && fieldName.startsWith(UNDERSCORE)) {
                
                //-- check to see if we need to remove underscore
                if (!_mappingLoader.canFindAccessors(cls, fieldName, fieldType))
                    fieldName = fieldName.substring(1);
                
                //-- check to see if we need to remove "List" prefix
                //-- used by generated source code
                if (!_mappingLoader.canFindAccessors(cls, fieldName, fieldType)) 
                {
                    if (fieldName.endsWith("List")) {
                        int len = fieldName.length()-4;
                        String tmpName = fieldName.substring(0, len);
                        if (_mappingLoader.canFindAccessors(cls, tmpName, fieldType))
                            fieldName = tmpName;                            
                    }
                }
            }
            
            fieldMap = new FieldMapping();
            fieldMap.setName( fieldName );
            
            
            //-- unwrap arrays of objects
            boolean isArray = fieldType.isArray();
            while (fieldType.isArray())
                fieldType = fieldType.getComponentType();
                
            
            //-- To prevent outputing of optional fields...check
            //-- for value first before setting
            if (fdesc.isRequired())  fieldMap.setRequired( true );
            if (fdesc.isTransient()) fieldMap.setTransient( true );
            if ( fdesc.isMultivalued() ) {
                
                //-- special case for collections
                if (isContainer) {
                    //-- backwards than what you'd expect, but
                    //-- if the collection had a "container" wrapper
                    //-- then we specify container="false" in the
                    //-- mapping file.
                    fieldMap.setContainer(false);
                }
                
                //-- try to guess collection type
                if (isArray) {
                    fieldMap.setCollection(FieldMappingCollectionType.ARRAY);
                }
                else {
                    //-- if the fieldType is the collection, then set appropriate
                    //-- collection type 
                    String colName = CollectionHandlers.getCollectionName(fieldType);
                    if (colName != null) {
                        fieldMap.setCollection(FieldMappingCollectionType.valueOf(colName));
                        fieldType = Object.class;
                    }
                    //-- help maintain compatibility with generated
                    //-- descriptors
                    else if (_mappingLoader.returnsArray(cls, fieldName, fieldType)) {
                        fieldMap.setCollection( FieldMappingCollectionType.ARRAY );
                    }
                    else {
                        fieldMap.setCollection( FieldMappingCollectionType.ENUMERATE );
                    }
                }
            }
            
            //-- fieldType
            fieldMap.setType( fieldType.getName() );
            
                
            //-- handle XML Specific information
            fieldMap.setBindXml( new BindXml() );
            fieldMap.getBindXml().setName( ( (XMLFieldDescriptor) fdesc ).getXMLName() );
            fieldMap.getBindXml().setNode( BindXmlNodeType.valueOf( ((XMLFieldDescriptor) fields[ i ]).getNodeType().toString() ) );
            if (classMap.getClassChoice() == null) {
                classMap.setClassChoice(new ClassChoice());
            }
            classMap.getClassChoice().addFieldMapping( fieldMap );
            
            if (deep) {
                if ( _mappings.get(fieldType) != null) continue;
                if (Types.isSimpleType(fieldType)) continue;                
                //-- recursive add needed classes
                addClass( fieldType );
            }
        }
    } //-- addClass

    /**
     * Enables or disables the forcing of introspection when
     * a ClassDescriptor already exists. This is false by
     * default.
     *
     * @param force when true will cause the MappingTool to
     * always use introspection regardless of whether or not
     * a ClassDescriptor exists for a given Class.
    **/
    public void setForceIntrospection(boolean force) {
        _forceIntrospection = force;
        if (force) {
            if (_introspector == null)
                _introspector = new Introspector();
        }
    } //-- setForceInstrospection

    /**
     * Serializes the mapping to the given writer
     *
     * @param writer, the Writer to serialize the mapping to
    **/
    public void write( Writer writer )
        throws MappingException
    {
        Marshaller  marshal;
        MappingRoot mapping;
        Enumeration enumeration;

        try {
            mapping = new MappingRoot();
            mapping.setDescription( "Castor generated mapping file" );
            enumeration = _mappings.elements();
            while ( enumeration.hasMoreElements() )
                mapping.addClassMapping( (ClassMapping) enumeration.nextElement() );
            marshal = new Marshaller( writer );
            marshal.setNamespaceMapping(null, "http://castor.exolab.org/");
            marshal.setNamespaceMapping("cst", "http://castor.exolab.org/");
            marshal.marshal( mapping );
        } catch ( Exception except ) {
            throw new MappingException( except );
        }
    } //-- write

    //-- Extend mapping loader to give us access to the
    //-- findAccessor method.
    class InternalLoader extends AbstractMappingLoader {
        
        private static final String GET = "get";
        private static final String SET = "set";
        private static final String ADD = "add";
        
        InternalLoader() {
            super(null);
        }
        
        public BindingType getBindingType() { return null; }

        /**
         * Returns true if the get method returns an array.
         * This method is used for greater compatability with
         * generated descriptors.
         *
         * @return if get method returns an array.
        **/
        boolean returnsArray(Class claz, String fieldName, Class type) {
            
            try {
                Class array = null;
                if (type.isArray()) {
                    array = type;
                }
                else {
                    array = Array.newInstance(type, 0).getClass();
                }
                //-- getMethod
                String prefix = JavaNaming.toJavaClassName(fieldName);
                String method = GET + prefix;
                boolean isGet = true;
                if (findAccessor(claz, method, array, isGet) != null)
                    return true;
            }
            catch(Exception ex) {}
            return false;
        }
        
        boolean canFindAccessors(Class claz, String fieldName, Class type) {
            
            try {
                //-- getMethod
                String prefix = JavaNaming.toJavaClassName(fieldName);
                String method = GET + prefix;
                boolean isGet = true;
                if (findAccessor(claz, method, type, isGet) != null)
                    return true;
                    
                //-- setMethod and/or addMethod
                isGet = false;
                method = SET + prefix;
                if (findAccessor(claz, method, type, isGet) != null)
                    return true;
                method = ADD + prefix;
                if (findAccessor(claz, method, type, isGet) != null)
                    return true;                
            }
            catch(Exception ex) {}
            return false;
        }

    } 
    
} //-- MappingTool






