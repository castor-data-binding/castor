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

package org.exolab.castor.builder;

import org.exolab.castor.builder.util.ClassInfoResolverImpl;

import org.exolab.javasource.*;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;

/**
 * A class for maintaining state for the SourceGenerator
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
class SGStateInfo extends ClassInfoResolverImpl {

    /**
     * The package used when creating new classes.
    **/
    protected String    packageName = null;

    private Hashtable   _classTypes  = null;

    private Vector      _processed   = null;

    private boolean     _promptForOverwrite = true;

    private boolean     _verbose     = false;

    private FactoryState _currentFactoryState = null;
    /**
     * The helper class for naming unnamed groups
    **/
    private GroupNaming _groupNaming = null;

    /**
     * Creates a new SGStateInfo
    **/
    protected SGStateInfo() {
        super();
        packageName    = "";
        _classTypes    = new Hashtable();
        _processed     = new Vector();
        _groupNaming   = new GroupNaming();
    } //-- SGStateInfo


    /**
     * Marks the given JClass as having been processed.
     * @param jClass the JClass to mark as having been processed.
    **/
    void markAsProcessed(JClass jClass) {
        String className = jClass.getName();
        if (!_processed.contains(className))
            _processed.addElement(className);
    } //-- markAsProcessed

    /**
     * Returns true if the given JClass has been marked as processed
     * @param jClass the JClass to check for being marked as processed
    **/
    boolean processed(JClass jClass) {
        return _processed.contains(jClass.getName());
    } //-- processed

    boolean promptForOverwrite() {
        return _promptForOverwrite;
    } //-- promptForOverwrite

    void setPromptForOverwrite(boolean promptForOverwrite) {
        this._promptForOverwrite = promptForOverwrite;
    } //-- setPromptForOverwrite

    /**
     * Returns the helper class used for naming groups
     *
     * @return the GroupNaming instance
    **/
    GroupNaming getGroupNaming() {
        return _groupNaming;
    } //-- getGroupNaming

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
