/*
 * (C) Copyright Keith Visco 1999  All rights reserved.
 *
 * The contents of this file are released under an Open Source 
 * Definition (OSD) compliant license; you may not use this file 
 * execpt in compliance with the license. Please see license.txt, 
 * distributed with this file. You may also obtain a copy of the
 * license at http://www.kvisco.com/xslp/license.txt
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
 * Represents a node-set. A node-set is used as the result of an XPath
 * expression, and during the expression to keep track of nodes being
 * evaluated. As such, it is a mutable object. Care must be taken, as
 * a node-set is not thread-safe and must not be used by two XPath
 * expressions evaluating concurrently.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public final class NodeSet
    extends XPathResult
{

    
    /**
     * The default size of the nodes array.
     */
    private int DefaultSize = 8;

    
    /**
     * An array of nodes. Initially null, and created only when
     * the first node is added. The array length is equal to or
     * larger than the next available location (<tt>_count</tt>).
     */
    private XPathNode[]  _nodes;
    

    /**
     * The next available location in the nodes array.
    */
    private int         _count;


    /**
     * Constructs a new empty node-set.
    **/
    public NodeSet() {
        //-- do nothing
    }

    
    /**
     * Constructs a new node-set with the specified node.
     *
     * @param node The node to include in the node-set
    **/
    public NodeSet( XPathNode node ) {
        if ( node == null )
            throw new IllegalArgumentException( "Argument node is null" );
        _nodes = new XPathNode[ 1 ];
        _nodes[ 0 ] = node;
        _count = 1;
    } //-- NodeSet


    /**
     * Constructs a new empty node-set with the specified size.
     * This constructor is used if the node-set is known to hold
     * only the specified number of nodes.
     *
     * @param size The expected node-set size
    **/
    public NodeSet( int size ) {
        if ( size > 0 )
            _nodes = new XPathNode[ size ];
    } //-- NodeSet


    //--------------------------------------------
    // Methods defined in XPath result
    //-------------------------------------------


    /**
     * Returns the type of this result.
     *
     * @return {@link XPathResult#NODE_SET}
    **/
    public int getResultType() {
        return XPathResult.NODE_SET;
    } //-- getResultType

    
    /**
     * Returns the result as a boolean value. Returns true if the
     * node-set is not empty.
     *
     * @return The result as a boolean value
    **/
    public boolean booleanValue() {
        return _count > 0;
    } //-- booleanValue

    
    /**
     * Returns the result as a number value. Returns the number value of
     * the node's string value, or {@link java.lang.Double#NaN} if the
     * node-set is empty.
     *
     * @return The result as a number value
    **/
    public double numberValue() {
        if ( _count > 0 ) {
            try {
                Double dbl = Double.valueOf( stringValue() );
                return dbl.doubleValue();
            } 
            catch ( NumberFormatException except ) { }
        }
        return Double.NaN;
    } //-- numberValue

    
    /**
     * Returns the result as a string value. Returns the string value of the
     * node that is first in document order, or the empty string if the node-set
     * is empty.
     * <p>
     * <b>Note:</b> currently returns the string value of the first node in
     * the node-set.
     *
     * @return The result as a string value
    **/
    public String stringValue() {
        return _count == 0 ? "" : _nodes[ 0 ].getStringValue();
    } //-- stringValue


    /**
     * Returns the result as a Java object. Returns an object of
     * type {@link NodeSet} representing this node-set.
     *
     * @return The result as a Java object
    **/
    public Object javaObject() {
        return this;
    } //-- javaObject


    /**
     * Returns true if the given result is a node-set result
     * and has the same set of nodes.
     *
     * @param result An XPath result
     * @return True if a node-set result and has same set of nodes
    **/
    public boolean equals( XPathResult result ) {
        NodeSet other;

        if ( result == this )
            return true;
        if ( result != null && result instanceof NodeSet ) {
            other = (NodeSet) result;
            if ( _count != other._count )
                return false;
            for ( int i = 0 ; i < _count ; ++i )
                if ( ! other.contains( _nodes[ i ] ) )
                    return false;
        }
        return false;
    } //-- equals


    //-----------------------------------------------
    // Methods introduced for supporting a node-set
    //-----------------------------------------------


    /**
     * Returns the number of nodes in the node-set.
     *
     * @return The number of nodes in the node-set
    **/
    public int size() {
        return _count;
    } //-- size


    /**
     * Adds the specified node to this node-set without duplication.
     *
     * @param node The node to add to this node-set
     * @return True if the node was added, false if the node was already
     * in this node-set
    **/
    public boolean add( XPathNode node ) {
        if ( node == null )
            throw new IllegalArgumentException( "Argument node is null" );
        if ( _nodes == null )
            increaseSize();
        else if ( _count == 0 ) {
            if ( _count == _nodes.length )
                increaseSize();
        } else {
            for ( int i = 0 ; i < _count ; ++i )
                if ( _nodes[ i ].equals( node ) )
                    return false;
            if ( _count == _nodes.length )
                increaseSize();
        }
        _nodes[ _count++ ] = node;
        return true;
    } //-- add

    /**
     * Adds the specified node to this node-set. This method allows
     * control over whether or not to perform duplicate checking.
     *
     * @param node The node to add to this node-set
     * @param ignoreDuplicates If true, do not perform duplicate checking
     * @return True if the node was added, false if the node was already
     * in this node-set
    **/
    public boolean add( XPathNode node, boolean ignoreDuplicates ) {
        if ( _nodes == null )
            increaseSize();
        else if ( _count == 0 ) {
            if ( _count == _nodes.length )
                increaseSize();
        } else {
            if ( ! ignoreDuplicates ) {
                for ( int i = 0 ; i < _count ; ++i )
                    if ( _nodes[ i ].equals( node ) )
                        return false;
            }
            if ( _count == _nodes.length )
                increaseSize();
        }
        _nodes[ _count++ ] = node;
        return true;
    } //-- add


    /**
     * Adds all nodes from the specified node-set to this node-set
     * without duplication.
     *
     * @param nodeSet The node-set from which to add nodes
    **/
    public void add( NodeSet nodeSet ) {
        int       size;
        int       orgSize;
        XPathNode node;

        if ( nodeSet == null )
            throw new IllegalArgumentException( "Argument nodeSet is null" );
        size = nodeSet.size();
        if ( _nodes == null || _count == 0 ) {
            ensureSize( size );
            for ( int i = 0 ; i < size ; i++)
                _nodes[ _count++ ] = nodeSet.item( i );;
        } else {
            ensureSize( size );
            orgSize = _count;
            for ( int i = 0 ; i < size ; i++) {
                node = nodeSet.item( i );
                int j;
                for ( j = 0 ; j < orgSize ; ++j )
                    if ( _nodes[ j ].equals( node ) )
                        break;
                if ( j == orgSize )
                    _nodes[ _count++ ] = node;
            }
        }
    } //-- add



    /**
     * Adds all nodes from the specified node-set to this node-set.
     * This method allows control over whether or not to perform
     * duplicate checking.
     *
     * @param nodeSet The node-set from which to add nodes
     * @param ignoreDuplicates If true, do not perform duplicate checking     *
    **/
    public void add( NodeSet nodeSet, boolean ignoreDuplicates ) {
        int       size;
        int       orgSize;
        XPathNode node;

        if ( nodeSet == null )
            throw new IllegalArgumentException( "Argument nodeSet is null" );
        size = nodeSet.size();
        if ( _nodes == null || _count == 0 ) {
            ensureSize( size );
            for ( int i = 0 ; i < size ; i++)
                _nodes[ _count++ ] = nodeSet.item( i );;
        } else if ( ignoreDuplicates ) {
            ensureSize( size );
            for ( int i = 0 ; i < size ; i++)
                _nodes[ _count++ ] = nodeSet.item( i );
        } else {
            ensureSize( size );
            orgSize = _count;
            for ( int i = 0 ; i < size ; i++) {
                node = nodeSet.item( i );
                int j;
                for ( j = 0 ; j < orgSize ; ++j )
                    if ( _nodes[ j ].equals( node ) )
                        break;
                if ( j == orgSize )
                    _nodes[ _count++ ] = node;
            }
        }
    } //-- add


    /**
     * Returns true if the specified node is contained in this node-set.
     *
     * @param node The specified node to search
     * @return True if the specified node is contained in this node-set
    **/
    public boolean contains( XPathNode node ) {
        for ( int i = 0 ; i < _count ; ++i )
            if ( _nodes[ i ] == node )
                return true;
        return false;
    } //-- contains


    /**
     * Returns the node at the specified index. Throws {@link
     * IndexOutOfBoundsException} is the index is out of range.
     * The index is as returned by {@link #indexOf}.
     *
     * @param index The position of the node to return
    **/
    public XPathNode item( int index ) {
        if ( _nodes == null )
            throw new IndexOutOfBoundsException();
        if ( index < 0 || index >= _count )
            throw new IndexOutOfBoundsException();
        return _nodes[ index ];
    } //-- item


    /**
     * Returns the position of the specified node in this node-set.
     * Returns -1 i the node is not included in this node-set.
     * <p>
     * The node's index is based on the order in which it was
     * added to the node-set, minus any nodes of lower index position
     * that were removed from the node-set. The index ranges between
     * zero and <tt>size() - 1</tt> (inclusive).
     *
     * @param node the Node to get the index for
    **/
    public int indexOf( XPathNode node ) {
        for ( int i = 0 ; i < _count ; i++ )
            if ( _nodes[ i ] == node )
                return i;
        return -1;
    } //-- indexOf


    /**
     * Removes the specified node from the node-set. Returns true
     * if the node was found and removed, false if the node was
     * not included in this node-set.
     *
     * @param node The node to remove from this node-set
     * @return True if the node was removed
    **/
    public boolean remove( XPathNode node ) {
        for ( int i = 0 ; i < _count ; ++ i )
            if ( _nodes[ i ] == node ) {
                _nodes[ i ] = null;
                for ( ++i ; i < _count ; ++i )
                    _nodes[ i - 1 ] = _nodes[ i ];
                --_count;
                return true;
            }
        return false;
    } //-- remove
    
    
    /**
     * Removes the specified node from the node-set. The node is
     * identified by position. Throws {@link IndexOutOfBoundsException}
     * is the index is out of range. The index is as returned by
     * {@link #indexOf}.
     *
     * @param index The position of the node to remove
     * @return True if the node was removed
    **/
    public void remove( int index ) {
        if ( _nodes == null )
            throw new IndexOutOfBoundsException();
        if ( index < 0 || index >= _count )
            throw new IndexOutOfBoundsException();
        for ( ++index ; index < _count ; ++index )
            _nodes[ index - 1 ] = _nodes[ index ];
        --_count;
    } //-- remove


    /**
     * Removes all elements from this node-set.
    **/
    public void clear() {
        if ( _nodes != null )
            for ( int i = 0; i < _count; i++ )
                _nodes[ i ] = null;
        _count = 0;
    } //-- clear


    /**
     * Returns an array containing all the nodes in this node-set.
     * Returns an empty array if the node-set is empty.
     *
     * @return An array containing all the nodes in this node-set
     */
    public XPathNode[] toArray() {
        XPathNode[] array;

        if ( _nodes == null )
            return new XPathNode[ 0 ];
        array = new XPathNode[ _count ];
        for( int i = 0 ; i < _count ; ++i )
            array[ i ] = _nodes[ i ];
        return array;
    } //-- toArray

    
    /**
     * Increases the node-set's capacity to allow for more nodes.
     * Must be called only if the node-set is empty (<tt>_nodes == null<tt/>),
     * or out of space (<tt>_nodes.length == _count</tt>).
    **/
    private void increaseSize() {
        XPathNode[] newNodes;

        if ( _nodes == null )
            _nodes = new XPathNode[ DefaultSize ];
        else {
            newNodes = new XPathNode[ _count + DefaultSize ];
            for ( int i = 0 ; i < _count ; ++i )
                newNodes[ i ] = _nodes[ i ];
            _nodes = newNodes;
        }
    } //-- increaseSize

    
    /**
     * Ensures that the node-set has sufficient capacity to hold an
     * additional number of nodes. The node-set will be increased
     * only as sufficient to accomodate for the additional nodes.
     *
     * @param count The number of additional nodes to be added
    **/
    private void ensureSize( int count ) {
        XPathNode[] newNodes;

        if ( _nodes == null )
            _nodes = new XPathNode[ count ];
        else if ( count > _nodes.length - _count ) {
            newNodes = new XPathNode[ _count + count ];
            for ( int i = 0 ; i < _count ; ++i )
                newNodes[ i ] = _nodes[ i ];
            _nodes = newNodes;
        }
    } //-- ensureSize


    /**
     * Returns the string representation of this NodeSet. This
     * will be the concatenation of the string values of
     * all the nodes contained within this NodeSet
     *
     * @return the string representation of this NodeSet.
    **/
    public String toString()
    {
        StringBuffer buffer;

        if ( _count == 0 )
            return "";
        else if ( _count == 1 )
            return _nodes[ 0 ].getStringValue();
        else {
            buffer = new StringBuffer();
            for ( int i = 0 ; i < _count ; ++i )
                buffer.append( _nodes[ i ].getStringValue() );
            return buffer.toString();
        }
    } //-- toString

} //-- NodeSet
