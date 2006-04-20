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


package org.exolab.adaptx.xpath;


/**
 * An implementation of VariableSet which is used to provide
 * variable bindings that can be used when evaluating
 * an XPath expression. Binds an unqualified variable name to an
 * XPath result.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @author <a href="kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$
 * @see XPathResult
 * @see XPathExpression
 */
public final class VariableSetImpl extends VariableSet {

    /**
     * References a parent variable binding.
     */
    private VariableSet _parent;


    /**
     * The first binding in a single-linked list of variable bindings.
     */
    private Binding  _binding;


    /**
     * Constructs a new empty set of variable bindings.
    **/
    public VariableSetImpl() {
        _parent = null;
    } //-- VaribaleSetImpl


    /**
     * Constructs a new empty variable binding with reference to
     * a parent variable binding. Will use the parent variables to
     * obtain variables not added directory to this object.
     *
     * @param parent The parent variable binding (may be null)
    **/
    public VariableSetImpl( VariableSet parent ) {
        _parent = parent;
    } //-- VariableSetImpl


    /**
     * Removes the current variable bindings from this VariableSet
    **/
    public void clear() {
        _binding = null;
    } //-- clear
    
    /**
     * Returns the value of a variable. Returns null if a variable
     * with this name was not found in this variable bindings, or any
     * parent variable binding.
     *
     * @param name The variable name
     * @return The variable's value as an XPath result, or null
    **/
    public XPathResult getVariable( String name ) {
        
        XPathResult value;
        Binding     binding;

        if ( name == null )
            throw new IllegalArgumentException( "Argument name is null" );
        binding = _binding;
        while ( binding != null ) {
            if ( binding.name.equals( name ) )
                return binding.value;
            binding = binding.next;
        }
        if ( _parent != null )
            return _parent.getVariable( name );
        return null;
        
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
        

        if ( name == null )
            throw new IllegalArgumentException( "Argument name is null" );
        if ( value == null )
            throw new IllegalArgumentException( "Argument value is null" );
            
        Binding binding = _binding;
        while ( binding != null ) {
            if ( binding.name.equals( name ) ) {
                binding.value = value;
                return;
            }
            binding = binding.next;
        }
        _binding = new Binding( _binding, name, value );
        
    } //-- setVariable


    /**
     * Internal Variable Binding
    **/
    static private final class Binding implements java.io.Serializable {

        final String name;

        XPathResult value;

        final Binding next;

        Binding( Binding next, String name, XPathResult value ) {
            this.next = next;
            this.name = name;
            this.value = value;
        }

    } //-- Binding

} //-- VariableSet

