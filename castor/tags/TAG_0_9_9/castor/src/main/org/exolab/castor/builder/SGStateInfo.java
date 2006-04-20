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
 * This file was originally developed by Keith Visco during the
 * course of employment at Intalio Inc.
 * All portions of this file developed by Keith Visco after Jan 19 2005 are
 * Copyright (C) 2005 Keith Visco. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.builder;


import org.exolab.castor.builder.util.ClassInfoResolverImpl;
import org.exolab.castor.builder.util.ConsoleDialog;
import org.exolab.castor.builder.util.Dialog;
import org.exolab.castor.xml.schema.Annotated;
import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.mapping.xml.MappingRoot;
import org.exolab.javasource.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

/**
 * A class for maintaining state for the SourceGenerator
 * 
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
class SGStateInfo extends ClassInfoResolverImpl {
    
    private static final Enumeration EMPTY_ENUMERATION 
        = new Vector(0).elements();

    public static final int NORMAL_STATUS = 0;
    public static final int STOP_STATUS = 1;

    /**
     * The in memory mapping files for each
     * package
     */
    private Hashtable _mappings = null;
    
    /**
     * The in memory package listings for
     * each package
     */
    private Hashtable _packageListings = null;
    
    /**
     * The package used when creating new classes.
    **/
    protected String    packageName = null;


    private Vector      _processed   = null;

    private boolean     _promptForOverwrite = true;

    /**
     * The schema we are generating source code for
     */
    private Schema      _schema = null;
    
    private boolean     _suppressNonFatalWarnings = false;

    private boolean     _verbose     = false;


    private FactoryState _currentFactoryState = null;

    private Dialog _dialog = null;
    
    private SourceGenerator _sgen = null;
    
    private int _status = NORMAL_STATUS;
    
    private Map _sourcesByComponent = null;
    
    private Map _sourcesByName = null;
    
    
    
//    private XMLBindingComponent _bindingComponent = null;

    /**
     * Creates a new SGStateInfo
     * 
     * @param schema the Schema to generate source for
     * @param sgen the SourceGenerator instance
     */
    protected SGStateInfo(Schema schema, SourceGenerator sgen) {
        super();
        _schema             = schema;
        packageName         = "";
        _processed          = new Vector();
        _dialog             = new ConsoleDialog();
        _sourcesByComponent = new HashMap();
        _sourcesByName      = new HashMap();
        _sgen               = sgen;
    } //-- SGStateInfo
    
    /**
     * Binds the given Annotated structure with it's
     * generated source classes
     *
     * @param annotated the Annotated structure to add JClass bindings for
     * @param classes the JClass[] to bind 
     */
    public void bindSourceCode(Annotated annotated, JClass[] classes) {
        _sourcesByComponent.put(annotated, classes);
        for (int i = 0; i < classes.length; i++) {
            JClass jClass = classes[i];
            if (jClass != null) {
                _sourcesByName.put(jClass.getName(), jClass);
            }
        }
    } //-- bindSourceCode
       
    /**
     * Returns the processed JClass with the given name. If
     * no such JClass has been marked as processed, 
     * null is returned.
     *
     * @param className the JClass name to check against
     * @return the JClass with the given name
     */
    JClass getProcessed(String className) {
        for (int i = 0; i < _processed.size(); i++) {
            JClass jClass = (JClass) _processed.elementAt(i);
            if (jClass.getName().equals(className))
                return jClass;
        }
        return null;
    } //-- getProcessed

    /**
     * Returns the array of JClass for the given Annotated structure
     * or null if no bindings have been specified for the given
     * Structure.
     * 
     * @return the JClass array
     */
    public JClass[] getSourceCode(Annotated annotated) {
        return (JClass[])_sourcesByComponent.get(annotated);
    } //-- getSourceCode
    
    /**
     * Returns the JClass with the given name or null
     * if no bindings have been specified for a JClass
     * with the name.
     * 
     * @param className the name of the JClass
     * @return the JClass if found
     */
    public JClass getSourceCode(String className) {
        return (JClass)_sourcesByName.get(className);
    } //-- getSourceCode
    
    
    /**
     * Returns the Mapping file associated with 
     * the given filename
     * 
     * @return the Mapping file
     */
    public MappingRoot getMapping(String filename) {
        if ((_mappings != null) && (filename != null)) {
            return (MappingRoot) _mappings.get(filename);
        }
        return null;
    } //-- getMapping
    
    /**
     * Returns the CDRFile (Properties file) associated
     * with the given filename
     * 
     * @param filename
     * @return the Properties file
     */
    public Properties getCDRFile(String filename) {
        if ((_packageListings != null) && (filename != null)) {
            return (Properties) _packageListings.get(filename);
        }
        return null;
    }

    /**
     * Returns the set of CDR file names
     * 
     * @return the set of CDR file names
     */
    public Enumeration getCDRFilenames() {
        if (_packageListings == null) {
            return EMPTY_ENUMERATION;
        }
        return _packageListings.keys();
    } //-- getCDRFilenames
    
    /**
     * Returns the set of mapping filenames
     * 
     * @return the set of mapping filenames
     */
    public Enumeration getMappingFilenames() {
        if (_mappings == null) {
            return EMPTY_ENUMERATION;
        }
        return _mappings.keys();
    } //-- getMappingFilenames
    
    /**
     * Returns the current status
     *
     * @return the current status
     */
    public int getStatusCode() {
        return _status;
    } //-- getStatusCode
    
    /**
     * Marks the given JClass as having been processed.
     * @param jClass the JClass to mark as having been processed.
    **/
    void markAsProcessed(JClass jClass) {
        //String className = jClass.getName();
        if (!_processed.contains(jClass))
            _processed.addElement(jClass);
    } //-- markAsProcessed

    /**
     * Returns true if the given JClass has been marked as processed
     * @param jClass the JClass to check for being marked as processed
    **/
    boolean processed(JClass jClass) {
        return _processed.contains(jClass);
    } //-- processed
    
    /**
     * Returns true if a JClass with the given name has been marked as 
     * processed
     *
     * @param className the JClass name to check against
     */
    boolean processed(String className) {
        for (int i = 0; i < _processed.size(); i++) {
            JClass jClass = (JClass) _processed.elementAt(i);
            if (jClass.getName().equals(className))
                return true;
        }
        return false;
    } //-- processed
    

    boolean promptForOverwrite() {
        return _promptForOverwrite;
    } //-- promptForOverwrite

    void setPromptForOverwrite(boolean promptForOverwrite) {
        this._promptForOverwrite = promptForOverwrite;
    } //-- setPromptForOverwrite

    Schema getSchema() {
        return _schema;
    } //-- getSchema
    
    /**
     * Returns the SourceGenerator instance being used
     */
    SourceGenerator getSourceGenerator() {
        return _sgen;
    } //-- getSourceGenerator
    
    boolean getSuppressNonFatalWarnings() {
        return _suppressNonFatalWarnings;
    }

    void setSuppressNonFatalWarnings(boolean suppressNonFatalWarnings) {
        _suppressNonFatalWarnings = suppressNonFatalWarnings;
    }

    /**
     * Sets the CDR (ClassDescriptorResolver) file associated with 
     * the given filename
     * 
     * @param filename the filename associated with the CDR file
     * @param props the Properties file
     */
    public void setCDRFile(String filename, Properties props) {
        if (filename == null) {
            String err = "The argument 'filename' must not be null.";
            throw new IllegalArgumentException(err);
        }
    
        if (_packageListings == null) {
            _packageListings = new Hashtable();
        }
        
        if (props == null) {
            _packageListings.remove(filename);
        }
        else {
            _packageListings.put(filename, props);
        }
    } //-- setCDRFile
    
    /**
     * Sets the Mapping file associated with 
     * the given filename
     * 
     * @param filename the filename associated with the Mapping
     * @param mapping the MappingRoot
     */
    public void setMapping(String filename, MappingRoot mapping) {
        if (filename == null) {
            String err = "The argument 'filename' must not be null.";
            throw new IllegalArgumentException(err);
        }
    
        if (_mappings == null) {
            _mappings = new Hashtable();
        }
        
        if (mapping == null) {
            _mappings.remove(filename);
        }
        else {
            _mappings.put(filename, mapping);
        }
    } //-- setMapping
    

    /**
     * Returns the Dialog used for interacting with the user
     *
     * @return the Dialog, or null if none has been set.
     */
    Dialog getDialog() {
        return _dialog;
    } //-- getConsoleDialog

    /**
     * Sets the Dialog used for interacting with the user
     *
     * @param dialog the Dialog to use
     */
    void setDialog(Dialog dialog) {
        _dialog = dialog;
    } //-- setDialog

    /**
     * Sets the XMLBindingComponent that this SGStateInfo is working on.
     * 
     */
   /* void setXMLBindingComponent(XMLBindingComponent compo) {
       _bindingComponent = compo;
    }*/
    
    /**
     * Returns the XMLBindingComponent this SGStateInfo is working on.
     * 
     *@return the XMLBindingComponent this SGStateInfo is working on.
     */
   /* XMLBindingComponent getXMLBindingComponent() {
        return _bindingComponent;
    }*/
    
    /**
     * Sets the current status code to the given one
     *
     * @param status the new status code
     */
    void setStatusCode(int status) {
        _status = status;
    } //-- setStatusCode
    
    /**
     * Sets whether or not the source code generator prints
     * additional messages during generating source code
     * @param verbose a boolean, when true indicates to
     * print additional messages
    **/
    void setVerbose(boolean verbose) {
        this._verbose = verbose;
    } //-- setVerbose

    /**
     * Returns the value of the verbose flag. A true value
     * indicates that additional messages may be printed
     * during processing
     * @return the value of the verbose flag.
    **/
    boolean verbose() {
        return this._verbose;
    } //-- verbose

    /**
     * Returns the current FactoryState that
     * holds information about the classes being generated.
     * @return the current FactoryState
     *
     */
    FactoryState getCurrentFactoryState() {
        return _currentFactoryState;
    }

    /**
     * Sets the current FactoryState.
     * @param state the current FactoryState
     * @see #getCurrentFactoryState
     */
    void setCurrentFactoryState(FactoryState state) {
       _currentFactoryState = state;
    }
    /*
    protected JClass getJClass(String name) {

        if (name == null) return null;
        JClass jClass = (JClass)classTypes.get(name);

        if (jClass == null) {
            jClass = new JClass(name);
            classTypes.put(name, jClass);
        }
        return jClass;
    }
    */

} //-- SGStateInfo
