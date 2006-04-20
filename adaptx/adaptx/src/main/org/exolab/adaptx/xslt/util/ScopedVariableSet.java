/*
 * (C) Copyright Keith Visco 1999  All rights reserved.
 *
 * The contents of this file are released under an Open Source 
 * Definition (OSD) compliant license; you may not use this file 
 * execpt in compliance with the license. Please see license.txt, 
 * distributed with this file. You may also obtain a copy of the
 * license at http://www.clc-marketing.com/xslp/license.txt
 *
 * The program is provided "as is" without any warranty express or
 * implied, including the warranty of non-infringement and the implied
 * warranties of merchantibility and fitness for a particular purpose.
 * The Copyright owner will not be liable for any damages suffered by
 * you as a result of using the Program. In no event will the Copyright
 * owner be liable for any special, indirect or consequential damages or
 * lost profits even if the Copyright owner has been advised of the
 * possibility of their occurrence.
 *
 * $Id$
 */
package org.exolab.adaptx.xslt.util;

import org.exolab.adaptx.xpath.VariableSet;
import org.exolab.adaptx.xpath.VariableSetImpl;
import org.exolab.adaptx.xpath.XPathResult;

/**
 * A simple stack for VariableSets
 *
 * @author <a href="kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public final class ScopedVariableSet {
    
    /**
     * The default size. 
    **/
    public static final int DEFAULT_SIZE = 7;
    
    
    private int _top = 0;
    
    private VariableSetImpl[] _variableSets = null;
    
    /**
     * Creates a new ScopedVariableSet
    **/
    public ScopedVariableSet() {
        this(DEFAULT_SIZE);
    } //-- ScopedVariableSet

    /**
     * Creates a new ScopedVariableSet using the givin size
     *
     * @param size the number of VariableSet objects to pre-initialize.
     * This must be a non-negative integer.
    **/
    public ScopedVariableSet(int size) {
        
        if (size < 0) 
            throw new IllegalArgumentException("size must be non negative");
            
        if (size == 0) {
            _top = -1;
            return;
        }
        
        _variableSets = new VariableSetImpl[size];
        
        VariableSet prevSet = null;
        for (int i = 0; i < size; i++) {
            _variableSets[i] = new VariableSetImpl(prevSet);
            prevSet = _variableSets[i];
        }
        
    } //-- ScopedVariableSet

    /** 
     * Returns the VariableSet currently in scope.
     *
     * @return the VariableSet currently in scope.
    **/
    public VariableSet current() {
        if (_variableSets == null) return null;
        return _variableSets[_top];
    } //-- current
    
    /**
     * Returns the value of a variable. Returns null if a variable
     * with this name was not found in this variable bindings, or any
     * parent variable binding.
     *
     * @param name The variable name
     * @return The variable's value as an XPath result, or null
    **/
    public XPathResult getVariable( String name ) {
        
        if (_variableSets == null) return null;
        
        return _variableSets[_top].getVariable( name );
        
    } //-- getVariable


    /**
     * Binds the XPath result to the variable name. The result will
     * be returned from subsequent call to {@link #getVariable} using
     * the same variable name.
     *
     * @param name The variable name
     * @param value The variable's value
    **/
    public void setVariable( String name, XPathResult value ) {        
        if (_variableSets == null) add();        
        _variableSets[_top].setVariable(name, value);
        
    } //-- setVariable

    /** 
     * Removes the current VariableSet scope.
    **/
    public void remove() {
        
        //-- user friendly...we should really throw
        //-- an exception...but be nice for now.
        if (_top <= 0) return;
        
        _variableSets[_top].clear();
        
        --_top;
        
    } //-- remove
    
    /**
     * adds a new VariableSet, which is then set as the current scope.
    **/
    public void add() {
        
        if (_variableSets == null) {
            _variableSets = new VariableSetImpl[1];
            _variableSets[0] = new VariableSetImpl();
            _top = 0;
        }
        else {
            ++_top;
            if (_top == _variableSets.length) {
                VariableSetImpl[] old = _variableSets;
                _variableSets = new VariableSetImpl[_variableSets.length+1];
                for (int i = 0; i < old.length; i++)
                    _variableSets[i] = old[i];                    
                _variableSets[_top] = new VariableSetImpl(_variableSets[_top-1]);                
            }
        }
    } //-- add
    
} //-- ScopedVariableSet